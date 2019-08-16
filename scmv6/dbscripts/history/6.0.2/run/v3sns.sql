-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（优化群组成果性能） 2016-12-19 ZZX begin
create index IDX_GIP_PSN_ID on GROUP_INVITE_PSN (PSN_ID)
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

create index IDX_PUBLICATION_CDATE on PUBLICATION (CREATE_DATE)
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

-- Create/Recreate indexes 
create index IDX_GPUB_PUB_ID on GROUP_PUBS (PUB_ID)
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


--原因（优化群组成果性能） 2016-12-19 ZZX end