package com.kawa.spbgateway.service;

import com.alibaba.nacos.common.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
public class CheckSumUtilTest {

    String filePath = "/home/un/code/springBoot_2017/spb-demo/spb-gateway/src/main/resources/core.yml";


    @Test
    public void checkSumByMD5() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 10000; i++) {
            try {
                byte[] bytes = Files.readAllBytes(Paths.get(filePath));
                String value = MD5Utils.md5Hex(bytes);
                log.info(">>>>>>>>>> MD5 generate value: {}", value);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        stopWatch.stop();
        long lastTaskTimeMillis = stopWatch.getLastTaskTimeMillis();
        log.info(">>>>>>>>>> MD5 generate used time: {}", lastTaskTimeMillis);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Paths.get(filePath).toFile().lastModified()), ZoneId.systemDefault());
        log.info(">>>>>>>>>> File last update time: {}", localDateTime);
    }

    @Test
    public void checkSumByCRC32() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        File origin = Paths.get(filePath).toFile();
        for (int i = 0; i < 10000; i++) {
            try {
                long checksumCRC32 = FileUtils.checksumCRC32(origin);
                log.info(">>>>>>>>>> CRC32 generate value: {}", checksumCRC32);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        stopWatch.stop();
        long lastTaskTimeMillis = stopWatch.getLastTaskTimeMillis();
        log.info(">>>>>>>>>> CRC32 generate used time: {}", lastTaskTimeMillis);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(origin.lastModified()), ZoneId.systemDefault());
        log.info(">>>>>>>>>> File last update time: {}", localDateTime);
    }

    @Test
    public void checkSumHandleTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("MD5");
        for (int i = 0; i < 1000000; i++) {
            try {
                byte[] bytes = Files.readAllBytes(Paths.get(filePath));
                String value = MD5Utils.md5Hex(bytes);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        stopWatch.stop();
        stopWatch.start("CRC32");
        File origin = Paths.get(filePath).toFile();
        for (int i = 0; i < 1000000; i++) {
            try {
                long checksumCRC32 = FileUtils.checksumCRC32(origin);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stopWatch.stop();
        log.info(">>>>>>>>>> Summary generate used time:\n\t {}", stopWatch.prettyPrint());
    }

}
