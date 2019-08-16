package com.smate.center.merge.quartz.task;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.merge.model.sns.task.AccountsMerge;
import com.smate.center.merge.service.AccountsMergeService;
import com.smate.center.merge.service.task.MergeService;
import com.smate.center.merge.service.task.email.PsnMergeEmailNoticeService;

/**
 * 帐号合并定时任务.
 * 
 * @author tsz
 *
 */
public class AccountsMergeTask {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private AccountsMergeService accountsMergeService;
  @Autowired
  private PsnMergeEmailNoticeService psnMergeEmailNoticeService;
  // 合并服务链
  private List<MergeService> mergeDealList;

  /**
   * 合并任务.
   */
  public void run() {
    try {
      List<AccountsMerge> accountsMergeList = accountsMergeService.getNeedMergeData();
      if (CollectionUtils.isNotEmpty(accountsMergeList)) {
        for (AccountsMerge accountsMerge : accountsMergeList) {
          try {
            // 按服务循环处理
            for (MergeService mergeTaskService : mergeDealList) {
              mergeTaskService.runMerge(accountsMerge.getSavePsnId(), accountsMerge.getDelPsnId());
            }
            // 成功
            accountsMerge.setStatus(1L);
            accountsMergeService.updateAccountsMerge(accountsMerge);
            // 邮件通知成功
            psnMergeEmailNoticeService.sendEmailToPsn(accountsMerge.getSavePsnId());
          } catch (Exception e) {
            logger.error("帐号合并 savePsnId=" + accountsMerge.getSavePsnId() + ",delPsnId=" + accountsMerge.getDelPsnId(),
                e);
            // 记录错误
            accountsMerge.setErrMsg(getErrorMsg(e));
            accountsMerge.setStatus(99L);
            accountsMergeService.updateAccountsMerge(accountsMerge);
            // 发送邮件通知失败 管理用户
            psnMergeEmailNoticeService.sendEmailToAdmin(accountsMerge.getSavePsnId());
          }
        }
      }
    } catch (Exception e) {
      logger.error("人员合并出错", e);
    }
  }

  public List<MergeService> getMergeDealList() {
    return mergeDealList;
  }

  public void setMergeDealList(List<MergeService> mergeDealList) {
    this.mergeDealList = mergeDealList;
  }

  /**
   * 记录全部错误记录
   * 
   * @param e
   * @return
   */
  private String getErrorMsg(Exception e) {
    Writer result = new StringWriter();
    PrintWriter printWriter = new PrintWriter(result);
    e.printStackTrace(printWriter);
    return result.toString();
  }
}
