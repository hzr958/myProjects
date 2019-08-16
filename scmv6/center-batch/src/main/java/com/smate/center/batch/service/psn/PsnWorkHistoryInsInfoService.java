package com.smate.center.batch.service.psn;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PsnWorkHistoryInsInfo;


public interface PsnWorkHistoryInsInfoService {

  public void savePsnWorkHistoryInsInfo(PsnWorkHistoryInsInfo psnWorkHistoryInsInfo) throws ServiceException;

  public void savePsnWorkHistoryInsInfo(Long psnId, Long insId, String insNameZh, String insNameEn, String departmentZh,
      String departmentEn, String positionZh, String positionEn) throws ServiceException;

  public PsnWorkHistoryInsInfo getPsnWorkHistoryInsInfo(Long psnId) throws ServiceException;

}
