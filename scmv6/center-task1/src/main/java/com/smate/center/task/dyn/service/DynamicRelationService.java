package com.smate.center.task.dyn.service;

import java.util.List;

import com.smate.center.task.dyn.model.base.DynamicMsg;
import com.smate.core.base.utils.exception.DynException;


/**
 * 动态关系服务接口
 * 
 * @author zk
 *
 */
public interface DynamicRelationService {

  /**
   * 获取待处理关系的动态
   * 
   * @param size
   * @return
   * @throws DynException
   */
  List<DynamicMsg> findDynNeedDeal(Integer size) throws DynException;

  /**
   * 处理动态关系
   * 
   * @param msgList
   * @throws DynException
   */
  void handleDynRelation(List<DynamicMsg> msgList) throws DynException;

}
