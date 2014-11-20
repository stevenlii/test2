INSERT INTO uweb.t_urp_resource (ID,RESNAME,DN,RESTYPE,TITLE,PARENTID,ORDERNUM,ISLEAF,CODE) VALUES ('00000000000000000000000000000001','联动优势','o=联动优势','o','联动优势','',0,1,'');
INSERT INTO uweb.t_urp_resource (ID,RESNAME,DN,RESTYPE,TITLE,PARENTID,ORDERNUM,ISLEAF,CODE) VALUES ('00000000000000000000000000000004','管理员','user=管理员,o=联动优势','user','管理员','00000000000000000000000000000001',43,0,'');
INSERT INTO uweb.t_urp_resource(ID,RESNAME,DN,RESTYPE,TITLE,PARENTID,ORDERNUM,ISLEAF,CODE) VALUES ('00000000000000000000000000000002','应用系统','app=应用系统,o=联动优势','app','应用系统','00000000000000000000000000000001',44,1,'001');
INSERT INTO uweb.t_urp_resource (ID,RESNAME,DN,RESTYPE,TITLE,PARENTID,ORDERNUM,ISLEAF,CODE) VALUES ('8a8abef03759eb6d013759eb6d6a0000','test','role=test,o=联动优势','role','test','00000000000000000000000000000001',130,0,'');

INSERT INTO uweb.t_urp_application (ID,APPNAME,DN,PARENTID,TITLE,DESCRIPTION,ORDERNUM,WELCOMEURL,EXTENDOPERATIONS,appcontextpath) VALUES ('00000000000000000000000000000002','应用系统','app=应用系统,o=联动优势','','应用系统','   ',44,'','','');

INSERT INTO uweb.T_URP_USER (ID,LOGINNAME,USERNAME,PASSWORD,DN,OFFICIAL,DUTYLEVEL,DUTY,EMAIL,GENDER,HOMETOWN,OFFICEADDRESS,OFFICEPHONE,OFFICEFAX,HOMEPHONE,HOMEADDRESS,MOBILEID,ORDERNUM,DEPARTMENTID,BIRTHDAY,SPASSWORD) VALUES ('00000000000000000000000000000004','admin','管理员','EFD9D1B8BFB00E8E3647990F7D74D1D8','user=管理员,o=联动优势',1,'1','1','',0,'','','','','','','',43,'00000000000000000000000000000001','','');


INSERT INTO uweb.T_URP_PROFILE (ID,PROFILENAME,PATH,DESCRIPTION,STATUS,ORDERNUM) VALUES ('402886f1200e3f6601200e4029f00003','绿色','/green','','0',0);
INSERT INTO uweb.T_URP_PROFILE (ID,PROFILENAME,PATH,DESCRIPTION,STATUS,ORDERNUM) VALUES ('402886f1200e5f8501200e69773e0002','红色','/red','','0',0);
INSERT INTO uweb.T_URP_PROFILE (ID,PROFILENAME,PATH,DESCRIPTION,STATUS,ORDERNUM) VALUES ('402886f1200e3f6601200e3f66b00000','蓝色','/blue','','1',0);
INSERT INTO uweb.T_URP_PROFILE (ID,PROFILENAME,PATH,DESCRIPTION,STATUS,ORDERNUM) VALUES ('402886f1200e3f6601200e4108d70005','黄色','/yellow','','0',0);

INSERT INTO uweb.T_URP_PERMISSION (ID,RESOURCEID,DESTID,DESTTYPE,ORDERNUM,OPERATION,ISINHERIT) VALUES ('2c2c83af2311be6f012311be6f7b0000','00000000000000000000000000000001','00000000000000000000000000000004','user',0,'管理:99',1);

INSERT INTO uweb.T_URP_LAYOUT (ID,LAYOUTNAME,URL,DESCRIPTION,ORDERNUM,STATUS,RENDERPROVIDER) VALUES ('402886e720370bfb0120371377340002','默认布局（上左右，左为树）','/WEB-INF/urp/system/topLeftTreeRight/main.vm','',0,'1','');
INSERT INTO uweb.T_URP_LAYOUT (ID,LAYOUTNAME,URL,DESCRIPTION,ORDERNUM,STATUS,RENDERPROVIDER) VALUES ('402886e720370bfb01203715dc3d0003','上下布局（使用下拉菜单）','/WEB-INF/urp/system/topCenter/main.vm','',0,'0','com.at21.layout.core.support.YUIMenuLayoutRender');
INSERT INTO uweb.T_URP_LAYOUT (ID,LAYOUTNAME,URL,DESCRIPTION,ORDERNUM,STATUS,RENDERPROVIDER) VALUES ('402886e7203bc34701203bc3479b0006','Jquery layout','/WEB-INF/urp/system/jqLayout/main.vm','',1,'0','com.at21.layout.core.support.ExtTreeLayoutRender');
INSERT INTO uweb.T_URP_LAYOUT (ID,LAYOUTNAME,URL,DESCRIPTION,ORDERNUM,STATUS,RENDERPROVIDER) VALUES ('2c2c83b222bae13b0122baf6da650001','zpl','/WEB-INF/urp/system/topLeftMenuRight/main.vm','topLeftMenuRight',0,'0','');

INSERT INTO uweb.t_urp_orgnization (ID,ORGNAME,TITLE,DN,PARENTID,ORGCODE,DESCRIPTION,ORDERNUM,phone,fax,address,url) VALUES ('00000000000000000000000000000001','联动优势','','o=联动优势','','','',0,'100002','11133','','');
insert into uweb.t_urp_org_admin (id,resourcedn,adminid,adminType,ordernum) values('00000000000000000000000000000009','o=联动优势','00000000000000000000000000000004','user',0);
