-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end  type        NUMBER(2)

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