package com.smate.web.psn.service.setting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.dao.merge.AccountsMergeDao;
import com.smate.web.psn.dao.psn.SysMergeUserHistoryDao;
import com.smate.web.psn.dao.psn.SysMergeUserInfoDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.merge.AccountsMerge;
import com.smate.web.psn.model.psninfo.SysMergeUserHis;
import com.smate.web.psn.model.psninfo.SysMergeUserInfo;

/**
 * 人员合并记录操作业务逻辑实现类.
 * 
 * @author mjg
 * 
 */
@Service("sysMergeUserHistoryService")
@Transactional(rollbackFor = Exception.class)
public class SysMergeUserHistoryServiceImpl implements SysMergeUserHistoryService {

  @Autowired
  private SysMergeUserHistoryDao sysMergeUserHistoryDao;
  @Autowired
  private SysMergeUserInfoDao sysMergeUserInfoDao;
  @Autowired
  private AccountsMergeDao accountsMergeDao;

  /**
   * 根据人员ID获取其正在合并的帐号记录.
   * 
   * @param psnId
   * @param status
   * @return
   */
  @Override
  public List<SysMergeUserInfo> getMergingListByPsn(Long psnId, Integer status) {
    // 获取其正在合并的帐号记录.
    List<Long> userHisList = sysMergeUserHistoryDao.getMergeUserList(psnId, status);
    if (CollectionUtils.isNotEmpty(userHisList)) {
      return sysMergeUserInfoDao.getMergeUserInfoList(psnId, userHisList);
    }
    return null;
  }

  @Override
  public boolean getCurrentPersonMergeStatus(Long delPsnId) throws ServiceException {
    try {
      List<SysMergeUserHis> list = sysMergeUserHistoryDao.getMergeByDelPsnId(delPsnId);
      if (list != null && list.size() > 0) {
        return true;
      }
      return false;
    } catch (DaoException e) {
      e.printStackTrace();
      throw new ServiceException(e);
    }

  }

  /**
   * 保存人员合并结果记录.
   * 
   * @param mergeUserHis
   */
  @Override
  public void saveMergeUserHis(SysMergeUserHis mergeUserHis) {
    sysMergeUserHistoryDao.saveMergeUserHis(mergeUserHis);
  }

  @Override
  public Map<String, String> getMergeResult(String ids) throws ServiceException {
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    Map<String, String> map = new HashMap<String, String>();
    StringBuilder error = new StringBuilder();
    StringBuilder scusess = new StringBuilder();
    String[] idsArray = ids.split(",");
    try {
      findMergingLoginname(currentPsnId, error, scusess, idsArray);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(e);
    }
    map.put("scusess", scusess.toString());
    map.put("error", error.toString());
    return map;
  }

  private void findMergingLoginname(Long currentPsnId, StringBuilder error, StringBuilder scusess, String[] idsArray)
      throws DaoException {
    for (int i = 0; i < idsArray.length; i++) {
      if (StringUtils.isNotBlank(idsArray[i])) {
        Long delPsnId = Long.valueOf(ServiceUtil.decodeFromDes3(idsArray[i]));
        List<SysMergeUserHis> list = sysMergeUserHistoryDao.getMergeBy(currentPsnId, delPsnId);
        Integer checkAccountsMergeStatus = checkAccountsMergeStatus(currentPsnId, delPsnId);
        if (list != null && list.size() > 0) {
          SysMergeUserHis sys = list.get(0);
          // 判断合并状态
          if (sys.getMergeStatus() == 3 && checkAccountsMergeStatus == 1) {
            scusess.append(sys.getLoginName());
          } else if (sys.getMergeStatus() == 2 || checkAccountsMergeStatus == 99) {
            error.append(sys.getLoginName());
          }
        }
      }
    }
  }

  /**
   * 检查新表数据，是否合并成功 ;;; 0 ==需要合并， 1==成功 ， 99 == 失败
   * 
   * @param savePsnId
   * @param delPsnId
   * @return
   */
  private Integer checkAccountsMergeStatus(Long savePsnId, Long delPsnId) {
    List<AccountsMerge> list = accountsMergeDao.getAccountsMerge(savePsnId, delPsnId);
    // 为空，没有数据 返回为成功
    if (list == null || list.size() == 0) {
      return 1;
    }
    // 多条数据，说明合并多次，有成功有失败的
    for (AccountsMerge accountsMerge : list) {
      if (accountsMerge.getStatus() == 1) {
        return 1;
      } else if (accountsMerge.getStatus() == 0) {
        return 0;
      }
    }
    return 99;
  }

  @Override
  public Integer getMergeStatus(Long psnId, Long delPsnId) {
    return sysMergeUserHistoryDao.getMergeStatus(psnId, delPsnId);
  }

  /**
   * 保存人员合并相关信息记录.
   */
  @Override
  public void saveMergeUserInfo(SysMergeUserInfo mergeUserInfo) {
    sysMergeUserInfoDao.saveMergeUserInfo(mergeUserInfo);
  }

}
