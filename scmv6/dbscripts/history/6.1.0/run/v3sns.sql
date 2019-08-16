-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end




--原因（    SCM-16106 首页动态显示逻辑判断不正确，个人设置） 2018-01-16 ajb start

insert   into CONST_DICTIONARY( category , Code  , En_Us_Caption   ,ZH_TW_CAPTION , ZH_CN_CAPTION  , SEQ_NO )   values ( 'privacy_permissions' , '4'   ,   'Follow Me'  ,'关注我的人' ,'关注我的人' ，'4' )  ;
-- Alter table 
alter table PRIVACY_SETTINGS
  storage
  (
    next 8
  )
;
-- Add comments to the columns 
comment on column PRIVACY_SETTINGS.permission
  is '查看权限0=任何人1=好友2=仅本人 4=关注我的人';

update privacy_settings  t set    t.permission = 4 where  t.privacy_action = 'vMyLiter'  and  t.permission !=2 ;

--原因（    SCM-16106 首页动态显示逻辑判断不正确，个人设置） 2018-01-16 ajb end



--原因（修改 privacy_settings 表的备注信息 ） 2018-01-15 ajb start

-- Alter table 
alter table PRIVACY_SETTINGS
  storage
  (
    next 8
  )
;
-- Add comments to the columns 
comment on column PRIVACY_SETTINGS.permission
  is '查看权限0=任何人1=好友2=仅本人 4=关注我的人[0 ,1 ,3 在文献动态权限中和 4 的意义相同]';

commit  ; 

--原因（修改 privacy_settings 表的备注信息 ） 2018-01-15 ajb end


--原因（    SCM-15541temp_code:241，群组新增成果，成员没有收到邮件） 2018-01-25 AJB begin

update  V_QUARTZ_CRON_EXPRESSION t set t.status = 1    where t.cron_trigger_bean = 'groupPublcationEmailTaskTrigger' ;

commit ;

--原因（    SCM-15541temp_code:241，群组新增成果，成员没有收到邮件） 2018-01-25 AJB end


--原因（    SCM-15547tmp_code:55、56 ，新增群组文件通知，没有收到邮件）2018-01-26 ajb start


-- Create sequence 
create sequence SEQ_EMAIL_GROUP_FILE_PSN
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 10;


insert into V_QUARTZ_CRON_EXPRESSION values (112 ,'grpFileEmailTaskTrigger' , '0 0/1 * * * ?'  , 1 ,'群组添加文件，给组员发送邮件任务')  ;


-- Create table
create table EMAIL_GRP_FILE_PSN
(
  id               NUMBER(18) not null,
  psn_id           NUMBER(18) not null,
  grp_id           NUMBER(18) not null,
  grp_file_id      NUMBER(18),
  create_date      DATE default sysdate,
  status           NVARCHAR2(5) default 0,
  file_module_type NUMBER(1)
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
comment on table EMAIL_GRP_FILE_PSN
  is '群组文件，发邮件表，一天只发送一封邮件';
-- Add comments to the columns 
comment on column EMAIL_GRP_FILE_PSN.id
  is '主键';
comment on column EMAIL_GRP_FILE_PSN.psn_id
  is '上传文件的psnid';
comment on column EMAIL_GRP_FILE_PSN.grp_id
  is '群组id';
comment on column EMAIL_GRP_FILE_PSN.grp_file_id
  is '群组文件id';
comment on column EMAIL_GRP_FILE_PSN.create_date
  is '创建日期';
comment on column EMAIL_GRP_FILE_PSN.status
  is '0=未发送 ， 1=发送成功，  99 =发送失败';
comment on column EMAIL_GRP_FILE_PSN.file_module_type
  is '0: 群组文件;  2==课件:';
-- Create/Recreate primary, unique and foreign key constraints 
alter table EMAIL_GRP_FILE_PSN
  add constraint PK_EMAIL_GRP_FILE_PSN primary key (ID)
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
  
  commit ;


--原因（    SCM-15547tmp_code:55、56 ，新增群组文件通知，没有收到邮件）2018-01-26 ajb end



--原因（    SCM-16347  新加帐号密码验证接口） 2018-02-07 ajb begin

insert into   v_open_token_service_const( id , token , service_type  , descr )  values (  675 , '00000000' , 'usn22pwd',  '检查用户名和密码')  ;
commit ;

--原因（    SCM-16347  新加帐号密码验证接口） 2018-02-07 ajb end

--原因（       SCM-16317 关闭群组文件邮件任务） 2018-02-07 ajb begin

update  v_quartz_cron_expression  t set    t.status =  0   where t.cron_trigger_bean   = 'grpFileEmailTaskTrigger' ;

commit ;

--原因（       SCM-16317关闭群组文件邮件任务） 2018-02-07 ajb end

--原因（    SCM-8791  增加微信登录） 2018-02-07 wcw begin
alter table V_WECHAT_RELATION add WECHAT_UNIONID VARCHAR2(50) default '0' not null;
comment on column V_WECHAT_RELATION.WECHAT_UNIONID
  is '微信unionid';
alter table V_WECHAT_RELATION add BIND_TYPE number(1) default '0' not null;
comment on column V_WECHAT_RELATION.BIND_TYPE
  is '绑定方式：0-微信端 1-PC端 PC端通过开放平台，无openId';
--原因（    SCM-8791  增加微信登录） 2018-02-07 wcw end

--原因（    SCM-16389 修改V_WECHAT_RELATION表上的唯一索引 ） 2018-02-23 wcw begin
drop index idx_v_wechat_relation_oid;
create index IDX_V_WECHAT_RELATION_OPENID on V_WECHAT_RELATION(WECHAT_OPENID);
create index IDX_V_WECHAT_RELATION_UNIONID on V_WECHAT_RELATION(WECHAT_UNIONID);
--原因（    SCM-16389 修改V_WECHAT_RELATION表上的唯一索引 ） 2018-02-23 wcw end



--原因（        SCM-16226 帐号邮箱验证） 2018-03-05 ajb begin

-- Create table
create table V_ACCOUNT_EMAIL_CHECK_LOG
(
  id                NUMBER(18) not null,
  psn_id            NUMBER(18) not null,
  account           VARCHAR2(50) not null,
  send_time         DATE not null,
  deal_status       NUMBER(1) default 0,
  deal_time         DATE,
  validate_code     VARCHAR2(10) not null,
  validate_code_big VARCHAR2(40) not null,
  deal_type         NUMBER(1)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 8
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table V_ACCOUNT_EMAIL_CHECK_LOG
  is '帐号邮箱验证 验证记录';
-- Add comments to the columns 
comment on column V_ACCOUNT_EMAIL_CHECK_LOG.id
  is '主键';
comment on column V_ACCOUNT_EMAIL_CHECK_LOG.psn_id
  is ' 人员id';
comment on column V_ACCOUNT_EMAIL_CHECK_LOG.account
  is '账号邮箱 ， 邮件接收邮箱';
comment on column V_ACCOUNT_EMAIL_CHECK_LOG.send_time
  is '发送邮件时间';
comment on column V_ACCOUNT_EMAIL_CHECK_LOG.deal_status
  is '处理状态  0=未处理 ， 1验证成功 ， 9=验证失败 ， 2=重新发送';
comment on column V_ACCOUNT_EMAIL_CHECK_LOG.deal_time
  is '处理时间';
comment on column V_ACCOUNT_EMAIL_CHECK_LOG.validate_code
  is ' 产生验证吗 随机8位 数字或者字母';
comment on column V_ACCOUNT_EMAIL_CHECK_LOG.validate_code_big
  is ' 邮件地址链接专用验证 uuid+psnId+32位随机码做md5--- 32位';
comment on column V_ACCOUNT_EMAIL_CHECK_LOG.deal_type
  is '1邮件链接验证， 2系统弹框验证';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_ACCOUNT_EMAIL_CHECK_LOG
  add constraint PK_V_ACCOUNT_EMAIL_CHECK_LOG unique (ID)
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
-- Create/Recreate indexes 
create index IDX_V_ACCOUNT_EMAIL_CK_ACT on V_ACCOUNT_EMAIL_CHECK_LOG (ACCOUNT)
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
create index IDX_V_ACCOUNT_EMAIL_CK_PSN_ID on V_ACCOUNT_EMAIL_CHECK_LOG (PSN_ID)
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


-- Create sequence 
create sequence SEQ_V_ACCOUNT_EMAIL_CHECK_LOG
minvalue 1
maxvalue 99999999999999999999
start with 21
increment by 1
cache 10
order;


commit ;

--原因（        SCM-16226 帐号邮箱验证） 2018-03-05 ajb  end


--原因（修改表字段注释） 2017-03-09 zll begin

comment on column v_file_download_record.FILE_ID is '对应文件类型的文件记录表id';

--原因（修改表字段注释） 2017-03-09 zll begin



--原因（调整OPENID 生成方案） 2018-03-12 tsz begin
-- Create table
create table V_PSN_OPEN
(
  psn_id  NUMBER(18) not null,
  open_id NUMBER(8)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 1
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns 
comment on column V_PSN_OPEN.psn_id
  is 'psn_id';
comment on column V_PSN_OPEN.open_id
  is 'open_id';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_PSN_OPEN
  add constraint PSN_OPEN_PRY_KEY primary key (PSN_ID)
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
-- Create/Recreate indexes 
create unique index PSN_OPEN_INDEX_1 on V_PSN_OPEN (OPEN_ID)
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

-- Create sequence 
create sequence V_SEQ_OPEN_ID
minvalue 10000001
maxvalue 99999998
start with 10000001
increment by 1
cache 20;


insert into v_open_token_service_const values(seq_v_open_token_service_id.nextval,'00000000','4kxiw2p0',0,sysdate,null);
commit;

--原因（调整OPENID 生成方案） 2018-03-12 tsz end




--原因（SCM-15553站内信，发送邮件通知 ，添加字段） 2018-03-13 ajb start

-- Alter table 
alter table V_MSG_RELATION
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table V_MSG_RELATION add send_email NUMBER(1) default 0;
alter table V_MSG_RELATION add send_email_date date;
-- Add comments to the columns 
comment on column V_MSG_RELATION.send_email
  is '发送邮件， 0==没有发送，1==发送';
-- Alter table 
alter table V_MSG_RELATION
  storage
  (
    next 8
  )
;
-- Add comments to the columns 
comment on column V_MSG_RELATION.send_email_date
  is '发送邮件，时间';


commit  ;

--原因（SCM-15553站内信，发送邮件通知 ，添加字段） 2018-03-13 ajb  end--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（       SCM-16582  open接口增加服务类型 ） 2018-03-09 ajb begin

insert into   v_open_token_service_const( id , token , service_type  , descr )  values (  676 , '00000000' , 'pdh22jid',  'issn获取 期刊 ID')  ;
commit ;

--原因（       SCM-16582  open接口增加服务类型 ） 2018-03-09 ajb end




--原因（SCM-15283 认同关键词） 2018-3-16 ltl begin
create table KEYWORD_AGREE_STATUS
(
  id              VARCHAR2(18) not null,
  psn_id          NUMBER not null,
  KW_ID           NUMBER not null,
  friend_id       NUMBER not null,
  op_date         DATE default sysdate,
  status          NUMBER(5)
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
comment on table KEYWORD_AGREE_STATUS
  is '认同关键邮件生成状态记录表';
-- Add comments to the columns 
comment on column KEYWORD_AGREE_STATUS.id
  is '与认同记录表ID一致';
comment on column KEYWORD_AGREE_STATUS.psn_id
  is '被认同人';
comment on column KEYWORD_AGREE_STATUS.KW_ID
  is '个人关键词ID';
comment on column KEYWORD_AGREE_STATUS.friend_id
  is '认同人员';
comment on column KEYWORD_AGREE_STATUS.op_date
  is '认同时间';
comment on column KEYWORD_AGREE_STATUS.status
  is '0表示未生成，1生成失败，2成功';
-- Create/Recreate indexes 
create index PK_KEYWORD_AGREE_STATUS on KEYWORD_AGREE_STATUS (ID)
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
  --原因（SCM-15283 认同关键词） 2018-3-16 ltl end





--原因（SCM-16544 论文SEO策略调整） 2018-03-20 zll begin

delete from pub_index_second_level t;

-- Alter table 
alter table PUB_INDEX_SECOND_LEVEL
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table PUB_INDEX_SECOND_LEVEL add title_type number(1);
-- Add comments to the columns 
comment on column PUB_INDEX_SECOND_LEVEL.title_type
  is '标题类型 1 中文标题；2 英文标题';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PUB_INDEX_SECOND_LEVEL
  drop constraint PK_SL cascade;
alter table PUB_INDEX_SECOND_LEVEL
  add constraint PK_SL primary key (PUB_ID, TITLE_TYPE)
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
--原因（SCM-16544 论文SEO策略调整） 2018-03-20 zll begin



  
  
  --原因（    SCM-16643 群组文件加标签功能开发） 2018-3-20  ajb start
  -- Create table
create table V_GRP_LABEL
(
  label_id      NUMBER(18) not null,
  grp_id        NUMBER(18) not null,
  label_name    VARCHAR2(50) not null,
  create_psn_id NUMBER(18) not null,
  create_date   DATE not null,
  status        NUMBER(1) default 0 not null,
  update_psn_id NUMBER(18),
  update_date   DATE
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 1
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table V_GRP_LABEL
  is '群组标签类';
-- Add comments to the columns 
comment on column V_GRP_LABEL.label_id
  is '主键';
comment on column V_GRP_LABEL.grp_id
  is '群组id';
comment on column V_GRP_LABEL.label_name
  is '标签名';
comment on column V_GRP_LABEL.create_psn_id
  is '标签创建人';
comment on column V_GRP_LABEL.create_date
  is '标签创建时间';
comment on column V_GRP_LABEL.status
  is '0=正常， 1=删除';
comment on column V_GRP_LABEL.update_psn_id
  is '标签更新人';
comment on column V_GRP_LABEL.update_date
  is '标签更新时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_GRP_LABEL
  add constraint PK_V_GRP_LABEL primary key (LABEL_ID)
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
-- Create/Recreate indexes 
create index IDX_V_GRP_LABEL_GRP_ID on V_GRP_LABEL (GRP_ID)
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


-- Create sequence 
create sequence SEQ_V_GRP_LABEL
minvalue 1
maxvalue 999999999999999999
start with 62
increment by 1
cache 10;

-- Create table
create table V_GRP_FILE_LABEL
(
  id            NUMBER(18) not null,
  grp_file_id   NUMBER(18) not null,
  grp_label_id  NUMBER(18) not null,
  create_psn_id NUMBER(18) not null,
  create_date   DATE not null,
  status        NUMBER(1) default 0 not null,
  update_psn_id NUMBER(18),
  update_date   DATE
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 8
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table V_GRP_FILE_LABEL
  is '群组文件 与群组标签关系';
-- Add comments to the columns 
comment on column V_GRP_FILE_LABEL.id
  is '主键';
comment on column V_GRP_FILE_LABEL.grp_file_id
  is '群组文件id';
comment on column V_GRP_FILE_LABEL.grp_label_id
  is '群组标签id';
comment on column V_GRP_FILE_LABEL.create_psn_id
  is '关系创建时间';
comment on column V_GRP_FILE_LABEL.create_date
  is '关系创建时间';
comment on column V_GRP_FILE_LABEL.status
  is '0=正常， 1=删除';
comment on column V_GRP_FILE_LABEL.update_psn_id
  is '关系更新人';
comment on column V_GRP_FILE_LABEL.update_date
  is '关系更时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_GRP_FILE_LABEL
  add constraint PK_V_GRP_FILE_LABEL primary key (ID)
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
-- Create/Recreate indexes 
create index IDX_V_GRP_FILE_LABEL_BASE_ID on V_GRP_FILE_LABEL (GRP_LABEL_ID)
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
create index IDX_V_GRP_FILE_LABEL_FILE_ID on V_GRP_FILE_LABEL (GRP_FILE_ID)
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

-- Create sequence 
create sequence SEQ_V_GRP_FILE_LABEL
minvalue 1
maxvalue 999999999999999999
start with 132
increment by 1
cache 10;

commit ;

--原因（    SCM-16643 群组文件加标签功能开发） 2018-3-20  ajb end

--原因（SCM-16544 论文SEO策略调整） 2018-03-22 zll begin
-- Alter table 
alter table PUB_INDEX_SECOND_LEVEL
  storage
  (
    next 8
  )
;
-- Create/Recreate primary, unique and foreign key constraints 
alter table PUB_INDEX_SECOND_LEVEL
  add constraint pk_index_second_level primary key (PUB_ID, TITLE_TYPE);
-- Drop indexes 
drop index PK_SL;
--原因（SCM-16544 论文SEO策略调整） 2018-03-22 zll end


--原因（    SCM-16714个人设置》邮件设置,更新常量字段） 2018-03-23 ajb start

update  CONST_MAIL_TYPE t set t.type_zh_name='认同关键词' ,t.type_en_name='Keyword Endorsement' where t.mail_type_id = 17 ;
commit ;

--原因（    SCM-16714个人设置》邮件设置,更新常量字段） 2018-03-23 ajb end




--原因（给sie系统添加服务常量） 2018-03-23 ajb start

insert into   v_open_token_service_const( id , token , service_type  , descr )  values (  730 , '11111111' , 'pdh22jid',  'issn获取 期刊 ID')  ;

commit ;
--原因（给sie系统添加服务常量） 2018-03-23 ajb end

--原因（SCM-16561 用户的首页邮箱设置有两个，另外lhd的添加关键词推荐任务） 2018-03-24 ltl begin

update PSN_EMAIL a set a.first_mail = 0
where a.rowid !=  
(  
select max(b.rowid) from PSN_EMAIL b  
where a.psn_id = b.psn_id and  
a.first_mail = b.first_mail and first_mail =1 and a.email = b.email
); 

insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(154,'HPubKeywordsRcmdTask1',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(155,'HPubKeywordsRcmdTask2',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(156,'HPubKeywordsRcmdTask3',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(157,'HPubKeywordsRcmdTask4',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(158,'HPubKeywordsRcmdTask5',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(159,'HPubKeywordsRcmdTask6',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(160,'HPubKeywordsRcmdTask7',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(161,'HPubKeywordsRcmdTask8',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(162,'HPubKeywordsRcmdTask9',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(163,'HPubKeywordsRcmdTask10',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(164,'HPubKeywordsRcmdTask11',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(165,'HPubKeywordsRcmdTask12',0);

insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(166,'HPubKeywordsRcmdTask13',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(167,'HPubKeywordsRcmdTask14',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(168,'HPubKeywordsRcmdTask15',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(169,'HPubKeywordsRcmdTask16',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(170,'HPubKeywordsRcmdTask17',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(171,'HPubKeywordsRcmdTask18',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(172,'HPubKeywordsRcmdTask19',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(173,'HPubKeywordsRcmdTask20',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(174,'HPubKeywordsRcmdTask21',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(175,'HPubKeywordsRcmdTask22',0);

insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(176,'HPubKeywordsRcmdTask23',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(177,'HPubKeywordsRcmdTask24',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(178,'HPubKeywordsRcmdTask25',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(179,'HPubKeywordsRcmdTask26',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(180,'HPubKeywordsRcmdTask27',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(181,'HPubKeywordsRcmdTask28',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(182,'HPubKeywordsRcmdTask29',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(183,'HPubKeywordsRcmdTask30',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(184,'HPubKeywordsRcmdTask31',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(185,'HPubKeywordsRcmdTask32',0);

insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(186,'HPubKeywordsRcmdTask33',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(187,'HPubKeywordsRcmdTask34',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(188,'HPubKeywordsRcmdTask35',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(189,'HPubKeywordsRcmdTask36',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(190,'HPubKeywordsRcmdTask37',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(191,'HPubKeywordsRcmdTask38',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(192,'HPubKeywordsRcmdTask39',0);
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(193,'HPubKeywordsRcmdTask40',0);

commit;
--原因（SCM-16561 用户的首页邮箱设置有两个） 2018-03-24 ltl end
--原因（SCM-16862 修改项目字段类型） 2018-03-26 zzx begin


 alter table project modify (scheme_agency_name_en varchar2(300 char));
 alter table project modify (scheme_name_en varchar2(200 char));
 commit;

--原因（SCM-16862 修改项目字段类型） 2018-03-26 zzx end




--原因（    SCM-16869 群组》文件标签  , 添加索引  ） 2018-03-27  ajb  start
-- Alter table 
alter table V_GRP_FILE_LABEL
  storage
  (
    next 8
  )
;
-- Create/Recreate indexes 
create index IDX_V_GRP_FILE_LABEL_F_B_S on V_GRP_FILE_LABEL (grp_file_id, grp_label_id, status);

commit ;

--原因（    SCM-16869 群组》文件标签  , 添加索引  ） 2018-03-27  ajb  end

--原因（SCM-16881） 2018-03-27 lj begin

-- Create table
create table PERSON_PM_NAME
(
  id     NUMBER(18) not null,
  name   VARCHAR2(300 CHAR) not null,
  psn_id NUMBER(18) not null,
  type   NUMBER(1) default 1 not null,
  ins_id NUMBER(10)
)
tablespace V3SNS
  pctfree 0
  initrans 1
  maxtrans 255
  storage
  (
    initial 16
    next 8
    minextents 1
    maxextents unlimited
  )
compress;
-- Add comments to the table 
comment on table PERSON_PM_NAME
  is 'SNS人员姓名和姓名别称信息记录表';
-- Add comments to the columns 
comment on column PERSON_PM_NAME.id
  is ' 主键id';
comment on column PERSON_PM_NAME.name
  is '姓名';
comment on column PERSON_PM_NAME.psn_id
  is '人员Id';
comment on column PERSON_PM_NAME.type
  is '姓名类型（各种组合）
   Full_name=1;prefix_name=2;addt_initname=3;addt_fullname=4;init_name=5;zh_name=6;addt_name=7';
comment on column PERSON_PM_NAME.ins_id
  is '机构Id';
-- Create/Recreate indexes 
create index IDX_PERSON_PM_NAME on PERSON_PM_NAME (NAME)
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
create index IDX_PERSON_PM_NAME_PSN on PERSON_PM_NAME (PSN_ID)
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
create unique index PK_PERSON_PM_NAME on PERSON_PM_NAME (ID)
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



create sequence SEQ_PERSON_PM_NAME
minvalue 1
maxvalue 9999999999999999999999999999
start with 10001
increment by 1
cache 20;


insert into v_quartz_cron_expression
  (id, cron_trigger_bean, cron_expression, status, description)
values
  (152, 'PdwhPubAddrAuthorMatchTaskTrigger', '*/10 * * * * ?', 0, '基准库成果地址作者匹配任务（历史数据处理）');


--原因（SCM-16881） 2018-03-27 lj  end
--原因（SCM-16595 修改来源字段） 2018-3-27 ltl begin
delete from v_quartz_cron_expression where  cron_trigger_bean='UpdateSnsPubBriefTaskTrigger1';
delete from v_quartz_cron_expression where  cron_trigger_bean='UpdateSnsPubBriefTaskTrigger2';
delete from v_quartz_cron_expression where  cron_trigger_bean='UpdateSnsPubBriefTaskTrigger3';
delete from v_quartz_cron_expression where  cron_trigger_bean='UpdateSnsPubBriefTaskTrigger4';
delete from v_quartz_cron_expression where  cron_trigger_bean='UpdateSnsPubBriefTaskTrigger5';
delete from v_quartz_cron_expression where  cron_trigger_bean='UpdateSnsPubBriefTaskTrigger6';
delete from v_quartz_cron_expression where  cron_trigger_bean='UpdateSnsPubBriefTaskTrigger7';
delete from v_quartz_cron_expression where  cron_trigger_bean='UpdateSnsPubBriefTaskTrigger8';
delete from v_quartz_cron_expression where  cron_trigger_bean='UpdateSnsPubBriefTaskTrigger9';
update v_quartz_cron_expression set status=1 where cron_trigger_bean='UpdateSnsPubBriefTaskTrigger'

delete from v_quartz_cron_expression where  cron_trigger_bean='UpdatePdwhPubBriefTaskTrigger1';
delete from v_quartz_cron_expression where  cron_trigger_bean='UpdatePdwhPubBriefTaskTrigger2';
delete from v_quartz_cron_expression where  cron_trigger_bean='UpdatePdwhPubBriefTaskTrigger3';
delete from v_quartz_cron_expression where  cron_trigger_bean='UpdatePdwhPubBriefTaskTrigger4';
delete from v_quartz_cron_expression where  cron_trigger_bean='UpdatePdwhPubBriefTaskTrigger5';
delete from v_quartz_cron_expression where  cron_trigger_bean='UpdatePdwhPubBriefTaskTrigger6';
delete from v_quartz_cron_expression where  cron_trigger_bean='UpdatePdwhPubBriefTaskTrigger7';
delete from v_quartz_cron_expression where  cron_trigger_bean='UpdatePdwhPubBriefTaskTrigger8';
delete from v_quartz_cron_expression where  cron_trigger_bean='UpdatePdwhPubBriefTaskTrigger9';
update v_quartz_cron_expression set status=1 where cron_trigger_bean='UpdatePdwhPubBriefTaskTrigger'

delete from TEM_TASK_SNSBRIEF;
insert into TEM_TASK_SNSBRIEF(pub_id,status,error_msg)
select a.pub_id, 0, '' from publication a where exists(select 1 from   scm_pub_xml b where a.pub_id=b.pub_id);
commit;
--原因（SCM-16595 修改来源字段） 2018-3-27 ltl  end



--原因（SCM-16544 论文SEO策略调整） 2018-03-27 zll begin

-- Alter table 
alter table PUB_INDEX_SECOND_LEVEL
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table PUB_INDEX_SECOND_LEVEL modify title VARCHAR2(2000);
--原因（SCM-16544 论文SEO策略调整） 2018-03-27 zll end

--原因（SCM-16738 将系统所有显示为other research 的地方统一改成 others） 2018-03-28 WCW begin

update CONST_PUB_TYPE t set t.en_name='Others' where t.seq_no=99;
commit;

--原因（SCM-16738 将系统所有显示为other research 的地方统一改成 others） 2018-03-28 WCW end
--原因（SCM-16600 来源字段的修改） 2018-03-29 ltl begin
update TEM_TASK_SNSBRIEF set status=0,error_msg='';
update v_quartz_cron_expression t set status=1 where cron_trigger_bean='UpdateSnsPubBriefTaskTrigger';
update v_quartz_cron_expression t set status=1 where cron_trigger_bean='UpdatePdwhPubBriefTaskTrigger';
commit;
--原因（SCM-16600 来源字段的修改） 2018-03-29 ltl end


--SCM-16810 2018-3-29 ZX begin
insert into v_quartz_cron_expression values(153,'FundAgencyAddressTaskTrigger','0 0/1 * * * ?',1,'更新资助机构地址字段的临时任务');
--SCM-16810 2018-3-29 ZX end



--原因（SCM-16783 评审系统接口编写） 2018-03-29 zll begin

insert into v_open_token_service_const(id,token,service_type,status,create_date,descr)
values(729,'00000000','seacpsn1',0,sysdate,'根据过滤检索专家');
--原因（SCM-16783 评审系统接口编写） 2018-03-29 zll end


--原因（给sie 添加检查账号密码服务常量） 2018-3-30 ajb start
  
insert into   v_open_token_service_const( id , token , service_type  , descr )  values (  731 , '11111111' , 'usn22pwd',  '检查用户名和密码')  ;

commit ;

--原因（给sie 添加检查账号密码服务常量） 2018-3-30 ajb end