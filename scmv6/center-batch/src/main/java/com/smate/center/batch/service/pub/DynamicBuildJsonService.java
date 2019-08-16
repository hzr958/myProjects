package com.smate.center.batch.service.pub;

import net.sf.json.JSONObject;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 动态接口.
 * 
 * @author chenxiangrong
 * 
 */
public interface DynamicBuildJsonService {
  /**
   * 添加成果、文献动态信息.
   * 
   * @param psnid
   * @param jsonObject
   * @return
   * @throws ServiceException
   */
  String addPubRefDynamic(Long psnId, JSONObject jsonObject) throws ServiceException;

}
