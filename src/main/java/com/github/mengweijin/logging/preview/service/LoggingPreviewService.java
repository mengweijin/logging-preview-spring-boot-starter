package com.github.mengweijin.logging.preview.service;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import java.io.File;

/**
 * @author mengweijin
 * @date 2022/3/25
 */
@Slf4j
public class LoggingPreviewService {

    /**
     * 明确指定日志文件位置。优先级最高。
     */
    @Value("${logging.preview.path:}")
    private String loggingPreviewPath;

    /**
     * 高版本 SpringBoot 日志配置
     */
    @Value("${logging.file.name:}")
    private String loggingFileName;

    /**
     * 低版本 SpringBoot 日志配置
     */
    @Value("${logging.file:}")
    private String loggingFile;

    public String getLogAbsolutePath() {
        String logPath;
        if(StrUtil.isNotBlank(loggingPreviewPath)) {
            logPath = this.loggingPreviewPath;
        } else if(StrUtil.isNotBlank(loggingFileName)) {
            logPath = this.loggingFileName;
        } else if(StrUtil.isNotBlank(loggingFile)) {
            logPath = this.loggingFile;
        } else {
            return null;
        }

        logPath = logPath.startsWith(File.separator) ? logPath : File.separator + logPath;
        return System.getProperty("user.dir") + logPath;
    }
}
