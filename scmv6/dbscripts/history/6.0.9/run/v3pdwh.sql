-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end




 --原因（SCM-15797） 2018-1-8 LJ begin

ALTER TABLE tmp_task_info_record  ADD relative_id NUMBER(18);
   comment on column tmp_task_info_record.relative_id
   is '与handle_Id关联的Id';

--原因（SCM-15797） 2018-12-8 LJ end


--原因（SCM-16221 成果加标识 有标记为可信来源） 2018-2-2 zzx begin


alter table pdwh_publication add update_mark number(2);
comment on column pdwh_publication.update_mark
  is '是否是在线导入或手工导入1=在线导入且未修改；2=在线导入且已修改；3=手工导入';
commit;
--原因（ SCM-16221 成果加标识 有标记为可信来源） 2018-2-2 zzx end



--原因（    SCM-16228 成果描述字段（brief_desc）字段需要修改改进） 2018-2-5 LTL begin
-- Create table
create table TEM_TASK_PDWHBRIEF
(
  pub_id    NUMBER(18) not null,
  status    NUMBER(2) default 0 not null,
  error_msg VARCHAR2(700 CHAR)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table TEM_TASK_PDWHBRIEF
  is '更新个人库成果的brief来源字段';
-- Add comments to the columns 
comment on column TEM_TASK_PDWHBRIEF.status
  is '0还没更新，1已经更新，2出错';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TEM_TASK_PDWHBRIEF
  add constraint PK_TEM_TASK_PDWHBRIEF primary key (PUB_ID)
  disable;
insert into TEM_TASK_PDWHBRIEF(pub_id,status) select pub_id,0 from PDWH_PUBLICATION order by pub_id asc;
commit;
--原因（    SCM-16228 成果描述字段（brief_desc）字段需要修改改进） 2018-2-5 LTL end