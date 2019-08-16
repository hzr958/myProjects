-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（SCM-15283 认同关键词邮件） 2018-3-15 ltl begin
create table KEYWORD_AGREE_STATUS
(
  id              VARCHAR2(18) not null,
  psn_id          NUMBER not null,
  KW_ID           NUMBER not null,
  friend_id       NUMBER not null,
  op_date         DATE default sysdate,
  status          NUMBER(5)
)
tablespace V3SNS
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
comment on table KEYWORD_AGREE_STATUS
  is '认同研究领域邮件生成状态记录表';
-- Add comments to the columns 
comment on column KEYWORD_AGREE_STATUS.id
  is '与认同记录表ID一致';
comment on column KEYWORD_AGREE_STATUS.psn_id
  is '被认同人';
comment on column KEYWORD_AGREE_STATUS.KW_ID
  is '个人关键词ID';
comment on column KEYWORD_AGREE_STATUS.friend_id
  is '认同人员';
comment on column KEYWORD_AGREE_STATUS.op_date
  is '认同时间';
comment on column KEYWORD_AGREE_STATUS.status
  is '0表示未生成，1生成失败，2成功';
-- Create/Recreate indexes 
create index PK_KEYWORD_AGREE_STATUS on KEYWORD_AGREE_STATUS (ID)
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
--原因（SCM-15283 认同关键词邮件） 2018-3-15 ltl end