package com.smate.web.psn.service.psnemail;

import java.util.Map;

import com.smate.core.base.utils.model.security.Person;

/**
 * 人员首要邮件服务
 *
 * @author wsn
 * @createTime 2017年3月28日 上午10:46:47
 *
 */
public interface PsnFirstEmailService {

  /**
   * 保存邮件初始化所需信息记录，等发邮件
   * 
   * @param dataMap
   * @throws Exception
   */
  public void saveMailInitData(Map<String, Object> dataMap) throws Exception;

  /**
   * 传入的email是否是首要邮箱
   * 
   * @param psnId
   * @param email
   * @return
   * @throws Exception
   */
  public boolean isPsnFirstEmail(Long psnId, String email) throws Exception;

  /**
   * 调用接口发送更新首要邮件
   * 
   * @param psnInfo
   * @param flag
   */
  public void restSendUpdateFirstEmail(Person psnInfo, boolean flag);
}
