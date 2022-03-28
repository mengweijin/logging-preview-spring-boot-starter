package com.github.mengweijin.logging.previewer.cofig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author mengweijin
 * @date 2022/3/24
 */
@Configuration
public class LogPreviewConfiguration {

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
        executor.setMaxPoolSize(Integer.MAX_VALUE);
        //活跃时间
        executor.setKeepAliveSeconds(60);
        //队列容量
        executor.setQueueCapacity(-1);
        //线程名字前缀
        executor.setThreadNamePrefix("Thread-pool-logging-preview-");
        // setRejectedExecutionHandler：当pool已经达到maxSize的时候，如何处理新进任务
        // session：浏览器标签页不关闭，session 就不会终止（F5 刷新页面会关闭 session）。
        // ThreadPoolExecutor.AbortPolicy: 丢弃任务并抛出RejectedExecutionException异常。  【默认】
        // ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
        // ThreadPoolExecutor.DiscardOldestPolicy：丢弃线称队列的旧的任务，将新的任务添加
        // ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务 【谁调用，谁处理】
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 是否等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(false);
        executor.initialize();
        return executor;
    }
}