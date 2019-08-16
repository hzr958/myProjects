-- 数据库脚本登记示例

--原因（SCM-13858） 2017-08-15 LJ begin
-- Create table  
create table PDWH_PUB_AUTHOR_INFO
(
  ID NUMBER(18) primary key not null,
  PUB_ID      VARCHAR2(18) ,
  AUTHOR_NAME VARCHAR2(100 char) ,
   AUTHOR_NAME_SPEC VARCHAR2(100 char) ,
  organization   VARCHAR2(200 char),
  email VARCHAR2(50 char)
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16K
    next 8K
    minextents 1
    maxextents unlimited
  );

create sequence SEQ_PDWH_PUB_AUTHOR_INFO
minvalue 1
maxvalue 999999999999999999999999999
start with 1000001
increment by 1
nocache;
insert into TMP_TASK_INFO_RECORD (job_id,JOB_TYPE,HANDLE_ID) select SEQ_TMP_TASK_INFO_RECORD.NEXTVAL , 1,pub_id from pdwh_publication;
--原因（SCM-13858） 2017-08-15 LJ end

  
  





--原因（SCM-13698  后台维护管理功能修改） 2017-08-21 zll begin

create table category_map_base as select t.* from uatsns.category_map_base t;


--原因（SCM-13698  后台维护管理功能修改） 2017-08-21 zll end 

--原因（SCM-14068） 2017-09-06 LJ begin

create table PDWH_SNS_PUBAUTHOR_RELATION
(
   ID number(18) not null primary key,
  PUB_ID      number(18) not null,
  PSN_ID      number(18) not null,
  relation_email       varchar2(50)
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16K
    next 8K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table PDWH_SNS_PUBAUTHOR_RELATION
  is 'PDWH成果作者与sns人员关联信息记录表';
-- Add comments to the columns 
comment on column PDWH_SNS_PUBAUTHOR_RELATION.relation_email
  is '关联email地址';

  
  -- Create sequence 
create sequence SEQ_PDWH_SNS_PA_RELATION
minvalue 1
maxvalue 999999999999999999999999999
start with 1000001
increment by 1
nocache;


insert into TMP_TASK_INFO_RECORD (job_id,job_type,handle_id) select SEQ_TMP_TASK_INFO_RECORD.nextval,4,t.id from PDWH_PUB_AHUTHOR_INFO t where t.email is not null ;

--原因（SCM-14068） 2017-09-06 LJ end--原因（SCM-14102） 2017-09-08 LJ end
  
  
  
--原因（SCM-14213） 2017-09-08 LJ begain

-- Create table
create table PDWH_FULLTEXT_IMG
(
  pub_id                    NUMBER(18) not null,
  file_id                   NUMBER(18) not null,
  fulltext_image_page_index NUMBER(4),
  fulltext_image_path       VARCHAR2(500),
  fulltext_file_ext         VARCHAR2(30 CHAR)
)
tablespace V3PDWH
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16K
    next 8K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table PDWH_FULLTEXT_IMG
  is 'PDWH成果全文图片信息表';
-- Add comments to the columns 
comment on column PDWH_FULLTEXT_IMG.pub_id
  is '基准库成果id';
comment on column PDWH_FULLTEXT_IMG.file_id
  is '全文文件id';
comment on column PDWH_FULLTEXT_IMG.fulltext_image_page_index
  is 'pdf转换为图片的页数';
comment on column PDWH_FULLTEXT_IMG.fulltext_image_path
  is '全文图片路径';
comment on column PDWH_FULLTEXT_IMG.fulltext_file_ext
  is '全文文件后缀名';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_FULLTEXT_IMG
  add primary key (PUB_ID)
  using index 
  tablespace V3PDWH
  pctfree 10
  initrans 2
  maxtrans 255;


-- Create table
create table PDWH_PUB_FULLTEXT_IMG_REFRESH
(
  pub_id            NUMBER(18) not null,
  fulltext_file_id  NUMBER(18) not null,
  fulltext_node     NUMBER(6) not null,
  status            NUMBER(2) default 0 not null,
  error_msg         CLOB,
  fulltext_file_ext VARCHAR2(30 CHAR)
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
-- Add comments to the table 
comment on table PDWH_PUB_FULLTEXT_IMG_REFRESH
  is 'PDWH成果全文转换成图片刷新表';
-- Add comments to the columns 
comment on column PDWH_PUB_FULLTEXT_IMG_REFRESH.pub_id
  is '成果id';
comment on column PDWH_PUB_FULLTEXT_IMG_REFRESH.fulltext_file_id
  is '全文对应的附件id，关联表archive_files的file_id';
comment on column PDWH_PUB_FULLTEXT_IMG_REFRESH.fulltext_node
  is '全文附件所在的节点';
comment on column PDWH_PUB_FULLTEXT_IMG_REFRESH.status
  is '0需要转换，1转换成功，99转换失败';
comment on column PDWH_PUB_FULLTEXT_IMG_REFRESH.error_msg
  is '错误日志';
comment on column PDWH_PUB_FULLTEXT_IMG_REFRESH.fulltext_file_ext
  is '全文附件后缀';
-- Create/Recreate indexes 
create index IDX_P_PUB_FULLTEXT_IMG_STATUS on PDWH_PUB_FULLTEXT_IMG_REFRESH (STATUS)
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
-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_FULLTEXT_IMG_REFRESH
  add constraint PK_P_PUB_FULLTEXT_IMG_REFRESH primary key (PUB_ID)
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

--原因（SCM-14213） 2017-09-08 LJ end