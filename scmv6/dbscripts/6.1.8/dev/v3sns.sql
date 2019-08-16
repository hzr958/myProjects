-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--SCM-25980 uat--联系人推荐：特定帐号点添加联系人提示系统出错  2019-6-21 WSN begin

update person t set t.region_id = null where t.region_id = 0;

commit;

--SCM-25980 uat--联系人推荐：特定帐号点添加联系人提示系统出错  2019-6-21 WSN begin