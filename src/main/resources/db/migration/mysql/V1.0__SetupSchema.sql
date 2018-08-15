DROP TABLE IF EXISTS oauth_access_token;
CREATE TABLE oauth_access_token
(
  token_id          VARCHAR(255) COMMENT 'MD5加密的access_token的值',
  token             LONGBLOB COMMENT 'OAuth2AccessToken.java对象序列化后的二进制数据',
  authentication_id VARCHAR(255) COMMENT 'MD5加密过的 username, client_id, scope',
  user_name         VARCHAR(255) COMMENT '登录的用户名',
  client_id         VARCHAR(255) COMMENT '客户端ID',
  authentication    BINARY COMMENT 'OAuth2Authentication.java 对象序列化后的二进制数据',
  refresh_token     VARCHAR(255) COMMENT 'MD5加密果的refresh_token的值'
) COMMENT '访问令牌表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS oauth_refresh_token;
CREATE TABLE oauth_refresh_token
(
  token_id       VARCHAR(255) COMMENT 'MD5加密过的refresh_token的值',
  token          LONGBLOB COMMENT 'OAuth2RefreshToken.java 对象序列化后的二进制数据',
  authentication LONGBLOB COMMENT 'OAuth2Authentication.java对象序列化后的二进制数据'
) COMMENT '更新令牌表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
# 授权记录表
DROP TABLE IF EXISTS oauth_approvals;
CREATE TABLE oauth_approvals
(
  userid         VARCHAR(255) COMMENT '登录的用户名',
  clientid       VARCHAR(255) COMMENT '客户端ID',
  scope          VARCHAR(255) COMMENT '申请的权限',
  status         VARCHAR(10) COMMENT '状态（Approve或Deny）',
  expiresat      DATETIME COMMENT '过期时间',
  lastmodifiedat DATETIME COMMENT '最终修改时间'
) COMMENT '授权记录表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
# 授权码表
DROP TABLE IF EXISTS oauth_code;
CREATE TABLE oauth_code
(
  code           VARCHAR(255) COMMENT '授权码(未加密)',
  authentication VARBINARY(255) COMMENT 'AuthorizationRequestHolder.java 对象序列化后的二进制数据'
) COMMENT '授权码表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
# client用户表
DROP TABLE IF EXISTS oauth_client_details;
CREATE TABLE oauth_client_details
(
  client_id               VARCHAR(255) NOT NULL COMMENT '客户端ID',
  resource_ids            VARCHAR(255) COMMENT '资源ID集合,多个资源时用逗号(,)分隔',
  client_secret           VARCHAR(255) COMMENT '客户端密匙',
  scope                   VARCHAR(255) COMMENT '客户端申请的权限范围',
  authorized_grant_types  VARCHAR(255) COMMENT '客户端支持的grant_type',
  web_server_redirect_uri VARCHAR(255) COMMENT '重定向URI',
  authorities             VARCHAR(255) COMMENT '客户端所拥有的Spring Security的权限值，多个用逗号(,)分隔',
  access_token_validity   INTEGER COMMENT '访问令牌有效时间值(单位:秒)',
  refresh_token_validity  INTEGER COMMENT '更新令牌有效时间值(单位:秒)',
  additional_information  VARCHAR(4096) COMMENT '预留字段',
  autoapprove             VARCHAR(255) COMMENT '用户是否自动Approval操作',
  CONSTRAINT pk_oauth_client_details_client_id PRIMARY KEY (client_id)
) COMMENT '客户端信息' ENGINE=InnoDB DEFAULT CHARSET=utf8;
# 客户端授权令牌表
DROP TABLE IF EXISTS oauth_client_token;
CREATE TABLE oauth_client_token
(
  token_id          VARCHAR(255) COMMENT 'MD5加密的access_token值',
  token             BINARY COMMENT 'OAuth2AccessToken.java对象序列化后的二进制数据',
  authentication_id VARCHAR(255) COMMENT 'MD5加密过的username,client_id,scope',
  user_name         VARCHAR(255) COMMENT '登录的用户名',
  client_id         VARCHAR(255) COMMENT '客户端ID'
) COMMENT '客户端授权令牌表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
# 用户表
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
  id                      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '用户id',
  username                VARCHAR(100) NOT NULL COMMENT '用户名',
  password                VARCHAR(100) NOT NULL COMMENT '用户密码密文',
  name                    VARCHAR(200) COMMENT '用户姓名',
  mobile                  VARCHAR(20) COMMENT '用户手机',
  email                   VARCHAR(255) COMMENT '用户电子邮件',
  avatar                  VARCHAR(255) COMMENT '用户头像',
  pinyin_name_initials    VARCHAR(100) COMMENT '用户姓名简拼',
  enabled                 BOOLEAN COMMENT '是否有效用户',
  account_non_expired     BOOLEAN COMMENT '账号是否未过期',
  credentials_non_expired BOOLEAN COMMENT '密码是否未过期',
  account_non_locked      BOOLEAN COMMENT '帐户是否未锁定',
  created_time            DATETIME    NOT NULL DEFAULT now() COMMENT '创建时间',
  updated_time            DATETIME    NOT NULL DEFAULT now() COMMENT '更新时间',
  created_by              VARCHAR(100) NOT NULL COMMENT '创建人',
  updated_by              VARCHAR(100) NOT NULL COMMENT '更新人'
) COMMENT '用户表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX ux_users_username
  ON users (username);
CREATE UNIQUE INDEX ux_users_mobile
  ON users (mobile);
# 用户组表
DROP TABLE IF EXISTS groups;
CREATE TABLE groups
(
  id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '用户组id',
  parent_id    BIGINT          NOT NULL COMMENT '用户组父id',
  name         VARCHAR(200) COMMENT '用户组名称',
  description   VARCHAR(500) COMMENT '用户组简介',
  created_time DATETIME    NOT NULL DEFAULT now() COMMENT '创建时间',
  updated_time DATETIME    NOT NULL DEFAULT now() COMMENT '更新时间',
  created_by   VARCHAR(100) NOT NULL COMMENT '创建人',
  updated_by   VARCHAR(100) NOT NULL COMMENT '更新人'
) COMMENT '用户组表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
# 用户和组关系表
DROP TABLE IF EXISTS users_groups_relation;
CREATE TABLE users_groups_relation
(
  id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '关系id',
  user_id      BIGINT          NOT NULL COMMENT '用户id',
  group_id     BIGINT          NOT NULL COMMENT '用户组id',
  created_time DATETIME    NOT NULL DEFAULT now() COMMENT '创建时间',
  updated_time DATETIME    NOT NULL DEFAULT now() COMMENT '更新时间',
  created_by   VARCHAR(100) NOT NULL COMMENT '创建人',
  updated_by   VARCHAR(100) NOT NULL COMMENT '更新人'
) COMMENT '用户和组关系表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
# 岗位表
DROP TABLE IF EXISTS positions;
CREATE TABLE positions
(
  id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '岗位id',
  name         VARCHAR(200) COMMENT '岗位名称',
  description   VARCHAR(500) COMMENT '岗位简介',
  created_time DATETIME    NOT NULL DEFAULT now() COMMENT '创建时间',
  updated_time DATETIME    NOT NULL DEFAULT now() COMMENT '更新时间',
  created_by   VARCHAR(100) NOT NULL COMMENT '创建人',
  updated_by   VARCHAR(100) NOT NULL COMMENT '更新人'
) COMMENT '岗位表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
# 用户和角色系表
DROP TABLE IF EXISTS users_positions_relation;
CREATE TABLE users_positions_relation
(
  id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '关系id',
  user_id      BIGINT          NOT NULL COMMENT '用户id',
  position_id  BIGINT          NOT NULL COMMENT '岗位id',
  created_time DATETIME    NOT NULL DEFAULT now() COMMENT '创建时间',
  updated_time DATETIME    NOT NULL DEFAULT now() COMMENT '更新时间',
  created_by   VARCHAR(100) NOT NULL COMMENT '创建人',
  updated_by   VARCHAR(100) NOT NULL COMMENT '更新人'
) COMMENT '用户和角色关系表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
# 角色表
DROP TABLE IF EXISTS roles;
CREATE TABLE roles
(
  id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '角色id',
  code         VARCHAR(100) NOT NULL COMMENT '角色编码',
  name         VARCHAR(200) COMMENT '角色名称',
  description   VARCHAR(500) COMMENT '角色简介',
  created_time DATETIME    NOT NULL DEFAULT now() COMMENT '创建时间',
  updated_time DATETIME    NOT NULL DEFAULT now() COMMENT '更新时间',
  created_by   VARCHAR(100) NOT NULL COMMENT '创建人',
  updated_by   VARCHAR(100) NOT NULL COMMENT '更新人'
) COMMENT '角色表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
# 用户和角色关系表
DROP TABLE IF EXISTS users_roles_relation;
CREATE TABLE users_roles_relation
(
  id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '关系id',
  user_id      BIGINT          NOT NULL COMMENT '用户id',
  role_id      BIGINT          NOT NULL COMMENT '角色id',
  created_time DATETIME    NOT NULL DEFAULT now() COMMENT '创建时间',
  updated_time DATETIME    NOT NULL DEFAULT now() COMMENT '更新时间',
  created_by   VARCHAR(100) NOT NULL COMMENT '创建人',
  updated_by   VARCHAR(100) NOT NULL COMMENT '更新人'
) COMMENT '用户和角色关系表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
# 菜单表
DROP TABLE IF EXISTS menus;
CREATE TABLE menus
(
  id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '菜单id',
  parent_id    BIGINT          NOT NULL COMMENT '父菜单id',
  type         VARCHAR(100) COMMENT '菜单类型',
  href         VARCHAR(200) COMMENT '菜单路径',
  icon         VARCHAR(200) COMMENT '菜单图标',
  name         VARCHAR(200) COMMENT '菜单名称',
  description   VARCHAR(500) COMMENT '菜单简介',
  order_num    INT COMMENT '菜单顺序',
  created_time DATETIME    NOT NULL DEFAULT now() COMMENT '创建时间',
  updated_time DATETIME    NOT NULL DEFAULT now() COMMENT '更新时间',
  created_by   VARCHAR(100) NOT NULL COMMENT '创建人',
  updated_by   VARCHAR(100) NOT NULL COMMENT '更新人'
) COMMENT '菜单表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
# 角色和菜单关系表
DROP TABLE IF EXISTS roles_menus_relation;
CREATE TABLE roles_menus_relation
(
  id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '关系id',
  menu_id      BIGINT          NOT NULL COMMENT '菜单id',
  role_id      BIGINT          NOT NULL COMMENT '角色id',
  created_time DATETIME    NOT NULL DEFAULT now() COMMENT '创建时间',
  updated_time DATETIME    NOT NULL DEFAULT now() COMMENT '更新时间',
  created_by   VARCHAR(100) NOT NULL COMMENT '创建人',
  updated_by   VARCHAR(100) NOT NULL COMMENT '更新人'
) COMMENT '角色和菜单关系表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
# 资源表
DROP TABLE IF EXISTS resources;
CREATE TABLE resources
(
  id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '资源id',
  code         VARCHAR(100) COMMENT '资源代码',
  type         VARCHAR(100) COMMENT '资源类型',
  name         VARCHAR(200) COMMENT '资源名称',
  url          VARCHAR(200) COMMENT '资源 URL',
  method       VARCHAR(20) COMMENT '资源访问方法',
  description  VARCHAR(500) COMMENT '资源简介',
  created_time DATETIME    NOT NULL DEFAULT now() COMMENT '创建时间',
  updated_time DATETIME    NOT NULL DEFAULT now() COMMENT '更新时间',
  created_by   VARCHAR(100) NOT NULL COMMENT '创建人',
  updated_by   VARCHAR(100) NOT NULL COMMENT '更新人'
) COMMENT '资源表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX ux_resources_code
  ON resources (code);
# 角色和资源关系表
DROP TABLE IF EXISTS roles_resources_relation;
CREATE TABLE roles_resources_relation
(
  id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '关系id',
  resource_id  BIGINT          NOT NULL COMMENT '资源id',
  role_id      BIGINT          NOT NULL COMMENT '角色id',
  created_time DATETIME    NOT NULL DEFAULT now() COMMENT '创建时间',
  updated_time DATETIME    NOT NULL DEFAULT now() COMMENT '更新时间',
  created_by   VARCHAR(100) NOT NULL COMMENT '创建人',
  updated_by   VARCHAR(100) NOT NULL COMMENT '更新人'
) COMMENT '角色和资源关系表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
