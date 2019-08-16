package com.smate.center.batch.chain.pub;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.service.pub.PubPdwhScmRolService;
import com.smate.center.batch.service.pub.mq.SnsGetPdwhIdProducer;
import com.smate.center.batch.util.pub.PdwhSupportDbUtils;

/**
 * 获取成果基准库ID任务.
 * 
 * @author liqinghua
 * 
 */
public class SnsPubGetPdwhIdTask implements IPubXmlTask {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private String name = "SnsPubGetPdwhIdTask";
  @Resource(name = "snsGetPdwhIdProducer")
  private SnsGetPdwhIdProducer snsGetPdwhIdProducer;
  @Autowired
  private PubPdwhScmRolService pubPdwhScmRolService;

  @Override
  public String getName() {

    return name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    // 必须是在线导入
    return XmlOperationEnum.Import.equals(context.getCurrentAction())
        && PdwhSupportDbUtils.isPdwhSupportDb(xmlDocument.getSourceDbId());
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    if (xmlDocument.getSourceDbId() == null) {
      return true;
    }
    // SCM-15147
    // 修改关系记录表，停用老系统queryPfetchDbIdTaskTriggers，sendPfetchDbIdTaskTriggers任务获取snspubId对应pdwhPubId
    // 改由task任务处理，保存对应关系到pub_pdwh_sns_relation表，老表还会保存一份记录。
    try {
      /*
       * Long titleHash = IrisNumberUtils
       * .createLong(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "title_hash")); Long
       * unitHash = IrisNumberUtils
       * .createLong(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "unit_hash")); Long
       * sourceIdHash = IrisNumberUtils
       * .createLong(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "source_id_hash"));
       * Long patentHash = IrisNumberUtils
       * .createLong(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "patent_hash"));
       */

      if (xmlDocument.getSourceDbId() != null && xmlDocument.getSourceDbId() > 0) {
        // 直接保存到临时表由task处理
        pubPdwhScmRolService.saveTmpPdwhPub(context.getCurrentPubId(), 1);
        /*
         * snsGetPdwhIdProducer.sendGetPdwhId(context.getCurrentPubId(),
         * xmlDocument.getSourceDbId().intValue(), titleHash, unitHash, sourceIdHash, patentHash,
         * context.getCurrentUserId());
         */
      }

    } catch (Exception e) {
      logger.error("获取成果基准库ID任务，保存到TMP_PUB_PDWH临时表出错", e);
    }
    return true;
  }
}
