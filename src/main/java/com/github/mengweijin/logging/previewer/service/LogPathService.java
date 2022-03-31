package com.github.mengweijin.logging.previewer.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.mengweijin.logging.previewer.entity.LogPath;
import com.github.mengweijin.logging.previewer.mapper.LogPathMapper;
import com.github.mengweijin.quickboot.framework.util.Const;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应用日志位置表 implement
 * Add @Transactional(rollbackFor = Exception.class) if you need.
 * </p>
 *
 * @author mengweijin
 * @since 2022-03-26
 */
@Slf4j
@Service
public class LogPathService extends ServiceImpl<LogPathMapper, LogPath> implements IService<LogPath> {

    /**
     * <p>
     * LogPathMapper
     * </p>
     */
    @Autowired
    private LogPathMapper logPathMapper;

    public void addLogPath(@NonNull String application, @NonNull String path) {
        path = StrUtil.replace(path, Const.BACK_SLASH, Const.SLASH);
        logPathMapper.insert(new LogPath().setApplication(application).setPath(path));
    }


}

