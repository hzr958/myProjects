-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因:基准库和rol库成果id关联    2016-8-24 ajb start
-- Create table
create table PUB_PDWH_ROL_RELATION
(
  rol_pub_id  NUMBER(18) not null,
  pdwh_pub_id NUMBER(18)
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
comment on table PUB_PDWH_ROL_RELATION
  is ' ROL与PDWH成果关系对应表';
-- Add comments to the columns 
comment on column PUB_PDWH_ROL_RELATION.rol_pub_id
  is 'ROL成果id';
comment on column PUB_PDWH_ROL_RELATION.pdwh_pub_id
  is '基准库成果id';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PUB_PDWH_ROL_RELATION
  add constraint PUB_PDWH_ROL_RELATION primary key (rol_pub_id)
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
  commit ;
   --原因:基准库和rol库成果id关联    2016-8-247 ajb end


--ROL-3270   2017-08-31 hd begin

--菜单
insert into SYS_RESOURCE (id, resource_type, value, order_num, parent_id, target, name, status, lavels, remark)
values (99010001, 'menu', '/scmwebrol/institution/main', 2, 0, null, 'rol.menu.institution.list', 1, 1, '单位列表-超级管理员');
--权限
insert into SYS_AUTHORITIE (id, name, display_name)
values (9902, 'A_INSTITUTION_LIST', '单位列表');
--菜单权限
insert into SYS_RESOURCE_AUTHORITIE (authority_id, resource_id)
values (9902, 99010001);
--角色权限
insert into SYS_ROLE_AUTHORITIE (role_id, authority_id)
values (99, 9902);
insert into SYS_ROLE_AUTHORITIE (role_id, authority_id)
values (99, 18);


--表INS_STAT_REFRESH 
create table INS_STAT_REFRESH
(
  ins_id     NUMBER(9) not null,
  status     NUMBER(1) default 0 not null,
  prior_code NUMBER(1) default 0 not null
);
-- Add comments to the table 
comment on table INS_STAT_REFRESH
  is '单位统计刷新表';
-- Add comments to the columns 
comment on column INS_STAT_REFRESH.ins_id
  is '机构id';
comment on column INS_STAT_REFRESH.status
  is '处理状态： 0待处理，1已处理,99处理失败';
comment on column INS_STAT_REFRESH.prior_code
  is '处理优先级：（0~9），0最小';
-- Create/Recreate primary, unique and foreign key constraints 

alter table INS_STAT_REFRESH
  add constraint INS_STAT_REFRESH_PK primary key (INS_ID);
 
  
  

--表INS_STATISTICS 
create table INS_STATISTICS
(
  ins_id        NUMBER(9) not null,
  prj_num       NUMBER default 0,
  pub_num       NUMBER default 0,
  thesis_num    NUMBER default 0,
  psn_num       NUMBER default 0,
  zh_name       VARCHAR2(100 CHAR),
  en_name       VARCHAR2(200 CHAR),
  ins_zh_admins VARCHAR2(2000 CHAR),
  ins_en_admins VARCHAR2(2000 CHAR)
);

-- Add comments to the table 
comment on table INS_STATISTICS
  is '单位统计表';
-- Add comments to the columns 
comment on column INS_STATISTICS.prj_num
  is '项目数';
comment on column INS_STATISTICS.pub_num
  is '成果数';
comment on column INS_STATISTICS.thesis_num
  is '论文数';
comment on column INS_STATISTICS.psn_num
  is '人员数';
comment on column INS_STATISTICS.zh_name
  is '单位中文名';
comment on column INS_STATISTICS.en_name
  is '单位英文名';
comment on column INS_STATISTICS.ins_zh_admins
  is '单位管理员';
comment on column INS_STATISTICS.ins_en_admins
  is '单位管理员';
-- Create/Recreate primary, unique and foreign key constraints 
alter table INS_STATISTICS
  add constraint PK_INS_STATISTICS primary key (INS_ID);
  
  
  
--后台任务
insert into QUARTZ_CRON_EXPRESSION (id, cron_trigger_bean, cron_expression, status, description)
values (53, 'insStatisticsTaskTriggers', '0 0/10 * * * ?', 1, '单位数据统计任务');

insert into APPLICATION_SETTING (id, key, value, remark)
values (54, 'rol_ins_stat_refresh', '1', '单位统计任务');


--初始化INS_STAT_REFRESH表
insert into INS_STAT_REFRESH (ins_id)
select t.ins_id from  institution t order by t.ins_id asc;


--ROL-3270   2017-08-31 hd end



--ROL-3288  20170905  hd

drop table PUB_FLAG;
drop table CONST_PUB_FLAG;


--ROL-3288  20170905   hd

--ROL-3303 20170906  hd

alter table INS_STATISTICS drop column THESIS_NUM;

--ROL-3303 20170906  hd


--ROL-3292  20170907   hd

update sys_resource t set t.order_num=2 where t.id=99010000;
update sys_resource t set t.order_num=3 where t.id=99010001;

--ROL-3292  20170907   hd
