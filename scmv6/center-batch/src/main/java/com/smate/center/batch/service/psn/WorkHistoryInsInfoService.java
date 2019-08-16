package com.smate.center.batch.service.psn;

import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PsnWorkHistoryInsInfo;
import com.smate.core.base.psn.model.WorkHistory;

public interface WorkHistoryInsInfoService {

  public void updateWorkHistoryInsInfo(WorkHistory wk, Integer isPrimaryFlag) throws ServiceException;

  public void handlePosition(WorkHistory wk, PsnWorkHistoryInsInfo psnInsInfo) throws ServiceException;

  public Map<String, String> getUnitAndDepartmentofIns(String insName, String searchName) throws ServiceException;

}
