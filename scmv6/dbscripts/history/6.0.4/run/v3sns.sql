-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end





--个人主页国际化处理 2017-6-26 WSN begin

alter table PSN_SCIENCE_AREA add (SCIENCE_AREA_EN VARCHAR2(200 char));

update PSN_SCIENCE_AREA sa set sa.science_area_en = (select p.category_en from CATEGORY_MAP_BASE p where p.categry_id = sa.science_area_id)

where exists (select p.category_en from CATEGORY_MAP_BASE p where p.categry_id = sa.science_area_id);

commit;

--个人主页国际化处理 2017-6-26 WSN end

-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end




--原因（    SCM-12963消息中心表实体类创建） 2017-06-15 ZZX begin
-- Create table
create table V_MSG_CONTENT
(
  content_id NUMBER(18) not null,
  content    VARCHAR2(2000)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table V_MSG_CONTENT
  is '消息内容表';
-- Add comments to the columns 
comment on column V_MSG_CONTENT.content_id
  is '消息内容ID';
comment on column V_MSG_CONTENT.content
  is '消息内容，存放json、 页面显示需要的字段 （人员信息除外）';
-- Create/Recreate indexes 
create unique index PK_V_MSG_CONTENT on V_MSG_CONTENT (CONTENT_ID)
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
-- Create table
create table V_MSG_CHAT_RELATION
(
  id          NUMBER(18) not null,
  sender_id   NUMBER(18) not null,
  receiver_id NUMBER(18) not null,
  status      NUMBER(1) not null
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
  create sequence SEQ_V_MSG_CHAT_RELATION
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 10;
-- Add comments to the table 
comment on table V_MSG_CHAT_RELATION
  is '站内信聊天关系表';
-- Add comments to the columns 
comment on column V_MSG_CHAT_RELATION.id
  is '主键';
comment on column V_MSG_CHAT_RELATION.sender_id
  is '发送者ID';
comment on column V_MSG_CHAT_RELATION.receiver_id
  is '接收者ID';
comment on column V_MSG_CHAT_RELATION.status
  is '聊天状态 ：0=正常 、1=删除（删除状态再次发送消息时会重新设置为0）';
-- Create/Recreate indexes 
create unique index PK_V_MSG_CHAT_RELATION on V_MSG_CHAT_RELATION (ID)
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
create table V_MSG_RELATION
(
  id          NUMBER(18) not null,
  sender_id   NUMBER(18) not null,
  receiver_id NUMBER(18) not null,
  content_id  NUMBER(18) not null,
  type        NUMBER(2) not null,
  create_date DATE,
  status      NUMBER(1) not null,
  deal_status NUMBER(1) not null,
  deal_date   DATE
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table V_MSG_RELATION
  is '消息关系表';
-- Add comments to the columns 
comment on column V_MSG_RELATION.id
  is '主键';
comment on column V_MSG_RELATION.sender_id
  is '发送者ID';
comment on column V_MSG_RELATION.receiver_id
  is '接收者ID';
comment on column V_MSG_RELATION.content_id
  is '消息内容ID';
comment on column V_MSG_RELATION.create_date
  is '创建时间';
comment on column V_MSG_RELATION.status
  is '消息状态：0=未读、1=已读、2=删除';
comment on column V_MSG_RELATION.deal_status
  is '消息处理状态：0=未处理、1=同意、2=拒绝/忽略';
comment on column V_MSG_RELATION.deal_date
  is '消息处理时间';
-- Create/Recreate indexes 
create unique index PK_V_MSG_RELATION on V_MSG_RELATION (ID)
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
  create sequence SEQ_V_MSG_CONTENT
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 10;
create sequence SEQ_V_MSG_RELATION
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 10;

--原因（SCM-12963消息中心表实体类创建） 2017-06-15 ZZX end

--原因（添加消息服务编码）  2017-06-19  AJB   begin

insert into v_open_token_service_const(id ,token , service_type ) values(seq_v_open_token_service_id.nextval ,'00000000' ,'msg77msg')  ;
commit ;

--原因（添加消息服务编码）  2017-06-19  AJB  end



--原因（添加消息服务编码）  2017-06-21  zjh   zjh
update v_quartz_cron_express t set t.cron_expression='0 0 12 * * ?' where t.cron_trigger_bean='noticeBeEndorseeAndEndorseTaskTrigger';
commit;
--原因（添加消息服务编码）  2017-06-21  zjh  end





--原因（添加消息服务编码）  2017-06-21  zjh  end--SCM-13021 psnScoreUpdateTaskTriggers 处理更新人员检索计分任务迁移  2017-06-21 zll begin

insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) 
values(45,'PsnScoreUpdateTaskTrigger','0 0/1 * * * ?',1,'检索人员信息计分更新');
commit;

--SCM-13021 psnScoreUpdateTaskTriggers 处理更新人员检索计分任务迁移  2017-06-21 zll end

--消息中心添加皮肤、菜单  2017-06-21 zzx begin

insert into SYS_RESOURCE values(7777,'/node-web6/dynweb/showmsg/msgmain',4,1,'sns.menu.inbox.message.inbox',1,0,'消息中心');commit;

--消息中心添加皮肤、菜单  2017-06-21 zzx end




--原因（修改备注） 2017-6-23 tsz begin

comment on column V_MSG_RELATION.type
  is ' 0=系统消息、1=请求添加好友消息、2=成果认领、3=全文认领、4=成果推荐、5=好友推荐、6=基金推荐、7=站内信、8=请求加入群组消息、9=邀请加入群组消息、10=群组动向 , 11=请求全文消息';


--原因（有CQ号带上CQ号）  2017-6-23 tsz end

  --原因（添加字段） 2017-6-26 ZZX begin

  alter table v_msg_chat_relation add update_date date;
  comment on column V_MSG_CHAT_RELATION.update_date
  is '更新时间';
	commit;
--原因（添加字段）  2017-6-26 ZZX end





--原因（有CQ号带上CQ号）2017-6-29 zjh begin
insert into v_quartz_cron_expression t (t.id，t.cron_trigger_bean,t.cron_expression,t.status,t.description) values(47,'333PsnScoreUpdateTaskTrigger','*/30 * * * * ?',1,'人员关键词解析');
commit;
--原因（有CQ号带上CQ号） 2017-6-29 zjh begin

--原因（消息中心初始化数据临时表）2017-6-29 zzx begin
-- Create table
create table TEST_V_MSG_RELATION_CONTENT
(
  id          NUMBER(18) not null,
  sender_id   NUMBER(18) not null,
  receiver_id NUMBER(18) not null,
  type        NUMBER(2) not null,
  create_date DATE,
  status      NUMBER(1) not null,
  deal_status NUMBER(1) not null,
  deal_date   DATE,
  content_id  NUMBER(18) not null,
  content     VARCHAR2(2000),
  task_status NUMBER(1)
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
comment on table TEST_V_MSG_RELATION_CONTENT
  is '消息中心初始化数据临时表';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TEST_V_MSG_RELATION_CONTENT
  add constraint PK_TEST_V_MSG_RELATION_CONTENT primary key (ID)
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

--原因（消息中心初始化数据临时表） 2017-6-29 zzx begin
  
  --原因（消息中心初始化任务）2017-6-29 zjh begin
insert into v_quartz_cron_expression t (t.id，t.cron_trigger_bean,t.cron_expression,t.status,t.description) 
values(48,'InitMsgTaskTrigger','0 18 18 29 7 ?',0,'msg 数据初始化');
commit;
--原因（消息中心初始化任务） 2017-6-29 zjh begin

  --原因（消息中心初始化任务）2017-6-29 zjh begin
insert into v_quartz_cron_expression t (t.id，t.cron_trigger_bean,t.cron_expression,t.status,t.description) 
values(47,'psnKwEptTaskTrigger','* */59 * * * ?',0,'msg 数据初始化');
commit;
--原因（消息中心初始化任务） 2017-6-29 zjh begin

  --原因（消息中心初始化任务）2017-6-29 zjh begin
delete from v_quartz_cron_expression where id=47 and cron_trigger_bean='333PsnScoreUpdateTaskTrigger';
commit;
insert into v_quartz_cron_expression t (t.id，t.cron_trigger_bean,t.cron_expression,t.status,t.description) 
values(47,'psnKwEptTaskTrigger','* */59 * * * ?',0,'msg 数据初始化');
commit;
--原因（消息中心初始化任务） 2017-6-29 zjh begin

--原因（修改菜单） 20170703 tsz begin


update  sys_resource set value='/psnweb/application/main' where id=4;


--原因（修改菜单） 20170703 tsz end


--原因（msg数据初始化） 2017-07-03 zzx begin


-- Create table
create table TEST_V_MSG_RELATION_CONTENT
(
  id          NUMBER(18) not null,
  sender_id   NUMBER(18) not null,
  receiver_id NUMBER(18) not null,
  type        NUMBER(2) not null,
  create_date DATE,
  status      NUMBER(1) not null,
  deal_status NUMBER(1) not null,
  deal_date   DATE,
  content_id  NUMBER(18) not null,
  content     VARCHAR2(2000),
  task_status NUMBER(1)
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
comment on table TEST_V_MSG_RELATION_CONTENT
  is '消息中心初始化数据临时表';
-- Add comments to the columns 
comment on column TEST_V_MSG_RELATION_CONTENT.id
  is '主键 从SEQ_V_MSG_RELATION获值';
comment on column TEST_V_MSG_RELATION_CONTENT.sender_id
  is '发送人id';
comment on column TEST_V_MSG_RELATION_CONTENT.receiver_id
  is '接收人id';
comment on column TEST_V_MSG_RELATION_CONTENT.type
  is '类型';
comment on column TEST_V_MSG_RELATION_CONTENT.create_date
  is '创建时间';
comment on column TEST_V_MSG_RELATION_CONTENT.status
  is '状态已读未读';
comment on column TEST_V_MSG_RELATION_CONTENT.deal_status
  is '处理状态';
comment on column TEST_V_MSG_RELATION_CONTENT.deal_date
  is '处理时间';
comment on column TEST_V_MSG_RELATION_CONTENT.content_id
  is 'contentId';
comment on column TEST_V_MSG_RELATION_CONTENT.content
  is '构造需要的关键数据';
comment on column TEST_V_MSG_RELATION_CONTENT.task_status
  is '是否跑过任务1=已经跑过';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TEST_V_MSG_RELATION_CONTENT
  add constraint PK_TEST_V_MSG_RELATION_CONTENT primary key (ID)
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



--原因（msg数据初始化） 2017-07-03 zzx end
--原因（msg数据初始化） 2017-07-03 zzx begin
--初步插入数据 --- 全文请求
insert into test_V_MSG_RELATION_content 
select 
SEQ_V_MSG_RELATION.nextval, 
m.sender_id,
 i.receiver_id,
 11,
 m.send_date ,
 i.status,
 i.op_status ,
 i.receive_date ,
 SEQ_V_MSG_CONTENT.nextval,
 regexp_substr(SUBSTR(m.mail_title,22,13),'[0-9]+'),
 0
from FULLTEXT_INBOX i ,FULLTEXT_MAILBOX m where i.mail_id=m.mail_id ;

--初步插入数据 --- 好友邀请
insert into test_V_MSG_RELATION_content 
select 
SEQ_V_MSG_RELATION.nextval, 
t1.sender_id,
 t2.psn_id,
 1,
 t1.opt_date ,
 t2.status,
 t2.opt_status ,
 null  ,
 SEQ_V_MSG_CONTENT.nextval,
'',
 0
from INVITE_MAILBOX t1 ,INVITE_INBOX t2 where t1.mail_id = t2.mail_id and invite_type = 0 ;
--初步插入数据 --- 群组邀请
insert into test_V_MSG_RELATION_content 
select 
SEQ_V_MSG_RELATION.nextval, 
t1.sender_id,
 t2.psn_id,
 9,
 t1.opt_date ,
 t2.status,
 t2.opt_status ,
 null  ,
 SEQ_V_MSG_CONTENT.nextval,
regexp_substr(SUBSTR(t1.ext_other_info,INSTR(t1.ext_other_info,'groupId')+7,18),'[0-9]+'),
 0
from INVITE_MAILBOX t1 ,INVITE_INBOX t2 where t1.mail_id = t2.mail_id and invite_type = 1 ;
--初步插入数据 --- 群组请求
insert into test_V_MSG_RELATION_content 
select 
SEQ_V_MSG_RELATION.nextval, 
t1.sender_id,
 t2.psn_id,
8,
 t1.opt_date ,
 t2.status,
 t2.opt_status ,
 null  ,
 SEQ_V_MSG_CONTENT.nextval,
regexp_substr(SUBSTR(t1.content,110,25),'[0-9]+'),
 0
from INVITE_MAILBOX t1 ,INVITE_INBOX t2 where t1.mail_id = t2.mail_id and invite_type = 2 ;
--初步插入数据--站内信 成果或文件的分享
insert into test_V_MSG_RELATION_content 
select 
SEQ_V_MSG_RELATION.nextval, 
t2.sender_id,
 t1.psn_id,
7,
 t2.opt_date ,
 t1.status,
 0 ,
 t1.receive_date  ,
 SEQ_V_MSG_CONTENT.nextval,
t1.res_rec_id,
 0
from SHARE_INBOX t1 ,SHARE_MAILBOX t2 where t1.mail_id = t2.mail_id and t1.res_rec_id is not null ;

--初步插入数据--站内信-文本信息
insert into test_V_MSG_RELATION_content 
select 
SEQ_V_MSG_RELATION.nextval, 
t2.psn_id,
 t1.psn_id,
77,
 t2.send_date ,
 t1.status,
 0 ,
 t1.recieve_date  ,
 SEQ_V_MSG_CONTENT.nextval,
t2.title,
 0
from INSIDE_INBOX t1 ,INSIDE_MAILBOX t2 where t1.mail_id = t2.mail_id and t2.title is not null ;
commit;
--原因（msg数据初始化） 2017-07-03 zzx end




--原因（V_MSG_CHAT_RELATION添加字段） 2017-7-4 zzx begin
alter table V_MSG_CHAT_RELATION add content_newest varchar(2000);
comment on column V_MSG_CHAT_RELATION.content_newest
  is '会话最新消息内容';
  --原因（V_MSG_CHAT_RELATION添加字段） 2017-7-4 zzx end

--原因（有CQ号带上CQ号） 2017-7-7 zjh begin
insert into v_quartz_cron_expression(id,CRON_TRIGGER_BEAN,CRON_EXPRESSION,STATUS,DESCRIPTION) values(52,'PsnKwRmcTaskTrigger','*/5 * * * * ?',1,'pkg_psn_kw_rmc存储过程');
commit;



--原因（有CQ号带上CQ号） 2017-7-7 zjh end

--原因（SCM-13208 好友推荐任务迁移至center-task） 2017-07-10 zll begin
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) 
values (49,'SystemRecommendTrigger','0 0/1 * * * ?',1,'智能推荐好友');
commit;
--原因（SCM-13208 好友推荐任务迁移至center-task） 2017-07-10 zll end--原因（SCM-13208 好友推荐任务迁移至center-task） 2017-07-10 zll begin
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) 
values (53,'SystemRecommendTrigger','0 0/1 * * * ?',1,'智能推荐好友');
commit;
--原因（SCM-13208 好友推荐任务迁移至center-task） 2017-07-10 zll end


--原因（SCM-7603）  2017-07-06 LJ begin

create table SCIENCEAREA_AGREE_STATUS
(
  id              VARCHAR2(18) not null,
  psn_id          NUMBER not null,
  science_area_id NUMBER not null,
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
comment on table SCIENCEAREA_AGREE_STATUS
  is '认同研究领域邮件生成状态记录表';
-- Add comments to the columns 
comment on column SCIENCEAREA_AGREE_STATUS.id
  is '与认同记录表ID一致';
comment on column SCIENCEAREA_AGREE_STATUS.psn_id
  is '被认同人';
comment on column SCIENCEAREA_AGREE_STATUS.science_area_id
  is '研究领域ID';
comment on column SCIENCEAREA_AGREE_STATUS.friend_id
  is '认同人员';
comment on column SCIENCEAREA_AGREE_STATUS.op_date
  is '认同时间';
comment on column SCIENCEAREA_AGREE_STATUS.status
  is '0表示未生成，1生成失败，2成功';
-- Create/Recreate indexes 
create index PK_SCIENCEAREA_AGREE_STATUS on SCIENCEAREA_AGREE_STATUS (ID)
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

--原因（SCM-7603）  2017-07-06 LJ end






--原因（SCM-13208 好友推荐任务迁移至center-task） 2017-07-10 zll begin


insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) 
values (55,'KnowPsnWorkEduTaskTrigger','0 0/1 * * * ?',1,'智能推荐人员工作经历，教育经历所有单位');


--原因（SCM-13208 好友推荐任务迁移至center-task） 2017-07-10 zll end


  --原因（有CQ号带上CQ号） PsnKwRmcTask 2017-07-10 zjh begin  本地有这张表，测试机uat都没有
create table KEYWORDS_SYNONYM_GROUP
(
  id          NUMBER not null,
  gid         NUMBER(18) not null,
  syn_keyword VARCHAR2(200 CHAR) not null,
  syn_kwtxt   VARCHAR2(200 CHAR)
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
comment on table KEYWORDS_SYNONYM_GROUP
  is '同义词分组表/zk';
-- Add comments to the columns 
comment on column KEYWORDS_SYNONYM_GROUP.id
  is '主键';
comment on column KEYWORDS_SYNONYM_GROUP.gid
  is '分组id,见KEYWORDS_SYNONYM.GID';
comment on column KEYWORDS_SYNONYM_GROUP.syn_keyword
  is '同义词';
comment on column KEYWORDS_SYNONYM_GROUP.syn_kwtxt
  is '同义词';
-- Create/Recreate primary, unique and foreign key constraints 
alter table KEYWORDS_SYNONYM_GROUP
  add constraint PK_KEYWORDS_SYNONYM_GROUP primary key (ID)
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
create index IDX_KEYWORDS_SYNONYM_GROUP_GID on KEYWORDS_SYNONYM_GROUP (GID)
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
  commit;
  --原因（有CQ号带上CQ号） 2017-07-10 zjh end
  
 
  
--原因（SCM-13142） 2017-07-10 LJ begin

-- Create table
create table SCM_VERIF_CODE
(
  account      VARCHAR2(200 CHAR) not null,
  code         VARCHAR2(200 CHAR),
  update_date  DATE,
  created_date DATE,
  count        NUMBER(18) default 0,
  status       NUMBER(5) default 0
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
comment on table SCM_VERIF_CODE
  is '科研之友用户注册验证码';
-- Add comments to the columns 
comment on column SCM_VERIF_CODE.account
  is '账号';
comment on column SCM_VERIF_CODE.code
  is '验证码';
comment on column SCM_VERIF_CODE.update_date
  is '更新时间';
comment on column SCM_VERIF_CODE.created_date
  is '创建时间';
comment on column SCM_VERIF_CODE.count
  is '已验证次数';
comment on column SCM_VERIF_CODE.status
  is '验证状态，0：未验证，1验证通过，2验证失败
';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SCM_VERIF_CODE
  add constraint PK_SCM_VERIF_CODE primary key (ACCOUNT)
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
create table MESSAGE_LOG
(
  log_id    NUMBER(18) not null,
  sms_type  NUMBER(18),
  sms_to    VARCHAR2(50 CHAR),
  content   VARCHAR2(200 CHAR),
  send_time DATE,
  error_msg VARCHAR2(200 CHAR),
  status    NUMBER(5)
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
comment on table MESSAGE_LOG
  is '短信日志表';
-- Add comments to the columns 
comment on column MESSAGE_LOG.log_id
  is '日志ID';
comment on column MESSAGE_LOG.sms_type
  is '短信类型 1000为注册验证短信';
comment on column MESSAGE_LOG.sms_to
  is '接收号码';
comment on column MESSAGE_LOG.content
  is '短信内容';
comment on column MESSAGE_LOG.send_time
  is '发送时间';
comment on column MESSAGE_LOG.error_msg
  is '错误信息';
comment on column MESSAGE_LOG.status
  is '发送状态，1成功，2失败';
-- Create/Recreate indexes 
create index IDX_MESSAGE_LOG_SMS_TO on MESSAGE_LOG (SMS_TO)
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
alter table MESSAGE_LOG
  add constraint PK_MESSAGE_LOG primary key (LOG_ID)
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
--原因（SCM-13142） 2017-07-10 LJ end

--原因（有CQ号带上CQ号） 2017-7-10 zjh begin
update  v_quartz_cron_expression t set t.cron_expression='*/30 * * * * ?' where t.cron_trigger_bean='psnKwEptTaskTrigger';
update  v_quartz_cron_expression t set t.cron_expression='* */5 * * * ?' where t.cron_trigger_bean='PsnKwRmcTaskTrigger';
--原因（有CQ号带上CQ号） 2017-7-10  zjh end


--原因(CQ: SCM-12618)  2017-7-11 hcj begin
update CONST_INS_UNIT_SEARCH u
set u.COLLEGE_NAME = REGEXP_REPLACE(REGEXP_REPLACE(u.COLLEGE_NAME, 'of' ,' of '), '([a-z])([A-Z])' ,'\1 \2'),
u.SEARCH_NAME = REGEXP_REPLACE(REGEXP_REPLACE(u.SEARCH_NAME, 'of' ,' of '), '([a-z])([A-Z])' ,'\1 \2')
WHERE u.sid IN
  (SELECT t.sid
  FROM CONST_INS_UNIT_SEARCH t
  WHERE NOT REGEXP_LIKE(t.SEARCH_NAME,'\s+')
  AND t.SEARCH_NAME LIKE '%of%'
  );
--原因(CQ: SCM-12618) 2017-7-11 hcj end
--原因(CQ: SCM-12618)  2017-7-11 hcj begin
update CONST_INS_UNIT_SEARCH u
set u.COLLEGE_NAME = REGEXP_REPLACE(u.college_name, '([a-z])([A-Z])' ,'\1 \2'),
u.SEARCH_NAME = REGEXP_REPLACE(u.search_name, '([a-z])([A-Z])' ,'\1 \2')
WHERE u.sid IN
  (SELECT t.sid
  FROM CONST_INS_UNIT_SEARCH t
  WHERE NOT REGEXP_LIKE(t.SEARCH_NAME,'\s+')
  and REGEXP_LIKE(t.SEARCH_NAME, '([a-z])([A-Z])')
  and t.sid not in (160534, 206063, 206056, 198664)
  );
--原因(CQ: SCM-12618) 2017-7-11 hcj end

--原因（增加文件主表字段） 2017-07-14 tsz begin

alter table archive_files ADD file_url varchar2(100)  Default 0;

comment on column archive_files.file_url is '文件路径，通过file_path算出，没有初始化的为0';

alter table archive_files ADD file_size number  Default 0;

comment on column archive_files.file_size is '文件大小(KB)，没有初始化的为0';


alter table archive_files ADD status number  Default 0;

comment on column archive_files.status is '文件状态 0正常 1 删除  默认0';

--原因（增加文件主表字段） 2017-07-14 tsz  end



--- PSN_KNOW_COPARTNER.copartner_name字段值太小
alter table PSN_KNOW_COPARTNER modify copartner_name VARCHAR2(100 CHAR);
--- PSN_KNOW_COPARTNER.copartner_name字段值太小

--原因（SCM-13268 文件改造） 2017-07-17  AJB begin


-- Create table
create table V_PSN_FILE
(
  id              NUMBER(18) not null,
  owner_psn_id    NUMBER(18) not null,
  file_name       VARCHAR2(300 CHAR) not null,
  file_type       VARCHAR2(100),
  file_desc       VARCHAR2(500 CHAR),
  status          NUMBER(1) default 0 not null,
  upload_date     DATE default SYSDATE not null,
  update_date     DATE default SYSDATE not null,
  archive_file_id NUMBER(18) not null,
  permission      NUMBER(1) default 0 not null
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
comment on table V_PSN_FILE
  is '个人文件新建与2017-07';
-- Add comments to the columns 
comment on column V_PSN_FILE.id
  is '主键';
comment on column V_PSN_FILE.owner_psn_id
  is '用户id';
comment on column V_PSN_FILE.file_name
  is '文件名';
comment on column V_PSN_FILE.file_type
  is '文件类型';
comment on column V_PSN_FILE.file_desc
  is '文件描述';
comment on column V_PSN_FILE.status
  is '文件状态 0=正常  1=删除';
comment on column V_PSN_FILE.upload_date
  is '上传时间';
comment on column V_PSN_FILE.update_date
  is '更新时间';
comment on column V_PSN_FILE.archive_file_id
  is '附件id';
comment on column V_PSN_FILE.permission
  is '查看权限：0：所有人可见；1：好友可见；2：仅本人可见'';';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_PSN_FILE
  add constraint PK_PSN_FILE primary key (ID)
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
create index IDX_PSN_FILE_ARCHIVE_FILE_ID on V_PSN_FILE (ARCHIVE_FILE_ID)
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
create sequence SEQ_V_PSN_FILE
minvalue 1
maxvalue 99999999999999999
start with 1100000010
increment by 1
cache 10;

commit ;

-- Create table
create table V_PSN_FILE_SHARE_BASE
(
  id          NUMBER(18) not null,
  sharer_id   NUMBER(18) not null,
  create_date DATE default sysdate not null,
  status      NUMBER(1) default 0 not null,
  update_date DATE default sysdate not null
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
comment on table V_PSN_FILE_SHARE_BASE
  is '个人文件分享 主表';
-- Add comments to the columns 
comment on column V_PSN_FILE_SHARE_BASE.id
  is '主键';
comment on column V_PSN_FILE_SHARE_BASE.sharer_id
  is '分享人id';
comment on column V_PSN_FILE_SHARE_BASE.create_date
  is '创建时间';
comment on column V_PSN_FILE_SHARE_BASE.status
  is '状态  0 =正常  1=取消分享  2=删除';
comment on column V_PSN_FILE_SHARE_BASE.update_date
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_PSN_FILE_SHARE_BASE
  add constraint PK_V_PSN_FILE_SHARE_BASE primary key (ID)
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


-- Create sequence 
create sequence SEQ_V_PSN_FILE_SHARE_BASE
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 10;

commit ;



-- Create table
create table V_PSN_FILE_SHARE_RECORD
(
  id              NUMBER(18) not null,
  sharer_id       NUMBER(18) not null,
  receiver_id     NUMBER(18) not null,
  file_id         NUMBER(18) not null,
  create_date     DATE default sysdate,
  update_date     DATE default sysdate,
  status          NUMBER(1) default 0,
  share_base_id   NUMBER(18) not null,
  msg_relation_id NUMBER(18) not null
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
comment on table V_PSN_FILE_SHARE_RECORD
  is '个人文件分享记录表-2017-07-12';
-- Add comments to the columns 
comment on column V_PSN_FILE_SHARE_RECORD.id
  is '主键';
comment on column V_PSN_FILE_SHARE_RECORD.sharer_id
  is '分享人id';
comment on column V_PSN_FILE_SHARE_RECORD.receiver_id
  is '接受人id';
comment on column V_PSN_FILE_SHARE_RECORD.file_id
  is '文件id';
comment on column V_PSN_FILE_SHARE_RECORD.create_date
  is '创建时间，分享时间';
comment on column V_PSN_FILE_SHARE_RECORD.update_date
  is '更新时间，';
comment on column V_PSN_FILE_SHARE_RECORD.status
  is '0=正常 ； 1=取消分享 ； 2=删除';
comment on column V_PSN_FILE_SHARE_RECORD.share_base_id
  is '分享主表主键id';
comment on column V_PSN_FILE_SHARE_RECORD.msg_relation_id
  is '消息表的关联id';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_PSN_FILE_SHARE_RECORD
  add constraint PK_V_PSN_FILE_SHARE_RECORD primary key (ID)
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

-- Create sequence 
create sequence SEQ_V_PSN_FILE_SHARE_RECORD
minvalue 1
maxvalue 999999999999999999
start with 41
increment by 1
cache 10;

commit ;


insert   into  v_psn_file  
value( 
select    t.file_id , t.owner_psn_id  , t.file_name   , t.file_type    , t.file_desc   , t.file_status   , t.upload_time    ,t.upload_time ,   t.archive_file_id  , t.pemission   
from  station_file  t  where (  t.is_share!=2 and t.is_share!=3)
 ) ;

commit ;

--原因（SCM-13268 文件改造） 2017-07-17  AJB end






--原因（有CQ号带上CQ号） 2017-7-18 zjh begin
update KEYWORDS_COMMEND_TASK set info='读取psn_kw_ept_refresh数据，刷新人员前50个熟悉关键词' where id=1;
commit;
--原因（有CQ号带上CQ号）2017-7-18 zjh end  

--原因（SCM-13338） 2017-07-18 LJ begin

insert into const_ref_db
  (dbid,
   db_code,
   zh_cn_name,
   en_us_name,
   action_url_inside,
   login_url_inside,
   only_support_lang,
   is_public,
   en_sort_key,
   zh_sort_key,
   db_type,
   db_bit_code,
   fulltext_url_inside,
   action_url,
   login_url,
   zh_abbr_name,
   en_abbr_name,
   batch_query)
values
  (31,
   'RAINPAT',
  ' 润桐专利',
  'RAINPAT',
   'http://www.rainpat.com',
   'http://www.rainpat.com',
   '',
   1,
   0,
   0,
   1,
   0,
   '',
   '',
   '',
   '润桐专利',
   'RAINPAT',
   '');
 
commit;

--原因（SCM-13338） 2017-07-18 LJ  end



--原因（有CQ号带上CQ号）存储过程的定时任务不跑，Disc_Keyword_Hot_Related数据进行插入 2017-7-18 zjh begin
Insert into Disc_Keyword_Hot_Related select seq_disc_keywords_related.nextval,id ,keyword,kw_txt,word_num,discode from  disc_keyword_hot;
commit;

update user_jobs t set t.BROKEN='Y' where t.JOB in(182,183);
commit;
--原因（有CQ号带上CQ号） 2017-7-18 zjh end


--原因（有CQ号带上CQ号）存储过程的定时任务不跑，Disc_Keyword_Hot_Related数据进行插入 2017-7-18 zjh begin
update user_jobs t set t.BROKEN='Y' where t.JOB in(101,141);
commit;
--原因（有CQ号带上CQ号） 2017-7-18 zjh end



--原因（有CQ号带上CQ号） 2017-7-18 zjh begin
update v_quartz_cron_expression t set t.description='pkg_psn_kw_ept存储过程' where t.cron_trigger_bean='psnKwEptTaskTrigger';
commit;

alter table psn_kw_ept_tmp add (SOURCE_TYPE number(1),pub_id number(18));
commit;
--原因（有CQ号带上CQ号） 2016-7-18 zjh end

--原因（SCM-13208 好友推荐任务迁移至center-task） 2017-07-18 zll begin


alter table PSN_KNOW_COPARTNER modify copartner_first_name VARCHAR2(50 char);
alter table PSN_KNOW_COPARTNER modify copartner_last_name VARCHAR2(50 char);


--原因（SCM-13208 好友推荐任务迁移至center-task） 2017-07-18 zll end

--原因（SCM-13116） 2017-07-19 LJ begin

insert into v_quartz_cron_expression
  (id, cron_trigger_bean, cron_expression, status, description)
values
  (56, 'UpdateAllPdwhPubCiteTimesTaskTrigger', '*/59 * * * * ?', 0, '更新所有pdwh成果的cited_times');
  commit;
  
 insert into V_QUARTZ_CRON_EXPRESSION values('57','UpdateDynContentTaskTrigger','* */1 * * * ?',0,' 更新动态内容历史数据任务'); commit;

--原因（SCM-13116） 2017-07-19 LJ end




--原因（有CQ号带上CQ号） 2017-7-19 zjh begin
ALTER TABLE PSN_KW_EPT_TMP SET UNUSED COLUMN source_type;
ALTER TABLE PSN_KW_EPT_TMP SET UNUSED COLUMN pub_id;
--原因（有CQ号带上CQ号） 2017-7-19 zjh end

--原因（    SCM-13374动态列表调整） 2017-07-20 ajb begin
-- Alter table 
alter table DYNAMIC_REPLY_PSN
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table DYNAMIC_REPLY_PSN add reply_add_res_id NUMBER(18);
-- Add comments to the columns 
comment on column DYNAMIC_REPLY_PSN.reply_add_res_id
  is '评论时添加的资源id--成果id';
  
  commit ;
  
--原因（    SCM-13374动态列表调整） 2017-07-20 ajb  end


--原因（有CQ号带上CQ号） 2016-12-8 WSN begin
CREATE SEQUENCE SEQ_PSN_KW_EPT_KWGID  --序列名
INCREMENT BY 1   -- 每次加几个  
START WITH 1       -- 从1开始计数  
NOMAXVALUE        -- 不设置最大值  
NOCYCLE               -- 一直累加，不循环  
CACHE 10;
--原因（有CQ号带上CQ号） 2016-12-8 WSN end



--原因（影响力邮件已经改为周影响力了，修改一下月为周） 2017-7-21 zjh begin
comment on column ETEMPLATE_INFLUENCE_COUNT.Month_Read_Count is '周阅读数';
comment on column ETEMPLATE_INFLUENCE_COUNT.Month_Award_Count is '周赞数';
comment on column ETEMPLATE_INFLUENCE_COUNT.Month_Download_Count is '周下载数';
comment on column ETEMPLATE_INFLUENCE_COUNT.Month_Cited_Times_Count is '周引用数';
comment on column ETEMPLATE_INFLUENCE_COUNT.Month_Share_Count is '周分享数';
comment on column ETEMPLATE_INFLUENCE_COUNT.Month_Pub_Count is '周成果数';
commit;
--原因（有CQ号带上CQ号） 2017-7-21 zjh end

--原因（SCM-13415） 2017-07-22 zzx begin


update person set avatars = replace(avatars,'/avatars/head_nan_photo.jpg','/resmod/smate-pc/img/logo_psndefault.png');


--原因（SCM-13415）  2017-07-22 zzx end

--原因（SCM-13508  -- 评论内容可为空） 2017-07-22 ajb begin

-- Alter table 
alter table DYNAMIC_REPLY_PSN
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table DYNAMIC_REPLY_PSN modify reply_content null;

--原因（SCM-13508  -- 评论内容可为空） 2017-07-22 ajb end
--SCM-13522 编辑人员信息可编辑人员姓名 2017-7-23 WSN begin

alter table person add (zh_lastName varchar2(50));

alter table person add (zh_firstName varchar2(50));

commit;

--SCM-13522 编辑人员信息可编辑人员姓名 2017-7-23 WSN begin
--原因（SCM-13465） 2017-07-21 lj begin
create table DYN_CONTENT_UPDATE_STATUS
(
  dyn_id        VARCHAR2(18) not null,
  dyn_type      VARCHAR2(10),
  update_date   DATE,
  update_status NUMBER(5) default 0,
  update_msg    VARCHAR2(200)
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
comment on table DYN_CONTENT_UPDATE_STATUS
  is '更新动态内容状态表';
-- Add comments to the columns 
comment on column DYN_CONTENT_UPDATE_STATUS.dyn_id
  is '动态Id';
comment on column DYN_CONTENT_UPDATE_STATUS.dyn_type
  is '动态类型';
comment on column DYN_CONTENT_UPDATE_STATUS.update_date
  is '更新时间';
comment on column DYN_CONTENT_UPDATE_STATUS.update_status
  is '默认为0 ，1表示更新成功，2表示更新失败';
comment on column DYN_CONTENT_UPDATE_STATUS.update_msg
  is '更新信息';
-- Create/Recreate indexes 
create index PK_DYN_CONTENT_UPDATE_STATUS on DYN_CONTENT_UPDATE_STATUS (DYN_ID)
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

  
insert into Dyn_Content_Update_Status(dyn_id,dyn_type) select dyn_id,dyn_type from v_dynamic_msg ;
 
commit;
--原因（SCM-13465） 2017-07-21 lj end













--增加 个人文件分享表 基础数据表 字段 （有CQ号带上CQ号） 2017-07-24 tsz begin


alter table v_psn_file_share_base add share_Content_Rel varchar2(500);
COMMENT ON column v_psn_file_share_base.share_Content_Rel IS '分享文件 发送的文本站内信，给来取消分享用';

--增加 个人文件分享表 基础数据表 字段（有CQ号带上CQ号） 2017-07-24 tsz end



--去掉好友的2级菜单 已经没用了（有CQ号带上CQ号） 20170725 tsz begin


update sys_resource t set t.status=0 where t.parent_id=2;  


--去掉好友的2级菜单 已经没用了（有CQ号带上CQ号） 20170725 tsz end  
   
--SCM-13558 主页，编辑姓名，姓、名输入最大值后提示系统繁忙   2017-7-25 wsn begin
ALTER TABLE person modify(zh_firstName varchar2(40 char)); 

ALTER TABLE person modify(zh_lastName varchar2(20 char));

--SCM-13558 主页，编辑姓名，姓、名输入最大值后提示系统繁忙   2017-7-25 wsn end

---SCM-13352 发现好友功能及页面优化 2017-7-25 WSN begin

create table friend_req_record(
       id  number(18) not null primary key,
       send_psn_id number(18) not null,
       receive_psn_id number(18) not null,
       create_date date default sysdate,
       deal_date date，
       status number(1) default 0
);
  
  
comment on table friend_req_record
  is '好友请求记录表';
-- Add comments to the columns 
comment on column friend_req_record.id
  is '主键';
  
comment on column friend_req_record.send_psn_id
  is '发送请求的人员ID';
  
comment on column friend_req_record.receive_psn_id
  is '接收请求的人员ID';
  
comment on column friend_req_record.create_date
  is '请求记录创建时间';
  
comment on column friend_req_record.deal_date
  is '请求处理时间';
  
comment on column friend_req_record.status
  is '处理状态 （0：未处理，1：接受，2：忽略，3：因重发好友请求而作废的请求，4：互相好友请求未操作的一方而作废的请求，5：取消好友请求而作废的请求，-1：好友移除请求）';
  
create index IDX_friend_req_sendID on friend_req_record (send_psn_id);

create index IDX_friend_req_receiveID on friend_req_record (receive_psn_id);

CREATE SEQUENCE SEQ_FRIEND_REQ_RECORD
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20;


commit;

---SCM-13352 发现好友功能及页面优化 2017-7-25 WSN end

--创建全文请求记录表（SCM-13077） 2017-7-25 hcj begin

   CREATE TABLE "V_PUB_FULLTEXT_REQ_BASE" 
   (	"REQ_ID" NUMBER(18,0) PRIMARY KEY NOT NULL, 
	"PUB_ID" NUMBER(18,0) NOT NULL, 
	"PUB_DB" NUMBER(1,0) NOT NULL, 
	"REQ_PSN_ID" NUMBER(18,0) NOT NULL, 
	"REQ_DATE" DATE NOT NULL, 
	"UPDATE_DATE" DATE, 
	"UPDATE_PSN_ID" NUMBER(18,0),
	"STATUS" NUMBER(1,0) DEFAULT 0 NOT NULL
   );
   
  CREATE TABLE "V_PUB_FULLTEXT_REQ_RECV" 
   (	"ID" NUMBER(18,0) PRIMARY KEY NOT NULL, 
    "MSG_ID" NUMBER(18,0) NOT NULL,
	"REQ_ID" NUMBER(18,0) NOT NULL,
	"REQ_PSN_ID" NUMBER(18,0) NOT NULL,
	"RECV_PSN_ID" NUMBER(18,0) NOT NULL,
	"PUB_ID" NUMBER(18,0) NOT NULL, 
	"PUB_DB" NUMBER(1,0) NOT NULL,
	"STATUS" NUMBER(1,0) DEFAULT 0 NOT NULL, 
	"REQ_DATE" DATE NOT NULL,
	"UPDATE_DATE" DATE,
	"UPDATE_PSN_ID" NUMBER(18,0)
   );

   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_RECV"."ID" IS '逻辑id';
   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_RECV"."REQ_ID" IS '外键，全文请求记录表Req_id';
   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_RECV"."REQ_PSN_ID" IS '请求人id，匿名用户此列为空';
   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_RECV"."RECV_PSN_ID" IS '收到请求的人员id';
   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_RECV"."PUB_ID" IS '关联的成果id，与pub_fulltext_req表的pub_id一致';
   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_RECV"."PUB_DB" IS '被请求成果所属，0-基准库，1-个人库';
   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_RECV"."STATUS" IS '处理状态 （0: 未处理, 1:同意, 2: 忽略/拒绝, 3: 上传, 4：用户选择其他版本下载 -1: 重复请求而作废的请求, -2: 其他用户完成而作废的请求） ';
   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_RECV"."REQ_DATE" IS '请求时间';
   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_RECV"."UPDATE_DATE" IS '处理时间';
   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_RECV"."UPDATE_PSN_ID" IS '处理人id，0: 系统处理，包括导入或者手动上传';
   COMMENT ON TABLE "V_PUB_FULLTEXT_REQ_RECV"  IS '记录收到的全文请求信息';
   
   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_BASE"."REQ_ID" IS '全文请求记录id';
   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_BASE"."PUB_ID" IS '被请求的成果id，可以是个人成果，也可以是基准库成果';
   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_BASE"."PUB_DB" IS '被请求成果所属，0-基准库，1-个人库';
   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_BASE"."REQ_PSN_ID" IS '请求人id，匿名用户此列为空';
   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_BASE"."REQ_DATE" IS '请求时间';
   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_BASE"."UPDATE_DATE" IS '处理时间';
   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_BASE"."UPDATE_PSN_ID" IS '处理人id，0: 系统处理，包括导入或者手动上传';
   COMMENT ON COLUMN "V_PUB_FULLTEXT_REQ_BASE"."STATUS" IS '处理状态 （0: 未处理, 1:同意, 2: 忽略/拒绝, 3: 上传, 4：用户选择其他版本下载 -1: 重复请求而作废的请求, -2: 其他用户完成而作废的请求）';
   COMMENT ON TABLE "V_PUB_FULLTEXT_REQ_BASE"  IS '成果全文请求记录表';
   CREATE SEQUENCE "SEQ_V_PUB_FULLTEXT_REQ_BASE"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
   CREATE SEQUENCE "SEQ_V_PUB_FULLTEXT_REQ_RECV"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;

--创建全文请求记录表（SCM-13077） 2017-7-25 hcj end

--原因（SCM-13423） 2017-07-26 zzx begin
insert into v_quartz_cron_expression t (t.id,t.cron_trigger_bean,t.cron_expression,t.status,t.description) 
values(59,'RecomMsgTaskTrigger','0 15 18 29 7 ?',0,'成果认领、全文认领-发消息-定时任务');
commit;
--原因（SCM-13423） 2017-07-26 zzx end




--应hzr要求拆分姓名需要调整下中文姓和名字段大小  2017-7-26 WSN begin

alter table person modify (zh_firstName varchar2(50 char));

alter table person modify (zh_lastName varchar2(50 char));

--应hzr要求拆分姓名需要调整下中文姓和名字段大小  2017-7-26 WSN end




--更新菜单连接（有CQ号带上CQ号） 20170727 tsz begin


update  sys_resource t set t.value='/psnweb/friend/main' where t.id=2;
commit;

--更新菜单连接（有CQ号带上CQ号）20170727 tsz end



update v_quartz_cron_expression set cron_expression  =  '0 0 1 * * ?' where cron_trigger_bean='noticeBeEndorseeAndEndorseTaskTrigger';
commit;    



