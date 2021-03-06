package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.JournalAreaClassifyDao;
import com.smate.center.task.exception.ServiceException;


@Service("journalAreaClassifyService")
@Transactional(rollbackFor = Exception.class)
public class JournalAreaClassifyServiceImpl implements JournalAreaClassifyService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private JournalAreaClassifyDao journalAreaClassifyDao;

  @Override
  public List<String> getJournalAreaClassify(String issn) throws ServiceException {
    if (StringUtils.isBlank(issn))
      return null;
    return journalAreaClassifyDao.getJournalAreaClassify(issn);
  }

}
