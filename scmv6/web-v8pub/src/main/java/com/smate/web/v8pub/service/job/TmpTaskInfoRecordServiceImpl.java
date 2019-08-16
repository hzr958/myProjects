package com.smate.web.v8pub.service.job;

import com.smate.web.v8pub.dao.job.TmpTaskInfoRecordDao;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.job.TmpTaskInfoRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("tmpTaskInfoRecordService")
@Transactional(rollbackFor = Exception.class)
public class TmpTaskInfoRecordServiceImpl implements TmpTaskInfoRecordService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;


  @Override
  public TmpTaskInfoRecord getPsnRepeatPubRecord(Long psnId) throws ServiceException {
    try {
      return tmpTaskInfoRecordDao.getGrpRecord(psnId);
    } catch (Exception e) {
      logger.error("出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(TmpTaskInfoRecord tmpTaskInfoRecord) throws ServiceException {
    try {
      tmpTaskInfoRecordDao.saveOrUpdate(tmpTaskInfoRecord);
    } catch (Exception e) {
      logger.error("保存个人成果触发重复成果分组任务记录！", e);
      throw new ServiceException(e);
    }

  }
}
