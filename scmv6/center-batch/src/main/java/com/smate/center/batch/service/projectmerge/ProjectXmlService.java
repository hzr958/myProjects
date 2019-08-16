package com.smate.center.batch.service.projectmerge;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.oldXml.prj.ProjectXml;

/**
 * 项目XML基本服务，提供与当前人、单位无关的操作.
 * 
 */
public interface ProjectXmlService extends Serializable {

  /**
   * 保存项目XML.
   * 
   * @param prjId
   * @param xml
   * @return
   * @throws Exception
   */
  ProjectXml save(Long prjId, String xml) throws Exception;

  /**
   * @param prjId
   * @return
   * @throws ServiceException
   */
  ProjectXml getById(Long prjId) throws Exception;

  /**
   * 清除缓存.
   * 
   * @param prjId
   * @throws ServiceException
   */
  void clearCache(Long prjId) throws Exception;

  /**
   * 批量获取项目XML.
   * 
   * @param prjIds
   * @return
   * @throws DaoException
   */
  List<ProjectXml> getBatchXmlByPrjIds(Long[] prjIds) throws Exception;
}
