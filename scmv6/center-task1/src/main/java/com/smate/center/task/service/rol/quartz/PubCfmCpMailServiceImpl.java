package com.smate.center.task.service.rol.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rol.quartz.PubCfmCpMailPubDao;
import com.smate.center.task.dao.rol.quartz.PubCfmCpMailStatDao;
import com.smate.center.task.exception.ServiceException;

@Service("pubCfmCpMailService")
@Transactional(rollbackFor = Exception.class)
public class PubCfmCpMailServiceImpl implements PubCfmCpMailService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubCfmCpMailStatDao pubCfmCpMailStatDao;
  @Autowired
  private PubCfmCpMailPubDao pubCfmCpMailPubDao;

  @Override
  public void markPubCfmCpMailStat(Long psnId, Long pubId, Long insId) throws ServiceException {
    try {
      pubCfmCpMailStatDao.savePubCfmCpMailStat(psnId);
      pubCfmCpMailPubDao.savePubCfmCpMailPub(psnId, insId, pubId);
    } catch (Exception e) {
      logger.error("标记成果认领后通知合作者标记.psnId=" + psnId + ",pubId=" + pubId + ",insId=" + insId, e);
      throw new ServiceException("标记成果认领后通知合作者标记.psnId=" + psnId + ",pubId=" + pubId + ",insId=" + insId, e);
    }
  }

}
