package com.smate.sie.center.task.pdwh.service;

import java.util.Map;

import org.dom4j.DocumentException;

import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 成果XML处理服务
 * 
 * @author jszhou
 */
public interface PublicationXmlManager {



  /**
   * 基准库同步任务导入成果.2019-3-14
   * 
   * @param pubJson
   * @param pdwhPublications
   * @return
   * @throws SysServiceException
   */
  Map<String, Object> backgroundImportPdwhPubJson(PubJsonDTO pubJson, PubPdwhPO pdwhPublications)
      throws SysServiceException;

  /**
   * 判断成果在机构中是否重复,若为true则不重复。若为false则重复。
   * 
   * @param pubid
   * @param InsId
   * @throws SysServiceException
   * @throws DocumentException
   */
  public boolean getRepeatPubStatus(Long pubid, int pubType, Long InsId) throws SysServiceException, DocumentException;
}
