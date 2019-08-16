-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因 （SCM-18615：人员检索》输入机构名全称为浙江理工大学的部分名称，人员无法检索出来，重新写入中文分词器中文词典） 2019-4-28 SYL begin
update V_QUARTZ_CRON_EXPRESSION t set t.status=1 where t.cron_trigger_bean='WriteIKAnalyerDictFileTaskTrigger';
--原因 （SCM-18615：人员检索》输入机构名全称为浙江理工大学的部分名称，人员无法检索出来，重新写入中文分词器中文词典） 2019-4-28 SYL end
--原因SCMAPP-1455 项目分享到qq好友、qq空间还是微信的状态值，正常应该是等于8 2018-4-29 LTL begin
comment on column V_PUB_SHARE.platform
  is '分享平台: 1:动态,2:联系人,3:群组,4:微信,5:新浪微博,6:Facebook,7:Linkedin,8:qq空间或qq好友';
--原因SCMAPP-1455 项目分享到qq好友、qq空间还是微信的状态值，正常应该是等于8 2018-4-29 LTL end  
--原因scm-0000 首页论文任务添加任务设置 可以删除目录表  HHT begin
---插入 记录是否要删除三个目录表  --513   indexInfoInitTaskTrigger
insert into APP_QUARTZ_SETTING(app_id,task_name,value) values(513,'indexInfoInitTask',0);
--原因scm-0000 首页论文任务添加任务设置表可以删除目录表  HHT  end  
  
--原因（SCM-25203 合并账号，第一次合并提示“操作失败” 第二次合并提示“正在被合并” 但实际上根本没有做合并操作 试了2个账号都有此情况） 2019-5-13 YHX begin 

alter table sys_merge_user_info modify ZH_NAME VARCHAR2(100);

--原因（SCM-25203 合并账号，第一次合并提示“操作失败” 第二次合并提示“正在被合并” 但实际上根本没有做合并操作 试了2个账号都有此情况） 2019-5-13 YHX end










--原因（新闻模块功能） 2019-05-20 ajb start

-- Create table
create table V_NEWS_BASE
(
  ID            NUMBER(18) not null,
  TITLE         NVARCHAR2(200),
  BRIEF         NVARCHAR2(200),
  IMAGE         NVARCHAR2(200),
  CONTENT       CLOB,
  STATUS        NUMBER(2),
  GMT_CREATE    DATE,
  GMT_UPDATE    DATE,
  GMT_PUBLISH   DATE,
  HEAT          NUMBER(8),
  SEQ_NO        NUMBER(18),
  CREATE_PSN_ID NUMBER(18)
);
-- Add comments to the table
comment on table V_NEWS_BASE
  is '新闻主表';
-- Add comments to the columns
comment on column V_NEWS_BASE.ID
  is '主键';
comment on column V_NEWS_BASE.TITLE
  is 'title';
comment on column V_NEWS_BASE.BRIEF
  is '简介';
comment on column V_NEWS_BASE.IMAGE
  is '图片';
comment on column V_NEWS_BASE.CONTENT
  is '正文';
comment on column V_NEWS_BASE.STATUS
  is '0=未发布； 1=已发布； 9=删除';
comment on column V_NEWS_BASE.GMT_CREATE
  is '创建时间';
comment on column V_NEWS_BASE.GMT_UPDATE
  is '更新时间';
comment on column V_NEWS_BASE.GMT_PUBLISH
  is '发布时间';
comment on column V_NEWS_BASE.HEAT
  is '热度';
comment on column V_NEWS_BASE.SEQ_NO
  is '管理员列表排序 字段';
comment on column V_NEWS_BASE.CREATE_PSN_ID
  is '创建人员psnId';
-- Create/Recreate primary, unique and foreign key constraints
alter table V_NEWS_BASE
  add constraint PK_V_NEWS_BASE primary key (ID);



-- Create table
create table V_NEWS_LIKE
(
  ID          NUMBER(18) not null,
  NEWS_ID     NUMBER(18) not null,
  LIKE_PSN_ID NUMBER(18) not null,
  STATUS      NUMBER(2),
  GMT_CREATE  DATE,
  GMT_UPDATE  DATE
);
-- Add comments to the table
comment on table V_NEWS_LIKE
  is '新闻 赞表';
-- Add comments to the columns
comment on column V_NEWS_LIKE.ID
  is '主键';
comment on column V_NEWS_LIKE.NEWS_ID
  is '新闻主键';
comment on column V_NEWS_LIKE.LIKE_PSN_ID
  is '赞人员id';
comment on column V_NEWS_LIKE.STATUS
  is '0 = 没有赞 ； 1=已赞';
comment on column V_NEWS_LIKE.GMT_CREATE
  is '创建时间';
comment on column V_NEWS_LIKE.GMT_UPDATE
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints
alter table V_NEWS_LIKE
  add constraint PK_V_NEWS_LIKE primary key (ID);
alter table V_NEWS_LIKE
  add constraint IDX_V_NEWS_LIKE unique (NEWS_ID, LIKE_PSN_ID);


-- Create table
create table V_NEWS_SHARE
(
  ID           NUMBER(18) not null,
  NEWS_ID      NUMBER(18) not null,
  SHARE_PSN_ID NUMBER(18) not null,
  STATUS       NUMBER(2),
  PLATFORM     NUMBER(2),
  BE_SHARED_ID NUMBER(18),
  GMT_CREATE   DATE,
  GMT_UPDATE   DATE
);
-- Add comments to the table
comment on table V_NEWS_SHARE
  is '新闻分享';
-- Add comments to the columns
comment on column V_NEWS_SHARE.ID
  is '主键';
comment on column V_NEWS_SHARE.NEWS_ID
  is '新闻主键';
comment on column V_NEWS_SHARE.SHARE_PSN_ID
  is '分享人员id';
comment on column V_NEWS_SHARE.STATUS
  is '状态 0=正常 ； 9=删除';
comment on column V_NEWS_SHARE.PLATFORM
  is '分享平台 见 SharePlatformEnum';
comment on column V_NEWS_SHARE.BE_SHARED_ID
  is '被分享的主键， 例如 好友，群组';
comment on column V_NEWS_SHARE.GMT_CREATE
  is '创建时间';
comment on column V_NEWS_SHARE.GMT_UPDATE
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints
alter table V_NEWS_SHARE
  add constraint PK_V_NEWS_SHARE primary key (ID);
-- Create/Recreate indexes
create index INX_V_NEWS_SHARE on V_NEWS_SHARE (NEWS_ID, SHARE_PSN_ID);


-- Create table
create table V_NEWS_STATISTICS
(
  NEWS_ID     NUMBER(18) not null,
  AWARD_COUNT NUMBER(8),
  VIEW_COUNT  NUMBER(8),
  SHARE_COUNT NUMBER(8)
);
-- Add comments to the table
comment on table V_NEWS_STATISTICS
  is '新闻  统计记录表';
-- Add comments to the columns
comment on column V_NEWS_STATISTICS.NEWS_ID
  is '新闻主键';
comment on column V_NEWS_STATISTICS.AWARD_COUNT
  is '赞统计';
comment on column V_NEWS_STATISTICS.VIEW_COUNT
  is '访问统计';
comment on column V_NEWS_STATISTICS.SHARE_COUNT
  is '分享统计';
-- Create/Recreate primary, unique and foreign key constraints
alter table V_NEWS_STATISTICS
  add constraint PK_V_NEWS_STATISTICS primary key (NEWS_ID);


-- Create table
create table V_NEWS_VIEW
(
  ID           NUMBER(18) not null,
  NEWS_ID      NUMBER(18) not null,
  VIEW_PSN_ID  NUMBER(18) not null,
  IP           VARCHAR2(30),
  GMT_CREATE   DATE,
  FORMATE_DATE NUMBER(15),
  TOTAL_COUNT  NUMBER(4)
);
-- Add comments to the table
comment on table V_NEWS_VIEW
  is '新闻 访问记录表';
-- Add comments to the columns
comment on column V_NEWS_VIEW.ID
  is '主键';
comment on column V_NEWS_VIEW.NEWS_ID
  is '新闻主键';
comment on column V_NEWS_VIEW.VIEW_PSN_ID
  is '访问人员id';
comment on column V_NEWS_VIEW.IP
  is '访问ip';
comment on column V_NEWS_VIEW.GMT_CREATE
  is '创建时间';
comment on column V_NEWS_VIEW.FORMATE_DATE
  is '日期格式化';
comment on column V_NEWS_VIEW.TOTAL_COUNT
  is '所有的浏览次数';
-- Create/Recreate primary, unique and foreign key constraints
alter table V_NEWS_VIEW
  add constraint PK_V_NEWS_VIEW primary key (ID);
-- Create/Recreate indexes
create index IND_V_NEWS_VIEW on V_NEWS_VIEW (NEWS_ID);



-- Create sequence
create sequence SEQ_V_NEWS_BASE
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 10;

-- Create sequence
create sequence SEQ_V_NEWS_LIKE
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 10;


-- create sequence
create sequence seq_v_news_share
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 10;


-- create sequence
create sequence seq_v_news_view
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 10;


-- add/modify columns
alter table v_news_share add content nvarchar2(2000);
-- add comments to the columns
comment on column v_news_share.content
  is '分享内容';



insert into sys_authoritie(id,name,display_name) values(10009,'role_news_scmmanagement','权限：新闻管理');
insert into sys_role_authoritie(role_id,authority_id) values(9,10009);
insert into sys_role(id,name,description) values(9,'新闻管理角色','访问新闻管理页面');


--原因（新闻模块功能） 2019-05-20 ajb end




--原因（scm-000 新闻） 2019-05-21  ajb start
-- Create table
create table V_NEWS_RECOMMEND_RECORD
(
  ID           NUMBER(18) not null,
  PSN_ID       NUMBER(18),
  NEWS_ID      NUMBER(18),
  STATUS       NUMBER(1),
  GMT_CREATE   DATE,
  GMT_MODIFIED DATE
);
-- Add comments to the table
comment on table V_NEWS_RECOMMEND_RECORD
is '新闻推荐操作记录表';
-- Add comments to the columns
comment on column V_NEWS_RECOMMEND_RECORD.ID
is 'ID';
comment on column V_NEWS_RECOMMEND_RECORD.PSN_ID
is '人员ID';
comment on column V_NEWS_RECOMMEND_RECORD.NEWS_ID
is '新闻ID';
comment on column V_NEWS_RECOMMEND_RECORD.STATUS
is '状态：0正常，1不感兴趣';
comment on column V_NEWS_RECOMMEND_RECORD.GMT_CREATE
is '创建时间';
comment on column V_NEWS_RECOMMEND_RECORD.GMT_MODIFIED
is '被修改时间';
-- Create/Recreate primary, unique and foreign key constraints
alter table V_NEWS_RECOMMEND_RECORD
  add primary key (ID);
-- Create/Recreate indexes
create index IDX_V_NEWS_RECOMMEND_RECORD on V_NEWS_RECOMMEND_RECORD (PSN_ID);

-- Create sequence
create sequence V_SEQ_NEWS_RECOMMEND_RECORD
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 10;

--原因（scm-000 新闻） 2019-05-21  ajb end
---SCM-25221 全文推荐，相同文件大小的不要重复推荐 2019-5-22 HHT start
------添加一列 全文文件大小
-- Add/modify columns 
alter table PUB_FULLTEXT_PSN_RCMD add file_size number(18);
-- Add comments to the columns 
comment on column PUB_FULLTEXT_PSN_RCMD.file_size
  is '全文文件大小';
---SCM-25221 全文推荐，相同文件大小的不要重复推荐 2019-5-22 HHT end 
--原因    SCM-25269 成果类型增加“标准”和“软件著作权” 2019-05-24 YJ begin
-- Create table
create table V_PUB_INDUSTRY
(
  id            NUMBER(18) not null,
  pub_id        NUMBER(18),
  industry_code VARCHAR2(20)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table
comment on table V_PUB_INDUSTRY is '成果产业记录表';
-- Add comments to the columns
comment on column V_PUB_INDUSTRY.id is '逻辑主键id';
comment on column V_PUB_INDUSTRY.pub_id is '成果id';
comment on column V_PUB_INDUSTRY.industry_code is '产业代码，CATEGORY_HIGHTTECH_INDUSTRY主键';
-- Create/Recreate primary, unique and foreign key constraints
alter table V_PUB_INDUSTRY
  add constraint V_PUB_INDUSTRY_PK primary key (ID)
  using index
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
-- SEQ
create sequence SEQ_PUB_INDUSTRY_ID
start with 1
increment by 1
nomaxvalue
nocycle;

insert into CONST_PUB_TYPE values(12,'标准','Standard',1,12,1);
insert into CONST_PUB_TYPE values(13,'软件著作权','Software Copyright',1,13,1);
--原因    SCM-25269 成果类型增加“标准”和“软件著作权” 2019-05-24 YJ end

--原因    SCM-25269 成果类型增加“标准”和“软件著作权” 2019-05-24 YJ begin
alter table v_pub_duplicate  add(HASH_STANDARD_NO VARCHAR2(32));
alter table v_pub_duplicate  add(HASH_REGISTER_NO VARCHAR2(32));
comment on column V_PUB_DUPLICATE.hash_standard_no is '标准的标准号hash值';
comment on column V_PUB_DUPLICATE.hash_register_no is '软件著作权的登记号hash值';
--原因    SCM-25269 成果类型增加“标准”和“软件著作权” 2019-05-24 YJ end
--原因（SCM-25215 sys_merge_user_info合并任务表的注释文字都是乱码） 2019-5-30 YHX begin
-- Add comments to the table 
comment on table SYS_MERGE_USER_INFO
  is '被合并删除的人员信息表';
-- Add comments to the columns 
comment on column SYS_MERGE_USER_INFO.id
  is '主键';
comment on column SYS_MERGE_USER_INFO.psn_id
  is '保留人的ID';
comment on column SYS_MERGE_USER_INFO.del_psn_id
  is '被删除人的ID';
comment on column SYS_MERGE_USER_INFO.del_des3_psn_id
  is '被删除人的加密ID';
comment on column SYS_MERGE_USER_INFO.avatars
  is '头像';
comment on column SYS_MERGE_USER_INFO.zh_name
  is '中文名称';
comment on column SYS_MERGE_USER_INFO.en_name
  is '英文名称';
comment on column SYS_MERGE_USER_INFO.first_name
  is '名';
comment on column SYS_MERGE_USER_INFO.last_name
  is '姓';
comment on column SYS_MERGE_USER_INFO.titolo
  is '头衔';
comment on column SYS_MERGE_USER_INFO.email
  is '首要邮件地址';
comment on column SYS_MERGE_USER_INFO.ins_name
  is '单位名称';
comment on column SYS_MERGE_USER_INFO.login_count
  is '登录帐号';
comment on column SYS_MERGE_USER_INFO.view_name
  is '显示的名称';
comment on column SYS_MERGE_USER_INFO.view_titolo
  is '显示的头衔';
  --原因（SCM-25215 sys_merge_user_info合并任务表的注释文字都是乱码） 2019-5-30 YHX end
--SCM-25241 SCM-23876修改之前，人员信息有变动但没手动跑的建议重跑一遍，详见图  2019-6-4 HHT begin
update suggest_str_init set status=0;
--SCM-25241 SCM-23876修改之前，人员信息有变动但没手动跑的建议重跑一遍，详见图  2019-6-4 HHT end




--SCM-25153 移动端需要新增群组相关功能页面  添加文件分享统计表   2019-6-6  WSN begin

create table V_GRP_FILE_STATISTICS
(
  grp_file_id NUMBER(18) primary key,
  share_count NUMBER default 0
);

--SCM-25153 移动端需要新增群组相关功能页面  添加文件分享统计表   2019-6-6  WSN end

--原因    SCM-25269 成果类型增加“标准”和“软件著作权” 2019-05-24 YJ begin
comment on column V_PUB_SNS.pub_type is '1:奖励；2:书/著作；3:会议论文；4:期刊论文；5:专利；7:其他；8:学位论文；10:书籍章节；12:标准；13:软件著作权';
--原因    SCM-25269 成果类型增加“标准”和“软件著作权” 2019-05-24 YJ end

---原因(SCM-24827:创建后台任务,修复base_journal表中与base_journal_title表中标题不一致的数据)2019-5-11 SYL start
   insert into V_QUARTZ_CRON_EXPRESSION
     (id, cron_trigger_bean, cron_expression, status, description)
   values
     (( select (max(t.id) + 1) from V_QUARTZ_CRON_EXPRESSION t)
     ,
      'BaseJournalTitleRepairTaskTrigger',
      '*/10 * * * * ?',
      1,
      '修复base_journal表中的标题数据');
---原因(SCM-24827:创建后台任务,修复base_journal表中与base_journal_title表中标题不一致的数据) 2019-5-11 SYL start

--SCM-25207 有部分账号的全文请求列表数据中有自己跟自己请求全文的数据，请处理     2019-6-13 HHT start
delete from  v_pub_fulltext_req_recv t where t.req_psn_id=t.recv_psn_id;
--SCM-25207 有部分账号的全文请求列表数据中有自己跟自己请求全文的数据，请处理     2019-6-13 HHT end--SCM-25269 成果类型增加“标准”和“软件著作权” 2019-06-13 YJ begin
insert into const_ref_db values(38,'JXBZ','江西省标准化信息服务平台','江西省标准化信息服务平台','http://sd.jxbz.org.cn/Standard/List.aspx','http://sd.jxbz.org.cn/Standard/List.aspx','',0,0,0,1,0,'','','','江西省标准化信息服务平台','江西省标准化信息服务平台','');
insert into const_ref_db values(39,'KAIMAILE','凯迈乐软件著作权登记网','凯迈乐软件著作权登记网','http://www.kaimaile.com/','http://www.kaimaile.com/','',0,0,0,1,0,'','','','凯迈乐软件著作权登记网','凯迈乐软件著作权登记网','');
--SCM-25269 成果类型增加“标准”和“软件著作权” 2019-06-13 YJ end

--原因（SCM-25378：创建人员姓氏拼音对应表，解决人员姓氏识别不准确问题)2019-6-14 SYL start
create table CONST_PSN_LASTNAME_PY
(
  id          NUMBER(5) not null,
  zh_word     VARCHAR2(10) not null,
  pinyin_word VARCHAR2(20) not null
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
  )
compress;
-- Add comments to the table 
comment on table CONST_PSN_LASTNAME_PY
  is '人员姓氏多音字读音(拼音)常量表';
-- Add comments to the columns 
comment on column CONST_PSN_LASTNAME_PY.id
  is '多音姓氏id主键';
comment on column CONST_PSN_LASTNAME_PY.zh_word
  is '多音姓氏中文词';
comment on column CONST_PSN_LASTNAME_PY.pinyin_word
  is '多音姓氏对应的读音拼音（首字母大写）';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CONST_PSN_LASTNAME_PY
  add constraint PK_CONST_PSN_LASTNAME_PY primary key (ID)
  using index 
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
-- Create/Recreate indexes 
create index IDX_CONST_PSN_LASTNAME_PY on CONST_PSN_LASTNAME_PY (ZH_WORD)
  tablespace V3SNS
  pctfree 10
  initrans 2
  maxtrans 255;
  
create sequence seq_const_psn_lastname_py
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

insert into const_psn_lastname_py values(seq_const_psn_lastname_py.nextval,'单','Shan');
insert into const_psn_lastname_py values(seq_const_psn_lastname_py.nextval,'仇','Qiu');
insert into const_psn_lastname_py values(seq_const_psn_lastname_py.nextval,'解','Xie');
insert into const_psn_lastname_py values(seq_const_psn_lastname_py.nextval,'覃','Qin');
insert into const_psn_lastname_py values(seq_const_psn_lastname_py.nextval,'乐','Yue');
insert into const_psn_lastname_py values(seq_const_psn_lastname_py.nextval,'曾','Zeng');
insert into const_psn_lastname_py values(seq_const_psn_lastname_py.nextval,'费','Fei');
insert into const_psn_lastname_py values(seq_const_psn_lastname_py.nextval,'车','Che');
insert into const_psn_lastname_py values(seq_const_psn_lastname_py.nextval,'洗','Xian');
insert into const_psn_lastname_py values(seq_const_psn_lastname_py.nextval,'宿','Su');
insert into const_psn_lastname_py values(seq_const_psn_lastname_py.nextval,'什','Shi');
insert into const_psn_lastname_py values(seq_const_psn_lastname_py.nextval,'省','Sheng');
insert into const_psn_lastname_py values(seq_const_psn_lastname_py.nextval,'缪','Miao');
insert into const_psn_lastname_py values(seq_const_psn_lastname_py.nextval,'柏','Bai');
insert into const_psn_lastname_py values(seq_const_psn_lastname_py.nextval,'长','Zhang');
--原因（SCM-25378：创建人员姓氏拼音对应表，解决人员姓氏识别不准确问题)2019-6-14 SYL end

--原因    SCM-25817 基准库筛选重复成果任务 2019-06-18 YJ begin
Insert Into v_quartz_cron_expression(Id,cron_trigger_bean,cron_expression,status,description) Values (700,'PdwhPubRepeatTaskTrigger','*/10 * * * * ?',0,'基准库筛选重复成果');
--原因    SCM-25817 基准库筛选重复成果任务 2019-06-18 YJ end

--原因（SCM-25718 影响力优化用户指引） 2019-6-19 YHX begin
-- Create table
create table V_IMPACT_GUIDE_RECORD
(
  id         NUMBER(18) not null,
  psn_id     NUMBER(18),
  status     NUMBER(1),
  gmt_create DATE
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
comment on table V_IMPACT_GUIDE_RECORD
  is '影响力引导记录表';
-- Add comments to the columns 
comment on column V_IMPACT_GUIDE_RECORD.id
  is 'ID';
comment on column V_IMPACT_GUIDE_RECORD.psn_id
  is '人员ID';
comment on column V_IMPACT_GUIDE_RECORD.status
  is '状态：0启用，1暂停';
comment on column V_IMPACT_GUIDE_RECORD.gmt_create
  is '创建时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_IMPACT_GUIDE_RECORD
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
create sequence V_SEQ_IMPACT_GUIDE_RECORD
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20;
--原因（SCM-25718 影响力优化用户指引） 2019-6-19 YHX end