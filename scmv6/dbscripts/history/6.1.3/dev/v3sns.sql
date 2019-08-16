-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--SCM-19406 tempcode=10025,10051,10052,10054，10106联系人更新研究领域，赞/评论/分享论文、待处理消息的邮件模板，点击退订，提示退订失败。2018-10-22 HHT begin

update v_mail_template t set t.mail_type_id=6  where t.template_code=10025;

--SCM-19406 tempcode=10025,10051,10052,10054，10106联系人更新研究领域，赞/评论/分享论文、待处理消息的邮件模板，点击退订，提示退订失败。2018-10-22 HHT end
