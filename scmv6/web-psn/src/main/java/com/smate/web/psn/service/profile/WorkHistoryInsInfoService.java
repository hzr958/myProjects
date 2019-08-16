package com.smate.web.psn.service.profile;

import java.util.Map;

import com.smate.core.base.psn.model.WorkHistory;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.workhistory.PsnWorkHistoryInsInfo;

public interface WorkHistoryInsInfoService {

  /**
   * 更新工作经历单位信息<只许是首要单位或最新工作经历调用>
   */
  void updateWorkHistoryInsInfo(WorkHistory wk, Integer isPrimaryFlag) throws PsnException;

  /**
   * 获取单位、部门的中英文
   * 
   * @param insName @param searchName @return @throws
   */
  Map<String, String> getUnitAndDepartmentofIns(String insName, String searchName) throws PsnException;

  /**
   * 处理职称 @param wk @param psnInsInfo @throws
   */
  void handlePosition(WorkHistory wk, PsnWorkHistoryInsInfo psnInsInfo) throws PsnException;

  /**
   * 当用户删除工作经历或取消首要单位时，检查并清除单位信息
   * 
   * @throws
   */
  void checkWorkHistoryInsInfo(WorkHistory wk) throws PsnException;

}
