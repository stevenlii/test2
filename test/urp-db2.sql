--jdbc:db2://10.10.38.138:50000/malltest
--umallmag/umallmag
drop table urp.t_mer_inf;
CREATE TABLE urp.t_mer_inf (
  ID varchar(38) NOT NULL,
  MERNAME varchar(200) DEFAULT NULL,
  MERTYPE varchar(4) DEFAULT NULL,
  STATE smallint DEFAULT NULL,
  POSTAL varchar(6) DEFAULT NULL,
  ADDRESS varchar(128) DEFAULT NULL,
  MODUSER varchar(32) DEFAULT NULL,
  MODTIME timestamp DEFAULT NULL,
  INTIME timestamp DEFAULT NULL,
  TIMELTD int DEFAULT NULL,
  ISSPECIAL smallint DEFAULT NULL,
  IFTRUST smallint DEFAULT NULL,
  CUSPHONE varchar(20) DEFAULT NULL,
  MERACCOUNTID varchar(38) DEFAULT NULL,
  MERID varchar(20) DEFAULT NULL,
  EXMERID varchar(20) DEFAULT NULL,
  AUDITTYPE smallint DEFAULT NULL,
  AUDITSTATE smallint DEFAULT NULL,
  PRIMARY KEY (ID)
);




drop table urp.urp_department;
CREATE TABLE urp.urp_department (
  ID varchar(38) NOT NULL,
  NAME varchar(400) DEFAULT NULL,
  DN varchar(500) DEFAULT NULL,
  ALIASNAME varchar(255) DEFAULT NULL,
  PARENTID varchar(38) DEFAULT NULL,
  ZIPCODE varchar(7) DEFAULT NULL,
  DEPTPHONE varchar(12) DEFAULT NULL,
  DEPTFAX varchar(12) DEFAULT NULL,
  ADDRESS varchar(50) DEFAULT NULL,
  DEPTLEADER varchar(50) DEFAULT NULL,
  DEPTMANAGER varchar(50) DEFAULT NULL,
  DESCRIPTION varchar(255) DEFAULT NULL,
  ORDERNUM int DEFAULT 0,
  ADDTIME timestamp DEFAULT NULL,
  URL varchar(400) DEFAULT NULL,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_department                                      is 'urp部门';
comment on column urp.urp_department.ID                                  is '部门号';
comment on column urp.urp_department.NAME                                is '部门名';
comment on column urp.urp_department.DN                                  is 'DN';
comment on column urp.urp_department.ALIASNAME                           is '别名';
comment on column urp.urp_department.PARENTID                            is '上级id';
comment on column urp.urp_department.ZIPCODE                             is '邮编';
comment on column urp.urp_department.DEPTPHONE                           is '部门电话';
comment on column urp.urp_department.DEPTFAX                             is '部门传真';
comment on column urp.urp_department.ADDRESS                             is '地址';
comment on column urp.urp_department.DEPTLEADER                          is '部门领导';
comment on column urp.urp_department.DEPTMANAGER                         is '部门经理';
comment on column urp.urp_department.DESCRIPTION                         is '描述';
comment on column urp.urp_department.ORDERNUM                            is '排序号';
comment on column urp.urp_department.ADDTIME                             is '添加时间';
comment on column urp.urp_department.URL                                 is 'URL路径';


drop table urp.urp_dic;
CREATE TABLE urp.urp_dic (
  ID varchar(38) NOT NULL,
  DICKEY varchar(100) DEFAULT NULL,
  DICVALUE varchar(100) DEFAULT NULL,
  ADDTIME timestamp DEFAULT NULL,
  CATEID varchar(38) DEFAULT NULL,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_dic                                      is '数据字典';
comment on column urp.urp_dic.ID                                  is 'id';
comment on column urp.urp_dic.DICKEY                              is '数据字典key';
comment on column urp.urp_dic.DICVALUE                            is '数据字典值';
comment on column urp.urp_dic.ADDTIME                             is '添加时间';
comment on column urp.urp_dic.CATEID                              is '种类id';


drop table urp.urp_dic_category;
CREATE TABLE urp.urp_dic_category (
  ID varchar(38) NOT NULL,
  CODE varchar(40) DEFAULT NULL,
  NAME varchar(200) DEFAULT NULL,
  ADDTIME timestamp DEFAULT NULL,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_dic_category                                  is 'urp分类';
comment on column urp.urp_dic_category.ID                              is '分类id';
comment on column urp.urp_dic_category.CODE                            is '分类代码';
comment on column urp.urp_dic_category.NAME                            is '名称';
comment on column urp.urp_dic_category.ADDTIME                         is '添加时间';

drop table urp.urp_function;
CREATE TABLE urp.urp_function (
  ID varchar(38) NOT NULL,
  NAME varchar(200) DEFAULT NULL,
  DN varchar(400) DEFAULT NULL,
  LINK varchar(200) DEFAULT NULL,
  TARGET varchar(20) DEFAULT NULL,
  ADDTIME timestamp DEFAULT NULL,
  PARENTID varchar(38) DEFAULT NULL,
  ORDERNUM int DEFAULT NULL,
  TITLE varchar(400) DEFAULT NULL,
  DESCRIPTION varchar(255) DEFAULT NULL,
  DEFAULTURL varchar(400) DEFAULT NULL,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_function                                  is 'urp功能';
comment on column urp.urp_function.ID                              is '功能id';
comment on column urp.urp_function.NAME                            is '名称';
comment on column urp.urp_function.DN                              is 'DN';
comment on column urp.urp_function.LINK                            is '功能链接';
comment on column urp.urp_function.TARGET                          is '目标';
comment on column urp.urp_function.ADDTIME                         is '添加时间';
comment on column urp.urp_function.PARENTID                        is '父节点';
comment on column urp.urp_function.ORDERNUM                        is '排序号';
comment on column urp.urp_function.TITLE                           is '标题';
comment on column urp.urp_function.DESCRIPTION                     is '描述';
comment on column urp.urp_function.DEFAULTURL                      is '默认路径';


drop table urp.urp_group;
CREATE TABLE urp.urp_group (
  ID varchar(38) NOT NULL,
  NAME varchar(255) DEFAULT NULL,
  DN varchar(500) DEFAULT NULL,
  DESCRIPTION varchar(255) DEFAULT NULL,
  ORDERNUM int DEFAULT 0,
  ADDTIME timestamp DEFAULT NULL,
  DEPARTMENTID varchar(38) DEFAULT NULL,
  URL varchar(400) DEFAULT NULL,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_group                                          is 'urp组';
comment on column urp.urp_group.ID                                      is '组id';
comment on column urp.urp_group.NAME                                    is '名称';
comment on column urp.urp_group.DN                                      is 'DN';
comment on column urp.urp_group.DESCRIPTION                             is '描述';
comment on column urp.urp_group.ORDERNUM                                is '排序号';
comment on column urp.urp_group.ADDTIME                                 is '添加时间';
comment on column urp.urp_group.DEPARTMENTID                            is '描述id';
comment on column urp.urp_group.URL                                     is '路径';


drop table urp.urp_group_user;
CREATE TABLE urp.urp_group_user (
  ID varchar(38) NOT NULL,
  GROUPID varchar(38) DEFAULT NULL,
  USERID varchar(38) DEFAULT NULL,
  ADDTIME timestamp DEFAULT NULL,
  ORDERNUM int DEFAULT NULL,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_group_user                                  is 'urp组用户';
comment on column urp.urp_group_user.ID                              is '组用户id';
comment on column urp.urp_group_user.GROUPID                         is '组id';
comment on column urp.urp_group_user.USERID                          is '用户id';
comment on column urp.urp_group_user.ADDTIME                         is '添加时间';
comment on column urp.urp_group_user.ORDERNUM                        is '排序号';


drop table urp.urp_layout;
CREATE TABLE urp.urp_layout (
  ID varchar(38) NOT NULL,
  NAME varchar(400) DEFAULT NULL,
  URL varchar(400) DEFAULT NULL,
  ADDTIME timestamp DEFAULT NULL,
  DESCRIPTION varchar(255) DEFAULT NULL,
  ORDERNUM int DEFAULT 0,
  STATUS varchar(20) DEFAULT '1',
  RENDERPROVIDER varchar(200) DEFAULT NULL,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_layout                                  is 'urp布局';
comment on column urp.urp_layout.ID                              is '布局id';
comment on column urp.urp_layout.NAME                            is '名称';
comment on column urp.urp_layout.URL                             is '路径';
comment on column urp.urp_layout.ADDTIME                         is '添加时间';
comment on column urp.urp_layout.DESCRIPTION                     is '描述';
comment on column urp.urp_layout.ORDERNUM                        is '排序号';
comment on column urp.urp_layout.STATUS                          is '状态';
comment on column urp.urp_layout.RENDERPROVIDER                  is '渲染提供';


drop table urp.urp_orgnization;
CREATE TABLE urp.urp_orgnization (
  ID varchar(38) NOT NULL,
  NAME varchar(255) DEFAULT NULL,
  TITLE varchar(255) DEFAULT NULL,
  DN varchar(500) DEFAULT NULL,
  PARENTID varchar(38) DEFAULT NULL,
  ORGCODE varchar(20) DEFAULT NULL,
  DESCRIPTION varchar(255) DEFAULT NULL,
  ORDERNUM int DEFAULT 0,
  ADDTIME timestamp DEFAULT NULL,
  phone varchar(20) DEFAULT NULL,
  fax varchar(20) DEFAULT NULL,
  address varchar(200) DEFAULT NULL,
  url varchar(300) DEFAULT NULL,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_orgnization                        is 'urp组织表';
comment on column urp.urp_orgnization.ID                    is 'id';
comment on column urp.urp_orgnization.NAME                  is '名称';
comment on column urp.urp_orgnization.TITLE                 is '标题';
comment on column urp.urp_orgnization.DN                    is 'DN';
comment on column urp.urp_orgnization.PARENTID              is '上级标识';
comment on column urp.urp_orgnization.ORGCODE               is '组织代码';
comment on column urp.urp_orgnization.DESCRIPTION           is '描述';
comment on column urp.urp_orgnization.ORDERNUM              is '排序号';
comment on column urp.urp_orgnization.ADDTIME               is '添加时间';
comment on column urp.urp_orgnization.phone                 is '电话';
comment on column urp.urp_orgnization.fax                   is '传真';
comment on column urp.urp_orgnization.address               is '地址';
comment on column urp.urp_orgnization.url                   is '路径';


drop table urp.urp_permission;
CREATE TABLE urp.urp_permission (
  ID varchar(38) NOT NULL,
  RESOURCEID varchar(38) DEFAULT NULL,
  DESTID varchar(38) DEFAULT NULL,
  DESTTYPE varchar(20) DEFAULT NULL,
  ADDTIME timestamp DEFAULT NULL,
  ORDERNUM int DEFAULT 0,
  OPERATION varchar(200) DEFAULT NULL,
  ISINHERIT smallint DEFAULT NULL,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_permission                       is 'urp权限表';
comment on column urp.urp_permission.ID                   is 'id';
comment on column urp.urp_permission.RESOURCEID           is '资源id';
comment on column urp.urp_permission.DESTID               is '目标id';
comment on column urp.urp_permission.DESTTYPE             is '描述类型';
comment on column urp.urp_permission.ADDTIME              is '添加时间';
comment on column urp.urp_permission.ORDERNUM             is '排序号';
comment on column urp.urp_permission.OPERATION            is '操作';
comment on column urp.urp_permission.ISINHERIT            is '是否继承 ';


drop table urp.urp_permissionfilter;
CREATE TABLE urp.urp_permissionfilter (
  ID varchar(38) NOT NULL,
  RESOURCEID varchar(38) DEFAULT NULL,
  ADDTIME timestamp DEFAULT NULL,
  ORDERNUM int DEFAULT NULL,
  OPERATION varchar(200) DEFAULT NULL,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_permissionfilter                              is '权限过滤表';
comment on column urp.urp_permissionfilter.ID                          is 'id';
comment on column urp.urp_permissionfilter.RESOURCEID                  is '资源号';
comment on column urp.urp_permissionfilter.ADDTIME                     is '添加时间';
comment on column urp.urp_permissionfilter.ORDERNUM                    is '排序号';
comment on column urp.urp_permissionfilter.OPERATION                   is '操作';


drop table urp.urp_position;
CREATE TABLE urp.urp_position (
  ID varchar(38) NOT NULL,
  NAME varchar(400) DEFAULT NULL,
  DN varchar(500) DEFAULT NULL,
  DESCRIPTION varchar(255) DEFAULT NULL,
  ORDERNUM int DEFAULT NULL,
  ADDTIME timestamp DEFAULT NULL,
  DEPARTMENTID varchar(38) DEFAULT NULL,
  URL varchar(400) DEFAULT NULL,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_position                        is '岗位';
comment on column urp.urp_position.ID                    is '岗位号';
comment on column urp.urp_position.NAME                  is '名称';
comment on column urp.urp_position.DN                    is 'DN';
comment on column urp.urp_position.DESCRIPTION           is '描述';
comment on column urp.urp_position.ORDERNUM              is '排序号';
comment on column urp.urp_position.ADDTIME               is '添加时间';
comment on column urp.urp_position.DEPARTMENTID          is '部门id';
comment on column urp.urp_position.URL                   is '路径';


drop table urp.urp_position_user;
CREATE TABLE urp.urp_position_user (
  ID varchar(38) NOT NULL,
  POSITIONID varchar(38) DEFAULT NULL,
  USERID varchar(38) DEFAULT NULL,
  ADDTIME timestamp DEFAULT NULL,
  ORDERNUM int DEFAULT NULL,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_position_user                           is '岗位用户';
comment on column urp.urp_position_user.ID                       is 'id';
comment on column urp.urp_position_user.POSITIONID               is '岗位id';
comment on column urp.urp_position_user.USERID                   is '用户id';
comment on column urp.urp_position_user.ADDTIME                  is '添加时间';
comment on column urp.urp_position_user.ORDERNUM                 is '排序号';


drop table urp.urp_profile;
CREATE TABLE urp.urp_profile (
  ID varchar(38) NOT NULL,
  NAME varchar(400) DEFAULT NULL,
  PATH varchar(400) DEFAULT NULL,
  ADDTIME timestamp DEFAULT NULL,
  DESCRIPTION varchar(255) DEFAULT NULL,
  STATUS varchar(20) DEFAULT '1',
  ORDERNUM int DEFAULT 0,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_profile                                        is '配置';
comment on column urp.urp_profile.ID                                    is 'id';
comment on column urp.urp_profile.NAME                                  is '名称';
comment on column urp.urp_profile.PATH                                  is '路径';
comment on column urp.urp_profile.ADDTIME                               is '添加时间';
comment on column urp.urp_profile.DESCRIPTION                           is '描述';
comment on column urp.urp_profile.STATUS                                is '状态';
comment on column urp.urp_profile.ORDERNUM                              is '排序号';


drop table urp.urp_resource;
CREATE TABLE urp.urp_resource (
  ID varchar(38) NOT NULL,
  NAME varchar(50) DEFAULT NULL,
  DN varchar(200) DEFAULT NULL,
  RESTYPE varchar(100) DEFAULT NULL,
  TITLE varchar(200) DEFAULT NULL,
  PARENTID varchar(38) DEFAULT NULL,
  ORDERNUM int DEFAULT 0,
  ADDTIME timestamp DEFAULT NULL,
  ISLEAF smallint DEFAULT 0,
  CODE varchar(50) DEFAULT NULL,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_resource                      is 'urp资源表';
comment on column urp.urp_resource.ID                  is 'id';
comment on column urp.urp_resource.NAME                is '名称';
comment on column urp.urp_resource.DN                  is 'DN';
comment on column urp.urp_resource.RESTYPE             is '资源类型';
comment on column urp.urp_resource.TITLE               is '标题';
comment on column urp.urp_resource.PARENTID            is '上级标识';
comment on column urp.urp_resource.ORDERNUM            is '排序号';
comment on column urp.urp_resource.ADDTIME             is '添加时间';
comment on column urp.urp_resource.ISLEAF              is '是否叶节点';
comment on column urp.urp_resource.CODE                is '编号';


drop table urp.urp_resource_permission;
CREATE TABLE urp.urp_resource_permission (
  ID varchar(38) NOT NULL,
  RESID varchar(38) DEFAULT NULL,
  RESNAME varchar(200) DEFAULT NULL,
  RESTYPE varchar(20) DEFAULT NULL,
  USERID varchar(38) DEFAULT NULL,
  USERNAME varchar(200) DEFAULT NULL,
  INHERITFROM varchar(38) DEFAULT NULL,
  INHERITFROMTYPE varchar(20) DEFAULT NULL,
  OPERATION varchar(400) DEFAULT NULL,
  ADDTIME timestamp DEFAULT NULL,
  ORDERNUM int DEFAULT NULL,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_resource_permission                       is '资源权限';
comment on column urp.urp_resource_permission.ID                   is 'id';
comment on column urp.urp_resource_permission.RESID                is '资源id';
comment on column urp.urp_resource_permission.RESNAME              is '资源名称';
comment on column urp.urp_resource_permission.RESTYPE              is '资源类型';
comment on column urp.urp_resource_permission.USERID               is '用户id';
comment on column urp.urp_resource_permission.USERNAME             is '用户名称';
comment on column urp.urp_resource_permission.INHERITFROM          is '继承';
comment on column urp.urp_resource_permission.INHERITFROMTYPE      is '继承类型';
comment on column urp.urp_resource_permission.OPERATION            is '操作';
comment on column urp.urp_resource_permission.ADDTIME              is '添加时间';
comment on column urp.urp_resource_permission.ORDERNUM             is '排序号';




drop table urp.urp_resproperty;
CREATE TABLE urp.urp_resproperty (
  ID varchar(38) NOT NULL,
  RESID varchar(38) DEFAULT NULL,
  PROPKEY varchar(200) DEFAULT NULL,
  PROPVALUE varchar(2000) DEFAULT NULL,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_resproperty                    is '资源属性表';
comment on column urp.urp_resproperty.ID                is 'id';
comment on column urp.urp_resproperty.RESID             is '资源id';
comment on column urp.urp_resproperty.PROPKEY           is '属性key';
comment on column urp.urp_resproperty.PROPVALUE         is '属性值';

drop table urp.urp_role;
CREATE TABLE urp.urp_role (
  ID varchar(38) NOT NULL,
  NAME varchar(400) DEFAULT NULL,
  DN varchar(500) DEFAULT NULL,
  DESCRIPTION varchar(255) DEFAULT NULL,
  ORDERNUM int DEFAULT 0,
  ADDTIME timestamp DEFAULT NULL,
  DEPARTMENTID varchar(38) DEFAULT NULL,
  URL varchar(400) DEFAULT NULL,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_role                            is 'urp角色';
comment on column urp.urp_role.ID                        is 'id';
comment on column urp.urp_role.NAME                      is '名称';
comment on column urp.urp_role.DN                        is 'DN';
comment on column urp.urp_role.DESCRIPTION               is '描述';
comment on column urp.urp_role.ORDERNUM                  is '排序号';
comment on column urp.urp_role.ADDTIME                   is '添加时间';
comment on column urp.urp_role.DEPARTMENTID              is '部门号';
comment on column urp.urp_role.URL                       is '路径';



drop table urp.urp_role_user;
CREATE TABLE urp.urp_role_user (
  ID varchar(38) NOT NULL,
  ROLEID varchar(38) DEFAULT NULL,
  USERID varchar(38) DEFAULT NULL,
  ADDTIME timestamp DEFAULT NULL,
  ORDERNUM int DEFAULT NULL,
  PRIMARY KEY (ID)
);


comment on table  urp.urp_role_user                      is '角色用户关联';
comment on column urp.urp_role_user.ID                  is 'id';
comment on column urp.urp_role_user.ROLEID              is '角色id';
comment on column urp.urp_role_user.USERID              is '用户号';
comment on column urp.urp_role_user.ADDTIME             is '添加时间';
comment on column urp.urp_role_user.ORDERNUM            is '排序号';


drop table urp.urp_user;
CREATE TABLE urp.urp_user (
  ID varchar(38) NOT NULL,
  LOGINNAME varchar(400) DEFAULT NULL,
  NAME varchar(400) DEFAULT NULL,
  PASSWORD varchar(50) DEFAULT NULL,
  DN varchar(500) DEFAULT NULL,
  OFFICIAL smallint DEFAULT NULL,
  DUTYLEVEL varchar(38) DEFAULT NULL,
  DUTY varchar(38) DEFAULT NULL,
  EMAIL varchar(50) DEFAULT NULL,
  GENDER varchar(8) DEFAULT NULL,
  HOMETOWN varchar(38) DEFAULT NULL,
  OFFICEADDRESS varchar(50) DEFAULT NULL,
  OFFICEPHONE varchar(12) DEFAULT NULL,
  OFFICEFAX varchar(12) DEFAULT NULL,
  HOMEPHONE varchar(12) DEFAULT NULL,
  HOMEADDRESS varchar(50) DEFAULT NULL,
  MOBILE varchar(12) DEFAULT '0',
  ORDERNUM int DEFAULT 0,
  ADDTIME timestamp DEFAULT NULL,
  DEPARTMENTID varchar(38) DEFAULT NULL,
  BIRTHDAY varchar(20) DEFAULT NULL,
  SPASSWORD varchar(400) DEFAULT NULL,
  PRIMARY KEY (ID)
); 

comment on table  urp.urp_user                                is 'urp用户';
comment on column urp.urp_user.ID                            is '用户id';
comment on column urp.urp_user.LOGINNAME                     is '登陆名';
comment on column urp.urp_user.NAME                          is '姓名';
comment on column urp.urp_user.PASSWORD                      is '密码';
comment on column urp.urp_user.DN                            is 'DN';
comment on column urp.urp_user.OFFICIAL                      is '公务员';
comment on column urp.urp_user.DUTYLEVEL                     is '职务等级 ';
comment on column urp.urp_user.DUTY                          is '职务';
comment on column urp.urp_user.EMAIL                         is 'email';
comment on column urp.urp_user.GENDER                        is '性别';
comment on column urp.urp_user.HOMETOWN                      is '家庭住址';
comment on column urp.urp_user.OFFICEADDRESS                 is '办公地址';
comment on column urp.urp_user.OFFICEPHONE                   is '办公电话';
comment on column urp.urp_user.OFFICEFAX                     is '办公传真';
comment on column urp.urp_user.HOMEPHONE                     is '家庭电话';
comment on column urp.urp_user.HOMEADDRESS                   is '家庭地址';
comment on column urp.urp_user.MOBILE                        is '移动电话';
comment on column urp.urp_user.ORDERNUM                      is '排序号';
comment on column urp.urp_user.ADDTIME                       is '添加时间';
comment on column urp.urp_user.DEPARTMENTID                  is '部门id';
comment on column urp.urp_user.BIRTHDAY                      is '生日';
comment on column urp.urp_user.SPASSWORD                     is '加密口令';

drop table urp.urp_application;
CREATE TABLE urp.urp_application (
  ID varchar(38) NOT NULL,
  NAME varchar(100) DEFAULT NULL,
  DN varchar(400) DEFAULT NULL,
  PARENTID varchar(38) DEFAULT NULL,
  TITLE varchar(200) DEFAULT NULL,
  ADDTIME timestamp DEFAULT NULL,
  DESCRIPTION varchar(20) DEFAULT NULL,
  ORDERNUM int DEFAULT NULL,
  WELCOMEURL varchar(400) DEFAULT NULL,
  EXTENDOPERATIONS varchar(400) DEFAULT NULL,
  appcontextpath varchar(300) DEFAULT NULL,
  PRIMARY KEY (ID)
);

comment on table  urp.urp_application                               is 'urp应用';
comment on column urp.urp_application.ID                           is '应用id';
comment on column urp.urp_application.NAME                         is '应用名';
comment on column urp.urp_application.DN                           is 'DN';
comment on column urp.urp_application.PARENTID                     is '父ID';
comment on column urp.urp_application.TITLE                        is '应用标题';
comment on column urp.urp_application.ADDTIME                      is '应用添加时间';
comment on column urp.urp_application.DESCRIPTION                  is '应用描述';
comment on column urp.urp_application.ORDERNUM                     is '排序号';
comment on column urp.urp_application.WELCOMEURL                   is '欢迎路径';
comment on column urp.urp_application.EXTENDOPERATIONS             is '扩展操作 ';
comment on column urp.urp_application.appcontextpath               is '应用路径';




drop table urp.urp_org_admin;
CREATE TABLE urp.urp_org_admin (
  id varchar(38) NOT NULL,
  resourceDn varchar(400) NOT NULL,
  addTime timestamp DEFAULT NULL,
  adminId varchar(38) NOT NULL,
  adminType varchar(20) NOT NULL,
  orderNum int DEFAULT NULL,
  PRIMARY KEY (id)
);


comment on table  urp.urp_org_admin                                     is 'urp组织管理';
comment on column urp.urp_org_admin.id                                 is 'id';
comment on column urp.urp_org_admin.resourceDn                         is '资源DN';
comment on column urp.urp_org_admin.addTime                            is '添加时间';
comment on column urp.urp_org_admin.adminId                            is '管理员ID';
comment on column urp.urp_org_admin.adminType                          is '管理类型';
comment on column urp.urp_org_admin.orderNum                           is '排序号';

