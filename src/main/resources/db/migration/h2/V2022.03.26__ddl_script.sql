DROP TABLE IF EXISTS LOG_PATH;
CREATE TABLE LOG_PATH (
  id bigint NOT NULL COMMENT '主键ID',
  application varchar(1024) NOT NULL COMMENT '应用名称',
  path varchar(1024) NOT NULL COMMENT '日志文件全路径名称',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  create_time datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  update_time datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY path_index (path) USING BTREE
) COMMENT='应用日志位置表';
