-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--原因（SCM-15200 通知项目负责人项目相关成果 ） 2017-11-27 zll begin


insert into mail_template_info(temp_code,name,subject,folder_name,status,create_date,prior_code,remarks)
 values(245,'Project_Pub_Confirm_Remind_zh_CN.ftl','通知项目负责人项目相关成果','promote',1,sysdate,2,'');


--原因（SCM-15200 通知项目负责人项目相关成果 ） 2017-11-27 zll end
 