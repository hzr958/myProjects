-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--SCM-6685 论文投稿建议》合作者，大部分要用到的信息都被挪到推荐服务器了，但推荐任务又还在SNS 2017-06-12 zll begin
insert into  setting_mq_quene (setting_id,quene_name,listen_nodes,send_nodes,enabled,descript,enabled_local) 
values(118,'psnRefreshInfo',1,1,1,'人员冗余信息同步至sns库标记消息',0);

--SCM-6685 论文投稿建议》合作者，大部分要用到的信息都被挪到推荐服务器了，但推荐任务又还在SNS 2017-06-12 zll end