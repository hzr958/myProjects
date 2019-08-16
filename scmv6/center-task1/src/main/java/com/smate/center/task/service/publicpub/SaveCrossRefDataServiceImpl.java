package com.smate.center.task.service.publicpub;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.publicpub.CrossrefYearCountDao;
import com.smate.center.task.model.common.CrossrefYearCount;

@Service("saveCrossRefDataService")
@Transactional(rollbackFor = Exception.class)
public class SaveCrossRefDataServiceImpl implements SaveCrossRefDataService {
  @Autowired
  private CrossrefYearCountDao crossrefYearCountDao;

  @Override
  public void saveYearCount(CrossrefYearCount yearCount) {
    crossrefYearCountDao.save(yearCount);
  }

  @Override
  public List<CrossrefYearCount> getYearCount() {
    return crossrefYearCountDao.getYearCount();
  }

  @Override
  public void updateYearCountStatus(Long year) {
    crossrefYearCountDao.updateYearCountStatus(year);
  }

}
