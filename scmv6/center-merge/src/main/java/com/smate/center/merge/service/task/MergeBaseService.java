package com.smate.center.merge.service.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.merge.dao.task.AccountsMergeDataDao;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 合并处理 基础类 所有的合并处理 都继承这个类
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public abstract class MergeBaseService implements MergeService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private AccountsMergeDataDao accountsMergeDataDao;

  public final Long DEAL_TYPE_MERGE = 1L; // 处理类型 合并数据
  public final Long DEAL_TYPE_DEL = 0L;// 处理类型 删除数据

  /**
   * 开始方法
   * 
   * @param savePsnId
   * @param delPsnId
   * @throws Exception
   */
  @Override
  public void runMerge(Long savePsnId, Long delPsnId) throws Exception {
    if (this.checkRun(savePsnId, delPsnId)) {
      this.dealMerge(savePsnId, delPsnId);
    }
  }

  /**
   * 保存合并备份数据
   * 
   * @return
   * @throws Exception
   */
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW,
      rollbackForClassName = {"java.lang.Exception"})
  public AccountsMergeData saveAccountsMergeData(Long savePsnId, Long delPsnId, String desc, Long dealType,
      Object sourceObj) throws Exception {

    try {
      String jsonData = JacksonUtils.jsonObjectSerializer(sourceObj);
      AccountsMergeData accountsMergeData =
          new AccountsMergeData(savePsnId, delPsnId, desc, new Date(), dealType, jsonData, 0L);
      accountsMergeDataDao.save(accountsMergeData);
      return accountsMergeData;

    } catch (Exception e) {
      logger.error("帐号合并备份数据出现异常（savePsnId:" + savePsnId + " delPsnId:" + delPsnId + ",desc=" + desc + " ）：", e);
      throw new Exception();
    }

  }

  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW,
      rollbackForClassName = {"java.lang.Exception"})
  public void updateAccountsMergeDataStatus(AccountsMergeData accountsMergeData) throws Exception {

    try {
      accountsMergeData.setStatus(1L);

      accountsMergeDataDao.save(accountsMergeData);

    } catch (Exception e) {
      logger.error("帐号合并备份数据 更新备份数据状态 " + accountsMergeData.toString(), e);
      throw new Exception();
    }

  }

}
