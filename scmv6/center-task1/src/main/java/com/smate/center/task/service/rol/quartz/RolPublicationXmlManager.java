package com.smate.center.task.service.rol.quartz;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.PubConfirmSyncMessage;
import com.smate.center.task.model.rol.quartz.PubMemberRol;
import com.smate.center.task.model.rol.quartz.PublicationRol;
import com.smate.center.task.model.sns.quartz.PubXmlProcessContext;
import com.smate.center.task.single.enums.pub.XmlOperationEnum;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;

public interface RolPublicationXmlManager {
  /**
   * 更新成果人员XML.
   * 
   * @param mb
   * @throws ServiceException
   */
  PubMemberRol updatePubMemeberXml(PubMemberRol mb) throws ServiceException;

  PubXmlDocument reBuildInsPubxml(PubConfirmSyncMessage msg, Long assignPmId, Long cofirmPmId, PublicationRol pub);

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

  PubXmlProcessContext buildXmlProcessContext(XmlOperationEnum action, int pubTypeId, long currentUserId,
      long currentInsId);

  void praseAuthorNames(PubXmlDocument pubXmlDoc, PublicationRol pub);

  void noSaveUpdatePubMemberXml(PubMemberRol mb, PubXmlDocument doc) throws ServiceException;

}
