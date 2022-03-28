package com.github.mengweijin.logging.previewer.controller;

import cn.hutool.system.HostInfo;
import com.github.mengweijin.logging.previewer.entity.LogPath;
import com.github.mengweijin.logging.previewer.service.LogPathService;
import com.github.mengweijin.quickboot.mybatis.Pager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 应用日志位置表 Controller
 * </p>
 *
 * @author mengweijin
 * @since 2022-03-26
 */
@Slf4j
@Validated
@Controller
@RequestMapping("/log-path")
public class LogPathController {

    /**
     * <p>
     * LogPathService
     * </p>
     */
    @Autowired
    private LogPathService logPathService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private Environment environment;

    private static String address;

    private static String port;

    @PostConstruct
    public void init(){
        address = new HostInfo().getAddress();
        address = address == null ? "localhost" : address;
        port = environment.getProperty("server.port", "8080");
    }

    @GetMapping("/preview/{id}")
    public String preview(@PathVariable Long id) {
        request.setAttribute("ip", address);
        request.setAttribute("port", port);

        LogPath logPath = logPathService.getById(id);
        request.setAttribute("logPath", logPath);
        return "layout/previewer/index";
    }

    @GetMapping("/list")
    @ResponseBody
    public Pager<LogPath> list() {
        List<LogPath> list = logPathService.list();

        Pager<LogPath> pager = new Pager<>();
        pager.setRecords(list);
        pager.setTotal(list.size());
        pager.setSize(list.size());
        pager.setCurrent(1);

        return pager;
    }

    /**
     * <p>
     * Get LogPath by id
     * </p>
     * @param id id
     * @return LogPath
     */
    @GetMapping("/{id}")
    @ResponseBody
    public LogPath getById(@PathVariable("id") Serializable id) {
        return logPathService.getById(id);
    }

    /**
     * <p>
     * Add LogPath
     * </p>
     * @param logPath logPath
     */
    @PostMapping
    public void add(@Valid @RequestBody LogPath logPath) {
        logPathService.addLogPath(logPath.getApplication(), logPath.getPath());
    }

    /**
     * <p>
     * Update LogPath
     * </p>
     * @param logPath logPath
     */
    @PutMapping
    public void update(@Valid @RequestBody LogPath logPath) {
        logPathService.updateById(logPath);
    }

    /**
     * <p>
     * Delete LogPath by id
     * </p>
     * @param id id
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Serializable id) {
        logPathService.removeById(id);
    }

}

