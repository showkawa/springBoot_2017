package com.kawa.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;
import java.util.UUID;


@Slf4j
public class FileChannelTest {

    @Test
    public void When_GetDataFromFileChannel_Except_Success() throws IOException {
        //create the file input stream
        var file = new File("/home/un/code/springBoot_2017/spb-demo/spb-gateway/src/main/resources/pancake.yml");
        try (var fileInputStream = new FileInputStream(file)) {
            // get the FileChannel (FileChannelImpl) by FileInputStream
            var fileChannel = fileInputStream.getChannel();
            // create the buffer
            var byteBuffer = ByteBuffer.allocate((int) file.length());
            // read the channel data to buffer
            fileChannel.read(byteBuffer);
            log.info("=== pancake.yml ===: \r\n{}", new String(byteBuffer.array()));
        }
    }

    @Test
    public void When_OutputData_Except_Success() throws IOException {
        //create the file input stream
        var file = new File("/home/un/code/springBoot_2017/spb-demo/spb-gateway/src/main/resources/pancake.yml");
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
        try (var sourceStream = new FileInputStream("/home/un/code/springBoot_2017/spb-demo/spb-gateway/src/main/resources/pancake.yml");
             var targetStream = new FileOutputStream("/home/un/app/test/text.txt")) {
            // create the FileChannel
            var sourceCh = sourceStream.getChannel();
            var targetCh = targetStream.getChannel();
            // use transferFrom transfer data to target FileChannel
            targetCh.transferFrom(sourceCh, 0, sourceCh.size());
        }
    }

    @Test
    public void When_ReadDataByMappedByteBuffer_Except_Success() throws IOException {
        File file = new File("/home/un/code/springBoot_2017/spb-demo/spb-gateway/src/main/resources/pancake.yml");
        long len = file.length();
        byte[] ds = new byte[(int) len];

        MappedByteBuffer mappedByteBuffer = new RandomAccessFile(file, "r")
                .getChannel()
                .map(FileChannel.MapMode.READ_ONLY, 0, len);
        for (int offset = 0; offset < len; offset++) {
            byte b = mappedByteBuffer.get();
            ds[offset] = b;
        }

        Scanner scan = new Scanner(new ByteArrayInputStream(ds)).useDelimiter("\n");
        while (scan.hasNext()) {
            log.info("=== MappedByteBuffer ===: {}", scan.next());
        }

        // try to put
        // java.nio.ReadOnlyBufferException
        mappedByteBuffer.flip();
        mappedByteBuffer.put("brian".getBytes());
    }

}