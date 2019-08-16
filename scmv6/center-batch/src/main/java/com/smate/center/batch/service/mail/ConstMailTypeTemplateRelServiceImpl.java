package com.smate.center.batch.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.mail.ConstMailTypeTemplateRelDao;
import com.smate.center.batch.dao.sns.psn.PsnMailSetDao;
import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 
 * @author zk
 * 
 */
@Service("constMailTypeTemplateRelService")
@Transactional(rollbackFor = Exception.class)
public class ConstMailTypeTemplateRelServiceImpl implements ConstMailTypeTemplateRelService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstMailTypeTemplateRelDao constMailTypeTemplateRelDao;

  @Autowired
  private PsnMailSetDao psnMailSetDao;

  @Override
  public Long getTypeidFromTemplateid(Integer templateId) throws ServiceException {

    try {
      return constMailTypeTemplateRelDao.getTypeidFromTemplateid(templateId);
    } catch (Exception e) {
      logger.error("查询邮件类型与邮件模板关系时出错,templateId=" + templateId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getIsreceive(Long receivePsnId, Long typeId) throws ServiceException {
    try {
      return psnMailSetDao.getIsreceive(receivePsnId, typeId);
    } catch (Exception e) {
      logger.error("查询是否接收邮件出错", e);
      throw new ServiceException(e);
    }
  }
}
