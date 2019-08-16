package com.smate.center.open.service.interconnection;

import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.interconnection.AccountInterconnectionForm;

/**
 * 账号关联服务类接口
 * 
 * @author zll
 */
public interface AccountInterconnectionService {

  /**
   * 用第三方系统的邮箱、名字、机构名称匹配科研之友的人员
   * 
   * @param form
   */
  void dofindMatchUser(AccountInterconnectionForm form) throws OpenException;

  Long doFastRelateAccount(AccountInterconnectionForm form) throws Exception;

  Long doRelateExistAccount(AccountInterconnectionForm form) throws Exception;

  /**
   * 检查请求的有效性
   * 
   * @param form
   * @return
   */
  boolean checkUrl(AccountInterconnectionForm form) throws OpenException;

  /**
   * 查看第三方用户的email在系统中是否已存在
   * 
   * @param email
   * @return
   * @throws OpenException
   */
  boolean findEmailIsExist(String email) throws OpenException;

  /**
   * 用MD5生成签名串
   * 
   * @param form
   * @return
   * @throws Exception
   */
  String generateSignature(AccountInterconnectionForm form) throws Exception;

  /**
   * 效验关联关系
   * 
   * @param form
   * @return
   * @throws Exception
   */
  boolean CheckRelate(AccountInterconnectionForm form) throws Exception;

  /**
   * 校验第三方系统标识是否有效
   * 
   * @param token
   * @return
   * @throws OpenException
   */
  boolean checkSysToken(String token) throws OpenException;

  /**
   * 同步人员信息
   * 
   * @param form
   * @throws OauthException
   */
  boolean syncPersonInfo(AccountInterconnectionForm form) throws Exception;

}
