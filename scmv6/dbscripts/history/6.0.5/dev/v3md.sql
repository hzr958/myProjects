-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--原因（ SCM-13825 邮件-发件箱发件上限设置修改） 2017-08-10 zll begin

-- Add/modify columns 
alter table SENDER_ACCOUNT add max_mail_count NUMBER(5);
-- Add comments to the columns 
comment on column SENDER_ACCOUNT.max_mail_count
  is '发件箱发件上限';
  
 update sender_account t set t.max_mail_count=800;

--原因（ SCM-13825 邮件-发件箱发件上限设置修改） 2017-08-10 zll end
