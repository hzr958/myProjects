package com.smate.center.task.dyn.dao.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.dyn.model.psn.AttPerson;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 关注人员Dao
 * 
 * @author zk
 *
 */
@Repository
public class AttPersonDao extends SnsHibernateDao<AttPerson, Long> {

  /**
   * 查找出关注refPsnId的人
   * 
   * @param refPsnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findAttPsnIds(Long refPsnId) {
    String hql = "select ap.psnId from AttPerson ap where ap.refPsnId=:refPsnId";
    return super.createQuery(hql).setParameter("refPsnId", refPsnId).list();
  }
}
