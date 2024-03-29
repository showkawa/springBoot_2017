package com.kawa.script.service.command;

import com.kawa.script.plugin.CommandPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JpsCommandService implements CommandPlugin {

    private static final Logger log = LoggerFactory.getLogger(JpsCommandService.class);

    @Override
    public void run(String param) {
        log.info("param:{}", param);
    }

    @Override
    public boolean supports(String s) {
        return s.equals("jps");
    }
}
