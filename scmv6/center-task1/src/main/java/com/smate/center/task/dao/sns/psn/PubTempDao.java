package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.PubTemp;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * @description 会议论文数据统计
 * @author xiexing
 * @date 2019年2月28日
 */
@Repository
public class PubTempDao extends SnsHibernateDao<PubTemp, Long> {

  /**
   * 从备份表中获取id 一次获取size条
   * 
   * @param size
   * @return
   */
  public List<Long> getIds(Integer size) {
    String HQL = "select t.pubId from PubTemp t where t.status = 0";
    return (List<Long>) getSession().createQuery(HQL).setMaxResults(size).list();
  }
}
