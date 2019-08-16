package com.smate.center.batch.chain.pubassign.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.rol.pubassign.PubAssignCniprXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignCnkiPatXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignCnkiXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignEiXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignPubMedXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignSpsXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignXmlService;
import com.smate.core.base.utils.constant.PubXmlConstants;

/**
 * 展开成果关键词.
 * 
 * @author liqinghua
 * 
 */
public class ExtractKeywordsTask implements IPubAssignXmlTask {

  private final String name = "ExtractJournalTask";
  @Autowired
  private PubAssignXmlService pubAssignXmlService;
  @Autowired
  private PubAssignCnkiXmlService pubAssignCnkiXmlService;
  @Autowired
  private PubAssignSpsXmlService pubAssignSpsXmlService;
  @Autowired
  private PubAssignCniprXmlService pubAssignCniprXmlService;
  @Autowired
  private PubAssignCnkiPatXmlService pubAssignCnkiPatXmlService;
  @Autowired
  private PubAssignPubMedXmlService pubAssignPubMedXmlService;
  @Autowired
  private PubAssignEiXmlService pubAssignEiXmlService;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {

    return true;
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    String ekeywords = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords");
    String ckeywords = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords");
    String keywordPlus = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "keyword_plus");
    List<String> hashSaved = new ArrayList<String>();
    this.extractKeyWordToDb(ekeywords, hashSaved, context);
    this.extractKeyWordToDb(ckeywords, hashSaved, context);
    this.extractKeyWordToDb(keywordPlus, hashSaved, context);
    return true;
  }

  /**
   * 展开数据.
   * 
   * @param keywords
   * @param hashSaved
   * @param context
   * @throws Exception
   */
  private void extractKeyWordToDb(String keywords, List<String> hashSaved, PubXmlProcessContext context)
      throws Exception {
    if (StringUtils.isBlank(keywords)) {
      return;
    }
    // 不应该直接这样处理，可能会发生可能截断不完整关键词的情况；
    // keywords = StringUtils.substring(keywords.trim().toLowerCase(), 0, 100);
    // 都是本线程操作，所以不存在现场不安全情况，用stringbuilder就可以了，速度更快
    StringBuilder sBuilder = new StringBuilder();

    String[] akeywords = null;
    if (keywords.indexOf(";") >= 0)
      akeywords = keywords.split(";");
    else {
      akeywords = keywords.split(",");
    }
    for (String keyword : akeywords) {

      if (sBuilder.length() > 100) {
        break;
      }
      String tkeyword = StringUtils.substring(keyword.trim().toLowerCase(), 0, 100);
      if (hashSaved.contains(tkeyword)) {
        continue;
      }
      saveKeyWordToDb(context, tkeyword);
      hashSaved.add(tkeyword);
      sBuilder.append(tkeyword);
    }

  }

  private void saveKeyWordToDb(PubXmlProcessContext context, String tkeyword) throws ServiceException {
    if (context.isCnkiImport()) {
      this.pubAssignCnkiXmlService.savePubKeywords(tkeyword, context.getCurrentPubId(), context.getCurrentInsId());
    } else if (context.isIsiImport()) {
      this.pubAssignXmlService.savePubKeywords(tkeyword, context.getCurrentPubId(), context.getCurrentInsId());
    } else if (context.isScopusImport()) {
      this.pubAssignSpsXmlService.savePubKeywords(tkeyword, context.getCurrentPubId(), context.getCurrentInsId());
    } else if (context.isCniprImport()) {
      this.pubAssignCniprXmlService.savePubKeywords(tkeyword, context.getCurrentPubId(), context.getCurrentInsId());
    } else if (context.isCnkiPatImport()) {
      this.pubAssignCnkiPatXmlService.savePubKeywords(tkeyword, context.getCurrentPubId(), context.getCurrentInsId());
    } else if (context.isPubMedImport()) {
      this.pubAssignPubMedXmlService.savePubKeywords(tkeyword, context.getCurrentPubId(), context.getCurrentInsId());
    } else if (context.isEiImport()) {
      this.pubAssignEiXmlService.savePubKeywords(tkeyword, context.getCurrentPubId(), context.getCurrentInsId());
    }

  }

}
