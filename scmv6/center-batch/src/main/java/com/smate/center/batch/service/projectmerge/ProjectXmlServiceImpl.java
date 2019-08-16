package com.smate.center.batch.service.projectmerge;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.oldXml.prj.ProjectXml;

/**
 * 项目XML基本服务，提供与当前人、单位无关的操作.
 * 
 * @author liqinghua
 * 
 */
@Service("projectXmlService")
@Transactional(rollbackFor = Exception.class)
public class ProjectXmlServiceImpl implements ProjectXmlService {

  /**
   * 
   */
  private static final long serialVersionUID = 300493127942630319L;

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PrjXmlStoreService prjXmlStoreService;

  @Override
  public ProjectXml save(Long prjId, String xml) throws Exception {
    return prjXmlStoreService.save(prjId, xml);
  }

  @Override
  public ProjectXml getById(Long prjId) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void clearCache(Long prjId) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public List<ProjectXml> getBatchXmlByPrjIds(Long[] prjIds) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

}
