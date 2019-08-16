package com.smate.center.task.quartz;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.service.pdwh.quartz.NsfcFullTextMatchService;

/**
 * nsfc pub与sns&pdwh匹配获取全文
 * 
 * @author LJ
 *
 *         2017年9月13日
 */
public class NsfcFullTextMatchTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 200; // 每次处理的个数
  @Autowired
  NsfcFullTextMatchService nsfcFullTextMatchService;

  public NsfcFullTextMatchTask() {
    super();
  }

  public NsfcFullTextMatchTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========NsfcFullTextMatchTask已关闭==========");
      return;
    }
    /**
     * 本任务执行前需要初始化数据到临时任务表（获取提供的NSFCpubid保存到临时任务表）,sql eg:
     * <p>
     * insert into scmpdwh.TMP_TASK_INFO_RECORD (job_id,job_type,handle_id)
     * <p>
     * select scmpdwh.SEQ_TMP_TASK_INFO_RECORD.nextval,6,t.pub_id
     * <p>
     * from 表名视情况 t ;
     * <p>
     */
    List<Long> pubIds = null;
    try {
      pubIds = nsfcFullTextMatchService.batchGetPdwhPubIds(SIZE);
    } catch (Exception e1) {
      logger.error("NsfcFullTextMatchTask批量获取预处理数据出错！", e1);
    }

    if (CollectionUtils.isEmpty(pubIds)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("关闭NsfcFullTextMatchTask出错！", e);
      }
    }
    for (Long pubId : pubIds) {

      try {
        // 先匹配sns,sns匹配不上在匹配pdwh
        boolean matchedSns = nsfcFullTextMatchService.matchSnsPubFulltext(pubId);
        if (matchedSns == false) {
          nsfcFullTextMatchService.matchPdwhPubFulltext(pubId);
        }
      } catch (Exception e2) {
        logger.error("NsfcFullTextMatchTask出错！pubid：" + pubId, e2);
        nsfcFullTextMatchService.updateTaskStatus(pubId, 2, "NsfcFullTextMatchTask出错！" + getErrorMsg(e2));
      }
    }
  }

  private String getErrorMsg(Exception e) {
    Writer result = new StringWriter();
    PrintWriter printWriter = new PrintWriter(result);
    e.printStackTrace(printWriter);
    String ErrorMsg = result.toString().length() > 450 ? result.toString().substring(0, 500) : result.toString();
    return ErrorMsg;
  }
}
