-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--------------已认领或者拒绝的成果认领记录未从pub_confirm表转移到pub_confirm_hi的数据，请进行处理，包括测试、UAT、生产机    SCM-8707    2016-12-19 zjh  begin---------------------
insert into pub_confirm_hi(id,psn_id,ins_id,confirm_date,confirm_result,assign_seq_no,confirm_seq_no,rol_pub_id,ins_assign_id,confirm_pm_id,dup_status,sync_status,dup_pubids,assign_score,sns_pub_id,auto_confirm) 
select p1.id,p1.psn_id,p1.ins_id,p1.confirm_date,p1.confirm_result,p1.assign_seq_no,p1.confirm_seq_no,p1.rol_pub_id,p1.ins_assign_id,p1.confirm_pm_id,p1.dup_status,p1.sync_status,p1.dup_pubids,p1.assign_score,p1.sns_pub_id,p1.auto_confirm from pub_confirm p1
where p1.confirm_result=1 or p1.confirm_result=2 and not exists (select 1 from pub_confirm_hi h where h.psn_id=p1.psn_id and h.rol_pub_id=p1.rol_pub_id and h.ins_id=p1.ins_id);
commit;

delete from pub_confirm c1 where  c1.confirm_result in(1,2) and exists(select 1 from pub_confirm_hi h where h.id=c1.id);
commit;
-------------- 已认领或者拒绝的成果认领记录未从pub_confirm表转移到pub_confirm_hi的数据，请进行处理，包括测试、UAT、生产机   SCM-8707    2016-12-19 zjh  end---------------------


