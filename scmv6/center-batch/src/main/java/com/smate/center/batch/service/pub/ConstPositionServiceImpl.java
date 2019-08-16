package com.smate.center.batch.service.pub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.ConstPositionDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.ConstPosition;

/**
 * 职务常量.
 * 
 * @author liqinghua
 * 
 */
@Transactional(rollbackFor = Exception.class)
@Service("constPositionService")
public class ConstPositionServiceImpl implements ConstPositionService {

  /**
   * 
   */
  private static final long serialVersionUID = -3035678416459597996L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstPositionDao constPositionDao;

  @Override
  public ConstPosition getConstPosition(Long id) throws ServiceException {

    try {
      return constPositionDao.get(id);
    } catch (Exception e) {
      logger.error("获取职务实体.", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Integer getPosGrades(Long id) throws ServiceException {
    try {
      return constPositionDao.getPosGrades(id);
    } catch (Exception e) {
      logger.error("获取指定职务ID的等级.", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<ConstPosition> getPosLike(String pos, int size) throws ServiceException {
    try {
      return constPositionDao.getPosLike(pos, size);
    } catch (Exception e) {
      logger.error("获取自动匹配的职务列表.", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public ConstPosition getPosByName(String name) throws ServiceException {
    try {
      return constPositionDao.getPosByName(name);
    } catch (Exception e) {
      logger.error("通过职务名称，获取职务实体.", e);
      throw new ServiceException(e);
    }
  }

}
