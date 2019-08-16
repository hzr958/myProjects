package com.smate.web.psn.service.setting;

import java.util.List;
import java.util.Map;

import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.psninfo.SysMergeUserHis;
import com.smate.web.psn.model.psninfo.SysMergeUserInfo;

/**
 * 人员合并结果记录操作业务逻辑接口.
 * 
 * @author mjg
 * 
 */

public interface SysMergeUserHistoryService {

  // 合并状态0-已初始化；1-正在合并中；2-合并失败；3-已合并成功 .
  public static final Integer MERGE_STATUS_INIT = 0;
  public static final Integer MERGE_STATUS_MERGING = 1;
  public static final Integer MERGE_STATUS_FAILED = 2;
  public static final Integer MERGE_STATUS_SUCCEED = 3;

  /**
   * 根据人员ID获取其正在合并的帐号记录.
   * 
   * @param psnId
   * @param status
   * @return
   */
  List<SysMergeUserInfo> getMergingListByPsn(Long psnId, Integer status);

  /**
   * 判断当前帐号是否正在被合并中
   * 
   * @param delPsnId
   * @return
   * @throws ServiceException
   */
  public boolean getCurrentPersonMergeStatus(Long delPsnId) throws ServiceException;

  /**
   * 保存人员合并结果记录.
   * 
   * @param mergeUserHis
   */
  public void saveMergeUserHis(SysMergeUserHis mergeUserHis);

  /**
   * 监听正在合并中帐号的合并结果
   * 
   * @param ids
   * @return
   * @throws ServiceException
   */
  public Map<String, String> getMergeResult(String ids) throws ServiceException;

  /**
   * 获取合并状态
   * 
   * @param psnId
   * @param delPsnId
   * @return
   */
  Integer getMergeStatus(Long psnId, Long delPsnId);

  /**
   * 保存人员合并相关信息记录.
   */
  public void saveMergeUserInfo(SysMergeUserInfo mergeUserInfo);

}
