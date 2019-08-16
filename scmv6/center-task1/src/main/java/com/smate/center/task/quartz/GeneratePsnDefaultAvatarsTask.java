package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.service.sns.psn.PsnDefaultAvatarService;

/**
 * 默认头像人员根据姓名重新生成头像任务
 * 
 * @author LJ
 *
 *         2017年9月8日
 */
public class GeneratePsnDefaultAvatarsTask extends TaskAbstract {
  private static final int BATCH_SIZE = 2000;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  PsnDefaultAvatarService psnDefaultAvatarService;

  public GeneratePsnDefaultAvatarsTask() {
    super();
  }

  public GeneratePsnDefaultAvatarsTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {

    /**
     * 本任务执行前需要初始化数据到临时任务表（获取默认头像人员psnId保存到临时任务表）,sql eg:
     * <p>
     * insert into scmpdwh.TMP_TASK_INFO_RECORD (job_id,job_type,handle_id)
     * <p>
     * select scmpdwh.SEQ_TMP_TASK_INFO_RECORD.nextval,5,t.psn_id
     * <p>
     * from scholar2.person t where t.avatars like '%logo_psndefault.png' or
     * <p>
     * t.avatars like '%head_nan_photo.jpg' or
     * <p>
     * t.avatars like '%head_nv_photo.jpg';
     * <p>
     * 
     */
    /**
     * SCM-15480任务执行前需要初始化数据到临时任务表（获取默认头像人员psnId保存到临时任务表）,sql eg:
     * <p>
     * insert into scmpdwh.TMP_TASK_INFO_RECORD (job_id,job_type,handle_id)
     * <p>
     * select scmpdwh.SEQ_TMP_TASK_INFO_RECORD.nextval,5,t.psn_id
     * <p>
     * from scholar2.person t where t.avatars like '%logo_psndefault.png' or
     * <p>
     * t.avatars like '%head_nan_photo.jpg' or
     * <p>
     * t.avatars like '%head_nv_photo.jpg' or
     * <p>
     * t.avatars like '?A=D';
     * <p>
     * 
     */
    if (!super.isAllowExecution()) {
      return;
    }

    List<Long> needTohandleList = null;
    try {
      needTohandleList = psnDefaultAvatarService.getNeedTohandleList(BATCH_SIZE);
    } catch (Exception e1) {
      logger.error("GeneratePsnDefaultAvatarsTask批量获取预处理数据出错！", e1);
    }

    if (CollectionUtils.isEmpty(needTohandleList)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("关闭GeneratePsnDefaultAvatarsTask出错！", e);
      }
    }
    for (Long psnId : needTohandleList) {
      try {
        psnDefaultAvatarService.startGenerateAvatars(psnId);
      } catch (Exception e) {
        logger.error("生成个人默认头像出错！psnId:" + psnId, e);
        psnDefaultAvatarService.updateTaskStatus(psnId, 2, "生成个人默认头像出错！");// 处理失败
      }
    }

  }
}
