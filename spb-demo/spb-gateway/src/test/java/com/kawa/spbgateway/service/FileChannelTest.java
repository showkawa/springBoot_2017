package com.kawa.spbgateway.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


@Slf4j
public class FileChannelTest {

    @Test
    public void When_GetDataFromFileChannel_Except_Success() throws IOException {
        //create the file input stream
        var file = new File("/home/un/code/springBoot_2017/spb-demo/spb-gateway/src/main/resources/core.yml");
        try (var fileInputStream = new FileInputStream(file)) {
            // get the FileChannel (FileChannelImpl) by FileInputStream
            var fileChannel = fileInputStream.getChannel();
            // create the buffer
            var byteBuffer = ByteBuffer.allocate((int) file.length());
            // read the channel data to buffer
            fileChannel.read(byteBuffer);
            log.info("=== core.yml ===: \r\n{}", new String(byteBuffer.array()));
        }
    }

}
