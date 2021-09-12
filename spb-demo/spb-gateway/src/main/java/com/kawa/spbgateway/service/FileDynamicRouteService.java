package com.kawa.spbgateway.service;

import com.kawa.spbgateway.domain.BrianGatewayProperties;
import com.kawa.spbgateway.property.RefreshRoutePropertySource;
import com.kawa.spbgateway.transformer.BrianRouteDefinitionTransformer;
import com.kawa.spbgateway.util.ChecksumUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.kawa.spbgateway.content.Contents.*;

@Service
@Slf4j
public class FileDynamicRouteService implements RouteDefinitionRepository {

    private Environment environment;

    private String folder;

    private List<String> resourceFileExt;

    private ConcurrentHashMap<String, String> fileChecksumMap = new ConcurrentHashMap<>(32);

    private ConcurrentHashMap<String, RouteDefinition> routes = new ConcurrentHashMap<>(32);

    private BrianRouteDefinitionTransformer transformer = new BrianRouteDefinitionTransformer();

    public FileDynamicRouteService(Environment environment) {
        this.environment = environment;
    }

    public boolean refreshRoutes() throws IOException {
        getAndInitProperties();
        List<Resource> resources = getCustomizedConfigs();
        if (isRefresh(resources)) {
            updateFileChecksumMap(resources);
            updateRefreshRoutePropertySource(resources);
            refreshRouteCache(readRouteConfig(resources));
            return true;
        }
        log.info(">>>>>>>>>> no need refresh route <<<<<<<<<<");
        return false;
    }

    /**
     * @param targets
     */
    private void refreshRouteCache(List<RouteDefinition> targets) {
        // when first load the RouteDefinition
        if (CollectionUtils.isEmpty(routes)) {
            targets.forEach(rd -> {
                // add routeDefinition
                save(Mono.just(rd)).subscribe();
                log.info(">>>>>>>>>> init add routeDefinition:{}", rd);
            });
            return;
        }

        List<RouteDefinition> definitions = new ArrayList<>();
        Collections.addAll(definitions, new RouteDefinition[routes.size()]);
        Collections.copy(definitions, routes.values().stream().collect(Collectors.toList()));

        targets.forEach(rd -> {
            if (Objects.isNull(routes.get(rd.getId()))) {
                // add new RouteDefinition
                save(Mono.just(rd)).subscribe();
                log.info(">>>>>>>>>> add routeDefinition:{}", rd);
            }
            // not null don't update
            if (Objects.nonNull(routes.get(rd.getId())) && rd.equals(routes.get(rd.getId()))) {
                definitions.remove(rd);
            }
        });

        // remove RouteDefinition
        if (Objects.nonNull(definitions)) {
            definitions.forEach(rd -> {
                delete(Mono.just(rd.getId())).subscribe();
                log.info(">>>>>>>>>> delete routeDefinition:{}", rd);
            });
        }
    }

    private List<RouteDefinition> readRouteConfig(List<Resource> resources) {
        Binder binder = Binder.get(environment);
        List<RouteDefinition> configs = new ArrayList<>();
        resources.stream().map(res -> res.getFilename()).forEach(fn -> {
            if (!fn.isEmpty()) {
                log.info(">>>>>>>>>> BrianGatewayProperties filename:{}", fn);
                BrianGatewayProperties brianGatewayProperties =
                        binder.bindOrCreate(fn, BrianGatewayProperties.class);
                log.info(">>>>>>>>>> {}", brianGatewayProperties);
                brianGatewayProperties.getRoutes().forEach(route -> {
                    configs.add(transformer.transform(route, route.getUri() == null ? null : route.getUri().toString()));
                });
            }
        });
        return configs;
    }


    private void updateRefreshRoutePropertySource(List<Resource> resources) {
        if (environment instanceof ConfigurableEnvironment) {
            MutablePropertySources propertySources =
                    ((ConfigurableEnvironment) this.environment).getPropertySources();

            List<PropertySourceLoader> propertySourceLoaders =
                    SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, getClass().getClassLoader());
            if (null != folder) {
                resources.forEach(res -> {
                    addCustomizedResource(propertySources, res, propertySourceLoaders);
                });
            }
        }
    }

    /**
     * @param propertySources
     * @param resource
     * @param propertySourceLoaders
     * @return
     */
    private void addCustomizedResource(MutablePropertySources propertySources, Resource resource,
                                       List<PropertySourceLoader> propertySourceLoaders) {
        propertySourceLoaders.forEach(psl -> {
            List<String> fileExts = Arrays.asList(psl.getFileExtensions());
            String filename = resource.getFilename();
            if (fileExts.contains(StringUtils.getFilenameExtension(filename))) {
                log.info(">>>>>>>>>> load file resource: {}", filename);
                try {
                    List<PropertySource<?>> propertySourceList = psl.load(filename, resource);
                    propertySourceList.forEach(ps -> {
                        String psName = ps.getName();
                        PropertySource refreshRoutePropertySource = new RefreshRoutePropertySource(psName, ps);
                        propertySources.addLast(refreshRoutePropertySource);
                        log.info(">>>>>>>>>> MutablePropertySources add propertySource: {}", psName);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateFileChecksumMap(List<Resource> resources) throws IOException {
        fileChecksumMap.clear();
        for (Resource resource : resources) {
            String fileName = resource.getFile().getName();
            // todo, or can use a easy way that use lastModified replace checksum -> resource.getFile().lastModified();
            String checksum = ChecksumUtil.checkSumByMD5(resource.getFile());
            log.info(">>>>>>>>>> fileName:{},checksum:{}", fileName, checksum);
            fileChecksumMap.put(fileName, checksum);
        }
    }


    private void getAndInitProperties() {
        if (!StringUtils.hasText(folder)) {
            folder = environment.getProperty(SEARCH_FOLDER_KEY) == null ?
                    environment.getProperty(DEFAULT_FOLDER_KEY) : environment.getProperty(SEARCH_FOLDER_KEY);
            resourceFileExt = Arrays.asList(environment.getProperty(RESOURCE_FILE_EXTENSION_KEY, String[].class,
                    DEFAULT_RESOURCE_FILE_EXTENSIONS));
        }
    }

    private List<Resource> getCustomizedConfigs() {
        List<Resource> resources = new ArrayList<>();
        List<String> exclude = Arrays.asList(EXCLUDES);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(folder))) {
            stream.forEach(path -> {
                if (!path.toFile().isDirectory() &&
                        resourceFileExt.contains(StringUtils.getFilenameExtension(path.toFile().getName()))
                        && !exclude.contains(path.toFile().getName())
                ) {
                    log.debug(">>>>>>>>>> load file source: {}", path);
                    resources.add(new FileSystemResource(path));
                }
            });
        } catch (IOException e) {
            throw new IllegalStateException(String.format("open %s field, %s", folder, e));
        }
        return resources;
    }

    private boolean isRefresh(List<Resource> resources) {

        if (resources.size() != fileChecksumMap.size()) {
            return true;
        }

        if (!Objects.equals(Arrays.asList(fileChecksumMap.keySet().stream().sorted().toArray()),
                Arrays.asList(resources.stream().map(res -> res.getFilename()).sorted().toArray()))) {
            return true;
        }
        for (Resource resource : resources) {
            try {
                if (!fileChecksumMap.get(resource.getFilename()).equals(ChecksumUtil.checkSumByMD5(resource.getFile()))) {
                    return true;
                }
            } catch (IOException e) {
                log.info(">>>>>>>>>> isRefresh checksum error:{}", e.getMessage());
            }
        }
        return false;
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        log.info(")))))))))))))))))))))))))))))) FileDynamicRouteService getRouteDefinitions～～～");
        return Flux.fromIterable(routes.values());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(r -> {
            routes.put(r.getId(), r);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            log.debug(">>>>>>>>>> remove the RouteDefinition id is: {}", id);
            if (routes.keySet().contains(id)) {
                routes.remove(id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(new NotFoundException(String.format("RouteDefinition not found -> %s", routeId))));
        });
    }
}
