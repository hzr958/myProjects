-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--原因（SCM-9283 邮件模板更新） 2018-04-08 zll begin
update md2test.mail_template_info  t set t.status=0  ,t.remarks='英文模板去掉'where t.name='RetrievePwdTemplate_en_US.ftl';
--原因（SCM-9283 邮件模板更新） 2018-04-08 zll end