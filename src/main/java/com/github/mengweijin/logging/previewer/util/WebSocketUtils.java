package com.github.mengweijin.logging.previewer.util;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author mengweijin
 * @date 2022/3/25
 */
@Slf4j
public final class WebSocketUtils {

    private WebSocketUtils(){}
    /**
     * 封装一个send方法，发送消息到前端
     */
    public static void send(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            // ignore
        }
    }

    public static void close(Session session) {
        try {
            if(session != null) {
                session.close();
            }
        } catch (Exception e) {
            // ignore
        }
    }

    public static long getFileLineCount(String filePath) {
        try {
            return Files.lines(Paths.get(filePath)).count();
        } catch (IOException e) {
            log.error("error", e);
            return -1;
        }
    }
}
