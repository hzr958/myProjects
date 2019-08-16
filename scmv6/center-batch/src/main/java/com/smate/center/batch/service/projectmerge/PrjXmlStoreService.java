package com.smate.center.batch.service.projectmerge;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.oldXml.prj.ProjectXml;

/**
 * 项目xml存储，读取服务.
 * 
 * @author liqinghua
 * 
 */
public interface PrjXmlStoreService extends Serializable {

  /**
   * 存储XML.
   * 
   * @param prjId
   * @param xml
   * @throws Exception
   */
  ProjectXml save(Long prjId, String xml) throws Exception;

  /**
   * 读取XML.
   * 
   * @param prjId
   * @throws Exception
   */
  ProjectXml get(Long prjId) throws Exception;

  /**
   * 获取批量指定ID的成果XML数据.
   * 
   * @param prjIds
   * @return
   * @throws DaoException
   */
  List<ProjectXml> getBatchXmlByPrjIds(Long[] prjIds) throws Exception;

  /**
   * 清除缓存.
   * 
   * @param prjId
   * @throws Exception
   */
  void clearCache(Long prjId) throws Exception;

}
