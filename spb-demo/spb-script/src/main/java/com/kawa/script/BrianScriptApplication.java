package com.kawa.script;

import com.kawa.script.service.command.GitCommandService;
import com.kawa.script.service.command.JpsCommandService;
import com.kawa.script.service.command.MavenCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


public class BrianScriptApplication {

    private static final Logger log = LoggerFactory.getLogger(BrianScriptApplication.class);
    static JpsCommandService jpsCommandService;
    static MavenCommandService mavenCommandService;
    static GitCommandService gitCommandService;

    public static void main(String[] args) {
        if (args == null || args.length <= 0) {
            log.info(">>>>>>>>> no args, exit!!!");
            return;
        }
        String value = null;
        String command = args[0];
        if (args.length > 1) {
            value = args[1];
        }
        loadPlugin();

        if (StringUtils.uncapitalize(jpsCommandService.getClass().getSimpleName()).startsWith(command)) {
            log.info(">>>>>>>>>> jps command <<<<<<<<<<");
            return;
        }

        if (StringUtils.uncapitalize(mavenCommandService.getClass().getSimpleName()).startsWith(command)) {
            log.info(">>>>>>>>>> maven command <<<<<<<<<<");
            return;
        }

        if (StringUtils.uncapitalize(gitCommandService.getClass().getSimpleName()).startsWith(command)) {
            log.info(">>>>>>>>>> git command <<<<<<<<<<");
            return;
        }

//        String commandClassName = "com.kawa.script.service.command." + StringUtils.capitalize(command) + "CommandService";
//        ClassLoader classLoader = Thread.currentThread().getClass().getClassLoader();
//        if (classLoader == null){
//            classLoader = new BrianScriptApplication().getClass().getClassLoader();
//        }
//            try {
//                Class<?> commandPlugin = classLoader.loadClass(commandClassName);
//                Method method = commandPlugin.getMethod("run", String.class);
//                method.invoke(CommandPlugin.class, value);
//            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//                e.printStackTrace();
//            }
    }

    private static void loadPlugin() {
        jpsCommandService = new JpsCommandService();
        mavenCommandService = new MavenCommandService();
        gitCommandService = new GitCommandService();

    }

}
