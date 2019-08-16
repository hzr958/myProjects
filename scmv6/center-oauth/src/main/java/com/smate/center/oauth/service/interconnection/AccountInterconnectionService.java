package com.smate.center.oauth.service.interconnection;

import com.smate.center.oauth.exception.OauthException;
import com.smate.center.oauth.model.interconnection.AccountInterconnectionForm;

/**
 * 账号关联服务类接口
 * 
 * @author zll
 */
public interface AccountInterconnectionService {

  /**
   * 创建帐号并关联
   * 
   * @param form
   * @return
   */
  Long doCreateAndRelateAccount(AccountInterconnectionForm form) throws OauthException;

  /**
   * 生成签名
   * 
   * @param form
   * @return
   * @throws Exception
   */
  String generateSignature(AccountInterconnectionForm form) throws Exception;

  /**
   * 检查系统来源token是否合法
   * 
   * @param token
   * @return
   * @throws ServiceException
   */
  boolean checkSysToken(String token) throws OauthException;

}
