package com.smate.web.group.dao.grp.job;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.group.model.grp.job.TmpTaskInfoRecord;

@Repository
public class TmpTaskInfoRecordDao extends PdwhHibernateDao<TmpTaskInfoRecord, Long> {
  public TmpTaskInfoRecord getJobByhandleId(Long handleId, Integer jobType) {
    String hql = "from TmpTaskInfoRecord t where t.handleId=:handleId and t.jobType=:jobType";
    return (TmpTaskInfoRecord) super.createQuery(hql).setParameter("handleId", handleId)
        .setParameter("jobType", jobType).uniqueResult();

  }
}
