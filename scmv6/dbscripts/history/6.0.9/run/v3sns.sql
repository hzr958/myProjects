-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end



--原因   SCM-15675 科技领域和关键词表添加更新时间字段，用于排序 begin
alter table PSN_SCIENCE_AREA add update_date date;
comment on column PSN_SCIENCE_AREA.update_date
  is '更新时间';
alter table PSN_DISCIPLINE_KEY add update_date date;
comment on column PSN_DISCIPLINE_KEY.update_date
  is '更新时间';
--原因   SCM-15675 科技领域和关键词表添加更新时间字段，用于排序 end


--原因（记录单项重复成果表记录和重复成果记录组） 2018-01-02 YJ begin


-- Create table
create table V_PUB_SAME_ITEM
(
  id          NUMBER(18) not null,
  record_id   NUMBER(18),
  psn_id      NUMBER(18),
  pub_id      NUMBER(18),
  deal_status NUMBER(1),
  create_date DATE,
  update_date DATE
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
  
  -- Create sequence 
create sequence SEQ_V_PUB_SAME_ITEM
minvalue 1
maxvalue 199999999999999999
start with 1
increment by 1
cache 20;
-- Add comments to the table 
comment on table V_PUB_SAME_ITEM
  is '单项重复成果记录信息表';
-- Add comments to the columns 
comment on column V_PUB_SAME_ITEM.id
  is '主键id';
comment on column V_PUB_SAME_ITEM.record_id
  is '组id';
comment on column V_PUB_SAME_ITEM.psn_id
  is '人员id';
comment on column V_PUB_SAME_ITEM.pub_id
  is '成果id';
comment on column V_PUB_SAME_ITEM.deal_status
  is '0=未处理；1=保留；2=删除';
comment on column V_PUB_SAME_ITEM.create_date
  is '创建时间';
comment on column V_PUB_SAME_ITEM.update_date
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_PUB_SAME_ITEM
  add constraint PK_V_PUB_SAME_ITEM primary key (ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;

-- Create table
create table V_PUB_SAME_RECORD
(
  record_id   NUMBER(18) not null,
  psn_id      NUMBER(18),
  pub_count   NUMBER(18),
  deal_status NUMBER(1),
  create_date DATE,
  update_date DATE
)

tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
  -- Create sequence 
create sequence SEQ_V_PUB_SAME_RECORD
minvalue 1
maxvalue 199999999999999999
start with 1
increment by 1
cache 20;
-- Add comments to the table 
comment on table V_PUB_SAME_RECORD
  is '重复成果记录组信息表';
-- Add comments to the columns 
comment on column V_PUB_SAME_RECORD.record_id
  is '组id（主键）';
comment on column V_PUB_SAME_RECORD.psn_id
  is '人员id';
comment on column V_PUB_SAME_RECORD.pub_count
  is '成果数';
comment on column V_PUB_SAME_RECORD.deal_status
  is '0=未处理；1=已处理';
comment on column V_PUB_SAME_RECORD.create_date
  is '创建时间';
comment on column V_PUB_SAME_RECORD.update_date
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_PUB_SAME_RECORD
  add constraint PK_V_PUB_SAME_RECORD primary key (RECORD_ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;


--原因（记录单项重复成果表记录和重复成果记录组） 2018-01-02 YJ end


--原因 （SCM-15650群组文件分享添加新表） 2018-01-03 ajb end


-- Create table
create table V_GRP_FILE_SHARE_BASE
(
  id                NUMBER(18) not null,
  sharer_id         NUMBER(18) not null,
  grp_id            NUMBER(18) not null,
  create_date       DATE default sysdate not null,
  status            NUMBER(1) default 0 not null,
  update_date       DATE default sysdate not null,
  share_content_rel VARCHAR2(500)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16
    next 8
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table V_GRP_FILE_SHARE_BASE
  is '群组文件分享 主表';
-- Add comments to the columns 
comment on column V_GRP_FILE_SHARE_BASE.id
  is '主键';
comment on column V_GRP_FILE_SHARE_BASE.sharer_id
  is '分享人id';
comment on column V_GRP_FILE_SHARE_BASE.grp_id
  is '群组id';
comment on column V_GRP_FILE_SHARE_BASE.create_date
  is '创建时间';
comment on column V_GRP_FILE_SHARE_BASE.status
  is '状态  0 =正常  1=取消分享  2=删除';
comment on column V_GRP_FILE_SHARE_BASE.update_date
  is '更新时间';
comment on column V_GRP_FILE_SHARE_BASE.share_content_rel
  is '分享文件 发送的文本站内信，给来取消分享用';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_GRP_FILE_SHARE_BASE
  add constraint PK_V_GRP_FILE_SHARE_BASE primary key (ID)
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
create table V_GRP_FILE_SHARE_RECORD
(
  id              NUMBER(18) not null,
  sharer_id       NUMBER(18) not null,
  receiver_id     NUMBER(18) not null,
  grp_file_id     NUMBER(18) not null,
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
    initial 16
    next 8
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table V_GRP_FILE_SHARE_RECORD
  is '群组文件分享记录表-2017-07-12';
-- Add comments to the columns 
comment on column V_GRP_FILE_SHARE_RECORD.id
  is '主键';
comment on column V_GRP_FILE_SHARE_RECORD.sharer_id
  is '分享人id';
comment on column V_GRP_FILE_SHARE_RECORD.receiver_id
  is '接受人id';
comment on column V_GRP_FILE_SHARE_RECORD.grp_file_id
  is '群组文件id';
comment on column V_GRP_FILE_SHARE_RECORD.create_date
  is '创建时间，分享时间';
comment on column V_GRP_FILE_SHARE_RECORD.update_date
  is '更新时间，';
comment on column V_GRP_FILE_SHARE_RECORD.status
  is '0=正常 ； 1=取消分享 ； 2=删除';
comment on column V_GRP_FILE_SHARE_RECORD.share_base_id
  is '分享主表主键id';
comment on column V_GRP_FILE_SHARE_RECORD.msg_relation_id
  is '消息表的关联id';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_GRP_FILE_SHARE_RECORD
  add constraint PK_V_GRP_FILE_SHARE_RECORD unique (ID)
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
create index IDX_GRP_MSG_RELATION_ID on V_GRP_FILE_SHARE_RECORD (MSG_RELATION_ID)
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
create index IDX_GRP_SHARE_BASE_ID on V_GRP_FILE_SHARE_RECORD (SHARE_BASE_ID)
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
create sequence SEQ_V_GRP_FILE_SHARE_BASE
minvalue 1
maxvalue 999999999999999999
start with 11
increment by 1
cache 10;


-- Create sequence 
create sequence SEQ_V_GRP_FILE_SHARE_RECORD
minvalue 1
maxvalue 999999999999999999
start with 11
increment by 1
cache 10;


commit ;


--原因（SCM-15650） 2018-01-03  ajb begin

--原因（重复成果表创建索引） 2018-01-04 zzx begin
-- Create/Recreate indexes 
create index IDX_V_PUB_SAME_RECORD_PSN_ID on V_PUB_SAME_RECORD (PSN_ID)
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
create index IDX_V_PUB_SAME_ITEM_PSN_ID on V_PUB_SAME_ITEM (PSN_ID)
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
create index IDX_V_PUB_SAME_ITEM_RECORD_ID on V_PUB_SAME_ITEM (RECORD_ID)
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
--原因（记录单项重复成果表记录和重复成果记录组） 2018-01-04 zzx end
--新帐号合并创建表 20180109 tsz begin
-- Create table
create table V_ACCOUNTS_MERGE_TASK
(
  id          NUMBER(18) not null,
  save_psn_id NUMBER(18) not null,
  del_psn_id  NUMBER(18) not null,
  status      NUMBER(2) not null,
  create_date DATE,
  err_msg     CLOB,
  status_ext  NUMBER(2) not null
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
comment on table V_ACCOUNTS_MERGE_TASK
  is '人员合并任务记录表';
-- Add comments to the columns 
comment on column V_ACCOUNTS_MERGE_TASK.id
  is '主键';
comment on column V_ACCOUNTS_MERGE_TASK.save_psn_id
  is '合并人员主键 ';
comment on column V_ACCOUNTS_MERGE_TASK.del_psn_id
  is '被合并人主键';
comment on column V_ACCOUNTS_MERGE_TASK.status
  is '合并状态 0=需要合并 1=合并成功 99=合并错误';
comment on column V_ACCOUNTS_MERGE_TASK.create_date
  is '合并日期';
comment on column V_ACCOUNTS_MERGE_TASK.err_msg
  is '错误信息';
comment on column V_ACCOUNTS_MERGE_TASK.status_ext
  is '是否需要跑动态合并任务：0需要/1成功/99失败';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_ACCOUNTS_MERGE_TASK
  add constraint PK_V_ACCOUNTS_MERGE_TASK primary key (ID)
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
create index IDX_V_ACCOUNTS_DEL_PSN on V_ACCOUNTS_MERGE_TASK (DEL_PSN_ID)
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
create index IDX_V_ACCOUNTS_SAVE_PSN on V_ACCOUNTS_MERGE_TASK (SAVE_PSN_ID)
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

  
  
create sequence V_SEQ_ACCOUNTS_MERGE_TASK
minvalue 1
maxvalue 1999999999999
start with 1
increment by 1
cache 20;
  
  
-- Create table
create table V_ACCOUNTS_MERGE_DATA
(
  id          NUMBER(18) not null,
  save_psn_id NUMBER(18) not null,
  del_psn_id  NUMBER(18) not null,
  op_type     NUMBER(1),
  desc_msg    VARCHAR2(1000 CHAR),
  bak_data    CLOB,
  create_date DATE,
  status      NUMBER(2) not null
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16
    next 8
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table V_ACCOUNTS_MERGE_DATA
  is '帐号合并任务备份表';
-- Add comments to the columns 
comment on column V_ACCOUNTS_MERGE_DATA.id
  is '主键';
comment on column V_ACCOUNTS_MERGE_DATA.save_psn_id
  is '合并人员主键 ';
comment on column V_ACCOUNTS_MERGE_DATA.del_psn_id
  is '被合并人主键';
comment on column V_ACCOUNTS_MERGE_DATA.op_type
  is '合并类型 1=合并，0=删除';
comment on column V_ACCOUNTS_MERGE_DATA.desc_msg
  is '合并信息';
comment on column V_ACCOUNTS_MERGE_DATA.bak_data
  is '备份数据';
comment on column V_ACCOUNTS_MERGE_DATA.create_date
  is '合并日期';
comment on column V_ACCOUNTS_MERGE_DATA.status
  is '状态0=不成功，1=成功';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_ACCOUNTS_MERGE_DATA
  add constraint PK_V_ACCOUNTS_MERGE_DATA primary key (ID)
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

create sequence V_SEQ_ACCOUNTS_MERGE_DATA
minvalue 1
maxvalue 1999999999999
start with 1
increment by 1
cache 20;

--新帐号合并创建表 20180109 tsz end

--原因（因为成果标题过长所以消息内容表添加长度） 2018-1-10 zzx begin


alter table V_MSG_CONTENT modify content VARCHAR2(4000);
commit;

--原因（因为成果标题过长所以消息内容表添加长度） 2018-1-10 zzx end






--原因（SCM-15797） 2018-01-10 LJ begin

ALTER TABLE v_pub_index_url ADD pub_long_index_url VARCHAR2(100 CHAR);

insert into v_quartz_cron_expression values(100,'SyncPmNameFromPersonTaskTrigger','*/30 * * * * ?',0,'补充数据到pmisiname(101)、pmcnkiname(102)、psnins(103)');

insert into v_quartz_cron_expression values(101,'SnsDupPubGroupingTaskTrigger','*/10 * * * * ?',0,'个人重复成果分组');

insert into v_quartz_cron_expression values(102,'RenamePdwhFulltextFileTaskTrigger','*/30 * * * * ?',0,'临时任务重命名pdwh全文文件名');


--原因（SCM-15797） 2018-01-10 LJ end


--SCM-15850 简历功能改造 2018-1-3 WSN begin

--------------------简历主表--------------
create table V_PSN_RESUME
(
  cv_id  NUMBER(18) not null,
  owner_psn_id NUMBER(18) not null,
  cv_name varchar2(250 char) not null,
  cv_url varchar2(200 char),
  create_date date default sysdate,
  update_date date default sysdate,
  cv_type number(1) not null
);


-- Add comments to the table 
comment on table V_PSN_RESUME
  is '新的简历主表';
-- Add comments to the columns 
comment on column V_PSN_RESUME.cv_id
  is '主键，简历ID';
comment on column V_PSN_RESUME.owner_psn_id
  is '简历所有者ID';
comment on column V_PSN_RESUME.cv_name
  is '简历名称';
comment on column V_PSN_RESUME.cv_url
  is '简历地址';
comment on column V_PSN_RESUME.create_date
  is '创建时间';
comment on column V_PSN_RESUME.update_date
  is '创建时间';
comment on column V_PSN_RESUME.cv_type
  is '简历类型， 1：基金委简历';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_PSN_RESUME
  add constraint PK_V_PSN_RESUME primary key (cv_id);
  
create index IDX_V_PSN_RESUME on V_PSN_RESUME (owner_psn_id);

create sequence SEQ_PSN_RESUME
minvalue 1
maxvalue 9999999999999
start with 1
increment by 1
cache 20;
--------------------简历主表--------------

--------------------简历模块信息表----------
create table V_RESUME_MODULE
(
  id  NUMBER(18) not null,
  cv_id NUMBER(18) not null,
  module_id number(1) not null,
  module_seq number(2) not null,
  module_title varchar2(100 char),
  update_date date default sysdate,
  module_info clob 
);

-- Add comments to the table 
comment on table V_RESUME_MODULE
  is '新的简历主表';
-- Add comments to the columns 
comment on column V_RESUME_MODULE.id
  is '主键';
comment on column V_RESUME_MODULE.cv_id
  is '简历ID';
comment on column V_RESUME_MODULE.module_id
  is '模块ID, 1: 个人基本信息， 2：教育经历，3：工作经历，4：项目，5：代表性成果， 6：其他成果';
comment on column V_RESUME_MODULE.module_seq
  is '模块在简历中顺序';
comment on column V_RESUME_MODULE.module_title
  is '模块标题';
comment on column V_RESUME_MODULE.update_date
  is '更新时间';
comment on column V_RESUME_MODULE.module_info
  is '模块信息，json格式';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_RESUME_MODULE
  add constraint PK_V_RESUME_MODULE primary key (id);
  
create index IDX_V_RESUME_MODULE on V_RESUME_MODULE (cv_id, module_id);
  
create sequence SEQ_RESUME_MODULE
minvalue 1
maxvalue 9999999999999
start with 1
increment by 1
cache 20;

--------------------简历模块信息表----------

commit;



--SCM-15850 简历功能改造 2018-1-3 WSN end


--SCM-15799 我的论文改造 2018-1-4 WCW begin

-- Create table
create table V_COLLECTED_PUB(
       id          NUMBER primary key,
       psn_id      NUMBER(18) not null,
       pub_id      NUMBER(18) not null,
       pub_db      NUMBER(1) not null,
       create_date DATE default sysdate not null
);
-- Create index
create index IDX_V_COLLECTED_PUB_ID on V_COLLECTED_PUB(psn_id);
-- Create sequence 
create sequence SEQ_V_COLLECTED_PUB
minvalue 1
nomaxvalue
start with 1
increment by 1
nocycle
cache 20;
-- Add comments to the table 
comment on table V_COLLECTED_PUB
  is '论文收藏表';
-- Add comments to the columns 
comment on column V_COLLECTED_PUB.id
  is '主键id';
comment on column V_COLLECTED_PUB.psn_id
  is '人员id';
comment on column V_COLLECTED_PUB.pub_id
  is '成果id';
comment on column V_COLLECTED_PUB.pub_db
  is '成果所属的库，0-基准库，1-个人库';
comment on column V_COLLECTED_PUB.create_date
  is '收藏时间';

-- init data
insert into V_COLLECTED_PUB(id, psn_id, pub_id, pub_db, create_date)
select SEQ_V_COLLECTED_PUB.NEXTVAL, t.owner_psn_id, t.pub_id, 1, t.create_date from v_pub_simple t where t.article_type=2 and t.status<>1; 
commit;
--SCM-15799 我的论文改造 2018-1-4 WCW end









--原因（SCM-15897添加备注） 2018-01-11 ajb start


-- Alter table 
alter table V_MSG_RELATION
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table V_MSG_RELATION modify deal_status NUMBER(2);

alter table V_MSG_RELATION
  storage
  (
    next 8
  )
;
-- Add comments to the columns 
comment on column V_MSG_RELATION.deal_status
  is '消息处理状态：0=未处理、1=同意、2=拒绝/忽略  , 11=成果被删除';

  --原因（SCM-15897添加备注） 2018-01-11 ajb end

--原因（SCM-15939后台任务创建帐号并关联） 2018-01-11 zzx begin
insert into v_quartz_cron_expression  values(103,'RegisterIsisPsnTaskTrigger','0 0/1 * * * ?',1,'isis注册后台任务');
commit;
--原因（SCM-15939后台任务创建帐号并关联） 2018-01-11 zzx end

--SCM-15850 简历功能改造 2018-1-3 WSN begin
-------------简历------------------------begin

alter table V_RESUME_MODULE add status NUMBER(1) default 0 not null;

create table V_CV_MODULE_INFO
(
  id  NUMBER(18) not null,
  module_info clob 
);

-- Add comments to the table 
comment on table V_CV_MODULE_INFO
  is '简历模块信息表';
-- Add comments to the columns 
comment on column V_CV_MODULE_INFO.id
  is '主键';
comment on column V_CV_MODULE_INFO.module_info
  is '模块信息，json格式';
  
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_CV_MODULE_INFO
  add constraint PK_V_CV_MODULE_INFO primary key (id);

create sequence SEQ_RESUME_MODULE_INFO
minvalue 1
maxvalue 9999999999999
start with 1
increment by 1
cache 20;

create table V_CV_MODULE_TYPE
(
  module_id  NUMBER(3) not null,
  instruction varchar2(100 char) 
);

insert into V_CV_MODULE_TYPE values(1, '个人基本信息');
insert into V_CV_MODULE_TYPE values(2, '教育经历');
insert into V_CV_MODULE_TYPE values(3, '工作经历');
insert into V_CV_MODULE_TYPE values(4, '项目');
insert into V_CV_MODULE_TYPE values(5, '代表性成果');
insert into V_CV_MODULE_TYPE values(6, '其他成果');
insert into V_CV_MODULE_TYPE values(7, '简介');

alter table V_RESUME_MODULE add module_info_id NUMBER(18) not null;

commit;


ALTER TABLE V_RESUME_MODULE DROP COLUMN module_info;


------------简历----------------------end
--SCM-15850 简历功能改造 2018-1-3 WSN begin

--SCM-15781 论文推荐，对科技领域和关键词的操作，不要同步到个人主页  2018-1-12 LTL begin
--------------------------------------------------------------create table RECOMMEND_DISCIPLINE_KEY
create table RECOMMEND_DISCIPLINE_KEY
(
  id           NUMBER(18) not null,
  key_id       NUMBER(18),
  key_words    VARCHAR2(200 CHAR) not null,
  psndis_id    NUMBER(18),
  lan_type     NUMBER(1) default 0,
  refresh_flag NUMBER(1) default 0,
  psn_id       NUMBER(18),
  status       NUMBER(1) default 0,
  update_date  DATE
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
comment on table RECOMMEND_DISCIPLINE_KEY
  is '成果推荐保存个人设置关键词的表';
-- Add comments to the columns 
comment on column RECOMMEND_DISCIPLINE_KEY.id
  is '主键';
comment on column RECOMMEND_DISCIPLINE_KEY.key_id
  is '对应的关键字表主键（const_dicipline_key），可为空';
comment on column RECOMMEND_DISCIPLINE_KEY.key_words
  is '关键词名称';
comment on column RECOMMEND_DISCIPLINE_KEY.psndis_id
  is '用户研究领域主键psn_discipline(废弃，改用psn_id)';
comment on column RECOMMEND_DISCIPLINE_KEY.lan_type
  is '关键字语言类别,0 中文,1 英文';
comment on column RECOMMEND_DISCIPLINE_KEY.refresh_flag
  is '相关关键词刷新之用';
comment on column RECOMMEND_DISCIPLINE_KEY.psn_id
  is '人员编码';
comment on column RECOMMEND_DISCIPLINE_KEY.status
  is '是否有效';
comment on column RECOMMEND_DISCIPLINE_KEY.update_date
  is '更新时间';
-- Create/Recreate indexes 
create index IDX_RECOMMEND_DIS_KEY_PSNID on RECOMMEND_DISCIPLINE_KEY (PSN_ID, KEY_WORDS)
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
alter table RECOMMEND_DISCIPLINE_KEY
  add constraint PK_RECOMMEND_DISCIPLINE_KEY primary key (ID)
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
--------------------------------------------------------------create table RECOMMEND_SCIENCE_AREA
-- Create table
create table RECOMMEND_SCIENCE_AREA
(
  id                 NUMBER(18) not null,
  science_area_id    NUMBER(5),
  science_area       VARCHAR2(200 CHAR) not null,
  psn_id             NUMBER(18),
  status             NUMBER(1) default 0,
  identification_sum NUMBER(6) default 0,
  science_area_en    VARCHAR2(200 CHAR),
  areaorder          NUMBER(5) default 0,
  update_date        DATE
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
comment on table RECOMMEND_SCIENCE_AREA
  is '论文推荐人员科技领域';
-- Add comments to the columns 
comment on column RECOMMEND_SCIENCE_AREA.id
  is '主键';
comment on column RECOMMEND_SCIENCE_AREA.science_area_id
  is '对应的关键字表主键（category_map_base），可为空';
comment on column RECOMMEND_SCIENCE_AREA.science_area
  is '关键字';
comment on column RECOMMEND_SCIENCE_AREA.psn_id
  is '人员编码';
comment on column RECOMMEND_SCIENCE_AREA.status
  is '是否有效';
comment on column RECOMMEND_SCIENCE_AREA.identification_sum
  is '认同数';
comment on column RECOMMEND_SCIENCE_AREA.areaorder
  is '领域排序';
comment on column RECOMMEND_SCIENCE_AREA.update_date
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table RECOMMEND_SCIENCE_AREA
  add constraint PK_RECOMMEND_SCIENCE_AREA primary key (ID)
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
-------------------
-- Create sequence 
create sequence SEQ_REC_SCIENCE_AREA
minvalue 1
maxvalue 999999999999999999
start with 667
increment by 1
cache 20;
-- Create sequence 
create sequence SEQ_REC_DISCIPLINE_KEY
minvalue 1
maxvalue 999999999999999999
start with 11960
increment by 1
cache 20;
--SCM-15781 论文推荐，对科技领域和关键词的操作，不要同步到个人主页  2018-1-12 LTL end

--原因（SCM-16120 人员信息更新，重新指派成果到人员） 2017-01-16 zll begin


insert into app_quartz_setting(app_id,task_name,value) values(98,'RePubAssignSendTask',0);


--原因（SCM-16120 人员信息更新，重新指派成果到人员） 2017-01-16 zll end



--原因（SCM-15784） 2018-1-16 HCJ begin

--修复archive_files表status列没有默认值得情况
UPDATE archive_files SET status=0 WHERE status IS NULL;
--修改archive_files表status列不能为null
alter table archive_files modify (status not null);

--个人文件添加到v_batch_jobs表记录，生成缩略图历史数据处理
INSERT INTO v_batch_jobs(job_id, job_context, weight, strategy, status, create_time)
SELECT batch_job_seq.nextval as job_id, '{"msg_id":'||f.file_id||',"file_type":"0"}' as job_context, 'B' as weight, 'abcdefgh' as strategy, 0 as status, sysdate as create_time
FROM archive_files f 
JOIN v_psn_file p ON p.archive_file_id = f.file_id
WHERE f.file_type='imgIc' AND f.status = 0 and p.status = 0;

--群组文件添加到v_batch_jobs表记录
INSERT INTO v_batch_jobs(job_id, job_context, weight, strategy, status, create_time)
SELECT batch_job_seq.nextval, '{"msg_id":'||f.file_id||',"file_type":"1"}' as job_context, 'B' as weight, 'abcdefgh' as strategy, 0 as status, sysdate as create_time
FROM archive_files f 
JOIN v_grp_file p ON p.archive_file_id = f.file_id
where f.file_type='imgIc' AND f.status = 0 and p.FILE_STATUS = 0;

--全文文件添加到v_batch_jobs表记录
INSERT INTO v_batch_jobs(job_id, job_context, weight, strategy, status, create_time)
select batch_job_seq.nextval,'{"msg_id":'||f.file_id||',"file_type":"2","pub_id":'||p.pub_id||'}' as job_context, 'B' as weight, 'abcdefgh' as strategy, 0 as status, sysdate as create_time
FROM archive_files f 
JOIN pub_fulltext p ON p.fulltext_file_id = f.file_id
where f.status = 0 and p.fulltext_file_ext in ('.jpg', '.pdf', '.png', '.jpeg', '.gif', '.bmp', '.tif', '.tiff', '.dib', '.psd', '.pcx', '.jfif', '.svg');

--原因（SCM-15784） 2018-1-16 HCJ end

--原因（数据统计分析平台） 2017-01-24 zll begin
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
 values(111,'JxProjectIndexTaskTrigger','*/10 * * * * ?',0,'江西项目创建索引');
insert into app_quartz_setting(app_id,task_name,value) values(99,'JxProjectIndexTask_removeUserCache',0);
insert into v_open_token_service_const(id,token,service_type,status,create_date,descr)
values(662,'00000000','kwsplit1',0,sysdate,'查找成果内容中的关键词');
insert into v_open_token_service_const(id,token,service_type,status,create_date,descr)
values(663,'00000000','kwsios01',0,sysdate,'根据关键词计算投入产出趋势取数据 ');
insert into v_open_token_service_const(id,token,service_type,status,create_date,descr)
values(664,'00000000','kwsres02',0,sysdate,'根据关键词计算科研趋势取数据');
insert into v_open_token_service_const(id,token,service_type,status,create_date,descr)
values(665,'00000000','seaibkw3',0,sysdate,'根据关键词检索机构');
insert into v_open_token_service_const(id,token,service_type,status,create_date,descr)
values(666,'00000000','seapbkw4',0,sysdate,'根据关键词检索论文');
insert into v_open_token_service_const(id,token,service_type,status,create_date,descr)
values(667,'00000000','seap1bkw',0,sysdate,'根据关键词检索专利');
insert into v_open_token_service_const(id,token,service_type,status,create_date,descr)
values(668,'00000000','seap2bkw',0,sysdate,'根据关键词检索专家');
insert into v_open_token_service_const(id,token,service_type,status,create_date,descr)
values(669,'00000000','seap3bkw',0,sysdate,'根据关键词检索项目');
--原因（数据统计分析平台） 2017-01-24 zll end








--原因（SCM-16221 成果加标识 有标记为可信来源） 2018-2-2 zzx begin


alter table publication add update_mark number(2);
comment on column publication.update_mark
  is '是否是在线导入或手工导入1=在线导入且未修改；2=在线导入且已修改；3=手工导入';
alter table v_pub_simple add update_mark number(2);
comment on column v_pub_simple.update_mark
  is '是否是在线导入或手工导入1=在线导入且未修改；2=在线导入且已修改；3=手工导入';

commit;
--原因（ SCM-16221 成果加标识 有标记为可信来源） 2018-2-2 zzx end






--原因（    SCM-16228 成果描述字段（brief_desc）字段需要修改） 2018-2-2 LTL begin

insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (116, 'UpdatePdwhPubBriefTaskTrigger', '*/10 * * * * ?', 1, '更新基准库表的brief_desc字段');

insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (117, 'UpdateSnsPubBriefTaskTrigger', '*/10 * * * * ?', 1, '更新个人库表的brief_desc字段');

insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (118, 'UpdateConfirmPubBriefTaskTrigger', '*/10 * * * * ?', 1, '更新成果指派表brief_desc字段');
commit;

--因（    SCM-16228 成果描述字段（brief_desc）字段需要修改） 2018-2-2 LTL end




--原因（SCM-16376 给用户发送新年邮件任务） 2018-02-09 zll begin


insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) values(140,'SendNewYearMailTaskTrigger','0 0/1 * * * ?',0,'给用户发新年邮件任务');


--原因（SCM-16376 给用户发送新年邮件任务） 2018-02-09 zll end


--原因（    SCM-16228 成果描述字段（brief_desc）字段需要修改） 2018-2-2 LTL begin
update v_quartz_cron_expression set status=0 where cron_trigger_bean='UpdatePdwhPubBriefTaskTrigger';
update v_quartz_cron_expression set status=0 where cron_trigger_bean='UpdateSnsPubBriefTaskTrigger';
update v_quartz_cron_expression set status=0 where cron_trigger_bean='UpdateConfirmPubBriefTaskTrigger';
commit;
--原因（    SCM-16228 成果描述字段（brief_desc）字段需要修改） 2018-2-2 LTL end


--原因（    SCM-16228 成果描述字段（brief_desc）字段需要修改改进） 2018-2-5 LTL begin
-- Create table
create table TEM_TASK_SNSBRIEF
(
  pub_id    NUMBER(18) not null,
  status    NUMBER(2) default 0 not null,
  error_msg VARCHAR2(700 CHAR)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table TEM_TASK_SNSBRIEF
  is '更新个人库成果的brief来源字段';
-- Add comments to the columns 
comment on column TEM_TASK_SNSBRIEF.status
  is '0还没更新，1已经更新，2出错';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TEM_TASK_SNSBRIEF
  add constraint PK_TEM_TASK_SNSBRIEF primary key (PUB_ID)
  disable;
insert into TEM_TASK_SNSBRIEF(pub_id,status) select pub_id,0 from publication where pub_id > 1000005278035 order by pub_id asc;

insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (120, 'UpdatePdwhPubBriefTaskTrigger1', '*/10 * * * * ?', 1, '更新基准库表的brief_desc字段');
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (121, 'UpdatePdwhPubBriefTaskTrigger2', '*/10 * * * * ?', 1, '更新基准库表的brief_desc字段');
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (122, 'UpdatePdwhPubBriefTaskTrigger3', '*/10 * * * * ?', 1, '更新基准库表的brief_desc字段');
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (123, 'UpdatePdwhPubBriefTaskTrigger4', '*/10 * * * * ?', 1, '更新基准库表的brief_desc字段');
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (124, 'UpdatePdwhPubBriefTaskTrigger5', '*/10 * * * * ?', 1, '更新基准库表的brief_desc字段');
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (125, 'UpdatePdwhPubBriefTaskTrigger6', '*/10 * * * * ?', 1, '更新基准库表的brief_desc字段');
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (126, 'UpdatePdwhPubBriefTaskTrigger7', '*/10 * * * * ?', 1, '更新基准库表的brief_desc字段');
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (127, 'UpdatePdwhPubBriefTaskTrigger8', '*/10 * * * * ?', 1, '更新基准库表的brief_desc字段');
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (128, 'UpdatePdwhPubBriefTaskTrigger9', '*/10 * * * * ?', 1, '更新基准库表的brief_desc字段');

insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (129, 'UpdateSnsPubBriefTaskTrigger1', '*/10 * * * * ?', 1, '更新个人库表的brief_desc字段');
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (130, 'UpdateSnsPubBriefTaskTrigger2', '*/10 * * * * ?', 1, '更新个人库表的brief_desc字段');
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (131, 'UpdateSnsPubBriefTaskTrigger3', '*/10 * * * * ?', 1, '更新个人库表的brief_desc字段');
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (132, 'UpdateSnsPubBriefTaskTrigger4', '*/10 * * * * ?', 1, '更新个人库表的brief_desc字段');
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (133, 'UpdateSnsPubBriefTaskTrigger5', '*/10 * * * * ?', 1, '更新个人库表的brief_desc字段');
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (134, 'UpdateSnsPubBriefTaskTrigger6', '*/10 * * * * ?', 1, '更新个人库表的brief_desc字段');
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (135, 'UpdateSnsPubBriefTaskTrigger7', '*/10 * * * * ?', 1, '更新个人库表的brief_desc字段');
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (136, 'UpdateSnsPubBriefTaskTrigger8', '*/10 * * * * ?', 1, '更新个人库表的brief_desc字段');
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (137, 'UpdateSnsPubBriefTaskTrigger9', '*/10 * * * * ?', 1, '更新个人库表的brief_desc字段');
commit;
--原因（    SCM-16228 成果描述字段（brief_desc）字段需要修改改进） 2018-2-5 LTL end










