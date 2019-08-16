package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.dom4j.Element;

import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.exception.pub.PublicationNotFoundException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rcmd.pub.PubConfirmSyncMessage;
import com.smate.center.batch.model.rol.pub.PubMemberRol;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.rol.pub.PublicationRolForm;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.process.pub.IPubXmlProcess;
import com.smate.center.batch.service.pub.mq.CniprPubCacheAssignMessage;
import com.smate.center.batch.service.pub.mq.CnkiPatPubCacheAssignMessage;
import com.smate.center.batch.service.pub.mq.CnkiPubCacheAssignMessage;
import com.smate.center.batch.service.pub.mq.EiPubCacheAssignMessage;
import com.smate.center.batch.service.pub.mq.IsiPubCacheAssignMessage;
import com.smate.center.batch.service.pub.mq.PdwhPubCacheAssignMessage;
import com.smate.center.batch.service.pub.mq.PubMedPubCacheAssignMessage;
import com.smate.center.batch.service.pub.mq.SpsPubCacheAssignMessage;


/**
 * @author yamingd 成果XML处理服务
 */
public interface RolPublicationXmlManager {

  /**
   * @param process 成果在线导入XML处理过程Bean
   */
  void setRolXmlOnlineImportProcess(IPubXmlProcess process);

  /**
   * @param process 成果后台导入XML处理过程Bean
   */
  void setRolXmlBackgroundImportProcess(IPubXmlProcess process);

  /**
   * @param process 成果保存XML处理过程Bean
   */
  // void setRolXmlSaveProcess(IPubXmlProcess process);

  /**
   * 在线导入成果.
   * 
   * @param importXml 导入的XML串
   * @param pubTypeId 记录类别
   * @return Long (成果ID)
   * @throws ServiceException ServiceException
   */
  Long onlineImportPubXml(String importXml, int pubTypeId) throws ServiceException;

  /**
   * 保持成果XML.
   * 
   * @param pubId
   * @param xml
   * @throws ServiceException
   */
  void updatePubXml(Long pubId, String xml) throws ServiceException;

  /**
   * 重新构造成果XML，生产Brief,校验数据.
   * 
   * @param pubId
   * @param xmlData
   * @throws ServiceException
   */
  void reBuildXml(long pubId, String xmlData) throws ServiceException;

  /**
   * 加载成果XML.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public String loadXmlById(Long pubId) throws ServiceException, PublicationNotFoundException;

  /**
   * 创建成果XML.
   * 
   * @param postData jsp发回的数据
   * @param pubTypeId 成果类型ID
   * @param articleType 成果(1)/文献(2)
   * @return Long (成果ID)
   * @throws ServiceException ServiceException
   */
  @SuppressWarnings("unchecked")
  Long createXml(Map postData, int pubTypeId, int articleType) throws ServiceException;

  /**
   * 同步V2.6成果XML.
   * 
   * @param pubType
   * @param articleType
   * @param oldPubId
   * @param pubXml
   * @param authorState
   * @param isOpen
   * @param confirmResult
   * @param insId
   * @return
   * @throws ServiceException
   */
  Long syncOldPubXml(Map<String, Object> param) throws ServiceException;

  /**
   * 修改成果XML.
   * 
   * @param postData jsp发回的数据
   * @param pubTypeId 成果类型ID
   * @param articleType 成果(1)/文献(2)
   * @return Long (成果ID)
   * @throws ServiceException ServiceException
   */
  @SuppressWarnings("unchecked")
  Long updateXml(long pubId, Map postData, int pubTypeId) throws ServiceException;

  /**
   * 
   * 保存/拆分SNS拉回的成果.
   * 
   * @param pubTypeId 成果类型
   * @param xmlData 成果xml
   * @return
   * @throws ServiceException
   */
  Long saveXmlFromSNS(Long insId, Long userId, Long snsPubId, String xmlData) throws ServiceException;

  /**
   * 设置Pubmember xml为单位具体人员.
   * 
   * @param pmId
   * @param insId
   * @param psnId
   * @param pubXmlDocument
   * @throws ServiceException
   * @throws ServiceException
   */
  void rebuildPubMember(Long pmId, Long insId, Long psnId, PubXmlDocument pubXmlDocument) throws ServiceException;

  /**
   * 设置Pubmember xml为单位具体人员.
   * 
   * @param pmId
   * @param insId
   * @param psnId
   * @param pubId
   * @throws ServiceException
   */
  PubXmlDocument rebuildPubMember(Long pmId, Long insId, Long psnId, Long pubId) throws ServiceException;

  /**
   * 转换导入成果xml.
   * 
   * @param importXml 导入的XML串
   * @param pubTypeId 记录类别
   * @return Long (成果ID)
   * @throws ServiceException ServiceException
   */
  PubXmlDocument translateImportXml(PubXmlProcessContext context, String importXml) throws ServiceException;

  /**
   * @param psnId
   * @param typeId
   * @return
   */
  PubXmlProcessContext buildXmlProcessContext(XmlOperationEnum action, int pubTypeId, long currentUserId,
      long currentInsId);

  /**
   * 从导入的XML生成Brief.
   * 
   * @param xmlDocument
   * @param typeId
   * @return
   */
  String generateBriefFromImportXml(PubXmlProcessContext context, PubXmlDocument xmlDocument, String formTmpl,
      Integer typeId) throws ServiceException;

  Map<String, String> generateBriefFromImportXmlMap(PubXmlProcessContext context, PubXmlDocument xmlDocument,
      String formTmpl, Integer typeId) throws ServiceException;

  /**
   * 更新成果人员XML.
   * 
   * @param mb
   * @throws ServiceException
   */
  PubMemberRol updatePubMemeberXml(PubMemberRol mb) throws ServiceException;

  /**
   * 更新成果人员XML.
   * 
   * @param mb
   * @throws ServiceException
   */
  void updatePubMemeberXml(Long pubId, List<PubMemberRol> mbs) throws ServiceException;

  /**
   * 更新成果人员XML，但是不保存.
   * 
   * @param mb
   * @param doc
   * @throws ServiceException
   */
  public void noSaveUpdatePubMemberXml(PubMemberRol mb, PubXmlDocument doc) throws ServiceException;

  /**
   * 成果确认后，重构XML.
   * 
   * @param msg
   * @param assignPmId
   * @param cofirmPmId
   * @param pub TODO
   * @return
   * @throws ServiceException
   */
  public PubXmlDocument reBuildInsPubxml(PubConfirmSyncMessage msg, Long assignPmId, Long cofirmPmId,
      PublicationRol pub) throws ServiceException;

  /**
   * 更新成果日期.
   * 
   * @param xmlDocument
   * @param publishDate
   * @param psnId TODO
   * @param insId TODO
   * @return
   * @throws ServiceException
   */
  public PubXmlDocument reBuildPublishDate(PubXmlDocument xmlDocument, String publishDate, Long psnId, Long insId)
      throws ServiceException;

  /**
   * 更新成果引用情况.
   * 
   * @param xmlDocument
   * @param pubList
   * @return
   * @throws ServiceException
   */
  public PubXmlDocument reBuildInsPubList(PubXmlDocument xmlDocument, Map<String, String> pubList)
      throws ServiceException;

  /**
   * 成果日期拆分.
   * 
   * @param doc
   * @param pub
   * @return
   */
  PublicationRol prasePublishDate(PubXmlDocument doc, PublicationRol pub);

  /**
   * 重构用户名,第一作者，作者列表.
   * 
   * @param doc
   * @param pub
   * @return
   */
  PublicationRol praseAuthorNames(PubXmlDocument doc, PublicationRol pub) throws ServiceException;

  /**
   * 映射成果作者与单位人员的关系.
   * 
   * <pre>
   * 1.1 没有添加作者，但是选择对应psn_id
   * 	map {type="1",pm_id:1111,psn_id:2222,pub_id:333}
   * 1.2 添加了作者，对应PSN_ID为dai dao人员
   * map {type="2",pm_id:"",au="新添加用户名",psn_id:2222,pub_id:333}
   * </pre>
   * 
   * @param info
   * @throws ServiceException
   */
  void mappingDupPubAuthor(Map<String, Object> mapInfo, String importXml, int pubTypeId) throws ServiceException;

  /**
   * 重构生成简要描述（页面表格的来源列）.
   * 
   * @param doc
   * @param pub
   * @throws ServiceException
   */
  void rebuildPublicationBrief(PubXmlDocument doc, PublicationRol pub) throws ServiceException;

  /**
   * 成果导入时，严格查重后，忽略重复成果，将忽略成果的收录情况等补充到重复成果.
   * 
   * @param dupPubNode
   * @param pubId
   * @throws ServiceException
   */
  void fillInfoByDupXml(Element dupPubNode, Long pubId) throws ServiceException;

  /**
   * 获取来源情况
   * 
   * @param locale
   * @param context
   * @param xmlDocument
   * @param briefDriver
   * @return
   * @throws Exception
   */
  public String getLanguagesBrief(Locale locale, PubXmlProcessContext context, PubXmlDocument xmlDocument,
      Integer pubType) throws Exception;

  /**
   * 根据pubId获取成果XML数据.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public PubXmlDocument getPubXmlByPubId(Long pubId) throws ServiceException;

  /**
   * 保存成果xml
   * 
   * @param newXmlData
   * @param postData
   * @param pubTypeId
   * @param articleType
   * @return
   * @throws ServiceException
   */
  Long createXml(String newXmlData, int pubTypeId, int articleType) throws ServiceException;

  /**
   * 保存成果xml
   * 
   * @param pubId
   * @param newXmlData
   * @param pubTypeId
   * @return
   * @throws ServiceException
   */
  Long updateXml(long pubId, String newXmlData, int pubTypeId) throws ServiceException;

  /**
   * BPO后台导入ISI成果.
   * 
   * @param message
   * @return
   * @throws ServiceException
   * @throws SysAdministratorNotFoundException
   */
  Long backgroundImportIsiPubXml(IsiPubCacheAssignMessage message) throws Exception;

  /**
   * 加载成果XML.
   * 
   * @param form 成果PublicationRolForm
   * @return String (成果XML串)
   * @throws ServiceException ServiceException
   */
  PublicationRolForm loadXml(PublicationRolForm form) throws ServiceException, PublicationNotFoundException;

  /**
   * BPO后台导入scopus成果.
   * 
   * @param message
   * @return
   * @throws Exception
   */
  Long backgroundImportSpsPubXml(SpsPubCacheAssignMessage message) throws Exception;

  /**
   * BPO后台导入PUBMED成果.
   * 
   * @param message
   * @return
   * @throws Exception
   */
  Long backgroundImportPubMedPubXml(PubMedPubCacheAssignMessage message) throws Exception;

  /**
   * BPO后台导入CNKI成果.
   * 
   * @param message
   * @return
   * @throws Exception
   * 
   */
  Long backgroundImportCnkiPubXml(CnkiPubCacheAssignMessage message) throws Exception;


  /**
   * BPO后台导入CnkiPat成果.
   * 
   * @param message
   * @return
   * @throws Exception
   * 
   */
  Long backgroundImportCnkiPatPubXml(CnkiPatPubCacheAssignMessage message) throws Exception;


  /**
   * BPO后台导入CNIPR成果.
   * 
   * @param message
   * @return
   * @throws Exception
   * 
   */
  Long backgroundImportCniprPubXml(CniprPubCacheAssignMessage message) throws Exception;

  /**
   * 后台导入Ei成果.
   * 
   * @param message
   * @return
   * @throws Exception
   * 
   */
  Long backgroundImportEiPubXml(EiPubCacheAssignMessage message) throws Exception;

  /**
   * BPO后台导入pdwh成果.
   * 
   * @param message
   * @return
   * @throws ServiceException
   * @throws SysAdministratorNotFoundException
   */
  Long backgroundImportPdwhPubXml(PdwhPubCacheAssignMessage msg);
}
