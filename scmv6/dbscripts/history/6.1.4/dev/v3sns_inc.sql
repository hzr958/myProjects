-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end
--SCM-22459  tempcode=10044，10073人员合并和确认系统地址的邮件，改为不可退订 2019-1-11 HHT  begin
update const_mail_type t set t.status=0 where t.mail_type_id=21;
--SCM-22459  tempcode=10044，10073人员合并和确认系统地址的邮件，改为不可退订 2019-1-11 HHT  end
