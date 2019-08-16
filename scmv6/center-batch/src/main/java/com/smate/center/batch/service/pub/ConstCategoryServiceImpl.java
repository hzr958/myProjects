package com.smate.center.batch.service.pub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.ConstCategoryDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.ConstCategory;

/**
 * 
 * @author liqinghua
 * 
 */
@Service("constCategoryService")
@Transactional(rollbackFor = Exception.class)
public class ConstCategoryServiceImpl extends EntityManagerImpl<ConstCategory, String> implements ConstCategoryService {

  /**
   * 
   */
  private static final long serialVersionUID = 544766238988399466L;

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstCategoryDao constCategoryDao;

  @Override
  protected ConstCategoryDao getEntityDao() {
    return constCategoryDao;
  }

  @Override
  public Boolean isConstCategoryExist(String id) throws ServiceException {

    try {
      return constCategoryDao.isConstCategoryExit(id);
    } catch (DaoException e) {
      logger.error("ConstCategoryServiceImpl#isConstCategoryExist判断常量类别是否存在错误.", id, e);
      throw new ServiceException("ConstCategoryServiceImpl#isConstCategoryExist判断常量类别是否存在错误.", e);
    }
  }

  @Override
  public List<ConstCategory> getAllConstCategory() {
    return constCategoryDao.getAll();
  }

  @Override
  public ConstCategory getConstCategory(String id) {
    return constCategoryDao.get(id);
  }

  @Override
  public ConstCategory saveConstCategory(ConstCategory constCategory) {
    constCategoryDao.save(constCategory);
    return constCategory;
  }

  @Override
  public Integer removeConstCategory(String id) throws ServiceException {
    try {
      return constCategoryDao.removeConstCategory(id);
    } catch (DaoException e) {
      logger.error("removeConstCategory移除常量错误.id:{}", id, e);
      throw new ServiceException("removeConstCategory移除常量错误.", e);
    }
  }

}
