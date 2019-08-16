-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因:SCM-22669 项目-因prj_group_relation表还有旧数据原因，导致项目列表已删除群组的项目还显示“进入群组”，应清理这些数据 2019-1-15 YWL begin
delete from prj_group_relation p where p.group_id in (select v.grp_id from v_grp_baseinfo v where v.status = 99 )
--原因:SCM-22669 项目-因prj_group_relation表还有旧数据原因，导致项目列表已删除群组的项目还显示“进入群组”，应清理这些数据 2019-1-15 YWL end

