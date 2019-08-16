-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN endcreate sequence SEQ_APDWH_PUB_MADDR
minvalue 1
maxvalue 999999999999999999999999999
start with 11
increment by 1
cache 10;



CREATE SEQUENCE SEQ_PDWH_INSNAME 
INCREMENT BY 1 
START WITH 1
NOMAXVALUE 
NOCYCLE 
CACHE 10;


CREATE SEQUENCE SEQ_APDWH_PUB_ADDR 
INCREMENT BY 1 
START WITH 1
NOMAXVALUE 
NOCYCLE 
CACHE 10;


CREATE SEQUENCE SEQ_PDWH_PUB_ADDR_EXC 
INCREMENT BY 1 
START WITH 1
NOMAXVALUE 
NOCYCLE 
CACHE 10;



CREATE SEQUENCE SEQ_APDWH_PUB_ASSIGN 
INCREMENT BY 1 
START WITH 1 
NOMAXVALUE 
NOCYCLE 
CACHE 10;


CREATE SEQUENCE SEQ_PDWH_PUB_EXPAND_LOG 
INCREMENT BY 1 
START WITH 1 
NOMAXVALUE 
NOCYCLE 
CACHE 10;


CREATE SEQUENCE SEQ_PUB_KEYWORDS_SPLIT 
INCREMENT BY 1 
START WITH 1 
NOMAXVALUE 
NOCYCLE 
CACHE 10;


CREATE SEQUENCE SEQ_PDWH_PUBLICATION 
INCREMENT BY 1 
START WITH 100000000
NOMAXVALUE 
NOCYCLE 
CACHE 10;

  
  
CREATE SEQUENCE SEQ_PDWH_FULLTEXT_FILE 
INCREMENT BY 1 
START WITH 1 
NOMAXVALUE 
NOCYCLE 
CACHE 10;

 
create sequence SEQ_PDWH_PUB_KEYWORDS
minvalue 3
maxvalue 9999999999999999999999999999
start with 21
increment by 1
cache 10;

-- Create sequence 
create sequence SEQ_PDWH_PUB_XML_TOHANDLE
minvalue 1
maxvalue 9999999999999999999999999999
start with 21
increment by 1
cache 20;
  

-- Create sequence 
create sequence SEQ_PDWH_PUB_XML
minvalue 1
maxvalue 9999999999999999999999999999
start with 21
increment by 1
cache 20;

  -----  SCM-11548 基准库改造  2017-3-3 钟靖鸿 end------------



--     SCM-11558 基准库改造--model与dao建立  2017.3.3 zjh begin

--  Create table
create table PDWH_PUB_XML_TOHANDLE
(
  TMP_ID  NUMBER(18) not null,
  TMP_XML   VARCHAR2(100 CHAR),
  INS_ID NUMBER(18),
  STATUS NUMBER(5)
  
)
tablespace V3PDWH
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
 comment on column PDWH_PUB_XML_TOHANDLE.TMP_ID
  is 'id';
comment on column PDWH_PUB_XML_TOHANDLE.TMP_XML
  is 'xml数据';
comment on column PDWH_PUB_XML_TOHANDLE.INS_ID
  is '机构id';
comment on column PDWH_PUB_XML_TOHANDLE.STATUS
  is ' 状态';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_XML_TOHANDLE
  add constraint PK_PDWH_PUB_XML_TOHANDLE primary key (TMP_ID)
  disable;
-- Create/Recreate indexes 
create unique index INDEX_PDWH_PUB_XML_TOHANDLE on PDWH_PUB_XML_TOHANDLE (TMP_ID)
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

  
  
  -- Create table
create table PDWH_PUB_XML
(
  pub_id NUMBER(18) not null,
  xml    CLOB
)
tablespace V3PDWH
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
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_XML
  add constraint PK_PDWH_PUB_XML primary key (PUB_ID)
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
  
  
  
create table PDWH_PUB_MADDR
(
  id              NUMBER(18) not null,
  addr_id         NUMBER(18),
  ins_id          NUMBER(18),
  pub_id          NUMBER(18),
  addr            VARCHAR2(600 CHAR),
  pdwh_insname_id NUMBER(18),
  matched         NUMBER(1) not null,
  matched_ins_id  NUMBER(18)
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 8
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table PDWH_PUB_MADDR
  is '成果地址匹配';
-- Add comments to the columns 
comment on column PDWH_PUB_MADDR.addr_id
  is '地址id';
comment on column PDWH_PUB_MADDR.ins_id
  is '成果的机构id';
comment on column PDWH_PUB_MADDR.pub_id
  is '成果ID';
comment on column PDWH_PUB_MADDR.addr
  is 'ei/issi 匹配或部分匹配上的成果地址，带加粗字符,cnki匹配上的成果地址，不带加粗字符';
comment on column PDWH_PUB_MADDR.pdwh_insname_id
  is '// 匹配上的机构别名ID';
comment on column PDWH_PUB_MADDR.matched
  is 'ei/issi 1:匹配上机构别名，2部分匹配上机构别名，3不确认是否是机构别名，4不是机构别名，0不需要匹配（有一个地址已经匹配上了机构，其他的不需要匹配）cnki 只有0和4';
comment on column PDWH_PUB_MADDR.matched_ins_id
  is ' 匹配上的机构Id';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_MADDR
  add constraint PDWH_PUB_MADDR_KEY primary key (ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  
  
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
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_FULLTEXT_FILE
  add constraint PK_FILE_ID primary key (FILE_ID)
  disable;
-- Create/Recreate indexes 
create unique index INDEX_PDWH_FULLTEXT_FILE on PDWH_FULLTEXT_FILE (file_id)
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  


create table PDWH_INS_NAME
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
comment on column PDWH_INS_NAME.id
  is 'id';
comment on column PDWH_INS_NAME.ins_id
  is '机构id';
comment on column PDWH_INS_NAME.ins_name
  is '机构名称';
comment on column PDWH_INS_NAME.name_length
  is '机构长度';
comment on column PDWH_INS_NAME.frequence
  is '别名匹配频率, 用于统计';
comment on column PDWH_INS_NAME.last_use
  is '最后使用时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_INS_NAME
  add constraint PK_PDWH_INS_NAME primary key (ID);
-- Create/Recreate indexes 
create unique index PK_PDWH_INS_NAME on PDWH_INS_NAME (ID)
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
  -- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_ADDR
  add constraint PK_PDWH_PUB_ADDR primary key (ADDR_ID);
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
  pub_year            NUMBER(4)
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
  
  
  
 create table PDWH_PUB_KEYWORDS
(
  pub_id   NUMBER(18) not null,
  keywords VARCHAR2(2000 CHAR),
  language NUMBER(1)
)
;
-- Add comments to the columns 
comment on column PDWH_PUB_KEYWORDS.pub_id
  is '成果ID';
comment on column PDWH_PUB_KEYWORDS.keywords
  is '关键词';
comment on column PDWH_PUB_KEYWORDS.language
  is '英文1，中文2';
  -- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PDWH_PUB_KEYWORDS
  add constraint PK_PDWH_PUB_KEYWORDS primary key (PUB_ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;






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
  ei      number(1)  default 0,
  sci     number(1)  default 0,
  istp    number(1)  default 0,
  ssci    number(1)  default 0,
  cnki    number(1)  default 0,
  cnkipat number(1)  default 0
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
    using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  
  -- Add/modify columns 
alter table PDWH_PUBLICATION modify author_name VARCHAR2(500 CHAR);
alter table PDWH_PUBLICATION modify author_name_spec VARCHAR2(500 CHAR);
--  SCM-11558 基准库改造--model与dao建立  2017.3.3 zjh begincommit;

--原因（SCM-11549） 2017-3-10 lj end

--2017-03-28 hzr begin 处理基准库历史数据
--------------------------------------
--  Changed table pdwh_publication  --
--------------------------------------
-- Add/modify columns 
alter table PDWH_PUBLICATION modify author_name VARCHAR2(500 CHAR);
alter table PDWH_PUBLICATION modify author_name_spec VARCHAR2(500 CHAR);
alter table PDWH_PUBLICATION add union_hash_key NUMBER;
-- Add comments to the columns 
comment on column PDWH_PUBLICATION.union_hash_key
  is '(title与pubyear,pubType生成的hash值, 数据表中建立唯一索引)';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUBLICATION
  add constraint UK_PDWH_PUBLICATION unique (UNION_HASH_KEY)
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
-------------------------------------------
--  Changed table pdwh_pub_xml_tohandle  --
-------------------------------------------
-- Add/modify columns 
alter table PDWH_PUB_XML_TOHANDLE modify psn_id not null;
-- Add comments to the columns 
comment on column PDWH_PUB_XML_TOHANDLE.status
  is '';
comment on column PDWH_PUB_XML_TOHANDLE.psn_id
  is '2：后台导入；其他：从sns，rol同步回基准库';
-- Drop indexes 
drop index INDEX_PK_PDWH_PUB_XML_TOHANDLE;
-- Create/Recreate indexes 
create unique index INDEX_PDWH_PUB_XML_TOHANDLE on PDWH_PUB_XML_TOHANDLE (TMP_ID)
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
  
  
--2017-03-28 hzr end



--原因（有CQ号带上CQ号） 2017-05-23 zjh begin
-- Create table
create table PDWH_PUB_CITED_TIMES
(
  pdwh_pub_id NUMBER(18) not null,
  cited_times NUMBER(8),
  update_date DATE default sysdate,
  type        NUMBER(2)
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
-- Create/Recreate indexes 
create unique index PK_PDWH_PUB_CITED_TIMES on PDWH_PUB_CITED_TIMES (PDWH_PUB_ID)
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  
  -- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_CITED_TIMES
  add constraint PK_PDWH_PUB_CITED_TIMES primary key (PDWH_PUB_ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  
  -- Add comments to the columns 
comment on column PDWH_PUB_CITED_TIMES.pdwh_pub_id
  is '成果id';
comment on column PDWH_PUB_CITED_TIMES.cited_times
  is '引用次数';
comment on column PDWH_PUB_CITED_TIMES.update_date
  is '更新的日期';
comment on column PDWH_PUB_CITED_TIMES.type
  is '手动更新1，后台更新0';




-- Create table
create table PDWH_CITED_RELATION
(
  cited_id          NUMBER(18)  not null,
  pdwh_pub_id       NUMBER(18)  not null,
  pdwh_cited_pub_id NUMBER(18)  not null
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
-- Create/Recreate indexes 
create unique index PK_PDWH_CITED_RELATION on PDWH_CITED_RELATION (CITED_ID)
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;

  
  -- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_CITED_RELATION
  add constraint PK_PDWH_CITED_RELATION primary key (CITED_ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;


  -- Add comments to the columns 
comment on column PDWH_CITED_RELATION.cited_id
  is 'id';
comment on column PDWH_CITED_RELATION.pdwh_pub_id
  is '成果id';
comment on column PDWH_CITED_RELATION.pdwh_cited_pub_id
  is '被引用的成果id';

 create sequence SEQ_PDWH_CITED_RELATION
 increment by 1
 start with 99999
 nomaxvalue 
 nocycle
 cache 10;


--原因（有CQ号带上CQ号） 2017-05-23 zjh end
---------zjh 2017-05-23 begin---------------

create index UK_PDWH_CITED_RELATION on PDWH_CITED_RELATION (PDWH_PUB_ID, PDWH_CITED_PUB_ID)
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  
  
  ----------zjh  2017-05-23 end-------
  
  
--原因（新增成果短地址表） 2017-06-02 LJ start ---

-- Create table
create table V_PDWH_PUB_INDEX_URL
(
  pub_id        NUMBER(18) not null,
  pub_index_url VARCHAR2(100 CHAR),
  psn_id        NUMBER(18),
  update_date   DATE
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table V_PDWH_PUB_INDEX_URL
  is '成果短地址表';
-- Add comments to the columns 
comment on column V_PDWH_PUB_INDEX_URL.pub_id
  is '成果id';
comment on column V_PDWH_PUB_INDEX_URL.pub_index_url
  is '成果短地址';
comment on column V_PDWH_PUB_INDEX_URL.psn_id
  is '最后修改人';
comment on column V_PDWH_PUB_INDEX_URL.update_date
  is '修改时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_PDWH_PUB_INDEX_URL
  add constraint PK_V_PDWH_PUB_INDEX_URL primary key (PUB_ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;


--原因（新增成果短地址表） 2017-06-02 LJ end  ----

--原因（初始化短地址任务数据） 2017-06-06 LJ begin

insert into V_pdwh_PUB_INDEX_URL (pub_id)  select m.pub_id from pdwh_publication m where not exists  (select t.pub_id from V_pdwh_PUB_INDEX_URL t where m.pub_id=t.pub_id );


--原因（初始化短地址任务数据） 2017-06-06 LJ end









