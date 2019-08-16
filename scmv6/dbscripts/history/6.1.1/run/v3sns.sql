-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--原因（SCM-15782）2018-4-12 ltl begin
-- Create table
create table V_FUND_RECOMMEND_REGION
(
  psn_id             NUMBER(18) not null,
  region_id          NUMBER(6),
  update_date        DATE,
  super_region_strid VARCHAR2(50),
  zh_name            VARCHAR2(50),
  en_name            VARCHAR2(50),
  id                 NUMBER(18) not null
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
comment on table V_FUND_RECOMMEND_REGION
  is '基金推荐关注的地区设置';
-- Add comments to the columns 
comment on column V_FUND_RECOMMEND_REGION.psn_id
  is '人员ID';
comment on column V_FUND_RECOMMEND_REGION.region_id
  is '关注的地区';
comment on column V_FUND_RECOMMEND_REGION.update_date
  is '更新时间';
comment on column V_FUND_RECOMMEND_REGION.super_region_strid
  is '该地区的所有上级id，从左到右';
comment on column V_FUND_RECOMMEND_REGION.zh_name
  is '国家中文名、行政区划名';
comment on column V_FUND_RECOMMEND_REGION.en_name
  is '国家英文名、行政区划拼音';
comment on column V_FUND_RECOMMEND_REGION.id
  is '主键id';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_FUND_RECOMMEND_REGION
  add constraint PK_FUND_RECOMMEND_REGION primary key (ID)
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
create sequence SEQ_FUND_RECOMMEND_REGION
minvalue 1
maxvalue 999999999999999999
start with 840
increment by 1
cache 20;
  -- Create table
-- Create table
create table V_FUND_RECOMMEND_AREA
(
  psn_id             NUMBER(18) not null,
  science_area_id    NUMBER(5),
  parent_category_id NUMBER(5),
  update_date        DATE,
  zh_name            VARCHAR2(50),
  en_name            VARCHAR2(50),
  id                 NUMBER(18) not null
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
comment on table V_FUND_RECOMMEND_AREA
  is '基金推荐科技领域设置表';
-- Add comments to the columns 
comment on column V_FUND_RECOMMEND_AREA.psn_id
  is '人员ID';
comment on column V_FUND_RECOMMEND_AREA.science_area_id
  is '科技领域id参照CATEGORY_SCM表';
comment on column V_FUND_RECOMMEND_AREA.parent_category_id
  is '一级学科领域为0';
comment on column V_FUND_RECOMMEND_AREA.update_date
  is '更新时间';
comment on column V_FUND_RECOMMEND_AREA.zh_name
  is '领域中文名称';
comment on column V_FUND_RECOMMEND_AREA.en_name
  is '领域英文名称';
comment on column V_FUND_RECOMMEND_AREA.id
  is '主键Id';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_FUND_RECOMMEND_AREA
  add constraint PK_FUND_RECOMMEND_AREA primary key (ID)
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
create sequence SEQ_FUND_RECOMMEND_AREA
minvalue 1
maxvalue 999999999999999999
start with 820
increment by 1
cache 20;
--原因（SCM-15782）2018-4-12 ltl end

--原因（SCM-15782）2018-4-12 ltl begin
-- Add/modify columns 
alter table V_FUND_CONDITIONS add status NUMBER(2) default 0;
-- Add comments to the columns 
comment on column V_FUND_CONDITIONS.status
  is '更新到新表的任务状态';
-- Add/modify columns 
alter table V_FUND_RECOMMEND_AREA modify zh_name VARCHAR2(150);
alter table V_FUND_RECOMMEND_AREA modify en_name VARCHAR2(150);

insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (166, 'UpdateFundConditionTaskTrigger', '*/10 * * * * ?', 1, '把基准库推荐设置条件换到其他表任务');
commit;

--原因（SCM-15782）2018-4-12 ltl end

--原因（SCM-15782）2018-4-12 ltl begin
-- Add/modify columns 
alter table V_FUND_CONDITIONS add status NUMBER(2) default 0;
-- Add comments to the columns 
comment on column V_FUND_CONDITIONS.status
  is '更新到新表的任务状态';
-- Add/modify columns 
alter table V_FUND_RECOMMEND_AREA modify zh_name VARCHAR2(150);
alter table V_FUND_RECOMMEND_AREA modify en_name VARCHAR2(150);

insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (166, 'UpdateFundConditionTaskTrigger', '*/10 * * * * ?', 1, '把基准库推荐设置条件换到其他表任务');
commit;

--原因（SCM-15782）2018-4-12 ltl end

-- Add/modify columns 
delete from V_FUND_RECOMMEND_REGION a  
where a.rowid !=  
(  
select max(b.rowid) from V_FUND_RECOMMEND_REGION b  
where a.psn_id = b.psn_id and  
a.region_id = b.region_id  
);
commit;
--原因（SCM-15782）2018-4-12 ltl end
--原因（有CQ号带上CQ号）SCM-17083  2017-4-19 zx begin
update PSN_STATISTICS f set f.prj_sum = (select count(*) as aa from project t  where t.owner_psn_id=f.psn_id and status=0 and f.psn_id=t.owner_psn_id )  
where f.prj_sum != (select count(*) as aa from project t  where t.owner_psn_id=f.psn_id and status=0 and f.psn_id=t.owner_psn_id );
--原因（有CQ号带上CQ号）SCM-17083  2017-4-19 zx end
--原因（有CQ号带上CQ号）SCM-17094  2018-4-19 zx begin
update PUBLICATION_STATISTICS f set f.comment_count=(select count(*) from pub_comments t where  t.pub_id=f.pub_id) 
where f.comment_count!=(select count(*) from pub_comments t where  t.pub_id=f.pub_id);
--原因（有CQ号带上CQ号）SCM-17094  2018-4-19 zx end




--原因（    SCM-6595 解除QQ、） 2018-04-20 ajb start

-- Alter table 
alter table V_WECHAT_RELATION
  storage
  (
    next 1
  )
;
-- Add/modify columns 
alter table V_WECHAT_RELATION add nick_name VARCHAR2(50);
-- Add comments to the columns 
comment on column V_WECHAT_RELATION.nick_name
  is '第三方帐号的昵称';
  
  commit  ;
  --原因（    SCM-6595 解除QQ、） 2018-04-20 ajb end
--新建邮件相关表 20180425 tsz begin
-- Create table
create table V_MAIL_BLACKLIST
(
  id          NUMBER(8) not null,
  email       VARCHAR2(50) not null,
  status      NUMBER(1) not null,
  update_date DATE not null,
  msg         VARCHAR2(500 CHAR),
  type        NUMBER(1) not null
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
comment on table V_MAIL_BLACKLIST
  is '黑名单表';
-- Add comments to the columns 
comment on column V_MAIL_BLACKLIST.email
  is '邮箱';
comment on column V_MAIL_BLACKLIST.status
  is '状态 0启用 1 关闭';
comment on column V_MAIL_BLACKLIST.update_date
  is '创建更新时间';
comment on column V_MAIL_BLACKLIST.msg
  is '描述';
comment on column V_MAIL_BLACKLIST.type
  is '类别  0 邮箱类别   1 域名 类别 ';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_MAIL_BLACKLIST
  add constraint V_MAIL_BLACKLIST_PRIMARY_KEY primary key (ID)
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
create unique index V_MAIL_BLACKLIST_INDEX_1 on V_MAIL_BLACKLIST (EMAIL)
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
create table V_MAIL_WHITELIST
(
  id     NUMBER(5) not null,
  email  VARCHAR2(50) not null,
  status NUMBER(1) not null
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
comment on table V_MAIL_WHITELIST
  is '邮箱白名单表';
-- Add comments to the columns 
comment on column V_MAIL_WHITELIST.email
  is '邮箱';
comment on column V_MAIL_WHITELIST.status
  is '状态 0启动 1关闭';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_MAIL_WHITELIST
  add constraint V_MAIL_WHITELIST_PRIMARY_KEY primary key (ID)
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
create unique index V_MAIL_WHITELIST_INDEX_1 on V_MAIL_WHITELIST (EMAIL)
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
create table V_MAIL_EVERYDAY_STATISTIC
(
  id                   NUMBER(18) not null,
  send_success         NUMBER(8) default 0 not null,
  send_error           NUMBER(8) default 0 not null,
  to_be_construct      NUMBER(8) default 0 not null,
  construct_error      NUMBER(8) default 0 not null,
  refuse_receive       NUMBER(8) default 0 not null,
  to_be_distributed    NUMBER(8) default 0 not null,
  to_be_send           NUMBER(8) default 0 not null,
  dispatch_error       NUMBER(8) default 0 not null,
  blacklist            NUMBER(8) default 0 not null,
  receiver_inexistence NUMBER(8) default 0 not null,
  mail_send_error      NUMBER(8) default 0 not null,
  mail_dispatch_error  NUMBER(8) default 0 not null,
  sending              NUMBER(8) default 0 not null,
  create_date          DATE default sysdate not null,
  other                NUMBER(8) default 0 not null,
  no_white_list        NUMBER(8) default 0 not null,
  total_count          NUMBER(8) default 0 not null
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
comment on table V_MAIL_EVERYDAY_STATISTIC
  is '邮件每天发送情况统计表';
-- Add comments to the columns 
comment on column V_MAIL_EVERYDAY_STATISTIC.id
  is '主键';
comment on column V_MAIL_EVERYDAY_STATISTIC.send_success
  is '发送成功数量';
comment on column V_MAIL_EVERYDAY_STATISTIC.send_error
  is '发送出错数量';
comment on column V_MAIL_EVERYDAY_STATISTIC.to_be_construct
  is '待构造数量';
comment on column V_MAIL_EVERYDAY_STATISTIC.construct_error
  is '构造出错数量';
comment on column V_MAIL_EVERYDAY_STATISTIC.refuse_receive
  is '拒绝接收数量';
comment on column V_MAIL_EVERYDAY_STATISTIC.to_be_distributed
  is '待分配数量';
comment on column V_MAIL_EVERYDAY_STATISTIC.to_be_send
  is '待发送数量';
comment on column V_MAIL_EVERYDAY_STATISTIC.dispatch_error
  is '调度出错数量';
comment on column V_MAIL_EVERYDAY_STATISTIC.blacklist
  is '黑名单数量';
comment on column V_MAIL_EVERYDAY_STATISTIC.receiver_inexistence
  is '收件箱不存在数量';
comment on column V_MAIL_EVERYDAY_STATISTIC.mail_send_error
  is '邮件发送出错数量';
comment on column V_MAIL_EVERYDAY_STATISTIC.mail_dispatch_error
  is '邮件调度出错数量';
comment on column V_MAIL_EVERYDAY_STATISTIC.sending
  is '正在发送数量';
comment on column V_MAIL_EVERYDAY_STATISTIC.create_date
  is '日期';
comment on column V_MAIL_EVERYDAY_STATISTIC.other
  is '其他数量';
comment on column V_MAIL_EVERYDAY_STATISTIC.no_white_list
  is '不在白名单数量';
comment on column V_MAIL_EVERYDAY_STATISTIC.total_count
  is '总数';


-- Create table
create table V_MAIL_CLIENT
(
  id                   NUMBER(8) not null,
  client_name          VARCHAR2(50) not null,
  status               NUMBER(1) not null,
  create_date          DATE not null,
  update_date          DATE,
  msg                  VARCHAR2(1000 CHAR),
  prior_template       VARCHAR2(20),
  prior_email          VARCHAR2(50),
  prior_sender_account VARCHAR2(100)
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
comment on table V_MAIL_CLIENT
  is '邮件发送客户端表';
-- Add comments to the columns 
comment on column V_MAIL_CLIENT.id
  is '主键id';
comment on column V_MAIL_CLIENT.client_name
  is '客户端名字(与真实客户端配置的名字保持一致)';
comment on column V_MAIL_CLIENT.status
  is '状态 0可用  1不可用';
comment on column V_MAIL_CLIENT.create_date
  is '创建时间';
comment on column V_MAIL_CLIENT.update_date
  is '更新时间';
comment on column V_MAIL_CLIENT.msg
  is '描述';
comment on column V_MAIL_CLIENT.prior_template
  is '优先发送模板 可多个 冒号分割,前后必须有冒号';
comment on column V_MAIL_CLIENT.prior_email
  is '优先发送邮箱 可多个 冒号分割,前后必须有冒号';
comment on column V_MAIL_CLIENT.prior_sender_account
  is '优先发送账号 冒号分割,前后必须有冒号';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_MAIL_CLIENT
  add constraint V_MAIL_CLIENT_KEY1 primary key (ID)
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
create unique index V_MAIL_CLIENT_INDEX1 on V_MAIL_CLIENT (CLIENT_NAME)
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
create table V_MAIL_ORIGINAL_DATA
(
  mail_id            NUMBER(18) not null,
  sender_psn_id      NUMBER(18) not null,
  receiver           VARCHAR2(50 CHAR) not null,
  mail_template_code NUMBER(8) not null,
  prior_level        VARCHAR2(2 CHAR) not null,
  status             NUMBER(2) not null,
  msg                VARCHAR2(1000 CHAR),
  create_date        DATE,
  update_date        DATE,
  receiver_psn_id    NUMBER(18) not null,
  send_status        NUMBER(2)
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
comment on table V_MAIL_ORIGINAL_DATA
  is '邮件原始数据表';
-- Add comments to the columns 
comment on column V_MAIL_ORIGINAL_DATA.mail_id
  is '邮件Id';
comment on column V_MAIL_ORIGINAL_DATA.sender_psn_id
  is '发送者psnId ，0=系统邮件';
comment on column V_MAIL_ORIGINAL_DATA.receiver
  is '接收邮箱';
comment on column V_MAIL_ORIGINAL_DATA.mail_template_code
  is '模版编号';
comment on column V_MAIL_ORIGINAL_DATA.prior_level
  is '优先级别';
comment on column V_MAIL_ORIGINAL_DATA.status
  is '构造状态 0=待构造邮件 1=构造成功 2=构造失败 3=用户不接收此类邮件';
comment on column V_MAIL_ORIGINAL_DATA.msg
  is '描述';
comment on column V_MAIL_ORIGINAL_DATA.create_date
  is '创建时间';
comment on column V_MAIL_ORIGINAL_DATA.update_date
  is '更新时间';
comment on column V_MAIL_ORIGINAL_DATA.receiver_psn_id
  is '接收人psnId,0=非科研之友用户';
comment on column V_MAIL_ORIGINAL_DATA.send_status
  is '发送状态 同v_mail_record.status';
-- Create/Recreate indexes 
create index IDX_V_MAIL_ORIGINAL_DATA_L on V_MAIL_ORIGINAL_DATA (PRIOR_LEVEL)
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
create index IDX_V_MAIL_ORIGINAL_DATA_S on V_MAIL_ORIGINAL_DATA (STATUS)
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
create unique index PK_V_MAIL_ORIGINAL_DATA on V_MAIL_ORIGINAL_DATA (MAIL_ID)
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
create table V_MAIL_ORIGINAL_DATA_HIS
(
  mail_id            NUMBER(18) not null,
  sender_psn_id      NUMBER(18) not null,
  receiver           VARCHAR2(50 CHAR) not null,
  mail_template_code NUMBER(8) not null,
  prior_level        VARCHAR2(2 CHAR) not null,
  status             NUMBER(2) not null,
  msg                VARCHAR2(1000 CHAR),
  create_date        DATE,
  update_date        DATE,
  receiver_psn_id    NUMBER(18) not null,
  send_status        NUMBER(2)
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
comment on table V_MAIL_ORIGINAL_DATA_HIS
  is '邮件原始数据表';
-- Add comments to the columns 
comment on column V_MAIL_ORIGINAL_DATA_HIS.mail_id
  is '邮件Id';
comment on column V_MAIL_ORIGINAL_DATA_HIS.sender_psn_id
  is '发送者psnId ，0=系统邮件';
comment on column V_MAIL_ORIGINAL_DATA_HIS.receiver
  is '接收邮箱';
comment on column V_MAIL_ORIGINAL_DATA_HIS.mail_template_code
  is '模版编号';
comment on column V_MAIL_ORIGINAL_DATA_HIS.prior_level
  is '优先级别';
comment on column V_MAIL_ORIGINAL_DATA_HIS.status
  is '构造状态 0=待构造邮件 1=构造成功 2=构造失败 3=用户不接收此类邮件';
comment on column V_MAIL_ORIGINAL_DATA_HIS.msg
  is '描述';
comment on column V_MAIL_ORIGINAL_DATA_HIS.create_date
  is '创建时间';
comment on column V_MAIL_ORIGINAL_DATA_HIS.update_date
  is '更新时间';
comment on column V_MAIL_ORIGINAL_DATA_HIS.receiver_psn_id
  is '接收人psnId,0=非科研之友用户';
comment on column V_MAIL_ORIGINAL_DATA_HIS.send_status
  is '发送状态 0=待分配 1=待发送 9=调度出错 2=发送成功 3=黑名单 4=receiver不存在';
-- Create table
create table V_MAIL_RECORD
(
  mail_id            NUMBER(10) not null,
  sender             VARCHAR2(50),
  receiver           VARCHAR2(50) not null,
  mail_template_code NUMBER(5) not null,
  prior_level        VARCHAR2(2) not null,
  create_date        DATE not null,
  update_date        DATE,
  distribute_date    DATE,
  status             NUMBER(2) not null,
  msg                VARCHAR2(500 CHAR),
  mail_client        VARCHAR2(50),
  subject            VARCHAR2(100 CHAR) not null,
  sender_name        VARCHAR2(50 CHAR) not null
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
comment on table V_MAIL_RECORD
  is '邮件发送记录表';
-- Add comments to the columns 
comment on column V_MAIL_RECORD.mail_id
  is '邮件id';
comment on column V_MAIL_RECORD.sender
  is '发送者邮箱';
comment on column V_MAIL_RECORD.receiver
  is '接收者 邮箱 ';
comment on column V_MAIL_RECORD.mail_template_code
  is '邮件模板';
comment on column V_MAIL_RECORD.prior_level
  is '优先级别 ';
comment on column V_MAIL_RECORD.create_date
  is '创建时间';
comment on column V_MAIL_RECORD.update_date
  is '更新时间';
comment on column V_MAIL_RECORD.distribute_date
  is '分配时间(分配客户端分配发送账号 )';
comment on column V_MAIL_RECORD.status
  is '0=待分配 1=待发送  2=发送成功 3=黑名单 4=receiver 不存在 5不在白名单 8发送出错 9邮件调度出错,10邮件正在发送
  11构造邮件发送信息出错,,7邮件信息被监控锁定';
comment on column V_MAIL_RECORD.msg
  is '描述 ';
comment on column V_MAIL_RECORD.mail_client
  is '发送客户端';
comment on column V_MAIL_RECORD.subject
  is '主题';
comment on column V_MAIL_RECORD.sender_name
  is '发送者名  默认为科研之友 分中英文';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_MAIL_RECORD
  add constraint V_MAIL_RECORD_PRIMARY_KEY primary key (MAIL_ID)
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
create index V_MAIL_RECORD_INDEX_1 on V_MAIL_RECORD (SENDER)
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
create index V_MAIL_RECORD_INDEX_2 on V_MAIL_RECORD (RECEIVER)
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
create table V_MAIL_RECORD_HIS
(
  mail_id            NUMBER(10) not null,
  sender             VARCHAR2(50),
  receiver           VARCHAR2(50) not null,
  mail_template_code NUMBER(5) not null,
  prior_level        VARCHAR2(2) not null,
  create_date        DATE not null,
  update_date        DATE,
  distribute_date    DATE,
  status             NUMBER(2) not null,
  msg                VARCHAR2(500 CHAR),
  mail_client        VARCHAR2(50),
  subject            VARCHAR2(100 CHAR) not null,
  sender_name        VARCHAR2(50 CHAR) not null
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
comment on table V_MAIL_RECORD_HIS
  is '邮件发送记录表';
-- Add comments to the columns 
comment on column V_MAIL_RECORD_HIS.mail_id
  is '邮件id';
comment on column V_MAIL_RECORD_HIS.sender
  is '发送者邮箱';
comment on column V_MAIL_RECORD_HIS.receiver
  is '接收者 邮箱 ';
comment on column V_MAIL_RECORD_HIS.mail_template_code
  is '邮件模板';
comment on column V_MAIL_RECORD_HIS.prior_level
  is '优先级别 ';
comment on column V_MAIL_RECORD_HIS.create_date
  is '创建时间';
comment on column V_MAIL_RECORD_HIS.update_date
  is '更新时间';
comment on column V_MAIL_RECORD_HIS.distribute_date
  is '分配时间(分配客户端分配发送账号 )';
comment on column V_MAIL_RECORD_HIS.status
  is '状态 0=待分配 1=待发送 9=调度出错 2=发送成功 3=黑名单 4=receiver 不存在';
comment on column V_MAIL_RECORD_HIS.msg
  is '描述 ';
comment on column V_MAIL_RECORD_HIS.mail_client
  is '发送客户端';
comment on column V_MAIL_RECORD_HIS.subject
  is '主题';
comment on column V_MAIL_RECORD_HIS.sender_name
  is '发送者名  默认为科研之友 分中英文';


-- Create table
create table V_MAIL_SENDER
(
  id                  NUMBER(10) not null,
  account             VARCHAR2(50) not null,
  password            VARCHAR2(50) not null,
  prior_template_code VARCHAR2(20),
  prior_email         VARCHAR2(50),
  prior_client        VARCHAR2(50),
  max_mail_count      NUMBER(5) not null,
  today_mail_count    NUMBER(5) not null,
  status              NUMBER(2) not null,
  create_date         DATE not null,
  update_date         DATE,
  msg                 VARCHAR2(500 CHAR),
  port                NUMBER(8) not null,
  host                VARCHAR2(50) not null
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
comment on table V_MAIL_SENDER
  is '邮件发送账号表';
-- Add comments to the columns 
comment on column V_MAIL_SENDER.id
  is 'id';
comment on column V_MAIL_SENDER.account
  is '账号';
comment on column V_MAIL_SENDER.password
  is '密码';
comment on column V_MAIL_SENDER.prior_template_code
  is '优先发送模板 可多个 冒号分割,前后必须有冒号';
comment on column V_MAIL_SENDER.prior_email
  is '优先发送邮箱 可多个 冒号分割,前后必须有冒号';
comment on column V_MAIL_SENDER.prior_client
  is '优先客户端 冒号分割,前后必须有冒号 ';
comment on column V_MAIL_SENDER.max_mail_count
  is '每天最大发送数';
comment on column V_MAIL_SENDER.today_mail_count
  is '已经发送数';
comment on column V_MAIL_SENDER.status
  is '状态 0为可用 1为不可用 9为超过限制（会自动更新） 88为管理员通知账号';
comment on column V_MAIL_SENDER.msg
  is '描述 \n如果是管理员邮件需要备注收件人 xxx@xx.com;xxx...分号分割,第一个为收件人,后面的是抄送';
comment on column V_MAIL_SENDER.port
  is '端口号';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_MAIL_SENDER
  add constraint V_MAIL_SENDER_PRIMARY_KEY primary key (ID)
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
create unique index V_MAIL_SENDER_PRIMARY_IDNEX_1 on V_MAIL_SENDER (ACCOUNT)
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
create table V_MAIL_TEMPLATE
(
  template_code NUMBER(18) not null,
  template_name VARCHAR2(100 CHAR) not null,
  subject_zh    VARCHAR2(500 CHAR) not null,
  subject_en    VARCHAR2(500 CHAR) not null,
  status        NUMBER(2) not null,
  create_date   DATE not null,
  update_date   DATE not null,
  msg           VARCHAR2(500 CHAR),
  mail_type_id  NUMBER(8) not null,
  limit_status  NUMBER(2) default 0 not null
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
comment on table V_MAIL_TEMPLATE
  is '邮件模版记录表';
-- Add comments to the columns 
comment on column V_MAIL_TEMPLATE.template_code
  is '模版标识,必须为3位数以上';
comment on column V_MAIL_TEMPLATE.template_name
  is '模版名字';
comment on column V_MAIL_TEMPLATE.subject_zh
  is '中文主题，如果有参数写成占位表达式';
comment on column V_MAIL_TEMPLATE.subject_en
  is '英文主题，如果有参数写成占位表达式';
comment on column V_MAIL_TEMPLATE.status
  is '状态 0=可用 1=不可用';
comment on column V_MAIL_TEMPLATE.create_date
  is '创建时间';
comment on column V_MAIL_TEMPLATE.update_date
  is '更新时间';
comment on column V_MAIL_TEMPLATE.msg
  is '描述';
comment on column V_MAIL_TEMPLATE.mail_type_id
  is '模版类型Id';
comment on column V_MAIL_TEMPLATE.limit_status
  is '0=无限制  1=每天对一个邮箱发一封 2=每周对一个邮箱发一封 3=每月对一个邮箱发一封';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_MAIL_TEMPLATE
  add constraint PK_V_MAIL_TEMPLATE primary key (TEMPLATE_CODE)
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
-- Create/Recreate check constraints 
alter table V_MAIL_TEMPLATE
  add constraint V_MAIL_TEMPLATE_CHECK_1
  check (TEMPLATE_CODE >100);



-- Create sequence 
create sequence V_SEQ_MAIL_BLACKLIST
minvalue 1
maxvalue 99999999
start with 81
increment by 1
cache 20;

-- Create sequence 
create sequence V_SEQ_MAIL_ORIGINAL_DATA
minvalue 1
maxvalue 199999999999999999
start with 98881
increment by 1
cache 20;


-- Create sequence 
create sequence V_SEQ_MAIL_TEMPLATE
minvalue 1
maxvalue 199999999999999999
start with 10120
increment by 1
cache 20;
--新建邮件相关表 20180425 tsz end


--原因（SCM-17330 关键词翻译接口） 2018-04-25 ajb start


insert into V_QUARTZ_CRON_EXPRESSION (id , Cron_Trigger_Bean , Cron_Expression , Status ,Description ) values(167,'pdwhPubKeywordZhSplitTaskTrigger' , '*/10 * * * * ?'  ,0 ,'基准库成果关键词，跑任务,任务完成，就要删除该任务' ) ;

commit ;

insert into   v_open_token_service_const( id , token , service_type  , descr )  values (  742 , '00000000' , 'pdw83pkd',  '获取需要翻译的基准库成果关键词')  ;
insert into   v_open_token_service_const( id , token , service_type  , descr )  values (  743 , '00000000' , 'tsl83pkd',  '翻译的基准库成果关键词')  ;

commit ;
--原因（SCM-17330 关键词翻译接口） 2018-04-25 ajb end

--原因（    SCM-17330 关键词翻译接口） 2018-04-25 ajb start

--update  V_QUARTZ_CRON_EXPRESSION t set   t.status = 1  where t.cron_trigger_bean ='pdwhPubKeywordZhSplitTaskTrigger' ;
--commit ;

--原因（    SCM-17330 关键词翻译接口） 2018-04-25 aj end


--原因（添加邮件记录表） 2018-04-27 zzx begin
create sequence V_SEQ_MAIL_CONNECT_ERROR
minvalue 1
maxvalue 199999999999999999
start with 1
increment by 1
cache 20;

-- Create table
create table V_MAIL_CONNECT_ERROR
(
  id          NUMBER(8) not null,
  account     VARCHAR2(100 CHAR),
  msg         VARCHAR2(4000 CHAR),
  pwd         VARCHAR2(100 CHAR),
  host        VARCHAR2(100 CHAR),
  create_date DATE default sysdate,
  update_date DATE default sysdate
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table V_MAIL_CONNECT_ERROR
  is '邮件收信箱链接失败记录表';
-- Add comments to the columns 
comment on column V_MAIL_CONNECT_ERROR.account
  is '邮箱地址';
comment on column V_MAIL_CONNECT_ERROR.msg
  is '错误描述';
comment on column V_MAIL_CONNECT_ERROR.pwd
  is '密码';
comment on column V_MAIL_CONNECT_ERROR.host
  is '链接服务';
comment on column V_MAIL_CONNECT_ERROR.create_date
  is '创建时间';
comment on column V_MAIL_CONNECT_ERROR.update_date
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_MAIL_CONNECT_ERROR
  add constraint PK_V_MAIL_CONNECT_ERROR primary key (ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;


--原因（添加邮件记录表） 2018-04-27 zzx end
--创建邮件管理通知邮件记录表 20180427 tsz begin
-- Create sequence 
create sequence V_SEQ_MAIL_MONITOR_LOG
minvalue 1
maxvalue 199999999999999999
start with 98881
increment by 1
cache 20;

-- Create table
create table V_MAIL_MONITOR_LOG
(
  id          NUMBER(10) not null,
  sender      VARCHAR2(50),
  receiver    VARCHAR2(200),
  update_date DATE,
  msg         VARCHAR2(1000 CHAR)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table V_MAIL_MONITOR_LOG
  is '管理员通知记录表';
-- Add comments to the columns 
comment on column V_MAIL_MONITOR_LOG.sender
  is '发送者';
comment on column V_MAIL_MONITOR_LOG.receiver
  is '接收者 后面 的为抄送 ';
comment on column V_MAIL_MONITOR_LOG.update_date
  is '时间';
comment on column V_MAIL_MONITOR_LOG.msg
  is '描述';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_MAIL_MONITOR_LOG
  add constraint PK_V_MAIL_MONITOR_LOG primary key (ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
  
  --创建邮件管理通知邮件记录表 20180427 tsz end
  
--原因（SCM-17380 成果指派改造）2018-04-27 zll start

  -- Create table
create table PUB_ASSIGN
(
  pub_id          number(18) not null,
  psn_id          number(18) not null,
  confrim_result  number(2) default 0,
  is_auto_confrim number(2) default 0,
  email_match     number(2) default 0,
  fullname_match  number(2) default 0,
  initname_match  number(2) default 0,
  ins_match       number(2) default 0,
  friend_match    number(2) default 0,
  keywords_match  number(2) default 0,
  score           number(4) default 0
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 128K
    next 64K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table PUB_ASSIGN
  is '成果指派记录表';
-- Add comments to the columns 
comment on column PUB_ASSIGN.pub_id
  is '基准库成果Id';
comment on column PUB_ASSIGN.psn_id
  is '指派人员Id';
comment on column PUB_ASSIGN.confrim_result
  is '认领结果 0：未认领，1：已认领，2：拒绝';
comment on column PUB_ASSIGN.is_auto_confrim
  is '是否自动确认';
comment on column PUB_ASSIGN.email_match
  is '邮件匹配';
comment on column PUB_ASSIGN.fullname_match
  is '全名匹配';
comment on column PUB_ASSIGN.initname_match
  is '简称匹配';
comment on column PUB_ASSIGN.ins_match
  is '单位匹配';
comment on column PUB_ASSIGN.friend_match
  is '合作者匹配';
comment on column PUB_ASSIGN.keywords_match
  is '关键词匹配';
comment on column PUB_ASSIGN.score
  is '推荐总分';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PUB_ASSIGN
  add constraint pk_pub_assign_log primary key (PUB_ID, PSN_ID);
--原因（SCM-17380 成果指派改造）2018-04-27 zll end

  --原因（SCM-17380 成果指派改造）2018-04-27 zll start
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) 
values(168,'PdwhPubAssignTaskTrigger','*/10 * * * * ?',1,'基准库成果指派任务');
--原因（SCM-17380 成果指派改造）2018-04-27 zll end
  
--原因（SCM-17380 成果指派改造） 2018-04-28 zll begin
  ALTER TABLE PUB_ASSIGN RENAME TO PUB_ASSIGN_LOG;
  
  --原因（SCM-17380 成果指派改造） 2018-04-28 zll end


--原因（SCM-17380 成果指派改造） 2018-04-28 zll begin
insert into app_quartz_setting(app_id,task_name,value) values(195,'PdwhPubAssignTask_removePubIdCache',0);
--原因（SCM-17380 成果指派改造） 2018-04-28 zll end
--原因（SCM-17380 成果指派改造） 2018-04-28 zll begin
alter table PUB_ASSIGN_LOG rename column is_auto_confrim to is_auto_confirm;
  alter table PUB_ASSIGN_LOG rename column friend_match to friends_match;
  alter table PUB_ASSIGN_LOG rename column confrim_result to confirm_result;
  --原因（SCM-17380 成果指派改造） 2018-04-28 zll end
--原因（SCM-17388 基金推荐----------左边栏申请资格条件，改成下拉框的形式） 2018-4-28 LTL begin
-- Create table
create table V_FUND_RECOMMEND_SENIOR
(
  id             NUMBER(18) not null,
  psn_id         NUMBER(18) not null,
  seniority_code NUMBER(2),
  update_date    DATE
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
comment on table V_FUND_RECOMMEND_SENIOR
  is '基金推荐申请资格条件表';
-- Add comments to the columns 
comment on column V_FUND_RECOMMEND_SENIOR.id
  is '主键Id';
comment on column V_FUND_RECOMMEND_SENIOR.psn_id
  is '人员ID';
comment on column V_FUND_RECOMMEND_SENIOR.seniority_code
  is '基金申请资格，1：企业， 2：科研机构，0：其他';
comment on column V_FUND_RECOMMEND_SENIOR.update_date
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_FUND_RECOMMEND_SENIOR
  add constraint PK_FUND_RECOMMEND_SENIOR primary key (ID)
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
create sequence SEQ_FUND_RECOMMEND_SENIOR
minvalue 1
maxvalue 999999999999999999
start with 980
increment by 1
cache 20;
--原因（SCM-17388 基金推荐----------左边栏申请资格条件，改成下拉框的形式） 2018-4-28 LTL end
--原因（SCM-17110 个人主页显示关键词优化） 2018-5-5 LTL begin

create index UK_V_KEYWORDS_CODE1 on V_KEYWORDS_DIC (application_code1);
create index UK_V_KEYWORDS_CODE2 on V_KEYWORDS_DIC (application_code2);

--原因（SCM-17110 个人主页显示关键词优化） 2018-5-5 LTL end
--原因（SCM-17487 任务跑完后，表pub_assign_log没有update_date字段）2018-05-05 zll begin
-- Alter table 
alter table PUB_ASSIGN_LOG
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table PUB_ASSIGN_LOG add update_time date;
-- Add comments to the columns 
comment on column PUB_ASSIGN_LOG.update_time
  is '更新时间';
  --原因（SCM-17487 任务跑完后，表pub_assign_log没有update_date字段）2018-05-05 zll end


--原因（SCM-17574 成果指派，pub_assign_log表新增一个id字段，作为主键） 2018-05-10 zll begin
-- Alter table 
alter table PUB_ASSIGN_LOG
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table PUB_ASSIGN_LOG add id NUMBER(18);
-- Add comments to the columns 
comment on column PUB_ASSIGN_LOG.id
  is '主键';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PUB_ASSIGN_LOG
  add constraint PK_PUB_ASSIGN_LOG_id unique (ID);
  
  alter table PUB_ASSIGN_LOG modify id not null;
  
  
  --原因（SCM-17574 成果指派，pub_assign_log表新增一个id字段，作为主键） 2018-05-10 zll end

  


--原因（SCM-17574 成果指派，pub_assign_log表新增一个id字段，作为主键） 2018-05-10 zll begin
create sequence SEQ_PUB_ASSIGN_LOG
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 10;

--原因（SCM-17574 成果指派，pub_assign_log表新增一个id字段，作为主键） 2018-05-10 zll end


--原因（SCM-17574 成果指派，pub_assign_log表新增一个id字段，作为主键） 2018-05-10 zll begin
alter table PUB_ASSIGN_LOG
  drop constraint PK_PUB_ASSIGN_LOG_ID cascade;
alter table PUB_ASSIGN_LOG
  add constraint PK_PUB_ASSIGN_LOG_ID primary key (ID)
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

  -- Alter table
alter table PUB_ASSIGN_LOG
  storage
  (
    next 8
  )
;
-- Drop primary, unique and foreign key constraints
alter table PUB_ASSIGN_LOG
  drop constraint PK_PUB_ASSIGN_LOG_LOG cascade;
-- Create/Recreate primary, unique and foreign key constraints
alter table PUB_ASSIGN_LOG
  add constraint uk_PUB_ASSIGN_LOG unique (PUB_ID, PSN_ID)
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
  --原因（SCM-17574 成果指派，pub_assign_log表新增一个id字段，作为主键） 2018-05-10 zll end



--原因（SCM-17574 成果指派，pub_assign_log表新增一个id字段，作为主键） 2018-05-15 zll begin
-- Alter table 
alter table PUB_ASSIGN_LOG
  storage
  (
    next 8
  )
;
-- Create/Recreate primary, unique and foreign key constraints 
alter table PUB_ASSIGN_LOG
  drop constraint PK_PUB_ASSIGN_LOG cascade;
alter table PUB_ASSIGN_LOG
  add constraint PK_PUB_ASSIGN_LOG unique (PUB_ID, PSN_ID)
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
  --原因（SCM-17574 成果指派，pub_assign_log表新增一个id字段，作为主键） 2018-05-15 zll end
  
  --原因（SCM-16501） 2018-5-15 HCJ begin
CREATE TABLE V_JOB_ONLINE_CONFIG
  ( ID VARCHAR2(40 BYTE) NOT NULL,
    JOB_NAME VARCHAR2(200 BYTE) NOT NULL,
    ENABLE NUMBER(1,0) DEFAULT 1 NOT NULL,
    WEIGHT CHAR(1 BYTE) DEFAULT 'B' NOT NULL,
    PRIORITY NUMBER(9,0) DEFAULT 99 NOT NULL,
    GMT_CREATE DATE DEFAULT sysdate,
    GMT_MODIFIED DATE DEFAULT sysdate,
    CONSTRAINT V_JOB_ONLINE_CONFIG_PK_ID PRIMARY KEY (ID) USING INDEX,
    CONSTRAINT V_JOB_ONLINE_CONFIG_UK1 UNIQUE (JOB_NAME) USING INDEX
  );

  COMMENT ON COLUMN V_JOB_ONLINE_CONFIG.ID IS '任务ID，UUID2生成';
  COMMENT ON COLUMN V_JOB_ONLINE_CONFIG.JOB_NAME IS '任务bean名称，唯一';
  COMMENT ON COLUMN V_JOB_ONLINE_CONFIG.ENABLE IS '任务是否可用';
  COMMENT ON COLUMN V_JOB_ONLINE_CONFIG.PRIORITY IS '任务优先级';
  COMMENT ON COLUMN V_JOB_ONLINE_CONFIG.GMT_CREATE IS '任务创建日期';
  COMMENT ON COLUMN V_JOB_ONLINE_CONFIG.GMT_MODIFIED IS '任务修改日期';
  COMMENT ON COLUMN V_JOB_ONLINE_CONFIG.WEIGHT IS '任务权重，A-50%，B-30%，C-15%，D-5%';
  COMMENT ON TABLE V_JOB_ONLINE_CONFIG  IS '在线任务信息配置表';

  CREATE OR REPLACE TRIGGER TRIG_UPDATE_JOB_ONLINE_CONFIG
  BEFORE INSERT OR UPDATE ON v_job_online_config
  for each row
  BEGIN
    IF inserting THEN
    :NEW.GMT_CREATE := SYSDATE;
    END IF;
    :NEW.GMT_MODIFIED := SYSDATE;
  END;
  /
  ALTER TRIGGER TRIG_UPDATE_JOB_ONLINE_CONFIG ENABLE;

CREATE TABLE V_JOB_ONLINE
   (  ID VARCHAR2(40 BYTE) NOT NULL,
      JOB_NAME VARCHAR2(200 BYTE) NOT NULL,
      STATUS NUMBER(1,0) DEFAULT 0 NOT NULL,
      PRIORITY NUMBER(9,0) DEFAULT 99 NOT NULL,
      DATA_MAP VARCHAR2(200 BYTE),
      ERR_MSG VARCHAR2(1000 BYTE),
      GMT_CREATE DATE DEFAULT sysdate,
      GMT_MODIFIED DATE DEFAULT sysdate,
      CONSTRAINT V_JOB_ONLINE_PK_ID PRIMARY KEY (ID) USING INDEX
   );

  COMMENT ON COLUMN V_JOB_ONLINE.ID IS '任务ID，由UUID2生成';
  COMMENT ON COLUMN V_JOB_ONLINE.JOB_NAME IS '任务bean名称，外键关联v_job_info_online_setting表字段job_name';
  COMMENT ON COLUMN V_JOB_ONLINE.STATUS IS '任务执行状态：-1-执行失败，0-未执行，1-等待中，2-正在处理，3-处理完毕';
  COMMENT ON COLUMN V_JOB_ONLINE.PRIORITY IS '任务优先级，同v_job_info_online_setting表中对应job_name的记录的优先级';
  COMMENT ON COLUMN V_JOB_ONLINE.DATA_MAP IS '任务处理需要的额外参数，json格式字符串';
  COMMENT ON COLUMN V_JOB_ONLINE.ERR_MSG IS '任务处理失败时记录错误信息';
  COMMENT ON COLUMN V_JOB_ONLINE.GMT_CREATE IS '记录创建时间';
  COMMENT ON COLUMN V_JOB_ONLINE.GMT_MODIFIED IS '记录修改时间';

  CREATE INDEX V_JOB_ONLINE_INDEX1 ON V_JOB_ONLINE (PRIORITY, GMT_MODIFIED);

  CREATE OR REPLACE TRIGGER TRIG_UPDATE_V_JOB_ONLINE
  BEFORE INSERT OR UPDATE ON v_job_online
  for each row
  BEGIN
    IF inserting THEN
    :NEW.GMT_CREATE := SYSDATE;
    END IF;
    :NEW.GMT_MODIFIED := SYSDATE;
  END;
  /
  ALTER TRIGGER TRIG_UPDATE_V_JOB_ONLINE ENABLE;


CREATE TABLE V_JOB_OFFLINE
  ( ID VARCHAR2(40 BYTE) NOT NULL,
    JOB_NAME VARCHAR2(200 BYTE) NOT NULL,
    ENABLE NUMBER(1,0) DEFAULT 0 NOT NULL ENABLE,
    DB_SESSION_ENUM VARCHAR2(50 BYTE) NOT NULL,
    TABLE_NAME VARCHAR2(50 BYTE) NOT NULL,
    UNIQUE_KEY VARCHAR2(50 BYTE) NOT NULL,
    BEGIN NUMBER(18,0) DEFAULT 1 NOT NULL,
    END NUMBER(18,0) DEFAULT 999999999999999999 NOT NULL,
    THREAD_COUNT NUMBER(9,0) DEFAULT 1 NOT NULL ENABLE,
    WEIGHT CHAR(1 BYTE) DEFAULT NULL NOT NULL ENABLE,
    PRIORITY NUMBER(9,0) DEFAULT 99,
    DATA_MAP VARCHAR2(200 BYTE),
    STATUS NUMBER(1,0) DEFAULT 0,
    PERCENT NUMBER(4,3) DEFAULT 0.000,
    ERR_MSG VARCHAR2(4000 BYTE),
    COUNT NUMBER(18,0),
    GMT_CREATE DATE DEFAULT sysdate,
    GMT_MODIFIED DATE DEFAULT sysdate,
    CONSTRAINT V_JOB_OFFLINE_PK PRIMARY KEY (ID) USING INDEX,
    CONSTRAINT V_JOB_OFFLINE_UK1 UNIQUE (JOB_NAME) USING INDEX
  );

  COMMENT ON COLUMN V_JOB_OFFLINE.ID IS '任务id，主键';
  COMMENT ON COLUMN V_JOB_OFFLINE.JOB_NAME IS '任务名称，非空，唯一';
  COMMENT ON COLUMN V_JOB_OFFLINE.DB_SESSION_ENUM IS '数据源，非空';
  COMMENT ON COLUMN V_JOB_OFFLINE.TABLE_NAME IS '业务表名，非空';
  COMMENT ON COLUMN V_JOB_OFFLINE.UNIQUE_KEY IS '业务表唯一键列名，非空';
  COMMENT ON COLUMN V_JOB_OFFLINE.BEGIN IS '任务处理的业务表唯一键列起始值，包括此值';
  COMMENT ON COLUMN V_JOB_OFFLINE.END IS '任务处理的业务表唯一键列结束值，不包括此值';
  COMMENT ON COLUMN V_JOB_OFFLINE.THREAD_COUNT IS '任务线程数，默认1，非空';
  COMMENT ON COLUMN V_JOB_OFFLINE.ENABLE IS '是否启动，默认0不启动';
  COMMENT ON COLUMN V_JOB_OFFLINE.STATUS IS '任务执行状态，0-未执行，1-等待中，2-正在执行，3-执行完毕';
  COMMENT ON COLUMN V_JOB_OFFLINE.PERCENT IS '任务执行进度，两位有效数字';
  COMMENT ON COLUMN V_JOB_OFFLINE.PRIORITY IS '同等权重情况下的任务优先级，默认为99，值越小优先级越高';
  COMMENT ON COLUMN V_JOB_OFFLINE.GMT_CREATE IS '记录最近一次的创建时间';
  COMMENT ON COLUMN V_JOB_OFFLINE.GMT_MODIFIED IS '记录最近一次的修改时间';
  COMMENT ON COLUMN V_JOB_OFFLINE.DATA_MAP IS '业务参数数据集合，json格式';
  COMMENT ON COLUMN V_JOB_OFFLINE.ERR_MSG IS '任务执行失败，记录错误信息';
  COMMENT ON COLUMN V_JOB_OFFLINE.COUNT IS '任务需要执行的总记录数，自动计算，无需设置';
  COMMENT ON COLUMN V_JOB_OFFLINE.WEIGHT IS '任务权重，A-50%，B-30%，C-15%，D-5%';
  COMMENT ON TABLE V_JOB_OFFLINE  IS '离线任务信息记录表';

  CREATE INDEX V_JOB_OFFLINE_INDEX1 ON V_JOB_OFFLINE (PRIORITY, GMT_MODIFIED);

  CREATE OR REPLACE TRIGGER TRIGGER_UPDATE_V_JOB_OFFLINE
  BEFORE INSERT ON V_JOB_OFFLINE
  FOR EACH ROW
  BEGIN
    IF inserting THEN
    :NEW.GMT_CREATE := SYSDATE;
    END IF;
    :NEW.GMT_MODIFIED := SYSDATE;
  END;
  /
  ALTER TRIGGER TRIGGER_UPDATE_V_JOB_OFFLINE ENABLE;


  CREATE TABLE V_JOB_SERVER_NODE
   (  ID NUMBER(*,0) NOT NULL ENABLE,
      NAME VARCHAR2(100 BYTE) NOT NULL ENABLE,
      HOST VARCHAR2(20 BYTE) NOT NULL ENABLE,
      MAX_POOL_SIZE NUMBER(18,0),
      CORE_POOL_SIZE NUMBER(18,0),
      KEEP_ALIVE_SECONDS NUMBER(18,0),
      QUEUE_CAPACITY NUMBER(18,0),
      GMT_CREATE DATE NOT NULL ENABLE,
      GMT_MODIFIED DATE NOT NULL ENABLE,
      CONSTRAINT V_JOB_SERVER_NODE_PK PRIMARY KEY (ID) USING INDEX,
      CONSTRAINT V_JOB_SERVER_NODE_UK1 UNIQUE (NAME) USING INDEX
   );

   COMMENT ON COLUMN V_JOB_SERVER_NODE.ID IS '服务器节点id';
   COMMENT ON COLUMN V_JOB_SERVER_NODE.NAME IS '服务器节点名称';
   COMMENT ON COLUMN V_JOB_SERVER_NODE.HOST IS '服务器IP地址';
   COMMENT ON COLUMN V_JOB_SERVER_NODE.GMT_CREATE IS '记录新增时间';
   COMMENT ON COLUMN V_JOB_SERVER_NODE.GMT_MODIFIED IS '更新时间';
   COMMENT ON COLUMN V_JOB_SERVER_NODE.MAX_POOL_SIZE IS '线程池最大线程数';
   COMMENT ON COLUMN V_JOB_SERVER_NODE.CORE_POOL_SIZE IS '线程池核心线程数';
   COMMENT ON COLUMN V_JOB_SERVER_NODE.KEEP_ALIVE_SECONDS IS '线程最大空闲时间';
   COMMENT ON COLUMN V_JOB_SERVER_NODE.QUEUE_CAPACITY IS '线程队列大小';
   COMMENT ON TABLE V_JOB_SERVER_NODE  IS 'center-job项目服务器节点信息记录表';
  CREATE SEQUENCE SEQ_V_JOB_SERVER_NODE MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 42 CACHE 20 ORDER  NOCYCLE ;


  CREATE TABLE V_JOB_ONLINE_HISTORY
   (  ID VARCHAR2(40 BYTE) NOT NULL ENABLE,
      JOB_NAME VARCHAR2(200 BYTE) NOT NULL ENABLE,
      PRIORITY NUMBER(9,0) DEFAULT 99 NOT NULL ENABLE,
      DATA_MAP VARCHAR2(200 BYTE),
      GMT_CREATE DATE DEFAULT sysdate,
      GMT_MODIFIED DATE DEFAULT sysdate,
      CONSTRAINT V_JOB_ONLINE_HISTORY_PK1 PRIMARY KEY (ID) USING INDEX
   );

  COMMENT ON COLUMN V_JOB_ONLINE_HISTORY.ID IS '任务ID，由UUID2生成';
  COMMENT ON COLUMN V_JOB_ONLINE_HISTORY.JOB_NAME IS '任务bean名称，外键关联V_JOB_ONLINE_HISTORY_setting表字段job_name';
  COMMENT ON COLUMN V_JOB_ONLINE_HISTORY.PRIORITY IS '任务优先级，同V_JOB_ONLINE_HISTORY_setting表中对应job_name的记录的优先级';
  COMMENT ON COLUMN V_JOB_ONLINE_HISTORY.DATA_MAP IS '任务处理需要的额外参数，json格式字符串';
  COMMENT ON COLUMN V_JOB_ONLINE_HISTORY.GMT_CREATE IS '记录创建时间';
  COMMENT ON COLUMN V_JOB_ONLINE_HISTORY.GMT_MODIFIED IS '记录修改时间';

  CREATE INDEX V_JOB_ONLINE_HISTORY_PRIORITY ON V_JOB_ONLINE_HISTORY (PRIORITY);

--原因（SCM-16501） 2018-5-15 HCJ end
  
  
  