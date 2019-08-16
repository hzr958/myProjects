package com.smate.center.batch.chain.pub;

import com.smate.center.batch.dao.sns.pub.PubSimpleHashDao;
import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.service.pub.PubAssignLogService;
import com.smate.center.batch.service.pub.PubSimpleService;
import com.smate.center.batch.service.pub.rcmd.PublicationConfirmService;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PubAutoConfirmDupTask implements IPubXmlTask {

  @Autowired
  private PubSimpleHashDao pubSimpleHashDao;

  @Autowired
  private PubSimpleService pubSimpleService;

  @Autowired
  private PublicationConfirmService publicationConfirmService;
  @Autowired
  private PubAssignLogService pubAssignLogService;

  private final String name = "pub_autoconfirm_duplicate";

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    if (context.getCurrentAction() != null && (context.getCurrentAction() == XmlOperationEnum.Enter
        || context.getCurrentAction() == XmlOperationEnum.Import || context.getCurrentAction() == XmlOperationEnum.Edit
        || context.getCurrentAction() == XmlOperationEnum.ImportPdwh)
        && (context.getGroupId() == null || context.getGroupId() == 1L || context.getGroupId() == 0L)) {// 只有导入个人成果才能自动确认被推荐成果
      return true;
    }
    return false;
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    Long pubId = xmlDocument.getNewPubId();
    // 扩展了当前操作者的节点
    // 当前用户
    Long psnId = context.getCurrentUserId();

    if (psnId == null || psnId == 0L) {
      psnId = Long.parseLong(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_EXPAND_XPATH, "psnId"));
    }

    // 查重规则 ：年份为空， title+type ；年份不为空，title+type+year
    String zhTitle = PubHashUtils.cleanTitle(xmlDocument.getZhTitle());
    String enTitle = PubHashUtils.cleanTitle(xmlDocument.getEnTitle());
    Integer pubType = xmlDocument.getPubTypeId();
    String pubYear = xmlDocument.getPublishYear();
    Long zhTitleHash = HashUtils.getStrHashCode(zhTitle);
    Long enTitleHash = HashUtils.getStrHashCode(enTitle);
    String[] titleValues = new String[] {zhTitle, enTitle};
    Long titleHashValue = PubHashUtils.fingerPrint(titleValues) == null ? 0L : PubHashUtils.fingerPrint(titleValues);
    List<Long> dupPubConfirmIds =
        pubAssignLogService.getDupPubConfirm(zhTitleHash, enTitleHash, titleHashValue, pubType, pubYear, psnId);

    if (CollectionUtils.isNotEmpty(dupPubConfirmIds)) {
      pubAssignLogService.autoConfirmPubSimple(dupPubConfirmIds, psnId);
    }

    return true;
  }

}
