package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SieRobotManIns;

/**
 * 社交机器人需要访问的机构Dao
 * 
 * @author 叶星源
 * 
 */
@Repository
public class SieRobotManInsDao extends SieHibernateDao<SieRobotManIns, Long> {
  /*
   * @SuppressWarnings("unchecked") public List<Long> getInsNum() { // 修改机器人访问的机构，查询机构存在才会去对机构进行访问
   * String hql = "select t.id from SieRobotManIns t where t.id in (select id from Sie6Institution)";
   * return super.createQuery(hql).list(); }
   */


  /**
   * 修改机器人访问的机构，查询机构存在，获取机构列表进行访问
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SieRobotManIns> getSubInsList() {
    String hql = "from SieRobotManIns t where t.id in (select id from Sie6Institution)";
    String countHql = "select count(0) from SieRobotManIns t where t.id in (select id from Sie6Institution)";
    Long count = (Long) super.createQuery(countHql).uniqueResult();
    if (count == 0 || count == 0L) {
      return null;
    }
    return super.createQuery(hql).list();
  }
}
