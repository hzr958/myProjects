-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end
--原因（数据库中 GROUP_RCMDS 表 group_name 字段设计与主表不一样 SCM-11358） 2017-1-16  AJB begin


alter  table GROUP_RCMDS  modify  (  GROUP_NAME         VARCHAR2(200 CHAR) )  ;

--原因（数据库中 GROUP_RCMDS 表 group_name 字段设计与主表不一样 SCM-11358） 2017-1-16 AJB end