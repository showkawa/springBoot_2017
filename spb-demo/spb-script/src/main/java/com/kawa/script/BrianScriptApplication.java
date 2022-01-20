package com.kawa.script;

import org.apache.maven.shared.invoker.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;


public class BrianScriptApplication {

    private static final Logger log = LoggerFactory.getLogger(BrianScriptApplication.class);

    public static void main(String[] args) throws MavenInvocationException, IOException, GitAPIException {
        log.info("====================BrianScriptApplication Start====================");
        Arrays.stream(args).forEach(param -> log.info(">>>>>>>>>> run params: {}", param));
        ClassLoader classLoader = BrianScriptApplication.class.getClassLoader();
        String classPath = classLoader.getResource("").getPath();
        Path path = Paths.get(classPath.replace("/target/classes/", "/pom.xml"));
        InvocationRequest invocationRequest = new DefaultInvocationRequest();
        invocationRequest.setPomFile(path.toFile());
        invocationRequest.setGoals(Collections.singletonList("test"));
        InvocationResult result = new DefaultInvoker()
                .setMavenHome(Paths.get("/usr/share/maven").toFile())
                .execute(invocationRequest);
        int exitCode = result.getExitCode();
        if (exitCode != 0) {
            log.info(">>>>>>>>>>> maven run command hit error <<<<<<<<<<");
        }

        log.info(result.toString());

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setGitDir(new File("/home/un/code/jvm-tools/.git"))
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build();

        Git git = new Git(repository);
        ListBranchCommand listBranchCommand = git.branchList();
        listBranchCommand.call().forEach(System.out::println);
    }

}
