package com.smate.web.prj.service.project.search;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.consts.dao.ConstDisciplineDao;
import com.smate.core.base.utils.common.MoneyFormatterUtils;
import com.smate.web.prj.exception.PrjException;
import com.smate.web.prj.form.PrjXmlProcessContext;
import com.smate.web.prj.xml.PrjXmlConstants;
import com.smate.web.prj.xml.PrjXmlDocument;

/**
 * .导入字段映射
 * 
 * @author wsn
 * @date Dec 18, 2018
 */
public class ImportPrjFieldMappingServiceImpl implements ImportPrjXmlDealService {

  @Autowired
  private ConstDisciplineDao constDisciplineDao;

  @Override
  public String checkParameter(PrjXmlProcessContext context) throws PrjException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String dealWithXml(PrjXmlDocument xmlDocument, PrjXmlProcessContext context) throws PrjException {
    Element rootNode = (Element) xmlDocument.getRootNode();
    Element ele = null;
    ele = (Element) xmlDocument.getNode(PrjXmlConstants.PROJECT_XPATH);
    if (ele == null) {
      ele = rootNode.addElement("project");
    }
    // 拷贝publication节点到project节点
    Element pubEle = (Element) xmlDocument.getNode(PrjXmlConstants.PUBLICATION_XPATH);
    if (pubEle != null) {
      xmlDocument.copyPrjElement(ele, pubEle);
    }
    // 处理其他属性
    xmlDocument.getXmlString();
    this.copyOtherXmlAttributes(xmlDocument);
    // 处理meta节点source_url属性
    String sourceUrl = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "source_url");
    Element prjMeta = (Element) xmlDocument.getNode(PrjXmlConstants.PRJ_META_XPATH);
    if (prjMeta == null) {
      prjMeta = rootNode.addElement("prj_meta");
    }
    prjMeta.addAttribute("source_url", sourceUrl);
    // 添加资金单位.
    this.dealWithPrjAmount(xmlDocument);
    // 将作者姓名用;拼接
    this.dealWithAuthorNames(xmlDocument);
    // 添加学科代码
    this.dealWithDiscipline(xmlDocument);
    return null;
  }

  /**
   * 添加学科代码
   *
   * @param xmlDocument
   */
  private void dealWithDiscipline(PrjXmlDocument xmlDocument) {
    String disciplineId = "";
    String oldDiscipline = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "discipline");
    if (StringUtils.isBlank(oldDiscipline)) {// 现在插件没有抓取学科代码，只抓取了学科名称，要查数据库获取学科代码
      String thesisDeptName = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PUBLICATION_XPATH, "thesis_dept_name");// 获取抓取的学科名称
      if (StringUtils.isNotBlank(thesisDeptName)) {
        Long id = constDisciplineDao.getIdByName(thesisDeptName.split(";|；")[0]);
        if (id != null) {
          disciplineId = id.toString();
        }
      }
    }
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PUBLICATION_XPATH, "discipline", disciplineId);
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "discipline", disciplineId);
  }

  /*
   * 添加资金单位.
   */
  private void dealWithPrjAmount(PrjXmlDocument xmlDocument) {
    String zhDBcode = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "zh_source_db_name");
    String enDBcode = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "en_source_db_name");
    String amount = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount");
    // 如果金额为0或非正常金额数，要清空prj的amount属性值，不然显示是可能显示成RMB 0
    amount = MoneyFormatterUtils.formatNum(amount);
    if (StringUtils.isBlank(amount) || "0".equals(amount)) {
      xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount", "");
    }
    // 资金单位
    if (StringUtils.isBlank(xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PUBLICATION_XPATH, "amount_unit"))) {
      if ("创新及科技基金".equals(zhDBcode) || "Innovation and Technology Fund".equals(enDBcode)) {
        xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PUBLICATION_XPATH, "amount_unit", "HKD");
        xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount_unit", "HKD");
      } else {
        xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PUBLICATION_XPATH, "amount_unit", "RMB");
        xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount_unit", "RMB");
      }
    }
  }

  /*
   * 拷贝属性
   */
  private void copyOtherXmlAttributes(PrjXmlDocument xmlDocument) {
    xmlDocument.copyAttributeValue(PrjXmlConstants.PROJECT_XPATH, "organization", PrjXmlConstants.PROJECT_XPATH,
        "ins_info");
    xmlDocument.copyAttributeValue(PrjXmlConstants.PROJECT_XPATH, "source", PrjXmlConstants.PROJECT_XPATH,
        "source_catalog");
    xmlDocument.copyAttributeValue(PrjXmlConstants.PROJECT_XPATH, "ctitle", PrjXmlConstants.PROJECT_XPATH, "zh_title");
    xmlDocument.copyAttributeValue(PrjXmlConstants.PROJECT_XPATH, "etitle", PrjXmlConstants.PROJECT_XPATH, "en_title");
    xmlDocument.copyAttributeValue(PrjXmlConstants.PROJECT_XPATH, "cabstract", PrjXmlConstants.PROJECT_XPATH,
        "zh_abstract");
    xmlDocument.copyAttributeValue(PrjXmlConstants.PROJECT_XPATH, "eabstract", PrjXmlConstants.PROJECT_XPATH,
        "en_abstract");
    xmlDocument.copyAttributeValue(PrjXmlConstants.PROJECT_XPATH, "ckeywords", PrjXmlConstants.PROJECT_XPATH,
        "zh_keywords");
    xmlDocument.copyAttributeValue(PrjXmlConstants.PROJECT_XPATH, "ekeywords", PrjXmlConstants.PROJECT_XPATH,
        "en_keywords");
    xmlDocument.copyAttributeValue(PrjXmlConstants.PROJECT_XPATH, "prj_no", PrjXmlConstants.PROJECT_XPATH,
        "prj_external_no");
    xmlDocument.copyAttributeValue(PrjXmlConstants.PROJECT_XPATH, "prj_scheme_agency_name",
        PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name");
    xmlDocument.copyAttributeValue(PrjXmlConstants.PROJECT_XPATH, "prj_scheme_name", PrjXmlConstants.PROJECT_XPATH,
        "scheme_name");
    xmlDocument.copyAttributeValue(PrjXmlConstants.PROJECT_XPATH, "organization", PrjXmlConstants.PROJECT_XPATH,
        "ins_name");
    // 删除不必要的XML
    xmlDocument.removeAttribute(PrjXmlConstants.PROJECT_XPATH, "ctitle", "etitle", "cabstract", "eabstract",
        "ckeywords", "ekeywords", "source_url", "prj_no", "prj_scheme_agency_name", "prj_scheme_name", "organization");
  }

  // 重新将人员姓名用分号拼接

  protected void dealWithAuthorNames(PrjXmlDocument prjXmlDoc) {
    String authorNamesStr = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_names");
    String[] authorNames =
        StringUtils.isNotBlank(authorNamesStr) ? StringUtils.stripAll(authorNamesStr.split("，|；|;| and ")) : null;
    if (authorNames != null) {
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_names", StringUtils.join(authorNames, "; "));
    }
  }
}
