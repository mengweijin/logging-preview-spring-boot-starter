package com.github.mengweijin.logging.previewer.websocket;

import cn.hutool.http.HtmlUtil;
import com.github.mengweijin.logging.previewer.util.LogFormatUtils;
import com.github.mengweijin.logging.previewer.util.WebSocketUtils;
import com.github.mengweijin.quickboot.framework.util.Const;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author mengweijin
 * @date 2022/3/25
 */
@Slf4j
public class LoggingPreviewerRunnable implements Runnable {

    private static final int ONE_HUNDRED_KB = 1024 * 100;

    private final Session session;

    private final Map<String, Session> sessionMap;

    private final String logPath;

    public LoggingPreviewerRunnable(Session session, Map<String, Session> sessionMap, String logPath) {
        this.session = session;
        this.sessionMap = sessionMap;
        this.logPath = logPath;
    }

    @Override
    public void run() {
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
            while(sessionMap.get(session.getId()) != null) {
                // 从最后一次读取到的数据开始读取
                randomAccessFile.seek(lastPointer);

                // 读取一行,遇到换行符停止,不包含换行符
                while((line = randomAccessFile.readLine()) != null) {
                    // RandomAccessFile 默认使用 ISO-8859-1 编码，这里转为 UTF-8，否则会乱码
                    line = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                    // 对日志进行着色，更加美观。先对原始内容进行转义
                    line = HtmlUtil.escape(line);
                    //处理日志等级背景颜色
                    line = LogFormatUtils.wrapSpanTagWithDefaultCssStyle(line);

                    sb.append(line).append(Const.NEWLINE_HTML);
                }

                //发送
                WebSocketUtils.send(session, sb.toString());
                // 清空 StringBuilder
                sb.setLength(0);

                // 读取完毕后,记录最后一次读取的指针位置
                lastPointer = randomAccessFile.getFilePointer();

                Thread.sleep(1000L);
            }
        } catch (IOException | InterruptedException e) {
            log.error("log preview read error!", e);
            WebSocketUtils.send(session, e.toString());
        } finally {
            // 不管什么原因导致线程终止，一律关闭 session.
            WebSocketUtils.close(session);
        }

        log.info("WebSocketServer task end. [sessionId={}]", session.getId());
    }
}
