package com.smate.web.fund.service.wechat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.fund.agency.dao.CategoryScmDao;
import com.smate.web.fund.agency.dao.FundAgencyInterestDao;
import com.smate.web.fund.agency.model.FundAgencyForm;
import com.smate.web.fund.agency.model.FundAgencyInterest;
import com.smate.web.fund.model.common.CategoryScm;
import com.smate.web.fund.model.common.ConstFundCategoryInfo;
import com.smate.web.fund.recommend.dao.FundRecommendAreaDao;
import com.smate.web.fund.recommend.dao.FundRecommendRegionDao;
import com.smate.web.fund.recommend.model.FundRecommendArea;
import com.smate.web.fund.recommend.model.FundRecommendForm;
import com.smate.web.fund.recommend.model.FundRecommendRegion;
import com.smate.web.fund.service.agency.FundAgencyService;
import com.smate.web.fund.service.recommend.FundRecommendService;
import com.smate.web.prj.exception.FundExcetpion;
import com.smate.web.prj.exception.PrjException;
import com.smate.web.prj.form.wechat.FundWeChatForm;
import com.smate.web.prj.model.wechat.FundWeChat;

/**
 * 基金-wechat-查询服务类
 * 
 * @author zk
 * @since 6.0.1
 */
@Service("fundWeChatQueryService")
@Transactional(rollbackFor = Exception.class)
public class FundWeChatQueryServiceImpl implements FundWeChatQueryService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private FundQueryService fundQueryService;
  @Autowired
  private FundRecommendService fundRecommendService;
  @Autowired
  private CategoryScmDao categoryScmDao;
  @Autowired
  private FundRecommendAreaDao fundRecommendAreaDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private FundRecommendRegionDao fundRecommendRegionDao;
  @Autowired
  private FundAgencyInterestDao fundAgencyInterestDao;
  @Autowired
  private FundAgencyService fundAgencyService;

  @Override
  public void queryFundForWeChat(FundWeChatForm form) throws Exception {
    if (form.getPsnId() != null) {
      // constFundCategoryDao.queryFundINfosForWechat(form);
      FundRecommendForm fundForm = buildFundRecommendForm(form);
      fundForm = fundRecommendService.fundRecommendConditionsShow(fundForm);// 初始化查询条件
      fundForm = fundRecommendService.fundRecommendListSearch(fundForm);// 获取查询结果
      form.setTotalCount(fundForm.getTotalCount());
      form.setTotalPages(fundForm.getTotalPages());
      form.setPageNum(fundForm.getPageNum());
      form.setDes3FundAgencyIds(fundForm.getDes3FundAgencyIds());// 用来加载基金logo用
      if (fundForm != null && CollectionUtils.isNotEmpty(fundForm.getFundInfoList())) {
        List<FundWeChat> fundList = new ArrayList<FundWeChat>();
        for (ConstFundCategoryInfo info : fundForm.getFundInfoList()) {
          FundWeChat fund = new FundWeChat();
          fund.setFundId(info.getFundId().toString());
          fund.setFundAgencyId(info.getFundAgencyId());
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
      // this.handleFundInfo(form);
    }
  }

  private FundRecommendForm buildFundRecommendForm(FundWeChatForm form) {
    FundRecommendForm fundForm = new FundRecommendForm();
    fundForm.setFromMobile(true);
    fundForm.setFirstIn(true);
    fundForm.setPageNum(form.getPage().getPageNo());
    fundForm.setPageSize(10);
    fundForm.setPsnId(form.getPsnId());
    fundForm.setScienceCodeSelect(form.getSearchAreaCodes());// 构造查询条件
    fundForm.setAgencyIdSelect(form.getSearchAgencyId());
    fundForm.setSeniorityCodeSelect(form.getSearchseniority());
    fundForm.setTimeCodeSelect(form.getSearchTimeCodes());
    return fundForm;
  }

  @Override
  public void queryFundxml(FundWeChatForm form) throws Exception {
    if (form.getDes3FundId() != null) {
      logger.info("微信--基金详情des3FundId=" + form.getDes3FundId());
      fundQueryService.queryfundinfo(form);
    } else {
      throw new FundExcetpion();
    }

  }

  /**
   * 处理基金信息
   * 
   * @param form
   * @throws FundExcetpion
   * @throws ParseException
   */
  @Deprecated
  private void handleFundInfo(FundWeChatForm form) throws Exception {
    Date date = new Date();
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Date timeNow = format.parse(format.format(date));
    if (form.getResultList().size() > 0) {
      for (FundWeChat fundWeChat : form.getResultList()) {
        Date enddate = format.parse(format.format(fundWeChat.getEndDate()));
        Date startdate = format.parse(format.format(fundWeChat.getStartDate()));
        Calendar cal = Calendar.getInstance();
        cal.setTime(enddate);
        long time1 = cal.getTimeInMillis(); // 转换成长整形的结束时间
        cal.setTime(startdate);
        long time2 = cal.getTimeInMillis();// 转换成长整形的开始时间
        cal.setTime(timeNow);
        long time3 = cal.getTimeInMillis();// 转换成长整形的此刻的时间
        int day1 = (int) ((time3 - time1) / (1000 * 3600 * 24));// 此刻时间和截止时间之差
        int day2 = (int) ((time3 - time2) / (1000 * 3600 * 24));// 此刻时间和开始之差
        int day3 = (int) ((time1 - time2) / (1000 * 3600 * 24));// 开始时间和截止时间之差
        if (day3 < 60) {// 如果开始截至之间天数小于60
          if (day1 < 0 && day2 > 0 && Math.abs(day1) > day3 / 3 || day2 == 0 || day1 == 0) {
            fundWeChat.setStatus("1");// 正在申请 在开始和即将截止之间
          }
          if (day1 < 0 && Math.abs(day1) < day3 / 3) {
            fundWeChat.setStatus("2");// 即将截止
                                      // 此刻时间在结束时间前并且在结束时间的开始结束时间差的1/3天内
          }
        } else {// 如果开始截至日期大于60
          if (day1 < 0 && day2 > 0 && Math.abs(day1) > 30 || day2 == 0 || day1 == 0) {
            fundWeChat.setStatus("1");// 正在申请 在截止时间30前
          }
          if (day1 < 0 && Math.abs(day1) < 30) {
            fundWeChat.setStatus("2");// 即将截止 在截止时间30内
          }
        }
        /*
         * if(day1>0){ fundWeChat.setStatus("3");//已截止 此刻时间在结束时间之后 }
         */
        if (day2 < 0 && Math.abs(day2) < 30) {
          fundWeChat.setStatus("0");// 即将开始申请
        }
        fundWeChat.setTime(format.format(startdate) + " ~ " + format.format(enddate));

      }
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
  public void getFundCondition(FundWeChatForm form) throws PrjException {
    FundRecommendForm fundForm = new FundRecommendForm();
    fundForm.setPsnId(form.getPsnId());
    fundRecommendService.fundRecommendConditionsShow(fundForm);
    form.setFundAreaList(fundForm.getFundAreaList());
    form.setDefultArea(getDefultArea(fundForm.getFundAreaList()));// 获取科技领域code，用,分隔
    form.setFundAgencyInterestList(fundForm.getFundAgencyInterestList());
    form.setDefaultAgencyId(getDefultRegion(fundForm.getFundAgencyInterestList()));// 获取地区code，用,分隔
    form.setSeniorityCode(fundForm.getSeniorityCode());
    if ("".equals(form.getSearchseniority())) {
      form.setSearchseniority(Objects.toString(fundForm.getSeniorityCode()));
    }
  }

  /**
   * 拼接设置的科技领域code
   * 
   * @param areaList
   * @return
   */
  private String getDefultArea(List<FundRecommendArea> areaList) {
    return Optional.ofNullable(areaList).map(list -> list.stream().map(FundRecommendArea::getScienceAreaId)
        .map(Objects::toString).collect(Collectors.joining(","))).orElseGet(() -> "");
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

  @Override
  public void initFundCondition(FundWeChatForm form) throws PrjException {
    FundRecommendForm fundForm = new FundRecommendForm();
    fundForm.setPsnId(form.getPsnId());
    fundRecommendService.fundRecommendConditionsShow(fundForm);
    form.setFundAgencyInterestList(fundForm.getFundAgencyInterestList());
  }

  @Override
  public FundWeChatForm searchMyFund(FundWeChatForm form) throws PrjException {
    FundRecommendForm fundForm = new FundRecommendForm();
    fundForm.setPage(form.getPage());
    fundForm.setPsnId(form.getPsnId());
    fundForm.setSearchKey(form.getSearchKey());
    fundForm = fundRecommendService.searchMyFund(fundForm);
    List<FundWeChat> fundList = new ArrayList<FundWeChat>();
    form.setPage(fundForm.getPage());
    Optional.ofNullable(fundForm.getFundInfoList()).map(list -> list.stream()).orElseGet(() -> null).forEach(info -> {// 遍历list把ConstFundCategoryInfo转到FundWeChat
      FundWeChat fund = new FundWeChat();
      fund.setFundId(info.getFundId().toString());
      fund.setFundAgencyId(info.getFundAgencyId());
      fund.setEncryptedFundId(Des3Utils.encodeToDes3(info.getFundId().toString()));
      fund.setFundAgency(info.getFundAgencyName());
      fund.setFundName(StringEscapeUtils.unescapeHtml4(info.getFundTitle()));
      fund.setShowDate(info.getShowDate());
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
      fund.setLogoUrl(info.getLogoUrl());
      fundList.add(fund);
    });
    form.setResultList(fundList);
    return form;
  }

  @Override
  public void getAllScienceArea(FundWeChatForm form) throws PrjException {
    List<Map<String, Object>> allScienceAreaList = new ArrayList<Map<String, Object>>();
    List<CategoryScm> firstAreaList = categoryScmDao.findFirstScienceArea();
    Map<String, Object> areaItemMap = null;
    for (CategoryScm item : firstAreaList) {
      areaItemMap = new HashMap<String, Object>();
      areaItemMap.put("first", item);
      List<CategoryScm> secondAreaList = categoryScmDao.findSecondScienceArea(item.getCategoryId());
      areaItemMap.put("second", secondAreaList);
      allScienceAreaList.add(areaItemMap);
    }
    form.setAllScienceAreaList(allScienceAreaList);
  }

  @Override
  public void savePsnArea(FundWeChatForm form) throws PrjException {
    Long psnId = form.getPsnId();
    String[] areaArray = Optional.ofNullable(form.getDefultArea()).map(str -> StringUtils.split(str, ","))
        .orElseGet(() -> new String[0]);
    List<FundRecommendArea> recommendareaList = fundRecommendAreaDao.getFundRecommendAreaListByPsnId(form.getPsnId());// 获取个人所有的科技领域
    fundRecommendAreaDao.deletePsnAllArea(form.getPsnId());// 删除所有
    int addNum = 0;
    for (int i = 0; i < areaArray.length && addNum < 3; i++) {// 最多保存3个科技领域
      Integer areaId = NumberUtils.toInt(areaArray[i], 0);
      CategoryScm cate = categoryScmDao.get(areaId.longValue());
      if (cate != null) {
        boolean isfind = false;
        for (FundRecommendArea item : recommendareaList) {
          if (item.getScienceAreaId().equals(areaId.longValue())) {// 存在就保存
            addScienceArea(item);
            isfind = true;
            ++addNum;
          }
        }
        if (!isfind) {// 找不到就添加
          addScienceArea(cate, psnId);
          ++addNum;
        }
      }
    }
  }

  private void addScienceArea(FundRecommendArea item) {
    FundRecommendArea area = new FundRecommendArea();
    area.setEnName(item.getEnName());
    area.setZhName(item.getZhName());
    area.setParentId(item.getParentId());
    area.setScienceAreaId(item.getScienceAreaId());
    area.setPsnId(item.getPsnId());
    area.setUpdateDate(item.getUpdateDate());
    fundRecommendAreaDao.save(area);
  }

  private void addScienceArea(CategoryScm cate, Long psnId) {
    FundRecommendArea area = new FundRecommendArea();
    area.setEnName(cate.getCategoryEn());
    area.setZhName(cate.getCategoryZh());
    area.setParentId(cate.getParentCategroyId());
    area.setScienceAreaId(cate.getCategoryId());
    area.setPsnId(psnId);
    area.setUpdateDate(new Date());
    fundRecommendAreaDao.save(area);
  }

  @Override
  public void queryAllProvince(FundWeChatForm form) throws PrjException {
    Long chinaRegId = 156L;// 中国编号
    List<ConstRegion> allProvinceList = constRegionDao.findRegionData(chinaRegId);
    form.setAllProvinceList(allProvinceList);
  }

  @Override
  public void queryAreaNext(FundWeChatForm form) throws PrjException {
    List<ConstRegion> areaNextList = new ArrayList<ConstRegion>();
    ConstRegion constRegion = constRegionDao.get(form.getSuperRegionId());
    Optional.ofNullable(constRegion).ifPresent(c -> areaNextList.add(c));
    List<ConstRegion> nextList = constRegionDao.findRegionData(form.getSuperRegionId());
    Optional.ofNullable(nextList).ifPresent(n -> areaNextList.addAll(nextList));
    form.setAreaNextList(areaNextList);
  }

  @Override
  public List<FundRecommendRegion> getFundRegion(Long psnId) throws PrjException {
    return fundRecommendRegionDao.getFundRegionListByPsnId(psnId);
  }

  @Override
  public List<FundAgencyInterest> getFundAgencyInterest(Long psnId) throws PrjException {
    try {
      return fundRecommendService.getPsnFundAgencyInterestList(psnId);
    } catch (Exception e) {
      logger.error("获取人员的关注资助机构出错，psnId=" + psnId, e);
      throw new PrjException(e);
    }
  }


  @Override
  public void searchRegion(FundWeChatForm form) throws PrjException {
    List<ConstRegion> searchRegionList = constRegionDao.searchForConstRegion(form.getSearchKey(), 10);
    form.setSearchRegionList(searchRegionList);
  }

  @Override
  public void editFundAgencyInterest(FundWeChatForm form) throws PrjException {
    try {
      FundAgencyForm fundForm = new FundAgencyForm();
      fundForm.setPsnId(form.getPsnId());
      fundAgencyService.buildFundMapBaseInfo(fundForm);// 获取所有的资助机构
      form.setFundAgencyMapList(fundForm.getFundAgencyMapList());
      form.setFundAgencyInterestList(fundForm.getSelectFundAgencyList());// 获取关注的资助机构
    } catch (Exception e) {
      logger.error("获取所有的资助机构出错，psnId=" + form.getPsnId(), e);
      throw new PrjException(e);
    }
  }

  @Override
  public Map<String, Object> savePsnAgency(FundWeChatForm form) throws PrjException {
    FundRecommendForm fundForm = new FundRecommendForm();
    fundForm.setPsnId(form.getPsnId());
    fundForm.setSaveAgencyIds(form.getSaveAgencyIds());
    Map<String, Object> result = fundRecommendService.saveFundConditionsFundAgencyInterest(fundForm);
    return result;
  }
}
