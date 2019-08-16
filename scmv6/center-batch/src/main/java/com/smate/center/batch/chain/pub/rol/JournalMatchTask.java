package com.smate.center.batch.chain.pub.rol;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.pub.BaseJournalTitleTo;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pdwh.pub.JournalNoSeqService;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 匹配期刊.
 * 
 * @author zk
 */
public class JournalMatchTask implements IPubXmlTask {
  /**
   * 
   */
  private final Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 
   */
  private final String name = "journal_match";
  @Autowired
  private JournalNoSeqService JournalNoSeqService;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    // 是否存在期刊，如果存在，才进入
    boolean flag1 = xmlDocument.existsNode(PubXmlConstants.PUB_JOURNAL_XPATH);
    String jid = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "jid");
    return flag1 || !"".equals(jid);
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    Long jid = IrisNumberUtils.createLong(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid"));
    String jname = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname");
    String issn = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jissn");
    if (StringUtils.isBlank(issn)) {
      issn = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issn");
    }
    String newJournalFrom = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "source_db_code");
    jname = XmlUtil.changeSBCChar(jname);
    issn = XmlUtil.buildStandardIssn(XmlUtil.changeSBCChar(issn));
    BaseJournalTitleTo journalInfo = null;
    // 导入可能期刊名称为空，否则编辑期刊名称不能为空
    if (StringUtils.isNotBlank(jname) && StringUtils.isNotBlank(jname)) {
      List<BaseJournalTitleTo> journalList = JournalNoSeqService.getSnsJnlMatchBaseJnlId(jname, issn);
      if (journalList != null && journalList.size() > 0) {
        journalInfo = journalList.get(0);
      }
    }
    // 如果期刊不存在，更加期刊名,issn查找
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "impact_factors", "");

    String zhJnlName = jname;
    String enJnlName = jname;
    if (journalInfo != null) {
      zhJnlName = journalInfo.getTitleXx();
      enJnlName = journalInfo.getTitleEn();
      issn = journalInfo.getPissn();
    }
    // 期刊统一放在1节点
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "node_id", "1");
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid", jid == null ? "" : jid.toString());
    // 匹配的基础期刊id
    // 如果jname中包含有中文字符则表明是中文,则重置期刊名
    if (XmlUtil.isChinese(jname)) {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "zh_name",
          StringUtils.isBlank(zhJnlName) ? enJnlName : zhJnlName);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname",
          StringUtils.isBlank(zhJnlName) ? enJnlName : zhJnlName);
    } else {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "en_name",
          StringUtils.isBlank(enJnlName) ? zhJnlName : enJnlName);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname",
          StringUtils.isBlank(enJnlName) ? zhJnlName : enJnlName);
    }
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jissn", issn);
    return true;
  }

}
