package com.smate.center.task.single.service.pub;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.smate.center.task.base.AppSettingConstants;
import com.smate.center.task.base.AppSettingContext;
import com.smate.center.task.model.sns.quartz.PublicationXml;
import com.smate.center.task.model.sns.quartz.ScmPubXml;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.file.HashFileNameParseService;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 存储XML到文件系统.
 * 
 * @author yamingd
 * 
 */

public class ScholarPubXmlFSStoreService implements PubXmlStoreService {

  /**
   * 
   */
  private static final long serialVersionUID = 1295474533102748183L;

  private static final String FILE_NAME_PATTEM = "scholar-xml-%s-%s.xml";
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private String rootFolder;
  // @Autowired
  // private CacheService cacheService;
  @Autowired
  private FileService fileService;

  @Autowired
  private ScmPubXmlService scmPubXmlService;

  @Override
  public PublicationXml get(Long pubId) throws BatchTaskException {

    PublicationXml xml = null;

    try {

      String ret = this.getCache(pubId);
      // 是否数据库存储
      if ("1".equals(AppSettingContext.getValue(AppSettingConstants.SNS_PUB_XML_USE_DB))) {
        ScmPubXml pubXml = scmPubXmlService.getPubXml(pubId);
        if (pubXml != null) {
          ret = pubXml.getPubXml();
          this.putCache(pubId, ret);
        }
      } else {
        String fileName = this.getFileName(pubId);
        ret = fileService.readText(fileName, this.getRootFolder(), "utf-8");
        this.putCache(pubId, ret);
      }
      xml = new PublicationXml();
      xml.setId(pubId);
      xml.setXmlData(ret);
      return xml;

    } catch (FileNotFoundException e) {
      String msg = String.format("读取成果XMl错误，FileNotFoundException, pubId=%s", pubId);
      logger.error("{}\n{}", msg, e);
      throw new BatchTaskException(e);
    } catch (IOException e) {
      logger.error("读取成果XMl错误，pubId={}", pubId, e);
      throw new BatchTaskException(e);
    }
  }

  @Override
  public PublicationXml save(Long pubId, String xml) throws BatchTaskException {

    if (StringUtils.isBlank(xml)) {
      throw new BatchTaskException("xml不能为空:pubId" + pubId);
    }
    try {
      if ("1".equals(AppSettingContext.getValue(AppSettingConstants.SNS_PUB_XML_USE_DB))) {
        scmPubXmlService.savePubXml(pubId, xml);
      } else {
        String fileName = this.getFileName(pubId);
        this.fileService.writeText(xml, this.getRootFolder(), fileName, "utf-8");
      }
      this.putCache(pubId, xml);
    } catch (IOException e) {
      logger.error("保存成果XMl错误，pubId={}", pubId, e);
      throw new BatchTaskException(e);
    }
    PublicationXml pubXml = new PublicationXml(pubId, xml);
    return pubXml;
  }

  private String getFileName(Long pubId) throws BatchTaskException {
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

  @Override
  public List<PublicationXml> getBatchXmlByPubIds(Long[] pubIds) throws BatchTaskException {

    List<PublicationXml> list = new ArrayList<PublicationXml>();
    try {
      for (Long pubId : pubIds) {
        PublicationXml xml = this.get(pubId);
        if (xml != null) {
          list.add(xml);
        }
      }
    } catch (BatchTaskException e) {
      logger.error("获取批量指定ID的成果XML数据.pubId={}", pubIds, e);
      throw e;
    }
    return list;
  }

  public static void main(String[] args) throws Exception {
    String fileName = String.format(FILE_NAME_PATTEM, 1, 1000000988235L);
    HashFileNameParseService b = new HashFileNameParseService();
    String dir = b.parseFileNameDir(fileName);
    System.out.println(dir);
    System.out.println(fileName);
  }

  @Override
  public PublicationXml saveNoCache(Long pubId, String xml) throws BatchTaskException {

    if (StringUtils.isBlank(xml)) {
      throw new BatchTaskException("xml不能为空:pubId" + pubId);
    }
    String fileName = this.getFileName(pubId);
    try {
      if ("1".equals(AppSettingContext.getValue(AppSettingConstants.SNS_PUB_XML_USE_DB))) {
        this.fileService.writeText(xml, this.getRootFolder(), fileName, "utf-8");
      } else {
        scmPubXmlService.savePubXml(pubId, xml);
      }
    } catch (IOException e) {
      logger.error("保存成果XMl错误，pubId={}", pubId, e);
      throw new BatchTaskException(e);
    }
    PublicationXml pubXml = new PublicationXml(pubId, xml);
    return pubXml;
  }

  /**
   * 存储XML到缓存，存储10分钟过期.
   * 
   * @param pubId
   * @param pubXml
   */
  private void putCache(Long pubId, String pubXml) {

    // // 是否启用缓存
    // if ("1".equals(AppSettingContext.getValue(CACHE_ENABLE))) {
    // cacheService.put(CACHE_NAME, CacheService.EXP_MIN_10,
    // pubId.toString(), pubXml);
    // }
  }

  /**
   * 获取缓存.
   * 
   * @param pubId
   * @return
   */
  private String getCache(Long pubId) {

    // // 是否启用缓存
    // if ("1".equals(AppSettingContext.getValue(CACHE_ENABLE))) {
    // return (String)cacheService.get(CACHE_NAME, pubId.toString());
    // }
    return null;
  }

  @Override
  public void clearCache(Long pubId) {
    // cacheService.remove(CACHE_NAME, pubId.toString());
  }

}
