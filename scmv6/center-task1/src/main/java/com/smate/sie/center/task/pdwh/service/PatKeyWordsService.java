package com.smate.sie.center.task.pdwh.service;

import java.io.Serializable;

import com.smate.core.base.utils.exception.SysServiceException;

public interface PatKeyWordsService extends Serializable {
  /**
   * 保存专利关键词.
   * 
   * @param patId
   * @param psnId
   * @param zhKeywords
   * @param enKeywords
   * @throws SysServiceException
   */
  public void savePatKeywords(Long patId, Long insId, String zhKeywords, String enKeywords) throws SysServiceException;

  /**
   * 删除专利关键词.
   * 
   * @param patId
   * @throws SysServiceException
   */
  public void delPatKeywords(Long patId) throws SysServiceException;

  /**
   * 获取专利关键词(包括中英文).
   * 
   * @param patId
   * @return
   * @throws SysServiceException
   */
  public String getPatkeywords(Long patId) throws SysServiceException;

}
