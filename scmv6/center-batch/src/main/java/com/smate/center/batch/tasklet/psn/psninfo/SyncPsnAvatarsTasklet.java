package com.smate.center.batch.tasklet.psn.psninfo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.mail.InboxService;
import com.smate.center.batch.service.mail.MailBoxService;
import com.smate.center.batch.service.prj.ProjectCommentService;
import com.smate.center.batch.service.psn.PersonProfileService;
import com.smate.center.batch.service.pub.FriendService;
import com.smate.center.batch.service.pub.PublicationCommentService;
import com.smate.center.batch.service.user.UserSettingsService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.model.security.Person;

public class SyncPsnAvatarsTasklet extends BaseTasklet {
  @Autowired
  private PersonProfileService personProfileService;
  @Autowired
  private InboxService inboxService;
  @Autowired
  private MailBoxService mailBoxService;
  @Autowired
  private UserSettingsService userSettingsService;
  @Autowired
  private ProjectCommentService projectCommentService;
  @Autowired
  private PublicationCommentService publicationCommentService;
  @Autowired
  private FriendService friendService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    String psnIdStr = String.valueOf(jobContentMap.get("msg_id"));
    Long psnId = Long.parseLong(psnIdStr);
    Person person = personProfileService.getpsnInfo(psnId);
    // 各自更新相应的冗余字段
    friendService.updatePersonInfo(person);
    // 收件箱人员数据冗余

    // inboxService.syncPersonInfo(person);
    // 发件箱人员数据冗余
    // mailBoxService.syncPersonInfo(person);
    // 关注人员数据同步
    userSettingsService.syncPersonInfo(person);
    // 同步人员信息到群组冗余字段
    // updateGroupMember(person);//不要
    // 同步人员信息到项目评论
    // projectCommentService.updatePsnInf(person);
    // 同步人员信息到成果，文献评论
    // publicationCommentService.syncPubCommentPsn(person);
    // 更新评价人信息
    // friendService.syncPersonFappraisal(person);
    // 更新赞的人员信息
    // updateAwardSyncPerson(message);
  }

}
