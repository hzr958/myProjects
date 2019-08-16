-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（SCM-22537 清除资助机构记录已删除的基金数据） 2019-01-11 YWL begin
delete from CONST_FUND_CATEGORY t where  not exists 
(select 1 from CONST_FUND_AGENCY a where a.id = t.fund_agency_id)
--原因（SCM-22537 清除资助机构记录已删除的基金数据） 2019-01-11 YWL end