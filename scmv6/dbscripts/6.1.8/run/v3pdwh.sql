-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因    SCM-25269 成果类型增加“标准”和“软件著作权” 2019-05-24 YJ begin
alter table v_pdwh_duplicate  add(HASH_STANDARD_NO VARCHAR2(32));
alter table v_pdwh_duplicate  add(HASH_REGISTER_NO VARCHAR2(32));
comment on column V_PDWH_DUPLICATE.hash_standard_no is '标准的标准号hash值';
comment on column V_PDWH_DUPLICATE.hash_register_no is '软件著作权的登记号hash值';
--原因    SCM-25269 成果类型增加“标准”和“软件著作权” 2019-05-24 YJ end

--原因(SCM-25327:bpo期刊管理功能迁移---期刊导入,增加dbID为空的字段和ERROR_COUNT的字段)2019-5-27 SYL begin
alter table BASE_JOURNAL_LOG add no_dbid_count NUMBER(18);
alter table BASE_JOURNAL_LOG add error_count NUMBER(18);
-- Add comments to the columns 
comment on column BASE_JOURNAL_LOG.no_dbid_count
  is '导入时没有DB_ID的数据条数';
comment on column BASE_JOURNAL_LOG.error_count
  is '导入时出现错误的数据条数';
--原因(SCM-25327:bpo期刊管理功能迁移---期刊导入,增加dbID为空的字段和ERROR_COUNT的字段)2019-5-27 SYL end
----原因（bpo期刊管理迁移，同时对一些表的外键进行删除） 2019-5-30 SYL start
alter table BASE_JOURNAL_DB
  drop constraint FK_BASE_JOURNAL_DB;
alter table BASE_JOURNAL_DB
  drop constraint FK_BASE_JOURNAL_JNL_ID;
  
alter table BASE_JOURNAL_TITLE
  drop constraint FK_BASE_JOURNAL_JNLID;
alter table BASE_JOURNAL_TITLE
  drop constraint FK_BASE_JOURNAL_TITLE_DBID;
  
  alter table BASE_JOURNAL_CATEGORY
  drop constraint FK_BASE_JNL_CAT_CATID;
alter table BASE_JOURNAL_CATEGORY
  drop constraint FK_BASE_JNL_CAT_JNLID;
  
  alter table BASE_JOURNAL_PUBLISHER
  drop constraint FK_BASE_JNL_PUBLISHER_DBID;
alter table BASE_JOURNAL_PUBLISHER
  drop constraint FK_BASE_JNL_PUBLISHER_JNLID;
  
  alter table BASE_JOURNAL_CATEGORY_RANK
  drop constraint FK_BASE_JNL_CAT_RANK_ID;
  ----原因（bpo期刊管理迁移，同时对一些表的外键进行删除） 2019-5-30 SYL end
--原因    SCM-25269 成果类型增加“标准”和“软件著作权” 2019-05-24 YJ begin
comment on column V_PUB_PDWH.pub_type is '1:奖励；2:书/著作；3:会议论文；4:期刊论文；5:专利；7:其他；8:学位论文；10:书籍章节；12:标准；13:软件著作权';
--原因    SCM-25269 成果类型增加“标准”和“软件著作权” 2019-05-24 YJ end


--原因    SCM-25817 基准库筛选重复成果任务 2019-06-18 YJ begin
create table V_PUB_PDWH_REPEAT_RECORD
(
  id          NUMBER(18) not null,
  pdwh_pub_id NUMBER(18),
  dup_pub_id  NUMBER(18)
);
comment on table V_PUB_PDWH_REPEAT_RECORD is '基准库重复成果记录表';
comment on column V_PUB_PDWH_REPEAT_RECORD.id is '主键';
comment on column V_PUB_PDWH_REPEAT_RECORD.pdwh_pub_id is '成果id';
comment on column V_PUB_PDWH_REPEAT_RECORD.dup_pub_id is '重复成果id';
alter table V_PUB_PDWH_REPEAT_RECORD
  add constraint V_PUB_PDWH_REPEAT_RECORD_PK primary key (ID);
create unique index V_PUB_PDWH_REPEAT_RECORD_UK on V_PUB_PDWH_REPEAT_RECORD (PDWH_PUB_ID, DUP_PUB_ID);
-- Create sequence 
create sequence V_SEQ_PUB_PDWH_REPEAT_RECORD
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;
--原因    SCM-25817 基准库筛选重复成果任务 2019-06-18 YJ end
