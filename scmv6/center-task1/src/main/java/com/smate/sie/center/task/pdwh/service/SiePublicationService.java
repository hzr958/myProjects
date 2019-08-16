package com.smate.sie.center.task.pdwh.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.model.SiePubDupFields;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;
import com.smate.sie.center.task.pdwh.task.PubXmlProcessContext;
import com.smate.sie.core.base.utils.model.pub.SiePubErrorField;
import com.smate.sie.core.base.utils.model.pub.SiePubMember;
import com.smate.sie.core.base.utils.model.pub.SiePublication;

/**
 * 成果创建，修改，删除服务.
 * 
 * @author jszhou
 */
public interface SiePublicationService extends Serializable {

  /**
   * 获取成果实体，先从自定义缓存读取，如果不存在直接查询数据库.
   * 
   * @param id
   * @return
   * @throws SysServiceException
   */
  SiePublication getPublicationById(Long id) throws SysServiceException;

  List<SiePublication> getPublicationByIds(List<Long> pubIds) throws SysServiceException;

  /**
   * @param id
   * @return
   * @throws SysServiceException
   */
  String getPubXmlById(Long pubId) throws SysServiceException;

  /**
   * 获取成果状态.
   * 
   * @param id
   * @return
   * @throws SysServiceException
   */
  Integer getPubStatusById(Long id) throws SysServiceException;

  /**
   * 批量获取单位成果.
   * 
   * @param ids
   * @param insId
   * @return
   * @throws SysServiceException
   */
  List<SiePublication> getPublicationById(List<Long> ids, Long insId) throws SysServiceException;

  /**
   * 保存新添加成果.
   * 
   * @param pub
   * @throws SysServiceException
   */
  SiePublication createPublication(PubXmlDocument doc, PubXmlProcessContext context) throws Exception;

  /**
   * 保存成果编辑内容.
   * 
   * @param pub
   * @throws BatchTaskException
   * @throws ServcieException
   */

  SiePublication updatePublication(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception;

  /**
   * 保存V2.6同步数据内容.
   * 
   * @param xmlDocument
   * @param context
   * @return
   * @throws SysServiceException
   */
  SiePublication saveSyncOldPublication(PubXmlDocument xmlDocument, PubXmlProcessContext context)
      throws SysServiceException;

  /**
   * 同步V2.6单位成果数据.
   * 
   * @param map
   * @throws SysServiceException
   */
  void syncOldInsPub(Map<String, Object> map) throws SysServiceException;

  /**
   * 删除成果成员.
   * 
   * @param doc
   * @param context
   * @throws SysServiceException
   */
  void deletePubMember(PubXmlDocument doc, PubXmlProcessContext context) throws SysServiceException;

  /**
   * 修改/添加成果成员.
   * 
   * @param doc
   * @param context
   * @throws SysServiceException
   */
  Long savePubMember(PubXmlDocument doc, PubXmlProcessContext context) throws SysServiceException;

  /**
   * 删除成果-单位人员关系.
   * 
   * @param doc
   * @param context
   * @throws SysServiceException
   */
  void deletePubPsn(PubXmlDocument doc, PubXmlProcessContext context) throws SysServiceException;

  /**
   * 添加、删除、保存成果错误字段.
   * 
   * @param doc
   * @return
   * @throws SysServiceException
   */
  List<SiePubErrorField> savePubErrorFields(PubXmlDocument doc, PubXmlProcessContext context)
      throws SysServiceException;

  /**
   * 把成果成员的名字用逗号分隔连接起来.
   * 
   * @param pubId
   * @return
   * @throws SysServiceException
   */
  Map<String, String> buildPubAuthorNames(long pubId) throws SysServiceException;

  /**
   * 判断2个成果是否同类型.
   * 
   * @param pubId
   * @return
   * @throws SysServiceException
   */
  boolean isSameType(long pubIdA, long pubIdB) throws SysServiceException;

  /**
   * 同步单位成果ID到Submission表.
   * 
   * @param insPubId
   * @param snsPubId
   * @throws SysServiceException
   */
  void syncInsPubIdToSubmission(PubXmlDocument xmlDocument, long insPubId, long snsPubId, PubXmlProcessContext context)
      throws SysServiceException;

  /**
   * 把成果排入查重队列.
   * 
   * @param context
   * @throws SysServiceException
   */
  void sendPubDupCheckQueue(PubXmlProcessContext context) throws SysServiceException;

  /**
   * 设置成果确认状态.
   * 
   * @param pubIds
   * @param confirmResult
   * @throws SysServiceException
   */
  void setPubConfirmResult(List<Long> pubIds, Integer confirmResult) throws SysServiceException;

  /**
   * 设置成果发布状态.
   * 
   * @param pubIds
   * @param confirmResult
   * @throws SysServiceException
   */
  void setPubPublish(List<Long> pubIds, Integer openStatus) throws SysServiceException;

  /**
   * 批量获取publication表数据，数据同步后重构XML用.
   * 
   * @param lastId
   * @param batchSize
   * @return
   * @throws SysServiceException
   */
  public List<SiePublication> getPubByBatchForOld(Long lastId, int batchSize) throws SysServiceException;

  /**
   * 重构导入的成果数据.
   * 
   * @param pub
   * @throws SysServiceException
   */
  public void rebuildOldPubData(SiePublication pub) throws SysServiceException;

  /**
   * 重新构造成果XML，生产Brief,校验数据.
   * 
   * @param doc
   * @param context
   * @return
   * @throws SysServiceException
   */
  SiePublication saveRebuildPublication(PubXmlDocument doc, PubXmlProcessContext context) throws SysServiceException;

  /**
   * 重新构造成果XML，生产Brief,校验数据.
   * 
   * @param doc
   * @param context
   * @throws SysServiceException
   */
  void saveRebuildPublication(Long pubId);

  /**
   * @param lastPubId
   * @param size
   * @throws SysServiceException
   */
  List<SiePublication> loadRebuildPubId(Long lastPubId, int size) throws SysServiceException;

  /**
   * 批量获取数据库表中的isi_id导入到XML数据中，数据同步后重构XML用.
   * 
   * @param lastId
   * @param batchSize
   * @return
   * @throws SysServiceException
   */
  public List<SiePublication> loadRebuildIsiId(Long lastId, int batchSize) throws SysServiceException;

  /**
   * 重构成果引用次数URL获取成果ID.
   * 
   * @param lastPubId
   * @param size
   * @throws SysServiceException
   */
  List<SiePublication> loadRebuildCiteRecordUrlPubId(Long lastPubId, int size) throws SysServiceException;

  /** 将数据库表中的isi_id导入到XML数据中. */
  public void rebuildPubXMLIsiId(SiePublication pub);

  /**
   * 单位端ISI成果匹配到单位记录.
   * 
   * @param xmlId
   * @param pubId
   * @param insId
   * @param imported
   * @return
   * @throws SysServiceException
   */

  /**
   * 把成果录入的参数拼成xml，校验xml的正确性.
   * 
   * @param parameterMap
   * @return
   * @throws SysServiceException
   */
  Boolean validatePubEnter(Map parameterMap) throws SysServiceException;

  /**
   * 标记成果不重复.
   * 
   * @param groupId
   * 
   * @param pubIds
   * 
   * @throws SysServiceException
   */
  // void markPubNoDup(Long groupId, List<Long> pubIds) throws
  // SysServiceException;

  /**
   * 补充成果KPI完整性.
   * 
   * @param fillData
   * @throws SysServiceException
   */
  // void fillKpiData(PubFillKpiData fillData) throws SysServiceException;

  /**
   * 单位代导人员时，查询该成果是否已经被指派到人.
   * 
   * @param pubId
   * @return
   * @throws SysServiceException
   */
  List<SiePubMember> getPubMembersByPubId(Long pubId) throws SysServiceException;

  /**
   * 确认人员与成果的关联关系.
   * 
   * @param pubIds
   * @param psnId
   * @return key:success成功关联 key:error关联失败，pub_psn找不到pmid.
   * @throws SysServiceException
   */
  public Map<String, List<Long>> confirmPsnPubRelation(List<Long> pubIds, Long psnId) throws SysServiceException;

  /**
   * 建立人员与成果的确认关联关系.
   * 
   * @param relation
   * @throws SysServiceException
   */
  // public void createPsnPubRelation(PubPsnCreateRelation relation) throws
  // SysServiceException;

  /**
   * 解析并保存成果查重数据.
   * 
   * @param doc
   * @param pub
   * @param context
   * @param now
   * @return
   */
  public SiePubDupFields parsePubDupFields(PubXmlDocument doc, SiePublication pub) throws SysServiceException;

  /**
   * 过滤出不是删除状态的成果来源.
   * 
   * @param pubIds
   * @return
   * @throws SysServiceException
   */
  public List<Object[]> getFilterNotDelPubSource(Collection<Long> pubIds) throws SysServiceException;

  /**
   * 批量获取成果ID.
   * 
   * @param lastId
   * @param batchSize
   * @return
   * @throws SysServiceException
   */
  public List findPubIdsBatch(Long lastId, int batchSize) throws SysServiceException;

  /**
   * 更新来源情况.
   * 
   * @param pubId
   * @return
   * @throws SysServiceException
   */
  public Long briefDescTask(Long pubId) throws SysServiceException;

  /**
   * 查找所有成果ID.
   * 
   * @param lastId
   * @return
   * @throws SysServiceException
   */
  public List<Long> findAllPubIdsBatch(Long lastId, int size) throws SysServiceException;

  /**
   * 查找院或者系的成果总数.
   * 
   * @param unitIdList
   * @param superUnitId
   * @param insId
   * @return
   * @throws SysServiceException
   */
  public List<Map<String, Long>> getPubCountByUnitId(List<Long> unitIdList, Long superUnitId, Long insId)
      throws SysServiceException;

  /**
   * 统计其他成果数
   * 
   * @param insId
   * @param parentUnitId
   * @return
   */
  public Map<String, Long> queryPubOtherCount(Long insId, Long parentUnitId) throws SysServiceException;

  /**
   * 统计全部成果数
   * 
   * @param insId
   * @param parentUnitId
   * @return
   */
  public Map<String, Long> queryPubCountAllByUnitId(Long unitId, Long insId) throws SysServiceException;

  /**
   * 查询人的成果总数.
   * 
   * @param psnIdList
   * @param insId
   * @return
   * @throws SysServiceException
   */
  public List<Map<String, Long>> getPubCountByPsnId(List<Long> psnIdList, Long insId) throws SysServiceException;

  /**
   * 人员是否已经关联的成果的作者.
   * 
   * @param pubId
   * @param insPsnId
   * @return
   * @throws SysServiceException
   */
  boolean isPubMemberLinkPsn(Long pubId, Long insPsnId) throws SysServiceException;

  /**
   * 根据成果id获取更新引用所需参数.
   * 
   * @param pubIds
   * @return
   * @throws SysServiceException
   */
  String getUpdatePubCitedParams(List<Long> pubIds) throws SysServiceException;

  /**
   * 更新指定成果的引用次数.
   * 
   * @param pubIds
   * @param citedXml
   * @throws SysServiceException
   */
  void updatePubCitedTimes(List<Long> pubIds, String citedXml) throws SysServiceException;

  /**
   * 获取成果分享时需要填写的信息
   * 
   * @param pubId
   * @param domain TODO
   * @return
   * @throws SysServiceException
   */
  Map<String, Object> getShowPubData(Long pubId, String domain) throws SysServiceException;

  /**
   * pendingConfirm保存数据
   * 
   * @param fillData
   * @throws SysServiceException
   */
  // public void pendingConfirm(PubFillKpiData fillData) throws
  // SysServiceException;

  void uploadPubXmlFulltext(Long pubId, Map<String, Object> parameterMap, Long insId);
}
