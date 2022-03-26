package com.kawa.bb.overwrite;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class OvUserService {

    public int getAge(String name) {
        log.info("--call OvUserService getAge--");
        return 99;
    }

    public String getIdNumber(String name) {
        log.info("--call OvUserService getIdNumber--");
        return String.format("%s - %s", UUID.randomUUID().toString().replace("-", ""), name);
    }

}
