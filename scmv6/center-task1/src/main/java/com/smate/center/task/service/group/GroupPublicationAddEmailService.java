package com.smate.center.task.service.group;

import java.util.List;
import java.util.Map;

import com.smate.center.task.model.group.EmailGroupPubPsn;
import com.smate.center.task.model.grp.GrpPubs;

public interface GroupPublicationAddEmailService {

  public int updateAddGroupPubEmail() throws Exception;

  public List<Long> getAddGroupPub() throws Exception;



  public boolean isExists(Long pubId, Long groupId) throws Exception;

  public void save(EmailGroupPubPsn emailGroupPubPsn) throws Exception;

  public List<Long> getUploadPubGrpId() throws Exception;

  public GrpPubs getTodayUplaodGrpPubsByGrpId(Long grpId) throws Exception;

  /**
   * 发送群组添加成果邮件
   * 
   * @param mailData
   * 
   * @param grpPubs
   * @throws Exception
   */
  public void sendGrpAddpubSendEmail(Map<String, Object> mailData, GrpPubs grpPubs) throws Exception;
}
