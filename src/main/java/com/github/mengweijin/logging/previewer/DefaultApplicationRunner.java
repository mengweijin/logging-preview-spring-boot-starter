package com.github.mengweijin.logging.previewer;

import com.github.mengweijin.logging.previewer.entity.LogPath;
import com.github.mengweijin.logging.previewer.service.LogPathService;
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
        Long count = logPathService.lambdaQuery()
                .ge(LogPath::getPath, logAbsolutePath)
                .count();
        if(count == 0) {
            logPathService.addLogPath(applicationName, logAbsolutePath);
        }
    }

    public String getApplicationLogAbsolutePath() {
        StringBuilder sb = new StringBuilder(System.getProperty("user.dir"));
        if(File.separatorChar != loggingFileName.charAt(0)) {
            sb.append(File.separatorChar);
        }
        return sb.append(loggingFileName).toString();
    }
}
