-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end



--原因（SCM-17098 群组添加成果,发送邮件） 2018-04-03 ajb start

update    V_QUARTZ_CRON_EXPRESSION t set t.cron_expression = '* 0/59 * * * ?'  , t.status = 1  where t.cron_trigger_bean='groupPublcationEmailTaskTrigger' ;

commit ;


--原因（SCM-17098 群组添加成果,发送邮件） 2018-04-03 ajb end



-原因（    SCM-17263   添加open接口   获取待认领成果,） 2018-04-16 ajb start


-- Alter table 
alter table V_OPEN_THIRD_REG
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table V_OPEN_THIRD_REG add ins_id NUMBER(18);
-- Add comments to the columns 
comment on column V_OPEN_THIRD_REG.ins_id
  is '单位id  psn_ins 表对应的  ； 对某些接口允许的单位id';
commit ;
insert into   v_open_token_service_const( id , token , service_type  , descr )  values (  737 , '11111111' , 'obt63pub',  '获取成果编目信息，需要判断insId权限')  ;
insert into   v_open_token_service_const( id , token , service_type  , descr )  values (  738 , '11111111' , 'obt75rcf',  '获取待认领成果信息，需要判断insId权限')  ;

insert into   v_open_token_service_const( id , token , service_type  , descr )  values (  739 , '11111111' , 'obtrcf6p',  '确认个人待认领的成果，需要判断insId权限')  ;

commit ;


--原因（    SCM-17263   添加open接口   获取待认领成果,） 2018-04-16 ajb end

