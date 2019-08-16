-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--原因（SCM-13698 后台维护管理功能修改） 2017-08-21 zll begin
-- Add/modify columns 
alter table CONST_FUND_CATEGORY add ins_type NUMBER(1);
-- Add comments to the columns 
comment on column CONST_FUND_CATEGORY.ins_type
  is '单位要求 1.企业，2，科研机构';
commit;

create table category_map_base as select t.* from uatsns.category_map_base t;


--原因（SCM-13698  后台维护管理功能修改） 2017-08-21 zll end
--原因（SCM-13698 后台维护管理功能修改 ） 2017-08-24 zll begin

alter table rcmd2.CONST_FUND_CATEGORY modify ins_type VARCHAR2(100 CHAR);

--原因（SCM-13698 后台维护管理功能修改 ） 2017-08-24 zll end



--原因（SCM-13698 后台维护管理功能修改 ） 2017-08-24 zll begin

alter table CONST_FUND_CATEGORY modify ins_type VARCHAR2(100 CHAR);

--原因（SCM-13698 后台维护管理功能修改 ） 2017-08-24 zll end

--原因（SCM-13698 后台维护管理功能修改 ） 2017-08-24 zll begin
update const_fund_category  t set t.ins_type= null where t.ins_type is not null;
commit;
alter table rcmd2.CONST_FUND_CATEGORY modify ins_type VARCHAR2(100 CHAR);

--原因（SCM-13698 后台维护管理功能修改 ） 2017-08-24 zll end

--原因（SCM-13698 后台维护管理功能修改 ） 2017-08-24 zll begin
update const_fund_category  t set t.ins_type= null where t.ins_type is not null;
commit;
alter table CONST_FUND_CATEGORY modify ins_type VARCHAR2(100 CHAR);

--原因（SCM-13698 后台维护管理功能修改 ） 2017-08-24 zll end
--原因（表名不好理解，修改表名） 2017-08-28  ajb begin

--原因（有CQ号带上CQ号） 2016-12-8 WSN end






-----原因（推荐库的成id对应基准库的成果id）2017-09-08 ajb start
-- Create table
create table PUB_PDWH_RELATION_FROM_ROL
(
  rol_pub_id  NUMBER(18) not null,
  pdwh_pub_id NUMBER(18)
)
tablespace V3RCMD
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
comment on table PUB_PDWH_RELATION_FROM_ROL
  is ' ROL与PDWH成果关系对应表';
-- Add comments to the columns 
comment on column PUB_PDWH_RELATION_FROM_ROL.rol_pub_id
  is 'ROL成果id';
comment on column PUB_PDWH_RELATION_FROM_ROL.pdwh_pub_id
  is '基准库成果id';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PUB_PDWH_RELATION_FROM_ROL
  add constraint PUB_PDWH_ROL_RELATION primary key (ROL_PUB_ID)
  using index 
  tablespace V3RCMD
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
  -----原因（推荐库的成id对应基准库的成果id）2017-09-08 ajb end