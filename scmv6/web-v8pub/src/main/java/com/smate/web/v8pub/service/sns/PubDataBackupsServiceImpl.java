package com.smate.web.v8pub.service.sns;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.web.v8pub.dao.sns.PubDataBackupsDao;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.backups.PubDataBackups;

/**
 * 后台任务
 * 
 * @author YJ
 *
 *         2018年11月19日
 */
@Service(value = "pubDataBackupsService")
@Transactional(rollbackFor = Exception.class)
public class PubDataBackupsServiceImpl implements PubDataBackupsService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubDataBackupsDao pubDataBackupsDao;

  @Override
  public PubDataBackups get(Long pubId) throws ServiceException {
    try {
      return pubDataBackupsDao.get(pubId);
    } catch (Exception e) {
      logger.error("获取成果备份表数据对象出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PubDataBackups pubDataBackups) throws ServiceException {
    // TODO Auto-generated method stub
  }

  @Override
  public void update(PubDataBackups pubDataBackups) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void saveOrUpdate(PubDataBackups pubDataBackups) throws ServiceException {
    try {
      pubDataBackups.setStatus(0);
      pubDataBackups.setGmtModified(new Date());
      pubDataBackupsDao.saveOrUpdate(pubDataBackups);
    } catch (Exception e) {
      logger.error("保存备份数据任务表出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(PubDataBackups pubDataBackups) throws ServiceException {
    // TODO Auto-generated method stub

  }

}
