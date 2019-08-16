package com.smate.web.v8pub.dao.job;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.job.TmpTaskInfoRecord;
import org.springframework.stereotype.Repository;

/**
 * 重复成果分组任务Dao
 * 
 * @author YJ
 *
 *         2018年9月10日
 */
@Repository
public class TmpTaskInfoRecordDao extends PdwhHibernateDao<TmpTaskInfoRecord, Long> {
  /**
   * 个人重复成果分组任务记录
   * 
   * @param handleId 人员ID
   * @param jobType 11
   * @return
   * @throws Exception
   * @author LIJUN
   * @date 2018年5月7日
   */
  public TmpTaskInfoRecord getGrpRecord(Long psnId) {
    String hql = "from TmpTaskInfoRecord t where t.handleId=:handleId and t.jobType=11";
    return (TmpTaskInfoRecord) super.createQuery(hql).setParameter("handleId", psnId).uniqueResult();

  }

}
