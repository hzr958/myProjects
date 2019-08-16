package com.smate.center.batch.chain.pub;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.core.base.utils.constant.PubXmlConstants;

/**
 * 初始化xml中的收录情况节点信息.
 * 
 * @author LY
 * 
 */
public class JournalCitedCleanTask implements IPubXmlTask {
  private String name = "clean_journal_list";
  public final static Map<String, String> listMap = new HashMap<String, String>();
  static {
    listMap.put("是", "1");
    listMap.put("yes", "1");
    listMap.put("y", "1");
    listMap.put("1", "1");
    listMap.put("true", "1");
    listMap.put("否", "0");
    listMap.put("0", "0");
    listMap.put("not", "0");
    listMap.put("n", "0");
    listMap.put("false", "0");
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    // 导入执行
    if (XmlOperationEnum.Import.equals(context.getCurrentAction())
        || XmlOperationEnum.ImportPdwh.equals(context.getCurrentAction())) {
      return true;
    } ;
    return false;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    if (xmlDocument.isJournalArticle() || xmlDocument.isConfPaper() || xmlDocument.isOther()) {
      if (!xmlDocument.existsNode(PubXmlConstants.PUB_LIST_XPATH)) {
        xmlDocument.createElement(PubXmlConstants.PUB_LIST_XPATH);
      }
      String listSci = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_sci");
      String listEi = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei");
      String listIstp = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_istp");
      String listSsci = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ssci");
      if (StringUtils.isNotBlank(listSci) && listMap.containsKey(listSci.toLowerCase())) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_sci", listMap.get(listSci));
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_sci", "0");
      }
      if (StringUtils.isNotBlank(listEi) && listMap.containsKey(listEi.toLowerCase())) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei", listMap.get(listEi));
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei", "0");
      }
      if (StringUtils.isNotBlank(listIstp) && listMap.containsKey(listIstp.toLowerCase())) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_istp", listMap.get(listIstp));
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_istp", "0");
      }
      if (StringUtils.isNotBlank(listSsci) && listMap.containsKey(listSsci.toLowerCase())) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ssci", listMap.get(listSsci));
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ssci", "0");
      }
    }

    return true;
  }
}
