package com.github.mengweijin.logging.previewer.controller;

import cn.hutool.core.io.FileUtil;
import com.github.mengweijin.logging.previewer.service.LogPathService;
import com.github.mengweijin.quickboot.framework.util.DownLoadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
@RequestMapping("/log-download")
public class LogDownloadController {

    @Autowired
    private LogPathService logPathService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping
    public String downloadPage(@NotBlank String path) {
        List<String> list = new ArrayList<>();
        File file = FileUtil.file(path);
        if(file.isFile() && file.exists()) {
            File[] listFiles = file.getParentFile().listFiles();
            if(listFiles != null) {
                Arrays.stream(listFiles)
                        .filter(f -> f.getName().contains(file.getName()))
                        .forEach(f -> list.add(f.getAbsolutePath()));
            }
        }
        request.setAttribute("fileList", list);
        return "layout/logpath/download";
    }

    @GetMapping("/byPath")
    public void download(@NotBlank String path, HttpServletRequest request, HttpServletResponse response) {
        File file = FileUtil.file(path);
        DownLoadUtils.download(file, request, response);
    }
}

