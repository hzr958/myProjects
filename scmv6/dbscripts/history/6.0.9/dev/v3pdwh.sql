
--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句



--原因（有CQ号带上CQ号） 2016-12-8 WSN end





--原因（SCM-16144） 2018-01-29 lj begin


-- Create table
create TABLE TMP_INS_BD_ADDR
(
  id                NUMBER(10) not null,
  tmp_ins_id        NUMBER(18),
  tmp_ins_name      VARCHAR2(400 CHAR),
  source_type       NUMBER(1),
  country           VARCHAR2(100 CHAR),
  province          VARCHAR2(100 CHAR),
  city              VARCHAR2(100 CHAR),
  full_address      VARCHAR2(200 CHAR),
  status            NUMBER default 0,
  tmp_ins_name_hash NUMBER
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
-- Add comments to the columns 
comment on column TMP_INS_BD_ADDR.id
  is '主键';
comment on COLUMN TMP_INS_BD_ADDR.tmp_ins_id
  is '处理来源数据表的id或者标识符';
comment on COLUMN TMP_INS_BD_ADDR.tmp_ins_name
  is '处理来源数据表的地址信息';
comment on COLUMN TMP_INS_BD_ADDR.source_type
  is '处理来源数据表的编号';
comment on column TMP_INS_BD_ADDR.country
  is '获取到的国家';
comment on column TMP_INS_BD_ADDR.province
  is '获取到的省';
comment on column TMP_INS_BD_ADDR.city
  is '获取到的城市';
comment on column TMP_INS_BD_ADDR.full_address
  is '获取到的格式化地址';
comment on column TMP_INS_BD_ADDR.status
  is '处理状态信息，0未处理，1处理成功，2处理异常，3获取不到地址，4接口受限，5有多个结果';
comment on COLUMN TMP_INS_BD_ADDR.tmp_ins_name_hash
  is '地址hash';
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
create index IDX_TMP_INS_BD_HASH on TMP_INS_BD_ADDR (TMP_INS_NAME_HASH)
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

--原因（SCM-16144） 2018-01-29 lj end