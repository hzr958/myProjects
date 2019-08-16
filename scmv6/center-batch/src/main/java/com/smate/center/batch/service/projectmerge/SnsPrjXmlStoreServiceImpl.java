package com.smate.center.batch.service.projectmerge;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.smate.center.batch.oldXml.prj.ProjectXml;
import com.smate.core.base.utils.constant.ServiceConstants;

/**
 * 存储XML到文件系统.
 * 
 * @author liqinghua
 * 
 */
public class SnsPrjXmlStoreServiceImpl implements PrjXmlStoreService {

  /**
   * 
   */
  private static final long serialVersionUID = 1295474533102748183L;

  private static final String FILE_NAME_PATTEM = "scholar-xml-%s-%s.xml";

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ScmPrjXmlService scmPrjXmlService;
  private String rootFolder;


  @Override
  public ProjectXml get(Long prjId) throws Exception {

    ProjectXml xml = null;

    try {
      String ret = null;
      // 是否使用数据库存储
      String fileName = this.getFileName(prjId);
      // ret = fileService.readText(fileName, this.getRootFolder(), "utf-8");

      xml = new ProjectXml();
      xml.setId(prjId);
      xml.setXmlData(ret);
      return xml;
    } catch (FileNotFoundException e) {
      String msg = String.format("读取项目XMl错误，FileNotFoundException, prjId=%s", prjId);
      logger.error("{}\n{}", msg, e);
      throw new Exception(e);
    } catch (IOException e) {
      logger.error("读取项目XMl错误，prjId={}", prjId, e);
      throw new Exception(e);
    }
  }

  @Override
  public ProjectXml save(Long prjId, String xml) throws Exception {

    // String fileName = this.getFileName(prjId);
    try {
      // if ("1".equals(AppSettingContext.getValue(AppSettingConstants.SNS_PRJ_XML_USE_DB))) {
      scmPrjXmlService.savePrjXml(prjId, xml);
      // } else {
      // this.fileService.writeText(xml, this.getRootFolder(), fileName, "utf-8");
      // }
    } catch (IOException e) {
      logger.error("保存项目XMl错误，prjId={}", prjId, e);
      throw new Exception(e);
    }
    ProjectXml pubXml = new ProjectXml(prjId, xml);
    return pubXml;
  }

  private String getFileName(Long prjId) throws Exception {

    Integer nodeId = 1;
    String fileName = String.format(FILE_NAME_PATTEM, nodeId, prjId);
    return fileName;
  }

  /**
   * @return the rootFolder
   */
  public String getRootFolder() {
    return rootFolder;
  }

  /**
   * @param rootFolder the rootFolder to set
   */
  public void setRootFolder(String rootFolder) {
    Assert.notNull(rootFolder);
    rootFolder = rootFolder.replace("\\", "/");
    if (rootFolder.endsWith("/")) {
      rootFolder = rootFolder.substring(0, rootFolder.length() - 1);
    }
    this.rootFolder = rootFolder + "/" + ServiceConstants.DIR_PROJECT_XML;
  }

  @Override
  public List<ProjectXml> getBatchXmlByPrjIds(Long[] prjIds) throws Exception {

    List<ProjectXml> list = new ArrayList<ProjectXml>();
    try {
      for (Long prjId : prjIds) {
        ProjectXml xml = this.get(prjId);
        if (xml != null) {
          list.add(xml);
        }
      }
    } catch (Exception e) {
      logger.error("获取批量指定ID的项目XML数据.prjId={}", prjIds, e);
      throw e;
    }
    return list;
  }


  @Override
  public void clearCache(Long prjId) {

    // this.projectCacheService.removeSNSPrjXml(prjId);
  }
}
