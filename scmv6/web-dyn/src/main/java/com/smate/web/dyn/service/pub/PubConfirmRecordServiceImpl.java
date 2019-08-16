package com.smate.web.dyn.service.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.dao.pub.PubConfirmRecordDao;
import com.smate.web.dyn.model.pub.PubConfirmRecord;

/**
 * 成果确认记录服务.
 * 
 * @author lqh
 * 
 */
@Service("pubConfirmRecordService")
@Transactional(rollbackFor = Exception.class)
public class PubConfirmRecordServiceImpl implements PubConfirmRecordService {
  /**
   * 
   */
  private static final long serialVersionUID = -770962645395231616L;
  private static Logger logger = LoggerFactory.getLogger(PubConfirmRecordServiceImpl.class);
  @Autowired
  private PubConfirmRecordDao pubConfirmRecordDao;



  @Override
  public PubConfirmRecord getRecordBySnsPubId(Long snsPubId) throws DynException {
    try {
      return pubConfirmRecordDao.getRecordBySnsPubId(snsPubId);
    } catch (Exception e) {
      logger.error("获取成果确认记录出错", e);
      throw new DynException("获取成果确认记录出错", e);
    }
  }

}
