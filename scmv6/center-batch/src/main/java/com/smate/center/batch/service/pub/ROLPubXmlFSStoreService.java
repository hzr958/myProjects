package com.smate.center.batch.service.pub;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.smate.center.batch.base.AppSettingConstants;
import com.smate.center.batch.base.AppSettingContext;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.RolPubXml;
import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.center.batch.service.rol.pub.RolPubXmlService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.file.HashFileNameParseService;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 存储XML到文件系统.
 * 
 * @author yamingd
 * 
 */
public class ROLPubXmlFSStoreService implements PubXmlStoreService {

  /**
   * 
   */
  private static final long serialVersionUID = 4296592049772083652L;

  private static final String FILE_NAME_PATTEM = "rol-xml-%s-%s.xml";
  private static final String CACHE_NAME = "rol-xml";
  private static final String CACHE_ENABLE = "pub_xml_enable_cache";
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RolPubXmlService rolPubXmlService;
  private String rootFolder;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private PublicationCacheService publicationCacheService;

  @Autowired
  private FileService fileService;

  @Override
  public PublicationXml get(Long pubId) throws ServiceException {

    PublicationXml xml = null;

    try {

      String ret = this.getCache(pubId);
      // 是否使用数据库存储
      if ("1".equals(AppSettingContext.getValue(AppSettingConstants.ROL_PUB_XML_USE_DB))) {
        RolPubXml pubXml = rolPubXmlService.getPubXml(pubId);
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
      if ("1".equals(AppSettingContext.getValue(CACHE_ENABLE))) {
        publicationCacheService.cacheROLPubXml(xml);
      }
      return xml;
    } catch (FileNotFoundException e) {
      String msg = String.format("读取成果XMl错误，FileNotFoundException, pubId=%s", pubId);
      logger.error("{}\n{}", msg, e);
      throw new ServiceException(e);
    } catch (IOException e) {
      logger.error("读取成果XMl错误，pubId={}", pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PublicationXml save(Long pubId, String xml) throws ServiceException {

    String fileName = this.getFileName(pubId);
    try {

      if ("1".equals(AppSettingContext.getValue(AppSettingConstants.ROL_PUB_XML_USE_DB))) {
        rolPubXmlService.savePubXml(pubId, xml);
      } else {
        this.fileService.writeText(xml, this.getRootFolder(), fileName, "utf-8");
      }
      this.putCache(pubId, xml);
    } catch (IOException e) {
      logger.error("保持成果XMl错误，pubId={}", pubId, e);
      throw new ServiceException(e);
    }
    PublicationXml pubXml = new PublicationXml(pubId, xml);
    // 是否启用缓存
    if ("1".equals(AppSettingContext.getValue(CACHE_ENABLE))) {
      publicationCacheService.cacheROLPubXml(pubXml);
    }
    return pubXml;
  }

  private String getFileName(Long pubId) throws ServiceException {

    Integer nodeId = SecurityUtils.getCurrentAllNodeId().get(0);
    String fileName = String.format(FILE_NAME_PATTEM, nodeId, pubId);
    return fileName;
  }

  public static void main(String[] args) throws Exception {
    String fileName = String.format(FILE_NAME_PATTEM, 10000, 100000000465528L);
    HashFileNameParseService b = new HashFileNameParseService();
    String dir = b.parseFileNameDir(fileName);
    System.out.println(dir);
    System.out.println(fileName);
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
  public List<PublicationXml> getBatchXmlByPubIds(Long[] pubIds) throws ServiceException {

    List<PublicationXml> list = new ArrayList<PublicationXml>();
    try {
      for (Long pubId : pubIds) {
        PublicationXml xml = this.get(pubId);
        if (xml != null) {
          list.add(xml);
        }
      }
    } catch (ServiceException e) {
      logger.error("获取批量指定ID的成果XML数据.pubId={}", pubIds, e);
      throw new ServiceException(e);
    }
    return list;
  }

  @Override
  public PublicationXml saveNoCache(Long pubId, String xml) throws ServiceException {
    String fileName = this.getFileName(pubId);
    try {
      if ("1".equals(AppSettingContext.getValue(AppSettingConstants.ROL_PUB_XML_USE_DB))) {
        rolPubXmlService.savePubXml(pubId, xml);
      } else {
        this.fileService.writeText(xml, this.getRootFolder(), fileName, "utf-8");
      }
    } catch (IOException e) {
      logger.error("保持成果XMl错误，pubId={}", pubId, e);
      throw new ServiceException(e);
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

    // 是否启用缓存
    if ("1".equals(AppSettingContext.getValue(CACHE_ENABLE)) && StringUtils.isBlank(pubXml)) {
      // 缓存10分钟
      cacheService.put(CACHE_NAME, CacheService.EXP_MIN_10, pubId.toString(), pubXml);
    }
  }

  /**
   * 获取缓存.
   * 
   * @param pubId
   * @return
   */
  private String getCache(Long pubId) {

    // 是否启用缓存
    if ("1".equals(AppSettingContext.getValue(CACHE_ENABLE))) {
      return (String) cacheService.get(CACHE_NAME, pubId.toString());
    }
    return null;
  }

  @Override
  public void clearCache(Long pubId) {
    this.publicationCacheService.removeROLPubXml(pubId);
  }

}
