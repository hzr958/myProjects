package com.smate.sie.center.task.pdwh.service;

import java.util.List;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.model.PubMeetingName;

public interface PubMeetingNameService {

  // 模糊查询
  public List<PubMeetingName> findAllPubMeetingName(String searchKey) throws SysServiceException;

  // 保存
  public void savePubMeetingName(String meetingName, Long pdwhId) throws SysServiceException;

}
