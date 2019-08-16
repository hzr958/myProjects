package com.smate.center.batch.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.FriendFappraisalSend;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.security.Person;

@Repository
public class FriendFappraisalSendDao extends SnsHibernateDao<FriendFappraisalSend, Long> {

  public void syncPersonFappraisalSend(Person person) {
    String hql = "update FriendFappraisalSend set friendPsnName = ?  where friendPsnId = ? ";
    super.createQuery(hql, person.getName(), person.getPersonId()).executeUpdate();
  }

}
