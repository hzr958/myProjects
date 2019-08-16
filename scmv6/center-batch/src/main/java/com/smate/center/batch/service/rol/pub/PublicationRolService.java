package com.smate.center.batch.service.rol.pub;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.cnipr.CniprPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.CnkiPatPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.CnkiPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.EiPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.IsiPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.PdwhPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.PubErrorFieldRol;
import com.smate.center.batch.model.rol.pub.PubFillKpiData;
import com.smate.center.batch.model.rol.pub.PubMedPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.PubMemberRol;
import com.smate.center.batch.model.rol.pub.PubPsnCreateRelation;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.rol.pub.RolPubDupFields;
import com.smate.center.batch.model.rol.pub.SpsPubcacheInsAssign;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;

/**
 * 成果创建，修改，删除服务.
 * 
 * @author yamingd
 * 
 */
public interface PublicationRolService extends Serializable {

  /**
   * 获取成果实体，先从自定义缓存读取，如果不存在直接查询数据库.
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  PublicationRol getPublicationById(Long id) throws ServiceException;

  List<PublicationRol> getPublicationByIds(List<Long> pubIds) throws ServiceException;

  /**
   * @param id
   * @return
   * @throws ServiceException
   */
  String getPubXmlById(Long pubId) throws ServiceException;

  /**
   * 获取成果状态.
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  Integer getPubStatusById(Long id) throws ServiceException;

  /**
   * 批量获取单位成果.
   * 
   * @param ids
   * @param insId
   * @return
   * @throws ServiceException
   */
  List<PublicationRol> getPublicationById(List<Long> ids, Long insId) throws ServiceException;

  /**
   * 保存新添加成果.
   * 
   * @param pub
   * @throws ServiceException
   */
  PublicationRol createPublication(PubXmlDocument doc, PubXmlProcessContext context) throws Exception;

  /**
   * 保存成果编辑内容.
   * 
   * @param pub
   * @throws ServcieException
   */
  PublicationRol updatePublication(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws ServiceException;

  /**
   * 保存V2.6同步数据内容.
   * 
   * @param xmlDocument
   * @param context
   * @return
   * @throws ServiceException
   */
  PublicationRol saveSyncOldPublication(PubXmlDocument xmlDocument, PubXmlProcessContext context)
      throws ServiceException;

  /**
   * 同步V2.6单位成果数据.
   * 
   * @param map
   * @throws ServiceException
   */
  void syncOldInsPub(Map<String, Object> map) throws ServiceException;

  /**
   * 删除成果成员.
   * 
   * @param doc
   * @param context
   * @throws ServiceException
   */
  void deletePubMember(PubXmlDocument doc, PubXmlProcessContext context) throws ServiceException;

  /**
   * 修改/添加成果成员.
   * 
   * @param doc
   * @param context
   * @throws ServiceException
   */
  Long savePubMember(PubXmlDocument doc, PubXmlProcessContext context) throws ServiceException;

  /**
   * 删除成果-单位人员关系.
   * 
   * @param doc
   * @param context
   * @throws ServiceException
   */
  void deletePubPsn(PubXmlDocument doc, PubXmlProcessContext context) throws ServiceException;

  /**
   * 添加、删除、保存成果错误字段.
   * 
   * @param doc
   * @return
   * @throws ServiceException
   */
  List<PubErrorFieldRol> savePubErrorFields(PubXmlDocument doc, PubXmlProcessContext context) throws ServiceException;

  /**
   * 把成果成员的名字用逗号分隔连接起来.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  Map<String, String> buildPubAuthorNames(long pubId) throws ServiceException;

  /**
   * 判断2个成果是否同类型.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  boolean isSameType(long pubIdA, long pubIdB) throws ServiceException;

  /**
   * 同步单位成果ID到Submission表.
   * 
   * @param insPubId
   * @param snsPubId
   * @throws ServiceException
   */
  void syncInsPubIdToSubmission(PubXmlDocument xmlDocument, long insPubId, long snsPubId, PubXmlProcessContext context)
      throws ServiceException;

  /**
   * 把成果排入查重队列.
   * 
   * @param context
   * @throws ServiceException
   */
  void sendPubDupCheckQueue(PubXmlProcessContext context) throws ServiceException;

  /**
   * 设置成果确认状态.
   * 
   * @param pubIds
   * @param confirmResult
   * @throws ServiceException
   */
  void setPubConfirmResult(List<Long> pubIds, Integer confirmResult) throws ServiceException;

  /**
   * 设置成果发布状态.
   * 
   * @param pubIds
   * @param confirmResult
   * @throws ServiceException
   */
  void setPubPublish(List<Long> pubIds, Integer openStatus) throws ServiceException;

  /**
   * 批量获取publication表数据，数据同步后重构XML用.
   * 
   * @param lastId
   * @param batchSize
   * @return
   * @throws ServiceException
   */
  public List<PublicationRol> getPubByBatchForOld(Long lastId, int batchSize) throws ServiceException;

  /**
   * 重构导入的成果数据.
   * 
   * @param pub
   * @throws ServiceException
   */
  public void rebuildOldPubData(PublicationRol pub) throws ServiceException;

  /**
   * 重新构造成果XML，生产Brief,校验数据.
   * 
   * @param doc
   * @param context
   * @return
   * @throws Exception
   */
  PublicationRol saveRebuildPublication(PubXmlDocument doc, PubXmlProcessContext context) throws Exception;

  /**
   * 重新构造成果XML，生产Brief,校验数据.
   * 
   * @param doc
   * @param context
   * @throws ServiceException
   */
  void saveRebuildPublication(Long pubId);

  /**
   * 
   * @param lastPubId
   * @param size
   * @throws ServiceException
   */
  List<PublicationRol> loadRebuildPubId(Long lastPubId, int size) throws ServiceException;

  /**
   * 批量获取数据库表中的isi_id导入到XML数据中，数据同步后重构XML用.
   * 
   * @param lastId
   * @param batchSize
   * @return
   * @throws ServiceException
   */
  public List<PublicationRol> loadRebuildIsiId(Long lastId, int batchSize) throws ServiceException;

  /**
   * 重构成果引用次数URL获取成果ID.
   * 
   * @param lastPubId
   * @param size
   * @throws ServiceException
   */
  List<PublicationRol> loadRebuildCiteRecordUrlPubId(Long lastPubId, int size) throws ServiceException;

  /**
   * 将数据库表中的isi_id导入到XML数据中.
   */
  public void rebuildPubXMLIsiId(PublicationRol pub);

  /**
   * 单位端ISI成果匹配到单位记录.
   * 
   * @param xmlId
   * @param pubId
   * @param insId
   * @param imported
   * @return
   * @throws ServiceException
   */
  IsiPubcacheInsAssign saveIsiPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported)
      throws ServiceException;

  /**
   * 单位端PUBMED成果匹配到单位记录.
   * 
   * @param xmlId
   * @param pubId
   * @param insId
   * @param imported
   * @return
   * @throws ServiceException
   */
  public PubMedPubcacheInsAssign savePubMedPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported)
      throws ServiceException;

  /**
   * 单位端CNKI成果匹配到单位记录.
   * 
   * @param xmlId
   * @param pubId
   * @param insId
   * @param imported
   * @return
   * @throws ServiceException
   */
  CnkiPubcacheInsAssign saveCnkiPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported)
      throws ServiceException;

  /**
   * 单位端Cnipr成果匹配到单位记录.
   * 
   * @param xmlId
   * @param pubId
   * @param insId
   * @param imported
   * @return
   * @throws ServiceException
   */
  CniprPubcacheInsAssign saveCniprPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported)
      throws ServiceException;

  /**
   * 单位端CnkiPat成果匹配到单位记录.
   * 
   * @param xmlId
   * @param pubId
   * @param insId
   * @param imported
   * @return
   * @throws ServiceException
   */
  CnkiPatPubcacheInsAssign saveCnkiPatPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported)
      throws ServiceException;

  /**
   * 单位端Scopus成果匹配到单位记录.
   * 
   * @param xmlId
   * @param pubId
   * @param insId
   * @param imported
   * @return
   * @throws ServiceException
   */
  SpsPubcacheInsAssign saveSpsPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported)
      throws ServiceException;

  /**
   * 把成果录入的参数拼成xml，校验xml的正确性.
   * 
   * @param parameterMap
   * @return
   * @throws ServiceException
   */
  Boolean validatePubEnter(Map parameterMap) throws ServiceException;

  /**
   * 标记成果不重复.
   * 
   * @param groupId
   * 
   * @param pubIds
   * 
   * @throws ServiceException
   */
  void markPubNoDup(Long groupId, List<Long> pubIds) throws ServiceException;

  /**
   * 补充成果KPI完整性.
   * 
   * @param fillData
   * @throws ServiceException
   */
  void fillKpiData(PubFillKpiData fillData) throws ServiceException;

  /**
   * 单位代导人员时，查询该成果是否已经被指派到人.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  List<PubMemberRol> getPubMembersByPubId(Long pubId) throws ServiceException;

  /**
   * 确认人员与成果的关联关系.
   * 
   * @param pubIds
   * @param psnId
   * @return key:success成功关联 key:error关联失败，pub_psn找不到pmid.
   * @throws ServiceException
   */
  public Map<String, List<Long>> confirmPsnPubRelation(List<Long> pubIds, Long psnId) throws ServiceException;

  /**
   * 建立人员与成果的确认关联关系.
   * 
   * @param relation
   * @throws ServiceException
   */
  public void createPsnPubRelation(PubPsnCreateRelation relation) throws ServiceException;

  /**
   * 解析并保存成果查重数据.
   * 
   * @param doc
   * @param pub
   * @param context
   * @param now
   * @return
   */
  public RolPubDupFields parsePubDupFields(PubXmlDocument doc, PublicationRol pub) throws ServiceException;

  /**
   * 过滤出不是删除状态的成果来源.
   * 
   * @param pubIds
   * @return
   * @throws ServiceException
   */
  public List<Object[]> getFilterNotDelPubSource(Collection<Long> pubIds) throws ServiceException;

  /**
   * 批量获取成果ID.
   * 
   * @param lastId
   * @param batchSize
   * @return
   * @throws ServiceException
   */
  public List findPubIdsBatch(Long lastId, int batchSize) throws ServiceException;

  /**
   * 更新来源情况.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Long briefDescTask(Long pubId) throws ServiceException;

  /**
   * 查找所有成果ID.
   * 
   * @param lastId
   * @return
   * @throws ServiceException
   */
  public List<Long> findAllPubIdsBatch(Long lastId, int size) throws ServiceException;

  /**
   * 查找院或者系的成果总数.
   * 
   * @param unitIdList
   * @param superUnitId
   * @param insId
   * @return
   * @throws ServiceException
   */
  public List<Map<String, Long>> getPubCountByUnitId(List<Long> unitIdList, Long superUnitId, Long insId)
      throws ServiceException;

  /**
   * 统计其他成果数
   * 
   * @param insId
   * @param parentUnitId
   * @return
   */
  public Map<String, Long> queryPubOtherCount(Long insId, Long parentUnitId) throws ServiceException;

  /**
   * 统计全部成果数
   * 
   * @param insId
   * @param parentUnitId
   * @return
   */
  public Map<String, Long> queryPubCountAllByUnitId(Long unitId, Long insId) throws ServiceException;

  /**
   * 查询人的成果总数.
   * 
   * @param psnIdList
   * @param insId
   * @return
   * @throws ServiceException
   */
  public List<Map<String, Long>> getPubCountByPsnId(List<Long> psnIdList, Long insId) throws ServiceException;

  /**
   * 人员是否已经关联的成果的作者.
   * 
   * @param pubId
   * @param insPsnId
   * @return
   * @throws ServiceException
   */
  boolean isPubMemberLinkPsn(Long pubId, Long insPsnId) throws ServiceException;

  /**
   * 根据成果id获取更新引用所需参数.
   * 
   * @param pubIds
   * @return
   * @throws ServiceException
   */
  String getUpdatePubCitedParams(List<Long> pubIds) throws ServiceException;

  /**
   * 更新指定成果的引用次数.
   * 
   * @param pubIds
   * @param citedXml
   * @throws ServiceException
   */
  void updatePubCitedTimes(List<Long> pubIds, String citedXml) throws ServiceException;

  /**
   * 获取成果分享时需要填写的信息
   * 
   * @param pubId
   * @param domain TODO
   * @return
   * @throws ServiceException
   */
  Map<String, Object> getShowPubData(Long pubId, String domain) throws ServiceException;

  /**
   * pendingConfirm保存数据
   * 
   * @param fillData
   * @throws ServiceException
   */
  public void pendingConfirm(PubFillKpiData fillData) throws ServiceException;


  public EiPubcacheInsAssign saveEiPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported)
      throws ServiceException;

  public PdwhPubcacheInsAssign savePdwhPubCacheInsAssign(Long xmlId, Long long1, Long insId, int i);

  List<PublicationRol> getPublicationList(int batchSize, Long lastPubId);
}
