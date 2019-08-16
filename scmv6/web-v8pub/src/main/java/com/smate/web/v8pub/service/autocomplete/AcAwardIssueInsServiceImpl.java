package com.smate.web.v8pub.service.autocomplete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.web.v8pub.dao.autocomplete.AcAwardIssueInsDao;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.autocomplete.AcAwardIssueIns;

/**
 * 颁奖机构服务类
 * 
 * @author YJ
 *
 *         2018年10月16日
 */
@Service("acAwardIssueInsService")
@Transactional(rollbackFor = Exception.class)
public class AcAwardIssueInsServiceImpl implements AcAwardIssueInsService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private AcAwardIssueInsDao acAwardIssueInsDao;

  @Override
  public AcAwardIssueIns get(Long code) throws ServiceException {
    try {
      return acAwardIssueInsDao.get(code);
    } catch (Exception e) {
      logger.error("通过奖励id获取奖励对象出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(AcAwardIssueIns acAwardIssueIns) throws ServiceException {
    try {
      acAwardIssueInsDao.save(acAwardIssueIns);
    } catch (Exception e) {
      logger.error("保存奖励对象出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(AcAwardIssueIns acAwardIssueIns) throws ServiceException {
    try {
      acAwardIssueInsDao.update(acAwardIssueIns);
    } catch (Exception e) {
      logger.error("更新奖励对象出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(AcAwardIssueIns acAwardIssueIns) throws ServiceException {
    try {
      acAwardIssueInsDao.saveOrUpdate(acAwardIssueIns);
    } catch (Exception e) {
      logger.error("保存或更新奖励对象出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(AcAwardIssueIns acAwardIssueIns) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public AcAwardIssueIns getByName(String name) throws ServiceException {
    try {
      return acAwardIssueInsDao.getByName(name);
    } catch (Exception e) {
      logger.error("通过奖励的name匹配奖励对象出错！", e);
      throw new ServiceException(e);
    }
  }
}
