-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end
--原因（SCM-12213 站外论文、人员迁至新系统 ） 2017-04-26 zll begin
insert into v_quartz_cron_expression (id ,cron_trigger_bean,cron_expression,status,description)
 values(28,'indexInfoInitTaskTrigger','0/10 * * * * ?',0,'获取站外检索信息');

insert into v_quartz_cron_expression (id ,cron_trigger_bean,cron_expression,status,description) 
values(29,'indexPsnInfoInitTaskTrigger','0/10 * * * * ?',0,'初始化系统人员信息定时器任务');


--原因（SCM-12213 站外论文、人员迁至新系统 ） 2017-04-26 zll end