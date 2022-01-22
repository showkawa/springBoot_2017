package com.kawa.script;

import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;


public class SimplePluginRegistry<T extends Plugin<S>, S> implements PluginRegistry<T, S>, Iterable<T> {

    private List<T> plugins;
    private boolean initialized;

    private SimplePluginRegistry(List<? extends T> plugins) {
        Assert.notNull(plugins, "Plugins must not be null!");

        this.plugins = plugins == null ? new ArrayList<>() : (List<T>) plugins;
        this.initialized = false;
    }

    public List<T> getPlugins() {

        if (!initialized) {
            this.plugins = initialize(this.plugins);
            this.initialized = true;
        }

        return plugins;
    }

    protected synchronized List<T> initialize(List<T> plugins) {
        Assert.notNull(plugins, "Plugins must not be null!");
        return plugins.stream()
                .filter(it -> it != null)
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<T> iterator() {
        return getPlugins().iterator();
    }


    public static <S, T extends Plugin<S>> SimplePluginRegistry<T, S> empty() {
        return of(Collections.emptyList());
    }

    @SafeVarargs
    public static <S, T extends Plugin<S>> SimplePluginRegistry<T, S> of(T... plugins) {
        return of(Arrays.asList(plugins));
    }


    public static <S, T extends Plugin<S>> SimplePluginRegistry<T, S> of(List<? extends T> plugins) {
        return new SimplePluginRegistry<>(plugins);
    }

    @Override
    public Optional<T> getPluginFor(S delimiter) {
        Assert.notNull(delimiter, "Delimiter must not be null!");
        return getPlugins().stream()
                .filter(it -> it.supports(delimiter))
                .findFirst();
    }

    @Override
    public List<T> getPluginsFor(S delimiter) {
        Assert.notNull(delimiter, "Delimiter must not be null!");
        return getPlugins().stream()
                .filter(it -> it.supports(delimiter))
                .collect(Collectors.toList());
    }

    @Override
    public int countPlugins() {
        return getPlugins().size();
    }

    @Override
    public boolean contains(T plugin) {
        return getPlugins().contains(plugin);
    }

    @Override
    public boolean hasPluginFor(S delimiter) {
        return getPluginFor(delimiter).isPresent();
    }
}
