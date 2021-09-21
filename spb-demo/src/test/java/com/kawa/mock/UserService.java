package com.kawa.mock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService {

    public int getAge(String name) {
        log.info("--call getAge method--");
        return 18;
    }

    public String getIdNumber(String name) {
        log.info("--call getIdNumber method--");
        throw new RuntimeException();
    }

}
