package com.kawa.script.service.command;

import com.kawa.script.BrianScriptApplication;
import com.kawa.script.plugin.CommandPlugin;
import com.kawa.script.plugin.ToolPlugin;
import org.apache.maven.shared.invoker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class MavenCommandService implements CommandPlugin {

    private static final Logger log = LoggerFactory.getLogger(MavenCommandService.class);

    @Override
    public void run(String envPath) {
        ClassLoader classLoader = BrianScriptApplication.class.getClassLoader();
        String classPath = classLoader.getResource("").getPath();
        Path path = Paths.get(classPath.replace("/target/classes/", "/pom.xml"));
        InvocationRequest invocationRequest = new DefaultInvocationRequest();
        invocationRequest.setPomFile(path.toFile());
        invocationRequest.setGoals(Collections.singletonList("test"));
        InvocationResult result = null;
        try {
            result = new DefaultInvoker()
                    .setMavenHome(Paths.get("/usr/share/maven").toFile())
                    .execute(invocationRequest);
        } catch (MavenInvocationException e) {
            e.printStackTrace();
        }
        int exitCode = result.getExitCode();
        if (exitCode != 0) {
            log.info(">>>>>>>>>>> maven run command hit error <<<<<<<<<<");
        }

        log.info(result.toString());
    }

    @Override
    public boolean supports(String s) {
        return s.equals("maven");
    }
}
