package com.smate.center.batch.connector.dao.job;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.connector.model.job.BatchJobsHistory;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

/**
 * BatchJobs历史表dao
 * 
 * @author hzr
 * @version 6.0.1
 * 
 */
@Repository
public class BatchJobsHistoryDao extends HibernateDao<BatchJobsHistory, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }
}
