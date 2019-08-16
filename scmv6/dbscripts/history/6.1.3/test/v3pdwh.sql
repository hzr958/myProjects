-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end--原因群组与成果关系表增加字段   2018-9-5 YJ end

--原因：成果分享表增加字段 2018-9-10 yhx begin
comment on column v_pub_pdwh_share.SHARE_PSN_GROUP_ID is '被分享人或群组Id';
--原因：成果分享表增加字段 2018-9-10 yhx end

--原因 修改分享表分享平台字段备注  2018-9-13 yhx begin
comment on column v_pub_pdwh_share.platform is '分享平台: 1:动态,2:联系人,3:群组,4:微信,5:新浪微博,6:Facebook,7:Linkedin';
--原因 修改分享表分享平台字段备注  2018-9-13 yhx end