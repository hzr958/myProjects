package com.smate.center.batch.chain.pub.rol;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.rol.pub.ImportCniprXmlRebuildAuthService;
import com.smate.center.batch.service.rol.pub.ImportCnkiXmlRebuildAuthService;
import com.smate.center.batch.service.rol.pub.ImportEiXmlRebuildAuthService;
import com.smate.center.batch.service.rol.pub.ImportPubMedXmlRebuildAuthService;
import com.smate.center.batch.service.rol.pub.ImportSpsXmlRebuildAuthService;
import com.smate.center.batch.service.rol.pub.ImportXmlRebuildAuthService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 导入人员，拆分人员.
 * 
 * @author liqinghua
 * 
 */
public class RolSplitImportAuthorNameTask extends SplitImportAuthorNameTask {

  /**
   * 
   */
  private String name = "rol_split_import_author_name";
  @Autowired
  private ImportXmlRebuildAuthService importXmlRebuildAuthService;
  @Autowired
  private ImportCnkiXmlRebuildAuthService importCnkiXmlRebuildAuthService;
  @Autowired
  private ImportSpsXmlRebuildAuthService importSpsXmlRebuildAuthService;
  @Autowired
  private ImportCniprXmlRebuildAuthService importCniprXmlRebuildAuthService;
  @Autowired
  private ImportPubMedXmlRebuildAuthService importPubMedXmlRebuildAuthService;
  @Autowired
  private ImportEiXmlRebuildAuthService importEiXmlRebuildAuthService;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {

    // 当前PUBID不为空，说明是合并成果，合并成果保留原有pub_member不动
    return context.isImport() && context.getCurrentPubId() == null;
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    // 是否是isi导入、PubMED, EI导入.
    if (context.isIsiImport() || context.isPubMedImport() || context.isEiImport()) {
      splitIsiImportAuthor(xmlDocument, context);
      // 是否是CNKI导入
    } else if (context.isCnkiImport()) {
      splitCnkiImportAuthor(xmlDocument, context);
      // 是否是scopus导入
    } else if (context.isScopusImport()) {
      splitScopusImportAuthor(xmlDocument, context);
      // 是否是CNIPR、CNKIPAT导入
    } else if (context.isCniprImport() || context.isCnkiPatImport()) {
      splitCniprImportAuthor(xmlDocument, context);
    } else {
      super.run(xmlDocument, context);
    }
    String authorNames = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names");
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original_author_names", authorNames);
    return true;
  }

  @SuppressWarnings("rawtypes")
  private void splitCnkiImportAuthor(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws ServiceException {
    // 处理作者节点
    importCnkiXmlRebuildAuthService.rebuildAuthXml(xmlDocument, context.getCurrentInsId());

    List authorList = xmlDocument.getPubAuthorList();
    if (authorList != null && authorList.size() > 0) {
      // 删除并创建新的pub_member节点
      xmlDocument.removeNode(PubXmlConstants.PUB_MEMBERS_XPATH);
      Element pubMembers = xmlDocument.createElement(PubXmlConstants.PUB_MEMBERS_XPATH);
      // 序号
      int k = 1;
      for (int i = 0; i < authorList.size(); i++) {
        Element author = (Element) authorList.get(i);
        String authName = author.attributeValue("name");
        if (StringUtils.isBlank(authName)) {
          authName = author.attributeValue("au");
        }
        if (StringUtils.isBlank(authName)) {
          continue;
        }
        this.buildPubMemberData(author, context, pubMembers, authName, k);
        k++;
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private void splitCniprImportAuthor(PubXmlDocument xmlDocument, PubXmlProcessContext context)
      throws ServiceException {
    // 处理作者节点
    importCniprXmlRebuildAuthService.rebuildAuthXml(xmlDocument, context.getCurrentInsId());

    List authorList = xmlDocument.getPubAuthorList();
    if (authorList != null && authorList.size() > 0) {
      // 删除并创建新的pub_member节点
      xmlDocument.removeNode(PubXmlConstants.PUB_MEMBERS_XPATH);
      Element pubMembers = xmlDocument.createElement(PubXmlConstants.PUB_MEMBERS_XPATH);
      // 序号
      int k = 1;
      for (int i = 0; i < authorList.size(); i++) {
        Element author = (Element) authorList.get(i);
        String authName = author.attributeValue("name");
        if (StringUtils.isBlank(authName)) {
          authName = author.attributeValue("au");
        }
        if (StringUtils.isBlank(authName)) {
          continue;
        }
        this.buildPubMemberData(author, context, pubMembers, authName, k);
        k++;
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private void splitScopusImportAuthor(PubXmlDocument xmlDocument, PubXmlProcessContext context)
      throws ServiceException {
    // 处理作者节点
    importSpsXmlRebuildAuthService.rebuildAuthXml(xmlDocument, context.getCurrentInsId());

    List authorList = xmlDocument.getPubAuthorList();
    if (authorList != null && authorList.size() > 0) {
      // 删除并创建新的pub_member节点
      xmlDocument.removeNode(PubXmlConstants.PUB_MEMBERS_XPATH);
      Element pubMembers = xmlDocument.createElement(PubXmlConstants.PUB_MEMBERS_XPATH);
      // 序号
      int k = 1;
      for (int i = 0; i < authorList.size(); i++) {
        Element author = (Element) authorList.get(i);
        String authName = author.attributeValue("name");
        if (StringUtils.isBlank(authName)) {
          continue;
        }
        this.buildPubMemberData(author, context, pubMembers, authName, k);
        k++;
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private void splitIsiImportAuthor(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws ServiceException {

    // 处理作者节点
    if (context.isIsiImport()) {
      importXmlRebuildAuthService.rebuildAuthXml(xmlDocument, context.getCurrentInsId());
    } else if (context.isPubMedImport()) {
      importPubMedXmlRebuildAuthService.rebuildAuthXml(xmlDocument, context.getCurrentInsId());
    } else if (context.isEiImport()) {
      importEiXmlRebuildAuthService.rebuildAuthXml(xmlDocument, context.getCurrentInsId());
    }

    List authorList = xmlDocument.getPubAuthorList();
    if (authorList != null && authorList.size() > 0) {
      // 删除并创建新的pub_member节点
      xmlDocument.removeNode(PubXmlConstants.PUB_MEMBERS_XPATH);
      Element pubMembers = xmlDocument.createElement(PubXmlConstants.PUB_MEMBERS_XPATH);
      // 序号
      int k = 1;
      for (int i = 0; i < authorList.size(); i++) {
        Element author = (Element) authorList.get(i);
        String abbrName = author.attributeValue("abbr_name");
        String fullName = author.attributeValue("full_name");
        String authName = StringUtils.isBlank(fullName) ? abbrName : fullName;
        if (StringUtils.isBlank(authName)) {
          continue;
        }
        this.buildPubMemberData(author, context, pubMembers, authName, k);
        k++;
      }
    }
  }

  private void buildPubMemberData(Element author, PubXmlProcessContext context, Element pubMembers, String authName,
      int seqNo) throws ServiceException {
    Integer insCount = IrisNumberUtils.createInteger(StringUtils.trimToEmpty(author.attributeValue("ins_count")));
    // 人员ID是否是数字
    String psnIdStr = StringUtils.trimToEmpty(author.attributeValue("psn_id"));
    if (!NumberUtils.isDigits(psnIdStr)) {
      psnIdStr = "";
    }
    insCount = insCount == null ? 0 : insCount;
    Element pubMember = pubMembers.addElement("pub_member");
    pubMember.addAttribute("seq_no", String.valueOf(seqNo));
    pubMember.addAttribute("import_member_name", authName);
    pubMember.addAttribute("member_psn_name", authName);
    pubMember.addAttribute("member_psn_id", psnIdStr);
    pubMember.addAttribute("email", StringUtils.trimToEmpty(author.attributeValue("email")));
    pubMember.addAttribute("issearchorg", StringUtils.trimToEmpty(author.attributeValue("issearchorg")));
    if (StringUtils.isNotBlank(psnIdStr)) {
      pubMember.addAttribute("ins_id", context.getCurrentInsId().toString());
    } else {
      pubMember.addAttribute("ins_id", StringUtils.trimToEmpty(author.attributeValue("ins_id")));
    }
    pubMember.addAttribute("matched_ins_id", StringUtils.trimToEmpty(author.attributeValue("matched_ins_id")));
    pubMember.addAttribute("ins_name", StringUtils.trimToEmpty(author.attributeValue("ins_name")));
    pubMember.addAttribute("author_pos", StringUtils.trimToEmpty(author.attributeValue("author_pos")));
  }
}
