package com.smate.center.task.service.rcmd.quartz;

import java.util.List;
import java.util.Map;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.PubDupFields;

/**
 * 成果查重service.
 * 
 * 
 */
public interface PubDupService {

  /**
   * 查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Map<Integer, List<Long>> getDupPub(PubDupFields dupFields) throws ServiceException;

  Map<Integer, List<PubDupFields>> getDupPubField(PubDupFields dupFields, Long ownerId, Long excludeId)
      throws ServiceException;

  Map<Integer, List<PubDupFields>> getDupPubField(PubDupFields dupFields, Long ownerId, Long excludeId, Integer status)
      throws ServiceException;

  /**
   * 查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Map<Integer, List<Long>> getDupPub(PubDupFields dupFields, Long ownerId) throws ServiceException;
}
