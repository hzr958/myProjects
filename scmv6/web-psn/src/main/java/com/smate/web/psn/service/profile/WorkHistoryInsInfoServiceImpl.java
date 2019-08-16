package com.smate.web.psn.service.profile;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.ConstPositionDao;
import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.workhistory.PsnWorkHistoryInsInfo;
import com.smate.web.psn.service.psnhtml.PsnHtmlRefreshService;
import com.smate.web.psn.service.psnwork.PsnWorkHistoryInsInfoService;

@Service("workHistoryInsInfoService")
@Transactional(rollbackFor = Exception.class)
public class WorkHistoryInsInfoServiceImpl implements WorkHistoryInsInfoService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private PsnWorkHistoryInsInfoService psnWorkHistoryInsInfoService;
  @Autowired
  private PsnHtmlRefreshService psnHtmlRefreshService;
  @Autowired
  private ConstPositionDao constPositionDao;



  /**
   * 更新工作经历单位信息<只许是首要单位或最新工作经历调用>
   */
  @Override
  public void updateWorkHistoryInsInfo(WorkHistory wk, Integer isPrimaryFlag) throws PsnException {
    try {
      // 不是首要单位，且已有首要单位，则返回
      if (isPrimaryFlag == 0 && workHistoryDao.hasPrimaryWorkHistory(wk.getPsnId())) {
        return;
      }
      if (wk != null) {
        PsnWorkHistoryInsInfo psnInsInfo = new PsnWorkHistoryInsInfo();
        psnInsInfo.setPsnId(wk.getPsnId());
        if (wk.getInsId() != null) {
          psnInsInfo.setInsId(wk.getInsId());
          Institution institution = institutionDao.findById(wk.getInsId());
          this.handleInfoByInsId(institution, psnInsInfo, wk);
        } else {
          this.handleInfoByInsName(psnInsInfo, wk);
        }
        this.handlePosition(wk, psnInsInfo);
        psnWorkHistoryInsInfoService.savePsnWorkHistoryInsInfo(psnInsInfo);
        // psnHtmlRefreshService.updatePsnHtmlRefresh(wk.getPsnId());
      }
    } catch (Exception e) {
      logger.error("更新工作经历单更新工作经历单位信息位信息出错" + wk, e);
      throw new PsnException(e);
    }

  }

  /**
   * 获取单位、部门的中英文
   * 
   * @param insName @param searchName @return @throws
   */
  @Override
  public Map<String, String> getUnitAndDepartmentofIns(String insName, String searchName) throws PsnException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * 处理职称 @param wk @param psnInsInfo @throws
   */
  @Override
  public void handlePosition(WorkHistory wk, PsnWorkHistoryInsInfo psnInsInfo) throws PsnException {
    try {
      if (wk.getPosId() == null && StringUtils.isNotBlank(wk.getPosition())) {
        psnInsInfo.setPositionZh(wk.getPosition());
        psnInsInfo.setPositionEn(wk.getPosition());
      } else if (wk.getPosId() != null) {
        Map posMap = constPositionDao.getConstPosition(wk.getPosId());
        if (MapUtils.isNotEmpty(posMap)) {
          psnInsInfo.setPositionZh(Objects.toString(posMap.get("ZHNAME")));
          psnInsInfo.setPositionEn(Objects.toString(posMap.get("ENNAME")));
          if (StringUtils.isBlank(psnInsInfo.getPositionZh()) && StringUtils.isNotBlank(psnInsInfo.getPositionEn())) {
            psnInsInfo.setPositionZh(psnInsInfo.getPositionEn());
          }
          if (StringUtils.isBlank(psnInsInfo.getPositionEn()) && StringUtils.isNotBlank(psnInsInfo.getPositionZh())) {
            psnInsInfo.setPositionEn(psnInsInfo.getPositionZh());
          }
        } else if (StringUtils.isNotBlank(wk.getPosition())) {
          psnInsInfo.setPositionZh(wk.getPosition());
          psnInsInfo.setPositionEn(wk.getPosition());
        }
      }
    } catch (Exception e) {
      logger.error("更新工作经历单处理职称信息出错" + wk, e);
    }

  }

  /**
   * 当用户删除工作经历或取消首要单位时，检查并清除单位信息
   * 
   * @throws
   */
  @Override
  public void checkWorkHistoryInsInfo(WorkHistory wk) throws PsnException {
    Long insId = wk.getInsId() == null ? -1 : wk.getInsId();
    psnWorkHistoryInsInfoService.deletePsnWorkHistoryInsInfo(wk.getPsnId(), insId, wk.getInsName());
    // psnHtmlRefreshService.updatePsnHtmlRefresh(wk.getPsnId());
  }

  /**
   * 通过insId处理相关数据
   * 
   * @param psnInsInfo @param wk @throws
   */
  private void handleInfoByInsId(Institution institution, PsnWorkHistoryInsInfo psnInsInfo, WorkHistory wk)
      throws PsnException {
    try {
      if (institution != null) {
        String insName = null;
        psnInsInfo.setInsNameZh(institution.getZhName());
        psnInsInfo.setInsNameEn(institution.getEnName());
        // 先用中文单位匹配
        if (StringUtils.isNotBlank(institution.getZhName())) {
          insName = institution.getZhName();
          this.handleDepartment(psnInsInfo, insName, wk.getDepartment());
        }
        // 匹配
        if (StringUtils.isBlank(psnInsInfo.getDepartmentZh()) && StringUtils.isNotBlank(institution.getEnName())) {
          insName = institution.getEnName();
          this.handleDepartment(psnInsInfo, insName, wk.getDepartment());
        }
      }
    } catch (Exception e) {
      logger.error("通过单位id更新工作经历单更新工作经历单位信息位信息出错" + wk, e);
      throw new PsnException(e);
    }
  }


  private void handleDepartment(PsnWorkHistoryInsInfo psnInsInfo, String insName, String department)
      throws PsnException {

    Map<String, String> insMap = this.getUnitAndDepartmentofIns(insName, department);
    if (MapUtils.isNotEmpty(insMap)) {
      psnInsInfo.setDepartmentZh(insMap.get("zhName"));
      psnInsInfo.setDepartmentEn(insMap.get("enName"));
    } else {
      if (StringUtils.isNotBlank(department)) {
        psnInsInfo.setDepartmentZh(department);
        psnInsInfo.setDepartmentEn(department);
      }
    }
  }


  /**
   * 通过单位名
   * 
   * @throws
   */
  private void handleInfoByInsName(PsnWorkHistoryInsInfo psnInsInfo, WorkHistory wk) throws PsnException {
    try {
      Institution institution = institutionDao.findByName(wk.getInsName());
      if (institution != null) {
        this.handleInfoByInsId(institution, psnInsInfo, wk);
      } else {
        psnInsInfo.setInsNameZh(wk.getInsName());
        psnInsInfo.setInsNameEn(wk.getInsName());
        this.handleDepartment(psnInsInfo, wk.getInsName(), wk.getDepartment());
      }
    } catch (Exception e) {
      logger.error("通过单位名更新工作经历单更新工作经历单位信息位信息出错" + wk, e);
      throw new PsnException(e);
    }
  }

}
