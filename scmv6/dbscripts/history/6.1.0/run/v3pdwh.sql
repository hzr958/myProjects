-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

 ---原因（SCM-16544 论文SEO策略调整） 2018-03-20 zll begin
insert into pdwh_index_publication(pub_id,db_id,zh_title,en_title,zh_title_short ) 
select t.pub_id,t.db_id,t.zh_title,t.en_title,fn_getpy(substr(t.zh_title,0,1)) from pdwh_publication t

---原因（SCM-16544 论文SEO策略调整） 2018-03-20 zll end

--原因（SCM-16881） 2018-03-27 lj begin


-- Create table
create table PDWH_INS_ADDR_CONST
(
  const_id      NUMBER(10) not null,
  ins_id        NUMBER(9),
  ins_name      VARCHAR2(500 CHAR),
  country       VARCHAR2(20 CHAR),
  province      VARCHAR2(20 CHAR),
  city          VARCHAR2(20 CHAR),
  full_addr     VARCHAR2(500 CHAR),
  language      NUMBER(1),
  const_status  NUMBER(1) default 0,
  last_operator NUMBER(1) default 0,
  addr_status   NUMBER(1),
  ins_name_hash NUMBER,
  update_time   DATE
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
comment on table PDWH_INS_ADDR_CONST
  is 'scm单位/机构 详细地址信息表';
-- Add comments to the columns 
comment on column PDWH_INS_ADDR_CONST.const_id
  is '主键';
comment on column PDWH_INS_ADDR_CONST.ins_id
  is '单位Id';
comment on column PDWH_INS_ADDR_CONST.ins_name
  is '单位名';
comment on column PDWH_INS_ADDR_CONST.country
  is '国家';
comment on column PDWH_INS_ADDR_CONST.province
  is '省';
comment on column PDWH_INS_ADDR_CONST.city
  is '市';
comment on column PDWH_INS_ADDR_CONST.full_addr
  is '详细地址';
comment on column PDWH_INS_ADDR_CONST.language
  is '语言，1中文，2英文';
comment on column PDWH_INS_ADDR_CONST.const_status
  is '单位常量的状态，0默认，1 ins_id信息已被修改，2省市其他信息被修改';
comment on column PDWH_INS_ADDR_CONST.last_operator
  is '最后操作人,默认为0系统,';
comment on column PDWH_INS_ADDR_CONST.addr_status
  is '单位具体地址可信度信息，0没有地址信息，1可信度高（百度api可以查到唯一地址），2可信度中（百度查到多个地址<只记录了一个>），3可信度较低（自动从名称中提取的省市）';
comment on column PDWH_INS_ADDR_CONST.update_time
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_INS_ADDR_CONST
  add constraint PK_PDWH_INS_ADDR_CONST_ID primary key (CONST_ID)
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
create index IDX_PDWH_INS_ADDR_CONST_INS on PDWH_INS_ADDR_CONST (INS_ID)
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




create sequence seq_pdwh_ins_addr_const
minvalue 1
maxvalue 9999999999999999999999999999
start with 10001
increment by 1
cache 20;


create sequence SEQ_PDWH_AUTH_SNS_PSN_RECORD
minvalue 1
maxvalue 9999999999999999999999999999
start with 10001
increment by 1
cache 20;


create sequence SEQ_PDWH_PUB_ADDR_INS_RECORD
minvalue 1
maxvalue 9999999999999999999999999999
start with 10001
increment by 1
cache 20;




-- Create table
create table PDWH_PUB_ADDR_INS_RECORD
(
  id            NUMBER(10) not null,
  const_id      NUMBER(10),
  pub_id        NUMBER(18),
  ins_id        NUMBER(9),
  ins_name      VARCHAR2(500 CHAR),
  ins_name_hash NUMBER,
  status        NUMBER(1),
  update_time   DATE
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
-- Add comments to the columns 
comment on column PDWH_PUB_ADDR_INS_RECORD.id
  is '主键';
comment on column PDWH_PUB_ADDR_INS_RECORD.const_id
  is '和PDWH_INS_ADDR_CONST表主键相同，便于PDWH_INS_ADDR_CONST表insid合并后更新记录';
comment on column PDWH_PUB_ADDR_INS_RECORD.pub_id
  is '基准库成果id';
comment on column PDWH_PUB_ADDR_INS_RECORD.ins_id
  is '单位id';
comment on column PDWH_PUB_ADDR_INS_RECORD.ins_name
  is '单位名';
comment on column PDWH_PUB_ADDR_INS_RECORD.ins_name_hash
  is '单位名hash';
comment on column PDWH_PUB_ADDR_INS_RECORD.status
  is '0表示系统匹配，用户确认后状态为1（暂时没有确认流程，预留）';
comment on column PDWH_PUB_ADDR_INS_RECORD.update_time
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_ADDR_INS_RECORD
  add constraint PK_PDWH_PUB_ADDR_INS_RECORD primary key (ID)
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
create index IDX_PDWH_PUB_ADDR_INS_RE_CONST on PDWH_PUB_ADDR_INS_RECORD (CONST_ID)
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
create index IDX_PDWH_PUB_ADDR_INS_RE_INS on PDWH_PUB_ADDR_INS_RECORD (INS_ID)
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
create index IDX_PDWH_PUB_ADDR_INS_RE_PUB on PDWH_PUB_ADDR_INS_RECORD (PUB_ID)
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
create table PDWH_AUTHOR_SNS_PSN_RECORD
(
  id          NUMBER(10) not null,
  pub_id      NUMBER(18),
  psn_id      NUMBER(18),
  psn_name    VARCHAR2(100 CHAR),
  ins_id      NUMBER(9),
  ins_name    VARCHAR2(500 CHAR),
  status      NUMBER(1),
  update_time DATE
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
-- Add comments to the table 
comment on table PDWH_AUTHOR_SNS_PSN_RECORD
  is '基准库成果作者和sns人员对应关系表';
-- Add comments to the columns 
comment on column PDWH_AUTHOR_SNS_PSN_RECORD.id
  is '主键Id';
comment on column PDWH_AUTHOR_SNS_PSN_RECORD.pub_id
  is '基准库成果Id';
comment on column PDWH_AUTHOR_SNS_PSN_RECORD.psn_id
  is 'SNS人员Id';
comment on column PDWH_AUTHOR_SNS_PSN_RECORD.psn_name
  is 'SNS人员名';
comment on column PDWH_AUTHOR_SNS_PSN_RECORD.ins_id
  is '单位/机构id';
comment on column PDWH_AUTHOR_SNS_PSN_RECORD.ins_name
  is '单位/机构名';
comment on column PDWH_AUTHOR_SNS_PSN_RECORD.status
  is '状态1.表示可信度低(仅人名匹配上)
2.表示可信度中（人名和未确认的地址），3表示可信度最高（由用户成果认领确认或人名和确认的地址）。（地址状态信息PDWH_PUB_ADDR_INS_RELATIVE表状态描述）';
comment on column PDWH_AUTHOR_SNS_PSN_RECORD.update_time
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_AUTHOR_SNS_PSN_RECORD
  add constraint PDWH_AUTHOR_SNS_PSN_RECORD primary key (ID)
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
create index IDX_PDWH_PUB_AU_SNS_RE_INS on PDWH_AUTHOR_SNS_PSN_RECORD (INS_ID)
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
create index IDX_PDWH_PUB_AU_SNS_RE_PSN on PDWH_AUTHOR_SNS_PSN_RECORD (PSN_ID)
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
create index IDX_PDWH_PUB_AU_SNS_RE_PUB on PDWH_AUTHOR_SNS_PSN_RECORD (PUB_ID)
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
create table TMP_INS_BD_ADDR
(
  id                NUMBER(10) not null,
  tmp_ins_id        NUMBER(18),
  tmp_ins_name      VARCHAR2(400 CHAR),
  country           VARCHAR2(100 CHAR),
  province          VARCHAR2(100 CHAR),
  city              VARCHAR2(100 CHAR),
  full_address      VARCHAR2(200 CHAR),
  status            NUMBER default 0,
  tmp_ins_name_hash NUMBER,
  language          NUMBER(1)
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
-- Add comments to the columns 
comment on column TMP_INS_BD_ADDR.id
  is '主键';
comment on column TMP_INS_BD_ADDR.tmp_ins_id
  is '处理来源数据表的id或者标识符';
comment on column TMP_INS_BD_ADDR.tmp_ins_name
  is '处理来源数据表的地址信息';
comment on column TMP_INS_BD_ADDR.country
  is '获取到的国家';
comment on column TMP_INS_BD_ADDR.province
  is '获取到的省';
comment on column TMP_INS_BD_ADDR.city
  is '获取到的城市';
comment on column TMP_INS_BD_ADDR.full_address
  is '获取到的格式化地址';
comment on column TMP_INS_BD_ADDR.status
  is '处理状态信息，0未处理，1处理成功，2处理异常，3获取不到地址，4接口受限，5有多个结果,6根据单位名获取到';
comment on column TMP_INS_BD_ADDR.tmp_ins_name_hash
  is '地址hash';
comment on column TMP_INS_BD_ADDR.language
  is '语言1中文 2英文';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TMP_INS_BD_ADDR
  add constraint PK_TMP_INS_BD_ADDR primary key (ID)
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
create index IDX_TMP_INS_BD_ADDR on TMP_INS_BD_ADDR (TMP_INS_ID)
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
create index IDX_TMP_INS_BD_ADDR1 on TMP_INS_BD_ADDR (TMP_INS_NAME)
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
create index IDX_TMP_INS_BD_HASH on TMP_INS_BD_ADDR (TMP_INS_NAME_HASH)
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
create index IDX_TMP_INS_BD_STATUS on TMP_INS_BD_ADDR (STATUS)
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
create table PDWH_MATCH_TASK_RECORD
(
  pdwh_pub_id  NUMBER(18) not null,
  match_status NUMBER(1),
  update_time  DATE,
  err_msg      VARCHAR2(200 CHAR)
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table PDWH_MATCH_TASK_RECORD
  is '基准库成果地址作者信息匹配状态记录表';
-- Add comments to the table 
comment on column PDWH_MATCH_TASK_RECORD.MATCH_STATUS
  is '1 都匹配上， 2地址匹配出错 ，3作者匹配出错';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_MATCH_TASK_RECORD
  add constraint PK_PDWH_MATCH_TASK_RECORD primary key (PDWH_PUB_ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
-- Create/Recreate indexes 
create index IDX_PDWH_MATCH_TASK_RECORD_ST on PDWH_MATCH_TASK_RECORD (MATCH_STATUS)
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;
  
--原因（SCM-16881）2018-03-27 lj  end
--原因（SCM-16595 修改来源字段） 2018-3-27 ltl begin
delete from TEM_TASK_PDWHBRIEF;
insert into TEM_TASK_PDWHBRIEF(pub_id,status,error_msg)
select a.pub_id, 0, '' from PDWH_PUBLICATION a where exists(select 1 from  PDWH_PUB_XML b where a.pub_id=b.pub_id);
commit;
--原因（SCM-16595 修改来源字段） 2018-3-27 ltl  end

--原因（SCM-16544 论文SEO策略调整） 2018-03-27 zll begin
-- Alter table 
alter table PDWH_INDEX_PUBLICATION
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table PDWH_INDEX_PUBLICATION add en_title_short VARCHAR2(50 CHAR);
-- Add comments to the columns 
comment on column PDWH_INDEX_PUBLICATION.en_title_short
  is '英文标题转拼音';


update pdwh_index_publication t set t.en_title_short=fn_getpy(substr(t.en_title,0,3));


--原因（SCM-16544 论文SEO策略调整） 2018-03-27 zll end
--原因（SCM-16600 来源字段的修改） 2018-03-29 ltl begin
update TEM_TASK_PDWHBRIEF set status=0,error_msg='';
commit;
--原因（SCM-16600 来源字段的修改） 2018-03-29 ltl end


