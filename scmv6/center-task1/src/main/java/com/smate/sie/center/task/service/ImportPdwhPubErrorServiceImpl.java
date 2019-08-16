package com.smate.sie.center.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.sie.center.task.dao.ImportPdwhPubErrorDao;
import com.smate.sie.center.task.model.ImportPdwhPubError;

/**
 * 
 * @author jszhou
 *
 */
@Service("importPdwhPubErrorService")
@Transactional(rollbackFor = Exception.class)
public class ImportPdwhPubErrorServiceImpl implements ImportPdwhPubErrorService {

  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ImportPdwhPubErrorDao importPdwhPubErrorDao;

  @Override
  public void saveObject(ImportPdwhPubError error) {
    try {
      importPdwhPubErrorDao.save(error);

    } catch (Exception e) {
      logger.error("保存错误日志异常 " + e);
    }

  }

}
