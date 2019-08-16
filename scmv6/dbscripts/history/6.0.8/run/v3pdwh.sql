-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--原因（SCM-15405） 2017-12-18 LJ begin

-- Create table
create table PDWH_PATENT_CATEGORY
(
  PUB_ID     NUMBER(18) not null,
  CATEGORY  NUMBER(5),
  MAIN_CATEGORY_NO VARCHAR2(100 CHAR),
  CATEGORY_NO VARCHAR2(500 CHAR)
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
comment on table PDWH_PATENT_CATEGORY
  is '基准库专利分类信息表';


-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PATENT_CATEGORY
  add constraint PK_PDWH_PATENT_CATEGORY primary key (PUB_ID)
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


--原因（SCM-15405） 2017-12-18 LJ end








--原因（有CQ号带上CQ号） 2017-12-19 LJ begin
ALTER TABLE v3pdwh.PDWH_PUB_AUTHOR_INFO ADD (ORGANIZATION_SPEC VARCHAR2(200 CHAR));

alter table v3pdwh.PDWH_SNS_PUBAUTHOR_RELATION add NAME VARCHAR2(20 CHAR);

ALTER TABLE v3pdwh.PDWH_SNS_PUBAUTHOR_RELATION ADD (ORGANIZATION VARCHAR2(200 CHAR));


--原因（有CQ号带上CQ号） 2017-12-19 LJ end