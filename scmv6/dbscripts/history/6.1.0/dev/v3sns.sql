-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（SCM-17060 处理全文请求消息历史记录，sender_id为1的情况） 2018-4-9 hcj begin
update v_msg_relation set deal_status=-1, deal_date=sysdate where sender_id =-1 and TYPE= 11 and deal_status = 0;
--原因（SCM-17060） 2018-4-9 hcj end