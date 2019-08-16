package com.smate.web.v8pub.service.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.dao.job.PubPdwhScmRolDao;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.job.PubPdwhScmRol;

@Service("pubPdwhScmRolService")
@Transactional(rollbackFor = Exception.class)
public class PubPdwhScmRolServiceImpl implements PubPdwhScmRolService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhScmRolDao pubPdwhScmRolDao;

  @Override
  public void save(PubPdwhScmRol pubPdwhScmRol) throws ServiceException {
    try {
      pubPdwhScmRolDao.save(pubPdwhScmRol);
    } catch (Exception e) {
      logger.error("保存scm对应至pdwh临时表的任务记录出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PubPdwhScmRol get(Long pubId) throws ServiceException {
    try {
      return pubPdwhScmRolDao.get(pubId);
    } catch (Exception e) {
      logger.error("获取scm对应至pdwh临时表的任务记录出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PubPdwhScmRol entity) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void saveOrUpdate(PubPdwhScmRol pubPdwhScmRol) throws ServiceException {
    try {
      pubPdwhScmRolDao.saveOrUpdate(pubPdwhScmRol);
    } catch (Exception e) {
      logger.error("保存或者更新scm对应至pdwh临时表的任务记录出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(PubPdwhScmRol entity) throws ServiceException {
    // TODO Auto-generated method stub

  }

}
