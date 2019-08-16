package com.smate.center.batch.tasklet.pdwh.pubimport;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubXmlToHandle;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.center.batch.service.pdwh.pub.PdwhPubCitedTimesService;
import com.smate.center.batch.service.pdwh.pubimport.DbcacheBfetchService;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Map;

public class updatePubCiteTimesTasklet extends BaseTasklet {

  @Autowired
  private DbcacheBfetchService dbcacheBfetchService;
  @Autowired
  private PdwhPublicationService pdwhPublicationService;
  @Autowired
  private PdwhPubCitedTimesService pdwhPubCitedTimesService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    Long tmpId = Long.parseLong(String.valueOf(jobContentMap.get("msg_id")));// 引用成果
    String citedSourceId = String.valueOf(jobContentMap.get("sourceId"));// 引用该成果的成果的sourceid
    Long pdwhPubId = Long.parseLong(String.valueOf(jobContentMap.get("pdwhPubId")));// 被引用的成果的id
    PdwhPubXmlToHandle tmpPubXml = dbcacheBfetchService.getXmlFileById(tmpId);
    Assert.notNull(tmpPubXml, "目标PdwhPubXmlToHandle文件不能为空");
    Assert.notNull(tmpPubXml.getPsnId(), "当前操作人信息tmpPubXml--getPsnId不能为空!");
    try {
      Map<String, Object> pdwhPubInfoMap = this.pdwhPublicationService.praseXmlData(tmpPubXml.getTmpXml());
      pdwhPubInfoMap.put("pdwhPubId", pdwhPubId);
      PdwhPublication pdwhPub = this.pdwhPublicationService.constructPdwhPub(pdwhPubInfoMap);
      // 查重
      Long dupPubId = this.dbcacheBfetchService.getDupPub(pdwhPub);
      // 添加原始xml文本
      pdwhPub.setXmlString(tmpPubXml.getTmpXml());
      pdwhPubInfoMap.put("operatePsnId", tmpPubXml.getPsnId());
      if (dupPubId == null) {
        // 保存被更新引用的成果
        this.dbcacheBfetchService.saveNewPdwhPubInfo(pdwhPub, pdwhPubInfoMap);
      } else {
        // 有就不保存，直接建立联系
        this.pdwhPubCitedTimesService.savePdwhPubCitedRelation(pdwhPubId, dupPubId);
      }
      // 处理成功
      this.dbcacheBfetchService.pdwhPubSaveSuccess(tmpId);
    } catch (Exception e) {
      this.dbcacheBfetchService.pdwhPubSaveError(tmpPubXml);
      logger.error("updatePubCiteTimesTask更新引用次数建立联系失败，pdwhPubId = " + pdwhPubId, e);
      throw new BatchTaskException(e);
    }
  }

}
