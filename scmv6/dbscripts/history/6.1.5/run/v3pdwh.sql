-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（SCM-21777 crossref数据拆分到基准库各个表） 2017-12-7 zll begin
-- Create table
create table DBCACHE_CFETCH
(
  crossref_id number(18),
  json_data   clob,
  status      number(1),
  error_msg    VARCHAR2(1000 CHAR),
  file_name   VARCHAR2(100 CHAR),
  pub_year    number(4)
)
tablespace V3SNS
  pctfree 0
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table DBCACHE_CFETCH
  is '批量抓取crossref临时存储表';
-- Add comments to the columns 
comment on column DBCACHE_CFETCH.crossref_id
  is '主键';
comment on column DBCACHE_CFETCH.json_data
  is '文件数据';
comment on column DBCACHE_CFETCH.status
  is '0未处理，1已处理，9异常';
comment on column DBCACHE_CFETCH.error_msg
  is '异常消息';
comment on column DBCACHE_CFETCH.file_name
  is '数据文件名';
comment on column DBCACHE_CFETCH.pub_year
  is '年份';
-- Create/Recreate primary, unique and foreign key constraints 
alter table DBCACHE_CFETCH
  add constraint PK_DBCACHE_CFETCH primary key (CROSSREF_ID);
  
  
  
  create sequence SEQ_DBCACHE_CFETCH
minvalue 1
maxvalue 999999999999999999999999999
start with 11
increment by 1
cache 10;


-- Create table
create table pub_category_crossref
(
  id                number(18),
  pub_id             number(18),
  crossref_category varchar2(20),
  scm_category_id   number(18)
)
tablespace v3pDWH
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
comment on table pub_category_crossref
  is '成果在crossref中的分类';
-- Add comments to the columns 
comment on column pub_category_crossref.id
  is '主键';
comment on column pub_category_crossref.pub_id
  is '成果id';
comment on column pub_category_crossref.crossref_category
  is 'crossref中的分类';
comment on column pub_category_crossref.scm_category_id
  is '科研之友分类id';
-- Create/Recreate primary, unique and foreign key constraints 
alter table pub_category_crossref
  add constraint pk_pub_category_crossref primary key (ID);
  
    create sequence SEQ_PUB_CATEGORY_CROSSREF
minvalue 1
maxvalue 999999999999999999999999999
start with 11
increment by 1
cache 10;


-- Create table
create table PUB_REFERENCE
(
  id            NUMBER(18) not null,
  pub_id         NUMBER(18),
  key           VARCHAR2(100),
  doi           VARCHAR2(100),
  issue         VARCHAR2(20),
  first_page    VARCHAR2(20),
  volume        VARCHAR2(20),
  edition       VARCHAR2(200),
  component     VARCHAR2(500),
  author        VARCHAR2(200),
  year          VARCHAR2(10),
  unstructured  VARCHAR2(500),
  journal_title VARCHAR2(500),
  article_title VARCHAR2(500),
  series_title  VARCHAR2(500),
  volume_title  VARCHAR2(500),
  issn          VARCHAR2(50),
  isbn          VARCHAR2(50),
  standard_designator VARCHAR2(500),
  standards_body VARCHAR2(500)
)
tablespace V3PDWH
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
comment on table PUB_REFERENCE
  is '成果参考文献';
-- Add comments to the columns 
comment on column PUB_REFERENCE.id
  is '主键';
comment on column PUB_REFERENCE.pub_id
  is '成果id';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PUB_REFERENCE
  add constraint PK_PUB_REFERENCE primary key (ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  
    create sequence SEQ_PUB_REFERENCE
minvalue 1
maxvalue 999999999999999999999999999
start with 11
increment by 1
cache 10;

alter table PUB_REFERENCE
  add constraint uk_PUB_REFERENCE unique (PUB_ID, KEY);
alter table PUB_CATEGORY
  add constraint uK_PUB_CATEGORY unique (PUB_ID, SCM_CATEGORY_ID);
  
 -- Create table
create table crossref_other_info
(
  pub_id           NUMBER(18)  ,
  crossref_member_id  NUMBER(18),
  deposited_date     VARCHAR2(200),
  fulltext_urls      VARCHAR2(2000)
)
tablespace v3pdWH
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
comment on table crossref_other_info
  is 'crossref其他数据';
-- Add comments to the columns 
comment on column crossref_other_info.pub_id
  is '成果id';
comment on column crossref_other_info.crossref_member_id
  is 'crossref中的人员id，下次其它成果可以向他推荐';
comment on column crossref_other_info.deposited_date
  is 'crossref中成果更新时间';
comment on column crossref_other_info.fulltext_urls
  is '全部全文url';
-- Create/Recreate primary, unique and foreign key constraints 
alter table crossref_other_info
  add constraint uk_crossref_other_info primary key (PUB_ID);

--原因（SCM-21777 crossref数据拆分到基准库各个表） 2017-12-7 zll end
--原因（SCM-22065 crossref 新增引用关系字段） 2018-12-25 zll start
alter table PUB_REFERENCE add doi_hash VARCHAR2(32);
alter table PUB_REFERENCE add journal_title_hash VARCHAR2(32);
alter table PUB_REFERENCE add article_title_hash VARCHAR2(32);
alter table PUB_REFERENCE add series_title_hash VARCHAR2(32);
alter table PUB_REFERENCE add volume_title_hash VARCHAR2(32);
alter table PUB_REFERENCE add pdwh_pub_id number(18);
-- Add/modify columns 
alter table PUB_REFERENCE rename column pub_id to cited_pdwh_pub_id;
-- Add comments to the columns 
comment on column PUB_REFERENCE.cited_pdwh_pub_id
  is '引用该成果的成果id';
comment on column PUB_REFERENCE.pdwh_pub_id
  is '被引用的成果id';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PUB_REFERENCE
  drop constraint UK_PUB_REFERENCE cascade;
alter table PUB_REFERENCE
  add constraint UK_PUB_REFERENCE unique (CITED_PDWH_PUB_ID, KEY)
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
  --原因（SCM-22065 crossref 新增引用关系字段） 2018-12-25 zll end
--原因    SCM-22035 pdwh成果导入，pub_member信息来源调整 2018-12-27 YJ begin
create table V_PUB_PDWH_MEMBER_INSNAME
(
  id        NUMBER not null,
  member_id NUMBER,
  dept      VARCHAR2(1000),
  ins_id    NUMBER,
  ins_name  VARCHAR2(500)
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
comment on table V_PUB_PDWH_MEMBER_INSNAME  is '基准库作者单位信息表';
comment on column V_PUB_PDWH_MEMBER_INSNAME.id  is '主键id';
comment on column V_PUB_PDWH_MEMBER_INSNAME.member_id  is 'V_PUB_PDWH_MEMBER表的主键id';
comment on column V_PUB_PDWH_MEMBER_INSNAME.dept  is '部门所在的全地址，对应xml中pub_author节点的dept数据';
comment on column V_PUB_PDWH_MEMBER_INSNAME.ins_id  is '单位/机构的id';
comment on column V_PUB_PDWH_MEMBER_INSNAME.ins_name  is '人员所在的单位/机构';

alter table V_PUB_PDWH_MEMBER_INSNAME
  add constraint V_PUB_PDWH_MEMBER_INSNAME_PK primary key (ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;

create index V_PUB_PDWH_MEMBER_INSNAME_INX1 on V_PUB_PDWH_MEMBER_INSNAME (MEMBER_ID)
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  
create sequence SEQ_PDWH_MEMBER_INSNAME_ID
  minvalue 1
  maxvalue 9999999999999999999999999999
  start with 1
  increment by 1
  cache 20;

--原因    SCM-22035 pdwh成果导入，pub_member信息来源调整 2018-12-27 YJ end
--原因  SCM-22336 基准库更新保存逻辑-基准库作者拆分，增加对邮件email的拆分逻辑  2019-01-08 YJ begin
create table V_PUB_PDWH_MEMBER_EMAIL
(
  id          NUMBER not null,
  pdwh_pub_id NUMBER(18),
  email       VARCHAR2(250)
)
tablespace V3PDWH
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

comment on table V_PUB_PDWH_MEMBER_EMAIL is '基准库成果作者邮件信息表';
comment on column V_PUB_PDWH_MEMBER_EMAIL.id is '主键id';
comment on column V_PUB_PDWH_MEMBER_EMAIL.pdwh_pub_id is '基准库成果id';
comment on column V_PUB_PDWH_MEMBER_EMAIL.email is '邮件地址';
alter table V_PUB_PDWH_MEMBER_EMAIL
  add constraint V_PUB_PDWH_MEMBER_EMAIL_PK primary key (ID)
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
create index V_PUB_PDWH_MEMBER_EMAIL_IDX on V_PUB_PDWH_MEMBER_EMAIL (PDWH_PUB_ID)
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

create sequence SEQ_PDWH_MEMBER_EMAIL_ID
start with 1
increment by 1
nomaxvalue
nocycle;
--原因  SCM-22336 基准库更新保存逻辑-基准库作者拆分，增加对邮件email的拆分逻辑  2019-01-08 YJ end
-----原因（SCM-22675 crossref数据拆分，issn导入，不应该存括号。另外确认下eissn和pissn是不是拼一起导入，如果拼一起该怎么匹配基准期刊） 2019-01-16 zll begin
-- Add/modify columns 
alter table CROSSREF_OTHER_INFO add eissn VARCHAR2(100);
-- Add comments to the columns 
comment on column CROSSREF_OTHER_INFO.eissn
  is 'eissn';
  
-----原因（SCM-22675 crossref数据拆分，issn导入，不应该存括号。另外确认下eissn和pissn是不是拼一起导入，如果拼一起该怎么匹配基准期刊） 2019-01-16 zll end

---原因 （SCM-22679 生产机》论文推荐、论文发现》选中信息科学这个科技领域，没有论文推荐出来,将nsfc的一部分相似数据导入到scm中) 2019-01-18 SYL start
insert into PUB_CATEGORY
  (id, pub_id, scm_category_id)
  select SEQ_CONST_FUND_AGENCY.Nextval, b.pub_id, 602
    from (select distinct (t.pub_id)
            from pub_category_nsfc_by_journal t
           where t.nsfc_category_id in ('F01')
             and rownum < 15000) b;
---原因 （SCM-22679 生产机》论文推荐、论文发现》选中信息科学这个科技领域，没有论文推荐出来,将nsfc的一部分相似数据导入到scm中) 2019-01-18 SYL end

-----原因（SCM-22675 crossref数据拆分，issn导入，不应该存括号。另外确认下eissn和pissn是不是拼一起导入，如果拼一起该怎么匹配基准期刊） 2019-01-16 zll begin
-- Add/modify columns 
alter table CROSSREF_OTHER_INFO add eissn VARCHAR2(100);
-- Add comments to the columns 
comment on column CROSSREF_OTHER_INFO.eissn
  is 'eissn';
  
-----原因（SCM-22675 crossref数据拆分，issn导入，不应该存括号。另外确认下eissn和pissn是不是拼一起导入，如果拼一起该怎么匹配基准期刊） 2019-01-16 zll end

---原因 （SCM-22679 生产机》论文推荐、论文发现》选中信息科学这个科技领域，没有论文推荐出来,将nsfc的一部分相似数据导入到scm中) 2019-01-18 SYL start
insert into PUB_CATEGORY
  (id, pub_id, scm_category_id)
  select SEQ_CONST_FUND_AGENCY.Nextval, b.pub_id, 602
    from (select distinct (t.pub_id)
            from pub_category_nsfc_by_journal t
           where t.nsfc_category_id in ('F01')
             and rownum < 15000) b;
---原因 （SCM-22679 生产机》论文推荐、论文发现》选中信息科学这个科技领域，没有论文推荐出来,将nsfc的一部分相似数据导入到scm中) 2019-01-18 SYL end

---原因 （SCM-22847 创建表，用于实现后台任务PdwhFullTextDownloadToLocalTask读取数据  SYL start
create table TMP_PUB_KW_FULLTEXT3
(
  pub_id    NUMBER(18),
  file_id   NUMBER(18) not null,
  file_name VARCHAR2(500 CHAR),
  file_path VARCHAR2(500 CHAR)
)
tablespace V3PDWH
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
comment on table TMP_PUB_KW_FULLTEXT3
  is '给昌盛打包全文临时库';
---原因 （SCM-22847 创建表，用于实现后台任务PdwhFullTextDownloadToLocalTask读取数据  SYL end
-- Add/modify columns 
alter table ORIGINAL_PDWH_PUB_RELATION rename column original_id to id;
-- Create/Recreate primary, unique and foreign key constraints 
alter table ORIGINAL_PDWH_PUB_RELATION
  drop constraint PK_ORIGINAL_PDWH_PUB_RELATION cascade;
alter table ORIGINAL_PDWH_PUB_RELATION
  add constraint PK_ORIGINAL_PDWH_PUB_RELATION primary key (ID)
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
alter table ORIGINAL_PDWH_PUB_RELATION add create_date date;
alter table ORIGINAL_PDWH_PUB_RELATION add update_date date;
alter table ORIGINAL_PDWH_PUB_RELATION add error_msg VARCHAR2(2000 char);
-- Add comments to the columns 
comment on column ORIGINAL_PDWH_PUB_RELATION.create_date
  is '创建时间';
comment on column ORIGINAL_PDWH_PUB_RELATION.update_date
  is '更新时间';
comment on column ORIGINAL_PDWH_PUB_RELATION.error_msg
  is '错误信息';
  -- Alter table 
alter table ORIGINAL_PDWH_PUB_RELATION
  storage
  (
    next 8
  )
;
