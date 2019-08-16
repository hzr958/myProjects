-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

---原因 （SCM-22337 crossref数据拆分，作者单位有多个的，在v_pub_pdwh_member_insname只存了一个) 20190108 zll start
alter table PUB_REFERENCE modify year VARCHAR2(20);
alter table PUB_REFERENCE modify unstructured VARCHAR2(2000);
alter table PUB_CATEGORY_CROSSREF modify crossref_category VARCHAR2(200);
comment on column ORIGINAL_PDWH_PUB_RELATION.record_from
  is '原始数据来源 0：后台导入，1：在线导入，2：crossref';
---原因 （SCM-22337 crossref数据拆分，作者单位有多个的，在v_pub_pdwh_member_insname只存了一个) 20190108 zll end