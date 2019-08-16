package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.KeywordsHot;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 热词Dao
 * 
 * @author zk
 *
 */
@Repository
public class KeywordsHotDao extends SnsHibernateDao<KeywordsHot, Long> {

  /**
   * 批量获取kwtxt
   * 
   * @param pageNo
   * @param pageSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> findKwtxt(Integer pageNo, Integer pageSize) {
    String hql = "select kh.kwTxt from KeywordsHot kh order by kh.id asc ";
    return super.createQuery(hql).setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
  }
}
