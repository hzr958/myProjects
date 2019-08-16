package com.smate.sie.center.task.pdwh.task;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.smate.center.task.single.enums.pub.PublicationRecordMode;
import com.smate.center.task.single.enums.pub.XmlOperationEnum;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.sie.center.task.model.SieConstRefDb;
import com.smate.sie.center.task.pdwh.service.SieConstRefDbService;

/**
 * 导入预处理程序.
 * 
 * @author liqinghua
 * 
 */
public class PublicationMetaTask implements IPubXmlTask {

  /**
   * 
   */
  private final String name = "pub_meta";

  @Autowired
  private SieConstRefDbService sieConstRefDbService;

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
    return true;
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    Assert.notNull(context.getPubTypeId());
    Assert.notNull(context.getCurrentUserId());
    Assert.notNull(context.getCurrentAction());
    Assert.notNull(context.getCurrentNodeId());

    // 类别信息
    xmlDocument.setPubTypeId(context.getPubTypeId());

    Element meta = (Element) xmlDocument.getPubMeta();
    if (meta == null) {
      meta = xmlDocument.createElement(PubXmlConstants.PUB_META_XPATH);
    }

    Date now = context.getNow();
    String userId = String.valueOf(context.getCurrentUserId());
    String nowStr = ServiceUtil.formateZhDateFull(now);
    // 最后更新信息
    meta.addAttribute("last_update_psn_id", userId);
    meta.addAttribute("last_update_date", nowStr);
    if (context.getCurrentPubId() != null && context.getCurrentPubId() != 0) {
      meta.addAttribute("pub_id", context.getCurrentPubId().toString());
    }
    // 导入
    if (XmlOperationEnum.Import.equals(context.getCurrentAction())
        || XmlOperationEnum.OfflineImport.equals(context.getCurrentAction())
        || XmlOperationEnum.ImportPdwh.equals(context.getCurrentAction())
        || XmlOperationEnum.SyncFromOld.equals(context.getCurrentAction())
        || XmlOperationEnum.SyncFromOldPrj.equals(context.getCurrentAction())) {

      Long sourceDbCode =
          IrisNumberUtils.createLong(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "source"));
      // if ("".equals(sourceDbCode)) {
      // sourceDbCode =
      // xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH,
      // "citation_index");
      // }
      if (NumberUtils.isNotZero(sourceDbCode) || sourceDbCode != null) {
        // SieConstRefDb sourceDb = null;
        SieConstRefDb sourceDb = sieConstRefDbService.getConstRefDbById(sourceDbCode);
        // 在线文件导入
        if (sourceDb == null) {
          meta.addAttribute("source_db_id", "-1");
          meta.addAttribute("record_from", "4");
        } else {
          // 离线导入
          if (XmlOperationEnum.OfflineImport.equals(context.getCurrentAction())) {
            meta.addAttribute("record_from", String.valueOf(PublicationRecordMode.OFFLINE_IMPORT));
            // 基准库导入
          } else if (XmlOperationEnum.ImportPdwh.equals(context.getCurrentAction())) {
            meta.addAttribute("record_from", String.valueOf(4));
          } else {
            meta.addAttribute("record_from", String.valueOf(PublicationRecordMode.ONLINE_IMPORT_FROM_DATABASE));
          }

          meta.addAttribute("source_db_id", String.valueOf(sourceDb.getId()));
          meta.addAttribute("source_db_code", sourceDbCode.toString());
          meta.addAttribute("zh_source_db_name", sourceDb.getZhAbbrName());
          meta.addAttribute("en_source_db_name", sourceDb.getEnAbbrName());
        }

        xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "source_db_code");
      }

      if (context.getCurrentInsId() != null) {
        // ROL导入
        meta.addAttribute("record_ins_id", String.valueOf(context.getCurrentInsId()));
      }
      meta.addAttribute("record_psn_id", userId);
      meta.addAttribute("create_psn_id", userId);
      meta.addAttribute("create_date", nowStr);
      meta.addAttribute("record_node_id", String.valueOf(context.getCurrentNodeId()));
      // 手工录入；
      // 还有一种情况是在负载较高时，导入或者手工录入的任务还没有运行，又被修改，此时需要相应的判断，保留正确的record_from的值
    } else if (XmlOperationEnum.Enter.equals(context.getCurrentAction())) {

      meta.addAttribute("record_psn_id", userId);
      meta.addAttribute("create_psn_id", userId);
      meta.addAttribute("create_date", nowStr);
      if (context.getCurrentInsId() != null) {
        // ROL录入
        meta.addAttribute("record_ins_id", String.valueOf(context.getCurrentInsId()));
      }

      String recordFrom = meta.attributeValue("record_from");

      if (StringUtils.isBlank(recordFrom)) {
        meta.addAttribute("record_from", String.valueOf(PublicationRecordMode.ENTER));
      }

      meta.addAttribute("record_node_id", String.valueOf(context.getCurrentNodeId()));

    } else if (XmlOperationEnum.PushFromIns.equals(context.getCurrentAction())) {
      // 从单位库拉回数据.
      meta.addAttribute("record_psn_id", userId);
      meta.addAttribute("create_psn_id", userId);
      meta.addAttribute("create_date", nowStr);
      meta.addAttribute("record_from", String.valueOf(PublicationRecordMode.SYNC_FROM_INS));
      meta.addAttribute("from_rol_node_id", meta.attributeValue("record_node_id"));
      meta.addAttribute("record_node_id", String.valueOf(context.getCurrentNodeId()));
    } else if (XmlOperationEnum.SyncFromSNS.equals(context.getCurrentAction())) {
      meta.addAttribute("from_sns_node_id", meta.attributeValue("record_node_id"));
      meta.addAttribute("record_node_id", String.valueOf(context.getCurrentNodeId()));
    }
    // 设置sourceDBID
    context.setSourceDbId(xmlDocument.getSourceDbId());

    return true;
  }
}
