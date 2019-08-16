-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（SCM-13423） 2017-07-26 zzx begin
create sequence SEQ_TEST_V_RECOM_MSG_FORM
minvalue 1
maxvalue 199999999999999999
start with 1
increment by 1
cache 20;
-- Create table
create table TEST_V_RECOM_MSG_FORM
(
  id          NUMBER(18),
  sender_id   NUMBER(18) default 0,
  receiver_id NUMBER(18) not null,
  count       NUMBER(8) default 0,
  type        NUMBER(2) not null,
  recom_date  DATE,
  res_id      NUMBER(18),
  task_status NUMBER(1) default 0
)
tablespace V3RCMD
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
comment on table TEST_V_RECOM_MSG_FORM
  is '成果认领全文认领消息任务表';

insert into TEST_V_RECOM_MSG_FORM 
select
0, 
0,
 t.psn_id,
 count(t.id),
 2,
 sysdate,
 max(rol_pub_id),
 0
  from pub_confirm t where t.CONFIRM_RESULT =0 group by psn_id;

update TEST_V_RECOM_MSG_FORM t set  t.id=SEQ_TEST_V_RECOM_MSG_FORM.nextval where t.id=0;
commit;
--原因（SCM-13423） 2017-07-26 zzx end

