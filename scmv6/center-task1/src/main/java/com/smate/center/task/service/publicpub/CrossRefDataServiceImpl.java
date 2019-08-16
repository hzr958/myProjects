package com.smate.center.task.service.publicpub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.DbCacheCfetchDao;
import com.smate.center.task.dao.publicpub.CrossrefYearCountDao;
import com.smate.center.task.model.common.CrossrefYearCount;
import com.smate.center.task.model.pdwh.pub.DbCacheCfetch;

@Service("crossRefDataService")
@Transactional(rollbackFor = Exception.class)
public class CrossRefDataServiceImpl implements CrossRefDataService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private CrossrefYearCountDao crossrefYearCountDao;
  @Autowired
  private DbCacheCfetchDao dbCacheCfetchDao;

  public void saveJson(Integer pubYear, String fileName, String json) {
    DbCacheCfetch cfetch = new DbCacheCfetch(json, fileName, pubYear);
    dbCacheCfetchDao.save(cfetch);
  }

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

  @Override
  public List<CrossrefYearCount> getImportCrossrefData() {
    return crossrefYearCountDao.getImportCrossrefData();
  }

}
