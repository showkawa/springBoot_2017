package com.kawa.script;

import com.kawa.script.plugin.CommandPlugin;
import com.kawa.script.service.command.GitCommandService;
import com.kawa.script.service.command.JpsCommandService;
import com.kawa.script.service.command.MavenCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;


public class BrianScriptApplication {

    private static final Logger log = LoggerFactory.getLogger(BrianScriptApplication.class);

    private static SimplePluginRegistry<CommandPlugin, String> simplePluginRegistry;

    static {
        simplePluginRegistry = new SimplePluginRegistry<>(Arrays.asList(
                new JpsCommandService(), new MavenCommandService(), new GitCommandService()));
    }

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
        Optional<CommandPlugin> pluginFor = simplePluginRegistry.getPluginFor(command);
        String finalValue = value;
        pluginFor.ifPresentOrElse(cp -> {
            log.info(">>>>>>>>>> {} run <<<<<<<<<<", cp.getClass().getSimpleName());
            cp.run(finalValue);
        }, new Thread(() -> log.info(">>>>>>>>>> invalid command <<<<<<<<<<")));
    }

}
