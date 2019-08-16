-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（    SCM-21286 个人版期刊匹配和保存） 2018-12-07 ajb start
 insert into  v_open_token_service_const (id , token,service_type,descr) values (2002  , '00000000' ,'hguu65op','查询期刊') ;
  insert into  v_open_token_service_const (id , token,service_type,descr) values (2003  , '11111111' ,'hguu65op','查询期刊') ;
insert into  v_open_token_service_const (id , token,service_type,descr) values (2004  , '00000000' ,'fgg34oiu','匹配期刊') ;
insert into  v_open_token_service_const (id , token,service_type,descr) values (2005  , '11111111' ,'fgg34oiu','匹配期刊') ;
commit ;
--原因（    SCM-21286 个人版期刊匹配和保存） 2018-12-07 ajb end

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
  
--SCM-21782 生产机：主页项目计数显示不准确，请批量处理 2018-12-10 ywl begin
update PSN_STATISTICS t1 set t1.prj_sum = (select count(*) from PROJECT t2 where t2.owner_psn_id = t1.psn_id and status = 0 group by t2.owner_psn_id)
where exists (select 1 from PROJECT t2 where t2.owner_psn_id = t1.psn_id)
commit;
--SCM-21782 生产机：主页项目计数显示不准确，请批量处理  2018-12-10 ywl end 
  

--原因（scm-0000 新增crossref库名） 2018-12-10 zll begin
 Insert Into const_ref_db(dbid,db_code,zh_cn_name,en_us_name,is_public,en_sort_key,zh_sort_key,db_type,db_bit_code,zh_abbr_name,en_abbr_name)
 Values(36,'CrossRef','CrossRef','CrossRef',0,0,0,1,0,'CrossRef','CrossRef');
 --原因（scm-0000 新增crossref库名） 2018-12-10 zll end
--SCM-21876 系统重置密码邮件改为新的发件方式  2018/12/13 HHT begin
update v_mail_template t set t.mail_type_id=23 ,t.status=0 where t.template_code=10009; 
update v_mail_template t set t.mail_type_id=23, t.status=0 where t.template_code=10010;
--SCM-21876 系统重置密码邮件改为新的发件方式  2018/12/13 HHT end
--原因（SCM-21777 crossref数据拆分到基准库各个表） 2017-12-7 zll begin
Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
Values(617,'PreImportCrossrefDataTaskTrigger','*/10 * * * * ?',0,'crossref数据预处理');
Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
Values(618,'DbcacheCfetchTaskTrigger','*/10 * * * * ?',0,'拆分crossref数据');
--原因（SCM-21777 crossref数据拆分到基准库各个表） 2017-12-7 zll end

--修改机构首页邮件分享主题（ROL-5933） 2018-12-19 LJM begin
update v_mail_template t set t.subject_zh = '分享机构主页邮件，{0}科研之友机构版：社会化机构知识库' where t.template_code = 10114;
update v_mail_template t set t.subject_en = '分享机构主页邮件，{0}科研之友机构版：社会化机构知识库' where t.template_code = 10114;
--修改机构首页邮件分享主题（ROL-5933） 2018-12-19 LJM end

--scm-000 添加sie同步单位人员关注的资助机构的接口  2018-12-21 LTL begin
insert into V_OPEN_TOKEN_SERVICE_CONST (ID, TOKEN, SERVICE_TYPE, STATUS, CREATE_DATE, DESCR, ACCESS_DATE, ACCESS_NUM, ACCESS_MAX_NUM, INS_ID)
values (15722, '11111111', 'siesync4', 0, to_date('21-12-2018 19:40:36', 'dd-mm-yyyy hh24:mi:ss'), '同步单位人员关注的资助机构', to_date('21-12-2018 19:40:36', 'dd-mm-yyyy hh24:mi:ss'), 9, 1000, null);
commit;
--scm-000 添加sie同步单位人员关注的资助机构的接口  2018-12-21 LTL end


--ROL-5925-全文反馈，在上传全文后，发送邮件给请求人，邮件内容见描述     2018-12-20 YXY begin
insert into V_MAIL_TEMPLATE (TEMPLATE_CODE, TEMPLATE_NAME, SUBJECT_ZH, SUBJECT_EN, STATUS, CREATE_DATE, UPDATE_DATE, MSG, MAIL_TYPE_ID, LIMIT_STATUS, PRIOR_LEVEL)
values (10115, 'Sie_Request_Fulltext_Feedback_Template', '您请求的全文已被上传', '您请求的全文已被上传', 0, to_date('17-12-2018 11:17:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-12-2018 11:17:00', 'dd-mm-yyyy hh24:mi:ss'), 'SIE全文请求反馈给请求人', 0, 0, 'D');

--ROL-5925-全文反馈，在上传全文后，发送邮件给请求人，邮件内容见描述     2018-12-20 YXY end

--原因（    SCM-21947 注册加手机号） 2018-12-25  ajb start

-- Create table
create table V_MOBILE_WHITELIST
(
  ID     NUMBER(18) not null,
  MOBILE NVARCHAR2(20),
  STATUS NUMBER(1) default 0
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table
comment on table V_MOBILE_WHITELIST
  is '手机白名单';
-- Add comments to the columns
comment on column V_MOBILE_WHITELIST.ID
  is '主键';
comment on column V_MOBILE_WHITELIST.MOBILE
  is '手机号';
comment on column V_MOBILE_WHITELIST.STATUS
  is '0=启动， 1=关闭';
-- Create/Recreate primary, unique and foreign key constraints
alter table V_MOBILE_WHITELIST
  add constraint PK_V_MOBILE_WHITE_ID primary key (ID)
  using index
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
-- Create/Recreate indexes
create unique index IDX_V_MOBILE_WHITE_M on V_MOBILE_WHITELIST (MOBILE)
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;

-- Add comments to the columns
comment on column MESSAGE_LOG.SMS_TYPE
  is '短信类型 1000为注册验证短信;2000=登录验证码';

commit ;
--原因（    SCM-21947 注册加手机号） 2018-12-25  ajb end


--原因（    SCM-21947 注册加手机号） 2018-12-25  ajb start
-- Create sequence
create sequence SEQ_MESSAGE_LOG
minvalue 1
maxvalue 9999999999999999999999999999
start with 100101
increment by 1
cache 20;

--原因（    SCM-21947 注册加手机号） 2018-12-25  ajb end
--SCM-22069 系统全文请求邮件修改为新的发件方式 2018-12-25 HHT begin
update v_mail_template t set t.status=0 where t.template_code=10048;
--SCM-22069 系统全文请求邮件修改为新的发件方式 2018-12-25 HHT end

---scm-0000  修改全文请求邮件标题 2018-12-26 HHT begin
update v_mail_template t set t.subject_zh='{1}向你请求论文全文',t.subject_en='{0} requested full-text of your article' where t.template_code=10048;  
---scm-0000  修改全文请求邮件标题 2018-12-26 HHT end---scm-0000  修改请求全文邮件typeid 2018-12-26 HHT begin
update v_mail_template t set t.mail_type_id=30 where t.template_code=10048;  
---scm-0000  修改请求全文邮件typeid 2018-12-26 HHT end

--原因    SCM-22035 pdwh成果导入，pub_member信息来源调整 2018-12-27 YJ begin
Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description) Values (440,'PdwhMemberInitTaskTrigger','*/10 * * * * ?',0,'初始化基准库成果作者信息表');
--原因    SCM-22035 pdwh成果导入，pub_member信息来源调整 2018-12-27 YJ end--SCM-22070 系统上传全文邮件改为新的发件方式 2018-12-27 HHT begin
update v_mail_template t set t.subject_zh='{0}为你上传了论文全文:{1}' ,t.subject_en='{0} uploaded full-text for you:{1}' ,t.status=0 where t.template_code=10038;
--SCM-22070 系统上传全文邮件改为新的发件方式 2018-12-27 HHT end


--SCM-22080 系统将邮件设置为登录账号 SCM-22079 系统首要邮箱设置通知 2018-12-27 HHT begin
update v_mail_template t set t.mail_type_id=29 where t.template_code=10038;
update v_mail_template t set t.subject_zh='登录账号设置',t.subject_en='Login account change',t.status=0,t.mail_type_id=23 
where t.template_code=10001;
update v_mail_template t set t.subject_zh='成功设置科研之友首要邮件',t.subject_en='Successfully setup primary email on ScholarMate',t.status=0,t.mail_type_id=22
where t.template_code=10000;
--SCM-22080 系统将邮件设置为登录账号 SCM-22079 系统首要邮箱设置通知  2018-12-27 HHT end
--原因（SCM-22103 系统群组上传文件，课件，作业通知其他人邮件改为新的发件方式） 2018-12-27 zll start
Update v_mail_template t Set t.subject_zh='{0}为群组新增了文件',t.subject_en='{0} added files to group' Where t.template_code=10093;
Update v_mail_template t Set t.subject_zh='{0}为群组新增了教学课件',t.subject_en='{0} added courseware to group' Where t.template_code=10094;
Update v_mail_template t Set t.subject_zh='{0}为群组新增了作业',t.subject_en='{0} added assignment to group' Where t.template_code=10072;
Update v_mail_template t Set t.subject_zh='{0}请求加入群组“{1}”',t.subject_en='{0}request to join the "{1} "group" ' Where t.template_code=10019;
--原因（SCM-22103 系统群组上传文件，课件，作业通知其他人邮件改为新的发件方式） 2018-12-27 zll start

--SCM-22105系统发送站内信邮件修改为新的发件方式 2018-12-27 HHT begin
update v_mail_template t set t.subject_zh='{0}，{1}给你发了一条站内信' ,t.subject_en='{0},{1} sent you a message',t.status=0,t.mail_type_id=27 where t.template_code=10003;
--SCM-22105系统发送站内信邮件修改为新的发件方式 2018-12-27 HHT end

--原因（SCM-22072 tempcode=186,187，合并账号的通知邮件没有发送成功） 2018-12-28 YHX begin
update v_mail_template t set t.status=0,t.prior_level='C',t.update_date=sysdate where t.template_code in (10044,10045);
update application_setting t set t.value=0 where t.key='psn_merge_email_notice';
commit;
--原因（SCM-22072 tempcode=186,187，合并账号的通知邮件没有发送成功） 2018-12-28 YHX end
--SCM-22111系统群组新增成果邮件改为新的发件方式  2018-12-28 HHT begin
update v_mail_template t set t.subject_zh='{0}在群组“{1}”添加了{2}条成果',
t.subject_en='{0} added {2} publication(s) to group {1} ',t.status=0,t.mail_type_id=11 where t.template_code=10076;
--SCM-22111系统群组新增成果邮件改为新的发件方式  2018-12-28 HHT end 
update v_mail_template t set t.subject_en='ScholarMate - Group Delete',t.status=0,t.mail_type_id=4 where t.template_code=10022;
update v_mail_template t set t.subject_zh='{0}，你已成功加入群组“{1}”',t.subject_en='{0},you are now in group "{1}"',t.status=0,t.mail_type_id=4 where t.template_code=10020;
update v_mail_template t set t.status=0,t.mail_type_id=5 where t.template_code=10019;

--SCM-22111系统群组新增成果邮件改为新的发件方式  2018-12-28 HHT end
--SCM-22108 系统删除群组通知邮件改为新的发件方式 SCM-22107系统同意加入群组邮件修改为新的发件方式 2018-12-28 HHT begin
update v_mail_template t set t.status=0,t.subject_zh='{0}成功加入群组',t.subject_en='{0} successfully joined group',t.mail_type_id=4 where t.template_code=10060; 
--SCM-22108 系统删除群组通知邮件改为新的发件方式 SCM-22107系统同意加入群组邮件修改为新的发件方式 2018-12-28 HHT end


--SCM-22110 系统基金分享邮件改为新的发件方式 2018-12-28 HHT begin 
update v_mail_template t set t.subject_zh='{0}向{1}分享了基金机会',t.subject_en='{0} shared a fund  with {1}',t.status=0,t.mail_type_id=20 where t.template_code=10075; 
--SCM-22110 系统基金分享邮件改为新的发件方式 2018-12-28 HHT end 


--生产机，拆分成果署名单位和通讯作者任务（ROL-6010） 2018-12-28 ZTG begin

INSERT INTO v_Quartz_Cron_Expression(ID,cron_trigger_bean,cron_expression,status,description)
VALUES(10057,'sieSplitPubOrgsTaskTrigger', '0 0 * * * ?', 0, '轮询task_split_pub_orgs，从PUB_JSON 字段拆分数据到 orgs、email_author（0 0 * * * ?）');

--生产机，拆分成果署名单位和通讯作者任务（ROL-6010） 2018-12-28 ZTG end


--BaiduMapGetInsAddsTask 拷贝一份 sieBaiduMapGetInsAddsTask 到机构版（ROL-5860） 2018-12-28 ZTG begin

INSERT INTO v_Quartz_Cron_Expression(ID,cron_trigger_bean,cron_expression,status,description)
VALUES(10058,'sieBaiduMapGetInsAddsTaskTrigger', '0 0 * * * ?', 0, '轮询TASK_INS_BAIDU_GET_ADDR表，获得单位详细地址（0 0 * * * ?）'); 

--BaiduMapGetInsAddsTask 拷贝一份 sieBaiduMapGetInsAddsTask 到机构版（ROL-5860） 2018-12-28 ZTG end



--SCM-22109 系统分享成果、文献、项目邮件改为新的发件方式  2018-12-29 HHT begin
update v_mail_template t set t.subject_zh='{0}分享了{1}个您可能感兴趣的{2}' ,t.subject_en='{0} shared {1} {2} that you may like',t.status=0,t.mail_type_id=20 where t.template_code=10102;
--SCM-22109 系统分享成果、文献、项目邮件改为新的发件方式  2018-12-29 HHT end


 --生产机，拆分科研验证结果明细任务（ROL-5938） 2018-12-29 ZTG begin
INSERT INTO v_Quartz_Cron_Expression(ID,cron_trigger_bean,cron_expression,status,description)
VALUES(10056,'sieSplitKpiValidateDetailTrigger', '*/10 * * * * ?', 0, '轮询task_kpi_validate_main, task_kpi_validate_detail表拆分数据(*/10 * * * * ?)'); 
 --生产机，拆分科研验证结果明细任务（ROL-5938） 2018-12-29 ZTG end


--原因（ROL-5678-单位分析，随机访问机器人） 2019-01-02 YXY begin
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (10100, 'sieRobotManTaskTriggers', '* */15 * * * ?', 1, 'SIE随机访问机器人');
--原因（ROL-5678-单位分析，随机访问机器人） 2019-01-02 YXY end

--SCMAPP-1105 清除个人关键词中的重复数据，保留最新添加的一条，否则PC端保存重复数据时会报错  2019-1-3 YWL begin
delete from PSN_DISCIPLINE_KEY t where t.rowid not in (select max(rowid) from PSN_DISCIPLINE_KEY t1 group by t1.psn_id, t1.key_words);
--SCMAPP-1105 清除个人关键词中的重复数据，保留最新添加的一条，否则PC端保存重复数据时会报错  2019-1-3 YWL end

---SCM-22173 系统账号关联验证码修改为新的发件方式 2019/1/3 HHT begin
update v_mail_template t set t.subject_en='Scholarmate-Account association verification code',t.status=0,t.mail_type_id=21 where t.template_code=10042;
---SCM-22173 系统账号关联验证码修改为新的发件方式 2019/1/3 HHT end

--SCM-22172 系统项目成员推荐成果修改为新的发件方式 2019/1/3 HHT begin
update v_mail_template t set t.subject_zh='{0},{1}认为你可能是以下论文的作者',t.subject_en='{0},{1}认为你可能是以下论文的作者',
t.status=0,t.mail_type_id=15 where t.template_code=10070;
--SCM-22172 系统项目成员推荐成果修改为新的发件方式 2019/1/3 HHT end




--SCM-21761 安卓版本科研之友应用程序开发，新增版本记录表 2019-1-3 WSN begin


create table v_mobile_app_version(
  id number(18) primary key,
  app_type varchar2(10 char) not null,
  must_update number(1) default 0 not null ,
  version_name varchar2(10 char) not null,
  version_code number(6) not null,
  version_desc varchar2(1000 char) not null,
  version_size varchar2(50 char) not null,
  download_url varchar2(100 char) not null,
  new_md5 varchar2(100 char),
  update_date Date
);

comment on table v_mobile_app_version is '移动端app版本信息_WSN';
comment on column v_mobile_app_version.id is '主键';
comment on column v_mobile_app_version.must_update is '是否需要强制更新， 1（需要），0（不用）';
comment on column v_mobile_app_version.app_type is 'app类型，安卓APP/苹果APP';
comment on column v_mobile_app_version.version_name is 'app版本号，如 1.0.1';
comment on column v_mobile_app_version.version_code is 'app版本code，方便APP端判断是否需要更新，安卓的APP中build.gradle中有个versionCode属性';
comment on column v_mobile_app_version.version_desc is '版本更新说明';
comment on column v_mobile_app_version.version_size is '版本更新包的大小，如 9.5 MB';
comment on column v_mobile_app_version.download_url is '新版本APP下载地址';
comment on column v_mobile_app_version.new_md5 is 'app文件签名的MD5值';
comment on column v_mobile_app_version.update_date is '版本更新时间';

create sequence mobile_app_version_seq
start with 1
increment by 1
maxvalue 999999999999999999;


insert into v_mobile_app_version values(mobile_app_version_seq.nextval, 'android', 0, '0.0.1', 1, '1.这是第一个版本\n2.一会要试下更新', '9.5 MB', 'https://test.scholarmate.com/F/633000abbce68b39ba5aff7d21481535', '', sysdate);
insert into v_mobile_app_version values(mobile_app_version_seq.nextval, 'android', 0, '0.0.2', 2, '1.这是第一个更新\n2.我要试下更新了', '9.5 MB', 'https://test.scholarmate.com/F/633000abbce68b39ba5aff7d21481535', '', sysdate);

commit;
--SCM-21761 安卓版本科研之友应用程序开发，新增版本记录表 2019-1-3 WSN end
--原因（ROL-5678-单位分析，随机访问机器人） 2019-01-03 YXY begin

update v_quartz_cron_expression t set t.CRON_EXPRESSION='0 0 0 1 * ?' , t.DESCRIPTION='SIE随机访问机器人(每个月执行一次0 0 0 1 * ?)' where t.ID=10100;


--原因（ROL-5678-单位分析，随机访问机器人） 2019-01-03 YXY end--SCM-22208 tempcode=10048，请求全文邮件模板，标题存在的问题 2019/1/4 HHT begin
update v_mail_template t set t.subject_zh='{0}向你请求论文全文' where t.template_code=10048;
--SCM-22208 tempcode=10048，请求全文邮件模板，标题存在的问题 2019/1/4 HHT end

--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--科研认证， 返回结果接口增加加密，解密的处理（ROL-5759） 2019-01-04 ZTG begin

--更改接口名称
UPDATE v_open_token_service_const t SET t.service_type = 'siescget' WHERE t.service_type = 'valiget1';


--科研认证， 返回结果接口增加加密，解密的处理（ROL-5759） 2019-01-04 ZTG end

--原因（ROL-5989-现有邮件迁移到新架构，规范命名和描述） 2019-01-04 YXY end
update v_mail_template t set t.template_name='Sie_Approved_Notification_Research_Group',t.msg='单位注册，审核通过，通知检索组',t.status=0,t.subject_zh='科研之友机构版，为{0}配置检索式和别名',t.subject_en='科研之友机构版，为{0}配置检索式和别名' where t.template_name='BPO_Approve_Notification_Research_Group';
update v_mail_template t set t.template_name='Sie_Approved_Notification_Artists',t.msg='单位注册，审核通过，通知美工',t.status=0,t.subject_zh='科研之友机构版，为{0}设计单位LOGO',t.subject_en='科研之友机构版，为{0}设计单位LOGO' where t.template_name='BPO_Approve_Notification_Artists';
update v_mail_template t set t.template_name='Sie_NewRegister_Need_Approve',t.msg='单位注册，通知超级管理员审核   ',t.status=0,t.subject_zh='科研之友机构版，新的单位注册：{0}',t.subject_en='科研之友机构版，新的单位注册：{0}' where t.template_name='sieorg_regist_byiris.ftl';
update v_mail_template t set t.template_name='Sie_RetrievePassword_Template',t.msg='重置密码',t.status=0,t.subject_zh='科研之友机构版，重置密码',t.subject_en='科研之友机构版，重置密码' where t.template_name='RetrievePwd_SimpleTemplate_bpo';
update v_mail_template t set t.template_name='Sie_Person_Send_Accounts_Template',t.msg='账号提醒',t.status=0,t.subject_zh='科研之友机构版，密码提醒',t.subject_en='科研之友机构版，密码提醒' where t.template_name='Rol_Person_Send_DoesnotLogin_Accounts_Template';
--原因（ROL-5989-现有邮件迁移到新架构，规范命名和描述） 2019-01-04 YXY end




--原因（ROL-5670 单位分析,社交化数据基表任务） 2019-01-07 HD begin

insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (10102, 'sieImpactBaseTaskTrigger', '0 0 0 1 * ? *', 0, '单位影响力，社交化数据基表任务');

--原因（ROL-5670 单位分析,社交化数据基表任务） 2019-01-07 HD end

-- 数据库脚本登记示例

--原因（ROL-5989-现有邮件迁移到新架构，规范命名和描述） 2019-01-08 YXY begin

insert into v_mail_template (TEMPLATE_CODE, TEMPLATE_NAME, SUBJECT_ZH, SUBJECT_EN, STATUS, CREATE_DATE, UPDATE_DATE, MSG, MAIL_TYPE_ID, LIMIT_STATUS, PRIOR_LEVEL)
values (10116, 'Sie_Approve_Reject', '{0}科研之友机构版，单位注册被拒绝', '{0}科研之友机构版，单位注册被拒绝', 0, to_date('08-01-2019 14:51:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-01-2019 14:51:00', 'dd-mm-yyyy hh24:mi:ss'), '单位注册审核，通知用户注册拒绝', 0, 0, 'D');

insert into v_mail_template (TEMPLATE_CODE, TEMPLATE_NAME, SUBJECT_ZH, SUBJECT_EN, STATUS, CREATE_DATE, UPDATE_DATE, MSG, MAIL_TYPE_ID, LIMIT_STATUS, PRIOR_LEVEL)
values (10117, 'Sie_Approve_Pass', '{0}科研之友机构版，单位注册审核通过', '{0}科研之友机构版，单位注册审核通过', 0, to_date('08-01-2019 14:51:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-01-2019 14:51:00', 'dd-mm-yyyy hh24:mi:ss'), '单位注册审核，通知用户注册通过', 0, 0, 'D');


--原因（ROL-5989-现有邮件迁移到新架构，规范命名和描述） 2019-01-08 YXY end
--原因（ROL-6123-邮件模板变更） 2019-01-09 YXY begin
update v_mail_template set template_name='Sie_Approved_Notification_Research' where template_name='Sie_Approved_Notification_Research_Group';
update v_mail_template set template_name='Sie_Approved_Notification_Reject' where template_name='Sie_Approve_Reject';
update v_mail_template set template_name='Sie_Approved_Notification_Pass' where template_name='Sie_Approve_Pass';
update v_mail_template set msg='申请加入，审核被拒' where template_name='Sie_Person_Send_Audit_Reject_Template';
update v_mail_template set msg='单位注册，通知用户被拒绝' where template_name='Sie_Approved_Notification_Reject';
update v_mail_template set msg='单位注册，通知用户审核通过' where template_name='Sie_Approved_Notification_Pass';
--原因（ROL-6123-邮件模板变更） 2019-01-09 YXY end

--原因（ROL-5670 单位分析,社交化数据拓展表任务） 2019-01-09 HD begin

insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (10103, 'sieImpactExtendTaskTrigger', '*/10 * * * * ?', 0, '单位影响力，社交化数据拓展表');

--原因（ROL-5670 单位分析,社交化数据拓展表任务） 2019-01-09 HD end--SCM-22436 tempcode=10044，人员合并的英文邮件模板，邮件标题还是中文 2019-01-10 HHT begin
update v_mail_template t set t.subject_en='Notification email for personnel merge' where t.template_code=10044;
--SCM-22436 tempcode=10044，人员合并的英文邮件模板，邮件标题还是中文 2019-01-10 HHT end

--原因（SCM-22384  手机注册发送消息，新增字段） 2019-01-10 艾江斌 start
-- Add/modify columns
alter table MESSAGE_LOG add PRODUCE_LOG_PSN_ID number(18);
-- Add comments to the columns
comment on column MESSAGE_LOG.PRODUCE_LOG_PSN_ID
  is '产生记录的psnId';
COMMIT ;
create index IDX_MESSAGE_LOG_PSN_ID on MESSAGE_LOG (produce_log_psn_id);
COMMIT ;

--原因（SCM-22384  手机注册发送消息，新增字段） 2019-01-10 艾江斌 end

--原因（ROL-5670 单位分析任务,修改执行时间） 2019-01-10 HD begin
update v_quartz_cron_expression t set t.cron_expression='0 0 0 1 * ?' where t.id=10102;
update v_quartz_cron_expression t set t.cron_expression='0 0 0 2 * ?' where t.id=10103;

--原因（ROL-5670 单位分析任务,修改执行时间） 2019-01-10 HD end
--SCM-22459  tempcode=10044，10073人员合并和确认系统地址的邮件，改为不可退订 2019-1-11 HHT  begin
update const_mail_type t set t.status=0 where t.mail_type_id=21;
--SCM-22459  tempcode=10044，10073人员合并和确认系统地址的邮件，改为不可退订 2019-1-11 HHT  end

--SCM-22459  tempcode=10044，10073人员合并和确认系统地址的邮件，改为不可退订 2019-1-11 HHT  begin
update const_mail_type t set t.status=0 where t.mail_type_id=21;
--SCM-22459  tempcode=10044，10073人员合并和确认系统地址的邮件，改为不可退订 2019-1-11 HHT  end


--原因:SCM-22802 生产机：主页，项目计数不对，请批量更新处理 2019-1-25 YWL begin
UPDATE PSN_STATISTICS t1
SET t1.prj_sum=(select count(*) from PROJECT t2 where t2.owner_psn_id = t1.psn_id and status =0 group by t2.owner_psn_id) WHERE EXISTS (SELECT 1 FROM PROJECT t2 WHERE t2.owner_psn_id = t1.psn_id);
commit;
--原因:SCM-22802 生产机：主页，项目计数不对，请批量更新处理 2019-1-25 YWL end


--原因（SCM-22834 项目-联邦检索，现在 不支持 香港创新及科技基金 库检索，应把此功能去掉） 2019-1-25 YWL begin
update CONST_REF_DB set is_Public = 0 where dbid = 7;
commit;
--原因（SCM-22834 项目-联邦检索，现在 不支持 香港创新及科技基金 库检索，应把此功能去掉） 2019-1-25 YWL end


---原因 （SCM-22847 增加后台任务开关，完成后台任务--为昌盛组打包提供成果全文数据) 2019-1-25 SYL start
insert into app_quartz_setting(app_id,task_name,value) values(248,'PdwhFullTextDownloadToLocalTask',0);
commit;
---原因 （SCM-22847 增加后台任务开关，完成后台任务--为昌盛组打包提供成果全文数据) 2019-1-25 SYL end

--原因（    SCM-23294 论文验证日志表v_pub_verify_log，加一个参与人字段）  2019-02-25 ajb start


-- Add/modify columns
alter table V_PUB_VERIFY_LOG add PARTICIPANT_NAMES NVARCHAR2(500);
-- Add comments to the columns
comment on column V_PUB_VERIFY_LOG.PARTICIPANT_NAMES
  is '参与人';



--原因（    SCM-23294 论文验证日志表v_pub_verify_log，加一个参与人字段）  2019-02-25 ajb  end


--数据统计脚本（SCM-20373） 2019-2-28 xiexing begin
DROP TABLE t_pub_temp;

CREATE TABLE t_pub_temp(
pub_id NUMBER(18) PRIMARY KEY,
organizer VARCHAR2(2000),
status NUMBER(2) DEFAULT 0
);

INSERT INTO t_pub_temp(pub_id) select t.pub_id from v_pub_sns t where t.PUB_TYPE = 3 AND t.status = 0;

INSERT INTO v_quartz_cron_expression VALUES(621,'ConferencePaperDataStatisticsTaskTrigger','0 0/1 * * * ?',0,'会议论文数据统计任务');

COMMIT;
--数据统计脚本（SCM-20373） 2019-2-28 xiexing end

--原因（SCM-23427 发送邀请订购科研验证服务邮件） 2019-03-01 zll begin
Insert Into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
Values(1009,'SendInvitePsnValidateMailTaskTrigger','0 0/1 * * * ?',7,'发送邀请订购科研验证服务邮件')

--原因（SCM-23427 发送邀请订购科研验证服务邮件） 2019-03-01 zll end


