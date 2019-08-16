package com.smate.sie.core.base.utils.service.psn;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.core.base.utils.model.psn.SieInsPerson;

/**
 * 同步人员信息到SNS服务
 * 
 * @author hd
 *
 */
public interface PsnInfoSyncToSnsService {
  /**
   * 调用open接口，同步信息至sns
   * 
   * @param psnIns
   * @throws SysServiceException
   */
  public String doSync(SieInsPerson psnIns) throws SysServiceException;

  /**
   * 同步单位联系人的邮箱信息到sns
   * 
   * @param psnId
   * @param email
   * @throws SysServiceException
   */
  public String updatePersonByApprove(Long psnId, String email) throws SysServiceException;

  /**
   * 同步单位联系人的账号信息到cas
   * 
   * @param email
   * @param password
   * @throws SysServiceException
   */
  public String updatePassword(Long psnId, String email, String password) throws SysServiceException;
}
