package com.smate.web.psn.service.psnwork;

import java.util.List;

import com.smate.core.base.psn.model.WorkHistory;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.workhistory.PsnWorkHistoryInsInfo;


/**
 * 个人工作经历单位服务接口
 * 
 * @author Administrator
 *
 */
public interface PsnWorkHistoryInsInfoService {

  /**
   * 保存<科研之友用>
   */
  public void savePsnWorkHistoryInsInfo(PsnWorkHistoryInsInfo psnWorkHistoryInsInfo) throws PsnException;

  /**
   * 通过psnId获取人员工作经历单位信息<科研之友用>
   */
  PsnWorkHistoryInsInfo getPsnWorkHistoryInsInfo(Long psnId) throws PsnException;

  /**
   * 根据人员id 获取工作经历
   * 
   * @param psnId
   * @return
   * @throws PsnException
   */
  public List<WorkHistory> getPsnWorkHistory(Long psnId) throws PsnException;

  /**
   * 获得首要工作单位 @param psnId @return @throws
   */
  public WorkHistory getFirstWork(Long psnId) throws PsnException;

  /**
   * 根据工作单位ID获取工作经历 记录
   * 
   * @param workId
   * @return
   * @throws PsnException
   */
  public WorkHistory findWorkHistoryById(Long workId) throws PsnException;

  /**
   * 删除人员首要单位信息，psn_workhistory_ins_info表
   * 
   * @param psnId
   * @param insId
   * @param insName
   * @throws PsnException
   */
  public void deletePsnWorkHistoryInsInfo(Long psnId, Long insId, String insName) throws PsnException;

  /**
   * 获取个人工作经历
   * 
   * @param psnId
   * @param workId
   * @return
   * @throws PsnException
   */
  WorkHistory getWorkHistoryByWorkId(Long psnId, Long workId) throws PsnException;
}
