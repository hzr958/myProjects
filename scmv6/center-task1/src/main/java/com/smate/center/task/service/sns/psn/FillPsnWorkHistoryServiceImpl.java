package com.smate.center.task.service.sns.psn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.model.security.Person;

@Service("fillPsnWorkHistoryService")
@Transactional(rollbackFor = Exception.class)
public class FillPsnWorkHistoryServiceImpl implements FillPsnWorkHistoryService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;

  @Override
  public List<Person> getHandlePsnList(Integer size) {
    return personProfileDao.getHandlePsnList(size);
  }

  @Override
  public void saveWorkHistory(WorkHistory psnWork) {
    try {
      workHistoryDao.save(psnWork);
    } catch (Exception e) {
      logger.error("FillPsnWorkHistoryTask 补充人员工作经历出错");
    }

  }

}
