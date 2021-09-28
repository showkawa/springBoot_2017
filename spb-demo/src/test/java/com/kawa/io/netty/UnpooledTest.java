package com.kawa.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class UnpooledTest {

    @Test
    public void Unpooled_Test_CopiedBuffer() {
        ByteBuf byteBuf = Unpooled.copiedBuffer("{'region':'cn'}", CharsetUtil.UTF_8);
        if (byteBuf.hasArray()) {
            byte[] bytes = byteBuf.array();
            log.info(">>>>>>>>>> byteBuf: {}", byteBuf);
            log.info(">>>>>>>>>> byteBuf convert to String: {}", new String(bytes, CharsetUtil.UTF_8));
            log.info(">>>>>>>>>> byteBuf arrayOffset:{}, readerIndex:{}, writerIndex:{}, capacity:{}", byteBuf.arrayOffset(), byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());

            log.info(">>>>>>>>>> byteBuf > getByte: {}", (char) byteBuf.getByte(0));
            log.info(">>>>>>>>>> byteBuf > readableBytes: {}", byteBuf.readableBytes());
            log.info(">>>>>>>>>> byteBuf arrayOffset:{}, readerIndex:{}, writerIndex:{}, capacity:{}", byteBuf.arrayOffset(), byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());

            log.info(">>>>>>>>>> byteBuf > readByte: {}", (char) byteBuf.readByte());
            log.info(">>>>>>>>>> byteBuf > readableBytes: {}", byteBuf.readableBytes());
            log.info(">>>>>>>>>> byteBuf arrayOffset:{}, readerIndex:{}, writerIndex:{}, capacity:{}", byteBuf.arrayOffset(), byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());

            log.info(">>>>>>>>>> byteBuf > readByte: {}", (char) byteBuf.readByte());
            log.info(">>>>>>>>>> byteBuf > readableBytes: {}", byteBuf.readableBytes());
            log.info(">>>>>>>>>> byteBuf arrayOffset:{}, readerIndex:{}, writerIndex:{}, capacity:{}", byteBuf.arrayOffset(), byteBuf.readerIndex(), byteBuf.writerIndex(), byteBuf.capacity());

            log.info(">>>>>>>>>> byteBuf > getCharSequence: {}", byteBuf.getCharSequence(2, 5, CharsetUtil.UTF_8));
            log.info(">>>>>>>>>> byteBuf > getCharSequence: {}", byteBuf.getCharSequence(5, 10, CharsetUtil.UTF_8));
        }
    }
}
