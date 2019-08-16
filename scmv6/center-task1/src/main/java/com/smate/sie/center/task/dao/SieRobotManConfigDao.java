package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SieRobotManConfig;

/**
 * 社交机器人需要用到的配置文件Dao
 * 
 * @author 叶星源
 * 
 */
@Repository
public class SieRobotManConfigDao extends SieHibernateDao<SieRobotManConfig, Long> {

  /**
   * 获取该单位的机器人访问配置信息
   */
  @SuppressWarnings("unchecked")
  public SieRobotManConfig getNormalModel(Integer configId) {
    String hql = " from SieRobotManConfig t where t.id=:configId  order by t.id desc ";
    // 若获取的该单位的配置信息有多条，通过配置id排序取配置id最大的那条
    List<SieRobotManConfig> list = super.createQuery(hql).setParameter("configId", configId).list();
    if (list != null && list.size() > 0 && list.get(0) != null) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 获取第一条按ID倒序排序的可用配置
   */
  /*
   * @SuppressWarnings("unchecked") public SieRobotManConfig getNormalModel() { String hql =
   * "from SieRobotManConfig t where t.status=1 order by t.id desc"; List<SieRobotManConfig> list =
   * super.createQuery(hql).list(); if (list != null && list.size() > 0 && list.get(0) != null) {
   * return list.get(0); } return null; }
   */

  /**
   * 获取ID最大值，不常用
   */
  public Integer getMaxId() {
    String hql = "select max(t.id) from SieRobotManConfig t";
    Integer maxId = super.findUnique(hql);
    if (maxId == null) {
      maxId = 0;
    }
    return maxId + 1;
  }
}
