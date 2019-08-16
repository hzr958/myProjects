package com.smate.web.fund.service.wechat;

import java.util.List;
import java.util.Map;

import com.smate.web.fund.agency.model.FundAgencyInterest;
import com.smate.web.fund.recommend.model.FundRecommendRegion;
import com.smate.web.prj.exception.FundExcetpion;
import com.smate.web.prj.exception.PrjException;
import com.smate.web.prj.form.wechat.FundWeChatForm;


/**
 * 基金-wechta查询服务类
 * 
 * @author zk
 * @since 6.0.1
 */
public interface FundWeChatQueryService {

  /**
   * 查询基金
   * 
   * @param form
   * @throws FundExcetpion
   * @throws Exception
   */
  void queryFundForWeChat(FundWeChatForm form) throws Exception;

  void queryFundxml(FundWeChatForm form) throws Exception;

  /**
   * 初始化基金推荐条件设置
   * 
   * @param form
   * @throws PrjException
   */
  public void initFundCondition(FundWeChatForm form) throws PrjException;

  /**
   * 获取基金推荐条件设置
   * 
   * @param form
   * @throws PrjException
   */
  public void getFundCondition(FundWeChatForm form) throws PrjException;

  /**
   * 分页查询我的基金
   * 
   * @param form
   * @return
   * @throws PrjException
   */
  public FundWeChatForm searchMyFund(FundWeChatForm form) throws PrjException;

  /**
   * 获取全部的科技领域
   * 
   * @param form
   * @return
   * @throws PrjException
   */
  public void getAllScienceArea(FundWeChatForm form) throws PrjException;

  /**
   * 保存科技领域
   * 
   * @param form
   * @return
   * @throws PrjException
   */
  public void savePsnArea(FundWeChatForm form) throws PrjException;

  /**
   * 保存关注的资助机构
   * 
   * @param form
   * @throws PrjException
   */
  public Map<String, Object> savePsnAgency(FundWeChatForm form) throws PrjException;

  public abstract void queryAllProvince(FundWeChatForm form) throws PrjException;

  public abstract void queryAreaNext(FundWeChatForm form) throws PrjException;

  /**
   * 获取关注地区
   * 
   * @param form
   * @throws PrjException
   */
  public List<FundRecommendRegion> getFundRegion(Long psnId) throws PrjException;

  /**
   * 获取关注资助机构
   * 
   * @param form
   * @throws PrjException
   */
  public List<FundAgencyInterest> getFundAgencyInterest(Long psnId) throws PrjException;

  /**
   * 获取关注地区
   * 
   * @param form
   * @throws PrjException
   */
  /* public Map<String, String> deleteRegion(String regionCode) throws PrjException; */

  public abstract void searchRegion(FundWeChatForm form) throws PrjException;

  /**
   * 编辑关注资助机构
   * 
   * @param form
   * @throws PrjException
   */
  public void editFundAgencyInterest(FundWeChatForm form) throws PrjException;
}
