package com.smate.center.task.service.rol.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rol.quartz.PubInsSyncDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.PubInsSync;

@Service("pubInsSyncRolService")
@Transactional(rollbackFor = Exception.class)
public class PubInsSyncRolServiceImpl implements PubInsSyncRolService {
  @Autowired
  private PubInsSyncDao pubInsSyncDao;

  @Override
  public void updateSnsPubSubmittedFlag(Long snsPubId, Long insId, boolean flag) throws ServiceException {
    try {
      PubInsSync rec = this.pubInsSyncDao.getPubInsSyncByPK(snsPubId, insId);
      if (rec != null) {
        rec.setIsSubmited(flag ? 1 : 0);
        this.pubInsSyncDao.savePubInsSync(rec);
      }
    } catch (DaoException e) {
      throw new ServiceException("更新pub-ins记录提交标记错误.", e);
    }
  }

}
