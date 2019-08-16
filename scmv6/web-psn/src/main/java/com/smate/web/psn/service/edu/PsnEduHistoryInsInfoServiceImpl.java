package com.smate.web.psn.service.edu;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.utils.LocaleStringUtils;

/**
 * 
 * @author zjh 教育经历实现类
 *
 */
@Service("psnEduHistoryInsInfoService")
@Transactional(rollbackFor = Exception.class)
public class PsnEduHistoryInsInfoServiceImpl implements PsnEduHistoryInsInfoService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private EducationHistoryDao educationHistoryDao;
  @Value("${domainscm}")
  private String domianScm;
  @Autowired
  private InsPortalDao insPortalDao;

  /**
   * 获取教育经历
   */
  @Override
  public List<EducationHistory> getPsnEduHistory(Long psnId) throws PsnException {
    List<EducationHistory> eduList = educationHistoryDao.findByPsnId(psnId);
    if (!CollectionUtils.isEmpty(eduList)) {
      for (EducationHistory edu : eduList) {
        edu.setEduDesc(this.buildEduDesc(edu));
        // 单位对应的图片
        if (NumberUtils.isNotNullOrZero(edu.getInsId())) {
          InsPortal insPortal = insPortalDao.get(edu.getInsId());
          String insImgPath = insPortal != null ? insPortal.getLogo() : "";
          edu.setInsImgPath(StringUtils.isNotEmpty(insImgPath) ? domianScm + insImgPath : null);
        }
      }
    }
    if (eduList == null) {
      eduList = new ArrayList<EducationHistory>();
    }
    return eduList;
  }

  /**
   * 获取首要教育经历
   */
  @Override
  public EducationHistory getFirstEdu(Long psnId) throws PsnException {
    try {
      return this.educationHistoryDao.getFirstEdu(psnId);
    } catch (Exception e) {
      logger.error("获取教育首要单位", e);
      throw new PsnException("获取教育首要单位", e);
    }
  }

  /**
   * 构建工作经历信息 ： 部门、职称、开始年月-结束年月
   * 
   * @param work
   * @return
   */
  private String buildEduDesc(EducationHistory edu) {
    String department = StringUtils.isBlank(edu.getStudy()) ? "" : edu.getStudy();
    String position = StringUtils.isBlank(edu.getDegreeName()) ? "" : edu.getDegreeName();
    Long fromYear = edu.getFromYear();
    Long toYear = edu.getToYear();
    Long fromMonth = edu.getFromMonth();
    Long toMonth = edu.getToMonth();
    Long isActive = edu.getIsActive() == null ? 0l : edu.getIsActive();
    StringBuilder workDesc = new StringBuilder();
    if (StringUtils.isBlank(department) || StringUtils.isBlank(position)) {
      workDesc.append(department + position);
    } else {
      workDesc.append(department + ", " + position);
    }
    if (fromYear != null) {
      if (StringUtils.isBlank(workDesc.toString())) {
        workDesc.append(fromYear);
      } else {
        workDesc.append(", " + fromYear);
      }
      if (fromMonth != null) {
        workDesc.append("/" + fromMonth);
      }
      if (toYear != null) {
        workDesc.append(" - " + toYear);
        if (toMonth != null) {
          workDesc.append("/" + toMonth);
        }
      } else {
        if (isActive == 1) {
          workDesc.append(LocaleStringUtils.getStringByLocale(" - to Present", " - 至今"));
        }
      }
    }
    return workDesc.toString();
  }

}
