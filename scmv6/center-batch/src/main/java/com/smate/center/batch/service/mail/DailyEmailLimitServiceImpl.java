package com.smate.center.batch.service.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.mail.emailsrv.DailyEmailLimitTempDao;
import com.smate.center.batch.dao.mail.emailsrv.DailyLimitSendRecoredsDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.emailsrv.DailyLimitSendRecoreds;
import com.smate.center.batch.model.mail.emailsrv.DailyLimitSendRecoredsPk;
import com.smate.core.base.utils.cache.CacheService;

/**
 * 每日限制邮件服务
 * 
 * @author zk
 *
 */
@Service("dailyEmailLimitService")
@Transactional(rollbackFor = Exception.class)
public class DailyEmailLimitServiceImpl implements DailyEmailLimitService {

  private String CACHE_NAME = "daily_email_limit_temp_name";
  private String CACHE_KEY = "tempname1234567890";

  @Autowired
  private DailyLimitSendRecoredsDao dailyLimitSendRecoredsDao;
  @Autowired
  private DailyEmailLimitTempDao dailyEmailLimitTempDao;
  @Autowired
  private CacheService cacheService;

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public List<String> getDailyEmailLimitTempName() throws ServiceException {

    // 取缓存
    cacheService.remove(this.CACHE_NAME, this.CACHE_KEY);
    ArrayList<String> tempNameList = (ArrayList<String>) cacheService.get(this.CACHE_NAME, this.CACHE_KEY);
    if (CollectionUtils.isEmpty(tempNameList)) {
      // 不存在，则从数据库取，30分钟失效期
      tempNameList = dailyEmailLimitTempDao.getDailyEmailLimitTempName();
      if (CollectionUtils.isNotEmpty(tempNameList)) {
        ArrayList tnList = new ArrayList();
        tnList.addAll(tempNameList);
        cacheService.put(this.CACHE_NAME, CacheService.EXP_MIN_30, this.CACHE_KEY, tnList);
      }
    }
    return tempNameList;
  }

  @Override
  public void saveSendRecored(String email, String tempName) throws ServiceException {
    if (StringUtils.isNotBlank(email) && StringUtils.isNotBlank(tempName)) {
      DailyLimitSendRecoreds record = new DailyLimitSendRecoreds();
      DailyLimitSendRecoredsPk pk = new DailyLimitSendRecoredsPk();
      pk.setEmail(email);
      pk.setTempName(tempName);
      record.setPk(pk);
      record.setSendDate(new Date());
      dailyLimitSendRecoredsDao.saveSendRecored(record);
    }
  }

  @Override
  public boolean isSendToday(String email, String tempName) throws ServiceException {
    return dailyLimitSendRecoredsDao.isSendToday(email, tempName);
  }
}
