package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PubDupFields;
import com.smate.center.batch.model.sns.pub.PubDupParam;
import com.smate.center.batch.model.sns.pub.PubDuplicatePO;

/**
 * 成果查重service.
 * 
 * @author liqinghua
 * 
 */
public interface PubDupService extends Serializable {

  /**
   * 查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Map<Integer, List<Long>> getDupPub(Long pubId) throws ServiceException;

  /**
   * 查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Map<Integer, List<Long>> getDupPub(PubDupFields dupFields) throws ServiceException;

  /**
   * 查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Map<Integer, List<Long>> getDupPub(PubDupFields dupFields, Long ownerId) throws ServiceException;

  /**
   * 查询重复成果Id.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public List<Long> getDupPubIds(Long pubId) throws ServiceException;

  /**
   * 查询重复成果,key=1严格，key=2宽松，查询到严格的，直接返回.
   * 
   * @param pubId
   * @param ownerId
   * @return
   * @throws ServiceException
   */
  public Map<Integer, List<Long>> getDupPub(PubDupParam param, Long ownerId) throws ServiceException;

  /**
   * 保存成果查重字段.
   * 
   * @param param
   * @param articleType
   * @param canDup
   * 
   * @return
   * @throws ServiceException
   */
  public PubDupFields savePubDupFields(PubDupParam param, Integer pubType, Long pubId, Long ownerId,
      Integer articleType, boolean canDup) throws ServiceException;

  /**
   * 保存成果查重字段.
   * 
   * @param param
   * @param articleType
   * @param canDup
   * 
   * @return
   * @throws ServiceException
   */
  public PubDupFields savePubDupFields(PubDupParam param, Integer pubType, Long pubId, Long ownerId,
      Integer articleType, Integer status) throws ServiceException;

  /**
   * 成果导入查重,key=1严格，key=2宽松，查询到严格的，直接返回.
   * 
   * @param doc
   * @return
   * @throws ServiceException
   */
  public Map<Integer, List<Long>> getDupPubByImportPub(Element pubEle, Long ownerId) throws ServiceException;

  /**
   * 成果导入查重.
   * 
   * @param doc
   * @return
   * @throws ServiceException
   */
  public List<Long> getDupPubByImportPubAll(Element pubEle, Long ownerId) throws ServiceException;

  /**
   * 成果导入严格查重.
   * 
   * @param doc
   * @return
   * @throws ServiceException
   */
  public List<Long> getStrictDupPubByImportPub(Element pubEle, Long ownerId) throws ServiceException;

  /**
   * 成果导入严格查重.
   * 
   * @param doc
   * @return
   * @throws ServiceException
   */
  public List<Long> getStrictDupPubByImportPub(Element pubEle, Long ownerId, Integer status) throws ServiceException;

  /**
   * 删除查重数据，删除成果时，将此数据删除.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void removeById(Long pubId) throws ServiceException;

  /**
   * 设置成果能够参与查重.
   * 
   * @param pubId
   * @param canDup
   * @throws ServiceException
   */
  public void setPubCanDup(Long pubId, boolean canDup) throws ServiceException;

  /**
   * 设置成果能够参与查重，并更新articleType字段
   * 
   * @param pubId
   * @param canDup
   * @param articleType
   * @throws ServiceException
   */
  public void setPubCanDupAndArticleType(Long pubId, boolean canDup, Integer articleType) throws ServiceException;

  /**
   * 获取成果查重信息.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public PubDupFields getPubDupFields(Long pubId) throws ServiceException;

  /**
   * getDupPub查询重复成果,key=1严格，key=2宽松.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Map<Integer, List<PubDupFields>> getDupPubField(Long pubId) throws ServiceException;

  /**
   * getDupPub查询重复成果,key=1严格，key=2宽松.
   * 
   * @param pubId
   * @param status 参与查重的状态 PubDupFields.status
   * @return
   * @throws ServiceException
   */
  public Map<Integer, List<PubDupFields>> getDupPubField(Long pubId, Integer status) throws ServiceException;

  /**
   * 查重重复成果,key=1严格，key=2宽松.
   * 
   * @param param
   * @param ownerId
   * @return
   * @throws ServiceException
   */
  public Map<Integer, List<PubDupFields>> getDupPubField(PubDupParam param, Long ownerId) throws ServiceException;

  /**
   * 查重重复成果,key=1严格，key=2宽松.
   * 
   * @param param
   * @param ownerId
   * @return
   * @throws ServiceException
   */
  public Map<Integer, List<PubDupFields>> getDupPubField(PubDupParam param, Long ownerId, Long excludeId)
      throws ServiceException;

  /**
   * 查重重复成果,key=1严格，key=2宽松.
   * 
   * @param param
   * @param ownerId
   * @return
   * @throws ServiceException
   */
  public Map<Integer, List<PubDupFields>> getDupPubField(PubDupParam param, Long ownerId, Long excludeId,
      Integer status) throws ServiceException;

  /**
   * 查重重复成果,key=1严格，key=2宽松.
   * 
   * @param param
   * @param ownerId
   * @return
   * @throws ServiceException
   */
  public Map<Integer, List<PubDupFields>> getDupPubField(PubDupFields dupFields, Long ownerId, Long excludeId)
      throws ServiceException;

  /**
   * 查重重复成果,key=1严格，key=2宽松.
   * 
   * @param param
   * @param ownerId
   * @return
   * @throws ServiceException
   */
  public Map<Integer, List<PubDupFields>> getDupPubField(PubDupFields dupFields, Long ownerId, Long excludeId,
      Integer status) throws ServiceException;

  /**
   * set un dup status.
   * 
   * @param pubIds
   * @throws ServiceException
   */
  public void setDupDisabled(List<Long> pubIds) throws ServiceException;

  public PubDupFields get(Long id) throws ServiceException;

  /**
   * 跟新成果查重表 成果所有者 id
   * 
   * @param pubId
   * @param ownerId
   * @throws ServiceException
   */
  public void updateDupOwner(Long pubId, Long ownerId) throws ServiceException;

  public PubDuplicatePO getPubDuplicatePO(Long pubId);

}
