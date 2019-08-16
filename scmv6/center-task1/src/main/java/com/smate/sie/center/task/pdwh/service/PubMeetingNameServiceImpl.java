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
import com.smate.sie.center.task.dao.PubMeetingNameDao;
import com.smate.sie.center.task.model.PubMeetingName;

@Service("pubMeetingNameService")
@Transactional(rollbackFor = Exception.class)
public class PubMeetingNameServiceImpl implements PubMeetingNameService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubMeetingNameDao pubMeetingNameDao;

  @Override
  public List<PubMeetingName> findAllPubMeetingName(String searchKey) throws SysServiceException {
    try {
      return pubMeetingNameDao.getAcConfName(searchKey);
    } catch (DaoException e) {
      logger.error("findAllPubMeetingName获取会议名称searchKey=" + searchKey, e);
      throw new SysServiceException("findAllPubMeetingName获取会议名称searchKey=" + searchKey);
    }
  }

  @Override
  public void savePubMeetingName(String meetingName, Long pdwhId) throws SysServiceException {
    try {
      // 判断是否已经存在该提示信息
      String query = meetingName.trim().toLowerCase();
      if (pubMeetingNameDao.isExistQuery(query)) {
        return;
      }
      PubMeetingName mettingName = new PubMeetingName();
      mettingName.setName(meetingName);
      mettingName.setCreateAt(new Date());
      mettingName.setQuery(query);
      pubMeetingNameDao.save(mettingName);
    } catch (Exception e) {
      logger.error("pdwhId:" + pdwhId + ",savePubMeetingName保存会议名称出错", e);
      throw new SysServiceException("pdwhId:" + pdwhId + ",savePubMeetingName保存会议名称出错");
    }
  }

}
