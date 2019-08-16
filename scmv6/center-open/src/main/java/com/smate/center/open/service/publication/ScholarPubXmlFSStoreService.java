package com.smate.center.open.service.publication;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.model.publication.PublicationXml;
import com.smate.center.open.model.publication.ScmPubXml;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 存储XML到文件系统.
 * 
 * @author ajb
 * 
 */
@Service("pubXmlStoreService")
public class ScholarPubXmlFSStoreService implements PubXmlStoreService {
  private static final String FILE_NAME_PATTEM = "scholar-xml-%s-%s.xml";
  private static final String CACHE_NAME = "scholar-xml";
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private String rootFolder;
  @Autowired
  private ScmPubXmlService scmPubXmlService;
  @Autowired
  private OpenCacheService openCacheService;

  @Override
  public PublicationXml get(Long pubId) throws Exception {

    PublicationXml xml = null;

    try {

      Object retObject = openCacheService.get(CACHE_NAME, pubId.toString());
      String ret = retObject == null ? null : retObject.toString();
      ScmPubXml pubXml = scmPubXmlService.getPubXml(pubId);
      if (pubXml != null) {
        ret = pubXml.getPubXml();
        this.openCacheService.put(CACHE_NAME, pubId.toString(), ret);
      }
      xml = new PublicationXml();
      xml.setId(pubId);
      xml.setXmlData(ret);
      return xml;

    } catch (FileNotFoundException e) {
      String msg = String.format("读取成果XMl错误，FileNotFoundException, pubId=%s", pubId);
      logger.error("{}\n{}", msg, e);
      throw new Exception("读取成果XMl错误", e);
    } catch (IOException e) {
      logger.error("读取成果XMl错误，pubId={}", pubId, e);
      throw new Exception("读取成果XMl错误，pubId={}", e);
    }
  }

  private String getFileName(Long pubId) throws Exception {
    Integer nodeId = SecurityUtils.getCurrentAllNodeId().get(0);
    String fileName = String.format(FILE_NAME_PATTEM, nodeId, pubId);
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
    this.rootFolder = rootFolder;
  }

}
