package com.smate.center.batch.service.isisstat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.dao.sns.isisstat.IsisPubStatDao;

@Service("isisPubStatService")
public class IsisPubStatServiceImpl implements IsisPubStatService {

  @Autowired
  private IsisPubStatDao isisPubStatDao;

  @Override
  public void doIsisPubStatProc() throws Exception {
    isisPubStatDao.doIsisPubStatProc();
  }

}
