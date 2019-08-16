
package com.smate.center.task.quartz.pdwh;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.pdwh.pub.DbCacheCfetch;
import com.smate.center.task.single.oldXml.pub.ImportPubXmlDocument;
import com.smate.center.task.single.service.pub.DbCacheCfetchService;
import com.smate.center.task.single.service.pub.PubOriginalDataService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 基准库改造，xml文本文件导入任务从DBCACHE_BFETCH表取值
 * 
 * @author LJ 2017-2-27
 *
 */
public class DbcacheCfetchTask extends TaskAbstract {
  private static final int BATCH_SIZE = 100;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private DbCacheCfetchService dbcacheCfetchService;
  @Autowired
  private PubOriginalDataService pubOriginalDataService;

  public DbcacheCfetchTask() {
    super();
  }

  public DbcacheCfetchTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    Pattern pattern = Pattern.compile("\"(.*?)\"");
    while (true) {
      try {
        Long crossrefId = 0L;
        List<DbCacheCfetch> list = null;
        try {
          /**
           * 获取待处理crossref
           */
          list = dbcacheCfetchService.getTohandleList(BATCH_SIZE);
        } catch (ServiceException e) {
          logger.error("批量获取待处理crossref数据错误", e);
        }

        if (CollectionUtils.isEmpty(list)) {
          logger.info("DbcacheCfetch待处理数据为空！");
          break;
        }

        for (DbCacheCfetch dbCacheCfetch : list) {
          Boolean flag = true;

          crossrefId = dbCacheCfetch.getCrossrefId();
          try {
            String jsonData = dbCacheCfetch.getJsonData();
            Map<Object, Object> map = JacksonUtils.jsonToMap(jsonData);
            List<Map<String, Object>> itemsMapList = (List<Map<String, Object>>) map.get("items");
            for (Map<String, Object> pubData : itemsMapList) {
              Long originalId = null;
              try {
                originalId = dbcacheCfetchService.saveOriginalPdwhPubRelation(pubData, 5L, crossrefId);
                pubOriginalDataService.savePubOriginalData(originalId, JacksonUtils.mapToJsonStr(pubData));
                dbcacheCfetchService.handleCrossrefData(originalId);
              } catch (Exception e) {
                flag = false;
                logger.error("拆分crossref数据出错,originalId:" + originalId, e);
                dbcacheCfetchService.saveError(crossrefId, "(" + originalId + ")" + e.getMessage());
              }
            }
          } catch (Exception e) {
            flag = false;
            dbcacheCfetchService.saveError(crossrefId, "dbCacheCfetch拆分Crossref处理出错，crossrefId:" + crossrefId);
          }
          if (flag == true) {
            // 处理完更新状态为1
            dbcacheCfetchService.saveSuccess(crossrefId);
          }
        }

      } catch (Exception e) {
        logger.error("DbcacheBfetchTask,运行异常,", e);
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
