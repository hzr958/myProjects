-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--原因（有CQ号带上CQ号） 2017-6-22 zjh begin
---yahoo邮箱全部加入黑名单不发送邮件
insert into email_blacklist(id,email,bl_level,bl_type,reason,create_date)  values ( seq_email_blacklist.nextval ,'yahoo.com',1,1,'yahoo邮箱全部发送邮件',sysdate);


insert into email_blacklist(id,email,bl_level,bl_type,reason,create_date) values ( seq_email_blacklist.nextval,'yahoo.com.cn',1,1,'yahoo邮箱全部发送邮件',sysdate );
commit;

insert into email_blacklist(id,email,bl_level,bl_type,reason,create_date) values ( seq_email_blacklist.nextval,'yahoo.cn.com',1,1,'yahoo邮箱全部不发送邮件',sysdate );
commit;



insert into email_blacklist(id,email,bl_level,bl_type,reason,create_date) values ( seq_email_blacklist.nextval,'yahoo.cn',1,1,'yahoo邮箱全部不发送邮件',sysdate );
commit;

insert into email_blacklist(id,email,bl_level,bl_type,reason,create_date) values ( seq_email_blacklist.nextval,'rol.com',1,1,'rol邮箱全部不发送邮件',sysdate);
commit;


insert into email_blacklist(id,email,bl_level,bl_type,reason,create_date) values (seq_email_blacklist.nextval,'scholarmate.com',1,1,'scholarmate.com邮箱全不发送邮件',sysdate );
commit;

insert into email_blacklist(id,email,bl_level,bl_type,reason,create_date) values ( seq_email_blacklist.nextval,'smate.com',1,1,'smate.com邮箱全不发送邮件',sysdate );
commit;


--原因（有CQ号带上CQ号）  2017-6-22 zjh end
