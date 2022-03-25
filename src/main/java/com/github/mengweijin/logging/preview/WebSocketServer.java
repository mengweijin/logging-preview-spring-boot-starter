package com.github.mengweijin.logging.preview;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.github.mengweijin.logging.preview.service.LoggingPreviewService;
import com.github.mengweijin.logging.preview.util.WebSocketUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mengweijin
 * @date 2022/3/24
 */
@Slf4j
@ServerEndpoint("/websocket/logging")
public class WebSocketServer {

    private static volatile Map<String, Session> SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        LoggingPreviewService loggingPreviewService = SpringUtil.getBean(LoggingPreviewService.class);
        String logPath = loggingPreviewService.getLogAbsolutePath();
        if(StrUtil.isBlank(logPath)) {
            WebSocketUtils.send(session, "The current application has not configured the log file path, the log preview function will not be available!");
            WebSocketUtils.close(session);
            return;
        }

        SESSION_MAP.put(session.getId(), session);

        // 获取日志信息
        ThreadPoolTaskExecutor threadPoolTaskExecutor = SpringUtil.getBean("loggingPreviewThreadPoolTaskExecutor");
        threadPoolTaskExecutor.execute(new LoggingPreviewRunnable(session, SESSION_MAP, logPath));
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        SESSION_MAP.remove(session.getId());
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable e) {}

    /**
     * 服务器接收到客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {}

}
