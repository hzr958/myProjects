package com.smate.web.fund.service.agency;

import java.util.List;
import java.util.Map;

import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.model.Page;
import com.smate.web.fund.agency.dto.FundAgencyDTO;
import com.smate.web.fund.agency.model.AgencyStatistics;
import com.smate.web.fund.agency.model.FundAgencyForm;
import com.smate.web.fund.model.common.ConstFundAgency;

/**
 * 基金页面
 * 
 * @author Administrator
 *
 */

public interface FundAgencyService {
  /**
   * 查找一个资助机构信息
   * 
   * @param form
   * @throws Exception
   */
  public void getFundAgency(FundAgencyForm form) throws Exception;

  /**
   * 查找基金列表
   */
  public void getFundList(FundAgencyForm form) throws Exception;

  /**
   * 查询科技领域和单位性质
   */
  public void getDetailLeftCondition(FundAgencyForm form) throws Exception;

  /**
   * 查询资助机构详情页面左侧基金单位统计数
   */
  public List<Map<String, String>> getDetailLeftCount(String disIdStr, Long fundAgencyId) throws Exception;

  /**
   * 获取全国地区机构的统计数
   */
  public String getAllFundAgencyCount() throws Exception;

  /**
   * 获取基金列表
   * 
   * @return
   */
  public List<ConstFundAgency> getFundAgencyList(List<Long> regionId, Page page) throws Exception;

  /**
   * 获取中国省级地区信息，按首字母排序
   * 
   * @return
   */
  public List<ConstRegion> getFundRegion() throws Exception;

  /**
   * 构建编辑资助机构
   * 
   * @return
   */
  public void buildFundMapBaseInfo(FundAgencyForm form) throws Exception;

  /**
   * 处理赞、取消赞资助机构操作
   * 
   * @throws ServiceException
   */
  public String dealWithAwardOpt(Long psnId, Long agencyId, Integer optType) throws ServiceException;

  /**
   * 更新资助机构统计数
   * 
   * @param agencyId 资助机构ID
   * @param updateShareSum 是否更新分享数
   * @param updateAwardSum 是否更新赞总数
   * @param updateInterestSum 是否更新关注数
   * @param addOrReduce 是增加还是减少， 1:增加，0：减少
   * @param updateNum 增加或减少的数量
   * @throws ServiceException
   */
  public AgencyStatistics updateAgencyStatistics(Long agencyId, boolean updateShareSum, boolean updateAwardSum,
      boolean updateInterestSum, Integer addOrReduce, Integer updateNum) throws ServiceException;

  /**
   * 处理分享资助机构操作
   * 
   * @throws ServiceException
   */
  public void dealWithShareOpt(FundAgencyForm form) throws ServiceException;

  /**
   * agencyId是否存在对应的资助机构
   * 
   * @param agencyId
   * @return
   * @throws ServiceException
   */
  public boolean existsAgency(Long agencyId) throws ServiceException;

  /**
   * 处理关注、取消关注资助机构
   * 
   * @param psnId
   * @param agencyId
   * @param optType
   * @throws ServiceException
   */
  public String dealWithInterestOpt(Long psnId, Long agencyId, Integer optType) throws ServiceException;

  /**
   * 初始化资助机构操作
   * 
   * @param des3Ids
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public List<Map<String, Object>> initAgencyOpt(String des3Ids, Long psnId) throws ServiceException;

  /**
   * 查找人员所有关注的资助机构的ID, 用逗号拼接
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public String findPsnAllInterestAgencyIds(Long psnId) throws ServiceException;

  /**
   * 分享资助机构给好友
   * 
   * @param form
   * @throws ServiceException
   */
  public void shareAgencyToFriends(FundAgencyForm form) throws ServiceException;

  /**
   * 查找资助机构统计信息
   * 
   * @param agencyId
   * @return
   * @throws ServiceException
   */
  public AgencyStatistics findAgencyStatistics(Long agencyId) throws ServiceException;

  public List<FundAgencyDTO> findFundAgencyList(FundAgencyForm form) throws Exception;

  /**
   * 查找基金机会是否存在
   * 
   * @param fundAgencyId
   * @return
   */
  public boolean existsFund(Long fundAgencyId);

  /**
   * 从solr获取基金详情
   * 
   * @param fundId
   * @return
   * @throws ServiceException
   */
  public Map<String, Object> queryFundDetail(Long fundId) throws ServiceException;

}
