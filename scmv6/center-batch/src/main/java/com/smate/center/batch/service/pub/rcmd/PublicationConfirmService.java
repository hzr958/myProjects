package com.smate.center.batch.service.pub.rcmd;

import java.util.List;

import com.smate.center.batch.constant.PubAssignSyncMessageEnum;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rcmd.pub.PubConfirmRolPubDupFields;
import com.smate.center.batch.model.rcmd.pub.PublicationConfirm;
import com.smate.center.batch.model.sns.pub.PubAssignSyncMessage;
import com.smate.core.base.utils.exception.BatchTaskException;

public interface PublicationConfirmService {

  // 0:未确认；1：确认；2不是我的成果；3：不需确认
  public static Integer RESULT_UNCONFIRM_INT = 0;
  public static String RESULT_UNCONFIRM = RESULT_UNCONFIRM_INT.toString();
  public static Integer RESULT_CONFIRM_INT = 1;
  public static String RESULT_CONFIRM = RESULT_CONFIRM_INT.toString();
  public static Integer RESULT_NOTMINE_INT = 2;
  public static String RESULT_NOTMINE = RESULT_NOTMINE_INT.toString();
  public static Integer RESULT_NONEED_INT = 3;
  public static String RESULT_NONEED = RESULT_NONEED_INT.toString();


  /**
   * 如果用户成果库中已经存在严格重复成果，自动给用户确认，减少用户负担.
   * 
   * @param msg
   * @throws BatchTaskException
   */
  public void autoComfirmStrictDupPub(PublicationConfirm confirm, PubConfirmRolPubDupFields dupFields)
      throws BatchTaskException;

  /**
   * 如果用户成果库中已经存在严格重复成果，自动给用户确认，减少用户负担.
   * 
   * @param msg
   * @throws BatchTaskException
   */
  public void autoComfirmStrictDupPub(Long psnId, Long cfmId) throws BatchTaskException;

  /**
   * 如果用户成果库中已经存在严格重复成果，自动给用户确认，减少用户负担.
   * 
   * @param psnId, insId
   * @throws BatchTaskException
   */
  public void autoComfirmDupPub(Long psnId, Long insId) throws BatchTaskException;

  /**
   * 备份确认结果到历史记录中.
   * 
   * @param existConfirm
   * @throws BatchTaskException
   */
  public void bakToHistory(PublicationConfirm confirm) throws BatchTaskException;

  /**
   * 接收单位的指派同步接口.
   * 
   * @param msg
   * @param syncAssignXml
   * @throws ServiceException
   */
  public void receivePubAssignFromInsSyncMessage(PubAssignSyncMessage msg, PubAssignSyncMessageEnum actionType)
      throws ServiceException;

  /**
   * pub_simple_hash中简单查重，然后自动确认
   * 
   * @param dupPubIds，psnId
   * 
   **/
  public void autoConfirmPubSimple(List<Long> dupPubIds, Long psnId, Long snsPubId) throws Exception;

  public Long getPubConfirmCountByPsnId(Long psnId) throws ServiceException;

  /**
   * 如果用户成果库中已经存在严格重复成果，自动给用户确认，减少用户负担.
   * 
   * @param msg
   * @throws ServiceException
   */
  public void autoComfirmDupPub(PubAssignSyncMessage msg) throws ServiceException;
}
