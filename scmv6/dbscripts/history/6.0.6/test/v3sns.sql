-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（删除烂数据） 2017-8-01 zzx begin
 update v_msg_relation t set t.status=2 ,t.deal_status=2 where not exists (select 1 from v_msg_content t2 where t2.content_id = t.content_id) and t.status<>2 and t.deal_status=0 and t.type=11;
 commit;
 
--原因（删除烂数据） 2017-8-01 zzx end
--原因      SCM-13743人员统计数，需要统计公开的成果和项目数 2017-8-10 ltl begin

create table PSN_STATISTICS_PUB_PRJ
(
  psn_id  NUMBER(18) not null,
  pub_sum NUMBER default 0,
  prj_sum NUMBER default 0,
  status  NUMBER default 0
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
comment on table PSN_STATISTICS_PUB_PRJ
  is '统计人员的公开成果和公开项目后台任务的表';
-- Add comments to the columns 
comment on column PSN_STATISTICS_PUB_PRJ.pub_sum
  is '公开成果统计数';
comment on column PSN_STATISTICS_PUB_PRJ.prj_sum
  is '公开项目统计数';
comment on column PSN_STATISTICS_PUB_PRJ.status
  is '处理状态0未处理、1已处理、-1处理失败';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PSN_STATISTICS_PUB_PRJ
  add constraint PK_PSN_ID primary key (PSN_ID)
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

  
-- Add/modify columns 
alter table PSN_STATISTICS add open_pub_sum number default 0;
alter table PSN_STATISTICS add open_prj_sum number default 0;
-- Add comments to the columns 
comment on column PSN_STATISTICS.open_pub_sum
  is '公开成果总数';
comment on column PSN_STATISTICS.open_prj_sum
  is '公开项目总数';
  
  
insert into V_QUARTZ_CRON_EXPRESSION (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
values (63, 'GetPubAndPrjSumTaskTrigger', '*/10 * * * * ?', 1, '获取个人的公开成果和项目数');

--原因     SCM-13743 人员统计数，需要统计公开的成果和项目数  2017-8-10 ltl end

--原因（SCM-13858） 2017-08-15 LJ begin

insert into V_QUARTZ_CRON_EXPRESSION values('66','SplitPubAuthorInfoTaskTrigger','*/10 * * * * ?',0,'从xml拆分获取作者信息');

--原因（SCM-13858） 2017-08-15 LJ end

---原因（SCM-13887 个人文献推荐迁至新系统） 2017-08-17 zll begin
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) 
values (67,'PsnRefRecommendTaskTrigger','0 0/30 * * * ?',1,'个人基准文献推荐新算法');
---原因（SCM-13887 个人文献推荐迁至新系统） 2017-08-17 zll end
--初始化群组默认logo 20170817 tsz begin


update  v_grp_baseinfo t1 set t1.grp_auatars =(select '/resmod/images/group/default_logo/'||t.first_category_id||'_'||trunc(dbms_random.value(1,7))||'.jpg' from v_grp_kw_disc t where t.first_category_id is not null and t.grp_id=t1.grp_id )
 where ( instr(t1.grp_auatars,'logo_grpdefault.png')>0 or instr(t1.grp_auatars,'50X50g.gif')>0 or instr(t1.grp_auatars,'group_no_photo.jpg')>0 )
 and exists (select 1 from v_grp_kw_disc t2 where t2.first_category_id is not  null and t2.grp_id=t1.grp_id);
 commit;

--初始化群组默认logo 20170817 tsz end

 --原因:demo改造，scm_group_tmp增加列 2017-8-18 houchuanjie begin
ALTER TABLE scm_group_tmp ADD (group_pic_url VARCHAR2(100 BYTE)
, pub_sum NUMBER(18,0));
COMMENT ON COLUMN scm_group_tmp.group_pic_url IS '群组头像图片链接';
comment on column scm_group_tmp.pub_sum is '成果/文献数';
ALTER TABLE demo_prj_group_union RENAME COLUMN demo_psn_id TO psn_id;
ALTER TABLE demo_scm_user_union RENAME COLUMN psnid TO psn_id;
--原因:demo改造，scm_group_tmp增加列 2017-8-18 houchuanjie end

--原因:移动端动态改造 2016-8-22 ltl end
alter table DYNAMIC_REPLY_PSN add platform VARCHAR2(50);
comment on column DYNAMIC_REPLY_PSN.platform
  is ' 数据来源pc端，移动端(mobile)';
  
alter table DYNAMIC_REPLY_RES add platform VARCHAR2(50);
comment on column DYNAMIC_REPLY_RES.platform1
  is '数据来源pc端，移动端(mobile)';
  
alter table V_DYNAMIC_MSG add platform VARCHAR2(50);
comment on column V_DYNAMIC_MSG.platform
  is '数据来源pc端，移动端(mobile)';
--原因:移动端动态改造 2016-8-22 ltl end

 --原因:open 接口添加服务常量   2016-8-247 ajb start
insert into  v_open_token_service_const (id , token,service_type) values (seq_v_open_token_service_id.nextval  , '00000000','v663ttyy' ) ;
insert into  v_open_token_service_const (id , token,service_type) values (seq_v_open_token_service_id.nextval  , '00000000','cfm55pub' ) ;
insert into  v_open_token_service_const (id , token,service_type) values (seq_v_open_token_service_id.nextval  , '00000000','pdwh5pub' ) ;
insert into  v_open_token_service_const (id , token,service_type) values (seq_v_open_token_service_id.nextval  , '00000000','sh66info' ) ;
insert into  v_open_token_service_const (id , token,service_type) values (seq_v_open_token_service_id.nextval  , '00000000','v5detail' ) ;
commit ;
 --原因:open 接口添加服务常量   2016-8-247 ajb end

-----INSTITUTION表的单位性质修改备注 zll  end
comment on column INSTITUTION.nature
  is '单位性质
1: college; 2: research center; 3: funding agency
4:企业;5:出版社;6:协会;7:医院 99: others
';
-----INSTITUTION表的单位性质修改备注  zll  end

--原因（demo成果推荐） 2017-8-29 zzx  begin
-- Create table
create table DEMO_CACHE_PUB_INFO
(
  id                NUMBER(18),
  scm_pub_id        NUMBER(18),
  pub_owner_open_id NUMBER(18),
  pub_type_id       NUMBER(2),
  authors           VARCHAR2(1000),
  zh_title          VARCHAR2(4000),
  en_title          VARCHAR2(4000),
  zh_source         VARCHAR2(500),
  en_source         VARCHAR2(500),
  has_full_text     NUMBER(1),
  list_info         VARCHAR2(250),
  publish_year      NUMBER(6),
  owner             NUMBER(1),
  authenticated     NUMBER(1),
  full_text_img_url VARCHAR2(500),
  product_mark      VARCHAR2(500),
  create_date       DATE,
  update_date       DATE,
  pub_type          NUMBER(2),
  guid              NUMBER(18),
  req_pub_record_id NUMBER(18),
  zh_keywords       VARCHAR2(1000),
  en_keywords       VARCHAR2(1000),
  pub_short_url     VARCHAR2(150),
  pub_detail_param  VARCHAR2(150),
  pub_owner_name    VARCHAR2(250),
  relevance         NUMBER(4),
  doi               VARCHAR2(250),
  labeled           NUMBER(8),
  pdwh_pub_id       NUMBER(18),
  doi_url           VARCHAR2(500)
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
-- Add comments to the columns 
comment on column DEMO_CACHE_PUB_INFO.id
  is '主键';
comment on column DEMO_CACHE_PUB_INFO.scm_pub_id
  is 'scm成果id';
comment on column DEMO_CACHE_PUB_INFO.pub_owner_open_id
  is '成果拥有者id';
comment on column DEMO_CACHE_PUB_INFO.pub_type_id
  is '成果类型';
comment on column DEMO_CACHE_PUB_INFO.authors
  is '作者信息';
comment on column DEMO_CACHE_PUB_INFO.zh_title
  is '中文标题';
comment on column DEMO_CACHE_PUB_INFO.en_title
  is '英文标题';
comment on column DEMO_CACHE_PUB_INFO.zh_source
  is '中文来源';
comment on column DEMO_CACHE_PUB_INFO.en_source
  is '英文来源';
comment on column DEMO_CACHE_PUB_INFO.has_full_text
  is '是否有全文：1=有；0=无';
comment on column DEMO_CACHE_PUB_INFO.list_info
  is ' 收录情况';
comment on column DEMO_CACHE_PUB_INFO.publish_year
  is '发表年份';
comment on column DEMO_CACHE_PUB_INFO.owner
  is '是否本人 0=否；1=是';
comment on column DEMO_CACHE_PUB_INFO.authenticated
  is '是否认证 0=否；1=是';
comment on column DEMO_CACHE_PUB_INFO.full_text_img_url
  is '全文图片url';
comment on column DEMO_CACHE_PUB_INFO.product_mark
  is '标注';
comment on column DEMO_CACHE_PUB_INFO.create_date
  is '创建时间';
comment on column DEMO_CACHE_PUB_INFO.update_date
  is '更新时间';
comment on column DEMO_CACHE_PUB_INFO.pub_type
  is '成果类型：1=个人成果；2=推荐成果；4=群组成果';
comment on column DEMO_CACHE_PUB_INFO.guid
  is '项目guid';
comment on column DEMO_CACHE_PUB_INFO.req_pub_record_id
  is '拉取成果记录id';
comment on column DEMO_CACHE_PUB_INFO.zh_keywords
  is '中文关键字';
comment on column DEMO_CACHE_PUB_INFO.en_keywords
  is '英文关键字';
comment on column DEMO_CACHE_PUB_INFO.pub_short_url
  is '短地址';
comment on column DEMO_CACHE_PUB_INFO.pub_owner_name
  is ' 成果拥有者姓名';
comment on column DEMO_CACHE_PUB_INFO.relevance
  is '相关度';
comment on column DEMO_CACHE_PUB_INFO.doi
  is 'doi';
comment on column DEMO_CACHE_PUB_INFO.doi_url
  is 'doi_url';
-- Create table
create table DEMO_SCM_REQ_PUB_RECORD
(
  id                NUMBER(18),
  guid              NUMBER(18),
  psn_name          VARCHAR2(200),
  email             VARCHAR2(250),
  create_date       DATE,
  last_date         DATE,
  last_get_pub_date DATE,
  type              NUMBER(2),
  psn_id            NUMBER(18)
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
-- Add comments to the columns 
comment on column DEMO_SCM_REQ_PUB_RECORD.id
  is '主键';
comment on column DEMO_SCM_REQ_PUB_RECORD.guid
  is 'guId';
comment on column DEMO_SCM_REQ_PUB_RECORD.psn_name
  is ' 人员名称';
comment on column DEMO_SCM_REQ_PUB_RECORD.email
  is ' 邮件';
comment on column DEMO_SCM_REQ_PUB_RECORD.create_date
  is ' 创建时间';
comment on column DEMO_SCM_REQ_PUB_RECORD.last_date
  is ' 最后更新时间';
comment on column DEMO_SCM_REQ_PUB_RECORD.last_get_pub_date
  is ' 时间戳（增量时间）';
comment on column DEMO_SCM_REQ_PUB_RECORD.type
  is '类型 1=项目负责人 2=项目参与人';
comment on column DEMO_SCM_REQ_PUB_RECORD.psn_id
  is 'demo-PsnId';
  
  --原因（demo成果推荐） 2017-8-29 zzx end

--原因：demo改造，scm_group_tmp表group_pic_url列容量太小，部分长url无法保存 2017-8-28 HCJ begin
alter table scm_group_tmp modify(GROUP_PIC_URL VARCHAR2(200 byte));
--原因：demo改造，scm_group_tmp表group_pic_url列容量太小，部分长url无法保存 2017-8-28 HCJ end
--原因（demo） 2017-8-29 zzx begin
alter table SCM_PUB_IMPORTED add guid number(18);
commit;
--原因（demo） 2017-8-29 zzx end
--原因（demo） 2017-8-29 zzx begin
create sequence SEQ_DEMO_CACHE_PUB_INFO
minvalue 1
maxvalue 199999999999999999
start with 21
increment by 1
cache 20;
create sequence SEQ_DEMO_SCM_REQ_PUB_RECORD
minvalue 1
maxvalue 199999999999999999
start with 21
increment by 1
cache 20;
--原因（demo） 2017-8-29 zzx  end


--WSN 2017-8-30 SCM-13701 基金推荐功能页面开发 begin



--我的基金 begin

create table v_my_fund(
       
       id number(18) not null primary key,
       
       psn_id number(18) not null,
       
       fund_id number(18) not null,
       
       collect_time date default sysdate not null  
);

comment on table v_my_fund
  is '收藏的基金表（我的基金）';
-- Add comments to the columns 
comment on column v_my_fund.id
  is '主键';
  
comment on column v_my_fund.psn_id
  is '人员ID';
  
comment on column v_my_fund.fund_id
  is '基金ID';
  
comment on column v_my_fund.collect_time
  is '收藏的时间';
  
  
create index IDX_MY_FUND_PSNID on V_MY_FUND (PSN_ID);

create index IDX_MY_FUND_ID on V_MY_FUND (FUND_ID);

create sequence SEQ_MY_FUND
minvalue 1
maxvalue 1999999999999
start with 1
increment by 1
cache 20;

--我的基金  end



--基金统计数  begin

create table v_fund_statistics(

       fund_id number(18) not null primary key,
       
       award_count number(5) default 0,
       
       share_count number(5) default 0
       
);


comment on table v_fund_statistics
  is '基金操作统计数';
-- Add comments to the columns 
comment on column v_fund_statistics.fund_id
  is '基金ID';
  
comment on column v_fund_statistics.award_count
  is '赞统计数';
  
comment on column v_fund_statistics.share_count
  is '分享统计数';
  

--基金统计数  end



--赞记录 begin
create table v_fund_award(
       record_id number(18) not null primary key,
       
       fund_id number(18) not null,
       
       award_psn_id number(18) not null,
       
       award_date date default sysdate not null,
       
       operate number(1) default 0 not null      
);


comment on table v_fund_award
is '基金赞操作记录';

comment on column v_fund_award.record_id
is '主键';

comment on column v_fund_award.fund_id
is '基金ID';

comment on column v_fund_award.award_psn_id
is '赞人员ID';

comment on column v_fund_award.award_date
is '赞操作时间';

comment on column v_fund_award.operate
is '赞操作类别：默认 0, 0：赞操作，1：取消赞操作';


CREATE INDEX index_fund_award
     ON v_fund_award (fund_id, award_psn_id);


create sequence SEQ_FUND_AWARD
minvalue 1
maxvalue 1999999999999
start with 1
increment by 1
cache 20;
--赞记录  end



---基金推荐条件 begin

create table v_fund_conditions(
       psn_id number(18) not null primary key,
       seniority varchar2(50char),
       interest_region varchar2(200char),
       science_area_id varchar2(50char)
);

comment on table V_FUND_CONDITIONS
is '人员基金推荐条件';

comment on column v_fund_conditions.psn_id
is '人员ID';

comment on column v_fund_conditions.seniority
is '基金申请资格，1：企业， 2：科研机构';

comment on column v_fund_conditions.interest_region
is '感兴趣的地区，json格式，{"seqNo":"1","regionName":"XXX", "regionId":"12"}格式';

comment on column v_fund_conditions.science_area_id
is '科技领域ID，code1,code2,code3格式';


---基金推荐条件 end


-- 基金分享记录 begin
create table V_FUND_SHARE_BASE
(
  id                NUMBER(18) not null,
  sharer_id         NUMBER(18) not null,
  create_date       DATE default sysdate not null,
  status            NUMBER(1) default 0 not null,
  update_date       DATE default sysdate not null,
  share_content_rel VARCHAR2(500)
);

-- Add comments to the table 
comment on table V_FUND_SHARE_BASE
  is '基金分享记录 主表';
-- Add comments to the columns 
comment on column V_FUND_SHARE_BASE.id
  is '主键';
comment on column V_FUND_SHARE_BASE.sharer_id
  is '分享人id';
comment on column V_FUND_SHARE_BASE.create_date
  is '创建时间';
comment on column V_FUND_SHARE_BASE.status
  is '状态  0 =正常  1=取消分享  2=删除';
comment on column V_FUND_SHARE_BASE.update_date
  is '更新时间';
comment on column V_FUND_SHARE_BASE.share_content_rel
  is '分享基金 发送的文本站内信，给来取消分享用';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_FUND_SHARE_BASE
  add constraint PK_V_FUND_SHARE_BASE primary key (ID);
  
  
-- Create sequence 
create sequence SEQ_V_FUND_SHARE_BASE
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 10;

-- 基金分享记录 end


-- 基金分享记录详情表 begin
create table V_FUND_SHARE_RECORD
(
  id              NUMBER(18) not null,
  sharer_id       NUMBER(18) not null,
  receiver_id     NUMBER(18) not null,
  fund_id         NUMBER(18) not null,
  create_date     DATE default sysdate,
  update_date     DATE default sysdate,
  status          NUMBER(1) default 0,
  share_base_id   NUMBER(18) not null,
  msg_relation_id NUMBER(18) not null
);
-- Add comments to the table 
comment on table V_FUND_SHARE_RECORD
  is '基金分享记录详情表';
-- Add comments to the columns 
comment on column V_FUND_SHARE_RECORD.id
  is '主键';
comment on column V_FUND_SHARE_RECORD.sharer_id
  is '分享人id';
comment on column V_FUND_SHARE_RECORD.receiver_id
  is '接受人id';
comment on column V_FUND_SHARE_RECORD.fund_id
  is '文件id';
comment on column V_FUND_SHARE_RECORD.create_date
  is '创建时间，分享时间';
comment on column V_FUND_SHARE_RECORD.update_date
  is '更新时间，';
comment on column V_FUND_SHARE_RECORD.status
  is '0=正常 ； 1=取消分享 ； 2=删除';
comment on column V_FUND_SHARE_RECORD.share_base_id
  is '分享主表主键id';
comment on column V_FUND_SHARE_RECORD.msg_relation_id
  is '消息表的关联id';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_FUND_SHARE_RECORD
  add constraint PK_V_FUND_SHARE_RECORD primary key (ID);
  
-- Create/Recreate indexes 
create index IDX_FUND_MSG_RELATION on V_FUND_SHARE_RECORD (MSG_RELATION_ID);
  
create index IDX_FUND_SHARE_BASE on V_FUND_SHARE_RECORD (SHARE_BASE_ID);

create sequence SEQ_V_FUND_SHARE_RECORD
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 10;


-- 基金分享记录详情表 end

--WSN 2017-8-30 SCM-13701 基金推荐功能页面开发 end



--原因（SCM-14035 基准库成果全文图片生成） 2017-09-01 LJ begin


insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) values(69,'BatchFulltextPdfToImageTaskTrigger','0 0/3 * * * ?',0,'基准库成果全文图片生成任务');


--原因（SCM-14035 基准库成果全文图片生成） 2017-09-01 LJ end
--原因（SCM-14032） 2017-9-4 ltl begin
--------------------------------------------------------删除重复的赞统计
delete from award_statistics
where ( psn_id,action_key,award_psn_id) in (select  psn_id,action_key,award_psn_id
from award_statistics
group by  psn_id,action_key,award_psn_id
having count(*) > 1)
and rowid not in (select min(rowid)
from award_statistics
group by psn_id,action_key,award_psn_id
having count(*) > 1);
--------------------------------------------------------删除重复的评论
delete from DYNAMIC_REPLY_RES
where ( res_id,res_type) in (select res_id,res_type
from DYNAMIC_REPLY_RES
group by res_id,res_type
having count(*) > 1)
and rowid not in (select min(rowid)
from DYNAMIC_REPLY_RES
group by res_id,res_type
having count(*) > 1);
--------------------------------------------------------删除重复的赞
delete from DYNAMIC_AWARD_PSN
where ( award_Id,awarder_PsnId) in (select award_Id,awarder_PsnId
from DYNAMIC_AWARD_PSN
group by award_Id,awarder_PsnId
having count(*) > 1)
and rowid not in (select min(rowid)
from DYNAMIC_AWARD_PSN
group by award_Id,awarder_PsnId
having count(*) > 1);
commit;
--原因（SCM-14032） 2017-9-4 ltl begin

--学位信息对应错误(SCM-13379)  2017-09-04 WCW begin

update CONST_DICTIONARY set zh_cn_caption = '学士',zh_tw_caption = '学士' where category = 'psn_degree' and seq_no = 3;
commit;

--学位信息对应错误(SCM-13379)  2017-09-04 WCW end

--学位信息修改,只保留“博士、硕士、学士、其他”4个字段(SCM-13379)  2017-09-05 WCW begin
update CONST_DICTIONARY set en_us_caption = 'Other', zh_cn_caption = '其他', zh_tw_caption = '其他' where category = 'psn_degree' and seq_no = 4;
delete from CONST_DICTIONARY where category = 'psn_degree' and seq_no > 4;
commit;
--学位信息修改,只保留“博士、硕士、学士、其他”4个字段(SCM-13379)  2017-09-05 WCW end

--原因（删除doi） 2017-09-5 zzx begin


alter table DEMO_CACHE_PUB_INFO drop column doi;
alter table DEMO_CACHE_PUB_INFO drop column doi_url;
commit;


--原因（删除doi） 2017-09-5 zzx end

--原因（SCM-14006）：增加群组短地址，成员数表字段 2017-9-5 hcj begin
ALTER TABLE scm_group_tmp ADD(group_url VARCHAR2(200));
ALTER TABLE scm_group_tmp ADD(member_sum number(18,0));
COMMENT ON COLUMN scm_group_tmp.group_url IS '群组短地址';
COMMENT ON COLUMN scm_group_tmp.member_sum IS '成员数';
--原因（SCM-14006）：增加群组短地址，成员数表字段 2017-9-5 hcj end

--原因（修改字段） 2017-9-6 zzx begin


alter table DEMO_CACHE_PUB_INFO modify (zh_source varchar2(4000));
alter table DEMO_CACHE_PUB_INFO modify (en_source varchar2(4000));
commit;


--原因（修改字段） 2017-9-6 zzx end


--原因（SCM-14068） 2017-09-01 LJ begin


insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) values(70,'PdwhPubAuthorMatchSnsTaskTrigger','0 0/1 * * * ?',0,'基准库成果作者信息与SNS人员匹配');


--原因（SCM-14068） 2017-09-01 LJ end--原因 SCM-11869 主页--科技领域，保存后，领域的显示顺序与选择时不一致 2017-9-7 ltl begin
alter table PSN_SCIENCE_AREA add areaorder NUMBER(5) default 0;
comment on column PSN_SCIENCE_AREA.areaorder
  is '领域排序';
--原因 SCM-11869 主页--科技领域，保存后，领域的显示顺序与选择时不一致 2017-9-7 ltl end
--原因（修改字段） 2017-09-8 zzx begin


 alter table DEMO_CACHE_PUB_INFO modify (product_mark varchar2(2000));
 commit;

--原因（修改字段） 2017-09-8 zzx end


--原因（SCM-13907 基金推荐邮件任务） 2016-9-8 zll end
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) 
values(71,'PsnFundRecommendTaskTrigger','0 0/1 * * * ?',1,'人员基金推荐');
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) 
values(72,'PsnFundRecommendMailTaskTrigger','0 0/1 * * * ?',1,'人员基金邮件');
--原因（SCM-13907 基金推荐邮件任务） 2017-9-8 zll end


--原因（SCM-14102） 2017-09-01 LJ begin


insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description) values(73,'GeneratePsnDefaultAvatarsTaskTrigger','0 0/1 * * * ?',0,'人员默认头像生成任务');


--原因（SCM-14102） 2017-09-01 LJ end

--原因（修改字段） 2017-09-8 zzx begin

 alter table Demo_PUB rename column PUB_OWNER_PSNID to DEMO_PSNID;
 commit;

--原因（修改字段） 2017-09-8 zzx end

--原因（推荐库中对应表修改字段同步至图示） 2017-09-11 hzr begin
drop materialized view const_fund_category;

CREATE MATERIALIZED VIEW CONST_FUND_CATEGORY
REFRESH FORCE ON COMMIT
AS
SELECT "CONST_FUND_CATEGORY"."ID" "ID","CONST_FUND_CATEGORY"."FUND_AGENCY_ID" "FUND_AGENCY_ID","CONST_FUND_CATEGORY"."NAME_ZH" "NAME_ZH","CONST_FUND_CATEGORY"."NAME_EN" "NAME_EN","CONST_FUND_CATEGORY"."CODE" "CODE","CONST_FUND_CATEGORY"."ABBR" "ABBR","CONST_FUND_CATEGORY"."LANGUAGE" "LANGUAGE","CONST_FUND_CATEGORY"."DESCRIPTION" "DESCRIPTION","CONST_FUND_CATEGORY"."GUIDE_URL" "GUIDE_URL","CONST_FUND_CATEGORY"."DECLARE_URL" "DECLARE_URL","CONST_FUND_CATEGORY"."START_DATE" "START_DATE","CONST_FUND_CATEGORY"."END_DATE" "END_DATE","CONST_FUND_CATEGORY"."TITLE_BEST" "TITLE_BEST","CONST_FUND_CATEGORY"."DEGREE_BEST" "DEGREE_BEST","CONST_FUND_CATEGORY"."BIRTH_LEAST" "BIRTH_LEAST","CONST_FUND_CATEGORY"."BIRTH_MAX" "BIRTH_MAX","CONST_FUND_CATEGORY"."AGE_LEAST" "AGE_LEAST","CONST_FUND_CATEGORY"."AGE_MAX" "AGE_MAX","CONST_FUND_CATEGORY"."CONDITION" "CONDITION","CONST_FUND_CATEGORY"."RELATIONSHIP" "RELATIONSHIP","CONST_FUND_CATEGORY"."CONTACT" "CONTACT","CONST_FUND_CATEGORY"."GRANT_NAME_ZH" "GRANT_NAME_ZH","CONST_FUND_CATEGORY"."GRANT_NAME_EN" "GRANT_NAME_EN","CONST_FUND_CATEGORY"."TITLE_REQUIRE1" "TITLE_REQUIRE1","CONST_FUND_CATEGORY"."DEGREE_REQUIRE1" "DEGREE_REQUIRE1","CONST_FUND_CATEGORY"."TITLE_REQUIRE2" "TITLE_REQUIRE2","CONST_FUND_CATEGORY"."DEGREE_REQUIRE2" "DEGREE_REQUIRE2","CONST_FUND_CATEGORY"."DEADLINE" "DEADLINE","CONST_FUND_CATEGORY"."STRENGTH" "STRENGTH","CONST_FUND_CATEGORY"."INS_ID" "INS_ID","CONST_FUND_CATEGORY"."REMARK" "REMARK","CONST_FUND_CATEGORY"."YEAR" "YEAR","CONST_FUND_CATEGORY"."STATUS" "STATUS","CONST_FUND_CATEGORY"."PSN_ID" "PSN_ID","CONST_FUND_CATEGORY"."CREATE_DATE" "CREATE_DATE","CONST_FUND_CATEGORY"."UPDATE_DATE" "UPDATE_DATE","CONST_FUND_CATEGORY"."PARENT_CATEGORY_ID" "PARENT_CATEGORY_ID","CONST_FUND_CATEGORY"."PERCENTAGE" "PERCENTAGE","CONST_FUND_CATEGORY"."ISMATCH" "ISMATCH","CONST_FUND_CATEGORY"."ISALLYEAR" "ISALLYEAR","CONST_FUND_CATEGORY"."INS_TYPE" "INS_TYPE" FROM "RCMD2TEST"."CONST_FUND_CATEGORY" "CONST_FUND_CATEGORY";

--原因（修改字段） 2017-09-11 hzr end
 
--原因    SCM-13743 人员统计数，需要统计公开的成果和项目数 2017-9-8 ltl begin
delete from scholar2test.PSN_STATISTICS_PUB_PRJ;
insert into scholar2test.PSN_STATISTICS_PUB_PRJ(psn_id,pub_sum,prj_sum,status) select psn_id,0,0,0 from scholar2test.PSN_STATISTICS;
commit;
--原因    SCM-13743 人员统计数，需要统计公开的成果和项目数 2017-9-8 ltl end

--原因（修改demo字段） 2017-09-12 zzx begin
alter table DEMO_PUB modify (ZH_TITLE varchar2(4000));
alter table DEMO_PUB modify (en_TITLE varchar2(4000));
alter table DEMO_PUB modify (zh_SOURCE varchar2(4000));
alter table DEMO_PUB modify (en_SOURCE varchar2(4000));
commit;
--原因（修改demo字段） 2017-09-12 zzx end

--原因（修改demo字段） 2017-09-12 zzx begin
alter table DEMO_PUB modify (authors varchar2(4000));
commit;
--原因（修改demo字段） 2017-09-12 zzx end
--原因（修改demo字段） 2017-09-12 zzx begin
alter table DEMO_PUB modify (LIST_INFO varchar2(1000));
commit;
--原因（修改demo字段） 2017-09-12 zzx end
----原因（SCM-13726 成果认领邮件修改） 2017-09-12 zll begin
insert into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
values(75,'PubConfirmPromoteTaskTrigger','0 0/1 * * * ?',1, '成果认领邮件');

----原因（SCM-13726 成果认领邮件修改） 2017-09-12 zll end
-- Create table
--原因（更新移动端动态历史数据后台任务） 2017-9-12 ltl begin
create table MOBILE_DYN_CONTENT_UPDATE
(
  dyn_id        VARCHAR2(18) not null,
  dyn_type      VARCHAR2(10),
  update_date   date,
  update_status NUMBER(5) default 0,
  update_msg    VARCHAR2(200)
)
tablespace V3SNS
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table MOBILE_DYN_CONTENT_UPDATE
  is '更新移动端动态内容状态表';
-- Add comments to the columns 
comment on column MOBILE_DYN_CONTENT_UPDATE.dyn_id
  is '动态Id';
comment on column MOBILE_DYN_CONTENT_UPDATE.dyn_type
  is '动态类型';
comment on column MOBILE_DYN_CONTENT_UPDATE.update_date
  is '更新时间';
comment on column MOBILE_DYN_CONTENT_UPDATE.update_status
  is '默认为0 ，1表示更新成功，2表示更新失败';
comment on column MOBILE_DYN_CONTENT_UPDATE.update_msg
  is '更新信息';
  
 insert into Mobile_dyn_content_update(dyn_id,Dyn_Type,Update_Date) select dyn_id,dyn_type,sysdate from V_DYNAMIC_MSG;
 insert into V_QUARTZ_CRON_EXPRESSION (ID, CRON_TRIGGER_BEAN, CRON_EXPRESSION, STATUS, DESCRIPTION)
 values (76, 'UpdateMobileDynContentTaskTrigger', '*/5 * * * * ?', 1, '更新移动端动态内容历史数据任务');
 commit;
  --原因（更新移动端动态历史数据后台任务） 2017-9-12 ltl end
