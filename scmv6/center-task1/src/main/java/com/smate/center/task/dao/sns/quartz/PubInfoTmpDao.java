package com.smate.center.task.dao.sns.quartz;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.PubInfoTmp;
import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubInfoTmpDao extends SnsHibernateDao<PubInfoTmp, Long> {

  public void deletePubInfoTmp(List<Publication> pubIds) {
    String hql = "delete from PubInfoTmp where pubId in (:pubIds) ";
    super.createQuery(hql).setParameterList("pubIds", pubIds).executeUpdate();
  }

}
