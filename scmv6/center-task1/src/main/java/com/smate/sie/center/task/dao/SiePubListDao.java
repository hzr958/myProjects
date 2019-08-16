package com.smate.sie.center.task.dao;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.PubList;

@Repository
public class SiePubListDao extends SieHibernateDao<PubList, Long> {

  /**
   * 更新pubid查重成果的收录情况.
   * 
   * @param pubId
   * @return
   */
  public PubList getPublicationList(Long pubId) {
    return super.findUnique(" from PubList t where t.id=?", pubId);
  }

  /**
   * 保存收录情况.
   * 
   * @see com.iris.scm.service.orm.Hibernate.SimpleHibernateDao#save(java.lang.Object)
   */
  public void save(PubList pubList) {
    super.getSession().saveOrUpdate(pubList);
  }

  /**
   * 删除引用情况.
   * 
   * @param pubId
   */
  public void deletePubList(Long pubId) {
    String hql = "delete from PubList t where t.id = ?";
    super.createQuery(hql, pubId).executeUpdate();
  }
}
