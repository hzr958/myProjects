-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end




--SCM-15019 机构版指派成果pub_list表数据错误与rol_pub_xml节点数据错误修复任务 2017-11-08 zll start 
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
values(89,'HandlePubListTaskTrigger','0 0/1 * * * ?',0,'处理pub_list数据');
--SCM-15019 机构版指派成果pub_list表数据错误与rol_pub_xml节点数据错误修复任务 2017-11-08 zll end 

--原因（SCM-15052 认同了你的研究领域邮件的修改） 2017-11-13 ltl begin
update V_QUARTZ_CRON_EXPRESSION t set t.CRON_EXPRESSION = '0 0 21 * * ?' where t.cron_trigger_bean = 'noticeBeEndorseeAndEndorseTaskTrigger';
commit;
insert into v_Quartz_Cron_Expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (94, 'BaseRaRecmdTaskTrigger', '0 0/1 * * * ?', 1, '推荐研究领域的推荐任务(每6个月跑一次任务)')
commit;
--原因（SCM-15052 认同了你的研究领域邮件的修改） 2017-11-13 ltl end

--原因（创新城添加成果详情服务） 2017-11-17 zzx begin
insert into  v_open_token_service_const (id , token,service_type) values (seq_v_open_token_service_id.nextval  , 'cba4b03f','v5detail' ) ;
commit;
--原因（创新城添加成果详情服务） 2017-11-17 zzx end




--原因（SCM-15084，SCM-15110） 2017-11-21 LJ begin


insert into v_quartz_cron_expression values(90,'GenerateIKAnalyerDictTaskTrigger','*/10 * * * * ?',0,'拆分生成关键词');

insert into v_quartz_cron_expression values(91,'WriteIKAnalyerDictFileTaskTrigger','*/10 * * * * ?',0,'写入关键词到字典文件');


-- Create table
create table V_KEYWORDS_SYNONYM
(
  id            NUMBER not null,
  zh_keyword_id NUMBER,
  zh_keyword    VARCHAR2(200 CHAR),
  zh_kwtxt      VARCHAR2(200 CHAR),
  en_keyword_id NUMBER,
  en_keyword    VARCHAR2(200 CHAR),
  en_kwtxt      VARCHAR2(200)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 8K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table V_KEYWORDS_SYNONYM
  is '拆分中英文关键词记录表';
-- Add comments to the columns 
comment on column V_KEYWORDS_SYNONYM.zh_keyword_id
  is '中文关键词ID 和V_KEYWORDS_DIC表ID对应';
comment on column V_KEYWORDS_SYNONYM.zh_keyword
  is '中文关键词';
comment on column V_KEYWORDS_SYNONYM.zh_kwtxt
  is '格式化后的中文关键词';
comment on column V_KEYWORDS_SYNONYM.en_keyword_id
  is '英文关键词ID 和V_KEYWORDS_DIC表ID对应';
comment on column V_KEYWORDS_SYNONYM.en_keyword
  is '英文关键词';
comment on column V_KEYWORDS_SYNONYM.en_kwtxt
  is '格式化后的英文关键词';
-- Create/Recreate indexes 
create unique index IDX_V_KEYWORDS_SYNONYM_EID on V_KEYWORDS_SYNONYM (EN_KEYWORD_ID)
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
create index IDX_V_KEYWORDS_SYNONYM_EKW on V_KEYWORDS_SYNONYM (EN_KWTXT)
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
create unique index IDX_V_KEYWORDS_SYNONYM_ZID on V_KEYWORDS_SYNONYM (ZH_KEYWORD_ID)
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
create index IDX_V_KEYWORDS_SYNONYM_ZKW on V_KEYWORDS_SYNONYM (ZH_KWTXT)
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
alter table V_KEYWORDS_SYNONYM
  add constraint PK_V_KEYWORDS_SYNONYM primary key (ID)
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




-- Create table
create table V_KEYWORDS_DIC
(
  id                NUMBER(18) not null,
  keyword           VARCHAR2(200 CHAR) not null,
  keyword_text      VARCHAR2(200 CHAR) not null,
  ftures_word       VARCHAR2(200 CHAR),
  ftures_hash       NUMBER,
  kw_hash           NUMBER,
  kw_rhash          NUMBER,
  type              NUMBER(1),
  word_len          NUMBER(5),
  application_id    NUMBER(18),
  year              NUMBER(5),
  application_code1 VARCHAR2(20 CHAR),
  application_code2 VARCHAR2(20 CHAR)
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
comment on table V_KEYWORDS_DIC
  is '关键词字典，用于拆分成果标题、摘要等使用';
-- Add comments to the columns 
comment on column V_KEYWORDS_DIC.keyword
  is '关键词';
comment on column V_KEYWORDS_DIC.keyword_text
  is '关键词格式后的';
comment on column V_KEYWORDS_DIC.ftures_word
  is '连接特征字符';
comment on column V_KEYWORDS_DIC.ftures_hash
  is '连接特征字符hashcode';
comment on column V_KEYWORDS_DIC.kw_hash
  is '关键词hashcode';
comment on column V_KEYWORDS_DIC.kw_rhash
  is '关键词反序hashcode';
comment on column V_KEYWORDS_DIC.type
  is '英文1，中文2';
comment on column V_KEYWORDS_DIC.word_len
  is '关键词单词个数';
comment on column V_KEYWORDS_DIC.application_id
  is '申请号';
comment on column V_KEYWORDS_DIC.year
  is '申请年份';
comment on column V_KEYWORDS_DIC.application_code1
  is '申请代码1';
comment on column V_KEYWORDS_DIC.application_code2
  is '申请代码2';
-- Create/Recreate indexes 
create unique index IDX_V_KEYWORDS_DIC on V_KEYWORDS_DIC (FTURES_HASH)
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
create index IDX_V_KEYWORDS_DIC_AID on V_KEYWORDS_DIC (APPLICATION_ID)
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
create index UK_V_KEYWORDS_DIC on V_KEYWORDS_DIC (KW_HASH)
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
alter table V_KEYWORDS_DIC
  add constraint PK_V_KEYWORDS_DIC primary key (ID)
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
  

--原因（SCM-15084，SCM-15110） 2017-11-21 LJ end



--原因（SCM-15110，SQL漏提交） 2017-11-21 LJ begin

create sequence SEQ_v_keywords_dic
minvalue 1
maxvalue 999999999999999999999999999
start with 10001
increment by 1
nocache;


create sequence SEQ_v_keywords_synonym
minvalue 1
maxvalue 999999999999999999999999999
start with 10001
increment by 1
nocache;




--原因（SCM-15110，SQL漏提交） 2017-11-21 LJend

--原因（SCM-15200 通知项目负责人项目相关成果 ） 2017-11-27 zll begin
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) 
values(92,'SendPrjGrpEmailTaskTrigger','*/10 * * * * ?',1,'通知项目负责人项目相关成果 ');
--原因（SCM-15200 通知项目负责人项目相关成果 ） 2017-11-27 zll end--原因（有CQ号带上CQ号） 2016-12-8 WSN end




--原因（SCM-15168，项目群组、课程群组：公开主页权限中的模块 ） 2017-11-28  ajb begin
  -- Alter table 
alter table V_GRP_CONTROL
  storage
  (
    next 1
  )
;
-- Add/modify columns 
alter table V_GRP_CONTROL add is_prj_pub_show NUMBER(1) default 1;
alter table V_GRP_CONTROL add is_prj_ref_show NUMBER(1) default 1;
alter table V_GRP_CONTROL add is_curware_file_show NUMBER(1) default 1;
alter table V_GRP_CONTROL add is_work_file_show NUMBER(1) default 1;
-- Add comments to the columns 
comment on column V_GRP_CONTROL.is_prj_pub_show
  is '群组外成员是否显示项目成果 [1=是 ， 0=否 ] 默认1';
comment on column V_GRP_CONTROL.is_prj_ref_show
  is '群组外成员是否显示项目文献 [1=是 ， 0=否 ] 默认1';
comment on column V_GRP_CONTROL.is_curware_file_show
  is '群组外成员是否显示课件 [1=是 ， 0=否 ] 默认1';
comment on column V_GRP_CONTROL.is_work_file_show
  is '群组外成员是否显示作业 [1=是 ， 0=否 ] 默认1';
commit ;

--原因（SCM-15168，项目群组、课程群组：公开主页权限中的模块 ） 2017-11-28  ajb end

--原因（有CQ号带上CQ号） 2016-12-8 WSN end



--原因（SCM-15110，SQL漏提交） 2017-11-21 LJ begin

create sequence SEQ_v_keywords_dic
minvalue 1
maxvalue 999999999999999999999999999
start with 10001
increment by 1
nocache;


create sequence SEQ_v_keywords_synonym
minvalue 1
maxvalue 999999999999999999999999999
start with 10001
increment by 1
nocache;




--原因（SCM-15110，SQL漏提交） 2017-11-21 LJ  end


--原因（SCM-15147） 2017-11-29 LJ begin

ALTER TABLE v3sns.tmp_pub_pdwh ADD  (match_counts NUMBER(2) DEFAULT 0);


--原因（SCM-15147） 2017-11-29 LJ  end

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



--SCM-15071 检索机构主页页面 2017-11-30 WSN begin


--------------------机构信息统计表---------------begin

-- Create table
create table V_INS_STATISTICS
(
  ins_id        NUMBER(9) not null,
  prj_sum       NUMBER default 0,
  pub_sum       NUMBER default 0,
  psn_sum       NUMBER default 0,
  like_sum      NUMBER default 0,
  share_sum     NUMBER default 0,
  zh_name       VARCHAR2(100 CHAR),
  en_name       VARCHAR2(200 CHAR)
);


-- Add comments to the table 
comment on table V_INS_STATISTICS
  is '单位统计信息表';
-- Add comments to the columns 
comment on column V_INS_STATISTICS.prj_sum
  is '项目数';
comment on column V_INS_STATISTICS.pub_sum
  is '成果数';
comment on column V_INS_STATISTICS.psn_sum
  is '人员数';
comment on column V_INS_STATISTICS.like_sum
  is '赞统计数';
comment on column V_INS_STATISTICS.share_sum
  is '分享统计数';
comment on column V_INS_STATISTICS.zh_name
  is '单位中文名';
comment on column V_INS_STATISTICS.en_name
  is '单位英文名';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_INS_STATISTICS
  add constraint PK_INS_STATISTICS primary key (INS_ID);
  
  
insert into v_ins_statistics(ins_id, prj_sum, pub_sum, psn_sum, zh_name, en_name) select t.ins_id, t.prj_num, t.pub_num, t.psn_num, t.zh_name, t.en_name from rol2.ins_statistics t;
commit;
  
--------------------机构信息统计表---------------end
  
--------------------机构赞操作表-----------------begin

-- Create table
create table V_INS_AWARD_PSN
(
  record_id      NUMBER(18) not null,
  ins_id       NUMBER(18) not null,
  awarder_psn_id  NUMBER(18) not null,
  award_date     DATE not null,
  status         NUMBER(1) default 0 not null
);
-- Add comments to the table 
comment on table V_INS_AWARD_PSN
  is '人员赞资源记录';
-- Add comments to the columns 
comment on column V_INS_AWARD_PSN.record_id
  is '主键';
comment on column V_INS_AWARD_PSN.ins_id
  is '机构ID';
comment on column V_INS_AWARD_PSN.awarder_psn_id
  is '赞人员ID';
comment on column V_INS_AWARD_PSN.award_date
  is '赞时间';
comment on column V_INS_AWARD_PSN.status
  is '1--赞，0--取消赞。java代码判断使用枚举类LikeStatusEnum.LIKE和LikeStatusEnum.UNLIKE';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_INS_AWARD_PSN
  add constraint PK_INS_AWARD_PSN primary key (RECORD_ID);
-- Create/Recreate indexes 
create index IDX_INS_AWARD_PSN on V_INS_AWARD_PSN (INS_ID, AWARDER_PSN_ID);


create sequence SEQ_PSN_AWARD_INS
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;
 
-------------------机构赞操作表-----------------end

--机构表信息更新后台任务
insert into V_QUARTZ_CRON_EXPRESSION values(93, 'UpdateInsInfoFromSIETaskTrigger', '0 0 22 * * ?', 1, '每天晚上9点从ROL更新SNS的institution和v_ins_statistics表');
commit;

--SCM-15071 检索机构主页页面 2017-11-30 WSN end

--原因（SCM-14409） 2017-11-30 HCJ begin
--文件下载记录表添加表字段owner_psn_id
ALTER TABLE v_file_download_record ADD (owner_psn_id NUMBER(18,0));
COMMENT on column v_file_download_record.OWNER_PSN_ID is '文件所有人id';
commit;
--更新文件类型为新的数字类型，对应枚举类FileTypeEnum
UPDATE v_file_download_record SET file_type='0' WHERE file_type='psnfile';
UPDATE v_file_download_record SET file_type='1' WHERE file_type='groupfile';
UPDATE v_file_download_record SET file_type='2' WHERE file_type='fulltextfile';
UPDATE v_file_download_record SET file_type='3' WHERE file_type='pdfulltextwhfile';
COMMENT ON COLUMN v_file_download_record.file_type IS '文件类型：0-个人文件，1-群组文件，2-个人库全文，3-基准库全文；对应java类型枚举类FileTypeEnum';
commit;
--原文件下载记录表的file_id记录的时对应文件类型的id，全文下载记录的是pub_id，个人文件记录的是psn_file_id，群组文件记录的是group_file_id

--v_file_download_record表file_type为'0'的记录,
--关联表v_psn_file中将其中的，
--owner_psn_id更新到表v_file_download_record字段owner_psn_id，
--archive_file_id更新到表v_file_download_record字段file_id
UPDATE v_file_download_record r 
SET
r.owner_psn_id=(SELECT p1.owner_psn_id 
                FROM v_psn_file p1 
                WHERE p1.ID = r.file_id),
r.file_id=(SELECT p2.archive_file_id
                FROM v_psn_file p2 
                WHERE p2.ID = r.file_id)
WHERE r.file_type='0';
commit;

--v_file_download_record表file_type为'1'的记录,
--关联表v_grp_file中将其中的，
--upload_psn_id更新到表v_file_download_record字段owner_psn_id，
--archive_file_id更新到表v_file_download_record字段file_id
UPDATE v_file_download_record r 
SET
r.owner_psn_id=(SELECT p1.upload_psn_id 
                FROM v_grp_file p1 
                WHERE p1.GRP_FILE_ID = r.file_id),
r.file_id=(SELECT p2.archive_file_id
                FROM v_grp_file p2 
                WHERE p2.GRP_FILE_ID = r.file_id)
WHERE r.file_type='1';
commit;

--删除filetype为2但全文已被删除的记录
delete FROM v_file_download_record r 
where r.file_id not in(select t.file_id FROM v_file_download_record t 
JOIN pub_fulltext p ON t.file_id=p.pub_id
WHERE t.file_type='2') AND r.file_type='2';
commit;

--v_file_download_record表file_type为'2'的记录,
--关联表publication中将其中的，
--owner_psn_id更新到表v_file_download_record字段owner_psn_id，
--关联表pub_fulltext将其中的archive_file_id更新到表v_file_download_record字段file_id
UPDATE v_file_download_record r 
SET
r.owner_psn_id=(SELECT p1.OWNER_PSN_ID 
                FROM publication p1 
                WHERE p1.pub_id = r.file_id),
r.file_id=(SELECT p2.FULLTEXT_FILE_ID
                FROM pub_fulltext p2 
                WHERE p2.pub_id = r.file_id)
WHERE r.file_type='2';
commit;

--原因（SCM-14409） 2017-11-30 HCJ  end

--原因（SCM-15023） 2017-12-05 lhd  begin
update psn_statistics t set t.pub_sum=0 where t.pub_sum is null;
update psn_statistics t set t.cited_sum=0 where t.cited_sum is null;
update psn_statistics t set t.hindex=0 where t.hindex is null;
update psn_statistics t set t.zh_sum=0 where t.zh_sum is null;
update psn_statistics t set t.en_sum=0 where t.en_sum is null;
update psn_statistics t set t.prj_sum=0 where t.prj_sum is null;
update psn_statistics t set t.frd_sum=0 where t.frd_sum is null;
update psn_statistics t set t.group_sum=0 where t.group_sum is null;
update psn_statistics t set t.pub_award_sum=0 where t.pub_award_sum is null;
update psn_statistics t set t.patent_sum=0 where t.patent_sum is null;
update psn_statistics t set t.pcfpub_sum=0 where t.pcfpub_sum is null;
update psn_statistics t set t.pub_fulltext_sum=0 where t.pub_fulltext_sum is null;
update psn_statistics t set t.open_pub_sum=0 where t.open_pub_sum is null;
update psn_statistics t set t.open_prj_sum=0 where t.open_prj_sum is null;
--原因（SCM-15023） 2017-12-05 lhd  end

--原因（SCM-15193 ） 2017-12-8 LJ begin

insert into v_quartz_cron_expression values(95,'AddFileSizeIntoArchiveFilesTaskTrigger','*/30 * * * * ?',0,'处理archivefile表文件大小字段为0或空的数据');


--原因（SCM-15193 ） 2017-12-8 LJ end
--原因（SCM-15193 ） 2017-12-8 LJ begin

alter table v_keywords_dic add scm_category_id VARCHAR2(100 CHAR);
comment on column v_keywords_dic.scm_category_id
  is '对应的category_map_scm_nsfc表 SCM关键词Id ';

--原因（SCM-15193 ） 2017-12-8 LJ end
  
--原因（    SCM-15082   个人设置迁移 ） 2017-12-12  ajb  start
  
  
 delete   from CONST_MAIL_TYPE  ;


commit ;

--原因（    SCM-15082   个人设置迁移 ） 2017-12-12  ajb  start



--原因（    SCM-15082   个人设置迁移 ） 2017-12-12  ajb  start

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (9, '好友论文动态更新', '', 'Friends Article Updates', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (10, '市场推广', '', 'Promotion email', 0);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (1, '邀请加为好友', '1', 'Friend request', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (2, '邀请加入群组', '2', 'Group request', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (3, '接受好友邀请通知', '', 'Accept Friend Request Notification', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (4, '群组通知', '2', 'Group Notification', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (5, '申请加入群组', '2', 'Join Group Request', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (6, '好友动态更新', '1', 'Friends Recent Updates', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (7, '好友推荐通知', '1', 'Suggested Friends', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (8, '好友工作更新', '', 'Friends New Job', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (13, '基金机会推荐', '3', 'Suggested Funding Opportunities', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (14, '期刊推荐', '3', 'Suggested Journals', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (15, '论文推荐', '3', 'Suggested Articles', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (16, '文件分享', '', 'Shared File', 0);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (11, '群组新增文件', '2', 'Group New File', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (12, '群组文件邀请', '2', 'Group File Invitation', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (17, '认同领域', '3', 'Research Area Endorsement', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (18, '收到评论', '', 'Receive Comments', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (19, '科研影响力分析统计', '4', 'Research Impact Statistics', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (20, '资源分享', '5', 'Shared Resource', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (21, '邮件验证', '5', 'Email Verification', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (22, '首要邮件设置', '5', 'Primary Email Confirmation', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (23, '登录帐号设置', '5', 'Account Change', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (24, '简历分享', '', 'Shared Smart CV', 0);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (25, '合作者推荐', '3', 'Suggested Co-authors', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (26, '成果认领', '4', 'Publication Confrimation', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (27, '站内消息', '5', 'Message', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (28, '领域推荐', '', 'Keyword Recommend', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (29, '上传全文', '4', 'Upload Fulltext', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (31, '引用更新', '4', 'Update Citation', 1);

insert into const_mail_type (MAIL_TYPE_ID, TYPE_ZH_NAME, REMARK, TYPE_EN_NAME, STATUS)
values (30, '全文请求', '', 'Fulltext Request', 1);

commit ;
--原因（    SCM-15082   个人设置迁移 ） 2017-12-12  ajb  end





--原因（SCM-15405） 2017-12-18 LJ begin
insert into v_quartz_cron_expression values(96,'SplitPatentCategoryTaskTrigger','*/10 * * * * ?',0,'获取基准库专利xml专利分类信息');
insert into v_quartz_cron_expression values(97,'SimplePdwhPubIndexTaskTrigger','0 0/1 * * * ?',0,'创建pdwh成果solr索引，简易版');
insert into v_quartz_cron_expression values(98,'SimpleSnsPubIndexTaskTrigger','0 0/1 * * * ?',0,'创建sns成果solr索引，简易版');
insert into v_quartz_cron_expression values(99,'PreGenerateShortUrlTaskTrigger','0 0/1 * * * ?',0,'预生成短地址任务');

--原因（SCM-15405） 2017-12-18 LJ end



--原因（    SCM-15543个人设置页面翻译问题请修改） 2017-12-19 ajb  begin
update  CONST_MAIL_TYPE t   set  t.type_en_name = 'Friend Request'    where t.mail_type_id = 1 ;
update  CONST_MAIL_TYPE t   set  t.type_en_name = 'Group  Request'    where t.mail_type_id = 2 ;
update  CONST_MAIL_TYPE t   set  t.type_en_name = 'Publication Confirmation'    where t.mail_type_id = 26 ;

commit ;
--原因（    SCM-15543个人设置页面翻译问题请修改） 2017-12-19 ajb  end