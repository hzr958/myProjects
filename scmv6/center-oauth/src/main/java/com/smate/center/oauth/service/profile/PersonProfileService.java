package com.smate.center.oauth.service.profile;

import com.smate.center.oauth.exception.OauthException;
import com.smate.center.oauth.form.ForgetPwdForm;

/**
 * 忘记密码服务接口
 * 
 * @author zzx
 *
 */
public interface PersonProfileService {
  /**
   * 验证邮箱或帐号是否存在
   * 
   * @param email
   * @return
   */
  public boolean validateEmailIsExist(String email);

  /**
   * 发送忘记密码邮件
   * 
   * @param emailOrLogin
   * @param serverURL
   * @return
   * @throws OauthException
   */
  public String sendForgetPasswordMail(String emailOrLogin) throws OauthException;

  /**
   * 校验访问重置密码页面的请求是否有效
   * 
   * @author ChuanjieHou
   * @date 2017年10月17日
   * @param form
   * @return true -- 有效； false -- 无效
   */
  public boolean verifyResetPwdReqParam(ForgetPwdForm form);

  /**
   * 更新用户密码
   * 
   * @author ChuanjieHou
   * @date 2017年10月23日
   * @param key
   * @param newPassword
   * @return 成功返回true，失败返回false
   */
  public boolean updateNewPwd(String key, String newPassword);
}
