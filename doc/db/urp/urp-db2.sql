
drop table uweb.t_urp_department;
CREATE TABLE uweb.t_urp_department (
  ID		char(64)	NOT NULL,
  DEPTNAME	varchar(64)	NOT NULL DEFAULT '',
  DN		varchar(500)	NOT NULL ,
  ALIASNAME	varchar(64)	NOT NULL DEFAULT '',
  PARENTID	char(64)	NOT NULL DEFAULT '',
  POSTAL	char(6)		NOT NULL DEFAULT '',
  DEPTPHONE	char(12)	NOT NULL DEFAULT '',
  DEPTFAX	char(12)	NOT NULL DEFAULT '',
  ADDRESS	varchar(50)	NOT NULL DEFAULT '',
  DEPTLEADER	varchar(50)	NOT NULL DEFAULT '',
  DEPTMANAGER	varchar(50)	NOT NULL DEFAULT '',
  DESCRIPTION	varchar(64)	NOT NULL DEFAULT '',
  ORDERNUM	int		NOT NULL DEFAULT 0,
  URL		varchar(255)	NOT NULL ,
  INTIME	timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_department                                     is 'urp部门';
comment on column uweb.t_urp_department.ID                                  is '部门ID';
comment on column uweb.t_urp_department.DEPTNAME                            is '部门名';
comment on column uweb.t_urp_department.DN                                  is '分布式数据名称 o=联动优势,o=科技公司';
comment on column uweb.t_urp_department.ALIASNAME                           is '别名';
comment on column uweb.t_urp_department.PARENTID                            is '上级id';
comment on column uweb.t_urp_department.POSTAL				    is '邮编';
comment on column uweb.t_urp_department.DEPTPHONE                           is '部门电话';
comment on column uweb.t_urp_department.DEPTFAX                             is '部门传真';
comment on column uweb.t_urp_department.ADDRESS                             is '地址';
comment on column uweb.t_urp_department.DEPTLEADER                          is '部门领导';
comment on column uweb.t_urp_department.DEPTMANAGER                         is '部门经理';
comment on column uweb.t_urp_department.DESCRIPTION                         is '描述';
comment on column uweb.t_urp_department.ORDERNUM                            is '排序号';
comment on column uweb.t_urp_department.URL                                 is 'URL路径';
comment on column uweb.t_urp_department.INTIME                              is '添加时间';


drop table uweb.t_urp_dic;
CREATE TABLE uweb.t_urp_dic (
  ID		char(64)	NOT NULL,
  DICKEY	varchar(64)	NOT NULL DEFAULT '',
  DICVALUE	varchar(64)	NOT NULL DEFAULT '',
  CATEID	char(64)	NOT NULL DEFAULT '',
  INTIME	timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_dic                                     is '数据字典';
comment on column uweb.t_urp_dic.ID                                  is 'id';
comment on column uweb.t_urp_dic.DICKEY                              is '数据字典key';
comment on column uweb.t_urp_dic.DICVALUE                            is '数据字典值';
comment on column uweb.t_urp_dic.CATEID                              is '分类id';
comment on column uweb.t_urp_dic.INTIME                              is '添加时间';


drop table uweb.t_urp_dic_category;
CREATE TABLE uweb.t_urp_dic_category (
  ID		char(64)	NOT NULL,
  CODE		varchar(40)	NOT NULL DEFAULT '',
  CATENAME	varchar(64)	NOT NULL DEFAULT '',
  INTIME	timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_dic_category                                 is 'urp分类';
comment on column uweb.t_urp_dic_category.ID                              is '分类id';
comment on column uweb.t_urp_dic_category.CODE                            is '用户自定义的唯一编码';
comment on column uweb.t_urp_dic_category.CATENAME                        is '分类名称';
comment on column uweb.t_urp_dic_category.INTIME			  is '添加时间';

drop table uweb.t_urp_function;
CREATE TABLE uweb.t_urp_function (
  ID			char(64)	NOT NULL,
  FUNCTIONNAME		varchar(64)	NOT NULL DEFAULT '',
  DN			varchar(500)	NOT NULL,
  LINK			varchar(255)	NOT NULL,
  TARGET		varchar(20)	NOT NULL DEFAULT '',
  PARENTID		char(64)	NOT NULL DEFAULT '',
  ORDERNUM		int		NOT NULL DEFAULT 0,
  TITLE			varchar(64)	NOT NULL DEFAULT '',
  DESCRIPTION		varchar(64)	NOT NULL DEFAULT '',
  DEFAULTURL		varchar(255)	NOT NULL ,
  INTIME		timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_function                                 is 'urp菜单';
comment on column uweb.t_urp_function.ID                              is '菜单id';
comment on column uweb.t_urp_function.FUNCTIONNAME                    is '菜单名称';
comment on column uweb.t_urp_function.DN                              is '分布式数据名称 o=联动优势,o=科技公司';
comment on column uweb.t_urp_function.LINK                            is '功能链接';
comment on column uweb.t_urp_function.TARGET                          is '跳转方式';
comment on column uweb.t_urp_function.PARENTID                        is '父节点';
comment on column uweb.t_urp_function.ORDERNUM                        is '排序号';
comment on column uweb.t_urp_function.TITLE                           is '标题';
comment on column uweb.t_urp_function.DESCRIPTION                     is '描述';
comment on column uweb.t_urp_function.DEFAULTURL                      is '默认路径';
comment on column uweb.t_urp_function.INTIME                          is '添加时间';


drop table uweb.t_urp_group;
CREATE TABLE uweb.t_urp_group (
  ID		char(64)	NOT NULL,
  GROUPNAME	varchar(64)	NOT NULL DEFAULT '',
  DN		varchar(500)	NOT NULL ,
  ORDERNUM	int		NOT NULL DEFAULT 0,
  DESCRIPTION	varchar(64)	NOT NULL DEFAULT '',
  DEPARTMENTID	char(64)	NOT NULL DEFAULT '',
  URL		varchar(255)	NOT NULL ,
  INTIME	timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_group                                         is 'urp组';
comment on column uweb.t_urp_group.ID                                      is '组id';
comment on column uweb.t_urp_group.GROUPNAME                               is '名称';
comment on column uweb.t_urp_group.DN                                      is '分布式数据名称 o=联动优势,o=科技公司';
comment on column uweb.t_urp_group.DESCRIPTION                             is '描述';
comment on column uweb.t_urp_group.ORDERNUM                                is '排序号';
comment on column uweb.t_urp_group.DEPARTMENTID                            is '描述id';
comment on column uweb.t_urp_group.URL                                     is '路径';
comment on column uweb.t_urp_group.INTIME                                  is '添加时间';


drop table uweb.t_urp_group_user;
CREATE TABLE uweb.t_urp_group_user (
  ID		char(64)	NOT NULL,
  GROUPID	char(64)	NOT NULL DEFAULT '',
  USERID	char(64)	NOT NULL DEFAULT '',
  ORDERNUM	int		NOT NULL DEFAULT 0,
  INTIME	timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_group_user                                 is 'urp组用户';
comment on column uweb.t_urp_group_user.ID                              is '组用户id';
comment on column uweb.t_urp_group_user.GROUPID                         is '组id';
comment on column uweb.t_urp_group_user.USERID                          is '用户id';
comment on column uweb.t_urp_group_user.ORDERNUM                        is '排序号';
comment on column uweb.t_urp_group_user.INTIME                          is '添加时间';


drop table uweb.t_urp_layout;
CREATE TABLE uweb.t_urp_layout (
  ID			char(64)	NOT NULL,
  LAYOUTNAME		varchar(64)	NOT NULL DEFAULT '',
  URL			varchar(255)	NOT NULL ,
  DESCRIPTION		varchar(64)	NOT NULL DEFAULT '',
  ORDERNUM		int		NOT NULL DEFAULT 0,
  STATUS		char(2)		NOT NULL DEFAULT '1',
  RENDERPROVIDER	varchar(100)	NOT NULL ,
  INTIME		timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_layout                                 is 'urp布局';
comment on column uweb.t_urp_layout.ID                              is '布局id';
comment on column uweb.t_urp_layout.LAYOUTNAME                      is '布局名称';
comment on column uweb.t_urp_layout.URL                             is '路径';
comment on column uweb.t_urp_layout.DESCRIPTION                     is '描述';
comment on column uweb.t_urp_layout.ORDERNUM                        is '排序号';
comment on column uweb.t_urp_layout.STATUS                          is '状态 1为启用，0为禁用';
comment on column uweb.t_urp_layout.RENDERPROVIDER                  is '渲染提供';
comment on column uweb.t_urp_layout.INTIME                          is '添加时间';


drop table uweb.t_urp_orgnization;
CREATE TABLE uweb.t_urp_orgnization (
  ID		char(64)	NOT NULL,
  ORGNAME	varchar(64)	NOT NULL DEFAULT '',
  TITLE		varchar(64)	NOT NULL DEFAULT '',
  DN		varchar(500)	NOT NULL ,
  PARENTID	char(64)	NOT NULL DEFAULT '',
  ORGCODE	varchar(20)	NOT NULL DEFAULT '',
  DESCRIPTION	varchar(64)	NOT NULL DEFAULT '',
  ORDERNUM	int		NOT NULL DEFAULT 0,
  phone		char(12)	NOT NULL DEFAULT '',
  fax		char(12)	NOT NULL DEFAULT '',
  address	varchar(32)	NOT NULL DEFAULT '',
  url		varchar(255)	NOT NULL ,
  INTIME	timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_orgnization                       is 'urp组织表';
comment on column uweb.t_urp_orgnization.ID                    is 'id';
comment on column uweb.t_urp_orgnization.ORGNAME               is '名称';
comment on column uweb.t_urp_orgnization.TITLE                 is '标题';
comment on column uweb.t_urp_orgnization.DN                    is '分布式数据名称 o=联动优势,o=科技公司';
comment on column uweb.t_urp_orgnization.PARENTID              is '上级标识';
comment on column uweb.t_urp_orgnization.ORGCODE               is '组织代码';
comment on column uweb.t_urp_orgnization.DESCRIPTION           is '描述';
comment on column uweb.t_urp_orgnization.ORDERNUM              is '排序号';
comment on column uweb.t_urp_orgnization.phone                 is '电话';
comment on column uweb.t_urp_orgnization.fax                   is '传真';
comment on column uweb.t_urp_orgnization.address               is '地址';
comment on column uweb.t_urp_orgnization.url                   is '路径';
comment on column uweb.t_urp_orgnization.INTIME                is '添加时间';


drop table uweb.t_urp_permission;
CREATE TABLE uweb.t_urp_permission (
  ID		char(64)	NOT NULL,
  RESOURCEID	char(64)	NOT NULL DEFAULT '',
  DESTID	char(64)	NOT NULL DEFAULT '',
  DESTTYPE	varchar(20)	NOT NULL DEFAULT '',
  ORDERNUM	int		NOT NULL DEFAULT 0,
  OPERATION	varchar(40)	NOT NULL DEFAULT '',
  ISINHERIT	smallint	NOT NULL DEFAULT 0,
  INTIME	timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_permission                      is 'urp权限表';
comment on column uweb.t_urp_permission.ID                   is 'id';
comment on column uweb.t_urp_permission.RESOURCEID           is '资源id';
comment on column uweb.t_urp_permission.DESTID               is '目标id';
comment on column uweb.t_urp_permission.DESTTYPE             is '目标类型 o组织,ou部门,user用户,role角色';
comment on column uweb.t_urp_permission.ORDERNUM             is '排序号';
comment on column uweb.t_urp_permission.OPERATION            is '操作';
comment on column uweb.t_urp_permission.ISINHERIT            is '是否继承 0：不继承 1：继承';
comment on column uweb.t_urp_permission.INTIME               is '添加时间';


drop table uweb.t_urp_permfilter;
CREATE TABLE uweb.t_urp_permfilter (
  ID		char(64)	NOT NULL,
  RESOURCEID	char(64)	NOT NULL DEFAULT '',
  ORDERNUM	int		NOT NULL DEFAULT 0,
  OPERATION	varchar(40)	NOT NULL DEFAULT '',
  INTIME	timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_permfilter                             is '权限过滤';
comment on column uweb.t_urp_permfilter.ID                          is 'id';
comment on column uweb.t_urp_permfilter.RESOURCEID                  is '资源号';
comment on column uweb.t_urp_permfilter.ORDERNUM                    is '排序号';
comment on column uweb.t_urp_permfilter.OPERATION                   is '操作';
comment on column uweb.t_urp_permfilter.INTIME                      is '添加时间';


drop table uweb.t_urp_position;
CREATE TABLE uweb.t_urp_position (
  ID			char(64)	NOT NULL,
  POSITIONNAME		varchar(64)	NOT NULL DEFAULT '',
  DN			varchar(500)	NOT NULL ,
  DESCRIPTION		varchar(64)	NOT NULL DEFAULT '',
  ORDERNUM		int		NOT NULL DEFAULT 0,
  DEPARTMENTID		char(64)	NOT NULL DEFAULT '',
  URL			varchar(255)	NOT NULL ,
  INTIME		timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_position                       is '岗位';
comment on column uweb.t_urp_position.ID                    is '岗位号';
comment on column uweb.t_urp_position.POSITIONNAME          is '岗位名称';
comment on column uweb.t_urp_position.DN                    is '分布式数据名称 o=联动优势,o=科技公司';
comment on column uweb.t_urp_position.DESCRIPTION           is '描述';
comment on column uweb.t_urp_position.ORDERNUM              is '排序号';
comment on column uweb.t_urp_position.DEPARTMENTID          is '部门id';
comment on column uweb.t_urp_position.URL                   is '路径';
comment on column uweb.t_urp_position.INTIME                is '添加时间';


drop table uweb.t_urp_position_user;
CREATE TABLE uweb.t_urp_position_user (
  ID		char(64)	NOT NULL,
  POSITIONID	char(64)	NOT NULL DEFAULT '',
  USERID	char(64)	NOT NULL DEFAULT '',
  ORDERNUM	int		NOT NULL DEFAULT 0,
  INTIME	timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_position_user                          is '岗位用户';
comment on column uweb.t_urp_position_user.ID                       is 'id';
comment on column uweb.t_urp_position_user.POSITIONID               is '岗位id';
comment on column uweb.t_urp_position_user.USERID                   is '用户id';
comment on column uweb.t_urp_position_user.ORDERNUM                 is '排序号';
comment on column uweb.t_urp_position_user.INTIME                   is '添加时间';


drop table uweb.t_urp_profile;
CREATE TABLE uweb.t_urp_profile (
  ID		char(64)	NOT NULL,
  PROFILENAME	varchar(64)	NOT NULL DEFAULT '',
  PATH		varchar(255)	NOT NULL ,
  DESCRIPTION	varchar(64)	NOT NULL DEFAULT '',
  STATUS	char(2)		NOT NULL DEFAULT '1',
  ORDERNUM	int		NOT NULL DEFAULT 0,
  INTIME	timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_profile                                       is '配置';
comment on column uweb.t_urp_profile.ID                                    is 'id';
comment on column uweb.t_urp_profile.PROFILENAME                           is '名称';
comment on column uweb.t_urp_profile.PATH                                  is '文件路径';
comment on column uweb.t_urp_profile.DESCRIPTION                           is '描述';
comment on column uweb.t_urp_profile.STATUS                                is '状态 1为启用，0为禁用';
comment on column uweb.t_urp_profile.ORDERNUM                              is '排序号';
comment on column uweb.t_urp_profile.INTIME                                is '添加时间';


drop table uweb.t_urp_resource;
CREATE TABLE uweb.t_urp_resource (
  ID		char(64)	NOT NULL,
  RESNAME	varchar(64)	NOT NULL DEFAULT '',
  DN		varchar(500)	NOT NULL ,
  RESTYPE	varchar(64)	NOT NULL DEFAULT '',
  TITLE		varchar(64)	NOT NULL DEFAULT '',
  PARENTID	char(64)	NOT NULL DEFAULT '',
  ORDERNUM	int		NOT NULL DEFAULT 0,
  ISLEAF	smallint	NOT NULL DEFAULT 0,
  CODE		varchar(50)	NOT NULL DEFAULT '',
  INTIME	timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_resource                     is 'urp资源表';
comment on column uweb.t_urp_resource.ID                  is 'id';
comment on column uweb.t_urp_resource.RESNAME             is '资源名称';
comment on column uweb.t_urp_resource.DN                  is '分布式数据名称 o=联动优势,o=科技公司';
comment on column uweb.t_urp_resource.RESTYPE             is '资源类型 o组织,ou部门,user用户,func菜单,app应用,role角色';
comment on column uweb.t_urp_resource.TITLE               is '标题';
comment on column uweb.t_urp_resource.PARENTID            is '上级标识';
comment on column uweb.t_urp_resource.ORDERNUM            is '排序号';
comment on column uweb.t_urp_resource.ISLEAF              is '是否是叶节点 0:是 1:不是';
comment on column uweb.t_urp_resource.CODE                is '用户自定义唯一的编号';
comment on column uweb.t_urp_resource.INTIME              is '添加时间';


drop table uweb.t_urp_resource_permission;
CREATE TABLE uweb.t_urp_resource_permission (
  ID			char(64)	NOT NULL,
  RESID			char(64)	NOT NULL DEFAULT '',
  RESNAME		varchar(64)	NOT NULL DEFAULT '',
  RESTYPE		varchar(20)	NOT NULL DEFAULT '',
  USERID		char(64)	NOT NULL DEFAULT '',
  USERNAME		varchar(200)	NOT NULL DEFAULT '',
  INHERITFROM		char(64)	NOT NULL DEFAULT '',
  INHERITFROMTYPE	varchar(20)	NOT NULL DEFAULT '',
  OPERATION		varchar(64)	NOT NULL DEFAULT '',
  ORDERNUM		int		NOT NULL DEFAULT 0,
  INTIME		timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_resource_permission                      is '资源权限';
comment on column uweb.t_urp_resource_permission.ID                   is 'id';
comment on column uweb.t_urp_resource_permission.RESID                is '资源id';
comment on column uweb.t_urp_resource_permission.RESNAME              is '资源名称';
comment on column uweb.t_urp_resource_permission.RESTYPE              is '资源类型 o组织,ou部门,user用户,func菜单,app应用,role角色';
comment on column uweb.t_urp_resource_permission.USERID               is '用户id';
comment on column uweb.t_urp_resource_permission.USERNAME             is '用户名称';
comment on column uweb.t_urp_resource_permission.INHERITFROM          is '继承自';
comment on column uweb.t_urp_resource_permission.INHERITFROMTYPE      is '继承自类型user用户,role角色';
comment on column uweb.t_urp_resource_permission.OPERATION            is '操作';
comment on column uweb.t_urp_resource_permission.ORDERNUM             is '排序号';
comment on column uweb.t_urp_resource_permission.INTIME               is '添加时间';




drop table uweb.t_urp_resproperty;
CREATE TABLE uweb.t_urp_resproperty (
  ID		char(64)	NOT NULL,
  RESID		char(64)	NOT NULL DEFAULT '',
  PROPKEY	varchar(64)	NOT NULL DEFAULT '',
  PROPVALUE	varchar(64)	NOT NULL DEFAULT '',
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_resproperty                   is '资源属性表';
comment on column uweb.t_urp_resproperty.ID                is 'id';
comment on column uweb.t_urp_resproperty.RESID             is '资源id';
comment on column uweb.t_urp_resproperty.PROPKEY           is '属性key';
comment on column uweb.t_urp_resproperty.PROPVALUE         is '属性值';

drop table uweb.t_urp_role;
CREATE TABLE uweb.t_urp_role (
  ID		char(64)	NOT NULL,
  ROLENAME	varchar(64)	NOT NULL DEFAULT '',
  DN		varchar(500)	NOT NULL ,
  DESCRIPTION	varchar(64)	NOT NULL DEFAULT '',
  ORDERNUM	int		NOT NULL DEFAULT 0,
  DEPARTMENTID	char(64)	NOT NULL DEFAULT '',
  URL		varchar(255)	NOT NULL ,
  INTIME	timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_role                           is 'urp角色';
comment on column uweb.t_urp_role.ID                        is 'id';
comment on column uweb.t_urp_role.ROLENAME                  is '名称';
comment on column uweb.t_urp_role.DN                        is '分布式数据名称 o=联动优势,o=科技公司';
comment on column uweb.t_urp_role.DESCRIPTION               is '描述';
comment on column uweb.t_urp_role.ORDERNUM                  is '排序号';
comment on column uweb.t_urp_role.DEPARTMENTID              is '部门号';
comment on column uweb.t_urp_role.URL                       is '路径';
comment on column uweb.t_urp_role.INTIME                    is '添加时间';



drop table uweb.t_urp_role_user;
CREATE TABLE uweb.t_urp_role_user (
  ID		char(64)	NOT NULL,
  ROLEID	char(64)	NOT NULL DEFAULT '',
  USERID	char(64)	NOT NULL DEFAULT '',
  ORDERNUM	int		NOT NULL DEFAULT 0,
  INTIME	timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);


comment on table  uweb.t_urp_role_user                     is '角色用户关联';
comment on column uweb.t_urp_role_user.ID                  is 'id';
comment on column uweb.t_urp_role_user.ROLEID              is '角色id';
comment on column uweb.t_urp_role_user.USERID              is '用户号';
comment on column uweb.t_urp_role_user.ORDERNUM            is '排序号';
comment on column uweb.t_urp_role_user.INTIME              is '添加时间';


drop table uweb.t_urp_user;
CREATE TABLE uweb.t_urp_user (
  ID		char(64)	NOT NULL,
  LOGINNAME	varchar(30)	NOT NULL DEFAULT '',
  USERNAME	varchar(20)	NOT NULL DEFAULT '',
  PASSWORD	varchar(50)	NOT NULL DEFAULT '',
  DN		varchar(500)	NOT NULL ,
  OFFICIAL	smallint	NOT NULL DEFAULT 0,
  DUTYLEVEL	varchar(38)	NOT NULL DEFAULT '',
  DUTY		varchar(38)	NOT NULL DEFAULT '',
  EMAIL		varchar(50)	NOT NULL DEFAULT '',
  GENDER	smallint	NOT NULL DEFAULT 0,
  HOMETOWN	varchar(38)	NOT NULL DEFAULT '',
  OFFICEADDRESS varchar(50)	NOT NULL DEFAULT '',
  OFFICEPHONE	varchar(12)	NOT NULL DEFAULT '',
  OFFICEFAX	varchar(12)	NOT NULL DEFAULT '',
  HOMEPHONE	varchar(12)	NOT NULL DEFAULT '',
  HOMEADDRESS	varchar(50)	NOT NULL DEFAULT '',
  MOBILEID	varchar(12)	NOT NULL DEFAULT '',
  ORDERNUM	int		NOT NULL DEFAULT 0,
  DEPARTMENTID	char(64)	NOT NULL DEFAULT '',
  BIRTHDAY	varchar(20)	NOT NULL DEFAULT '',
  SPASSWORD	varchar(40)	NOT NULL DEFAULT '',
  INTIME	timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
); 

comment on table  uweb.t_urp_user                               is 'urp用户';
comment on column uweb.t_urp_user.ID                            is '用户id';
comment on column uweb.t_urp_user.LOGINNAME                     is '登陆名';
comment on column uweb.t_urp_user.USERNAME                      is '姓名';
comment on column uweb.t_urp_user.PASSWORD                      is '密码';
comment on column uweb.t_urp_user.DN                            is '分布式数据名称';
comment on column uweb.t_urp_user.OFFICIAL                      is '是否在职 0:不在职 1:在职';
comment on column uweb.t_urp_user.DUTYLEVEL                     is '职务等级 ';
comment on column uweb.t_urp_user.DUTY                          is '职务';
comment on column uweb.t_urp_user.EMAIL                         is 'email';
comment on column uweb.t_urp_user.GENDER                        is '性别 0-未知,1-女,2-男';
comment on column uweb.t_urp_user.HOMETOWN                      is '家庭住址';
comment on column uweb.t_urp_user.OFFICEADDRESS                 is '办公地址';
comment on column uweb.t_urp_user.OFFICEPHONE                   is '办公电话';
comment on column uweb.t_urp_user.OFFICEFAX                     is '办公传真';
comment on column uweb.t_urp_user.HOMEPHONE                     is '家庭电话';
comment on column uweb.t_urp_user.HOMEADDRESS                   is '家庭地址';
comment on column uweb.t_urp_user.MOBILEID                      is '移动电话';
comment on column uweb.t_urp_user.ORDERNUM                      is '排序号';
comment on column uweb.t_urp_user.DEPARTMENTID                  is '部门id';
comment on column uweb.t_urp_user.BIRTHDAY                      is '生日 2012-09-01';
comment on column uweb.t_urp_user.SPASSWORD                     is '加密口令';
comment on column uweb.t_urp_user.INTIME                        is '添加时间';


drop table uweb.t_urp_application;
CREATE TABLE uweb.t_urp_application (
  ID			char(64)	NOT NULL,
  APPNAME		varchar(64)	NOT NULL DEFAULT '',
  DN			varchar(500)	NOT NULL ,
  PARENTID		char(64)	NOT NULL DEFAULT '',
  TITLE			varchar(64)	NOT NULL DEFAULT '',
  DESCRIPTION		varchar(64)	NOT NULL DEFAULT '',
  ORDERNUM		int		NOT NULL DEFAULT 0,
  WELCOMEURL		varchar(255)	NOT NULL ,
  EXTENDOPERATIONS	varchar(64)	NOT NULL DEFAULT '',
  appcontextpath	varchar(64)	NOT NULL DEFAULT '',
  ICONPATH		varchar(80)	NOT NULL WITH DEFAULT '',
  inTime		timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (ID)
);

comment on table  uweb.t_urp_application                              is 'urp应用';
comment on column uweb.t_urp_application.ID                           is '应用id';
comment on column uweb.t_urp_application.APPNAME                      is '应用名';
comment on column uweb.t_urp_application.DN                           is '分布式数据名称';
comment on column uweb.t_urp_application.PARENTID                     is '父ID';
comment on column uweb.t_urp_application.TITLE                        is '应用标题';
comment on column uweb.t_urp_application.DESCRIPTION                  is '应用描述';
comment on column uweb.t_urp_application.ORDERNUM                     is '排序号';
comment on column uweb.t_urp_application.WELCOMEURL                   is '欢迎路径';
comment on column uweb.t_urp_application.EXTENDOPERATIONS             is '扩展操作 ';
comment on column uweb.t_urp_application.appcontextpath               is '应用路径';
comment on column uweb.t_urp_application.ICONPATH		      is '图标路径';
comment on column uweb.t_urp_application.inTime                       is '应用添加时间';


drop table uweb.t_urp_org_admin;
CREATE TABLE uweb.t_urp_org_admin (
  id		char(64)	NOT NULL,
  resourceDn	varchar(500)	NOT NULL,
  adminId	char(64)	NOT NULL DEFAULT '',
  adminType	varchar(20)	NOT NULL DEFAULT '',
  orderNum	int		NOT NULL DEFAULT 0,
  inTime	timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (id)
);

comment on table  uweb.t_urp_org_admin                                 is 'urp组织管理员';
comment on column uweb.t_urp_org_admin.id                              is 'id';
comment on column uweb.t_urp_org_admin.resourceDn                      is '资源分布式名称';
comment on column uweb.t_urp_org_admin.adminId                         is '管理员ID';
comment on column uweb.t_urp_org_admin.adminType                       is '管理类型 user用户 role角色';
comment on column uweb.t_urp_org_admin.orderNum                        is '排序号';
comment on column uweb.t_urp_org_admin.inTime                          is '添加时间';


drop table uweb.t_urp_acl_url;
create table uweb.t_urp_acl_url(
  id		char(64)	NOT NULL,
  aclname	varchar(40)	NOT NULL DEFAULT '',
  urlpattern	varchar(255)	NOT NULL ,
  appid		varchar(64)	NOT NULL DEFAULT '',
  description	varchar(64)	NOT NULL DEFAULT '',
  inTime	timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
  PRIMARY KEY (id)
);

comment on table  uweb.t_urp_acl_url                                   is 'urp访问控制';
comment on column uweb.t_urp_acl_url.id                                is 'id';
comment on column uweb.t_urp_acl_url.aclname                           is '操作名称';
comment on column uweb.t_urp_acl_url.urlpattern                        is 'url';
comment on column uweb.t_urp_acl_url.appid                             is '应用id';
comment on column uweb.t_urp_acl_url.description                       is 'url描述';
comment on column uweb.t_urp_acl_url.inTime                            is '添加时间';

drop table uweb.t_urp_acl_url_role;
create table uweb.t_urp_acl_url_role(
   id		char(64)	NOT NULL,
   acl_id	char(64)	NOT NULL DEFAULT '',
   role_id	char(64)	NOT NULL DEFAULT '',
   INTIME	timestamp	NOT NULL DEFAULT CURRENT TIMESTAMP,
   PRIMARY KEY (id) 
);

comment on table  uweb.t_urp_acl_url_role                              is 'urp访问角色关联';
comment on column uweb.t_urp_acl_url_role.id                           is 'id';
comment on column uweb.t_urp_acl_url_role.acl_id                       is 'aclid';
comment on column uweb.t_urp_acl_url_role.role_id                      is '角色id';
comment on column uweb.t_urp_acl_url_role.inTime                       is '添加时间';


grant select,insert,update,delete on table uweb.t_urp_group_user	  to user uwebmngb;       
grant select,insert,update,delete on table uweb.t_urp_group               to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_function            to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_dic_category        to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_dic                 to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_department          to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_position_user       to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_position            to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_permfilter          to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_permission          to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_orgnization         to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_layout              to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_role_user           to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_role                to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_resproperty         to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_resource_permission to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_resource            to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_profile             to user uwebmngb;  
grant select,insert,update,delete on table uweb.t_urp_org_admin           to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_application         to user uwebmngb; 
grant select,insert,update,delete on table uweb.t_urp_user                to user uwebmngb;
grant select,insert,update,delete on table uweb.t_urp_acl_url             to user uwebmngb;
grant select,insert,update,delete on table uweb.t_urp_acl_url_role        to user uwebmngb;