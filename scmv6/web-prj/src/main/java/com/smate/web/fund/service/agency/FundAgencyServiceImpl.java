package com.smate.web.fund.service.agency;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.email.service.MailInitDataService;
import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.common.BeanUtils;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.common.LocaleTextUtils;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.fund.agency.dao.AgencyStatisticsDao;
import com.smate.web.fund.agency.dao.CategoryScmDao;
import com.smate.web.fund.agency.dao.FundAgencyAwardDao;
import com.smate.web.fund.agency.dao.FundAgencyDao;
import com.smate.web.fund.agency.dao.FundAgencyInterestDao;
import com.smate.web.fund.agency.dao.FundAgencyShareDao;
import com.smate.web.fund.agency.dto.FundAgencyDTO;
import com.smate.web.fund.agency.model.AgencyStatistics;
import com.smate.web.fund.agency.model.FundAgencyAward;
import com.smate.web.fund.agency.model.FundAgencyForm;
import com.smate.web.fund.agency.model.FundAgencyInterest;
import com.smate.web.fund.agency.model.FundAgencyShare;
import com.smate.web.fund.dao.wechat.ConstFundCategoryDao;
import com.smate.web.fund.dao.wechat.ConstFundCategoryRcmdDao;
import com.smate.web.fund.model.common.CategoryScm;
import com.smate.web.fund.model.common.ConstFundAgency;
import com.smate.web.fund.model.common.ConstFundCategory;
import com.smate.web.fund.model.common.ConstFundCategoryInfo;
import com.smate.web.fund.recommend.dao.ConstFundAgencyDao;
import com.smate.web.fund.recommend.dao.FundAwardDao;
import com.smate.web.fund.recommend.dao.FundStatisticsDao;
import com.smate.web.fund.recommend.dao.MyFundDao;
import com.smate.web.fund.recommend.model.FundAward;
import com.smate.web.fund.recommend.model.FundStatistics;
import com.smate.web.fund.recommend.rcmd.dao.ConstFundCategoryDisDao;
import com.smate.web.fund.service.recommend.FundRecommendService;
import com.smate.web.prj.consts.FundAgencyConst;

/**
 * 基金页面
 * 
 * @author Administrator
 *
 */
@Service("fundAgencyService")
@Transactional(rollbackFor = Exception.class)
public class FundAgencyServiceImpl implements FundAgencyService {
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  @Autowired
  private FundAgencyDao fundAgencyDao;// 机构dao
  @Autowired
  private ConstFundCategoryRcmdDao constFundCategoryRcmdDao;// 基金dao
  @Autowired
  private CacheService cacheService;
  @Autowired
  private ConstFundCategoryDisDao constFundCategoryDisDao;// 基金和领域dao
  @Autowired
  private CategoryScmDao categoryScmDao;// 科技领域
  @Autowired
  private FundAwardDao fundAwardDao;
  @Autowired
  private MyFundDao myFundDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;
  @Autowired
  private FundStatisticsDao fundStatisticsDao;
  // 缓存机构地区的统计数
  static final String FUND_REGIONCOUNT_CACHE = "fund_regionCount_cache";
  @Autowired
  private FundAgencyAwardDao fundAgencyAwardDao;
  @Autowired
  private AgencyStatisticsDao agencyStatisticsDao;
  @Autowired
  private FundAgencyShareDao fundAgencyShareDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private FundAgencyInterestDao fundAgencyInterestDao;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String openResfulUrl;
  @Autowired
  private FundRecommendService fundRecommendService;
  @Autowired
  private MailInitDataService mailInitDataService;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private ConstFundCategoryDao constFundCategoryDao;
  @Autowired
  private UserDao userDao;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;
  @Autowired
  private SolrIndexService solrIndexService;

  @Override
  public void getFundAgency(FundAgencyForm form) throws Exception {
    ConstFundAgency fundAgency = fundAgencyDao.get(form.getFundAgencyId());
    if (fundAgency != null) {
      if (StringUtils.isNotEmpty(fundAgency.getLogoUrl()) && !fundAgency.getLogoUrl().contains("http")) {
        form.setLogoUrl("/resmod" + fundAgency.getLogoUrl());// 不能直接设置fundAgency的logoUrl,因为hibernate会直接保存数据库,导致更改视图报错
      } else {
        form.setLogoUrl(fundAgency.getLogoUrl());
      }
      Long count = fundAgencyDao.getOneFundAllAgencyCount(form.getFundAgencyId());
      fundAgency.setFundCount(count == null ? 0 : count);// 获取全部基金机会
    }
    form.setFund(fundAgency);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void getFundList(FundAgencyForm form) throws Exception {
    List<Long> allSubIdList = new ArrayList<Long>();
    form.setDisIdStr(HtmlUtils.htmlUnescape(form.getDisIdStr()));
    // 把包括选中的一级科技领域的所有子领域查出来
    if (JacksonUtils.isJsonString10(form.getDisIdStr())) {
      List<Integer> disIdList = JacksonUtils.jsonToList(form.getDisIdStr());
      if (CollectionUtils.isNotEmpty(disIdList) && disIdList.get(0) != 0) {
        for (Integer disId : disIdList) {
          allSubIdList.addAll(categoryScmDao.getSubCategoryIdList(disId.longValue()));// 添加子领域
          allSubIdList.add(disId.longValue());
        }
      } else if (disIdList.size() > 0 && disIdList.get(0) == 0) {
        allSubIdList.add(0L);
      }
    }
    List<ConstFundCategory> fundCategoryList;
    if (StringUtils.isNoneBlank(form.getSearchKey())) {
      fundCategoryList = constFundCategoryRcmdDao.findConstFundCategory(form.getFundAgencyId(), allSubIdList,
          form.getSearchKey(), form.getInsType(), form.getPage());
    } else {
      fundCategoryList = constFundCategoryRcmdDao.findConstFundCategory(form.getFundAgencyId(), allSubIdList,
          form.getInsType(), form.getPage());
    }
    List<ConstFundCategoryInfo> fundInfoList = buiFundCategoryInfo(fundCategoryList, form);// 构建显示信息
    form.getPage().setResult(fundInfoList);
  }

  /*
   * 构建显示信息
   */
  private List<ConstFundCategoryInfo> buiFundCategoryInfo(List<ConstFundCategory> fundCategoryList,
      FundAgencyForm form) {
    List<ConstFundCategoryInfo> fundInfoList = new ArrayList<ConstFundCategoryInfo>();
    ConstFundAgency fundAgency = fundAgencyDao.get(form.getFundAgencyId());
    if (CollectionUtils.isNotEmpty(fundCategoryList)) {
      StringBuffer des3FundIds = new StringBuffer();// 拼接所有的基金的资助机构id,用于获取基金logo
      for (ConstFundCategory fund : fundCategoryList) {
        des3FundIds.append(Des3Utils.encodeToDes3(String.valueOf(fund.getAgencyId())) + ",");
        Locale locale = LocaleContextHolder.getLocale();
        ConstFundCategoryInfo fundInfo = new ConstFundCategoryInfo();
        fundInfo.setFundId(fund.getId());
        fundInfo.setEncryptedFundId(Des3Utils.encodeToDes3(fund.getId().toString()));
        fundInfo.setFundAgencyId(fund.getAgencyId());
        /*
         * String angencyName = LocaleTextUtils.getLocaleText(locale,
         * Objects.toString(fundAgency.getNameZh()), Objects.toString(fundAgency.getNameEn()));
         */
        fundInfo.setFundAgencyName(fundAgency.getNameView());// 机构名称
        fundInfo.setLogoUrl(form.getLogoUrl());// 机构图片
        fundInfo.setFundTitle(
            StringEscapeUtils.unescapeHtml4(LocaleTextUtils.getLocaleText(locale, fund.getNameZh(), fund.getNameEn())));// 显示基金的名称
        // 基金标题
        fundInfo.setZhTitle(ObjectUtils.toString(fund.getNameZh()));
        fundInfo.setEnTitle(ObjectUtils.toString(fund.getNameEn()));
        fundInfo.setShowDate(getFundShowDate(fund.getStartDate(), fund.getEndDate()));// 格式显示截止时间
        if (fund.getEndDate() != null) {
          Date now = new Date();
          if (!now.after(fund.getEndDate())) {
            fundInfo.setIsStaleDated(false);
          }
        }
        String disAllName = getDisAllName(locale, fund.getId());
        fundInfo.setDisAllName(disAllName);// 显示科技领域名称
        // 中文机构名称
        String angencyNameZh =
            LocaleTextUtils.getLocaleText(new Locale("zh_CN"), fundAgency.getNameZh(), fundAgency.getNameEn());
        // 英文机构名称
        String angencyNameEn =
            LocaleTextUtils.getLocaleText(new Locale("en_US"), fundAgency.getNameZh(), fundAgency.getNameEn());
        String disAllNameZh = getDisAllName(new Locale("zh_CN"), fund.getId());
        String disAllNameEn = getDisAllName(new Locale("en_US"), fund.getId());
        // 构建基金描述用与分享等
        fundInfo
            .setZhShowDesc(this.getFundShowDescByLocale(angencyNameZh, disAllNameZh, fundInfo.getShowDate(), "zh_CN"));
        fundInfo
            .setEnShowDesc(this.getFundShowDescByLocale(angencyNameEn, disAllNameEn, fundInfo.getShowDate(), "en_US"));
        fundInfo.setZhShowDescBr(this.getFundShowDescByBr(angencyNameZh, disAllNameZh, fundInfo.getShowDate()));
        fundInfo.setEnShowDescBr(this.getFundShowDescByBr(angencyNameEn, disAllNameEn, fundInfo.getShowDate()));
        awardAndSave(fundInfo, form.getPsnId());
        fundInfoList.add(fundInfo);
      }
      form.setDes3FundIds(subDes3FundIds(des3FundIds));
    }
    return fundInfoList;
  }

  /**
   * 对拼接后的加密基金id做截取
   * 
   * @param stringBuffer
   * @return
   */
  public String subDes3FundIds(StringBuffer stringBuffer) {
    if (Objects.nonNull(stringBuffer) && stringBuffer.length() > 0) {
      StringBuffer newStr = new StringBuffer();
      newStr.append(",").append(stringBuffer.toString());
      return StringUtils.strip(newStr.toString(), ",").trim();
    }
    return "";
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

  private void awardAndSave(ConstFundCategoryInfo fund, Long psnId) {
    if (NumberUtils.isNotNullOrZero(psnId)) {
      // 统计数
      FundStatistics sta = fundStatisticsDao.findFundStatisticsById(fund.getFundId());
      if (sta != null) {
        fund.setAwardCount(NumberUtils.isZero(sta.getAwardCount()) ? null : sta.getAwardCount());
        fund.setShareCount(NumberUtils.isZero(sta.getShareCount()) ? null : sta.getShareCount());
      }
      // 查找已收藏过的基金
      List<Long> collectIds = myFundDao.findCollectFundIds(psnId);
      // 是否已收藏过
      if (CollectionUtils.isNotEmpty(collectIds) && collectIds.contains(fund.getFundId())) {
        fund.setHasCollected(true);
      }
      // 是否赞过
      FundAward awardRecord = fundAwardDao.findFundAwardRecord(psnId, fund.getFundId());
      if (awardRecord != null && awardRecord.getStatus() == LikeStatusEnum.LIKE) {
        fund.setHasAward(true);
      }
    }
  }

  /**
   * 获取科技领域的的名称（包括父领域）
   * 
   * @param locale
   * @param fundId
   * @param disId
   * @return
   */
  @SuppressWarnings("unchecked")
  private String getDisAllName(Locale locale, Long fundId) {
    String disName = "";
    try {
      List<Long> disIdList = constFundCategoryDisDao.findFundDisciplineIds(fundId);
      if (CollectionUtils.isNotEmpty(disIdList)) {
        List<CategoryScm> disMapList = categoryScmDao.findCategoryScm(disIdList);
        if (CollectionUtils.isNotEmpty(disMapList)) {
          for (CategoryScm dis : disMapList) {
            disName += ", " + LocaleTextUtils.getLocaleText(locale, Objects.toString(dis.getCategoryZh()),
                Objects.toString(dis.getCategoryEn()));
          }
          disName = disName.replaceFirst(",", "");
        }
      }
    } catch (Exception e) {
      logger.error("获取基金科技领域名称出错 fundId=" + fundId, e);
    }
    return disName;
  }

  private String getFundShowDate(Date starDate, Date endDate) {
    String showDate = "";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    if (starDate != null) {
      showDate = sdf.format(starDate);
      if (endDate != null) {
        showDate += "~" + sdf.format(endDate);
      }
    } else {
      if (endDate != null) {
        showDate += sdf.format(endDate);
      }
    }
    return showDate;
  }

  @Override
  public void getDetailLeftCondition(FundAgencyForm form) throws Exception {
    form.setDisciplineList(categoryScmDao.findFirstCategoryScm());
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Map<String, String>> getDetailLeftCount(String disIdStr, Long fundAgencyId) throws Exception {
    List<Integer> disIdList = JacksonUtils.jsonToList(disIdStr);
    List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
    for (Integer disId : disIdList) {// 查询一级研究领域的所有包括子领域基金统计数
      if (disId != 0) {
        Map<String, String> map = new HashMap<String, String>();
        List<Long> allSubIdList = new ArrayList<Long>();
        allSubIdList = categoryScmDao.getSubCategoryIdList(disId.longValue());
        allSubIdList.add(disId.longValue());
        Long count = constFundCategoryRcmdDao.getfundCountByDisId(allSubIdList, fundAgencyId);
        map.put("disId", disId.toString());
        map.put("count", count.toString());
        resultList.add(map);
      }
    }
    Long otherDisSum = constFundCategoryRcmdDao.getNoDisFundCount(fundAgencyId);// 没有科技领域的基金数量
    Long typeSum1 = constFundCategoryRcmdDao.getfundCountByInsType("1", fundAgencyId);// 企业基金基金数量
    Long typeSum2 = constFundCategoryRcmdDao.getfundCountByInsType("2", fundAgencyId);// 科研机构基金数量
    Long typeSum3 = constFundCategoryRcmdDao.getfundCountNotInsType(fundAgencyId);// 没有单位要求的基金数量
    resultList.add(getCountMap("0", otherDisSum.toString()));
    resultList.add(getCountMap("insType1", typeSum1.toString()));
    resultList.add(getCountMap("insType2", typeSum2.toString()));
    resultList.add(getCountMap("insType0", typeSum3.toString()));

    return resultList;
  }

  private Map<String, String> getCountMap(String disId, String count) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("disId", disId);
    map.put("count", count);
    return map;
  }

  /**
   * 获取机构基金列表 以及基金数
   */
  @Override
  public List<ConstFundAgency> getFundAgencyList(List<Long> regionId, Page page) throws Exception {
    // 查询下级地区
    if (CollectionUtils.isNotEmpty(regionId) && !regionId.contains(156L)) {
      List<Long> subRegionIds = constRegionDao.findSubRegionIdBySuperRegionId(regionId);
      regionId.addAll(subRegionIds);
    }
    List<ConstFundAgency> fundAgencyList = fundAgencyDao.getFundAgencyList(regionId, page);
    List<Long> ids = new ArrayList<Long>();
    String language = LocaleContextHolder.getLocale().toString();
    if ("zh_CN".equals(language)) {
      for (ConstFundAgency constFundAgency : fundAgencyList) {
        ids.add(constFundAgency.getId());
        constFundAgency.setNameView(constFundAgency.getNameZh());
        constFundAgency.setViewAddress(constFundAgency.getAddress());
      }
    } else {
      for (ConstFundAgency constFundAgency : fundAgencyList) {
        ids.add(constFundAgency.getId());
        constFundAgency.setNameView(constFundAgency.getNameEn());
        constFundAgency.setViewAddress(constFundAgency.getEnAddress());
      }
    }
    List<Map<String, Long>> fundCategoryList = fundAgencyDao.getFundAgencyCount(regionId, ids);
    for (ConstFundAgency constFundAgency : fundAgencyList) {
      for (Map<String, Long> map : fundCategoryList) {
        if (constFundAgency.getId().equals(map.get("id"))) {
          constFundAgency.setFundCount(map.get("count"));
        }
      }
    }
    page.setResult(fundAgencyList);
    return fundAgencyList;
  }

  /**
   * 获取机构基金列表
   * 
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   */
  @Override
  public List<FundAgencyDTO> findFundAgencyList(FundAgencyForm form) throws Exception {
    List<FundAgencyDTO> fundAgencyDTOs = new ArrayList<>();
    String regionAgency = form.getRegionAgency();
    List<Long> regionIds = new ArrayList<Long>();
    if (StringUtils.isNotBlank(regionAgency)) {
      String[] ids = regionAgency.split(",");
      for (String id : ids) {
        regionIds.add(NumberUtils.toLong(id));
      }
    }
    // 查询下级地区
    if (CollectionUtils.isNotEmpty(regionIds) && !regionIds.contains(156L)) {
      List<Long> subRegionIds = constRegionDao.findSubRegionIdBySuperRegionId(regionIds);
      regionIds.addAll(subRegionIds);
    }
    List<ConstFundAgency> fundAgencyList;
    if (com.smate.core.base.utils.string.StringUtils.isNoneBlank(form.getSearchKey())) {
      fundAgencyList = fundAgencyDao.findFundAgencyList(regionIds, form.getSearchKey(), form.getPage());
    } else {
      fundAgencyList = fundAgencyDao.getFundAgencyList(regionIds, form.getPage());
    }
    if (fundAgencyList == null) {
      return null;
    }
    List<Long> ids = new ArrayList<Long>();
    for (ConstFundAgency constFundAgency : fundAgencyList) {
      ids.add(constFundAgency.getId());
      constFundAgency.setNameView(StringEscapeUtils
          .unescapeHtml4(StringUtils.isNotBlank(constFundAgency.getNameZh()) ? constFundAgency.getNameZh()
              : constFundAgency.getNameEn()));
      constFundAgency.setViewAddress(StringUtils.isNotBlank(constFundAgency.getAddress()) ? constFundAgency.getAddress()
          : constFundAgency.getEnAddress());
    }
    List<Map<String, Long>> fundCategoryList = fundAgencyDao.getFundAgencyCount(regionIds, ids);
    for (ConstFundAgency constFundAgency : fundAgencyList) {
      for (Map<String, Long> map : fundCategoryList) {
        if (constFundAgency.getId().equals(map.get("id"))) {
          constFundAgency.setFundCount(map.get("count"));
        }
      }
    }
    // 初始化
    List<Map<String, Object>> initInfo = new ArrayList<Map<String, Object>>();
    Long psnId = form.getPsnId();
    // 构建资助机构操作相关信息
    this.buildAgencyOptInfo(ids, initInfo, psnId);
    // 把加密的id值转成未加密的
    initInfo.forEach(i -> i.put("id", NumberUtils.toLong(Des3Utils.decodeFromDes3(i.get("id").toString()))));
    for (int i = 0; i < fundAgencyList.size(); i++) {
      FundAgencyDTO fundAgencyDTO = new FundAgencyDTO();
      BeanUtils.copyProperties(fundAgencyDTO, fundAgencyList.get(i));
      BeanUtils.copyProperties(fundAgencyDTO, initInfo.get(i));
      fundAgencyDTOs.add(fundAgencyDTO);
    }
    return fundAgencyDTOs;
  }

  /**
   * 获取地区机构数
   */

  @Override
  public String getAllFundAgencyCount() throws Exception {
    Long[] regionIds = {156L, 360000L, 370000L, 410000L, 420000L, 430000L, 440000L, 450000L, 460000L, 500000L, 510000L,
        520000L, 530000L, 540000L, 610000L, 620000L, 630000L, 640000L, 650000L, 158L, 344L, 446L, 110000L, 120000L,
        130000L, 140000L, 150000L, 210000L, 220000L, 230000L, 310000L, 320000L, 330000L, 340000L, 350000L};
    List<Map<String, String>> regionIdsCount = new ArrayList<Map<String, String>>();
    for (Long regionId : regionIds) {
      Long regionCount = getRecommendPsnCache(regionId);
      Map<String, String> map = new HashMap<String, String>();
      if (regionCount != null) {
        map.put(regionId.toString(), regionCount.toString());

      } else {
        List<Long> superAndSubIds = new ArrayList<Long>();
        superAndSubIds.add(regionId);
        if (regionId != 156L) {
          // 获取下一级的地区
          List<Long> subRegionIds = constRegionDao.findSubRegionIds(regionId);
          if (CollectionUtils.isNotEmpty(subRegionIds)) {
            superAndSubIds.addAll(subRegionIds);
          }
        }
        Long count = 0L;
        for (Long id : superAndSubIds) {
          count += fundAgencyDao.getAllAgencyCount(id);
        }
        if (count != null) {
          putRecommendPsnCache(regionId, count);
          map.put(regionId.toString(), count.toString());
        }
        /* map.put("count", count); */
      }

      regionIdsCount.add(map);
    }

    String count = JacksonUtils.listToJsonStr(regionIdsCount);
    count = count.replace("{", "");
    count = count.replace("}", "");
    count = count.replace("]", "}");
    count = count.replace("[", "{");
    count = "{" + "\"regionAgency\":" + count + "}";
    return count;
  }

  @SuppressWarnings("unchecked")
  private Long getRecommendPsnCache(Long regionId) {
    // SCM-16289
    return (Long) cacheService.get(FUND_REGIONCOUNT_CACHE, regionId.toString());
  }

  @SuppressWarnings("rawtypes")
  private void putRecommendPsnCache(Long regionId, Long count) {
    // SCM-16289
    cacheService.put(FUND_REGIONCOUNT_CACHE, CacheService.EXP_HOUR_1, regionId.toString(), count);
  }

  @Override
  public List<ConstRegion> getFundRegion() throws Exception {
    return constRegionDao.getChinaRegion(true);
  }

  @Override
  public void buildFundMapBaseInfo(FundAgencyForm form) throws Exception {
    List<FundAgencyInterest> agencyInterestList = fundRecommendService.getPsnFundAgencyInterestList(form.getPsnId());
    form.setSelectFundAgencyList(agencyInterestList);
    String agencyCodes = getDefultRegion(agencyInterestList);// 选中的科技领域

    List<Map<String, Object>> allData = new LinkedList<Map<String, Object>>();
    // 一级地区
    List<ConstRegion> firstLevelList = new ArrayList<ConstRegion>();// 存一级的
    ConstRegion country = new ConstRegion();
    country.setId(0L);// 国家级的放进去
    firstLevelList.add(country);
    firstLevelList.addAll(constRegionDao.getChinaRegion(false));
    ConstRegion other = new ConstRegion();
    other.setId(1L);// 其他
    firstLevelList.add(other);

    List<ConstFundAgency> subLevelList = new ArrayList<ConstFundAgency>();// 存二级的
    String language = LocaleContextHolder.getLocale().toString();
    for (ConstRegion region : firstLevelList) {
      Integer selectCount = 0;
      Map<String, Object> subDataDetail = new HashMap<String, Object>();
      if (region.getId() == 0) {
        subDataDetail.put("regionName", LocaleTextUtils.getStrByLocale(language, "国家", "Country"));
        subLevelList = getCountryAgency();// 国家级资助机构
      } else if (region.getId() == 1) {
        subDataDetail.put("regionName", LocaleTextUtils.getStrByLocale(language, "其他", "Other"));
        subLevelList = getOtherAgency();// 其他资助机构
      } else {
        subDataDetail.put("regionName",
            LocaleTextUtils.getStrByLocale(language, region.getZhName(), region.getEnName()));
        // 二级资助机构
        List<Long> regionList = new ArrayList<Long>();
        regionList.add(region.getId());
        subLevelList = getFundAgencyListByRegionId(regionList);

      }
      // 检查人员已选资助机构
      if (StringUtils.isNotBlank(agencyCodes)) {
        for (ConstFundAgency fund : subLevelList) {
          if (agencyCodes.contains(fund.getId().toString())) {
            fund.setSelectStatus(1);
            selectCount++;
          }
        }
      }

      subDataDetail.put("regionId", region.getId());
      subDataDetail.put("selectCount", selectCount);
      subDataDetail.put("agencyList", subLevelList);
      allData.add(subDataDetail);
    }

    form.setFundAgencyMapList(allData);
  }

  /**
   * 拼接设置的地区code
   * 
   * @param areaList
   * @return
   */
  private String getDefultRegion(List<FundAgencyInterest> agencyInterestList) {
    return Optional.ofNullable(agencyInterestList).map(list -> list.stream().map(FundAgencyInterest::getAgencyId)
        .map(Objects::toString).collect(Collectors.joining(","))).orElseGet(() -> "");
  }

  /**
   * 排序选中的资助机构
   * 
   * @param agencyCodes
   * @param selectFund
   * @return
   */
  private List<ConstFundAgency> getSortAreaList(String agencyCodes, List<ConstFundAgency> selectFund) {
    List<ConstFundAgency> sortAreaList = new ArrayList<ConstFundAgency>();
    if (StringUtils.isNotBlank(agencyCodes)) {
      String[] selectIds = agencyCodes.split(",");
      for (String id : selectIds) {// 排序选中的
        for (ConstFundAgency fund : selectFund) {
          if (id.equals(fund.getId().toString())) {
            sortAreaList.add(fund);
          }
        }
      }
    }
    return sortAreaList;
  }

  /**
   * 获取国家级资助机构
   * 
   * @return
   */
  public List<ConstFundAgency> getCountryAgency() {
    List<ConstFundAgency> fundAgencyList = fundAgencyDao.getCountryFundAgencyList();
    String language = LocaleContextHolder.getLocale().toString();
    for (ConstFundAgency constFundAgency : fundAgencyList) {
      constFundAgency.setNameView(
          LocaleTextUtils.getStrByLocale(language, constFundAgency.getNameZh(), constFundAgency.getNameEn()));
    }
    return fundAgencyList;
  }

  /**
   * 获取其他的级资助机构
   * 
   * @return
   */
  public List<ConstFundAgency> getOtherAgency() {
    List<ConstFundAgency> fundAgencyList = fundAgencyDao.getOtherFundAgencyList();
    String language = LocaleContextHolder.getLocale().toString();
    for (ConstFundAgency constFundAgency : fundAgencyList) {
      constFundAgency.setNameView(
          LocaleTextUtils.getStrByLocale(language, constFundAgency.getNameZh(), constFundAgency.getNameEn()));
    }
    return fundAgencyList;
  }

  public List<ConstFundAgency> getFundAgencyListByRegionId(List<Long> regionId) throws Exception {
    // 查询下级地区
    if (CollectionUtils.isNotEmpty(regionId) && !regionId.contains(156L)) {
      List<Long> subRegionIds = constRegionDao.findSubRegionIdBySuperRegionId(regionId);
      regionId.addAll(subRegionIds);
    }
    List<ConstFundAgency> fundAgencyList = fundAgencyDao.getFundAgencyListByRegionIds(regionId);
    List<Long> ids = new ArrayList<Long>();
    String language = LocaleContextHolder.getLocale().toString();
    for (ConstFundAgency constFundAgency : fundAgencyList) {
      ids.add(constFundAgency.getId());
      constFundAgency.setNameView(
          LocaleTextUtils.getStrByLocale(language, constFundAgency.getNameZh(), constFundAgency.getNameEn()));
      constFundAgency.setViewAddress(constFundAgency.getAddress());
    }
    return fundAgencyList;
  }

  @Override
  public String dealWithAwardOpt(Long psnId, Long agencyId, Integer optType) throws ServiceException {
    String checkResult = checkAwardOptParam(psnId, agencyId, optType);
    if (StringUtils.isBlank(checkResult)) {
      // 更新赞记录
      FundAgencyAward award = fundAgencyAwardDao.findAgencyAwardByPsnIdAndAgencyId(psnId, agencyId);
      if (award == null) {
        award = new FundAgencyAward();
        award.setCreateDate(new Date());
        award.setPsnId(psnId);
        award.setAgencyId(agencyId);
      } else if (CommonUtils.compareIntegerValue(award.getStatus(), optType)) {
        checkResult = "repetition operators";
      }
      award.setUpdateDate(new Date());
      award.setStatus(
          CommonUtils.compareIntegerValue(optType, FundAgencyConst.AWARD_AGENCY_OPT) ? FundAgencyConst.AWARD_AGENCY_OPT
              : FundAgencyConst.CANCEL_AWARD_AGENCY_OPT);
      fundAgencyAwardDao.save(award);
    }
    return checkResult;
  }

  @Override
  public AgencyStatistics updateAgencyStatistics(Long agencyId, boolean updateShareSum, boolean updateAwardSum,
      boolean updateInterestSum, Integer addOrReduce, Integer updateNum) throws ServiceException {
    AgencyStatistics stat = null;
    if (!NumberUtils.isNullOrZero(agencyId)) {
      // 先确定该资助机构是否存在
      ConstFundAgency agency = fundAgencyDao.get(agencyId);
      if (agency != null) {
        stat = agencyStatisticsDao.get(agencyId);
        if (stat == null) {
          stat = new AgencyStatistics();
          stat.setAgencyId(agencyId);
        }
        boolean isAdd = CommonUtils.compareIntegerValue(addOrReduce, FundAgencyConst.ADD_AGENCY_STATISTICS);
        // 更新分享数
        if (updateShareSum) {
          Long shareCount = stat.getShareSum() == null ? 0 : stat.getShareSum();
          shareCount = isAdd ? shareCount + updateNum : shareCount - updateNum;
          stat.setShareSum(shareCount > 0 ? shareCount : 0);
        }
        // 更新赞数量
        if (updateAwardSum) {
          Long awardCount = stat.getAwardSum() == null ? 0 : stat.getAwardSum();
          awardCount = isAdd ? awardCount + updateNum : awardCount - updateNum;
          stat.setAwardSum(awardCount > 0 ? awardCount : 0);
        }
        // 更新关注数
        if (updateInterestSum) {
          Long interestCount = stat.getInterestSum() == null ? 0 : stat.getInterestSum();
          interestCount = isAdd ? interestCount + updateNum : interestCount - updateNum;
          stat.setInterestSum(interestCount > 0 ? interestCount : 0);
        }
        agencyStatisticsDao.save(stat);
      }
    }
    return stat;
  }

  @Override
  public void dealWithShareOpt(FundAgencyForm form) throws ServiceException {
    if (checkShareParams(form)) {
      Long currentLoginPsnId = form.getCurrentPsnId();
      // 分享给好友
      if (CommonUtils.compareIntegerValue(form.getShareToPlatform(), FundAgencyConst.SHARE_TO_FRIEND)) {
        for (Long psnId : form.getPsnIds()) {
          FundAgencyShare share = new FundAgencyShare();
          share.setAgencyId(form.getFundAgencyId());
          share.setSharePsnId(currentLoginPsnId);
          share.setShareToPlatform(form.getShareToPlatform());
          share.setComments(form.getComments());
          share.setReceivePsnId(psnId);
          share.setCreateDate(new Date());
          fundAgencyShareDao.save(share);
        }
      } else {
        FundAgencyShare share = new FundAgencyShare();
        share.setAgencyId(form.getFundAgencyId());
        share.setSharePsnId(currentLoginPsnId);
        share.setShareToPlatform(form.getShareToPlatform());
        share.setComments(form.getComments());
        share.setCreateDate(new Date());
        share.setGroupId(NumberUtils.parseLong(Des3Utils.decodeFromDes3(form.getDes3GroupId()), 0L));
        fundAgencyShareDao.save(share);
      }
    }
  }

  // 检查分享操作的参数
  protected boolean checkShareParams(FundAgencyForm form) {
    // 检查当前登录的人员ID
    if (NumberUtils.isNullOrZero(form.getCurrentPsnId())) {
      form.setErrorMsg("currentPsnId is null");
      return false;
    }
    // 暂时资助机构的应该只能一个一个分享的
    if (NumberUtils.isNullOrZero(form.getFundAgencyId())) {
      if (StringUtils.isBlank(form.getDes3AgencyIds())) {
        form.setErrorMsg("agencyId is null");
        return false;
      } else {
        form.setFundAgencyId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3AgencyIds()), 0L));
      }
    }
    if (!existsAgency(form.getFundAgencyId())) {
      form.setErrorMsg("agency is not exists");
      return false;
    }
    // 分享的类型是否正确
    Integer shareToPlatform = form.getShareToPlatform();
    if (shareToPlatform == null
        || (shareToPlatform != null && !FundAgencyConst.SHARE_TYPE_LIST.contains(shareToPlatform))) {
      form.setErrorMsg("shareToPlatform is error");
      return false;
    }
    // 如果是分享给好友，要检查接收者
    if (CommonUtils.compareIntegerValue(form.getShareToPlatform(), FundAgencyConst.SHARE_TO_FRIEND)) {
      // 接收者为空
      if (StringUtils.isBlank(form.getDes3ReceiverIds()) && StringUtils.isBlank(form.getReceiverMails())) {
        form.setErrorMsg("share to friend need receivePsnIds or emails");
        return false;
      }
      // 接收者不为空，检查是否存在该人员
      String[] des3PsnIds = form.getDes3ReceiverIds().split(",");
      List<Long> psnIds = new ArrayList<Long>();
      for (String des3PsnId : des3PsnIds) {
        String psnIdStr = Des3Utils.decodeFromDes3(des3PsnId);
        if (StringUtils.isNotBlank(psnIdStr)) {
          psnIds.add(NumberUtils.toLong(psnIdStr));
        }
      }
      if (CollectionUtils.isNotEmpty(psnIds)) {
        form.setPsnIds(personDao.findExistsPsnIds(psnIds));
      }
      if (CollectionUtils.isEmpty(form.getPsnIds()) && StringUtils.isBlank(form.getReceiverMails())) {
        form.setErrorMsg("share to friend need correct receivePsnIds");
      }
    }
    // 如果是分享到群组，要检查群组ID
    if (CommonUtils.compareIntegerValue(form.getShareToPlatform(), FundAgencyConst.SHARE_TO_GROUP)
        && StringUtils.isBlank(form.getDes3GroupId())) {
      // TODO 校验群组ID是否是当前人员所在的群组
      form.setErrorMsg("share to group need groupId");
      return false;
    }
    return true;
  }

  // 校验赞操作参数
  private String checkAwardOptParam(Long psnId, Long agencyId, Integer optType) {
    // 当前登录操作人员ID不能为空
    if (NumberUtils.isNullOrZero(psnId)) {
      return "currentPsnId is null";
    }
    // 资助机构ID不能为空
    if (!existsAgency(agencyId)) {
      return "agencyId is null or not exists";
    }
    // 操作类型不正确
    if (!FundAgencyConst.AWARD_OPT_LIST.contains(optType)) {
      return "award operate type is error";
    }
    return "";
  }

  // 校验关注操作参数
  private String checkInterestOptParam(Long psnId, Long agencyId, Integer optType) {
    // 当前登录操作人员ID不能为空
    if (NumberUtils.isNullOrZero(psnId)) {
      return "currentPsnId is null";
    }
    // 资助机构ID不能为空
    if (!existsAgency(agencyId)) {
      return "agencyId is null or not exists";
    }
    // 操作类型不正确
    if (!FundAgencyConst.INTEREST_OPT_LIST.contains(optType)) {
      return "interest operate type is error";
    }
    Integer interestNum = fundAgencyInterestDao.getPsnFundAgencyInteresNum(psnId);
    // 是否已达到10个资助机构
    if (interestNum >= 10 && CommonUtils.compareIntegerValue(1, optType)) {
      return "interest agency has reached the maximum";
    }
    // 至少关注一个资助机构
    if (interestNum == 1 && CommonUtils.compareIntegerValue(0, optType)) {
      return "interested in at least one funding agency";
    }
    boolean hasInterestedAgency = fundAgencyInterestDao.hasInterestedAgency(psnId, agencyId);
    // 取消关注资助机构要已关注资助机构
    if (!hasInterestedAgency && CommonUtils.compareIntegerValue(0, optType)) {
      return "has not interested this agency";
    }
    // 是否重复关注
    if (hasInterestedAgency && CommonUtils.compareIntegerValue(1, optType)) {
      return "has interested this agency";
    }
    return "";
  }

  @Override
  public boolean existsAgency(Long agencyId) throws ServiceException {
    if (!NumberUtils.isNullOrZero(agencyId)) {
      ConstFundAgency agency = fundAgencyDao.get(agencyId);
      if (agency != null) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String dealWithInterestOpt(Long psnId, Long agencyId, Integer optType) throws ServiceException {
    // 校验参数
    String checkResult = this.checkInterestOptParam(psnId, agencyId, optType);
    // 校验通过才更新
    if (StringUtils.isBlank(checkResult)) {
      // 更新关注记录
      FundAgencyInterest interest = fundAgencyInterestDao.findInterestAgencyByPsnIdAndAgencyId(psnId, agencyId);
      Integer num = fundAgencyInterestDao.getPsnFundAgencyInteresNum(psnId);
      if (interest == null) {
        interest = new FundAgencyInterest();
        interest.setAgencyId(agencyId);
        interest.setPsnId(psnId);
        interest.setAgencyOrder(num != null ? num + 1 : 1);
        interest.setCreateDate(new Date());
      }
      interest.setUpdateDate(new Date());
      interest.setStatus(optType);
      interest.setAgencyOrder(num != null ? num + 1 : 1);
      fundAgencyInterestDao.save(interest);
    }
    return checkResult;
  }

  @Override
  public List<Map<String, Object>> initAgencyOpt(String des3Ids, Long psnId) throws ServiceException {
    List<Map<String, Object>> initInfo = new ArrayList<Map<String, Object>>();
    if (StringUtils.isNotBlank(des3Ids)) {
      // 解析要初始化的资助机构加密ID
      String[] ids = des3Ids.split(",");
      List<Long> agencyIds = new ArrayList<Long>();
      for (String des3Id : ids) {
        agencyIds.add(NumberUtils.parseLong(Des3Utils.decodeFromDes3(des3Id.trim()), 0L));
      }
      // 构建资助机构操作相关信息
      this.buildAgencyOptInfo(agencyIds, initInfo, psnId);
    }
    return initInfo;
  }

  @Override
  public String findPsnAllInterestAgencyIds(Long psnId) throws ServiceException {
    String ids = "";
    if (NumberUtils.isNotNullOrZero(psnId)) {
      List<Long> interestIds = fundAgencyInterestDao.findAllInterestAgencyIds(psnId);
      if (CollectionUtils.isNotEmpty(interestIds)) {
        for (Long id : interestIds) {
          ids += Des3Utils.encodeToDes3(id.toString()) + ",";
        }
      }
    }
    int index = ids.lastIndexOf(",");
    return index > -1 ? ids.substring(0, index) : ids;
  }

  @Override
  public void shareAgencyToFriends(FundAgencyForm form) throws ServiceException {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    buildReceiver(form);
    try {
      if (!checkShareParams(form)) {
        return;
      }
      // 循环保存记录 循环发送消息
      String[] des3ReceiverIds = form.getDes3ReceiverIds().split(",");
      String[] receiverMails = form.getReceiverMails().split(",");
      String[] des3AgencyIds = form.getDes3AgencyIds().split(",");
      Person sender = personDao.get(currentUserId);

      shareByMail(form, receiverMails, des3AgencyIds, sender);
      shareByPsnId(form, des3ReceiverIds, des3AgencyIds, sender);
    } catch (Exception e) {
      logger.error("分享资助机构给好友出错， sharePsnId = " + currentUserId + ", fundId = " + form.getDes3AgencyIds(), e);
      throw new ServiceException(e);
    }
  }

  /**
   * 通过psnId进行分享
   * 
   * @param form
   * @param des3ReceiverIds
   * @param des3AgencyIds
   * @param sender
   * @throws Exception
   */
  private void shareByPsnId(FundAgencyForm form, String[] des3ReceiverIds, String[] des3AgencyIds, Person sender)
      throws Exception {
    for (String des3ReceiverId : des3ReceiverIds) {
      if (StringUtils.isNotBlank(des3ReceiverId)) {
        Long receiverId = Long.parseLong(Des3Utils.decodeFromDes3(des3ReceiverId));
        if (receiverId != null) {
          if (receiverId.equals(sender.getPersonId())) {// 发送者和接收者是同一个人
            continue;
          }
          // 循环保存记录 与发送消息
          for (String des3AgencyId : des3AgencyIds) {
            Long agencyId = null;
            if (NumberUtils.isCreatable(des3AgencyId)) {
              agencyId = Long.parseLong(des3AgencyId);
            } else {
              agencyId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3AgencyId));
            }
            if (agencyId != null) {
              // 调open接口发送资助机构消息
              Map<String, Object> map1 = this.buildSendResMsgParam(form, receiverId, agencyId);
              sendShareAgencyToFriendMsg(map1);
              // 保存分享记录
              form.setFundAgencyId(agencyId);
              this.dealWithShareOpt(form);
              // 发送邮件给这个人
              Person receiver = personDao.get(receiverId);
              restSendShareAgencyToFriendEmail(sender, receiver, agencyId, des3AgencyIds[0], form.getComments(),
                  des3AgencyIds.length);
            }
          }
          // 发送文本消息
          if (StringUtils.isNotBlank(form.getComments())) {
            Map<String, Object> map1 = this.buildSendCommentsMsgParam(form, receiverId);
            sendShareAgencyToFriendMsg(map1);
          }
        }
      }
    }
  }

  /**
   * 通过psnId进行发送邮件，同时发送站内信
   * 
   * @param form
   * @param receiverMails
   * @param des3AgencyIds
   * @param sender
   * @throws Exception
   */
  private void shareByMail(FundAgencyForm form, String[] receiverMails, String[] des3AgencyIds, Person sender)
      throws Exception {
    for (String reciverMail : receiverMails) {
      if (StringUtils.isNotEmpty(reciverMail) && isEmail(reciverMail)) {
        // 循环保存记录 与发送消息
        for (String des3AgencyId : des3AgencyIds) {
          Long agencyId = null;
          if (NumberUtils.isCreatable(des3AgencyId)) {
            agencyId = Long.parseLong(des3AgencyId);
          } else {
            agencyId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3AgencyId));
          }
          if (agencyId != null) {
            // 保存分享记录
            form.setFundAgencyId(agencyId);
            FundAgencyShare share = new FundAgencyShare();
            share.setAgencyId(form.getFundAgencyId());
            share.setSharePsnId(sender.getPersonId());
            share.setShareToPlatform(form.getShareToPlatform());
            share.setComments(form.getComments());
            share.setReceivePsnId(0L);
            share.setCreateDate(new Date());
            fundAgencyShareDao.save(share);
            // 发送邮件给这个人----
            Person receiver = new Person();
            receiver.setEmail(reciverMail);
            receiver.setPersonId(0L);
            restSendShareAgencyToFriendEmail(sender, receiver, agencyId, des3AgencyIds[0], form.getComments(),
                des3AgencyIds.length);
          }
        }
      }
    }
  }

  /**
   * 调用接口发送分享资助机构给好友的邮件
   * 
   * @param sender
   * @param receiver
   * @param agencyId
   * @param des3AgencyId
   * @param comments
   * @param length
   * @throws Exception
   */
  private void restSendShareAgencyToFriendEmail(Person sender, Person receiver, Long agencyId, String des3AgencyId,
      String comments, int total) throws Exception {
    ConstFundAgency constFundAgency = null;
    if (agencyId != null) {
      constFundAgency = fundAgencyDao.get(agencyId);
    }
    if (sender == null || receiver == null || constFundAgency == null) {
      throw new Exception("构建文件回复，邮件对象为空" + this.getClass().getName());
    }
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, Object> mailData = new HashMap<String, Object>();

    String language = StringUtils.isNotBlank(receiver.getEmailLanguageVersion()) ? receiver.getEmailLanguageVersion()
        : sender.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().getLanguage();
    }
    if ("zh".equalsIgnoreCase(language)) {
      language = "zh_CN";
    } else if ("en".equalsIgnoreCase(language)) {
      language = "en_US";
    }
    String senderName = getPersonName(sender, language);
    String receiverName = getPersonName(receiver, language);

    // 跟踪链接 根据key放置到模板中 所有的链接不再需要放到mailData中
    // 发件人主页
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(sender.getPersonId());
    String senderPersonUrl = "";
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
      senderPersonUrl = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl();
    }
    String fundLogoUrl = "";
    fundLogoUrl = constFundAgency.getLogoUrl();
    if (StringUtils.isNotBlank(fundLogoUrl)) {
      if (fundLogoUrl.indexOf("scholarmate") == -1) {
        fundLogoUrl = domainscm + "/resmod" + fundLogoUrl;
      }
    } else {
      fundLogoUrl = domainscm + "/resmod/images_v5/fund_logo/default_logo.jpg";
    }
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("senderPersonUrl");
    l2.setUrl(senderPersonUrl);
    l2.setUrlDesc("发件人主页链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    MailLinkInfo l3 = new MailLinkInfo();
    l3.setKey("fundUrl");
    // 邮件链接属于站外,修改为站外可以访问的链接 SCM-24730
    // l3.setUrl(domainscm + "/prjweb/fundAgency/detailMain?Des3FundAgencyId=" + des3AgencyId);
    l3.setUrl(domainscm + "/prjweb/outside/agency?Des3FundAgencyId=" + des3AgencyId);
    l3.setUrlDesc("基资助机构详情链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));
    MailLinkInfo l4 = new MailLinkInfo();
    l4.setKey("viewUrl");
    l4.setUrl(domainscm + "/dynweb/showmsg/msgmain?model=chatMsg");
    l4.setUrlDesc("查看站内信地址");
    linkList.add(JacksonUtils.jsonObjectSerializer(l4));
    MailLinkInfo l5 = new MailLinkInfo();
    l5.setKey("fundLogoUrl");
    l5.setUrl(fundLogoUrl);
    l5.setUrlDesc("基金logo地址");
    linkList.add(JacksonUtils.jsonObjectSerializer(l5));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));

    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    Integer tempcode = 10102;
    info.setSenderPsnId(sender.getPersonId());
    info.setReceiverPsnId(receiver.getPersonId());
    info.setReceiver(receiver.getEmail());
    info.setMsg("分享资助机构给好友邮件");
    info.setMailTemplateCode(tempcode);
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));

    mailData.put("recvName", receiverName);
    mailData.put("recommendReason", comments);
    String minEnShareTitle =
        StringUtils.isNotBlank(constFundAgency.getNameEn()) ? constFundAgency.getNameEn() : constFundAgency.getNameZh();
    String minZhShareTitle =
        StringUtils.isNotBlank(constFundAgency.getNameZh()) ? constFundAgency.getNameZh() : constFundAgency.getNameEn();
    if (StringUtils.isNotBlank(minEnShareTitle)) {
      minEnShareTitle = "\"" + minEnShareTitle.trim() + "\"";
    }
    if (StringUtils.isNotBlank(minZhShareTitle)) {
      minZhShareTitle = "“" + minZhShareTitle + "”";
    }

    // 发件人头衔
    mailData.put("psnName", senderName);
    mailData.put("total", total);
    // 0：其他，1：成果，2：文献， 3：工"\"" + file + "\""作文档；4：项目；5：基金 6:资助机构
    mailData.put("type", "6");
    mailData.put("emailTypeKey", 0);
    mailData.put("mailContext", "");

    // 主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    String subjectType = "";
    String subjectCount = total + "";
    if ("zh".equals(language) || "zh_CN".equals(language)) {
      subjectType = "资助机构";
    } else {
      subjectType = "funding agencys";
      if ("1".equals(subjectCount)) {
        subjectCount = "a";
        subjectType = "funding agency";
      }
    }
    mailData.put("minEnShareTitle", minEnShareTitle);
    mailData.put("minZhShareTitle", minZhShareTitle);
    subjectParamLinkList.add(senderName);
    subjectParamLinkList.add(subjectCount);
    subjectParamLinkList.add(subjectType);
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);

  }

  /**
   * 重新构建接收者，原因是有些邮件是站内人员的，要给他们发站内信
   * 
   * @param form
   */
  private void buildReceiver(FundAgencyForm form) {
    String[] mails = form.getReceiverMails().split(",");
    StringBuffer mailStr = new StringBuffer();
    StringBuffer psnIdStr = new StringBuffer(form.getDes3ReceiverIds());
    for (int i = 0; i < mails.length; i++) {
      if (StringUtils.isNoneBlank(mails[i]) && isEmail(mails[i])) {
        User user = userDao.findByLoginName(mails[i]);// 判断当前邮件是不是在站内注册了
        if (user != null) {
          psnIdStr.append(Des3Utils.encodeToDes3(user.getId().toString())).append(",");
        } else {
          mailStr.append(mails[i]).append(",");
        }
      }
    }
    form.setDes3ReceiverIds(psnIdStr.toString());
    form.setReceiverMails(mailStr.toString());
  }


  /**
   * 构建资助机构名称
   * 
   * @param constFundCategory
   * @param form
   */
  private String buildFundAgency(ConstFundAgency constFundAgency, String language) {
    String fundAgencyName = "";
    if (constFundAgency != null) {
      String enName = constFundAgency.getNameEn();
      String zhName = constFundAgency.getNameZh();
      if ("zh".equals(language) || "zh_CN".equals(language)) {
        if (StringUtils.isNotBlank(zhName)) {
          fundAgencyName = zhName;
        } else {
          fundAgencyName = enName;
        }
      } else {
        if (StringUtils.isNotBlank(enName)) {
          fundAgencyName = enName;
        } else {
          fundAgencyName = zhName;
        }
      }
    }
    return fundAgencyName;
  }


  /**
   * 
   * @param person
   * @param language zh=中文
   * @return
   */
  private String getPersonName(Person person, String language) {
    if ("zh".equals(language) || "zh_CN".equals(language)) {
      if (StringUtils.isNotBlank(person.getName())) {
        return person.getName();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getEname();
      }
    } else {
      if (StringUtils.isNotBlank(person.getEname())) {
        return person.getEname();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getName();
      }
    }
  }

  /**
   * 构建发消息需要的参数
   * 
   * @param form
   * @return
   */
  private Map<String, Object> buildSendResMsgParam(FundAgencyForm form, Long receiverId, Long agencyId) {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    map.put("openid", "99999999");
    map.put("token", "00000000msg77msg");
    dataMap.put(MsgConstants.MSG_TYPE, MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER);
    dataMap.put(MsgConstants.MSG_SENDER_ID, form.getCurrentPsnId());
    dataMap.put(MsgConstants.MSG_RECEIVER_IDS, receiverId);
    dataMap.put(MsgConstants.MSG_AGENCY_ID, agencyId);
    dataMap.put(MsgConstants.MSG_DES3_AGENCY_ID, Des3Utils.encodeToDes3(Objects.toString(agencyId, "")));
    dataMap.put(MsgConstants.MSG_CONTENT, form.getComments());

    dataMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_AGENCY);
    dataMap.put(MsgConstants.MSG_BELONG_PERSON, "true");
    ConstFundAgency currAgency = fundAgencyDao.get(agencyId);
    if (currAgency != null) {
      Map<String, String> agencyInfoMap = new HashMap<String, String>();
      // TODO 构建分享的信息
      agencyInfoMap.put("agency_desc_zh", currAgency.getAddress());
      agencyInfoMap.put("agency_desc_en", currAgency.getEnAddress());
      form.setResInfoJson(JacksonUtils.mapToJsonStr(agencyInfoMap));
    }
    dataMap.put(MsgConstants.MSG_AGENCY_INFO, form.getResInfoJson());
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return map;
  }

  /**
   * 只发文本消息
   * 
   * @param form
   * @return
   */
  private Map<String, Object> buildSendCommentsMsgParam(FundAgencyForm form, Long receiverId) {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    map.put("openid", "99999999");
    map.put("token", "00000000msg77msg");
    dataMap.put(MsgConstants.MSG_TYPE, MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER);
    dataMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, "text");
    dataMap.put(MsgConstants.MSG_SENDER_ID, form.getCurrentPsnId());
    dataMap.put(MsgConstants.MSG_RECEIVER_IDS, receiverId);
    dataMap.put(MsgConstants.MSG_CONTENT, form.getComments());
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return map;
  }

  /**
   * 调用open接口发送分享资助机构给好友的站内信
   * 
   * @param form
   * @param receiverId
   * @param agencyId
   * @return
   * @throws Exception
   */
  private Long sendShareAgencyToFriendMsg(Map<String, Object> map1) {
    Object obj = restTemplate.postForObject(this.openResfulUrl, map1, Object.class);
    Map<String, Object> resultMap = JacksonUtils.jsonToMap(obj.toString());
    Long msgRelationId = 0L;
    if (resultMap != null && "success".equals(resultMap.get("status"))) {
      List<Map<String, Object>> result = (List<Map<String, Object>>) resultMap.get("result");
      if (result != null && result.size() > 0 && "success".equals(result.get(0).get("status"))) {
        msgRelationId = NumberUtils.toLong(result.get(0).get("msgRelationId").toString());
      }
    }
    return msgRelationId;
  }

  @Override
  public AgencyStatistics findAgencyStatistics(Long agencyId) throws ServiceException {
    if (NumberUtils.isNotNullOrZero(agencyId)) {
      return agencyStatisticsDao.get(agencyId);
    }
    return null;
  }

  // 构建资助机构操作信息
  private void buildAgencyOptInfo(List<Long> agencyIds, List<Map<String, Object>> initInfo, Long psnId) {
    if (CollectionUtils.isNotEmpty(agencyIds)) {
      // 找到所有已赞的资助机构ID
      List<Long> awardAgencyIds = fundAgencyAwardDao.findHasAwardAgencyIds(psnId, agencyIds);
      boolean hasAwardAgency = CollectionUtils.isNotEmpty(awardAgencyIds);
      // 找到当前人员所有已关注的资助机构ID
      List<Long> interestAgencyIds = fundAgencyInterestDao.findAllInterestAgencyIds(psnId);
      boolean hasInterestAgencyId = CollectionUtils.isNotEmpty(interestAgencyIds);
      // 查询需要初始化的资助机构的操作统计数
      List<AgencyStatistics> stats = agencyStatisticsDao.findAgencyStatistics(agencyIds);
      boolean statsNotEmpty = CollectionUtils.isNotEmpty(stats);
      // 遍历构建初始化信息
      for (Long agencyId : agencyIds) {
        Map<String, Object> info = new HashMap<String, Object>();
        info.put("id", Des3Utils.encodeToDes3(agencyId.toString()));
        info.put("hasAward", hasAwardAgency && awardAgencyIds.contains(agencyId) ? 1 : 0);
        // 构建统计数信息
        if (statsNotEmpty) {
          for (AgencyStatistics sta : stats) {
            if (CommonUtils.compareLongValue(agencyId, sta.getAgencyId())) {
              info.put("awardCount", sta.getAwardSum());
              info.put("shareCount", sta.getShareSum());
              info.put("interestCount", sta.getInterestSum());
            }
          }
        }
        // 是否关注
        if (hasInterestAgencyId) {
          info.put("interested", interestAgencyIds.contains(agencyId) ? 1 : 0);
        }
        initInfo.add(info);
      }
    }
  }

  @Override
  public boolean existsFund(Long fundAgencyId) {
    if (!NumberUtils.isNullOrZero(fundAgencyId)) {
      ConstFundCategory constFundCategory = constFundCategoryDao.get(fundAgencyId);
      if (constFundCategory != null) {
        return true;
      }
    }
    return false;
  }

  /**
   * 判断当前输入字符是否为邮箱地址
   * 
   * @param str
   * @return
   */
  private boolean isEmail(String str) {
    String reg = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    return Pattern.matches(reg, str);
  }

  @Override
  public Map<String, Object> queryFundDetail(Long fundId) throws ServiceException {
    // TODO Auto-generated method stub
    try {
      return solrIndexService.queryFundDetail(fundId);
    } catch (SolrServerException e) {
      logger.error("solr获取基金详情失败,fundId={}", fundId, e);
    }
    return null;
  }
}
