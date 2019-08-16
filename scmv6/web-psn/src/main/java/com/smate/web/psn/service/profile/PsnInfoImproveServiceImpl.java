package com.smate.web.psn.service.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.web.psn.dao.keywork.CategoryMapBaseDao;
import com.smate.web.psn.dao.keywork.PsnScienceAreaDao;
import com.smate.web.psn.dao.profile.PsnDiscScmDao;
import com.smate.web.psn.dao.profile.PsnDisciplineKeyDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.model.keyword.CategoryMapBase;
import com.smate.web.psn.model.keyword.PsnScienceArea;
import com.smate.web.psn.model.profile.PsnDiscScm;
import com.smate.web.psn.service.keyword.CategoryMapBaseService;
import com.smate.web.psn.service.sciencearea.ScienceAreaService;

/**
 * 人员信息完善服务
 * 
 * @author WSN
 *
 *         2017年8月15日 上午11:52:55
 *
 */
@Service("psnInfoImproveService")
@Transactional(rollbackFor = Exception.class)
public class PsnInfoImproveServiceImpl implements PsnInfoImproveService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnScienceAreaDao psnScienceAreaDao;
  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;
  @Autowired
  private PsnDiscScmDao psnDiscScmDao;
  @Autowired
  private CategoryMapBaseService categoryMapBaseService;
  @Autowired
  private PsnDisciplineKeyService psnDisciplineKeyService;
  @Autowired
  private ScienceAreaService scienceAreaService;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;
  @Autowired
  private EducationHistoryDao educationHistoryDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;

  @Override
  public Map<String, Boolean> psnHasScienceAreaAndKeywords(Long psnId) throws PsnException {
    boolean needArea = true;
    boolean needKeyword = true;
    boolean needWorkEdu = true;
    Map<String, Boolean> result = new HashMap<String, Boolean>();
    try {
      Long scienceAreaCount = psnScienceAreaDao.findPsnHasScienceArea(psnId, 1);
      if (scienceAreaCount > 0) {
        needArea = false;
      }
      Long keyCount = psnDisciplineKeyDao.countPsnDisciplineKey(psnId);
      if (keyCount > 0) {
        needKeyword = false;
      }
      // ==========教育经历或工作经历两个都没有才弹框================
      if (educationHistoryDao.isEduHistoryExit(psnId) || workHistoryDao.isWorkHistoryExit(psnId)) {
        needWorkEdu = false;
      }
      result.put("needArea", needArea);
      result.put("needKeyword", needKeyword);
      result.put("needWorkEdu", needWorkEdu);
    } catch (Exception e) {
      logger.error("判断人员是否填写了科技领域和关键词出错， psnId = " + psnId, e);
      throw new PsnException(e);
    }
    return result;
  }

  /**
   * 利用推荐的科技领域ID构建科技领域list
   * 
   * @param disc
   * @return
   */
  private List<PsnScienceArea> getScienceAreaByDisc(List<PsnDiscScm> disc) {
    List<PsnScienceArea> list = new ArrayList<PsnScienceArea>();
    String locale = LocaleContextHolder.getLocale().toString();
    if (CollectionUtils.isNotEmpty(disc)) {
      // 获取科技领域IDs
      List<Integer> scIds = new ArrayList<Integer>();
      for (PsnDiscScm dis : disc) {
        scIds.add(dis.getScmDiscNo().intValue());
      }
      // 根据科技领域ID构建科技领域list
      if (CollectionUtils.isNotEmpty(scIds)) {
        List<CategoryMapBase> caList = categoryMapBaseDao.findCategoryByIds(scIds);
        if (CollectionUtils.isNotEmpty(caList)) {
          for (CategoryMapBase ca : caList) {
            PsnScienceArea area = new PsnScienceArea();
            area.setScienceAreaId(ca.getCategryId());
            area.setEnScienceArea(ca.getCategoryEn());
            area.setScienceArea(ca.getCategoryZh());
            area.setShowScienceArea("en_US".equals(locale) ? ca.getCategoryEn() : ca.getCategoryZh());
            list.add(area);
          }
        }
      }
    }
    return list;
  }

  @Override
  public PersonProfileForm buildPsnImproveScienceAreaInfo(PersonProfileForm form) throws PsnException {
    try {
      Long psnId = form.getPsnId();
      if (psnId != null && psnId > 0) {
        // 获取人员已选的科技领域
        List<PsnScienceArea> scienceAreaList = scienceAreaService.findPsnScienceAreaList(form.getPsnId(), 1);
        // 最多5个
        if (scienceAreaList != null && scienceAreaList.size() > 5) {
          scienceAreaList = scienceAreaList.subList(0, 4);
        }
        // 人员科技领域没有值
        if (CollectionUtils.isEmpty(scienceAreaList)) {
          // 获取人员分类
          List<PsnDiscScm> dis = psnDiscScmDao.findPsnDisc(psnId);
          // ----------人造数据 begin-------------------
          // PsnDiscScm disc1 = new PsnDiscScm(1000000727634L, 207L);
          // PsnDiscScm disc2 = new PsnDiscScm(1000000727634L, 702L);
          // PsnDiscScm disc3 = new PsnDiscScm(1000000727634L, 511L);
          // dis.add(disc1);
          // dis.add(disc2);
          // dis.add(disc3);
          // ----------人造数据 end-------------------
          form.setDiscList(dis);
          scienceAreaList = this.getScienceAreaByDisc(dis);
        }
        form.setScienceAreaList(scienceAreaList);
        // 获取科技领域构建成的Map
        form.setCategoryMap(categoryMapBaseService.buildCategoryMapBaseInfo(scienceAreaList));
        if (CollectionUtils.isNotEmpty(scienceAreaList)) {
          StringBuilder scienceAreaIds = new StringBuilder();
          for (PsnScienceArea area : scienceAreaList) {
            scienceAreaIds.append("," + area.getScienceAreaId());
          }
          form.setScienceAreaIds(scienceAreaIds.toString());
        }
      }
    } catch (Exception e) {
      logger.error("构建人员科技领域信息完善页面所需信息出错，psnId = " + form.getPsnId(), e);
      throw new PsnException(e);
    }
    return form;
  }

  @Override
  public PersonProfileForm buildPsnImproveKeywordsInfo(PersonProfileForm form) throws PsnException {
    try {
      Long psnId = form.getPsnId();
      if (psnId != null && psnId > 0) {
        // 获取人员已选的关键词和推荐关键词
        List<PsnDisciplineKey> keyList = psnDisciplineKeyService.findPsnKeyWords(form.getPsnId());
        List<String> recommendKeywords =
            psnDisciplineKeyService.findPsnRecommendKeyWords(form.getPsnId(), LocaleContextHolder.getLocale(), keyList);
        form.setKeywords(keyList);
        if (recommendKeywords != null && recommendKeywords.size() > 100) {
          recommendKeywords.subList(0, 100);
        }
        form.setRecommendKeywords(recommendKeywords);
        // TODO 过滤推荐的关键词
      }
    } catch (Exception e) {
      logger.error("构建人员信息完善页面所需信息出错，psnId = " + form.getPsnId(), e);
      throw new PsnException(e);
    }
    return form;
  }
}
