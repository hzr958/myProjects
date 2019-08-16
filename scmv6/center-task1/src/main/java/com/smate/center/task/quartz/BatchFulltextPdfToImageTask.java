package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.service.pdwh.quartz.BatchFulltextPdfToImageService;

/**
 * 生成基准库全文图片
 * 
 * @author JunLi （任务已改造，由原来插入记录到batch处理改为直接在task处理） 2017年11月1日 废弃该任务，移动到center-job,且需要生成原图
 * 
 */
@Deprecated
public class BatchFulltextPdfToImageTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 200; // 每次处理的个数
  @Autowired
  private BatchFulltextPdfToImageService batchFulltextPdfToImageService;

  public BatchFulltextPdfToImageTask() {
    super();
  }

  public BatchFulltextPdfToImageTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    /**
     * 本任务执行前需要初始化数据到临时任务表（获取有全文的pubid保存到临时任务表）,sql eg:
     * 
     * insert into v3pdwh.TMP_TASK_INFO_RECORD (job_id,job_type,handle_id) <br>
     * select v3pdwh.SEQ_TMP_TASK_INFO_RECORD.nextval,3,t.pub_id<br>
     * FROM v3pdwh.pdwh_fulltext_file t <br>
     * WHERE t.pub_id NOT IN( select pub_id from v3pdwh.pdwh_fulltext_img )<br>
     * and t.pub_id not in<br>
     * (select handle_id from v3pdwh.TMP_TASK_INFO_RECORD wherejob_type=3);
     * 
     */
    if (!super.isAllowExecution()) {
      return;
    }

    List<Long> pdwhPubIds = null;
    try {
      pdwhPubIds = batchFulltextPdfToImageService.batchGetPdwhPubIds(SIZE);
    } catch (Exception e1) {
      logger.error("BatchFulltextPdfToImageTask批量获取预处理数据出错！", e1);
    }

    if (CollectionUtils.isEmpty(pdwhPubIds)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("关闭BatchFulltextPdfToImageTask出错！", e);
      }
    }

    else {
      try {
        // 先删除旧图片
        for (Long pdwhPubId : pdwhPubIds) {
          boolean deleteOldImage = batchFulltextPdfToImageService.deleteOldImage(pdwhPubId);
          if (deleteOldImage) {
            batchFulltextPdfToImageService.batchConvertPubFulltextPdfToimage(pdwhPubId);
          }
        }
        /*
         * // 批量生成 batchFulltextPdfToImageService.batchConvertPubFulltextPdfToimage(pdwhPubIds);
         */
      } catch (Exception e) {
        logger.error("pdwh全文转图片出错！pubids：" + pdwhPubIds.toString(), e);
      }

    }

  }

}
