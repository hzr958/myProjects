-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end--原因（SCM-11610 **PubCacheSendTask指派任务与基准库相关表调整） 2017-03-23 zll begin

create table PDWH_PUBCACHE_INS_ASSIGN
(
  assign_id NUMBER(18),
  xml_id    NUMBER(18) not null,
  pub_id    NUMBER(18) not null,
  ins_id    NUMBER(18) not null,
  imported  NUMBER(1) not null
)
tablespace V3ROL
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
comment on table PDWH_PUBCACHE_INS_ASSIGN
  is '单位端成果匹配到单位记录';
  
  
--原因（SCM-11610 **PubCacheSendTask指派任务与基准库相关表调整） 2017-03-23 zll end