package com.smate.center.batch.chain.pub.rol;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.util.pub.ImportAuthorMergeUtils;
import com.smate.center.batch.util.pub.ImportCnkiAuthorMergeUtils;
import com.smate.center.batch.util.pub.ImportCommonAuthorMergeUtils;
import com.smate.center.batch.util.pub.ImportSpsAuthorMergeUtils;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * @author yamingd 仅在导入过程使用
 */
public class SplitImportAuthorNameTask implements IPubXmlTask {

  /**
   * 
   */
  private String name = "split_import_author_name";

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {

    // 当前PUBID不为空，说明是合并成果，合并成果保留原有pub_member不动
    return context.isImport() && context.getCurrentPubId() == null;
  }

  @SuppressWarnings({"rawtypes"})
  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    // 合并导入作者
    // 是否是isi导入.
    if (context.isIsiImport() || context.isPubMedImport()) {
      xmlDocument = ImportAuthorMergeUtils.megergeImportAuthor(xmlDocument);
      // 是否是CNKI导入
    } else if (context.isCnkiImport()) {
      xmlDocument = ImportCnkiAuthorMergeUtils.megergeImportAuthor(xmlDocument);
      // 是否是scopus导入
    } else if (context.isScopusImport()) {
      xmlDocument = ImportSpsAuthorMergeUtils.megergeImportAuthor(xmlDocument);
    } else {
      xmlDocument = ImportCommonAuthorMergeUtils.mergeImportAuthor(xmlDocument);
    }

    List authorList = xmlDocument.getPubAuthorList();
    if (authorList == null || authorList.size() == 0) {
      return true;
    }
    // 删除并创建新的pub_members节点
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
      this.buildPubMemberData(author, pubMembers, authName, k);
      k++;
    }
    String authorNames = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names");
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original_author_names", authorNames);

    return true;

  }

  private void buildPubMemberData(Element author, Element pubMembers, String authName, int seqNo)
      throws ServiceException {
    Integer insCount = IrisNumberUtils.createInteger(StringUtils.trimToEmpty(author.attributeValue("ins_count")));
    insCount = insCount == null ? 0 : insCount;
    Element pubMember = pubMembers.addElement("pub_member");
    pubMember.addAttribute("seq_no", String.valueOf(seqNo));
    pubMember.addAttribute("import_member_name", authName);
    pubMember.addAttribute("member_psn_name", authName);
    pubMember.addAttribute("member_psn_id", StringUtils.trimToEmpty(author.attributeValue("psn_id")));
    pubMember.addAttribute("email", StringUtils.trimToEmpty(author.attributeValue("email")));
    pubMember.addAttribute("issearchorg", StringUtils.trimToEmpty(author.attributeValue("issearchorg")));
    pubMember.addAttribute("ins_id", StringUtils.trimToEmpty(author.attributeValue("ins_id")));
    pubMember.addAttribute("matched_ins_id", StringUtils.trimToEmpty(author.attributeValue("matched_ins_id")));
    pubMember.addAttribute("ins_name", StringUtils.trimToEmpty(author.attributeValue("ins_name")));
    pubMember.addAttribute("author_pos", StringUtils.trimToEmpty(author.attributeValue("author_pos")));
    // 在线导入，默认第一个作者为第一作者
    if (seqNo == 1) {
      pubMember.addAttribute("first_author", "1");
    }
  }

}
