package com.kawa.spbgateway.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;


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

    @Test
    public void When_OutputData_Except_Success() throws IOException {
        //create the file input stream
        var file = new File("/home/un/code/springBoot_2017/spb-demo/spb-gateway/src/main/resources/core.yml");
        ByteBuffer outputStr;

        try (var fileInputStream = new FileInputStream(file)) {
            // get the FileChannel (FileChannelImpl) by FileInputStream
            var fileChannel = fileInputStream.getChannel();
            // create the buffer
            outputStr = ByteBuffer.allocate((int) file.length());
            // read the channel data to buffer
            fileChannel.read(outputStr);
        }

        // create a file output stream
        var fileName = UUID.randomUUID().toString().replace("-", "");
        try (var fileOutputStream = new FileOutputStream("/home/un/app/test/" + fileName)) {
            // get file channel by stream
            var channel = fileOutputStream.getChannel();
            outputStr.flip();
            channel.write(outputStr);
        }
    }

    @Test
    public void When_CopyFileByTransferFrom_Except_Success() throws IOException {
        //create FileInputStream and FileOutputStream
        try (var sourceStream = new FileInputStream("/home/un/code/springBoot_2017/spb-demo/spb-gateway/src/main/resources/core.yml");
             var targetStream = new FileOutputStream("/home/un/app/test/text.txt")) {
            // create the FileChannel
            var sourceCh = sourceStream.getChannel();
            var targetCh = targetStream.getChannel();
            // use transferFrom transfer data to target FileChannel
            targetCh.transferFrom(sourceCh,0 , sourceCh.size());
        }

    }


}
