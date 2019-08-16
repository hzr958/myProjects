-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--ROL-3386  2017-12-21 hd begin


 alter table INS_STATISTICS  add  update_date DATE;  
 
 comment on column INS_STATISTICS.update_date  is '更新时间';
 
 update quartz_cron_expression t set t.cron_expression='0 0 21 * * ? *' where t.cron_trigger_bean='insStatisticsTaskTriggers'; 


--ROL-3386  2017-12-21 hd end