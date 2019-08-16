-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end



--原因（SCM-18211） 2018-6-28 lzx begin
alter table SYS_THIRD_USER add union_id varchar2(500); 
commit;
--原因（SCM-18211） 2018-6-28 lzx end