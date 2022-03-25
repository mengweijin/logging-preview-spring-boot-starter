package com.github.mengweijin.logging.preview;

import com.github.mengweijin.logging.preview.controller.LoggingPreviewController;
import com.github.mengweijin.logging.preview.service.LoggingPreviewService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author mengweijin
 * @date 2022/3/24
 */
@Profile({"!prod"})
@ConditionalOnProperty(prefix = "logging", name = "preview.enabled", havingValue = "true")
@Configuration
public class LoggingPreviewAutoConfiguration {

    @Bean
    public WebSocketServer webSocketServer() {
        return new WebSocketServer();
    }

    @Bean
    public LoggingPreviewController loggingPreviewController() {
        return new LoggingPreviewController();
    }

    @Bean
    public LoggingPreviewService loggingPreviewService() {
        return new LoggingPreviewService();
    }

    /**
     * 扫描并注册所有携带 @ServerEndpoint 注解的实例。 @ServerEndpoint("/websocket")
     * PS：如果使用外部容器，则无需提供 ServerEndpointExporter。
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public ThreadPoolTaskExecutor loggingPreviewThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程池大小
        executor.setCorePoolSize(0);
        //最大线程数
        executor.setMaxPoolSize(100);
        //活跃时间
        executor.setKeepAliveSeconds(60);
        //队列容量
        executor.setQueueCapacity(-1);
        //线程名字前缀
        executor.setThreadNamePrefix("Thread-pool-logging-preview-");
        // setRejectedExecutionHandler：当pool已经达到maxSize的时候，如何处理新进任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 是否等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(false);
        executor.initialize();
        return executor;
    }
}