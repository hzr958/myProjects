package com.smate.center.mail.service;

import java.util.List;

import com.smate.center.mail.connector.model.MailOriginalData;
import com.smate.center.mail.model.MailDataInfo;

/**
 * 构造邮件信息接口
 * 
 * @author zzx
 *
 */
public interface MailBuildInfoService {

  /**
   * 获取 对象 根据id
   * 
   * @param mailId
   * @return
   */
  public MailOriginalData getMailOriginalData(Long mailId);

  /**
   * 查询待构建数据列表
   * 
   * @param mailDispatchSize
   * @return
   * @throws Exception
   */
  List<MailOriginalData> findMailOriginalDataList(int mailDispatchSize) throws Exception;

  /**
   * 构造必要参数
   * 
   * @param one
   * @return
   * @throws Exception
   */
  MailDataInfo buildExcuteParam(MailOriginalData one) throws Exception;

  /**
   * 保存邮件接收数据
   * 
   * @param info
   * @throws Exception
   */
  void saveMailRecord(MailDataInfo info) throws Exception;

  /**
   * 保存邮件模版数据
   * 
   * @param info
   * @throws Exception
   */
  void saveTemplateInfo(MailDataInfo info) throws Exception;

  /**
   * 保存邮件链接数据
   * 
   * @param info
   * @throws Exception
   */
  void saveLinkInfo(MailDataInfo info) throws Exception;

  /**
   * 统一保存数据
   * 
   * @param info
   * @throws Exception
   */
  void mainSaveData(MailDataInfo info) throws Exception;

  /**
   * 添加默认参数到构造模版的map
   * 
   * @param info
   * @throws Exception
   */
  void addDefaultParam(MailDataInfo info) throws Exception;

  /**
   * 效验是否能对接收人发送邮件
   * 
   * @param info
   * @throws Exception
   */
  void checkUsable(MailDataInfo info) throws Exception;

  /**
   * 不接收此类邮件，记录标识
   * 
   * @param one
   * @throws Exception
   */
  void buildFailedForNotReceive(MailOriginalData one);

  /**
   * 构造失败，记录标识
   * 
   * @param one
   * @throws Exception
   */
  void buildFailedForError(MailOriginalData one) throws Exception;

  /**
   * 是否有模版限制
   * 
   * @param info
   * @throws Exception
   */
  void checkTempLimit(MailDataInfo info) throws Exception;

  /**
   * 有模版限制，记录标识
   * 
   * @param one
   * @throws Exception
   */
  void buildFailedForTemplateTimeLimit(MailOriginalData one) throws Exception;

  void checkFristEmail(MailDataInfo info) throws Exception;

  void buildFirstEmailSame(MailOriginalData one) throws Exception;

  public void validateEmail(MailDataInfo info);

}
