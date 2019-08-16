package com.smate.center.batch.service.psn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.institution.AcInsUnitDao;
import com.smate.center.batch.dao.sns.institution.InstitutionDao;
import com.smate.center.batch.dao.sns.pub.ConstPositionDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.AcInsUnit;
import com.smate.center.batch.model.sns.pub.Institution;
import com.smate.center.batch.model.sns.pub.PsnWorkHistoryInsInfo;
import com.smate.center.batch.service.mail.ReceiverServiceImpl;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.WorkHistory;

/**
 * 首要工作经历单位信息处理服务类
 * 
 * @author zk
 * 
 */
@Service("workHistoryInsInfoService")
@Transactional(rollbackFor = Exception.class)
public class WorkHistoryInsInfoServiceImpl implements WorkHistoryInsInfoService {

  private static Logger logger = LoggerFactory.getLogger(ReceiverServiceImpl.class);

  @Autowired
  private WorkHistoryDao workHistoryDao;

  @Autowired
  private ConstPositionDao constPositionDao;

  @Autowired
  private InstitutionDao institutionDao;

  @Autowired
  private AcInsUnitDao acInsUnitDao;

  @Autowired
  private PsnHtmlRefreshService psnHtmlRefreshService;

  @Autowired
  private PsnWorkHistoryInsInfoService psnWorkHistoryInsInfoService;

  /**
   * 更新工作经历单位信息<只许是首要单位或最新工作经历调用>
   */
  @Override
  public void updateWorkHistoryInsInfo(WorkHistory wk, Integer isPrimaryFlag) throws ServiceException {
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
        psnHtmlRefreshService.updatePsnHtmlRefresh(wk.getPsnId());
      }
    } catch (Exception e) {
      logger.error("更新工作经历单更新工作经历单位信息位信息出错" + wk, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 通过insId处理相关数据
   * 
   * @param psnInsInfo
   * @param wk
   * @throws ServiceException
   */
  private void handleInfoByInsId(Institution institution, PsnWorkHistoryInsInfo psnInsInfo, WorkHistory wk)
      throws ServiceException {
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
      throw new ServiceException(e);
    }
  }

  /**
   * 通过单位名
   * 
   * @throws ServiceException
   */
  private void handleInfoByInsName(PsnWorkHistoryInsInfo psnInsInfo, WorkHistory wk) throws ServiceException {
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
      throw new ServiceException(e);
    }
  }

  // 处理职称
  @SuppressWarnings("rawtypes")
  @Override
  public void handlePosition(WorkHistory wk, PsnWorkHistoryInsInfo psnInsInfo) throws ServiceException {

    try {
      if (wk.getPosId() == null && StringUtils.isNotBlank(wk.getPosition())) {
        psnInsInfo.setPositionZh(wk.getPosition());
        psnInsInfo.setPositionEn(wk.getPosition());
      } else if (wk.getPosId() != null) {
        Map posMap = constPositionDao.getConstPosition(wk.getPosId());
        if (MapUtils.isNotEmpty(posMap)) {
          psnInsInfo.setPositionZh(Objects.toString(posMap.get("ZHNAME"), ""));
          psnInsInfo.setPositionEn(Objects.toString(posMap.get("ENNAME"), ""));
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

  private void handleDepartment(PsnWorkHistoryInsInfo psnInsInfo, String insName, String department)
      throws ServiceException {

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
   * 获取单位、部门的中英文
   * 
   * @param insName
   * @param searchName
   * @return
   * @throws ServiceException
   */
  @Override
  public Map<String, String> getUnitAndDepartmentofIns(String insName, String searchName) throws ServiceException {
    Map<String, String> returnMap = null;
    try {
      returnMap = new HashMap<String, String>();
      List<AcInsUnit> acInsUnitList = acInsUnitDao.getAcInsUnit(insName, searchName);
      if (CollectionUtils.isNotEmpty(acInsUnitList)) {
        AcInsUnit acInsUnit = acInsUnitList.get(0);
        returnMap = this.getUnitName(acInsUnit.getUnitIds(), returnMap, insName);
      }
      return returnMap;
    } catch (Exception e) {
      logger.error("获取单位、部门的中英文出错，insName={},searchName={},Exception={}", insName, searchName, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取部门名<整理院系的中英文>
   * 
   * @param unitIdsStr
   * @param returnMap
   * @param insName
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("rawtypes")
  private Map<String, String> getUnitName(String unitIdsStr, Map<String, String> returnMap, String insName)
      throws ServiceException {
    try {
      if (StringUtils.isNotBlank(unitIdsStr)) {
        String[] unitIds = StringUtils.split(unitIdsStr, ",");
        List<Long> unitList = new ArrayList<Long>();
        for (String unitS : unitIds) {
          unitList.add(Long.valueOf(unitS));
        }
        String zhName = "";
        String enName = "";
        List<Map> unitMap = acInsUnitDao.getConstInsUnit(unitList, insName);
        for (Long unitId : unitList) {
          for (Map map : unitMap) {
            if (unitId.equals(Long.valueOf(String.valueOf(map.get("UNIT_ID"))))) {
              if (StringUtils.isNotBlank(Objects.toString(map.get("ZH_NAME"), ""))) {
                zhName += map.get("ZH_NAME") + " ";
              }
              if (StringUtils.isNotBlank(Objects.toString(map.get("EN_NAME"), ""))) {
                enName += map.get("EN_NAME") + " ";
              }
            }
          }
        }
        if (StringUtils.isNotBlank(zhName)) {
          returnMap.put("zhName", zhName);
        }
        if (StringUtils.isNotBlank(enName)) {
          returnMap.put("enName", enName);
        }
      }
      return returnMap;
    } catch (Exception e) {
      logger.error("获取部门名时出错，insName={},unitIdStr={},Exception={}", insName, unitIdsStr, e);
      throw new ServiceException(e);
    }
  }
}
