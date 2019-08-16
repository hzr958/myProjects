-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end





-- 数据库脚本登记示例

--原因（SCM-15797） 2018-01-02 LJ begin

ALTER TABLE v_pub_index_url ADD pub_long_index_url VARCHAR2(100 CHAR);

insert into v_quartz_cron_expression values(100,'SyncPmNameFromPersonTaskTrigger','0 0/1 * * * ?',0,'补充数据到pmisiname、pmcnkiname、psnins');


--原因（SCM-15797） 2018-01-02 LJ end

