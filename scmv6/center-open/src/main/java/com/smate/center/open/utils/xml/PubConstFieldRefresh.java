package com.smate.center.open.utils.xml;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;

import com.smate.center.open.model.publication.PubXmlDocument;
import com.smate.center.open.service.publication.IPubXmlServiceFactory;
import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.service.consts.ConstDictionaryManage;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 刷新xml常数字段.
 * 
 * @author yamingd
 */
public class PubConstFieldRefresh {

  private Document xmlDocument = null;

  protected static final Logger LOGGER = LoggerFactory.getLogger(PubConstFieldRefresh.class);

  /**
   * 刷新XML.
   * 
   * @param xmlDocument
   * @param xmlDaoService
   * @param journalSerivce
   * @throws Exception
   */
  public static void refresh(PubXmlDocument xmlDocument, IPubXmlServiceFactory xmlServiceFactory) throws Exception {

    Assert.notNull(xmlDocument);
    Assert.notNull(xmlServiceFactory);

    Locale locale = LocaleContextHolder.getLocale();

    ConstDictionaryManage constDictionaryManage = xmlServiceFactory.getConstDictionaryManage();

    String bookType = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "book_type");
    if (!StringUtils.isBlank(bookType)) {
      try {
        ConstDictionary cd = constDictionaryManage.findConstByCategoryAndCode("pub_book_type", bookType);
        if (cd == null) {
          throw new Exception("读取pub_book_type常数错误，code=" + bookType);
        } else {
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "zh_book_type_name", cd.getZhCnName());
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "en_book_type_name", cd.getEnUsName());
        }
      } catch (Exception e) {
        throw e;
      }
    }

    String confType = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_paper");
    if (!StringUtils.isBlank(confType)) {
      ConstDictionary cd = constDictionaryManage.findConstByCategoryAndCode("pub_conf_type", confType);
      if (cd == null) {
        throw new Exception("读取pub_conf_type常数错误，code=" + confType);
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "zh_conf_type_name", cd.getZhCnName());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "en_conf_type_name", cd.getEnUsName());
      }
    }

    String confCategory = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_category");
    if (!StringUtils.isBlank(confCategory)) {
      ConstDictionary cd = constDictionaryManage.findConstByCategoryAndCode("pub_conf_category", confCategory);
      if (cd == null) {
        throw new Exception("读取pub_conf_category常数错误，code=" + confCategory);
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "zh_conf_category_name",
            cd.getZhCnName());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "en_conf_category_name",
            cd.getEnUsName());
      }
    }
    String confPaperType = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "paper_type");
    if (!StringUtils.isBlank(confPaperType)) {
      ConstDictionary cd = constDictionaryManage.findConstByCategoryAndCode("pub_paper_type", confPaperType);
      if (cd == null) {
        throw new Exception("读取pub_paper_type常数错误，code=" + confPaperType);
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "zh_paper_type_name", cd.getZhCnName());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "en_paper_type_name", cd.getEnUsName());
      }
    }

    String publishState = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_state");
    if (!StringUtils.isBlank(publishState)) {
      ConstDictionary cd = constDictionaryManage.findConstByCategoryAndCode("pub_publish_state", publishState);
      if (cd == null) {
        throw new Exception("读取pub_publish_state常数错误，code=" + publishState);
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_publish_state_name", cd.getZhCnName());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_publish_state_name", cd.getEnUsName());
      }
    }

    String patentType = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_type");
    if (!StringUtils.isBlank(patentType)) {
      ConstDictionary cd = constDictionaryManage.findConstByCategoryAndCode("pub_patent_type", patentType);
      if (cd == null) {
        throw new Exception("读取pub_patent_type常数错误，code=" + patentType);
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "zh_patent_type_name", cd.getZhCnName());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "en_patent_type_name", cd.getEnUsName());
      }
    }

    String programme = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_THESIS_XPATH, "programme");
    if (!StringUtils.isBlank(programme)) {
      ConstDictionary cd = constDictionaryManage.findConstByCategoryAndCode("pub_thesis_degree", programme);
      if (cd == null) {
        LOGGER.error("读取pub_thesis_degree常数错误，code=" + programme);
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_THESIS_XPATH, "zh_programme_name", cd.getZhCnName());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_THESIS_XPATH, "en_programme_name", cd.getEnUsName());
      }
    }

    String jid = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid");
    if (!StringUtils.isBlank(jid)) {
      try {

        Long userId = IrisNumberUtils
            .createLong(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "last_update_psn_id"));
        if (userId == null) {
          userId = IrisNumberUtils
              .createLong(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "create_psn_id"));
        }
      } catch (NumberFormatException e) {
        throw new Exception("NumberFormatException读取journal常数错误，jid=" + jid);
      }
    }

    String citeTimes = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times");
    String citeDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cited_date");
    if (StringUtils.isNotBlank(citeTimes)) {
      try {
        Integer ct = Integer.valueOf(citeTimes);
        if (ct >= 0) {
          String datePattern = PubXmlConstants.CITE_CHS_DATE_PATTERN;
          if ("en".equals(locale.getLanguage())) {
            datePattern = PubXmlConstants.CITE_ENG_DATE_PATTERN;
          }
          if (StringUtils.isBlank(citeDate)) {
            citeDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "create_date");
          }
          if (StringUtils.isNotBlank(citeDate)) {
            citeDate = citeDate.replace("/", "-");
            Date date = DateUtils.parseDate(citeDate, new String[] {"yyyy-MM-dd"});
            citeDate = DateFormatUtils.format(date, datePattern);
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cited_date", citeDate);
          }
        }
      } catch (Exception e) {
        xmlDocument.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times",
            StringUtils.isBlank(citeTimes) ? "" : citeTimes);
      }
    }
  }

  /**
   * 设置属性，防止旧的XML文档覆盖 新生成的XML文档中的cite_times属性值.
   * 
   * @param nodepath xml元素路径
   * @param attrName xml属性名
   * @param newValue xml属性新值
   */
  public void setNodeAttribute(String nodepath, String attrName, String newValue) {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    if (attrName.startsWith("@")) {
      attrName = attrName.substring(1);
    }
    Element ele = (Element) this.getNode(nodepath);
    if (ele == null) {
      ele = this.createElement(nodepath);
    }
    if (ele != null) {
      ele.addAttribute(attrName, newValue);
    }
  }

  /**
   * @param xpath xml元素路径
   * @return Node
   */
  public Node getNode(String xpath) {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    if (!xpath.startsWith(PubXmlConstants.ROOT_XPATH))
      xpath = PubXmlConstants.ROOT_XPATH + xpath;
    return this.xmlDocument.selectSingleNode(xpath);
  }

  /**
   * @param elePath xml元素路径 (e.g. /publication, /pub_members/pub_member[01], /pub_members, )
   * @return Element
   */
  public Element createElement(String elePath) {
    if (XmlUtil.isEmpty(elePath)) {
      throw new java.lang.NullPointerException("can't create Element with NULL (elePath).");
    }

    elePath = elePath.toLowerCase();
    String[] paths = XmlUtil.splitXPath(elePath); // 分割元素和元素的属性
    String[] segs = paths[0].split("/"); // will return ['/','publication']
    // or
    // ['/','pub_members','pub_member[1]']
    Element root = (Element) this.getRootNode();
    Element parentEle = (Element) this.getNode("/" + segs[1]);
    if (parentEle == null) {
      parentEle = root.addElement(segs[1]);
    }
    // 只支持2级节点
    if (segs.length > 2) {
      String name = segs[2];
      int pos = name.indexOf("[");
      Integer seqNo = null;
      if (pos > 0) {// 获取序号
        String tmp = name.substring(pos + 1, name.length() - 1);
        seqNo = Integer.parseInt(tmp);
      }
      // 删除序号
      name = name.replaceAll("\\[.*\\]", "");
      String selectedPath = segs[2];
      Element ele = null;

      if (seqNo != null) {
        // 页面提交的数据顺序是不确定的，因此如果前面序号的节点后到，则提前创建，防止页面获取XML数据乱序
        for (int i = 1; i <= seqNo; i++) {
          selectedPath = name + "[@seq_no=" + String.valueOf(i) + "]";
          ele = (Element) parentEle.selectSingleNode(selectedPath); // segs[2]如:pub_member[01]
          if (ele == null) {
            ele = parentEle.addElement(name);
            ele.addAttribute("seq_no", String.valueOf(i));
          }
        }
      } else {
        ele = (Element) parentEle.selectSingleNode(selectedPath);
        if (ele == null) {
          ele = parentEle.addElement(name);
        }
      }
      return ele;
    }
    return parentEle;
  }

  /**
   * 获取根节点.
   * 
   * @return Node
   */
  public Node getRootNode() {
    return this.xmlDocument.selectSingleNode(PubXmlConstants.ROOT_XPATH);
  }



}
