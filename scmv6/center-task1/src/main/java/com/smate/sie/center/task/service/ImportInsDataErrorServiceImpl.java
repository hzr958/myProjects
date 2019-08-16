package com.smate.sie.center.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.sie.center.task.dao.ImportInsDataErrorDao;
import com.smate.sie.center.task.model.ImportInsDataError;

/**
 * 
 * @author hd
 *
 */
@Service("importInsDataErrorService")
@Transactional(rollbackFor = Exception.class)
public class ImportInsDataErrorServiceImpl implements ImportInsDataErrorService {

  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ImportInsDataErrorDao importInsDataErrorDao;

  @Override
  public void saveObject(ImportInsDataError error) {
    try {
      importInsDataErrorDao.save(error);

    } catch (Exception e) {
      logger.error("保存错误日志异常 " + e);
    }

  }

}
