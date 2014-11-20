drop table UMPAY.T_HFMER_EXP;
drop table UMPAY.T_HFGOODS_EXP;
drop table UMPAY.T_HFAUDIT;
drop table UMPAY.T_HFOPER_LOG;
drop table UMPAY.T_SERVICE_STAT;
drop table UMPAY.T_HFMER_GRADE;
drop table UMPAY.T_HFDOC;


create table UMPAY.T_HFMER_EXP
(
   MERID                CHAR(8)                not null,
   BUSITYPE             CHAR(2)                not null default '',
   OPERATOR             CHAR(64)               not null default '',
   ADDWAY               SMALLINT               not null default 0,
   INTERTYPE            SMALLINT               not null default 0,
   CHNLCHECK            SMALLINT               not null default 0,
   MODTIME              TIMESTAMP              not null default CURRENT TIMESTAMP,
   MODLOCK              SMALLINT               not null default 0,
   MODUSER              VARCHAR(64)            not null default '',
   CATEGORY             CHAR(16)               not null default '',
   COMPANYNAME          VARCHAR(64)            not null default '',
   INTIME               TIMESTAMP              not null default CURRENT TIMESTAMP,
   primary key (MERID)
);

comment on table UMPAY.T_HFMER_EXP                             is '商户扩展信息';
comment on column UMPAY.T_HFMER_EXP.MERID                      is '商户编号';
comment on column UMPAY.T_HFMER_EXP.BUSITYPE                   is '01,02,10,11,12 第一位含义0：不含全网业务；1：含全网业务 第二位含义0：不含省网业务；1：本地接入；2：全网落地';
comment on column UMPAY.T_HFMER_EXP.ADDWAY                     is '添加方式 0：本地，1：基地';
comment on column UMPAY.T_HFMER_EXP.INTERTYPE                  is '接口类型 0：标准；1：特殊';
comment on column UMPAY.T_HFMER_EXP.CHNLCHECK                  is '是否渠道报备 0:否；1：是';
comment on column UMPAY.T_HFMER_EXP.INTIME                     is '添加时间';
comment on column UMPAY.T_HFMER_EXP.MODTIME                    is '修改时间';
comment on column UMPAY.T_HFMER_EXP.MODLOCK                    is '修改锁 1：锁定中,0:未锁定';
comment on column UMPAY.T_HFMER_EXP.MODUSER                    is '修改人';
comment on column UMPAY.T_HFMER_EXP.OPERATOR                   is '运营负责人';
comment on column UMPAY.T_HFMER_EXP.CATEGORY                   is '商户分类:001软件 002网络游戏 003单机游戏';
comment on column UMPAY.T_HFMER_EXP.COMPANYNAME                is '公司名称';


create table UMPAY.T_HFGOODS_EXP
(
   MERID                CHAR(8)             not null,
   GOODSID              CHAR(8)             not null,
   CATEGORY             CHAR(16)            not null default '',
   BUSITYPE             CHAR(2)             not null default '',
   AMOUNT               DECIMAL(13,0)       not null default 0,
   ADDWAY               SMALLINT            not null default 0,
   MODLOCK              SMALLINT            not null default 0,
   MODTIME              TIMESTAMP           not null default CURRENT TIMESTAMP,
   MODUSER              VARCHAR(64)         not null default '',
   INTIME               TIMESTAMP           not null default CURRENT TIMESTAMP,
   primary key (MERID, GOODSID)
);

comment on table UMPAY.T_HFGOODS_EXP 					is	'商品扩展信息';
comment on column UMPAY.T_HFGOODS_EXP.MERID 			is '商户编号';
comment on column UMPAY.T_HFGOODS_EXP.GOODSID 			is '商品编号';
comment on column UMPAY.T_HFGOODS_EXP.BUSITYPE 			is '10,11,01 第一位含义0：不含全网业务；1：含全网业务 第二位含义0：不含省网业务；1：含全网';
comment on column UMPAY.T_HFGOODS_EXP.AMOUNT 			is '金额';
comment on column UMPAY.T_HFGOODS_EXP.ADDWAY 			is '0：本地添加 1：其他';
comment on column UMPAY.T_HFGOODS_EXP.MODLOCK 			is '修改锁 1：锁定中,0:未锁定';
comment on column UMPAY.T_HFGOODS_EXP.INTIME			is '添加时间';
comment on column UMPAY.T_HFGOODS_EXP.MODTIME			is '修改时间';
comment on column UMPAY.T_HFGOODS_EXP.MODUSER			is '修改人';
comment on column UMPAY.T_HFGOODS_EXP.CATEGORY			is '商品分类 101数字及游戏点卡、102电子书、103邮箱、104视频、105软件、106保险、107彩票、001电子票券、002公共事业、003线下商超、004医疗服务、005实物';


create table UMPAY.T_HFAUDIT
(
   ID                   CHAR(16)               not null,
   TABLENAME            CHAR(32)               not null default '',
   IXDATA               VARCHAR(64)            not null default '',
   IXDATA2              VARCHAR(64)            not null default '',
   DESC                 VARCHAR(64)	           not null default '',
   MODDATA              VARCHAR(2048)          not null ,
   AUDITTYPE            SMALLINT               not null default 0,
   STATE                SMALLINT               not null default 0,
   BATCHID		VARCHAR(32)		not null default '',
   CREATOR              CHAR(64)	           not null default '',
   MODTIME              TIMESTAMP              not null default CURRENT TIMESTAMP,
   MODUSER              CHAR(64)               not null default '',
   RESULTDESC           VARCHAR(64)            not null default '',
   INTIME               TIMESTAMP              not null default CURRENT TIMESTAMP,
   primary key (ID)
);

comment on table UMPAY.T_HFAUDIT 						is '话费信息审核';
comment on column UMPAY.T_HFAUDIT.ID 					is 'YYYYMMDD10位序列号';
comment on column UMPAY.T_HFAUDIT.TABLENAME 			is '操作的数据库表英文名 如UMPAY.T_MER_INF';
comment on column UMPAY.T_HFAUDIT.IXDATA				is '查询字段';
comment on column UMPAY.T_HFAUDIT.MODDATA 				is '修改后数据';
comment on column UMPAY.T_HFAUDIT.AUDITTYPE				is '审核类型 1：新增:2：修改:3：启用:4：禁用 0:未知';
comment on column UMPAY.T_HFAUDIT.STATE					is '审核状态 0待审核 1审核不通过 2审核通过';
comment on column UMPAY.T_HFAUDIT.INTIME				is '添加时间';
comment on column UMPAY.T_HFAUDIT.CREATOR				is '添加人';
comment on column UMPAY.T_HFAUDIT.BATCHID				is '批次号';
comment on column UMPAY.T_HFAUDIT.MODTIME				is '审核时间';
comment on column UMPAY.T_HFAUDIT.MODUSER				is '审核人';
comment on column UMPAY.T_HFAUDIT.RESULTDESC			is '描述信息';
comment on column UMPAY.T_HFAUDIT.IXDATA2				is '查询字段';
comment on column UMPAY.T_HFAUDIT.DESC					is '被审核数据的描述信息';


CREATE  TABLE UMPAY.T_HFOPER_LOG(
  OPERID			CHAR(32)      NOT NULL,
	TABLENAME		CHAR(32)			NOT NULL    DEFAULT '',
	DATAID			VARCHAR(64)		NOT NULL    DEFAULT '',
	OPERTYPE		SMALLINT			NOT NULL		DEFAULT -99,
	DETAIL			VARCHAR(255)		NOT NULL	DEFAULT '',
	MODUSER			CHAR(64)			NOT NULL		DEFAULT '',
	INTIME			TIMESTAMP			NOT NULL		DEFAULT	CURRENT_TIMESTAMP,
	primary key (OPERID)
);

comment on table  UMPAY.T_HFOPER_LOG                                   is '操作日志';
comment on column UMPAY.T_HFOPER_LOG.OPERID												 		 is '日志ID';
comment on column UMPAY.T_HFOPER_LOG.TABLENAME                         is '操作的数据库表英文名';
comment on column UMPAY.T_HFOPER_LOG.DATAID                            is '所操作数据的主键';
comment on column UMPAY.T_HFOPER_LOG.OPERTYPE                          is '操作类型 1：创建 2：修改 3：审核 4：删除';
comment on column UMPAY.T_HFOPER_LOG.MODUSER                           is '操作人';
comment on column UMPAY.T_HFOPER_LOG.DETAIL                            is '明细';
comment on column UMPAY.T_HFOPER_LOG.INTIME                            is '入库时间';


CREATE TABLE UMPAY.T_SERVICE_STAT (
	SERVICEID       CHAR(8)             NOT NULL,
	USECOUNT        SMALLINT            NOT NULL        DEFAULT 0,
	MATCHTYPE       SMALLINT            NOT NULL        DEFAULT -99,
	MODTIME		TIMESTAMP	    NOT NULL	    DEFAULT CURRENT_TIMESTAMP,
	INTIME          TIMESTAMP           NOT NULL        DEFAULT CURRENT_TIMESTAMP,
	primary key (SERVICEID)
);

comment on table  UMPAY.T_SERVICE_STAT                                 is '计费代码统计表';
comment on column UMPAY.T_SERVICE_STAT.SERVICEID                       is '计费代码';
comment on column UMPAY.T_SERVICE_STAT.USECOUNT                        is '使用次数 用于多少个商品做计费';
comment on column UMPAY.T_SERVICE_STAT.MATCHTYPE                       is '匹配度 0-精确匹配，1-套用，2-其他，-99未知';
comment on column UMPAY.T_SERVICE_STAT.MODTIME                         is '修改时间';
comment on column UMPAY.T_SERVICE_STAT.INTIME                          is '入库时间';


create table UMPAY.T_HFMER_GRADE
(
   MERID                CHAR(8)                not null,
   MONTH             		CHAR(7)                not null,
   TURNOVER							DECIMAL(12,0)          not null default -99,
   LASTTURNOVER					DECIMAL(12,0)          not null default -99,
   RISERATE             DECIMAL(10,2)          not null default -99,
   REDUCESUM            DECIMAL(12,0)          not null default -99,
   REDUCERATE           DECIMAL(10,2)          not null default -99,
   TRADINGCOUNT         INTEGER                not null default -99,
   COMPLAINTCOUNT       INTEGER                not null default -99,
   TURNOVERINDEX        DECIMAL(10,2)          not null default -99,
   RISERATEINDEX        DECIMAL(10,2)          not null default -99,
   FALSETRADEINDEX      DECIMAL(10,2)          not null default -99,
   COMPLAINTINDEX       DECIMAL(10,2)          not null default -99,
   SYSSTABINDEX         DECIMAL(10,2)          not null default -99,
   COOPERATEINDEX       DECIMAL(10,2)          not null default -99,
   BREACHINDEX          DECIMAL(10,2)          not null default -99,
   UPGRADEINDEX         DECIMAL(10,2)          not null default -99,
   MARKETINGINDEX       DECIMAL(10,2)          not null default -99,
   SUPPORTINDEX         DECIMAL(10,2)          not null default -99,
   TOTAL           			DECIMAL(10,2)          not null default -99,
   MODLOCK              SMALLINT               not null default 0,
   MODUSER              VARCHAR(64)            not null default '',      
   MODTIME              TIMESTAMP              not null default CURRENT TIMESTAMP,
   INTIME               TIMESTAMP              not null default CURRENT TIMESTAMP,
   primary key (MERID, MONTH)
);

comment on table UMPAY.T_HFMER_GRADE                           	 is '商户评分表';
comment on column UMPAY.T_HFMER_GRADE.MERID                      is '商户编号';
comment on column UMPAY.T_HFMER_GRADE.MONTH                      is '所属月份 (T-1)月 格式为yyyy-MM';
comment on column UMPAY.T_HFMER_GRADE.TURNOVER                   is '(T-1)月交易额';
comment on column UMPAY.T_HFMER_GRADE.LASTTURNOVER               is '(T-2)月交易额';
comment on column UMPAY.T_HFMER_GRADE.RISERATE                   is '交易额增长率';
comment on column UMPAY.T_HFMER_GRADE.REDUCESUM                  is '(T-2)月核减金额';
comment on column UMPAY.T_HFMER_GRADE.REDUCERATE                 is '(T-2)月账单核减率';
comment on column UMPAY.T_HFMER_GRADE.TRADINGCOUNT               is '交易笔数';
comment on column UMPAY.T_HFMER_GRADE.COMPLAINTCOUNT             is '客诉笔数';
comment on column UMPAY.T_HFMER_GRADE.TURNOVERINDEX              is '交易额指标';
comment on column UMPAY.T_HFMER_GRADE.RISERATEINDEX              is '交易额增长率指标';
comment on column UMPAY.T_HFMER_GRADE.FALSETRADEINDEX            is '虚假交易指标';
comment on column UMPAY.T_HFMER_GRADE.COMPLAINTINDEX             is '客诉指标';
comment on column UMPAY.T_HFMER_GRADE.SYSSTABINDEX               is '系统稳定性指标';
comment on column UMPAY.T_HFMER_GRADE.COOPERATEINDEX             is '配合力度指标';
comment on column UMPAY.T_HFMER_GRADE.BREACHINDEX                is '违约情况指标';
comment on column UMPAY.T_HFMER_GRADE.UPGRADEINDEX               is '升级重大投诉指标';
comment on column UMPAY.T_HFMER_GRADE.MARKETINGINDEX             is '营销活动指标';
comment on column UMPAY.T_HFMER_GRADE.SUPPORTINDEX               is '业务资源支持指标';
comment on column UMPAY.T_HFMER_GRADE.TOTAL                      is '总分';
comment on column UMPAY.T_HFMER_GRADE.MODLOCK               	   is '修改锁 1：锁定中,0:未锁定';
comment on column UMPAY.T_HFMER_GRADE.MODUSER            		     is '修改人';
comment on column UMPAY.T_HFMER_GRADE.MODTIME              		   is '修改时间';
comment on column UMPAY.T_HFMER_GRADE.INTIME              		   is '添加时间';



create table UMPAY.T_HFDOC
(
   ID                		CHAR(16)               not null,
   DOCNAME             	VARCHAR(64)            not null,
   DOCTYPE							SMALLINT               not null default -99,
   DOCPATH					 		VARCHAR(64)            not null default '',
   DOCDESC             	VARCHAR(64)         	 not null default '',
   CREATOR            	VARCHAR(64)            not null default '',
   INTIME               TIMESTAMP              not null default CURRENT TIMESTAMP,
   MODUSER              VARCHAR(64)            not null default '',      
   MODTIME              TIMESTAMP              not null default CURRENT TIMESTAMP,
   primary key (ID)
);

comment on table UMPAY.T_HFDOC                           	 			is '商户评分表';
comment on column UMPAY.T_HFDOC.ID                      				is '文档编号';
comment on column UMPAY.T_HFDOC.DOCNAME                      		is '文档名称';
comment on column UMPAY.T_HFDOC.DOCTYPE                   			is '文档类型 -99：未知类型 1：评分等级文档';
comment on column UMPAY.T_HFDOC.DOCPATH               					is '文档的相对路径';
comment on column UMPAY.T_HFDOC.DOCDESC                  				is '文档描述';
comment on column UMPAY.T_HFDOC.CREATOR                  				is '创建者';
comment on column UMPAY.T_HFDOC.INTIME              		   			is '添加时间';
comment on column UMPAY.T_HFDOC.MODUSER            		     			is '修改人';
comment on column UMPAY.T_HFDOC.MODTIME              		   			is '修改时间';