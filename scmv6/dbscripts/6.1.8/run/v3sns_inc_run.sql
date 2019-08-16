-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（solr中文分词词典重新加载，现在要对检索下拉提示框中的数据进行重建索引）2019-6-22 SYL start
update suggest_str_init t set t.status=0;
update V_QUARTZ_CRON_EXPRESSION t set t.status=1 where t.cron_trigger_bean='suggestStrIndexTaskTrigger';
--原因（solr中文分词词典重新加载，现在要对检索下拉提示框中的数据进行重建索引）2019-6-22 SYL end

--原因（ROL-5742） 2019-6-21 ZYJ begin

-- 新建机构主页申诉模板数据
insert into V_MAIL_TEMPLATE(template_code, template_name, subject_zh, subject_en, status, create_date, update_date, msg, mail_type_id, limit_status, prior_level, template_language) 
values(10128, 'Ins_Complain_Notice_Template', '机构申诉', '机构申诉', 0, sysdate, sysdate, '客户申诉', 0, 0, 'B', 1);

--原因（ROL-5742） 2019-6-21 ZYJ end


--原因（SCM-25770 生产机，有接近30万的人员，person表有单位ID，但是没有工作经历和教育经历的记录，需要进行补充） 2019-06-25 zll begin
Insert Into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
Values(2000,'FillPsnWorkHistoryTaskTrigger','0/10 * * * * ?',0,'个人工作经历填充');
--原因（SCM-25770 生产机，有接近30万的人员，person表有单位ID，但是没有工作经历和教育经历的记录，需要进行补充） 2019-06-25 zll end

--原因（ROL-5742） 2019-06-25 ZYJ begin

-- 将测试邮箱添加到白名单
insert into V_MAIL_WHITELIST(id, email, STATUS) values(33, 'youwenwang@irissz.com', 0);
insert into V_MAIL_WHITELIST(id, email, STATUS) values(34, 'qianqiongzhu@irissz.com', 0);

--原因（ROL-5742） 2019-06-25 ZYJ end

--SCM-25980 uat--联系人推荐：特定帐号点添加联系人提示系统出错  2019-6-21 WSN begin

update person t set t.region_id = null where t.region_id = 0;

commit;

--SCM-25980 uat--联系人推荐：特定帐号点添加联系人提示系统出错  2019-6-21 WSN begin


--原因（ROL-6762 机构主页赞、关注） 2019-6-26 XR begin

--配置接口sieinssc的机构版和个人版权限

insert into V_OPEN_TOKEN_SERVICE_CONST(ID,TOKEN,SERVICE_TYPE,STATUS,CREATE_DATE,DESCR,ACCESS_DATE,ACCESS_NUM,ACCESS_MAX_NUM,INS_ID) 
values(seq_v_open_token_service_id.nextval,'00000000','sieinssc',0,to_date(sysdate),'对机构社交化操作接口，sie',to_date(sysdate),0,99999999,'');

insert into V_OPEN_TOKEN_SERVICE_CONST(ID,TOKEN,SERVICE_TYPE,STATUS,CREATE_DATE,DESCR,ACCESS_DATE,ACCESS_NUM,ACCESS_MAX_NUM,INS_ID) 
values(seq_v_open_token_service_id.nextval,'11111111','sieinssc',0,to_date(sysdate),'对机构社交化操作接口，sie',to_date(sysdate),0,99999999,'');
commit;

--原因（ROL-6762 机构主页赞、关注） 2019-6-26 XR end

--原因（ROL-7228） 2019-6-27 ZYJ begin

update V_MAIL_TEMPLATE t set t.template_name = 'Sie_Institution_Complain' where t.template_code = 10128;

--原因（ROL-7228） 2019-6-27 ZYJ end

--原因（ROL-6762 机构主页赞、关注） 2019-6-26 XR begin

--删除接口sieinssc配置的机构版和个人版权限
delete from V_OPEN_TOKEN_SERVICE_CONST t where t.service_type = 'sieinssc';
commit;

--原因（ROL-6762 机构主页赞、关注） 2019-6-26 XR end

--SCM-26361  群组管理功能   2019-07-12 ajb start
-- Add/modify columns
alter table V_GRP_KW_DISC add NSFC_CATEGORY_ID VARCHAR2(10 CHAR);
-- Add comments to the columns
comment on column V_GRP_KW_DISC.NSFC_CATEGORY_ID
is '学科代码';
commit ;

--SCM-26361  群组管理功能   2019-07-12 ajb  end

--原因（SCM-26458 兴趣群组成果初始化） 2019-07-12 zll begin
-- Create table
create table v_grp_pub_init
(
  id           number(18) not null,
  grp_id       number(18) not null,
  pub_id       number(18) not null,
  pub_year     number(4),
  citations    number(4),
  has_fulltext number(2),
  status       number(2),
  create_date  date,
  update_date  date
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
comment on table v_grp_pub_init
  is '兴趣群组成果初始化';
-- Add comments to the columns 
comment on column v_grp_pub_init.id
  is '主键';
comment on column v_grp_pub_init.grp_id
  is '群组id';
comment on column v_grp_pub_init.pub_id
  is '基准库成果';
comment on column v_grp_pub_init.pub_year
  is '成果年份';
comment on column v_grp_pub_init.citations
  is '成果引用次数';
comment on column v_grp_pub_init.has_fulltext
  is '是否有全文,0:无全文，1:有全文';
comment on column v_grp_pub_init.status
  is '是否已加入群组';
comment on column v_grp_pub_init.create_date
  is '创建时间';
comment on column v_grp_pub_init.update_date
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table v_grp_pub_init
  add constraint pk_v_grp_pub_init primary key (ID);
-- Create/Recreate indexes 
create unique index uk_v_grp_pub_init on v_grp_pub_init (grp_id, pub_id);

alter table V_GRP_PUB_INIT modify status default 0;

create sequence SEQ_V_GRP_PUB_INIT
minvalue 1
maxvalue 999999999999
start with 11
increment by 1
cache 10;


 Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
 Values(1957,'InstrestGrpPubInitTaskTrigger','0 */1 * * * ?',0,'兴趣群组成果初始化');
  Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
 Values(1958,'InstrestGrpAddPubTaskTrigger','0 */1 * * * ?',0,'兴趣群组加成果');
 
 
 --原因（SCM-26458 兴趣群组成果初始化） 2019-07-12 zll end
 
 --SCM-26361  群组管理功能    2019-07-15  ajb begin


insert into sys_authoritie(id,name,display_name) values(10012,'ROLE_GROUP_SCMANAGEMENT','权限：群组管理');
insert into sys_role_authoritie(role_id,authority_id) values(12,10012);
insert into sys_role(id,name,description) values(12,'群组管理角色','访问群组管理页面');

--SCM-26361  群组管理功能    2019-07-15  ajb  end

--原因（SCM-26514 兴趣群组推荐给个人） 2019-07-16 zll begin


Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
Values(1959,'InstrestGrpRcmdTaskTrigger','0 */1 * * * ?',0,'兴趣群组推荐');
 
 -- Add/modify columns 
alter table V_GRP_RCMD add rcmd_score NUMBER(5);
-- Add comments to the columns 
comment on column V_GRP_RCMD.rcmd_score
  is '推荐度';

--原因（SCM-26514 兴趣群组推荐给个人） 2019-07-16 zll end

--SCM-26580 群组访问记录表  2019-7-23 YHX begin

-- Create table
create table V_GRP_VIEW
(
  id           NUMBER(18) not null,
  grp_id       NUMBER(18) not null,
  view_psn_id  NUMBER(18) not null,
  ip           VARCHAR2(20),
  gmt_create   DATE,
  formate_date NUMBER(15),
  total_count  NUMBER(4)
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
comment on table V_GRP_VIEW
  is '群组访问记录表';
-- Add comments to the columns 
comment on column V_GRP_VIEW.id
  is '主键';
comment on column V_GRP_VIEW.grp_id
  is '群组id';
comment on column V_GRP_VIEW.view_psn_id
  is '查看人员id';
comment on column V_GRP_VIEW.ip
  is '访问者IP';
comment on column V_GRP_VIEW.gmt_create
  is '访问时间';
comment on column V_GRP_VIEW.formate_date
  is '日期格式化';
comment on column V_GRP_VIEW.total_count
  is '当天的浏览总数';
-- Create sequence 
create sequence V_SEQ_GRP_VIEW
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_GRP_VIEW
  add constraint V_GRP_VIEW_PK primary key (ID)
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
create index V_GRP_VIEW_IDX1 on V_GRP_VIEW (GRP_ID)
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

--SCM-26580 群组访问记录表  2019-7-23 YHX end

 
--SCM-26587 v_grp_rcmd群组推荐表数据重新计算  2019-07-23 zll begin

Update v_quartz_cron_expression  t Set t.cron_trigger_bean='GroupRcmdTaskTrigger',t.description='群组推荐' Where t.cron_trigger_bean='InstrestGrpRcmdTaskTrigger' ;
Insert Into application_setting(id,key,value,remark) Values(1005,'grp_rcmd_start',0,'群组推荐-开始grpId');

--SCM-26587 v_grp_rcmd群组推荐表数据重新计算  2019-07-23 zll end

--SCM-26583基金详情页面调整  2019-07-24 zll begin

-- Add/modify columns 
alter table V_FUND_STATISTICS add read_count NUMBER(5) default 0;
-- Add comments to the columns 
comment on column V_FUND_STATISTICS.read_count
  is '阅读数';

--SCM-26583基金详情页面调整  2019-07-24 zll end

--SCM-26590 群组-兴趣群组推荐给个人，修改了群组或者个人的 科技领域、关键词、群组成果合作者关系变更时， 按理rcmd_score分数要变化  2019-07-25 zll begin

Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
Values(1960,'GroupRcmdIncTaskTrigger','*/10 * * * * ?',0,'更新、新增群组推荐')

--SCM-26590 群组-兴趣群组推荐给个人，修改了群组或者个人的 科技领域、关键词、群组成果合作者关系变更时， 按理rcmd_score分数要变化  2019-07-25 zll end

-- 增加基准库期刊数据修复任务  2019-07-25 YJ begin
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) values(701,'PdwhPubJournalRepairTaskTrigger','*/10 * * * * ?',0,'基准库库修复期刊数据');
-- 增加基准库期刊数据修复任务  2019-07-25 YJ end