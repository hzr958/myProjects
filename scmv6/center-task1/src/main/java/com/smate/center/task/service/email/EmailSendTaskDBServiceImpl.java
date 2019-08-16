package com.smate.center.task.service.email;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.email.ConstEmailIntervalDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.email.ConstEmailInterval;


/**
 * 邮件发送任务事务类,带DB,
 * 
 * @author zk
 */

@Service("emailSendTaskDbService")
@Transactional(rollbackFor = Exception.class)
public class EmailSendTaskDBServiceImpl implements EmailSendTaskDBService {

  @Autowired
  ConstEmailIntervalDao constEmailIntervalDao;

  /**
   * 获取模板发送时间
   */
  @Override
  public ConstEmailInterval getConstEmailInterval(Integer etempCode) throws ServiceException {
    return constEmailIntervalDao.get(etempCode);
  }

  /**
   * 保存模板发送时间
   */
  @Override
  public void saveConstEmailInterval(ConstEmailInterval emailInterval) throws ServiceException {
    constEmailIntervalDao.save(emailInterval);
  }

  /***
   * 自动处理模板下一次运行时间
   */
  @Override
  public void updateNextDate(Integer etempCode) throws ServiceException {
    ConstEmailInterval interval = getConstEmailInterval(etempCode);
    interval.setCurrDeal(interval.getNextDeal());
    interval.setNextDeal(EmailInfoBaseMethod.getNextExeTime(interval.getNextDeal(), interval.getDealInterval()));
    constEmailIntervalDao.save(interval);
  }

  /**
   * 判断tempCode当前是否可以执行邮件发送任务
   */

  @Override
  public Boolean canExecuteEmailTask(Integer etempCode) throws ServiceException {

    ConstEmailInterval emailInterval = getConstEmailInterval(etempCode);
    // 不存在，或下一次执行时间小于当前时间,状态位不为1,返回false
    if (emailInterval == null || emailInterval.getNextDeal().after(new Date()) || emailInterval.getStatus() != 1) {
      return false;
    }
    return true;
  }

}
