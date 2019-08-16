-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--     SCM-11558 基准库改造--model与dao建立  2017.3.3 zjh begin
create table PDWH_FULLTEXT_FILE
(
  pub_id      NUMBER(18) not null,
  file_id     NUMBER(18),
  create_date DATE
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the columns 
comment on column PDWH_FULLTEXT_FILE.pub_id
  is '成果id';
comment on column PDWH_FULLTEXT_FILE.file_id
  is '文件id';
comment on column PDWH_FULLTEXT_FILE.create_date
  is '创建时间';
  


create table PDWH_INSNAME
(
  id          NUMBER(18) not null,
  ins_id      NUMBER(18) not null,
  ins_name    VARCHAR2(200 CHAR) not null,
  name_length NUMBER(3) not null,
  frequence   NUMBER(8) not null,
  last_use    DATE
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the columns 
comment on column PDWH_INSNAME.id
  is 'id';
comment on column PDWH_INSNAME.ins_id
  is '机构id';
comment on column PDWH_INSNAME.ins_name
  is '机构名称';
comment on column PDWH_INSNAME.name_length
  is '机构长度';
comment on column PDWH_INSNAME.frequence
  is '别名匹配频率, 用于统计';
comment on column PDWH_INSNAME.last_use
  is '最后使用时间';
-- Create/Recreate indexes 
create unique index PK_PDWH_INSNAME on PDWH_INSNAME (ID)
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  
  
  

create table PDWH_PUB_ADDR
(
  addr_id   NUMBER(18),
  pub_id    NUMBER(18),
  address   VARCHAR2(500 CHAR),
  addr_hash NUMBER,
  language  NUMBER(1)
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the columns 
comment on column PDWH_PUB_ADDR.addr_id
  is '地址ID';
comment on column PDWH_PUB_ADDR.pub_id
  is '成果ID';
comment on column PDWH_PUB_ADDR.address
  is '成果地址';
comment on column PDWH_PUB_ADDR.addr_hash
  is '地址hash';
comment on column PDWH_PUB_ADDR.language
  is '语言：英文1，中文2';
-- Create/Recreate indexes 
create unique index PK_PDWH_PUB_ADDR on PDWH_PUB_ADDR (ADDR_ID)
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  
  
  

create table PDWH_PUB_ADDR_EXC
(
  id        NUMBER(18) not null,
  ins_id    NUMBER(18) not null,
  addr      VARCHAR2(500 CHAR) not null,
  addr_hash NUMBER not null
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the columns 
comment on column PDWH_PUB_ADDR_EXC.id
  is '主键';
comment on column PDWH_PUB_ADDR_EXC.ins_id
  is '单位ID
';
comment on column PDWH_PUB_ADDR_EXC.addr
  is '排除的ISI成果地址
';
comment on column PDWH_PUB_ADDR_EXC.addr_hash
  is '成果地址HASH，用于判断成果地址是否相同';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_ADDR_EXC
  add constraint PK_PDWH_PUB_ADDR_EXC primary key (ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  
  
  

create table PDWH_PUB_ASSIGN
(
  assign_id NUMBER(18) not null,
  pub_id    NUMBER(18) not null,
  ins_id    NUMBER(18) not null,
  status    NUMBER(1) not null,
  result    NUMBER(1) not null,
  is_send   NUMBER(1) not null
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the columns 
comment on column PDWH_PUB_ASSIGN.assign_id
  is '地址ID';
comment on column PDWH_PUB_ASSIGN.pub_id
  is '外键，成果ID';
comment on column PDWH_PUB_ASSIGN.ins_id
  is '外键，单位ID';
comment on column PDWH_PUB_ASSIGN.status
  is '匹配状态(0:等待匹配，1已经进行匹配)';
comment on column PDWH_PUB_ASSIGN.result
  is '匹配结果(0:未匹配上，1匹配上机构，2匹配上其他机构，3部分匹配上，4匹配上其他机构或者地址为空)';
comment on column PDWH_PUB_ASSIGN.is_send
  is '发送状态(0:未发送到单位，1已经发送到单位 )';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_ASSIGN
  add constraint PK_PDWH_PUB_ASSIGN primary key (ASSIGN_ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  
create table PDWH_PUB_DUP
(
  pub_id              NUMBER(18) not null,
  pub_type            NUMBER(2),
  union_hash_key      NUMBER,
  doi                 VARCHAR2(100 CHAR),
  doi_hash            NUMBER,
  cnki_doi            VARCHAR2(100 CHAR),
  cnki_doi_hash       NUMBER,
  isi_soruce_id       VARCHAR2(100 CHAR),
  isi_soruce_id_hash  NUMBER,
  ei_source_id        VARCHAR2(100 CHAR),
  ei_source_id_hash   NUMBER,
  zh_title_hash       NUMBER,
  en_title_hash       NUMBER,
  title_hash          NUMBER,
  patent_no_hash      NUMBER,
  patent_open_no_hash NUMBER,
  pub_year            NUMBER(2)
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the columns 
comment on column PDWH_PUB_DUP.pub_id
  is '成果id';
comment on column PDWH_PUB_DUP.pub_type
  is '成果类型';
comment on column PDWH_PUB_DUP.union_hash_key
  is '(title与pubyear,pubType生成的hash值, 数据表中建立唯一索引)';
comment on column PDWH_PUB_DUP.doi
  is 'doi';
comment on column PDWH_PUB_DUP.doi_hash
  is 'doihash';
comment on column PDWH_PUB_DUP.cnki_doi
  is 'cnki的doi';
comment on column PDWH_PUB_DUP.cnki_doi_hash
  is 'cnki的doihash';
comment on column PDWH_PUB_DUP.isi_soruce_id
  is 'isi的Source_id';
comment on column PDWH_PUB_DUP.isi_soruce_id_hash
  is 'isi的Source_id的hash';
comment on column PDWH_PUB_DUP.ei_source_id
  is 'ei的Source_id';
comment on column PDWH_PUB_DUP.ei_source_id_hash
  is 'ei的Source_id的hash值';
comment on column PDWH_PUB_DUP.zh_title_hash
  is '中文标题hash';
comment on column PDWH_PUB_DUP.en_title_hash
  is '英文标题hash';
comment on column PDWH_PUB_DUP.title_hash
  is '(zh_title | en_title 组合hash)';
comment on column PDWH_PUB_DUP.patent_no_hash
  is '专利号hash';
comment on column PDWH_PUB_DUP.patent_open_no_hash
  is '专利公开号hash';
comment on column PDWH_PUB_DUP.pub_year
  is '成果发表年份';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_DUP
  add constraint PK_PDWH_PUB_DUP primary key (PUB_ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  
  
  

-- Create table
create table PDWH_PUB_EXPAND_LOG
(
  id        NUMBER not null,
  pub_id    NUMBER(18) not null,
  stauts    NUMBER(2) not null,
  error_log VARCHAR2(2000)
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the columns 
comment on column PDWH_PUB_EXPAND_LOG.id
  is '主键';
comment on column PDWH_PUB_EXPAND_LOG.pub_id
  is '成果ID';
comment on column PDWH_PUB_EXPAND_LOG.stauts
  is '拆分状态0-未处理；1-成功；2-失败；-1-作者数超过30个，不拆分';
comment on column PDWH_PUB_EXPAND_LOG.error_log
  is '错误日志信息';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_EXPAND_LOG
  add constraint PK_PDWH_PUB_EXPAND_LOG primary key (ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  
  
  
 create table PDWH_PUB_KYWORDS
(
  pub_id   NUMBER(18) not null,
  keywords VARCHAR2(2000 CHAR),
  language NUMBER(1)
)
;
-- Add comments to the columns 
comment on column PDWH_PUB_KYWORDS.pub_id
  is '成果ID';
comment on column PDWH_PUB_KYWORDS.keywords
  is '关键词';
comment on column PDWH_PUB_KYWORDS.language
  is '英文1，中文2';
-- Create/Recreate indexes 
create index INDEX_PDWH_PUB_KEYWORDS on PDWH_PUB_KYWORDS (pub_id);





-- Create table
create table PDWH_PUB_KEYWORDS_SPLIT
(
  id           NUMBER(18) not null,
  pub_id       NUMBER(18) not null,
  keyword      VARCHAR2(200 CHAR) not null,
  keyword_text VARCHAR2(200 CHAR),
  keyword_hash NUMBER,
  language     NUMBER(1)
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the columns 
comment on column PDWH_PUB_KEYWORDS_SPLIT.pub_id
  is '成果ID';
comment on column PDWH_PUB_KEYWORDS_SPLIT.keyword
  is '关键词';
comment on column PDWH_PUB_KEYWORDS_SPLIT.keyword_text
  is '关键词格式后的';
comment on column PDWH_PUB_KEYWORDS_SPLIT.keyword_hash
  is '转成小写，生成hash';
comment on column PDWH_PUB_KEYWORDS_SPLIT.language
  is '英文1，中文2';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_KEYWORDS_SPLIT
  add constraint PK_PDWH_PUB_KEYWORDS_SPLIT primary key (ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
-- Create/Recreate indexes 
create index INDEX_PDWH_PUB_KEYWORDS_SPLIT on PDWH_PUB_KEYWORDS_SPLIT (PUB_ID)
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  
  




create table PDWH_PUBLICATION
(
  pub_id           NUMBER(18) not null,
  db_id            NUMBER not null,
  zh_title         VARCHAR2(500 CHAR),
  en_title         VARCHAR2(500 CHAR),
  author_name      VARCHAR2(1000 CHAR),
  author_name_spec VARCHAR2(1000 CHAR),
  zh_brief_desc    VARCHAR2(500 CHAR),
  en_brief_desc    VARCHAR2(500 CHAR),
  zh_keywords      VARCHAR2(200 CHAR),
  en_keywords      VARCHAR2(200 CHAR),
  jnl_id           NUMBER(18),
  pub_type         NUMBER(2),
  pub_year         NUMBER(4),
  isbn             VARCHAR2(40 CHAR),
  issn             VARCHAR2(40 CHAR),
  issue            VARCHAR2(20 CHAR),
  volume           VARCHAR2(20 CHAR),
  start_page       VARCHAR2(50 CHAR),
  end_page         VARCHAR2(50 CHAR),
  article_no       VARCHAR2(100 CHAR),
  patent_no        VARCHAR2(100 CHAR),
  patent_open_no   VARCHAR2(100 CHAR),
  conf_name        VARCHAR2(200 CHAR),
  create_psn_id    NUMBER(18),
  update_psn_id    NUMBER(18),
  create_date      DATE default sysdate,
  update_date      DATE
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the columns 
comment on column PDWH_PUBLICATION.pub_id
  is '主键';
comment on column PDWH_PUBLICATION.db_id
  is '网站ID';
comment on column PDWH_PUBLICATION.zh_title
  is '中文标题';
comment on column PDWH_PUBLICATION.en_title
  is '英文标题';
comment on column PDWH_PUBLICATION.author_name
  is '英文作者名';
comment on column PDWH_PUBLICATION.author_name_spec
  is '中文作者名';
comment on column PDWH_PUBLICATION.zh_brief_desc
  is '中文简介';
comment on column PDWH_PUBLICATION.en_brief_desc
  is '英文简介';
comment on column PDWH_PUBLICATION.zh_keywords
  is '中文关键词';
comment on column PDWH_PUBLICATION.en_keywords
  is '英文关键词';
comment on column PDWH_PUBLICATION.jnl_id
  is '匹配基准期刊后的base_journal表id';
comment on column PDWH_PUBLICATION.pub_type
  is '成果类型';
comment on column PDWH_PUBLICATION.pub_year
  is '成果年份';
comment on column PDWH_PUBLICATION.isbn
  is 'ISBN';
comment on column PDWH_PUBLICATION.issn
  is 'ISSN';
comment on column PDWH_PUBLICATION.issue
  is '期号';
comment on column PDWH_PUBLICATION.volume
  is '卷号';
comment on column PDWH_PUBLICATION.start_page
  is '开始页';
comment on column PDWH_PUBLICATION.end_page
  is '结束页';
comment on column PDWH_PUBLICATION.article_no
  is 'article number记录一篇文章在一本期刊集里位置';
comment on column PDWH_PUBLICATION.patent_no
  is '专利号';
comment on column PDWH_PUBLICATION.patent_open_no
  is '专利公开号';
comment on column PDWH_PUBLICATION.conf_name
  is '会议名称';
comment on column PDWH_PUBLICATION.create_psn_id
  is '创建人id';
comment on column PDWH_PUBLICATION.update_psn_id
  is '更新人id';
comment on column PDWH_PUBLICATION.create_date
  is '创建时间';
comment on column PDWH_PUBLICATION.update_date
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUBLICATION
  add constraint PK_PDWH_PUBLICATION primary key (PUB_ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  
  
  
  
  -- Create table
create table PDWH_PUB_SOURCE_DB
(
  pub_id  number(18) not null,
  ei      number(1),
  sci     number(1),
  istp    number(1),
  ssci    number(1),
  cnki    number(1),
  cnkipat number(1)
)
;
-- Add comments to the columns 
comment on column PDWH_PUB_SOURCE_DB.pub_id
  is '成果id';
comment on column PDWH_PUB_SOURCE_DB.ei
  is '成果来自ei标识为1';
comment on column PDWH_PUB_SOURCE_DB.sci
  is '成果来自sci标识为1';
comment on column PDWH_PUB_SOURCE_DB.istp
  is '成果来自istp标识为1';
comment on column PDWH_PUB_SOURCE_DB.ssci
  is '成果来自ssci标识为1';
comment on column PDWH_PUB_SOURCE_DB.cnki
  is '成果来自cnki标识为1';
comment on column PDWH_PUB_SOURCE_DB.cnkipat
  is '成果来自cnkipat标识为1';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_SOURCE_DB
  add constraint PK_PDWH_PUB_SOURCE_DB primary key (PUB_ID);



--  SCM-11558 基准库改造--model与dao建立  2017.3.3 zjh begin