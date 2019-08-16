-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end
--SCM-21711 生产机，基金》存在资助机构和资助类别ins_id不为0的数据，请清理 2018-12-1 LTL begin
delete const_fund_agency where ins_id!=0;
delete const_fund_category where ins_id!=0;
commit;
--SCM-21711 生产机，基金》存在资助机构和资助类别ins_id不为0的数据，请清理 2018-12-1 LTL end
