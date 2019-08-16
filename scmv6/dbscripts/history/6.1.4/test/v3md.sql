-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end--SCM-21518  分享基金机会有邮件通知，分享资助机构是否需要2018/12/5 HHT begin
insert into maildispatch.mail_template_info t (t.temp_code,t.name,t.subject,t.folder_name,t.status,t.create_date,t.prior_code)
values (259,'Notify_Share_Agency_Template_en_US.ftl','分享资助机构模板','sns',1,to_date('2018/12/5 10:30:01','YYYY-MM-DD HH24:MI:SS'),7);
insert into maildispatch.mail_template_info t (t.temp_code,t.name,t.subject,t.folder_name,t.status,t.create_date,t.prior_code)
values (258,'Notify_Share_Agency_Template_zh_CN.ftl','分享资助机构模板','sns',1,to_date('2018/12/5 10:30:01','YYYY-MM-DD HH24:MI:SS'),7);
--SCM-21518  分享基金机会有邮件通知，分享资助机构是否需要2018/12/5 HHT end--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--SCM-21518  分享基金机会有邮件通知，分享资助机构是否需要2018/12/5 HHT begin
insert into mail_template_info t (t.temp_code,t.name,t.subject,t.folder_name,t.status,t.create_date,t.prior_code)
values (259,'Notify_Share_Agency_Template_en_US.ftl','分享资助机构模板','sns',1,to_date('2018/12/5 10:30:01','YYYY-MM-DD HH24:MI:SS'),7);
insert into mail_template_info t (t.temp_code,t.name,t.subject,t.folder_name,t.status,t.create_date,t.prior_code)
values (258,'Notify_Share_Agency_Template_zh_CN.ftl','分享资助机构模板','sns',1,to_date('2018/12/5 10:30:01','YYYY-MM-DD HH24:MI:SS'),7);
--SCM-21518  分享基金机会有邮件通知，分享资助机构是否需要2018/12/5 HHT end

