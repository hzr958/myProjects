package com.smate.center.task.dao.sns.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PublicationList;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果收录情况DAO.
 * 
 * @author LY
 * 
 */
@Repository
public class PublicationListDao extends SnsHibernateDao<PublicationList, Long> {

  /**
   * 更新pubid查重成果的收录情况.
   * 
   * @param pubId
   * @return
   */
  public PublicationList getPublicationList(Long pubId) {
    return super.findUnique(" from PublicationList t where t.id=?", pubId);
  }

  /**
   * 保存收录情况.
   * 
   * @see com.iris.scm.service.orm.Hibernate.SimpleHibernateDao#save(java.lang.Object)
   */
  public void save(PublicationList pubList) {
    super.getSession().saveOrUpdate(pubList);
  }

  /**
   * 删除引用情况.
   * 
   * @param pubId
   */
  public void deletePubList(Long pubId) {
    String hql = "delete from PublicationList t where t.id = ?";
    super.createQuery(hql, pubId).executeUpdate();
  }
}
