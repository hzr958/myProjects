-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（数据库中 GROUP_RCMDS 表 group_name 字段设计与主表不一样 SCM-11358） 2017-1-16  AJB begin


alter  table GROUP_RCMDS  modify  (  GROUP_NAME         VARCHAR2(200 CHAR) )  ;

--原因（数据库中 GROUP_RCMDS 表 group_name 字段设计与主表不一样 SCM-11358） 2017-1-16 AJB end

--原因（SCM-11329） 2017-1-16 lj begin
-- Create table
create table ISIS_MATCHED_PRJMEMBER
(
  id      NUMBER(18) not null,
  name    VARCHAR2(61 CHAR),
  psn_id  NUMBER(18),
  email   VARCHAR2(100 CHAR),
  company VARCHAR2(200 CHAR)
)
tablespace V3SNS
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
comment on table ISIS_MATCHED_PRJMEMBER
  is '基金委项目人员信息与scm科研之友注册人员匹配表';
-- Create/Recreate indexes 
create unique index UK_ISIS_MATCHED_PRJMEMBER on ISIS_MATCHED_PRJMEMBER (ID)
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



-- Create sequence 
create sequence SEQ_ISIS_MATCHED_PRJMEMBER
minvalue 1
maxvalue 9999999999999999999999999999
start with 1401
increment by 1
cache 20;








insert into V_QUARTZ_CRON_EXPRESSION values('5','isisPrjMemberMatchTaskTrigger','*/30 * * * * ?',1,'基金委项目成员与科研之友注册人员匹配任务');


commit;

--原因（SCM-11329） 2017-1-16 lj end

--原因（SCM-11361） 2017-1-19 lj begin

insert into V_QUARTZ_CRON_EXPRESSION values('6','inputFullTextTaskTrigger','*/30 * * * * ?',1,'全文上传任务');

comment on column ARCHIVE_FILES.create_psn_id
  is '创建人,后台任务上传默认为9999999999999';
  
commit;

--原因（SCM-11361） 2017-1-19 lj end

--原因（SCM-11378） 2017-02-07 LJ begin


--中间放sql语句

insert into V_QUARTZ_CRON_EXPRESSION values('7','pubToPubsimpleTask1Trigger','*/30 * * * * ?',0,'pubToPubsimpleTask1');
insert into V_QUARTZ_CRON_EXPRESSION values('8','pubToPubsimpleTask2Trigger','*/30 * * * * ?',0,'pubToPubsimpleTask2');
insert into V_QUARTZ_CRON_EXPRESSION values('9','pubToPubsimpleTask3Trigger','*/30 * * * * ?',0,'pubToPubsimpleTask3');
insert into V_QUARTZ_CRON_EXPRESSION values('10','pubToPubsimpleTask4Trigger','*/30 * * * * ?',0,'pubToPubsimpleTask4');
insert into V_QUARTZ_CRON_EXPRESSION values('11','pubToPubsimpleTask5Trigger','*/30 * * * * ?',0,'pubToPubsimpleTask5');
insert into V_QUARTZ_CRON_EXPRESSION values('12','pubToPubsimpleTask7Trigger','*/30 * * * * ?',0,'pubToPubsimpleTask7');
insert into V_QUARTZ_CRON_EXPRESSION values('13','pubToPubsimpleTask8Trigger','*/30 * * * * ?',0,'pubToPubsimpleTask8');
insert into V_QUARTZ_CRON_EXPRESSION values('14','pubToPubsimpleTask9Trigger','*/30 * * * * ?',0,'pubToPubsimpleTask9');
insert into V_QUARTZ_CRON_EXPRESSION values('15','pubToPubsimpleTask10Trigger','*/30 * * * * ?',0,'pubToPubsimpleTask10');
insert into V_QUARTZ_CRON_EXPRESSION values('16','pubToPubsimpleTask11Trigger','*/30 * * * * ?',0,'pubToPubsimpleTask11');
insert into V_QUARTZ_CRON_EXPRESSION values('17','pubToPubsimpleTask12Trigger','*/30 * * * * ?',0,'pubToPubsimpleTask12');
insert into V_QUARTZ_CRON_EXPRESSION values('18','pubToPubsimpleTask13Trigger','*/30 * * * * ?',0,'pubToPubsimpleTask13');
insert into V_QUARTZ_CRON_EXPRESSION values('19','pubToPubsimpleTask14Trigger','*/30 * * * * ?',0,'pubToPubsimpleTask14');
COMMIT;

--原因（SCM-11378） 2017-02-07 LJ  end

--原因（SCM-8963） 2017-02-07 lhd begin

insert into QUOTE_TEMPLATE_INFO values(31,1,10,'GBT7714-2015','GBT7714-2015','pub_chapter-GBT_quote',1,5);
insert into QUOTE_TEMPLATE_INFO values(32,1,2,'GBT7714-2015','GBT7714-2015','pub_book-GBT_quote',1,5);
insert into QUOTE_TEMPLATE_INFO values(33,1,3,'GBT7714-2015','GBT7714-2015','pub_conference-GBT_quote',1,5);
insert into QUOTE_TEMPLATE_INFO values(34,1,4,'GBT7714-2015','GBT7714-2015','pub_journal-GBT_quote',1,5);
insert into QUOTE_TEMPLATE_INFO values(35,1,5,'GBT7714-2015','GBT7714-2015','pub_patent-GBT_quote',1,5);
insert into QUOTE_TEMPLATE_INFO values(36,1,8,'GBT7714-2015','GBT7714-2015','pub_thesis-GBT_quote',1,5);

commit;

--原因（SCM-8963） 2017-02-07 lhd end
--原因（SCM-8963） 2017-02-08 lhd begin
 UPDATE QUOTE_TEMPLATE_INFO SET TEMPLATE_NAME = 'pub_chapter_GBT_quote' WHERE id = 31;
 UPDATE QUOTE_TEMPLATE_INFO SET TEMPLATE_NAME = 'pub_book_GBT_quote' WHERE id = 32;
 UPDATE QUOTE_TEMPLATE_INFO SET TEMPLATE_NAME = 'pub_conference_GBT_quote' WHERE id = 33;
 UPDATE QUOTE_TEMPLATE_INFO SET TEMPLATE_NAME = 'pub_journal_GBT_quote' WHERE id = 34;
 UPDATE QUOTE_TEMPLATE_INFO SET TEMPLATE_NAME = 'pub_patent_GBT_quote' WHERE id = 35;
 UPDATE QUOTE_TEMPLATE_INFO SET TEMPLATE_NAME = 'pub_thesis_GBT_quote' WHERE id = 36;

commit;



--原因（SCM-11398） 20170208 lj begin


insert into V_QUARTZ_CRON_EXPRESSION values('20','pubAllIndexTaskTrigger','*/10 * * * * ?',1,'pubAllIndexTask');
insert into V_QUARTZ_CRON_EXPRESSION values('21','PublicationIndexTrigger','*/10 * * * * ?',0,'pubAllIndexTask');

commit;

--原因（SCM-11398）  20170208 lj end

--原因（SCM-11398） 20170208 lj begin

delete  V_QUARTZ_CRON_EXPRESSION where id='20';
delete  V_QUARTZ_CRON_EXPRESSION where id='21';

insert into V_QUARTZ_CRON_EXPRESSION values('20','pubAllIndexTaskTrigger','*/10 * * * * ?',1,'pubAllIndexTask');
insert into V_QUARTZ_CRON_EXPRESSION values('21','publicationIndexTrigger','*/10 * * * * ?',0,'publicationIndexTask');
commit
--原因（SCM-11398）  20170208 lj end
--原因（SCM-11398） 20170208 lj begin

delete  V_QUARTZ_CRON_EXPRESSION where id='20';
delete  V_QUARTZ_CRON_EXPRESSION where id='21';

insert into V_QUARTZ_CRON_EXPRESSION values('20','pubAllIndexTaskTrigger','*/10 * * * * ?',1,'pubAllIndexTask');
insert into V_QUARTZ_CRON_EXPRESSION values('21','publicationIndexTrigger','*/10 * * * * ?',0,'publicationIndexTask');
commit;
--原因（SCM-11398）  20170208 lj end
--原因（SCM-11448,SCM-11447） 2017-02-10 lj end
commit;


--原因（有CQ号带上CQ号） 2017-2-21 lj end--原因（有CQ号带上CQ号） 2017-02-21 ljN end
--原因（    SCM-11506  open系统 新加token与服务编码权限逻辑） 2017-02-22 ajb begin

-- Create table
create table V_OPEN_TOKEN_SERVICE_CONST
(
  ID           NUMBER(18) not null,
  TOKEN        NVARCHAR2(10) not null,
  SERVICE_TYPE NVARCHAR2(10) not null,
  STATUS       NUMBER(1) default 0 not null,
  CREATE_DATE  DATE default sysdate not null,
  DESCR        NVARCHAR2(100)
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 8
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table V_OPEN_TOKEN_SERVICE_CONST
  is 'token权限和service服务编码的常量';
-- Add comments to the columns 
comment on column V_OPEN_TOKEN_SERVICE_CONST.ID
  is 'id号 ，自增';
comment on column V_OPEN_TOKEN_SERVICE_CONST.TOKEN
  is '表示某某系统的权限 ，长度为8位';
comment on column V_OPEN_TOKEN_SERVICE_CONST.SERVICE_TYPE
  is '服务编码，表示系统的具体服务   ， 长度为8位''';
comment on column V_OPEN_TOKEN_SERVICE_CONST.STATUS
  is '默认为0 可用   ， 1不可用';
comment on column V_OPEN_TOKEN_SERVICE_CONST.CREATE_DATE
  is '创建时间。默认为系统当前时间';
comment on column V_OPEN_TOKEN_SERVICE_CONST.DESCR
  is 'oken和服务编码的  描述';
-- Create/Recreate primary, unique and foreign key constraints 
alter table V_OPEN_TOKEN_SERVICE_CONST
  add constraint PK_ID_TOKEN_SERVICE primary key (ID)
  using index 
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
create unique index UNION_IDX_TOKEN_SERVICE on V_OPEN_TOKEN_SERVICE_CONST (TOKEN, SERVICE_TYPE)
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



-- Create sequence 
create sequence SEQ_V_OPEN_TOKEN_SERVICE_ID
minvalue 1
maxvalue 999999999999999999
start with 81
increment by 1
cache 20;


CREATE OR REPLACE TRIGGER TRG_V_OPEN_TOKEN_SERVICE
  BEFORE INSERT ON v_open_token_service_const
  FOR EACH ROW
  
when (new.id is null)
begin
  select SEQ_V_OPEN_TOKEN_SERVICE_ID.nextval into :new.id from dual;
end;


insert into v_open_token_service_const (token ,service_type ) select  t.token ,t.service_type  from  v_open_data_handle_log  t  ;

commit ;



--原因（    SCM-11506  open系统 新加token与服务编码权限逻辑） 2017-02-22 ajb end


--原因（基准库改造,xml文本文件导入任务） 2017-02-28 LJ begin


insert into V_QUARTZ_CRON_EXPRESSION values('25','DbcacheBfetchTaskTrigger','*/30 * * * * ?',1,' 基准库改造,xml文本文件导入任务');
commit;


--原因（基准库改造,xml文本文件导入任务） 2017-02-28 LJ end






--原因（基准库成果改造创建表） 2017-03-03 LJ begin
--  Create table
create table PDWH_PUB_XML
(
  PUB_ID  NUMBER(18) not null,
  XML   VARCHAR2(100 CHAR)
)
tablespace V3SNS
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
  

-- Create sequence 
create sequence SEQ_PDWH_PUB_XML
minvalue 1
maxvalue 9999999999999999999999999999
start with 21
increment by 1
cache 20;


-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_XML
  add constraint PK_PDWH_PUB_XML primary key (PUB_ID)
  using index 
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
  
COMMIT;





--  Create table
create table PDWH_PUB_XML_TOHANDLE
(
  TMP_ID  NUMBER(18) not null,
  TMP_XML   VARCHAR2(100 CHAR),
  INS_ID NUMBER(18),
  STATUS NUMBER(5)
  
)
tablespace V3SNS
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
  

-- Create sequence 
create sequence SEQ_PDWH_PUB_XML_TOHANDLE
minvalue 1
maxvalue 9999999999999999999999999999
start with 21
increment by 1
cache 20;


-- Create/Recreate primary, unique and foreign key constraints 
alter table PDWH_PUB_XML_TOHANDLE
  add constraint PK_PDWH_PUB_XML_TOHANDLE primary key (TMP_ID)
  using index 
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
  
COMMIT;




--原因（基准库成果改造创建表） 2017-03-03 LJ end
--原因（有CQ号带上CQ号） 2016-12-8 WSN end
--创新城拉取成果服务未开通 2017-03-09 WSN begin


insert into V_OPEN_TOKEN_SERVICE_CONST(TOKEN, SERVICE_TYPE, Status, CREATE_DATE, DESCR) values('cba4b03f' ,'jsu32x2s', 0, sysdate,'');
commit;


--创新城拉取成果服务未开通 2017-03-09 WSN begin

--江西系统获取AID服务未开通 2017-03-10 WSN begin

insert into V_OPEN_TOKEN_SERVICE_CONST(token,Service_Type, STATUS, CREATE_DATE, DESCR) values('oxksu3x0', 'ime82dt2', 0, sysdate, '');
commit;

--江西系统获取AID服务未开通 2017-03-10 WSN begin



--原因（SCM-11549） 2017-3-10 lj begin

insert into V_QUARTZ_CRON_EXPRESSION values('26','DbcachePfetchTaskTrigger','*/30 * * * * ?',0,'基准库改造,xml文本文件导入任务从Dbcache_Pfetch获取源数据');
commit;

--原因（SCM-11549） 2017-3-10 lj end
















--原因（基准库改造SCM-11645）2017-3-24 LJ begin


-- Create table
create table pub_pdwh_sns_relation
(
  SNS_PUB_ID           NUMBER(18) not null,
  PDWH_PUB_ID          NUMBER(18) 
  
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 8
    minextents 1
    maxextents unlimited
  );




-- Create/Recreate primary, unique and foreign key constraints 
alter table pub_pdwh_sns_relation
  add constraint PK_pub_pdwh_sns_relationL primary key (SNS_PUB_ID)
  using index 
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
  
  
  -- Add comments to the table 
comment on table pub_pdwh_sns_relation
  is 'SNS成果与基准库成果关系表';
-- Add comments to the columns 
comment on column pub_pdwh_sns_relation.SNS_PUB_ID
  is 'SNS成果id';
comment on column  pub_pdwh_sns_relation.PDWH_PUB_ID 
  is '基准库成果id';

  
  
COMMIT;


--原因（有CQ号带上CQ号） 2017-3-24 LJ end


--原因（基准库改造SCM-11645）2017-3-24 LJ begin


-- Create table
create table pub_pdwh_sns_relation
(
  SNS_PUB_ID           NUMBER(18) not null,
  PDWH_PUB_ID          NUMBER(18) 
  
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 8
    minextents 1
    maxextents unlimited
  );




-- Create/Recreate primary, unique and foreign key constraints 
alter table pub_pdwh_sns_relation
  add constraint PK_pub_pdwh_sns_relationL primary key (SNS_PUB_ID)
  using index 
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
  
  
  -- Add comments to the table 
comment on table pub_pdwh_sns_relation
  is 'SNS成果与基准库成果关系表';
-- Add comments to the columns 
comment on column pub_pdwh_sns_relation.SNS_PUB_ID
  is 'SNS成果id';
comment on column  pub_pdwh_sns_relation.PDWH_PUB_ID 
  is '基准库成果id';

  
  
COMMIT;


--原因（有CQ号带上CQ号） 2017-3-24 LJ end


-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end



--原因（基准库改造SCM-11645）2017-3-24 LJ begin


-- Create table
create table pub_pdwh_sns_relation
(
  SNS_PUB_ID           NUMBER(18) not null,
  PDWH_PUB_ID          NUMBER(18) 
  
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 8
    minextents 1
    maxextents unlimited
  );

commit;


-- Create/Recreate primary, unique and foreign key constraints 
alter table pub_pdwh_sns_relation
  add constraint PK_pub_pdwh_sns_relationL primary key (SNS_PUB_ID)
  using index 
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
 COMMIT; 
  
  -- Add comments to the table 
comment on table pub_pdwh_sns_relation
  is 'SNS成果与基准库成果关系表';
-- Add comments to the columns 
comment on column pub_pdwh_sns_relation.SNS_PUB_ID
  is 'SNS成果id';
comment on column  pub_pdwh_sns_relation.PDWH_PUB_ID 
  is '基准库成果id';

  
  
COMMIT;


--原因（有CQ号带上CQ号） 2017-3-24 LJ end



--原因（基准库改造SCM-11645）2017-3-24 LJ begin
drop table pub_pdwh_sns_relation;
create table pub_pdwh_sns_relation
(
  SNS_PUB_ID           NUMBER(18) not null,
  PDWH_PUB_ID          NUMBER(18) 
  
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 8
    minextents 1
    maxextents unlimited
  );

alter table pub_pdwh_sns_relation
  add constraint PK_pub_pdwh_sns_relationL primary key (SNS_PUB_ID)
  using index 
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
  
comment on table pub_pdwh_sns_relation
  is 'SNS成果与基准库成果关系表';
-- Add comments to the columns 
comment on column pub_pdwh_sns_relation.SNS_PUB_ID
  is 'SNS成果id';
comment on column  pub_pdwh_sns_relation.PDWH_PUB_ID 
  is '基准库成果id';

  
  
COMMIT;
  
  
  
  
--原因（基准库改造SCM-11645）2017-3-24 LJ end  PDWH_PUB_ID          NUMBER(18) 
  
)
tablespace V3SNS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 8
    minextents 1
    maxextents unlimited
  );

alter table pub_pdwh_sns_relation
  add constraint PK_pub_pdwh_sns_relationL primary key (SNS_PUB_ID)
  using index 
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
  
-- Add comments to the columns 
comment on table pub_pdwh_sns_relation
  is 'SNS成果与基准库成果关系表';
comment on column pub_pdwh_sns_relation.SNS_PUB_ID
  is 'SNS成果id';
comment on column  pub_pdwh_sns_relation.PDWH_PUB_ID 
  is '基准库成果id';

  
  
COMMIT;
  

--原因（scm11645） 2017-3-24 LJ end)
tablespace V3SNS
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
comment on table PUB_PDWH_SNS_RELATION
  is 'SNS成果与基准库成果关系表';
-- Add comments to the columns 
comment on column PUB_PDWH_SNS_RELATION.sns_pub_id
  is 'SNS成果id';
comment on column PUB_PDWH_SNS_RELATION.pdwh_pub_id
  is '基准库成果id';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PUB_PDWH_SNS_RELATION
  add constraint PK_PUB_PDWH_SNS_RELATION primary key (SNS_PUB_ID)
  using index 
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
--原因（scm11645） 2017-3-24 LJ end
  
  





commit ;

--原因兴趣群组添加常量    2017-04-07 ajb end


--原因添加接口常量 ajb  2017-04-07 start
insert into v_open_token_service_const (token , service_type ) values ('00000000' ,'unn22ggi' ) ;
insert into v_open_token_service_const (token , service_type ) values ('00000000' ,'unn22gpg' ) ;
insert into v_open_token_service_const (token , service_type ) values ('00000000' ,'unn22ung' ) ;
insert into v_open_token_service_const (token , service_type ) values ('00000000' ,'unn22url' ) ;
insert into v_open_token_service_const (token , service_type ) values ('00000000' ,'xin329xz' ) ;
insert into v_open_token_service_const (token , service_type ) values ('00000000' ,'sisjuxy2' ) ;
insert into v_open_token_service_const (token , service_type ) values ('00000000' ,'kxjwui3s' ) ;

commit ;
--原因原因添加接口常量     2017-04-07 ajb end

--原因（WS接口测试任务） 2017-4-11 lj begin

insert into V_QUARTZ_CRON_EXPRESSION values('30','InterfaceTestTaskTrigger','*/5 * * * * ?',0,'WS接口访问测试任务');

commit;

--原因（有CQ号带上CQ号）2017-4-11 lj end


-----------------主页改造  2017-3-22 WSN begin


create table SCIENCEAREA_IDENTIFICATION
(
  id        NUMBER not null,
  psn_id    NUMBER not null,
  science_area_id  NUMBER not null,
  friend_id NUMBER not null,
  op_date   DATE default sysdate
);
-- Add comments to the table 
comment on table SCIENCEAREA_IDENTIFICATION
  is '科技领域关键词认同';
-- Add comments to the columns 
comment on column SCIENCEAREA_IDENTIFICATION.id
  is '主键';
comment on column SCIENCEAREA_IDENTIFICATION.psn_id
  is '人员ID';
comment on column SCIENCEAREA_IDENTIFICATION.science_area_id
  is 'psn_science_area 主键';
comment on column SCIENCEAREA_IDENTIFICATION.friend_id
  is '好友ID';
comment on column SCIENCEAREA_IDENTIFICATION.op_date
  is '认同时间';
 
create index IDX_SI_OPDATE on SCIENCEAREA_IDENTIFICATION (OP_DATE);

alter table SCIENCEAREA_IDENTIFICATION
  add constraint PK_SI_ID primary key (ID);

create sequence SEQ_SCIENCEAREA_IDENTIFICATION
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20;


-----------------------------------新建psn_science_area表------------------------------------------------

create table PSN_SCIENCE_AREA
(
  id           NUMBER(18) not null,
  science_area_id       NUMBER(5),
  science_area    VARCHAR2(200 CHAR) not null,
  psn_id       NUMBER(18),
  status       NUMBER(1) default 0
);


comment on table PSN_SCIENCE_AREA
  is '人员科技领域';
-- Add comments to the columns 
comment on column PSN_SCIENCE_AREA.id
  is '主键';
comment on column PSN_SCIENCE_AREA.science_area_id
  is '对应的关键字表主键（category_map_base），可为空';
comment on column PSN_SCIENCE_AREA.science_area
  is '关键字';
comment on column PSN_SCIENCE_AREA.psn_id
  is '人员编码';
comment on column PSN_SCIENCE_AREA.status
  is '是否有效';
  
create index IDX_PSN_SCIENCE_AREA_PSNID on PSN_SCIENCE_AREA (PSN_ID, science_area);

alter table PSN_SCIENCE_AREA
  add constraint PK_PSN_SCIENCE_AREA primary key (ID);

alter table psn_science_area add identification_sum NUMBER(6) default 0;

comment on column PSN_SCIENCE_AREA.identification_sum
  is '认同数';

alter table psn_science_area modify science_area_id NUMBER(5) ;


create sequence SEQ_PSN_SCIENCE_AREA
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20;



----------------------------------新建v_represent_pub表--------------------------------------------------
create table V_REPRESENT_PUB(
       pub_id              NUMBER(18) not null,
       psn_id        NUMBER(18) not null,
       seq_no        NUMBER(2) default 0 not null ,
       status        NUMBER(1) default 0 not null
);

comment on table V_REPRESENT_PUB is '人员代表性成果';

comment on column V_REPRESENT_PUB.pub_id is '成果ID';

comment on column V_REPRESENT_PUB.psn_id is '人员ID';

comment on column V_REPRESENT_PUB.seq_no is '序号，排序用';

comment on column V_REPRESENT_PUB.status is '状态， 0：有效， 1：无效';

alter table V_REPRESENT_PUB
  add constraint PK_V_REPRESENT_PUB primary key (PUB_ID, PSN_ID);
  
create index REPRESENTPUB_PSNID on V_REPRESENT_PUB (PSN_ID);

create index REPRESENTPUB_PUBID on V_REPRESENT_PUB (PUB_ID);


--------------------------------新建v_represent_prj表--------------------------------------------------

create table V_REPRESENT_PRJ(
       prj_id              NUMBER(18) not null,
       psn_id        NUMBER(18) not null,
       seq_no        NUMBER(2) default 0 not null ,
       status        NUMBER(1) default 0 not null
);

comment on table V_REPRESENT_PRJ is '人员代表性项目';

comment on column V_REPRESENT_PRJ.prj_id is '项目ID';

comment on column V_REPRESENT_PRJ.psn_id is '人员ID';

comment on column V_REPRESENT_PRJ.seq_no is '序号，排序用';

comment on column V_REPRESENT_PRJ.status is '状态， 0：有效， 1：无效';

alter table V_REPRESENT_PRJ
  add constraint PK_V_REPRESENT_PRJ primary key (PRJ_ID, PSN_ID);
  
create index REPRESENTPRJ_PSNID on V_REPRESENT_PRJ (PSN_ID);

create index REPRESENTPRJ_PRJID on V_REPRESENT_PRJ (PRJ_ID);


-----------------------------学科表------------------------------------------------------

create table CATEGORY_MAP_BASE
(
  categry_id    NUMBER(5),
  category_zh   VARCHAR2(150 CHAR),
  category_en   VARCHAR2(150 CHAR),
  igi_zh        VARCHAR2(1000 CHAR),
  igi_en        VARCHAR2(1000 CHAR),
  moe           VARCHAR2(1000 CHAR),
  moe_sub       VARCHAR2(1000 CHAR),
  nsfc_category VARCHAR2(1000 CHAR),
  wos_category  VARCHAR2(1000 CHAR)
);

comment on table CATEGORY_MAP_BASE
  is '存放分类对应数据，fzq根据dora提供的smate,nsfc,wos科研分类对应创建';

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (702, '临床医学', 'Clinical Medicine', null, null, '320 临床医学', '"32011临床诊断学32014保健医学32017理疗学32021麻醉学32024内科学32027外科学32031妇产科学32034儿科学32037眼科学32041耳鼻咽喉科学32044口腔医学32047皮肤病学32051性医学32054神经病学32057精神病学32058重症医学32061急诊医学32064核医学32065全科医学32067肿瘤学32071护理学32099临床医学其他学科"', 'H01 呼吸系统/H02 循环系统/H03 消化系统/H04 生殖系统/围生医学/新生儿/H05 泌尿系统/H06 运动系统/H07 内分泌系统/代谢和营养支持/H08 血液系统/H09 神经系统和精神疾病/H11 皮肤及其附属器/H12 眼科学/H13 耳鼻咽喉头颈科学/H14 口腔颅颌面科学/H15 急重症医学/创伤/烧伤/整形/H16 肿瘤学/H17 康复医学/H20 检验医学/H23 法医学', 'Allergy 过敏/Andrology 男科/Anesthesiology 麻醉/Audiology & Speech-Language Pathology 听力学及言语 - 语言病理学/Cardiac & Cardiovascular Systems 心脏及心血管系统/Clinical Neurology 临床神经病学/Critical Care Medicine 危重病急救医学/Dentistry, Oral Surgery & Medicine 牙科，口腔外科与医学/Dermatology  皮肤科/Emergency Medicine 急救医学/Hematology 血液学/Endocrinology & Metabolism 内分泌与代谢/Neurosciences 神经科学/Obstetrics & Gynecology 妇产科/Oncology 肿瘤学/Ophthalmology 眼科/Orthopedics 骨科/Endocrinology & Metabolism 内分泌与代谢/Otorhinolaryngology 耳鼻咽喉科/Pediatrics 儿科/Peripheral Vascular Disease 周围血管疾病/Psychiatry 精神病学/Radiology, Nuclear Medicine & Medical Imaging 放射学，核医学和医学成像/Rehabilitation 康复/Respiratory System 呼吸系统/Rheumatology 风湿病/Surgery 手术/Urology & Nephrology 泌尿外科和肾脏病学/Gastroenterology & Hepatology 肠胃病学和肝脏/Nursing 护理/Primary Health Care 初级卫生保健/Transplantation 移植');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (703, '预防医学与公共卫生学', 'Preventive Medicine and Public Health', null, null, '330 预防医学与公共卫生学', '"33011营养学33014毒理学33017消毒学33021流行病学33027媒介生物控制学33031环境医学33034职业病学33037地方病学33035热带医学33041社会医学33044卫生检验学33047食品卫生学33051儿少与学校卫生学33054妇幼卫生学33057环境卫生学33061劳动卫生学33064放射卫生学33067卫生工程学33071卫生经济学33072卫生统计学33074优生学33077健康促进与健康教育学33081卫生管理学33099预防医学与公共卫生学其他学科"', 'H24 地方病学/职业病学/H25 老年医学/H26 预防医学', 'Infectious Diseases 传染性疾病/Nutrition & Dietetics 营养与营养学/Toxicology 毒理学/Public, Environmental & Occupational Health 公众，环境和职业健康/Substance Abuse 药物滥用/Health Care Sciences & Services 医疗保健科学与服务/Health Policy & Services 卫生政策与服务/Geriatrics & Gerontology 老年医学和老年学/Gerontology 老年学/Medical Ethics 医学伦理/Medicine, Legal 医学，法律/Tropical Medicine 热带医学/Women''s Studies 女性研究');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (704, '军事医学与特种医学', 'Military Medicine and Specialty Medicine', null, null, '340 军事医学与特种医学', '"34010军事医学34020特种医学34099军事医学与特种医学其他学科"', 'H21 特种医学', null);

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (705, '药学', 'Pharmacy', null, null, '350 药学', '"35010药物化学35020生物药物学35025微生物药物学35030放射性药物学35035药剂学35040药效学35045药物管理学35050药物统计学35099药学其他学科"', 'H30 药物学/H31 药理学', 'Pharmacology & Pharmacy 药理学和药学');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (706, '中医学与中药学', 'Medicine and Pharmacy', null, null, '360 中医学与中药学', '"36010中医学36020民族医学36030中西医结合医学36040中药学36099中医学与中药学其他学科"', 'H27 中医学/H28 中药学/H29 中西医结合', 'Integrative & Complementary Medicine 中西医结合与补充医学');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (1, '农业科学', 'Agriculture', null, null, null, null, null, null);

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (101, '农学', 'Agronomy', null, null, '210 农学', '"21010农业史21020农业基础学科21030农艺学21040园艺学21045农产品贮藏与加工21050土壤学21060植物保护学21099农学其他学科"', 'C13 农学基础与作物学/C14 植物保护学/C15 园艺学与植物营养学', 'Agricultural Economics & Policy 农业经济与政策/Agricultural Engineering 农业工程/Agriculture, Multidisciplinary 农业，多学科/Agronomy 农学/Horticulture 园艺/Soil Science 土壤学/Agriculture, Dairy & Animal Science 农业，奶制品和动物科学');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (102, '林学', 'Forestry', null, null, '220 林学', '"22010林业基础学科22015林木遗传育种学22020森林培育学22025森林经理学22030森林保护学22035野生动物保护与管理22040防护林学22045经济林学22050园林学22055林业工程22060森林统计学22065林业经济学22099林学其他学科"', 'C16 林学', 'Forestry 林业');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (103, '畜牧、兽医科学', 'Animal Husbandry and Veterinary Sciences', null, null, '230 畜牧、兽医科学', '"23010畜牧、兽医科学基础学科23020畜牧学23030兽医学23099畜牧、兽医科学其他学科"', 'C17 畜牧学与草地科学/C18 兽医学 ', 'Veterinary Sciences 兽医学');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (104, '水产学', 'Fishery', null, null, '240 水产学', '"24010水产学基础学科24015水产增殖学24020水产养殖学24025水产饲料学24030水产保护学24035捕捞学24040水产品贮藏与加工24045水产工程学24050水产资源学24055水产经济学24099水产学其他学科"', 'C19 水产学', 'Fisheries 渔业/Marine & Freshwater Biology 海洋与淡水生物学 ');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (105, '食品科学技术', 'Food Science and Technology', null, null, '550 食品科学技术', '"55010食品科学技术基础学科55020食品加工技术55030食品包装与储藏55040食品机械55050食品加工的副产品加工与利用55060食品工业企业管理学55070食品工程与粮油工程55099食品科学技术其他学科"', 'C20 食品科学', 'Food Science & Technology 食品科技');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (2, '理学', 'Science', null, null, null, null, null, null);

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (201, '数学', 'Mathematics', 'Chemical Engineering/Geoinformatics/Bioinformatics/Biologically Inspired Computing/Biomedical Technologies/Biometrics/Chemo Informatics', '化学工程/地理信息/生物信息学/生物启发计算/生物科技/生物特征识别/化疗情报', '110 数学', '"11011数学史11014数理逻辑与数学基础11017数论11021代数学11024代数几何学11027几何学11031拓扑学11034数学分析11037非标准分析11041函数论11044常微分方程11047偏微分方程11051动力系统11054积分方程11057泛函分析11061计算数学11064概率论11067数理统计学11071应用统计数学11074运筹学11077组合数学11081离散数学11084模糊数学11085计算机数学11087应用数学11099数学其他学科"', 'A01 数学', 'Mathematics 数学/Mathematics, Applied 数学，应用/Mathematics, Interdisciplinary Applications 数学，跨学科的应用/Logic 逻辑');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (202, '力学', 'Mechanics', null, null, '130 力学', '"13010基础力学13015固体力学13020振动与波13025流体力学13030流变学13035爆炸力学13040物理力学13041生物力学13045统计力学13050应用力学13099力学其他学科"', 'A02 力学', 'Mechanics 力学');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (203, '物理学', 'Physics', null, null, '140 物理学', '"14010物理学史14015理论物理学14020声学14025热学14030光学14035电磁学14040无线电物理14045电子物理学14050凝聚态物理学14055等离子体物理学14060原子分子物理学14065原子核物理学14070高能物理学14075计算物理学14080应用物理学14099物理学其他学科"', 'A04 物理学I/A05 物理学II//F05 光学和光电子学', 'Optics 光学/Physics, Applied 物理，应用/Physics, Atomic, Molecular & Chemical 物理，原子，分子和化学/Physics, Condensed Matter 物理，凝聚态/Physics, Fluids & Plasmas 物理，流体和等离子体/Physics, Mathematical 物理，数学/Physics, Multidisciplinary 物理，多学科/Physics, Nuclear 物理，核/Physics, Particles & Fields 物理，粒子和场/Spectroscopy 光谱学/Thermodynamics 热力学/PHYSICS 物理/Acoustics 声学/Imaging Science & Photographic Technology 成像科学与照相技术/Microscopy 显微镜/Nuclear Science & Technology 核科学与技术');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (204, '化学', 'Chemistry', null, null, '150 化学', '"15010化学史15015无机化学15020有机化学15025分析化学15030物理化学15035化学物理学15040高分子物理15045高分子化学15050核化学15055应用化学15060化学生物学15065材料化学15099化学其他学科"', 'B01 无机化学/B02 有机化学/B03 物理化学/B04 高分子科学/B05 分析化学/B06 化学工程及工业化学', 'Chemistry, Analytical 化学，分析/Chemistry, Applied 化学，应用/Chemistry, Inorganic & Nuclear 化学，无机与核/Chemistry, Medicinal 化学，医药/Chemistry, Multidisciplinary 化学，多学科/Chemistry, Organic 化学，有机/Chemistry, Physical 化学，物理/Electrochemistry 电化学/Crystallography 晶体/Engineering, Chemical 工程，化学');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (205, '天文学', 'Astronomy', null, null, '160 天文学', '"16010天文学史16015天体力学16020天体物理学16025宇宙化学16030天体测量学16035射电天文学16040空间天文学16045天体演化学16050星系与宇宙学16055恒星与银河系16060太阳与太阳系16065天体生物学16070天文地球动力学16075时间测量学16099天文学其他学科"', 'A03 天文学', 'Astronomy & Astrophysics 天文学和天体物理学');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (206, '地球科学', 'Earth Sciences', null, null, '170 地球科学', '"17010地球科学史17015大气科学17020固体地球物理学17025空间物理学17030地球化学17035大地测量学17040地图学17045地理学17050地质学17055水文学17060海洋科学17099地球科学其他学科"', 'D01 地理学/D02 地质学/D03 地球化学/D04 地球物理学和空间物理学/D05 大气科学/D06 海洋科学', 'Engineering, Geological 工程，地质/Engineering, Marine 工程，海洋/Engineering, Ocean 工程，海洋/Geochemistry & Geophysics 地球化学与地球物理/Geography 地理/Geography, Physical 地理，物理/Geology 地质学/Geosciences, Multidisciplinary 地球科学，多学科/Meteorology & Atmospheric Sciences 气象学和大气科学/Water Resources 水资源/Limnology 湖沼学/Oceanography 海洋学/Remote Sensing 遥感');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (207, '生物学', 'Biology', null, null, '180 生物学', '"18011生物数学18014生物物理学18017生物化学18021细胞生物学18022免疫学18024生理学18027发育生物学18031遗传学18034放射生物学18037分子生物学18039专题生物学研究18041生物进化论18044生态学18047神经生物学18051植物学18054昆虫学18057动物学18061微生物学18064病毒学18067人类学18099生物学其他学科"', 'C01 微生物学/C02 植物学/C03 生态学/C04 动物学/C05 生物物理、生物化学与分子生物学/C06 遗传学与生物信息学/C07 细胞生物学/C08 免疫学/C10 生物力学与组织工程学/C11 生理学与整合生物学/C12 发育生物学与生殖生物学', 'Biochemical Research Methods 生化研究方法/Biochemistry & Molecular Biology 生物化学与分子生物学/Biodiversity Conservation 生物多样性保护/Biology 生物学/Biophysics 生物物理学/Biotechnology & Applied Microbiology 生物技术和应用微生物学/Cell & Tissue Engineering 细胞与组织工程/Cell Biology 细胞生物学/Developmental Biology 发育生物学/Ecology 生态/Entomology 昆虫学/Evolutionary Biology 进化生物学/Genetics & Heredity 遗传学与遗传/Microbiology 微生物学/Mycology 真菌学/Ornithology 鸟类/Paleontology 古生物学/Plant Sciences 植物科学/Polymer Science 高分子科学/Reproductive Biology 生殖生物学/Virology 病毒学/Mathematical & Computational Biology 数学与计算生物学/Parasitology 寄生物学/Physiology 生理/Zoology 动物学');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (208, '心理学', 'Psychology', null, null, '190 心理学', '"19010心理学史19015认知心理学19020社会心理学19025实验心理学19030发展心理学19040医学心理学19041人格心理学19042临床与咨询心理学19045心理测量19046心理统计19050生理心理学19055工业心理学19060管理心理学19065应用心理学19070教育心理学19075法制心理学19099心理学其他学科"', 'C09 神经科学、认知科学与心理学', 'Psychology 心理学/Psychology, Applied 心理学，应用/Psychology, Biological 心理学，生物学/Psychology, Developmental 心理学，发展/Psychology, Educational 心理学，教育/Psychology, Experimental 心理学，实验/Psychology, Mathematical 心理学，数学/Psychology, Multidisciplinary 心理学，多学科/Psychology, Social 心理学，社会/Psychology, Clinical 心理学，临床/Behavioral Sciences 行为科学/Psychology, Psychoanalysis 心理学，心理分析');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (3, '人文社科', 'Humanities and Social Sciences', null, null, null, null, null, null);

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (301, '语言学', 'Linguistics', 'Law and Policy/Archaeology and Anthropology/Computer Graphics and Art/Culture and Population Studies/Cyber Behavior/Political Science/Religious Studies/Sociology/Cyber Warfare and Terrorism/Information Ethics/Adult Learning/Applied E-Learning/Blending and Mobile Learning/Curriculum Development and Instructional Design/Digital Literacy/Distance Education/Educational Administration and Leadership/Higher Education/K-12 Education/Learning Assessment and Measurement/Research Methods/Special Education/Teacher Education/Sustainable Development/Urban and Regional Development', '法律和政策/考古学和人类学/计算机图形学和艺术/文化与人口学院/网络性能/政治学/宗教学/社会学/网络战和反恐/信息伦理/成人教育/应用电子学习/混合和移动学习/课程开发和教学设计/数字素养/远程教育/教育管理和领导力/高等教育/ K-12教育/学习评价与测量/研究方法/特殊教育/师范教育/可持续发展/城市与区域发展', '740 语言学', '"74010普通语言学74015比较语言学74020语言地理学74025社会语言学74030心理语言学74035应用语言学74040汉语研究74045中国少数民族语言文字74050外国语言74099语言学其他学科"', null, 'Linguistics 语言学');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (302, '法学', 'Law', null, null, '820 法学', '"82010理论法学82020法律史学82030部门法学82040国际法学82099法学其他学科"', null, 'Law 法律/Criminology & Penology 犯罪与刑罚学');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (303, '社会学', 'Sociology', null, null, '840 社会学', '"84011社会学史84014社会学理论84017社会学方法84021实验社会学84024数理社会学84027应用社会学84031比较社会学84034社会地理学84037文化社会学84041历史社会学84044经济社会学84047军事社会学84054公共关系学84057社会人类学84061组织社会学84064发展社会学84067福利社会学84071人口学84074劳动科学84099社会学其他学科"', 'G0306 公共管理与公共政策／G0310 公共安全与危机管理／G0313 区域发展管理', 'History of Social Sciences 社会科学史/Industrial Relations & Labor 劳资关系和劳工/Social Sciences, Interdisciplinary 社会科学，跨学科/Social Sciences, Mathematical Methods 社会科学，数学方法/Social Sciences, Biomedical 社会科学，生物医学/Social Work 社会工作/Sociology 社会学/Demography 人口/Ethics 伦理/Ethnic Studies 种族研究/Planning & Development 规划发展/Social Issues 社会问题');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (304, '新闻学与传播学', 'Journalism and Communication', null, null, '860 新闻学与传播学', '"860新闻学与传播学86010新闻理论86020新闻史86030新闻业务86040新闻事业经营管理86050广播与电视86060传播学86099新闻学与传播学其他学科"', null, 'Communication 通讯');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (305, '教育学', 'Education', null, null, '880 教育学', '"88011教育史88014教育学原理88017教学论88021德育原理88024教育社会学88031教育经济学88034教育管理学88037比较教育学88041教育技术学88044军事教育学88047学前教育学88051普通教育学88054高等教育学88057成人教育学88061职业技术教育学88064特殊教育学88099教育学其他学科"', 'G0309 教育管理与政策', 'Education, Scientific Disciplines 教育，科学学科/Education & Educational Research 教育与教育研究/Education, Special 教育，特别');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (306, '体育科学', 'Sports Science', null, null, '890 体育科学', '"89010体育史89015体育理论89020运动生物力学89025运动生理学89030运动心理学89035运动生物化学89040体育保健学89045运动训练学89050体育教育学89055武术理论与方法89060体育管理学89065体育经济学89099体育科学其他学科"', null, 'Sport Sciences 体育科学');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (4, '经济与管理', 'Economics and Management', null, null, null, null, null, null);

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (401, '管理学', 'Management', 'Accounting and Finance/Business Education/Business Information Systems/Business Intelligence/Enterprise Information Systems/Business and Organizational Research/Business Policy/Business Process Management/Crisis Response and Management/E-commence/Economics and Economic Theory/Electronic Government/Electronic Services/Enterprise Resource Planning/Entrepreneruship and E-innovation/Global Business/Hospitality, Travel, and Tourism/Human Resources Development/Marketing/Operations and Service Management/Public and Sector Management/Risk Assessment/Small and Medium Enterprises/Supply Chain Management/Gaming/Outsourcing/Data Analysis and Statistics/IT Governance/Knowledge Management', '会计与金融/商业教育/商务信息系统/商业智能/企业信息系统/企业与组织研究/业务政策/业务流程管理/危机响应与管理/ 电子商务/经济学和经济理论/电子政务/电子服务/企业资源规划/ 创业和电子创新/全球商业/酒店，旅游，观光/人力资源开发/市场学/运营和服务管理/公共及部门管理/风险评估/中小型企业/供应链管理/博弈/ 外包 /数据分析和统计/ IT治理/知识管理', '630 管理学', '"63010管理思想史63015管理理论63025管理计量学63030部门经济管理63032区域经济管理63035科学学与科技管理63040企业管理63044公共管理63050管理工程63055人力资源开发与管理63060未来学63099管理学其他学科"', 'G01 管理科学与工程/G03 宏观管理与政策', 'Management 管理/Hospitality, Leisure, Sport & Tourism 酒店，休闲，运动与旅游');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (402, '经济学', 'Economics', null, null, '790 经济学', '"79011政治经济学79013宏观经济学79015微观经济学79017比较经济学79019经济地理学79021发展经济学79023生产力经济学79025经济思想史79027经济史79029世界经济学79031国民经济学79033管理经济学79035数量经济学79037会计学79039审计学79041技术经济学79043生态经济学79045劳动经济学79047城市经济学79049资源经济学79051环境经济学79052可持续发展经济学79053物流经济学79055工业经济学79057农村经济学79059农业经济学79061交通运输经济学79063商业经济学79065价格学79067旅游经济学79069信息经济学79071财政学79073金融学79075保险学79077国防经济学79099经济学其他学科"', 'G02 工商管理', 'Business 商业/Economics 经济学/Urban Studies 城市发展研究/Planning & Development 规划发展');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (403, '统计学', 'Statistics', null, null, '910 统计学', '"91010统计学史91030经济统计学91035科学技术统计学91040社会统计学91045人口统计学91050环境与生态统计学91060生物与医学统计学91099统计学其他学科"', null, 'Statistics & Probability 统计与概率');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (404, '金融学', 'Finance', null, null, null, null, 'G0115 金融工程/G0302 金融管理与政策 ', 'Business, Finance 商业，金融');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (405, '市场营销 ', 'Marketing ', null, null, null, null, 'G0208 市场营销', 'Business 商业');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (406, '管理信息系统', 'Management Information Systems', null, null, null, null, 'G0112 信息系统与管理/G0211 企业信息管理', 'Computer Science, Information Systems 计算机科学，信息系统/Management 管理');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (407, '运筹学与管理科学 ', 'Operations Research and Management Science', null, null, null, null, 'G0103 运筹与管理', 'Operations Research & Management Science 运筹与管理科学');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (408, '公共管理', 'Public Administration', null, null, null, null, 'G0306 公共管理与公共政策/G0311 劳动就业与社会保障', 'Public Administration 公共管理');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (409, '会计学', 'Accounting', null, null, null, 'G0206 公司理财与财务管理/G0207 会计与审计', 'G0207 会计与审计', 'Business, Finance 商业，金融');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (5, '工学', 'Engineering', null, null, null, null, null, null);

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (501, '材料科学', 'Material Science', 'Aerospace Engineering/Electrical Engineering/Engineering Education/Environmental Engineering/Mechanical Engineering', '航空航天工程/电子工程/工程教育/环境工程/机械工程', '430 材料科学', '"43010材料科学基础学科43015材料表面与界面43020材料失效与保护43025材料检测与分析技术43030材料实验43035材料合成与加工工艺43040金属材料43045无机非金属材料43050有机高分子材料43055复合材料43060生物材料43070纳米材料43099材料科学其他学科"', 'E01 金属材料/E02 无机非金属材料/E03 有机高分子材料', 'Materials Science, Biomaterials 材料科学，生物材料/Materials Science, Ceramics 材料科学，陶瓷/Materials Science, Characterization & Testing 材料科学，表征和测试/Materials Science, Coatings & Films 材料科学，涂料和薄膜/Materials Science, Composites 材料科学，复合材料/Materials Science, Multidisciplinary 材料科学，多学科/Materials Science, Paper & Wood 材料科学，纸张和木材/Nanoscience & Nanotechnology 纳米科技');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (502, '矿山&冶金', 'Mining and Metallurgy', null, null, '440&450 矿山&冶金', '"44010矿山地质学44015矿山测量44020矿山设计44025矿山地面工程44030井巷工程44035采矿工程44040选矿工程44045钻井工程44050油气田井开发工程44055石油、天然气储存与运输工程44060矿山机械工程44065矿山电气工程44070采矿环境工程44075矿山安全44080矿山综合利用工程44099矿山工程技术其他学科45010冶金物理化学45015冶金反应工程45020冶金原料与预处理45025冶金热能工程45030冶金技术45035钢铁冶金45040有色金属冶金45045轧制45050冶金机械及自动化45099冶金工程技术其他学科"', 'E04 冶金与矿业/B0610 化工冶金', 'Engineering, Petroleum 工程，石油/Mineralogy 矿物学/Mining & Mineral Processing 采矿与矿物加工/Metallurgy & Metallurgical Engineering 冶金和冶金工程');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (503, '机械工程', 'Mechanical Engineering', null, null, '460 机械工程', '"46010机械史46015机械学46020机械设计46025机械制造工艺与设备46030刀具技术46035机床技术46045流体传动与控制46050机械制造自动化46099机械工程其他学科"', 'E05 机械工程', 'Engineering, Mechanical 工程，机械');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (504, '动力与电气工程', 'Power and Electrical Engineering', null, null, '470 动力与电气工程', '"47010工程热物理47020热工学47030动力机械工程47035制冷与低温工程47040电气工程47099动力与电气工程其他学科"', 'E06 工程热物理与能源利用/E07 电气科学与工程', 'Engineering, Electrical & Electronic 工程，电气与电子');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (505, '能源科学技术', 'Energy Science and Technology', null, null, '480 能源科学技术', '"48010能源化学48020能源地理学48030能源计算与测量48040储能技术48050节能技术48060一次能源48070二次能源48080能源系统工程48099能源科学技术其他学科"', 'E06 工程热物理与能源利用/B0609 能源化工', 'Energy & Fuels 能源与燃料');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (506, '纺织科学技术', 'Textile Science and Technology', null, null, '540 纺织科学技术', '"54010纺织科学技术基础学科54020纺织材料54030纤维制造技术54040纺织技术54050染整技术54060服装技术54070纺织机械与设备54099纺织科学技术其他学科"', null, 'Materials Science, Textiles 材料科学，纺织');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (507, '土木建筑工程', 'Civil Engineering', null, null, '560 土木建筑工程', '"56010建筑史56015土木建筑工程基础学科56020土木建筑工程测量56025建筑材料56030工程结构56035土木建筑结构56040土木建筑工程设计56045土木建筑工程施工56050土木工程机械与设备56055市政工程56060建筑经济学56099土木建筑工程其他学科"', 'E08 建筑环境与结构工程', 'Construction & Building Technology 建筑与建筑技术/Engineering, Civil 工程，土木工程');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (508, '水利工程', 'Hydraulic Engineering', null, null, '570 水利工程', '"57010水利工程基础学科57015水利工程测量57020水工材料57025水工结构57030水力机械57035水利工程施工57040水处理57045河流泥沙工程学57055环境水利57060水利管理57065防洪工程57070水利经济学57099水利工程其他学科"', 'E09 水利科学与海洋工程', null);

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (509, '交通运输工程', 'Transportation Engineering', null, null, '580 交通运输工程', '"58010道路工程58020公路运输58030铁路运输58040水路运输58050船舶、舰船工程58060航空运输58070交通运输系统工程58080交通运输安全工程58099交通运输工程其他学科"', 'E0807 交通工程', 'ENGINEERING, MARINE工程，船舶/Transportation Science & Technology 交通科技/Transportation 运输');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (510, '航空、航天科学技术', 'Aerospace Science and Technology', null, null, '590 航空、航天科学技术', '"59010航空、航天科学技术基础学科59015航空器结构与设计59020航天器结构与设计59025航空、航天推进系统59030飞行器仪表、设备59035飞行器控制、导航技术59040航空、航天材料59045飞行器制造技术59050飞行器试验技术59055飞行器发射与回收、飞行技术59060航空航天地面设施、技术保障59065航空、航天系统工程59099航空、航天科学技术其他学科"', 'D04 地球物理学和空间物理学', 'Engineering, Aerospace 工程，航空航天');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (511, '环境科学技术及资源科学技术', 'Environment, Resource Science and Technology ', null, null, '610 环境科学技术及资源科学技术', '"61010环境科学技术基础学科61020环境学61030环境工程学61050资源科学技术61099环境科学技术及资源科学技术其他学科"', 'B07 环境化学/B0611 环境化工/B0612 资源化工', 'Engineering, Environmental 工程，环境/Environmental Sciences 环境科学/Environmental Studies 环境研究/LIMNOLOGY 湖沼 ');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (6, '信息科学', 'Information Science', null, null, null, null, null, null);

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (601, '电子、通信与自动控制技术', 'Electronics, Communications and Automatic Control Technology', 'End User Computing/Human Aspects of Technology/Human Computer Interaction/Interactive Technologies/Mobile Computing/Multimedia Technologies/Music Technologies/Networking/Portal Technologies/Semantic Web/Social Computing/Telecommunications/Web Engineering/Web Services/Wireless Systems/Algorithms/Artifical Intelligence/Global Information Systems/Grid and High Performance Computing/Information Security and Privacy/IT Research and Theory/Network Security/Open Source Software/Electronic Services/Systems and Software Engineering/Data Mining and Databases/Digital Libraries/Information Resources Management/Information Retrieval/Information Value and Quality/Instructional Design/Knowledge ', '终端用户计算/人性化科技/人机交互/互动科技/移动计算/多媒体技术/音乐技术/网络/门户技术/语义网络/社交计算/电信/网络工程/ Web服务/无线系统/算法/人工智能/全球信息系统/网格和高性能计算/信息安全和隐私/ IT研究和理论/网络安全/开源软件/电子服务/系统和软件工程/数据挖掘和数据库/数字图书馆/信息资源管理/信息检索/信息的价值和质量/教学设计/知识', '510 电子、通信与自动控制技术', '"51010电子技术51020光电子学与激光技术51030半导体技术51040信息处理技术51050通信技术51060广播与电视工程技术51070雷达工程51099电子与通信技术其他学科"', 'F01 电子学与信息系统/F03 自动化/F04 半导体科学与信息器件/F05 光学和光电子学', 'Automation & Control Systems 自动化与控制系统/Telecommunications 电信');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (602, '信息科学与系统科学', 'Information Science and Systems Science', null, null, '120 信息科学与系统科学', '"12010信息科学与系统科学基础学科12020系统学12030控制理论12040系统评估与可行性分析12050系统工程方法论12099信息科学与系统科学其他学科"', 'F01 电子学与信息系统', null);

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (603, '计算机科学技术', 'Computer Science and Technology', null, null, '520 计算机科学技术', '"52010计算机科学技术基础学科52020人工智能52030计算机系统结构52040计算机软件52050计算机工程52060计算机应用52099计算机科学技术其他学科"', 'F02 计算机科学/F03 自动化', 'Computer Science, Artificial Intelligence 计算机科学，人工智能/Computer Science, Cybernetics 计算机科学，控制论/Computer Science, Hardware & Architecture 计算机科学，硬件和架构/Computer Science, Information Systems 计算机科学，信息系统/Computer Science, Interdisciplinary Applications 计算机科学，跨学科的应用/Computer Science, Software Engineering 计算机科学，软件工程/Computer Science, Theory & Methods 计算机科学，理论与方法/Robotics 机器人');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (604, '图书馆、情报与文献学', 'Library, Information and Documentation Science', null, null, '870 图书馆、情报与文献学', '"87010图书馆学87020文献学87030情报学87040档案学87050博物馆学87099图书馆、情报与文献学其他学科"', 'G0314 信息资源管理', 'Information Science & Library Science 信息科学与图书馆学');

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (7, '医药科学', 'Pharmaceutical Sciences', null, null, null, null, null, null);

insert into category_map_base (CATEGRY_ID, CATEGORY_ZH, CATEGORY_EN, IGI_ZH, IGI_EN, MOE, MOE_SUB, NSFC_CATEGORY, WOS_CATEGORY)
values (701, '基础医学', 'Basic Medicine', 'Clinical Science and Technologies/E-Health/Healthcare Administration/Healthcare Information Technologies/Medical Engineering/Pharmacology/Telemedicine', '临床科学与技术/电子保健/医疗管理/医疗保健信息技术/医疗技术/药理学/远程医疗', '310 基础医学', '"31010医学史31011医学生物化学31014人体解剖学31017医学细胞生物学31021人体生理学31024人体组织胚胎学31027医学遗传学31031放射医学31034人体免疫学31037医学寄生虫学31041医学微生物学31044病理学31047药理学31051医学实验动物学31057医学统计学31099基础医学其他学科"', 'H10 医学免疫学/H18 影像医学与生物医学工程/H19 医学病原微生物与感染/H22 放射医学', 'Anatomy & Morphology 解剖和形态/Engineering, Biomedical 工程，生物医学/Immunology 免疫学/Medical Informatics 医学信息/Medical Laboratory Technology 医学检验技术/Medicine, General & Internal 医药，通用及内部/Neuroimaging 神经影像学/Pathology 病理/Medicine, Research & Experimental 医学研究与实验');

 alter table category_map_base add SUPER_CATEGORY_ID NUMBER(5) DEFAULT 0; 
 
 update category_map_base t set t.super_category_id = 7 where t.categry_id in (701,702,703,704,705,706);
 
  update category_map_base t set t.super_category_id = 6 where t.categry_id in (601,602,603,604);
  
   update category_map_base t set t.super_category_id = 5 where t.categry_id in (501,502,503,504,505,506,507,508,509,510,511);
   
    update category_map_base t set t.super_category_id = 4 where t.categry_id in (401,402,403,404,405,406,407,408,409);
    
     update category_map_base t set t.super_category_id = 3 where t.categry_id in (301,302,303,304,305,306);
     
      update category_map_base t set t.super_category_id = 2 where t.categry_id in (201,202,203,204,205,206,207,208);
      
       update category_map_base t set t.super_category_id = 1 where t.categry_id in (101,102,103,104,105);

commit;

alter table person add department varchar2(601 CHAR);

commit;

----------修改菜单----------begin
update SYS_RESOURCE t set t.value = '/psnweb/homepage/show?menuId=1200' where t.id = 1;

commit;

update SYS_RESOURCE t set t.status = 0 where t.id in (1200, 1300, 1400, 1700, 8);

commit;

update SYS_RESOURCE t set t.status = 1 where t.id in (1200, 1700, 8);

update SYS_RESOURCE t set t.value = '/psnweb/homepage/show?menuId=1200' where t.id = 1200;

update sys_resource set value='/groupweb/mygrp/main?menuId=3'  where id=3;

update sys_resource set status=0 where parent_id=3;

commit;
----------修改菜单----------end

-----------psn_work_history表字段和psn_workhisroty_ins_info对应的字段大小不一致----------------begin

alter table psn_workhistory_ins_info modify ins_name_zh VARCHAR2(200 CHAR);
alter table psn_workhistory_ins_info modify position_zh VARCHAR2(200 CHAR);
commit;

-----------psn_work_history表字段和psn_workhisroty_ins_info对应的字段大小不一致----------------end

-----------------主页改造 2017-3-22 WSN end



--原因（有CQ号带上CQ号） 2017-6-9 成果认领task  zjh begin
insert into v_quartz_cron_expression t values(142,'noticeBeEndorseeAndEndorseTaskTrigger','*/30 * * * * ?',0,'研究领域邮件task');
commit;
--原因（有CQ号带上CQ号）2017-6-9 成果认领task zjh end

insert into v_quartz_cron_expression (id,cron_trigger_bean,cron_expression,status,description) values(38,'UpdatePubCiteTimesTaskTrigger','*/10 * * * * ?',
1,'基准库成果引用更新任务');


