package com.kawa.script.service.command;

import com.kawa.script.plugin.CommandPlugin;
import com.kawa.script.service.tool.CommandLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassCommandService implements CommandPlugin {

    private static final Logger log = LoggerFactory.getLogger(ClassCommandService.class);

    @Override
    public boolean supports(String delimiter) {
        return delimiter.equals("class");
    }

    @Override
    public void run(String param) {
        log.info(">>>>>>>>>> ClassCommandService param: {}", param);
        new CommandLauncher().start(new String[]{"class", param});
    }
}
