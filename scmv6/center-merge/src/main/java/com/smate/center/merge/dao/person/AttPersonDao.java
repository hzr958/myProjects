package com.smate.center.merge.dao.person;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.merge.model.sns.person.AttPerson;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 关注人员Dao
 * 
 * @author zk
 *
 */
@Repository
public class AttPersonDao extends SnsHibernateDao<AttPerson, Long> {

  @SuppressWarnings("unchecked")
  public List<AttPerson> findAttPsnIds(Long refPsnId) {
    String hql = "from AttPerson ap where ap.refPsnId=:refPsnId or ap.psnId=:refPsnId";
    return super.createQuery(hql).setParameter("refPsnId", refPsnId).list();
  }
}
