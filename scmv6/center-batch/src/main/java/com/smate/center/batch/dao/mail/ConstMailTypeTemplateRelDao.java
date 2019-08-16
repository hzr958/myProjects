package com.smate.center.batch.dao.mail;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.psn.ConstMailTypeTemplateRel;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 
 * @author zk
 * 
 */
@Repository
public class ConstMailTypeTemplateRelDao extends SnsHibernateDao<ConstMailTypeTemplateRel, Long> {

  /**
   * 根据模板id得到类型id
   */
  public Long getTypeidFromTemplateid(Integer templateId) throws DaoException {
    String hql = "select t.typeId from ConstMailTypeTemplateRel t where t.templateId=:templateId";
    return (Long) super.createQuery(hql).setParameter("templateId", templateId).uniqueResult();
  }

}
