package com.smate.web.fund.service.wechat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.utils.common.LocaleTextUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.fund.dao.wechat.ConstDisplineDao;
import com.smate.web.fund.dao.wechat.ConstFundCategoryDao;
import com.smate.web.fund.dao.wechat.ConstFundCategoryRegionDao;
import com.smate.web.fund.model.common.ConstFundAgency;
import com.smate.web.fund.recommend.dao.ConstFundAgencyDao;
import com.smate.web.fund.recommend.dao.FundAwardDao;
import com.smate.web.fund.recommend.dao.FundStatisticsDao;
import com.smate.web.fund.recommend.dao.MyFundDao;
import com.smate.web.fund.recommend.model.FundAward;
import com.smate.web.fund.recommend.model.FundStatistics;
import com.smate.web.fund.service.recommend.FundRecommendService;
import com.smate.web.prj.exception.FundExcetpion;
import com.smate.web.prj.exception.PrjException;
import com.smate.web.prj.form.wechat.FundWeChatForm;
import com.smate.web.prj.model.wechat.FundWeChat;

/**
 * 基金查询服务类
 * 
 * @author zk
 *
 */
@Service("fundQueryService")
@Transactional(rollbackFor = Exception.class)
public class FundQueryServiceImpl implements FundQueryService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ConstFundCategoryRegionDao constFundCategoryRegionDao;
  @Autowired
  private ConstDisplineDao constDisplineDao;
  @Autowired
  private ConstFundCategoryDao constFundCategoryDao;
  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;
  @Autowired
  private FundRecommendService fundRecommendService;
  @Autowired
  private MyFundDao myFundDao;
  @Autowired
  private FundAwardDao fundAwardDao;
  @Autowired
  private FundStatisticsDao fundStatisticsDao;

  @Override
  public void queryfundinfo(FundWeChatForm form) throws FundExcetpion {
    try {
      FundWeChat fundwechat = new FundWeChat();
      constFundCategoryDao.queryFundINfo(form);// 获取资助类别
      String locale = LocaleContextHolder.getLocale().toString();
      if (form.getResultList() != null && form.getResultList().size() > 0) {
        fundwechat = form.getResultList().get(0);
        ConstFundAgency fundAgence = constFundAgencyDao.findFundAgencyInfo(fundwechat.getFundAgencyId());
        fundwechat.setFundAgency(fundAgence.getNameZh());
        fundwechat.setFundAgencyEn(fundAgence.getNameEn());
        Long fundId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3FundId()));
        fundwechat.setFundId(Objects.toString(fundId));
        fundwechat.setEncryptedFundId(form.getDes3FundId());
        // fundwechat.setDiscipline(constDisplineDao.querydiscipline(form));
        Map<String, String> areas = fundRecommendService
            .buildFundScienceArea(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3FundId())));
        if (areas != null) {
          fundwechat.setDiscipline("zh_CN".equals(locale) ? areas.get("zhNames") : areas.get("enNames"));
        }
        fundwechat.setViewName(constFundCategoryRegionDao.queryFundRegion(form));
        builFundWeChat(fundwechat, fundId);// 查询赞分享收藏的一些数据
        buildFundInternationalInfo(fundwechat);// 构建分享信息
        fundwechat.setFundName(StringEscapeUtils.unescapeHtml4(fundwechat.getFundName()));
        List<FundWeChat> fund = new ArrayList<FundWeChat>();
        fund.add(fundwechat);
        form.setResultList(fund);
      }
    } catch (Exception e) {
      logger.error("移动端查询基金信息出错 fundId = " + form.getDes3FundId(), e);
    }
  }

  /**
   * 构建基金国际化信息，分享的时候有用到
   * 
   * @throws PrjException
   */
  private void buildFundInternationalInfo(FundWeChat fund) throws PrjException {
    // TODO 科技领域
    Map<String, String> descInfo = fundRecommendService.buildFundScienceArea(NumberUtils.toLong(fund.getFundId()));
    String zhFundAgency = LocaleTextUtils.getStrByLocale("zh_CN", fund.getFundAgency(), fund.getFundAgencyEn());
    String zhScienceArea = LocaleTextUtils.getStrByLocale("zh_CN", descInfo.get("zhNames"), descInfo.get("enNames"));
    String enFundAgency = LocaleTextUtils.getStrByLocale("en_US", fund.getFundAgency(), fund.getFundAgencyEn());
    String enScienceArea = LocaleTextUtils.getStrByLocale("en_US", descInfo.get("zhNames"), descInfo.get("enNames"));
    fund.setZhShowDesc(this.getFundShowDescByLocale(zhFundAgency, zhScienceArea, fund.getTime(), "zh_CN"));
    fund.setEnShowDesc(this.getFundShowDescByLocale(enFundAgency, enScienceArea, fund.getTime(), "en_US"));
    fund.setZhShowDescBr(this.getFundShowDescByBr(zhFundAgency, zhScienceArea, fund.getTime()));
    fund.setEnShowDescBr(this.getFundShowDescByBr(enFundAgency, enScienceArea, fund.getTime()));
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

  private String getFundShowDescByLocale(String fundAgency, String scienceArea, String applyTime, String locale) {
    String showDesc = "";
    String joinStr = ", ";
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

  private void builFundWeChat(FundWeChat fundwechat, Long fundId) {
    Long psnId = SecurityUtils.getCurrentUserId();
    // 查找已收藏过的基金
    List<Long> collectIds = myFundDao.findCollectFundIds(psnId);
    // 是否已收藏过
    if (CollectionUtils.isNotEmpty(collectIds) && collectIds.contains(fundId)) {
      fundwechat.setHasCollected(true);
    }
    // 是否赞过
    FundAward awardRecord = fundAwardDao.findFundAwardRecord(psnId, fundId);
    if (awardRecord != null && awardRecord.getStatus() == LikeStatusEnum.LIKE) {
      fundwechat.setHasAward(true);
    }
    // 统计数
    FundStatistics sta = fundStatisticsDao.findFundStatisticsById(fundId);
    if (sta != null) {
      fundwechat.setAwardCount(sta.getAwardCount());
      fundwechat.setShareCount(sta.getShareCount());
    } else {
      fundwechat.setAwardCount(0);
      fundwechat.setShareCount(0);
    }
  }

  @Override
  public void queryFundInfoForShare(FundWeChatForm form) throws FundExcetpion {
    try {
      constFundCategoryDao.queryFundINfo(form);
      if (CollectionUtils.isNotEmpty(form.getResultList())) {
        FundWeChat fundwechat = form.getResultList().get(0);
        ConstFundAgency fundAgence = constFundAgencyDao.findFundAgencyInfo(fundwechat.getFundAgencyId());
        if (fundAgence != null) {
          if (StringUtils.isNotBlank(fundAgence.getLogoUrl())) {
            fundwechat.setLogoUrl(fundAgence.getLogoUrl().contains("images_v5") ? "/resmod" + fundAgence.getLogoUrl()
                : fundAgence.getLogoUrl());
          }
          fundwechat.setFundAgency(fundAgence.getNameZh());
          fundwechat.setFundAgencyEn(fundAgence.getNameEn());
          buildFundInternationalInfo(fundwechat);// 构建分享信息
        }
        fundwechat.setFundName(StringEscapeUtils.unescapeHtml4(fundwechat.getFundName()));
        List<FundWeChat> fund = new ArrayList<FundWeChat>();
        fund.add(fundwechat);
        form.setResultList(fund);
      }
    } catch (Exception e) {
      logger.error("移动端查询基金信息异常， fundId = {}", form.getDes3FundId(), e);
      throw new FundExcetpion(e);
    }

  }
}
