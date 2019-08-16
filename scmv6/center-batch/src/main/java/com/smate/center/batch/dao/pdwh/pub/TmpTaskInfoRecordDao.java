package com.smate.center.batch.dao.pdwh.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.TmpTaskInfoRecord;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * 
 * @author LJ
 *
 *         2017年9月8日
 */
@Repository
public class TmpTaskInfoRecordDao extends PdwhHibernateDao<TmpTaskInfoRecord, Long> {

  public TmpTaskInfoRecord getJobByhandleId(Long handleId, Integer jobType) {
    String hql = "from TmpTaskInfoRecord t where t.handleId=:handleId and t.jobType=:jobType";
    return (TmpTaskInfoRecord) super.createQuery(hql).setParameter("handleId", handleId)
        .setParameter("jobType", jobType).uniqueResult();

  }


}
