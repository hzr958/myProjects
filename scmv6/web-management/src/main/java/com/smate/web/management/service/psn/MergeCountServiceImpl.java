package com.smate.web.management.service.psn;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.management.dao.institution.bpo.PersonMergeDao;
import com.smate.web.management.dao.psn.AccountsMergeDao;
import com.smate.web.management.dao.psn.SysMergeUserHistoryDao;
import com.smate.web.management.dao.psn.SysMergeUserInfoDao;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.institution.bpo.PersonMerge;
import com.smate.web.management.model.psn.AccountsMerge;
import com.smate.web.management.model.psn.SysMergeUserHis;
import com.smate.web.management.model.psn.SysMergeUserInfo;
import com.smate.web.management.service.analysis.PersonManager;

/**
 * 合并账户设置的业务逻辑实现类.
 * 
 * @author mjg
 * 
 */
@Service("mergeCountService")
@Transactional(rollbackFor = Exception.class)
public class MergeCountServiceImpl implements MergeCountService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  // 合并状态0-已初始化；1-正在合并中；2-合并失败；3-已合并成功 .
  public static final Integer MERGE_STATUS_INIT = 0;
  public static final Integer MERGE_STATUS_MERGING = 1;
  public static final Integer MERGE_STATUS_FAILED = 2;
  public static final Integer MERGE_STATUS_SUCCEED = 3;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private SysMergeUserHistoryDao sysMergeUserHistoryDao;
  @Autowired
  private SysMergeUserInfoDao sysMergeUserInfoDao;
  @Autowired
  private PersonMergeDao personMergeTaskDao;
  @Autowired
  private AccountsMergeDao accountsMergeDao;

  @Override
  public Integer getMergeStatus(Long mergePsnId, Long deletePsnId) throws Exception {
    return sysMergeUserHistoryDao.getMergeStatus(mergePsnId, deletePsnId);
  }

  /**
   * 进行帐号合并操作，并返回被合并帐号的人员信息.
   * 
   * @param targetPsnId
   * @return
   * @throws ServiceException
   */
  @Override
  public String mergePsnCount(Long MergePsnId, List<Long> deletePsnIds, String mergeCount) throws Exception {
    Person savePsn = personManager.getPersonByRecommend(MergePsnId);
    for (Long deletePsnId : deletePsnIds) {
      Person delPsn = personManager.getPersonByRecommend(deletePsnId);
      this.saveMergeUserOperat(mergeCount, MergePsnId, delPsn, MERGE_USER_STATUS_DEL);
      // 保存需合并人员记录到person
      this.saveMergePsn(MergePsnId, deletePsnId);
      // 在sns库保存合并账号记录
      AccountsMerge accountsMerge = new AccountsMerge();
      accountsMerge.setCreateDate(new Date());
      accountsMerge.setSavePsnId(MergePsnId);
      accountsMerge.setDelPsnId(deletePsnId);
      accountsMerge.setStatus(0L);
      accountsMerge.setStatusExt(1L);
      accountsMergeDao.save(accountsMerge);
    }
    return savePsn.getEmail();
  }

  /**
   * 保存被合并用户记录.
   * 
   * @param loginName
   * @param savePsnId
   * @param delPerson
   * @param status
   */
  private void saveMergeUserOperat(String loginName, Long savePsnId, Person delPerson, Integer status) {
    // 保存被合并用户状态记录.
    SysMergeUserHis mergedUser = this.buildMergeUserHis(loginName, delPerson.getEmail(), savePsnId, status,
        MERGE_STATUS_MERGING, delPerson.getPersonId());
    sysMergeUserHistoryDao.saveMergeUserHis(mergedUser);
    // 保存被合并删除的人员信息记录.
    SysMergeUserInfo mergeUserInfo = this.buildMergeUserInfo(loginName, savePsnId, delPerson);
    sysMergeUserInfoDao.saveMergeUserInfo(mergeUserInfo);
  }

  /**
   * 构建scholar库合并用户邮件通知记录表对象.
   * 
   * @param loginName
   * @param email
   * @param psnId
   * @param status
   * @param mergeStatus
   * @param delPsnId
   * @return
   */
  private SysMergeUserHis buildMergeUserHis(String loginName, String email, Long psnId, Integer status,
      Integer mergeStatus, Long delPsnId) {
    SysMergeUserHis mergeUserHis = new SysMergeUserHis();
    mergeUserHis.setDealTime(new Date());
    mergeUserHis.setLoginName(loginName);
    mergeUserHis.setEmail(email);
    mergeUserHis.setMailStatus(0);
    mergeUserHis.setMergeStatus(mergeStatus);
    mergeUserHis.setPsnId(psnId);
    mergeUserHis.setDelPsnId(delPsnId);
    mergeUserHis.setStatus(status);
    mergeUserHis.setOperatePsnId(SecurityUtils.getCurrentUserId());
    return mergeUserHis;
  }

  /**
   * 构建scholar库被合并删除的人员信息表对象.
   * 
   * @param loginName
   * @param savePsnId
   * @param person
   * @return
   */
  private SysMergeUserInfo buildMergeUserInfo(String loginName, Long savePsnId, Person person) {
    SysMergeUserInfo mergeUserInfo = new SysMergeUserInfo();
    mergeUserInfo.setPsnId(savePsnId);
    mergeUserInfo.setDelPsnId(person.getPersonId());
    mergeUserInfo.setDelDesPsnId(person.getPersonDes3Id());
    mergeUserInfo.setInsName(person.getInsName());
    mergeUserInfo.setLoginCount(loginName);
    mergeUserInfo.setPsnAvatars(person.getAvatars());
    mergeUserInfo.setPsnEmail(person.getEmail());
    mergeUserInfo.setPsnEnName(person.getEname());
    mergeUserInfo.setPsnFirstName(person.getFirstName());
    mergeUserInfo.setPsnLastName(person.getLastName());
    mergeUserInfo.setPsnTitolo(person.getTitolo());
    mergeUserInfo.setPsnZhName(person.getName());
    mergeUserInfo.setPsnViewName(person.getViewName());
    mergeUserInfo.setPsnViewTitolo(person.getViewTitolo());
    return mergeUserInfo;
  }

  /**
   * 保存需合并人员记录.
   * 
   * @param savePsnId
   * @param delPsnId
   * @throws ServiceException
   */
  public void saveMergePsn(Long savePsnId, Long delPsnId) throws Exception {
    try {
      PersonMerge mergePsn = new PersonMerge();
      mergePsn.setSavePsnId(savePsnId);
      mergePsn.setDelPsnId(delPsnId);
      mergePsn.setCreateDate(new Date());
      mergePsn.setStatus(0L);
      mergePsn.setStatusExt(0L);// 是否需要跑动态合并任务.
      personMergeTaskDao.savePersonMerge(mergePsn);
    } catch (DaoException e) {
      logger.error("保存合并人员的psnId的记录出错,savePsnId=" + savePsnId + ",delPsnId" + delPsnId, e);
      throw new Exception(e);
    }
  }

}
