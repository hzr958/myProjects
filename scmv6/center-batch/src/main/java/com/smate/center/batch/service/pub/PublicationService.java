package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PubAuthor;
import com.smate.center.batch.model.sns.pub.PubMember;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.PublicationForm;
import com.smate.center.batch.model.sns.pub.PublicationList;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.model.Page;

/**
 * 成果SERVICE接口.
 * 
 * @author liqinghua
 * 
 */
public interface PublicationService extends Serializable {

  /**
   * 删除成果.
   * 
   * @param pubId
   * @throws ServiceException
   */
  void deletePublication(String getPubIds) throws ServiceException;

  /**
   * 删除成果补充过程（低优先级任务）.
   * 
   * @param pubId
   * @throws BatchTaskException
   */
  public void deletePublicationComplementaryProcesses(String getPubIds) throws BatchTaskException;



  /**
   * 读取成果.
   * 
   * @param form
   * @return publication
   * @throws ServiceException
   */
  public PublicationForm getPublication(PublicationForm form) throws ServiceException;



  /**
   * 基准库批量导入我的成果文献库时，查重显示列表.
   * 
   * @param pdwhPuballKey
   * @return
   * @throws ServiceException
   */
  public List<Publication> getPdwhPubs(List<Map<String, Object>> pdwhPuballKey) throws ServiceException;



  /**
   * 通过pubid获取成果XML.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public String getPubXmlById(Long pubId) throws ServiceException;

  /**
   * 直接用XML更新成果数据.
   * 
   * @param xml
   * @param pubId
   * @throws ServiceException
   */
  public void updatePublication(String xml, PubXmlProcessContext context) throws ServiceException;

  /**
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Publication getPublicationById(Long pubId) throws ServiceException;

  /**
   * 获取成果总数.
   * 
   * @param psnId
   * @return TODO
   * @throws ServiceException
   */
  public Long getTotalPubsByPsnId(Long psnId) throws ServiceException;



  /**
   * 获取同步数据的新成果.
   * 
   * @param oldPubId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public Publication getPubByOldPub(Long oldPubId, Long psnId) throws ServiceException;

  /**
   * 获取成果表中source_db_id=2并且在publist中被isi收录的数据.
   * 
   * @param lastId
   * @param batchSize
   * @return
   * @throws ServiceException
   */
  List<PublicationList> getPubListByBatchForSourceDbId2(Long lastId, int batchSize) throws ServiceException;

  /**
   * 重构pubXml，pub表source_db_id=2并且在publist中被isi收录.
   * 
   * @param pubId
   * @throws ServiceException
   */
  void rebuildPubXmlByPubSourceDbId2(PublicationList pubList) throws ServiceException;

  /**
   * 重构成果全文附件权限.
   * 
   * @param pub
   * @throws ServiceException
   */
  public void rebuildPubFulltext(Publication pub) throws ServiceException;

  /**
   * 成果同步到结题报告.
   * 
   * @param publicationForm
   * @throws ServiceException
   */
  void isSyncPublicationToFinalReport(PublicationForm publicationForm) throws ServiceException;



  /**
   * 同步专家成果数据.
   * 
   * @param loadXml
   * @throws ServiceException
   */
  void isSyncPublicationToExpertPub(PublicationForm loadXml) throws ServiceException;

  void isSyncPublicationToLabPub(PublicationForm loadXml) throws ServiceException;



  /**
   * 获取成果.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  Publication getPub(Long pubId) throws ServiceException;

  /**
   * 计算在指定的成果中某个成果类型的个数.
   * 
   * @param pubIds
   * @param type
   * @return
   * @throws ServiceException
   */
  Integer queryPubTotalByPubIdAndType(String pubIds, String type) throws ServiceException;

  /**
   * 人员推荐-查询人员所有成果，字段限成果查重字段.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<Publication> findPubByPnsId(Long psnId) throws ServiceException;

  /**
   * 人员推荐-根据成果查找相同成果、文献人员.
   * 
   * @param pub
   * @return
   * @throws ServiceException
   */
  List<Map<String, Object>> matchPubPsnIds(Publication pub) throws ServiceException;

  List<Map<String, Object>> matchRefPsnIds(Publication pub) throws ServiceException;

  /**
   * 查询.
   * 
   * @param pubIds
   * @return
   * @throws ServiceException
   */
  List<Publication> findPubsByIds(List<Long> pubIds) throws ServiceException;

  /**
   * 查找tmPsnId的成果合作者中是否likeName的人员.
   * 
   * @param tmPsnId
   * @return
   * @throws ServiceException
   */
  int pubMatchName(Long tmPsnId, String zhName, String likeName) throws ServiceException;

  /**
   * 批量查询PubId.
   * 
   * @param lastId
   * @param batchSize
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("rawtypes")
  List findPubIdsBatch(Long lastId, int batchSize) throws ServiceException;

  /**
   * 获取成果拥有者Id.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  Publication getPubOwnerPsnIdOrStatus(Long pubId) throws ServiceException;


  /**
   * @author liangguokeng
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<Publication> findPubIdsByPsnId(Long psnId) throws ServiceException;

  /**
   * 更新成果全文附件.
   * 
   * @param pubId
   * @param params
   * @param dynamic
   * @return
   * @throws ServiceException
   */
  Map<String, String> updatePubXmlFulltext(Long pubId, Map<String, Object> params, boolean dynamic)
      throws ServiceException;

  /**
   * 通过成果ID查询成果合作者
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public List<PubMember> getPubMembersByPubId(Long pubId) throws ServiceException;

  /**
   * 获取成果标题摘要关键词部分信息
   * 
   * @return
   * @throws ServiceException
   */
  public Map<String, String> getPubPartDetail(Long pubId) throws ServiceException;

  /**
   * 获取成果的标题.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public String getPublicationTitle(Long pubId) throws ServiceException;

  /**
   * 根据成果id查询是否是当前登录用户的成果.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  boolean isCurrPsnPub(Long pubId) throws ServiceException;

  /**
   * 根据一系列成果id获取成果
   * 
   * @param pubIds
   * @return
   * @throws ServiceException
   */
  public List<Publication> getPubListByPubIds(List<Long> pubIds) throws ServiceException;


  /**
   * @param pubIds
   * @return
   * @throws ServiceException
   */
  List<Map<String, Object>> findPdwhPubLinkeScmPubs(Long pubId, int dbid) throws ServiceException;

  List<Map> getLastMonthPsnPubs(Integer size) throws ServiceException;

  List<Long> getAllPsnIdBySize(Integer size) throws ServiceException;

  /**
   * 判断记录是否删除.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  boolean checkPubIsDeleteByPubId(Long pubId) throws ServiceException;

  /**
   * 取得分享成果到站外所需要的成果信息.
   * 
   * @param pubId
   * @return 以json格式返回
   */
  String getSharePubContent(Long pubId);

  /**
   * 刷新成果合作者计算需要的数据.
   * 
   * @param pubId
   * @throws ServiceException
   */
  void savePubKnow(Long pubId, Long psnId, int isOwner) throws ServiceException;

  /**
   * 查询某人的公开成果数（IRIS业务系统接口使用）.
   * 
   * @param psnId
   * @param keywords
   * @param excludedPubIds 以,号分割的pubId字符串.
   * @param pubTypes TODO
   * @return
   * @throws ServiceException
   */
  Long getPsnPublicPubCount(Long psnId, String keywords, String excludedPubIds, List<Integer> permissions,
      String pubTypes) throws ServiceException;

  /**
   * 分页获取指定用户公开成果记录（IRIS业务系统接口使用）.
   * 
   * @param psnId
   * @param keywords
   * @param excludedPubIds 以,号分割的pubId字符串.
   * @param permissions
   * @param sortType
   * @param pubTypes TODO
   * @return
   */
  Page<Publication> getPsnPublicPubByPage(Long psnId, String keywords, String excludedPubIds, List<Integer> permissions,
      String sortType, Page<Publication> page, String pubTypes) throws ServiceException;

  /**
   * 查询某人的成果数（IRIS业务系统接口使用）,应华师要求增加作者参数.
   * 
   * @param psnId
   * @param keywords
   * @param excludedPubIds
   * @param pubTypes TODO
   * @return
   * @throws ServiceException
   */
  Long getPsnPubCount(Long psnId, String keywords, String authors, String excludedPubIds, String pubTypes)
      throws ServiceException;

  /**
   * 分页获取指定用户成果记录（IRIS业务系统接口使用）.
   * 
   * @param psnId
   * @param keywords
   * @param excludedPubIds 以,号分割的pubId字符串.
   * @param sortType
   * @param pubTypes TODO
   * @return
   */
  Page<Publication> getPsnPubByPage(Long psnId, String keywords, String authors, String excludedPubIds, String sortType,
      Page<Publication> page, String pubTypes) throws ServiceException;

  /**
   * 检验成果是否属于某用户.
   * 
   * @param pubId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  boolean checkPubBelongToPsn(Long pubId, Long psnId) throws ServiceException;

  /**
   * 批量查询上周发表过成果的人
   */

  List<Long> getPsnIdByLastWeekly(Integer size, Long lastPsnId) throws ServiceException;

  @Deprecated
  Map<String, Object> findPubLastWeekByPsnId(Long psnId, Long configId) throws ServiceException;

  Map<String, Object> findPubLastWeekByPsnId(Long psnId) throws ServiceException;

  Map<String, Object> getShowPubData(Long pubId) throws ServiceException;

  Map<String, String> getPubTitleById(Long pubId);

  Map<String, Object> getShowPdwhPubData(Long pubId, Integer dbid) throws ServiceException;

  /**
   * 基准库成果详情页面之相关成果
   * 
   * @param page
   * @param pubId
   * @param dbid
   * @return
   * @throws ServiceException
   */
  Page findPdwhRelatedPubs(Page page, Long pubId, int dbid) throws ServiceException;

  /**
   * 基准库成果详情页面之查找相关成果信息
   * 
   * @param pubId
   * @param dbid
   * @return
   * @throws ServiceException
   */
  Map<String, Object> findPdwhRelatedPub(Long pubId, int dbid) throws ServiceException;

  /**
   * 根据基准库ID和DBID查找出sns的成果的Authors
   * 
   * @param id
   * @param dbId
   * @return
   * @throws ServiceException
   */
  public Page<PubAuthor> findSnsPubAuthorList(Page page, Long id, Integer dbId) throws ServiceException;

  /**
   * 根据pubId查找出sns的其它相关成果的Authors
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Page<PubAuthor> findSnsPubAuthorListByPubId(Page page, Long pubId) throws ServiceException;

  /**
   * 查找其它相关成果的pubId
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  Long findOtherRelatedPubId(Long pubId) throws ServiceException;

  /**
   * 获取成果相关文献推荐、读者推荐必要条件.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  Map<String, Object> getPubRecommendRequir(Long pubId) throws ServiceException;

  /**
   * 获取相关全文. 获取相关成果的全文列表，不加载全文信息。（弹出框列表）
   * 
   * @param des3PubId
   * @param isExcludeCurPub
   * @return
   * @throws ServiceException
   */
  List<Map<String, Object>> getPubFullTexts(String des3PubId, boolean isExcludeCurPub) throws ServiceException;

  /**
   * 查询需要刷新到pub_pdwh表记录的成果.
   * 
   * @param maxSize
   * @return
   * @throws ServiceException
   */
  List<Publication> getNeedRefreshPub(int maxSize) throws ServiceException;

  /**
   * 刷新成果到基准库成果
   * 
   * @param pub
   * @throws ServiceException
   */
  void refreshPubToPubPdwh(Publication pub) throws ServiceException;

  /**
   * 获取成果冗余信息.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  Map<String, Object> getPubSyncPubFtSrvData(Long pubId) throws ServiceException;

  /**
   * 获取成果冗余信息.
   * 
   * @param pub
   * @return
   * @throws ServiceException
   */
  Map<String, Object> getPubSyncPubFtSrvData(Publication pub, boolean syncOldData) throws ServiceException;

  /**
   * 同步成果冗余信息.
   * 
   * @param pub
   * @throws ServiceException
   */
  void syncPubToPubFtSrv(Publication pub) throws ServiceException;

  /**
   * 查找所有成果的ID
   * 
   * @param lastPubId
   * @param size
   * @return
   * @throws ServiceException
   */
  List<Long> findAllPubIds(Long lastPubId, Integer size) throws ServiceException;

  /**
   * 获取成果推荐基本信息.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  Publication getPubBasicRcmdInfo(Long pubId) throws ServiceException;


  /**
   * 成果添加成功生成动态
   * 
   * @param retValue
   * @param xmlDocument
   * @param context
   */
  void pubCreateDynamic(PubXmlDocument newDoc, PubXmlProcessContext context);



  /**
   * 更新全文动态
   * 
   * @param newDoc
   * @param context
   */
  void updateFullTextDynamic(PubXmlDocument newDoc, PubXmlProcessContext context);


  /**
   * @param pe
   * @throws ServiceException
   */
  void save(Publication pe) throws ServiceException;

  /**
   * @param size
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("rawtypes")
  List findPubMatchOwnerIds(int size) throws ServiceException;

  /**
   * @param pubId
   * @throws ServiceException
   */
  void executedPubMatchOwnerIds(Long pubId) throws ServiceException;

  /**
   * 更新成果隐私权限
   * 
   * @param pubId
   * @throws ServiceException
   */
  void updatePubAuthority(Long pubId) throws ServiceException;

  /**
   * 查找用户成果界面显示的成果列表，更新brief_desc_en字段用
   * 
   * @param lastId
   * @param batchSize
   * @return
   */
  @SuppressWarnings("rawtypes")
  List findPubIdsBatchByPubType(Long lastId, int batchSize) throws ServiceException;

  /**
   * 根据pubId更新成果brief_desc_en字段
   * 
   * @param lastId
   */
  Long updateBriefDescEnTask(Long lastId) throws ServiceException;

  /**
   * 获取上月人员成果引用数
   * 
   * @param size
   * @return
   * @throws ServiceException
   */
  List<Map> getLastMonthPsnCitedTimes(Integer size) throws ServiceException;

  /**
   * 获取全文的pubIds
   * 
   * @param pubIdsStr
   * @return
   * @throws ServiceException
   */
  String getFulltextPubIds(String pubIdsStr) throws ServiceException;

  Integer getMatchPubAuthor(Long pubId, Long psnId) throws ServiceException;

  /**
   * 判断成果全文权限.
   * 
   * @param pubDes3Id
   * @param currPsnId
   * @return
   * @throws ServiceException
   */
  Map<String, Object> getPubFullTextInfo(String pubDes3Id, Long currPsnId) throws ServiceException;

  /**
   * @param loadXml
   * @throws ServiceException
   */
  public void isSyncPublicationToProposal(PublicationForm loadXml) throws ServiceException;

  public Map<String, String> getPubFullTextInfo(Long pubId) throws ServiceException;

  public Map<String, String> getPubFullTextInfoFromPubDataStore(Long pubId) throws ServiceException;

  PublicationForm getPublication(Long pubId) throws ServiceException;
}
