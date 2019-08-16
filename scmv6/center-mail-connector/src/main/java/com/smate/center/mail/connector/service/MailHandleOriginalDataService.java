package com.smate.center.mail.connector.service;

import java.util.Map;

/**
 * 邮件原始数据处理接口
 * 
 * @author zzx
 *
 */
public interface MailHandleOriginalDataService {
  /**
   * 处理原始邮件参数
   * 
   * @param paramData=｛ originalData:{ senderPsnId:"", receiver:"", mailTemplateCode:"",
   *        priorConde:"", desc:"" }, mailData:{
   * 
   *        } ｝
   * @return
   */
  Map<String, String> doHandle(Map<String, String> paramData) throws Exception;

  void buildNecessaryParam(String email, Long currentUserId, Long psnId, Integer templateCode, String msg,
      Map<String, String> paramData) throws Exception;

}
