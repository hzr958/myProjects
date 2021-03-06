package com.smate.center.task.single.service.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.PublicationInsDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.PublicationIns;

/**
 * 成果单位查找匹配.
 * 
 * @author yamingd
 * 
 */
@Service("publicationInsService")
@Transactional(rollbackFor = Exception.class)
public class PublicationInsServiceImpl implements PublicationInsService {

  private final Logger logger = LoggerFactory.getLogger(PublicationInsServiceImpl.class);

  @Autowired
  private PublicationInsDao publicationInsDao;

  @Override
  public PublicationIns lookUpByName(String name) throws ServiceException {
    try {
      return this.publicationInsDao.lookUpByName(name);
    } catch (DaoException e) {
      logger.error("lookUpByName查找单位错误name=" + name, e);
      throw new ServiceException(e);
    }
  }

  public PublicationIns lookUpById(Long insId) throws ServiceException {
    try {
      return this.publicationInsDao.get(insId);
    } catch (Exception e) {
      logger.error("lookUpById查找单位错误insId=" + insId, e);
      throw new ServiceException(e);
    }
  }

}
