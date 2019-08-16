-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end
--SCM-13370 SCM-13021 子问题，注册（pc端和移动端、open）新人员后，没有往sns库的psn_score_refresh插入记录 zll begin
update setting_mq_quene  t set t.enabled=1 where t.quene_name='psnRefreshInfo';
--SCM-13370 SCM-13021 子问题，注册（pc端和移动端、open）新人员后，没有往sns库的psn_score_refresh插入记录 zll end