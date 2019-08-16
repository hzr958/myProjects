package com.smate.sie.center.task.dao.tmp;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.tmp.SieTaskSplitPubOrgs;

@Repository
public class SieTaskSplitPubOrgsDao extends SieHibernateDao<SieTaskSplitPubOrgs, Long> {

  public Long countNeedHandleKeyId() {
    String hql = "select count(pubId) from SieTaskSplitPubOrgs where status=0";
    return findUnique(hql);
  }

  public List<SieTaskSplitPubOrgs> loadNeedHandleKeyId(int maxSize) {
    String hql = "from SieTaskSplitPubOrgs k where k.status = 0";
    Query queryResult = super.createQuery(hql);
    queryResult.setMaxResults(maxSize);
    return queryResult.list();
  }

}
