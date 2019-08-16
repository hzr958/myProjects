package com.smate.sie.core.base.utils.dao.psn;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.psn.SieInsPsnLog;

/**
 * 
 * @author yxs
 * @descript 人员日志dao
 */
@Repository
public class SieInsPsnLogDao extends SieHibernateDao<SieInsPsnLog, Long> {
  /**
   * 
   *
   * @descript 保存日志
   */
  public SieInsPsnLog log(Long opPsn, Long opIns, Long targetPsn, String action, String detail) {
    SieInsPsnLog log = new SieInsPsnLog(opPsn, opIns, targetPsn, action, detail);
    super.save(log);
    return log;
  }
}
