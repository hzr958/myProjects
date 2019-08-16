package com.smate.center.merge.service.task.thirdparty;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.merge.dao.account.AccountDao;
import com.smate.center.merge.dao.person.PersonDao;
import com.smate.center.merge.model.cas.account.Account;
import com.smate.center.merge.model.sns.person.Person;
import com.smate.center.merge.service.task.MergeBaseService;

/**
 * 处理手机号关联
 * 
 * @author yhx
 *
 * @date 2019年1月7日
 */
@Transactional(rollbackFor = Exception.class)
public class MergeMobileServiceImpl extends MergeBaseService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private AccountDao accountDao;
  @Autowired
  private PersonDao personDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    Account account = accountDao.get(delPsnId);
    if (account != null) {
      return true;
    }
    return false;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    Account delAccount = accountDao.get(delPsnId);
    Account saveAccount = accountDao.get(savePsnId);
    String mobile = delAccount.getMobileNumber();
    // 保留人没有手机号，删除人有手机号的时候把删除人的手机号保留给保留人
    if (StringUtils.isNotBlank(mobile) && StringUtils.isBlank(saveAccount.getMobileNumber())) {
      delAccount.setMobileNumber("");
      accountDao.save(delAccount);
      saveAccount.setMobileNumber(mobile);
      accountDao.save(saveAccount);
      // person表同步tel字段
      Person person = personDao.get(savePsnId);
      person.setTel(mobile);
      personDao.save(person);
    }
    return true;
  }
}
