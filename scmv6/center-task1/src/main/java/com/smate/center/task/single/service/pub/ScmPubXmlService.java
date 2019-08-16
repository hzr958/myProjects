package com.smate.center.task.single.service.pub;

import java.io.Serializable;

import com.smate.center.task.model.sns.quartz.ScmPubXml;
import com.smate.core.base.utils.exception.BatchTaskException;



/**
 * 成果XML服务.
 * 
 * @author liqinghua
 * 
 */
public interface ScmPubXmlService extends Serializable {

  /**
   * 保存成果XML.
   * 
   * @param pubId
   * @param xml
   * @return
   * @throws BatchTaskException
   */
  public ScmPubXml savePubXml(Long pubId, String xml) throws BatchTaskException;

  /**
   * 获取成果XML.
   * 
   * @param pubId
   * @return
   * @throws BatchTaskException
   */
  public ScmPubXml getPubXml(Long pubId) throws BatchTaskException;

  /**
   * 添加成果XML为空的记录.
   * 
   * @param pubId
   * @throws BatchTaskException
   */
  public void saveScmPubXmlEmpty(Long pubId) throws BatchTaskException;
}
