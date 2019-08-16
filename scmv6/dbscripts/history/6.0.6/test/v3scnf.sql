-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

-------WSN  alpha环境和development用不同的缓存地址  2017-8-23  begin

alter table scnf_srv_cache add (env varchar2(50char));

comment on column scnf_srv_cache.env
is '配置适用哪个环境的';

--环境有alpha、development、test、uat、run
update scnf_srv_cache t set t.env = 'test';

-------WSN  alpha环境和development用不同的缓存地址  2017-8-23  end