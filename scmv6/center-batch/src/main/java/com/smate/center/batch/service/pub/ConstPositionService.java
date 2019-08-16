package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.ConstPosition;

/**
 * 职务常量.
 * 
 * @author liqinghua
 * 
 */
public interface ConstPositionService extends Serializable {

  /**
   * 获取自动匹配的职务列表.
   * 
   * @param pos
   * @param size
   * @return
   */
  List<ConstPosition> getPosLike(String pos, int size) throws ServiceException;

  /**
   * 获取指定职务ID的等级.
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  Integer getPosGrades(Long id) throws ServiceException;

  /**
   * 获取职务实体.
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  ConstPosition getConstPosition(Long id) throws ServiceException;

  /**
   * 通过职务名称，获取职务实体.
   * 
   * @param name
   * @return
   * @throws ServiceException
   */
  ConstPosition getPosByName(String name) throws ServiceException;
}
