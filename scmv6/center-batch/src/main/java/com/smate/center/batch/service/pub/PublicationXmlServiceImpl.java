package com.smate.center.batch.service.pub;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.ConstRefDbDao;
import com.smate.center.batch.dao.sns.pub.InsRefDbDao;
import com.smate.center.batch.dao.sns.pub.PubDataStoreDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.ConstRefDb;
import com.smate.center.batch.model.sns.pub.InsRefDb;
import com.smate.center.batch.model.sns.pub.PubDataStore;
import com.smate.center.batch.model.sns.pub.PubFulltextExtend;
import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.common.URIencodeUtils;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.json.JacksonUtils;


/**
 * 
 * @author liqinghua
 * 
 */
@Service("publicationXmlService")
@Transactional(rollbackFor = Exception.class)
public class PublicationXmlServiceImpl implements PublicationXmlService {
  private static final long serialVersionUID = 533618978437244145L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InsRefDbDao insRefDbDao;
  @Autowired
  private ConstRefDbDao constRefDbDao;
  @Autowired
  private PubDataStoreDao pubDataStoreDao;
  @Resource(name = "pubXmlStoreService")
  private PubXmlStoreService pubXmlStoreService;
  @Resource(name = "rolPubXmlStoreService")
  private PubXmlStoreService rolPubXmlStoreService;
  // @Resource(name = "serviceFactory")
  // private BasicRemotingServiceFactory serviceFactory;

  @Override
  public void clearCache(Long pubId) throws BatchTaskException {

    this.pubXmlStoreService.clearCache(pubId);
  }

  @Override
  public PublicationXml getById(Long pubId) throws BatchTaskException {

    return pubXmlStoreService.get(pubId);
  }

  @Override
  public List<PublicationXml> getBatchXmlByPubIds(Long[] pubIds) throws BatchTaskException {

    try {

      return pubXmlStoreService.getBatchXmlByPubIds(pubIds);
    } catch (BatchTaskException e) {

      logger.error("批量获取成果XML错误pubIds:{}", pubIds, e);
      throw e;
    }

  }

  @Override
  public PublicationXml rolGetById(Long pubId) throws BatchTaskException {

    return rolPubXmlStoreService.get(pubId);
  }

  @Override
  public PublicationXml save(Long pubId, String xml) throws BatchTaskException {
    try {
      Document doc = DocumentHelper.parseText(xml);
    } catch (DocumentException e) {
      logger.error("保存XML失败" + ":" + xml, e);
      throw new BatchTaskException("保存XML失败");
    }
    return pubXmlStoreService.save(pubId, xml);
  }

  @Override
  public PublicationXml rolSave(Long pubId, String xml) throws BatchTaskException {
    try {
      Document doc = DocumentHelper.parseText(xml);
    } catch (DocumentException e) {
      logger.error("保存XML失败" + ":" + xml, e);
      throw new BatchTaskException("保存XML失败");
    }
    return rolPubXmlStoreService.save(pubId, xml);
  }

  @Override
  public PublicationXml saveNoCache(Long pubId, String xml) throws BatchTaskException {

    return pubXmlStoreService.saveNoCache(pubId, xml);
  }

  @Override
  public PubFulltextExtend getFullText(long pubId, boolean isCache) throws BatchTaskException {
    try {
      PublicationXml xml = this.getById(pubId);
      if (xml == null) {
        throw new BatchTaskException("getFullText xml is null pubId=" + pubId);
      }
      PubXmlDocument xmlDocument = new PubXmlDocument(xml.getXmlData());
      PubFulltextExtend ret = xmlDocument.getFulltext();
      return ret;
    } catch (DocumentException e) {
      logger.error("getFullText,转换XML错误", e);
      throw new BatchTaskException(e);

    } catch (BatchTaskException e) {
      logger.error("getFullText读取数据库错误", e);
      throw new BatchTaskException(e);
    }
  }

  @Override
  public String getFullTextUrl(long pubId, boolean isCache, List<Long> insIdList) throws BatchTaskException {
    try {
      PubFulltextExtend ret = getFullText(pubId, isCache);
      String url = ret.getFullTextUrl();
      PublicationXml xml = this.getById(pubId);
      PubXmlDocument xmlDocument = new PubXmlDocument(xml.getXmlData());
      String sourceDbId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_db_id");
      if ("4".equals(sourceDbId)) {
        String titleZh = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title");
        String titleEn = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title");
        String title = StringUtils.isBlank(titleZh) ? titleEn : titleZh;
        String cnkiurl =
            "http://epub.cnki.net/kns/brief/default_result.aspx?txt_1_sel=FT%24%25%3D%7C&txt_1_value1=@@title@@&txt_1_special1=%25&txt_extension=&expertvalue=&cjfdcode=&currentid=txt_1_value1&dbJson=coreJson&dbPrefix=SCDB&db_opt=CJFQ%2CCDFD%2CCMFD%2CCPFD%2CCCND&db_value=&hidTabChange=&hidDivIDS=&singleDB=SCDB&db_codes=&singleDBName=&=*&againConfigJson=false&action=scdbsearch";
        if (StringUtils.isNotBlank(title)) {
          url = cnkiurl.replace("@@title@@", URIencodeUtils.encodeURIComponent(title));
        }
      }
      String tmpUrl = "{\"sourceDbId\":\"" + sourceDbId
          + "\",\"loginUrlInside\":\"\",\"loginUrlOutside\":\"\",\"urlInside\":\"" + url + "\",\"urlOutside\":\"\"}";
      if (CollectionUtils.isNotEmpty(insIdList)) {
        for (Long insId : insIdList) {
          String tempUrl = gererateTmpUrl(insId, sourceDbId, "FULLTEXT", url);
          if (!url.equals(tempUrl)) {
            tmpUrl = tempUrl;
            break;
          }
        }
      }
      String tmpSrcDbId = "";
      if ("4".equals(sourceDbId) || "21".equals(sourceDbId)) {
        tmpSrcDbId = sourceDbId;
      }

      JSONObject jsonObject = (JSONObject) JacksonUtils.jsonObject(tmpUrl);// JSONObject.fromObject(tmpUrl);
      jsonObject.accumulate("tmpSrcDbId", tmpSrcDbId);
      tmpUrl = jsonObject.toString();
      return tmpUrl;
    } catch (Exception e) {
      logger.error("=======getFullText=null,pubId={},isCache={}", new Object[] {pubId, isCache});
      throw new BatchTaskException(e);
    }
  }

  @Override
  public String getCitationUrl(long pubId, boolean isCache, List<Long> insIdList) throws BatchTaskException {
    try {
      PublicationXml xml = this.getById(pubId);
      PubXmlDocument xmlDocument = new PubXmlDocument(xml.getXmlData());
      String url = xmlDocument.getCitationUrl();
      String sourceDbId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_db_id");
      if (StringUtils.isNotBlank(sourceDbId)) {
        if ("15".equals(sourceDbId) || "16".equals(sourceDbId) || "17".equals(sourceDbId)) {
          String sourceId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "source_id");
          url =
              "http://apps.webofknowledge.com/CitingArticles.do?product=UA&SID=@SID@&search_mode=CitingArticles&parentProduct=UA&UT=WOS:"
                  + sourceId;
        }
      }

      String tmpUrl = "{\"sourceDbId\":\"" + sourceDbId
          + "\",\"loginUrlInside\":\"\",\"loginUrlOutside\":\"\",\"urlInside\":\"" + url + "\",\"urlOutside\":\"\"}";
      if (insIdList != null) {
        for (Long insId : insIdList) {
          String tempUrl = gererateTmpUrl(insId, sourceDbId, "CITATIONS", url);
          if (!url.equals(tempUrl)) {
            tmpUrl = tempUrl;
            break;
          }
        }
      }
      return tmpUrl;
    } catch (DocumentException e) {
      logger.error("getCitationUrl,转换XML错误", e);
      throw new BatchTaskException(e);

    } catch (DataException e) {
      logger.error("getCitationUrl读取数据库错误", e);
      throw new BatchTaskException(e);
    } catch (BatchTaskException e) {
      logger.error("getCitationUrl读取数据库错误", e);
      throw new BatchTaskException(e);
    }
  }

  @Override
  public String getSourceUrl(long pubId, boolean isCache, List<Long> insIdList) throws BatchTaskException {
    try {
      PublicationXml xml = this.getById(pubId);
      PubXmlDocument xmlDocument = new PubXmlDocument(xml.getXmlData());
      String url = xmlDocument.getSourceUrl();
      String sourceDbId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_db_id");
      if ("4".equals(sourceDbId)) {
        String titleZh = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title");
        String titleEn = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title");
        String title = StringUtils.isBlank(titleZh) ? titleEn : titleZh;
        String cnkiurl =
            "http://epub.cnki.net/kns/brief/default_result.aspx?txt_1_sel=FT%24%25%3D%7C&txt_1_value1=@@title@@&txt_1_special1=%25&txt_extension=&expertvalue=&cjfdcode=&currentid=txt_1_value1&dbJson=coreJson&dbPrefix=SCDB&db_opt=CJFQ%2CCDFD%2CCMFD%2CCPFD%2CCCND&db_value=&hidTabChange=&hidDivIDS=&singleDB=SCDB&db_codes=&singleDBName=&=*&againConfigJson=false&action=scdbsearch";
        if (StringUtils.isNotBlank(title)) {
          url = cnkiurl.replace("@@title@@", URIencodeUtils.encodeURIComponent(title));
        }
      }
      String tmpUrl = "{\"sourceDbId\":\"" + sourceDbId
          + "\",\"loginUrlInside\":\"\",\"loginUrlOutside\":\"\",\"urlInside\":\"" + url + "\",\"urlOutside\":\"\"}";
      if (CollectionUtils.isNotEmpty(insIdList)) {
        for (Long insId : insIdList) {
          String tempUrl = gererateTmpUrl(insId, sourceDbId, "SOURCE", url);
          if (!url.equals(tempUrl)) {
            tmpUrl = tempUrl;
            break;
          }
        }
      }
      String tmpSrcDbId = "";
      if ("4".equals(sourceDbId) || "21".equals(sourceDbId)) {
        tmpSrcDbId = sourceDbId;
      }
      JSONObject jsonObject = (JSONObject) JacksonUtils.jsonObject(tmpUrl);// JSONObject.fromObject(tmpUrl);
      jsonObject.accumulate("tmpSrcDbId", tmpSrcDbId);
      tmpUrl = jsonObject.toString();
      return tmpUrl;
    } catch (DocumentException e) {
      logger.error("getSourceUrl,转换XML错误", e);
      throw new BatchTaskException(e);

    } catch (DataException e) {
      logger.error("getSourceUrl读取数据库错误", e);
      throw new BatchTaskException(e);
    } catch (Exception e) {
      logger.error("getCitationUrl读取数据库错误", e);
      throw new BatchTaskException(e);
    }
  }

  /**
   * 返回当前查看类型可能需要使用的url.如果没有配置校外访问url，或校内url与原url域名不匹配，则只返回原url供打开.
   * 
   * @param insRefDb
   * @param viewType
   * @return
   * @throws DaoException
   */
  private String gererateTmpUrl(Long insId, String sourceDbId, String viewType, String url) throws BatchTaskException {
    if (StringUtils.isBlank(url)) {
      return url;
    }
    url = url.replace("apps.isiknowledge.com/", "apps.webofknowledge.com/");
    StringBuffer defResutl = new StringBuffer();
    defResutl.append("{\"sourceDbId\":\"\",\"loginUrlInside\":\"\",\"loginUrlOutside\":\"\",\"urlInside\":\"")
        .append(url).append("\",\"urlOutside\":\"\"}");
    try {
      if (StringUtils.isBlank(sourceDbId))
        return defResutl.toString();
      Long dbId = Long.parseLong(sourceDbId);
      InsRefDb insRefDb = insRefDbDao.getURL(insId, dbId);
      ConstRefDb constRefDb = constRefDbDao.getConstRefDbById(dbId);

      StringBuilder result = new StringBuilder("");
      if (insRefDb != null) {
        result.append("{\"sourceDbId\":\"").append(sourceDbId);
        result.append("\",\"loginUrlInside\":\"").append(insRefDb.getLoginUrlInside());
        result.append("\",\"loginUrlOutside\":\"").append(insRefDb.getLoginUrl());
        result.append("\",\"urlInside\":\"").append(url);
        result.append("\",\"urlOutside\":\"");

        if ("FULLTEXTURL".equals(viewType)) {
          if (!"".equals(insRefDb.getFulltextUrl()) && StringUtils.isNotBlank(insRefDb.getFulltextUrlInside())
              && url.indexOf(insRefDb.getFulltextUrlInside()) > 0)
            result.append(url.replace(insRefDb.getFulltextUrlInside(), insRefDb.getFulltextUrl()));
          else
            result.append("");
        } else {
          if (!"".equals(insRefDb.getActionUrl()) && StringUtils.isNotBlank(insRefDb.getFulltextUrlInside())
              && url.indexOf(insRefDb.getActionUrlInside()) >= 0) {
            if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(insRefDb.getActionUrl())
                && StringUtils.isNotBlank(insRefDb.getActionUrlInside())) {
              result.append(url.replace(insRefDb.getActionUrlInside() + "/", insRefDb.getActionUrl() + "/"));
            } else {
              result.append("");
            }
          }
        }
        result.append("\",\"searchId\":\"").append(constRefDb.getDbBitCode());
        result.append("\"}");
        return result.toString();
      } else {
        return defResutl.toString();
      }
    } catch (Exception e) {
      logger.error("gererateTmpUrl error", e);
      return defResutl.toString();
    }

  }

  // TODO 如果其他任务有用到，再改造
  /*
   * @Override public String getGeneralFullTextDownUrl(PubFulltextExtend pft) throws
   * BatchTaskException {
   * 
   * if (pft == null) { return ""; } Long fileId = pft.getFullTextFileId(); if (fileId == null) {
   * return ""; } Integer nodeId = pft.getFileNodeId(); Long insId = pft.getInsId(); if (nodeId ==
   * null) { nodeId = SecurityUtils.getCurrentUserNodeId(); } if (nodeId == null || nodeId == 0) {
   * nodeId = SecurityUtils.getCurrentAllNodeId().get(0); } try { String url =
   * serviceFactory.getWebUrl(nodeId.longValue());
   * 
   * return ArchiveFileUtil.generateDownloadLink(url, fileId, nodeId, insId); } catch (Exception e) {
   * logger.error("构造全文附件下载URL nodeId="+nodeId, e); throw new
   * BatchTaskException("构造全文附件下载URL nodeId="+nodeId, e); } }
   * 
   * @Override public String getPdwhPubSourceUrl(long pubId, int dbid, List<Long> insIdList) throws
   * BatchTaskException { try { PublicationXml pubxml =
   * serviceFactory.getPdwhModuleService(PublicationXmlPdwhService.class ).getPdwhPubXmlById(pubId,
   * dbid); PubXmlDocument xmlDocument = new PubXmlDocument(pubxml.getXmlData()); String url =
   * xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "source_url"); String tmpUrl =
   * "{\"sourceDbId\":\"" + dbid +
   * "\",\"loginUrlInside\":\"\",\"loginUrlOutside\":\"\",\"urlInside\":\"" + url +
   * "\",\"urlOutside\":\"\"}"; if (CollectionUtils.isNotEmpty(insIdList)) { for (Long insId :
   * insIdList) { String tempUrl = gererateTmpUrl(insId, ObjectUtils.toString(dbid), "SOURCE", url);
   * if (!url.equals(tempUrl)) { tmpUrl = tempUrl; break; } } } String tmpSrcDbId = ""; if (4 == dbid
   * || 21 == dbid) { tmpSrcDbId = String.valueOf(dbid); } JSONObject jsonObject =
   * JSONObject.fromObject(tmpUrl); jsonObject.accumulate("tmpSrcDbId", tmpSrcDbId); tmpUrl =
   * jsonObject.toString(); return tmpUrl; } catch (Exception e) { logger.error("", e); } return ""; }
   * 
   * @Override public String getPdwhPubFulltextUrl(long pubId, int dbid, List<Long> insIdList) throws
   * BatchTaskException { try { PublicationXml pubxml =
   * serviceFactory.getPdwhModuleService(PublicationXmlPdwhService.class ).getPdwhPubXmlById(pubId,
   * dbid); PubXmlDocument xmlDocument = new PubXmlDocument(pubxml.getXmlData()); String url =
   * xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fulltext_url"); String tmpUrl
   * = "{\"sourceDbId\":\"" + dbid +
   * "\",\"loginUrlInside\":\"\",\"loginUrlOutside\":\"\",\"urlInside\":\"" + url +
   * "\",\"urlOutside\":\"\"}"; if (CollectionUtils.isNotEmpty(insIdList)) { for (Long insId :
   * insIdList) { String tempUrl = gererateTmpUrl(insId, ObjectUtils.toString(dbid), "SOURCE", url);
   * if (!url.equals(tempUrl)) { tmpUrl = tempUrl; break; } } } String tmpSrcDbId = ""; if (4 == dbid
   * || 21 == dbid) { tmpSrcDbId = String.valueOf(dbid); } JSONObject jsonObject =
   * JSONObject.fromObject(tmpUrl); jsonObject.accumulate("tmpSrcDbId", tmpSrcDbId); tmpUrl =
   * jsonObject.toString(); return tmpUrl; } catch (Exception e) { logger.error("", e); } return ""; }
   */

  // 过滤版权信息.
  private void removeCopyrightForPubXML(PubXmlDocument xmlDocument, String enAbstract, String localAbstract)
      throws BatchTaskException {
    if (StringUtils.isNotBlank(enAbstract)) {
      // Test //enAbstract +=
      // "(C) All rights reserved © 2003 American Chemical Society.";
      int len = enAbstract.length();
      String firstHalf = "";
      String lastHalf = "";
      if (len > 100) {// 摘要拆分成两部分，以后100个字符为划分点
        firstHalf = enAbstract.substring(0, len - 100);
        lastHalf = enAbstract.substring(len - 100);
      } else {// 不够100个字符
        lastHalf = enAbstract;
      }

      int copyPos = StringUtils.indexOfAny(lastHalf,
          new String[] {"All rights reserved", "Copyright", "(C)", "(c)", "©", "&copy;"});// lastHalf是否包含数组中的任意版权字符
      if (copyPos > -1) {
        lastHalf = lastHalf.substring(0, copyPos);// 截取包含的版权字符
        int dotPos = StringUtils.lastIndexOf(lastHalf, ".");// 以最后出现的英文句号为划分点
        if (dotPos > -1) {
          lastHalf = lastHalf.substring(0, dotPos + 1);// 包含句号
          // 拼接去除版权信息的摘要内容写回xml中
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, localAbstract, firstHalf + lastHalf);
        }
      }
    }
  }

  @Override
  public void rebuildPubXMLCopyright(PubXmlDocument xmlDocument) throws BatchTaskException {
    try {

      // 英文摘要
      String enAbstract = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_abstract");
      this.removeCopyrightForPubXML(xmlDocument, enAbstract, "en_abstract");
      // 中文摘要
      String zhAbstract = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_abstract");
      this.removeCopyrightForPubXML(xmlDocument, zhAbstract, "zh_abstract");

    } catch (Exception e) {
      logger.error("pubId: " + xmlDocument.getPubId() + "去除版权信息出错啦！", e);
    }
  }

  @Override
  public PubDataStore getXmlFromPubDataStore(Long pubId) {

    PubDataStore pubDataStore = this.pubDataStoreDao.get(pubId);

    return pubDataStore;
  }


}
