package com.github.mengweijin.logging.preview;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HtmlUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mengweijin
 * @date 2022/3/24
 */
@Slf4j
@ServerEndpoint("/websocket/logging")
public class WebSocketServer implements EnvironmentAware {

    private static final Map<String, Session> SESSION_MAP = new ConcurrentHashMap<>();

    private static final int ONE_HUNDRED_KB = 1024 * 100;

    /**
     * 明确指定日志文件位置。优先级最高。
     */
    @Getter
    @Value("${logging.preview.path:}")
    private String loggingPreviewPath;

    private Environment environment;

    @PostConstruct
    public void init() {
        if(StrUtil.isBlank(loggingPreviewPath)) {
            // 高版本 SpringBoot 默认配置
            String loggingFileName = environment.getProperty("logging.file.name");
            if(StrUtil.isBlank(loggingFileName)) {
                // 低版本 SpringBoot 默认配置
                loggingFileName = environment.getProperty("logging.file");
            }
            if(StrUtil.isBlank(loggingFileName)) {
                log.warn("The current application has configured the log file path, the log preview function will not be available!");
            } else {
                loggingFileName = loggingFileName.startsWith(File.separator) ? loggingFileName : File.separator + loggingFileName;
                loggingPreviewPath = System.getProperty("user.dir") + loggingFileName;
            }
        }
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        WebSocketServer webSocketServer = SpringUtil.getBean(WebSocketServer.class);
        String logPath = webSocketServer.getLoggingPreviewPath();
        if(StrUtil.isBlank(logPath)) {
            return;
        }

        SESSION_MAP.put(session.getId(), session);

        // 获取日志信息
        ThreadPoolTaskExecutor threadPoolTaskExecutor = SpringUtil.getBean("loggingPreviewThreadPoolTaskExecutor");
        threadPoolTaskExecutor.execute(() -> {
            log.info("WebSocketServer task start. [sessionId={}]", session.getId());
            // 注意：不能打开写权限("w"), 不然其他程序没法写入数据到该文件
            try(RandomAccessFile randomAccessFile = new RandomAccessFile(logPath, "r")) {
                long length = randomAccessFile.length();
                // 最后一次指针的字节位置, 默认从头开始(这里读取最后 100 KB。1024 * 100 = 100KB)
                long lastPointer = length <= ONE_HUNDRED_KB ? 0 : length - ONE_HUNDRED_KB;

                // 每一行读取到的数据
                String line;
                StringBuilder sb = new StringBuilder();
                // 持续监听文件，只要这个 session 没有关闭，就一直监听。
                while(SESSION_MAP.get(session.getId()) != null) {
                    // 从最后一次读取到的数据开始读取
                    randomAccessFile.seek(lastPointer);

                    // 读取一行,遇到换行符停止,不包含换行符
                    while((line = randomAccessFile.readLine()) != null) {
                        // RandomAccessFile 默认使用 ISO-8859-1 编码，这里转为 UTF-8，否则会乱码
                        line = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                        // 对日志进行着色，更加美观。先对原始内容进行转义
                        line = HtmlUtil.escape(line);

                        //处理等级
                        line = line.replace("DEBUG", "<span style='color: blue;'>DEBUG</span>");
                        line = line.replace("INFO", "<span style='color: green;'>INFO</span>");
                        line = line.replace("WARN", "<span style='color: orange;'>WARN</span>");
                        line = line.replace("ERROR", "<span style='color: red;'>ERROR</span>");

                        sb.append(line).append("<br>");
                    }

                    //发送
                    send(session, sb.toString());
                    // 清空 StringBuilder
                    sb.setLength(0);

                    // 读取完毕后,记录最后一次读取的指针位置
                    lastPointer = randomAccessFile.getFilePointer();

                }
            } catch (IOException e) {
                log.error("log preview read error!", e);
            }

            log.info("WebSocketServer task end. [sessionId={}]", session.getId());
        });

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
    public void onError(Session session, Throwable e) {

    }

    /**
     * 服务器接收到客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {

    }

    /**
     * 封装一个send方法，发送消息到前端
     */
    private void send(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public long getFileLineNum(String filePath) {
        try {
            return Files.lines(Paths.get(filePath)).count();
        } catch (IOException e) {
            log.error("error", e);
            return -1;
        }
    }

}
