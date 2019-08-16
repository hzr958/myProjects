package com.smate.center.task.dao.tmp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.tmp.TmpNsfcKwsTaskRecord;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class TmpNsfcKwsTaskRecordDao extends SnsbakHibernateDao<TmpNsfcKwsTaskRecord, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getUnprocessedPubKwsIds(Integer size) {
    String hql = "select id from TmpNsfcKwsTaskRecord where status =0 order by Id desc";
    return super.createQuery(hql).setMaxResults(size).list();
  }

}
