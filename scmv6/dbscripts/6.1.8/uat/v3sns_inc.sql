-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--SCM-26885 新增根据地区检索基准库的成果接口 2019-8-14 zll begin

Insert Into v_open_token_service_const(id,token,service_type,status,create_date,descr,access_date,access_num,access_max_num)
Values(15721,'11111111','seap1rid',0,Sysdate,'根据地区检索基准库成果',Sysdate,0,1000);

--SCM-26885 新增根据地区检索基准库的成果接口 2019-8-14 zll end

--SCM-26762 兴趣群组新增成果发送动态更新邮件  2019-8-14 zll begin

 Insert Into v_quartz_cron_expression(id,cron_trigger_bean,cron_expression,status,description)
 Values(1962,'SendGrpDnyUpdateEmailTaskTrigger','0 0 16 * * ?',0,'兴趣群组动态更新通知');
--SCM-26762 兴趣群组新增成果发送动态更新邮件  2019-8-14 zll begin