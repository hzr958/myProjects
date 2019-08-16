package com.smate.center.batch.dao.rol.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PublicationListRol;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 成果收录情况DAO.
 * 
 * @author LY
 * 
 */
@Repository
public class PublicationListRolDao extends RolHibernateDao<PublicationListRol, Long> {

  /**
   * 更新pubid查重成果的收录情况.
   * 
   * @param pubId
   * @return
   */
  public PublicationListRol getPublicationList(Long pubId) {
    return super.findUnique(" from PublicationListRol t where t.id=?", pubId);
  }

  /**
   * 保存收录情况.
   * 
   * @see com.iris.scm.service.orm.Hibernate.SimpleHibernateDao#save(java.lang.Object)
   */
  public void save(PublicationListRol pubList) {
    super.getSession().saveOrUpdate(pubList);
  }

  /**
   * 删除引用情况.
   * 
   * @param pubId
   */
  public void deletePubList(Long pubId) {
    String hql = "delete from PublicationListRol t where t.id = ?";
    super.createQuery(hql, pubId).executeUpdate();
  }
}
