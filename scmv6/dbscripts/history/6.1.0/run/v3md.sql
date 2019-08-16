-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（SCM-16337 新增添加群组作业邮件模板） 2018-02-06 zll begin
insert into mail_template_info(temp_code,name,subject,folder_name,status,create_date,prior_code,remarks)
 values(248,'Group_Add_New_Assignments_Template_zh_CN.ftl','群组新增作业','sns',1,sysdate,7,'');
 insert into mail_template_info(temp_code,name,subject,folder_name,status,create_date,prior_code,remarks)
 values(249,'Group_Add_New_Assignments_Template_en_US.ftl','群组新增作业','sns',1,sysdate,7,'');

--原因（SCM-16337 新增添加群组作业邮件模板） 2018-02-06 zll end
 
 --原因（SCM-16231 根据原型新加邮件模版） 2018-02-06 zll begin
   insert into mail_template_info(temp_code,name,subject,folder_name,status,create_date,prior_code,remarks)
 values(250,'Confirm_Email_Address_Template_zh_CN.ftl','确认邮件地址','sns',1,sysdate,7,'');
  --原因（SCM-16231 根据原型新加邮件模版） 2018-02-06 zll end

--原因（SCM-15786 目前没有基金分享邮件模板，需要新增基金分享的邮件模板）2018-03-08 Zll begin

insert into mail_template_info(temp_code,name,subject,folder_name,status,create_date,prior_code,remarks)
values(252,'Notify_Share_fund_Template_zh_CN.ftl','分享基金模板','sns',1,sysdate,7,'');

--原因（SCM-15786 目前没有基金分享邮件模板，需要新增基金分享的邮件模板）2018-03-08 Zll end


--原因（添加群组新增成果英文模板） 2018-03-27 zll begin
insert into md2test.mail_template_info(temp_code,name,subject,folder_name,status,create_date,prior_code,remarks) 
values(255,'Group_Add_New_Pub_Template_en_US.ftl','群组新增成果','sns',1,sysdate,8,'');

--原因（添加群组新增成果英文模板） 2018-03-27 zll end