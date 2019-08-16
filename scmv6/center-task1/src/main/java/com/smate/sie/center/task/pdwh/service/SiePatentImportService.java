package com.smate.sie.center.task.pdwh.service;

import java.io.Serializable;

import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.model.SiePatDupFields;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;
import com.smate.sie.center.task.pdwh.task.PubXmlProcessContext;
import com.smate.sie.core.base.utils.model.pub.SiePatent;

/**
 * 专利创建，修改，删除服务.
 * 
 * @author jszhou
 */
public interface SiePatentImportService extends Serializable {

  /**
   * 获取专利实体，先从自定义缓存读取，如果不存在直接查询数据库.
   * 
   * @param id
   * @return
   * @throws SysServiceException
   */
  SiePatent getPatentById(Long id) throws SysServiceException;

  /**
   * 保存新添加专利.
   * 
   * @param pub
   * @throws SysServiceException
   */
  SiePatent createPatent(PubXmlDocument doc, PubXmlProcessContext context) throws Exception;

  /**
   * 保存专利编辑内容.
   * 
   * @param pub
   * @throws BatchTaskException
   * @throws ServcieException
   */
  SiePatent updatePatent(PubXmlDocument xmlDocument, PubXmlProcessContext context)
      throws SysServiceException, BatchTaskException;

  // 保存查重信息
  public SiePatDupFields parsePatDupFields(PubXmlDocument doc, SiePatent pat) throws SysServiceException;

}
