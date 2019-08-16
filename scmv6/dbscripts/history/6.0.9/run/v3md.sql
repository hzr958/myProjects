-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（SCM-16376 给用户发送新年邮件任务） 2018-02-09 zll begin


insert into mail_template_info(temp_code,name,subject,folder_name,status,create_date,prior_code,remarks)
values(251,'2018New_year_greetings.ftl','给用户发新年邮件','promote',1,sysdate,7,'');

--原因（SCM-16376 给用户发送新年邮件任务） 2018-02-09 zll end


--原因（SCM-16337 新增添加群组作业邮件模板） 2018-02-26 zll begin
insert into mail_template_info(temp_code,name,subject,folder_name,status,create_date,prior_code,remarks)
 values(248,'Group_Add_New_Assignments_Template_zh_CN.ftl','群组新增作业','sns',1,sysdate,7,'');
 insert into mail_template_info(temp_code,name,subject,folder_name,status,create_date,prior_code,remarks)
 values(249,'Group_Add_New_Assignments_Template_en_US.ftl','群组新增作业','sns',1,sysdate,7,'');

--原因（SCM-16337 新增添加群组作业邮件模板） 2018-02-26 zll end