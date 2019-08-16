package com.smate.center.batch.service.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.PubRefSyncRolFlagDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PubRefSyncRolFlag;

/**
 * 文献同步ROL业务类(实现类).
 * 
 * @author WeiLong Peng
 * 
 */
@Service("pubRefSyncRolService")
@Transactional(rollbackFor = Exception.class)
public class PubRefSyncRolServiceImpl implements PubRefSyncRolService {

  /**
   * 
   */
  private static final long serialVersionUID = 5073634525003081633L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubRefSyncRolFlagDao pubRefSyncRolFlagDao;


  @Override
  public void markPubRefSync(Long refId, Integer isDel) throws ServiceException {
    try {
      PubRefSyncRolFlag pubRefSyncRolFlag = pubRefSyncRolFlagDao.get(refId);
      if (pubRefSyncRolFlag == null) {
        pubRefSyncRolFlag = new PubRefSyncRolFlag(refId, isDel, 0);
      } else {
        pubRefSyncRolFlag.setIsDel(isDel);
        pubRefSyncRolFlag.setFlag(0);
      }
      pubRefSyncRolFlagDao.save(pubRefSyncRolFlag);
    } catch (Exception e) {
      logger.error("标记需要同步更新到ROL的文献出现异常", e);
      throw new ServiceException(e);
    }
  }

}
