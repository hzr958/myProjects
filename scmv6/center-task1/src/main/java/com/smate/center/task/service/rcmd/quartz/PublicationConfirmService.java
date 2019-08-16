package com.smate.center.task.service.rcmd.quartz;

import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.PubAssignSyncMessageEnum;
import com.smate.center.task.model.rcmd.quartz.PublicationConfirm;
import com.smate.center.task.model.rol.quartz.PubAssignSyncMessage;

/**
 * 成果确认业务逻辑.
 * 
 * @author zjh
 *
 */
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
   * 个人确认成果后，可能ROL那边系统错误，导致成果没有确认，需要程序自动确认，加载符合条件的数据.
   * 
   * @param startId
   * @return
   * @throws ServiceException
   */
  public List<PublicationConfirm> loadReconfirmList(Long startId) throws ServiceException;

  /**
   * 个人确认成果后，可能ROL那边系统错误，导致成果没有确认，需要程序自动确认.
   * 
   * @param form
   * @param map
   * @throws ServiceException
   */
  public void reconfirmPublication(PublicationConfirm pubConfirm) throws ServiceException;

  /**
   * 备份确认结果到历史记录中.
   * 
   * @param existConfirm
   * @throws ServiceException
   */
  public void bakToHistory(PublicationConfirm confirm) throws ServiceException;

  /**
   * 接收单位的指派同步接口.
   * 
   * @param msg
   * @param syncAssignXml
   * @throws ServiceException
   */
  public void receivePubAssignFromInsSyncMessage(PubAssignSyncMessage msg, PubAssignSyncMessageEnum assign)
      throws ServiceException;

  public void autoComfirmDupPub(PubAssignSyncMessage msg) throws ServiceException;

  public Long getPubConfirmCountByPsnId(Long psnId) throws ServiceException;

  void setPubConfirmSuccess(Long psnId, Long snsPubId, Long insPubId, Long insId) throws ServiceException;

  void updateConfirmSyncNum(PublicationConfirm pubConfirm) throws ServiceException;

}
