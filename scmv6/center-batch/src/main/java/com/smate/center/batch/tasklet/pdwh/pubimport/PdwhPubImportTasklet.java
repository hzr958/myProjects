package com.smate.center.batch.tasklet.pdwh.pubimport;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubXmlToHandle;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.center.batch.service.pdwh.pubimport.DbcacheBfetchService;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;
import com.smate.center.batch.service.solr.task.SolrIndexDifService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;

public class PdwhPubImportTasklet extends BaseTasklet {
  @Autowired
  private DbcacheBfetchService dbcacheBfetchService;
  @Autowired
  private PdwhPublicationService pdwhPublicationService;
  @Autowired
  private SolrIndexDifService solrIndexDifService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    Long id = Long.parseLong(withData);
    PdwhPubXmlToHandle tmpPubXml = dbcacheBfetchService.getXmlFileById(id);

    if (tmpPubXml == null || StringUtils.isEmpty(tmpPubXml.getTmpXml())) {
      return DataVerificationStatus.NULL;
    }

    if (tmpPubXml.getStatus() == 3) {// 获取的状态已经被标记为出错
      return DataVerificationStatus.FALSE;
    }

    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    String pubIdStr = String.valueOf(jobContentMap.get("msg_id"));
    Long id = Long.parseLong(pubIdStr);
    PdwhPubXmlToHandle tmpPubXml = dbcacheBfetchService.getXmlFileById(id);
    Assert.notNull(tmpPubXml, "目标PdwhPubXmlToHandle文件不能为空");
    Assert.notNull(tmpPubXml.getPsnId(), "当前操作人信息tmpPubXml--getPsnId不能为空!");
    PdwhPublication pdwhPub = new PdwhPublication();
    Map<String, Object> pdwhPubInfoMap = this.pdwhPublicationService.praseXmlData(tmpPubXml.getTmpXml());
    pdwhPub = this.pdwhPublicationService.constructPdwhPub(pdwhPubInfoMap);
    // 查重
    Long dupPubId = this.dbcacheBfetchService.getDupPub(pdwhPub);
    // 添加原始xml文本
    try {
      pdwhPub.setXmlString(tmpPubXml.getTmpXml());
      if (tmpPubXml.getInsId() != null) {
        pdwhPubInfoMap.put("insId", tmpPubXml.getInsId());
      }
      pdwhPubInfoMap.put("operatePsnId", tmpPubXml.getPsnId());
      if (dupPubId == null) {
        // 保存新成果
        this.dbcacheBfetchService.saveNewPubInfo(pdwhPub, pdwhPubInfoMap);
      } else {
        // 更新重复成果
        this.dbcacheBfetchService.updatePubInfo(pdwhPub, pdwhPubInfoMap, dupPubId);
      }
      // 处理成功
      this.dbcacheBfetchService.pdwhPubSaveSuccess(id);
      // 处理成功，事务提交后才创建solr索引，防止回滚后solr数据不一致
      try {
        int patentType = pdwhPub.getPubType();
        // 判断是否为专利
        if (patentType == 5) {
          solrIndexDifService.indexPatent(pdwhPub);
        } else {
          solrIndexDifService.indexPublication(pdwhPub);
        }
      } catch (Exception e) {
        logger.error("PdwhPublication索引创建出错,pubId:" + pdwhPub.getPubId(), e);
      }
    } catch (Exception e) {
      // 处理失败
      this.dbcacheBfetchService.pdwhPubSaveError(tmpPubXml);
      logger.error("PdwhPubImportTask基准库成果导入错误，tmpXmlId = " + pubIdStr, e);
      throw new BatchTaskException(e);
    }
  }

}
