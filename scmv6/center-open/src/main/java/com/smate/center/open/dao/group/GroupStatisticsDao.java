package com.smate.center.open.dao.group;


import org.springframework.stereotype.Repository;

import com.smate.center.open.model.group.GroupStatistics;
import com.smate.core.base.utils.data.SnsHibernateDao;



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
   * @author lhd
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
   * @author lhd
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
}
