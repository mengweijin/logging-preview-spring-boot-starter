package com.github.mengweijin.logging.previewer.enums;

import cn.hutool.core.util.ReUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mengweijin
 * @date 2022/3/29
 */
class LogLevelTest {

    @Test
    void selectLogLevel() {
        String row = "2022-03-29 19:56:14.848  INFO 16164 --- [gging-preview-1] c.g.m.l.p.w.LoggingPreviewerRunnable: WebSocketServer task start. [sessionId=0]";
        LogLevel logLevel = LogLevel.selectLogLevel(row);
        Assertions.assertEquals(LogLevel.INFO, logLevel);
    }

    @Test
    void testRegex() {
        String regex = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{0,3} +INFO [\\s\\S]+";
        String row = "2022-03-29 19:56:14.848  INFO 16164 --- [gging-preview-1] c.g.m.l.p.w.LoggingPreviewerRunnable: WebSocketServer task start. [sessionId=0]";
        Pattern pattern = Pattern.compile(regex, CASE_INSENSITIVE);
        assertTrue(ReUtil.isMatch(pattern, row));
    }
}