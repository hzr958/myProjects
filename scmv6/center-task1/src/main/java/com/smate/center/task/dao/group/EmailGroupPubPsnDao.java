package com.smate.center.task.dao.group;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.group.EmailGroupPubPsn;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class EmailGroupPubPsnDao extends SnsHibernateDao<EmailGroupPubPsn, Long> {

  public boolean isExists(Long pubId, Long groupId) {
    String hql = "select count(1) from EmailGroupPubPsn a where a.pubId=:pubId and a.groupId=:groupId";
    Long i = (Long) super.createQuery(hql).setParameter("pubId", pubId).setParameter("groupId", groupId).uniqueResult();
    if (i != null && i == 0L) {
      return false;
    }
    return true;
  }
}
