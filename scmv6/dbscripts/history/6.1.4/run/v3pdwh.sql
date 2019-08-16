-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（SCM-21991后台、在线导入数据前期拆分原始数据到mongodb） 2018-12-20 zll begin
-- Create table
create table original_pdwh_pub_relation
(
  original_id number(18),
  xml_id      number(18),
  seq_no      varchar2(50),
  ins_id      number(8),
  psn_id      number(18),
  record_from number(2),
  status      number(2)
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
comment on table original_pdwh_pub_relation
  is '原始数据与基准库成果关联关系';
-- Add comments to the columns 
comment on column original_pdwh_pub_relation.original_id
  is '主键（V_PUB_ORIGINAL_DATA中的id）';
comment on column original_pdwh_pub_relation.xml_id
  is '原始数据id';
comment on column original_pdwh_pub_relation.seq_no
  is '原始数据序列';
comment on column original_pdwh_pub_relation.ins_id
  is '原始数据单位';
comment on column original_pdwh_pub_relation.psn_id
  is '原始数据导入人员';
comment on column original_pdwh_pub_relation.record_from
  is '原始数据来源 1：后台导入，2：在线导入，3：crossref';
comment on column original_pdwh_pub_relation.status
  is '处理状态';
-- Create/Recreate primary, unique and foreign key constraints 
alter table original_pdwh_pub_relation
  add constraint pk_original_pdwh_pub_relation primary key (ORIGINAL_ID);
-- Alter table 
alter table ORIGINAL_PDWH_PUB_RELATION
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table ORIGINAL_PDWH_PUB_RELATION add pdwh_pub_id NUMBER(18);
-- Add comments to the columns 
comment on column ORIGINAL_PDWH_PUB_RELATION.pdwh_pub_id
  is '基准库成果id';
  
  create sequence SEQ_ORIGINALE_PDWH_PUB
minvalue 1
maxvalue 99999999999999999
start with 11
increment by 1
cache 10;


--原因（SCM-21991后台、在线导入数据前期拆分原始数据到mongodb） 2018-12-20 zll end
