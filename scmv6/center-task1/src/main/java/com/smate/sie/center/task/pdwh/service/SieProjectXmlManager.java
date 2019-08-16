package com.smate.sie.center.task.pdwh.service;

import org.dom4j.DocumentException;

import com.smate.core.base.utils.exception.SysServiceException;

/**
 * 项目XML处理服务
 * 
 * @author yexingyuan
 */
public interface SieProjectXmlManager {

  /**
   * 判断成果在机构中是否重复,若为true则不重复。若为false则重复。
   * 
   * @param prjId
   * @param InsId
   * @throws SysServiceException
   * @throws DocumentException
   */
  public boolean getRepeatPrjStatus(Long prjId, Long InsId) throws SysServiceException, DocumentException;
}
