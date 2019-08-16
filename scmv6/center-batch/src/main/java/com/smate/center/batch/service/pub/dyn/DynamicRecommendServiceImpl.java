package com.smate.center.batch.service.pub.dyn;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.smate.center.batch.constant.DynamicTemplateConstant;
import com.smate.center.batch.constant.PdwhDynamicConstants;
import com.smate.center.batch.constant.RcmdDynamicConstants;
import com.smate.center.batch.dao.dynamic.DynamicRecommendHtmlDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.dynamic.DynRecommendFundForm;
import com.smate.center.batch.model.dynamic.DynRecommendPubForm;
import com.smate.center.batch.model.dynamic.DynamicRecommendHtml;
import com.smate.center.batch.model.psn.PsnFundReForm;
import com.smate.center.batch.service.psn.PsnStatisticsService;
import com.smate.center.batch.service.pub.PublicationHTMLService;
import com.smate.center.batch.service.pub.mq.DynRecommendThesisForm;
import com.smate.center.batch.util.pub.DynamicConstant;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.string.MapBuilder;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 推荐动态服务实现_SCM-5913.
 * 
 * @author mjg
 * @since 2014-11-10.
 */
@Service("dynamicRecommendService")
@Transactional(rollbackFor = Exception.class)
public class DynamicRecommendServiceImpl implements DynamicRecommendService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private static final String ENCODING = "utf-8";
  @Autowired
  private PsnStatisticsService psnStatisticsService;
  @Autowired
  private PublicationHTMLService publicationHTMLService;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private Configuration dynFreemarkerConfiguration;
  @Autowired
  private DynamicRecommendHtmlDao dynamicRecommendHtmlDao;

  /**
   * 保存推荐动态HTML内容到科研之友.
   * 
   * @param psnId
   * @param dynReType
   * @param dynContent
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void saveRecommendDyn(int dynReType, List reDynParamList) {
    if (CollectionUtils.isEmpty(reDynParamList)) {
      return;
    }

    try {
      // 构建各种推荐动态.
      switch (dynReType) {
        case RcmdDynamicConstants.DYN_RECOMMEND_TYPE_PUB:// 成果推荐动态.
          this.saveRePubDyn(reDynParamList);
          break;
        case RcmdDynamicConstants.DYN_RECOMMEND_TYPE_FUND:// 基金推荐动态.
          this.saveReFundDyn(reDynParamList);
          break;
        case PdwhDynamicConstants.DYN_RECOMMEND_TYPE_THESIS:// 论文推荐动态.
          this.saveReThesisDyn(reDynParamList);
          break;
      }
    } catch (Exception e) {
      logger.error("构建推荐动态出错", e);
    }
  }

  /**
   * 保存基金推荐动态广告HTML内容.
   * 
   * @param reDynParamList
   * @throws Exception
   */
  private void saveReFundDyn(List<DynRecommendFundForm> reDynParamList) throws Exception {
    int dynReType = RcmdDynamicConstants.DYN_RECOMMEND_TYPE_FUND;
    Template templateZh = dynFreemarkerConfiguration
        .getTemplate(DynamicTemplateConstant.DYN_TEMP_RECOMMEND_FUND + "_zh_CN.ftl", ENCODING);
    Template templateEn = dynFreemarkerConfiguration
        .getTemplate(DynamicTemplateConstant.DYN_TEMP_RECOMMEND_FUND + "_en_US.ftl", ENCODING);
    Long psnId = reDynParamList.get(0).getPsnId();
    List<PsnFundReForm> fundParamList = new ArrayList<PsnFundReForm>();
    for (DynRecommendFundForm fundForm : reDynParamList) {
      if (psnId.longValue() == fundForm.getPsnId().longValue()) {
        PsnFundReForm reFund = new PsnFundReForm();
        reFund.setCatDes3Id(fundForm.getCatDes3Id());
        reFund.setAgencyViewName(fundForm.getAgencyViewName());
        reFund.setAgencyViewNameEn(fundForm.getAgencyViewNameEn());
        reFund.setCategoryViewName(fundForm.getCategoryViewName());
        reFund.setCategoryViewNameEn(fundForm.getCategoryViewNameEn());
        fundParamList.add(reFund);
      }
    }
    Map<String, Object> paramMap = new HashMap<String, Object>();
    paramMap.put("psnFundReFormList", fundParamList);
    // 构建推荐动态HTML内容.
    String dynContentEn = FreeMarkerTemplateUtils.processTemplateIntoString(templateEn, paramMap);
    String dynContentZh = FreeMarkerTemplateUtils.processTemplateIntoString(templateZh, paramMap);
    this.saveDynRecommendHtml(psnId, dynReType, dynContentZh, dynContentEn);
    this.removeDynRcmdCache(psnId, dynReType);
  }

  /**
   * 保存成果推荐动态广告HTML内容.
   * 
   * @param reDynParamList
   * @throws Exception
   */
  private void saveRePubDyn(List<DynRecommendPubForm> reDynParamList) throws Exception {
    int dynReType = RcmdDynamicConstants.DYN_RECOMMEND_TYPE_PUB;
    Template templateZh =
        dynFreemarkerConfiguration.getTemplate(DynamicTemplateConstant.DYN_TEMP_RECOMMEND_PUB + "_zh_CN.ftl", ENCODING);
    Template templateEn =
        dynFreemarkerConfiguration.getTemplate(DynamicTemplateConstant.DYN_TEMP_RECOMMEND_PUB + "_en_US.ftl", ENCODING);
    for (DynRecommendPubForm pubForm : reDynParamList) {
      Long psnId = pubForm.getPsnId();
      Map<String, Object> paramMap = new HashMap<String, Object>();
      paramMap.put("des3Id", pubForm.getDes3Id());
      paramMap.put("authors", pubForm.getAuthors());
      paramMap.put("dtid", pubForm.getDtId());
      paramMap.put("pubTitle", pubForm.getPubTitle());
      paramMap.put("source", pubForm.getSource());
      paramMap.put("insId", pubForm.getInsId());
      // 构建推荐动态HTML内容.
      String dynContentZh = FreeMarkerTemplateUtils.processTemplateIntoString(templateZh, paramMap);
      String dynContentEn = FreeMarkerTemplateUtils.processTemplateIntoString(templateEn, paramMap);
      this.saveDynRecommendHtml(psnId, dynReType, dynContentZh, dynContentEn);
      this.removeDynRcmdCache(psnId, dynReType);
    }
  }

  /**
   * 保存论文推荐广告HTML内容.
   * 
   * @param reDynParamList
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private void saveReThesisDyn(List<DynRecommendThesisForm> reDynParamList) throws Exception {
    int dynReType = PdwhDynamicConstants.DYN_RECOMMEND_TYPE_THESIS;
    Template templateZh = dynFreemarkerConfiguration
        .getTemplate(DynamicTemplateConstant.DYN_TEMP_RECOMMEND_THESIS + "_zh_CN.ftl", ENCODING);
    Template templateEn = dynFreemarkerConfiguration
        .getTemplate(DynamicTemplateConstant.DYN_TEMP_RECOMMEND_THESIS + "_en_US.ftl", ENCODING);
    for (DynRecommendThesisForm pubForm : reDynParamList) {
      // 构建成果显示内容.
      String pubHtml = publicationHTMLService.buildPubShowCellNoFull(pubForm);
      Map<String, Object> paramMap = MapBuilder.getInstance().getMap();
      paramMap.put("htmlAbstract", pubHtml);
      // 构建推荐动态HTML内容.
      String dynContentZh = FreeMarkerTemplateUtils.processTemplateIntoString(templateZh, paramMap);
      String dynContentEn = FreeMarkerTemplateUtils.processTemplateIntoString(templateEn, paramMap);
      this.saveDynRecommendHtml(pubForm.getPsnId(), dynReType, dynContentZh, dynContentEn);
      this.removeDynRcmdCache(pubForm.getPsnId(), dynReType);
    }
  }

  /**
   * 保存推荐动态HTML结果内容.
   * 
   * @param psnId
   * @param dynReType
   * @param dynContent
   */
  // private void saveDynRecommendHtml(Long psnId, int dynReType, String
  // dynContent) {
  // DynamicRecommendHtml reDynHtml =
  // dynamicRecommendHtmlDao.getDynReHtml(psnId, dynReType);
  // if (reDynHtml == null) {
  // reDynHtml = new DynamicRecommendHtml();
  // reDynHtml.setPsnId(psnId);
  // reDynHtml.setDynReType(dynReType);
  // reDynHtml.setCreateDate(new Date());
  // }
  // reDynHtml.setDynHtml(dynContent);
  // reDynHtml.setUpdateDate(new Date());
  // dynamicRecommendHtmlDao.saveDynReHtml(reDynHtml);
  // }

  /**
   * 保存推荐动态HTML结果内容,增加保存英文版的html内容
   * 
   * @param psnId
   * @param dynReType
   * @param dynContentZh
   * @param dynContentEn
   */
  private void saveDynRecommendHtml(Long psnId, int dynReType, String dynContentZh, String dynContentEn) {
    DynamicRecommendHtml reDynHtml = dynamicRecommendHtmlDao.getDynReHtml(psnId, dynReType);
    if (reDynHtml == null) {
      reDynHtml = new DynamicRecommendHtml();
      reDynHtml.setPsnId(psnId);
      reDynHtml.setDynReType(dynReType);
    }
    reDynHtml.setDynHtml(dynContentZh);
    reDynHtml.setDynHtmlEn(dynContentEn);
    reDynHtml.setCreateDate(new Date());
    reDynHtml.setUpdateDate(new Date());
    dynamicRecommendHtmlDao.save(reDynHtml);
  }

  // public String getReDynHtml(Long psnId, int dynReType) {
  // return dynamicRecommendHtmlDao.getDynReHtmlCon(psnId, dynReType);
  // }

  public String getReDynHtml(String locale, Long psnId, int dynReType) {
    return dynamicRecommendHtmlDao.getDynReHtmlCon(locale, psnId, dynReType);
  }

  @Override
  public void delDynRecommendHtml(Long psnId, int dynReType) throws ServiceException {
    try {
      this.dynamicRecommendHtmlDao.delDynRecommendHtml(psnId, dynReType);
      this.removeDynRcmdCache(psnId, dynReType);
    } catch (Exception e) {
      logger.error("删除推荐动态HTML内容时出错啦！", e);
      throw new ServiceException(e);
    }
  }

  private void removeDynRcmdCache(Long psnId, int dynReType) {
    cacheService.remove(DynamicConstant.CACHE_DYN_RECOMMEND_NAME,
        DynamicConstant.CACHE_DYN_RECOMMEND_KEY + psnId + dynReType + "zh_CN");
    cacheService.remove(DynamicConstant.CACHE_DYN_RECOMMEND_NAME,
        DynamicConstant.CACHE_DYN_RECOMMEND_KEY + psnId + dynReType + "en_US");
  }

  @Override
  public void delDynRecommendBatch(List<Long> psnIds, int dynReType) throws ServiceException {
    if (CollectionUtils.isEmpty(psnIds)) {
      return;
    }
    for (Long psnId : psnIds) {
      DynamicRecommendHtml dynRcmdhtml = dynamicRecommendHtmlDao.getDynReHtml(psnId, dynReType);
      if (dynRcmdhtml != null) {
        dynamicRecommendHtmlDao.delete(dynRcmdhtml);
        this.removeDynRcmdCache(psnId, dynReType);
      }
    }
  }
}
