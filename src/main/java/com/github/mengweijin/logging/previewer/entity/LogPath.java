package com.github.mengweijin.logging.previewer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.github.mengweijin.quickboot.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 应用日志位置表
 * </p>
 *
 * @author mengweijin
 * @since 2022-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("LOG_PATH")
public class LogPath extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 应用名称
     */
    @NotBlank
    @TableField("APPLICATION")
    private String application;

    /**
     * 日志文件全路径名称，并且忽略大小写（Pattern.Flag.CASE_INSENSITIVE）
     */
    @Pattern(regexp = "[\\s\\S]+(\\.log|\\.txt|\\.yaml|\\.yml|\\.properties)$", flags = {Pattern.Flag.CASE_INSENSITIVE})
    @TableField("PATH")
    private String path;

}
