package com.github.mengweijin.logging.previewer;

import cn.hutool.core.util.StrUtil;
import com.github.mengweijin.logging.previewer.entity.LogPath;
import com.github.mengweijin.logging.previewer.service.LogPathService;
import com.github.mengweijin.quickboot.util.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author mengweijin
 * @date 2022/3/26
 */
@Slf4j
@Component
public class DefaultApplicationRunner implements ApplicationRunner {

    @Value("${logging.file.name}")
    private String loggingFileName;

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private LogPathService logPathService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String logAbsolutePath = this.getApplicationLogAbsolutePath();
        logAbsolutePath = StrUtil.replace(logAbsolutePath, Const.BACK_SLASH, Const.SLASH);
        Long count = logPathService.lambdaQuery()
                .eq(LogPath::getPath, logAbsolutePath)
                .count();
        if(count == 0) {
            logPathService.addLogPath(applicationName, logAbsolutePath);
        }
        log.debug("Please ignore this log（请忽略这条日志）! Just to test the style of the log display.");
        log.info("Please ignore this log（请忽略这条日志）! Just to test the style of the log display.");
        log.warn("Please ignore this log（请忽略这条日志）! Just to test the style of the log display.");
        log.error("Please ignore this log（请忽略这条日志）! Just to test the style of the log display.");
    }

    public String getApplicationLogAbsolutePath() {
        StringBuilder sb = new StringBuilder(System.getProperty("user.dir"));
        if(File.separatorChar != loggingFileName.charAt(0)) {
            sb.append(File.separatorChar);
        }
        return StrUtil.replace(sb.append(loggingFileName).toString(), Const.BACK_SLASH, Const.SLASH);
    }
}
