-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end




--原因（SCM-17947 成果指派新增逻辑，添加指派具体匹配内容） 2018-05-31 ZLL begin


-- Create table
create table pub_assign_log_detail
(
  id                number(18) not null,
  pub_id            number(18) not null,
  psn_id            number(18) not null,
  matched_email     varchar2(50),
  matched_name      varchar2(50),
  matched_name_type varchar2(50),
  matched_insid     number(9)
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
comment on table pub_assign_log_detail
  is '成果指派记录详情表';
-- Add comments to the columns 
comment on column pub_assign_log_detail.id
  is '主键';
comment on column pub_assign_log_detail.pub_id
  is '基准库成果id';
comment on column pub_assign_log_detail.psn_id
  is '指派人员id';
comment on column pub_assign_log_detail.matched_email
  is '匹配上的eamil';
comment on column pub_assign_log_detail.matched_name
  is '匹配上的名称';
comment on column pub_assign_log_detail.matched_name_type
  is '匹配名称类型 1 全称;0 简称';
comment on column pub_assign_log_detail.matched_insid
  is '匹配上的insId';
-- Create/Recreate primary, unique and foreign key constraints 
alter table pub_assign_log_detail
  add constraint pk_pub_assign_log_detail primary key (ID);
-- Create/Recreate indexes 
create index uk_pub_assign_log_detail on pub_assign_log_detail (pub_id, psn_id);

  
  create sequence SEQ_PUB_ASSIGN_LOG_DETAIL
minvalue 1
maxvalue 99999999999
start with 11
increment by 1
cache 10;


--原因（SCM-17947 成果指派新增逻辑，添加指派具体匹配内容） 2018-05-31 ZLL end

--SCM-17965 科研之友个人版这边需要支持SIE机构版的新旧域名  2018-6-7 WSN begin

alter table INS_PORTAL add new_domain varchar2(50);

--SCM-17965 科研之友个人版这边需要支持SIE机构版的新旧域名  2018-6-7 WSN end












--原因（    SCM-17844 新建人员匹配以及同步接口） 2018-05-23 ajb begin


insert into   v_open_token_service_const( id , token , service_type  , descr )  values (  910 , '00000000' , 'gpin9reg',  '人员匹配以及同步接口')  ;

commit ;


--原因（    SCM-17844 新建人员匹配以及同步接口） 2018-05-23 ajb end


--原因（获取成果编目信息 --通用） 2018-05-30 start


insert into   v_open_token_service_const( id , token , service_type  , descr )  values (  911 , '00000000' , 'obc18pub',  '获取成果编目信息 --通用')  ;
commit ;

--原因（获取成果编目信息 --通用） 2018-05-30 ajb  end


--原因（SCM-17904 添加重置密码模板） 2018-06-05 ajb  start

insert into V_MAIL_TEMPLATE values( 10104 , 'Person_Email_Reset_Password_Template' ,'科研之友–重置密码' ,'科研之友–重置密码' ,0 , sysdate ,sysdate ,'', 0 , 0 ) ;

commit ;
--原因（SCM-17904 添加重置密码模板） 2018-06-05 ajb  end
--原因（SCM-18000 邮件模板表优化） 2018-6-7 yhx begin
alter table v_mail_template add(PRIOR_LEVEL VARCHAR2(2 CHAR) default 'D' not null) ;
comment  on  column  v_mail_template.PRIOR_LEVEL is '优先级别';
--原因（SCM-18000 邮件模板表优化） 2018-6-7 yhx end

--原因（    SCM-17994 重置密码邮件》） 2018-06-08 ajb start 

update V_MAIL_TEMPLATE t  set t.mail_type_id =21 where t.template_code = 10104 ;
commit ;

--原因（ SCM-17994 重置密码邮件） 2018-06-08 ajb end

--原因（SCM-17960 成果推荐邮件改变发送策略，每月发送，并使用新邮件模板,增加邮件监控） 2018-06-01 zll begin
-- Alter table 
alter table PUB_ASSIGN_LOG
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table PUB_ASSIGN_LOG add is_send_mail NUMBER(2) default 0;
alter table PUB_ASSIGN_LOG add send_mail_date date;
-- Add comments to the columns 
comment on column PUB_ASSIGN_LOG.is_send_mail
  is '是否发送过邮件 0 未发送;1 发送成功; 2 需要重新发送;-3 人员信息为空;9 发送失败';
comment on column PUB_ASSIGN_LOG.send_mail_date
  is '发送邮件时间';
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
 values(178,'SendPubAssignEmailTaskTrigger','*/10 * * * * ?',0,'成果认领邮件发送');
 
 --原因（SCM-17960 成果推荐邮件改变发送策略，每月发送，并使用新邮件模板,增加邮件监控） 2018-06-01 zll end
 
 --原因（SCM-17960 成果推荐邮件改变发送策略，每月发送，并使用新邮件模板,增加邮件监控） 2018-06-01 zll begin
 update v_mail_template t set t.subject_zh='{0}，你有{1}篇新成果',t.subject_en='{0}，你有{1}篇新成果', t.limit_status=3 where t.template_code=10017;
 --原因（SCM-17960 成果推荐邮件改变发送策略，每月发送，并使用新邮件模板,增加邮件监控） 2018-06-01 zll end

--原因（SCM-18001 基金推荐邮件 增加邮件监控 每周对一个邮箱发一封） 2018-6-8 yhx begin
update v_mail_template t set t.limit_status='2' where t.template_code='10028';
--原因（SCM-18001 基金推荐邮件 增加邮件监控 每周对一个邮箱发一封） 2018-6-8 yhx end

--原因（SCM-18000 邮件模板表优化） 2018-6-8 yhx begin
update v_mail_template t set t.prior_level='A' where t.template_code='10104';
--原因（SCM-18000 邮件模板表优化） 2018-6-8 yhx end

--原因（SCM-18050 成果指派任务，建议对表pub_assign_log_detail 加上字段update_time,方便验证是否更新） 2018-06-15 zll begin
-- Add/modify columns 
alter table PUB_ASSIGN_LOG_DETAIL add update_time date;
-- Add comments to the columns 
comment on column PUB_ASSIGN_LOG_DETAIL.update_time
  is '更新时间';
  
--原因（SCM-18050 成果指派任务，建议对表pub_assign_log_detail 加上字段update_time,方便验证是否更新） 2018-06-15 zll end

--原因（SCM-18023 影响力------阅读人员分布地图） 2018-6-11 ltl begin
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (180, 'updateVistStatisticsTaskTrigger', '*/10 * * * * ?', 1, '更新vist_statistics的PROVINCE_REGION_ID字段');
delete from  RECOMMEND_SCIENCE_AREA t  where t.science_area_id in(1,2,3,4,5,6,7) or status=0;
delete from RECOMMEND_DISCIPLINE_KEY t where t.status=0;
commit;
alter table RECOMMEND_SCIENCE_AREA drop (IDENTIFICATION_SUM);
alter table RECOMMEND_SCIENCE_AREA drop (STATUS);
alter table RECOMMEND_DISCIPLINE_KEY drop (STATUS);
alter table RECOMMEND_DISCIPLINE_KEY drop (REFRESH_FLAG);
alter table RECOMMEND_DISCIPLINE_KEY drop (LAN_TYPE);
alter table RECOMMEND_DISCIPLINE_KEY drop (PSNDIS_ID);
alter table RECOMMEND_DISCIPLINE_KEY drop (KEY_ID);

 alter table vist_statistics add province_region_id number(6);
 create index IDX_PROVINCE_REGION_ID on VIST_STATISTICS (province_region_id);
 
 alter table psn_statistics add visit_sum number default 0;
 update psn_statistics t set t.visit_sum = (select sum(vs.count) from vist_statistics vs where vs.vist_psn_id = t.psn_id)
 where exists(select 1 from vist_statistics vs where vs.vist_psn_id = t.psn_id);
 commit;
--原因（SCM-18023 影响力------阅读人员分布地图） 2018-6-11 ltl end--原因（SCM-18023 影响力------阅读人员分布地图） 2018-6-11 ltl begin
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (181, 'updateVistStatisticsTaskTrigger', '*/10 * * * * ?', 1, '更新vist_statistics的PROVINCE_REGION_ID字段');
--原因（SCM-18023 影响力------阅读人员分布地图） 2018-6-11 ltl end--原因（SCM-10000 模板表中使用新邮件发送方式的模板标记为可用，其他标记为不可用） 2018-6-20 yhx begin
update v_mail_template t set t.status='1',t.update_date=sysdate; 
update v_mail_template t set t.status='0',t.update_date=sysdate where t.template_code in (10104,10032,10033,10028,10017,10097,10098);
--原因（SCM-10000 模板表中使用新邮件发送方式的模板标记为可用，其他标记为不可用） 2018-6-20 yhx end







--原因（    SCM-18097 open接口权限拦截增加逻辑） 2018-06-21 ajb start

-- Alter table 
alter table V_OPEN_TOKEN_SERVICE_CONST
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table V_OPEN_TOKEN_SERVICE_CONST add access_date date default sysdate;
alter table V_OPEN_TOKEN_SERVICE_CONST add access_num NUMBER(8) default 0;
alter table V_OPEN_TOKEN_SERVICE_CONST add access_max_num NUMBER(8) default 1000;
-- Add comments to the columns 
comment on column V_OPEN_TOKEN_SERVICE_CONST.access_date
  is '访问时间，30分钟内不会改变';
comment on column V_OPEN_TOKEN_SERVICE_CONST.access_num
  is '30分钟内访问 ，次数会加一';
comment on column V_OPEN_TOKEN_SERVICE_CONST.access_max_num
  is '30分钟内访问的 最大次数';
  
  
--原因（    SCM-18097 open接口权限拦截增加逻辑） 2018-06-21 ajb end--原因（SCM-18166个人影响力邮件》个人设置》邮件设置中该邮件通知是勾选状态，但是点击邮件中的退订该邮件，却提示退订失败（ 你已经退订了此类邮件），请修改） 2018-6-23 yhx begin
update v_mail_template t set t.mail_type_id='26',t.update_date=sysdate where t.template_code='10017'; 
update v_mail_template t set t.mail_type_id='19',t.update_date=sysdate where t.template_code='10033'; 
update v_mail_template t set t.mail_type_id='17',t.update_date=sysdate where t.template_code='10032'; 
update v_mail_template t set t.mail_type_id='13',t.update_date=sysdate where t.template_code='10028'; 
--原因（SCM-18166个人影响力邮件》个人设置》邮件设置中该邮件通知是勾选状态，但是点击邮件中的退订该邮件，却提示退订失败（ 你已经退订了此类邮件），请修改） 2018-6-23 yhx end--原因（SCM-10000 个人影响力邮件 每周对一个邮箱发一封） 2018-6-25 yhx begin
update v_mail_template t set t.limit_status='2',t.update_date=sysdate where t.template_code='10033';
--原因（SCM-10000 个人影响力邮件 每周对一个邮箱发一封） 2018-6-25 yhx begin

--原因（center-job相关调整） 2018-6-26 hcj begin
drop trigger TRIG_UPDATE_V_JOB_ONLINE;
drop trigger TRIG_UPDATE_JOB_ONLINE_CONFIG;
drop trigger TRIGGER_UPDATE_V_JOB_OFFLINE;

alter table v_job_offline add(FILTER VARCHAR2(255 BYTE));
alter table v_job_offline add(START_TIME DATE);
alter table v_job_offline add(ELAPSED_TIME NUMBER(18,0));
alter table v_job_offline modify(PRIORITY NUMBER(9,0) DEFAULT 99 NOT NULL);

alter table v_job_online add(START_TIME DATE);
alter table v_job_online add(ELAPSED_TIME NUMBER(18,0));

COMMENT ON COLUMN v_job_offline.FILTER IS 'where过滤条件，表字段过滤';
COMMENT ON COLUMN v_job_offline.START_TIME IS '执行开始时间';
COMMENT ON COLUMN v_job_offline.ELAPSED_TIME is '任务执行总耗时，单位：ms';

COMMENT ON COLUMN V_JOB_ONLINE.START_TIME IS '执行开始时间';
COMMENT ON COLUMN V_JOB_ONLINE.ELAPSED_TIME is '任务执行总耗时，单位：ms';

COMMENT ON COLUMN V_JOB_ONLINE.STATUS is '任务执行状态：0-未执行，1-已加载，2-已分配，3-等待中，4-正在执行，5-执行完毕';
COMMENT ON COLUMN V_JOB_OFFLINE.STATUS is '任务执行状态：0-未执行，1-已加载，2-已分配，3-等待中，4-正在执行，5-执行完毕';
--原因（center-job相关调整） 2018-6-26 hcj end
--原因（SCM-17718 从基准库导入的成果，record_from字段标记的不对，请对各基准库的入口进行修改)添加成果认领标识7 2018-6-28 LTL begin
comment on column PUBLICATION.record_from
  is '数据来源：0: 手工输入，1:数据库导入，2:文件导入   3离线导入,4R确认提交,6基准库导入,7认领成果';
--原因（SCM-17718 从基准库导入的成果，record_from字段标记的不对，请对各基准库的入口进行修改)添加成果认领标识7 2018-6-28 LTL end  



--原因（SCM-18240） 2018-6-28 lzx begin
update const_mail_type set type_zh_name=REPLACE(type_zh_name, '好友' ,'联系人');
commit;
--原因（SCM-18240） 2018-6-28 lzx end

--原因（SCM-18283 我的简历》在ie、谷歌、360等浏览器新增教育经历，点击保存提示系统繁忙）ltl begin
alter table PERSON_SYNC modify psn_titolo VARCHAR2(200 CHAR);
--原因（SCM-18283 我的简历》在ie、谷歌、360等浏览器新增教育经历，点击保存提示系统繁忙）ltl end


-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（SCM-17356 微信版，“资助计划”按APP修改，需要基金收藏和基金推荐两个菜单功能） 2016-7-3 LTL begin
delete V_FUND_RECOMMEND_AREA t where t.parent_category_id = 0;
commit;
delete V_FUND_RECOMMEND_AREA m where m.id in(
	select id from (
	       select count(1) mcount, t.psn_id , max(t.id)as id from V_FUND_RECOMMEND_AREA t group by psn_id
	) where mcount>3
);
commit;
delete V_FUND_RECOMMEND_AREA m where m.id in(
	select id from (
	       select count(1) mcount, t.psn_id , max(t.id)as id from V_FUND_RECOMMEND_AREA t group by psn_id
	) where mcount>3
);
commit;
--原因（SCM-17356 微信版，“资助计划”按APP修改，需要基金收藏和基金推荐两个菜单功能） 2016-7-3 LTL end


--原因（SCM-18444 生产机 ，专利数据，专利状态丢失问题） 2018-07-05 zll begin
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
values(188,'HandlePatentHisDataTaskTrigger','*/10 * * * * ?',0,'更新专利状态');
--原因（SCM-18444 生产机 ，专利数据，专利状态丢失问题） 2018-07-05 zll end


--原因（SCM-18395 生产机psn_statistics表中的好友数被清空的问题修复后，没有恢复之前的数据） 2018-07-05 WSN begin
update psn_statistics pst set pst.frd_sum = (select frd_count from (select t.psn_id, count(t.psn_id) as frd_count from psn_friend t group by t.psn_id) f where f.psn_id = pst.psn_id and f.frd_count <> 0)
 where exists (select 1 from (select t.psn_id, count(t.psn_id) as frd_count from psn_friend t group by t.psn_id) f where f.psn_id = pst.psn_id and f.frd_count <> 0);
 commit;
--原因（SCM-18395 生产机psn_statistics表中的好友数被清空的问题修复后，没有恢复之前的数据） 2018-07-05 WSN end


--原因（新建sie专用接口 (根据ins_id获取 对应的token)） 2018-07-10 ajb start
insert into   v_open_token_service_const( id , token , service_type  , descr )  values (  912 , '11111111' , 'yung90kk',  'sie通过insid获取token服务')  ;
commit ;

--原因（新建sie专用接口 (根据ins_id获取 对应的token)） 2018-07-10 ajb end

--SCM-18467 修复psn_statistics表引用数、成果数、项目数、访问数  2018-7-12 wsn start

update psn_statistics pst set pst.pub_sum =
 (select pub_count from (select t.owner_psn_id, count(t.owner_psn_id) as pub_count from v_pub_simple t where t.article_type = 1 and  t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.pub_count <> pst.pub_sum)
where exists (select 1 from (select t.owner_psn_id, count(t.owner_psn_id) as pub_count from v_pub_simple t where t.article_type = 1 and  t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.pub_count <> pst.pub_sum);

commit;


update psn_statistics pst set pst.prj_sum =
 (select prj_count from (select t.owner_psn_id, count(t.owner_psn_id) as prj_count from project t where t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.prj_count <> pst.prj_sum)
where exists (select 1 from (select t.owner_psn_id, count(t.owner_psn_id) as prj_count from project t where t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.prj_count <> pst.prj_sum);

commit;


update psn_statistics pst set pst.visit_sum =
 (select visit_count from (select t.vist_psn_id, sum(t.count) as visit_count from vist_statistics t group by t.vist_psn_id) f where f.vist_psn_id = pst.psn_id and f.visit_count <> pst.visit_sum)
where exists (select 1 from (select t.vist_psn_id, sum(t.count) as visit_count from vist_statistics t group by t.vist_psn_id) f where f.vist_psn_id = pst.psn_id and f.visit_count <> pst.visit_sum);

commit;

update psn_statistics pst set pst.cited_sum =
 (select cited_count from (select t.owner_psn_id, sum(nvl(t.cited_times, 0)) as cited_count from publication t where t.article_type = 1 and t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.cited_count <> pst.cited_sum)
where exists (select 1 from (select t.owner_psn_id, sum(nvl(t.cited_times, 0)) as cited_count from publication t where t.article_type = 1 and t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.cited_count <> pst.cited_sum);

commit;

--SCM-18467 修复psn_statistics表引用数、成果数、项目数、访问数  2018-7-12 wsn end


--原因（SCM-18626 影响力》阅读人员分布地图》数据库中陕西省的英文名称翻译有误，请修改） 2016-7-17 LTL begin
update const_region set en_name='Shanxi' where region_id=610000;
commit;
--原因（SCM-18626 影响力》阅读人员分布地图》数据库中陕西省的英文名称翻译有误，请修改） 2016-7-17 LTL end
--原因（    SCM-18621 论文推荐》科技领域常量表category_map_base，category_zh中有些值前后有空格） 2016-7-17 LTL begin
update category_map_base t set t.category_zh = REGEXP_REPLACE(t.category_zh,'^\s|\s$',''), t.category_en = REGEXP_REPLACE(t.category_en,'^\s|\s$','');
commit;
--原因（ SCM-18621 论文推荐》科技领域常量表category_map_base，category_zh中有些值前后有空格） 2016-7-17 LTL end

--SCM-18692 open系统，新加一个接口给SIE系统调用，传递人员数据到科研之友，供科研之友同步更新对应的人员信息  2018-7-23 WSN begin
insert into v_open_token_service_const values(913, '11111111', 'syncfsie', 0, sysdate, '供SIE调用，同步人员信息到SNS库'， sysdate, 0, 1000);
--SCM-18692 open系统，新加一个接口给SIE系统调用，传递人员数据到科研之友，供科研之友同步更新对应的人员信息  2018-7-23 WSN end

--SCM-17406新增和编辑成果》全文以及附件的名称长度超过200时，应该给出正确提示，目前显示是系统异常，uat有问题，test为正常 2018-7-23 LTL begin
alter table ARCHIVE_FILES modify FILE_NAME VARCHAR2(700)
--SCM-17406新增和编辑成果》全文以及附件的名称长度超过200时，应该给出正确提示，目前显示是系统异常，uat有问题，test为正常 2018-7-23 LTL end

--原因（SCM-18793 新邮件调度优化调整） 2018-7-31 yhx begin

alter table V_MAIL_CLIENT add WAIT_TIME number(4) default 6;

--原因（SCM-18793 新邮件调度优化调整）2018-7-31 yhx end

--原因（SCM-18807 基金推荐邮件-规则修改及邮件标题修改） 2018-7-31 zll begin
Update v_mail_template t Set t.subject_zh='{0}，你有{1}个可能感兴趣的资助机会'
,t.subject_en='{0}，你有{1}个可能感兴趣的资助机会' Where t.template_code=10028
--原因（SCM-18807 基金推荐邮件-规则修改及邮件标题修改） 2018-7-31 zll end


--原因（SCM-17356 微信版，“资助计划”按APP修改，需要基金收藏和基金推荐两个菜单功能） 2016-7-3 LTL begin
delete V_FUND_RECOMMEND_AREA t where t.parent_category_id = 0;
commit;
delete V_FUND_RECOMMEND_AREA m where m.id in(
	select id from (
	       select count(1) mcount, t.psn_id , max(t.id)as id from V_FUND_RECOMMEND_AREA t group by psn_id
	) where mcount>3
);
commit;
delete V_FUND_RECOMMEND_AREA m where m.id in(
	select id from (
	       select count(1) mcount, t.psn_id , max(t.id)as id from V_FUND_RECOMMEND_AREA t group by psn_id
	) where mcount>3
);
commit;
--原因（SCM-17356 微信版，“资助计划”按APP修改，需要基金收藏和基金推荐两个菜单功能） 2016-7-3 LTL end


--原因（SCM-18444 生产机 ，专利数据，专利状态丢失问题） 2018-07-05 zll begin
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
values(188,'HandlePatentHisDataTaskTrigger','*/10 * * * * ?',0,'更新专利状态');
--原因（SCM-18444 生产机 ，专利数据，专利状态丢失问题） 2018-07-05 zll end


--原因（SCM-18395 生产机psn_statistics表中的好友数被清空的问题修复后，没有恢复之前的数据） 2018-07-05 WSN begin
update psn_statistics pst set pst.frd_sum = (select frd_count from (select t.psn_id, count(t.psn_id) as frd_count from psn_friend t group by t.psn_id) f where f.psn_id = pst.psn_id and f.frd_count <> 0)
 where exists (select 1 from (select t.psn_id, count(t.psn_id) as frd_count from psn_friend t group by t.psn_id) f where f.psn_id = pst.psn_id and f.frd_count <> 0);
 commit;
--原因（SCM-18395 生产机psn_statistics表中的好友数被清空的问题修复后，没有恢复之前的数据） 2018-07-05 WSN end


--原因（SCM-17356 微信版，“资助计划”按APP修改，需要基金收藏和基金推荐两个菜单功能） 2016-7-3 LTL begin
delete V_FUND_RECOMMEND_AREA t where t.parent_category_id = 0;
commit;
delete V_FUND_RECOMMEND_AREA m where m.id in(
	select id from (
	       select count(1) mcount, t.psn_id , max(t.id)as id from V_FUND_RECOMMEND_AREA t group by psn_id
	) where mcount>3
);
commit;
delete V_FUND_RECOMMEND_AREA m where m.id in(
	select id from (
	       select count(1) mcount, t.psn_id , max(t.id)as id from V_FUND_RECOMMEND_AREA t group by psn_id
	) where mcount>3
);
commit;
--原因（SCM-17356 微信版，“资助计划”按APP修改，需要基金收藏和基金推荐两个菜单功能） 2016-7-3 LTL end


--原因（SCM-18444 生产机 ，专利数据，专利状态丢失问题） 2018-07-05 zll begin
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
values(188,'HandlePatentHisDataTaskTrigger','*/10 * * * * ?',0,'更新专利状态');
--原因（SCM-18444 生产机 ，专利数据，专利状态丢失问题） 2018-07-05 zll end


--原因（SCM-18395 生产机psn_statistics表中的好友数被清空的问题修复后，没有恢复之前的数据） 2018-07-05 WSN begin
update psn_statistics pst set pst.frd_sum = (select frd_count from (select t.psn_id, count(t.psn_id) as frd_count from psn_friend t group by t.psn_id) f where f.psn_id = pst.psn_id and f.frd_count <> 0)
 where exists (select 1 from (select t.psn_id, count(t.psn_id) as frd_count from psn_friend t group by t.psn_id) f where f.psn_id = pst.psn_id and f.frd_count <> 0);
 commit;
--原因（SCM-18395 生产机psn_statistics表中的好友数被清空的问题修复后，没有恢复之前的数据） 2018-07-05 WSN end


--原因（新建sie专用接口 (根据ins_id获取 对应的token)） 2018-07-10 ajb start
insert into   v_open_token_service_const( id , token , service_type  , descr )  values (  912 , '11111111' , 'yung90kk',  'sie通过insid获取token服务')  ;
commit ;

--原因（新建sie专用接口 (根据ins_id获取 对应的token)） 2018-07-10 ajb end

--SCM-18467 修复psn_statistics表引用数、成果数、项目数、访问数  2018-7-12 wsn start

update psn_statistics pst set pst.pub_sum =
 (select pub_count from (select t.owner_psn_id, count(t.owner_psn_id) as pub_count from v_pub_simple t where t.article_type = 1 and  t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.pub_count <> pst.pub_sum)
where exists (select 1 from (select t.owner_psn_id, count(t.owner_psn_id) as pub_count from v_pub_simple t where t.article_type = 1 and  t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.pub_count <> pst.pub_sum);

commit;


update psn_statistics pst set pst.prj_sum =
 (select prj_count from (select t.owner_psn_id, count(t.owner_psn_id) as prj_count from project t where t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.prj_count <> pst.prj_sum)
where exists (select 1 from (select t.owner_psn_id, count(t.owner_psn_id) as prj_count from project t where t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.prj_count <> pst.prj_sum);

commit;


update psn_statistics pst set pst.visit_sum =
 (select visit_count from (select t.vist_psn_id, sum(t.count) as visit_count from vist_statistics t group by t.vist_psn_id) f where f.vist_psn_id = pst.psn_id and f.visit_count <> pst.visit_sum)
where exists (select 1 from (select t.vist_psn_id, sum(t.count) as visit_count from vist_statistics t group by t.vist_psn_id) f where f.vist_psn_id = pst.psn_id and f.visit_count <> pst.visit_sum);

commit;

update psn_statistics pst set pst.cited_sum =
 (select cited_count from (select t.owner_psn_id, sum(nvl(t.cited_times, 0)) as cited_count from publication t where t.article_type = 1 and t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.cited_count <> pst.cited_sum)
where exists (select 1 from (select t.owner_psn_id, sum(nvl(t.cited_times, 0)) as cited_count from publication t where t.article_type = 1 and t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.cited_count <> pst.cited_sum);

commit;

--SCM-18467 修复psn_statistics表引用数、成果数、项目数、访问数  2018-7-12 wsn end
--原因（SCM-18626 影响力》阅读人员分布地图》数据库中陕西省的英文名称翻译有误，请修改） 2016-7-17 LTL begin
update const_region set en_name='Shanxi' where region_id=610000;
commit;
--原因（SCM-18626 影响力》阅读人员分布地图》数据库中陕西省的英文名称翻译有误，请修改） 2016-7-17 LTL end
--原因（    SCM-18621 论文推荐》科技领域常量表category_map_base，category_zh中有些值前后有空格） 2016-7-17 LTL begin
update category_map_base t set t.category_zh = REGEXP_REPLACE(t.category_zh,'^\s|\s$',''), t.category_en = REGEXP_REPLACE(t.category_en,'^\s|\s$','');
commit;
--原因（ SCM-18621 论文推荐》科技领域常量表category_map_base，category_zh中有些值前后有空格） 2016-7-17 LTL end

--SCM-18692 open系统，新加一个接口给SIE系统调用，传递人员数据到科研之友，供科研之友同步更新对应的人员信息  2018-7-23 WSN begin
insert into v_open_token_service_const values(913, '11111111', 'syncfsie', 0, sysdate, '供SIE调用，同步人员信息到SNS库'， sysdate, 0, 1000);
--SCM-18692 open系统，新加一个接口给SIE系统调用，传递人员数据到科研之友，供科研之友同步更新对应的人员信息  2018-7-23 WSN end

--SCM-17406新增和编辑成果》全文以及附件的名称长度超过200时，应该给出正确提示，目前显示是系统异常，uat有问题，test为正常 2018-7-23 LTL begin
alter table ARCHIVE_FILES modify FILE_NAME VARCHAR2(700)
--SCM-17406新增和编辑成果》全文以及附件的名称长度超过200时，应该给出正确提示，目前显示是系统异常，uat有问题，test为正常 2018-7-23 LTL end

--原因（SCM-18793 新邮件调度优化调整） 2018-7-31 yhx begin

alter table V_MAIL_CLIENT add WAIT_TIME number(4) default 6;

--原因（SCM-18793 新邮件调度优化调整）2018-7-31 yhx end

--原因（SCM-18807 基金推荐邮件-规则修改及邮件标题修改） 2018-7-31 zll begin
Update v_mail_template t Set t.subject_zh='{0}，你有{1}个可能感兴趣的资助机会'
,t.subject_en='{0}，你有{1}个可能感兴趣的资助机会' Where t.template_code=10028
--原因（SCM-18807 基金推荐邮件-规则修改及邮件标题修改） 2018-7-31 zll end


-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（SCM-17356 微信版，“资助计划”按APP修改，需要基金收藏和基金推荐两个菜单功能） 2016-7-3 LTL begin
delete V_FUND_RECOMMEND_AREA t where t.parent_category_id = 0;
commit;
delete V_FUND_RECOMMEND_AREA m where m.id in(
	select id from (
	       select count(1) mcount, t.psn_id , max(t.id)as id from V_FUND_RECOMMEND_AREA t group by psn_id
	) where mcount>3
);
commit;
delete V_FUND_RECOMMEND_AREA m where m.id in(
	select id from (
	       select count(1) mcount, t.psn_id , max(t.id)as id from V_FUND_RECOMMEND_AREA t group by psn_id
	) where mcount>3
);
commit;
--原因（SCM-17356 微信版，“资助计划”按APP修改，需要基金收藏和基金推荐两个菜单功能） 2016-7-3 LTL end


--原因（SCM-18444 生产机 ，专利数据，专利状态丢失问题） 2018-07-05 zll begin
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
values(188,'HandlePatentHisDataTaskTrigger','*/10 * * * * ?',0,'更新专利状态');
--原因（SCM-18444 生产机 ，专利数据，专利状态丢失问题） 2018-07-05 zll end


--原因（SCM-18395 生产机psn_statistics表中的好友数被清空的问题修复后，没有恢复之前的数据） 2018-07-05 WSN begin
update psn_statistics pst set pst.frd_sum = (select frd_count from (select t.psn_id, count(t.psn_id) as frd_count from psn_friend t group by t.psn_id) f where f.psn_id = pst.psn_id and f.frd_count <> 0)
 where exists (select 1 from (select t.psn_id, count(t.psn_id) as frd_count from psn_friend t group by t.psn_id) f where f.psn_id = pst.psn_id and f.frd_count <> 0);
 commit;
--原因（SCM-18395 生产机psn_statistics表中的好友数被清空的问题修复后，没有恢复之前的数据） 2018-07-05 WSN end


--原因（新建sie专用接口 (根据ins_id获取 对应的token)） 2018-07-10 ajb start
insert into   v_open_token_service_const( id , token , service_type  , descr )  values (  912 , '11111111' , 'yung90kk',  'sie通过insid获取token服务')  ;
commit ;

--原因（新建sie专用接口 (根据ins_id获取 对应的token)） 2018-07-10 ajb end

--SCM-18467 修复psn_statistics表引用数、成果数、项目数、访问数  2018-7-12 wsn start

update psn_statistics pst set pst.pub_sum =
 (select pub_count from (select t.owner_psn_id, count(t.owner_psn_id) as pub_count from v_pub_simple t where t.article_type = 1 and  t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.pub_count <> pst.pub_sum)
where exists (select 1 from (select t.owner_psn_id, count(t.owner_psn_id) as pub_count from v_pub_simple t where t.article_type = 1 and  t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.pub_count <> pst.pub_sum);

commit;


update psn_statistics pst set pst.prj_sum =
 (select prj_count from (select t.owner_psn_id, count(t.owner_psn_id) as prj_count from project t where t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.prj_count <> pst.prj_sum)
where exists (select 1 from (select t.owner_psn_id, count(t.owner_psn_id) as prj_count from project t where t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.prj_count <> pst.prj_sum);

commit;


update psn_statistics pst set pst.visit_sum =
 (select visit_count from (select t.vist_psn_id, sum(t.count) as visit_count from vist_statistics t group by t.vist_psn_id) f where f.vist_psn_id = pst.psn_id and f.visit_count <> pst.visit_sum)
where exists (select 1 from (select t.vist_psn_id, sum(t.count) as visit_count from vist_statistics t group by t.vist_psn_id) f where f.vist_psn_id = pst.psn_id and f.visit_count <> pst.visit_sum);

commit;

update psn_statistics pst set pst.cited_sum =
 (select cited_count from (select t.owner_psn_id, sum(nvl(t.cited_times, 0)) as cited_count from publication t where t.article_type = 1 and t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.cited_count <> pst.cited_sum)
where exists (select 1 from (select t.owner_psn_id, sum(nvl(t.cited_times, 0)) as cited_count from publication t where t.article_type = 1 and t.status = 0 group by t.owner_psn_id) f where f.owner_psn_id = pst.psn_id and f.cited_count <> pst.cited_sum);

commit;

--SCM-18467 修复psn_statistics表引用数、成果数、项目数、访问数  2018-7-12 wsn end


--原因（SCM-18626 影响力》阅读人员分布地图》数据库中陕西省的英文名称翻译有误，请修改） 2016-7-17 LTL begin
update const_region set en_name='Shanxi' where region_id=610000;
commit;
--原因（SCM-18626 影响力》阅读人员分布地图》数据库中陕西省的英文名称翻译有误，请修改） 2016-7-17 LTL end
--原因（    SCM-18621 论文推荐》科技领域常量表category_map_base，category_zh中有些值前后有空格） 2016-7-17 LTL begin
update category_map_base t set t.category_zh = REGEXP_REPLACE(t.category_zh,'^\s|\s$',''), t.category_en = REGEXP_REPLACE(t.category_en,'^\s|\s$','');
commit;
--原因（ SCM-18621 论文推荐》科技领域常量表category_map_base，category_zh中有些值前后有空格） 2016-7-17 LTL end

--SCM-18692 open系统，新加一个接口给SIE系统调用，传递人员数据到科研之友，供科研之友同步更新对应的人员信息  2018-7-23 WSN begin
insert into v_open_token_service_const values(913, '11111111', 'syncfsie', 0, sysdate, '供SIE调用，同步人员信息到SNS库'， sysdate, 0, 1000);
--SCM-18692 open系统，新加一个接口给SIE系统调用，传递人员数据到科研之友，供科研之友同步更新对应的人员信息  2018-7-23 WSN end

--SCM-17406新增和编辑成果》全文以及附件的名称长度超过200时，应该给出正确提示，目前显示是系统异常，uat有问题，test为正常 2018-7-23 LTL begin
alter table ARCHIVE_FILES modify FILE_NAME VARCHAR2(700)
--SCM-17406新增和编辑成果》全文以及附件的名称长度超过200时，应该给出正确提示，目前显示是系统异常，uat有问题，test为正常 2018-7-23 LTL end

--原因（SCM-18793 新邮件调度优化调整） 2018-7-31 yhx begin

alter table V_MAIL_CLIENT add WAIT_TIME number(4) default 6;

--原因（SCM-18793 新邮件调度优化调整）2018-7-31 yhx end

--原因（SCM-18807 基金推荐邮件-规则修改及邮件标题修改） 2018-7-31 zll begin
Update v_mail_template t Set t.subject_zh='{0}，你有{1}个可能感兴趣的资助机会'
,t.subject_en='{0}，你有{1}个可能感兴趣的资助机会' Where t.template_code=10028
--原因（SCM-18807 基金推荐邮件-规则修改及邮件标题修改） 2018-7-31 zll end

--原因:提交v8pub初始化数据任务  2018-8-28 YJ begin
insert into v_Quartz_Cron_Expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (401, 'PubSnsDataInitTaskTrigger', '*/10 * * * * ?', 0, '个人库成果初始化任务-yj')
insert into v_Quartz_Cron_Expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (402, 'PubPdwhDataInitTaskTrigger', '*/10 * * * * ?', 0, '基准库数据初始化任务-yj');
--原因:提交v8pub初始化数据任务   2018-8-28 YJ begin

--原因（SCM-18999 uat，登录状态下，点击tempcode=10075,10104,10073的邮件模板页脚的‘退订’链接，进入老系统页面）yhx begin
insert into const_mail_type_template_rel(rel_id,type_id,template_id) values(223,20,252);
insert into const_mail_type_template_rel(rel_id,type_id,template_id) values(222,21,250);
insert into const_mail_type_template_rel(rel_id,type_id,template_id) values(221,23,97);
insert into const_mail_type_template_rel(rel_id,type_id,template_id) values(220,23,96);
--原因（SCM-18999 uat，登录状态下，点击tempcode=10075,10104,10073的邮件模板页脚的‘退订’链接，进入老系统页面）yhx end
