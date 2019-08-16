package com.smate.web.dyn.dao.psn;



import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.psn.AttPerson;

/**
 * 人员关注Dao
 * 
 * @author zk
 *
 */
@Repository
public class AttPersonDao extends SnsHibernateDao<AttPerson, Long> {


  /**
   * 取消某人的关注.
   * 
   * @param psnId
   * @param refPsnId
   * @throws DaoException
   */
  public void deleteAttPerson(Long psnId, Long refPsnId) {
    String hql = "delete from AttPerson t where t.psnId=:psnId and t.refPsnId=:refPsnId";
    super.createQuery(hql).setParameter("psnId", psnId).setParameter("refPsnId", refPsnId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<Long> isAttPerson(Long psnId, Long refPsnId) {
    String hql = "select id from AttPerson t where t.psnId=:psnId and t.refPsnId=:refPsnId";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("refPsnId", refPsnId).list();
  }

}
