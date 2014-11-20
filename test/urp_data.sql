
INSERT INTO urp.urp_resource (ID,NAME,DN,RESTYPE,TITLE,PARENTID,ORDERNUM,ADDTIME,ISLEAF,CODE) VALUES ('00000000000000000000000000000001','联动优势','o=联动优势','ou','联动优势',NULL,0,NULL,1,NULL);
INSERT INTO urp.urp_resource (ID,NAME,DN,RESTYPE,TITLE,PARENTID,ORDERNUM,ADDTIME,ISLEAF,CODE) VALUES ('00000000000000000000000000000004','管理员','user=管理员,o=联动优势','user','管理员','00000000000000000000000000000001',43,NULL,0,NULL);
INSERT INTO urp.urp_resource(ID,NAME,DN,RESTYPE,TITLE,PARENTID,ORDERNUM,ADDTIME,ISLEAF,CODE) VALUES ('00000000000000000000000000000002','应用系统','app=应用系统,o=联动优势','app','应用系统','00000000000000000000000000000001',44,NULL,1,'001');
INSERT INTO urp.urp_resource (ID,NAME,DN,RESTYPE,TITLE,PARENTID,ORDERNUM,ADDTIME,ISLEAF,CODE) VALUES ('8a8abef03759eb6d013759eb6d6a0000','test','role=test,o=联动优势','role','test','00000000000000000000000000000001',130,NULL,0,NULL);

INSERT INTO urp.urp_application (ID,NAME,DN,PARENTID,TITLE,ADDTIME,DESCRIPTION,ORDERNUM,WELCOMEURL,EXTENDOPERATIONS,appcontextpath) VALUES ('00000000000000000000000000000002','应用系统','app=应用系统,o=联动优势',NULL,'应用系统',NULL,'   ',44,NULL,NULL,NULL);

INSERT INTO urp.URP_USER (ID,LOGINNAME,NAME,PASSWORD,DN,OFFICIAL,DUTYLEVEL,DUTY,EMAIL,GENDER,HOMETOWN,OFFICEADDRESS,OFFICEPHONE,OFFICEFAX,HOMEPHONE,HOMEADDRESS,MOBILE,ORDERNUM,ADDTIME,DEPARTMENTID,BIRTHDAY,SPASSWORD) VALUES ('00000000000000000000000000000004','admin','管理员','EFD9D1B8BFB00E8E3647990F7D74D1D8','user=管理员,o=联动优势',1,'1','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,43,NULL,'00000000000000000000000000000001',NULL,NULL);


INSERT INTO urp.URP_PROFILE (ID,NAME,PATH,ADDTIME,DESCRIPTION,STATUS,ORDERNUM) VALUES ('402886f1200e3f6601200e4029f00003','绿色','/green',NULL,NULL,'0',0);
INSERT INTO urp.URP_PROFILE (ID,NAME,PATH,ADDTIME,DESCRIPTION,STATUS,ORDERNUM) VALUES ('402886f1200e5f8501200e69773e0002','红色','/red',NULL,NULL,'0',0);
INSERT INTO urp.URP_PROFILE (ID,NAME,PATH,ADDTIME,DESCRIPTION,STATUS,ORDERNUM) VALUES ('402886f1200e3f6601200e3f66b00000','蓝色','/blue',NULL,NULL,'1',0);
INSERT INTO urp.URP_PROFILE (ID,NAME,PATH,ADDTIME,DESCRIPTION,STATUS,ORDERNUM) VALUES ('402886f1200e3f6601200e4108d70005','黄色','/yellow',NULL,NULL,'0',0);

INSERT INTO urp.URP_PERMISSION (ID,RESOURCEID,DESTID,DESTTYPE,ADDTIME,ORDERNUM,OPERATION,ISINHERIT) VALUES ('2c2c83af2311be6f012311be6f7b0000','00000000000000000000000000000001','00000000000000000000000000000004','user',NULL,0,'管理:99',1);

INSERT INTO urp.URP_LAYOUT (ID,NAME,URL,ADDTIME,DESCRIPTION,ORDERNUM,STATUS,RENDERPROVIDER) VALUES ('402886e720370bfb0120371377340002','默认布局（上左右，左为树）','/WEB-INF/urp/system/topLeftTreeRight/main.vm',NULL,NULL,0,'1',NULL);
INSERT INTO urp.URP_LAYOUT (ID,NAME,URL,ADDTIME,DESCRIPTION,ORDERNUM,STATUS,RENDERPROVIDER) VALUES ('402886e720370bfb01203715dc3d0003','上下布局（使用下拉菜单）','/WEB-INF/urp/system/topCenter/main.vm',NULL,NULL,0,'0','com.at21.layout.core.support.YUIMenuLayoutRender');
INSERT INTO urp.URP_LAYOUT (ID,NAME,URL,ADDTIME,DESCRIPTION,ORDERNUM,STATUS,RENDERPROVIDER) VALUES ('402886e7203bc34701203bc3479b0006','Jquery layout','/WEB-INF/urp/system/jqLayout/main.vm',NULL,NULL,1,'0','com.at21.layout.core.support.ExtTreeLayoutRender');
INSERT INTO urp.URP_LAYOUT (ID,NAME,URL,ADDTIME,DESCRIPTION,ORDERNUM,STATUS,RENDERPROVIDER) VALUES ('2c2c83b222bae13b0122baf6da650001','zpl','/WEB-INF/urp/system/topLeftMenuRight/main.vm',NULL,'topLeftMenuRight',0,'0',NULL);

INSERT INTO urp.urp_orgnization (ID,NAME,TITLE,DN,PARENTID,ORGCODE,DESCRIPTION,ORDERNUM,ADDTIME,phone,fax,address,url) VALUES ('00000000000000000000000000000001','联动优势',NULL,'o=联动优势',NULL,NULL,NULL,0,NULL,'100002','11133',NULL,'');
insert into urp.urp_org_admin (id,resourcedn,addtime,adminid,adminType,ordernum) values('00000000000000000000000000000009','o=联动优势',null,'00000000000000000000000000000004','user',0)
