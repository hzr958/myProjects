package com.smate.web.fund.service.find;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.ConstPositionDao;
import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.model.ConstPosition;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.common.LocaleTextUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.fund.agency.dao.FundAgencyDao;
import com.smate.web.fund.dao.wechat.ConstFundCategoryDao;
import com.smate.web.fund.dao.wechat.ConstFundCategoryKeywordsDao;
import com.smate.web.fund.dao.wechat.ConstFundCategoryRcmdDao;
import com.smate.web.fund.dao.wechat.ConstFundCategoryRegionDao;
import com.smate.web.fund.find.model.FundFindForm;
import com.smate.web.fund.model.common.ConstFundAgency;
import com.smate.web.fund.model.common.ConstFundCategory;
import com.smate.web.fund.model.common.ConstFundCategoryInfo;
import com.smate.web.fund.recommend.dao.CategoryMapBaseDao;
import com.smate.web.fund.recommend.dao.ConstFundAgencyDao;
import com.smate.web.fund.recommend.dao.FundAwardDao;
import com.smate.web.fund.recommend.dao.FundStatisticsDao;
import com.smate.web.fund.recommend.dao.MyFundDao;
import com.smate.web.fund.recommend.model.CategoryMapBase;
import com.smate.web.fund.recommend.model.FundAward;
import com.smate.web.fund.recommend.model.FundStatistics;
import com.smate.web.fund.recommend.rcmd.dao.ConstFundCategoryDisDao;
import com.smate.web.fund.service.recommend.CategoryMapBaseService;
import com.smate.web.prj.exception.PrjException;
import com.smate.web.prj.model.wechat.FundWeChat;

@Service("fundFindService")
@Transactional(rollbackFor = Exception.class)
public class FundFindServiceImpl implements FundFindService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MyFundDao myFundDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private CategoryMapBaseService categoryMapBaseService;
  @Autowired
  private SolrIndexService solrIndexService;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private ConstPositionDao constPositionDao;
  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;
  @Autowired
  private ConstFundCategoryDao constFundCategoryDao;
  @Autowired
  private FundAwardDao fundAwardDao;
  @Value("${domainscm}")
  private String domainScm;
  @Autowired
  private FundStatisticsDao fundStatisticsDao;
  @Autowired
  private ConstFundCategoryRcmdDao constFundCategoryRcmdDao;// 基金dao
  @Autowired
  private FundAgencyDao fundAgencyDao;// 机构dao
  @Autowired
  private ConstFundCategoryKeywordsDao constFundCategoryKeywordsDao;// 基金关键词dao
  @Autowired
  private ConstFundCategoryDisDao constFundCategoryDisDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;
  @Autowired
  private ConstFundCategoryRegionDao constFundCategoryRegionDao;

  /**
   * 获取基金发现条件筛选数据
   */
  @Override
  public void fundFindConditionsShow(FundFindForm form) throws PrjException {
    try {
      Long psnId = form.getPsnId();
      if (psnId == null) {
        return;
      }
      // 获取地区
      List<ConstRegion> regionList = this.getFundRegion();
      form.setRegionList(regionList);
      // 获取科技领域
      form.setCategoryMap(categoryMapBaseService.buildCategoryMapBaseInfo2());
    } catch (Exception e) {
      logger.error("构建基金发现条件显示出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
      throw new PrjException(e);
    }
  }

  /**
   * 查询基金
   * 
   * @param form
   * @return
   * @throws PrjException
   */
  @Override
  public FundFindForm fundFindListSearch(FundFindForm form) throws PrjException {
    try {
      Long psnId =
          com.smate.core.base.utils.number.NumberUtils.isZero(SecurityUtils.getCurrentUserId()) ? form.getPsnId()
              : SecurityUtils.getCurrentUserId();
      Integer grade = this.findPsnGrades(form.getPsnId());
      // String locale = LocaleContextHolder.getLocale().toString();
      String locale = form.getLocale();
      // 都为空按结题时间排序sortOrder按时间排序标志
      boolean sortOrder = false;
      if (isBlankSelectCondition(form)) {
        sortOrder = true;
      }
      List<ConstFundCategory> fundCategoryList;
      // form.getPage().setPageNo(form.getPageNum());
      if (sortOrder) {
        fundCategoryList = constFundCategoryRcmdDao.findAllConstFundCategory(form.getPage());
      } else {
        // 获取单位类型 ：0为获取所有，1为科技企业，2为科研机构
        String seniority = StringUtils.isBlank(form.getSeniorityCodeSelect()) ? "0" : form.getSeniorityCodeSelect();
        // 获取科技领域
        List<Long> scienceAreas = new ArrayList<>();
        if (StringUtils.isNotBlank(form.getScienceCodesSelect())) {
          String[] strArry = form.getScienceCodesSelect().split(",");
          scienceAreas = Arrays.asList(com.smate.core.base.utils.number.NumberUtils.parseLongArry(strArry));
        }
        // 获取所选地区
        String regionIdStrs = form.getRegionCodesSelect();
        List<Long> categoryfundIdList = new ArrayList<>();
        boolean ifFindCountry = false;// 是否要查找国家级基金
        // 根据所选地区条件下的基金
        if (StringUtils.isNoneBlank(regionIdStrs)) {
          String[] regionIdStrArray = regionIdStrs.split(",");
          List<Long> regionIds = new ArrayList<>();
          for (String regionIdStr : regionIdStrArray) {
            regionIds.add(Long.parseLong(regionIdStr));
          }
          if (regionIds.contains(157L)) {
            List<Long> newRegionIds = new ArrayList<>();
            for (Long regionId : regionIds) {
              if (regionId != 157L) {
                newRegionIds.add(regionId);
              }
            }
            regionIds = newRegionIds;
            ifFindCountry = true;
          }
          // 查询下级地区
          if (CollectionUtils.isNotEmpty(regionIds)) {
            List<Long> subRegionIds = constRegionDao.findSubRegionIdBySuperRegionId(regionIds);
            regionIds.addAll(subRegionIds);
            // 获取适用地区下的基金
            categoryfundIdList = constFundCategoryRegionDao.queryfundIdByregionIds(regionIds);
          }
        }
        String searchKey = form.getSearchKey().trim();
        // 匹配关键词获取基金id
        List<Long> fundIdBySearchKey = new ArrayList<>();
        if (StringUtils.isNoneBlank(searchKey)) {
          fundIdBySearchKey = constFundCategoryKeywordsDao.findFundIdBySearchKey(searchKey);
        }
        // 获取基金列表
        fundCategoryList = constFundCategoryRcmdDao.findConstFundCategory(regionIdStrs, categoryfundIdList,
            ifFindCountry, searchKey, fundIdBySearchKey, seniority, scienceAreas, form.getPage());
      }
      Long count = form.getPage().getTotalCount();
      if (com.smate.core.base.utils.number.NumberUtils.isNotNullOrZero(count)) {
        Integer num = NumberUtils.toInt(count.toString());
        Integer total = num % form.getPageSize() > 0 ? num / form.getPageSize() + 1 : num / form.getPageSize();
        form.setTotalPages(total);
        form.setTotalCount(num);
      }
      // 构建基金信息
      if (CollectionUtils.isNotEmpty(fundCategoryList)) {
        List<ConstFundCategoryInfo> infoList = new ArrayList<ConstFundCategoryInfo>();
        // 查找已收藏过的基金
        List<Long> collectIds = myFundDao.findCollectFundIds(form.getPsnId());
        List<String> des3FundAgencyIds = new ArrayList<>();
        for (ConstFundCategory fundCategory : fundCategoryList) {
          ConstFundCategoryInfo fund = new ConstFundCategoryInfo();
          // 构建显示信息
          this.buildFundInfo(fundCategory, fund, form);
          des3FundAgencyIds.add(Des3Utils.encodeToDes3(fundCategory.getAgencyId().toString()));
          this.buildFundInternationalInfo(fund);
          Long fundId = fundCategory.getId();
          // 是否已收藏过
          if (CollectionUtils.isNotEmpty(collectIds) && collectIds.contains(fundId)) {
            fund.setHasCollected(true);
          }
          // 是否赞过
          FundAward awardRecord = fundAwardDao.findFundAwardRecord(psnId, fundId);
          if (awardRecord != null && awardRecord.getStatus() == LikeStatusEnum.LIKE) {
            fund.setHasAward(true);
          }
          infoList.add(fund);
        }

        form.setDes3FundAgencyIds(StringUtils.join(des3FundAgencyIds, ","));
        form.setFundInfoList(infoList);
      }
    } catch (Exception e) {
      logger.error("查询基金发现列表出错，" + this.buildSearchString(form), e);
      throw new PrjException(e);
    }
    return form;
  }

  /**
   * 日志用
   * 
   * @param form
   * @return
   */
  private String buildSearchString(FundFindForm form) {
    String searchString = "psnId = " + SecurityUtils.getCurrentUserId();
    searchString += ", regionCodes = " + form.getRegionCodesSelect();
    searchString += ", scienceAreaNames = " + form.getScienceCodesSelect();
    searchString += ", senority = " + form.getSeniorityCodeSelect();
    return searchString;
  }

  /**
   * 构建基金国际化信息，分享的时候有用到
   */
  private void buildFundInternationalInfo(ConstFundCategoryInfo fund) {
    String zhFundAgency = LocaleTextUtils.getStrByLocale("zh_CN", fund.getZhAgencyName(), fund.getEnAgencyName());
    String zhScienceArea = LocaleTextUtils.getStrByLocale("zh_CN", fund.getZhScienceArea(), fund.getEnScienceArea());
    String enFundAgency = LocaleTextUtils.getStrByLocale("en_US", fund.getZhAgencyName(), fund.getEnAgencyName());
    String enScienceArea = LocaleTextUtils.getStrByLocale("en_US", fund.getZhScienceArea(), fund.getEnScienceArea());
    fund.setZhShowDesc(this.getFundShowDescByLocale(zhFundAgency, zhScienceArea, fund.getShowDate(), "zh_CN"));
    fund.setEnShowDesc(this.getFundShowDescByLocale(enFundAgency, enScienceArea, fund.getShowDate(), "en_US"));
    fund.setZhShowDescBr(this.getFundShowDescByBr(zhFundAgency, zhScienceArea, fund.getShowDate()));
    fund.setEnShowDescBr(this.getFundShowDescByBr(enFundAgency, enScienceArea, fund.getShowDate()));
  }

  private String getFundShowDescByLocale(String fundAgency, String scienceArea, String applyTime, String locale) {
    String showDesc = "";
    String joinStr = "zh_CN".equals(locale) ? "，" : ", ";
    if (StringUtils.isNotBlank(fundAgency)) {
      showDesc += fundAgency;
    }
    if (StringUtils.isNotBlank(scienceArea)) {
      if (StringUtils.isNotBlank(showDesc)) {
        showDesc += joinStr + scienceArea;
      } else {
        showDesc += scienceArea;
      }
    }
    if (StringUtils.isNotBlank(applyTime)) {
      if (StringUtils.isNotBlank(showDesc)) {
        showDesc += joinStr + applyTime;
      } else {
        showDesc += applyTime;
      }
    }
    return showDesc;
  }

  private String getFundShowDescByBr(String fundAgency, String scienceArea, String applyTime) {
    String showDesc = "";
    String joinStr = "[br]";
    if (StringUtils.isNotBlank(fundAgency)) {
      showDesc += fundAgency;
    }
    if (StringUtils.isNotBlank(scienceArea)) {
      if (StringUtils.isNotBlank(showDesc)) {
        showDesc += joinStr + scienceArea;
      } else {
        showDesc += scienceArea;
      }
    }
    if (StringUtils.isNotBlank(applyTime)) {
      if (StringUtils.isNotBlank(showDesc)) {
        showDesc += joinStr + applyTime;
      } else {
        showDesc += applyTime;
      }
    }
    return showDesc;
  }

  private ConstFundCategoryInfo buildFundInfo(ConstFundCategory fundCategory, ConstFundCategoryInfo fund,
      FundFindForm form) {
    String locale = form.getLocale();
    Long agencyId = fundCategory.getAgencyId();
    // 基金资助机构ID
    fund.setFundAgencyId(agencyId);
    // 基金ID
    Long fundId = fundCategory.getId();
    // ConstFundCategory constFundCategory = constFundCategoryDao.findFundName(fundId);
    fund.setFundId(fundId);
    if (fundId != null) {
      fund.setEncryptedFundId(Des3Utils.encodeToDes3(fundId.toString()));
    }
    // 基金标题
    fund.setZhTitle(fundCategory.getNameZh());
    fund.setEnTitle(fundCategory.getNameEn());
    fund.setFundTitle(LocaleTextUtils.getStrByLocale(locale, fund.getZhTitle(), fund.getEnTitle()));
    // 开始时间
    String startDate = null;
    if (fundCategory.getStartDate() != null) {
      startDate = fundCategory.getStartDate().toString();
    }
    fund.setStartDate(formateDate(startDate, ""));
    // 结束时间
    String endDate = null;
    if (fundCategory.getEndDate() != null) {
      endDate = fundCategory.getEndDate().toString();
    }
    fund.setEndDate(formateDate(endDate, ""));
    fund.setShowDate(this.dealNullVal(buildFundTime(fund.getStartDate(), fund.getEndDate())));
    if (fundCategory.getEndDate() != null) {
      Date now = new Date();
      if (!now.after(fundCategory.getEndDate())) {
        fund.setIsStaleDated(false);
      }
    }
    // 资助机构名称
    ConstFundAgency fundAgency = fundAgencyDao.getFundAgencyNameByAgencyId(agencyId);
    if (fundAgency != null) {
      fund.setZhAgencyName(fundAgency.getNameZh());
      fund.setEnAgencyName(fundAgency.getNameEn());
      fund.setFundAgencyName(LocaleTextUtils.getStrByLocale(locale, fund.getZhAgencyName(), fund.getEnAgencyName()));
    }
    // 科技领域 const_fund_category_discipline
    List<Long> ids = constFundCategoryDisDao.findFundDisciplineIds(fundId);
    if (CollectionUtils.isNotEmpty(ids)) {
      List<Integer> areaIds = new ArrayList<>();
      for (Long id : ids) {
        areaIds.add(Integer.parseInt(id.toString()));
      }
      List<CategoryMapBase> findCategorys = categoryMapBaseDao.findCategoryByIds(areaIds);
      List<String> zhScienceAreas = new ArrayList<>();
      List<String> enScienceAreas = new ArrayList<>();
      for (CategoryMapBase categoryMapBase : findCategorys) {
        String zhScienceArea = categoryMapBase.getCategoryZh();
        String enScienceArea = categoryMapBase.getCategoryEn();
        if (StringUtils.isNotBlank(zhScienceArea)) {
          zhScienceAreas.add(zhScienceArea);
          fund.setZhScienceArea(String.join(", ", zhScienceAreas));
        }
        if (StringUtils.isNotBlank(enScienceArea)) {
          enScienceAreas.add(enScienceArea);
          fund.setEnScienceArea(String.join(", ", enScienceAreas));
        }
        fund.setScienceAreas(LocaleTextUtils.getStrByLocale(locale, fund.getZhScienceArea(), fund.getEnScienceArea()));
      }
    }

    // 推荐分数 fund.setScore(ObjectUtils.toString(item.get("score")));
    // 根据基金ID获取一些其他信息 Long agencyId =
    // NumberUtils.toLong(ObjectUtils.toString(item.get("fundAgencyId")));
    // 不要在这里取图片了，加载会很慢。用另一个action来加载，/prjweb/fund/ajaxrecommendlogo
    // app用
    if ("true".equals(form.getFromAPP())) {
      if (agencyId != null) {
        String logoUrl = constFundAgencyDao.findFundAgencyLogoUrl(agencyId);
        if (StringUtils.isNotBlank(logoUrl) && logoUrl.contains("http")) {
          fund.setLogoUrl(logoUrl);
        } else {
          fund.setLogoUrl(domainScm + "/resmod" + (logoUrl == null ? "" : logoUrl));
        }
      }
    }
    // 统计数
    FundStatistics sta = fundStatisticsDao.findFundStatisticsById(fundId);
    if (sta != null) {
      fund.setAwardCount(sta.getAwardCount());
      fund.setShareCount(sta.getShareCount());
    } else {
      fund.setAwardCount(0);
      fund.setShareCount(0);
    }
    return fund;
  }

  // 构建起止时间
  private String buildFundTime(String start, String end) {
    if (StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end)) {
      return start + " ~ " + end;
    } else {
      return start + end;
    }
  }

  /**
   * 处理空值字符串，null的转为""
   * 
   * @param val
   * @return
   */
  private String dealNullVal(String val) {
    if (StringUtils.isBlank(val)) {
      return "";
    } else {
      return val;
    }
  }

  private String formateDate(String dateStr, String result) {
    try {
      if (StringUtils.isNotBlank(dateStr)) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd K:m:s", Locale.ENGLISH);
        Date date = sdf.parse(dateStr);
        if (date != null) {
          result = new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
      }
    } catch (Exception e) {
      logger.error("时间转换出错， dateStr = " + dateStr, e);
    }
    return result;
  }


  private boolean isBlankSelectCondition(FundFindForm form) {
    return StringUtils.isBlank(form.getRegionCodesSelect()) && StringUtils.isBlank(form.getScienceCodesSelect())
        && StringUtils.isBlank(form.getSearchKey().trim())
        && (StringUtils.isBlank(form.getSeniorityCodeSelect()) || "0".equals(form.getSeniorityCodeSelect()));
  }

  private Integer findPsnGrades(Long psnId) {
    Person psn = this.personProfileDao.findPsnDegreeAndPosition(psnId);
    Integer degree = -1;
    if (psn != null) {
      if (StringUtils.isNotBlank(psn.getPosition())) {
        ConstPosition pos = constPositionDao.getPosByName(psn.getPosition());
        if (pos != null) {
          degree = pos.getGrades();
        }
      }
      if (degree == -1 && StringUtils.isNotBlank(psn.getDegreeName())) {
        ConstPosition pos = constPositionDao.getPosByName(psn.getPosition());
        if (pos != null) {
          degree = pos.getGrades();
        }
      }
    }
    return degree;

  }

  @Override
  public List<ConstRegion> getFundRegion() {
    return constRegionDao.getChinaRegion(false);
  }

  @Override
  public void fundFindListForWeChat(FundFindForm form) throws PrjException {
    if (form.getPsnId() != null) {
      form = this.fundFindListSearch(form);
      if (form != null && CollectionUtils.isNotEmpty(form.getFundInfoList())) {
        List<FundWeChat> fundList = new ArrayList<FundWeChat>();
        for (ConstFundCategoryInfo info : form.getFundInfoList()) {
          FundWeChat fund = new FundWeChat();
          fund.setFundId(info.getFundId().toString());
          // 基金发现全部使用的是资助机构的id,所以在前台获取的都是资助机构的id
          if (com.smate.core.base.utils.number.NumberUtils.isNotNullOrZero(info.getFundAgencyId())) {
            fund.setDes3FundId(Des3Utils.encodeToDes3(info.getFundAgencyId().toString()));
          }
          fund.setFundAgencyId(info.getFundAgencyId());
          if (com.smate.core.base.utils.number.NumberUtils.isNotZero(info.getFundAgencyId())) {
            String logoUrl = constFundAgencyDao.findFundAgencyLogoUrl(info.getFundAgencyId());
            if (StringUtils.isNotBlank(logoUrl) && logoUrl.contains("http")) {
              fund.setLogoUrl(logoUrl);
            } else {
              fund.setLogoUrl(domainScm + "/resmod" + (logoUrl == null ? "" : logoUrl));
            }

          }
          fund.setEncryptedFundId(Des3Utils.encodeToDes3(info.getFundId().toString()));
          fund.setFundAgency(info.getFundAgencyName());
          fund.setFundName(StringEscapeUtils.unescapeHtml4(info.getFundTitle()));
          fund.setShowDate(this.dealNullVal(this.buildFundTime(info.getStartDate(), info.getEndDate())));
          fund.setAwardCount(info.getAwardCount());
          fund.setShareCount(info.getShareCount());
          fund.setHasAward(info.getHasAward());
          fund.setHasCollected(info.getHasCollected());
          fund.setZhTitle(info.getZhTitle());
          fund.setEnTitle(info.getEnTitle());
          fund.setZhShowDesc(info.getZhShowDesc());
          fund.setEnShowDesc(info.getEnShowDesc());
          fund.setZhShowDescBr(info.getZhShowDescBr());
          fund.setEnShowDescBr(info.getEnShowDescBr());
          fundList.add(fund);
        }
        form.setResultList(fundList);
      }
    }

  }

  // 查找子地区的上一级地区
  @Override
  public String findSuperRegion(String searchRegion, String locale) throws PrjException {
    Long regionId = constRegionDao.getRegionIdByCityName(searchRegion);
    ConstRegion SuperRegion = null;
    if (com.smate.core.base.utils.number.NumberUtils.isNotZero(regionId)) {
      SuperRegion = constRegionDao.findSuperRegionById(regionId);
    }
    if (SuperRegion != null) {
      if ("en_US".equalsIgnoreCase(locale)) {
        return SuperRegion.getEnName();
      } else {
        return SuperRegion.getZhName();
      }
    }
    return null;
  }

}
