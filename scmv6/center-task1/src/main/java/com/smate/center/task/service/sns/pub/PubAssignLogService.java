package com.smate.center.task.service.sns.pub;

import java.util.List;

import com.smate.center.task.model.sns.psn.SendmailPsnLog;

public interface PubAssignLogService {

  List<SendmailPsnLog> getNeedSendMailPsnIds(Integer size);

  Integer sendConfirmEmailToPsn(SendmailPsnLog sendmailPsnLog) throws Exception;

  void UpdateMailSendStatus(Long psnId, Integer result);

  List<Long> getReSendMailPsnIds(Integer size);

  void saveWeChatMessagePsn(Long psnId);

  Long getUserOpenId(Long psnId, String token);

  boolean getDataByOpenId(Long openId);

}
