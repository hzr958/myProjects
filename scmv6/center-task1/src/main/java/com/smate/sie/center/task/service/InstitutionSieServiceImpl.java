package com.smate.sie.center.task.service;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.exception.ServiceException;
import com.smate.sie.center.task.dao.InsGuidDao;
import com.smate.sie.center.task.model.InsGuid;

@Service("institutionSieService")
@Transactional(rollbackOn = Exception.class)
public class InstitutionSieServiceImpl implements InstitutionSieService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InsGuidDao insGuidDao;

  @Override
  public List<InsGuid> getNeedRefresh(int maxSize) throws ServiceException {
    try {
      return this.insGuidDao.queryNeedRefresh(maxSize);
    } catch (Exception e) {
      logger.error("加载需要刷新guid的单位记录出现异常", e);
      throw new ServiceException("", e);
    }
  }

  @Override
  public void refreshInsGuid(InsGuid insGuid) throws ServiceException {
    try {
      String guid = UUID.randomUUID().toString().replace("-", "");
      insGuid.setGuid(guid);
      this.insGuidDao.save(insGuid);
    } catch (Exception e) {
      logger.error("刷新单位guid出现异常", e);
      throw new ServiceException("", e);
    }
  }

}
