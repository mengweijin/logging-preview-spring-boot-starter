package com.github.mengweijin.logging.preview.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author mengweijin
 * @date 2022/3/24
 */
@Slf4j
@Component
public class GenerateLogFactory {

    @Scheduled(cron = "0/5 * * * * ?")
    public void doTaskInSecond() {
        log.debug("测试 debug. now time is {}", LocalDateTime.now());
        log.info("测试 info. now time is {}", LocalDateTime.now());
        log.warn("测试 warn. now time is {}", LocalDateTime.now());
        log.error("测试 error. now time is {}", LocalDateTime.now());
    }
}
