package com.smate.web.psn.service.edu;

import java.util.List;

import com.smate.core.base.psn.model.EducationHistory;
import com.smate.web.psn.exception.PsnException;

/**
 * 教育经历 服务接口
 * 
 * @author tsz
 *
 */
public interface PsnEduHistoryInsInfoService {

  /**
   * 根据人员id 获取教育经历
   * 
   * @param psnId
   * @return
   * @throws PsnException
   */
  public List<EducationHistory> getPsnEduHistory(Long psnId) throws PsnException;

  /**
   * 获取首要的教育经历
   * 
   * @param psnId
   * @return
   * @throws PsnException
   */
  public EducationHistory getFirstEdu(Long psnId) throws PsnException;
}
