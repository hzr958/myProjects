-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end




--原因（ 记录表open系统 数据处理记录  方便查看   以后的服务调用情况 ） 2016-12-8 tsz begin


create table V_OPEN_DATA_HANDLE_LOG
(
  id           NUMBER(13),
  token        VARCHAR2(8),
  service_type VARCHAR2(8),
  sum          NUMBER(13),
  last_access  DATE,
  disc         VARCHAR2(100 CHAR)
);
-- Add comments to the columns 
comment on column V_OPEN_DATA_HANDLE_LOG.id
  is '序列id';
comment on column V_OPEN_DATA_HANDLE_LOG.token
  is '系统授权码';
comment on column V_OPEN_DATA_HANDLE_LOG.service_type
  is '服务编码';
comment on column V_OPEN_DATA_HANDLE_LOG.sum
  is '成功访问次数(只统计成功访问并成功响应的次数，报错或有异常的 在错误日志表有记录)';
comment on column V_OPEN_DATA_HANDLE_LOG.last_access
  is '最后访问成功时间';
comment on column V_OPEN_DATA_HANDLE_LOG.disc
  is '描述';
  
  
  -- Create sequence 
create sequence V_SEQ_OPEN_DATA_HANDLE_LOG
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

--原因（ 记录表open系统 数据处理记录  方便查看   以后的服务调用情况） 2016-12-8 TSZ end



--原因（群组动态开发sql） 2016-12-19 ZZX begin
--V_GROUP_DYNAMIC_AWARDS
-- Create table
create table V_GROUP_DYNAMIC_AWARDS
(
  id           NUMBER(18) not null,
  dyn_id       NUMBER(18) not null,
  award_psn_id NUMBER(18) not null,
  award_date   DATE,
  status       NUMBER(2)
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
comment on table V_GROUP_DYNAMIC_AWARDS
  is '动态赞记录';
-- Add comments to the columns 
comment on column V_GROUP_DYNAMIC_AWARDS.id
  is '主键';
comment on column V_GROUP_DYNAMIC_AWARDS.dyn_id
  is '被赞的动态id';
comment on column V_GROUP_DYNAMIC_AWARDS.award_psn_id
  is '赞的人员id';
comment on column V_GROUP_DYNAMIC_AWARDS.award_date
  is '赞或者取消赞的时间';
comment on column V_GROUP_DYNAMIC_AWARDS.status
  is '状态0：赞 1：取消赞';
-- Create/Recreate indexes 
create index IDX_GDA_DYN_ID on V_GROUP_DYNAMIC_AWARDS (DYN_ID)
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
alter table V_GROUP_DYNAMIC_AWARDS
  add constraint PK_V_GROUP_DYNAMIC_AWARDS primary key (ID)
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
-- V_GROUP_DYNAMIC_COMMENTS
-- Create table
create table V_GROUP_DYNAMIC_COMMENTS
(
  id              NUMBER(18) not null,
  dyn_id          NUMBER(18) not null,
  comment_psn_id  NUMBER(18) not null,
  comment_content VARCHAR2(500 CHAR),
  comment_date    DATE,
  status          NUMBER(2)
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
comment on table V_GROUP_DYNAMIC_COMMENTS
  is '群组动态评论记录';
-- Add comments to the columns 
comment on column V_GROUP_DYNAMIC_COMMENTS.id
  is ' 主键id';
comment on column V_GROUP_DYNAMIC_COMMENTS.dyn_id
  is '被评论的动态id';
comment on column V_GROUP_DYNAMIC_COMMENTS.comment_psn_id
  is '评论的人员id';
comment on column V_GROUP_DYNAMIC_COMMENTS.comment_content
  is '评论的内容';
comment on column V_GROUP_DYNAMIC_COMMENTS.comment_date
  is '评论或者删除评论的时间';
comment on column V_GROUP_DYNAMIC_COMMENTS.status
  is '状态0：评论 1：删除评论';
-- Create/Recreate indexes 
create index IDX_GDC_DYN_ID on V_GROUP_DYNAMIC_COMMENTS (DYN_ID)
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
alter table V_GROUP_DYNAMIC_COMMENTS
  add constraint PK_V_GROUP_DYNAMIC_COMMENTS primary key (ID)
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

--V_GROUP_DYNAMIC_MSG
-- Create table
create table V_GROUP_DYNAMIC_MSG
(
  dyn_id          NUMBER(18) not null,
  group_id        NUMBER(18) not null,
  producer        NUMBER(18) not null,
  dyn_type        VARCHAR2(100 CHAR),
  dyn_tmp         VARCHAR2(100 CHAR),
  crente_date     DATE,
  update_date     DATE,
  status          NUMBER(2),
  rel_deal_status NUMBER(2),
  res_id          NUMBER(18),
  res_type        VARCHAR2(20 CHAR),
  same_flag       NUMBER(18),
  extend          VARCHAR2(100 CHAR)
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
comment on table V_GROUP_DYNAMIC_MSG
  is '群组动态信息表 群组动态主表';
-- Add comments to the columns 
comment on column V_GROUP_DYNAMIC_MSG.dyn_id
  is ' 动态id';
comment on column V_GROUP_DYNAMIC_MSG.group_id
  is '群组id';
comment on column V_GROUP_DYNAMIC_MSG.producer
  is '创建人ID';
comment on column V_GROUP_DYNAMIC_MSG.dyn_type
  is '动态类型';
comment on column V_GROUP_DYNAMIC_MSG.dyn_tmp
  is '动态模版';
comment on column V_GROUP_DYNAMIC_MSG.crente_date
  is '创建时间';
comment on column V_GROUP_DYNAMIC_MSG.update_date
  is '最后更新时间';
comment on column V_GROUP_DYNAMIC_MSG.status
  is ' 状态 正常:0， 删除:99';
comment on column V_GROUP_DYNAMIC_MSG.rel_deal_status
  is '关系处理状态 0 未处理 1 已经处理';
comment on column V_GROUP_DYNAMIC_MSG.res_id
  is '资源Id';
comment on column V_GROUP_DYNAMIC_MSG.res_type
  is '资源类型';
comment on column V_GROUP_DYNAMIC_MSG.same_flag
  is '动态来源（父级动态id）';
comment on column V_GROUP_DYNAMIC_MSG.extend
  is '预留字段';
-- Create/Recreate indexes 
create index V_GROUP_DYN_MSG_GROUP_ID on V_GROUP_DYNAMIC_MSG (GROUP_ID)
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
alter table V_GROUP_DYNAMIC_MSG
  add constraint PK_V_GROUP_DYNAMIC_MSG primary key (DYN_ID)
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

--V_GROUP_DYNAMIC_SHARE
-- Create table
create table V_GROUP_DYNAMIC_SHARE
(
  id            NUMBER(18) not null,
  dyn_id        NUMBER(18) not null,
  share_psn_id  NUMBER(18) not null,
  share_content VARCHAR2(1000 CHAR),
  share_date    DATE,
  status        NUMBER(2)
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
comment on table V_GROUP_DYNAMIC_SHARE
  is '群组动态分享记录';
-- Add comments to the columns 
comment on column V_GROUP_DYNAMIC_SHARE.id
  is '主键id';
comment on column V_GROUP_DYNAMIC_SHARE.dyn_id
  is '被分享的动态id';
comment on column V_GROUP_DYNAMIC_SHARE.share_psn_id
  is '分享的人员id';
comment on column V_GROUP_DYNAMIC_SHARE.share_content
  is ' 分享内容';
comment on column V_GROUP_DYNAMIC_SHARE.share_date
  is '分享或者删除分享时间';
comment on column V_GROUP_DYNAMIC_SHARE.status
  is ' 状态 0：分享 1： 删除分享';
-- Create/Recreate indexes 
create index IDX_GDS_DYN_ID on V_GROUP_DYNAMIC_SHARE (DYN_ID)
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
alter table V_GROUP_DYNAMIC_SHARE
  add constraint PK_V_GROUP_DYNAMIC_SHARE primary key (ID)
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

--V_GROUP_DYNAMIC_STATISTIC
-- Create table
create table V_GROUP_DYNAMIC_STATISTIC
(
  dyn_id        NUMBER(18) not null,
  award_count   NUMBER(6),
  comment_count NUMBER(6),
  share_count   NUMBER(6)
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
comment on table V_GROUP_DYNAMIC_STATISTIC
  is '群组动态操作统计 记录';
-- Add comments to the columns 
comment on column V_GROUP_DYNAMIC_STATISTIC.dyn_id
  is ' 动态id';
comment on column V_GROUP_DYNAMIC_STATISTIC.award_count
  is '赞次数';
comment on column V_GROUP_DYNAMIC_STATISTIC.comment_count
  is ' 评论次数';
comment on column V_GROUP_DYNAMIC_STATISTIC.share_count
  is ' 分享次数';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_GROUP_DYNAMIC_STATISTIC
  add constraint PK_V_GROUP_DYNAMIC_STATISTIC primary key (DYN_ID)
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
--序列
-- Create sequence 
create sequence SEQ_V_GROUP_DYNAMIC_AWARDS
minvalue 1
maxvalue 199999999999999999
start with 1
increment by 1
cache 20;
-- Create sequence 
create sequence SEQ_V_GROUP_DYNAMIC_COMMENTS
minvalue 1
maxvalue 199999999999999999
start with 1
increment by 1
cache 20;
-- Create sequence 
create sequence SEQ_V_GROUP_DYNAMIC_SHARE
minvalue 1
maxvalue 199999999999999999
start with 1
increment by 1
cache 20;
-- Create sequence 
create sequence SEQ_V_GROUP_DYNAMIC_MSG
minvalue 1
maxvalue 199999999999999999
start with 1
increment by 1
cache 20;
--原因（群组动态开发sql） 2016-12-19 ZZX end

--原因（修改sns无效表名） 2016-12-19 hzr begin

alter table pub_confirm rename to zdel_pub_confirm;
alter table pub_confirm_hi rename to zdel_pub_confirm_hi;

--原因（修改sns无效表名） 2016-12-19 hzr end



