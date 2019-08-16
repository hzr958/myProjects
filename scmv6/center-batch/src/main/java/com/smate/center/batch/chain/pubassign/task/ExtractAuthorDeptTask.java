package com.smate.center.batch.chain.pubassign.task;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
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
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 展开成果作者部门.
 * 
 * @author liqinghua
 * 
 */
public class ExtractAuthorDeptTask implements IPubAssignXmlTask {

  private final String name = "ExtractAuthorDeptTask";
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
    return context.isImport();
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
    List authorList = xmlDocument.getPubAuthorList();
    if (authorList != null && authorList.size() > 0) {
      for (int i = 0; i < authorList.size(); i++) {
        Element author = (Element) authorList.get(i);
        String dept = StringUtils.trimToEmpty(author.attributeValue("addr"));
        Integer seqNo = IrisNumberUtils.createInteger(author.attributeValue("seq_no"));
        if (StringUtils.isBlank(dept) || seqNo == null) {
          continue;
        }
        dept = dept.trim().toLowerCase();
        // 查找是否有本单位，如果没有，随机取一个单位
        Long insId = ExtractIsiAuthorNameTask.getAuthorInsId(author, context);
        saveAuthorDeptToDb(context, dept, seqNo, insId);
      }
    }
    return true;
  }

  private void saveAuthorDeptToDb(PubXmlProcessContext context, String dept, Integer seqNo, Long insId)
      throws ServiceException {
    if (context.isCnkiImport()) {
      this.pubAssignCnkiXmlService.savePubDept(dept, context.getCurrentPubId(), context.getCurrentInsId(), seqNo,
          insId);
    } else if (context.isIsiImport()) {
      this.pubAssignXmlService.savePubDept(dept, context.getCurrentPubId(), context.getCurrentInsId(), seqNo, insId);
    } else if (context.isScopusImport()) {
      this.pubAssignSpsXmlService.savePubDept(dept, context.getCurrentPubId(), context.getCurrentInsId(), seqNo, insId);
    } else if (context.isCniprImport()) {
      this.pubAssignCniprXmlService.savePubDept(dept, context.getCurrentPubId(), context.getCurrentInsId(), seqNo,
          insId);
    } else if (context.isCnkiPatImport()) {
      this.pubAssignCnkiPatXmlService.savePubDept(dept, context.getCurrentPubId(), context.getCurrentInsId(), seqNo,
          insId);
    } else if (context.isPubMedImport()) {
      this.pubAssignPubMedXmlService.savePubDept(dept, context.getCurrentPubId(), context.getCurrentInsId(), seqNo,
          insId);
    } else if (context.isEiImport()) {
      this.pubAssignEiXmlService.savePubDept(dept, context.getCurrentPubId(), context.getCurrentInsId(), seqNo, insId);
    }

  }

}
