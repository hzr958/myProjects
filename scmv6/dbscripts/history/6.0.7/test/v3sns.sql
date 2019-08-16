-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end




--原因（有CQ号带上CQ号） 2016-12-8 WSN end



--原因（SCM-13782） 2017-09-14 hcj begin
CREATE TABLE V_FILE_DOWNLOAD_RECORD 
(
  ID NUMBER NOT NULL PRIMARY KEY
, FILE_ID NUMBER(18, 0) NOT NULL
, FILE_TYPE VARCHAR2(100 BYTE) 
, DOWNLOAD_PSN_ID NUMBER(18, 0) NOT NULL 
, DOWNLOAD_DATE DATE 
, DOWNLOAD_SOURCE VARCHAR2(200 BYTE) 
, DOWNLOAD_IP VARCHAR2(20 BYTE) )
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;


COMMENT ON TABLE V_FILE_DOWNLOAD_RECORD IS '文件下载记录表';

COMMENT ON COLUMN V_FILE_DOWNLOAD_RECORD.ID IS '逻辑主键';

COMMENT ON COLUMN V_FILE_DOWNLOAD_RECORD.FILE_ID IS '文件id(archive fileId)';

COMMENT ON COLUMN V_FILE_DOWNLOAD_RECORD.FILE_TYPE IS '文件类型';

COMMENT ON COLUMN V_FILE_DOWNLOAD_RECORD.DOWNLOAD_PSN_ID IS '下载人的id';

COMMENT ON COLUMN V_FILE_DOWNLOAD_RECORD.DOWNLOAD_DATE IS '下载时间';

COMMENT ON COLUMN V_FILE_DOWNLOAD_RECORD.DOWNLOAD_SOURCE IS '下载来源referer';

COMMENT ON COLUMN v_file_download_record.download_ip IS '下载的ip地址';

CREATE SEQUENCE SEQ_V_FILE_DOWNLOAD_RECORD INCREMENT BY 1 START WITH 1 MINVALUE 1 ORDER;

--原因（SCM-13782） 2017-09-14 hcj end

--国家地区表天津对应英文错误2017-9-20 WCW begin


update CONST_REGION set en_name='Tianjin' where region_id = 120000;
commit;


--国家地区表天津对应英文错误2017-9-20 WCW end

--原因（SCM-14374） 2017-09-21 LJ begin


insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) values(78,'GeneratePersonNullNameTaskTrigger','0 0/1 * * * ?',0,'拆分person表人员姓名');


--原因（SCM-14374） 2017-09-21 LJ end


--原因（SCM-14374） 2017-09-21 LJ begin

-- Create table
create table PERSON_FL_NAME
(
  psn_id        NUMBER(18) not null,
  name_zh       VARCHAR2(100 CHAR),
  name_en       VARCHAR2(100 CHAR),
  first_name_en CHAR(100 CHAR),
  last_name_en  CHAR(100 CHAR),
  first_name_zh CHAR(100 CHAR),
  last_name_zh  CHAR(100 CHAR)

 
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

-- Create/Recreate primary, unique and foreign key constraints 
alter table PERSON_FL_NAME
  add constraint PK_PERSON_FL_NAME primary key (PSN_ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );


--原因（SCM-14374） 2017-09-21 LJ end
--原因（修改demo的guid类型为varchar2） 2017-09-21 zzx begin
--DEMO_CACHE_PUB_INFO
alter table DEMO_CACHE_PUB_INFO add tmp_col varchar2(3999);-- 添加临时列  
update DEMO_CACHE_PUB_INFO set tmp_col = guid ; --将目标字段中数据加入到临时列中  
update DEMO_CACHE_PUB_INFO set guid = null; --将目标字段数据清空  
alter table DEMO_CACHE_PUB_INFO modify (guid varchar2(100)); --更改目标字段类型  
update DEMO_CACHE_PUB_INFO set guid = tmp_col; --将临时列数据加回到目标字段中  
alter table DEMO_CACHE_PUB_INFO drop column tmp_col; --清除临时列  
--DEMO_SCM_REQ_PUB_RECORD
alter table DEMO_SCM_REQ_PUB_RECORD add tmp_col varchar2(3999);-- 添加临时列  
update DEMO_SCM_REQ_PUB_RECORD set tmp_col = guid ; --将目标字段中数据加入到临时列中  
update DEMO_SCM_REQ_PUB_RECORD set guid = null; --将目标字段数据清空  
alter table DEMO_SCM_REQ_PUB_RECORD modify (guid varchar2(100)); --更改目标字段类型  
update DEMO_SCM_REQ_PUB_RECORD set guid = tmp_col; --将临时列数据加回到目标字段中  
alter table DEMO_SCM_REQ_PUB_RECORD drop column tmp_col; --清除临时列  
--SCM_PUB_IMPORTED
alter table SCM_PUB_IMPORTED add tmp_col varchar2(3999);-- 添加临时列  
update SCM_PUB_IMPORTED set tmp_col = guid ; --将目标字段中数据加入到临时列中  
update SCM_PUB_IMPORTED set guid = null; --将目标字段数据清空  
alter table SCM_PUB_IMPORTED modify (guid varchar2(100)); --更改目标字段类型  
update SCM_PUB_IMPORTED set guid = tmp_col; --将临时列数据加回到目标字段中  
alter table SCM_PUB_IMPORTED drop column tmp_col; --清除临时列  
commit;
--原因（修改demo的guid类型为varchar2） 2017-09-21 zzx end
    
--原因（SCM-14403） 2017-09-27 hcj begin 
COMMENT ON COLUMN dynamic_award_psn.status IS '1--赞，0--取消赞。java代码判断使用枚举类LikeStatusEnum.LIKE和LikeStatusEnum.UNLIKE';
COMMENT ON COLUMN award_statistics.action IS '操作：1--赞，0--取消赞。java代码判断使用枚举类LikeStatusEnum.LIKE和LikeStatusEnum.UNLIKE';
COMMENT ON COLUMN v_group_dynamic_awards.status IS '操作：1--赞，0--取消赞。java代码判断使用枚举类LikeStatusEnum.LIKE和LikeStatusEnum.UNLIKE';
ALTER TABLE v_fund_award RENAME COLUMN operate TO status;
COMMENT ON COLUMN v_fund_award.status IS '赞状态：1--赞，0--取消赞。java代码判断使用枚举类LikeStatusEnum.LIKE和LikeStatusEnum.UNLIKE';
--原因（SCM-14403） 2017-09-27 hcj end
--SCM-14539 2017-09-29 hzr begin
insert into V_OPEN_TOKEN_SERVICE_CONST (ID, TOKEN, SERVICE_TYPE, STATUS, CREATE_DATE, DESCR)
values (523, '00000000', 'psndsolr', 0, to_date('29-09-2017 16:55:40', 'dd-mm-yyyy hh24:mi:ss'), 'Delete user solr info');

insert into V_OPEN_TOKEN_SERVICE_CONST (ID, TOKEN, SERVICE_TYPE, STATUS, CREATE_DATE, DESCR)
values (522, '00000000', 'psn2solr', 0, to_date('28-09-2017 18:34:40', 'dd-mm-yyyy hh24:mi:ss'), 'Update user solr info');
--SCM-14539 2017-09-29 hzr end

--SCM-14563 2017-10-12 zzx begin
 alter table DEMO_CACHE_PUB_INFO add cited_times number(8);
 commit;
 --SCM-14563 2017-10-12 zzx end
 
 
 
 
 ----SCM-14511 nodeweb7管理模块权限添加--仅限管理员操作  2017-10-12 zll begin
 
 insert into sys_resource(id,resource_type,value,order_num,parent_id,name,status,lavels,remark) values(10,'url','/scmmanagement/psnInfo/main',3,0,'人员管理',1,1,'人员管理');

insert into sys_authoritie(id,name,display_name) values(10005,'ROLE_URL_SCMMANAGEMENT','权限：人员管理');

insert into sys_role_authoritie(role_id,authority_id) values(2,10005);

insert into sys_resource_authoritie(authority_id,resource_id) values(10005,10);

 ----SCM-14511 nodeweb7管理模块权限添加--仅限管理员操作  2017-10-12 zll end



---SCM-14583 人员合并之后添加操作记录  2017-10-13 zll begin

-- Add/modify columns 
alter table SYS_MERGE_USER_HIS add operate_psn_id NUMBER(20);
-- Add comments to the columns 
comment on column SYS_MERGE_USER_HIS.operate_psn_id
  is '合并账号的操作人员Id';
---SCM-14583 人员合并之后添加操作记录  2017-10-13 zll end
--原因（demo日志字段修改） 2017-10-17 zzx begin
alter table scmsrv_access_log drop column params;--删除旧列
alter table scmsrv_access_log add params varchar2(4000);-- 添加临时列 
        
alter table scmsrv_error_log drop column params;--删除旧列
alter table scmsrv_error_log add params varchar2(4000);-- 添加临时列 
          
alter table scmsrv_error_log drop column scm_error_msg;--删除旧列
alter table scmsrv_error_log add scm_error_msg varchar2(4000);-- 添加临时列 
           
alter table scmsrv_error_log drop column demo_error_msg;--删除旧列
alter table scmsrv_error_log add demo_error_msg varchar2(4000);-- 添加临时列 
commit;
--原因（demo日志字段修改） 2017-10-17 zzx end


--PSN_STATISTICS表中成果统计数和人员实际的成果数量不一致  2017-10-17 WSN begin

update PSN_STATISTICS ps set ps.pub_sum = (select count(1) from v_pub_simple t where t.owner_psn_id = ps.psn_id and t.article_Type=1 and t.status = 0 and t.simple_Status in( 0,1,99) group by t.owner_psn_id)

where exists (select 1 from v_pub_simple p where p.owner_psn_id = ps.psn_id and p.article_Type=1 and p.status = 0 and p.simple_Status in( 0,1,99));

commit;

--PSN_STATISTICS表中成果统计数和人员实际的成果数量不一致  2017-10-17 WSN end



---SCM-14625 psn_refresh_user_info表主键调整 2017-10-18 zll begin
delete  from PSN_REFRESH_USER_INFO t;

-- Add/modify columns 
alter table PSN_REFRESH_USER_INFO add id NUMBER(18) not null;
-- Add comments to the columns 
comment on column PSN_REFRESH_USER_INFO.id
  is '主键id';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PSN_REFRESH_USER_INFO
  drop constraint PK_PSN_REFRESH_USER_INFO cascade;
alter table PSN_REFRESH_USER_INFO
  add constraint PK_PSN_REFRESH_USER_INFO primary key (ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );




create sequence SEQ_PSN_REFRESH_USER_INFO
minvalue 1
maxvalue 999999999999999999999999999
start with 11
increment by 1
cache 10;

---SCM-14625 psn_refresh_user_info表主键调整 2017-10-18 zll end


---SCM-14625 psn_refresh_user_info表主键调整 zll begin
-- Create/Recreate primary, unique and foreign key constraints 
alter table PSN_REFRESH_USER_INFO
  add constraint PK_REFRESH_USER_INFO primary key (ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Drop indexes 
drop index PK_PSN_REFRESH_USER_INFO;

---SCM-14625 psn_refresh_user_info表主键调整 zll end
--原因（demo添加新字段） 2017-10-19 zzx begin
alter table SCM_PUB_IMPORTED add PUB_TYPE number(2);
comment on column SCM_PUB_IMPORTED.pub_type
  is '导入成果类型 12=是从检索论文/专利导入的';
  commit;
  --原因（demo添加新字段） 2017-10-19 zzx end


--原因（新加open访问日志表） 2017-10-24 tsz begin
-- Create table
create table V_OPEN_DATA_ACCESS_LOG
(
  id           NUMBER(13) not null,
  open_id      VARCHAR2(20),
  token        VARCHAR2(8),
  service_type VARCHAR2(8),
  parameter    CLOB,
  access_date  DATE,
  disc         VARCHAR2(100),
  reqeust_type VARCHAR2(2)
);
-- Add comments to the table 
comment on table V_OPEN_DATA_ACCESS_LOG
  is 'open接口访问日志记录 (只记录参数校验过后的访问)';
-- Add comments to the columns 
comment on column V_OPEN_DATA_ACCESS_LOG.id
  is '主键';
comment on column V_OPEN_DATA_ACCESS_LOG.open_id
  is 'openid';
comment on column V_OPEN_DATA_ACCESS_LOG.token
  is '第3方授权码';
comment on column V_OPEN_DATA_ACCESS_LOG.service_type
  is '服务编码';
comment on column V_OPEN_DATA_ACCESS_LOG.parameter
  is '访问参数';
comment on column V_OPEN_DATA_ACCESS_LOG.access_date
  is '访问时间';
comment on column V_OPEN_DATA_ACCESS_LOG.disc
  is '描述';
comment on column V_OPEN_DATA_ACCESS_LOG.reqeust_type
  is '访问类型 1 webservice, 2 restful ,3 内部调用';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_OPEN_DATA_ACCESS_LOG
  add primary key (ID)
  using index ;
  
  
  -- Create sequence 
create sequence V_SEQ_OPEN_DATA_ACCESS_LOG
minvalue 1
maxvalue 999999999999999999999999999
start with 11
increment by 1
cache 10;

--原因（新加open访问日志表） 2017-10-24 tsz end


--原因（成果收录情况修改） 2017-10-24 hzr begin
-- Add/modify columns 
alter table PUB_LIST add list_cssci NUMBER(1) default 0;
alter table PUB_LIST add list_pku NUMBER(1) default 0;
alter table PUB_LIST add list_other NUMBER(1) default 0;
-- Add comments to the columns 
comment on column PUB_LIST.list_cssci
  is '0:未收录；1：收录';
comment on column PUB_LIST.list_pku
  is '0:未收录；1：收录';
comment on column PUB_LIST.list_other
  is '0:未收录；1：收录';
  
  INSERT INTO CONST_DICTIONARY T (CATEGORY, CODE, EN_US_CAPTION, ZH_TW_CAPTION, ZH_CN_CAPTION, SEQ_NO)
  VALUES ('pub_patent_type', '54', 'Plant Patent', '植物专利', '植物专利', 4);
INSERT INTO CONST_DICTIONARY T (CATEGORY, CODE, EN_US_CAPTION, ZH_TW_CAPTION, ZH_CN_CAPTION, SEQ_NO)
  VALUES ('pub_patent_area', 'china', 'Chinese Patent', '中国专利', '中国专利', 1);
INSERT INTO CONST_DICTIONARY T (CATEGORY, CODE, EN_US_CAPTION, ZH_TW_CAPTION, ZH_CN_CAPTION, SEQ_NO)
  VALUES ('pub_patent_area', 'europe', 'European Patent', '欧洲专利', '欧洲专利', 3);
INSERT INTO CONST_DICTIONARY T (CATEGORY, CODE, EN_US_CAPTION, ZH_TW_CAPTION, ZH_CN_CAPTION, SEQ_NO)
  VALUES ('pub_patent_area', 'japan', 'Japanese Patent', '日本专利', '日本专利', 5);
INSERT INTO CONST_DICTIONARY T (CATEGORY, CODE, EN_US_CAPTION, ZH_TW_CAPTION, ZH_CN_CAPTION, SEQ_NO)
  VALUES ('pub_patent_area', 'other', 'Patent of Other Countries', '其他国家专利', '其他国家专利', 6);
INSERT INTO CONST_DICTIONARY T (CATEGORY, CODE, EN_US_CAPTION, ZH_TW_CAPTION, ZH_CN_CAPTION, SEQ_NO)
  VALUES ('pub_patent_area', 'usa', 'US Patent', '美国专利', '美国专利', 2);
INSERT INTO CONST_DICTIONARY T (CATEGORY, CODE, EN_US_CAPTION, ZH_TW_CAPTION, ZH_CN_CAPTION, SEQ_NO)
  VALUES ('pub_patent_area', 'wipo', 'WIPO Patent', 'WIPO专利', 'WIPO专利', 4);
COMMIT;
--原因（成果收录情况修改） 2017-10-24 hzr end

)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16K
    next 8K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table APP_AUTH_CODE
  is 'app人员token表';
-- Add comments to the columns 
comment on column APP_AUTH_CODE.psn_id
  is '人员id';
comment on column APP_AUTH_CODE.effective_date
  is '生效日期';
comment on column APP_AUTH_CODE.token
  is 'token';
-- Create/Recreate indexes 
create unique index IDX_APP_AUTH_CODE_TOKEN on APP_AUTH_CODE (TOKEN)
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table APP_AUTH_CODE
  add constraint PK_APP_AUTH_CODE primary key (PSN_ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );


--原因（app token） 2017-10-25 LJ end


-- Create table
create table V_REGISTER_TEMP
(
  token       NUMBER(18) not null,
  email       VARCHAR2(200) not null,
  param       VARCHAR2(2000),
  status      NUMBER(2) not null,
  operator_id NUMBER(18) not null,
  psn_id      NUMBER(18),
  temp_type   NUMBER(2) not null,
  update_date DATE,
  create_date DATE
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table V_REGISTER_TEMP
  is '注册回调处理表';
-- Add comments to the columns 
comment on column V_REGISTER_TEMP.token
  is '主建';
comment on column V_REGISTER_TEMP.email
  is '邮件';
comment on column V_REGISTER_TEMP.param
  is '必要参数';
comment on column V_REGISTER_TEMP.status
  is '状态 0=新建；1=已同意等待注册；2=已处理；';
comment on column V_REGISTER_TEMP.operator_id
  is '操作人（生成该记录的人）';
comment on column V_REGISTER_TEMP.psn_id
  is '注册后生成的psnId';
comment on column V_REGISTER_TEMP.temp_type
  is '回调类型：1=群组邀请站外人员';
comment on column V_REGISTER_TEMP.update_date
  is '更新时间';
comment on column V_REGISTER_TEMP.create_date
  is '创建时间';
-- Create/Recreate indexes 
create index IDX_VRT_EMAIL_STATUS on V_REGISTER_TEMP (EMAIL, STATUS)
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_REGISTER_TEMP
  add constraint PK_V_REGISTER_TEMP primary key (TOKEN)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
  
create sequence SEQ_V_REGISTER_TEMP
minvalue 1
maxvalue 199999999999999999
start with 1
increment by 1
cache 20;



