-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--原因（有CQ号带上CQ号） 2016-12-8 WSN end--SCM-19406 tempcode=10025,10051,10052,10054，10106联系人更新研究领域，赞/评论/分享论文、待处理消息的邮件模板，点击退订，提示退订失败。2018-10-22 HHT begin

update v_mail_template t set t.mail_type_id=6  where t.template_code=10025; 

--SCM-19406 tempcode=10025,10051,10052,10054，10106联系人更新研究领域，赞/评论/分享论文、待处理消息的邮件模板，点击退订，提示退订失败。2018-10-22 HHT end--SCM-19406 tempcode=10025,10051,10052,10054，10106联系人更新研究领域，赞/评论/分享论文、待处理消息的邮件模板，点击退订，提示退订失败。2018-10-22 HHT begin

update v_mail_template t set t.mail_type_id=6 where t.template_code=10052;

--SCM-19406 tempcode=10025,10051,10052,10054，10106联系人更新研究领域，赞/评论/分享论文、待处理消息的邮件模板，点击退订，提示退订失败。2018-10-22 HHT end
--原因（有CQ号带上CQ号） 2016-12-8 WSN end
--SCM-19406 tempcode=10025,10051,10052,10054，10106联系人更新研究领域，赞/评论/分享论文、待处理消息的邮件模板，点击退订，提示退订失败。2018-10-23 HHT begin

update v_mail_template t set t.mail_type_id=0 where t.template_code=10052 and t.template_code=10025;

--SCM-19406 tempcode=10025,10051,10052,10054，10106联系人更新研究领域，赞/评论/分享论文、待处理消息的邮件模板，点击退订，提示退订失败。2018-10-23 HHT end

--SCM-19406 tempcode=10025,10051,10052,10054，10106联系人更新研究领域，赞/评论/分享论文、待处理消息的邮件模板，点击退订，提示退订失败。2018-10-23 HHT begin

update v_mail_template t set t.mail_type_id=0 where t.template_code=10052;
update v_mail_template t set t.mail_type_id=0 where t.template_code=10025;
--SCM-19406 tempcode=10025,10051,10052,10054，10106联系人更新研究领域，赞/评论/分享论文、待处理消息的邮件模板，点击退订，提示退订失败。2018-10-23 HHT end
--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--SCM-19406 tempcode=10025,10051,10052,10054，10106联系人更新研究领域，赞/评论/分享论文、待处理消息的邮件模板，点击退订，提示退订失败。2018-10-23 HHT begin
update v_mail_template t set t.mail_type_id=20 where t.template_code=10052;
update v_mail_template t set t.mail_type_id=17 where t.template_code=10025;
update v_mail_template t set t.mail_type_id=20 where t.template_code=10051;
update v_mail_template t set t.mail_type_id=20 where t.template_code=10054;
update v_mail_template t set t.mail_type_id=27 where t.template_code=10106;
--SCM-19406 tempcode=10025,10051,10052,10054，10106联系人更新研究领域，赞/评论/分享论文、待处理消息的邮件模板，点击退订，提示退订失败。2018-10-23 HHT end
--SCM - 科研之友SCM-19641 科研影响力邮件任务触发后，没有发送邮件2018/10/15 HHT start
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,Description) values(537,'InfluenceStatisticalTaskTrigger','*/10 * * * * ?',0,'科研影响力月度统计邮件任务')
--SCM - 科研之友SCM-19641 科研影响力邮件任务触发后，没有发送邮件2018/10/15 HHT end

--原因（SCM-20731 项目统计数不正确） 2018-10-30 LTL begin
update PSN_STATISTICS a set a.prj_sum=(select count(1) as prj from Project b where b.OWNER_PSN_ID = a.psn_id and b.status =0)
--原因（SCM-20731 项目统计数不正确） 2018-10-30 LTL end


--原因（SCM-20986 创新城登录页面新增国际化字段）
alter table v_open_third_reg add THIRD_SYS_NAME_US varchar2(100 char);
update v_open_third_reg set THIRD_SYS_NAME_US = 'Innocity' where token = 'cba4b03f';
commit;
--原因


--原因(SCM-16165 中国的region_id 是156 ，不存在157的region_id)
update PERSON set region_id = 156 where region_id = 157;
--原因





--SCM-19946 全文权限有部分数据是2（应该是以前好友权限的）的，现将2的改为1（隐私）  2018-11-08 WSN begin

update v_pub_fulltext t set t.permission = 1 where t.permission = 2;
commit;

--SCM-19946 全文权限有部分数据是2（应该是以前好友权限的）的，现将2的改为1（隐私）  2018-11-08 WSN end


--原因（    SCM-21177  open新加成果验证） 2018-11-14 ajb start

insert into  v_open_token_service_const (id , token,service_type,descr) values (2000  , '00000000' ,'lxj3219s','论文验证') ;
insert into  v_open_token_service_const (id , token,service_type,descr) values (2001  , '00000000' ,'jyh99kls','个人主页成果验证') ;
commit ;

--原因（    SCM-21177  open新加成果验证） 2018-11-14 ajb end  error_msg    VARCHAR2(1000)

--SCM-20374 新创建的成果在v_pub_sns_detail表没有生成记录     2018-11-15  YJ begin
create table V_PUB_DATA_BACKUPS
(
  pub_id       NUMBER(18) not null,
  data_type    NUMBER(2),
  status       NUMBER(2),
  gmt_modified DATE,
  error_msg    VARCHAR2(1000)
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
comment on column V_PUB_DATA_BACKUPS.pub_id is '成果主键';
comment on column V_PUB_DATA_BACKUPS.data_type is '数据的类型，0未sns库，1为pdwh库';
comment on column V_PUB_DATA_BACKUPS.status is '成果备份状态，0尚未处理，1处理成功，99处理失败';
comment on column V_PUB_DATA_BACKUPS.gmt_modified is '数据备份的处理时间';
comment on column V_PUB_DATA_BACKUPS.error_msg is '数据备份失败的错误信息';
alter table V_PUB_DATA_BACKUPS
  add constraint V_PUB_DATA_BACKUPS_ID_PK primary key (PUB_ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
  
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) values(430,'PubPdwhDataBackupsTaskTrigger','*/10 * * * * ?',0,'基准库mongodb数据备份任务');
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) values(431,'PubSnsDataBackupsTaskTrigger','*/10 * * * * ?',0,'个人库mongodb数据备份任务');

--SCM-20374 新创建的成果在v_pub_sns_detail表没有生成记录     2018-11-15  YJ end







---SCM-21196 应用功能修改  2018-11-13 WSN begin

----分享资助机构记录表  begin
create table v_agency_share(
       share_id number(18) not null primary key,
       agency_id number(18) not null,
       share_psn_id number(18),
       receive_psn_id number(18),
       group_id number(18),
       create_date date,
       share_to_platform number(1),
       comments varchar2(4000 char)
);


comment on table v_agency_share is '资助机构分享记录表';
comment on column v_agency_share.share_id is '分享ID，主键';
comment on column v_agency_share.agency_id is '资助机构ID';
comment on column v_agency_share.share_psn_id is '做分享操作的人员ID';
comment on column v_agency_share.receive_psn_id is '分享给联系人时接收的人员ID';
comment on column v_agency_share.group_id is '分享给群组时的对应的群组ID';
comment on column v_agency_share.create_date is '分享操作的时间';
comment on column v_agency_share.share_to_platform is '分享到哪个地方，1:动态,2:联系人,3:群组,4:微信,5:新浪微博,6:Facebook,7:Linkedin';
comment on column v_agency_share.comments is '分享留言';


create sequence SEQ_FUND_AGENCY_SHARE
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20;

----分享资助机构记录表  end


----关注资助机构记录表  begin
create table v_fund_agency_interest(
       id number(18) not null primary key,
       psn_id number(18) not null,
       agency_id number(18) not null,
       create_date date default sysdate not null ,
       update_date date default sysdate not null,
       status number(1) not null
);

comment on table v_fund_agency_interest is '人员关注的资助机构表';
comment on column v_fund_agency_interest.id is '主键';
comment on column v_fund_agency_interest.psn_id is '关注的人员ID';
comment on column v_fund_agency_interest.agency_id is '关注的资助机构ID';
comment on column v_fund_agency_interest.create_date is '记录创建时间';
comment on column v_fund_agency_interest.update_date is '记录更新时间';
comment on column v_fund_agency_interest.status is '状态，1：已关注， 0，未关注（SIE有接口会更新该表，为0的即使SIE那边有传过来也不更新为已关注）';

alter table v_fund_agency_interest add constraint fund_agency_interest_unique unique(psn_id, agency_id);

create sequence SEQ_FUND_AGENCY_INTEREST
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20;

alter table V_FUND_AGENCY_INTEREST add agency_order NUMBER(5) default 0;
comment on column V_FUND_AGENCY_INTEREST.agency_order
  is '排序字段';
----关注资助机构记录表  end


----赞资助机构记录表  begin

create table v_fund_agency_award(
       id number(18) not null primary key,
       psn_id number(18) not null,
       agency_id number(18) not null,
       create_date date default sysdate not null ,
       update_date date default sysdate not null,
       status number(1) not null
);


comment on table v_fund_agency_award is '人员赞资助机构表记录表';
comment on column v_fund_agency_award.id is '主键';
comment on column v_fund_agency_award.psn_id is '赞/取消赞的人员ID';
comment on column v_fund_agency_award.agency_id is '赞/取消赞的资助机构ID';
comment on column v_fund_agency_award.create_date is '记录创建时间';
comment on column v_fund_agency_award.update_date is '记录更新时间';
comment on column v_fund_agency_award.status is '状态，1：赞， 0：取消赞';


alter table v_fund_agency_award add constraint fund_agency_award_unique unique(psn_id, agency_id);

create sequence SEQ_FUND_AGENCY_AWARD
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20;


----赞资助机构记录表  end


----资助机构操作统计表  begin

create table v_agency_statistics(
       agency_id number(18) not null primary key,
       award_sum number(9) default 0,
       share_sum number(9) default 0,
       interest_sum number(9) default 0 
);

comment on table v_agency_statistics is '资助机构操作统计表';
comment on column v_agency_statistics.agency_id is '资助机构ID, 主键';
comment on column v_agency_statistics.award_sum is '赞总数';
comment on column v_agency_statistics.share_sum is '分享总数';
comment on column v_agency_statistics.interest_sum is '关注总数';

----资助机构操作统计表  end
---基金推荐初始化 start
update V_RECOMMEND_INIT set fund_recommend_init=0;
---基金推荐初始化 end
commit;

---SCM-21196 应用功能修改  2018-11-13 WSN end


--原因（    ROL-5615  科研验证，受理接口） 2018-11-22 hd start

insert into  v_open_token_service_const (id , token,service_type,descr) values (15713 , '11111111' ,'siescirv','科研验证,受理接口') ;
insert into  v_open_token_service_const (id , token,service_type,descr) values (15714 , '11111111' ,'jyh99kls','个人主页成果验证') ;
insert into  v_open_token_service_const (id , token,service_type,descr) values (15715 , '11111111' ,'lxj3219s','论文验证') ;

--原因（    ROL-5615  科研验证，受理接口） 2018-11-22 hd end


--原因 （SCM-21326 首页动态列表加载推荐信息 ）2018-11-23 YHX begin
create table V_PUB_RECOMMEND_RECORD(
  id           NUMBER(18) not null primary key,
  psn_id       NUMBER(18),
  pub_id       NUMBER(18),
  status       NUMBER(1),
  gmt_create   DATE,
  gmt_modified DATE
);
-- Add comments to the table 
comment on table V_PUB_RECOMMEND_RECORD is '论文推荐操作记录表';
-- Add comments to the columns 
comment on column V_PUB_RECOMMEND_RECORD.id is 'ID';
comment on column V_PUB_RECOMMEND_RECORD.psn_id is '人员ID';
comment on column V_PUB_RECOMMEND_RECORD.pub_id is '成果ID';
comment on column V_PUB_RECOMMEND_RECORD.status is '状态：0正常，1不感兴趣';
comment on column V_PUB_RECOMMEND_RECORD.gmt_create is '创建时间';
comment on column V_PUB_RECOMMEND_RECORD.gmt_modified is '被修改时间';
  -- Create table
create table V_FUND_RECOMMEND_RECORD(
  id           NUMBER(18) not null primary key,
  psn_id       NUMBER(18),
  fund_id      NUMBER(18),
  status       NUMBER(1),
  gmt_create   DATE,
  gmt_modified DATE
);
-- Add comments to the table 
comment on table V_FUND_RECOMMEND_RECORD is '基金推荐操作记录表';
-- Add comments to the columns 
comment on column V_FUND_RECOMMEND_RECORD.id is 'ID';
comment on column V_FUND_RECOMMEND_RECORD.psn_id is '人员ID';
comment on column V_FUND_RECOMMEND_RECORD.fund_id is '基金ID';
comment on column V_FUND_RECOMMEND_RECORD.status is '状态：0正常，1不感兴趣';
comment on column V_FUND_RECOMMEND_RECORD.gmt_create is '创建时间';
comment on column V_FUND_RECOMMEND_RECORD.gmt_modified is '被修改时间';

-- Create sequence 
create sequence V_SEQ_PUB_RECOMMEND_RECORD
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20;
-- Create sequence 
create sequence V_SEQ_FUND_RECOMMEND_RECORD
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20;

--原因 （SCM-21326 首页动态列表加载推荐信息 ） 2018-11-23 YHX end


--科研验证，后台任务sieKpiValidateTask，test分支上开发（ROL-5623） 2018-11-24 ZTG start
-- 更新后台任务表
INSERT INTO v_quartz_cron_expression(ID,cron_trigger_bean,cron_expression,status,description)
VALUES(1003,'sieKpiValidateTaskTrigger', '0 */1 * * * ?', 0, '轮询kpi_validate_detail表并根据业务类型调用具体的验证接口');

-- 更新接口服务token配置


INSERT INTO v_open_token_service_const(id,token,service_type,descr)
VALUES (SEQ_V_OPEN_TOKEN_SERVICE_ID.Nextval,'11111111','valiget1','科研认证，获取接口');
--科研验证，后台任务sieKpiValidateTask，test分支上开发（ROL-5623） 2018-11-24 ZTG end

--SCM-18864 v_fund_recommend_senior表会出现生成重复记录的问题 ltl begin
delete  from V_FUND_RECOMMEND_SENIOR a  
where a.rowid !=  
(  
select max(b.rowid) from V_FUND_RECOMMEND_SENIOR b  
where a.psn_id = b.psn_id 
);
commit;
create unique index PK_FUND_RECOMMEND_PSNID on V_FUND_RECOMMEND_SENIOR (psn_id);
--SCM-18864 v_fund_recommend_senior表会出现生成重复记录的问题 ltl end

--SCM-18864 v_fund_recommend_senior表会出现生成重复记录的问题 ltl begin
delete  from V_FUND_RECOMMEND_SENIOR a  
where a.rowid !=  
(  
select max(b.rowid) from V_FUND_RECOMMEND_SENIOR b  
where a.psn_id = b.psn_id 
);
commit;
create unique index PK_FUND_RECOMMEND_PSNID on V_FUND_RECOMMEND_SENIOR (psn_id);
--SCM-18864 v_fund_recommend_senior表会出现生成重复记录的问题 ltl end

--ROL-5663 科研验证应用入口权限 2018-11-28 zsj begin


insert into v_open_token_service_const (ID, TOKEN, SERVICE_TYPE, STATUS, CREATE_DATE, DESCR, ACCESS_DATE, ACCESS_NUM, ACCESS_MAX_NUM, INS_ID)
values (810, '11111111', 'kpipayv1', 0, to_date('28-11-2018 16:24:50', 'dd-mm-yyyy hh24:mi:ss'), '判断单位模块是否付费接口', to_date('28-11-2018 16:24:50', 'dd-mm-yyyy hh24:mi:ss'), 0, 1000, null);


--ROL-5663 科研验证应用入口权限 2018-11-28 zsj begin


--SCM-18410 生产机：存在13条用户有绑定2个微信的记录，是否类同qq的处理下 WSN 2018-11-29 begin
DELETE v_wechat_relation
WHERE smate_openid IN
(SELECT smate_openid FROM v_wechat_relation GROUP BY smate_openid
HAVING COUNT(*) > 1)
AND ROWID NOT IN
(SELECT MIN(ROWID) FROM v_wechat_relation GROUP BY smate_openid
HAVING COUNT(*) > 1); 

commit;
--SCM-18410 生产机：存在13条用户有绑定2个微信的记录，是否类同qq的处理下 WSN 2018-11-29 end


-- 原因（SCM-21751 公司邮件回访功能迁移） 2018-12-5 YHX begin
create table V_IRISSZ_MAIL_DETAIL(
  id          NUMBER(18) not null primary key,
  cliect_ip   VARCHAR2(50),
  name        VARCHAR2(100 CHAR),
  email       VARCHAR2(100),
  tel         VARCHAR2(100 CHAR),
  type        NUMBER(1),
  ins_name    VARCHAR2(200 CHAR),
  address     VARCHAR2(200 CHAR),
  remark      VARCHAR2(2000 CHAR),
  create_date DATE,
  domain      VARCHAR2(100)
);
-- Add comments to the table
comment on table V_IRISSZ_MAIL_DETAIL is 'IRIS申请试用记录';

-- Add comments to the columns
comment on column V_IRISSZ_MAIL_DETAIL.id is '主键ID';
comment on column V_IRISSZ_MAIL_DETAIL.email is '邮箱';
comment on column V_IRISSZ_MAIL_DETAIL.tel is '手机号';
comment on column V_IRISSZ_MAIL_DETAIL.ins_name is '单位名称';
comment on column V_IRISSZ_MAIL_DETAIL.address is '地址';
comment on column V_IRISSZ_MAIL_DETAIL.type is '类别:1.科技管理;2.成果推广;3.技术转移';
comment on column V_IRISSZ_MAIL_DETAIL.remark is '备注';

create sequence V_SEQ_IRISSZ_MAIL_DETAIL
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20;

update v_mail_template t  set t.status=0,t.update_date=sysdate,t.template_name='irissz_com_contact' where t.template_code=10058;
insert into v_mail_template (TEMPLATE_CODE, TEMPLATE_NAME, SUBJECT_ZH, SUBJECT_EN, STATUS, CREATE_DATE, UPDATE_DATE, MSG, MAIL_TYPE_ID, LIMIT_STATUS, PRIOR_LEVEL)
values (10113, 'new_irissz_com_contact', '公司网站销售邮件', '公司网站销售邮件', 0, sysdate, sysdate, '', 0, 0, 'D');
-- 原因（SCM-21751 公司邮件回访功能迁移） 2018-12-5 YHX end

-- 原因（SCM-21751 公司邮件回访功能迁移） 2018-12-5 YHX begin
update v_mail_template t  set t.subject_zh='{0} 在[{1}]网站向我们咨询',t.subject_en='{0} 在[{1}]网站向我们咨询', t.update_date=sysdate where t.template_code=10058;
update v_mail_template t  set t.subject_zh='{0} 在[{1}]网站向我们咨询' ,t.subject_en='{0} 在[{1}]网站向我们咨询' ,t.update_date=sysdate where t.template_code=10113;
-- 原因（SCM-21751 公司邮件回访功能迁移） 2018-12-5 YHX end

-    SCM-21816论文验证，添加日志记录表。 ajb 2018-12-06 begin
-- Create sequence
create sequence SEQ_V_PUB_VERIFY_LOG
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 10;


-- Create table
create table V_PUB_VERIFY_LOG
(
  ID           NUMBER(18) not null,
  TITLE        NVARCHAR2(2000),
  ITEM_STATUS  NUMBER(1),
  ITEM         NVARCHAR2(2000),
  CORRECT_DATA NVARCHAR2(2000),
  TYPE         NVARCHAR2(200),
  ITEM_MSG     NVARCHAR2(200),
  GMT_CREATE   DATE,
  SERVICE_TYPE NUMBER(1)
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
comment on table V_PUB_VERIFY_LOG
  is '成果验证日志';
-- Add comments to the columns
comment on column V_PUB_VERIFY_LOG.ID
  is '主键';
comment on column V_PUB_VERIFY_LOG.TITLE
  is '验证成果标题';
comment on column V_PUB_VERIFY_LOG.ITEM_STATUS
  is '验证结果 1=成功；2=不成功；3=不确定';
comment on column V_PUB_VERIFY_LOG.ITEM
  is '验证的成果信息';
comment on column V_PUB_VERIFY_LOG.CORRECT_DATA
  is '正确数据';
comment on column V_PUB_VERIFY_LOG.TYPE
  is '验证成果的状态码';
comment on column V_PUB_VERIFY_LOG.ITEM_MSG
  is '验证成果的状态信息';
comment on column V_PUB_VERIFY_LOG.GMT_CREATE
  is '验证日期';
comment on column V_PUB_VERIFY_LOG.SERVICE_TYPE
  is '服务类型，1=论文验证；2=主页成果验证';
-- Create/Recreate primary, unique and foreign key constraints
alter table V_PUB_VERIFY_LOG
  add constraint PK_PUB_VERIFY primary key (ID)
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
commit;
--    SCM-21816论文验证，添加日志记录表。 ajb 2018-12-06 end

--scm-000 验证成果日志  ajb 2018-12-06 begin
-- Add/modify columns
alter table V_PUB_VERIFY_LOG modify TYPE NVARCHAR2(500);
alter table V_PUB_VERIFY_LOG modify ITEM_MSG NVARCHAR2(500);
commit;
--scm-000 验证成果日志  ajb 2018-12-06 end
--增加科研之友机构版首页发送邮件模板脚本（ROL-3956） 2018-12-12 LJM begin
insert into v_mail_template (TEMPLATE_CODE, TEMPLATE_NAME, SUBJECT_ZH, SUBJECT_EN, STATUS, CREATE_DATE, UPDATE_DATE, MSG, MAIL_TYPE_ID, LIMIT_STATUS, PRIOR_LEVEL)
values (10114, 'Sie_Homepage_Email_Share_Template', '{0}科研之友机构版：社会化机构知识库', '{0}科研之友机构版：社会化机构知识库', 0, to_date('07-12-2018 14:26:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-12-2018 14:26:00', 'dd-mm-yyyy hh24:mi:ss'), '主页分享邮件', 0, 0, 'D');
--增加科研之友机构版首页发送邮件模板脚本（ROL-3956） 2018-12-12 LJM end


--SCM-21844 基金推荐--》左边栏关注的资助机构条件，初始化的时候有些国家级的不要初始化给个人  ltl 2018-12-10 begin
update v_fund_agency_interest set status=0 where psn_id in (select psn_id from (
select psn_id ,count(1) as cnum from v_fund_agency_interest group by psn_id) where cnum=10) and agency_id not in(1000,2207,2205,1002470,2206,1002849,1002528);
commit;
--SCM-21844 基金推荐--》左边栏关注的资助机构条件，初始化的时候有些国家级的不要初始化给个人  ltl 2018-12-10 end

--SCM-000 基金赞表删除重复记录和添加唯一约束 2018-12-10 begin
delete  from V_FUND_AWARD a  
where a.rowid !=  
(  
select max(b.rowid) from V_FUND_AWARD b  
where a.award_psn_id = b.award_psn_id and  
a.fund_id = b.fund_id  
); 
commit;
alter table V_FUND_AWARD
  add constraint V_FUND_AWARD_UNIQUE unique (FUND_ID, AWARD_PSN_ID);
--SCM-000 基金赞表删除重复记录和添加唯一约束 2018-12-10 end

--SCM-21842 移动端：连续多次点击绑定按钮会快速提示请勿重复绑定，但数据库还是会产生多条重复的绑定记录 2018-12-11 begin
alter table v_wechat_relation
  add constraint v_wechat_relation unique (WECHAT_OPENID,WECHAT_UNIONID);
--SCM-21842 移动端：连续多次点击绑定按钮会快速提示请勿重复绑定，但数据库还是会产生多条重复的绑定记录 2018-12-11 end

-- 原因（ROL-5676） 2018-12-11 HD begin
update v_quartz_cron_expression t set t.cron_trigger_bean='sieSyncPubSocialBehaveTaskTrigger' where t.id=1001;
update v_quartz_cron_expression t set t.cron_trigger_bean='sieSyncPatSocialBehaveTaskTrigger' where t.id=1002;
-- 原因（ROL-5676） 2018-12-11 HD begin

--SCM-21853 基金推荐已经初始化的了但是用户没有设置资助机构的，重新给他初始化  LTL 2018-12-12 begin
delete v_fund_agency_interest where psn_id in (select psn_id from (
select psn_id ,count(1) as cnum from v_fund_agency_interest group by psn_id) where cnum=10); 

update V_RECOMMEND_INIT a set a.fund_recommend_init=0 where  not exists(select 1 from v_fund_agency_interest b where a.psn_id = b.psn_id);

commit;
--SCM-21853 基金推荐已经初始化的了但是用户没有设置资助机构的，重新给他初始化  LTL 2018-12-12 end


--SCM-21876 系统重置密码邮件改为新的发件方式 HHT 2018-12-14 begin
update v_mail_template t set t.subject_zh='科研之友–密码重设',t.subject_en='ScholarMate - Reset password' where t.template_code=10009;
--SCM-21876 系统重置密码邮件改为新的发件方式 HHT 2018-12-14 end

--SCM-0000 10010邮件模板英文主题为中文  HHT 2018-12-17 begin
update v_mail_template t set t.subject_zh='科研之友–密码重设',t.subject_en='ScholarMate - Reset password' where t.template_code=10010;
--SCM-0000 10010邮件模板英文主题为中文  HHT 2018-12-17 end

update v_mail_template t set t.mail_type_id=23,t.status=0 where t.template_code=10009;
update v_mail_template t set t.mail_type_id=23,t.status=0 where t.template_code=10010;
update v_mail_template t set t.status=0,t.mail_type_id=21 where t.template_code=10073;


--SCM-21925 请求好友邮件改为新的发件方式  2018-12-19 HHT begin

update v_mail_template t set t.status=0,t.mail_type_id=1 where t.template_code=10006;
update v_mail_template t set t.status=0,t.mail_type_id=1 where t.template_code=10007;

--SCM-21925 请求好友邮件改为新的发件方式  2018-12-19 HHT  end

--原因（SCM-21991后台、在线导入数据前期拆分原始数据到mongodb） 2018-12-20 zll begin
Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
Values(619,'SavePdwhPubDataTaskTrigger','*/10 * * * * ?',0,'保存基准库成果');

--原因（SCM-21991后台、在线导入数据前期拆分原始数据到mongodb） 2018-12-20 zll end


--原因（有CQ号带上CQ号） 2016-12-8 WSN end
--SCM-21985 tempcode=10053 下载论文改为新的发件方式 2018-12-20 HHT begin
update v_mail_template t set t.subject_zh ='{0}下载了你的论文：{1}',
t.subject_en='{0} downloaded your publications : {1}', t.status=0,t.mail_type_id=20 where t.template_code=10053;
--SCM-21985 tempcode=10053 下载论文改为新的发件方式 2018-12-20 HHT end

--SCM-0000 请求添加联系人邮件主题出问题 2018-12-20 HHT begin
update v_mail_template t set t.subject_zh='{0}请求成为你的联系人',t.subject_en='{0} Request to be your contact' where t.template_code=10006;
update v_mail_template t set t.subject_zh='{0}请求成为你的联系人',t.subject_en='{0} Request to be your contact' where t.template_code=10007;
--SCM-0000 请求添加联系人邮件主题出问题 2018-12-20 HHT begin

--SCM-21983 tempcode=10091 好友邀请成功通知模板 改为新的发件方式 2018-12-21 HHT begin
update v_mail_template t set t.subject_zh='你已经与{0}成为了联系人 ',t.subject_en='You are now connected with {0}',t.mail_type_id=3,t.status=0 where t.template_code=10091;
--SCM-21983 tempcode=10091 好友邀请成功通知模板 改为新的发件方式 2018-12-21 HHT end

--SCM-000 新增任务根据成果id更新sorl信息  2018-12-21 YJ begin

insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) values(320,'PdwhPubSorlUpdateTaskTrigger','*/10 * * * * ?',0,'指定成果id更新sorl');

--SCM-000 新增任务根据成果id更新sorl信息  2018-12-21 YJ end


--SCM-0000 接受联系人邀请通知没有对应的邮件设置 2018-12-21 HHT begin
update const_mail_type t set t.remark=1 where t.mail_type_id=3;
--SCM-0000 接受联系人邀请通知没有对应的邮件设置 2018-12-21 HHT end