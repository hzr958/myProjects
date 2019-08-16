package com.smate.center.batch.service.projectmerge;

import java.io.Serializable;

import com.smate.center.batch.model.sns.prj.ScmPrjXml;

/**
 * 项目XML服务.
 * 
 * @author liqinghua
 * 
 */
public interface ScmPrjXmlService extends Serializable {

  /**
   * 保存项目XML.
   * 
   * @param prjId
   * @param xml
   * @return
   * @throws Exception
   */
  public ScmPrjXml savePrjXml(Long prjId, String xml) throws Exception;

  /**
   * 获取项目XML.
   * 
   * @param pubId
   * @return
   * @throws Exception
   */
  public ScmPrjXml getPrjXml(Long prjId) throws Exception;

  /**
   * 添加项目XML为空的记录.
   * 
   * @param prjId
   * @throws Exception
   */
  public void saveScmPrjXmlEmpty(Long prjId) throws Exception;
}
