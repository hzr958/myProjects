package com.smate.core.base.consts.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.ConstPositionDao;
import com.smate.core.base.consts.model.ConstPosition;


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
  public ConstPosition getConstPosition(Long id) {

    try {
      return constPositionDao.get(id);
    } catch (Exception e) {
      logger.error("获取职务实体.", e);
      return null;
    }
  }

  @Override
  public Integer getPosGrades(Long id) {
    try {
      return constPositionDao.getPosGrades(id);
    } catch (Exception e) {
      logger.error("获取指定职务ID的等级.", e);
      return null;
    }
  }

  @Override
  public List<ConstPosition> getPosLike(String pos, int size) {
    try {
      return constPositionDao.getPosLike(pos, size);
    } catch (Exception e) {
      logger.error("获取自动匹配的职务列表.", e);
      return null;
    }
  }

  @Override
  public ConstPosition getPosByName(String name) {
    try {
      return constPositionDao.getPosByName(name);
    } catch (Exception e) {
      logger.error("通过职务名称，获取职务实体.", e);
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, String> getPositionNameById(Long id) {
    if (id == null) {
      return null;
    }
    return constPositionDao.getConstPosition(id);
  }

}
