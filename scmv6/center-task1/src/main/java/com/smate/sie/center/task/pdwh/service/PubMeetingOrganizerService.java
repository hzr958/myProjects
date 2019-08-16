package com.smate.sie.center.task.pdwh.service;

import java.util.List;

import com.smate.center.task.exception.DaoException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.model.PubMeetingOrganizer;

public interface PubMeetingOrganizerService {

  // 模糊查询
  public List<PubMeetingOrganizer> findAllPubMeetingOrganizer(String searchKey) throws SysServiceException;

  // 保存
  public void savePubMeetingOrganizer(String organizer, Long pdwhId) throws DaoException;

}
