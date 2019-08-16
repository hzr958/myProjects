package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.connector.util.BatchJobUtil;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;

/**
 * 在center-batch那边更新人员solr信息
 * 
 * @author Administrator
 *
 */
public class AsRefreshPsnInfoInSolrImpl implements PubHandlerAssemblyService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "batchJobsContextFactory")
  private BatchJobsFactory batchJobsFactory;
  @Resource
  private BatchJobsService batchJobsService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      BatchJobs job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.REFRESH_PSN_SOLR_INFO,
          BatchJobUtil.getPsnSolrRefreshContext(pub.psnId.toString(), "refreshSolrInfo"), BatchWeightEnum.A.toString());
      batchJobsService.saveJob(job);
    } catch (Exception e) {
      logger.error("保存更新人员solr信息任务记录出错， psnId = " + pub.psnId, e);
    }
    return null;
  }

}
