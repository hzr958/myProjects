-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end



--原因（SCM-13338） 2017-07-18 LJ begin
create table RAINPAT_PUB_DUP
(
  pub_id           NUMBER(18) not null,
  patent_hash      NUMBER,
  title_hash       NUMBER not null,
  patent_open_hash NUMBER
)
tablespace V3PDWH
  pctfree 0
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  )
compress;

comment on table RAINPAT_PUB_DUP
  is 'RAINPAT专利成果查重表';

comment on column RAINPAT_PUB_DUP.pub_id
  is '主键';
comment on column RAINPAT_PUB_DUP.patent_hash
  is 'PATENT_NO的hash标识';
comment on column RAINPAT_PUB_DUP.title_hash
  is 'ZH_TITLE|EN_TITLE的hash标识';
comment on column RAINPAT_PUB_DUP.patent_open_hash
  is 'PATENT_OPEN_NO的hash标识';

create index IDX_RAINPAT_PUB_DUP1 on RAINPAT_PUB_DUP (TITLE_HASH, PATENT_HASH)
  tablespace V3PDWH
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
create index IDX_RAINPAT_PUB_DUP2 on RAINPAT_PUB_DUP (TITLE_HASH, PATENT_OPEN_HASH)
  tablespace V3PDWH
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

alter table RAINPAT_PUB_DUP
  add constraint PK_RAINPAT_PUB_DUP primary key (PUB_ID)
  using index 
  tablespace V3PDWH
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
  
  

create table RAINPAT_PUB_EXTEND
(
  pub_id   NUMBER(18) not null,
  xml_data CLOB default 0 not null
)
tablespace V3PDWH
  pctfree 0
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  )
compress;

comment on table RAINPAT_PUB_EXTEND
  is 'RAINPAT专利成果xml数据表';

comment on column RAINPAT_PUB_EXTEND.pub_id
  is '主键';
comment on column RAINPAT_PUB_EXTEND.xml_data
  is 'Xml数据';

alter table RAINPAT_PUB_EXTEND
  add constraint PK_RAINPAT_PUB_EXTEND primary key (PUB_ID)
  using index 
  tablespace V3PDWH
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
 
  
  alter table PDWH_PUB_SOURCE_DB add RAINPAT NUMBER(1);
comment on column PDWH_PUB_SOURCE_DB.rainpat
  is '成果来自rainpat标识为1';

commit;

--原因（SCM-13338） 2017-07-18 LJ end
--原因  创建新表  2017-7-20 zx begin
create table PDWH_PUB_COMMENTS
(
  comment_id         NUMBER(18) not null,
  pub_id              NUMBER(18) not null,
  db_id               NUMBER(18) not null,
  comment_psn_id               NUMBER(18) not null,
  comments_content    VARCHAR2(4000 CHAR) not null,
  create_date         DATE default sysdate not null
);
select * from  PDWH_PUB_COMMENTS
add constraint PK_PDWH_COMMENT_ID primary key (COMMENT_ID);

comment on table PDWH_PUB_COMMENTS
  is '成果评论表';
-- Add comments to the columns 
comment on column PDWH_PUB_COMMENTS.comment_id
  is 'ID';
comment on column PDWH_PUB_COMMENTS.pub_id
  is '成果ID';
comment on column PDWH_PUB_COMMENTS.comment_psn_id
  is '评论人ID';
comment on column PDWH_PUB_COMMENTS.comments_content
  is '评论内容';
comment on column PDWH_PUB_COMMENTS.create_date
  is '评论日期';
comment on column PDWH_PUB_COMMENTS.db_id
  is '网站Id';
--原因  创建新表  2017-7-20 zx end
 --原因  创建新表  2017-7-20 zx begin 
create table PDWH_PUB_STATISTICS
(
  pub_id NUMBER(18) primary key,
  db_id NUMBER not null,
  award_count   NUMBER(5),
  share_count   NUMBER(5),
  comment_count NUMBER(5),
  read_count    NUMBER(5)
);

-- Add comments to the table 
comment on table PDWH_PUB_STATISTICS
  is '成果操作统计表';
-- Add comments to the columns 
comment on column PDWH_PUB_STATISTICS.pub_id
  is '成果ID';
comment on column PDWH_PUB_STATISTICS.db_id
  is '基准库dbId';
comment on column PDWH_PUB_STATISTICS.award_count
  is '赞统计数';
comment on column PDWH_PUB_STATISTICS.share_count
  is '分享统计数';
comment on column PDWH_PUB_STATISTICS.comment_count
  is '评论统计数';
comment on column PDWH_PUB_STATISTICS.read_count
  is '阅读统计数';
  
  alter table PDWH_PUB_STATISTICS
  add constraint PDWH_PUB_STATISTICS_PK_ID primary key (PUB_ID);
 
  --原因  创建新表  2017-7-20 zx end
  
--原因  创建新表  2017-7-20 lhd begin
-- Create table
create table PDWH_PUB_AWARD
(
  record_id    NUMBER(18) not null,
  pub_id       NUMBER(18) not null,
  db_id        NUMBER not null,
  award_psn_id NUMBER(18) not null,
  award_date   DATE not null,
  status       NUMBER(1) default 0 not null
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table PDWH_PUB_AWARD
  is '成果赞记录表';
-- Add comments to the columns 
comment on column PDWH_PUB_AWARD.record_id
  is '主键';
comment on column PDWH_PUB_AWARD.pub_id
  is '基准库成果ID';
comment on column PDWH_PUB_AWARD.db_id
  is '基准库dbId';
comment on column PDWH_PUB_AWARD.award_psn_id
  is '赞/取消赞人员Id';
comment on column PDWH_PUB_AWARD.award_date
  is '更新时间';
comment on column PDWH_PUB_AWARD.status
  is '赞还是取消赞(默认0) 0:赞 1:取消赞';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_AWARD
  add primary key (RECORD_ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
-- Create/Recreate indexes 
create unique index IDX_U_PAID on PDWH_PUB_AWARD (PUB_ID, AWARD_PSN_ID)
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;  
--原因  创建新表  2017-7-20 lhd end
  
--原因  创建新表  2017-7-20 lhd begin
 -- Create table
create table PDWH_PUB_SHARE
(
  record_id      NUMBER(18) not null,
  share_title_zh VARCHAR2(500 CHAR),
  share_title_en VARCHAR2(500 CHAR),
  share_psn_id   NUMBER(18) not null,
  share_date     DATE not null
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table PDWH_PUB_SHARE
  is '成果分享记录表';
-- Add comments to the columns 
comment on column PDWH_PUB_SHARE.record_id
  is '主键';
comment on column PDWH_PUB_SHARE.share_title_zh
  is '中文分享标题';
comment on column PDWH_PUB_SHARE.share_title_en
  is '英文分享标题';
comment on column PDWH_PUB_SHARE.share_psn_id
  is '分享人员ID';
comment on column PDWH_PUB_SHARE.share_date
  is '分享时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_SHARE
  add primary key (RECORD_ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
-- Create/Recreate indexes 
create index IDX_N_SID on PDWH_PUB_SHARE (SHARE_PSN_ID)
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255; 
--原因  创建新表  2017-7-20 lhd end
  
  
  
  
  
  
  
  
  
  --原因（SCM-13338） 2017-07-18 LJ begin

insert into const_ref_db
  (dbid,
   db_code,
   zh_cn_name,
   en_us_name,
   action_url_inside,
   login_url_inside,
   only_support_lang,
   is_public,
   en_sort_key,
   zh_sort_key,
   db_type,
   db_bit_code,
   fulltext_url_inside,
   action_url,
   login_url,
   zh_abbr_name,
   en_abbr_name,
   batch_query)
values
  (31,
   'RAINPAT',
  ' 润桐专利',
  'RAINPAT',
   'http://www.rainpat.com',
   'http://www.rainpat.com',
   '',
   1,
   0,
   0,
   1,
   0,
   '',
   '',
   '',
   '润桐专利',
   'RAINPAT',
   '');
 
commit;

--原因（SCM-13338） 2017-07-18 LJ  end

  
  
  
  
  
  --原因  创建新表  2017-7-20 zx begin
create table PDWH_PUB_COMMENTS
(
  comment_id         NUMBER(18) not null,
  pub_id              NUMBER(18) not null,
  db_id               NUMBER(18) not null,
  comment_psn_id               NUMBER(18) not null,
  comments_content    VARCHAR2(4000 CHAR) not null,
  create_date         DATE default sysdate not null
);
alter table  PDWH_PUB_COMMENTS
add constraint PK_PDWH_COMMENT_ID primary key (COMMENT_ID);

comment on table PDWH_PUB_COMMENTS
  is '成果评论表';
-- Add comments to the columns 
comment on column PDWH_PUB_COMMENTS.comment_id
  is 'ID';
comment on column PDWH_PUB_COMMENTS.pub_id
  is '成果ID';
comment on column PDWH_PUB_COMMENTS.comment_psn_id
  is '评论人ID';
comment on column PDWH_PUB_COMMENTS.comments_content
  is '评论内容';
comment on column PDWH_PUB_COMMENTS.create_date
  is '评论日期';
comment on column PDWH_PUB_COMMENTS.db_id
  is '网站Id';
  
create sequence SEQ_PDWH_PUB_COMMENTS
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;
--原因  创建新表  2017-7-20 zx end
     create table PDWH_PUB_STATISTICS
(
  pub_id        NUMBER(18) not null,
  award_count   NUMBER(5),
  share_count   NUMBER(5),
  comment_count NUMBER(5),
  read_count    NUMBER(5)
);

-- Add comments to the table 
comment on table PDWH_PUB_STATISTICS
  is '成果操作统计数（包括赞，分享，评论，阅读，引用）';
-- Add comments to the columns 
comment on column PDWH_PUB_STATISTICS.pub_id
  is '成果ID';
comment on column PDWH_PUB_STATISTICS.award_count
  is '赞统计数';
comment on column PDWH_PUB_STATISTICS.share_count
  is '分享统计数';
comment on column PDWH_PUB_STATISTICS.comment_count
  is '评论统计数';
comment on column PDWH_PUB_STATISTICS.read_count
  is '阅读统计数';
  
  alter table PDWH_PUB_STATISTICS
  add constraint PDWH_PUB_STATISTICS_PK_ID primary key (PUB_ID);
 --原因  创建新表  2017-7-20 zx begin 
 
  --原因  创建新表  2017-7-20 zx end

--原因（修改db_id字段允许为空） 2017-07-21 lhd begin
alter table pdwh_pub_comments modify db_id null;
alter table pdwh_pub_award modify db_id null;
alter table pdwh_pub_statistics modify db_id null;
--原因（修改db_id字段允许为空） 2017-07-21 lhd end
--原因（给pwdh_pub_share加字段） 2017-07-24 lhd begin
alter table pdwh_pub_share add(res_id number(18) not null);
alter table pdwh_pub_share add(res_type number(2));
comment on column pdwh_pub_share.res_id is '资源ID';
comment on column pdwh_pub_share.res_type is '资源类型 1:成果'; 
drop index IDX_N_SID;
--原因（给pwdh_pub_share加字段） 2017-07-24 lhd end
--原因（建序列） 2017-07-25 lhd begin
create sequence SEQ_PDWH_PUB_AWARD
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

create sequence SEQ_PDWH_PUB_SHARE
minvalue 1
maxvalue 9999999999999999999999999999
start with 21
increment by 1
cache 20;
--原因（建序列） 2017-07-25 WSN end


--SCM-13404 成果详情页面改造 2017-7-23 WSN begin
create table pdwh_READ_STATISTICS
(
  id           NUMBER(18) not null,
  psn_id       NUMBER(18),
  read_psn_id  NUMBER(18),
  action_key   NUMBER(18),
  action_type  NUMBER(2),
  create_date  DATE,
  formate_date NUMBER(15),
  total_count  NUMBER(4) default 0,
  ip           VARCHAR2(20),
  normal_count NUMBER(4) default 0,
  detail_count NUMBER(4) default 0
);

-- Add comments to the table 
comment on table pdwh_READ_STATISTICS
  is '阅读统计表';
-- Add comments to the columns 
comment on column pdwh_READ_STATISTICS.id
  is '主键';
comment on column pdwh_READ_STATISTICS.psn_id
  is '阅读人PsnId';
comment on column pdwh_READ_STATISTICS.read_psn_id
  is '被阅读人PsnId';
comment on column pdwh_READ_STATISTICS.action_key
  is '被阅读的东西的主键';
comment on column pdwh_READ_STATISTICS.action_type
  is '被阅读东西的类型 详情请看DynamicConstant.java';
comment on column pdwh_READ_STATISTICS.create_date
  is '创建日期';
comment on column pdwh_READ_STATISTICS.formate_date
  is '日期格式化';
comment on column pdwh_READ_STATISTICS.total_count
  is '所有的浏览次数';
comment on column pdwh_READ_STATISTICS.ip
  is 'IP地址';
comment on column pdwh_READ_STATISTICS.normal_count
  is '普通的浏览，例如查看个人主页的时候，看了别人的成果';
comment on column pdwh_READ_STATISTICS.detail_count
  is '详情浏览，点进成果详情来浏览';
-- Create/Recreate indexes 
create index IDX_READ_PSN_ID on pdwh_READ_STATISTICS (READ_PSN_ID);
create index INDEX_READ_STAT_PSN_ID on pdwh_READ_STATISTICS (PSN_ID);
-- Create/Recreate primary, unique and foreign key constraints 
alter table pdwh_READ_STATISTICS
  add constraint pdwh_READ_STATISTICS_PK primary key (ID);  
  
-- Create sequence 
create sequence SEQ_PDWH_READ_STATISTICS
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20;

commit;

--SCM-13404 成果详情页面改造 2017-7-23 WSN end