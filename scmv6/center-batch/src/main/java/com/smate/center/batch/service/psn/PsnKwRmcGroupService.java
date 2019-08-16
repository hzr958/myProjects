package com.smate.center.batch.service.psn;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * @author lichangwen
 * 
 */
public interface PsnKwRmcGroupService {

  /**
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  int getMatchPsnKwRmcGids(Long psnId, List<String> refKws) throws ServiceException;

  /**
   * 根据成果关键词匹配人员关键词
   * 
   * @param pubKws
   * @return
   * @throws ServiceException
   */
  List<Map<String, Object>> getMatchPsnIds(List<String> pubKws) throws ServiceException;

  List<String> querykwBykwTxt(String kwTxt) throws ServiceException;

}
