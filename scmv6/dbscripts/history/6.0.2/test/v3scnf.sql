-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

---原因（SCM-9755 rcmd库、sns库的INS_PORTAL表没有跟rol库的同步更新，会导致单位成果认领后没在个人成果列表显示.） 2016-12-30  zll begin
insert into setting_mq_quene(setting_id,quene_name,listen_nodes,send_nodes,enabled,descript,enabled_local) 
values(249,'approveInsToRcmd',2000,0,1,'单位审核通过后，同步INSTITUTION表和INS_PORTAL表至rcmd库',0);
commit;
---原因（SCM-9755 rcmd库、sns库的INS_PORTAL表没有跟rol库的同步更新，会导致单位成果认领后没在个人成果列表显示.） 2016-12-30  zll end