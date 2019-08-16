package com.smate.center.task.quartz.pdwh;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.pdwh.pub.DbCachePfetch;
import com.smate.center.task.single.oldXml.pub.ImportPubXmlDocument;
import com.smate.center.task.single.service.pub.DbCacheBfetchService;
import com.smate.center.task.single.service.pub.DbCachePfetchService;
import com.smate.center.task.single.service.pub.PubOriginalDataService;
import com.smate.center.task.single.util.pub.ImportPubXmlUtils;
import com.smate.core.base.utils.data.XmlUtil;

/**
 * 基准库改造，xml文本文件导入任务从DBCACHE_PFETCH表取值
 * 
 * @author LJ 2017-03-10
 *
 */
public class DbcachePfetchTask extends TaskAbstract {
  private static final int BATCH_SIZE = 100;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private DbCachePfetchService dbcachePfetchService;
  @Autowired
  private DbCacheBfetchService dbCacheBfetchService;
  @Autowired
  private PubOriginalDataService pubOriginalDataService;

  public DbcachePfetchTask() {
    super();
  }

  public DbcachePfetchTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    Pattern pattern = Pattern.compile("\"(.*?)\"");
    while (true) {
      try {
        Long XmlId = 0L;
        List<DbCachePfetch> list = null;
        try {
          /**
           * 获取待处理XML数据
           */
          list = dbcachePfetchService.getTohandleList(BATCH_SIZE);
        } catch (ServiceException e) {
          logger.error("批量获取待处理XML数据错误", e);
        }

        if (CollectionUtils.isEmpty(list)) {
          logger.info("DbcachePfetch待处理数据为空！");
          break;
        }
        for (DbCachePfetch dbCachePfetch : list) {
          boolean flag = true;
          /**
           * 拆分XML数据
           */
          XmlId = dbCachePfetch.getXmlId();
          try {
            String xmlData = dbCachePfetch.getXmlData();
            List<String> pubXmls = ImportPubXmlUtils.splitOnlineXml(xmlData);
            if (CollectionUtils.isEmpty(pubXmls)) {
              dbcachePfetchService.saveError(XmlId);
              continue;
            }

            String seqNo = "";
            /**
             * 
             * 清理XML数据并保存到基准库PdwhPubXmlToHandle表
             */

            for (String pubData : pubXmls) {
              try {
                // 清理XML数据
                pubData = pubData.replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]", "");
                seqNo = ImportPubXmlUtils.getSeqNo(pubData);
                // 处理xml属性值中含有的非法字符
                ImportPubXmlDocument doc = null;
                try {
                  doc = new ImportPubXmlDocument(pubData);
                } catch (DocumentException e) {// 一般情况xml在抓取时已经处理过了，含有非法字符很少
                  logger.error("xml中含有非法字符，将清理后在解析,XmlId={},seqNo={}", XmlId, seqNo);
                  doc = this.reCleanXML(pubData, pattern);
                }
                if (!dbcachePfetchService.validateXml(doc)) {
                  continue;
                }
                Long id = dbCacheBfetchService.saveOriginalPdwhPubRelation(XmlId, seqNo, dbCachePfetch.getInsId(),
                    dbCachePfetch.getPsnId(), 2);
                pubOriginalDataService.savePubOriginalData(id, pubData);
                /*
                 * String result = dbcacheBfetchService.savePdwhPub(pubData, dbCachePfetch.getInsId(),
                 * dbCachePfetch.getPsnId()); String status =
                 * JacksonUtils.jsonToMap(result).get("status").toString(); if ("SUCCESS".equals(status)) { flag =
                 * true; } else { flag = false; }
                 */

              } catch (Exception e) {
                flag = false;
                logger.error("dbcachePfetch拆分成果XML保存到PDWH_PUB_XML_TOHANDLE出错,xmlId:" + XmlId + "seq_no:" + seqNo, e);
                dbcachePfetchService.saveError(XmlId);
              }
            }

          } catch (Exception e) {
            flag = false;
            dbcachePfetchService.saveError(XmlId);
          }
          if (flag == true) {
            // 处理完更新状态为1
            dbcachePfetchService.saveSuccess(dbCachePfetch.getXmlId());
          }
        }
      } catch (Exception e) {
        logger.error("DbcachePfetchTask,运行异常,", e);
      }
    }
  }

  /**
   * 作者节点长度检查，超过1000 false
   * 
   * @param doc
   * @return
   * @author LIJUN
   * @date 2018年6月29日
   */
  public Boolean checkAuthorLength(ImportPubXmlDocument doc) {
    String authorNames = doc.getAuthorNames();
    String authorNameSpec = doc.getAuthorNameSpec();
    Integer aulength = 0;
    Integer auslength = 0;
    if (StringUtils.isNotBlank(authorNameSpec)) {
      aulength = authorNameSpec.length();
    }
    if (StringUtils.isNotBlank(authorNames)) {
      auslength = authorNames.length();
    }
    if (aulength > 1000 || auslength > 1000) {
      return false;
    }
    return true;
  }

  /**
   * 处理转义字符,如果属性值中含有 ""则会导致处理出错
   * 
   * @param pubData
   * @return
   * @throws Exception
   * @author LIJUN
   * @date 2018年7月3日
   */
  public ImportPubXmlDocument reCleanXML(String pubData, Pattern pattern) throws Exception {
    // 先还原转义字符，dom解析时在处理
    pubData = pubData.replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"")
        .replace("&apos;", "\'");
    // dom解析出错，则可能含有非法字符，进一步处理
    Matcher m = pattern.matcher(pubData);
    while (m.find() && StringUtils.isNotBlank(m.group())) {
      String xmlStr = m.group().substring(1, m.group().length() - 1);// 提取""里面的所有数据
      if (xmlStr.contains("&") || xmlStr.contains("<") || xmlStr.contains(">") || xmlStr.contains("\"")
          || xmlStr.contains("\'")) {
        xmlStr = XmlUtil.replaceXmlHtmlTags(xmlStr);
        xmlStr = xmlStr.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")
            .replace("\'", "&apos;");
      }
      pubData = pubData.replace(m.group(), "\"" + xmlStr + "\"");
    }
    ImportPubXmlDocument doc = new ImportPubXmlDocument(pubData);
    return doc;
  }
}
