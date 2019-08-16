package com.smate.web.psn.service.email;

import com.smate.web.psn.form.AccountEmailForm;

/**
 * 账号邮箱检查服务
 * 
 * @author aijiangbin
 *
 */
public interface AccountEmailCheckLogService {

  /**
   * 是否需要验证邮箱
   * 
   * @param currentPsnId
   * @return
   * @throws Exception
   */
  Boolean needValidateAccount(AccountEmailForm form) throws Exception;

  /**
   * 通过邮箱验证吗，验证 , 0=没成功 1== 成功
   * 
   * @param validateCodeBig
   * @return
   * @throws Exception
   */
  Integer daValidateByEmail(AccountEmailForm form) throws Exception;

  /**
   * 通过邮箱 弹框验证吗，验证 , 0=没成功 1== 成功
   * 
   * @param validateCodeBig
   * @return
   * @throws Exception
   */
  Integer daValidateByCode(AccountEmailForm form) throws Exception;

  /**
   * 发送账户邮箱验证邮件
   * 
   * @param form
   */
  Boolean sendAccountEmailValidateEmail(AccountEmailForm form);

  /**
   * 重新发送账户邮箱验证邮件
   * 
   * @param form
   */
  Boolean reSendAccountEmailValidateEmail(AccountEmailForm form);

  /**
   * 检查邮箱是否有效
   * 
   * @param form
   * @return
   */
  Boolean checkEmailIsValidate(AccountEmailForm form);

  /**
   * 判断该邮箱是否被验证
   * 
   * @param psnId
   * @param loginName
   * @return
   */
  public Boolean findHasConfirm(Long psnId, String loginName);

  /**
   * 个人设置，验证码验证邮箱
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public Integer psnSetDoValidateEmail(AccountEmailForm form) throws Exception;

  public Boolean checkAccountSendDate(AccountEmailForm form);
}
