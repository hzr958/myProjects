package com.smate.web.fund.service.recommend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.consts.dao.ConstPositionDao;
import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.ConstPosition;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.solr.service.SolrIndexSerivceImpl;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.statistics.dao.RecommendInitDao;
import com.smate.core.base.statistics.model.RecommendInit;
import com.smate.core.base.utils.common.LocaleTextUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.fund.agency.dao.AgencyStatisticsDao;
import com.smate.web.fund.agency.dao.CategoryScmDao;
import com.smate.web.fund.agency.dao.FundAgencyDao;
import com.smate.web.fund.agency.dao.FundAgencyInterestDao;
import com.smate.web.fund.agency.model.FundAgencyInterest;
import com.smate.web.fund.dao.wechat.ConstFundCategoryDao;
import com.smate.web.fund.dao.wechat.ConstFundCategoryFileDao;
import com.smate.web.fund.dao.wechat.ConstFundCategoryRcmdDao;
import com.smate.web.fund.dao.wechat.ConstFundCategoryRegionDao;
import com.smate.web.fund.dao.wechat.PsnFundRecommendDao;
import com.smate.web.fund.model.common.CategoryScm;
import com.smate.web.fund.model.common.ConstFundAgency;
import com.smate.web.fund.model.common.ConstFundCategory;
import com.smate.web.fund.model.common.ConstFundCategoryInfo;
import com.smate.web.fund.model.common.ConstFundCategoryRegion;
import com.smate.web.fund.recommend.dao.ConstFundAgencyDao;
import com.smate.web.fund.recommend.dao.DynamicAwardResDao;
import com.smate.web.fund.recommend.dao.FundAwardDao;
import com.smate.web.fund.recommend.dao.FundRecommendAreaDao;
import com.smate.web.fund.recommend.dao.FundRecommendRecordDAO;
import com.smate.web.fund.recommend.dao.FundRecommendSeniorityDao;
import com.smate.web.fund.recommend.dao.FundStatisticsDao;
import com.smate.web.fund.recommend.dao.MyFundDao;
import com.smate.web.fund.recommend.dao.PsnScienceAreaDao;
import com.smate.web.fund.recommend.model.DynamicRemdForm;
import com.smate.web.fund.recommend.model.FundAward;
import com.smate.web.fund.recommend.model.FundRecommendArea;
import com.smate.web.fund.recommend.model.FundRecommendForm;
import com.smate.web.fund.recommend.model.FundRecommendRecord;
import com.smate.web.fund.recommend.model.FundRecommendSeniority;
import com.smate.web.fund.recommend.model.FundScienceArea;
import com.smate.web.fund.recommend.model.FundStatistics;
import com.smate.web.fund.recommend.model.MyFund;
import com.smate.web.fund.recommend.rcmd.dao.ConstFundCategoryDisDao;
import com.smate.web.prj.exception.PrjException;
import com.smate.web.prj.model.common.DynamicAwardRes;

/**
 * 基金推荐服务
 * 
 * @author WSN
 *
 *         2017年8月18日 上午10:21:53
 *
 */
@Service("fundRecommendService")
@Transactional(rollbackFor = Exception.class)
public class FundRecommendServiceImpl implements FundRecommendService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MyFundDao myFundDao;
  @Autowired
  private ConstFundCategoryDao constFundCategoryDao;
  @Autowired
  private ConstFundCategoryRcmdDao constFundCategoryRcmdDao;
  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;
  @Autowired
  private FundStatisticsDao fundStatisticsDao;
  @Autowired
  private FundAwardDao fundAwardDao;
  @Autowired
  private CategoryScmDao categoryScmDao;
  @Autowired
  private SolrIndexService solrIndexService;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PsnScienceAreaDao psnScienceAreaDao;
  @Autowired
  private ConstPositionDao constPositionDao;
  @Autowired
  private ConstFundCategoryRegionDao constFundCategoryRegionDao;
  @Autowired
  private ConstFundCategoryFileDao constFundCategoryFileDao;
  @Autowired
  private ConstFundCategoryDisDao constFundCategoryDisDao;
  @Value("${domainscm}")
  private String domainScm;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private DynamicAwardResDao dynamicAwardResDao;
  @Autowired
  private FundRecommendAreaDao fundRecommendAreaDao;
  @Autowired
  private FundAgencyInterestDao fundAgencyInterestDao;
  @Autowired
  private FundRecommendSeniorityDao fundRecommendSeniorityDao;
  @Autowired
  private RecommendInitDao recommendInitDao;
  @Autowired
  private FundAgencyDao fundAgencyDao;// 机构dao
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private FundRecommendRecordDAO fundRecommendRecordDAO;
  @Autowired
  private AgencyStatisticsDao agencyStatisticsDao;
  @Autowired
  private PsnFundRecommendDao psnFundRecommendDao;

  private String objToStr(Object obj) {
    if (obj == null) {
      return "";
    }
    return obj.toString().trim();
  }

  @Override
  public void showMyFund(FundRecommendForm form) throws Exception {
    this.searchMyFund(form);
    if (form.getFundInfoList() != null && form.getFundInfoList().size() > 0) {
      for (ConstFundCategoryInfo info : form.getFundInfoList()) {
        try {
          buildShowMyFund(info, form);
        } catch (Exception e) {
          // 吃掉异常
          logger.error("构建基金信息异常，psnId = " + form.getPsnId(), e);
        }
      }
    }
  }

  private void buildShowMyFund(ConstFundCategoryInfo info, FundRecommendForm form) {
    info.setEncryptedFundId(Des3Utils.encodeToDes3(info.getFundId().toString()));
    info.setFundTitle(info.getZhTitle());
    info.setLogoUrl("/ressns/images/default/default_fund_logo.jpg");
    // 查询基金资助机构
    Long agencyId = info.getFundAgencyId();
    if (agencyId != null) {
      ConstFundAgency agency = constFundAgencyDao.findFundAgencyInfo(agencyId);
      if (agency != null) {
        info.setFundAgencyName(StringUtils.isBlank(agency.getNameZh()) ? agency.getNameEn() : agency.getNameZh());
        info.setRegionId(agency.getRegionId());
        // 图片
        if (StringUtils.isNotBlank(agency.getLogoUrl())) {
          if (agency.getLogoUrl().contains("http")) {
            info.setLogoUrl(agency.getLogoUrl());
          } else {
            info.setLogoUrl("/resmod" + agency.getLogoUrl());
          }
        }
      }
    }

    info.setShowDate(this.dealNullVal(buildFundApplyTime(info)));
    // 统计数
    FundStatistics sta = fundStatisticsDao.findFundStatisticsById(info.getFundId());
    if (sta != null) {
      info.setAwardCount(sta.getAwardCount());
      info.setShareCount(sta.getShareCount());
    }
    // 赞状态
    FundAward fundAward = fundAwardDao.awardOperationStatus(form.getPsnId(), info.getFundId());
    if (fundAward != null && fundAward.getStatus() == LikeStatusEnum.LIKE) {
      info.setHasAward(true);
    }
  }

  @Override
  public FundRecommendForm searchMyFund(FundRecommendForm form) throws PrjException {
    try {
      Page<Long> page = new Page<Long>();
      // 分页查询基金ID
      if (StringUtils.isNotBlank(form.getSearchKey())) {
        page = constFundCategoryDao.searchMyFundList(form);
      } else {
        page = myFundDao.searchMyFund(form);
      }
      if (CollectionUtils.isNotEmpty(page.getResult())) {
        // 构建基金ID的List
        List<Long> fundIds = new ArrayList<Long>();
        for (Long id : page.getResult()) {
          fundIds.add(id);
        }
        // 根据基金ID查询基金详情
        List<ConstFundCategory> fundList = constFundCategoryDao.findConstFundCategoryByIds(fundIds);
        // 构建基金显示信息
        List<ConstFundCategoryInfo> infoList = this.buildFundShowInfo(fundList);
        // 是否赞过
        infoList = this.hasFundAwarded(infoList, fundIds, form.getPsnId());
        form.setFundInfoList(infoList);
      }
    } catch (Exception e) {
      logger.error("分页查询我的基金出错, psnId = " + form.getPsnId(), e);
      throw new PrjException(e);
    }
    return form;
  }

  /**
   * 保存设置条件初始化标志
   * 
   * @param psnId
   */
  private void saveRecommendInit(Long psnId) {
    RecommendInit init = new RecommendInit();
    init.setPsnId(psnId);
    init.setFundRecommendInit(1);
    recommendInitDao.saveRecommendInit(init);
  }

  /**
   * 推荐设置条件是否已经初始化了
   * 
   * @param psnId
   * @return
   */
  private boolean hasInit(Long psnId) {
    RecommendInit init = recommendInitDao.getRecommendInit(psnId);
    return Optional.ofNullable(init).map(RecommendInit::getFundRecommendInit).filter(i -> i == 1).isPresent();
  }

  /**
   * 构建要显示的基金信息
   * 
   * @param fundList
   * @return
   */
  private List<ConstFundCategoryInfo> buildFundShowInfo(List<ConstFundCategory> fundList) {
    String locale = LocaleContextHolder.getLocale().toString();
    List<ConstFundCategoryInfo> infoList = new ArrayList<ConstFundCategoryInfo>();
    if (CollectionUtils.isNotEmpty(fundList)) {
      for (ConstFundCategory fund : fundList) {
        ConstFundCategoryInfo info = new ConstFundCategoryInfo();
        info.setFundId(fund.getId());
        info.setEncryptedFundId(Des3Utils.encodeToDes3(fund.getId().toString()));
        // 查询基金资助机构
        ConstFundAgency agency = constFundAgencyDao.findFundAgencyInfo(fund.getAgencyId());
        // 标题
        info.setZhTitle(StringUtils.isNotBlank(fund.getNameZh()) ? fund.getNameZh() : fund.getNameEn());
        info.setEnTitle(StringUtils.isNotBlank(fund.getNameEn()) ? fund.getNameEn() : fund.getNameZh());
        if ("en_US".equals(locale)) {
          info.setFundTitle(StringUtils.isNotBlank(fund.getNameEn()) ? fund.getNameEn() : fund.getNameZh());
          // 资助机构名称
          if (agency != null) {
            info.setFundAgencyName(
                StringUtils.isNotBlank(agency.getNameEn()) ? agency.getNameEn() : agency.getNameZh());
          }
        } else {
          // 标题
          info.setFundTitle(StringUtils.isNotBlank(fund.getNameZh()) ? fund.getNameZh() : fund.getNameEn());
          // 资助机构名称
          if (agency != null) {
            info.setFundAgencyName(
                StringUtils.isNotBlank(agency.getNameZh()) ? agency.getNameZh() : agency.getNameEn());
          }
        }
        // 科技领域
        info.setScienceAreas(buildFundScienceAreaInfo(fund.getId()));
        if (agency != null) {
          info.setRegionId(agency.getRegionId());
          // 图片,有些图片在resmod里面
          // String logoUrl =
          // Optional.ofNullable(agency.getLogoUrl()).map(url -> {
          // if (url.contains("http")) {
          // return url;
          // } else {
          // return "/resmod" + url;
          // }
          // }).orElseGet(null);
          String logoUrl = (StringUtils.isNotBlank(agency.getLogoUrl()) && !agency.getLogoUrl().contains("http"))
              ? ("/resmod" + agency.getLogoUrl())
              : agency.getLogoUrl();
          info.setLogoUrl(logoUrl);
          info.setZhAgencyName(agency.getNameZh());
          info.setEnAgencyName(agency.getNameEn());
        }
        info.setFundAgencyId(fund.getAgencyId());
        // 统计数
        FundStatistics sta = fundStatisticsDao.findFundStatisticsById(fund.getId());
        if (sta != null) {
          info.setAwardCount(sta.getAwardCount());
          info.setShareCount(sta.getShareCount());
        }
        // 开始结束时间
        info.setStart(fund.getStartDate());
        info.setEnd(fund.getEndDate());
        info.setShowDate(this.dealNullVal(buildFundApplyTime(info)));
        if (fund.getEndDate() != null) {
          Date now = new Date();
          if (!now.after(fund.getEndDate())) {
            info.setIsStaleDated(false);
          }
        }
        this.buildFundInternationalInfo(info);
        infoList.add(info);
      }
    }
    return infoList;
  }

  /**
   * 构建基金科技领域信息
   * 
   * @param fundId
   * @return
   */
  private String buildFundScienceAreaInfo(Long fundId) {
    List<Long> scienceAreaIds = constFundCategoryDisDao.findFundDisciplineIds(fundId);
    FundRecommendForm form = new FundRecommendForm();
    if (CollectionUtils.isNotEmpty(scienceAreaIds)) {
      List<Long> ids = new ArrayList<Long>();
      for (Long id : scienceAreaIds) {
        ids.add(id);
      }
      this.buildFundScienceAreaInfo(ids, null, form);
    }
    return form.getScienceAreaNames();
  }

  // 构建起止时间
  private String buildFundApplyTime(ConstFundCategoryInfo fundinfo) {
    SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
    Date startTime = fundinfo.getStart();
    Date endTime = fundinfo.getEnd();
    String start = "";
    String end = "";
    if (startTime != null) {
      start = smf.format(startTime);
    }
    if (endTime != null) {
      end = smf.format(endTime);
    }
    if (StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end)) {
      return start + " ~ " + end;
    } else {
      return start + end;
    }
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

  @Override
  public FundRecommendForm fundAwardOperation(FundRecommendForm form) throws PrjException {
    Long psnId = form.getPsnId();
    Long fundId = form.getFundId();
    try {
      // 查询赞操作记录
      FundAward award = fundAwardDao.findFundAwardRecord(psnId, fundId);
      // 点赞，如果赞了就不要赞
      if (form.getAwardOperation() == 0 && award != null && award.getStatus() == LikeStatusEnum.LIKE) {
        logger.error("已经点赞了 psnId=" + psnId + " fundId=" + fundId);
        form.setErrorMsg("已经点赞了 psnId=" + psnId + "fundId=" + fundId);
        return form;
      }
      if (form.getAwardOperation() == 1 && award != null && award.getStatus() == LikeStatusEnum.UNLIKE) {
        logger.error("已经取消赞了 psnId=" + psnId + " fundId=" + fundId);
        form.setErrorMsg("已经取消赞了 psnId=" + psnId + "fundId=" + fundId);
        return form;
      }
      FundStatistics sta = fundStatisticsDao.findFundStatisticsById(fundId);
      // 反转赞操作，已赞 ==> 取消赞 未赞==>赞
      LikeStatusEnum action =
          (award != null && award.getStatus() == LikeStatusEnum.LIKE) ? LikeStatusEnum.UNLIKE : LikeStatusEnum.LIKE;

      if (award == null) {
        award = new FundAward();
        award.setFundId(fundId);
        award.setAwardPsnId(psnId);
      }
      // 更新赞状态
      award.setAwardDate(new Date());
      award.setStatus(action);
      fundAwardDao.save(award);
      if (sta == null) {
        sta = new FundStatistics();
        sta.setFundId(fundId);
        sta.setAwardCount(0);
        sta.setShareCount(0);
      }
      // 更新赞统计
      Integer awardCount = sta.getAwardCount() == null ? 0 : sta.getAwardCount();
      if (action == LikeStatusEnum.LIKE) {
        awardCount += 1;
      } else if (action == LikeStatusEnum.UNLIKE) {
        awardCount = awardCount.intValue() > 0 ? awardCount - 1 : 0;
      }
      sta.setAwardCount(awardCount);
      fundStatisticsDao.save(sta);
      // 更新动态res
      try {
        // 更新资源赞次数.
        Long resId = form.getFundId();
        int resType = DynamicConstants.RES_TYPE_FUND;
        int resNode = 1;
        DynamicAwardRes dynamicAwardRes = this.dynamicAwardResDao.getDynamicAwardRes(resId, resType, resNode);
        if (dynamicAwardRes == null) {
          dynamicAwardRes = new DynamicAwardRes();
          dynamicAwardRes.setResId(resId);
          dynamicAwardRes.setResType(resType);
          dynamicAwardRes.setResNode(resNode);
          dynamicAwardRes.setAwardTimes(1l);
          dynamicAwardRes.setUpdateDate(new Date());
        } else {
          Long awardTimes = dynamicAwardRes.getAwardTimes();
          if (action == LikeStatusEnum.LIKE) {
            awardTimes += 1;
          } else if (action == LikeStatusEnum.UNLIKE) {
            awardTimes = awardTimes > 0 ? awardTimes - 1 : 0;
          }
          dynamicAwardRes.setAwardTimes(awardTimes);
        }
        dynamicAwardResDao.save(dynamicAwardRes);

      } catch (Exception e) {
        logger.error(" 更新资源出错， fundId = " + fundId, e);
      }
      form.setHasAward(action == LikeStatusEnum.LIKE ? true : false);
      form.setAwardCount(awardCount);
    } catch (Exception e) {
      logger.error("基金赞操作出错， fundId = " + fundId + ", psnId = " + psnId, e);
      form.setErrorMsg("throw new PrjException");
      throw new PrjException(e);
    }
    return form;
  }

  @Override
  public FundRecommendForm fundCollectOperation(FundRecommendForm form) throws PrjException {
    Long psnId = form.getPsnId();
    Long fundId = form.getFundId();
    try {
      if (!myFundDao.checkFundInfo(fundId)) {
        logger.error(
            "基金收藏操作出错,基金信息不完整，请检查const_fund_category，const_fund_agency数据， psnId = " + psnId + ", fundId = " + fundId);
        throw new PrjException(
            "基金收藏操作出错,基金信息不完整，请检查const_fund_category，const_fund_agency数据， psnId = " + psnId + ", fundId = " + fundId);
      }
      MyFund fund = myFundDao.findMyFund(psnId, fundId);
      if (form.getCollectOperate() != null && form.getCollectOperate().equals(1)) {
        // 取消收藏, 没收藏该基金则不做操作
        if (fund != null) {
          myFundDao.delete(fund);
        }
      } else {
        // 收藏, 已收藏过的则不做操作
        if (fund == null) {
          fund = new MyFund();
          fund.setFundId(fundId);
          fund.setPsnId(psnId);
          fund.setCollectTime(new Date());
          myFundDao.save(fund);
        }
      }
    } catch (Exception e) {
      logger.error("基金收藏操作出错， psnId = " + psnId + ", fundId = " + fundId, e);
      throw new PrjException(e);
    }
    return form;
  }

  /**
   * 查询推荐基金条件
   * 
   * 1.要查询人员的学位，position或degreeName--------》CONST_POSITION表的grades字段（ PC端是在查询列表时查询的）
   * 
   * 2.v_fund_conditions表中的地区、申请资格、科技领域条件，某项为空的话，则用人员对应的信息替代
   * 
   * 3.时间限制：1周之内、一个月之内、三个月之内、不限（不限时要求结束时间至少在今天之后）-----用户选择
   * 
   * 注意： 该处检索条件或需求有改变的时候，一定要修改对应的推荐基金的后台任务的逻辑代码
   * 
   */
  @Override
  public FundRecommendForm fundRecommendConditionsShow(FundRecommendForm form) throws PrjException {
    try {
      Long psnId = form.getPsnId();
      if (psnId == null) {
        return null;
      }
      // 查询关注的资助机构
      List<FundAgencyInterest> fundAgencyInterestList = fundAgencyInterestDao.fundAgencyInteresByPsnId(psnId);
      // 查询设置的科技领域
      List<FundRecommendArea> fundAreaList = fundRecommendAreaDao.getFundRecommendAreaListByPsnId(psnId);
      // 查询申请资格
      FundRecommendSeniority fundSenior = fundRecommendSeniorityDao.getSeniorityByPsnId(psnId);
      if (psnId > 0 && !hasInit(psnId)) {
        if (CollectionUtils.isEmpty(fundAgencyInterestList) || isNSFC(fundAgencyInterestList)) {
          fundAgencyInterestList = initFundAgencyInterest(psnId);// 初始化关注的资助机构
        }
        if (CollectionUtils.isEmpty(fundAreaList)) {
          fundAreaList = initArea(psnId, fundAreaList);// 初始化科技领域
        }
        if (fundSenior == null) {
          initSeniority(psnId, fundSenior);// 初始化申请资格
        }
        saveRecommendInit(psnId);// 保存已经初始化的标志表
      }
      form.setFundAgencyInterestList(setFundAgencyInterestsName(fundAgencyInterestList));
      form.setFundAreaList(fundAreaList);
      form.setSeniorityCode(fundSenior != null ? fundSenior.getCode() : 0);
    } catch (Exception e) {
      logger.error("构建基金推荐条件显示出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
      throw new PrjException(e);
    }
    return form;
  }

  @Override
  public void initPsnRecommendFund(Long psnId) throws PrjException {
    /**
     * 1.后台任务只处理有单位的人员的数据 2.刚注册完了以后的人员也只处理有单位的人员的数据 3.进入推荐页面以后的保持和现在的逻辑不变
     */

    try {
      if (psnId == null) {
        return;
      }
      // 查询关注的资助机构
      List<FundAgencyInterest> fundAgencyInterestList = fundAgencyInterestDao.fundAgencyInteresByPsnId(psnId);
      // 查询设置的科技领域
      List<FundRecommendArea> fundAreaList = fundRecommendAreaDao.getFundRecommendAreaListByPsnId(psnId);
      // 查询申请资格
      FundRecommendSeniority fundSenior = fundRecommendSeniorityDao.getSeniorityByPsnId(psnId);
      Long psnInsId = personProfileDao.getPsnInsId(psnId);
      if (psnId > 0 && !hasInit(psnId) && psnInsId != null && psnInsId > 0) {// 单位不为空才初始化
        if (CollectionUtils.isEmpty(fundAgencyInterestList) || isNSFC(fundAgencyInterestList)) {
          fundAgencyInterestList = initFundAgencyInterest(psnId);// 初始化关注的资助机构
        }
        if (CollectionUtils.isEmpty(fundAreaList)) {
          fundAreaList = initArea(psnId, fundAreaList);// 初始化科技领域
        }
        if (fundSenior == null) {
          initSeniority(psnId, fundSenior);// 初始化申请资格
        }
        saveRecommendInit(psnId);// 保存已经初始化的标志表
      }
    } catch (Exception e) {
      logger.error("构建基金推荐条件显示出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
      throw new PrjException(e);
    }
    return;
  }

  /**
   * v3sie.data_ins_nsfc_2018 这个方法是4000所大学或者机构是手工sql导入的，初始化只有这个的话也要初始化
   * 
   * @param fundAgencyInterestList
   * @return
   */
  private boolean isNSFC(List<FundAgencyInterest> fundAgencyInterestList) {
    if (CollectionUtils.isNotEmpty(fundAgencyInterestList) && fundAgencyInterestList.size() <= 1) {
      FundAgencyInterest fund = fundAgencyInterestList.get(0);
      if (fund != null && fund.getAgencyId().longValue() == 1000) {
        return true;
      }
    }
    return false;
  }

  private List<FundAgencyInterest> initFundAgencyInterest(Long psnId) {
    List<FundAgencyInterest> fundAgencyInterestList = new ArrayList<FundAgencyInterest>();
    Long psnInsId = personProfileDao.getPsnInsId(psnId);
    if (psnInsId != null && psnInsId > 0) {
      Institution ins = institutionDao.findById(psnInsId);
      if (ins != null) {
        int natrue = ins.getNature() != null ? ins.getNature().intValue() : 0;
        Long regionId = ins.getRegionId();
        List<String> addAgencyName = new ArrayList<String>();
        List<String> localAgencyName = new ArrayList<String>();// 本地的
        List<ConstFundAgency> addFundAgencyList = new ArrayList<ConstFundAgency>();
        ConstFundAgency agencykj = fundAgencyDao.getOneFundAgencyByName("科技部");

        switch (natrue) {
          case 1:// 大学
            if (regionId != null && (regionId.longValue() == 158 || regionId.longValue() == 344
                || regionId.longValue() == 446 || regionId.longValue() == 710000 || regionId.longValue() == 810000
                || regionId.longValue() == 820000)) {// 港澳台的大学
              addAgencyName.add("国家自然科学基金委员会");
              addAgencyName.add("深圳市科技创新委员会");
              addAgencyName.add("广东省科学技术厅");
              addFundAgencyList = setUniversityFundAgency(addAgencyName);
            } else {
              addAgencyName.add("科技");
              addAgencyName.add("科学");
              addFundAgencyList = setFundAgencyInterestByName(ins, addAgencyName, false);
              addAgencykj(addFundAgencyList, agencykj);
            }
            break;
          case 2:// 科研机构
            addAgencyName.add("科技");
            addAgencyName.add("科学");
            addFundAgencyList = setFundAgencyInterestByName(ins, addAgencyName, false);
            addAgencykj(addFundAgencyList, agencykj);
            break;
          case 4:// 公司
            addAgencyName.add("科技");
            addAgencyName.add("科学");
            addAgencyName.add("工业和信息化");
            addFundAgencyList = setFundAgencyInterestByName(ins, addAgencyName, false);

            localAgencyName.add("经济和信息化委员会");
            localAgencyName.add("发展和改革委员会");
            addFundAgencyList.addAll(setFundAgencyInterestByName(ins, localAgencyName, true));// 仅本地
            break;
          case 7:// 医院
            addAgencyName.add("科技");
            addAgencyName.add("科学");
            addAgencyName.add("卫生");
            addAgencyName.add("卫计委");
            addFundAgencyList = setFundAgencyInterestByName(ins, addAgencyName, false);
            addAgencykj(addFundAgencyList, agencykj);
            break;
        }
        fundAgencyInterestList = this.saveFundRecommendAgencyByAgencyList(addFundAgencyList, psnId);
      }
    }
    return fundAgencyInterestList;
  }

  /**
   * 添加一个机构排除重复
   * 
   * @param addFundAgencyList
   * @param agencykj
   */
  private void addAgencykj(List<ConstFundAgency> addFundAgencyList, ConstFundAgency agencykj) {
    if (addFundAgencyList == null) {
      addFundAgencyList = new ArrayList<ConstFundAgency>();
    }
    if (agencykj != null) {
      boolean isRepeat = false;
      for (ConstFundAgency fund : addFundAgencyList) {
        if (fund != null && fund.getId().equals(agencykj.getId())) {
          isRepeat = true;
          break;
        }
      }
      if (!isRepeat) {
        addFundAgencyList.add(agencykj);
      }
    }
  }

  /**
   * 设置港澳台大学的关注资助机构
   * 
   * @param ins
   */
  private List<ConstFundAgency> setUniversityFundAgency(List<String> agencyNameList) {
    List<ConstFundAgency> addFundAgencyList = new ArrayList<ConstFundAgency>();
    for (int i = 0; i < agencyNameList.size(); ++i) {
      String name = agencyNameList.get(i);
      ConstFundAgency agency = fundAgencyDao.getOneFundAgencyByName(name);
      if (agency != null) {
        addFundAgencyList.add(agency);
      }
    }
    return addFundAgencyList;
  }

  /**
   * 设置关注资助机构
   * 
   * @param ins
   */
  private List<ConstFundAgency> setFundAgencyInterestByName(Institution ins, List<String> agencyNameList,
      boolean localOnly) {
    List<ConstFundAgency> addFundAgencyList = new ArrayList<ConstFundAgency>();
    if (ins.getRegionId() == null) {
      return addFundAgencyList;
    }
    String searchName =
        Optional.ofNullable(agencyNameList).map(list -> list.stream().collect(Collectors.joining("|"))).orElseGet(null);// 查询的名称正则
    List<Long> regionIds = new ArrayList<Long>();
    if (localOnly) {// 是否只查本地
      regionIds.add(ins.getRegionId());
    } else {
      regionIds = constRegionDao.getSuperRegionList(ins.getRegionId(), false);// 不包括国家级的
    }
    if (CollectionUtils.isNotEmpty(regionIds)) {
      addFundAgencyList = fundAgencyDao.getFundAgencyListByName(searchName, regionIds);
    }
    return addFundAgencyList;
  }



  /**
   * 根据个人设置保存省级
   * 
   */
  private void saveInitFundAgencyInterest(Long psnId, Long regionCode,
      List<FundAgencyInterest> fundAgencyInterestList) {

    ConstRegion constRegion = constRegionDao.findRegionNameById(regionCode);
    if (constRegion != null && !regionCode.equals(156L)) {
      if (constRegion.getSuperRegionId() != null && constRegion.getSuperRegionId().equals(156L)) {
        List<Long> regionId = new ArrayList<Long>();
        regionId.add(regionCode);
        List<Long> subRegionIds = constRegionDao.findSubRegionIdBySuperRegionId(regionId);
        subRegionIds.add(regionCode);
        List<ConstFundAgency> fundAgencyList = fundAgencyDao.getFundAgencyListByRegionIds(subRegionIds);// 省级及以下资助机构
        for (ConstFundAgency agency : fundAgencyList) {
          saveFundRecommendAgency(psnId, agency.getId(), fundAgencyInterestList);
        }
      } else if (constRegion.getSuperRegionId() != null) {
        saveInitFundAgencyInterest(psnId, constRegion.getSuperRegionId(), fundAgencyInterestList);
      }
    }
  }

  /**
   * 设置资助机构名
   * 
   * @param fundAgencyInterestList
   */
  public List<FundAgencyInterest> setFundAgencyInterestsName(List<FundAgencyInterest> fundAgencyInterestList) {
    if (CollectionUtils.isNotEmpty(fundAgencyInterestList)) {
      for (FundAgencyInterest agency : fundAgencyInterestList) {
        agency.setShowName(getAgencyName(agency.getAgencyId()));
      }
    }
    return fundAgencyInterestList;
  }

  /**
   * 初始化科技领域
   * 
   * @param fundAreaList
   * @return
   */
  private List<FundRecommendArea> initArea(Long psnId, List<FundRecommendArea> fundAreaList) {
    fundAreaList = new ArrayList<FundRecommendArea>();
    List<Long> scienceAreaIds = psnScienceAreaDao.findPsnScienceAreaIds(psnId);
    if (CollectionUtils.isNotEmpty(scienceAreaIds)) {// 限制3个
      fundAreaList = scienceAreaIds.stream().limit(3).map(areaId -> saveFundRecommendArea(psnId, areaId))
          .filter(areaId -> Optional.ofNullable(areaId).isPresent()).collect(Collectors.toList());
    }
    return fundAreaList;
  }

  /**
   * 初始化申请资格
   * 
   * @param psnId
   * @param fundSenior
   * @return
   */
  private FundRecommendSeniority initSeniority(Long psnId, FundRecommendSeniority fundSenior) {
    Long insId = personProfileDao.getPsnInsId(psnId);
    Long nature = 0L;
    if (insId != null) {
      nature = institutionDao.getInsNatureByInsId(insId);
      if (nature == null) {// 不是企业或科研单位设置为其他
        nature = 0L;
      } else if (nature == 4) {// 生产机4是表示企业
        nature = 1L;
      } else {// 其他算是科研机构
        nature = 2L;
      }
    } else {
      nature = 0L;
    }
    // 查询申请资格
    FundRecommendSeniority oldfundSenior = fundRecommendSeniorityDao.getSeniorityByPsnId(psnId);
    if (oldfundSenior == null) {
      fundSenior = new FundRecommendSeniority();
      fundSenior.setCode(nature.intValue());
      fundSenior.setPsnId(psnId);
      fundSenior.setUpdateDate(new Date());
      fundRecommendSeniorityDao.save(fundSenior);
    } else {
      fundSenior = oldfundSenior;
    }
    return fundSenior;
  }

  /**
   * 获取人员首要工作单位的region_id .
   * 
   * @param person
   * @return
   */
  private Long getInsRegion(Person person) {
    Long insRegionId = null;
    try {
      Institution institute = null;
      if (person.getInsId() != null) {
        institute = institutionDao.findById(person.getInsId());
      } else if (StringUtils.isNotBlank(person.getInsName())) {
        institute = institutionDao.findByName(person.getInsName());
      }
      if (institute != null) {
        insRegionId = institute.getRegionId();
      }
    } catch (Exception e) {
      logger.error("获取人员首要单位的regionId出错, psnId=" + (person != null ? person.getPersonId() : "null"));
    }
    return insRegionId;
  }

  /**
   * 
   * @param idList 基金的科技领域id列表 ----我的基金
   * @param scienceAreaIds 科技领域 code1,code2,code3形式 ---推荐基金
   * @param form
   * @return
   */
  private List<FundScienceArea> buildFundScienceAreaInfo(List<Long> idList, String scienceAreaIds,
      FundRecommendForm form) {
    List<FundScienceArea> areaList = new ArrayList<FundScienceArea>();
    String locale = LocaleContextHolder.getLocale().toString();
    List<Long> ids = new ArrayList<Long>();
    if (CollectionUtils.isNotEmpty(idList)) {
      ids = idList;
    } else if (StringUtils.isNotBlank(scienceAreaIds)) {
      String[] idStr = scienceAreaIds.split(",");
      if (idStr != null && idStr.length > 0) {
        for (String id : idStr) {
          ids.add(NumberUtils.toLong(id));
        }
      }
    }
    if (CollectionUtils.isNotEmpty(ids)) {
      StringBuffer scienceAreaNames = new StringBuffer();
      // 获取科技领域
      List<CategoryScm> list = categoryScmDao.findCategoryScm(ids);
      if (CollectionUtils.isNotEmpty(list)) {
        // 构建科技领域信息
        for (CategoryScm ca : list) {
          FundScienceArea area = new FundScienceArea();
          area.setScienceAreaId(ca.getCategoryId());
          area.setEnTitle(ca.getCategoryEn());
          area.setZhTitle(ca.getCategoryZh());
          area.setShowTitle("zh_CN".equals(locale) ? ca.getCategoryZh() : ca.getCategoryEn());
          String searchKey = area.getShowTitle();
          if (ca.getParentCategroyId() != null && ca.getParentCategroyId() > 0L) {
            CategoryScm superCa = categoryScmDao.get(ca.getParentCategroyId());
            if (superCa != null) {
              String superName = ("zh_CN".equals(locale) ? superCa.getCategoryZh() : superCa.getCategoryEn());
              searchKey = area.getShowTitle() + ", " + superName;
            }
          }
          area.setSearchKey(searchKey);
          // SCM-14848 直接改动可能影响推荐基金功能，判断是否是我的基金请求
          String appendAreas = idList == null ? area.getSearchKey() : area.getShowTitle();
          if (StringUtils.isBlank(scienceAreaNames.toString())) {
            scienceAreaNames.append(appendAreas);
          } else {
            scienceAreaNames.append(", " + appendAreas);
          }
          areaList.add(area);
        }
      }
      form.setScienceAreaNames(scienceAreaNames.toString());
    }
    return areaList;
  }

  /**
   * 查询推荐基金列表
   * 
   * 1.要查询人员的学位，position或degreeName--------》CONST_POSITION表的grades字段
   * 
   * 2.v_fund_conditions表中的地区、申请资格、科技领域条件，某项为空的话，则用人员对应的信息替代
   * 
   * 3.时间限制：1周之内、一个月之内、三个月之内、不限（不限时要求结束时间至少在今天之后）
   * 
   * 注意： 该处检索条件或需求有改变的时候，一定要修改对应的推荐基金的后台任务的逻辑代码
   * 
   */
  @Override
  public FundRecommendForm fundRecommendListSearch(FundRecommendForm form) throws PrjException {
    try {
      Long psnId = form.getPsnId();
      if (psnId == null || psnId == 0L) {
        psnId = SecurityUtils.getCurrentUserId();
      }
      // 查询人员身份
      Integer grade = this.findPsnGrades(psnId);
      // 分页获取
      String agencyIds = "";
      String scienceAreas = "";
      Integer enterprise = null;
      Integer researchIns = null;
      Integer seniority = 0;
      Integer sortOrder = null;
      // 都为空按结题时间排序sortOrder按时间排序标志
      /*
       * if (isBlankSelectCondition(form)) { }
       */
      sortOrder = 1;
      Locale locale = LocaleContextHolder.getLocale();
      List<Long> excludeFundIds = buildExcludeFundIdList(form.getDes3FundIds(), psnId);
      form.setFundIdList(excludeFundIds);
      Integer timeCode =
          Optional.ofNullable(form).map(FundRecommendForm::getTimeCodeSelect).map(NumberUtils::toInt).orElse(null);
      // 有选中值取选中值，没有值按默认的选
      agencyIds = Optional.ofNullable(form).map(FundRecommendForm::getAgencyIdSelect)
          .orElseGet(() -> getPsnFundAgencyIdsList(form.getPsnId()));// 关注资助机构名称,为空按默认取
      scienceAreas = Optional.ofNullable(form).map(FundRecommendForm::getScienceCodeSelect)
          .map(a -> a.replaceAll(",", " ")).orElseGet(() -> getPsnSetAreaId(form.getPsnId()));// 科技领域为空按默认的取

      seniority = Optional.ofNullable(form).map(FundRecommendForm::getSeniorityCodeSelect).map(Integer::valueOf)
          .orElseGet(() -> getPsnSeniority(form.getPsnId()));// 申请资格为空按默认的取
      if (seniority == 1) {// 企业
        enterprise = 1;
      }
      if (seniority == 2) {// 科研单位
        researchIns = 1;
      }

      /*
       * String enScienceAreas = null; String zhScienceAreas = null; if
       * ("en_US".equals(locale.toString())) {// 获取当前的语言的科技领域和地区 enScienceAreas = scienceAreas; } else {
       * zhScienceAreas = scienceAreas; }
       */
      Map<String, Object> rsMap = solrIndexService.getRecommendFundRecommend(form.getPageNum(), form.getPageSize(),
          agencyIds, scienceAreas, grade, timeCode, enterprise, researchIns, sortOrder, excludeFundIds);

      /*
       * // 查询结果为空且第一次加载就把领域、资助机构、资格、时间条件限制去掉再查 if
       * ("0".equals(rsMap.get(SolrIndexSerivceImpl.RESULT_NUMFOUND). toString()) &&
       * isBlankSelectCondition(form)) { form.setFundsUnlimit(true); rsMap =
       * solrIndexService.getRecommendFundRecommend(form.getPageNum(), form.getPageSize(), "", "", "",
       * grade, null, null, null, 1, excludeFundIds);// 默认按结题时间排 }
       */

      String count = (String) rsMap.get(SolrIndexSerivceImpl.RESULT_NUMFOUND);
      String items = (String) rsMap.get(SolrIndexSerivceImpl.RESULT_ITEMS);
      if (StringUtils.isNotBlank(count)) {
        Integer num = NumberUtils.toInt(count);
        Integer total = num % form.getPageSize() > 0 ? num / form.getPageSize() + 1 : num / form.getPageSize();
        form.setTotalPages(total);
        form.setTotalCount(num);
      }
      // 构建基金信息
      List<Map<String, Object>> listItems = JacksonUtils.jsonToList(items);
      if (CollectionUtils.isNotEmpty(listItems)) {
        List<ConstFundCategoryInfo> infoList = new ArrayList<ConstFundCategoryInfo>();
        // 查找已收藏过的基金
        List<Long> collectIds = myFundDao.findCollectFundIds(form.getPsnId());
        // StringBuilder des3FundAgencyIds = new StringBuilder();
        List<String> des3FundAgencyIds = new ArrayList<>();
        for (Map<String, Object> item : listItems) {
          ConstFundCategoryInfo fund = new ConstFundCategoryInfo();
          // 构建显示信息 如果solr中有 数据库没有跳过这次循环 之前是会从solr重新构建基金信息
          if (this.buildFundInfo(item, fund, form) == null)
            continue;
          des3FundAgencyIds.add(Des3Utils.encodeToDes3(ObjectUtils.toString(item.get("fundAgencyId"))));
          this.buildFundInternationalInfo(fund);
          Long fundId = NumberUtils.toLong(ObjectUtils.toString(item.get("fundId")));
          // 是否已收藏过
          if (CollectionUtils.isNotEmpty(collectIds) && collectIds.contains(fundId)) {
            fund.setHasCollected(true);
          }
          // 是否赞过
          FundAward awardRecord = fundAwardDao.findFundAwardRecord(SecurityUtils.getCurrentUserId(), fundId);
          if (awardRecord != null && awardRecord.getStatus() == LikeStatusEnum.LIKE) {
            fund.setHasAward(true);
          }

          infoList.add(fund);
        }

        form.setDes3FundAgencyIds(StringUtils.join(des3FundAgencyIds, ","));
        form.setFundInfoList(infoList);
      }
    } catch (Exception e) {
      logger.error("查询基金推荐列表出错，" + this.buildSearchString(form), e);
      throw new PrjException(e);
    }
    return form;
  }

  private List<Long> buildExcludeFundIdList(String fundIds, Long psnId) {
    // 先排除用户不感兴趣的推荐论文
    List<Long> excludeFundIds = fundRecommendRecordDAO.getFundIdsByPsnId(psnId, 1);
    if (StringUtils.isNotBlank(fundIds)) {
      String[] splitFundIds = fundIds.split(",");
      for (String fundIdStr : splitFundIds) {
        long fundId = NumberUtils.toLong(Des3Utils.decodeFromDes3(fundIdStr));
        excludeFundIds.add(fundId);
      }
      if (excludeFundIds.size() > 1000) {
        excludeFundIds.subList(0, 1000);
      }
    }
    return excludeFundIds;
  }

  private boolean isBlankSelectCondition(FundRecommendForm form) {
    return form.getAgencyIdSelect() == null && form.getScienceCodeSelect() == null
        && (form.getSeniorityCodeSelect() == null || "0".equals(form.getSeniorityCodeSelect()))
        && (form.getTimeCodeSelect() == null || "0".equals(form.getTimeCodeSelect()));
  }

  /**
   * 获取用户设置的科技领域ID
   * 
   * @param locale
   * @param psnId
   * @return
   */
  private String getPsnSetAreaId(Long psnId) {
    List<Long> psnSetAreaId = fundRecommendAreaDao.getPsnSetAreaId(psnId);
    if (psnSetAreaId != null && psnSetAreaId.size() > 0) {
      return psnSetAreaId.stream().map(a -> a.toString()).collect(Collectors.joining(" "));
    }
    return "";
  }

  /**
   * 获取用户设置单位性质
   * 
   * @param locale
   * @param psnId
   * @return
   */
  private Integer getPsnSeniority(Long psnId) {
    FundRecommendSeniority psnSeniority = fundRecommendSeniorityDao.getSeniorityByPsnId(psnId);
    return psnSeniority != null ? psnSeniority.getCode() : 0;
  }

  /**
   * 获取用户选中的科技领域名称
   * 
   * @param locale
   * @param psnId
   * @param scienceCodeSelect
   * @return
   */
  private String getAreaName(Locale locale, Long psnId, String scienceCodeSelect) {
    List<String> namesList = new ArrayList<String>();
    if (StringUtils.isNotBlank(scienceCodeSelect)) {
      String[] codeList = StringUtils.split(scienceCodeSelect, ",");
      if (codeList.length > 0) {
        for (int i = 0; i < codeList.length; i++) {
          Long areaId = NumberUtils.toLong(StringUtils.trim(codeList[i]));
          if (areaId == 0) {
            continue;
          }
          FundRecommendArea fundArea = fundRecommendAreaDao.getFundRecommendArea(psnId, areaId);
          if (fundArea != null) {
            namesList.add(LocaleTextUtils.getLocaleText(locale, fundArea.getZhName(), fundArea.getEnName()));
          }
        }
      }
    }
    namesList = new ArrayList<String>(new HashSet<String>(namesList)); // 去重复
    return StringUtils.strip(namesList.toString(), "[]");
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

  /**
   * 用solr查询到的信息构建基金信息对象
   * 
   * @param item
   * @param fund
   * @return
   */
  private ConstFundCategoryInfo buildFundInfo(Map<String, Object> item, ConstFundCategoryInfo fund,
      FundRecommendForm form) {
    String locale = LocaleContextHolder.getLocale().toString();
    // 基金资助机构ID
    fund.setFundAgencyId(NumberUtils.toLong(ObjectUtils.toString(item.get("fundAgencyId"))));
    // 基金ID
    Long fundId = NumberUtils.toLong(ObjectUtils.toString(item.get("fundId")));
    ConstFundCategory constFundCategory = constFundCategoryDao.findFundName(fundId);
    // 数据库中不存在
    if (constFundCategory == null) {
      return null;
    }
    fund.setFundId(fundId);
    if (fundId != null) {
      fund.setEncryptedFundId(Des3Utils.encodeToDes3(fundId.toString()));
    }
    // 基金标题
    fund.setZhTitle(ObjectUtils.toString(constFundCategory.getNameZh()));
    fund.setEnTitle(ObjectUtils.toString(constFundCategory.getNameEn()));
    fund.setFundTitle(LocaleTextUtils.getStrByLocale(locale, fund.getZhTitle(), fund.getEnTitle()));
    // 开始时间
    String startDate = ObjectUtils.toString(item.get("fundStartDate"));
    fund.setStartDate(formateDate(startDate, ""));
    // 结束时间
    String endDate = ObjectUtils.toString(item.get("fundEndDate"));
    fund.setEndDate(formateDate(endDate, ""));
    fund.setShowDate(this.dealNullVal(buildFundTime(fund.getStartDate(), fund.getEndDate())));
    Date parseDate = parseDate(endDate);
    if (parseDate != null) {
      Date now = new Date();
      if (!now.after(parseDate)) {
        fund.setIsStaleDated(false);
      }
    }
    // 资助机构名称
    fund.setZhAgencyName(ObjectUtils.toString(item.get("fundAgencyNameZh")));
    fund.setEnAgencyName(ObjectUtils.toString(item.get("fundAgencyNameEn")));
    fund.setFundAgencyName(LocaleTextUtils.getStrByLocale(locale, fund.getZhAgencyName(), fund.getEnAgencyName()));
    // 科技领域
    String zhScienceArea = ObjectUtils.toString(item.get("fundCategoryStrZh"));
    if (StringUtils.isNotBlank(zhScienceArea)) {
      zhScienceArea = zhScienceArea.replace(" ", "").replace(",", "，");
    }
    fund.setZhScienceArea(zhScienceArea);
    fund.setEnScienceArea(ObjectUtils.toString(item.get("fundCategoryStrEn")));
    fund.setScienceAreas(LocaleTextUtils.getStrByLocale(locale, fund.getZhScienceArea(), fund.getEnScienceArea()));
    // 推荐分数
    fund.setScore(ObjectUtils.toString(item.get("score")));
    // 根据基金ID获取一些其他信息
    Long agencyId = NumberUtils.toLong(ObjectUtils.toString(item.get("fundAgencyId")));
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

  /**
   * 日志用
   * 
   * @param form
   * @return
   */
  private String buildSearchString(FundRecommendForm form) {
    String searchString = "psnId = " + SecurityUtils.getCurrentUserId();
    searchString += ", regionName = " + form.getRegionNames();
    searchString += ", scienceAreaIds = " + form.getScienceAreaIds();
    searchString += ", senority = " + form.getSeniority();
    searchString += ", timeFlag = " + form.getTimeFlag();
    return searchString;
  }

  @Override
  public List<ConstFundCategoryInfo> hasFundAwarded(List<ConstFundCategoryInfo> infoList, List<Long> fundIds,
      Long psnId) throws PrjException {
    try {
      // 先用fundIds和psnId查询人员对基金赞的状态
      List<FundAward> awardList = fundAwardDao.findAwardStatusList(psnId, fundIds);
      // 遍历查询结果，存在记录且status值为LikeStatusEnum.LIKE的表示赞过
      if (CollectionUtils.isNotEmpty(awardList)) {
        for (FundAward fund : awardList) {
          if (fund.getStatus() == LikeStatusEnum.LIKE) {
            for (ConstFundCategoryInfo info : infoList) {
              if (fund.getFundId().longValue() == info.getFundId().longValue()) {
                info.setHasAward(true);
              }
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("看是否赞过基金出错， psnId = " + psnId, e);
      throw new PrjException(e);
    }
    return infoList;
  }

  /**
   * 处理solr返回的时间字符串
   * 
   * @param dateStr
   * @param result
   * @return
   * @throws ParseException
   */
  private String formateDate(String dateStr, String result) {
    try {
      if (StringUtils.isNotBlank(dateStr)) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
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

  private Date parseDate(String dateStr) {
    Date result = null;
    try {
      if (StringUtils.isNotBlank(dateStr)) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
        result = sdf.parse(dateStr);
      }
    } catch (Exception e) {
      logger.error("时间转换出错， dateStr = " + dateStr, e);
    }
    return result;
  }

  @Override
  public void appBuildFundScienceAreaInfo(FundRecommendForm form) throws PrjException {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId != null && psnId > 0) {
      List<CategoryScm> categoryList = null;
      if (StringUtils.isBlank(form.getSearchKey())) {
        categoryList = categoryScmDao.appFindAllCategory();
      } else {
        categoryList = categoryScmDao.appFindCategoryByName(form.getSearchKey());
      }
      form.setCategoryScmList(categoryList);
    }
  }

  /**
   * 利用推荐的科技领域ID构建科技领域list
   * 
   * @param
   * @return
   */
  private List<FundScienceArea> getScienceAreaByDisc(String ids, List<FundScienceArea> fundScienceArea) {
    if (StringUtils.isNotBlank(ids)) {
      if (CollectionUtils.isEmpty(fundScienceArea)) {
        fundScienceArea = new ArrayList<FundScienceArea>();
      }
      Long psnId = SecurityUtils.getCurrentUserId();
      String locale = LocaleContextHolder.getLocale().toString();
      String[] scienceIds = ids.split(",");
      List<Long> idList = new ArrayList<Long>();
      if (scienceIds != null && scienceIds.length > 0) {
        for (String id : scienceIds) {
          idList.add(NumberUtils.toLong(id));
        }
      }
      List<CategoryScm> scienceArea = categoryScmDao.findCategoryScm(idList);
      if (CollectionUtils.isNotEmpty(scienceArea)) {
        for (CategoryScm ca : scienceArea) {
          FundScienceArea area = new FundScienceArea(psnId, NumberUtils.toLong(ca.getCategoryId().toString()),
              ca.getCategoryZh(), ca.getCategoryEn(), "");
          area.setShowTitle("zh_CN".equals(locale) ? area.getZhTitle() : area.getEnTitle());
          fundScienceArea.add(area);
        }
      }
    }
    return fundScienceArea;
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
        ConstPosition pos = constPositionDao.getPosByName(psn.getDegreeName());
        if (pos != null) {
          degree = pos.getGrades();
        }
      }
    }
    return degree;

  }

  @Override
  public FundRecommendForm buildFundDetailsInfo(FundRecommendForm form) throws PrjException {
    ConstFundCategoryInfo constFundCategoryInfo = form.getConstFundCategoryInfo();
    constFundCategoryInfo.setFundId(form.getFundId());
    if (form.getFundId() != null) {
      constFundCategoryInfo.setDetailsUrl(domainScm + "/prjweb/outside/showfund?encryptedFundId="
          + Des3Utils.encodeToDes3(form.getFundId().toString()));
    }
    ConstFundCategory constFundCategory = constFundCategoryDao.get(form.getFundId());
    if (constFundCategory != null) {
      buildFundName(constFundCategory, constFundCategoryInfo);// 构建基金名称
      buildFundYear(constFundCategory, constFundCategoryInfo);// 构建基金年度
      buildFundApplyDate(constFundCategory, constFundCategoryInfo);// 申请日期
      buildFundAgency(constFundCategory, constFundCategoryInfo);// 构建资助机构名称
      buildFundStrength(constFundCategory, constFundCategoryInfo);// 预计资助金额
      constFundCategoryInfo.setDescription(HtmlUtils.htmlUnescape(constFundCategory.getDescription()));// 类别描述
      buildFundMatch(constFundCategory, constFundCategoryInfo);// 是否配套
      buildFundPercentage(constFundCategory, constFundCategoryInfo);// 比例
      buildFundRegionName(constFundCategory, constFundCategoryInfo);// 适合地区
      buildFundFile(constFundCategory, constFundCategoryInfo);// 附件

      constFundCategoryInfo.setGuideUrl(StringUtils.trimToEmpty(constFundCategory.getGuideUrl()));// 申报指南网址
      constFundCategoryInfo.setDeclareUrl(StringUtils.trimToEmpty(constFundCategory.getDeclareUrl()));// 申报网址

      buildFundScienceArea(form.getFundId(), constFundCategoryInfo); // TODO 科技领域

      this.buildFundInternationalInfo(constFundCategoryInfo); // 构建分享所需信息
      // 构建左侧其他基金机会
      this.buildOtherFundsInfo(form);
    } else {
      return null;
    }
    return form;
  }

  private void buildOtherFundsInfo(FundRecommendForm form) throws PrjException {
    List<ConstFundCategoryInfo> otherFundList = new ArrayList<ConstFundCategoryInfo>();
    // 获取推荐给当前人员的其他五条基金
    List<Long> fundIds = psnFundRecommendDao.getFundIdList(form.getPsnId(), form.getFundId());
    if (CollectionUtils.isEmpty(fundIds)) {
      fundIds = constFundCategoryRcmdDao.getFundIdList(form.getFundId());
    }
    for (Long fundId : fundIds) {
      ConstFundCategory constFundCategory = constFundCategoryDao.get(fundId);
      ConstFundCategoryInfo fund = new ConstFundCategoryInfo();
      if (constFundCategory != null) {
        buildFundName(constFundCategory, fund);// 构建基金名称
        buildFundAgency(constFundCategory, fund);// 构建资助机构名称,logo
        buildFundScienceArea(fundId, fund); // 科技领域
        fund.setEncryptedFundId(Des3Utils.encodeToDes3(fundId.toString()));
        otherFundList.add(fund);
      }
    }
    form.setFundInfoList(otherFundList);
  }

  /**
   * 构建比例
   * 
   * @param constFundCategory
   * @param constFundCategoryInfo
   */
  private void buildFundPercentage(ConstFundCategory constFundCategory, ConstFundCategoryInfo constFundCategoryInfo) {
    String percentage = constFundCategory.getPercentage();
    if (StringUtils.isNotBlank(percentage)) {
      percentage += "%";
    }
    constFundCategoryInfo.setPercentage(percentage);
  }

  /**
   * 构建预计资助金额
   * 
   * @param constFundCategory
   * @param constFundCategoryInfo
   */
  private void buildFundStrength(ConstFundCategory constFundCategory, ConstFundCategoryInfo constFundCategoryInfo) {
    String strength = constFundCategory.getStrength();
    if (StringUtils.isNotBlank(strength)) {
      strength += "万元";
    }
    constFundCategoryInfo.setStrength(strength);
  }

  /**
   * 是否匹配
   * 
   * @param constFundCategory
   * @param constFundCategoryInfo
   */
  private void buildFundMatch(ConstFundCategory constFundCategory, ConstFundCategoryInfo constFundCategoryInfo) {
    String showIsMatch = "";
    Integer isMatch = constFundCategory.getIsMatch();
    Locale locale = LocaleContextHolder.getLocale();
    if (Locale.US.equals(locale)) {
      if (isMatch != null && isMatch == 1) {
        showIsMatch = "Yes";
      } else {
        showIsMatch = "No";
      }
    } else {
      if (isMatch != null && isMatch == 1) {
        showIsMatch = "是";
      } else {
        showIsMatch = "否";
      }
    }
    constFundCategoryInfo.setShowIsMatch(showIsMatch);
  }

  /**
   * 构建基金年度
   * 
   * @param constFundCategory
   * @param constFundCategoryInfo
   */
  private void buildFundYear(ConstFundCategory constFundCategory, ConstFundCategoryInfo constFundCategoryInfo) {
    String showYear = "";
    Long year = constFundCategory.getYear();
    if (year != null) {
      Locale locale = LocaleContextHolder.getLocale();
      if (Locale.US.equals(locale)) {
        showYear = year + "";
      } else {
        showYear = year + "年";
      }
    }
    constFundCategoryInfo.setShowYear(showYear);
  }

  /**
   * 构建附件信息
   * 
   * @param constFundCategory
   * @param constFundCategoryInfo
   */
  private void buildFundFile(ConstFundCategory constFundCategory, ConstFundCategoryInfo constFundCategoryInfo) {
    constFundCategoryInfo.setFundFileList(constFundCategoryFileDao.findFundFileByCategoryId(constFundCategory.getId()));
  }

  /**
   * 构建适合地区信息
   * 
   * @param constFundCategory
   * @param constFundCategoryInfo
   */
  private void buildFundRegionName(ConstFundCategory constFundCategory, ConstFundCategoryInfo constFundCategoryInfo) {
    String regionName = "";
    List<ConstFundCategoryRegion> viewNameList = constFundCategoryRegionDao.queryViewName(constFundCategory.getId());

    regionName = Optional.ofNullable(viewNameList).orElse(new ArrayList<ConstFundCategoryRegion>()).stream()
        .map(ConstFundCategoryRegion::getViewName).filter(StringUtils::isNotBlank).collect(Collectors.joining(", "));

    constFundCategoryInfo.setRegionName(regionName);
  }

  /**
   * 构建资助机构名称
   * 
   * @param constFundCategory
   * @param form
   */
  private void buildFundAgency(ConstFundCategory constFundCategory, ConstFundCategoryInfo constFundCategoryInfo) {
    Locale locale = LocaleContextHolder.getLocale();
    Long agencyId = constFundCategory.getAgencyId();
    if (agencyId != null) {
      ConstFundAgency constFundAgency = constFundAgencyDao.findFundAgencyInfo(agencyId);
      if (constFundAgency != null) {
        String enName = constFundAgency.getNameEn();
        String zhName = constFundAgency.getNameZh();
        constFundCategoryInfo.setZhAgencyName(zhName);
        constFundCategoryInfo.setEnAgencyName(enName);
        String fundAgencyName = "";
        if (Locale.US.equals(locale)) {
          if (StringUtils.isBlank(enName)) {
            fundAgencyName = zhName;
          } else {
            fundAgencyName = enName;
          }
        } else {
          if (StringUtils.isBlank(zhName)) {
            fundAgencyName = enName;
          } else {
            fundAgencyName = zhName;
          }
        }
        constFundCategoryInfo.setFundAgencyName(fundAgencyName);
        constFundCategoryInfo.setFundAgencyId(agencyId);
        if (StringUtils.isNotBlank(constFundAgency.getLogoUrl())) {
          if (constFundAgency.getLogoUrl().contains("http")) {
            constFundCategoryInfo.setLogoUrl(constFundAgency.getLogoUrl());
          } else {
            constFundCategoryInfo.setLogoUrl("/resmod" + constFundAgency.getLogoUrl());
          }
        }
      }
    }
  }

  /**
   * 构建基金申请日期
   * 
   * @param constFundCategory
   * @param form
   */
  private void buildFundApplyDate(ConstFundCategory constFundCategory, ConstFundCategoryInfo constFundCategoryInfo) {
    Date startDate = constFundCategory.getStartDate();
    Date endDate = constFundCategory.getEndDate();
    String showDate = "";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    if (startDate != null && endDate != null) {
      showDate = sdf.format(startDate) + " ~ " + sdf.format(endDate);
    } else if (startDate != null && endDate == null) {
      showDate = sdf.format(startDate) + " ~ ";
    } else if (startDate == null && endDate != null) {
      showDate = " ~ " + sdf.format(endDate);
    }
    constFundCategoryInfo.setShowDate(showDate);
  }

  /**
   * 构建基金名称
   * 
   * @param constFundCategory
   * @param form
   */
  private void buildFundName(ConstFundCategory constFundCategory, ConstFundCategoryInfo constFundCategoryInfo) {
    Locale locale = LocaleContextHolder.getLocale();
    String enName = constFundCategory.getNameEn();
    String zhName = constFundCategory.getNameZh();
    constFundCategoryInfo.setZhTitle(zhName);
    constFundCategoryInfo.setEnTitle(enName);
    String fundTitle = "";
    if (Locale.US.equals(locale)) {
      if (StringUtils.isBlank(enName)) {
        fundTitle = zhName;
      } else {
        fundTitle = enName;
      }
    } else {
      if (StringUtils.isBlank(zhName)) {
        fundTitle = enName;
      } else {
        fundTitle = zhName;
      }
    }
    constFundCategoryInfo.setFundTitle(fundTitle);
  }

  @Override
  public FundRecommendForm updateFundShareCount(FundRecommendForm form) throws PrjException {
    try {
      if (form.getFundId() != null && form.getFundId() > 0L) {
        Long psnId = SecurityUtils.getCurrentUserId();
        FundStatistics sta = fundStatisticsDao.findFundStatisticsById(form.getFundId());
        if (sta == null) {
          sta = new FundStatistics(form.getFundId(), 0, 0);
        }
        Integer shareCount = sta.getShareCount();
        if (shareCount == null) {
          sta.setShareCount(1);
        } else {
          sta.setShareCount(shareCount + 1);
        }
        fundStatisticsDao.save(sta);
        form.setShareCount(shareCount + 1);
      }
    } catch (Exception e) {
      logger.error("更新基金分享统计数出错， fundId = " + form.getFundId() + ", psnId = " + SecurityUtils.getCurrentUserId(), e);
      throw new PrjException(e);
    }
    return form;
  }

  @Override
  public Map<String, Object> initFundCollectedStatus(FundRecommendForm form) throws PrjException {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (StringUtils.isNotBlank(form.getDes3FundIds())) {
        String des3FundIds = form.getDes3FundIds();
        String[] fundIdArr = des3FundIds.split(",");
        // 获取需要初始化的基金ID List
        List<Long> fundIdList = new ArrayList<Long>();
        if (fundIdArr != null && fundIdArr.length > 0) {
          for (int i = 0; i < fundIdArr.length; i++) {
            fundIdList.add(NumberUtils.toLong(Des3Utils.decodeFromDes3(fundIdArr[i])));
          }
        }
        List<Long> ids = myFundDao.findCollectFundIds(fundIdList, SecurityUtils.getCurrentUserId());
        if (ids != null) {
          result.put("collectedFundId", StringUtils.join(ids.toArray(), ","));
        }
      }
      result.put("result", "success");
    } catch (Exception e) {
      logger.error("查询基金收藏状态出错， fundIds = " + form.getDes3FundIds() + ",  psnId = " + SecurityUtils.getCurrentUserId(),
          e);
      result.put("result", "error");
      throw new PrjException(e);
    }
    return result;
  }

  @Override
  public FundRecommendForm initFundOperations(FundRecommendForm form) throws PrjException {
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      Long fundId = form.getFundId();
      if (fundId != null && fundId > 0L) {
        if (psnId != null && psnId > 0L) {
          // 是否赞过
          FundAward awardRecord = fundAwardDao.findFundAwardRecord(psnId, fundId);
          if (awardRecord != null && awardRecord.getStatus() == LikeStatusEnum.LIKE) {
            form.setHasAward(true);
          }
          // 是否收藏过
          MyFund fund = myFundDao.findMyFund(psnId, fundId);
          if (fund != null) {
            form.setHasCollected(true);
          }

        }
        // 分享和赞统计数
        FundStatistics sta = fundStatisticsDao.findFundStatisticsById(fundId);
        if (sta != null) {
          form.setAwardCount(sta.getAwardCount());
          form.setShareCount(sta.getShareCount());
        }
      }
    } catch (Exception e) {
      logger.error("初始化基金操作失败， fundId = " + form.getFundId() + ", psnId = " + SecurityUtils.getCurrentUserId(), e);
      throw new PrjException(e);
    }
    return form;
  }

  /**
   * 构建基金科技领域信息
   * 
   * @param fundId
   * @param constFundCategoryInfo
   * @return
   */
  public void buildFundScienceArea(Long fundId, ConstFundCategoryInfo constFundCategoryInfo) {
    List<Long> scienceAreaIds = constFundCategoryDisDao.findFundDisciplineIds(fundId);
    if (CollectionUtils.isNotEmpty(scienceAreaIds)) {
      StringBuffer zhScienceAreaNames = new StringBuffer();
      StringBuffer enScienceAreaNames = new StringBuffer();
      // 获取科技领域
      List<CategoryScm> list = categoryScmDao.findCategoryScm(scienceAreaIds);
      if (CollectionUtils.isNotEmpty(list)) {
        // 构建科技领域信息
        for (CategoryScm ca : list) {
          String zhTitle = StringUtils.isNotBlank(ca.getCategoryZh()) ? ca.getCategoryZh() : ca.getCategoryEn();
          String enTitle = StringUtils.isNotBlank(ca.getCategoryEn()) ? ca.getCategoryEn() : ca.getCategoryZh();
          if (StringUtils.isBlank(zhScienceAreaNames.toString())) {
            zhScienceAreaNames.append(zhTitle);
          } else {
            zhScienceAreaNames.append(", " + zhTitle);
          }
          if (StringUtils.isBlank(enScienceAreaNames.toString())) {
            enScienceAreaNames.append(enTitle);
          } else {
            enScienceAreaNames.append(", " + enTitle);
          }
        }
        constFundCategoryInfo.setZhScienceArea(zhScienceAreaNames.toString());
        constFundCategoryInfo.setEnAgencyName(enScienceAreaNames.toString());
        String scienceAreaNames = "";
        if (Locale.US.equals(LocaleContextHolder.getLocale())) {
          if (StringUtils.isBlank(enScienceAreaNames.toString())) {
            scienceAreaNames = zhScienceAreaNames.toString();
          } else {
            scienceAreaNames = enScienceAreaNames.toString();
          }
        } else {
          if (StringUtils.isBlank(zhScienceAreaNames.toString())) {
            scienceAreaNames = enScienceAreaNames.toString();
          } else {
            scienceAreaNames = zhScienceAreaNames.toString();
          }
        }
        constFundCategoryInfo.setScienceAreas(scienceAreaNames);
      }
    }
  }


  @Override
  public List<Map<String, String>> getFundLogos(String[] des3FundAgencyIds) throws PrjException {
    List<Map<String, String>> fundLogos = new ArrayList<>();
    if (des3FundAgencyIds == null) {
      return fundLogos;
    }
    for (int i = 0; i < des3FundAgencyIds.length; i++) {
      Map<String, String> fundLogoInfo = new HashMap<>();
      String fundAgencyId = Des3Utils.decodeFromDes3(des3FundAgencyIds[i]);
      if (StringUtils.isNotBlank(fundAgencyId)) {
        fundLogoInfo.put("fundAgencyId", fundAgencyId);
        String logoUrl = constFundAgencyDao.findFundAgencyLogoUrl(Long.parseLong(fundAgencyId));
        if (StringUtils.isNotBlank(logoUrl)) {
          if (logoUrl.contains("http")) {
            fundLogoInfo.put("logoUrl", logoUrl);
          } else {
            fundLogoInfo.put("logoUrl", "/resmod" + (logoUrl == null ? "" : logoUrl));
          }
        }
      }
      fundLogos.add(fundLogoInfo);
    }
    return fundLogos;
  }

  @SuppressWarnings("unused")
  @Override
  public Map<String, Object> saveFundConditionsFundAgencyInterest(FundRecommendForm form) throws PrjException {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      String locale = LocaleContextHolder.getLocale().toString();
      List<FundAgencyInterest> fundAgencyInterestList = new ArrayList<FundAgencyInterest>();
      Long psnId = form.getPsnId();
      if (psnId > 0 && StringUtils.isNotBlank(form.getSaveAgencyIds())) {
        String[] saveAgencyIds = form.getSaveAgencyIds().split(",");
        List<Long> agencyIds = fundAgencyInterestDao.findAllInterestAgencyIds(psnId);
        if (CollectionUtils.isNotEmpty(agencyIds)) {
          agencyStatisticsDao.reduceAgencyListStatistics(agencyIds);// 统计关注数减一
        }
        fundAgencyInterestDao.deletePsnAllFundAgencyInteresByPsnId(psnId);// 先取消全部关注
        for (String agencyIdStr : saveAgencyIds) {
          int order = fundAgencyInterestList.size() + 1;
          Long agencyId = NumberUtils.toLong(agencyIdStr);
          FundAgencyInterest fundAgency = fundAgencyInterestDao.getFundAgencyInteresByPsnIdAndAgencyId(psnId, agencyId);
          if (fundAgency != null) {// 有重复就更新
            fundAgency.setUpdateDate(new Date());
            fundAgency.setStatus(1);
            fundAgency.setAgencyOrder(order);
            fundAgencyInterestDao.save(fundAgency);
            agencyStatisticsDao.addAgencyStatistics(agencyId);
            fundAgencyInterestList.add(fundAgency);
          } else {// 没有重复就插入记录
            saveFundRecommendAgency(psnId, agencyId, fundAgencyInterestList);
          }
        }
        result.put("result", "success");
      } else {
        result.put("errmsg", "至少保存一个资助机构");
        result.put("result", "error");
      }

    } catch (Exception e) {
      logger.error("保存基金推荐条件出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
      throw new PrjException(e);
    }
    return result;

  }

  /**
   * 根据机构id获取机构名称
   * 
   * @param agencyId
   * @return
   */
  public String getAgencyNameByAgencyIds(String agencyIds) {
    return Stream.of(agencyIds.split(",")).map(idstr -> NumberUtils.toLong(idstr)).filter(id -> id > 0)
        .map(id -> getAgencyName(id)).collect(Collectors.joining(","));
  }

  /**
   * 根据机构id获取机构名称
   * 
   * @param agencyId
   * @return
   */
  public String getAgencyName(Long agencyId) {
    ConstFundAgency agency = fundAgencyDao.get(agencyId);
    String locale = LocaleContextHolder.getLocale().toString();
    if ("en_US".equals(locale)) {
      return agency.getNameEn() != null ? agency.getNameEn() : agency.getNameZh();
    } else {
      return agency.getNameZh() != null ? agency.getNameZh() : agency.getNameEn();
    }
  }

  /**
   * 保存关注的资助机构
   * 
   */
  private void saveFundRecommendAgency(Long psnId, Long agencyId, List<FundAgencyInterest> fundAgencyInterestList) {
    Integer interesOrder = 0;
    if (fundAgencyInterestList != null) {
      interesOrder = fundAgencyInterestList.size();
    } else {
      fundAgencyInterestList = new ArrayList<FundAgencyInterest>();
    }
    if (interesOrder < 10) {
      FundAgencyInterest agency = new FundAgencyInterest();
      agency.setAgencyId(agencyId);
      agency.setCreateDate(new Date());
      agency.setUpdateDate(new Date());
      agency.setPsnId(psnId);
      agency.setAgencyOrder(interesOrder + 1);
      agency.setStatus(1);
      fundAgencyInterestDao.save(agency);
      agencyStatisticsDao.addAgencyStatistics(agencyId);
      fundAgencyInterestList.add(agency);
    }
  }

  private List<FundAgencyInterest> saveFundRecommendAgencyByAgencyList(List<ConstFundAgency> addAgencyList,
      Long psnId) {
    List<FundAgencyInterest> fundAgencyInterestList = new ArrayList<FundAgencyInterest>();
    if (CollectionUtils.isNotEmpty(addAgencyList)) {
      for (int i = 0; i < addAgencyList.size(); ++i) {
        ConstFundAgency addAgency = addAgencyList.get(i);
        if (i < 10 && addAgency != null) {
          FundAgencyInterest agencyInterest =
              fundAgencyInterestDao.getFundAgencyInteresByPsnIdAndAgencyId(psnId, addAgency.getId());
          if (agencyInterest == null) {
            agencyInterest = new FundAgencyInterest();
            agencyInterest.setAgencyId(addAgency.getId());
            agencyInterest.setCreateDate(new Date());
            agencyInterest.setUpdateDate(new Date());
            agencyInterest.setPsnId(psnId);
            agencyInterest.setAgencyOrder(i + 1);
            agencyInterest.setStatus(1);
            fundAgencyInterestDao.save(agencyInterest);
            agencyStatisticsDao.addAgencyStatistics(addAgency.getId());
            fundAgencyInterestList.add(agencyInterest);
          }
        }
      }
    }
    return fundAgencyInterestList;
  }

  @Override
  public List<FundRecommendArea> fundConditionsScienceAreaSave(FundRecommendForm form) throws PrjException {
    List<FundRecommendArea> areaList = new ArrayList<FundRecommendArea>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId > 0 && form.getAreaCodes() != null) {
        String[] codes = form.getAreaCodes().split(",");
        for (String code : codes) {// 添加多个科技领域
          Long areaCode = NumberUtils.toLong(code);
          fundRecommendAreaDao.deletePsnAllArea(psnId);// 删除个人所有的科技领域
          Long psnAreaNum = fundRecommendAreaDao.getPsnFundRecommendAreaSize(psnId);// 查询科技领域数
          if (psnAreaNum < 3) {// 科技领域小于3
            FundRecommendArea area = saveFundRecommendArea(psnId, areaCode);// 保存并返回area
            Optional.ofNullable(area).ifPresent((a) -> areaList.add(a));
          }
        }
      }
      return areaList;
    } catch (Exception e) {
      logger.error("保存基金推荐条件出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
      throw new PrjException(e);
    }

  }

  private FundRecommendArea saveFundRecommendArea(Long psnId, Long areaCode) {
    FundRecommendArea fundArea = null;
    if (psnId > 0 && areaCode != null) {
      fundArea = fundRecommendAreaDao.getFundRecommendArea(psnId, areaCode);
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
    return fundArea;
  }

  @Override
  public Map<String, String> deleteFundConditionAgencyInterest(Long psnId, String agencyIdstr) throws PrjException {
    Map<String, String> resultMap = new HashMap<String, String>();
    if (psnId > 0 && agencyIdstr != null) {
      Long agencyId = NumberUtils.toLong(agencyIdstr);
      FundAgencyInterest fundAgencyInterest =
          fundAgencyInterestDao.getFundAgencyInteresByPsnIdAndAgencyId(psnId, agencyId);
      if (fundAgencyInterest != null) {// 取消关注资助机构
        fundAgencyInterestDao.deleteFundAgencyInteresByPsnIdAndAgencyId(psnId, agencyId);
        agencyStatisticsDao.reduceAgencyStatistics(agencyId);
        resultMap.put("result", "success");
      }
    }
    return resultMap;
  }

  @Override
  public Map<String, String> deleteFundScienceArea(String areaCode) throws PrjException {
    Map<String, String> resultMap = new HashMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId > 0 && areaCode != null) {
      Long areaId = NumberUtils.toLong(areaCode);
      FundRecommendArea fundArea = fundRecommendAreaDao.getFundRecommendArea(psnId, areaId);
      if (fundArea != null) {
        fundRecommendAreaDao.delete(fundArea);
        resultMap.put("result", "success");
      }
    }
    return resultMap;
  }

  @Override
  public void fundConditionsSenioritySave(FundRecommendForm form) throws PrjException {
    FundRecommendSeniority fundSenior = fundRecommendSeniorityDao.getSeniorityByPsnId(form.getPsnId());
    Integer code = form.getSeniorityCode();
    if (fundSenior == null) {
      fundSenior = new FundRecommendSeniority();
      fundSenior.setCode(code);
      fundSenior.setPsnId(form.getPsnId());
      fundSenior.setUpdateDate(new Date());
      fundRecommendSeniorityDao.save(fundSenior);
    } else {
      fundRecommendSeniorityDao.updateSeniorityCode(form.getPsnId(), code);
    }
  }

  @Override
  public List<FundAgencyInterest> getPsnFundAgencyInterestList(Long psnId) throws Exception {
    List<FundAgencyInterest> fundAgencyInterestList = fundAgencyInterestDao.fundAgencyInteresByPsnId(psnId);
    setFundAgencyInterestsName(fundAgencyInterestList);
    return fundAgencyInterestList;
  }

  public String getPsnFundAgencyIdsList(Long psnId) {
    List<FundAgencyInterest> fundAgencyInterestList = fundAgencyInterestDao.fundAgencyInteresByPsnId(psnId);
    return Optional.ofNullable(fundAgencyInterestList).map(
        list -> list.stream().map(agency -> Objects.toString(agency.getAgencyId())).collect(Collectors.joining(",")))
        .orElseGet(() -> "");
  }

  @Override
  public void buildDynFundRecommend(FundRecommendForm form) {
    List<DynamicRemdForm> dynRemdRes = new ArrayList<DynamicRemdForm>();
    try {
      if (form.getFundInfoList() != null && form.getFundInfoList().size() > 0) {
        for (ConstFundCategoryInfo fund : form.getFundInfoList()) {
          DynamicRemdForm remd = new DynamicRemdForm();
          remd.setDes3ResId(fund.getEncryptedFundId());
          remd.setResId(fund.getFundId());
          remd.setResTitle(fund.getFundTitle());
          remd.setResAuthorNames(fund.getZhShowDesc());
          String defaultLogoUrl = "/ressns/images/default/default_fund_logo.jpg";

          if (fund.getFundAgencyId() != null) {
            String logoUrl = this.dealNullVal(constFundAgencyDao.findFundAgencyLogoUrl(fund.getFundAgencyId()));
            if (StringUtils.isNotBlank(logoUrl)) {
              if (logoUrl.contains("http")) {
                defaultLogoUrl = logoUrl;
              } else {
                defaultLogoUrl = "/resmod" + logoUrl;
              }
            }
          }

          remd.setFullTextUrl(defaultLogoUrl);
          remd.setType(2);
          dynRemdRes.add(remd);
        }
      }

    } catch (Exception e) {
      logger.error("构建首页推荐基金信息出错", e);
    }
    form.setDynRemdResList(dynRemdRes);
  }

  @Override
  public void insertFundRecmRecord(Long psnId, Long fundId) {
    try {
      FundRecommendRecord fundRecm = fundRecommendRecordDAO.findFundByFundIdAndPsnId(psnId, fundId);
      if (fundRecm == null) {
        fundRecm = new FundRecommendRecord();
        fundRecm.setPsnId(psnId);
        fundRecm.setFundId(fundId);
        fundRecm.setStatus(1);// 状态：0正常，1不感兴趣
        fundRecm.setGmtCreate(new Date());
        fundRecm.setGmtModified(new Date());
        fundRecommendRecordDAO.save(fundRecm);
      }
    } catch (Exception e) {
      logger.error("插入V_FNUD_RECOMMEND_RECORD表出错，psnId=" + psnId + ",fundId=" + fundId, e);
    }
  }

  @Override
  public void initAgencyInterestAndAreaSeniority(Long psnId) throws Exception {
    if (psnId > 0 && !hasInit(psnId)) {
      // 查询关注的资助机构
      List<FundAgencyInterest> fundAgencyInterestList = fundAgencyInterestDao.fundAgencyInteresByPsnId(psnId);
      // 查询设置的科技领域
      List<FundRecommendArea> fundAreaList = fundRecommendAreaDao.getFundRecommendAreaListByPsnId(psnId);
      // 查询申请资格
      FundRecommendSeniority fundSenior = fundRecommendSeniorityDao.getSeniorityByPsnId(psnId);
      if (CollectionUtils.isEmpty(fundAgencyInterestList)) {
        fundAgencyInterestList = initFundAgencyInterest(psnId);// 初始化关注的资助机构
      }
      if (CollectionUtils.isEmpty(fundAreaList)) {
        fundAreaList = initArea(psnId, fundAreaList);// 初始化科技领域
      }
      if (fundSenior == null) {
        initSeniority(psnId, fundSenior);// 初始化申请资格
      }
      saveRecommendInit(psnId);// 保存已经初始化的标志表
    }
  }

  @Override
  public FundRecommendForm updateFundReadCount(FundRecommendForm form) {
    FundStatistics sta = fundStatisticsDao.findFundStatisticsById(form.getFundId());
    if (sta == null) {
      sta = new FundStatistics(form.getFundId(), 0, 0);
      sta.setReadCount(1);
    } else {
      int readCount = sta.getReadCount() == null ? 0 : sta.getReadCount();
      sta.setReadCount(readCount + 1);
    }
    fundStatisticsDao.save(sta);
    form.setReadCount(sta.getReadCount());
    return form;
  }

  @Override
  public Map<String, String> buildFundScienceArea(Long fundId) {
    Map<String, String> area = new HashMap<String, String>();
    List<Long> scienceAreaIds = constFundCategoryDisDao.findFundDisciplineIds(fundId);
    if (CollectionUtils.isNotEmpty(scienceAreaIds)) {
      StringBuffer zhScienceAreaNames = new StringBuffer();
      StringBuffer enScienceAreaNames = new StringBuffer();
      // 获取科技领域
      List<CategoryScm> list = categoryScmDao.findCategoryScm(scienceAreaIds);
      if (CollectionUtils.isNotEmpty(list)) {
        // 构建科技领域信息
        for (CategoryScm ca : list) {
          String zhTitle = StringUtils.isNotBlank(ca.getCategoryZh()) ? ca.getCategoryZh() : ca.getCategoryEn();
          String enTitle = StringUtils.isNotBlank(ca.getCategoryEn()) ? ca.getCategoryEn() : ca.getCategoryZh();
          if (StringUtils.isBlank(zhScienceAreaNames.toString())) {
            zhScienceAreaNames.append(zhTitle);
          } else {
            zhScienceAreaNames.append(", " + zhTitle);
          }
          if (StringUtils.isBlank(enScienceAreaNames.toString())) {
            enScienceAreaNames.append(enTitle);
          } else {
            enScienceAreaNames.append(", " + enTitle);
          }
        }
        area.put("zhNames", zhScienceAreaNames.toString());
        area.put("enNames", enScienceAreaNames.toString());
      }
    }
    return area;
  }


}
