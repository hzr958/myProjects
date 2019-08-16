package com.smate.sie.center.task.pdwh.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.DaoException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.dao.PubMeetingOrganizerDao;
import com.smate.sie.center.task.model.PubMeetingOrganizer;

@Service("pubMeetingOrganizerService")
@Transactional(rollbackFor = Exception.class)
public class PubMeetingOrganizerServiceImpl implements PubMeetingOrganizerService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubMeetingOrganizerDao pubMeetingOrganizerDao;

  @Override
  public List<PubMeetingOrganizer> findAllPubMeetingOrganizer(String searchKey) throws SysServiceException {
    try {
      return pubMeetingOrganizerDao.getPubMeetingOrganizer(searchKey);
    } catch (DaoException e) {
      logger.error("findAllPubMeetingOrganizer获取会议组织者searchKey=" + searchKey, e);
      throw new SysServiceException("findAllPubMeetingOrganizer获取会议组织者searchKey=" + searchKey);
    }
  }

  @Override
  public void savePubMeetingOrganizer(String organizer, Long pdwhId) throws DaoException {
    String query = organizer.trim().toLowerCase();
    try {
      // 判断是否已经存在该提示信息
      if (pubMeetingOrganizerDao.isExistQuery(query)) {
        return;
      }
      PubMeetingOrganizer meetingOrganizer = new PubMeetingOrganizer();
      meetingOrganizer.setName(organizer);
      meetingOrganizer.setCreateAt(new Date());
      meetingOrganizer.setQuery(query);
      pubMeetingOrganizerDao.save(meetingOrganizer);
    } catch (Exception e) {
      logger.error("pdwhId:" + pdwhId + ",savePubMeetingOrganizer保存会议组织者出错", e);
      throw new DaoException("pdwhId:" + pdwhId + ",savePubMeetingOrganizer保存会议组织者出错");
    }

  }

}
