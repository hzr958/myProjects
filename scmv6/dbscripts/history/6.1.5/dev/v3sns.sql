-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（SCM-22722 成果更新保存 记录历史版本） 2019-1-25 YHX begin
alter table v_pub_sns add version number(18);
comment on column v_pub_sns.version is '最新修改的版本号';
--原因（SCM-22722 成果更新保存 记录历史版本） 2019-1-25 YHX end
