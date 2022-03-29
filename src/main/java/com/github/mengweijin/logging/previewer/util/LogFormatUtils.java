package com.github.mengweijin.logging.previewer.util;

import com.github.mengweijin.logging.previewer.enums.LogLevel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mengweijin
 * @date 2022/3/29
 */
@Slf4j
public final class LogFormatUtils {

    private LogFormatUtils(){}

    public static String wrapSpanTagWithDefaultCssStyle(String row) {
        LogLevel logLevel = LogLevel.selectLogLevel(row);
        return wrapSpanTagWithCssStyles(row, logLevel.getStyles());
    }

    public static String wrapSpanTagWithCssStyles(String row, String styles) {
        return "<span style='" + styles + "'>" + row + "</span>";
    }

}
