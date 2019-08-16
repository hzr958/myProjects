package com.smate.center.task.service.tmp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.fund.sns.FundRecommendAreaDao;
import com.smate.center.task.dao.fund.sns.FundRecommendConditionsDao;
import com.smate.center.task.dao.fund.sns.FundRecommendRegionDao;
import com.smate.center.task.dao.sns.quartz.CategoryScmDao;
import com.smate.center.task.dao.sns.quartz.ConstRegionDao;
import com.smate.center.task.model.fund.sns.FundRecommendArea;
import com.smate.center.task.model.fund.sns.FundRecommendConditions;
import com.smate.center.task.model.fund.sns.FundRecommendRegion;
import com.smate.center.task.model.sns.pub.CategoryScm;
import com.smate.center.task.model.sns.pub.ConstRegion;
import com.smate.core.base.utils.json.JacksonUtils;

@Service
@Transactional(rollbackFor = Exception.class)
public class UpdateFundConditionTaskServiceImpl implements UpdateFundConditionTaskService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private FundRecommendConditionsDao fundRecommendConditionsDao;
  @Autowired
  private FundRecommendAreaDao fundRecommendAreaDao;
  @Autowired
  private FundRecommendRegionDao fundRecommendRegionDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private CategoryScmDao categoryScmDao;

  @Override
  public List<FundRecommendConditions> getFundRecommendConditions(Integer maxSize) throws Exception {
    return fundRecommendConditionsDao.getFundRecommendConditions(maxSize);
  }

  @Override
  public void updateFundRecommend(FundRecommendConditions fundRecommend) throws Exception {
    if (fundRecommend != null) {
      String jsonRegionStr = fundRecommend.getInterestRegion();
      saveFundRecommendRegion(jsonRegionStr, fundRecommend.getPsnId());// 保存关注地区
      String[] areaIds = StringUtils.split(fundRecommend.getScienceArea(), ",");
      if (ArrayUtils.isNotEmpty(areaIds)) {
        for (int j = 0; j < areaIds.length; j++) {
          fundConditionsScienceAreaSave(areaIds[j], fundRecommend.getPsnId());// 保存科技领域
        }
      }
    }

  }

  @SuppressWarnings("unchecked")
  private void saveFundRecommendRegion(String jsonRegionStr, Long psnId) {
    if (StringUtils.isNotBlank(jsonRegionStr)) {
      List<Map<String, String>> regionList = JacksonUtils.jsonToList(jsonRegionStr);
      if (CollectionUtils.isNotEmpty(regionList)) {
        List<FundRecommendRegion> regionInfo = new ArrayList<FundRecommendRegion>();
        for (Map<String, String> regionMap : regionList) {
          try {
            regionInfo.addAll(fundConditionsRegionSave(regionMap.get("regionId"), psnId));
          } catch (Exception e) {
            logger.error("保存基金推荐条件出错， jsonRegionStr = " + jsonRegionStr, e);
          }
        }
        Set<FundRecommendRegion> newRegionList = new HashSet<FundRecommendRegion>();
        for (FundRecommendRegion r : regionInfo) {
          newRegionList.add(r);
        }
        for (FundRecommendRegion r : newRegionList) {
          fundRecommendRegionDao.save(r);
        }
      }
    }
  }

  @SuppressWarnings("unused")
  public List<FundRecommendRegion> fundConditionsRegionSave(String regionId, Long psnId) throws Exception {
    try {
      String locale = LocaleContextHolder.getLocale().toString();
      List<FundRecommendRegion> regionInfo = new ArrayList<FundRecommendRegion>();
      if (psnId > 0 && StringUtils.isNotBlank(regionId)) {
        Long regionCode = NumberUtils.toLong(regionId);
        FundRecommendRegion fundRegion = fundRecommendRegionDao.getFundRegion(psnId, regionCode);
        if (fundRegion != null) {// 有重复就更新
          fundRegion.setUpdateDate(new Date());
          regionInfo.add(fundRegion);
        } else {// 没有重复就插入记录
          regionInfo.addAll(saveNewFundRecommendRegion(psnId, regionCode));
        }
      }
      return regionInfo;
    } catch (Exception e) {
      logger.error("保存基金推荐条件出错， psnId = " + psnId, e);
      throw new Exception(e);
    }

  }

  /**
   * 保存新加的地区和他父级地区，并存到list中
   * 
   */
  @SuppressWarnings("unchecked")
  private List<FundRecommendRegion> saveNewFundRecommendRegion(Long psnId, Long regionCode) {
    ConstRegion constRegion = constRegionDao.findRegionNameByRegionId(regionCode);
    List<FundRecommendRegion> res = new ArrayList<FundRecommendRegion>();
    if (constRegion != null) {
      // 获取所有父级id
      List<Long> superRegionIds = new ArrayList<Long>();
      superRegionIds = constRegionDao.getSuperRegionList(regionCode, false);
      String superRegionIdStr = "0";// 存到表里的字段
      superRegionIdStr = JacksonUtils.listToJsonStr(superRegionIds);
      superRegionIds.add(regionCode);// 把原本要插入的regionID也放进去，可能重复
      FundRecommendRegion fundRegion;
      Set<Long> set = new HashSet(superRegionIds);
      for (Long regId : set) {// 遍历把父类地区也存到表中
        fundRegion = fundRecommendRegionDao.getFundRegion(psnId, regId);
        if (fundRegion == null) {// 在设置表里没记录
          fundRegion = new FundRecommendRegion();
          if (regId.equals(regionCode)) {// 原本要插入的需要存父级地区
            fundRegion.setSuperRegionStrId(superRegionIdStr);
          }
          ConstRegion superConstRegion = constRegionDao.findRegionNameById(regId);// 查出地区表
          fundRegion.setPsnId(psnId);
          fundRegion.setRegionId(regId);
          fundRegion.setZhName(superConstRegion.getZhName());
          fundRegion.setEnName(superConstRegion.getEnName());
          fundRegion.setUpdateDate(new Date());
          res.add(fundRegion);
          // fundRecommendRegionDao.save(fundRegion);

        }
      }

    }
    return res;
  }

  public void fundConditionsScienceAreaSave(String areaCodeStr, Long psnId) throws Exception {
    try {
      if (psnId > 0 && StringUtils.isNotBlank(areaCodeStr)) {
        Long areaCode = NumberUtils.toLong(areaCodeStr);
        Long psnAreaNum = fundRecommendAreaDao.getPsnFundRecommendAreaSize(psnId);// 查询科技领域数
        boolean ishave = fundRecommendAreaDao.haveFundRecommendArea(psnId, areaCode);
        if ((psnAreaNum == 5 && ishave) || psnAreaNum < 5) {// 存在记录（更新时间）科技领域小于5
          saveFundRecommendArea(psnId, areaCode);// 保存并返回list
        }
      }
    } catch (Exception e) {
      logger.error("保存基金推荐条件出错， psnId = " + psnId, e);
      throw new Exception(e);
    }

  }

  private void saveFundRecommendArea(Long psnId, Long areaCode) {
    if (psnId > 0 && areaCode != null) {
      FundRecommendArea fundArea = fundRecommendAreaDao.getFundRecommendArea(psnId, areaCode);
      if (fundArea != null) {// 有重复就更新
        fundArea.setUpdateDate(new Date());
        fundRecommendAreaDao.save(fundArea);
      } else {// 没有重复就插入记录
        CategoryScm categoryScm = categoryScmDao.get(areaCode);
        if (categoryScm != null) {
          fundArea = new FundRecommendArea();
          fundArea.setPsnId(psnId);
          fundArea.setScienceAreaId(areaCode);
          fundArea.setParentId(categoryScm.getParentCategroyId());
          fundArea.setUpdateDate(new Date());
          fundArea.setEnName(categoryScm.getCategoryEn());
          fundArea.setZhName(categoryScm.getCategoryZh());
          fundRecommendAreaDao.save(fundArea);
        }

      }
    }
  }

  @Override
  public void updateFundRecommendConditionStatus(Long psnId) throws Exception {
    fundRecommendConditionsDao.updateFundRecommendConditionStatus(psnId);
  }

}
