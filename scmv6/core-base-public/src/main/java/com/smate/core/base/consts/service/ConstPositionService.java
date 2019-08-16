package com.smate.core.base.consts.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.core.base.consts.model.ConstPosition;

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
  List<ConstPosition> getPosLike(String pos, int size);

  /**
   * 获取指定职务ID的等级.
   * 
   * @param id @return @throws
   */
  Integer getPosGrades(Long id);

  /**
   * 获取职务实体.
   * 
   * @param id @return @throws
   */
  ConstPosition getConstPosition(Long id);

  /**
   * 通过职务名称，获取职务实体.
   * 
   * @param name @return @throws
   */
  ConstPosition getPosByName(String name);

  /**
   * 获取职称的中英文名称
   * 
   * @param id
   * @return
   */
  Map<String, String> getPositionNameById(Long id);
}
