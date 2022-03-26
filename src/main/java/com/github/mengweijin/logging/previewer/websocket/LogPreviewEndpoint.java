package com.github.mengweijin.logging.previewer.websocket;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * "ws://localhost:9000/websocket/log/{path}"
 * @author mengweijin
 * @date 2022/3/24
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/log/{path}")
public class LogPreviewEndpoint {

    private static final Map<String, Session> SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("path") String logPath, Session session) {
        SESSION_MAP.put(session.getId(), session);

        // 获取日志信息
        ThreadPoolTaskExecutor threadPoolTaskExecutor = SpringUtil.getBean("loggingPreviewThreadPoolTaskExecutor");
        threadPoolTaskExecutor.execute(new LoggingPreviewerRunnable(session, SESSION_MAP, logPath));
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
