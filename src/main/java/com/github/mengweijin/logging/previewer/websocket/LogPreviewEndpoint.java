package com.github.mengweijin.logging.previewer.websocket;

import cn.hutool.extra.spring.SpringUtil;
import com.github.mengweijin.logging.previewer.entity.LogPath;
import com.github.mengweijin.logging.previewer.service.LogPathService;
import com.github.mengweijin.logging.previewer.util.WebSocketUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
 * "ws://localhost:9000/websocket/log/{id}"
 * @author mengweijin
 * @date 2022/3/24
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/log/{id}")
public class LogPreviewEndpoint {

    private static final Map<String, Session> SESSION_MAP = new ConcurrentHashMap<>();

    @Getter @Setter
    @Value("${logging.max-session-limit:100}")
    private int maxSessionLimit;

    public int getSessionCount(){
        return SESSION_MAP.size();
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("id") String id, Session session) {
        LogPreviewEndpoint logPreviewEndpoint = SpringUtil.getBean(LogPreviewEndpoint.class);
        if(SESSION_MAP.size() >= logPreviewEndpoint.getMaxSessionLimit()) {
            String message = "Connection Fail! 连接失败！<br>";
            message += "The maximum number of connections has been reached. 已达到最大连接数。<br>";
            message += "The maximum number of connections is " + logPreviewEndpoint.getMaxSessionLimit() + ". <br>";
            message += "Please change the maximum number of connections or wait for the connection to be released. 请修改最大连接数或者等待连接被释放。";
            WebSocketUtils.send(session, message);
            WebSocketUtils.close(session);
            return;
        }
        SESSION_MAP.put(session.getId(), session);

        LogPathService logPathService = SpringUtil.getBean(LogPathService.class);
        LogPath logPath = logPathService.getById(id);

        // 获取日志信息
        ThreadPoolTaskExecutor threadPoolTaskExecutor = SpringUtil.getBean("loggingPreviewThreadPoolTaskExecutor");
        threadPoolTaskExecutor.execute(new LoggingPreviewerRunnable(session, SESSION_MAP, logPath.getPath()));
    }

    /**
     * 连接关闭调用的方法（正常关闭）
     */
    @OnClose
    public void onClose(Session session) {
        SESSION_MAP.remove(session.getId());
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable e) {
        SESSION_MAP.remove(session.getId());
        log.error("Websocket session id = " + session.getId() + " error!", e);
    }

    /**
     * 服务器接收到客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {}

}
