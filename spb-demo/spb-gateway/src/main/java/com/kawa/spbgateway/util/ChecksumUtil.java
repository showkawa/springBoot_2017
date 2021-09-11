package com.kawa.spbgateway.util;

import com.alibaba.nacos.common.utils.MD5Utils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

public class ChecksumUtil {

    public static String checkSumByMD5(String origin) {
        try {
            return MD5Utils.md5Hex(origin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String checkSumByMD5(Path origin) {
        try {
            byte[] bytes = Files.readAllBytes(origin);
            return MD5Utils.md5Hex(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String checkSumByMD5(File origin) {
        try {
            long checksumCRC32 = FileUtils.checksumCRC32(origin);
            return String.valueOf(checksumCRC32);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
