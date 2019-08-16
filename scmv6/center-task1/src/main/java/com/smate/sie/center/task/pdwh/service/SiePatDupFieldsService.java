package com.smate.sie.center.task.pdwh.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.model.PatDupParam;
import com.smate.sie.center.task.model.SiePatDupFields;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 专利查重service.
 * 
 * @author jszhou
 *
 */
public interface SiePatDupFieldsService extends Serializable {

  /**
   * 成果导入查重,key=1严格，key=2宽松，查询到严格的，直接返回.
   * 
   * @param doc
   * @return
   * @throws SysServiceException
   */
  public Map<Integer, List<Long>> getDupPatByImportPat(PubJsonDTO pubJson) throws SysServiceException;

  public Map<Integer, List<Long>> getDupPat(PatDupParam param, Long ownerId) throws SysServiceException;

  public Map<Integer, List<SiePatDupFields>> getDupPatField(PatDupParam param, Long ownerId) throws SysServiceException;

  public Map<Integer, List<SiePatDupFields>> getDupPatField(PatDupParam param, Long ownerId, Integer status)
      throws SysServiceException;

  public SiePatDupFields savePatDupFields(PatDupParam param, Long patId, Long ownerId, boolean canDup)
      throws SysServiceException;

  public SiePatDupFields savePatDupFields(PatDupParam param, Long patId, Long ownerId, Integer status)
      throws SysServiceException;

  /**
   * 单位专利合并任务
   * 
   * @param pubJson
   * @return
   */
  public boolean getDupPatStatus(PubJsonDTO pubJson);
}
