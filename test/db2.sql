select * from umpay.t_order_2
select * from mobile.tmnrejtrans201211
insert into mobile.tmnrejtrans201211 (rpid,Platdate,plattime,mobileid,provcode,Area,Merid,goodsid,orderid,bankid,amount)
 values ('YWR201211201001','20121120','114012','13605530001','010','010','9996','100','494498','MW551000',100);
 
 insert into mobile.tmnrejtrans201211 (rpid,Platdate,plattime,mobileid,provcode,Area,Merid,goodsid,orderid,bankid,amount,retcode)
 values ('YWR201211201001','20121120','114012','13605530001','010','010','9996','100','494498','MW551000',100,86001821);
 
  insert into mobile.tmnrejtrans201211 (rpid,Platdate,plattime,mobileid,provcode,Area,Merid,goodsid,orderid,bankid,amount,retcode)
 values ('YWR201211201002','20121120','114012','13426344502 ','010','010','9996','100','OD10004410','MW010000',100,86001821);
 
 
   insert into mobile.tmnrejtrans201211 (rpid,Platdate,plattime,mobileid,provcode,Area,Merid,goodsid,orderid,bankid,amount,retcode)
 values ('YWR201211201003','20121120','114012','13426344502 ','010','010','9996','100','OD10004349','MW010000',100,86001821);
 
 update mobile.tmnrejtrans201211 set retcode = 86001821

select * from umpay.bank_inf 
 
select * from UMPAY.T_HFOPER_LOG
drop table UMPAY.T_HFOPER_LOG;
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




values(current timestamp)

CREATE TABLE yangwr.T_HFCATEGORY(
	ID CHAR(16) NOT NULL,
	DATATYPE CHAR(32),
	DATAKEY1 VARCHAR(32),
	DATAKEY2 VARCHAR(32),
	DATAVALUE VARCHAR(8),
	PRIMARY KEY (ID)
);

CREATE TABLE yangwr.T_MER(
	merid CHAR(16) NOT NULL,
	merName varchar(32),
	PRIMARY KEY (merid)
);
insert into yangwr.T_MER values('9996','测试商户');
insert into yangwr.T_MER values('9998','测试商户2');



select * from  yangwr.T_HFCATEGORY

insert into yangwr.T_HFCATEGORY values('1208060000000001','T_MER','9996','','001');
insert into yangwr.T_HFCATEGORY values('1208060000000002','T_MER','9996','','002');
insert into yangwr.T_HFCATEGORY values('1208060000000003','T_MER','9996','','003');
insert into yangwr.T_HFCATEGORY values('1208060000000004','T_MER','9998','','001');

select * from yangwr.T_MER mer
where 
--merid = '9996' and
 exists 
(select 'A' from yangwr.T_HFCATEGORY hc where hc.DATATYPE = 'T_MER'  
and mer.merid = hc.DATAKEY1 
and hc.datavalue in('003','004','001')
)


create table yangwr.T_HFAUDIT
(
   ID                   CHAR(16)               not null,
   DATATYPE             CHAR(32)               not null default '',
   IXDATA               VARCHAR(64)            not null default '',
   DATAP                CLOB(1024)             not null default '',
   DATA              CLOB(1024)             not null default '',
   AUDITTYPE            SMALLINT               not null default 0,
   STATE                SMALLINT               not null default 0,
   INTIME               TIMESTAMP              not null default CURRENT TIMESTAMP,
   CREATOR              VARCHAR(64)            not null default '',
   MODTIME              TIMESTAMP              not null default CURRENT TIMESTAMP,
   MODUSER              VARCHAR(64)            not null default '',
   RESULTDESC           VARCHAR(255)           not null default '',
   RESERVED             VARCHAR(64)            not null default '',
   primary key (ID)
);

select DATA from umpay.T_AUDIT
order by state


select * from umpay.T_HFAUDIT