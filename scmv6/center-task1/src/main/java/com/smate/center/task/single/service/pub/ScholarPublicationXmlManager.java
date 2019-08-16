/**
 * 
 */
package com.smate.center.task.single.service.pub;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import com.smate.center.task.exception.PublicationNotFoundException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.PubSimple;
import com.smate.center.task.model.sns.quartz.PubXmlProcessContext;
import com.smate.center.task.model.sns.quartz.PublicationForm;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.exception.SysServiceException;


/**
 * @author yamingd 成果XML处理服务
 */
public interface ScholarPublicationXmlManager {
  /**
   * 创建成果XML.
   * 
   * @param postData jsp发回的数据
   * @param pubTypeId 成果类型ID
   * @param articleType 成果(1)/文献(2)
   * @return Long (成果ID)
   */
  Integer createXmlNew(String newXmlData, int pubTypeId, int articleType, PubSimple pubSimple);

  PubXmlProcessContext buildXmlProcessContext(Long psnId, Integer typeId, String local, Date date);

  /**
   * 加载成果XML.
   * 
   * @param form 成果PublicationForm
   * @return String (成果XML串)
   * @throws ServiceException ServiceException
   */
  PublicationForm loadXml(PublicationForm form) throws ServiceException, PublicationNotFoundException;

  /**
   * 根据相应语言获取相应biref字段.
   * 
   * @param locale
   * @param context
   * @param xmlDocument
   * @param typeId
   * @return
   * @throws ServiceException
   */
  String getLanguagesBriefDesc(Locale locale, PubXmlProcessContext context, PubXmlDocument xmlDocument, Integer typeId)
      throws ServiceException;

  /**
   * 加载基准库成果xml.
   * 
   * @param form
   * @return
   * @throws ServiceException
   * @throws PublicationNotFoundException
   */
  PublicationForm loadPdwhXml(PublicationForm form) throws ServiceException, PublicationNotFoundException;

  /**
   * 修改成果XML.
   * 
   * @param postData jsp发回的数据
   * @param pubTypeId 成果类型ID
   * @param articleType 成果(1)/文献(2)
   * @return Long (成果ID)
   * @throws ServiceException ServiceException
   */
  Integer updateXml(long pubId, String newXmlData, int pubTypeId, int articleType, PubSimple pubSimple)
      throws ServiceException;

  /**
   * 成果XMLComplementaryProcesses.
   * 
   * @param actionType xml所属操作种类，XmlOperationEnum
   * @param pubTypeId 成果类型ID
   * @param articleType 成果(1)/文献(2)
   * @return BatchConfConstant
   * @throws BatchTaskException
   * 
   */
  public void createOrUpdateXmlComplementaryProcesses(String actionType, int pubTypeId, int articleType,
      PubSimple pubSimple) throws BatchTaskException;

  /**
   * 导入成果.
   * 
   * @param importXml 导入的XML串
   * @param pubTypeId 记录类别
   * @param privacyLevel
   * @param isToMyPub
   * @return Long (成果ID)
   * @throws ServiceException ServiceException
   */
  Long importPubXml(String importXmlData, int pubTypeId, int articleType, PubSimple pubSimple) throws ServiceException;

  PubXmlProcessContext buildXmlProcessContext(Long psnId, Integer typeId);

  PubXmlDocument translateImportXml(PubXmlProcessContext context, String importXml) throws SysServiceException;

  Map<String, String> generateBriefFromImportXmlMap(PubXmlProcessContext context, PubXmlDocument xmlDocument,
      String formTmpl, Integer typeId) throws ServiceException;

  Long importPubXml(String importXml, Long psnId, int pubTypeId, Integer isToMyPub, Integer privacyLevel, Long groupId,
      String groupFolderId) throws ServiceException;

  Long syncPubConfirmXmlFromIns(Long insId, Long psnId, Long insPubId, PubXmlDocument doc, Integer articleType)
      throws ServiceException;

}
