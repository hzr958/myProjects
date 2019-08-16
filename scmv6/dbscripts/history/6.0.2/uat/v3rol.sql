-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--ROL-3157 成果，成果合并未显示满足条件的成果,原因定时器没启动     2016-12-20 ltl begin
update quartz_cron_expression t set t.status=1 where t.cron_trigger_bean='localQueneTaskTriggers';
commit;
