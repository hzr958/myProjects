package com.smate.sie.center.task.pdwh.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.model.PubDupParam;
import com.smate.sie.center.task.model.SiePubDupFields;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 成果查重service.
 * 
 * @author jszhou
 */
public interface SiePubDupFieldsService extends Serializable {

  /**
   * 查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.
   * 
   * @param pubId
   * @return
   * @throws SysServiceException
   */
  public Map<Integer, List<Long>> getDupPub(Long pubId) throws SysServiceException;

  /**
   * 查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.
   * 
   * @param pubId
   * @return
   * @throws SysServiceException
   */
  public Map<Integer, List<Long>> getDupPub(SiePubDupFields dupFields) throws SysServiceException;

  /**
   * 查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.
   * 
   * @param pubId
   * @return
   * @throws SysServiceException
   */
  public Map<Integer, List<Long>> getDupPub(SiePubDupFields dupFields, Long ownerId) throws SysServiceException;

  /**
   * 查询重复成果Id.
   * 
   * @param pubId
   * @return
   * @throws SysServiceException
   */
  public List<Long> getDupPubIds(Long pubId) throws SysServiceException;

  /**
   * 查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.
   * 
   * @param pubId
   * @param ownerId
   * @return
   * @throws SysServiceException
   */
  public Map<Integer, List<Long>> getDupPub(PubDupParam param, Long ownerId) throws SysServiceException;

  /**
   * 保存成果查重字段.
   * 
   * @param param
   * @param articleType
   * @param canDup
   * 
   * @return
   * @throws SysServiceException
   */
  public SiePubDupFields savePubDupFields(PubDupParam param, Integer pubType, Long pubId, Long ownerId, boolean canDup)
      throws SysServiceException;

  /**
   * 保存成果查重字段.
   * 
   * @param param
   * @param articleType
   * @param canDup
   * 
   * @return
   * @throws SysServiceException
   */
  public SiePubDupFields savePubDupFields(PubDupParam param, Integer pubType, Long pubId, Long ownerId, Integer status)
      throws SysServiceException;

  /**
   * 基准库指派成果查重，与sie文件导入查重逻辑一致 2019-3-14
   * 
   * @param pubJson
   * @return
   * @throws SysServiceException
   */
  public Map<Integer, List<Long>> getDupPubByImportPub(PubJsonDTO pubJson) throws SysServiceException;



  /**
   * 获取成果查重信息.
   * 
   * @param pubId
   * @return
   * @throws SysServiceException
   */
  public SiePubDupFields getPubDupFields(Long pubId) throws SysServiceException;

  /**
   * getDupPub查询重复成果,key=1严格，key=2宽松.
   * 
   * @param pubId
   * @return
   * @throws SysServiceException
   */
  public Map<Integer, List<SiePubDupFields>> getDupPubField(Long pubId) throws SysServiceException;

  /**
   * getDupPub查询重复成果,key=1严格，key=2宽松.
   * 
   * @param pubId
   * @param status 参与查重的状态 SiePubDupFields.status
   * @return
   * @throws SysServiceException
   */
  public Map<Integer, List<SiePubDupFields>> getDupPubField(Long pubId, Integer status) throws SysServiceException;

  /**
   * 查重重复成果,key=1严格，key=2宽松.
   * 
   * @param param
   * @param ownerId
   * @return
   * @throws SysServiceException
   */
  public Map<Integer, List<SiePubDupFields>> getDupPubField(PubDupParam param, Long ownerId) throws SysServiceException;

  /**
   * 查重重复成果,key=1严格，key=2宽松.
   * 
   * @param param
   * @param ownerId
   * @return
   * @throws SysServiceException
   */
  public Map<Integer, List<SiePubDupFields>> getDupPubField(PubDupParam param, Long ownerId, Long excludeId)
      throws SysServiceException;

  /**
   * 查重重复成果,key=1严格，key=2宽松.
   * 
   * @param param
   * @param ownerId
   * @return
   * @throws SysServiceException
   */
  public Map<Integer, List<SiePubDupFields>> getDupPubField(PubDupParam param, Long ownerId, Long excludeId,
      Integer status) throws SysServiceException;

  /**
   * 查重重复成果,key=1严格，key=2宽松.
   * 
   * @param param
   * @param ownerId
   * @return
   * @throws SysServiceException
   */
  public Map<Integer, List<SiePubDupFields>> getDupPubField(SiePubDupFields dupFields, Long ownerId, Long excludeId)
      throws SysServiceException;

  /**
   * 查重重复成果,key=1严格，key=2宽松.
   * 
   * @param param
   * @param ownerId
   * @return
   * @throws SysServiceException
   */
  public Map<Integer, List<SiePubDupFields>> getDupPubField(SiePubDupFields dupFields, Long ownerId, Long excludeId,
      Integer status) throws SysServiceException;

  /**
   * set un dup status.
   * 
   * @param pubIds
   * @throws SysServiceException
   */
  public void setDupDisabled(List<Long> pubIds) throws SysServiceException;

  public SiePubDupFields get(Long id) throws SysServiceException;

  /**
   * 跟新成果查重表 成果所有者 id
   * 
   * @param pubId
   * @param ownerId
   * @throws SysServiceException
   */
  public void updateDupOwner(Long pubId, Long ownerId) throws SysServiceException;

  /**
   * 单位成果合并任务
   * 
   * @param pubJson
   * @return
   */
  public boolean getDupPubStatus(PubJsonDTO pubJson);
}
