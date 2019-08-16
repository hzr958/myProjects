-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end



--原因（SCM-17764） 2018-7-3 zzx begin
--执行sql: 
--清理2018年以前的数据 
delete v_batch_jobs t where t.status = 3 and t.job_start_time<to_date('2018-01-01', 'yyyy-mm-dd'); 
--ee747f8c --数据过大内存溢出 
delete v_batch_jobs t where t.status = 3 and t.strategy='ee747f8c'; 
--rpcxit26 --数据问题 
delete v_batch_jobs t where t.status = 3 and t.strategy='rpcxit26'; 
--reginfo3--数据问题 
delete v_batch_jobs t where t.status = 3 and t.strategy='reginfo3'; 
--ee747f8b--任务超时-数据检查不通过 
delete v_batch_jobs t where t.status = 3 and t.strategy='ee747f8b'; 
--attn2dyn --已修改 
delete v_batch_jobs t where t.status = 3 and t.strategy='attn2dyn'; 
--regpuba1--已修改 
delete v_batch_jobs t where t.status = 3 and t.strategy='regpuba1'; 
--pdwhpub2--数据插入出错 
delete v_batch_jobs t where t.status = 3 and t.strategy='pdwhpub2'; 
--5oul930i--输入数据异常 
delete v_batch_jobs t where t.status = 3 and t.strategy='5oul930i'; 
--cetvceq6--超时 
delete v_batch_jobs t where t.status = 3 and t.strategy='cetvceq6'; 
--87ef0869--部分数据异常 
delete v_batch_jobs t where t.status = 3 and t.strategy='87ef0869'; 
--matpdpba--超时 
delete v_batch_jobs t where t.status = 3 and t.strategy='matpdpba'; 
--87eff870--超时 
delete v_batch_jobs t where t.status = 3 and t.strategy='87eff870'; 
--abcdefgh--部分数据更新缺失字段报错 
delete v_batch_jobs t where t.status = 3 and t.strategy='abcdefgh'; 
--87eff869 
delete v_batch_jobs t where t.status = 3 and t.strategy='87eff869'; 
--avatars1-Person数据不存在 
delete v_batch_jobs t where t.status = 3 and t.strategy='avatars1'; 
--ba59abbe--超时 
delete v_batch_jobs t where t.status = 3 and t.strategy='ba59abbe'; 
--addindx1-solr连接异常 
delete v_batch_jobs t where t.status = 3 and t.strategy='addindx1'; 
--oaftiz2b--solr获取person数据缺失报错 
delete v_batch_jobs t where t.status = 3 and t.strategy='oaftiz2b'; 
--67pub8b1--少部分更新数据不全报错 
delete v_batch_jobs t where t.status = 3 and t.strategy='67pub8b1'; 
--pubcite1--字符串处理没有判空 
delete v_batch_jobs t where t.status = 3 and t.strategy='pubcite1'; 
--67pub8a9--字段为空执行sql报错 
delete v_batch_jobs t where t.status = 3 and t.strategy='67pub8a9'; 
--dxvd63av--部分数据id为空 
delete v_batch_jobs t where t.status = 3 and t.strategy='dxvd63av';
--原因（SCM-17764） 2018-7-3 zzx end



--原因（SCM-17764） 2018-7-3 zzx begin
--test-超时数据-
delete v_batch_jobs t where t.status = 3 and t.strategy='reginfo4'; 
delete v_batch_jobs t where t.status = 3 and t.strategy='5eb7d5a3'; 
delete v_batch_jobs t where t.status = 3 and t.strategy='reginfo1'; 
delete v_batch_jobs t where t.status = 3 and t.strategy='gruppub1'; 
--原因（SCM-17764） 2018-7-3 zzx end

--原因（SCM-18408） 2018-7-4 yhx begin
update CONST_DICTIONARY t set t.zh_tw_caption='联系人',t.zh_cn_caption='联系人' where t.category='privacy_permissions' and t.code='1';
--原因（SCM-18408） 2018-7-4 yhx end


--原因（SCM-14853） 2018-07-06 -ajb  start

insert into
V_MAIL_TEMPLATE ( template_code , TEMPLATE_NAME , Subject_Zh  ,Subject_En  ,Status ,create_date ,Update_Date ,Mail_Type_Id , prior_level )
values( 10105 , 'SCM_accout_modify_notify' ,'科研之友-修改帐号通知' ,'科研之友-修改帐号通知' , 0 ,sysdate ,sysdate ,23 ,'A') ;

commit ;

--原因（SCM-14853） 2018-07-06 -ajb  end

--原因（SCM-18014） 2018-7-9 ltl begin
update psn_SCIENCE_AREA a  
set a.science_area=(select c.category_zh from CATEGORY_SCM c where a.science_area_id = c.categry_id)
,a.science_area_en=(select d.category_en from CATEGORY_SCM d where a.science_area_id = d.categry_id)
where exists 
(select 1 from CATEGORY_SCM b where a.science_area_id = b.categry_id and (a.science_area!=b.category_zh or a.science_area_en != b.category_en));
commit;
update RECOMMEND_SCIENCE_AREA a  
set a.science_area=(select c.category_zh from CATEGORY_SCM c where a.science_area_id = c.categry_id)
,a.science_area_en=(select d.category_en from CATEGORY_SCM d where a.science_area_id = d.categry_id)
where exists 
(select 1 from CATEGORY_SCM b where a.science_area_id = b.categry_id and (a.science_area!=b.category_zh or a.science_area_en != b.category_en));
commit;
--原因（SCM-18014） 2018-7-9 ltl end

--原因（SCM-18240） 2018-7-9 lzx begin
update CONST_DICTIONARY set en_us_caption=REPLACE(en_us_caption, 'Friend' ,'Contact'),ZH_TW_CAPTION=REPLACE(ZH_TW_CAPTION, '好友' ,'联系人'), ZH_CN_CAPTION=REPLACE(ZH_CN_CAPTION, '好友' ,'联系人') where category='privacy_permissions';
update CONST_DICTIONARY set en_us_caption=REPLACE(en_us_caption, 'Friend' ,'Contact'),ZH_TW_CAPTION=REPLACE(ZH_TW_CAPTION, '好友' ,'联系人'), ZH_CN_CAPTION=REPLACE(ZH_CN_CAPTION, '好友' ,'联系人') where en_us_caption like '%Friend%';
update CONST_DICTIONARY set en_us_caption=REPLACE(en_us_caption, 'friend' ,'contact'),ZH_TW_CAPTION=REPLACE(ZH_TW_CAPTION, '好友' ,'联系人'), ZH_CN_CAPTION=REPLACE(ZH_CN_CAPTION, '好友' ,'联系人') where en_us_caption like '%friend%';
commit;
--原因（SCM-18240） 2018-7-9 lzx end


--原因（SCM-15544 temp_code:137 138，好友更新研究领域，没有收到邮件） 2018-07-13 zll begin
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
values(291,'InviteEndorseResearchAreaTaskTrigger','*/10 * * * * ?',0,'邀请认同研究领域邮件');
--原因（SCM-15544 temp_code:137 138，好友更新研究领域，没有收到邮件） 2018-07-13 zll end


--原因（SCM-18626 影响力》阅读人员分布地图》数据库中陕西省的英文名称翻译有误，请修改） 2016-7-17 LTL begin
update const_region set en_name='Shanxi' where region_id=610000;
commit;
--原因（SCM-18626 影响力》阅读人员分布地图》数据库中陕西省的英文名称翻译有误，请修改） 2016-7-17 LTL end

--原因（SCM-13725  每月发待处理消息统计邮件） 2018-07-17 zll begin 
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) values(293,'SendDealMessageNoticeMailTaskTrigger','*/10 * * * * ?',1,'待处理任务邮件通知');
insert into app_quartz_setting(app_id,task_name,value) values(148,'SendDealMessageNoticeMailTask_PsnId',0);
insert into v_mail_template(template_code,template_name,subject_zh,subject_en,status,create_date,update_date,msg,mail_type_id,limit_status,prior_level)
values(10106,'Deal_Message_Notice_Template','科研之友-待处理消息通知'，'科研之友-待处理消息通知'，1，sysdate,sysdate,'',0,3,'D');

--原因（SCM-13725  每月发待处理消息统计邮件） 2018-07-17 zll end 

--原因（有CQ号带上CQ号） 2016-12-8 WSN end



--原因（center-job相关调整） 2018-6-27 hcj start
alter table v_job_online_history add(START_TIME DATE);
alter table v_job_online_history add(ELAPSED_TIME NUMBER(18,0));
COMMENT ON COLUMN v_job_online_history.START_TIME IS '执行开始时间';
COMMENT ON COLUMN v_job_online_history.ELAPSED_TIME is '任务执行总耗时，单位：ms';
--原因（center-job相关调整） 2018-6-27 hcj end

--原因（SCM-17100 站外底部论文：pdwh_index_publication表中文标题的记录zh_title_short第一个字是中文的？） 2018-07-19 Zll begin

insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
values(198,'CleanPdwhIndexPubDataTaskTrigger','*/10 * * * * ?',1,'处理pdwh_index_publication表中文转拼音不成功问题');

--原因（SCM-17100 站外底部论文：pdwh_index_publication表中文标题的记录zh_title_short第一个字是中文的？） 2018-07-19 Zll end

--原因    SCM-18627 微信》论文推荐》科技领域，删除后，保存，还是显示了被删除的科技领域 2018-07-31 LTL begin

-- Create table
create table V_RECOMMEND_INIT
(
  id                  NUMBER(18) not null,
  psn_id              NUMBER(18) not null,
  pub_recommend_init  NUMBER(1) default 0,
  fund_recommend_init NUMBER(1) default 0
)
tablespace V3SNS
  pctfree 0
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
comment on table V_RECOMMEND_INIT
  is '基金推荐、论文推荐初始化表';
-- Add comments to the columns 
comment on column V_RECOMMEND_INIT.id
  is 'id';
comment on column V_RECOMMEND_INIT.psn_id
  is '人员id';
comment on column V_RECOMMEND_INIT.pub_recommend_init
  is '推荐成果是否初始化0：没有初始化、1：已经初始化';
comment on column V_RECOMMEND_INIT.fund_recommend_init
  is '推荐基金是否初始化0：没有初始化、1：已经初始化';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_RECOMMEND_INIT
  add constraint PK_V_RECOMMEND_INIT primary key (ID)
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
alter table V_RECOMMEND_INIT
  add constraint PK_V_RECOMMEND_INIT_PSN_ID unique (PSN_ID)
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
create sequence SEQ_V_RECOMMEND_INIT
minvalue 1
maxvalue 999999999999999999
start with 3409
increment by 1
cache 20;
--原因    SCM-18627 微信》论文推荐》科技领域，删除后，保存，还是显示了被删除的科技领域 2018-07-31 LTL end
--原因（SCM-18824 每个月待处理消息通知邮件，需要实现每个月定时自动发送，不需要人工介入） 2018-08-09 ZLL begin
Update v_quartz_cron_expression t Set t.cron_expression='0 59 23 L * ?',t.description='待处理任务邮件通知(每个月最后一天晚上23:59分触发)'
Where t.cron_trigger_bean='SendDealMessageNoticeMailTaskTrigger';

--原因（SCM-18824 每个月待处理消息通知邮件，需要实现每个月定时自动发送，不需要人工介入） 2018-08-09 ZLL end
--原因（SCM-000 成果改造邮件修改 赞分享评论邮件改为新的发送方式） 2018-9-4 YHX begin
update v_mail_template t set t.subject_zh='{0}赞了你的论文：{1}',t.subject_en='{0} liked your publications : {1}', t.status='0',t.update_date=sysdate where t.template_code=10051;
update v_mail_template t set t.subject_zh='{0}分享了你的论文：{1}',t.subject_en='{0} shared your publications : {1}', t.status='0',t.update_date=sysdate where t.template_code=10052;
update v_mail_template t set t.subject_zh='{0}评论了你的论文：{1}',t.subject_en='{0} commented your publications : {1}', t.status='0',t.update_date=sysdate where t.template_code=10054;
--原因（SCM-000 成果改造邮件修改 赞分享评论邮件改为新的发送方式） 2018-9-4 YHX end




----SCM-19204 成果全文指派任务测试（基准库全文与个人全文）2018-09-08 zll begin 
Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
Values(300,'PubfulltextRcmdTaskTrigger','*/10 * * * * ?',0,'成果全文推荐');

----SCM-19204 成果全文指派任务测试（基准库全文与个人全文）2018-09-08 zll end alter table V_PUB_DUPLICATE add(HASH_DOI VARCHAR2(32));



---scm-0000 新增全文上传记录表   2018-09-10 zll begin-------
-- Create table
create table pub_fulltext_upload_log
(
  id               NUMBER(18),
  upload_psn_id    NUMBER(18),
  sns_pub_id       NUMBER(18),
  pdwh_pub_id      NUMBER(18),
  fulltext_file_id NUMBER(18),
  status           number(2)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 128K
    next 8K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table pub_fulltext_upload_log
  is '全文上传记录表';
-- Add comments to the columns 
comment on column pub_fulltext_upload_log.id
  is '主键';
comment on column pub_fulltext_upload_log.upload_psn_id
  is '上传全文人员id';
comment on column pub_fulltext_upload_log.sns_pub_id
  is '个人库成果id';
comment on column pub_fulltext_upload_log.pdwh_pub_id
  is '基准库成果id';
comment on column pub_fulltext_upload_log.fulltext_file_id
  is '全文id';
comment on column pub_fulltext_upload_log.status
  is '是否匹配了其他人';
-- Create/Recreate indexes 
create unique index pk_upload_log_id on pub_fulltext_upload_log (id);


create sequence SEQ_PUB_FULLTEXT_UPLOAD_LOG
minvalue 1
maxvalue 999999999999999999999999999
start with 11
increment by 1
cache 10;

-- Alter table 
alter table PUB_FULLTEXT_UPLOAD_LOG
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table PUB_FULLTEXT_UPLOAD_LOG add gmt_create DATE;
-- Add comments to the columns 
comment on column PUB_FULLTEXT_UPLOAD_LOG.gmt_create
  is '创建时间';
  
  ---scm-0000 新增全文上传记录表   2018-09-10 zll end-------


--原因 修改分享表分享平台字段备注  2018-9-13 yhx begin
comment on column v_pub_share.platform is '分享平台: 1:动态,2:联系人,3:群组,4:微信,5:新浪微博,6:Facebook,7:Linkedin';
--原因 修改分享表分享平台字段备注  2018-9-13 yhx end

--原因:修改备注信息  2018-09-14 ajb begin
-- Add comments to the columns 
comment on column V_PUB_SAME_RECORD.UPDATE_DATE
  is '取该分组的所有成果，最新的成果的更新时间。';

-- Add comments to the columns 
comment on column V_PUB_SAME_ITEM.UPDATE_DATE
  is '取值这条成果的更新时间';
  
--原因:修改备注信息  2018-09-14 ajb end
--原因（SCM-19204 成果全文指派任务测试（基准库全文与个人全文））20180918 zll begin
-- Alter table 
alter table PUB_FULLTEXT_PSN_RCMD
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table PUB_FULLTEXT_PSN_RCMD modify rcmd_id null;
--原因（SCM-19204 成果全文指派任务测试（基准库全文与个人全文））20180918 zll end


--创建同步人员统计任务表 20180919  tsz begin
create table V_PSN_STATISTICS_REFRESH
(
  psn_id      NUMBER(18) not null,
  status      NUMBER(1),
  update_date DATE,
  msg         VARCHAR2(2000)
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
-- Add comments to the columns 
comment on column V_PSN_STATISTICS_REFRESH.status
  is '0待处理,1处理成功,2处理失败';
comment on column V_PSN_STATISTICS_REFRESH.update_date
  is '最后处理时间 ,如果状态没变化，那么最后一次处理是成功的';
comment on column V_PSN_STATISTICS_REFRESH.msg
  is '信息';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_PSN_STATISTICS_REFRESH
  add primary key (PSN_ID)
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
  
  --创建同步人员统计任务表 20180919  tsz end

--原因（SCM-18998 数据库关键字存在重复数据，此语句用于去除数据库用户的重复关键字记录） 2018-9-21 WSN begin

delete from PSN_DISCIPLINE_KEY t
where
    (t.psn_id,t.key_words) 
    in
      (select t.psn_id, t.key_words from PSN_DISCIPLINE_KEY t group by t.key_words, t.psn_id having count(t.id) >=2)
    and rowid not in ( select min(rowid) from PSN_DISCIPLINE_KEY t group by t.key_words, t.psn_id having count(t.id)>=2 );


--原因（SCM-18998 数据库关键字存在重复数据，此语句用于去除数据库用户的重复关键字记录） 2018-9-21 WSN end

----SCM-19415  基准库全文pdf转预览图任务》将没有全文预览图的成果拿去跑任务，转换预览图任务失败 2018-09-29 zll begin
-- Add/modify columns 
alter table PUB_FULLTEXT_UPLOAD_LOG add pdwh_pub_to_image number(2);
alter table PUB_FULLTEXT_UPLOAD_LOG add sns_pub_to_image number(2);
-- Add comments to the columns 
comment on column PUB_FULLTEXT_UPLOAD_LOG.pdwh_pub_to_image
  is '基准库成果全文是否转成图片 0:未转,1:已转 ,2:失败';
comment on column PUB_FULLTEXT_UPLOAD_LOG.sns_pub_to_image
  is '个人库成果全文是否转成图片 0:未转,1:已转 ,2:失败';
alter table PUB_FULLTEXT_UPLOAD_LOG modify STATUS default 0;
alter table PUB_FULLTEXT_UPLOAD_LOG modify pdwh_pub_to_image default 0;
alter table PUB_FULLTEXT_UPLOAD_LOG modify sns_pub_to_image default 0;

-- Add/modify columns 
alter table PUB_FULLTEXT_UPLOAD_LOG add error_msg varchar2(500);
-- Add comments to the columns 
comment on column PUB_FULLTEXT_UPLOAD_LOG.error_msg
  is '全文转图片错误信息';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PUB_FULLTEXT_UPLOAD_LOG
add constraint PK_UPLOAD_LOG_ID primary key (ID);
  
Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
Values (416,'PdwhPubFulltextToImageTaskTrigger','*/10 * * * * ?',0,'基准库全文转图片');

Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
Values (417,'SnsPubFulltextToImageTaskTrigger','*/10 * * * * ?',0,'个人库全文转图片');
  
----SCM-19415  基准库全文pdf转预览图任务》将没有全文预览图的成果拿去跑任务，转换预览图任务失败 2018-09-29 zll end



--原因（SCM-18816 BPO已经删除了基金资助类别，但在solr里面没有删除） 2018-10-10 zll begin
 Insert Into v_open_token_service_const(Id,token,service_type,status,create_date,descr,access_date,access_num,access_max_num)
 Values(15700,'00000000','fundsolr',0,Sysdate,'删除基金信息'，Sysdate,0,1000);
  Insert Into v_open_token_service_const(Id,token,service_type,status,create_date,descr,access_date,access_num,access_max_num)
 Values(15701,'11111111','fundsolr',0,Sysdate,'删除基金信息'，Sysdate,0,1000);
 
  Insert Into v_open_token_service_const(Id,token,service_type,status,create_date,descr,access_date,access_num,access_max_num)
 Values(15702,'00000000','fun2solr',0,Sysdate,'更新基金信息'，Sysdate,0,1000);
  Insert Into v_open_token_service_const(Id,token,service_type,status,create_date,descr,access_date,access_num,access_max_num)
 Values(15703,'11111111','fun2solr',0,Sysdate,'更新基金信息'，Sysdate,0,1000);
 --原因（SCM-18816 BPO已经删除了基金资助类别，但在solr里面没有删除） 2018-10-10 zll end

 
 -- 单位合并后台任务 (ROL-5280) 2018-11-04 yxy begin
insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (310, 'SieMergeInsTaskTriggers', '*/10 * * * * ?', 0, 'SIE合并单位任务');

-- 单位合并后台任务 (ROL-5280) 2018-11-04 yxy end

--原因（SCM-21086 扩展取消关联接口） 2018-11-06  ajb begin

-- Create table
create table V_OPEN_USER_UNUNION_LOG
(
  ID       NUMBER(12) not null,
  PSN_ID   NUMBER(22),
  OPEN_ID  NUMBER(22),
  TOKEN    VARCHAR2(30),
  LOG_DATE DATE,
  MSG      VARCHAR2(100)
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
-- Create/Recreate primary, unique and foreign key constraints
alter table V_OPEN_USER_UNUNION_LOG
  add primary key (ID)
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
create index IDX_OPEN_USER_UNION_PSNID on V_OPEN_USER_UNUNION_LOG (PSN_ID)
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
commit ;

insert into  v_open_token_service_const (id , token,service_type,descr) values (800  , '00000000' ,'5h6jjk9b','关联账号') ;
insert into  v_open_token_service_const (id , token,service_type,descr) values (801  , '1111111' ,'5h6jjk9b','关联账号') ;
-- Add comments to the columns
comment on column V_OPEN_USER_UNION.CREATE_TYPE
  is '0 通过openid验证页面生成; 1 通过guid验证 系统自动生成; 2 通过人员注册 或人员绑定自动生成; 3 微信 绑定生成; 4 互联互通帐号关联; 5 后台任务自动生成 ;7调open接口生成';

commit ;

--原因（SCM-21086 扩展取消关联接口） 2018-11-06  ajb end

--原因（SCM-21086 扩展取消关联接口） 2018-11-06  ajb begin

-- Create sequence
create sequence SEQ_V_OPEN_USER_UNUNION_LOG
minvalue 1
maxvalue 99999999
start with 19
increment by 1
nocache;

commit ;

--原因（SCM-21086 扩展取消关联接口） 2018-11-06  ajb end

--原因（SCM-20227 全文推荐PUB_FULLTEXT_PSN_RCMD表，db_id字段，个人全文时也取1（isi成果）） 2018-10-16 zll begin
comment on column PUB_FULLTEXT_PSN_RCMD.match_type
  is '匹配类型：1、sourceId，2、title.3、基准库跟个人库的关联关系匹配';

--原因（SCM-20227 全文推荐PUB_FULLTEXT_PSN_RCMD表，db_id字段，个人全文时也取1（isi成果）） 2018-10-16 zll end

 --原因( SCM-20577 pub_fulltext_upload_log，一条成果id只有一条记录，上传2篇同一条基准库成果全文后，第一篇有推荐给科研之友成果1，第二篇就不推荐了？以前的逻辑应该都保存的)  2018-10-16 zll begin
  -- Drop indexes
drop index UQ_PUB_FULLTEXT_PSN_RCMD;
-- Create/Recreate indexes
create unique index uk_pub_fulltext_psn_rcmd on PUB_FULLTEXT_PSN_RCMD (pub_id, fulltext_file_id, db_id);
 --原因( SCM-20577 pub_fulltext_upload_log，一条成果id只有一条记录，上传2篇同一条基准库成果全文后，第一篇有推荐给科研之友成果1，第二篇就不推荐了？以前的逻辑应该都保存的)  2018-10-16 zll end

 --原因（    SCM-20756 报告成果推荐_3.pptx，demo相关调整） 2018-10-26  ajb begin


-- Add/modify columns
alter table DEMO_PRJ_GROUP_UNION add LAST_GET_GROUP_RCMD_PUB_DATE date;
-- Add comments to the columns
comment on column DEMO_PRJ_GROUP_UNION.LAST_GET_GROUP_RCMD_PUB_DATE
  is '上一次获取群组推荐成果的更新时间';


insert into  v_open_token_service_const (id , token,service_type) values (seq_v_open_token_service_id.nextval  , '00000000' ,'grp45rcd') ;

-- Alter table
alter table DEMO_CACHE_PUB_INFO
  storage
  (
    next 1
  )
;
-- Add comments to the columns
comment on column DEMO_CACHE_PUB_INFO.PUB_TYPE
  is '成果类型：1=个人成果；2=推荐成果；4=群组成果；5=群组推荐成果';


commit ;

--原因（    SCM-20756 报告成果推荐_3.pptx，demo相关调整） 2018-10-26  ajb end


--SCM-20659 联邦检索，导入成果后，如果有合作者，应该弹出合作者列表，更新pub_know表中的title_hash值，供查找有相同成果的人员   2018-10-30  begin
update pub_know p set p.zh_title_hash = (select hash_title from V_PUB_DUPLICATE d where d.pub_id = p.pub_id) where exists (select 1 from V_PUB_DUPLICATE d where d.pub_id = p.pub_id);

update pub_know p set p.en_title_hash = p.zh_title_hash;

commit;
--SCM-20659 联邦检索，导入成果后，如果有合作者，应该弹出合作者列表，更新pub_know表中的title_hash值，供查找有相同成果的人员   2018-10-30  end

Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
Values(614,'SendGroupPubRcmdMailTaskTrigger','*/10 * * * * ?',0,'报告成果推荐邮件');

Insert Into v_mail_template(template_code,template_name,subject_zh,subject_en,status,create_date,update_date,msg,mail_type_id,limit_status,prior_level)
Values(10112,'Group_Pub_Rcmd_Template','报告成果推荐','报告成果推荐',0,Sysdate,Sysdate,'报告成果推荐',26,3,'D');

 Insert Into app_quartz_setting(app_id,task_name,value)
 Values(242,'SendGroupPubRcmdMailTask_removeJtGrpId',0);
  Insert Into app_quartz_setting(app_id,task_name,value)
 Values(243,'SendGroupPubRcmdMailTask_removeJzGrpId',0);


alter table JZBGSJ_MODIFIED
  add constraint pk_JZBGSJ_MODIFIED primary key (PRJ_ID);
alter table JTBGSJ_MODIFIED
  add constraint pk_JTBGSJ_MODIFIED primary key (PRJ_ID);


  --原因（SCM-20752 报告成果推荐_3.pptx，报告成果推荐邮件模板） 2018-10-31  zll begin

  alter table PUB_FULLTEXT_UPLOAD_LOG add is_privacy number(2)  default 0;
alter table PUB_FULLTEXT_UPLOAD_LOG add is_delete number(2)  default 0;
-- Add comments to the columns
comment on column PUB_FULLTEXT_UPLOAD_LOG.is_privacy
  is '是否是隐私全文;2:自己可下载;0:所有人可下载;1好友可下载';
comment on column PUB_FULLTEXT_UPLOAD_LOG.is_delete
  is '全文是否被删除 1:是;0:否';
 --原因（SCM-20752 报告成果推荐_3.pptx，报告成果推荐邮件模板） 2018-10-31  zll end


--原因（SCM-20799 全文推荐，删除成果后，没有重置pub_fulltext_upload_log表status，导致已推荐记录没有被删除） 2018-11-1 zll begin

drop index UK_PUB_FULLTEXT_PSN_RCMD;

--原因（SCM-20799 全文推荐，删除成果后，没有重置pub_fulltext_upload_log表status，导致已推荐记录没有被删除） 2018-11-1 zll end

--原因（Crossref中通过restful API抓取成果数据） 2018-11-7 zll begin
Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
Values(538,'SaveCrossRefDataTaskTrigger','*/10 * * * * ?',0,'保存crossRef数据');
--原因（Crossref中通过restful API抓取成果数据） 2018-11-7 zll end


--合并个人版的，赞、阅读、分享（ROL-5281） 2018-11-07 ZTG begin


insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (1001, 'sieAddSnsPubStatisticsTaskTrigger', '*/10 * * * * ?', 0, '同步个人库成果赞，分享，阅读的统计数');

insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (1002, 'sieAddSnsPatStatisticsTaskTrigger', '*/10 * * * * ?', 0, '同步个人库专利赞，分享，阅读的统计数');


--合并个人版的，赞、阅读、分享（ROL-5281） 2018-11-07 ZTG end


-原因（SCM-20227 全文推荐PUB_FULLTEXT_PSN_RCMD表，db_id字段，个人全文时也取1（isi成果）） 2018-10-16 zll begin
comment on column PUB_FULLTEXT_PSN_RCMD.match_type
  is '匹配类型：1、sourceId，2、title.3、基准库跟个人库的关联关系匹配';
  
--原因（SCM-20227 全文推荐PUB_FULLTEXT_PSN_RCMD表，db_id字段，个人全文时也取1（isi成果）） 2018-10-16 zll end
  
 --原因( SCM-20577 pub_fulltext_upload_log，一条成果id只有一条记录，上传2篇同一条基准库成果全文后，第一篇有推荐给科研之友成果1，第二篇就不推荐了？以前的逻辑应该都保存的)  2018-10-16 zll begin
  -- Drop indexes 
drop index UQ_PUB_FULLTEXT_PSN_RCMD;
-- Create/Recreate indexes 
create unique index uk_pub_fulltext_psn_rcmd on PUB_FULLTEXT_PSN_RCMD (pub_id, fulltext_file_id, db_id);
 --原因( SCM-20577 pub_fulltext_upload_log，一条成果id只有一条记录，上传2篇同一条基准库成果全文后，第一篇有推荐给科研之友成果1，第二篇就不推荐了？以前的逻辑应该都保存的)  2018-10-16 zll end

 -原因（    SCM-20756 报告成果推荐_3.pptx，demo相关调整） 2018-10-26  ajb begin


-- Add/modify columns
alter table DEMO_PRJ_GROUP_UNION add LAST_GET_GROUP_RCMD_PUB_DATE date;
-- Add comments to the columns
comment on column DEMO_PRJ_GROUP_UNION.LAST_GET_GROUP_RCMD_PUB_DATE
  is '上一次获取群组推荐成果的更新时间';


insert into  v_open_token_service_const (id , token,service_type) values (seq_v_open_token_service_id.nextval  , '00000000' ,'grp45rcd') ;

-- Alter table
alter table DEMO_CACHE_PUB_INFO
  storage
  (
    next 1
  )
;
-- Add comments to the columns
comment on column DEMO_CACHE_PUB_INFO.PUB_TYPE
  is '成果类型：1=个人成果；2=推荐成果；4=群组成果；5=群组推荐成果';


commit ;
  
--原因（    SCM-20756 报告成果推荐_3.pptx，demo相关调整） 2018-10-26  ajb end


--SCM-20659 联邦检索，导入成果后，如果有合作者，应该弹出合作者列表，更新pub_know表中的title_hash值，供查找有相同成果的人员   2018-10-30  begin
update pub_know p set p.zh_title_hash = (select hash_title from V_PUB_DUPLICATE d where d.pub_id = p.pub_id) where exists (select 1 from V_PUB_DUPLICATE d where d.pub_id = p.pub_id);

update pub_know p set p.en_title_hash = p.zh_title_hash;

commit;
--SCM-20659 联邦检索，导入成果后，如果有合作者，应该弹出合作者列表，更新pub_know表中的title_hash值，供查找有相同成果的人员   2018-10-30  end

Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
Values(614,'SendGroupPubRcmdMailTaskTrigger','*/10 * * * * ?',0,'报告成果推荐邮件');

Insert Into v_mail_template(template_code,template_name,subject_zh,subject_en,status,create_date,update_date,msg,mail_type_id,limit_status,prior_level)
Values(10112,'Group_Pub_Rcmd_Template','报告成果推荐','报告成果推荐',0,Sysdate,Sysdate,'报告成果推荐',26,3,'D');

 Insert Into app_quartz_setting(app_id,task_name,value)
 Values(242,'SendGroupPubRcmdMailTask_removeJtGrpId',0);
  Insert Into app_quartz_setting(app_id,task_name,value)
 Values(243,'SendGroupPubRcmdMailTask_removeJzGrpId',0);
 

alter table JZBGSJ_MODIFIED
  add constraint pk_JZBGSJ_MODIFIED primary key (PRJ_ID); 
alter table JTBGSJ_MODIFIED
  add constraint pk_JTBGSJ_MODIFIED primary key (PRJ_ID);
  
  
  --原因（SCM-20752 报告成果推荐_3.pptx，报告成果推荐邮件模板） 2018-10-31  zll begin

  alter table PUB_FULLTEXT_UPLOAD_LOG add is_privacy number(2)  default 0;
alter table PUB_FULLTEXT_UPLOAD_LOG add is_delete number(2)  default 0;
-- Add comments to the columns 
comment on column PUB_FULLTEXT_UPLOAD_LOG.is_privacy
  is '是否是隐私全文;2:自己可下载;0:所有人可下载;1好友可下载';
comment on column PUB_FULLTEXT_UPLOAD_LOG.is_delete
  is '全文是否被删除 1:是;0:否';
 --原因（SCM-20752 报告成果推荐_3.pptx，报告成果推荐邮件模板） 2018-10-31  zll end
 
  
--原因（SCM-20799 全文推荐，删除成果后，没有重置pub_fulltext_upload_log表status，导致已推荐记录没有被删除） 2018-11-1 zll begin

drop index UK_PUB_FULLTEXT_PSN_RCMD;
  
--原因（SCM-20799 全文推荐，删除成果后，没有重置pub_fulltext_upload_log表status，导致已推荐记录没有被删除） 2018-11-1 zll end

--原因（Crossref中通过restful API抓取成果数据） 2018-11-7 zll begin
Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
Values(538,'SaveCrossRefDataTaskTrigger','*/10 * * * * ?',0,'保存crossRef数据');
--原因（Crossref中通过restful API抓取成果数据） 2018-11-7 zll end


--合并个人版的，赞、阅读、分享（ROL-5281） 2018-11-07 ZTG begin


insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (1001, 'sieAddSnsPubStatisticsTaskTrigger', '*/10 * * * * ?', 0, '同步个人库成果赞，分享，阅读的统计数');

insert into v_quartz_cron_expression (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (1002, 'sieAddSnsPatStatisticsTaskTrigger', '*/10 * * * * ?', 0, '同步个人库专利赞，分享，阅读的统计数');


--合并个人版的，赞、阅读、分享（ROL-5281） 2018-11-07 ZTG end

--原因（Crossref中通过restful API抓取成果数据） 2018-11-7 zll begin
Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
Values(538,'SaveCrossRefDataTaskTrigger','*/10 * * * * ?',0,'保存crossRef数据');
--原因（Crossref中通过restful API抓取成果数据） 2018-11-7 zll end


--合并个人版的，赞、阅读、分享（ROL-5281） 2018-11-10 ZTG begin
UPDATE  v_quartz_cron_expression t SET  t.description='同步个人库成果赞，分享，阅读的统计数(0 0 0 10/15 * ?)' WHERE t.id = 1001;
UPDATE  v_quartz_cron_expression t SET  t.description='同步个人库专利赞，分享，阅读的统计数(0 0 0 10/15 * ?)' WHERE t.id = 1002;
--合并个人版的，赞、阅读、分享（ROL-5281） 2018-11-10 ZTG end

--原因  open接口单位id限制调整（SCM-21184） 20181112 tsz begin
alter table v_open_token_service_const add (ins_id number(18));
comment  on  column  v_open_token_service_const.ins_id   is  '限制单位id';

alter table v_open_third_reg drop (ins_id);

update v_open_token_service_const t set t.ins_id=774 where t.token='lai23c3j' and t.service_type='obt63pub';
update v_open_token_service_const t set t.ins_id=774 where t.token='lai23c3j' and t.service_type='obt75rcf';
update v_open_token_service_const t set t.ins_id=774 where t.token='lai23c3j' and t.service_type='obtrcf6p';

update v_open_token_service_const t set t.ins_id=1079 where t.token='ydso20de' and t.service_type='obt63pub';
update v_open_token_service_const t set t.ins_id=1079 where t.token='ydso20de' and t.service_type='obt63pub';
update v_open_token_service_const t set t.ins_id=1079 where t.token='ydso20de' and t.service_type='obt63pub';

--原因  open接口单位id限制调整（SCM-21184） 20181112 tsz end


--原因  SCM-20852：现在将v_pub_view中的数据同步到vist_statistics表中，使用定时任务：InsertDataFromPubViewToVistStatisticsTask，创建定时任务  2018-11-13 syl begin
insert into V_QUARTZ_CRON_EXPRESSION values(620,'InsertDataFromPubViewToVistStatisticsTaskTrigger','*/10 * * * * ?',0,'将数据从V_PUB_VIEW中修复vist_statistics表中的数据，插入那些没有同步的数据');
commit;
--原因  SCM-20852：现在将v_pub_view中的数据同步到vist_statistics表中，使用定时任务：InsertDataFromPubViewToVistStatisticsTask，创建定时任务 2018-11-13 syl  end

--原因 ROL-5280:单位合并后台任务 2018-11-14 YXY begin
update v_quartz_cron_expression t set t.cron_trigger_bean='sieMergeInsTaskTriggers' where t.cron_trigger_bean='SieMergeInsTaskTriggers';
--原因 ROL-5280:单位合并后台任务 2018-11-14 YXY end


--原因（SCM-20991 生产机，有个账号的个人成果详情的相似论文显示数据异常） 2018-11-15 zll begin

Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description)
Values(615,'HandlePubRelationErrorDataTaskTrigger','*/10 * * * * ?',0,'处理基准库成果与个人库数据关联错误问题 ');

--原因（SCM-20991 生产机，有个账号的个人成果详情的相似论文显示数据异常） 2018-11-15 zll end


--ROL-5614基准库成果导入SIE后台任务（ImportPublicationsTaskTrigger），无收录情况时没有清除pub_list记录  2018-11-14 zsj begin

update v_quartz_cron_expression t set t.cron_trigger_bean = 'sieImportPublicationsTaskTrigger' where t.id = 173;

--ROL-5614基准库成果导入SIE后台任务（ImportPublicationsTaskTrigger），无收录情况时没有清除pub_list记录  2018-11-14 zsj end