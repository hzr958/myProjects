package com.smate.center.merge.service;

import com.smate.center.merge.model.sns.task.AccountsMerge;

import java.util.List;

/**
 * 帐号合并服务
 * 
 * @author tsz
 *
 */
public interface AccountsMergeService {
  /**
   * 获取需要合并的数据
   * 
   * @return
   * @throws Exception
   */
  public List<AccountsMerge> getNeedMergeData() throws Exception;

  /**
   * 保存对象
   * 
   * @param accountsMerge
   * @return
   * @throws Exception
   */
  public void saveAccountsMerge(AccountsMerge accountsMerge) throws Exception;

  public void updateAccountsMerge(AccountsMerge accountsMerge) throws Exception;
}
