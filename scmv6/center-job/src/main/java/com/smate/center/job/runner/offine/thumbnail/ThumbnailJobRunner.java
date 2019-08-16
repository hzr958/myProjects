package com.smate.center.job.runner.offine.thumbnail;

import com.smate.center.job.business.log.model.TmpTaskInfoRecord;
import com.smate.center.job.business.pub.service.PdwhPubFullTextService;
import com.smate.center.job.framework.exception.PrecheckException;
import com.smate.center.job.framework.runner.BaseOfflineJobRunner;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 基准库全文pdf转图片离线任务
 *
 * @author LIJUN
 * @date 2018年5月23日
 */
@Component("offlinethumbnailJob")
public class ThumbnailJobRunner extends BaseOfflineJobRunner {

  @Autowired
  private PdwhPubFullTextService pdwhPubFullTextService;


  @Override
  protected void process(Object entity, JobDataMap jobDataMap) throws JobExecutionException {
    TmpTaskInfoRecord taskInfoRecord = (TmpTaskInfoRecord) entity;
    Long pubId = taskInfoRecord.getHandleId();
    try {
      // 1.先删除可能存在的旧文件
      pdwhPubFullTextService.delPdwhPubFulltextByPubId(pubId);
      pdwhPubFullTextService.ConvertPubFulltextPdfToimage(pubId);
    } catch (Exception e) {
      pdwhPubFullTextService.updateTaskStatus(pubId, 2, "pdf全文转换图片处理失败" + e.getMessage());
      // 打印异常日志
      logger.error("基准库pdf转图片离线任务处理时出现异常！该记录id={}", pubId);
      // 不再进行处理，抛出异常，终止任务执行。任务表记录，这里不抛出
      // throw new JobExecutionException(e);
    }
  }

  @Override
  public void preCheck(JobDataMap jobDataMap) throws PrecheckException {

  }

  @Override
  protected Class<?> getPersistentClass() {
    return TmpTaskInfoRecord.class;
  }

}
