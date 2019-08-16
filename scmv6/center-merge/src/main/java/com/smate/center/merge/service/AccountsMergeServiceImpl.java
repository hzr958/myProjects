package com.smate.center.merge.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.merge.dao.person.SysMergeUserHistoryDao;
import com.smate.center.merge.dao.task.AccountsMergeDao;
import com.smate.center.merge.model.sns.task.AccountsMerge;

/**
 * 帐号合并 服务实现
 * 
 * @author tsz
 *
 */
@Service("accountsMergeService")
@Transactional(rollbackFor = Exception.class)
public class AccountsMergeServiceImpl implements AccountsMergeService {
  @Autowired
  private AccountsMergeDao accountsMergeDao;
  @Autowired
  private SysMergeUserHistoryDao sysMergeUserHistoryDao;

  @Override
  public List<AccountsMerge> getNeedMergeData() throws Exception {
    return accountsMergeDao.getNeedMergeData();
  }

  @Override
  public void saveAccountsMerge(AccountsMerge accountsMerge) throws Exception {
    accountsMergeDao.save(accountsMerge);
  }

  @Override
  public void updateAccountsMerge(AccountsMerge accountsMerge) throws Exception {
    accountsMergeDao.update(accountsMerge);
    // 人员合并，也同步更新人员合并状态记录的结果
    Integer mergeUserHisStatus = 1;
    Integer status = Integer.valueOf(String.valueOf(accountsMerge.getStatus()));
    if (status == 99) {
      mergeUserHisStatus = 2;
    } else {
      mergeUserHisStatus = 3;
    }
    sysMergeUserHistoryDao.changePsnMergeStatus(accountsMerge.getDelPsnId(), mergeUserHisStatus);
  }
}
