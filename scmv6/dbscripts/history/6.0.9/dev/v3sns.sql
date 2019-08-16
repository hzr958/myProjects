-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end



--原因（SCM-16144） 2018-01-29 lj begin


  insert into v_quartz_cron_expression values(104,'BaiduMapGetInsAddsTaskTrigger','0 0/1 * * * ?',0,'百度地图单位地址获取');


--原因（SCM-16144） 2018-01-29 lj end
  
--原因（SCM-16561 用户的首页邮箱设置有两个） 2018-03-24 ltl begin

update PSN_EMAIL a set a.first_mail = 0
where a.rowid !=  
(  
select max(b.rowid) from PSN_EMAIL b  
where a.psn_id = b.psn_id and  
a.first_mail = b.first_mail and first_mail =1 and a.email = b.email
); 
commit;
--原因（SCM-16561 用户的首页邮箱设置有两个） 2018-03-24 ltl end