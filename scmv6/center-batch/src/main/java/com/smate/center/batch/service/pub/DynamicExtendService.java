package com.smate.center.batch.service.pub;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.DynamicExtend;

/**
 * 动态扩展信息接口.
 * 
 * @author chenxiangrong
 * 
 */
public interface DynamicExtendService {
  /**
   * 保存动态扩展信息.
   * 
   * @param resDetailJson
   * @throws ServiceException
   */
  void saveDynamicExtends(String resDetailJson, Long dynId, int resType) throws ServiceException;

  /**
   * 获取扩展信息.
   * 
   * @param dynId
   * @return
   * @throws ServiceException
   */
  List<DynamicExtend> getDynamicExtends(Long dynId) throws ServiceException;
}
