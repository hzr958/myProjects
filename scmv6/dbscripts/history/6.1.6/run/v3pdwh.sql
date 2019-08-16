-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因：SCM-23079 2019-03-05 YJ begin
alter table V_PUB_PDWH add(STATUS NUMBER(2) default 0);
comment on column V_PUB_PDWH.status is '成果状态：0.正常，1.已删除';
update V_PUB_PDWH t set t.status = 0;
--原因：SCM-23079 2019-03-05 YJ end--SCM-23503 SEO- 成果新增页面需要同时更新 2019-3-11 HHT begin
--备份 pdwh_index_publication
 ALTER TABLE pdwh_index_publication rename to  pdwh_index_publication_bak; 
  commit;
--SCM-23503 SEO- 成果新增页面需要同时更新 2019-3-11 HHT end;--SCM-23503 SEO- 成果新增页面需要同时更新 2019-3-11 HHT begin
--创建新的 pdwh_index_publication
create table pdwh_index_publication as select * from pdwh_index_publication_bak;
  commit;
alter table pdwh_index_publication  drop column dbid;
alter table pdwh_index_publication  drop column en_title;
alter table pdwh_index_publication  drop column en_title_short;
alter table pdwh_index_publication rename column zh_title to pub_title;
alter table pdwh_index_publication rename column zh_title_short to short_title;
commit;
--SCM-23503 SEO- 成果新增页面需要同时更新 2019-3-11 HHT end;
--scm-0000 删除pdwh_index_publication原有的数据 和列dbid HHT start
truncate table pdwh_index_publication;
commit;
--scm-0000 删除pdwh_index_publication原有的数据 和列dbid HHT start
--scm-0000 删除 pdwh_index_publication中db_id  2019-3-13 HHT begin
alter table pdwh_index_publication  drop column db_id;
commit;
--scm-0000  删除 pdwh_index_publication中db_id 2019-3-13 HHT end

--原因    SCM-24373 基准库成果mongodb数据拆分任务 2019-03-29 YJ begin
-- Create table
create table V_PUB_PDWH_DOI
(
  pdwh_pub_id NUMBER(18) not null,
  doi         VARCHAR2(200 CHAR)
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
comment on table V_PUB_PDWH_DOI is '基准库成果doi信息';
comment on column V_PUB_PDWH_DOI.pdwh_pub_id is '基准库成果id';
comment on column V_PUB_PDWH_DOI.doi is '成果doi';
alter table V_PUB_PDWH_DOI add constraint V_PUB_PDWH_DOI_PK primary key (PDWH_PUB_ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  
create table V_PUB_PDWH_INS
(
  pdwh_pub_id NUMBER(18) not null,
  ins_id      NUMBER(18)
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
comment on table V_PUB_PDWH_INS is '基准库成果单位关系';
comment on column V_PUB_PDWH_INS.pdwh_pub_id is '基准库成果id';
comment on column V_PUB_PDWH_INS.ins_id is '单位id';
alter table V_PUB_PDWH_INS add constraint V_PUB_PDWH_INS_PK primary key (PDWH_PUB_ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;

create table V_PUB_PDWH_JOURNAL
(
  pdwh_pub_id NUMBER(18) not null,
  jid         NUMBER(18),
  name        VARCHAR2(500 CHAR),
  issn        VARCHAR2(100 CHAR)
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
comment on table V_PUB_PDWH_JOURNAL is '基准库成果期刊信息';
comment on column V_PUB_PDWH_JOURNAL.pdwh_pub_id is '基准库成果id';
comment on column V_PUB_PDWH_JOURNAL.jid is '期刊id，base_journal主键';
comment on column V_PUB_PDWH_JOURNAL.name is '期刊名';
comment on column V_PUB_PDWH_JOURNAL.issn is 'issn';
alter table V_PUB_PDWH_JOURNAL add constraint V_PUB_PDWH_JOURNAL_PK primary key (PDWH_PUB_ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;

create table V_PUB_PDWH_PATENT
(
  pdwh_pub_id         NUMBER(18) not null,
  application_no      VARCHAR2(200 CHAR),
  publication_open_no VARCHAR2(200 CHAR)
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
comment on table V_PUB_PDWH_PATENT is '基准库成果专利信息';
comment on column V_PUB_PDWH_PATENT.pdwh_pub_id is '基准库成果id';
comment on column V_PUB_PDWH_PATENT.application_no is '专利申请号';
comment on column V_PUB_PDWH_PATENT.publication_open_no is '专利公开号';
alter table V_PUB_PDWH_PATENT add constraint V_PUB_PDWH_PATENT_PK primary key (PDWH_PUB_ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;

--原因    SCM-24373 基准库成果mongodb数据拆分任务 2019-03-29 YJ end

-- 原因 SCM-21253 成果查重doi去除标点符号  2019-04-01 YJ start
alter table v_Pdwh_Duplicate add(hash_clean_doi varchar(32));
comment on column v_Pdwh_Duplicate.hash_clean_doi is '去除标点符号doi的hash值';
alter table v_Pdwh_Duplicate add(hash_clean_cnki_doi varchar(32));
comment on column v_Pdwh_Duplicate.hash_clean_cnki_doi is '去除标点符号cnkidoi的hash值';
-- 原因 SCM-21253 成果查重doi去除标点符号  2019-04-01 YJ end
