package com.smate.web.v8pub.service.handler.assembly.jobsave;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.connector.dao.exception.CreateBatchJobException;
import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.connector.util.BatchJobUtil;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;

@Transactional(rollbackFor = Exception.class)
public class ASBatchJobsSaveImpl implements PubHandlerAssemblyService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "batchJobsContextFactory")
  private BatchJobsFactory batchJobsFactory;
  @Resource
  private BatchJobsService batchJobsService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    /*
     * if (StringUtils.isBlank(pub.organization)) { throw new
     * PubHandlerAssemblyException("成果地址为空，不需要向下处理"); }
     */

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    if (StringUtils.isBlank(pub.organization) || pub.members == null || pub.members.size() == 0) {
      return null;
    }
    // 基准库成果新增更新任务结束，插入记录跑成果地址和作者匹配任务
    if (StringUtils.isNotBlank(pub.organization)) {
      try {
        BatchJobs job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.MATCH_PDWHPUB_ADRR_AUTHOR,
            BatchJobUtil.getPdwhAddrAuthMatchContext(pub.pubId + "", pub.pubHandlerName), BatchWeightEnum.C.toString());
        batchJobsService.saveJob(job);
      } catch (CreateBatchJobException e) {
        logger.error("将需要匹配单位的数据放置v_batch_jobs表出错----------");
      }
    }


    return null;
  }

}
