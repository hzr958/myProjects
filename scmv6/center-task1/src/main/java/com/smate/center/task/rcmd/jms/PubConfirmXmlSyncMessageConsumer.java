package com.smate.center.task.rcmd.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.PubConfirmSyncMessage;
import com.smate.center.task.model.rcmd.quartz.PubConfirmSyncMessageEnum;
import com.smate.center.task.service.rol.quartz.PubRolPersonService;

@Component("pubConfirmXmlSyncMessageConsumer")
public class PubConfirmXmlSyncMessageConsumer {


  /**
   * 
   */
  private static final long serialVersionUID = 5005123829011481157L;

  @Autowired
  private PubRolPersonService pubRolPersonService;

  public void receive(PubConfirmSyncMessage message) throws ServiceException {
    PubConfirmSyncMessageEnum actionType = message.getActionType();
    if (actionType.equals(PubConfirmSyncMessageEnum.CONFIRM)) {// 同步rcmd的成果认领信息到rol
      pubRolPersonService.receiveConfirmMessage(message);
    } else if (actionType.equals(PubConfirmSyncMessageEnum.AUTO_CONFIRM)) {
      pubRolPersonService.receiveConfirmMessage(message);
      // 仅仅确认是我的成果，不导入成果（查重后用户选择忽略）.
    } else if (actionType.equals(PubConfirmSyncMessageEnum.JUST_CONFIRM)) {
      pubRolPersonService.receiveConfirmMessage(message);
    } else if (actionType.equals(PubConfirmSyncMessageEnum.SYNC_XML_SUCCESS)) {
      pubRolPersonService.pubConfirmSuccessSyncXml(message);

    }
  }

}
