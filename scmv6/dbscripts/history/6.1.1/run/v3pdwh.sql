-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end



--原因（SCM-17341） 2018-04-24 lj begin
alter table Pdwh_Author_Sns_Psn_Record add name_type number(2);
comment on column PDWH_AUTHOR_SNS_PSN_RECORD.name_type
  is '匹配上的人名类型，对应person_pm_name表的 type'; 
create index IDX_PDWH_PUB_AU_SNS_RE_NT on PDWH_AUTHOR_SNS_PSN_RECORD (STATUS, NAME_TYPE)
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

--原因（SCM-17341） 2018-04-24 lj end





--原因（SCM-17330 关键词翻译接口） 2018-04-25 ajb start

-- Create sequence 
create sequence seq_pdwh_pub_kyword_dictionary
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 10;


-- Alter table 
alter table PDWH_PUB_KEYWORDS
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table PDWH_PUB_KEYWORDS add status NUMBER(1) default 1;
-- Add comments to the columns 
comment on column PDWH_PUB_KEYWORDS.status
  is '默认值为0=没有跑任务， 1=已经跑了任务';
 commit ;
--原因（SCM-17330 关键词翻译接口） 2018-04-25 ajb end



--原因（    SCM-17330 关键词翻译接口） 2018-04-25 ajb start

-- Create table
create table PDWH_PUB_KYWORD_DICTIONARY
(
  id            NUMBER(18) not null,
  zh_keyword    VARCHAR2(200),
  en_keyword    VARCHAR2(200),
  language      NUMBER(1) not null,
  update_date   DATE,
  status        NUMBER(1) default 0 not null,
  en_keyword_bd VARCHAR2(200),
  en_keyword_tx VARCHAR2(200),
  en_keyword_gg VARCHAR2(200) default 0,
  zh_keyword_gg VARCHAR2(200),
  zh_keyword_bd VARCHAR2(200),
  zh_keyword_tx VARCHAR2(200)
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
comment on table PDWH_PUB_KYWORD_DICTIONARY
  is '基准库成果，关键词词典';
-- Add comments to the columns 
comment on column PDWH_PUB_KYWORD_DICTIONARY.id
  is '主键';
comment on column PDWH_PUB_KYWORD_DICTIONARY.zh_keyword
  is '中文关键词 ，需要翻译的关键词';
comment on column PDWH_PUB_KYWORD_DICTIONARY.en_keyword
  is '英文关键词 ，需要翻译的关键词';
comment on column PDWH_PUB_KYWORD_DICTIONARY.language
  is '语种  1=英文  ， 2=中文';
comment on column PDWH_PUB_KYWORD_DICTIONARY.update_date
  is '更新时间';
comment on column PDWH_PUB_KYWORD_DICTIONARY.status
  is '状态  0=没有翻译 ， 1=已经翻译';
comment on column PDWH_PUB_KYWORD_DICTIONARY.en_keyword_bd
  is '百度翻译 ， 中文关键词翻译成英文';
comment on column PDWH_PUB_KYWORD_DICTIONARY.en_keyword_tx
  is '腾讯翻译翻译 ， 中文关键词翻译成英文';
comment on column PDWH_PUB_KYWORD_DICTIONARY.en_keyword_gg
  is 'google翻译 ， 中文关键词翻译成英文';
comment on column PDWH_PUB_KYWORD_DICTIONARY.zh_keyword_gg
  is 'google翻译 ， 英文关键词翻译成中文';
comment on column PDWH_PUB_KYWORD_DICTIONARY.zh_keyword_bd
  is '百度翻译 ，   英文关键词翻译成中文';
comment on column PDWH_PUB_KYWORD_DICTIONARY.zh_keyword_tx
  is '腾讯翻译 ， 英文关键词翻译成中文';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_KYWORD_DICTIONARY
  add constraint PK_PDWH_PUB_KYWORD_DICTIONARY primary key (ID)
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
-- Create/Recreate indexes 
create index IDX_PDWH_PUB_KYWORD_DIC_EK on PDWH_PUB_KYWORD_DICTIONARY (EN_KEYWORD)
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
create index IDX_PDWH_PUB_KYWORD_DIC_ZK on PDWH_PUB_KYWORD_DICTIONARY (ZH_KEYWORD)
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

 commit ;
 
 --原因（    SCM-17330 关键词翻译接口） 2018-04-25 ajb end

--原因（SCM-17330 关键词翻译接口） 2018-04-25 ajb start
-- Add/modify columns 
alter table PDWH_PUB_KYWORD_DICTIONARY modify zh_keyword VARCHAR2(200 char);
alter table PDWH_PUB_KYWORD_DICTIONARY modify en_keyword VARCHAR2(200 char);
alter table PDWH_PUB_KYWORD_DICTIONARY modify en_keyword_bd VARCHAR2(200 char);
alter table PDWH_PUB_KYWORD_DICTIONARY modify en_keyword_tx VARCHAR2(200 char);
alter table PDWH_PUB_KYWORD_DICTIONARY modify en_keyword_gg VARCHAR2(200 char);
alter table PDWH_PUB_KYWORD_DICTIONARY modify zh_keyword_gg VARCHAR2(200 char);
alter table PDWH_PUB_KYWORD_DICTIONARY modify zh_keyword_bd VARCHAR2(200 char);
alter table PDWH_PUB_KYWORD_DICTIONARY modify zh_keyword_tx VARCHAR2(200 char);
commit ;
--原因（SCM-17330 关键词翻译接口） 2018-04-25 ajb end


--原因（SCM-17400  修改备注 ） 2018-04-28 艾江斌  start
-- Alter table 
alter table PDWH_PUB_KYWORD_DICTIONARY
  storage
  (
    next 8
  )
;
-- Add comments to the columns 
comment on column PDWH_PUB_KYWORD_DICTIONARY.status
  is '状态  0=没有翻译 ， 1=已经翻译 ,9=错误数据';
  
  --原因（SCM-17400  修改备注 ） 2018-04-28 艾江斌  end



--原因（基准库成果关键词 ，添加hashcode） 2018-05-10 ajb begin

-- Alter table 
alter table PDWH_PUB_KYWORD_DICTIONARY
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table PDWH_PUB_KYWORD_DICTIONARY add keyword_hash_code VARCHAR2(32);

-- Add comments to the columns 
comment on column PDWH_PUB_KYWORD_DICTIONARY.keyword_hash_code
  is '关键词hash值';
-- Create/Recreate indexes 
create index IDX_PDWH_PUB_KYWORD_HASH on PDWH_PUB_KYWORD_DICTIONARY (keyword_hash_code);

commit; 


--原因（基准库成果关键词 ，添加hashcode） 2018-05-10 ajb end




--原因（修改成果关键词 索引为唯一索引） 2018-05-11 ajb start 

-- Alter table 
alter table PDWH_PUB_KYWORD_DICTIONARY
  storage
  (
    next 8
  )
;
-- Create/Recreate indexes 
drop index IDX_PDWH_PUB_KYWORD_DIC_EK;
create unique index IDX_PDWH_PUB_KYWORD_DIC_EK on PDWH_PUB_KYWORD_DICTIONARY (EN_KEYWORD)
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
drop index IDX_PDWH_PUB_KYWORD_DIC_ZK;
create unique index IDX_PDWH_PUB_KYWORD_DIC_ZK on PDWH_PUB_KYWORD_DICTIONARY (ZH_KEYWORD)
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
drop index IDX_PDWH_PUB_KYWORD_HASH;
create unique index IDX_PDWH_PUB_KYWORD_HASH on PDWH_PUB_KYWORD_DICTIONARY (KEYWORD_HASH_CODE)
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
  
commit ;
--原因（修改成果关键词 索引为唯一索引） 2018-05-11 ajb end