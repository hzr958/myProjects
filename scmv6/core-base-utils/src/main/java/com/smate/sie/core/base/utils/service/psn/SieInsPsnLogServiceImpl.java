package com.smate.sie.core.base.utils.service.psn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.sie.core.base.utils.dao.psn.SieInsPsnLogDao;

/**
 * 单位人员表操作日志.
 * 
 * @author yxs
 * 
 */
@Service("sieInsPsnLogService")
@Transactional(rollbackFor = Exception.class)
public class SieInsPsnLogServiceImpl implements SieInsPsnLogService {

  @Autowired
  private SieInsPsnLogDao insPsnLogDao;

  @Override
  public void log(Long opPsn, Long opIns, Long targetPsn, String action, String detail) {
    insPsnLogDao.log(opPsn, opIns, targetPsn, action, detail);
  }

}
