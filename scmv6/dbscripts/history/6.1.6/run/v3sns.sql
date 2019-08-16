-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end
--原因:SCM-22669 项目-因prj_group_relation表还有旧数据原因，导致项目列表已删除群组的项目还显示“进入群组”，应清理这些数据 2019-1-15 YWL begin
delete from prj_group_relation p where p.group_id in (select v.grp_id from v_grp_baseinfo v where v.status = 99 )；
delete from prj_group_relation where exists (select 1 from project where status = 1)；
--原因:SCM-22669 项目-因prj_group_relation表还有旧数据原因，导致项目列表已删除群组的项目还显示“进入群组”，应清理这些数据 2019-1-15 YWL end

--原因（SCM-22671 我的文件》分享文件给站外的邮箱账号，查看分享记录，显示被分享人为空） 2019-1-17 YHX begin
alter table V_PSN_FILE_SHARE_RECORD add outside_email VARCHAR2(50 CHAR);
comment on column V_PSN_FILE_SHARE_RECORD.outside_email
  is '站外邮箱';
--原因（SCM-22671 我的文件》分享文件给站外的邮箱账号，查看分享记录，显示被分享人为空） 2019-1-17 YHX end
--原因:SCM-22477 访问表vist_statistic表中统计数与界面的阅读数不一致 ，执行更新sql将psn_statistics表中的阅读数与表vist_statistics中进行同步 2019-1-18 SYL begin
update psn_statistics psn
   set psn.visit_sum =
       (select sum(t.count)
          from vist_statistics t
         where t.vist_psn_id = psn.psn_id)
 where exists
 (select 1 from vist_statistics vi where vi.vist_psn_id = psn.psn_id);
 commit;
--原因:SCM-22477 访问表vist_statistic表中统计数与界面的阅读数不一致 ，执行更新sql将psn_statistics表中的阅读数与表vist_statistics中进行同步 2019-1-18 SYL end-- 原因 （邮件模板表新增template_language字段）2019-1-18 YHX begin
alter table v_mail_template add template_language NUMBER(2) default 0 not null;
comment on column v_mail_template.template_language
  is '模板语言：0.中英文模板都有，1.只有中文模板';
-- 原因 （邮件模板表新增template_language字段）2019-1-18 YHX end
-- 原因 （将只有中文模板的邮件模板template_language设置为1）2019-1-21 YHX begin
update v_mail_template t set t.template_language=1,t.update_date=sysdate where t.template_code in(10045,10058,10113);
-- 原因 （将只有中文模板的邮件模板template_language设置为1）2019-1-21 YHX end

 

--SCM-21761 安卓版本科研之友应用程序开发，版本管理  2019-1-25 WSN begin 

alter table v_mobile_app_version add status number(1);

update v_mobile_app_version t set t.status = 1;

commit;

alter table v_mobile_app_version modify (status number(1) not null);

--SCM-21761 安卓版本科研之友应用程序开发，版本管理  2019-1-25 WSN end 



--原因（    SCM-22720 成果录入 文件导入） 2019-01-28 ajb start
-- Create table
create table V_PUB_FILE_RELATION
(
  pub_id  number(18) not null,
  file_id number(18) not null
)
;
-- Add comments to the table
comment on table V_PUB_FILE_RELATION
  is '成果文件关联表';

-- Add comments to the columns
comment on column V_PUB_FILE_RELATION.PUB_ID
  is '成果id 主键';
comment on column V_PUB_FILE_RELATION.FILE_ID
  is '文件id';
-- Create/Recreate primary, unique and foreign key constraints
alter table V_PUB_FILE_RELATION
  add constraint PK_V_PUB_FILE_RELATION primary key (PUB_ID);

commit ;
--原因（    SCM-22720 成果录入 文件导入） 2019-01-28 ajb start

--原因（SCM-22722 成果更新保存 记录历史版本） 2019-1-25 YHX begin
alter table v_pub_sns add version number(18);
comment on column v_pub_sns.version is '最新修改的版本号';
--原因（SCM-22722 成果更新保存 记录历史版本） 2019-1-25 YHX end





--原因（ROL-6030-邮件模板） 2019-02-11 YXY begin
update v_mail_template t set t.template_language=1 where t.template_code in (10117,10116,10115,10114,10111,10110,10085,10068,10056,10047,10046);
--原因（ROL-6030-邮件模板） 2019-02-11 YXY end

--原因（SCM-23061 群组-v_group_dynamic_msg表的crente_date字段命名有误，应该是create_date） 2019-02-14 YHX begin
alter table v_group_dynamic_msg rename column CRENTE_DATE to CREATE_DATE;
--原因（SCM-23061 群组-v_group_dynamic_msg表的crente_date字段命名有误，应该是create_date） 2019-02-14 YHX end



--原因（ROL-6030-邮件模板） 2019-02-11 YXY begin
update v_mail_template t set t.template_language=1 where t.template_code in (10117,10116,10115,10114,10111,10110,10085,10068,10056,10047,10046);
--原因（ROL-6030-邮件模板） 2019-02-11 YXY end
--原因  SCM-18949成果认领后，通知合作者的邮件模板，需要重新修改为可用 2019-2-20 HHT start
update v_mail_template t set t.subject_zh='{0}，{1}认领了论文，你可能是合作者。',t.subject_en='{0},{1} uploaded following publication, are you an co-author?',
t.status=0,t.limit_status=1 where t.template_code=10021;
--原因  SCM-18949成果认领后，通知合作者的邮件模板，需要重新修改为可用 2019-2-20 HHT startupdate v_mail_template t set t.mail_type_id=25 where t.template_code=10021;
--原因  SCM-18949成果认领后，通知合作者的邮件模板，需要重新修改为可用 2019-2-20 HHT start
--原因 SCM-22994 解决个人项目表中的项目数量和PSN_STATISTICS表中的prj_sum字段对应的项目数不一致的问题 2019-2-20 SYL start
update PSN_STATISTICS t1
   set t1.prj_sum =
       (select count(1)
          from PROJECT t2
         where t2.owner_psn_id = t1.psn_id
           and status = 0)
 where exists (select 1 from PROJECT t2 where t2.owner_psn_id = t1.psn_id);
 commit;
--原因 SCM-22994 解决个人项目表中的项目数量和PSN_STATISTICS表中的prj_sum字段对应的项目数不一致的问题 2019-2-20 SYL end
--原因（SCM-23331 成果指派pub_assign_log 表新增一个更新时间字段，重新指派更新只更新原纪录，不重新产生一条记录） 2019-02-26 zll begin

-- Alter table 
alter table PUB_ASSIGN_LOG
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table PUB_ASSIGN_LOG add update_date date;
-- Add comments to the columns 
comment on column PUB_ASSIGN_LOG.update_date
  is '更新时间';
  --原因（SCM-23331 成果指派pub_assign_log 表新增一个更新时间字段，重新指派更新只更新原纪录，不重新产生一条记录） 2019-02-26 zll end


--原因  2018-2-27  SCM-23181 uat，有个新注册的账号，在基金推荐设置栏，默认选了2次同样的科技领域 ltl begin
delete  from v_fund_recommend_area  a  
where a.rowid !=  
(  
select max(b.rowid) from v_fund_recommend_area b  
where a.science_area_id = b.science_area_id and  
a.psn_id = b.psn_id  
);
commit;
alter table V_FUND_RECOMMEND_AREA
  add constraint PK_FUND_RECOMMEND_PSNID_AREA unique (PSN_ID, SCIENCE_AREA_ID);
--原因  2018-2-27  SCM-23181 uat，有个新注册的账号，在基金推荐设置栏，默认选了2次同样的科技领域 ltl end 
--原因（ROL-6408-动态/ROL-6430-同步动态后台任务） 2019-02-28 YXY begin

insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (10110, 'sieSyncDynMsgTaskTriggers', '0 0 0/1 * * ? ', 1, '同步Sns动态信息到Sie库中');


--原因（ROL-6408-动态/ROL-6430-同步动态后台任务） 2019-02-28 YXY end

--原因 ：SCM-23247 个人库成果全文的数据存在一部分权限为1的数据，现在将其重新设置权限 2019-3-1 SYL begin

update v_pub_fulltext vpf
   set vpf.permission = 0
 where exists (select 1
          from psn_config_pub p
         where vpf.pub_id = p.pub_id
           and vpf.permission = 1
           and p.any_user  in (1, 7));
           
update v_pub_fulltext vpf
   set vpf.permission = 2
 where exists (select 1
          from psn_config_pub p
         where vpf.pub_id = p.pub_id
           and vpf.permission = 1
           and p.any_user not in (1, 7));
           
update v_pub_fulltext vpf set vpf.permission = 0 where vpf.permission=1;
commit;
--原因 ：SCM-23247 个人库成果全文的数据存在一部分权限为1的数据，现在将其重新设置权限 2019-3-1 SYL end



--原因（ROL-6407  引导） 2019-03-04  ZTG begin
INSERT INTO v_quartz_cron_expression(id, cron_trigger_bean, cron_expression, status, description)
VALUES (10111, 'sieGenerateUserGuidanceTaskTrigger', '*/10 * * * * ?', 0, '生成二次引导的后台任务');
--原因（有CQ号带上CQ号） 2019-03-04  ZTG end

--原因 scm-0000 2019-03-05  HHT begin
insert into v_quartz_cron_expression (id,cron_trigger_bean,cron_expression,status,description) values(1935,'ShortUrlGeneratorSiteMapTaskTrigger','0 0 19 26 * ?',1,'每月26号19点执行把最近一个月短地址生成sitemap任务');
--原因 scm-0000 2019-03-05  HHT end

-
--运行定时任务,由于每个库的任务号不一致,所以需要手动运行定时任务
--新增工作经历拼音和首字母提醒功能,以及按机构人数排序  2019-1-21 xiexing end

--给单位关注的基金机构和基金机会冗余和缺失数据处理后台任务做定时器时间配置（ROL-6054） 2019-03-07 LJM begin
insert into V_QUARTZ_CRON_EXPRESSION(ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
    values(10112, 'sieSynBpoGrantInfoTaskTrigger', '0/10 * * * * ?', 0, '清除资助机构和资助机会关注业务表的冗余数据和缺失数据（0 0 0 1/15 * ?）');
--给单位关注的基金机构和基金机会冗余和缺失数据处理后台任务做定时器时间配置（ROL-6054） 2019-03-07 LJM end


--原因（ROL-6430-同步动态后台任务） 2019-03-11 YXY begin
update v_quartz_cron_expression t set t.description='同步Sns动态信息到Sie库中(每小时一次0 0 0/1 * * ?)' where t.id=10110;
--原因（ROL-6430-同步动态后台任务） 2019-03-11 YXY end

--SCM-23503 SEO- 成果新增页面需要同时更新 2019-3-11 HHT begin
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) values(1936,'PdwhPubUpdateImportPublicationTaskTrigger','*/2 * * * * ?',0,'基准库成果更新之后导入pdwh_index_publication任务');
--备份 pub_index_second_level 
 ALTER TABLE pub_index_second_level rename to  pub_index_second_level_bak; 
  commit;
--创建新的 pub_index_second_level 
create table pub_index_second_level as select * from pub_index_second_level_bak;
delete from  pub_index_second_level;
commit;

--删除dbid
alter table pub_index_second_level  drop column dbid ;
commit;
--SCM-23503 SEO- 成果新增页面需要同时更新 2019-3-11 HHT end


--原因（ROL-6407  引导） 2019-03-12  ZTG begin
UPDATE v_quartz_cron_expression t SET t.description = '生成二次引导的任务(0 0 1 ? * 1 *), 每周日凌晨1点触发' WHERE t.id = 10111;
--原因（ROL-6407  引导） 2019-03-12  ZTG end

--原因（SCM-23700  微信关联检查） 2019-03-13 ajb begin
 insert into  v_open_token_service_const (id , token,service_type,descr) values (2010  , '00000000' ,'yjhiu8rr','微信关联检查') ;
COMMIT  ;
--原因（SCM-23700  微信关联检查） 2019-03-13 ajb end
--原因（SCM-23792 需要发送成果认领的收件人改成从中间表获取） 2019-03-14 zll begin
-- Create table
create table v_send_mail_psn_log
(
  id          number(18) not null,
  psn_id      number(18),
  send_status number(10) default 0
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
comment on table v_send_mail_psn_log
  is '邮件发送人员记录表';
-- Add comments to the columns 
comment on column v_send_mail_psn_log.id
  is '主键';
comment on column v_send_mail_psn_log.psn_id
  is '收件人';
comment on column v_send_mail_psn_log.send_status
  is '发送状态';
-- Create/Recreate primary, unique and foreign key constraints 
alter table v_send_mail_psn_log
  add constraint pk_send_mail_psn_log unique (ID);
  
  
create sequence SEQ_SEND_MAIL_PSN_LOG
minvalue 1
maxvalue 9999999999
start with 11
increment by 1
cache 10;

--原因（SCM-23792 需要发送成果认领的收件人改成从中间表获取） 2019-03-14 zll end




--原因（ROL-6626  引导，二级引导生成方法调整） 2019-03-14  ZTG begin 
DELETE FROM v_quartz_cron_expression t WHERE t.id = 10111; 
--原因（ROL-6626  引导，二级引导生成方法调整） 2019-03-14  ZTG end
--SCM-23503 SEO- 成果新增页面需要同时更新 2019-03-15  HHT begin 
--清空表
Truncate table PUB_INDEX_SECOND_LEVEL;
commit;
-- Alter table 
alter table PUB_INDEX_SECOND_LEVEL
  storage
  (
    next 8
  )
;
-- Drop columns 
alter table PUB_INDEX_SECOND_LEVEL drop column title_type;
-- Create/Recreate primary, unique and foreign key constraints 
alter table PUB_INDEX_SECOND_LEVEL
  drop constraint PK_INDEX_SECOND_LEVEL cascade;
alter table PUB_INDEX_SECOND_LEVEL
  add constraint PK_INDEX_SECOND_LEVEL primary key (PUB_ID)
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
--SCM-23503 SEO- 成果新增页面需要同时更新 2019-03-15  HHT end --SCM-23503 SEO- 成果新增页面需要同时更新 2019-03-15  HHT begin 
truncate table pub_index_first_level;
--SCM-23503 SEO- 成果新增页面需要同时更新 2019-03-15  HHT end 
--原因（ROL-6601-行为分析架构分析任务） 2019-03-19 YXY begin
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (10114, 'sieAuditLogTaskTriggers', '0/10 * * * * ? ', 0, 'SIE分析切面记录日志（0/10 * * * * ? ）');
--原因（ROL-6601-行为分析架构分析任务） 2019-03-19 YXY end

--原因SCM-23772：个人主页构建任务由于使用到旧表，导致一部分之前的数据需要重新跑任务 2019-3-21 SYL begin

 update V_BATCH_JOBS t set t.status=0 where t.strategy='rpcxit26' and t.status=3;

--原因SCM-23772：个人主页构建任务由于使用到旧表，导致一部分之前的数据需要重新跑任务 2019-3-21 SYL end

--原因（SCM-23906 成果相关统计数初始化。） 2019-3-21 YHX begin
create table v_pub_pdwh_sns_relation_bak as select * from v_pub_pdwh_sns_relation;
alter table v_pub_pdwh_sns_relation_bak add(STATUS NUMBER(2) default 0);
comment on column v_pub_pdwh_sns_relation_bak.status is '任务状态：0.待执行，1.正常，99.失败';
alter table v_pub_pdwh_sns_relation_bak add(err_msg clob);
comment on column v_pub_pdwh_sns_relation_bak.err_msg is '错误信息';

create table v_pub_pdwh_sns_relation_bak_c as select * from v_pub_pdwh_sns_relation;
alter table v_pub_pdwh_sns_relation_bak_c add(STATUS NUMBER(2) default 0);
comment on column v_pub_pdwh_sns_relation_bak_c.status is '任务状态：0.待执行，1.正常，99.失败';
alter table v_pub_pdwh_sns_relation_bak_c add(err_msg clob);
comment on column v_pub_pdwh_sns_relation_bak_c.err_msg is '错误信息';

create table v_pub_pdwh_sns_relation_bak_s as select * from v_pub_pdwh_sns_relation;
alter table v_pub_pdwh_sns_relation_bak_s add(STATUS NUMBER(2) default 0);
comment on column v_pub_pdwh_sns_relation_bak_s.status is '任务状态：0.待执行，1.正常，99.失败';
alter table v_pub_pdwh_sns_relation_bak_s add(err_msg clob);
comment on column v_pub_pdwh_sns_relation_bak_s.err_msg is '错误信息';

insert into v_Quartz_Cron_Expression(Id,Cron_Trigger_Bean,Cron_Expression,status,Description) values (10116,'SynSnsPdwhRelationLikeTaskTrigger','0/10 * * * * ?',0,'关联成果个人库基准库赞记录同步');
insert into v_Quartz_Cron_Expression(Id,Cron_Trigger_Bean,Cron_Expression,status,Description) values (10117,'SynSnsPdwhRelationCommentTaskTrigger','0/10 * * * * ?',0,'关联成果个人库基准库评论记录同步');
insert into v_Quartz_Cron_Expression(Id,Cron_Trigger_Bean,Cron_Expression,status,Description) values (10118,'SynSnsPdwhRelationShareTaskTrigger','0/10 * * * * ?',0,'关联成果个人库基准库分享记录同步');
--原因（SCM-23906 成果相关统计数初始化。） 2019-3-21 YHX end





--原因:新增工作经历拼音和首字母提醒功能,以及按机构人数排序(SCM-22700)  2019-1-21 xiexing begin
--删除多于函数
drop function fn_getpy;
--Oracle汉字转拼音和首字母的函数
CREATE OR REPLACE FUNCTION fn_getpy (p_str    IN VARCHAR2,
                                     p_flag      NUMBER DEFAULT NULL)
   RETURN VARCHAR2
AS
   v_compare   VARCHAR2 (4);
   v_return    VARCHAR2 (4000);
   v_length    INT := 0;
   v_substr    VARCHAR2 (4);
 
   FUNCTION fn_nlssort (p_word IN VARCHAR2)
      RETURN VARCHAR2
   AS
   BEGIN
      RETURN SUBSTR (NLSSORT (p_word, 'nls_sort=schinese_pinyin_m'), 1, 4);
   END fn_nlssort;
BEGIN
   IF p_str IS NULL
   THEN
      RETURN '';
   END IF;
 
   v_length := LENGTH (p_str);
 
   CASE p_flag
      WHEN 1
      THEN                                                              --全拼大写
         FOR i IN 1 .. v_length
         LOOP
            v_substr := SUBSTR (p_str, i, 1);
            v_compare := fn_nlssort (v_substr);
 
            CASE
               WHEN v_compare BETWEEN '3B29' AND '3B30'
               THEN
                  v_return := v_return || 'A';
               WHEN v_compare = '3B31'
               THEN
                  v_return := v_return || 'AES';
               WHEN v_compare BETWEEN '3B32' AND '3B9E'
               THEN
                  v_return := v_return || 'AI';
               WHEN v_compare BETWEEN '3BA0' AND '3BFE'
               THEN
                  v_return := v_return || 'AN';
               WHEN v_compare BETWEEN '3C01' AND '3C14'
               THEN
                  v_return := v_return || 'ANG';
               WHEN v_compare BETWEEN '3C15' AND '3C82'
               THEN
                  v_return := v_return || 'AO';
               WHEN v_compare BETWEEN '3C84' AND '3CE9'
               THEN
                  v_return := v_return || 'BA';
               WHEN v_compare BETWEEN '3CED' AND '3D1D'
               THEN
                  v_return := v_return || 'BAI';
               WHEN v_compare BETWEEN '3D20' AND '3D64'
               THEN
                  v_return := v_return || 'BAN';
               WHEN v_compare BETWEEN '3D66' AND '3DA2'
               THEN
                  v_return := v_return || 'BANG';
               WHEN v_compare BETWEEN '3DA4' AND '3E10'
               THEN
                  v_return := v_return || 'BAO';
               WHEN v_compare = '3E11'
               THEN
                  v_return := v_return || 'BE';
               WHEN v_compare BETWEEN '3E12' AND '3E7A'
               THEN
                  v_return := v_return || 'BEI';
               WHEN v_compare BETWEEN '3E7C' AND '3EA0'
               THEN
                  v_return := v_return || 'BEN';
               WHEN v_compare BETWEEN '3EA1' AND '3ED5'
               THEN
                  v_return := v_return || 'BENG';
               WHEN v_compare BETWEEN '3ED8' AND '3FE9'
               THEN
                  v_return := v_return || 'BI';
               WHEN v_compare BETWEEN '3FEA' AND '4055'
               THEN
                  v_return := v_return || 'BIAN';
               WHEN v_compare BETWEEN '4058' AND '40AE'
               THEN
                  v_return := v_return || 'BIAO';
               WHEN v_compare = '4060'
               THEN
                  v_return := v_return || 'BIA';
               WHEN v_compare BETWEEN '40B4' AND '40D4'
               THEN
                  v_return := v_return || 'BIE';
               WHEN v_compare BETWEEN '40D6' AND '4116'
               THEN
                  v_return := v_return || 'BIN';
               WHEN v_compare BETWEEN '4118' AND '4160'
               THEN
                  v_return := v_return || 'BING';
               WHEN v_compare BETWEEN '4161' AND '4224'
               THEN
                  v_return := v_return || 'BO';
               WHEN v_compare BETWEEN '4225' AND '427C'
               THEN
                  v_return := v_return || 'BU';
               WHEN v_compare BETWEEN '427D' AND '4289'
               THEN
                  v_return := v_return || 'CA';
               WHEN v_compare BETWEEN '428C' AND '42B5'
               THEN
                  v_return := v_return || 'CAI';
               WHEN v_compare BETWEEN '42B9' AND '430C'
               THEN
                  v_return := v_return || 'CAN';
               WHEN v_compare BETWEEN '430D' AND '4334'
               THEN
                  v_return := v_return || 'CANG';
               WHEN v_compare BETWEEN '4335' AND '435C'
               THEN
                  v_return := v_return || 'CAO';
               WHEN v_compare BETWEEN '435D' AND '438C'
               THEN
                  v_return := v_return || 'CE';
               WHEN v_compare BETWEEN '4390' AND '4398'
               THEN
                  v_return := v_return || 'CEN';
               WHEN v_compare BETWEEN '439D' AND '43AA'
               THEN
                  v_return := v_return || 'CENG';
               WHEN v_compare = '43AC'
               THEN
                  v_return := v_return || 'CEOK';
               WHEN v_compare = '43AE'
               THEN
                  v_return := v_return || 'CEOM';
               WHEN v_compare = '43B0'
               THEN
                  v_return := v_return || 'CEON';
               WHEN v_compare = '43B1'
               THEN
                  v_return := v_return || 'CEOR';
               WHEN v_compare BETWEEN '43B2' AND '440A'
               THEN
                  v_return := v_return || 'CHA';
               WHEN v_compare BETWEEN '440E' AND '442D'
               THEN
                  v_return := v_return || 'CHAI';
               WHEN v_compare BETWEEN '4431' AND '44E1'
               THEN
                  v_return := v_return || 'CHAN';
               WHEN v_compare BETWEEN '44E4' AND '4552'
               THEN
                  v_return := v_return || 'CHANG';
               WHEN v_compare BETWEEN '4554' AND '458E'
               THEN
                  v_return := v_return || 'CHAO';
               WHEN v_compare BETWEEN '4590' AND '45C8'
               THEN
                  v_return := v_return || 'CHE';
               WHEN v_compare BETWEEN '45C9' AND '463D'
               THEN
                  v_return := v_return || 'CHEN';
               WHEN v_compare BETWEEN '463E' AND '46CD'
               THEN
                  v_return := v_return || 'CHENG';
               WHEN v_compare BETWEEN '46CE' AND '47A6'
               THEN
                  v_return := v_return || 'CHI';
               WHEN v_compare BETWEEN '47A8' AND '47EC'
               THEN
                  v_return := v_return || 'CHONG';
               WHEN v_compare BETWEEN '47ED' AND '484C'
               THEN
                  v_return := v_return || 'CHOU';
               WHEN v_compare BETWEEN '484D' AND '48E2'
               THEN
                  v_return := v_return || 'CHU';
               WHEN v_compare BETWEEN '48E9' AND '48F4'
               THEN
                  v_return := v_return || 'CHUAI';
               WHEN v_compare BETWEEN '48F6' AND '4924'
               THEN
                  v_return := v_return || 'CHUAN';
               WHEN v_compare BETWEEN '4925' AND '4951'
               THEN
                  v_return := v_return || 'CHUANG';
               WHEN v_compare BETWEEN '4954' AND '496E'
               THEN
                  v_return := v_return || 'CHUI';
               WHEN v_compare BETWEEN '4971' AND '49C6'
               THEN
                  v_return := v_return || 'CHUN';
               WHEN v_compare BETWEEN '49C8' AND '49EA'
               THEN
                  v_return := v_return || 'CHUO';
               WHEN v_compare BETWEEN '49EC' AND '4A4A'
               THEN
                  v_return := v_return || 'CI';
               WHEN v_compare = '4A50'
               THEN
                  v_return := v_return || 'CIS';
               WHEN v_compare BETWEEN '4A51' AND '4AB2'
               THEN
                  v_return := v_return || 'CONG';
               WHEN v_compare BETWEEN '4AB4' AND '4ABA'
               THEN
                  v_return := v_return || 'COU';
               WHEN v_compare BETWEEN '4ABC' AND '4AEA'
               THEN
                  v_return := v_return || 'CU';
               WHEN v_compare BETWEEN '4AEE' AND '4B0C'
               THEN
                  v_return := v_return || 'CUAN';
               WHEN v_compare BETWEEN '4B0D' AND '4B56'
               THEN
                  v_return := v_return || 'CUI';
               WHEN v_compare BETWEEN '4B59' AND '4B6C'
               THEN
                  v_return := v_return || 'CUN';
               WHEN v_compare BETWEEN '4B70' AND '4BA9'
               THEN
                  v_return := v_return || 'CUO';
               WHEN v_compare BETWEEN '4BAD' AND '4BFE'
               THEN
                  v_return := v_return || 'DA';
               WHEN v_compare BETWEEN '4C00' AND '4C4E'
               THEN
                  v_return := v_return || 'DAI';
               WHEN v_compare BETWEEN '4C50' AND '4CDC'
               THEN
                  v_return := v_return || 'DAN';
               WHEN v_compare BETWEEN '4CDE' AND '4D26'
               THEN
                  v_return := v_return || 'DANG';
               WHEN v_compare BETWEEN '4D28' AND '4D76'
               THEN
                  v_return := v_return || 'DAO';
               WHEN v_compare BETWEEN '4D7E' AND '4D8D'
               THEN
                  v_return := v_return || 'DE';
               WHEN v_compare = '4D8E'
               THEN
                  v_return := v_return || 'DEM';
               WHEN v_compare BETWEEN '4D90' AND '4D91'
               THEN
                  v_return := v_return || 'DEN';
               WHEN v_compare BETWEEN '4D94' AND '4DC0'
               THEN
                  v_return := v_return || 'DENG';
               WHEN v_compare BETWEEN '4DC4' AND '4E8A'
               THEN
                  v_return := v_return || 'DI';
               WHEN v_compare = '4E8C'
               THEN
                  v_return := v_return || 'DIA';
               WHEN v_compare BETWEEN '4E8D' AND '4EE8'
               THEN
                  v_return := v_return || 'DIAN';
               WHEN v_compare BETWEEN '4EE9' AND '4F38'
               THEN
                  v_return := v_return || 'DIAO';
               WHEN v_compare BETWEEN '4F39' AND '4F90'
               THEN
                  v_return := v_return || 'DIE';
               WHEN v_compare = '4F8D'
               THEN
                  v_return := v_return || 'DEI';
               WHEN v_compare = '4F91'
               THEN
                  v_return := v_return || 'DIM';
               WHEN v_compare BETWEEN '4F92' AND '4FCD'
               THEN
                  v_return := v_return || 'DING';
               WHEN v_compare BETWEEN '4FCD' AND '4FD4'
               THEN
                  v_return := v_return || 'DIU';
               WHEN v_compare BETWEEN '4FD5' AND '5032'
               THEN
                  v_return := v_return || 'DONG';
               WHEN v_compare BETWEEN '5034' AND '507C'
               THEN
                  v_return := v_return || 'DOU';
               WHEN v_compare = '5044'
               THEN
                  v_return := v_return || 'DUL';
               WHEN v_compare BETWEEN '507E' AND '50E9'
               THEN
                  v_return := v_return || 'DU';
               WHEN v_compare BETWEEN '50EA' AND '5110'
               THEN
                  v_return := v_return || 'DUAN';
               WHEN v_compare BETWEEN '5114' AND '514E'
               THEN
                  v_return := v_return || 'DUI';
               WHEN v_compare BETWEEN '5152' AND '518D'
               THEN
                  v_return := v_return || 'DUN';
               WHEN v_compare = '5160'
               THEN
                  v_return := v_return || 'TON';
               WHEN v_compare BETWEEN '518E' AND '5200'
               THEN
                  v_return := v_return || 'DUO';
               WHEN v_compare BETWEEN '5205' AND '52C2'
               THEN
                  v_return := v_return || 'E';
               WHEN v_compare BETWEEN '52C4' AND '52CD'
               THEN
                  v_return := v_return || 'EN';
               WHEN v_compare = '52D4'
               THEN
                  v_return := v_return || 'ENG';
               WHEN v_compare = '52D5'
               THEN
                  v_return := v_return || 'EO';
               WHEN v_compare = '52D6'
               THEN
                  v_return := v_return || 'EOL';
               WHEN v_compare = '52D8'
               THEN
                  v_return := v_return || 'EOS';
               WHEN v_compare BETWEEN '52D9' AND '5332'
               THEN
                  v_return := v_return || 'ER';
               WHEN v_compare BETWEEN '5334' AND '5366'
               THEN
                  v_return := v_return || 'FA';
               WHEN v_compare BETWEEN '536A' AND '53FA'
               THEN
                  v_return := v_return || 'FAN';
               WHEN v_compare BETWEEN '53FD' AND '5438'
               THEN
                  v_return := v_return || 'FANG';
               WHEN v_compare BETWEEN '5439' AND '54B2'
               THEN
                  v_return := v_return || 'FEI';
               WHEN v_compare BETWEEN '54B4' AND '5528'
               THEN
                  v_return := v_return || 'FEN';
               WHEN v_compare BETWEEN '5529' AND '55A9'
               THEN
                  v_return := v_return || 'FENG';
               WHEN v_compare BETWEEN '55AA' AND '55AE'
               THEN
                  v_return := v_return || 'FO';
               WHEN v_compare BETWEEN '55B1' AND '55BC'
               THEN
                  v_return := v_return || 'FOU';
               WHEN v_compare BETWEEN '55BD' AND '5739'
               THEN
                  v_return := v_return || 'FU';
               WHEN v_compare = '569D'
               THEN
                  v_return := v_return || 'M';
               WHEN v_compare BETWEEN '573C' AND '574C'
               THEN
                  v_return := v_return || 'GA';
               WHEN v_compare BETWEEN '574D' AND '578C'
               THEN
                  v_return := v_return || 'GAI';
               WHEN v_compare BETWEEN '578D' AND '57F0'
               THEN
                  v_return := v_return || 'GAN';
               WHEN v_compare BETWEEN '57F1' AND '582C'
               THEN
                  v_return := v_return || 'GANG';
               WHEN v_compare BETWEEN '582E' AND '5884'
               THEN
                  v_return := v_return || 'GAO';
               WHEN v_compare BETWEEN '5885' AND '5905'
               THEN
                  v_return := v_return || 'GE';
               WHEN v_compare = '5906'
               THEN
                  v_return := v_return || 'GEI';
               WHEN v_compare BETWEEN '5909' AND '5915'
               THEN
                  v_return := v_return || 'GEN';
               WHEN v_compare BETWEEN '5918' AND '594E'
               THEN
                  v_return := v_return || 'GENG';
               WHEN v_compare = '5956'
               THEN
                  v_return := v_return || 'GIB';
               WHEN v_compare = '5958'
               THEN
                  v_return := v_return || 'Go';
               WHEN v_compare BETWEEN '5959' AND '59BA'
               THEN
                  v_return := v_return || 'GONG';
               WHEN v_compare BETWEEN '59BD' AND '5A0E'
               THEN
                  v_return := v_return || 'GOU';
               WHEN v_compare BETWEEN '5A10' AND '5AB2'
               THEN
                  v_return := v_return || 'GU';
               WHEN v_compare BETWEEN '5AB4' AND '5AE8'
               THEN
                  v_return := v_return || 'GUA';
               WHEN v_compare BETWEEN '5AE9' AND '5AF8'
               THEN
                  v_return := v_return || 'GUAI';
               WHEN v_compare BETWEEN '5AFD' AND '5B5E'
               THEN
                  v_return := v_return || 'GUAN';
               WHEN v_compare BETWEEN '5B60' AND '5B8C'
               THEN
                  v_return := v_return || 'GUANG';
               WHEN v_compare BETWEEN '5B8D' AND '5C2E'
               THEN
                  v_return := v_return || 'GUI';
               WHEN v_compare = '5BC8'
               THEN
                  v_return := v_return || 'KWI';
               WHEN v_compare BETWEEN '5C30' AND '5C58'
               THEN
                  v_return := v_return || 'GUN';
               WHEN v_compare BETWEEN '5C51' AND '5CB6'
               THEN
                  v_return := v_return || 'GUO';
               WHEN v_compare BETWEEN '5CB8' AND '5CBD'
               THEN
                  v_return := v_return || 'HA';
               WHEN v_compare BETWEEN '5CC6' AND '5CEC'
               THEN
                  v_return := v_return || 'HAI';
               WHEN v_compare = '5CED'
               THEN
                  v_return := v_return || 'HAL';
               WHEN v_compare BETWEEN '5CEE' AND '5D99'
               THEN
                  v_return := v_return || 'HAN';
               WHEN v_compare BETWEEN '5D9D' AND '5DBC'
               THEN
                  v_return := v_return || 'HANG';
               WHEN v_compare BETWEEN '5DBE' AND '5E20'
               THEN
                  v_return := v_return || 'HAO';
               WHEN v_compare = '5E02'
               THEN
                  v_return := v_return || 'HO';
               WHEN v_compare BETWEEN '5E22' AND '5EC5'
               THEN
                  v_return := v_return || 'HE';
               WHEN v_compare BETWEEN '5EC6' AND '5ECE'
               THEN
                  v_return := v_return || 'HEI';
               WHEN v_compare BETWEEN '5ED0' AND '5EDC'
               THEN
                  v_return := v_return || 'HEN';
               WHEN v_compare BETWEEN '5EDD' AND '5F03'
               THEN
                  v_return := v_return || 'HENG';
               WHEN v_compare = '5F04'
               THEN
                  v_return := v_return || 'HOL';
               WHEN v_compare BETWEEN '5F05' AND '5F8D'
               THEN
                  v_return := v_return || 'HONG';
               WHEN v_compare BETWEEN '5F8E' AND '5FD2'
               THEN
                  v_return := v_return || 'HOU';
               WHEN v_compare BETWEEN '5FD4' AND '60B1'
               THEN
                  v_return := v_return || 'HU';
               WHEN v_compare BETWEEN '60B2' AND '6111'
               THEN
                  v_return := v_return || 'HUA';
               WHEN v_compare BETWEEN '6112' AND '612D'
               THEN
                  v_return := v_return || 'HUAI';
               WHEN v_compare BETWEEN '612E' AND '61C6'
               THEN
                  v_return := v_return || 'HUAN';
               WHEN v_compare BETWEEN '61CA' AND '624A'
               THEN
                  v_return := v_return || 'HUANG';
               WHEN v_compare BETWEEN '624C' AND '6344'
               THEN
                  v_return := v_return || 'HUI';
               WHEN v_compare BETWEEN '6346' AND '6388'
               THEN
                  v_return := v_return || 'HUN';
               WHEN v_compare BETWEEN '638C' AND '63FA'
               THEN
                  v_return := v_return || 'HUO';
               WHEN v_compare = '63FD'
               THEN
                  v_return := v_return || 'HWA';
               WHEN v_compare BETWEEN '63FE' AND '6601'
               THEN
                  v_return := v_return || 'JI';
               WHEN v_compare BETWEEN '6604' AND '6691'
               THEN
                  v_return := v_return || 'JIA';
               WHEN v_compare BETWEEN '6692' AND '67F8'
               THEN
                  v_return := v_return || 'JIAN';
               WHEN v_compare BETWEEN '67F9' AND '6860'
               THEN
                  v_return := v_return || 'JIANG';
               WHEN v_compare BETWEEN '6862' AND '6930'
               THEN
                  v_return := v_return || 'JIAO';
               WHEN v_compare BETWEEN '6931' AND '6A18'
               THEN
                  v_return := v_return || 'JIE';
               WHEN v_compare BETWEEN '6A1A' AND '6AC9'
               THEN
                  v_return := v_return || 'JIN';
               WHEN v_compare BETWEEN '6ACA' AND '6B65'
               THEN
                  v_return := v_return || 'JING';
               WHEN v_compare BETWEEN '6B66' AND '6B9A'
               THEN
                  v_return := v_return || 'JIONG';
               WHEN v_compare BETWEEN '6B9C' AND '6C0C'
               THEN
                  v_return := v_return || 'JIU';
               WHEN v_compare = '6C0D'
               THEN
                  v_return := v_return || 'JOU';
               WHEN v_compare BETWEEN '6C0E' AND '6D2A'
               THEN
                  v_return := v_return || 'JU';
               WHEN v_compare BETWEEN '6D2D' AND '6D80'
               THEN
                  v_return := v_return || 'JUAN';
               WHEN v_compare BETWEEN '6D82' AND '6E28'
               THEN
                  v_return := v_return || 'JUE';
               WHEN v_compare BETWEEN '6E2A' AND '6E85'
               THEN
                  v_return := v_return || 'JUN';
               WHEN v_compare BETWEEN '6E86' AND '6E92'
               THEN
                  v_return := v_return || 'KA';
               WHEN v_compare BETWEEN '6E94' AND '6EC9'
               THEN
                  v_return := v_return || 'KAI';
               WHEN v_compare = '6ECC'
               THEN
                  v_return := v_return || 'KAL';
               WHEN v_compare BETWEEN '6ECD' AND '6F00'
               THEN
                  v_return := v_return || 'KAN';
               WHEN v_compare BETWEEN '6F02' AND '6F30'
               THEN
                  v_return := v_return || 'KANG';
               WHEN v_compare BETWEEN '6F31' AND '6F4D'
               THEN
                  v_return := v_return || 'KAO';
               WHEN v_compare BETWEEN '6F50' AND '6FC8'
               THEN
                  v_return := v_return || 'KE';
               WHEN v_compare BETWEEN '6FC9' AND '6FDA'
               THEN
                  v_return := v_return || 'KEN';
               WHEN v_compare BETWEEN '6FDC' AND '6FF5'
               THEN
                  v_return := v_return || 'KENG';
               WHEN v_compare = '6FFC'
               THEN
                  v_return := v_return || 'KI';
               WHEN v_compare BETWEEN '6FFD' AND '7016'
               THEN
                  v_return := v_return || 'KONG';
               WHEN v_compare = '7018'
               THEN
                  v_return := v_return || 'KOS';
               WHEN v_compare BETWEEN '7019' AND '703E'
               THEN
                  v_return := v_return || 'KOU';
               WHEN v_compare BETWEEN '7041' AND '707A'
               THEN
                  v_return := v_return || 'KU';
               WHEN v_compare BETWEEN '707C' AND '7095'
               THEN
                  v_return := v_return || 'KUA';
               WHEN v_compare BETWEEN '709A' AND '70C1'
               THEN
                  v_return := v_return || 'KUAI';
               WHEN v_compare BETWEEN '70C2' AND '70D4'
               THEN
                  v_return := v_return || 'KUAN';
               WHEN v_compare BETWEEN '70D8' AND '7128'
               THEN
                  v_return := v_return || 'KUANG';
               WHEN v_compare BETWEEN '7129' AND '71B1'
               THEN
                  v_return := v_return || 'KUI';
               WHEN v_compare BETWEEN '71B2' AND '71FE'
               THEN
                  v_return := v_return || 'KUN';
               WHEN v_compare BETWEEN '7200' AND '7226'
               THEN
                  v_return := v_return || 'KUO';
               WHEN v_compare = '7228'
               THEN
                  v_return := v_return || 'KWEOK';
               WHEN v_compare BETWEEN '722C' AND '726A'
               THEN
                  v_return := v_return || 'LA';
               WHEN v_compare BETWEEN '726C' AND '72B5'
               THEN
                  v_return := v_return || 'LAI';
               WHEN v_compare BETWEEN '72B9' AND '733C'
               THEN
                  v_return := v_return || 'LAN';
               WHEN v_compare BETWEEN '733D' AND '7388'
               THEN
                  v_return := v_return || 'LANG';
               WHEN v_compare BETWEEN '7389' AND '73E5'
               THEN
                  v_return := v_return || 'LAO';
               WHEN v_compare BETWEEN '73E8' AND '7402'
               THEN
                  v_return := v_return || 'LE';
               WHEN v_compare BETWEEN '7404' AND '7485'
               THEN
                  v_return := v_return || 'LEI';
               WHEN v_compare BETWEEN '7488' AND '7499'
               THEN
                  v_return := v_return || 'LENG';
               WHEN v_compare BETWEEN '749C' AND '7642'
               THEN
                  v_return := v_return || 'LI';
               WHEN v_compare BETWEEN '7644' AND '7645'
               THEN
                  v_return := v_return || 'LIA';
               WHEN v_compare BETWEEN '7646' AND '76EC'
               THEN
                  v_return := v_return || 'LIAN';
               WHEN v_compare BETWEEN '76ED' AND '7731'
               THEN
                  v_return := v_return || 'LIANG';
               WHEN v_compare BETWEEN '7732' AND '7794'
               THEN
                  v_return := v_return || 'LIAO';
               WHEN v_compare BETWEEN '7795' AND '77E2'
               THEN
                  v_return := v_return || 'LIE';
               WHEN v_compare BETWEEN '77E4' AND '785D'
               THEN
                  v_return := v_return || 'LIN';
               WHEN v_compare = '77EA'
               THEN
                  v_return := v_return || 'LEN';
               WHEN v_compare BETWEEN '7860' AND '7904'
               THEN
                  v_return := v_return || 'LING';
               WHEN v_compare BETWEEN '7905' AND '7986'
               THEN
                  v_return := v_return || 'LIU';
               WHEN v_compare BETWEEN '7988' AND '7989'
               THEN
                  v_return := v_return || 'LO';
               WHEN v_compare BETWEEN '798A' AND '79FD'
               THEN
                  v_return := v_return || 'LONG';
               WHEN v_compare BETWEEN '79FE' AND '7A49'
               THEN
                  v_return := v_return || 'LOU';
               WHEN v_compare BETWEEN '7A4C' AND '7B4D'
               THEN
                  v_return := v_return || 'LU';
               WHEN v_compare BETWEEN '7B4E' AND '7B80'
               THEN
                  v_return := v_return || 'LUAN';
               WHEN v_compare BETWEEN '7B81' AND '7BB2'
               THEN
                  v_return := v_return || 'LUN';
               WHEN v_compare BETWEEN '7BB5' AND '7C25'
               THEN
                  v_return := v_return || 'LUO';
               WHEN v_compare BETWEEN '7C26' AND '7C82'
               THEN
                  v_return := v_return || 'LV';
               WHEN v_compare BETWEEN '7C84' AND '7C98'
               THEN
                  v_return := v_return || 'LUE';
               WHEN v_compare BETWEEN '7C9C' AND '7CE4'
               THEN
                  v_return := v_return || 'MA';
               WHEN v_compare BETWEEN '7CE5' AND '7DOC'
               THEN
                  v_return := v_return || 'MAI';
               WHEN v_compare BETWEEN '7D11' AND '7D6E'
               THEN
                  v_return := v_return || 'MAN';
               WHEN v_compare BETWEEN '7D70' AND '7DA9'
               THEN
                  v_return := v_return || 'MANG';
               WHEN v_compare BETWEEN '7DAC' AND '7E15'
               THEN
                  v_return := v_return || 'MAO';
               WHEN v_compare = '7E0C'
               THEN
                  v_return := v_return || 'Q';
               WHEN v_compare BETWEEN '7E18' AND '7E1E'
               THEN
                  v_return := v_return || 'ME';
               WHEN v_compare BETWEEN '7E20' AND '7E9A'
               THEN
                  v_return := v_return || 'MEI';
               WHEN v_compare BETWEEN '7E9D' AND '7EC1'
               THEN
                  v_return := v_return || 'MEN';
               WHEN v_compare BETWEEN '7EC2' AND '7F36'
               THEN
                  v_return := v_return || 'MENG';
               WHEN v_compare = '7F38'
               THEN
                  v_return := v_return || 'MEO';
               WHEN v_compare BETWEEN '7F39' AND '7FE4'
               THEN
                  v_return := v_return || 'MI';
               WHEN v_compare BETWEEN '7FE6' AND '8034'
               THEN
                  v_return := v_return || 'MIAN';
               WHEN v_compare BETWEEN '8035' AND '805A'
               THEN
                  v_return := v_return || 'MIAO';
               WHEN v_compare BETWEEN '805C' AND '8081'
               THEN
                  v_return := v_return || 'MIE';
               WHEN v_compare BETWEEN '8084' AND '80E4'
               THEN
                  v_return := v_return || 'MIN';
               WHEN v_compare = '8096'
               THEN
                  v_return := v_return || 'LEM';
               WHEN v_compare BETWEEN '80E5' AND '8116'
               THEN
                  v_return := v_return || 'MING';
               WHEN v_compare BETWEEN '8119' AND '811D'
               THEN
                  v_return := v_return || 'MIU';
               WHEN v_compare BETWEEN '811E' AND '81A9'
               THEN
                  v_return := v_return || 'MO';
               WHEN v_compare BETWEEN '81AC' AND '81CC'
               THEN
                  v_return := v_return || 'MOU';
               WHEN v_compare BETWEEN '81CD' AND '821E'
               THEN
                  v_return := v_return || 'MU';
               WHEN v_compare = '8220'
               THEN
                  v_return := v_return || 'MYEO';
               WHEN v_compare = '8221'
               THEN
                  v_return := v_return || 'MYEON';
               WHEN v_compare = '8222'
               THEN
                  v_return := v_return || 'MYEONG';
               WHEN v_compare BETWEEN '8224' AND '8258'
               THEN
                  v_return := v_return || 'NA';
               WHEN v_compare BETWEEN '825D' AND '8285'
               THEN
                  v_return := v_return || 'NAI';
               WHEN v_compare BETWEEN '8289' AND '82B5'
               THEN
                  v_return := v_return || 'NAN';
               WHEN v_compare BETWEEN '82B9' AND '82D0'
               THEN
                  v_return := v_return || 'NANG';
               WHEN v_compare BETWEEN '82D1' AND '8311'
               THEN
                  v_return := v_return || 'NAO';
               WHEN v_compare BETWEEN '8312' AND '8320'
               THEN
                  v_return := v_return || 'NE';
               WHEN v_compare BETWEEN '8322' AND '8331'
               THEN
                  v_return := v_return || 'NEI';
               WHEN v_compare = '8334'
               THEN
                  v_return := v_return || 'NEM';
               WHEN v_compare = '8336'
               THEN
                  v_return := v_return || 'NEN';
               WHEN v_compare = '8339'
               THEN
                  v_return := v_return || 'NENG';
               WHEN v_compare = '833E'
               THEN
                  v_return := v_return || 'NEUS';
               WHEN v_compare = '8342'
               THEN
                  v_return := v_return || 'NGAG';
               WHEN v_compare = '8344'
               THEN
                  v_return := v_return || 'NGAI';
               WHEN v_compare = '8345'
               THEN
                  v_return := v_return || 'NGAM';
               WHEN v_compare BETWEEN '8346' AND '83B9'
               THEN
                  v_return := v_return || 'NI';
               WHEN v_compare BETWEEN '83BC' AND '83ED'
               THEN
                  v_return := v_return || 'NIAN';
               WHEN v_compare BETWEEN '83EE' AND '83F5'
               THEN
                  v_return := v_return || 'NIANG';
               WHEN v_compare BETWEEN '83F8' AND '8414'
               THEN
                  v_return := v_return || 'NIAO';
               WHEN v_compare BETWEEN '8415' AND '8478'
               THEN
                  v_return := v_return || 'NIE';
               WHEN v_compare BETWEEN '8479' AND '8480'
               THEN
                  v_return := v_return || 'NIN';
               WHEN v_compare BETWEEN '8481' AND '84B4'
               THEN
                  v_return := v_return || 'NING';
               WHEN v_compare BETWEEN '84B5' AND '84D1'
               THEN
                  v_return := v_return || 'NIU';
               WHEN v_compare BETWEEN '84D4' AND '84FA'
               THEN
                  v_return := v_return || 'NONG';
               WHEN v_compare = '84E8'
               THEN
                  v_return := v_return || 'NUNG';
               WHEN v_compare BETWEEN '84FD' AND '850E'
               THEN
                  v_return := v_return || 'NOU';
               WHEN v_compare BETWEEN '8511' AND '8522'
               THEN
                  v_return := v_return || 'NU';
               WHEN v_compare BETWEEN '8524' AND '852C'
               THEN
                  v_return := v_return || 'NUAN';
               WHEN v_compare = '852D'
               THEN
                  v_return := v_return || 'NUN';
               WHEN v_compare BETWEEN '8530' AND '8559'
               THEN
                  v_return := v_return || 'NUO';
               WHEN v_compare BETWEEN '855A' AND '8566'
               THEN
                  v_return := v_return || 'NV';
               WHEN v_compare BETWEEN '856D' AND '8574'
               THEN
                  v_return := v_return || 'NUE';
               WHEN v_compare = '8575'
               THEN
                  v_return := v_return || 'O';
               WHEN v_compare = '8579'
               THEN
                  v_return := v_return || 'OES';
               WHEN v_compare = '857A'
               THEN
                  v_return := v_return || 'OL';
               WHEN v_compare = '857C'
               THEN
                  v_return := v_return || 'ON';
               WHEN v_compare BETWEEN '857D' AND '85AE'
               THEN
                  v_return := v_return || 'OU';
               WHEN v_compare BETWEEN '85B1' AND '85C9'
               THEN
                  v_return := v_return || 'PA';
               WHEN v_compare BETWEEN '85CA' AND '85E4'
               THEN
                  v_return := v_return || 'PAI';
               WHEN v_compare = '85E5'
               THEN
                  v_return := v_return || 'PAK';
               WHEN v_compare BETWEEN '85E8' AND '8625'
               THEN
                  v_return := v_return || 'PAN';
               WHEN v_compare BETWEEN '8626' AND '8658'
               THEN
                  v_return := v_return || 'PANG';
               WHEN v_compare BETWEEN '8659' AND '8688'
               THEN
                  v_return := v_return || 'PAO';
               WHEN v_compare BETWEEN '868A' AND '86C5'
               THEN
                  v_return := v_return || 'PEI';
               WHEN v_compare BETWEEN '86C8' AND '86D6'
               THEN
                  v_return := v_return || 'PEN';
               WHEN v_compare BETWEEN '86D8' AND '8740'
               THEN
                  v_return := v_return || 'PENG';
               WHEN v_compare = '8741'
               THEN
                  v_return := v_return || 'PEOL';
               WHEN v_compare = '8742'
               THEN
                  v_return := v_return || 'PHAS';
               WHEN v_compare = '8744'
               THEN
                  v_return := v_return || 'PHDENG';
               WHEN v_compare = '8745'
               THEN
                  v_return := v_return || 'PHOI';
               WHEN v_compare = '8746'
               THEN
                  v_return := v_return || 'PHOS';
               WHEN v_compare BETWEEN '8748' AND '880D'
               THEN
                  v_return := v_return || 'PI';
               WHEN v_compare BETWEEN '880E' AND '883A'
               THEN
                  v_return := v_return || 'PIAN';
               WHEN v_compare BETWEEN '883C' AND '8869'
               THEN
                  v_return := v_return || 'PIAO';
               WHEN v_compare BETWEEN '886D' AND '8879'
               THEN
                  v_return := v_return || 'PIE';
               WHEN v_compare BETWEEN '887A' AND '88A0'
               THEN
                  v_return := v_return || 'PIN';
               WHEN v_compare BETWEEN '88A1' AND '88EC'
               THEN
                  v_return := v_return || 'PING';
               WHEN v_compare BETWEEN '88F0' AND '8938'
               THEN
                  v_return := v_return || 'PO';
               WHEN v_compare BETWEEN '893E' AND '8958'
               THEN
                  v_return := v_return || 'POU';
               WHEN v_compare BETWEEN '895A' AND '895C'
               THEN
                  v_return := v_return || 'PPUN';
               WHEN v_compare BETWEEN '895D' AND '89C4'
               THEN
                  v_return := v_return || 'PU';
               WHEN v_compare BETWEEN '89C5' AND '8B3E'
               THEN
                  v_return := v_return || 'QI';
               WHEN v_compare BETWEEN '8B41' AND '8B61'
               THEN
                  v_return := v_return || 'QIA';
               WHEN v_compare BETWEEN '8B62' AND '8C54'
               THEN
                  v_return := v_return || 'QIAN';
               WHEN v_compare BETWEEN '8C5A' AND '8CB4'
               THEN
                  v_return := v_return || 'QIANG';
               WHEN v_compare BETWEEN '8CB8' AND '8D3D'
               THEN
                  v_return := v_return || 'QIAO';
               WHEN v_compare BETWEEN '8D40' AND '8D7E'
               THEN
                  v_return := v_return || 'QIE';
               WHEN v_compare BETWEEN '8D81' AND '8DFA'
               THEN
                  v_return := v_return || 'QIN';
               WHEN v_compare BETWEEN '8DFC' AND '8E5D'
               THEN
                  v_return := v_return || 'QING';
               WHEN v_compare BETWEEN '8E5E' AND '8E98'
               THEN
                  v_return := v_return || 'QIONG';
               WHEN v_compare BETWEEN '8E9A' AND '8F2A'
               THEN
                  v_return := v_return || 'QIU';
               WHEN v_compare BETWEEN '8F2E' AND '8FE9'
               THEN
                  v_return := v_return || 'QU';
               WHEN v_compare BETWEEN '8FEA' AND '905D'
               THEN
                  v_return := v_return || 'QUAN';
               WHEN v_compare BETWEEN '905E' AND '9099'
               THEN
                  v_return := v_return || 'QUE';
               WHEN v_compare BETWEEN '909A' AND '90AA'
               THEN
                  v_return := v_return || 'QUN';
               WHEN v_compare BETWEEN '90B0' AND '90B1'
               THEN
                  v_return := v_return || 'RA';
               WHEN v_compare = '90B2'
               THEN
                  v_return := v_return || 'RAM';
               WHEN v_compare BETWEEN '90B4' AND '90E5'
               THEN
                  v_return := v_return || 'RAN';
               WHEN v_compare BETWEEN '90E6' AND '9104'
               THEN
                  v_return := v_return || 'RANG';
               WHEN v_compare BETWEEN '9105' AND '911C'
               THEN
                  v_return := v_return || 'RAO';
               WHEN v_compare BETWEEN '911D' AND '9120'
               THEN
                  v_return := v_return || 'RE';
               WHEN v_compare BETWEEN '9121' AND '9180'
               THEN
                  v_return := v_return || 'REN';
               WHEN v_compare BETWEEN '9181' AND '918D'
               THEN
                  v_return := v_return || 'RENG';
               WHEN v_compare BETWEEN '918E' AND '9196'
               THEN
                  v_return := v_return || 'RI';
               WHEN v_compare BETWEEN '9189' AND '91F1'
               THEN
                  v_return := v_return || 'RONG';
               WHEN v_compare BETWEEN '91F2' AND '9218'
               THEN
                  v_return := v_return || 'ROU';
               WHEN v_compare BETWEEN '9219' AND '9269'
               THEN
                  v_return := v_return || 'RU';
               WHEN v_compare BETWEEN '926C' AND '9292'
               THEN
                  v_return := v_return || 'RUAN';
               WHEN v_compare BETWEEN '9294' AND '92BD'
               THEN
                  v_return := v_return || 'RUI';
               WHEN v_compare BETWEEN '92BE' AND '92C9'
               THEN
                  v_return := v_return || 'RUN';
               WHEN v_compare = '92CA'
               THEN
                  v_return := v_return || 'RUA';
               WHEN v_compare BETWEEN '92CA' AND '92E4'
               THEN
                  v_return := v_return || 'RUO';
               WHEN v_compare BETWEEN '92E5' AND '9309'
               THEN
                  v_return := v_return || 'SA';
               WHEN v_compare = '930A'
               THEN
                  v_return := v_return || 'SAENG';
               WHEN v_compare BETWEEN '930C' AND '9325'
               THEN
                  v_return := v_return || 'SAI';
               WHEN v_compare = '9328'
               THEN
                  v_return := v_return || 'SAL';
               WHEN v_compare BETWEEN '9329' AND '9355'
               THEN
                  v_return := v_return || 'SAN';
               WHEN v_compare BETWEEN '9358' AND '936A'
               THEN
                  v_return := v_return || 'SANG';
               WHEN v_compare BETWEEN '936C' AND '9391'
               THEN
                  v_return := v_return || 'SAO';
               WHEN v_compare BETWEEN '9392' AND '93C5'
               THEN
                  v_return := v_return || 'SE';
               WHEN v_compare = '93C6'
               THEN
                  v_return := v_return || 'SED';
               WHEN v_compare BETWEEN '93C8' AND '93CC'
               THEN
                  v_return := v_return || 'SEN';
               WHEN v_compare BETWEEN '93CD' AND '93D0'
               THEN
                  v_return := v_return || 'SENG';
               WHEN v_compare = '93D1'
               THEN
                  v_return := v_return || 'SEO';
               WHEN v_compare = '93D2'
               THEN
                  v_return := v_return || 'SEON';
               WHEN v_compare BETWEEN '93D4' AND '941A'
               THEN
                  v_return := v_return || 'SHA';
               WHEN v_compare BETWEEN '941D' AND '9428'
               THEN
                  v_return := v_return || 'SHAI';
               WHEN v_compare BETWEEN '9429' AND '94C1'
               THEN
                  v_return := v_return || 'SHAN';
               WHEN v_compare BETWEEN '94C2' AND '94EE'
               THEN
                  v_return := v_return || 'SHANG';
               WHEN v_compare BETWEEN '94F1' AND '952D'
               THEN
                  v_return := v_return || 'SHAO';
               WHEN v_compare BETWEEN '952E' AND '9571'
               THEN
                  v_return := v_return || 'SHE';
               WHEN v_compare BETWEEN '9574' AND '9602'
               THEN
                  v_return := v_return || 'SHEN';
               WHEN v_compare BETWEEN '9604' AND '965C'
               THEN
                  v_return := v_return || 'SHENG';
               WHEN v_compare BETWEEN '965E' AND '9786'
               THEN
                  v_return := v_return || 'SHI';
               WHEN v_compare BETWEEN '9788' AND '97AE'
               THEN
                  v_return := v_return || 'SHOU';
               WHEN v_compare BETWEEN '97B0' AND '9878'
               THEN
                  v_return := v_return || 'SHU';
               WHEN v_compare BETWEEN '987A' AND '987E'
               THEN
                  v_return := v_return || 'SHUA';
               WHEN v_compare BETWEEN '9880' AND '988A'
               THEN
                  v_return := v_return || 'SHUAI';
               WHEN v_compare BETWEEN '988C' AND '9894'
               THEN
                  v_return := v_return || 'SHUAN';
               WHEN v_compare BETWEEN '9895' AND '98BE'
               THEN
                  v_return := v_return || 'SHUANG';
               WHEN v_compare BETWEEN '98C0' AND '98D6'
               THEN
                  v_return := v_return || 'SHUI';
               WHEN v_compare BETWEEN '98DC' AND '98EE'
               THEN
                  v_return := v_return || 'SHUN';
               WHEN v_compare BETWEEN '98F1' AND '9911'
               THEN
                  v_return := v_return || 'SHUO';
               WHEN v_compare BETWEEN '9912' AND '99AD'
               THEN
                  v_return := v_return || 'SI';
               WHEN v_compare = '99AE'
               THEN
                  v_return := v_return || 'SO';
               WHEN v_compare = '99B0'
               THEN
                  v_return := v_return || 'SOL';
               WHEN v_compare BETWEEN '99B1' AND '99F6'
               THEN
                  v_return := v_return || 'SONG';
               WHEN v_compare BETWEEN '99F8' AND '9A36'
               THEN
                  v_return := v_return || 'SOU';
               WHEN v_compare BETWEEN '9A38' AND '9AB6'
               THEN
                  v_return := v_return || 'SU';
               WHEN v_compare BETWEEN '9AB8' AND '9AC4'
               THEN
                  v_return := v_return || 'SUAN';
               WHEN v_compare BETWEEN '9AC5' AND '9B3A'
               THEN
                  v_return := v_return || 'SUI';
               WHEN v_compare = '9AF0'
               THEN
                  v_return := v_return || 'WIE';
               WHEN v_compare BETWEEN '9B3C' AND '9B62'
               THEN
                  v_return := v_return || 'SUN';
               WHEN v_compare BETWEEN '9B65' AND '9BA9'
               THEN
                  v_return := v_return || 'SUO';
               WHEN v_compare BETWEEN '9BAA' AND '9C10'
               THEN
                  v_return := v_return || 'TA';
               WHEN v_compare = '9C11'
               THEN
                  v_return := v_return || 'TAE';
               WHEN v_compare BETWEEN '9C12' AND '9C59'
               THEN
                  v_return := v_return || 'TAI';
               WHEN v_compare BETWEEN '9C5A' AND '9CE0'
               THEN
                  v_return := v_return || 'TAN';
               WHEN v_compare BETWEEN '9CE2' AND '9D55'
               THEN
                  v_return := v_return || 'TANG';
               WHEN v_compare BETWEEN '9D56' AND '9DB4'
               THEN
                  v_return := v_return || 'TAO';
               WHEN v_compare = '9DB6'
               THEN
                  v_return := v_return || 'TAP';
               WHEN v_compare BETWEEN '9DB8' AND '9DC6'
               THEN
                  v_return := v_return || 'TE';
               WHEN v_compare BETWEEN '9DC8' AND '9DED'
               THEN
                  v_return := v_return || 'TENG';
               WHEN v_compare = '9DEE'
               THEN
                  v_return := v_return || 'TEO';
               WHEN v_compare = '9DF0'
               THEN
                  v_return := v_return || 'TEUL';
               WHEN v_compare BETWEEN '9DF1' AND '9E82'
               THEN
                  v_return := v_return || 'TI';
               WHEN v_compare BETWEEN '9E85' AND '9EED'
               THEN
                  v_return := v_return || 'TIAN';
               WHEN v_compare BETWEEN '9EEE' AND '9F38'
               THEN
                  v_return := v_return || 'TIAO';
               WHEN v_compare BETWEEN '9F39' AND '9F56'
               THEN
                  v_return := v_return || 'TIE';
               WHEN v_compare BETWEEN '9F59' AND '9FAE'
               THEN
                  v_return := v_return || 'TING';
               WHEN v_compare = '9FB0'
               THEN
                  v_return := v_return || 'TOL';
               WHEN v_compare BETWEEN '9FB1' AND 'A015'
               THEN
                  v_return := v_return || 'TONG';
               WHEN v_compare BETWEEN 'A016' AND 'A03A'
               THEN
                  v_return := v_return || 'TOU';
               WHEN v_compare BETWEEN 'A040' AND 'A0A9'
               THEN
                  v_return := v_return || 'TU';
               WHEN v_compare BETWEEN 'A0AA' AND 'A0D5'
               THEN
                  v_return := v_return || 'TUAN';
               WHEN v_compare BETWEEN 'A0D6' AND 'A106'
               THEN
                  v_return := v_return || 'TUI';
               WHEN v_compare BETWEEN 'A108' AND 'A131'
               THEN
                  v_return := v_return || 'TUN';
               WHEN v_compare BETWEEN 'A134' AND 'A1AE'
               THEN
                  v_return := v_return || 'TUO';
               WHEN v_compare BETWEEN 'A1B0' AND 'A1E8'
               THEN
                  v_return := v_return || 'WA';
               WHEN v_compare BETWEEN 'A1E9' AND 'A1F5'
               THEN
                  v_return := v_return || 'WAI';
               WHEN v_compare BETWEEN 'A1F8' AND 'A279'
               THEN
                  v_return := v_return || 'WAN';
               WHEN v_compare BETWEEN 'A27A' AND 'A2B9'
               THEN
                  v_return := v_return || 'WANG';
               WHEN v_compare BETWEEN 'A2BC' AND 'A408'
               THEN
                  v_return := v_return || 'WEI';
               WHEN v_compare BETWEEN 'A40D' AND 'A47C'
               THEN
                  v_return := v_return || 'WEN';
               WHEN v_compare BETWEEN 'A47D' AND 'A4A2'
               THEN
                  v_return := v_return || 'WENG';
               WHEN v_compare BETWEEN 'A4A4' AND 'A4EA'
               THEN
                  v_return := v_return || 'WO';
               WHEN v_compare BETWEEN 'A4EC' AND 'A5D4'
               THEN
                  v_return := v_return || 'WU';
               WHEN v_compare BETWEEN 'A5D6' AND 'A784'
               THEN
                  v_return := v_return || 'XI';
               WHEN v_compare BETWEEN 'A785' AND 'A7FA'
               THEN
                  v_return := v_return || 'XIA';
               WHEN v_compare BETWEEN 'A7FD' AND 'A951'
               THEN
                  v_return := v_return || 'XIAN';
               WHEN v_compare BETWEEN 'A954' AND 'A9CE'
               THEN
                  v_return := v_return || 'XIANG';
               WHEN v_compare BETWEEN 'A9D0' AND 'AA8A'
               THEN
                  v_return := v_return || 'XIAO';
               WHEN v_compare BETWEEN 'AA8D' AND 'AB7E'
               THEN
                  v_return := v_return || 'XIE';
               WHEN v_compare BETWEEN 'AB80' AND 'ABEE'
               THEN
                  v_return := v_return || 'XIN';
               WHEN v_compare BETWEEN 'ABF0' AND 'AC41'
               THEN
                  v_return := v_return || 'XING';
               WHEN v_compare BETWEEN 'AC42' AND 'AC64'
               THEN
                  v_return := v_return || 'XIONG';
               WHEN v_compare BETWEEN 'AC65' AND 'ACBA'
               THEN
                  v_return := v_return || 'XIU';
               WHEN v_compare BETWEEN 'ACBC' AND 'AD90'
               THEN
                  v_return := v_return || 'XU';
               WHEN v_compare = 'ACD9'
               THEN
                  v_return := v_return || 'CHUA';
               WHEN v_compare BETWEEN 'AD91' AND 'AE32'
               THEN
                  v_return := v_return || 'XUAN';
               WHEN v_compare BETWEEN 'AE34' AND 'AE89'
               THEN
                  v_return := v_return || 'XUE';
               WHEN v_compare BETWEEN 'AE8C' AND 'AF1E'
               THEN
                  v_return := v_return || 'XUN';
               WHEN v_compare BETWEEN 'AF20' AND 'AF96'
               THEN
                  v_return := v_return || 'YA';
               WHEN v_compare BETWEEN 'AF98' AND 'B118'
               THEN
                  v_return := v_return || 'YAN';
               WHEN v_compare = 'B030'
               THEN
                  v_return := v_return || 'EOM';
               WHEN v_compare BETWEEN 'B11A' AND 'B1A8'
               THEN
                  v_return := v_return || 'YANG';
               WHEN v_compare BETWEEN 'B1AD' AND 'B275'
               THEN
                  v_return := v_return || 'YAO';
               WHEN v_compare BETWEEN 'B276' AND 'B30A'
               THEN
                  v_return := v_return || 'YE';
               WHEN v_compare BETWEEN 'B30D' AND 'B30E'
               THEN
                  v_return := v_return || 'YEN';
               WHEN v_compare BETWEEN 'B310' AND 'B594'
               THEN
                  v_return := v_return || 'YI';
               WHEN v_compare = 'B359'
               THEN
                  v_return := v_return || 'I';
               WHEN v_compare BETWEEN 'B596' AND 'B684'
               THEN
                  v_return := v_return || 'YIN';
               WHEN v_compare BETWEEN 'B685' AND 'B768'
               THEN
                  v_return := v_return || 'YING';
               WHEN v_compare BETWEEN 'B76C' AND 'B76E'
               THEN
                  v_return := v_return || 'YO';
               WHEN v_compare BETWEEN 'B770' AND 'B7EA'
               THEN
                  v_return := v_return || 'YONG';
               WHEN v_compare BETWEEN 'B7EC' AND 'B8B2'
               THEN
                  v_return := v_return || 'YOU';
               WHEN v_compare BETWEEN 'B8B5' AND 'BA98'
               THEN
                  v_return := v_return || 'YU';
               WHEN v_compare BETWEEN 'BA99' AND 'BB58'
               THEN
                  v_return := v_return || 'YUAN';
               WHEN v_compare BETWEEN 'BB59' AND 'BBBE'
               THEN
                  v_return := v_return || 'YUE';
               WHEN v_compare BETWEEN 'BBC1' AND 'BC58'
               THEN
                  v_return := v_return || 'YUN';
               WHEN v_compare BETWEEN 'BC59' AND 'BC7E'
               THEN
                  v_return := v_return || 'ZA';
               WHEN v_compare BETWEEN 'BC81' AND 'BCA8'
               THEN
                  v_return := v_return || 'ZAI';
               WHEN v_compare BETWEEN 'BCAA' AND 'BCEA'
               THEN
                  v_return := v_return || 'ZAN';
               WHEN v_compare BETWEEN 'BCEE' AND 'BD0A'
               THEN
                  v_return := v_return || 'ZANG';
               WHEN v_compare BETWEEN 'BD0C' AND 'BD46'
               THEN
                  v_return := v_return || 'ZAO';
               WHEN v_compare BETWEEN 'BD48' AND 'BD99'
               THEN
                  v_return := v_return || 'ZE';
               WHEN v_compare BETWEEN 'BD9A' AND 'BDA2'
               THEN
                  v_return := v_return || 'ZEI';
               WHEN v_compare BETWEEN 'BDA5' AND 'BDAC'
               THEN
                  v_return := v_return || 'ZEN';
               WHEN v_compare BETWEEN 'BDAD' AND 'BDCC'
               THEN
                  v_return := v_return || 'ZENG';
               WHEN v_compare BETWEEN 'BDCE' AND 'BE40'
               THEN
                  v_return := v_return || 'ZHA';
               WHEN v_compare = 'BDF8'
               THEN
                  v_return := v_return || 'GAD';
               WHEN v_compare BETWEEN 'BE41' AND 'BE62'
               THEN
                  v_return := v_return || 'ZHAI';
               WHEN v_compare BETWEEN 'BE65' AND 'BEF4'
               THEN
                  v_return := v_return || 'ZHAN';
               WHEN v_compare BETWEEN 'BEF6' AND 'BF3E'
               THEN
                  v_return := v_return || 'ZHANG';
               WHEN v_compare BETWEEN 'BF40' AND 'BF8C'
               THEN
                  v_return := v_return || 'ZHAO';
               WHEN v_compare BETWEEN 'BF8E' AND 'BFF8'
               THEN
                  v_return := v_return || 'ZHE';
               WHEN v_compare BETWEEN 'BFF9' AND 'C0B2'
               THEN
                  v_return := v_return || 'ZHEN';
               WHEN v_compare BETWEEN 'C0B4' AND 'C11E'
               THEN
                  v_return := v_return || 'ZHENG';
               WHEN v_compare BETWEEN 'C122' AND 'C2C4'
               THEN
                  v_return := v_return || 'ZHI';
               WHEN v_compare BETWEEN 'C2C5' AND 'C31A'
               THEN
                  v_return := v_return || 'ZHONG';
               WHEN v_compare BETWEEN 'C31D' AND 'C39A'
               THEN
                  v_return := v_return || 'ZHOU';
               WHEN v_compare BETWEEN 'C39C' AND 'C47C'
               THEN
                  v_return := v_return || 'ZHU';
               WHEN v_compare BETWEEN 'C47D' AND 'C484'
               THEN
                  v_return := v_return || 'ZHUA';
               WHEN v_compare BETWEEN 'C485' AND 'C486'
               THEN
                  v_return := v_return || 'ZHUAI';
               WHEN v_compare BETWEEN 'C488' AND 'C4C0'
               THEN
                  v_return := v_return || 'ZHUAN';
               WHEN v_compare BETWEEN 'C4C2' AND 'C4E5'
               THEN
                  v_return := v_return || 'ZHUANG';
               WHEN v_compare BETWEEN 'C4E6' AND 'C51C'
               THEN
                  v_return := v_return || 'ZHUI';
               WHEN v_compare BETWEEN 'C51D' AND 'C530'
               THEN
                  v_return := v_return || 'ZHUN';
               WHEN v_compare BETWEEN 'C534' AND 'C5A5'
               THEN
                  v_return := v_return || 'ZHUO';
               WHEN v_compare BETWEEN 'C5A8' AND 'C648'
               THEN
                  v_return := v_return || 'ZI';
               WHEN v_compare = 'C64A'
               THEN
                  v_return := v_return || 'ZO';
               WHEN v_compare BETWEEN 'C64C' AND 'C6B5'
               THEN
                  v_return := v_return || 'ZONG';
               WHEN v_compare BETWEEN 'C6B6' AND 'C6D6'
               THEN
                  v_return := v_return || 'ZOU';
               WHEN v_compare BETWEEN 'C6E1' AND 'C714'
               THEN
                  v_return := v_return || 'ZU';
               WHEN v_compare BETWEEN 'C715' AND 'C72D'
               THEN
                  v_return := v_return || 'ZUAN';
               WHEN v_compare BETWEEN 'C72E' AND 'C75E'
               THEN
                  v_return := v_return || 'ZUI';
               WHEN v_compare BETWEEN 'C760' AND 'C776'
               THEN
                  v_return := v_return || 'ZUN';
               WHEN v_compare BETWEEN 'C77A' AND 'C7B4'
               THEN
                  v_return := v_return || 'ZUO';
               ELSE
                  v_return := v_return || v_substr;
            END CASE;
         END LOOP;
      WHEN 2
      THEN                                                           --全拼首字母大写
         FOR i IN 1 .. v_length
         LOOP
            v_substr := SUBSTR (p_str, i, 1);
            v_compare := fn_nlssort (v_substr);
 
            CASE
               WHEN v_compare BETWEEN '3B29' AND '3B30'
               THEN
                  v_return := v_return || 'A';
               WHEN v_compare = '3B31'
               THEN
                  v_return := v_return || 'Aes';
               WHEN v_compare BETWEEN '3B32' AND '3B9E'
               THEN
                  v_return := v_return || 'Ai';
               WHEN v_compare BETWEEN '3BA0' AND '3BFE'
               THEN
                  v_return := v_return || 'An';
               WHEN v_compare BETWEEN '3C01' AND '3C14'
               THEN
                  v_return := v_return || 'Ang';
               WHEN v_compare BETWEEN '3C15' AND '3C82'
               THEN
                  v_return := v_return || 'Ao';
               WHEN v_compare BETWEEN '3C84' AND '3CE9'
               THEN
                  v_return := v_return || 'Ba';
               WHEN v_compare BETWEEN '3CED' AND '3D1D'
               THEN
                  v_return := v_return || 'Bai';
               WHEN v_compare BETWEEN '3D20' AND '3D64'
               THEN
                  v_return := v_return || 'Ban';
               WHEN v_compare BETWEEN '3D66' AND '3DA2'
               THEN
                  v_return := v_return || 'Bang';
               WHEN v_compare BETWEEN '3DA4' AND '3E10'
               THEN
                  v_return := v_return || 'Bao';
               WHEN v_compare = '3E11'
               THEN
                  v_return := v_return || 'Be';
               WHEN v_compare BETWEEN '3E12' AND '3E7A'
               THEN
                  v_return := v_return || 'Bei';
               WHEN v_compare BETWEEN '3E7C' AND '3EA0'
               THEN
                  v_return := v_return || 'Ben';
               WHEN v_compare BETWEEN '3EA1' AND '3ED5'
               THEN
                  v_return := v_return || 'Beng';
               WHEN v_compare BETWEEN '3ED8' AND '3FE9'
               THEN
                  v_return := v_return || 'Bi';
               WHEN v_compare BETWEEN '3FEA' AND '4055'
               THEN
                  v_return := v_return || 'Bian';
               WHEN v_compare BETWEEN '4058' AND '40AE'
               THEN
                  v_return := v_return || 'Biao';
               WHEN v_compare = '4060'
               THEN
                  v_return := v_return || 'Bia';
               WHEN v_compare BETWEEN '40B4' AND '40D4'
               THEN
                  v_return := v_return || 'Bie';
               WHEN v_compare BETWEEN '40D6' AND '4116'
               THEN
                  v_return := v_return || 'Bin';
               WHEN v_compare BETWEEN '4118' AND '4160'
               THEN
                  v_return := v_return || 'Bing';
               WHEN v_compare BETWEEN '4161' AND '4224'
               THEN
                  v_return := v_return || 'Bo';
               WHEN v_compare BETWEEN '4225' AND '427C'
               THEN
                  v_return := v_return || 'Bu';
               WHEN v_compare BETWEEN '427D' AND '4289'
               THEN
                  v_return := v_return || 'Ca';
               WHEN v_compare BETWEEN '428C' AND '42B5'
               THEN
                  v_return := v_return || 'Cai';
               WHEN v_compare BETWEEN '42B9' AND '430C'
               THEN
                  v_return := v_return || 'Can';
               WHEN v_compare BETWEEN '430D' AND '4334'
               THEN
                  v_return := v_return || 'Cang';
               WHEN v_compare BETWEEN '4335' AND '435C'
               THEN
                  v_return := v_return || 'Cao';
               WHEN v_compare BETWEEN '435D' AND '438C'
               THEN
                  v_return := v_return || 'Ce';
               WHEN v_compare BETWEEN '4390' AND '4398'
               THEN
                  v_return := v_return || 'Cen';
               WHEN v_compare BETWEEN '439D' AND '43AA'
               THEN
                  v_return := v_return || 'Ceng';
               WHEN v_compare = '43AC'
               THEN
                  v_return := v_return || 'Ceok';
               WHEN v_compare = '43AE'
               THEN
                  v_return := v_return || 'Ceom';
               WHEN v_compare = '43B0'
               THEN
                  v_return := v_return || 'Ceon';
               WHEN v_compare = '43B1'
               THEN
                  v_return := v_return || 'Ceor';
               WHEN v_compare BETWEEN '43B2' AND '440A'
               THEN
                  v_return := v_return || 'Cha';
               WHEN v_compare BETWEEN '440E' AND '442D'
               THEN
                  v_return := v_return || 'Chai';
               WHEN v_compare BETWEEN '4431' AND '44E1'
               THEN
                  v_return := v_return || 'Chan';
               WHEN v_compare BETWEEN '44E4' AND '4552'
               THEN
                  v_return := v_return || 'Chang';
               WHEN v_compare BETWEEN '4554' AND '458E'
               THEN
                  v_return := v_return || 'Chao';
               WHEN v_compare BETWEEN '4590' AND '45C8'
               THEN
                  v_return := v_return || 'Che';
               WHEN v_compare BETWEEN '45C9' AND '463D'
               THEN
                  v_return := v_return || 'Chen';
               WHEN v_compare BETWEEN '463E' AND '46CD'
               THEN
                  v_return := v_return || 'Cheng';
               WHEN v_compare BETWEEN '46CE' AND '47A6'
               THEN
                  v_return := v_return || 'Chi';
               WHEN v_compare BETWEEN '47A8' AND '47EC'
               THEN
                  v_return := v_return || 'Chong';
               WHEN v_compare BETWEEN '47ED' AND '484C'
               THEN
                  v_return := v_return || 'Chou';
               WHEN v_compare BETWEEN '484D' AND '48E2'
               THEN
                  v_return := v_return || 'Chu';
               WHEN v_compare BETWEEN '48E9' AND '48F4'
               THEN
                  v_return := v_return || 'Chuai';
               WHEN v_compare BETWEEN '48F6' AND '4924'
               THEN
                  v_return := v_return || 'Chuan';
               WHEN v_compare BETWEEN '4925' AND '4951'
               THEN
                  v_return := v_return || 'Chuang';
               WHEN v_compare BETWEEN '4954' AND '496E'
               THEN
                  v_return := v_return || 'Chui';
               WHEN v_compare BETWEEN '4971' AND '49C6'
               THEN
                  v_return := v_return || 'Chun';
               WHEN v_compare BETWEEN '49C8' AND '49EA'
               THEN
                  v_return := v_return || 'Chuo';
               WHEN v_compare BETWEEN '49EC' AND '4A4A'
               THEN
                  v_return := v_return || 'Ci';
               WHEN v_compare = '4A50'
               THEN
                  v_return := v_return || 'Cis';
               WHEN v_compare BETWEEN '4A51' AND '4AB2'
               THEN
                  v_return := v_return || 'Cong';
               WHEN v_compare BETWEEN '4AB4' AND '4ABA'
               THEN
                  v_return := v_return || 'Cou';
               WHEN v_compare BETWEEN '4ABC' AND '4AEA'
               THEN
                  v_return := v_return || 'Cu';
               WHEN v_compare BETWEEN '4AEE' AND '4B0C'
               THEN
                  v_return := v_return || 'Cuan';
               WHEN v_compare BETWEEN '4B0D' AND '4B56'
               THEN
                  v_return := v_return || 'Cui';
               WHEN v_compare BETWEEN '4B59' AND '4B6C'
               THEN
                  v_return := v_return || 'Cun';
               WHEN v_compare BETWEEN '4B70' AND '4BA9'
               THEN
                  v_return := v_return || 'Cuo';
               WHEN v_compare BETWEEN '4BAD' AND '4BFE'
               THEN
                  v_return := v_return || 'Da';
               WHEN v_compare BETWEEN '4C00' AND '4C4E'
               THEN
                  v_return := v_return || 'Dai';
               WHEN v_compare BETWEEN '4C50' AND '4CDC'
               THEN
                  v_return := v_return || 'Dan';
               WHEN v_compare BETWEEN '4CDE' AND '4D26'
               THEN
                  v_return := v_return || 'Dang';
               WHEN v_compare BETWEEN '4D28' AND '4D76'
               THEN
                  v_return := v_return || 'Dao';
               WHEN v_compare BETWEEN '4D7E' AND '4D8D'
               THEN
                  v_return := v_return || 'De';
               WHEN v_compare = '4D8E'
               THEN
                  v_return := v_return || 'Dem';
               WHEN v_compare BETWEEN '4D90' AND '4D91'
               THEN
                  v_return := v_return || 'Den';
               WHEN v_compare BETWEEN '4D94' AND '4DC0'
               THEN
                  v_return := v_return || 'Deng';
               WHEN v_compare BETWEEN '4DC4' AND '4E8A'
               THEN
                  v_return := v_return || 'Di';
               WHEN v_compare = '4E8C'
               THEN
                  v_return := v_return || 'Dia';
               WHEN v_compare BETWEEN '4E8D' AND '4EE8'
               THEN
                  v_return := v_return || 'Dian';
               WHEN v_compare BETWEEN '4EE9' AND '4F38'
               THEN
                  v_return := v_return || 'Diao';
               WHEN v_compare BETWEEN '4F39' AND '4F90'
               THEN
                  v_return := v_return || 'Die';
               WHEN v_compare = '4F8D'
               THEN
                  v_return := v_return || 'Dei';
               WHEN v_compare = '4F91'
               THEN
                  v_return := v_return || 'Dim';
               WHEN v_compare BETWEEN '4F92' AND '4FCD'
               THEN
                  v_return := v_return || 'Ding';
               WHEN v_compare BETWEEN '4FCD' AND '4FD4'
               THEN
                  v_return := v_return || 'Diu';
               WHEN v_compare BETWEEN '4FD5' AND '5032'
               THEN
                  v_return := v_return || 'Dong';
               WHEN v_compare BETWEEN '5034' AND '507C'
               THEN
                  v_return := v_return || 'Dou';
               WHEN v_compare = '5044'
               THEN
                  v_return := v_return || 'Dul';
               WHEN v_compare BETWEEN '507E' AND '50E9'
               THEN
                  v_return := v_return || 'Du';
               WHEN v_compare BETWEEN '50EA' AND '5110'
               THEN
                  v_return := v_return || 'Duan';
               WHEN v_compare BETWEEN '5114' AND '514E'
               THEN
                  v_return := v_return || 'Dui';
               WHEN v_compare BETWEEN '5152' AND '518D'
               THEN
                  v_return := v_return || 'Dun';
               WHEN v_compare = '5160'
               THEN
                  v_return := v_return || 'Ton';
               WHEN v_compare BETWEEN '518E' AND '5200'
               THEN
                  v_return := v_return || 'Duo';
               WHEN v_compare BETWEEN '5205' AND '52C2'
               THEN
                  v_return := v_return || 'E';
               WHEN v_compare BETWEEN '52C4' AND '52CD'
               THEN
                  v_return := v_return || 'En';
               WHEN v_compare = '52D4'
               THEN
                  v_return := v_return || 'Eng';
               WHEN v_compare = '52D5'
               THEN
                  v_return := v_return || 'Eo';
               WHEN v_compare = '52D6'
               THEN
                  v_return := v_return || 'Eol';
               WHEN v_compare = '52D8'
               THEN
                  v_return := v_return || 'Eos';
               WHEN v_compare BETWEEN '52D9' AND '5332'
               THEN
                  v_return := v_return || 'Er';
               WHEN v_compare BETWEEN '5334' AND '5366'
               THEN
                  v_return := v_return || 'Fa';
               WHEN v_compare BETWEEN '536A' AND '53FA'
               THEN
                  v_return := v_return || 'Fan';
               WHEN v_compare BETWEEN '53FD' AND '5438'
               THEN
                  v_return := v_return || 'Fang';
               WHEN v_compare BETWEEN '5439' AND '54B2'
               THEN
                  v_return := v_return || 'Fei';
               WHEN v_compare BETWEEN '54B4' AND '5528'
               THEN
                  v_return := v_return || 'Fen';
               WHEN v_compare BETWEEN '5529' AND '55A9'
               THEN
                  v_return := v_return || 'Feng';
               WHEN v_compare BETWEEN '55AA' AND '55AE'
               THEN
                  v_return := v_return || 'Fo';
               WHEN v_compare BETWEEN '55B1' AND '55BC'
               THEN
                  v_return := v_return || 'Fou';
               WHEN v_compare BETWEEN '55BD' AND '5739'
               THEN
                  v_return := v_return || 'Fu';
               WHEN v_compare = '569D'
               THEN
                  v_return := v_return || 'M';
               WHEN v_compare BETWEEN '573C' AND '574C'
               THEN
                  v_return := v_return || 'Ga';
               WHEN v_compare BETWEEN '574D' AND '578C'
               THEN
                  v_return := v_return || 'Gai';
               WHEN v_compare BETWEEN '578D' AND '57F0'
               THEN
                  v_return := v_return || 'Gan';
               WHEN v_compare BETWEEN '57F1' AND '582C'
               THEN
                  v_return := v_return || 'Gang';
               WHEN v_compare BETWEEN '582E' AND '5884'
               THEN
                  v_return := v_return || 'Gao';
               WHEN v_compare BETWEEN '5885' AND '5905'
               THEN
                  v_return := v_return || 'Ge';
               WHEN v_compare = '5906'
               THEN
                  v_return := v_return || 'Gei';
               WHEN v_compare BETWEEN '5909' AND '5915'
               THEN
                  v_return := v_return || 'Gen';
               WHEN v_compare BETWEEN '5918' AND '594E'
               THEN
                  v_return := v_return || 'Geng';
               WHEN v_compare = '5956'
               THEN
                  v_return := v_return || 'Gib';
               WHEN v_compare = '5958'
               THEN
                  v_return := v_return || 'Go';
               WHEN v_compare BETWEEN '5959' AND '59BA'
               THEN
                  v_return := v_return || 'Gong';
               WHEN v_compare BETWEEN '59BD' AND '5A0E'
               THEN
                  v_return := v_return || 'Gou';
               WHEN v_compare BETWEEN '5A10' AND '5AB2'
               THEN
                  v_return := v_return || 'Gu';
               WHEN v_compare BETWEEN '5AB4' AND '5AE8'
               THEN
                  v_return := v_return || 'Gua';
               WHEN v_compare BETWEEN '5AE9' AND '5AF8'
               THEN
                  v_return := v_return || 'Guai';
               WHEN v_compare BETWEEN '5AFD' AND '5B5E'
               THEN
                  v_return := v_return || 'Guan';
               WHEN v_compare BETWEEN '5B60' AND '5B8C'
               THEN
                  v_return := v_return || 'Guang';
               WHEN v_compare BETWEEN '5B8D' AND '5C2E'
               THEN
                  v_return := v_return || 'Gui';
               WHEN v_compare = '5BC8'
               THEN
                  v_return := v_return || 'Kwi';
               WHEN v_compare BETWEEN '5C30' AND '5C58'
               THEN
                  v_return := v_return || 'Gun';
               WHEN v_compare BETWEEN '5C51' AND '5CB6'
               THEN
                  v_return := v_return || 'Guo';
               WHEN v_compare BETWEEN '5CB8' AND '5CBD'
               THEN
                  v_return := v_return || 'Ha';
               WHEN v_compare BETWEEN '5CC6' AND '5CEC'
               THEN
                  v_return := v_return || 'Hai';
               WHEN v_compare = '5CED'
               THEN
                  v_return := v_return || 'Hal';
               WHEN v_compare BETWEEN '5CEE' AND '5D99'
               THEN
                  v_return := v_return || 'Han';
               WHEN v_compare BETWEEN '5D9D' AND '5DBC'
               THEN
                  v_return := v_return || 'Hang';
               WHEN v_compare BETWEEN '5DBE' AND '5E20'
               THEN
                  v_return := v_return || 'Hao';
               WHEN v_compare = '5E02'
               THEN
                  v_return := v_return || 'Ho';
               WHEN v_compare BETWEEN '5E22' AND '5EC5'
               THEN
                  v_return := v_return || 'He';
               WHEN v_compare BETWEEN '5EC6' AND '5ECE'
               THEN
                  v_return := v_return || 'Hei';
               WHEN v_compare BETWEEN '5ED0' AND '5EDC'
               THEN
                  v_return := v_return || 'Hen';
               WHEN v_compare BETWEEN '5EDD' AND '5F03'
               THEN
                  v_return := v_return || 'Heng';
               WHEN v_compare = '5F04'
               THEN
                  v_return := v_return || 'Hol';
               WHEN v_compare BETWEEN '5F05' AND '5F8D'
               THEN
                  v_return := v_return || 'Hong';
               WHEN v_compare BETWEEN '5F8E' AND '5FD2'
               THEN
                  v_return := v_return || 'Hou';
               WHEN v_compare BETWEEN '5FD4' AND '60B1'
               THEN
                  v_return := v_return || 'Hu';
               WHEN v_compare BETWEEN '60B2' AND '6111'
               THEN
                  v_return := v_return || 'Hua';
               WHEN v_compare BETWEEN '6112' AND '612D'
               THEN
                  v_return := v_return || 'Huai';
               WHEN v_compare BETWEEN '612E' AND '61C6'
               THEN
                  v_return := v_return || 'Huan';
               WHEN v_compare BETWEEN '61CA' AND '624A'
               THEN
                  v_return := v_return || 'Huang';
               WHEN v_compare BETWEEN '624C' AND '6344'
               THEN
                  v_return := v_return || 'Hui';
               WHEN v_compare BETWEEN '6346' AND '6388'
               THEN
                  v_return := v_return || 'Hun';
               WHEN v_compare BETWEEN '638C' AND '63FA'
               THEN
                  v_return := v_return || 'Huo';
               WHEN v_compare = '63FD'
               THEN
                  v_return := v_return || 'Hwa';
               WHEN v_compare BETWEEN '63FE' AND '6601'
               THEN
                  v_return := v_return || 'Ji';
               WHEN v_compare BETWEEN '6604' AND '6691'
               THEN
                  v_return := v_return || 'Jia';
               WHEN v_compare BETWEEN '6692' AND '67F8'
               THEN
                  v_return := v_return || 'Jian';
               WHEN v_compare BETWEEN '67F9' AND '6860'
               THEN
                  v_return := v_return || 'Jiang';
               WHEN v_compare BETWEEN '6862' AND '6930'
               THEN
                  v_return := v_return || 'Jiao';
               WHEN v_compare BETWEEN '6931' AND '6A18'
               THEN
                  v_return := v_return || 'Jie';
               WHEN v_compare BETWEEN '6A1A' AND '6AC9'
               THEN
                  v_return := v_return || 'Jin';
               WHEN v_compare BETWEEN '6ACA' AND '6B65'
               THEN
                  v_return := v_return || 'Jing';
               WHEN v_compare BETWEEN '6B66' AND '6B9A'
               THEN
                  v_return := v_return || 'Jiong';
               WHEN v_compare BETWEEN '6B9C' AND '6C0C'
               THEN
                  v_return := v_return || 'Jiu';
               WHEN v_compare = '6C0D'
               THEN
                  v_return := v_return || 'Jou';
               WHEN v_compare BETWEEN '6C0E' AND '6D2A'
               THEN
                  v_return := v_return || 'Ju';
               WHEN v_compare BETWEEN '6D2D' AND '6D80'
               THEN
                  v_return := v_return || 'Juan';
               WHEN v_compare BETWEEN '6D82' AND '6E28'
               THEN
                  v_return := v_return || 'Jue';
               WHEN v_compare BETWEEN '6E2A' AND '6E85'
               THEN
                  v_return := v_return || 'Jun';
               WHEN v_compare BETWEEN '6E86' AND '6E92'
               THEN
                  v_return := v_return || 'Ka';
               WHEN v_compare BETWEEN '6E94' AND '6EC9'
               THEN
                  v_return := v_return || 'Kai';
               WHEN v_compare = '6ECC'
               THEN
                  v_return := v_return || 'Kal';
               WHEN v_compare BETWEEN '6ECD' AND '6F00'
               THEN
                  v_return := v_return || 'Kan';
               WHEN v_compare BETWEEN '6F02' AND '6F30'
               THEN
                  v_return := v_return || 'Kang';
               WHEN v_compare BETWEEN '6F31' AND '6F4D'
               THEN
                  v_return := v_return || 'Kao';
               WHEN v_compare BETWEEN '6F50' AND '6FC8'
               THEN
                  v_return := v_return || 'Ke';
               WHEN v_compare BETWEEN '6FC9' AND '6FDA'
               THEN
                  v_return := v_return || 'Ken';
               WHEN v_compare BETWEEN '6FDC' AND '6FF5'
               THEN
                  v_return := v_return || 'Keng';
               WHEN v_compare = '6FFC'
               THEN
                  v_return := v_return || 'Ki';
               WHEN v_compare BETWEEN '6FFD' AND '7016'
               THEN
                  v_return := v_return || 'Kong';
               WHEN v_compare = '7018'
               THEN
                  v_return := v_return || 'Kos';
               WHEN v_compare BETWEEN '7019' AND '703E'
               THEN
                  v_return := v_return || 'Kou';
               WHEN v_compare BETWEEN '7041' AND '707A'
               THEN
                  v_return := v_return || 'Ku';
               WHEN v_compare BETWEEN '707C' AND '7095'
               THEN
                  v_return := v_return || 'Kua';
               WHEN v_compare BETWEEN '709A' AND '70C1'
               THEN
                  v_return := v_return || 'Kuai';
               WHEN v_compare BETWEEN '70C2' AND '70D4'
               THEN
                  v_return := v_return || 'Kuan';
               WHEN v_compare BETWEEN '70D8' AND '7128'
               THEN
                  v_return := v_return || 'Kuang';
               WHEN v_compare BETWEEN '7129' AND '71B1'
               THEN
                  v_return := v_return || 'Kui';
               WHEN v_compare BETWEEN '71B2' AND '71FE'
               THEN
                  v_return := v_return || 'Kun';
               WHEN v_compare BETWEEN '7200' AND '7226'
               THEN
                  v_return := v_return || 'Kuo';
               WHEN v_compare = '7228'
               THEN
                  v_return := v_return || 'Kweok';
               WHEN v_compare BETWEEN '722C' AND '726A'
               THEN
                  v_return := v_return || 'La';
               WHEN v_compare BETWEEN '726C' AND '72B5'
               THEN
                  v_return := v_return || 'Lai';
               WHEN v_compare BETWEEN '72B9' AND '733C'
               THEN
                  v_return := v_return || 'Lan';
               WHEN v_compare BETWEEN '733D' AND '7388'
               THEN
                  v_return := v_return || 'Lang';
               WHEN v_compare BETWEEN '7389' AND '73E5'
               THEN
                  v_return := v_return || 'Lao';
               WHEN v_compare BETWEEN '73E8' AND '7402'
               THEN
                  v_return := v_return || 'Le';
               WHEN v_compare BETWEEN '7404' AND '7485'
               THEN
                  v_return := v_return || 'Lei';
               WHEN v_compare BETWEEN '7488' AND '7499'
               THEN
                  v_return := v_return || 'Leng';
               WHEN v_compare BETWEEN '749C' AND '7642'
               THEN
                  v_return := v_return || 'Li';
               WHEN v_compare BETWEEN '7644' AND '7645'
               THEN
                  v_return := v_return || 'Lia';
               WHEN v_compare BETWEEN '7646' AND '76EC'
               THEN
                  v_return := v_return || 'Lian';
               WHEN v_compare BETWEEN '76ED' AND '7731'
               THEN
                  v_return := v_return || 'Liang';
               WHEN v_compare BETWEEN '7732' AND '7794'
               THEN
                  v_return := v_return || 'Liao';
               WHEN v_compare BETWEEN '7795' AND '77E2'
               THEN
                  v_return := v_return || 'Lie';
               WHEN v_compare BETWEEN '77E4' AND '785D'
               THEN
                  v_return := v_return || 'Lin';
               WHEN v_compare = '77EA'
               THEN
                  v_return := v_return || 'Len';
               WHEN v_compare BETWEEN '7860' AND '7904'
               THEN
                  v_return := v_return || 'Ling';
               WHEN v_compare BETWEEN '7905' AND '7986'
               THEN
                  v_return := v_return || 'Liu';
               WHEN v_compare BETWEEN '7988' AND '7989'
               THEN
                  v_return := v_return || 'Lo';
               WHEN v_compare BETWEEN '798A' AND '79FD'
               THEN
                  v_return := v_return || 'Long';
               WHEN v_compare BETWEEN '79FE' AND '7A49'
               THEN
                  v_return := v_return || 'Lou';
               WHEN v_compare BETWEEN '7A4C' AND '7B4D'
               THEN
                  v_return := v_return || 'Lu';
               WHEN v_compare BETWEEN '7B4E' AND '7B80'
               THEN
                  v_return := v_return || 'Luan';
               WHEN v_compare BETWEEN '7B81' AND '7BB2'
               THEN
                  v_return := v_return || 'Lun';
               WHEN v_compare BETWEEN '7BB5' AND '7C25'
               THEN
                  v_return := v_return || 'Luo';
               WHEN v_compare BETWEEN '7C26' AND '7C82'
               THEN
                  v_return := v_return || 'Lv';
               WHEN v_compare BETWEEN '7C84' AND '7C98'
               THEN
                  v_return := v_return || 'Lue';
               WHEN v_compare BETWEEN '7C9C' AND '7CE4'
               THEN
                  v_return := v_return || 'Ma';
               WHEN v_compare BETWEEN '7CE5' AND '7DOC'
               THEN
                  v_return := v_return || 'Mai';
               WHEN v_compare BETWEEN '7D11' AND '7D6E'
               THEN
                  v_return := v_return || 'Man';
               WHEN v_compare BETWEEN '7D70' AND '7DA9'
               THEN
                  v_return := v_return || 'Mang';
               WHEN v_compare BETWEEN '7DAC' AND '7E15'
               THEN
                  v_return := v_return || 'Mao';
               WHEN v_compare = '7E0C'
               THEN
                  v_return := v_return || 'Q';
               WHEN v_compare BETWEEN '7E18' AND '7E1E'
               THEN
                  v_return := v_return || 'Me';
               WHEN v_compare BETWEEN '7E20' AND '7E9A'
               THEN
                  v_return := v_return || 'Mei';
               WHEN v_compare BETWEEN '7E9D' AND '7EC1'
               THEN
                  v_return := v_return || 'Men';
               WHEN v_compare BETWEEN '7EC2' AND '7F36'
               THEN
                  v_return := v_return || 'Meng';
               WHEN v_compare = '7F38'
               THEN
                  v_return := v_return || 'Meo';
               WHEN v_compare BETWEEN '7F39' AND '7FE4'
               THEN
                  v_return := v_return || 'Mi';
               WHEN v_compare BETWEEN '7FE6' AND '8034'
               THEN
                  v_return := v_return || 'Mian';
               WHEN v_compare BETWEEN '8035' AND '805A'
               THEN
                  v_return := v_return || 'Miao';
               WHEN v_compare BETWEEN '805C' AND '8081'
               THEN
                  v_return := v_return || 'Mie';
               WHEN v_compare BETWEEN '8084' AND '80E4'
               THEN
                  v_return := v_return || 'Min';
               WHEN v_compare = '8096'
               THEN
                  v_return := v_return || 'Lem';
               WHEN v_compare BETWEEN '80E5' AND '8116'
               THEN
                  v_return := v_return || 'Ming';
               WHEN v_compare BETWEEN '8119' AND '811D'
               THEN
                  v_return := v_return || 'Miu';
               WHEN v_compare BETWEEN '811E' AND '81A9'
               THEN
                  v_return := v_return || 'Mo';
               WHEN v_compare BETWEEN '81AC' AND '81CC'
               THEN
                  v_return := v_return || 'Mou';
               WHEN v_compare BETWEEN '81CD' AND '821E'
               THEN
                  v_return := v_return || 'Mu';
               WHEN v_compare = '8220'
               THEN
                  v_return := v_return || 'Myeo';
               WHEN v_compare = '8221'
               THEN
                  v_return := v_return || 'Myeon';
               WHEN v_compare = '8222'
               THEN
                  v_return := v_return || 'Myeong';
               WHEN v_compare BETWEEN '8224' AND '8258'
               THEN
                  v_return := v_return || 'Na';
               WHEN v_compare BETWEEN '825D' AND '8285'
               THEN
                  v_return := v_return || 'Nai';
               WHEN v_compare BETWEEN '8289' AND '82B5'
               THEN
                  v_return := v_return || 'Nan';
               WHEN v_compare BETWEEN '82B9' AND '82D0'
               THEN
                  v_return := v_return || 'Nang';
               WHEN v_compare BETWEEN '82D1' AND '8311'
               THEN
                  v_return := v_return || 'Nao';
               WHEN v_compare BETWEEN '8312' AND '8320'
               THEN
                  v_return := v_return || 'Ne';
               WHEN v_compare BETWEEN '8322' AND '8331'
               THEN
                  v_return := v_return || 'Nei';
               WHEN v_compare = '8334'
               THEN
                  v_return := v_return || 'Nem';
               WHEN v_compare = '8336'
               THEN
                  v_return := v_return || 'Nen';
               WHEN v_compare = '8339'
               THEN
                  v_return := v_return || 'Neng';
               WHEN v_compare = '833E'
               THEN
                  v_return := v_return || 'Neus';
               WHEN v_compare = '8342'
               THEN
                  v_return := v_return || 'Ngag';
               WHEN v_compare = '8344'
               THEN
                  v_return := v_return || 'Ngai';
               WHEN v_compare = '8345'
               THEN
                  v_return := v_return || 'Ngam';
               WHEN v_compare BETWEEN '8346' AND '83B9'
               THEN
                  v_return := v_return || 'Ni';
               WHEN v_compare BETWEEN '83BC' AND '83ED'
               THEN
                  v_return := v_return || 'Nian';
               WHEN v_compare BETWEEN '83EE' AND '83F5'
               THEN
                  v_return := v_return || 'Niang';
               WHEN v_compare BETWEEN '83F8' AND '8414'
               THEN
                  v_return := v_return || 'Niao';
               WHEN v_compare BETWEEN '8415' AND '8478'
               THEN
                  v_return := v_return || 'Nie';
               WHEN v_compare BETWEEN '8479' AND '8480'
               THEN
                  v_return := v_return || 'Nin';
               WHEN v_compare BETWEEN '8481' AND '84B4'
               THEN
                  v_return := v_return || 'Ning';
               WHEN v_compare BETWEEN '84B5' AND '84D1'
               THEN
                  v_return := v_return || 'Niu';
               WHEN v_compare BETWEEN '84D4' AND '84FA'
               THEN
                  v_return := v_return || 'Nong';
               WHEN v_compare = '84E8'
               THEN
                  v_return := v_return || 'Nung';
               WHEN v_compare BETWEEN '84FD' AND '850E'
               THEN
                  v_return := v_return || 'Nou';
               WHEN v_compare BETWEEN '8511' AND '8522'
               THEN
                  v_return := v_return || 'Nu';
               WHEN v_compare BETWEEN '8524' AND '852C'
               THEN
                  v_return := v_return || 'Nuan';
               WHEN v_compare = '852D'
               THEN
                  v_return := v_return || 'Nun';
               WHEN v_compare BETWEEN '8530' AND '8559'
               THEN
                  v_return := v_return || 'Nuo';
               WHEN v_compare BETWEEN '855A' AND '8566'
               THEN
                  v_return := v_return || 'Nv';
               WHEN v_compare BETWEEN '856D' AND '8574'
               THEN
                  v_return := v_return || 'Nue';
               WHEN v_compare = '8575'
               THEN
                  v_return := v_return || 'O';
               WHEN v_compare = '8579'
               THEN
                  v_return := v_return || 'Oes';
               WHEN v_compare = '857A'
               THEN
                  v_return := v_return || 'Ol';
               WHEN v_compare = '857C'
               THEN
                  v_return := v_return || 'On';
               WHEN v_compare BETWEEN '857D' AND '85AE'
               THEN
                  v_return := v_return || 'Ou';
               WHEN v_compare BETWEEN '85B1' AND '85C9'
               THEN
                  v_return := v_return || 'Pa';
               WHEN v_compare BETWEEN '85CA' AND '85E4'
               THEN
                  v_return := v_return || 'Pai';
               WHEN v_compare = '85E5'
               THEN
                  v_return := v_return || 'Pak';
               WHEN v_compare BETWEEN '85E8' AND '8625'
               THEN
                  v_return := v_return || 'Pan';
               WHEN v_compare BETWEEN '8626' AND '8658'
               THEN
                  v_return := v_return || 'Pang';
               WHEN v_compare BETWEEN '8659' AND '8688'
               THEN
                  v_return := v_return || 'Pao';
               WHEN v_compare BETWEEN '868A' AND '86C5'
               THEN
                  v_return := v_return || 'Pei';
               WHEN v_compare BETWEEN '86C8' AND '86D6'
               THEN
                  v_return := v_return || 'Pen';
               WHEN v_compare BETWEEN '86D8' AND '8740'
               THEN
                  v_return := v_return || 'Peng';
               WHEN v_compare = '8741'
               THEN
                  v_return := v_return || 'Peol';
               WHEN v_compare = '8742'
               THEN
                  v_return := v_return || 'Phas';
               WHEN v_compare = '8744'
               THEN
                  v_return := v_return || 'Phdeng';
               WHEN v_compare = '8745'
               THEN
                  v_return := v_return || 'Phoi';
               WHEN v_compare = '8746'
               THEN
                  v_return := v_return || 'Phos';
               WHEN v_compare BETWEEN '8748' AND '880D'
               THEN
                  v_return := v_return || 'Pi';
               WHEN v_compare BETWEEN '880E' AND '883A'
               THEN
                  v_return := v_return || 'Pian';
               WHEN v_compare BETWEEN '883C' AND '8869'
               THEN
                  v_return := v_return || 'Piao';
               WHEN v_compare BETWEEN '886D' AND '8879'
               THEN
                  v_return := v_return || 'Pie';
               WHEN v_compare BETWEEN '887A' AND '88A0'
               THEN
                  v_return := v_return || 'Pin';
               WHEN v_compare BETWEEN '88A1' AND '88EC'
               THEN
                  v_return := v_return || 'Ping';
               WHEN v_compare BETWEEN '88F0' AND '8938'
               THEN
                  v_return := v_return || 'Po';
               WHEN v_compare BETWEEN '893E' AND '8958'
               THEN
                  v_return := v_return || 'Pou';
               WHEN v_compare BETWEEN '895A' AND '895C'
               THEN
                  v_return := v_return || 'Ppun';
               WHEN v_compare BETWEEN '895D' AND '89C4'
               THEN
                  v_return := v_return || 'Pu';
               WHEN v_compare BETWEEN '89C5' AND '8B3E'
               THEN
                  v_return := v_return || 'Qi';
               WHEN v_compare BETWEEN '8B41' AND '8B61'
               THEN
                  v_return := v_return || 'Qia';
               WHEN v_compare BETWEEN '8B62' AND '8C54'
               THEN
                  v_return := v_return || 'Qian';
               WHEN v_compare BETWEEN '8C5A' AND '8CB4'
               THEN
                  v_return := v_return || 'Qiang';
               WHEN v_compare BETWEEN '8CB8' AND '8D3D'
               THEN
                  v_return := v_return || 'Qiao';
               WHEN v_compare BETWEEN '8D40' AND '8D7E'
               THEN
                  v_return := v_return || 'Qie';
               WHEN v_compare BETWEEN '8D81' AND '8DFA'
               THEN
                  v_return := v_return || 'Qin';
               WHEN v_compare BETWEEN '8DFC' AND '8E5D'
               THEN
                  v_return := v_return || 'Qing';
               WHEN v_compare BETWEEN '8E5E' AND '8E98'
               THEN
                  v_return := v_return || 'Qiong';
               WHEN v_compare BETWEEN '8E9A' AND '8F2A'
               THEN
                  v_return := v_return || 'Qiu';
               WHEN v_compare BETWEEN '8F2E' AND '8FE9'
               THEN
                  v_return := v_return || 'Qu';
               WHEN v_compare BETWEEN '8FEA' AND '905D'
               THEN
                  v_return := v_return || 'Quan';
               WHEN v_compare BETWEEN '905E' AND '9099'
               THEN
                  v_return := v_return || 'Que';
               WHEN v_compare BETWEEN '909A' AND '90AA'
               THEN
                  v_return := v_return || 'Qun';
               WHEN v_compare BETWEEN '90B0' AND '90B1'
               THEN
                  v_return := v_return || 'Ra';
               WHEN v_compare = '90B2'
               THEN
                  v_return := v_return || 'Ram';
               WHEN v_compare BETWEEN '90B4' AND '90E5'
               THEN
                  v_return := v_return || 'Ran';
               WHEN v_compare BETWEEN '90E6' AND '9104'
               THEN
                  v_return := v_return || 'Rang';
               WHEN v_compare BETWEEN '9105' AND '911C'
               THEN
                  v_return := v_return || 'Rao';
               WHEN v_compare BETWEEN '911D' AND '9120'
               THEN
                  v_return := v_return || 'Re';
               WHEN v_compare BETWEEN '9121' AND '9180'
               THEN
                  v_return := v_return || 'Ren';
               WHEN v_compare BETWEEN '9181' AND '918D'
               THEN
                  v_return := v_return || 'Reng';
               WHEN v_compare BETWEEN '918E' AND '9196'
               THEN
                  v_return := v_return || 'Ri';
               WHEN v_compare BETWEEN '9189' AND '91F1'
               THEN
                  v_return := v_return || 'Rong';
               WHEN v_compare BETWEEN '91F2' AND '9218'
               THEN
                  v_return := v_return || 'Rou';
               WHEN v_compare BETWEEN '9219' AND '9269'
               THEN
                  v_return := v_return || 'Ru';
               WHEN v_compare BETWEEN '926C' AND '9292'
               THEN
                  v_return := v_return || 'Ruan';
               WHEN v_compare BETWEEN '9294' AND '92BD'
               THEN
                  v_return := v_return || 'Rui';
               WHEN v_compare BETWEEN '92BE' AND '92C9'
               THEN
                  v_return := v_return || 'Run';
               WHEN v_compare = '92CA'
               THEN
                  v_return := v_return || 'Rua';
               WHEN v_compare BETWEEN '92CA' AND '92E4'
               THEN
                  v_return := v_return || 'Ruo';
               WHEN v_compare BETWEEN '92E5' AND '9309'
               THEN
                  v_return := v_return || 'Sa';
               WHEN v_compare = '930A'
               THEN
                  v_return := v_return || 'Saeng';
               WHEN v_compare BETWEEN '930C' AND '9325'
               THEN
                  v_return := v_return || 'Sai';
               WHEN v_compare = '9328'
               THEN
                  v_return := v_return || 'Sal';
               WHEN v_compare BETWEEN '9329' AND '9355'
               THEN
                  v_return := v_return || 'San';
               WHEN v_compare BETWEEN '9358' AND '936A'
               THEN
                  v_return := v_return || 'Sang';
               WHEN v_compare BETWEEN '936C' AND '9391'
               THEN
                  v_return := v_return || 'Sao';
               WHEN v_compare BETWEEN '9392' AND '93C5'
               THEN
                  v_return := v_return || 'Se';
               WHEN v_compare = '93C6'
               THEN
                  v_return := v_return || 'Sed';
               WHEN v_compare BETWEEN '93C8' AND '93CC'
               THEN
                  v_return := v_return || 'Sen';
               WHEN v_compare BETWEEN '93CD' AND '93D0'
               THEN
                  v_return := v_return || 'Seng';
               WHEN v_compare = '93D1'
               THEN
                  v_return := v_return || 'Seo';
               WHEN v_compare = '93D2'
               THEN
                  v_return := v_return || 'Seon';
               WHEN v_compare BETWEEN '93D4' AND '941A'
               THEN
                  v_return := v_return || 'Sha';
               WHEN v_compare BETWEEN '941D' AND '9428'
               THEN
                  v_return := v_return || 'Shai';
               WHEN v_compare BETWEEN '9429' AND '94C1'
               THEN
                  v_return := v_return || 'Shan';
               WHEN v_compare BETWEEN '94C2' AND '94EE'
               THEN
                  v_return := v_return || 'Shang';
               WHEN v_compare BETWEEN '94F1' AND '952D'
               THEN
                  v_return := v_return || 'Shao';
               WHEN v_compare BETWEEN '952E' AND '9571'
               THEN
                  v_return := v_return || 'She';
               WHEN v_compare BETWEEN '9574' AND '9602'
               THEN
                  v_return := v_return || 'Shen';
               WHEN v_compare BETWEEN '9604' AND '965C'
               THEN
                  v_return := v_return || 'Sheng';
               WHEN v_compare BETWEEN '965E' AND '9786'
               THEN
                  v_return := v_return || 'Shi';
               WHEN v_compare BETWEEN '9788' AND '97AE'
               THEN
                  v_return := v_return || 'Shou';
               WHEN v_compare BETWEEN '97B0' AND '9878'
               THEN
                  v_return := v_return || 'Shu';
               WHEN v_compare BETWEEN '987A' AND '987E'
               THEN
                  v_return := v_return || 'Shua';
               WHEN v_compare BETWEEN '9880' AND '988A'
               THEN
                  v_return := v_return || 'Shuai';
               WHEN v_compare BETWEEN '988C' AND '9894'
               THEN
                  v_return := v_return || 'Shuan';
               WHEN v_compare BETWEEN '9895' AND '98BE'
               THEN
                  v_return := v_return || 'Shuang';
               WHEN v_compare BETWEEN '98C0' AND '98D6'
               THEN
                  v_return := v_return || 'Shui';
               WHEN v_compare BETWEEN '98DC' AND '98EE'
               THEN
                  v_return := v_return || 'Shun';
               WHEN v_compare BETWEEN '98F1' AND '9911'
               THEN
                  v_return := v_return || 'Shuo';
               WHEN v_compare BETWEEN '9912' AND '99AD'
               THEN
                  v_return := v_return || 'Si';
               WHEN v_compare = '99AE'
               THEN
                  v_return := v_return || 'So';
               WHEN v_compare = '99B0'
               THEN
                  v_return := v_return || 'Sol';
               WHEN v_compare BETWEEN '99B1' AND '99F6'
               THEN
                  v_return := v_return || 'Song';
               WHEN v_compare BETWEEN '99F8' AND '9A36'
               THEN
                  v_return := v_return || 'Sou';
               WHEN v_compare BETWEEN '9A38' AND '9AB6'
               THEN
                  v_return := v_return || 'Su';
               WHEN v_compare BETWEEN '9AB8' AND '9AC4'
               THEN
                  v_return := v_return || 'Suan';
               WHEN v_compare BETWEEN '9AC5' AND '9B3A'
               THEN
                  v_return := v_return || 'Sui';
               WHEN v_compare = '9AF0'
               THEN
                  v_return := v_return || 'Wie';
               WHEN v_compare BETWEEN '9B3C' AND '9B62'
               THEN
                  v_return := v_return || 'Sun';
               WHEN v_compare BETWEEN '9B65' AND '9BA9'
               THEN
                  v_return := v_return || 'Suo';
               WHEN v_compare BETWEEN '9BAA' AND '9C10'
               THEN
                  v_return := v_return || 'Ta';
               WHEN v_compare = '9C11'
               THEN
                  v_return := v_return || 'Tae';
               WHEN v_compare BETWEEN '9C12' AND '9C59'
               THEN
                  v_return := v_return || 'Tai';
               WHEN v_compare BETWEEN '9C5A' AND '9CE0'
               THEN
                  v_return := v_return || 'Tan';
               WHEN v_compare BETWEEN '9CE2' AND '9D55'
               THEN
                  v_return := v_return || 'Tang';
               WHEN v_compare BETWEEN '9D56' AND '9DB4'
               THEN
                  v_return := v_return || 'Tao';
               WHEN v_compare = '9DB6'
               THEN
                  v_return := v_return || 'Tap';
               WHEN v_compare BETWEEN '9DB8' AND '9DC6'
               THEN
                  v_return := v_return || 'Te';
               WHEN v_compare BETWEEN '9DC8' AND '9DED'
               THEN
                  v_return := v_return || 'Teng';
               WHEN v_compare = '9DEE'
               THEN
                  v_return := v_return || 'Teo';
               WHEN v_compare = '9DF0'
               THEN
                  v_return := v_return || 'Teul';
               WHEN v_compare BETWEEN '9DF1' AND '9E82'
               THEN
                  v_return := v_return || 'Ti';
               WHEN v_compare BETWEEN '9E85' AND '9EED'
               THEN
                  v_return := v_return || 'Tian';
               WHEN v_compare BETWEEN '9EEE' AND '9F38'
               THEN
                  v_return := v_return || 'Tiao';
               WHEN v_compare BETWEEN '9F39' AND '9F56'
               THEN
                  v_return := v_return || 'Tie';
               WHEN v_compare BETWEEN '9F59' AND '9FAE'
               THEN
                  v_return := v_return || 'Ting';
               WHEN v_compare = '9FB0'
               THEN
                  v_return := v_return || 'Tol';
               WHEN v_compare BETWEEN '9FB1' AND 'A015'
               THEN
                  v_return := v_return || 'Tong';
               WHEN v_compare BETWEEN 'A016' AND 'A03A'
               THEN
                  v_return := v_return || 'Tou';
               WHEN v_compare BETWEEN 'A040' AND 'A0A9'
               THEN
                  v_return := v_return || 'Tu';
               WHEN v_compare BETWEEN 'A0AA' AND 'A0D5'
               THEN
                  v_return := v_return || 'Tuan';
               WHEN v_compare BETWEEN 'A0D6' AND 'A106'
               THEN
                  v_return := v_return || 'Tui';
               WHEN v_compare BETWEEN 'A108' AND 'A131'
               THEN
                  v_return := v_return || 'Tun';
               WHEN v_compare BETWEEN 'A134' AND 'A1AE'
               THEN
                  v_return := v_return || 'Tuo';
               WHEN v_compare BETWEEN 'A1B0' AND 'A1E8'
               THEN
                  v_return := v_return || 'Wa';
               WHEN v_compare BETWEEN 'A1E9' AND 'A1F5'
               THEN
                  v_return := v_return || 'Wai';
               WHEN v_compare BETWEEN 'A1F8' AND 'A279'
               THEN
                  v_return := v_return || 'Wan';
               WHEN v_compare BETWEEN 'A27A' AND 'A2B9'
               THEN
                  v_return := v_return || 'Wang';
               WHEN v_compare BETWEEN 'A2BC' AND 'A408'
               THEN
                  v_return := v_return || 'Wei';
               WHEN v_compare BETWEEN 'A40D' AND 'A47C'
               THEN
                  v_return := v_return || 'Wen';
               WHEN v_compare BETWEEN 'A47D' AND 'A4A2'
               THEN
                  v_return := v_return || 'Weng';
               WHEN v_compare BETWEEN 'A4A4' AND 'A4EA'
               THEN
                  v_return := v_return || 'Wo';
               WHEN v_compare BETWEEN 'A4EC' AND 'A5D4'
               THEN
                  v_return := v_return || 'Wu';
               WHEN v_compare BETWEEN 'A5D6' AND 'A784'
               THEN
                  v_return := v_return || 'Xi';
               WHEN v_compare BETWEEN 'A785' AND 'A7FA'
               THEN
                  v_return := v_return || 'Xia';
               WHEN v_compare BETWEEN 'A7FD' AND 'A951'
               THEN
                  v_return := v_return || 'Xian';
               WHEN v_compare BETWEEN 'A954' AND 'A9CE'
               THEN
                  v_return := v_return || 'Xiang';
               WHEN v_compare BETWEEN 'A9D0' AND 'AA8A'
               THEN
                  v_return := v_return || 'Xiao';
               WHEN v_compare BETWEEN 'AA8D' AND 'AB7E'
               THEN
                  v_return := v_return || 'Xie';
               WHEN v_compare BETWEEN 'AB80' AND 'ABEE'
               THEN
                  v_return := v_return || 'Xin';
               WHEN v_compare BETWEEN 'ABF0' AND 'AC41'
               THEN
                  v_return := v_return || 'Xing';
               WHEN v_compare BETWEEN 'AC42' AND 'AC64'
               THEN
                  v_return := v_return || 'Xiong';
               WHEN v_compare BETWEEN 'AC65' AND 'ACBA'
               THEN
                  v_return := v_return || 'Xiu';
               WHEN v_compare BETWEEN 'ACBC' AND 'AD90'
               THEN
                  v_return := v_return || 'Xu';
               WHEN v_compare = 'ACD9'
               THEN
                  v_return := v_return || 'Chua';
               WHEN v_compare BETWEEN 'AD91' AND 'AE32'
               THEN
                  v_return := v_return || 'Xuan';
               WHEN v_compare BETWEEN 'AE34' AND 'AE89'
               THEN
                  v_return := v_return || 'Xue';
               WHEN v_compare BETWEEN 'AE8C' AND 'AF1E'
               THEN
                  v_return := v_return || 'Xun';
               WHEN v_compare BETWEEN 'AF20' AND 'AF96'
               THEN
                  v_return := v_return || 'Ya';
               WHEN v_compare BETWEEN 'AF98' AND 'B118'
               THEN
                  v_return := v_return || 'Yan';
               WHEN v_compare = 'B030'
               THEN
                  v_return := v_return || 'Eom';
               WHEN v_compare BETWEEN 'B11A' AND 'B1A8'
               THEN
                  v_return := v_return || 'Yang';
               WHEN v_compare BETWEEN 'B1AD' AND 'B275'
               THEN
                  v_return := v_return || 'Yao';
               WHEN v_compare BETWEEN 'B276' AND 'B30A'
               THEN
                  v_return := v_return || 'Ye';
               WHEN v_compare BETWEEN 'B30D' AND 'B30E'
               THEN
                  v_return := v_return || 'Yen';
               WHEN v_compare BETWEEN 'B310' AND 'B594'
               THEN
                  v_return := v_return || 'Yi';
               WHEN v_compare = 'B359'
               THEN
                  v_return := v_return || 'I';
               WHEN v_compare BETWEEN 'B596' AND 'B684'
               THEN
                  v_return := v_return || 'Yin';
               WHEN v_compare BETWEEN 'B685' AND 'B768'
               THEN
                  v_return := v_return || 'Ying';
               WHEN v_compare BETWEEN 'B76C' AND 'B76E'
               THEN
                  v_return := v_return || 'Yo';
               WHEN v_compare BETWEEN 'B770' AND 'B7EA'
               THEN
                  v_return := v_return || 'Yong';
               WHEN v_compare BETWEEN 'B7EC' AND 'B8B2'
               THEN
                  v_return := v_return || 'You';
               WHEN v_compare BETWEEN 'B8B5' AND 'BA98'
               THEN
                  v_return := v_return || 'Yu';
               WHEN v_compare BETWEEN 'BA99' AND 'BB58'
               THEN
                  v_return := v_return || 'Yuan';
               WHEN v_compare BETWEEN 'BB59' AND 'BBBE'
               THEN
                  v_return := v_return || 'Yue';
               WHEN v_compare BETWEEN 'BBC1' AND 'BC58'
               THEN
                  v_return := v_return || 'Yun';
               WHEN v_compare BETWEEN 'BC59' AND 'BC7E'
               THEN
                  v_return := v_return || 'Za';
               WHEN v_compare BETWEEN 'BC81' AND 'BCA8'
               THEN
                  v_return := v_return || 'Zai';
               WHEN v_compare BETWEEN 'BCAA' AND 'BCEA'
               THEN
                  v_return := v_return || 'Zan';
               WHEN v_compare BETWEEN 'BCEE' AND 'BD0A'
               THEN
                  v_return := v_return || 'Zang';
               WHEN v_compare BETWEEN 'BD0C' AND 'BD46'
               THEN
                  v_return := v_return || 'Zao';
               WHEN v_compare BETWEEN 'BD48' AND 'BD99'
               THEN
                  v_return := v_return || 'Ze';
               WHEN v_compare BETWEEN 'BD9A' AND 'BDA2'
               THEN
                  v_return := v_return || 'Zei';
               WHEN v_compare BETWEEN 'BDA5' AND 'BDAC'
               THEN
                  v_return := v_return || 'Zen';
               WHEN v_compare BETWEEN 'BDAD' AND 'BDCC'
               THEN
                  v_return := v_return || 'Zeng';
               WHEN v_compare BETWEEN 'BDCE' AND 'BE40'
               THEN
                  v_return := v_return || 'Zha';
               WHEN v_compare = 'BDF8'
               THEN
                  v_return := v_return || 'Gad';
               WHEN v_compare BETWEEN 'BE41' AND 'BE62'
               THEN
                  v_return := v_return || 'Zhai';
               WHEN v_compare BETWEEN 'BE65' AND 'BEF4'
               THEN
                  v_return := v_return || 'Zhan';
               WHEN v_compare BETWEEN 'BEF6' AND 'BF3E'
               THEN
                  v_return := v_return || 'Zhang';
               WHEN v_compare BETWEEN 'BF40' AND 'BF8C'
               THEN
                  v_return := v_return || 'Zhao';
               WHEN v_compare BETWEEN 'BF8E' AND 'BFF8'
               THEN
                  v_return := v_return || 'Zhe';
               WHEN v_compare BETWEEN 'BFF9' AND 'C0B2'
               THEN
                  v_return := v_return || 'Zhen';
               WHEN v_compare BETWEEN 'C0B4' AND 'C11E'
               THEN
                  v_return := v_return || 'Zheng';
               WHEN v_compare BETWEEN 'C122' AND 'C2C4'
               THEN
                  v_return := v_return || 'Zhi';
               WHEN v_compare BETWEEN 'C2C5' AND 'C31A'
               THEN
                  v_return := v_return || 'Zhong';
               WHEN v_compare BETWEEN 'C31D' AND 'C39A'
               THEN
                  v_return := v_return || 'Zhou';
               WHEN v_compare BETWEEN 'C39C' AND 'C47C'
               THEN
                  v_return := v_return || 'Zhu';
               WHEN v_compare BETWEEN 'C47D' AND 'C484'
               THEN
                  v_return := v_return || 'Zhua';
               WHEN v_compare BETWEEN 'C485' AND 'C486'
               THEN
                  v_return := v_return || 'Zhuai';
               WHEN v_compare BETWEEN 'C488' AND 'C4C0'
               THEN
                  v_return := v_return || 'Zhuan';
               WHEN v_compare BETWEEN 'C4C2' AND 'C4E5'
               THEN
                  v_return := v_return || 'Zhuang';
               WHEN v_compare BETWEEN 'C4E6' AND 'C51C'
               THEN
                  v_return := v_return || 'Zhui';
               WHEN v_compare BETWEEN 'C51D' AND 'C530'
               THEN
                  v_return := v_return || 'Zhun';
               WHEN v_compare BETWEEN 'C534' AND 'C5A5'
               THEN
                  v_return := v_return || 'Zhuo';
               WHEN v_compare BETWEEN 'C5A8' AND 'C648'
               THEN
                  v_return := v_return || 'Zi';
               WHEN v_compare = 'C64A'
               THEN
                  v_return := v_return || 'Zo';
               WHEN v_compare BETWEEN 'C64C' AND 'C6B5'
               THEN
                  v_return := v_return || 'Zong';
               WHEN v_compare BETWEEN 'C6B6' AND 'C6D6'
               THEN
                  v_return := v_return || 'Zou';
               WHEN v_compare BETWEEN 'C6E1' AND 'C714'
               THEN
                  v_return := v_return || 'Zu';
               WHEN v_compare BETWEEN 'C715' AND 'C72D'
               THEN
                  v_return := v_return || 'Zuan';
               WHEN v_compare BETWEEN 'C72E' AND 'C75E'
               THEN
                  v_return := v_return || 'Zui';
               WHEN v_compare BETWEEN 'C760' AND 'C776'
               THEN
                  v_return := v_return || 'Zun';
               WHEN v_compare BETWEEN 'C77A' AND 'C7B4'
               THEN
                  v_return := v_return || 'Zuo';
               ELSE
                  v_return := v_return || v_substr;
            END CASE;
         END LOOP;
      WHEN 3
      THEN                                                             --首字母小写
         FOR i IN 1 .. v_length
         LOOP
            v_substr := SUBSTR (p_str, i, 1);
            v_compare := fn_nlssort (v_substr);
 
            CASE
               WHEN v_compare BETWEEN '3B29' AND '3C82'
               THEN
                  v_return := v_return || 'a';
               WHEN v_compare BETWEEN '3C84' AND '427C'
               THEN
                  v_return := v_return || 'b';
               WHEN v_compare BETWEEN '427D' AND '4BA9'
               THEN
                  v_return := v_return || 'c';
               WHEN v_compare BETWEEN '4BAD' AND '5200'
               THEN
                  v_return := v_return || 'd';
               WHEN v_compare BETWEEN '5205' AND '5332'
               THEN
                  v_return := v_return || 'e';
               WHEN v_compare BETWEEN '5334' AND '5739'
               THEN
                  v_return := v_return || 'f';
               WHEN v_compare BETWEEN '573C' AND '5CB6'
               THEN
                  v_return := v_return || 'g';
               WHEN v_compare BETWEEN '5CB8' AND '63FA'
               THEN
                  v_return := v_return || 'h';
               WHEN v_compare = 'B359'
               THEN
                  v_return := v_return || 'i';
               WHEN v_compare BETWEEN '63FE' AND '6E85'
               THEN
                  v_return := v_return || 'j';
               WHEN v_compare BETWEEN '5BC8' AND '7226'
               THEN
                  v_return := v_return || 'k';
               WHEN v_compare BETWEEN '722C' AND '7C98'
               THEN
                  v_return := v_return || 'l';
               WHEN v_compare BETWEEN '569D' AND '821E'
               THEN
                  v_return := v_return || 'm';
               WHEN v_compare BETWEEN '8224' AND '8574'
               THEN
                  v_return := v_return || 'n';
               WHEN v_compare BETWEEN '8575' AND '85AE'
               THEN
                  v_return := v_return || 'o';
               WHEN v_compare BETWEEN '85B1' AND '89C4'
               THEN
                  v_return := v_return || 'p';
               WHEN v_compare BETWEEN '7E0C' AND '90AA'
               THEN
                  v_return := v_return || 'q';
               WHEN v_compare BETWEEN '90B0' AND '92E4'
               THEN
                  v_return := v_return || 'r';
               WHEN v_compare BETWEEN '92E5' AND '9BA9'
               THEN
                  v_return := v_return || 's';
               WHEN v_compare BETWEEN '5160' AND 'A1AE'
               THEN
                  v_return := v_return || 't';
               WHEN v_compare BETWEEN '9AF0' AND 'A5D4'
               THEN
                  v_return := v_return || 'w';
               WHEN v_compare BETWEEN 'A5D6' AND 'AF1E'
               THEN
                  v_return := v_return || 'x';
               WHEN v_compare BETWEEN 'AF20' AND 'BC58'
               THEN
                  v_return := v_return || 'y';
               WHEN v_compare BETWEEN 'BC59' AND 'C7B4'
               THEN
                  v_return := v_return || 'z';
               ELSE
                  v_return := v_return || v_substr;
            END CASE;
         END LOOP;
      WHEN 4
      THEN                                                             --首字母大写
         FOR i IN 1 .. v_length
         LOOP
            v_substr := SUBSTR (p_str, i, 1);
            v_compare := fn_nlssort (v_substr);
 
            CASE
               WHEN v_compare BETWEEN '3B29' AND '3C82'
               THEN
                  v_return := v_return || 'A';
               WHEN v_compare BETWEEN '3C84' AND '427C'
               THEN
                  v_return := v_return || 'B';
               WHEN v_compare BETWEEN '427D' AND '4BA9'
               THEN
                  v_return := v_return || 'C';
               WHEN v_compare BETWEEN '4BAD' AND '5200'
               THEN
                  v_return := v_return || 'D';
               WHEN v_compare BETWEEN '5205' AND '5332'
               THEN
                  v_return := v_return || 'E';
               WHEN v_compare BETWEEN '5334' AND '5739'
               THEN
                  v_return := v_return || 'F';
               WHEN v_compare BETWEEN '573C' AND '5CB6'
               THEN
                  v_return := v_return || 'G';
               WHEN v_compare BETWEEN '5CB8' AND '63FA'
               THEN
                  v_return := v_return || 'H';
               WHEN v_compare = 'B359'
               THEN
                  v_return := v_return || 'I';
               WHEN v_compare BETWEEN '63FE' AND '6E85'
               THEN
                  v_return := v_return || 'J';
               WHEN v_compare BETWEEN '5BC8' AND '7226'
               THEN
                  v_return := v_return || 'K';
               WHEN v_compare BETWEEN '722C' AND '7C98'
               THEN
                  v_return := v_return || 'L';
               WHEN v_compare BETWEEN '569D' AND '821E'
               THEN
                  v_return := v_return || 'M';
               WHEN v_compare BETWEEN '8224' AND '8574'
               THEN
                  v_return := v_return || 'N';
               WHEN v_compare BETWEEN '8575' AND '85AE'
               THEN
                  v_return := v_return || 'O';
               WHEN v_compare BETWEEN '85B1' AND '89C4'
               THEN
                  v_return := v_return || 'P';
               WHEN v_compare BETWEEN '7E0C' AND '90AA'
               THEN
                  v_return := v_return || 'Q';
               WHEN v_compare BETWEEN '90B0' AND '92E4'
               THEN
                  v_return := v_return || 'R';
               WHEN v_compare BETWEEN '92E5' AND '9BA9'
               THEN
                  v_return := v_return || 'S';
               WHEN v_compare BETWEEN '5160' AND 'A1AE'
               THEN
                  v_return := v_return || 'T';
               WHEN v_compare BETWEEN '9AF0' AND 'A5D4'
               THEN
                  v_return := v_return || 'W';
               WHEN v_compare BETWEEN 'A5D6' AND 'AF1E'
               THEN
                  v_return := v_return || 'X';
               WHEN v_compare BETWEEN 'AF20' AND 'BC58'
               THEN
                  v_return := v_return || 'Y';
               WHEN v_compare BETWEEN 'BC59' AND 'C7B4'
               THEN
                  v_return := v_return || 'Z';
               ELSE
                  v_return := v_return || v_substr;
            END CASE;
         END LOOP;
      ELSE                                                              --全拼小写
         FOR i IN 1 .. v_length
         LOOP
            v_substr := SUBSTR (p_str, i, 1);
            v_compare := fn_nlssort (v_substr);
 
            CASE
               WHEN v_compare BETWEEN '3B29' AND '3B30'
               THEN
                  v_return := v_return || 'a';
               WHEN v_compare = '3B31'
               THEN
                  v_return := v_return || 'aes';
               WHEN v_compare BETWEEN '3B32' AND '3B9E'
               THEN
                  v_return := v_return || 'ai';
               WHEN v_compare BETWEEN '3BA0' AND '3BFE'
               THEN
                  v_return := v_return || 'an';
               WHEN v_compare BETWEEN '3C01' AND '3C14'
               THEN
                  v_return := v_return || 'ang';
               WHEN v_compare BETWEEN '3C15' AND '3C82'
               THEN
                  v_return := v_return || 'ao';
               WHEN v_compare BETWEEN '3C84' AND '3CE9'
               THEN
                  v_return := v_return || 'ba';
               WHEN v_compare BETWEEN '3CED' AND '3D1D'
               THEN
                  v_return := v_return || 'bai';
               WHEN v_compare BETWEEN '3D20' AND '3D64'
               THEN
                  v_return := v_return || 'ban';
               WHEN v_compare BETWEEN '3D66' AND '3DA2'
               THEN
                  v_return := v_return || 'bang';
               WHEN v_compare BETWEEN '3DA4' AND '3E10'
               THEN
                  v_return := v_return || 'bao';
               WHEN v_compare = '3E11'
               THEN
                  v_return := v_return || 'be';
               WHEN v_compare BETWEEN '3E12' AND '3E7A'
               THEN
                  v_return := v_return || 'bei';
               WHEN v_compare BETWEEN '3E7C' AND '3EA0'
               THEN
                  v_return := v_return || 'ben';
               WHEN v_compare BETWEEN '3EA1' AND '3ED5'
               THEN
                  v_return := v_return || 'beng';
               WHEN v_compare BETWEEN '3ED8' AND '3FE9'
               THEN
                  v_return := v_return || 'bi';
               WHEN v_compare BETWEEN '3FEA' AND '4055'
               THEN
                  v_return := v_return || 'bian';
               WHEN v_compare BETWEEN '4058' AND '40AE'
               THEN
                  v_return := v_return || 'biao';
               WHEN v_compare = '4060'
               THEN
                  v_return := v_return || 'bia';
               WHEN v_compare BETWEEN '40B4' AND '40D4'
               THEN
                  v_return := v_return || 'bie';
               WHEN v_compare BETWEEN '40D6' AND '4116'
               THEN
                  v_return := v_return || 'bin';
               WHEN v_compare BETWEEN '4118' AND '4160'
               THEN
                  v_return := v_return || 'bing';
               WHEN v_compare BETWEEN '4161' AND '4224'
               THEN
                  v_return := v_return || 'bo';
               WHEN v_compare BETWEEN '4225' AND '427C'
               THEN
                  v_return := v_return || 'bu';
               WHEN v_compare BETWEEN '427D' AND '4289'
               THEN
                  v_return := v_return || 'ca';
               WHEN v_compare BETWEEN '428C' AND '42B5'
               THEN
                  v_return := v_return || 'cai';
               WHEN v_compare BETWEEN '42B9' AND '430C'
               THEN
                  v_return := v_return || 'can';
               WHEN v_compare BETWEEN '430D' AND '4334'
               THEN
                  v_return := v_return || 'cang';
               WHEN v_compare BETWEEN '4335' AND '435C'
               THEN
                  v_return := v_return || 'cao';
               WHEN v_compare BETWEEN '435D' AND '438C'
               THEN
                  v_return := v_return || 'ce';
               WHEN v_compare BETWEEN '4390' AND '4398'
               THEN
                  v_return := v_return || 'cen';
               WHEN v_compare BETWEEN '439D' AND '43AA'
               THEN
                  v_return := v_return || 'ceng';
               WHEN v_compare = '43AC'
               THEN
                  v_return := v_return || 'ceok';
               WHEN v_compare = '43AE'
               THEN
                  v_return := v_return || 'ceom';
               WHEN v_compare = '43B0'
               THEN
                  v_return := v_return || 'ceon';
               WHEN v_compare = '43B1'
               THEN
                  v_return := v_return || 'ceor';
               WHEN v_compare BETWEEN '43B2' AND '440A'
               THEN
                  v_return := v_return || 'cha';
               WHEN v_compare BETWEEN '440E' AND '442D'
               THEN
                  v_return := v_return || 'chai';
               WHEN v_compare BETWEEN '4431' AND '44E1'
               THEN
                  v_return := v_return || 'chan';
               WHEN v_compare BETWEEN '44E4' AND '4552'
               THEN
                  v_return := v_return || 'chang';
               WHEN v_compare BETWEEN '4554' AND '458E'
               THEN
                  v_return := v_return || 'chao';
               WHEN v_compare BETWEEN '4590' AND '45C8'
               THEN
                  v_return := v_return || 'che';
               WHEN v_compare BETWEEN '45C9' AND '463D'
               THEN
                  v_return := v_return || 'chen';
               WHEN v_compare BETWEEN '463E' AND '46CD'
               THEN
                  v_return := v_return || 'cheng';
               WHEN v_compare BETWEEN '46CE' AND '47A6'
               THEN
                  v_return := v_return || 'chi';
               WHEN v_compare BETWEEN '47A8' AND '47EC'
               THEN
                  v_return := v_return || 'chong';
               WHEN v_compare BETWEEN '47ED' AND '484C'
               THEN
                  v_return := v_return || 'chou';
               WHEN v_compare BETWEEN '484D' AND '48E2'
               THEN
                  v_return := v_return || 'chu';
               WHEN v_compare BETWEEN '48E9' AND '48F4'
               THEN
                  v_return := v_return || 'chuai';
               WHEN v_compare BETWEEN '48F6' AND '4924'
               THEN
                  v_return := v_return || 'chuan';
               WHEN v_compare BETWEEN '4925' AND '4951'
               THEN
                  v_return := v_return || 'chuang';
               WHEN v_compare BETWEEN '4954' AND '496E'
               THEN
                  v_return := v_return || 'chui';
               WHEN v_compare BETWEEN '4971' AND '49C6'
               THEN
                  v_return := v_return || 'chun';
               WHEN v_compare BETWEEN '49C8' AND '49EA'
               THEN
                  v_return := v_return || 'chuo';
               WHEN v_compare BETWEEN '49EC' AND '4A4A'
               THEN
                  v_return := v_return || 'ci';
               WHEN v_compare = '4A50'
               THEN
                  v_return := v_return || 'cis';
               WHEN v_compare BETWEEN '4A51' AND '4AB2'
               THEN
                  v_return := v_return || 'cong';
               WHEN v_compare BETWEEN '4AB4' AND '4ABA'
               THEN
                  v_return := v_return || 'cou';
               WHEN v_compare BETWEEN '4ABC' AND '4AEA'
               THEN
                  v_return := v_return || 'cu';
               WHEN v_compare BETWEEN '4AEE' AND '4B0C'
               THEN
                  v_return := v_return || 'cuan';
               WHEN v_compare BETWEEN '4B0D' AND '4B56'
               THEN
                  v_return := v_return || 'cui';
               WHEN v_compare BETWEEN '4B59' AND '4B6C'
               THEN
                  v_return := v_return || 'cun';
               WHEN v_compare BETWEEN '4B70' AND '4BA9'
               THEN
                  v_return := v_return || 'cuo';
               WHEN v_compare BETWEEN '4BAD' AND '4BFE'
               THEN
                  v_return := v_return || 'da';
               WHEN v_compare BETWEEN '4C00' AND '4C4E'
               THEN
                  v_return := v_return || 'dai';
               WHEN v_compare BETWEEN '4C50' AND '4CDC'
               THEN
                  v_return := v_return || 'dan';
               WHEN v_compare BETWEEN '4CDE' AND '4D26'
               THEN
                  v_return := v_return || 'dang';
               WHEN v_compare BETWEEN '4D28' AND '4D76'
               THEN
                  v_return := v_return || 'dao';
               WHEN v_compare BETWEEN '4D7E' AND '4D8D'
               THEN
                  v_return := v_return || 'de';
               WHEN v_compare = '4D8E'
               THEN
                  v_return := v_return || 'dem';
               WHEN v_compare BETWEEN '4D90' AND '4D91'
               THEN
                  v_return := v_return || 'den';
               WHEN v_compare BETWEEN '4D94' AND '4DC0'
               THEN
                  v_return := v_return || 'deng';
               WHEN v_compare BETWEEN '4DC4' AND '4E8A'
               THEN
                  v_return := v_return || 'di';
               WHEN v_compare = '4E8C'
               THEN
                  v_return := v_return || 'dia';
               WHEN v_compare BETWEEN '4E8D' AND '4EE8'
               THEN
                  v_return := v_return || 'dian';
               WHEN v_compare BETWEEN '4EE9' AND '4F38'
               THEN
                  v_return := v_return || 'diao';
               WHEN v_compare BETWEEN '4F39' AND '4F90'
               THEN
                  v_return := v_return || 'die';
               WHEN v_compare = '4F8D'
               THEN
                  v_return := v_return || 'dei';
               WHEN v_compare = '4F91'
               THEN
                  v_return := v_return || 'dim';
               WHEN v_compare BETWEEN '4F92' AND '4FCD'
               THEN
                  v_return := v_return || 'ding';
               WHEN v_compare BETWEEN '4FCD' AND '4FD4'
               THEN
                  v_return := v_return || 'diu';
               WHEN v_compare BETWEEN '4FD5' AND '5032'
               THEN
                  v_return := v_return || 'dong';
               WHEN v_compare BETWEEN '5034' AND '507C'
               THEN
                  v_return := v_return || 'dou';
               WHEN v_compare = '5044'
               THEN
                  v_return := v_return || 'dul';
               WHEN v_compare BETWEEN '507E' AND '50E9'
               THEN
                  v_return := v_return || 'du';
               WHEN v_compare BETWEEN '50EA' AND '5110'
               THEN
                  v_return := v_return || 'duan';
               WHEN v_compare BETWEEN '5114' AND '514E'
               THEN
                  v_return := v_return || 'dui';
               WHEN v_compare BETWEEN '5152' AND '518D'
               THEN
                  v_return := v_return || 'dun';
               WHEN v_compare = '5160'
               THEN
                  v_return := v_return || 'ton';
               WHEN v_compare BETWEEN '518E' AND '5200'
               THEN
                  v_return := v_return || 'duo';
               WHEN v_compare BETWEEN '5205' AND '52C2'
               THEN
                  v_return := v_return || 'e';
               WHEN v_compare BETWEEN '52C4' AND '52CD'
               THEN
                  v_return := v_return || 'en';
               WHEN v_compare = '52D4'
               THEN
                  v_return := v_return || 'eng';
               WHEN v_compare = '52D5'
               THEN
                  v_return := v_return || 'eo';
               WHEN v_compare = '52D6'
               THEN
                  v_return := v_return || 'eol';
               WHEN v_compare = '52D8'
               THEN
                  v_return := v_return || 'eos';
               WHEN v_compare BETWEEN '52D9' AND '5332'
               THEN
                  v_return := v_return || 'er';
               WHEN v_compare BETWEEN '5334' AND '5366'
               THEN
                  v_return := v_return || 'fa';
               WHEN v_compare BETWEEN '536A' AND '53FA'
               THEN
                  v_return := v_return || 'fan';
               WHEN v_compare BETWEEN '53FD' AND '5438'
               THEN
                  v_return := v_return || 'fang';
               WHEN v_compare BETWEEN '5439' AND '54B2'
               THEN
                  v_return := v_return || 'fei';
               WHEN v_compare BETWEEN '54B4' AND '5528'
               THEN
                  v_return := v_return || 'fen';
               WHEN v_compare BETWEEN '5529' AND '55A9'
               THEN
                  v_return := v_return || 'feng';
               WHEN v_compare BETWEEN '55AA' AND '55AE'
               THEN
                  v_return := v_return || 'fo';
               WHEN v_compare BETWEEN '55B1' AND '55BC'
               THEN
                  v_return := v_return || 'fou';
               WHEN v_compare BETWEEN '55BD' AND '5739'
               THEN
                  v_return := v_return || 'fu';
               WHEN v_compare = '569D'
               THEN
                  v_return := v_return || 'm';
               WHEN v_compare BETWEEN '573C' AND '574C'
               THEN
                  v_return := v_return || 'ga';
               WHEN v_compare BETWEEN '574D' AND '578C'
               THEN
                  v_return := v_return || 'gai';
               WHEN v_compare BETWEEN '578D' AND '57F0'
               THEN
                  v_return := v_return || 'gan';
               WHEN v_compare BETWEEN '57F1' AND '582C'
               THEN
                  v_return := v_return || 'gang';
               WHEN v_compare BETWEEN '582E' AND '5884'
               THEN
                  v_return := v_return || 'gao';
               WHEN v_compare BETWEEN '5885' AND '5905'
               THEN
                  v_return := v_return || 'ge';
               WHEN v_compare = '5906'
               THEN
                  v_return := v_return || 'gei';
               WHEN v_compare BETWEEN '5909' AND '5915'
               THEN
                  v_return := v_return || 'gen';
               WHEN v_compare BETWEEN '5918' AND '594E'
               THEN
                  v_return := v_return || 'geng';
               WHEN v_compare = '5956'
               THEN
                  v_return := v_return || 'gib';
               WHEN v_compare = '5958'
               THEN
                  v_return := v_return || 'go';
               WHEN v_compare BETWEEN '5959' AND '59BA'
               THEN
                  v_return := v_return || 'gong';
               WHEN v_compare BETWEEN '59BD' AND '5A0E'
               THEN
                  v_return := v_return || 'gou';
               WHEN v_compare BETWEEN '5A10' AND '5AB2'
               THEN
                  v_return := v_return || 'gu';
               WHEN v_compare BETWEEN '5AB4' AND '5AE8'
               THEN
                  v_return := v_return || 'gua';
               WHEN v_compare BETWEEN '5AE9' AND '5AF8'
               THEN
                  v_return := v_return || 'guai';
               WHEN v_compare BETWEEN '5AFD' AND '5B5E'
               THEN
                  v_return := v_return || 'guan';
               WHEN v_compare BETWEEN '5B60' AND '5B8C'
               THEN
                  v_return := v_return || 'guang';
               WHEN v_compare BETWEEN '5B8D' AND '5C2E'
               THEN
                  v_return := v_return || 'gui';
               WHEN v_compare = '5BC8'
               THEN
                  v_return := v_return || 'kwi';
               WHEN v_compare BETWEEN '5C30' AND '5C58'
               THEN
                  v_return := v_return || 'gun';
               WHEN v_compare BETWEEN '5C51' AND '5CB6'
               THEN
                  v_return := v_return || 'guo';
               WHEN v_compare BETWEEN '5CB8' AND '5CBD'
               THEN
                  v_return := v_return || 'ha';
               WHEN v_compare BETWEEN '5CC6' AND '5CEC'
               THEN
                  v_return := v_return || 'hai';
               WHEN v_compare = '5CED'
               THEN
                  v_return := v_return || 'hal';
               WHEN v_compare BETWEEN '5CEE' AND '5D99'
               THEN
                  v_return := v_return || 'han';
               WHEN v_compare BETWEEN '5D9D' AND '5DBC'
               THEN
                  v_return := v_return || 'hang';
               WHEN v_compare BETWEEN '5DBE' AND '5E20'
               THEN
                  v_return := v_return || 'hao';
               WHEN v_compare = '5E02'
               THEN
                  v_return := v_return || 'ho';
               WHEN v_compare BETWEEN '5E22' AND '5EC5'
               THEN
                  v_return := v_return || 'he';
               WHEN v_compare BETWEEN '5EC6' AND '5ECE'
               THEN
                  v_return := v_return || 'hei';
               WHEN v_compare BETWEEN '5ED0' AND '5EDC'
               THEN
                  v_return := v_return || 'hen';
               WHEN v_compare BETWEEN '5EDD' AND '5F03'
               THEN
                  v_return := v_return || 'heng';
               WHEN v_compare = '5F04'
               THEN
                  v_return := v_return || 'hol';
               WHEN v_compare BETWEEN '5F05' AND '5F8D'
               THEN
                  v_return := v_return || 'hong';
               WHEN v_compare BETWEEN '5F8E' AND '5FD2'
               THEN
                  v_return := v_return || 'hou';
               WHEN v_compare BETWEEN '5FD4' AND '60B1'
               THEN
                  v_return := v_return || 'hu';
               WHEN v_compare BETWEEN '60B2' AND '6111'
               THEN
                  v_return := v_return || 'hua';
               WHEN v_compare BETWEEN '6112' AND '612D'
               THEN
                  v_return := v_return || 'huai';
               WHEN v_compare BETWEEN '612E' AND '61C6'
               THEN
                  v_return := v_return || 'huan';
               WHEN v_compare BETWEEN '61CA' AND '624A'
               THEN
                  v_return := v_return || 'huang';
               WHEN v_compare BETWEEN '624C' AND '6344'
               THEN
                  v_return := v_return || 'hui';
               WHEN v_compare BETWEEN '6346' AND '6388'
               THEN
                  v_return := v_return || 'hun';
               WHEN v_compare BETWEEN '638C' AND '63FA'
               THEN
                  v_return := v_return || 'huo';
               WHEN v_compare = '63FD'
               THEN
                  v_return := v_return || 'hwa';
               WHEN v_compare BETWEEN '63FE' AND '6601'
               THEN
                  v_return := v_return || 'ji';
               WHEN v_compare BETWEEN '6604' AND '6691'
               THEN
                  v_return := v_return || 'jia';
               WHEN v_compare BETWEEN '6692' AND '67F8'
               THEN
                  v_return := v_return || 'jian';
               WHEN v_compare BETWEEN '67F9' AND '6860'
               THEN
                  v_return := v_return || 'jiang';
               WHEN v_compare BETWEEN '6862' AND '6930'
               THEN
                  v_return := v_return || 'jiao';
               WHEN v_compare BETWEEN '6931' AND '6A18'
               THEN
                  v_return := v_return || 'jie';
               WHEN v_compare BETWEEN '6A1A' AND '6AC9'
               THEN
                  v_return := v_return || 'jin';
               WHEN v_compare BETWEEN '6ACA' AND '6B65'
               THEN
                  v_return := v_return || 'jing';
               WHEN v_compare BETWEEN '6B66' AND '6B9A'
               THEN
                  v_return := v_return || 'jiong';
               WHEN v_compare BETWEEN '6B9C' AND '6C0C'
               THEN
                  v_return := v_return || 'jiu';
               WHEN v_compare = '6C0D'
               THEN
                  v_return := v_return || 'jou';
               WHEN v_compare BETWEEN '6C0E' AND '6D2A'
               THEN
                  v_return := v_return || 'ju';
               WHEN v_compare BETWEEN '6D2D' AND '6D80'
               THEN
                  v_return := v_return || 'juan';
               WHEN v_compare BETWEEN '6D82' AND '6E28'
               THEN
                  v_return := v_return || 'jue';
               WHEN v_compare BETWEEN '6E2A' AND '6E85'
               THEN
                  v_return := v_return || 'jun';
               WHEN v_compare BETWEEN '6E86' AND '6E92'
               THEN
                  v_return := v_return || 'ka';
               WHEN v_compare BETWEEN '6E94' AND '6EC9'
               THEN
                  v_return := v_return || 'kai';
               WHEN v_compare = '6ECC'
               THEN
                  v_return := v_return || 'kal';
               WHEN v_compare BETWEEN '6ECD' AND '6F00'
               THEN
                  v_return := v_return || 'kan';
               WHEN v_compare BETWEEN '6F02' AND '6F30'
               THEN
                  v_return := v_return || 'kang';
               WHEN v_compare BETWEEN '6F31' AND '6F4D'
               THEN
                  v_return := v_return || 'kao';
               WHEN v_compare BETWEEN '6F50' AND '6FC8'
               THEN
                  v_return := v_return || 'ke';
               WHEN v_compare BETWEEN '6FC9' AND '6FDA'
               THEN
                  v_return := v_return || 'ken';
               WHEN v_compare BETWEEN '6FDC' AND '6FF5'
               THEN
                  v_return := v_return || 'keng';
               WHEN v_compare = '6FFC'
               THEN
                  v_return := v_return || 'ki';
               WHEN v_compare BETWEEN '6FFD' AND '7016'
               THEN
                  v_return := v_return || 'kong';
               WHEN v_compare = '7018'
               THEN
                  v_return := v_return || 'kos';
               WHEN v_compare BETWEEN '7019' AND '703E'
               THEN
                  v_return := v_return || 'kou';
               WHEN v_compare BETWEEN '7041' AND '707A'
               THEN
                  v_return := v_return || 'ku';
               WHEN v_compare BETWEEN '707C' AND '7095'
               THEN
                  v_return := v_return || 'kua';
               WHEN v_compare BETWEEN '709A' AND '70C1'
               THEN
                  v_return := v_return || 'kuai';
               WHEN v_compare BETWEEN '70C2' AND '70D4'
               THEN
                  v_return := v_return || 'kuan';
               WHEN v_compare BETWEEN '70D8' AND '7128'
               THEN
                  v_return := v_return || 'kuang';
               WHEN v_compare BETWEEN '7129' AND '71B1'
               THEN
                  v_return := v_return || 'kui';
               WHEN v_compare BETWEEN '71B2' AND '71FE'
               THEN
                  v_return := v_return || 'kun';
               WHEN v_compare BETWEEN '7200' AND '7226'
               THEN
                  v_return := v_return || 'kuo';
               WHEN v_compare = '7228'
               THEN
                  v_return := v_return || 'kweok';
               WHEN v_compare BETWEEN '722C' AND '726A'
               THEN
                  v_return := v_return || 'la';
               WHEN v_compare BETWEEN '726C' AND '72B5'
               THEN
                  v_return := v_return || 'lai';
               WHEN v_compare BETWEEN '72B9' AND '733C'
               THEN
                  v_return := v_return || 'lan';
               WHEN v_compare BETWEEN '733D' AND '7388'
               THEN
                  v_return := v_return || 'lang';
               WHEN v_compare BETWEEN '7389' AND '73E5'
               THEN
                  v_return := v_return || 'lao';
               WHEN v_compare BETWEEN '73E8' AND '7402'
               THEN
                  v_return := v_return || 'le';
               WHEN v_compare BETWEEN '7404' AND '7485'
               THEN
                  v_return := v_return || 'lei';
               WHEN v_compare BETWEEN '7488' AND '7499'
               THEN
                  v_return := v_return || 'leng';
               WHEN v_compare BETWEEN '749C' AND '7642'
               THEN
                  v_return := v_return || 'li';
               WHEN v_compare BETWEEN '7644' AND '7645'
               THEN
                  v_return := v_return || 'lia';
               WHEN v_compare BETWEEN '7646' AND '76EC'
               THEN
                  v_return := v_return || 'lian';
               WHEN v_compare BETWEEN '76ED' AND '7731'
               THEN
                  v_return := v_return || 'liang';
               WHEN v_compare BETWEEN '7732' AND '7794'
               THEN
                  v_return := v_return || 'liao';
               WHEN v_compare BETWEEN '7795' AND '77E2'
               THEN
                  v_return := v_return || 'lie';
               WHEN v_compare BETWEEN '77E4' AND '785D'
               THEN
                  v_return := v_return || 'lin';
               WHEN v_compare = '77EA'
               THEN
                  v_return := v_return || 'len';
               WHEN v_compare BETWEEN '7860' AND '7904'
               THEN
                  v_return := v_return || 'ling';
               WHEN v_compare BETWEEN '7905' AND '7986'
               THEN
                  v_return := v_return || 'liu';
               WHEN v_compare BETWEEN '7988' AND '7989'
               THEN
                  v_return := v_return || 'lo';
               WHEN v_compare BETWEEN '798A' AND '79FD'
               THEN
                  v_return := v_return || 'long';
               WHEN v_compare BETWEEN '79FE' AND '7A49'
               THEN
                  v_return := v_return || 'lou';
               WHEN v_compare BETWEEN '7A4C' AND '7B4D'
               THEN
                  v_return := v_return || 'lu';
               WHEN v_compare BETWEEN '7B4E' AND '7B80'
               THEN
                  v_return := v_return || 'luan';
               WHEN v_compare BETWEEN '7B81' AND '7BB2'
               THEN
                  v_return := v_return || 'lun';
               WHEN v_compare BETWEEN '7BB5' AND '7C25'
               THEN
                  v_return := v_return || 'luo';
               WHEN v_compare BETWEEN '7C26' AND '7C82'
               THEN
                  v_return := v_return || 'lv';
               WHEN v_compare BETWEEN '7C84' AND '7C98'
               THEN
                  v_return := v_return || 'lue';
               WHEN v_compare BETWEEN '7C9C' AND '7CE4'
               THEN
                  v_return := v_return || 'ma';
               WHEN v_compare BETWEEN '7CE5' AND '7DOC'
               THEN
                  v_return := v_return || 'mai';
               WHEN v_compare BETWEEN '7D11' AND '7D6E'
               THEN
                  v_return := v_return || 'man';
               WHEN v_compare BETWEEN '7D70' AND '7DA9'
               THEN
                  v_return := v_return || 'mang';
               WHEN v_compare BETWEEN '7DAC' AND '7E15'
               THEN
                  v_return := v_return || 'mao';
               WHEN v_compare = '7E0C'
               THEN
                  v_return := v_return || 'q';
               WHEN v_compare BETWEEN '7E18' AND '7E1E'
               THEN
                  v_return := v_return || 'me';
               WHEN v_compare BETWEEN '7E20' AND '7E9A'
               THEN
                  v_return := v_return || 'mei';
               WHEN v_compare BETWEEN '7E9D' AND '7EC1'
               THEN
                  v_return := v_return || 'men';
               WHEN v_compare BETWEEN '7EC2' AND '7F36'
               THEN
                  v_return := v_return || 'meng';
               WHEN v_compare = '7F38'
               THEN
                  v_return := v_return || 'meo';
               WHEN v_compare BETWEEN '7F39' AND '7FE4'
               THEN
                  v_return := v_return || 'mi';
               WHEN v_compare BETWEEN '7FE6' AND '8034'
               THEN
                  v_return := v_return || 'mian';
               WHEN v_compare BETWEEN '8035' AND '805A'
               THEN
                  v_return := v_return || 'miao';
               WHEN v_compare BETWEEN '805C' AND '8081'
               THEN
                  v_return := v_return || 'mie';
               WHEN v_compare BETWEEN '8084' AND '80E4'
               THEN
                  v_return := v_return || 'min';
               WHEN v_compare = '8096'
               THEN
                  v_return := v_return || 'lem';
               WHEN v_compare BETWEEN '80E5' AND '8116'
               THEN
                  v_return := v_return || 'ming';
               WHEN v_compare BETWEEN '8119' AND '811D'
               THEN
                  v_return := v_return || 'miu';
               WHEN v_compare BETWEEN '811E' AND '81A9'
               THEN
                  v_return := v_return || 'mo';
               WHEN v_compare BETWEEN '81AC' AND '81CC'
               THEN
                  v_return := v_return || 'mou';
               WHEN v_compare BETWEEN '81CD' AND '821E'
               THEN
                  v_return := v_return || 'mu';
               WHEN v_compare = '8220'
               THEN
                  v_return := v_return || 'myeo';
               WHEN v_compare = '8221'
               THEN
                  v_return := v_return || 'myeon';
               WHEN v_compare = '8222'
               THEN
                  v_return := v_return || 'myeong';
               WHEN v_compare BETWEEN '8224' AND '8258'
               THEN
                  v_return := v_return || 'na';
               WHEN v_compare BETWEEN '825D' AND '8285'
               THEN
                  v_return := v_return || 'nai';
               WHEN v_compare BETWEEN '8289' AND '82B5'
               THEN
                  v_return := v_return || 'nan';
               WHEN v_compare BETWEEN '82B9' AND '82D0'
               THEN
                  v_return := v_return || 'nang';
               WHEN v_compare BETWEEN '82D1' AND '8311'
               THEN
                  v_return := v_return || 'nao';
               WHEN v_compare BETWEEN '8312' AND '8320'
               THEN
                  v_return := v_return || 'ne';
               WHEN v_compare BETWEEN '8322' AND '8331'
               THEN
                  v_return := v_return || 'nei';
               WHEN v_compare = '8334'
               THEN
                  v_return := v_return || 'nem';
               WHEN v_compare = '8336'
               THEN
                  v_return := v_return || 'nen';
               WHEN v_compare = '8339'
               THEN
                  v_return := v_return || 'neng';
               WHEN v_compare = '833E'
               THEN
                  v_return := v_return || 'neus';
               WHEN v_compare = '8342'
               THEN
                  v_return := v_return || 'ngag';
               WHEN v_compare = '8344'
               THEN
                  v_return := v_return || 'ngai';
               WHEN v_compare = '8345'
               THEN
                  v_return := v_return || 'ngam';
               WHEN v_compare BETWEEN '8346' AND '83B9'
               THEN
                  v_return := v_return || 'ni';
               WHEN v_compare BETWEEN '83BC' AND '83ED'
               THEN
                  v_return := v_return || 'nian';
               WHEN v_compare BETWEEN '83EE' AND '83F5'
               THEN
                  v_return := v_return || 'niang';
               WHEN v_compare BETWEEN '83F8' AND '8414'
               THEN
                  v_return := v_return || 'niao';
               WHEN v_compare BETWEEN '8415' AND '8478'
               THEN
                  v_return := v_return || 'nie';
               WHEN v_compare BETWEEN '8479' AND '8480'
               THEN
                  v_return := v_return || 'nin';
               WHEN v_compare BETWEEN '8481' AND '84B4'
               THEN
                  v_return := v_return || 'ning';
               WHEN v_compare BETWEEN '84B5' AND '84D1'
               THEN
                  v_return := v_return || 'niu';
               WHEN v_compare BETWEEN '84D4' AND '84FA'
               THEN
                  v_return := v_return || 'nong';
               WHEN v_compare = '84E8'
               THEN
                  v_return := v_return || 'nung';
               WHEN v_compare BETWEEN '84FD' AND '850E'
               THEN
                  v_return := v_return || 'nou';
               WHEN v_compare BETWEEN '8511' AND '8522'
               THEN
                  v_return := v_return || 'nu';
               WHEN v_compare BETWEEN '8524' AND '852C'
               THEN
                  v_return := v_return || 'nuan';
               WHEN v_compare = '852D'
               THEN
                  v_return := v_return || 'nun';
               WHEN v_compare BETWEEN '8530' AND '8559'
               THEN
                  v_return := v_return || 'nuo';
               WHEN v_compare BETWEEN '855A' AND '8566'
               THEN
                  v_return := v_return || 'nv';
               WHEN v_compare BETWEEN '856D' AND '8574'
               THEN
                  v_return := v_return || 'nue';
               WHEN v_compare = '8575'
               THEN
                  v_return := v_return || 'o';
               WHEN v_compare = '8579'
               THEN
                  v_return := v_return || 'oes';
               WHEN v_compare = '857A'
               THEN
                  v_return := v_return || 'ol';
               WHEN v_compare = '857C'
               THEN
                  v_return := v_return || 'on';
               WHEN v_compare BETWEEN '857D' AND '85AE'
               THEN
                  v_return := v_return || 'ou';
               WHEN v_compare BETWEEN '85B1' AND '85C9'
               THEN
                  v_return := v_return || 'pa';
               WHEN v_compare BETWEEN '85CA' AND '85E4'
               THEN
                  v_return := v_return || 'pai';
               WHEN v_compare = '85E5'
               THEN
                  v_return := v_return || 'pak';
               WHEN v_compare BETWEEN '85E8' AND '8625'
               THEN
                  v_return := v_return || 'pan';
               WHEN v_compare BETWEEN '8626' AND '8658'
               THEN
                  v_return := v_return || 'pang';
               WHEN v_compare BETWEEN '8659' AND '8688'
               THEN
                  v_return := v_return || 'pao';
               WHEN v_compare BETWEEN '868A' AND '86C5'
               THEN
                  v_return := v_return || 'pei';
               WHEN v_compare BETWEEN '86C8' AND '86D6'
               THEN
                  v_return := v_return || 'pen';
               WHEN v_compare BETWEEN '86D8' AND '8740'
               THEN
                  v_return := v_return || 'peng';
               WHEN v_compare = '8741'
               THEN
                  v_return := v_return || 'peol';
               WHEN v_compare = '8742'
               THEN
                  v_return := v_return || 'phas';
               WHEN v_compare = '8744'
               THEN
                  v_return := v_return || 'phdeng';
               WHEN v_compare = '8745'
               THEN
                  v_return := v_return || 'phoi';
               WHEN v_compare = '8746'
               THEN
                  v_return := v_return || 'phos';
               WHEN v_compare BETWEEN '8748' AND '880D'
               THEN
                  v_return := v_return || 'pi';
               WHEN v_compare BETWEEN '880E' AND '883A'
               THEN
                  v_return := v_return || 'pian';
               WHEN v_compare BETWEEN '883C' AND '8869'
               THEN
                  v_return := v_return || 'piao';
               WHEN v_compare BETWEEN '886D' AND '8879'
               THEN
                  v_return := v_return || 'pie';
               WHEN v_compare BETWEEN '887A' AND '88A0'
               THEN
                  v_return := v_return || 'pin';
               WHEN v_compare BETWEEN '88A1' AND '88EC'
               THEN
                  v_return := v_return || 'ping';
               WHEN v_compare BETWEEN '88F0' AND '8938'
               THEN
                  v_return := v_return || 'po';
               WHEN v_compare BETWEEN '893E' AND '8958'
               THEN
                  v_return := v_return || 'pou';
               WHEN v_compare BETWEEN '895A' AND '895C'
               THEN
                  v_return := v_return || 'ppun';
               WHEN v_compare BETWEEN '895D' AND '89C4'
               THEN
                  v_return := v_return || 'pu';
               WHEN v_compare BETWEEN '89C5' AND '8B3E'
               THEN
                  v_return := v_return || 'qi';
               WHEN v_compare BETWEEN '8B41' AND '8B61'
               THEN
                  v_return := v_return || 'qia';
               WHEN v_compare BETWEEN '8B62' AND '8C54'
               THEN
                  v_return := v_return || 'qian';
               WHEN v_compare BETWEEN '8C5A' AND '8CB4'
               THEN
                  v_return := v_return || 'qiang';
               WHEN v_compare BETWEEN '8CB8' AND '8D3D'
               THEN
                  v_return := v_return || 'qiao';
               WHEN v_compare BETWEEN '8D40' AND '8D7E'
               THEN
                  v_return := v_return || 'qie';
               WHEN v_compare BETWEEN '8D81' AND '8DFA'
               THEN
                  v_return := v_return || 'qin';
               WHEN v_compare BETWEEN '8DFC' AND '8E5D'
               THEN
                  v_return := v_return || 'qing';
               WHEN v_compare BETWEEN '8E5E' AND '8E98'
               THEN
                  v_return := v_return || 'qiong';
               WHEN v_compare BETWEEN '8E9A' AND '8F2A'
               THEN
                  v_return := v_return || 'qiu';
               WHEN v_compare BETWEEN '8F2E' AND '8FE9'
               THEN
                  v_return := v_return || 'qu';
               WHEN v_compare BETWEEN '8FEA' AND '905D'
               THEN
                  v_return := v_return || 'quan';
               WHEN v_compare BETWEEN '905E' AND '9099'
               THEN
                  v_return := v_return || 'que';
               WHEN v_compare BETWEEN '909A' AND '90AA'
               THEN
                  v_return := v_return || 'qun';
               WHEN v_compare BETWEEN '90B0' AND '90B1'
               THEN
                  v_return := v_return || 'ra';
               WHEN v_compare = '90B2'
               THEN
                  v_return := v_return || 'ram';
               WHEN v_compare BETWEEN '90B4' AND '90E5'
               THEN
                  v_return := v_return || 'ran';
               WHEN v_compare BETWEEN '90E6' AND '9104'
               THEN
                  v_return := v_return || 'rang';
               WHEN v_compare BETWEEN '9105' AND '911C'
               THEN
                  v_return := v_return || 'rao';
               WHEN v_compare BETWEEN '911D' AND '9120'
               THEN
                  v_return := v_return || 're';
               WHEN v_compare BETWEEN '9121' AND '9180'
               THEN
                  v_return := v_return || 'ren';
               WHEN v_compare BETWEEN '9181' AND '918D'
               THEN
                  v_return := v_return || 'reng';
               WHEN v_compare BETWEEN '918E' AND '9196'
               THEN
                  v_return := v_return || 'ri';
               WHEN v_compare BETWEEN '9189' AND '91F1'
               THEN
                  v_return := v_return || 'rong';
               WHEN v_compare BETWEEN '91F2' AND '9218'
               THEN
                  v_return := v_return || 'rou';
               WHEN v_compare BETWEEN '9219' AND '9269'
               THEN
                  v_return := v_return || 'ru';
               WHEN v_compare BETWEEN '926C' AND '9292'
               THEN
                  v_return := v_return || 'ruan';
               WHEN v_compare BETWEEN '9294' AND '92BD'
               THEN
                  v_return := v_return || 'rui';
               WHEN v_compare BETWEEN '92BE' AND '92C9'
               THEN
                  v_return := v_return || 'run';
               WHEN v_compare = '92CA'
               THEN
                  v_return := v_return || 'rua';
               WHEN v_compare BETWEEN '92CA' AND '92E4'
               THEN
                  v_return := v_return || 'ruo';
               WHEN v_compare BETWEEN '92E5' AND '9309'
               THEN
                  v_return := v_return || 'sa';
               WHEN v_compare = '930A'
               THEN
                  v_return := v_return || 'saeng';
               WHEN v_compare BETWEEN '930C' AND '9325'
               THEN
                  v_return := v_return || 'sai';
               WHEN v_compare = '9328'
               THEN
                  v_return := v_return || 'sal';
               WHEN v_compare BETWEEN '9329' AND '9355'
               THEN
                  v_return := v_return || 'san';
               WHEN v_compare BETWEEN '9358' AND '936A'
               THEN
                  v_return := v_return || 'sang';
               WHEN v_compare BETWEEN '936C' AND '9391'
               THEN
                  v_return := v_return || 'sao';
               WHEN v_compare BETWEEN '9392' AND '93C5'
               THEN
                  v_return := v_return || 'se';
               WHEN v_compare = '93C6'
               THEN
                  v_return := v_return || 'sed';
               WHEN v_compare BETWEEN '93C8' AND '93CC'
               THEN
                  v_return := v_return || 'sen';
               WHEN v_compare BETWEEN '93CD' AND '93D0'
               THEN
                  v_return := v_return || 'seng';
               WHEN v_compare = '93D1'
               THEN
                  v_return := v_return || 'seo';
               WHEN v_compare = '93D2'
               THEN
                  v_return := v_return || 'seon';
               WHEN v_compare BETWEEN '93D4' AND '941A'
               THEN
                  v_return := v_return || 'sha';
               WHEN v_compare BETWEEN '941D' AND '9428'
               THEN
                  v_return := v_return || 'shai';
               WHEN v_compare BETWEEN '9429' AND '94C1'
               THEN
                  v_return := v_return || 'shan';
               WHEN v_compare BETWEEN '94C2' AND '94EE'
               THEN
                  v_return := v_return || 'shang';
               WHEN v_compare BETWEEN '94F1' AND '952D'
               THEN
                  v_return := v_return || 'shao';
               WHEN v_compare BETWEEN '952E' AND '9571'
               THEN
                  v_return := v_return || 'she';
               WHEN v_compare BETWEEN '9574' AND '9602'
               THEN
                  v_return := v_return || 'shen';
               WHEN v_compare BETWEEN '9604' AND '965C'
               THEN
                  v_return := v_return || 'sheng';
               WHEN v_compare BETWEEN '965E' AND '9786'
               THEN
                  v_return := v_return || 'shi';
               WHEN v_compare BETWEEN '9788' AND '97AE'
               THEN
                  v_return := v_return || 'shou';
               WHEN v_compare BETWEEN '97B0' AND '9878'
               THEN
                  v_return := v_return || 'shu';
               WHEN v_compare BETWEEN '987A' AND '987E'
               THEN
                  v_return := v_return || 'shua';
               WHEN v_compare BETWEEN '9880' AND '988A'
               THEN
                  v_return := v_return || 'shuai';
               WHEN v_compare BETWEEN '988C' AND '9894'
               THEN
                  v_return := v_return || 'shuan';
               WHEN v_compare BETWEEN '9895' AND '98BE'
               THEN
                  v_return := v_return || 'shuang';
               WHEN v_compare BETWEEN '98C0' AND '98D6'
               THEN
                  v_return := v_return || 'shui';
               WHEN v_compare BETWEEN '98DC' AND '98EE'
               THEN
                  v_return := v_return || 'shun';
               WHEN v_compare BETWEEN '98F1' AND '9911'
               THEN
                  v_return := v_return || 'shuo';
               WHEN v_compare BETWEEN '9912' AND '99AD'
               THEN
                  v_return := v_return || 'si';
               WHEN v_compare = '99AE'
               THEN
                  v_return := v_return || 'so';
               WHEN v_compare = '99B0'
               THEN
                  v_return := v_return || 'sol';
               WHEN v_compare BETWEEN '99B1' AND '99F6'
               THEN
                  v_return := v_return || 'song';
               WHEN v_compare BETWEEN '99F8' AND '9A36'
               THEN
                  v_return := v_return || 'sou';
               WHEN v_compare BETWEEN '9A38' AND '9AB6'
               THEN
                  v_return := v_return || 'su';
               WHEN v_compare BETWEEN '9AB8' AND '9AC4'
               THEN
                  v_return := v_return || 'suan';
               WHEN v_compare BETWEEN '9AC5' AND '9B3A'
               THEN
                  v_return := v_return || 'sui';
               WHEN v_compare = '9AF0'
               THEN
                  v_return := v_return || 'wie';
               WHEN v_compare BETWEEN '9B3C' AND '9B62'
               THEN
                  v_return := v_return || 'sun';
               WHEN v_compare BETWEEN '9B65' AND '9BA9'
               THEN
                  v_return := v_return || 'suo';
               WHEN v_compare BETWEEN '9BAA' AND '9C10'
               THEN
                  v_return := v_return || 'ta';
               WHEN v_compare = '9C11'
               THEN
                  v_return := v_return || 'tae';
               WHEN v_compare BETWEEN '9C12' AND '9C59'
               THEN
                  v_return := v_return || 'tai';
               WHEN v_compare BETWEEN '9C5A' AND '9CE0'
               THEN
                  v_return := v_return || 'tan';
               WHEN v_compare BETWEEN '9CE2' AND '9D55'
               THEN
                  v_return := v_return || 'tang';
               WHEN v_compare BETWEEN '9D56' AND '9DB4'
               THEN
                  v_return := v_return || 'tao';
               WHEN v_compare = '9DB6'
               THEN
                  v_return := v_return || 'tap';
               WHEN v_compare BETWEEN '9DB8' AND '9DC6'
               THEN
                  v_return := v_return || 'te';
               WHEN v_compare BETWEEN '9DC8' AND '9DED'
               THEN
                  v_return := v_return || 'teng';
               WHEN v_compare = '9DEE'
               THEN
                  v_return := v_return || 'teo';
               WHEN v_compare = '9DF0'
               THEN
                  v_return := v_return || 'teul';
               WHEN v_compare BETWEEN '9DF1' AND '9E82'
               THEN
                  v_return := v_return || 'ti';
               WHEN v_compare BETWEEN '9E85' AND '9EED'
               THEN
                  v_return := v_return || 'tian';
               WHEN v_compare BETWEEN '9EEE' AND '9F38'
               THEN
                  v_return := v_return || 'tiao';
               WHEN v_compare BETWEEN '9F39' AND '9F56'
               THEN
                  v_return := v_return || 'tie';
               WHEN v_compare BETWEEN '9F59' AND '9FAE'
               THEN
                  v_return := v_return || 'ting';
               WHEN v_compare = '9FB0'
               THEN
                  v_return := v_return || 'tol';
               WHEN v_compare BETWEEN '9FB1' AND 'A015'
               THEN
                  v_return := v_return || 'tong';
               WHEN v_compare BETWEEN 'A016' AND 'A03A'
               THEN
                  v_return := v_return || 'tou';
               WHEN v_compare BETWEEN 'A040' AND 'A0A9'
               THEN
                  v_return := v_return || 'tu';
               WHEN v_compare BETWEEN 'A0AA' AND 'A0D5'
               THEN
                  v_return := v_return || 'tuan';
               WHEN v_compare BETWEEN 'A0D6' AND 'A106'
               THEN
                  v_return := v_return || 'tui';
               WHEN v_compare BETWEEN 'A108' AND 'A131'
               THEN
                  v_return := v_return || 'tun';
               WHEN v_compare BETWEEN 'A134' AND 'A1AE'
               THEN
                  v_return := v_return || 'tuo';
               WHEN v_compare BETWEEN 'A1B0' AND 'A1E8'
               THEN
                  v_return := v_return || 'wa';
               WHEN v_compare BETWEEN 'A1E9' AND 'A1F5'
               THEN
                  v_return := v_return || 'wai';
               WHEN v_compare BETWEEN 'A1F8' AND 'A279'
               THEN
                  v_return := v_return || 'wan';
               WHEN v_compare BETWEEN 'A27A' AND 'A2B9'
               THEN
                  v_return := v_return || 'wang';
               WHEN v_compare BETWEEN 'A2BC' AND 'A408'
               THEN
                  v_return := v_return || 'wei';
               WHEN v_compare BETWEEN 'A40D' AND 'A47C'
               THEN
                  v_return := v_return || 'wen';
               WHEN v_compare BETWEEN 'A47D' AND 'A4A2'
               THEN
                  v_return := v_return || 'weng';
               WHEN v_compare BETWEEN 'A4A4' AND 'A4EA'
               THEN
                  v_return := v_return || 'wo';
               WHEN v_compare BETWEEN 'A4EC' AND 'A5D4'
               THEN
                  v_return := v_return || 'wu';
               WHEN v_compare BETWEEN 'A5D6' AND 'A784'
               THEN
                  v_return := v_return || 'xi';
               WHEN v_compare BETWEEN 'A785' AND 'A7FA'
               THEN
                  v_return := v_return || 'xia';
               WHEN v_compare BETWEEN 'A7FD' AND 'A951'
               THEN
                  v_return := v_return || 'xian';
               WHEN v_compare BETWEEN 'A954' AND 'A9CE'
               THEN
                  v_return := v_return || 'xiang';
               WHEN v_compare BETWEEN 'A9D0' AND 'AA8A'
               THEN
                  v_return := v_return || 'xiao';
               WHEN v_compare BETWEEN 'AA8D' AND 'AB7E'
               THEN
                  v_return := v_return || 'xie';
               WHEN v_compare BETWEEN 'AB80' AND 'ABEE'
               THEN
                  v_return := v_return || 'xin';
               WHEN v_compare BETWEEN 'ABF0' AND 'AC41'
               THEN
                  v_return := v_return || 'xing';
               WHEN v_compare BETWEEN 'AC42' AND 'AC64'
               THEN
                  v_return := v_return || 'xiong';
               WHEN v_compare BETWEEN 'AC65' AND 'ACBA'
               THEN
                  v_return := v_return || 'xiu';
               WHEN v_compare BETWEEN 'ACBC' AND 'AD90'
               THEN
                  v_return := v_return || 'xu';
               WHEN v_compare = 'ACD9'
               THEN
                  v_return := v_return || 'chua';
               WHEN v_compare BETWEEN 'AD91' AND 'AE32'
               THEN
                  v_return := v_return || 'xuan';
               WHEN v_compare BETWEEN 'AE34' AND 'AE89'
               THEN
                  v_return := v_return || 'xue';
               WHEN v_compare BETWEEN 'AE8C' AND 'AF1E'
               THEN
                  v_return := v_return || 'xun';
               WHEN v_compare BETWEEN 'AF20' AND 'AF96'
               THEN
                  v_return := v_return || 'ya';
               WHEN v_compare BETWEEN 'AF98' AND 'B118'
               THEN
                  v_return := v_return || 'yan';
               WHEN v_compare = 'B030'
               THEN
                  v_return := v_return || 'eom';
               WHEN v_compare BETWEEN 'B11A' AND 'B1A8'
               THEN
                  v_return := v_return || 'yang';
               WHEN v_compare BETWEEN 'B1AD' AND 'B275'
               THEN
                  v_return := v_return || 'yao';
               WHEN v_compare BETWEEN 'B276' AND 'B30A'
               THEN
                  v_return := v_return || 'ye';
               WHEN v_compare BETWEEN 'B30D' AND 'B30E'
               THEN
                  v_return := v_return || 'yen';
               WHEN v_compare BETWEEN 'B310' AND 'B594'
               THEN
                  v_return := v_return || 'yi';
               WHEN v_compare = 'B359'
               THEN
                  v_return := v_return || 'i';
               WHEN v_compare BETWEEN 'B596' AND 'B684'
               THEN
                  v_return := v_return || 'yin';
               WHEN v_compare BETWEEN 'B685' AND 'B768'
               THEN
                  v_return := v_return || 'ying';
               WHEN v_compare BETWEEN 'B76C' AND 'B76E'
               THEN
                  v_return := v_return || 'yo';
               WHEN v_compare BETWEEN 'B770' AND 'B7EA'
               THEN
                  v_return := v_return || 'yong';
               WHEN v_compare BETWEEN 'B7EC' AND 'B8B2'
               THEN
                  v_return := v_return || 'you';
               WHEN v_compare BETWEEN 'B8B5' AND 'BA98'
               THEN
                  v_return := v_return || 'yu';
               WHEN v_compare BETWEEN 'BA99' AND 'BB58'
               THEN
                  v_return := v_return || 'yuan';
               WHEN v_compare BETWEEN 'BB59' AND 'BBBE'
               THEN
                  v_return := v_return || 'yue';
               WHEN v_compare BETWEEN 'BBC1' AND 'BC58'
               THEN
                  v_return := v_return || 'yun';
               WHEN v_compare BETWEEN 'BC59' AND 'BC7E'
               THEN
                  v_return := v_return || 'za';
               WHEN v_compare BETWEEN 'BC81' AND 'BCA8'
               THEN
                  v_return := v_return || 'zai';
               WHEN v_compare BETWEEN 'BCAA' AND 'BCEA'
               THEN
                  v_return := v_return || 'zan';
               WHEN v_compare BETWEEN 'BCEE' AND 'BD0A'
               THEN
                  v_return := v_return || 'zang';
               WHEN v_compare BETWEEN 'BD0C' AND 'BD46'
               THEN
                  v_return := v_return || 'zao';
               WHEN v_compare BETWEEN 'BD48' AND 'BD99'
               THEN
                  v_return := v_return || 'ze';
               WHEN v_compare BETWEEN 'BD9A' AND 'BDA2'
               THEN
                  v_return := v_return || 'zei';
               WHEN v_compare BETWEEN 'BDA5' AND 'BDAC'
               THEN
                  v_return := v_return || 'zen';
               WHEN v_compare BETWEEN 'BDAD' AND 'BDCC'
               THEN
                  v_return := v_return || 'zeng';
               WHEN v_compare BETWEEN 'BDCE' AND 'BE40'
               THEN
                  v_return := v_return || 'zha';
               WHEN v_compare = 'BDF8'
               THEN
                  v_return := v_return || 'gad';
               WHEN v_compare BETWEEN 'BE41' AND 'BE62'
               THEN
                  v_return := v_return || 'zhai';
               WHEN v_compare BETWEEN 'BE65' AND 'BEF4'
               THEN
                  v_return := v_return || 'zhan';
               WHEN v_compare BETWEEN 'BEF6' AND 'BF3E'
               THEN
                  v_return := v_return || 'zhang';
               WHEN v_compare BETWEEN 'BF40' AND 'BF8C'
               THEN
                  v_return := v_return || 'zhao';
               WHEN v_compare BETWEEN 'BF8E' AND 'BFF8'
               THEN
                  v_return := v_return || 'zhe';
               WHEN v_compare BETWEEN 'BFF9' AND 'C0B2'
               THEN
                  v_return := v_return || 'zhen';
               WHEN v_compare BETWEEN 'C0B4' AND 'C11E'
               THEN
                  v_return := v_return || 'zheng';
               WHEN v_compare BETWEEN 'C122' AND 'C2C4'
               THEN
                  v_return := v_return || 'zhi';
               WHEN v_compare BETWEEN 'C2C5' AND 'C31A'
               THEN
                  v_return := v_return || 'zhong';
               WHEN v_compare BETWEEN 'C31D' AND 'C39A'
               THEN
                  v_return := v_return || 'zhou';
               WHEN v_compare BETWEEN 'C39C' AND 'C47C'
               THEN
                  v_return := v_return || 'zhu';
               WHEN v_compare BETWEEN 'C47D' AND 'C484'
               THEN
                  v_return := v_return || 'zhua';
               WHEN v_compare BETWEEN 'C485' AND 'C486'
               THEN
                  v_return := v_return || 'zhuai';
               WHEN v_compare BETWEEN 'C488' AND 'C4C0'
               THEN
                  v_return := v_return || 'zhuan';
               WHEN v_compare BETWEEN 'C4C2' AND 'C4E5'
               THEN
                  v_return := v_return || 'zhuang';
               WHEN v_compare BETWEEN 'C4E6' AND 'C51C'
               THEN
                  v_return := v_return || 'zhui';
               WHEN v_compare BETWEEN 'C51D' AND 'C530'
               THEN
                  v_return := v_return || 'zhun';
               WHEN v_compare BETWEEN 'C534' AND 'C5A5'
               THEN
                  v_return := v_return || 'zhuo';
               WHEN v_compare BETWEEN 'C5A8' AND 'C648'
               THEN
                  v_return := v_return || 'zi';
               WHEN v_compare = 'C64A'
               THEN
                  v_return := v_return || 'zo';
               WHEN v_compare BETWEEN 'C64C' AND 'C6B5'
               THEN
                  v_return := v_return || 'zong';
               WHEN v_compare BETWEEN 'C6B6' AND 'C6D6'
               THEN
                  v_return := v_return || 'zou';
               WHEN v_compare BETWEEN 'C6E1' AND 'C714'
               THEN
                  v_return := v_return || 'zu';
               WHEN v_compare BETWEEN 'C715' AND 'C72D'
               THEN
                  v_return := v_return || 'zuan';
               WHEN v_compare BETWEEN 'C72E' AND 'C75E'
               THEN
                  v_return := v_return || 'zui';
               WHEN v_compare BETWEEN 'C760' AND 'C776'
               THEN
                  v_return := v_return || 'zun';
               WHEN v_compare BETWEEN 'C77A' AND 'C7B4'
               THEN
                  v_return := v_return || 'zuo';
               ELSE
                  v_return := v_return || v_substr;
            END CASE;
         END LOOP;
   END CASE;
 
   RETURN v_return;
END fn_getpy;
--防止受影响,先删除后再创建
drop view institution;
create or replace view institution
as
select *from v3sie.institution;
--删除表
drop table v_ins_psn_count;
--创建表
CREATE TABLE v_ins_psn_count
AS
SELECT t.ins_id,t.zh_name,t.en_name,FN_GETPY(t."ZH_NAME") AS pin_yin,FN_GETPY(t."ZH_NAME",3) AS first_letter FROM institution t;
--添加字段
alter table v_ins_psn_count add UPDATE_DATE date default sysdate;
alter table v_ins_psn_count add history_psn_count number(6) default 0;
-- Add comments to the columns 
comment on column v_ins_psn_count.history_psn_count
  is '数据来源于表psn_work_history和表psn_edu_history,有多少个则统计多少个,不进行去重处理';
--给ins_id添加主键约束
alter table v_ins_psn_count add constraint pk_ins_id primary key(ins_id);
--初始化统计数
merge into v_ins_psn_count p
using  
(
   select i.INS_ID, sum(tmp.work_count) AS history_psn_count from institution i, 
 (select t1.ins_name, count(t1.psn_id) work_count from psn_work_history t1 group by t1.ins_name
   UNION ALL
   SELECT t2.ins_name, count(t2.psn_id) work_count from psn_edu_history t2 group by t2.ins_name         
  ) tmp 
  where (tmp.ins_name = i.ZH_NAME or i.EN_NAME = tmp.ins_name) group by i.INS_ID order by sum(tmp.work_count) DESC
)
otmp
on (p.ins_id = otmp.ins_id)
when matched then
update set p.history_psn_count = otmp.history_psn_count;
--删除视图,存储过程中不能直接执行DDL语句,若视图没有更新,需要手动进行视图更新
DROP VIEW INSTITUTION;
--创建视图
create or replace view INSTITUTION as
select t."INS_ID",t."ZH_NAME",t."EN_NAME",t."ABBR",t."CONTACT_PERSON",t."TEL",
t."SERVER_EMAIL",t."URL",t."REGION_ID",t."STATUS",t."NATURE",t."CHECK_EMAILS",
t."ZH_ADDRESS",t."EN_ADDRESS",t."POST_CODE",t."FOX",t."UNIFORM_ID1",t."UNIFORM_ID2",
t."SERVER_TEL",t."IS_INIT",t."DATA_FROM",t."MOBILE",t."BRIEF_DESC",t."CONTACT_EMAIL",
t."AUTO_COMPLETE",t.auto_complete as ENABLED,p."HISTORY_PSN_COUNT",p."PIN_YIN",p."FIRST_LETTER"
from v3sie.institution t,
v_ins_psn_count p WHERE p.ins_id = t.ins_id;
--删除存储过程
drop procedure ins_job_procedure;
--创建定时任务需要的存储过程
create or replace procedure ins_job_procedure as
begin
  insert into v_ins_psn_count(ins_id,zh_name,en_name,pin_yin,first_letter) select t.ins_id,t.zh_name,t.en_name,FN_GETPY(t."ZH_NAME") AS pin_yin,FN_GETPY(t."ZH_NAME",3) AS first_letter from v3sie.institution t minus 
  select p.ins_id,p.zh_name,p.en_name,p.pin_yin,p.first_letter from v_ins_psn_count p;
end;
--创建定时任务
declare ins_view_job NUMBER;
BEGIN 
dbms_job.submit(ins_view_job,'ins_job_procedure;',sysdate,'sysdate+1/(24*60)'); --启动后立即执行,时间间隔为每隔一分钟运行一次
COMMIT; 
END; 
--运行定时任务,需查询任务号进行执行
declare
	begin    
      dbms_job.run(84);  
end;    

--运行定时任务,由于每个库的任务号不一致,所以需要手动运行定时任务
--新增工作经历拼音和首字母提醒功能,以及按机构人数排序  2019-1-21 xiexing end



--原因    SCM-24374 个人库成果mongodb数据拆分任务 2019-03-29 YJ begin
-- V_PUB_DOI
create table V_PUB_DOI
(
  pub_id NUMBER(18) not null,
  doi    VARCHAR2(200 CHAR)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
comment on table V_PUB_DOI is '个人库成果doi数据表';
comment on column V_PUB_DOI.pub_id is 'sns库pubId，主键';
comment on column V_PUB_DOI.doi is 'doi';
alter table V_PUB_DOI
  add constraint V_PUB_DOI_PK primary key (PUB_ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;

-- V_PUB_JOURNAL
create table V_PUB_JOURNAL
(
  pub_id NUMBER(18) not null,
  jid    NUMBER(18),
  name   VARCHAR2(500 CHAR),
  issn   VARCHAR2(100 CHAR)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
comment on table V_PUB_JOURNAL is '个人库成果期刊信息';
comment on column V_PUB_JOURNAL.pub_id is '成果id';
comment on column V_PUB_JOURNAL.jid is '期刊id，JOURNAL的主键';
comment on column V_PUB_JOURNAL.name is '期刊名';
comment on column V_PUB_JOURNAL.issn is 'issn';
alter table V_PUB_JOURNAL
  add constraint V_PUB_JOURNAL_PK primary key (PUB_ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;

-- V_PUB_PATENT
create table V_PUB_PATENT
(
  pub_id              NUMBER(18) not null,
  application_no      VARCHAR2(200 CHAR),
  publication_open_no VARCHAR2(200 CHAR)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
comment on table V_PUB_PATENT is '个人库成果专利信息';
comment on column V_PUB_PATENT.pub_id is '成果id';
comment on column V_PUB_PATENT.application_no is '专利申请号';
comment on column V_PUB_PATENT.publication_open_no is '专利公开号';
alter table V_PUB_PATENT
  add constraint V_PUB_PATENT_PK primary key (PUB_ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;

--原因    SCM-24374 个人库成果mongodb数据拆分任务 2019-03-29 YJ end

-- 原因 SCM-21253 成果查重doi去除标点符号  2019-04-01 YJ start
alter table v_Pub_Duplicate add(hash_clean_doi varchar(32));
comment on column v_Pub_Duplicate.hash_clean_doi is '去除标点符号doi的hash值';
alter table v_Pub_Duplicate add(hash_clean_cnki_doi varchar(32));
comment on column v_Pub_Duplicate.hash_clean_cnki_doi is '去除标点符号cnkidoi的hash值';
-- 原因 SCM-21253 成果查重doi去除标点符号  2019-04-01 YJ end

-- SCMAPP-1367 生产机，特定账号登录提示“网络错误” ltl begin
delete from AWARD_STATISTICS t where psn_id is null or ACTION_KEY is null

delete from AWARD_STATISTICS a  
where a.create_date !=  
(  
  select max(b.create_date) from AWARD_STATISTICS b  
  where a.psn_id = b.psn_id and  
  a.action_key = b.action_key  
);
commit;

delete from AWARD_STATISTICS a  
where a.rowid !=  
(  
  select max(b.rowid) from AWARD_STATISTICS b  
  where a.psn_id = b.psn_id and  
  a.action_key = b.action_key  
);
commit;

alter table AWARD_STATISTICS add constraint PK_PSN_ID_AND_ACTION_KEY unique (PSN_ID, ACTION_KEY);   
-- SCMAPP-1367 生产机，特定账号登录提示“网络错误” ltl end


--原因（SCM-24532 成果指派-特定成果优先指派） 2019-4-8 zll begin
Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
Values(1937,'PdwhPubAssignInsTaskTrigger','0/10 * * * * ?',0,'特定成果优先指派');
--原因（SCM-24532 成果指派-特定成果优先指派） 2019-4-8 zll end


--原因    SCM-24374 个人库成果mongodb数据拆分任务 2019-03-29 YJ begin
-- V_PUB_DOI
create table V_PUB_DOI
(
  pub_id NUMBER(18) not null,
  doi    VARCHAR2(200 CHAR)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
comment on table V_PUB_DOI is '个人库成果doi数据表';
comment on column V_PUB_DOI.pub_id is 'sns库pubId，主键';
comment on column V_PUB_DOI.doi is 'doi';
alter table V_PUB_DOI
  add constraint V_PUB_DOI_PK primary key (PUB_ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;

-- V_PUB_JOURNAL
create table V_PUB_JOURNAL
(
  pub_id NUMBER(18) not null,
  jid    NUMBER(18),
  name   VARCHAR2(500 CHAR),
  issn   VARCHAR2(100 CHAR)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
comment on table V_PUB_JOURNAL is '个人库成果期刊信息';
comment on column V_PUB_JOURNAL.pub_id is '成果id';
comment on column V_PUB_JOURNAL.jid is '期刊id，JOURNAL的主键';
comment on column V_PUB_JOURNAL.name is '期刊名';
comment on column V_PUB_JOURNAL.issn is 'issn';
alter table V_PUB_JOURNAL
  add constraint V_PUB_JOURNAL_PK primary key (PUB_ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;

-- V_PUB_PATENT
create table V_PUB_PATENT
(
  pub_id              NUMBER(18) not null,
  application_no      VARCHAR2(200 CHAR),
  publication_open_no VARCHAR2(200 CHAR)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
comment on table V_PUB_PATENT is '个人库成果专利信息';
comment on column V_PUB_PATENT.pub_id is '成果id';
comment on column V_PUB_PATENT.application_no is '专利申请号';
comment on column V_PUB_PATENT.publication_open_no is '专利公开号';
alter table V_PUB_PATENT
  add constraint V_PUB_PATENT_PK primary key (PUB_ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;

--原因    SCM-24374 个人库成果mongodb数据拆分任务 2019-03-29 YJ end

-- 原因 SCM-21253 成果查重doi去除标点符号  2019-04-01 YJ start
alter table v_Pub_Duplicate add(hash_clean_doi varchar(32));
comment on column v_Pub_Duplicate.hash_clean_doi is '去除标点符号doi的hash值';
alter table v_Pub_Duplicate add(hash_clean_cnki_doi varchar(32));
comment on column v_Pub_Duplicate.hash_clean_cnki_doi is '去除标点符号cnkidoi的hash值';
-- 原因 SCM-21253 成果查重doi去除标点符号  2019-04-01 YJ end

-- SCMAPP-1367 生产机，特定账号登录提示“网络错误” ltl begin
delete from AWARD_STATISTICS t where psn_id is null or ACTION_KEY is null

delete from AWARD_STATISTICS a  
where a.create_date !=  
(  
  select max(b.create_date) from AWARD_STATISTICS b  
  where a.psn_id = b.psn_id and  
  a.action_key = b.action_key  
);
commit;

delete from AWARD_STATISTICS a  
where a.rowid !=  
(  
  select max(b.rowid) from AWARD_STATISTICS b  
  where a.psn_id = b.psn_id and  
  a.action_key = b.action_key  
);
commit;

alter table AWARD_STATISTICS add constraint PK_PSN_ID_AND_ACTION_KEY unique (PSN_ID, ACTION_KEY);   
-- SCMAPP-1367 生产机，特定账号登录提示“网络错误” ltl end



