package com.kawa.spbgateway.mask;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.extern.slf4j.Slf4j;

import static com.kawa.spbgateway.content.Contents.DEFAULT_PATH;


@Slf4j
public class LogMessageConverter extends MessageConverter {

    @Override
    public String convert(ILoggingEvent event) {
        String requestLogMsg = event.getFormattedMessage();
        String loggerName = event.getLoggerName();
        if (loggerName.startsWith(DEFAULT_PATH)) {
//        Level level = event.getLevel();
//        Object[] argumentArrays = event.getArgumentArray();
        }
        return requestLogMsg;
    }


}
