-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--原因（SCM-26808 论文在solr新增地区，分类字段） 2019-08-12 zll begin
  
  -- Add/modify columns 
alter table PDWH_PUB_ADDR_INS_RECORD add region_id NUMBER(6);
-- Add comments to the columns 
comment on column PDWH_PUB_ADDR_INS_RECORD.region_id
  is '地区id';

--原因（SCM-26808 论文在solr新增地区，分类字段） 2019-08-12 zll end
