package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.TempRecordPubId;

@Repository
public class TempRecordPubIdDao extends SieHibernateDao<TempRecordPubId, Long> {
  @SuppressWarnings("unchecked")
  public List<TempRecordPubId> getRolPubXmlListPub(int size) {
    String hql = "from TempRecordPubId where status = 0";
    return super.createQuery(hql).setMaxResults(size).list();
  }
}
