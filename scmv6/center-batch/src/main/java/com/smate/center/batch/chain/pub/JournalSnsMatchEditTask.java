package com.smate.center.batch.chain.pub;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.model.sns.pub.BaseJournalSns;
import com.smate.center.batch.model.sns.pub.JournalSns;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.BaseJournalSnsService;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.service.pub.JournalSnsService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 匹配期刊. 新增编辑执行
 * 
 * @author lxz
 *
 */
public class JournalSnsMatchEditTask implements IPubXmlTask {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final String name = "journal_match";
  @Autowired
  private JournalSnsService journalSnsService;
  @Autowired
  private BaseJournalSnsService baseJournalSnsService;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    // 导入的不执行
    if (XmlOperationEnum.Import.equals(context.getCurrentAction())) {
      return false;
    }
    // 是否存在期刊，如果存在， 才进入
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
    JournalSns journalsns = null;

    // 导入可能期刊名称为空，否则编辑期刊名称不能为空
    if (jid != null && (context.isImport() || StringUtils.isNotBlank(jname))) {
      journalsns = journalSnsService.getById(jid);
    }
    // 如果期刊不存在，更加期刊名,issn查找
    if (journalsns == null && StringUtils.isNotBlank(jname)) {
      Long userId = context.getCurrentUserId();
      if (userId != null && userId == 0) {
        userId = null;
      }
      journalsns = journalSnsService.findJournalByNameIssn(jname, issn, userId);
    }
    // 如果期刊不存在，创建期刊
    if (journalsns == null && StringUtils.isNotBlank(jname)) {
      journalsns = journalSnsService.addJournal(jname, issn, context.getCurrentUserId(), newJournalFrom);
    }
    if (journalsns != null) {
      try {
        // 影响因子 及 基础期刊标题是否需要冗余
        if (journalsns.getMatchBaseJnlId() != null) {
          BaseJournalSns bjsns = baseJournalSnsService.getById(journalsns.getMatchBaseJnlId());
          String imp = bjsns.getImpactFactors();
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "impact_factors", imp);
        }
      } catch (Exception e) {
        logger.error("读取期刊ImpactFactors错误", e);
      }
      // 期刊统一放在1节点
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "node_id", "1");
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid", String.valueOf(journalsns.getId()));
      // 匹配的基础期刊id
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jnl_id",
          ObjectUtils.toString(journalsns.getMatchBaseJnlId()));
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "jid", String.valueOf(journalsns.getId()));
      // 如果jname中包含有中文字符则表明是中文,则重置期刊名
      if (XmlUtil.isChinese(jname)) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "zh_name", jname);
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "en_name", jname);
      }
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname", jname);

      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jissn",
          StringUtils.isBlank(issn) ? StringUtils.trimToEmpty(journalsns.getIssn()) : issn);
    } else {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid", "");
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jnl_id", "");
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "jid", "");
    }

    return true;
  }

}
