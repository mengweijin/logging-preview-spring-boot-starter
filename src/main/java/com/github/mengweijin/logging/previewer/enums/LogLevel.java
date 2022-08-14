package com.github.mengweijin.logging.previewer.enums;

import cn.hutool.core.util.ReUtil;
import com.github.mengweijin.quickboot.util.Const;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * @author mengweijin
 * @date 2022/3/29
 */
public enum LogLevel {
    /**
     * log level and style
     */
    DEBUG("color: #88817f;"),
    INFO("color: #c2c2c2;"),
    WARN("color: #FFB800;"),
    ERROR("color: #FF5722;");

    /**
     * css style. For example: "color: #c2c2c2; background-color:red;"
     */
    @Getter
    private final String styles;

    LogLevel(String styles){
        this.styles = styles;
    }

    /**
     * "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{0,3} +INFO [\\s\\S]+"
     */
    public static LogLevel selectLogLevel(String row){
        Optional<LogLevel> optional = Arrays.stream(LogLevel.values())
                .filter(item -> {
                    String regex = Const.CARET + "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{0,3} +" + item.name() + " [\\s\\S]+";
                    Pattern pattern = Pattern.compile(regex, CASE_INSENSITIVE);
                    return ReUtil.isMatch(pattern, row);
                })
                .findFirst();
        return optional.orElse(LogLevel.INFO);
    }
}
