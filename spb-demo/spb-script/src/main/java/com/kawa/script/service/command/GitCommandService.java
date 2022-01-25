package com.kawa.script.service.command;

import com.kawa.script.plugin.CommandPlugin;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;


public class GitCommandService implements CommandPlugin {

    private static final Logger log = LoggerFactory.getLogger(GitCommandService.class);

    @Override
    public void run(String param) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = null;
        try {
            repository = builder.setGitDir(new File("/home/un/code/jvm-tools/.git"))
                    .readEnvironment()
                    .findGitDir()
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Git git = new Git(repository);
        ListBranchCommand listBranchCommand = git.branchList();
        try {
            listBranchCommand.call().forEach(System.out::println);
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean supports(String s) {
        return s.equals("git");
    }

}
