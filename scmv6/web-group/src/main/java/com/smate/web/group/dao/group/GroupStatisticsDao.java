package com.smate.web.group.dao.group;


import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.GroupStatistics;



/**
 * 群组统计表DAO类.
 * 
 * @author mjg
 * 
 */
@Repository
public class GroupStatisticsDao extends SnsHibernateDao<GroupStatistics, Long> {

  /**
   * 保存群组统计记录.
   * 
   * @param groupStat
   */
  public void saveGroupStatistics(GroupStatistics groupStat) {
    if (groupStat != null) {
      if (groupStat.getId() != null) {
        super.getSession().update(groupStat);
      } else {
        super.save(groupStat);
      }
    }
  }

  /**
   * 获取群组统计记录.
   * 
   * @param groupId
   * @return
   */
  public GroupStatistics getStatistics(Long groupId) {
    String hql = "from GroupStatistics t where t.groupId=? and rownum=1 ";
    Object obj = super.createQuery(hql, groupId).uniqueResult();
    if (obj != null) {
      return (GroupStatistics) obj;
    }
    return null;
  }

  /**
   * 获取群组头部的成果数据和人员数
   * 
   * @param groupId
   * @return
   */
  public GroupStatistics getGroupStaticForTop(Long groupId) {
    String hql =
        "select new GroupStatistics(t.sumMembers  ,t.sumPubs)from GroupStatistics t where t.groupId=? and rownum=1 ";
    Object obj = super.createQuery(hql, groupId).uniqueResult();
    if (obj != null) {
      return (GroupStatistics) obj;
    }
    return null;
  }

  /**
   * 群组成员-移除此人操作-更新群组成员统计数
   * 
   * @param groupId
   */
  public void getSumMembersByGroupId(Long groupId) {
    super.createQuery(
        "update GroupStatistics t set t.sumMembers = t.sumMembers-1 where t.groupId=:groupId and rownum=1 ")
            .setParameter("groupId", groupId).executeUpdate();
  }
}
