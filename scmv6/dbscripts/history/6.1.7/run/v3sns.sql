-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end
--SCM-24387 v_wechat_relation_his表的wechat_openid字段存在唯一约束导致无法插入数据 2019-4-3 YWL begin
drop index IDX_V_WECHAT_RELATION_HIS_OID;
--SCM-24387 v_wechat_relation_his表的wechat_openid字段存在唯一约束导致无法插入数据 2019-4-3 YWL end

--SCM-24484 APP 论文推荐条件编辑页面统一样式 2019-4-4 YWL begin
update CONST_PUB_TYPE set seq_no =2 where type_id = 3;
update CONST_PUB_TYPE set seq_no =3 where type_id = 5;
--SCM-24484 APP 论文推荐条件编辑页面统一样式 2019-4-4 YWL end


--原因（demo 新增科研号）  2019-04-09  aijianbin start
-- Add/modify columns
alter table demo_scm_req_pub_record add RESEARCHER_ID NUMBER(18);
-- Add comments to the columns
comment on column demo_scm_req_pub_record.RESEARCHER_ID
  is '科研号';
-- Create/Recreate indexes
create index IDX_DEMO_SCM_REQ_OPEN_ID on demo_scm_req_pub_record (RESEARCHER_ID);

--原因（demo 新增科研号）  2019-04-09  aijianbin  end

---原因（SCM-24550 成果，项目合作者计算） 2019-4-9 zll start


  -- Alter table 
alter table PUB_ASSIGN_LOG
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table PUB_ASSIGN_LOG add sns_pub_id NUMBER(18);
-- Add comments to the columns 
comment on column PUB_ASSIGN_LOG.sns_pub_id
  is '认领后对应个人库成果';
  
  
  -- Alter table 
alter table PUB_ASSIGN_LOG_DETAIL
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table PUB_ASSIGN_LOG_DETAIL add pub_member_id NUMBER(18);
alter table PUB_ASSIGN_LOG_DETAIL add pub_member_name VARCHAR2(50);
-- Add comments to the columns 
comment on column PUB_ASSIGN_LOG_DETAIL.pub_member_id
  is '基准库成果作者memberId';
comment on column PUB_ASSIGN_LOG_DETAIL.pub_member_name
  is '基准库成果作者';
  
  
 -- Create table
create table PSN_COPARTNER
(
  id            NUMBER(18),
  psn_id        NUMBER(18),
  co_psn_id     NUMBER(18),
  pdwh_pub_id   NUMBER(18),
  pdwh_pub_name VARCHAR2(50),
  co_type       NUMBER(2),
  grp_id        NUMBER(18)
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
comment on table PSN_COPARTNER
  is '人员合作者';
-- Add comments to the columns 
comment on column PSN_COPARTNER.id
  is '主键id';
comment on column PSN_COPARTNER.psn_id
  is '人员id';
comment on column PSN_COPARTNER.co_psn_id
  is '合作者人员id';
comment on column PSN_COPARTNER.pdwh_pub_id
  is '合作成果id';
comment on column PSN_COPARTNER.pdwh_pub_name
  is '合作成果作者';
comment on column PSN_COPARTNER.co_type
  is '合作类型，1：成果 2：项目';
comment on column PSN_COPARTNER.grp_id
  is '合作项目群组id';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PSN_COPARTNER
  add constraint PK_PSN_COPARTNER unique (ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
alter table PSN_COPARTNER
  add constraint UK_PSN_COPARTNER unique (PSN_ID, CO_PSN_ID, PDWH_PUB_ID, CO_TYPE)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
alter table PSN_COPARTNER
  add constraint UK_PSN_COPARTNER1 unique (PSN_ID, CO_PSN_ID, GRP_ID, CO_TYPE)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;

  
create sequence SEQ_PSN_COPARTNER
minvalue 1
maxvalue 99999999999
start with 11
increment by 1
cache 10;


Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
Values(1938,'GeneratePsnPubCopartnerTaskTrigger','0/10 * * * * ?',0,'成果合作者');
Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
Values(1939,'GeneratePsnPrjCopartnerTaskTrigger','0/10 * * * * ?',0,'项目合作者');

Insert Into application_setting(id,key,value,remark) Values(1003,'sns_psn_prj_copartner_start',0,'项目合作者-开始psnId');
Insert Into application_setting(id,key,value,remark) Values(1004,'sns_psn_pub_copartner_start',0,'成果合作者-开始psnId');

-- Add/modify columns 
alter table PSN_COPARTNER add pdwh_pub_member_id NUMBER(18);
-- Add comments to the columns 
comment on column PSN_COPARTNER.pdwh_pub_member_id
  is '合作成果作者memberId';
  
---原因（SCM-24550 成果，项目合作者计算） 2019-4-9 zll end
  
  
  -- Alter table 
alter table V_PUB_PDWH_SNS_RELATION
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table V_PUB_PDWH_SNS_RELATION add create_date date;
alter table V_PUB_PDWH_SNS_RELATION add update_date date;
-- Add comments to the columns 
comment on column V_PUB_PDWH_SNS_RELATION.create_date
  is '关联关系创建时间';
comment on column V_PUB_PDWH_SNS_RELATION.update_date
  is '关联关系更新时间';

--SCM-24387 v_wechat_relation_his表的wechat_openid字段存在唯一约束导致无法插入数据 2019-4-3 YWL begin
drop index IDX_V_WECHAT_RELATION_HIS_OID;
--SCM-24387 v_wechat_relation_his表的wechat_openid字段存在唯一约束导致无法插入数据 2019-4-3 YWL end

--SCM-24484 APP 论文推荐条件编辑页面统一样式 2019-4-4 YWL begin
update CONST_PUB_TYPE set seq_no =2 where type_id = 3;
update CONST_PUB_TYPE set seq_no =3 where type_id = 5;
--SCM-24484 APP 论文推荐条件编辑页面统一样式 2019-4-4 YWL end

--SCM-24559 站外主页》逻辑更改 新增表pub_index_third_level 2019-4-9 HHT begin
---将 pub_index_second_level 表重命名   为 pub_index_third_level
alter table pub_index_second_level rename to pub_index_third_level;
---备份 pub_index_third_level(pub_index_second_level) 表
create table pub_index_third_level_bak as select * from pub_index_third_level;

---添加索引
-- Alter table 
alter table PUB_INDEX_THIRD_LEVEL
  storage
  (
    next 8
  )
;
-- Create/Recreate indexes 
drop index INDEX_SL_FL_SG;
create index INDEX_SL_FL_SG on PUB_INDEX_THIRD_LEVEL (first_letter, second_group, third_group)
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
---删除表数据
delete from pub_index_first_level;
delete from pub_index_second_level;
delete from pub_index_third_level;
---复制 pub_index_first_level 表 为 pub_index_second_level 
create table pub_index_second_level as select * from pub_index_first_level where 1=2;
-- 修改 PUB_INDEX_SECOND_LEVEL 表字段
alter table PUB_INDEX_SECOND_LEVEL rename column fl_id to sl_id;
alter table PUB_INDEX_SECOND_LEVEL rename column first_label to second_label;
alter table PUB_INDEX_SECOND_LEVEL add third_group number not null;
--主键
alter table PUB_INDEX_SECOND_LEVEL
  add constraint PK_SL primary key (SL_ID);
--创建序列
create sequence SEQ_SL
minvalue 1
maxvalue 99999999999
start with 11
increment by 1
cache 10;

--- 新增 pub_index_third_level 字段 third_group
alter table pub_index_third_level add third_group number not null;

-- 添加索引
create index INDEX_SL_FG on PUB_INDEX_SECOND_LEVEL (FIRST_GROUP)
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
create index INDEX_SL_SG on PUB_INDEX_SECOND_LEVEL (SECOND_GROUP)
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
--SCM-24559 站外主页》逻辑更改 新增表pub_index_third_level 2019-4-9 HHT end


--成果新加publish_month,publish_day字段  2019-04-09  tsz start
-- Add/modify columns
alter table v_pub_sns add publish_month NUMBER(2);
alter table v_pub_sns add publish_day NUMBER(2);
-- Add comments to the columns
comment on column v_pub_sns.publish_month
  is '发表日期月';
comment on column v_pub_sns.publish_day
  is '发表日期日';
-- Create/Recreate indexes
create index IDX_V_PUB_SNS_M on v_pub_sns (publish_month);
create index IDX_V_PUB_SNS_D on v_pub_sns (publish_day);

--成果新加publish_month,publish_day字段  2019-04-09  tsz  end




--原因（SCM-24560 demo互联互通排序功能调整） 2019-04-10  aijiangbin start
-- Add/modify columns
alter table DEMO_CACHE_PUB_INFO add PUBLISH_MONTH NUMBER(2);
alter table DEMO_CACHE_PUB_INFO add PUBLISH_DAY NUMBER(2);
-- Add comments to the columns
comment on column DEMO_CACHE_PUB_INFO.PUBLISH_MONTH
  is '发表月份';
comment on column DEMO_CACHE_PUB_INFO.PUBLISH_DAY
  is '发表日';

--原因（SCM-24560 demo互联互通排序功能调整） 2019-04-10  aijiangbin  end
--原因（SCM-5520 论文全文：请确定是否需要设置对应的退订复选框） 2019-04-10  zll end  

--原因（SCM-24638 tempcode=10021，合作者的通知邮件模板，点击按钮，跳转到成果认领页面的链接需修改） 2019-04-11 zll start

Update  v_mail_template  t Set t.template_name='Pub_Confirm_Cp_Detail' Where t.template_code=10021;

--原因（SCM-24638 tempcode=10021，合作者的通知邮件模板，点击按钮，跳转到成果认领页面的链接需修改） 2019-04-11 zll end


--SCM-24086 版本升级弹框，建议：将描述文字显示完整  2019-4-16 wsn begin

alter table V_MOBILE_APP_VERSION  modify version_desc varchar2(100 char);
commit;

--SCM-24086 版本升级弹框，建议：将描述文字显示完整  2019-4-16 wsn end


--SCM-000 备份OriginalPdwhPubRelation表数据  2019-4-17 zll begin
Insert Into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
Values(1940,'RemOriPdwhPubRelationDataTaskTrigger','*/10 * * * * ?',0,'备份OriginalPdwhPubRelation表数据');

--SCM-000 备份OriginalPdwhPubRelation表数据  2019-4-17 zll end
-- SCM-24758 邮件发送时新增模板可用检查 2019-4-17 zll begin
comment on column V_MAIL_RECORD.status
  is '0=待分配 1=待发送  2=发送成功 3=黑名单 4=receiver 不存在 5不在白名单 8发送出错 9邮件调度出错,10邮件正在发送
	11构造邮件发送信息出错,13模板不可用';
-- SCM-24758 邮件发送时新增模板可用检查 2019-4-17 zll end
	
--原因：SCM-24712 管理系统中，基准库成果删除功能 2019-04-17 YHX begin
alter table PUB_ASSIGN_LOG add(STATUS NUMBER(2) default 0);
comment on column PUB_ASSIGN_LOG.status is '基准库成果状态：0.正常，1.已删除';
update PUB_ASSIGN_LOG t set t.status = 0;
comment on column v_grp_pub_rcmd.status is '状态(0 未确认)(1已经确认)(9已经忽略)(8成果已被删除)';

insert into sys_resource(id,resource_type,value,order_num,parent_id,name,status,lavels,remark) values(15,'url','/scmmanagement/pubInfo/main',3,0,'基准库成果管理',1,1,'基准库成果管理');
insert into sys_authoritie(id,name,display_name) values(10008,'ROLE_PAPER_SCMMANAGEMENT','权限：基准库成果管理');
insert into sys_role_authoritie(role_id,authority_id) values(7,10008);
insert into sys_role_authoritie(role_id,authority_id) values(2,10008);
insert into sys_resource_authoritie(authority_id,resource_id) values(10008,15);
insert into sys_role(id,name,parent_id,description,state,type) values('7','基准库成果管理','','访问基准库成果管理页面',1,1);
--原因：SCM-24712 管理系统中，基准库成果删除功能 2019-04-17 YHX end

--原因（SCM-24638 tempcode=10021，合作者的通知邮件模板，点击按钮，跳转到成果认领页面的链接需修改） 2019-04-11 zll start

Update  v_mail_template  t Set t.template_name='Pub_Confirm_Cp_Detail' Where t.template_code=10021;

--原因（SCM-24638 tempcode=10021，合作者的通知邮件模板，点击按钮，跳转到成果认领页面的链接需修改） 2019-04-11 zll end


--SCM-24086 版本升级弹框，建议：将描述文字显示完整  2019-4-16 wsn begin

alter table V_MOBILE_APP_VERSION  modify version_desc varchar2(100 char);
commit;

--SCM-24086 版本升级弹框，建议：将描述文字显示完整  2019-4-16 wsn end

-- SCM-24758 邮件发送时新增模板可用检查 2019-4-17 zll begin
comment on column scholar2.V_MAIL_RECORD.status
  is '0=待分配 1=待发送  2=发送成功 3=黑名单 4=receiver 不存在 5不在白名单 8发送出错 9邮件调度出错,10邮件正在发送
	11构造邮件发送信息出错,13模板不可用';
-- SCM-24758 邮件发送时新增模板可用检查 2019-4-17 zll end
	
--原因：SCM-24712 管理系统中，基准库成果删除功能 2019-04-17 YHX begin
alter table PUB_ASSIGN_LOG add(STATUS NUMBER(2) default 0);
comment on column PUB_ASSIGN_LOG.status is '基准库成果状态：0.正常，1.已删除';
update PUB_ASSIGN_LOG t set t.status = 0;
comment on column v_grp_pub_rcmd.status is '状态(0 未确认)(1已经确认)(9已经忽略)(8成果已被删除)';

insert into sys_resource(id,resource_type,value,order_num,parent_id,name,status,lavels,remark) values(15,'url','/scmmanagement/pubInfo/main',3,0,'基准库成果管理',1,1,'基准库成果管理');
insert into sys_authoritie(id,name,display_name) values(10008,'ROLE_PAPER_SCMMANAGEMENT','权限：基准库成果管理');
insert into sys_role_authoritie(role_id,authority_id) values(7,10008);
insert into sys_role_authoritie(role_id,authority_id) values(2,10008);
insert into sys_resource_authoritie(authority_id,resource_id) values(10008,15);
insert into sys_role(id,name,parent_id,description,state,type) values('7','基准库成果管理','','访问基准库成果管理页面',1,1);
--原因：SCM-24712 管理系统中，基准库成果删除功能 2019-04-17 YHX end

--原因（ROL-6934 修改历史数据XML转Json后台任务名称） 2019-04-18 LJM begin
update v_quartz_cron_expression t set t.cron_trigger_bean = 'sieFixPubXml2JsonTaskTrigger' where t.id = 10130;
--原因（ROL-6934 修改历史数据XML转Json后台任务名称） 2019-04-18 LJM end

--原因：SCM-24712 管理系统中，基准库成果删除功能 2019-04-17 YHX begin
-- Create table
create table V_PUB_OPERATE_LOG
(
  id          NUMBER(18) not null,
  op_psn_id   NUMBER(18) not null,
  pub_id      NUMBER(18) not null,
  op_type     NUMBER(1),
  desc_msg    VARCHAR2(1000 CHAR),
  create_date DATE
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table V_PUB_OPERATE_LOG
  is '管理系统成果操作日志表';
-- Add comments to the columns 
comment on column V_PUB_OPERATE_LOG.id
  is '主键';
comment on column V_PUB_OPERATE_LOG.op_psn_id
  is '操作人';
comment on column V_PUB_OPERATE_LOG.pub_id
  is '成果Id';
comment on column V_PUB_OPERATE_LOG.op_type
  is '操作类型：0删除';
comment on column V_PUB_OPERATE_LOG.desc_msg
  is '描述';
comment on column V_PUB_OPERATE_LOG.create_date
  is '操作时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_PUB_OPERATE_LOG
  add constraint PK_V_PUB_OPERATE_LOG primary key (ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
create sequence V_SEQ_PUB_OPERATE_LOG
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20
--原因：SCM-24712 管理系统中，基准库成果删除功能 2019-04-18 YHX end--原因（SCM-24759 person表关于firstname、lastname、ename注释错误，以及当姓名如zhi qiang fan时firstname和lastname取值相互反了） 2019-4-18 YHX begin
comment on column person.first_name is 'First name of user（名）';
comment on column person.last_name is 'Last name of user（姓）';
comment on column person.ename is 'first name +"  "+ last name';
--原因（SCM-24759 person表关于firstname、lastname、ename注释错误，以及当姓名如zhi qiang fan时firstname和lastname取值相互反了） 2019-4-18 YHX end
--原因 SCMAPP-1436 v_pub_share.platform字段中新加一个状态值代表QQ空间和QQ好友   2018-4-19 ltl begin
comment on column V_PUB_SHARE.platform
  is '分享平台: 1:动态,2:联系人,3:群组,4:微信,5:新浪微博,6:Facebook,7:Linkedin,8:qq空间,9:qq好友';
--原因 SCMAPP-1436 v_pub_share.platform字段中新加一个状态值代表QQ空间和QQ好友   2018-4-19 ltl end  
  
--原因（SCM-24844 表实体类的定义 与创建） 2019-4-23 YHX begin
-- Create table
create table V_THIRD_SOURCES
(
  source_id   NUMBER(18) not null,
  from_sys    VARCHAR2(50) not null,
  from_url    VARCHAR2(500),
  status      NUMBER(1) not null,
  create_date DATE not null
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table V_THIRD_SOURCES
  is '信息来源记录表';
-- Add comments to the columns 
comment on column V_THIRD_SOURCES.source_id
  is '主键';
comment on column V_THIRD_SOURCES.from_sys
  is '来源系统';
comment on column V_THIRD_SOURCES.from_url
  is '来源系统接口地址';
comment on column V_THIRD_SOURCES.status
  is '状态：0可用，1不可用';
comment on column V_THIRD_SOURCES.create_date
  is '创建时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_THIRD_SOURCES
  add constraint PK_V_THIRD_SOURCES primary key (SOURCE_ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
  
 -- Create table
create table V_THIRD_SOURCES_TYPE
(
  id            NUMBER(18) not null,
  type          NUMBER(2) not null,
  source_id     NUMBER(18) not null,
  status        NUMBER(1) not null,
  create_date   DATE not null,
  last_get_date DATE
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table V_THIRD_SOURCES_TYPE
  is '业务接口业务类型开放表';
-- Add comments to the columns 
comment on column V_THIRD_SOURCES_TYPE.id
  is '主键';
comment on column V_THIRD_SOURCES_TYPE.type
  is '数据类型 （1：基金机会,2：单位项目,3：通知公告）';
comment on column V_THIRD_SOURCES_TYPE.source_id
  is '来源id';
comment on column V_THIRD_SOURCES_TYPE.status
  is '状态：0可用，1不可用';
comment on column V_THIRD_SOURCES_TYPE.create_date
  is '创建时间';
comment on column V_THIRD_SOURCES_TYPE.last_get_date
  is '最后获取信息时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_THIRD_SOURCES_TYPE
  add constraint PK_V_THIRD_SOURCES_TYPE primary key (ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
-- Create table
create table V_THIRD_SOURCES_GET_LOG
(
  id             NUMBER(18) not null,
  type           NUMBER(2) not null,
  source_id      NUMBER(18) not null,
  request_params VARCHAR2(1000),
  result_status  VARCHAR2(50),
  result         CLOB,
  create_date    DATE,
   status         NUMBER(2)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table V_THIRD_SOURCES_GET_LOG
  is '接口调用记录表';
-- Add comments to the columns 
comment on column V_THIRD_SOURCES_GET_LOG.id
  is '主键';
comment on column V_THIRD_SOURCES_GET_LOG.type
  is '数据类型 （1：基金机会,2：单位项目,3：通知公告）';
comment on column V_THIRD_SOURCES_GET_LOG.source_id
  is '来源id';
comment on column V_THIRD_SOURCES_GET_LOG.request_params
  is '接口请求参数';
comment on column V_THIRD_SOURCES_GET_LOG.result_status
  is '接口返回状态:success/error';
comment on column V_THIRD_SOURCES_GET_LOG.result
  is '接口返回数据';
comment on column V_THIRD_SOURCES_GET_LOG.create_date
  is '创建时间';
  comment on column V_THIRD_SOURCES_GET_LOG.status
  is '状态：0 正常调用,1 调用失败,2.响应解析失败,3.响应数据格式不对,4.返回结果 result解析失败';
create sequence V_SEQ_PUB_OPERATE_LOG
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_THIRD_SOURCES_GET_LOG
  add constraint PK_V_THIRD_SOURCES_GET_LOG primary key (ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;

  
Insert Into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
Values(1941,'GetThirdPartyDataTaskTrigger','*/50 * * * * ?',0,'获取第三方业务系统信息接口调用任务');

-- Create table
create table V_THIRD_SOURCES_ERROR_LOG
(
  id          NUMBER(18) not null,
  source_id   NUMBER(18) not null,
  type        NUMBER(2) not null,
  error_data  CLOB,
  error_msg   VARCHAR2(1000),
  create_date DATE not null
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
comment on table V_THIRD_SOURCES_ERROR_LOG
  is '获取数据错误日志表';
-- Add comments to the columns 
comment on column V_THIRD_SOURCES_ERROR_LOG.id
  is '主键';
comment on column V_THIRD_SOURCES_ERROR_LOG.source_id
  is '来源id';
comment on column V_THIRD_SOURCES_ERROR_LOG.type
  is '数据类型 （1：基金机会,2：单位项目,3：通知公告）';
comment on column V_THIRD_SOURCES_ERROR_LOG.error_data
  is '错误数据';
comment on column V_THIRD_SOURCES_ERROR_LOG.error_msg
  is '错误描述';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_THIRD_SOURCES_ERROR_LOG
  add constraint PK_V_THIRD_SOURCES_ERROR_LOG primary key (ID)
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
  
--原因（SCM-24844 表实体类的定义 与创建） 2019-4-23 YHX endalter table V_THIRD_SOURCES_GET_LOG add STATUS NUMBER(2);
-- Add comments to the columns
comment on column V_THIRD_SOURCES_GET_LOG.STATUS
  is '状态：0 正常调用,1 调用失败,2.响应解析失败,3.响应数据格式不对,4.返回结果 result解析失败';

alter table v_third_sources add token NUMBER(8);
-- Add comments to the columns
comment on column v_third_sources.token
  is '校验码';
--原因（SCM-24844 表实体类的定义 与创建） 2019-4-24 YHX end





--原因（SCM-24844 表实体类的定义 与创建） 2019-4-24 ajb start
-- Create sequence
create sequence SEQ_V_THIRD_SOURCES_FUND
minvalue 1
maxvalue 9999999999999999
start with 31
increment by 1
cache 10;

--原因（SCM-24844 表实体类的定义 与创建） 2019-4-24 ajb  end




--原因（SCM-24844 表实体类的定义 与创建） 2019-4-24 ajb start
-- Create table
create table V_THIRD_SOURCES_FUND
(
  ID                             NUMBER(18) not null,
  FUND_TITLE_CN                  NVARCHAR2(500),
  FUND_TITLE_EN                  NVARCHAR2(500),
  FUND_TITLE_ABBR                NVARCHAR2(100),
  FUND_NUMBER                    NVARCHAR2(50),
  FUND_DESC                      NVARCHAR2(2000) not null,
  DISCIPLINE_CLASSIFICATION_TYPE NVARCHAR2(100),
  DISCIPLINE_LIMIT               NVARCHAR2(100),
  FUND_KEYWORDS                  NVARCHAR2(500),
  FUND_YEAR                      NUMBER(4),
  APPLY_DATE_START               DATE,
  APPLY_DATE_END                 DATE,
  FUNDING_AGENCY                 NVARCHAR2(100),
  ESTIMATED_GRANT_AMOUNT         NUMBER(10,2),
  DECLARE_GUIDE_URL              NVARCHAR2(200),
  DECLARE_URL                    NVARCHAR2(200),
  ACCESSORY_URL                  NVARCHAR2(200),
  REGION_LIMIT                   NUMBER(10),
  TITLE_LIMIT                    NVARCHAR2(50),
  INS_LIMIT                      NVARCHAR2(50),
  DEGREE_LIMIT                   NUMBER(4),
  AGE_LIMIT                      NVARCHAR2(50),
  STATUS                         NUMBER(1),
  UPDATE_TIME                    DATE,
  AUDIT_STATUS                   NUMBER(1) default 0
);

-- Add comments to the table
comment on table V_THIRD_SOURCES_FUND
  is '项目信息表';
-- Add comments to the columns
comment on column V_THIRD_SOURCES_FUND.ID
  is '主键';
comment on column V_THIRD_SOURCES_FUND.FUND_TITLE_CN
  is '基金机会名称 - 中文';
comment on column V_THIRD_SOURCES_FUND.FUND_TITLE_EN
  is '基金机会名称 - 英文';
comment on column V_THIRD_SOURCES_FUND.FUND_TITLE_ABBR
  is '基金机会简称';
comment on column V_THIRD_SOURCES_FUND.FUND_NUMBER
  is '基金机会编号';
comment on column V_THIRD_SOURCES_FUND.FUND_DESC
  is '基金机会描述';
comment on column V_THIRD_SOURCES_FUND.DISCIPLINE_CLASSIFICATION_TYPE
  is '分类标准';
comment on column V_THIRD_SOURCES_FUND.DISCIPLINE_LIMIT
  is '适用分类';
comment on column V_THIRD_SOURCES_FUND.FUND_KEYWORDS
  is '关键词';
comment on column V_THIRD_SOURCES_FUND.FUND_YEAR
  is '基金年度';
comment on column V_THIRD_SOURCES_FUND.APPLY_DATE_START
  is '申请日期开始';
comment on column V_THIRD_SOURCES_FUND.APPLY_DATE_END
  is '申请日期结束';
comment on column V_THIRD_SOURCES_FUND.FUNDING_AGENCY
  is '资助机构名称';
comment on column V_THIRD_SOURCES_FUND.ESTIMATED_GRANT_AMOUNT
  is '预计资助金额（单位万元）';
comment on column V_THIRD_SOURCES_FUND.DECLARE_GUIDE_URL
  is '申报指南网址';
comment on column V_THIRD_SOURCES_FUND.DECLARE_URL
  is '申报网址';
comment on column V_THIRD_SOURCES_FUND.ACCESSORY_URL
  is '附件地址';
comment on column V_THIRD_SOURCES_FUND.REGION_LIMIT
  is '适合地区';
comment on column V_THIRD_SOURCES_FUND.TITLE_LIMIT
  is '申请职称要求';
comment on column V_THIRD_SOURCES_FUND.INS_LIMIT
  is '单位要求';
comment on column V_THIRD_SOURCES_FUND.DEGREE_LIMIT
  is '学位要求';
comment on column V_THIRD_SOURCES_FUND.AGE_LIMIT
  is '年龄要求';
comment on column V_THIRD_SOURCES_FUND.STATUS
  is '基金机会状态';
comment on column V_THIRD_SOURCES_FUND.UPDATE_TIME
  is '记录更新时间';
comment on column V_THIRD_SOURCES_FUND.AUDIT_STATUS
  is '审核状态 默认0 未审核 ；';
-- Create/Recreate primary, unique and foreign key constraints
alter table V_THIRD_SOURCES_FUND
  add constraint PK_V_THIRD_SOURCES_FUND primary key (ID)
  using index ;

-- Create/Recreate indexes
create unique index SEQ_V_THIRD_SOURCES_FUND_1 on V_THIRD_SOURCES_FUND (FUND_NUMBER, FUNDING_AGENCY);
commit ;
--原因（SCM-24844 表实体类的定义 与创建） 2019-4-24 ajb  end

--原因（SCM-24948 tempcode=10028,基金推荐的邮件里“资助机会”改为“基金机会”） 2019-04-25 zll begin
Update v_mail_template t Set t.subject_zh='{0}，你有{1}个可能感兴趣的基金机会',t.subject_en='{0}，你有{1}个可能感兴趣的基金机会'
where t.template_code=10028;
--原因（SCM-24948 tempcode=10028,基金推荐的邮件里“资助机会”改为“基金机会”） 2019-04-25 zll end

--原因（scm-000 成果发表日期拆分） 2019-04-26 zll begin
Insert Into  v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
Values(1942,'PubRebuildPublishDateTaskTrigger','0/10 * * * * ?',0,'拆分个人库成果发表日期');
Insert Into  v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
Values(1943,'PdwhRebuildPublishDateTaskTrigger','0/10 * * * * ?',0,'拆分基准库成果发表日期');

--原因（scm-000 成果发表日期拆分） 2019-04-26 zll end 

--原因（scm-000 动态赞有重复的记录） 2019-04-27 ltl begin
delete from DYNAMIC_AWARD_PSN a where rowid !=(
  select  max(rowid) from DYNAMIC_AWARD_PSN b where a.award_id=b.award_id and a.awarder_psnid=b.awarder_psnid
);
commit;
create unique index DYNAMIC_AWARD_PSN_IDX on DYNAMIC_AWARD_PSN (award_id, awarder_psnid); 
--原因（scm-000 动态赞有重复的记录） 2019-04-27 ltl end 
--原因（ROL-6651 开发票自动发邮件给财务） 2019-4-27 XR begin
 
--在表V_MAIL_TEMPLATE（邮件模板）中添加一条开发票邮件通知财务模板数据
insert into V_MAIL_TEMPLATE (TEMPLATE_CODE, TEMPLATE_NAME, SUBJECT_ZH, SUBJECT_EN, STATUS, CREATE_DATE, UPDATE_DATE, MSG, MAIL_TYPE_ID, LIMIT_STATUS, PRIOR_LEVEL, TEMPLATE_LANGUAGE)
values (10120, 'Issue_Invoice_To_Inform_Finance', '科研之友-科研验证服务付费成功，通知财务开发票', '科研之友-科研验证服务付费成功，通知财务开发票', 0, to_date('24-04-2019 20:35:55', 'dd-mm-yyyy hh24:mi:ss'), to_date('24-04-2019 20:35:55', 'dd-mm-yyyy hh24:mi:ss'), '支付成功，通知财务开发票', 0, 0, 'D', 0);

--原因（ROL-6651 开发票自动发邮件给财务）  2019-4-27 XR end

--原因（ROL-6651 开发票自动发邮件给财务） 2019-4-29 XR begin

--更改科研验证开电子发票邮件模板
update V_MAIL_TEMPLATE t set  t.TEMPLATE_NAME = 'Sie_Invoice_To_Finance', t.SUBJECT_ZH = '科研验证开电子发票' ,t.SUBJECT_EN = '科研验证开电子发票',t.PRIOR_LEVEL = 'B' where t.template_code=10120;

--原因（ROL-6651 开发票自动发邮件给财务）  2019-4-29 XR end

--原因（ROL-6936 调整后台任务调度时间） 2019-04-29 LJM begin
-- sieStPsnTaskTrigger
update v_quartz_cron_expression t set t.cron_expression = '0 0 22 1,11,21 * ?' where t.cron_trigger_bean='sieStPsnTaskTrigger';
update v_quartz_cron_expression t set t.description = 'SIE,人员统计（0 0 22 1,11,21 * ?）' where t.cron_trigger_bean='sieStPsnTaskTrigger';
-- sieStUnitTaskTrigger
update v_quartz_cron_expression t set t.cron_expression = '0 0 23 1,11,21 * ?' where t.cron_trigger_bean='sieStUnitTaskTrigger';
update v_quartz_cron_expression t set t.description = 'SIE,部门统计（0 0 23 1,11,21 * ?）' where t.cron_trigger_bean='sieStUnitTaskTrigger';
-- sieStInsTaskTrigger
update v_quartz_cron_expression t set t.cron_expression = '0 0 0 1,11,21 * ?' where t.cron_trigger_bean='sieStInsTaskTrigger';
update v_quartz_cron_expression t set t.description = 'SIE,单位统计（0 0 0 1,11,21 * ?）' where t.cron_trigger_bean='sieStInsTaskTrigger';
-- sieImportThirdUnitsTaskTrigger
update v_quartz_cron_expression t set t.cron_expression = '0 0 21 1,16 * ?' where t.cron_trigger_bean='sieImportThirdUnitsTaskTrigger';
update v_quartz_cron_expression t set t.description = '导入第三方部门信息（0 0 21 1,16 * ?）' where t.cron_trigger_bean='sieImportThirdUnitsTaskTrigger';
-- sieImportThirdPsnsTaskTrigger
update v_quartz_cron_expression t set t.cron_expression = '0 0 22 1,16 * ?' where t.cron_trigger_bean='sieImportThirdPsnsTaskTrigger';
update v_quartz_cron_expression t set t.description = '导入第三方人员信息（0 0 22 1,16 * ?）' where t.cron_trigger_bean='sieImportThirdPsnsTaskTrigger';
-- sieImpactBaseTaskTrigger
update v_quartz_cron_expression t set t.cron_expression = '0 0 1 1,16 * ?' where t.cron_trigger_bean='sieImpactBaseTaskTrigger';
update v_quartz_cron_expression t set t.description = '单位影响力，社交化数据基表（0 0 1 1,16 * ?）' where t.cron_trigger_bean='sieImpactBaseTaskTrigger';
-- sieImpactExtendTaskTrigger
update v_quartz_cron_expression t set t.cron_expression = '0 0 2 1,16 * ?' where t.cron_trigger_bean='sieImpactExtendTaskTrigger';
update v_quartz_cron_expression t set t.description = '单位影响力，社交化数据拓展表（0 0 2 1,16 * ?）' where t.cron_trigger_bean='sieImpactExtendTaskTrigger';
-- sieSplitKpiValidateDetailTrigger
update v_quartz_cron_expression t set t.cron_expression = '0 */1 * * * ?' where t.cron_trigger_bean='sieSplitKpiValidateDetailTrigger';
update v_quartz_cron_expression t set t.description = '轮询task_kpi_validate_main, task_kpi_validate_detail表并根据业务类型拆分数据' where t.cron_trigger_bean='sieSplitKpiValidateDetailTrigger';
-- sieGenPdwhInsAddrConstTaskTrigger
update v_quartz_cron_expression t set t.cron_expression = '0 */1 * * * ?' where t.cron_trigger_bean='sieGenPdwhInsAddrConstTaskTrigger';
update v_quartz_cron_expression t set t.description = '处理单位别名表（0 */1 * * * ?）' where t.cron_trigger_bean='sieGenPdwhInsAddrConstTaskTrigger';
-- sieSplitPubOrgsTaskTrigger
update v_quartz_cron_expression t set t.cron_expression = '*/10 * * * * ?' where t.cron_trigger_bean='sieSplitPubOrgsTaskTrigger';
update v_quartz_cron_expression t set t.description = '轮询task_split_pub_orgs，从PUB_JSON 字段拆分数据到 orgs、email_author' where t.cron_trigger_bean='sieSplitPubOrgsTaskTrigger';
--原因（ROL-6936 调整后台任务调度时间） 2019-04-29 LJM end

--原因（ROL-6936 调整后台任务调度时间） 2019-04-30 LJM begin
-- sieStInsTaskTrigger
update v_quartz_cron_expression t set t.cron_expression = '0 0 0 2,12,22 * ?' where t.cron_trigger_bean='sieStInsTaskTrigger';
update v_quartz_cron_expression t set t.description = 'SIE,单位统计（0 0 0 2,12,22 * ?）' where t.cron_trigger_bean='sieStInsTaskTrigger';
-- sieImpactBaseTaskTrigger
update v_quartz_cron_expression t set t.cron_expression = '0 0 1 13 * ?' where t.cron_trigger_bean='sieImpactBaseTaskTrigger';
update v_quartz_cron_expression t set t.description = '单位影响力，社交化数据基表（0 0 1 13 * ?）' where t.cron_trigger_bean='sieImpactBaseTaskTrigger';
-- sieImpactExtendTaskTrigger
update v_quartz_cron_expression t set t.cron_expression = '0 0 2 16 * ?' where t.cron_trigger_bean='sieImpactExtendTaskTrigger';
update v_quartz_cron_expression t set t.description = '单位影响力，社交化数据拓展表（0 0 2 16 * ?）' where t.cron_trigger_bean='sieImpactExtendTaskTrigger';
--原因（ROL-6936 调整后台任务调度时间） 2019-04-30 LJM end

---scm-000  SCM-25121 获取业务系统基金信息任务  ajb start
-- Alter table
-- Drop columns
alter table V_THIRD_SOURCES_FUND drop column ESTIMATED_GRANT_AMOUNT;
alter table V_THIRD_SOURCES_FUND drop column REGION_LIMIT;
alter table V_THIRD_SOURCES_FUND drop column TITLE_LIMIT;
alter table V_THIRD_SOURCES_FUND drop column INS_LIMIT;
alter table V_THIRD_SOURCES_FUND drop column DEGREE_LIMIT;
alter table V_THIRD_SOURCES_FUND drop column AGE_LIMIT;
alter table V_THIRD_SOURCES_FUND drop column STATUS;
alter table V_THIRD_SOURCES_FUND add CREATE_TIME date;
-- Add comments to the columns
comment on column V_THIRD_SOURCES_FUND.CREATE_TIME
  is '创建时间';

  -- Add/modify columns
alter table V_THIRD_SOURCES_FUND add fund_type NVARCHAR2(20);
-- Add comments to the columns
comment on column V_THIRD_SOURCES_FUND.fund_type
  is '五大类基金类型';

-- Add/modify columns
alter table V_THIRD_SOURCES_FUND rename column ACCESSORY_URL to accessorys;

-- Add/modify columns
alter table V_THIRD_SOURCES_FUND modify ACCESSORYS NVARCHAR2(1000);
---scm-000  SCM-25121 获取业务系统基金信息任务  ajb  end

---SCM-000 邮件管理每天发送统计列表增加字段显示 YHX begin
-- Add/modify columns 
alter table V_MAIL_EVERYDAY_STATISTIC add TEMPLATE_TIME_LIMIT NUMBER(8) default 0;
-- Add comments to the columns 
comment on column V_MAIL_EVERYDAY_STATISTIC.TEMPLATE_TIME_LIMIT
  is '模版发送频率限制数';
-- Add/modify columns 
alter table V_MAIL_EVERYDAY_STATISTIC add FIRST_EMAIL_SAME NUMBER(8) default 0;
-- Add comments to the columns 
comment on column V_MAIL_EVERYDAY_STATISTIC.FIRST_EMAIL_SAME
  is '收件人与发件人首要邮箱一致数';
---SCM-000 邮件管理每天发送统计列表增加字段显示 YHX end
 
 ------ SCM-25183 基金推荐邮件发送策略调整  Zll  begin
   -- Add/modify columns 
alter table PSN_FUND_RECOMMEND add is_send_mail number;
-- Add comments to the columns 
comment on column PSN_FUND_RECOMMEND.is_send_mail
  is '是否已发送邮件';
  
  -- Create table
create table user_union_login_log
(
  psn_id         number(18) not null,
  is_login       number,
  is_union       number,
  psn_fund_score number
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
comment on table user_union_login_log
  is '人员登录及业务系统关联记录表';
-- Add comments to the columns 
comment on column user_union_login_log.psn_id
  is '主键';
comment on column user_union_login_log.is_login
  is '是否登录过';
comment on column user_union_login_log.is_union
  is '是否关联过其他业务系统';
comment on column user_union_login_log.psn_fund_score
  is '基金推荐加分分数';
-- Create/Recreate primary, unique and foreign key constraints 
alter table user_union_login_log
  add constraint pk_user_union_login_log primary key (PSN_ID);
-- Create/Recreate indexes 
create unique index uk_user_union_login_log on user_union_login_log (psn_id, psn_fund_score);

alter table USER_UNION_LOGIN_LOG modify is_login default 0;
alter table USER_UNION_LOGIN_LOG modify is_union default 0;

Insert Into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
Values(1944,'SyncUserUnionLoginLogTaskTrigger','*/10 * * * * ?',0,'同步系统人员的登录及关联其他系统情况');

Insert Into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
Values(1945,'PsnFundRecommendTaskTrigger1','*/10 * * * * ?',0,'人员基金推荐');

Insert Into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
Values(1946,'PsnFundRecommendTaskTrigger2','*/10 * * * * ?',0,'人员基金推荐');
------ SCM-25183 基金推荐邮件发送策略调整  Zll  end
  
--原因（scm-000 修改token数据类型） 2019-05-13 ajb  start
alter table V_THIRD_SOURCES modify TOKEN VARCHAR2(8);
commit ;
--原因（scm-000 修改token数据类型） 2019-05-13 ajb  end

--原因（scm-000  创建序列）  ajb  start

-- Create sequence
create sequence V_SEQ_THIRD_SOURCES_GET_LOG
minvalue 1
maxvalue 9999999999999999999999999999
start with 42161
increment by 1
cache 20;


create sequence V_SEQ_THIRD_SOURCES_ERROR_LOG
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;


--原因（scm-000  创建序列）  ajb   end
--原因（SCM-000 v_third_sources增加一个字段 资助机构id字段） 2019-5-14 YHX begin

  -- Add/modify columns
alter table v_third_sources add agency_id number(18);
-- Add comments to the columns
comment on column v_third_sources.agency_id
  is '资助机构Id';

--原因（SCM-000 v_third_sources增加一个字段 资助机构id字段） 2019-5-14 YHX end

--SCM-25270 初始化已有人员，和刚注册并且有单位人员的基金推荐条件 ltl 2019-5-17 begin
insert into V_QUARTZ_CRON_EXPRESSION values(2003,'InitPsnRecommendFundTaskTrigger','*/5 * * * * ?','1','初始化人员基金推荐条件');
insert into APP_QUARTZ_SETTING values(250,'InitPsnRecommendFundTask_removePatCache','0');
--SCM-25270 初始化已有人员，和刚注册并且有单位人员的基金推荐条件 ltl 2019-5-17 end


--原因（ROL-7042单位logo》是否将之前通过单位注册接口（sieregi1）、后台任务（sieAddInsByBacthTaskTrigger）生成的历史数据没有单位logo，是否修复一下？） 2019-5-20 XR begin
insert into  v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) 
values(20003,'sieGenerateInsLogoTaskTrigger','*/10 * * * * ?',0,'批量生成单位默认LOGO');
--原因（ROL-7042单位logo》是否将之前通过单位注册接口（sieregi1）、后台任务（sieAddInsByBacthTaskTrigger）生成的历史数据没有单位logo，是否修复一下？） 2019-5-20 XR end

--sns库 institution 视图修改。2019-6-3 LTL begin
create or replace view institution as
select t."INS_ID",t."ZH_NAME",t."EN_NAME",t."ABBR",t."CONTACT_PERSON",t."TEL",
t."SERVER_EMAIL",t."URL",t."STATUS",t."NATURE",t."CHECK_EMAILS",
t."ZH_ADDRESS",t."EN_ADDRESS",t."POST_CODE",t."FOX",t."UNIFORM_ID1",t."UNIFORM_ID2",
t."SERVER_TEL",t."DATA_FROM",t."MOBILE",t."BRIEF_DESC",t."CONTACT_EMAIL",
t."AUTO_COMPLETE",decode(t.status,2,1,1,0,3,0,9,0,8,2) as enabled,
NVL(NVL(NVL(r.dis_id,r.cy_id),r.prv_id),r.country_id) AS REGION_ID
from v3sie.institution t left join v3sie.ins_region r on t.ins_id =  r.ins_id;
--sns库 institution 视图修改。2019-6-3 LTL end


--原因（ROL-5956） 2019-6-4 ZYJ begin

update v_mail_template set subject_zh = '为{0}配置检索式和别名' , subject_en = '为{0}配置检索式和别名' where template_code = 10046;
update v_mail_template set subject_zh = '为{0}设计单位LOGO',subject_en = '为{0}设计单位LOGO' where template_code = 10047;
update v_mail_template set subject_zh = '新的单位注册：{0}' , subject_en = '新的单位注册：{0}' where template_code = 10056;
update v_mail_template set subject_zh = '申请加入被拒绝' , subject_en = '申请加入被拒绝' where template_code = 10111;
update v_mail_template set subject_zh = '申请加入成功' , subject_en = '申请加入成功' where template_code = 10110;
update v_mail_template set subject_zh = '分享机构主页' , subject_en = '分享机构主页' where template_code = 10114;
update v_mail_template set subject_zh = '重置密码，来自科研之友' , subject_en = '重置密码，来自科研之友' where template_code = 10068;
update v_mail_template set subject_zh = '登录密码提醒，来自科研之友' , subject_en = '登录密码提醒，来自科研之友' where template_code = 10085;
update v_mail_template set subject_zh = '您请求的全文已被上传' , subject_en = '您请求的全文已被上传' where template_code = 10115;
update v_mail_template set subject_zh = '单位注册被拒绝' , subject_en = '单位注册被拒绝' where template_code = 10116;
update v_mail_template set subject_zh = '单位注册成功' , subject_en = '单位注册成功' where template_code = 10117;
update v_mail_template set subject_zh = '科研验证开电子发票' , subject_en = '科研验证开电子发票' where template_code = 10120;

--原因（ROL-5956） 2019-6-4 ZYJ end


-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--原因（ROL-7097 删除 kpi_main_） 2019-6-11 XR begin
--删除后台任务sieStUserLoginTaskTrigger（SIE,单位用户登录数统计）
delete from v_quartz_cron_expression t where t.cron_trigger_bean = 'sieStUserLoginTaskTrigger';
commit;
--原因（ROL-7097 删除 kpi_main_） 2019-6-11 XR end