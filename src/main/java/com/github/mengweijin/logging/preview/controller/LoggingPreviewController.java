package com.github.mengweijin.logging.preview.controller;

import cn.hutool.system.HostInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mengweijin
 * @date 2022/3/24
 */
@Controller
@RequestMapping("/logging")
public class LoggingPreviewController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private Environment environment;

    @GetMapping("/preview")
    public String preview() {
        String address = new HostInfo().getAddress();
        address = address == null ? "localhost" : address;
        String port = environment.getProperty("server.port", "8080");
        request.setAttribute("ip", address);
        request.setAttribute("port", port);
        return "loggingPreview";
    }

}
