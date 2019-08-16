-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句



--原因（有CQ号带上CQ号） 2016-12-8 WSN end



--SCM-21718 邮件设置中，缺少全文请求的邮件接收设置 HHT 2018-12-03 begin
update const_mail_type t set t.remark='4' where t.mail_type_id=30;

--SCM-21718 邮件设置中，缺少全文请求的邮件接收设置 HHT  2018-12-03 end

--原因（SCM-21653 uat--移动端：论文推荐，设置条件页面，成果类型显示顺序和测试机的不同？） 2018-12-3 ywl begin

update CONST_PUB_TYPE t set t.seq_no = 1 where t.type_id = 4;
update CONST_PUB_TYPE t set t.seq_no = 2 where t.type_id = 5;
update CONST_PUB_TYPE t set t.seq_no = 3 where t.type_id = 3;
update CONST_PUB_TYPE t set t.seq_no = 4 where t.type_id = 1;
update CONST_PUB_TYPE t set t.seq_no = 5 where t.type_id = 2;
update CONST_PUB_TYPE t set t.seq_no = 6 where t.type_id = 10;
update CONST_PUB_TYPE t set t.seq_no = 7 where t.type_id = 11;
update CONST_PUB_TYPE t set t.seq_no = 7 where t.type_id = 8;
update CONST_PUB_TYPE t set t.seq_no = 99 where t.type_id = 7;

--原因（有CQ号带上CQ号） 2018-12-3 ywl end