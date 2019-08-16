-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


-- Add comments to the columns 
alter table CONST_FUND_AGENCY add (EN_ADDRESS varchar2(200 char) );
comment on column CONST_FUND_AGENCY.en_address is '英文资助机构单位地址';

--SCM-16810 2018-3-29 ZX end