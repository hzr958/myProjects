package com.smate.sie.center.task.pdwh.task;

import java.util.List;

import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.v8pub.dao.pdwh.PdwhMemberInsNameDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubMemberDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhMemberInsNamePO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubMemberPO;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.pdwh.utils.ImportCommonAuthorMergeUtils;
import com.smate.sie.center.task.pdwh.utils.XmlUtil;

/**
 * @author yamingd 仅在导入过程使用
 */
public class SplitImportAuthorNameTask implements IPubXmlTask {

  /**
   * 
   */
  private String name = "split_import_author_name";
  @Autowired
  private PdwhPubMemberDAO pdwhPubMemberDAO;
  @Autowired
  private PdwhMemberInsNameDAO pdwhMemberInsNameDAO;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    // 当前PUBID不为空，说明是合并成果，合并成果保留原有pub_member不动
    return true;
  }

  @SuppressWarnings({"rawtypes"})
  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
    String authorNames = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names");
    authorNames = XmlUtil.formateAuthorNames(authorNames);
    if (authorNames.endsWith(";")) {
      authorNames = authorNames.substring(0, authorNames.length() - 1);
    }
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names", authorNames);
    // author_names节点的值
    List<String> authorList = ImportCommonAuthorMergeUtils.parseNameList(xmlDocument);

    // 删除并创建新的pub_members节点
    xmlDocument.removeNode(PubXmlConstants.PUB_MEMBERS_XPATH);
    Element pubMembers = xmlDocument.createElement(PubXmlConstants.PUB_MEMBERS_XPATH);

    // 直接从pdwh作者相关表取数据回来构建member节点
    List<PdwhPubMemberPO> members = pdwhPubMemberDAO.findByPubId(context.getPdwhId());
    for (PdwhPubMemberPO pdwhPubMemberPO : members) {
      this.buildPubMemberData(pdwhPubMemberPO, pubMembers);
    }


    xmlDocument.removeNode(PubXmlConstants.PUB_AUTHORS_XPATH);
    String authorNamesSpec = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "authors_names_spec");
    authorNamesSpec = XmlUtil.formateAuthorNames(authorNamesSpec);
    if (!"".equals(authorNames)) {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original_author_names", authorNames);
    } else {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original_author_names", authorNamesSpec);
    }

    return true;

  }

  private void buildPubMemberData(PdwhPubMemberPO pdwhPubMemberPO, Element pubMembers) throws SysServiceException {
    Element pubMember = pubMembers.addElement("pub_member");
    pubMember.addAttribute("seq_no", pdwhPubMemberPO.getSeqNo().toString());
    pubMember.addAttribute("member_psn_name", pdwhPubMemberPO.getName());
    pubMember.addAttribute("member_psn_id",
        pdwhPubMemberPO.getPsnId() == null ? "" : pdwhPubMemberPO.getPsnId().toString());
    if (pdwhPubMemberPO != null) {
      PdwhMemberInsNamePO memberIns = pdwhMemberInsNameDAO.findMemberByMemberId(pdwhPubMemberPO.getId());
      if (memberIns != null) {
        pubMember.addAttribute("ins_id", memberIns.getInsId() == null ? "" : memberIns.getInsId().toString());
        pubMember.addAttribute("ins_name", memberIns.getDept()); // 取pdwh的dept的值回来存到sie的ins_name中
      } else {
        pubMember.addAttribute("ins_id", "");
        pubMember.addAttribute("ins_name", ""); // 取pdwh的dept的值回来存到sie的ins_name中

      }
      pubMember.addAttribute("email", pdwhPubMemberPO.getEmail());
      pubMember.addAttribute("author_pos", pdwhPubMemberPO.getCommunicable().toString());
      pubMember.addAttribute("first_author", pdwhPubMemberPO.getFirstAuthor().toString());
    }
  }

}
