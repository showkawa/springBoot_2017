package com.kawa.ssist;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class JustRun {

    private String name;

    public JustRun() {
        if (StringUtils.hasText(name)) {
            this.name = this.getClass().getSimpleName();
        }
    }

    public String returnPublicStr(String name) {
        log.info(">>>>>>>>>>> name: {}", name);
        String format = String.format("JustRun - returnPublicStr - %s", name);
        return format;
    }

    protected String returnProtectedStr(String name) {
        return String.format("JustRun - returnProtectedStr - %s", name);
    }

    private String returnPrivateStr(String name) {
        return String.format("JustRun - returnPrivateStr - %s", name);
    }
}
