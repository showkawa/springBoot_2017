package com.kawa.spbgateway.mask;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import static com.kawa.spbgateway.content.Contents.DEFAULT_PATH;


@Slf4j
public class LogMessageConverter extends MessageConverter {
    private static final String PHONE_REGEX = "(\\+861[3|4|5|7|8][0-9]\\d{8}[^\\d])|(^1[3|4|5|7|8][0-9]\\d{8})[^\\d]|([^\\d]1[3|4|5|7|8][0-9]\\d{8}[^\\d])";
    private static final String IDCARD_REGEX = "[^\\d]([1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx])[^\\d]|[^\\d](^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3})[^\\d]";
    private static final String KEY = "*";

    @Override
    public String convert(ILoggingEvent event) {
        // 获取原始日志
        String requestLogMsg = event.getFormattedMessage();
        String loggerName = event.getLoggerName();
        if(loggerName.startsWith(DEFAULT_PATH)){
            return filterSensitive(requestLogMsg);
        }
//        Level level = event.getLevel();
//        Object[] argumentArrays = event.getArgumentArray();
        return requestLogMsg;
    }

    /**
     * 对敏感信息脱敏
     */
    private static String filterSensitive(String content) {
        MDC.put("gid", "12345671232");
        try {
            if (StringUtils.isBlank(content)) {
                return content;
            }
            content = baseSensitive(content, 3 ,3);
            return content;
        } catch (Exception e) {
            return content;
        }
    }



    /**
     * 基础脱敏处理
     *
     * @param str         待脱敏的字符串
     * @param startLength 开始展示长度
     * @param endLength   末尾展示长度
     * @return 脱敏后的字符串
     */
    private static String baseSensitive(String str, int startLength, int endLength) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        String replacement = str.substring(startLength, str.length() - endLength);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < replacement.length(); i++) {
            char ch;
//            sb.append(KEY);
            sb.append(replacement.charAt(i));
        }
        return StringUtils.left(str, startLength).concat(StringUtils.leftPad(StringUtils.right(str, endLength), str.length() - startLength, sb.toString()));
    }


}
