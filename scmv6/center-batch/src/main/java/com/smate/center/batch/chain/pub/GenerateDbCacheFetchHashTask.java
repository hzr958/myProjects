package com.smate.center.batch.chain.pub;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.util.pub.PubXmlDbUtils;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * 在线导入，生产查询基准库hash任务.
 * 
 * @author liqinghua
 * 
 */
public class GenerateDbCacheFetchHashTask implements IPubXmlTask {

  /**
   * 
   */
  private final String name = "GenerateDbCacheFetchHashTask";

  @Override
  public String getName() {

    return name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    // 在center-batch中，从pdwh导入group，也需要运行
    // return XmlOperationEnum.Import.equals(context.getCurrentAction());
    return true;
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    String dbCode = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "source_db_code");
    dbCode =
        StringUtils.isBlank(dbCode) ? xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_db_code")
            : dbCode;
    if (StringUtils.isBlank(dbCode)) {
      return true;
    }

    Long titleHash = null;
    Long unitHash = null;
    Long sourceIdHash = null;
    Long patentHash = null;

    String zhtitle = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ctitle");
    String entitle = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "etitle");
    String original = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original");
    String sourceId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "source_id");
    Integer pubyear = xmlDocument.getImportPubYear();
    String authorNames = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names");
    String patentNo = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_no");

    // isi文献
    if (PubXmlDbUtils.isIsiDb(dbCode)) {
      sourceIdHash = PubHashUtils.cleanSourceIdHash(sourceId);
      titleHash = PubHashUtils.cleanTitleHash(entitle);
      unitHash = PubHashUtils.getEnPubUnitFingerPrint(pubyear, original, authorNames);
      // scopus文献
    } else if (PubXmlDbUtils.isScopusDb(dbCode)) {
      sourceIdHash = PubHashUtils.cleanSourceIdHash(sourceId);
      titleHash = PubHashUtils.cleanTitleHash(entitle);
      unitHash = PubHashUtils.getEnPubUnitFingerPrint(pubyear, original, authorNames);
      // cnki文献
    } else if (PubXmlDbUtils.isCnkiDb(dbCode)) {
      titleHash = PubHashUtils.cleanTitleHash(zhtitle, entitle);
      unitHash = PubHashUtils.getEnPubUnitFingerPrint(pubyear, original, authorNames);
      // ei
    } else if (PubXmlDbUtils.isEiDb(dbCode)) {
      sourceIdHash = PubHashUtils.cleanSourceIdHash(sourceId);
      titleHash = PubHashUtils.cleanTitleHash(entitle);
      unitHash = PubHashUtils.getEnPubUnitFingerPrint(pubyear, original, authorNames);
      // wanfang
    } else if (PubXmlDbUtils.isWanFangDb(dbCode)) {
      titleHash = PubHashUtils.cleanTitleHash(zhtitle, entitle);
      unitHash = PubHashUtils.getEnPubUnitFingerPrint(pubyear, original, authorNames);
      // CNIPRDb
    } else if (PubXmlDbUtils.isCNIPRDb(dbCode)) {
      patentHash = PubHashUtils.cleanPatentNoHash(patentNo);
      titleHash = PubHashUtils.cleanTitleHash(zhtitle, entitle);
    } // PUBMED
    else if (PubXmlDbUtils.isPubMedDb(dbCode)) {
      sourceIdHash = PubHashUtils.cleanSourceIdHash(sourceId);
      titleHash = PubHashUtils.cleanTitleHash(entitle);
      unitHash = PubHashUtils.getEnPubUnitFingerPrint(pubyear, original, authorNames);
    } // IEEEXP
    else if (PubXmlDbUtils.isIEEEXploreDb(dbCode)) {
      sourceIdHash = PubHashUtils.cleanSourceIdHash(sourceId);
      titleHash = PubHashUtils.cleanTitleHash(entitle);
      unitHash = PubHashUtils.getEnPubUnitFingerPrint(pubyear, original, authorNames);
    } // ScienceDirect
    else if (PubXmlDbUtils.isScienceDirectDb(dbCode)) {
      titleHash = PubHashUtils.cleanTitleHash(entitle);
      unitHash = PubHashUtils.getEnPubUnitFingerPrint(pubyear, original, authorNames);
    } // BAIDU
    else if (PubXmlDbUtils.isBaiduDb(dbCode)) {
      patentHash = PubHashUtils.cleanPatentNoHash(patentNo);
      titleHash = PubHashUtils.cleanTitleHash(zhtitle, entitle);
    } // CNKIPAT
    else if (PubXmlDbUtils.isCnkipatDb(dbCode)) {
      patentHash = PubHashUtils.cleanPatentNoHash(patentNo);
      titleHash = PubHashUtils.cleanTitleHash(zhtitle, entitle);
    } else {
      return true;
    }
    // 标题不能为空
    if (titleHash == null) {
      return true;
    }

    Element ele = (Element) xmlDocument.getNode(PubXmlConstants.PUB_LIST_XPATH);
    if (ele == null) {
      ele = xmlDocument.createElement(PubXmlConstants.PUB_LIST_XPATH);
    }
    ele.addAttribute("title_hash", titleHash == null ? "" : titleHash.toString());
    ele.addAttribute("unit_hash", unitHash == null ? "" : unitHash.toString());
    ele.addAttribute("source_id_hash", sourceIdHash == null ? "" : sourceIdHash.toString());
    ele.addAttribute("patent_hash", patentHash == null ? "" : patentHash.toString());
    return true;
  }

}
