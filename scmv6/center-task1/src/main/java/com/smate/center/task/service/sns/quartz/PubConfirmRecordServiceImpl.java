package com.smate.center.task.service.sns.quartz;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.PubConfirmRecordDao;
import com.smate.center.task.exception.ServiceException;

@Service("pubConfirmRecordService")
@Transactional(rollbackFor = Exception.class)
public class PubConfirmRecordServiceImpl implements PubConfirmRecordService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubConfirmRecordDao pubConfirmRecordDao;

  @Override
  public Long savePubConfirmRecord(Long psnId, Long insId, Long rolPubId, Long snsPubId) {
    try {
      return pubConfirmRecordDao.savePubConfirmRecord(psnId, insId, rolPubId, snsPubId);
    } catch (Exception e) {
      logger.error("保存成果确认记录", e);
      throw new ServiceException("保存成果确认记录", e);
    }
  }

  @Override
  public void setSyncRcmdStatus(Long id, Integer syncRcmd) throws ServiceException {
    try {
      pubConfirmRecordDao.setSyncRcmdStatus(id, syncRcmd);
    } catch (Exception e) {
      logger.error("设置同步推荐系统状态", e);
      throw new ServiceException("设置同步推荐系统状态", e);
    }
  }

}
