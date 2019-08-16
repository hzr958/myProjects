package com.smate.center.batch.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.FriendFappraisalRec;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.security.Person;

@Repository
public class FriendFappraisalRecDao extends SnsHibernateDao<FriendFappraisalRec, Long> {

  public void syncPersonFappraisalRec(Person person) {
    String hql = "update FriendFappraisalRec set friendHead = ?,friendPsnName = ?  where friendPsnId = ? ";
    super.createQuery(hql, person.getAvatars(), person.getName(), person.getPersonId()).executeUpdate();

  }

}
