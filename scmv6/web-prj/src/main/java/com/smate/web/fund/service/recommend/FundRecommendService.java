package com.smate.web.fund.service.recommend;

import java.util.List;
import java.util.Map;

import com.smate.web.fund.agency.model.FundAgencyInterest;
import com.smate.web.fund.model.common.ConstFundCategoryInfo;
import com.smate.web.fund.recommend.model.FundRecommendArea;
import com.smate.web.fund.recommend.model.FundRecommendForm;
import com.smate.web.prj.exception.PrjException;

/**
 * 基金推荐服务接口
 * 
 * @author WSN
 *
 *         2017年8月18日 上午10:21:24
 *
 */
public interface FundRecommendService {

  /**
   * 分页查询我的基金
   * 
   * @param form
   * @return
   * @throws PrjException
   */
  public FundRecommendForm searchMyFund(FundRecommendForm form) throws PrjException;

  /**
   * 基金赞/取消赞操作
   * 
   * @param form
   * @return
   * @throws PrjException
   */
  public FundRecommendForm fundAwardOperation(FundRecommendForm form) throws PrjException;

  /**
   * 收藏/取消收藏操作
   * 
   * @param form
   * @return
   * @throws PrjException
   */
  public FundRecommendForm fundCollectOperation(FundRecommendForm form) throws PrjException;

  /**
   * 基金推荐条件关注资助机构保存操作
   * 
   * @param form
   * @throws PrjException
   */
  public Map<String, Object> saveFundConditionsFundAgencyInterest(FundRecommendForm form) throws PrjException;

  /**
   * 删除推荐条件关注的资助机构
   * 
   * @param regionCode
   * @return
   * @throws PrjException
   */
  public Map<String, String> deleteFundConditionAgencyInterest(Long psnId, String agencyId) throws PrjException;

  /**
   * 保存推荐条件申请资格
   * 
   * @param regionCode
   * @return
   * @throws PrjException
   */
  public void fundConditionsSenioritySave(FundRecommendForm form) throws PrjException;

  /**
   * 基金推荐条件科技领域保存操作
   * 
   * @param form
   * @throws PrjException
   */
  public List<FundRecommendArea> fundConditionsScienceAreaSave(FundRecommendForm form) throws PrjException;

  /**
   * 删除基金条件科技领域
   * 
   * @param areaCode
   * @return
   * @throws PrjException
   */
  public Map<String, String> deleteFundScienceArea(String areaCode) throws PrjException;

  /**
   * 显示基金推荐条件
   * 
   * @param form
   * @return
   * @throws PrjException
   */
  public FundRecommendForm fundRecommendConditionsShow(FundRecommendForm form) throws PrjException;

  /**
   * 初始化基金推荐条件
   * 
   * @param form
   * @return
   * @throws PrjException
   */
  public void initPsnRecommendFund(Long psnId) throws PrjException;

  /**
   * 查询基金推荐列表
   * 
   * @param form
   * @return
   * @throws PrjException
   */
  public FundRecommendForm fundRecommendListSearch(FundRecommendForm form) throws PrjException;

  /**
   * 是否赞过基金
   * 
   * @param infoList
   * @param psnId
   * @return
   * @throws PrjException
   */
  public List<ConstFundCategoryInfo> hasFundAwarded(List<ConstFundCategoryInfo> infoList, List<Long> fundIds,
      Long psnId) throws PrjException;

  /**
   * 构建基金详情信息
   * 
   * @param form
   * @return
   * @throws PrjException
   */
  public FundRecommendForm buildFundDetailsInfo(FundRecommendForm form) throws PrjException;

  /**
   * 更新基金分享统计数
   * 
   * @param form
   * @return
   * @throws PrjException
   */
  public FundRecommendForm updateFundShareCount(FundRecommendForm form) throws PrjException;

  /**
   * 初始化基金收藏状态
   * 
   * @param form
   * @return
   * @throws PrjException
   */
  public Map<String, Object> initFundCollectedStatus(FundRecommendForm form) throws PrjException;

  /**
   * 初始化基金操作信息
   * 
   * @param form
   * @return
   * @throws PrjException
   */
  public FundRecommendForm initFundOperations(FundRecommendForm form) throws PrjException;



  /**
   * 根据fundId获取logo信息
   * 
   * @param des3FundIds
   * @return
   * @throws PrjException
   */
  public List<Map<String, String>> getFundLogos(String[] des3FundIds) throws PrjException;

  public void appBuildFundScienceAreaInfo(FundRecommendForm form) throws PrjException;

  public void showMyFund(FundRecommendForm form) throws Exception;

  /**
   * 获取人员关注的资助机构
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  public List<FundAgencyInterest> getPsnFundAgencyInterestList(Long psnId) throws Exception;

  public void buildDynFundRecommend(FundRecommendForm form);

  public void insertFundRecmRecord(Long psnId, Long fundId);

  /**
   * 初始化关注的资助机构、科技领域、申请资格
   * 
   * @param form
   * @throws ServiceException
   */
  public void initAgencyInterestAndAreaSeniority(Long psnId) throws Exception;

  public FundRecommendForm updateFundReadCount(FundRecommendForm form);

  public Map<String, String> buildFundScienceArea(Long fundId);

}
