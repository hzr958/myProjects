-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

-- Alter table 
alter table PDWH_AUTHOR_SNS_PSN_RECORD
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table PDWH_AUTHOR_SNS_PSN_RECORD add pub_member_name VARCHAR2(100 CHAR);
alter table PDWH_AUTHOR_SNS_PSN_RECORD add pub_member_id NUMBER(18);
-- Add comments to the columns 
comment on column PDWH_AUTHOR_SNS_PSN_RECORD.pub_member_name
  is '成果作者';
comment on column PDWH_AUTHOR_SNS_PSN_RECORD.pub_member_id
  is '成果作者memberId';
  
  
  


--成果新加publish_month,publish_day字段  2019-04-09  tsz start
-- Add/modify columns
alter table v_pub_pdwh add publish_month NUMBER(2);
alter table v_pub_pdwh add publish_day NUMBER(2);
-- Add comments to the columns
comment on column v_pub_pdwh.publish_month
  is '发表日期月';
comment on column v_pub_pdwh.publish_day
  is '发表日期日';
-- Create/Recreate indexes
create index IDX_V_PUB_PDWH_M on v_pub_pdwh (publish_month);
create index IDX_V_PUB_PDWH_D on v_pub_pdwh (publish_day);

--成果新加publish_month,publish_day字段  2019-04-09  tsz  end---scm-0000  修改字段最大长度 2019-4-10 begin
-- Add/modify columns 
alter table PDWH_INDEX_PUBLICATION modify short_title VARCHAR2(50);
---scm-0000  修改字段最大长度 2019-4-10 end