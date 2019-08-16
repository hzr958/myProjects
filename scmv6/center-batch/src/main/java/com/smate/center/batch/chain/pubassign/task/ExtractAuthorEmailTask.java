package com.smate.center.batch.chain.pubassign.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.rol.pubassign.PubAssignEiXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignPubMedXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignSpsXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignXmlService;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 展开成果作者EMAIL.
 * 
 * @author liqinghua
 * 
 */
public class ExtractAuthorEmailTask implements IPubAssignXmlTask {

  private final String name = "ExtractAuthorEmailTask";
  @Autowired
  private PubAssignXmlService pubAssignXmlService;
  @Autowired
  private PubAssignSpsXmlService pubAssignSpsXmlService;
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
    return context.isImport()
        && (context.isIsiImport() || context.isScopusImport() || context.isPubMedImport() || context.isEiImport());
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
    List authorList = xmlDocument.getPubAuthorList();
    if (authorList != null && authorList.size() > 0) {
      List<String> hashSaved = new ArrayList<String>();
      for (int i = 0; i < authorList.size(); i++) {
        Element author = (Element) authorList.get(i);
        String email = StringUtils.trimToEmpty(author.attributeValue("email"));
        Integer seqNo = IrisNumberUtils.createInteger(author.attributeValue("seq_no"));
        if (StringUtils.isBlank(email)) {
          continue;
        }
        // 查找是否有本单位，如果没有，随机取一个单位
        Long insId = ExtractIsiAuthorNameTask.getAuthorInsId(author, context);
        // email可能是多个
        String[] emails = email.split("[;,\\t\\n\\x0B\\f\\r ]{1,}");
        for (String strEmail : emails) {
          if (StringUtils.isBlank(strEmail)) {
            continue;
          }
          String tpemail = StringUtils.substring(strEmail.trim().toLowerCase(), 0, 100);
          if (hashSaved.contains(tpemail)) {
            continue;
          }
          if (context.isIsiImport()) {
            this.pubAssignXmlService.savePubEmail(tpemail, context.getCurrentPubId(), context.getCurrentInsId(), insId,
                seqNo);
          } else if (context.isScopusImport()) {
            this.pubAssignSpsXmlService.savePubEmail(tpemail, context.getCurrentPubId(), context.getCurrentInsId(),
                insId, seqNo);
          } else if (context.isPubMedImport()) {
            this.pubAssignPubMedXmlService.savePubEmail(tpemail, context.getCurrentPubId(), context.getCurrentInsId(),
                insId, seqNo);
          } else if (context.isEiImport()) {
            this.pubAssignEiXmlService.savePubEmail(tpemail, context.getCurrentPubId(), context.getCurrentInsId(),
                insId, seqNo);
          }

          hashSaved.add(strEmail);
        }
      }
    }
    return true;
  }

}
