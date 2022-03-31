package com.github.mengweijin.logging.previewer.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.system.HostInfo;
import com.github.mengweijin.logging.previewer.entity.LogPath;
import com.github.mengweijin.logging.previewer.service.LogPathService;
import com.github.mengweijin.quickboot.framework.util.Const;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/edit")
    public String edit(@RequestParam(required = false) Long id) {
        LogPath logPath;
        if(id == null) {
            // add
            logPath = new LogPath();
        } else {
            // edit
            logPath = logPathService.getById(id);
        }
        request.setAttribute("logPath", logPath);
        return "layout/logpath/edit";
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
    @ResponseBody
    public void add(@Valid LogPath logPath) {
        logPathService.addLogPath(logPath.getApplication(), logPath.getPath());
    }

    /**
     * <p>
     * Update LogPath
     * </p>
     * @param logPath logPath
     */
    @PutMapping
    @ResponseBody
    public void update(@Valid LogPath logPath) {
        String path = StrUtil.replace(logPath.getPath(), Const.BACK_SLASH, Const.SLASH);
        logPath.setPath(path);
        logPathService.updateById(logPath);
    }

    /**
     * <p>
     * Delete LogPath by id
     * </p>
     * @param id id
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable("id") Serializable id) {
        logPathService.removeById(id);
    }

}

