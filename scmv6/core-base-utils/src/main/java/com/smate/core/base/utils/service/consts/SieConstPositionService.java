package com.smate.core.base.utils.service.consts;

import java.util.List;

import com.smate.core.base.utils.exception.SmateException;
import com.smate.core.base.utils.model.consts.SieConstPosition;

/**
 * 职务常量
 * 
 * @author hd
 *
 */
public interface SieConstPositionService {

  /**
   * 获取自动匹配的职务列表.
   * 
   * @param pos
   * @return
   */
  List<SieConstPosition> getPosLike(String pos) throws SmateException;

  /**
   * 通过职务名称，获取职务实体.
   * 
   * @param name
   * @return
   * @throws ServiceException
   */
  SieConstPosition getPosByName(String name) throws SmateException;

  /**
   * 获取第一级职称
   * 
   * @return
   * @throws SmateException
   */
  List<SieConstPosition> getAllFirstPos() throws SmateException;

  /**
   * 获取二级职称
   * 
   * @return
   * @throws SmateException
   */
  String getSecondPosByFirst(String superId) throws SmateException;

  /**
   * 获取指定职务ID的等级.
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  String getPosGrades(Long id) throws SmateException;


}
