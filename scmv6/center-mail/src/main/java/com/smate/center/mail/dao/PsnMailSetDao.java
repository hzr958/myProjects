package com.smate.center.mail.dao;

import org.springframework.stereotype.Repository;

import com.smate.center.mail.model.PsnMailSet;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 邮件发送设置
 * 
 * @author YPH
 * 
 */
@Repository
public class PsnMailSetDao extends SnsHibernateDao<PsnMailSet, Long> {
  /**
   * 
   * @param psnId
   * @param mailTypeId
   * @return
   */
  public PsnMailSet getByPsnIdAndMailTypeId(Long psnId, Long mailTypeId) {
    String hql = " from PsnMailSet p where p.psnId =:psnId  and p.mailTypeId=:mailTypeId ";
    return (PsnMailSet) this.createQuery(hql).setParameter("psnId", psnId).setParameter("mailTypeId", mailTypeId)
        .uniqueResult();
  }
}
