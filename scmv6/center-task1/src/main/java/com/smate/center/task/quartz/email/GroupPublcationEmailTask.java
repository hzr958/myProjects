package com.smate.center.task.quartz.email;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.model.group.EmailGroupPubPsn;
import com.smate.center.task.model.grp.GrpPubs;
import com.smate.center.task.service.group.GroupPublicationAddEmailService;

/**
 * 添加成果到群组后，发送邮件的任务,群组成员为群组添加文件或成果时，要发邮件通知，每个群组一天只有第一次通知，后面就不用通知了
 * 
 * @author zjh
 *
 */
public class GroupPublcationEmailTask extends TaskAbstract {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupPublicationAddEmailService groupPublicationAddEmailService;

  public GroupPublcationEmailTask() {}

  public GroupPublcationEmailTask(String beanName) {
    super(beanName);
  }

  public void run() {

    try {
      logger.info("执行群组添加成果发送邮件任务");
      if (!super.isAllowExecution()) {
        return;
      }
      // 群组成员为群组添加文件或成果时，要发邮件通知，每个群组一天只有第一次通知，后面就不用通知了
      grpAddpubSendEmail();

    } catch (Exception e) {
      logger.error("GroupPublcationEmailTask--新增群组成果任务处理任务出错", e);
    }
  }

  /**
   * 群组添加成果 ，发送邮件
   * 
   * @throws Exception
   */
  private void grpAddpubSendEmail() throws Exception {
    EmailGroupPubPsn eg = null;
    // 今天上传成果的群组id
    List<Long> grpIdList = groupPublicationAddEmailService.getUploadPubGrpId();
    if (grpIdList == null || grpIdList.size() < 1) {
      return;
    }
    for (Long grpId : grpIdList) {
      GrpPubs grpPubs = groupPublicationAddEmailService.getTodayUplaodGrpPubsByGrpId(grpId);
      if (grpPubs == null) {
        continue;
      }
      eg = new EmailGroupPubPsn();
      eg.setCreateDate(new Date());
      eg.setGroupId(grpPubs.getGrpId());
      eg.setPsnId(grpPubs.getCreatePsnId());
      eg.setPubId(grpPubs.getPubId());
      eg.setStatus("0");

      try {
        Map<String, Object> mailData = new HashMap<String, Object>();
        groupPublicationAddEmailService.sendGrpAddpubSendEmail(mailData, grpPubs);
        eg.setStatus("1");
      } catch (Exception e) {
        logger.error("发送群组邮件信息出错,pubId=" + grpPubs.getPubId() + ",groupId=" + grpPubs.getPubId(), e);
        eg.setStatus("99");
      }
      groupPublicationAddEmailService.save(eg);
    }
  }
}
