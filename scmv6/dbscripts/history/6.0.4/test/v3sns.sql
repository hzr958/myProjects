-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（SCM-12213 站外论文、人员迁至新系统 ） 2017-04-26 zll begin
insert into v_quartz_cron_expression (id ,cron_trigger_bean,cron_expression,status,description)
 values(28,'indexInfoInitTaskTrigger','0/10 * * * * ?',0,'获取站外检索信息');

insert into v_quartz_cron_expression (id ,cron_trigger_bean,cron_expression,status,description) 
values(29,'indexPsnInfoInitTaskTrigger','0/10 * * * * ?',0,'初始化系统人员信息定时器任务');


--原因（SCM-12213 站外论文、人员迁至新系统 ） 2017-04-26 zll end
--原因（SCM-12641科研之友系统还有一些url是不安全的，需要修改，详细见描述） 2017-05-16 zzx begin

update person set avatars = replace(avatars,'http:','https:');
update v_grp_baseinfo set grp_auatars= replace(grp_auatars,'http:','https:'); 
update DYNAMIC_AWARD_PSN set awarder_avatar = replace(awarder_avatar,'http:','https:');
update PERSON_SYNC set psn_head_url = replace(psn_head_url,'http:','https:');
update psn_html set html_zh = replace(html_zh,'http:','https:');
update psn_html set html_en = replace(html_en,'http:','https:');
update ATT_PERSON set ref_head_url = replace(ref_head_url,'http:','https:');
update FULLTEXT_INBOX set receiver_avatar = replace(receiver_avatar,'http:','https:');
update FULLTEXT_INBOX set param_json = replace(param_json,'http:','https:');
update INVITE_INBOX set psn_head_url = replace(psn_head_url,'http:','https:');
update INVITE_MAILBOX set psn_head_url = replace(psn_head_url,'http:','https:');
update INVITE_MAILBOX set ext_other_info = replace(ext_other_info,'http:','https:');

--原因（SCM-12641科研之友系统还有一些url是不安全的，需要修改，详细见描述） 2017-05-16 zzx end










--原因（有CQ号带上CQ号） 2017-5-19 zx begin


alter table v_pub_simple add CITE_DATE date;



--原因（有CQ号带上CQ号） 2017-5-19 zx end


--原因（人员站外添加新url字段） 2017-05-27 ZZX begin

alter table PSN_PROFILE_URL add PSN_INDEX_URL varchar(100)


--原因（人员站外添加新url字段） 2017-05-27 ZZX end

--原因（SCM-12788 短地址 表，实体类，读取服务开发） 2017-05-25 ZZX begin

create sequence SEQ_V_OPEN_SHORT_URL
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 10;

create sequence SEQ_V_OPEN_SHORT_URL_USE_LOG
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 10;

create table V_OPEN_SHORT_URL_USE_LOG
(
  id                NUMBER(18) not null,
  short_url_paramet VARCHAR2(100 CHAR) not null,
  use_date          DATE,
  use_psn_id        NUMBER(18),
  use_ip            VARCHAR2(50 CHAR)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table V_OPEN_SHORT_URL_USE_LOG
  is '短地址使用日志表';
-- Add comments to the columns 
comment on column V_OPEN_SHORT_URL_USE_LOG.id
  is '主键ID';
comment on column V_OPEN_SHORT_URL_USE_LOG.short_url_paramet
  is '短地址';
comment on column V_OPEN_SHORT_URL_USE_LOG.use_date
  is '使用日期';
comment on column V_OPEN_SHORT_URL_USE_LOG.use_psn_id
  is '使用人';
comment on column V_OPEN_SHORT_URL_USE_LOG.use_ip
  is '来源IP';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_OPEN_SHORT_URL_USE_LOG
  add constraint PK_V_OPEN_SHORT_URL_USE_LOG primary key (ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
-- Create table
create table V_OPEN_SHORT_URL
(
  id                  NUMBER(18) not null,
  type                VARCHAR2(2),
  short_url           VARCHAR2(100 CHAR) not null,
  real_url_paramet    VARCHAR2(1000 CHAR),
  real_url_hash       NUMBER(18),
  has_expiration_time NUMBER(1),
  expiration_time     DATE,
  has_times_limit     NUMBER(1),
  times_limit         NUMBER(4),
  times_used          NUMBER(4),
  create_date         DATE,
  create_psn_id       NUMBER(18)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table V_OPEN_SHORT_URL
  is '短地址实体表';
-- Add comments to the columns 
comment on column V_OPEN_SHORT_URL.id
  is 'ID';
comment on column V_OPEN_SHORT_URL.type
  is '短地址业务类型AB...';
comment on column V_OPEN_SHORT_URL.short_url
  is ' 短地址 不带类型的';
comment on column V_OPEN_SHORT_URL.real_url_paramet
  is '真实地址需要的参数 标准的json格式';
comment on column V_OPEN_SHORT_URL.real_url_hash
  is '真实地址参数hash 方便查询';
comment on column V_OPEN_SHORT_URL.has_expiration_time
  is '是否可过期 0=长期 1=临时';
comment on column V_OPEN_SHORT_URL.expiration_time
  is '过期时间';
comment on column V_OPEN_SHORT_URL.has_times_limit
  is '是否有次数限制 0=没有 1=有';
comment on column V_OPEN_SHORT_URL.times_limit
  is '可使用次数';
comment on column V_OPEN_SHORT_URL.times_used
  is '已使用次数';
comment on column V_OPEN_SHORT_URL.create_date
  is '创建时间';
comment on column V_OPEN_SHORT_URL.create_psn_id
  is '创建人 0=系统 1=匿名用户';
-- Create/Recreate indexes 
create unique index IDX_V_OPEN_SHORT_URL_SHORTURL on V_OPEN_SHORT_URL (SHORT_URL)
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_OPEN_SHORT_URL
  add constraint PK_V_OPEN_SHORT_URL primary key (ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;

--原因（SCM-12788 短地址 表，实体类，读取服务开发） 2017-05-25 ZZX end




--原因（    SCM-12787 短地址 获取短地址open接口开发） 2017-05-26 ajb begin


insert into V_OPEN_TOKEN_SERVICE_CONST(id , token , service_type ) 
values(seq_v_open_token_service_id.nextval ,'00000000' ,'sht22url' )  ;
commit ;


--原因（    SCM-12787 短地址 获取短地址open接口开发） 2017-05-26 ajb end--原因（人员站外添加新url字段） 2017-05-27 ZZX begin

alter table PSN_PROFILE_URL add PSN_INDEX_URL varchar(100)


--原因（人员站外添加新url字段） 2017-05-27 ZZX end
--原因（人员站外添加新url字段） 2017-05-27 ZZX begin

alter table PSN_PROFILE_URL add PSN_INDEX_URL varchar(100);



--原因（人员站外添加新url字段） 2017-05-27 ZZX end
--原因（新建群组站外地址表） 2017-06-01 ZZX begin
-- Create table
create table V_GRP_INDEX_URL
(
  grp_id        NUMBER(18) not null,
  grp_index_url VARCHAR2(100 CHAR),
  psn_id        NUMBER(18),
  update_date   DATE
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table V_GRP_INDEX_URL
  is '群组站外地址表';
-- Add comments to the columns 
comment on column V_GRP_INDEX_URL.grp_id
  is ' 群组id';
comment on column V_GRP_INDEX_URL.grp_index_url
  is '群组短地址';
comment on column V_GRP_INDEX_URL.psn_id
  is '更新短地址的用户id';
comment on column V_GRP_INDEX_URL.update_date
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_GRP_INDEX_URL
  add constraint PK_V_GRP_INDEX_URL primary key (GRP_ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
  
  --原因（新建群组站外地址表） 2017-06-01 ZZX end--原因（SCM-12812，短地址数据初始化任务） 2017-6-2 LJ end

--原因（新增成果短地址表） 2017-06-02 zzx begin


-- Create table
create table V_PUB_INDEX_URL
(
  pub_id        NUMBER(18) not null,
  pub_index_url VARCHAR2(100 CHAR),
  psn_id        NUMBER(18),
  update_date   DATE
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table V_PUB_INDEX_URL
  is '成果短地址表';
-- Add comments to the columns 
comment on column V_PUB_INDEX_URL.pub_id
  is '成果id';
comment on column V_PUB_INDEX_URL.pub_index_url
  is '成果短地址';
comment on column V_PUB_INDEX_URL.psn_id
  is '最后修改人';
comment on column V_PUB_INDEX_URL.update_date
  is '修改时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_PUB_INDEX_URL
  add constraint PK_V_PUB_INDEX_URL primary key (PUB_ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;


--原因（新增成果短地址表） 2017-06-02 zzx end
  
  
  

  
--原因（初始化短地址任务数据） 2017-06-06 LJ begin

insert into V_QUARTZ_CRON_EXPRESSION values('39','ShortUrlInitTaskTrigger','*/59 * * * * ?',0,'短地址数据初始化任务');

insert into PSN_PROFILE_URL (psn_id)  select m.psn_id from person m where not exists  (select t.psn_id from PSN_PROFILE_URL t where m.psn_id=t.psn_id );

insert into V_PUB_INDEX_URL (pub_id)  select m.pub_id from v_pub_simple m where not exists  (select t.pub_id from V_PUB_INDEX_URL t where m.pub_id=t.pub_id );

insert into V_GRP_INDEX_URL (grp_Id)  select m.grp_Id from v_grp_baseinfo m where not exists  (select t.grp_Id from V_GRP_INDEX_URL t where m.grp_Id=t.grp_Id );

insert into V_QUARTZ_CRON_EXPRESSION values('40','CleanExpiredShortUrlTaskTrigger','0 0 0 1 * ?',0,'过期短地址清理任务');

--原因（初始化短地址任务数据） 2017-06-06 LJ end

--原因（新增群组成果短地址） 2017-06-08 ZZX begin


-- Create table
create table V_GRP_PUB_INDEX_URL
(
  id            NUMBER(18) not null,
  pub_id        NUMBER(18) not null,
  grp_id        NUMBER(18) not null,
  pub_index_url VARCHAR2(100 CHAR),
  psn_id        NUMBER(18),
  update_date   DATE
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table V_GRP_PUB_INDEX_URL
  is '群组成果短地址表';
-- Add comments to the columns 
comment on column V_GRP_PUB_INDEX_URL.id
  is '主键';
comment on column V_GRP_PUB_INDEX_URL.pub_id
  is '成果id';
comment on column V_GRP_PUB_INDEX_URL.grp_id
  is '群组id';
comment on column V_GRP_PUB_INDEX_URL.pub_index_url
  is '短地址';
comment on column V_GRP_PUB_INDEX_URL.psn_id
  is '创建人id';
comment on column V_GRP_PUB_INDEX_URL.update_date
  is '创建时间';
-- Create/Recreate indexes 
create unique index IND_V_GRP_PUB_INDEX_URL_G_P on V_GRP_PUB_INDEX_URL (PUB_ID, GRP_ID)
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_GRP_PUB_INDEX_URL
  add constraint PK_V_GRP_PUB_INDEX_URL primary key (ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;

create sequence SEQ_V_grp_PUB_INDEX_URL
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 10;

--原因（新增群组成果短地址） 2017-06-08 ZZX end


--原因（有CQ号带上CQ号） 2017-6-9 成果认领task  zjh begin
insert into v_quartz_cron_expression t values(41,'pubReconfirmTaskTrigger','*/30 * * * * ?',1,'成果认领task');
--原因（有CQ号带上CQ号）2017-6-9 成果认领task zjh end

--原因（SCM-12897，open系统加一个token跟ip的绑定关系） 2017-06-09 AJB  begin

-- Alter table 
alter table V_OPEN_THIRD_REG
  storage
  (
    next 1
  )
;
-- Add/modify columns 
alter table V_OPEN_THIRD_REG add PERMIT_IP varchar2(200);
-- Add comments to the columns 
comment on column V_OPEN_THIRD_REG.PERMIT_IP
  is '一个允许token只允许一个ip';
  
commit ;
--原因（SCM-12897，open系统加一个token跟ip的绑定关系） 2017-06-09 AJB  end



--原因（SCM-12895，扩展群组成果短地址） 2017-06-09 LJ begin


insert into V_GRP_PUB_INDEX_URL (ID,pub_id,grp_id)  select SEQ_V_GRP_PUB_INDEX_URL.NEXTVAL,m.pub_id,m.grp_id from v_grp_pubs m where not exists  (select t.pub_id,t.grp_id from V_GRP_PUB_INDEX_URL t where m.pub_id=t.pub_id and m.grp_id=t.grp_id ) and m.grp_id!=0;


--原因（SCM-12895，扩展群组成果短地址）2017-06-09 LJ  end





--原因（SCM-12897，open系统加一个token跟ip的绑定关系） 2017-06-09 AJB  end

update v_open_third_reg t  set t.permit_ip = '192.168.10;192.168.15'  ;


--原因（SCM-12897，open系统加一个token跟ip的绑定关系） 2017-06-09 AJB  end