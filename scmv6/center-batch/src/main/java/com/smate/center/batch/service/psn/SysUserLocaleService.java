package com.smate.center.batch.service.psn;

import java.io.Serializable;

import com.smate.core.base.utils.model.local.SysUserLocale;

/**
 * 用户语言版本Service
 * 
 * @author weilong peng
 * 
 */
public interface SysUserLocaleService extends Serializable {

  /**
   * 通过用户id取得上次保存的语言版本
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public SysUserLocale getSysUserLocaleByPsnId(Long psnId) throws Exception;

  /**
   * 根据人员ID获取其语言环境.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  String getLocale(Long psnId) throws Exception;

}
