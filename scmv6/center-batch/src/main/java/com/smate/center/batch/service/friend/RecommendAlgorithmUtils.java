package com.smate.center.batch.service.friend;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.psn.model.WorkHistory;

/**
 * @author tsz
 * 
 */

public class RecommendAlgorithmUtils implements java.io.Serializable {
  private static final long serialVersionUID = -7061668963099709799L;
  private static final Logger logger = LoggerFactory.getLogger(RecommendAlgorithmUtils.class);

  public static double workSecore(WorkHistory psnWork, WorkHistory tempWork) {
    double secore = 0;
    try {
      if (psnWork == null || tempWork == null)
        return 0;
      boolean isMatchIns = false;
      boolean isMatchUnit = false;
      boolean isMatchDate = false;
      if (psnWork.getInsId() != null && tempWork.getInsId() != null && psnWork.getInsId().equals(tempWork.getInsId())) {
        isMatchIns = true;
      }
      if (StringUtils.isNotBlank(psnWork.getInsName()) && StringUtils.isNotBlank(tempWork.getInsName())
          && psnWork.getInsName().equals(tempWork.getInsName())) {
        isMatchIns = true;
      }
      if (StringUtils.isNotBlank(psnWork.getDepartment()) && StringUtils.isNotBlank(tempWork.getDepartment())
          && psnWork.getDepartment().equals(tempWork.getDepartment())) {
        isMatchUnit = true;
      }
      if (psnWork.getFromYear() != null && psnWork.getFromMonth() != null && tempWork.getFromYear() != null
          && tempWork.getFromMonth() != null) {
        if (psnWork.getIsActive() != null && tempWork.getIsActive() != null
            && psnWork.getIsActive().intValue() == tempWork.getIsActive().intValue())
          isMatchDate = true;
        if (psnWork.getIsActive() != null && tempWork.getToYear() != null
            && (tempWork.getToYear().intValue() - psnWork.getFromYear().intValue() > 0))
          isMatchDate = true;
        if (psnWork.getIsActive() == null && tempWork.getIsActive() != null && psnWork.getToYear() != null
            && (psnWork.getToYear() >= tempWork.getFromYear()))
          isMatchDate = true;
        if (psnWork.getIsActive() == null && tempWork.getIsActive() == null && tempWork.getToYear() != null
            && tempWork.getToYear() >= psnWork.getFromYear())
          isMatchDate = true;
      }
      if (isMatchIns && isMatchUnit && isMatchDate) {
        secore = 100;
      } else if (isMatchIns && isMatchDate) {
        secore = 100 / 2;
      } else if (isMatchIns) {
        secore = 50 / 2;
      }
    } catch (Exception e) {
      logger.error("计算智能推荐好友工作经历得分出错", e);
    }
    return secore;
  }

  public static double eduSecore(EducationHistory psnEdu, EducationHistory tempEdu) {
    double secore = 0;
    try {
      if (psnEdu == null || tempEdu == null)
        return 0;
      boolean isMatchIns = false;
      boolean isMatchUnit = false;
      boolean isMatchDate = false;
      if (psnEdu.getInsId() != null && tempEdu.getInsId() != null && psnEdu.getInsId().equals(tempEdu.getInsId())) {
        isMatchIns = true;
      }
      if (StringUtils.isNotBlank(psnEdu.getInsName()) && StringUtils.isNotBlank(tempEdu.getInsName())
          && psnEdu.getInsName().equals(tempEdu.getInsName())) {
        isMatchIns = true;
      }
      if (StringUtils.isNotBlank(psnEdu.getStudy()) && StringUtils.isNotBlank(tempEdu.getStudy())
          && psnEdu.getStudy().equals(tempEdu.getStudy())) {
        isMatchUnit = true;
      }
      if (psnEdu.getFromYear() != null && psnEdu.getToYear() != null && tempEdu.getFromYear() != null
          && tempEdu.getToYear() != null && (tempEdu.getToYear() >= psnEdu.getFromYear())) {
        isMatchDate = true;
      }
      if (isMatchIns && isMatchUnit && isMatchDate) {
        secore = 100;
      } else if (isMatchIns && isMatchDate) {
        secore = 100 / 2;
      } else if (isMatchIns) {
        secore = 50 / 2;
      }
    } catch (Exception e) {
      logger.error("计算智能推荐好友教育经历得分出错", e);
    }
    return secore;
  }

  public static double friendOfFriendSecore(int matchPsnFriendCount) {
    double secore = 0;
    if (matchPsnFriendCount < 3)
      secore = 60;
    else if (matchPsnFriendCount >= 3 && matchPsnFriendCount < 5)
      secore = 80;
    else if (matchPsnFriendCount >= 5)
      secore = 100;
    return secore;
  }

}
