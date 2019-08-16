package com.smate.web.management.service.other;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.other.fund.ConstFundAgency;
import com.smate.web.management.model.other.fund.ConstFundCategory;
import com.smate.web.management.model.other.fund.FundForm;
import com.smate.web.management.model.other.fund.FundLeftMenu;

/**
 * 基准库基金服务.
 * 
 * @author lichangwen
 * 
 */
public interface BpoFundService extends Serializable {
  /**
   * bpo基金机构左菜单
   * 
   * @return
   * @throws ServiceException
   */
  List<FundLeftMenu> getConstFundAgencyLeftMenu() throws ServiceException;

  /**
   * bpo基金类别的左菜单
   * 
   * @return
   * @throws ServiceException
   */
  List<FundLeftMenu> getConstFundCategoryLeftMenu() throws ServiceException;

  /**
   * @param page
   * @param form
   * @return
   * @throws ServiceException
   */
  Page<ConstFundAgency> findFundAgency(Page<ConstFundAgency> page, FundForm form) throws ServiceException;

  /**
   * @param page
   * @param form
   * @return
   * @throws ServiceException
   */
  Page<ConstFundAgency> findFundAgencyAudit(Page<ConstFundAgency> page, FundForm form) throws ServiceException;

  /**
   * @param page
   * @param form
   * @return
   * @throws ServiceException
   */
  Page<ConstFundCategory> findFundCategory(Page<ConstFundCategory> page, FundForm form) throws ServiceException;

  /**
   * @param page
   * @param form
   * @return
   * @throws ServiceException
   */
  Page<ConstFundCategory> findFundCategoryAudit(Page<ConstFundCategory> page, FundForm form) throws ServiceException;

  /**
   * 新增、编辑资助机构 ---(批准)新增
   * 
   * @param constFundAgency
   * @throws ServiceException
   */
  void saveConstFundAgency(ConstFundAgency constFundAgency) throws ServiceException;

  /**
   * 新增、编辑资助机构 ---更新
   * 
   * @param constFundAgency
   * @throws ServiceException
   */
  void updateConstFundAgency(ConstFundAgency constFundAgency) throws ServiceException;

  /**
   * 审核资助机构
   * 
   * @param id
   * @param status 1:通过，2拒绝
   * @param reason 理由
   * @throws ServiceException
   */
  void auditInsFundAgency(Long id, int status) throws ServiceException;

  /**
   * 审核资助类别
   * 
   * @param id
   * @param status 1:通过，2拒绝
   * @param reason 理由
   * @throws ServiceException
   */
  void auditInsFundCategory(Long id, int status) throws ServiceException;

  /**
   * 根据id获取基准资助机构
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  ConstFundAgency getConstFundAgency(Long id) throws ServiceException;

  /**
   * 删除资助机构
   * 
   * @param id
   * @throws ServiceException
   */
  void deleteConstFundAgency(Long id) throws ServiceException;

  /**
   * 新增、编辑资助类别---批准
   * 
   * @param constFundCategory
   * @return
   * @throws ServiceException
   */
  ConstFundCategory saveConstFundCategory(ConstFundCategory constFundCategory) throws ServiceException;

  /**
   * 新增、编辑资助类别---更新
   * 
   * @param constFundCategory
   * @return
   * @throws ServiceException
   */
  ConstFundCategory updateConstFundCategory(ConstFundCategory constFundCategory) throws ServiceException;

  /**
   * 根据id获取基准资助类别
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  ConstFundCategory getConstFundCategory(Long id) throws ServiceException;

  /**
   * 删除资助类别
   * 
   * @param id
   * @throws ServiceException
   */
  void deleteConstFundCategory(Long id) throws ServiceException;

  /**
   * 检查待审核资助类别中的资助机构是否通过了审核
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  Long checkInsFundCategoryByAgency(Long id) throws ServiceException;

  /**
   * 机构查重
   * 
   * @param cfa
   * @return
   * @throws ServiceException
   */
  boolean getConstFundAgency(ConstFundAgency cfa) throws ServiceException;

  /**
   * 类别查重
   * 
   * @param cfc
   * @return
   * @throws ServiceException
   */
  boolean getConstFundCategory(ConstFundCategory cfc) throws ServiceException;

  /**
   * 基金机构类别
   * 
   * @return
   * @throws ServiceException
   */
  List<Map<String, Object>> getFundAgencyTypeList() throws ServiceException;

  /**
   * 基金机构所在地区
   * 
   * @param regionId TODO
   * 
   * @return
   * @throws ServiceException
   */
  List<Map<String, Object>> getConstRegionList(Long regionId) throws ServiceException;

  /**
   * 所有国家和地区
   * 
   * @return
   * @throws ServiceException
   */
  public List<Map<String, Object>> getAllCountryAndRegion() throws ServiceException;

  /**
   * 查询所有基金机构.
   * 
   * @return map{id:id,name:name}
   * @throws ServiceException
   */
  List<Map<String, Object>> getFundAgencyAll() throws ServiceException;

  /**
   * 基金语言类别
   * 
   * @return
   * @throws ServiceException
   */
  List<Map<String, Object>> getLanguageList() throws ServiceException;

  /**
   * 学位
   * 
   * @return
   * @throws ServiceException
   */
  List<Map<String, Object>> getDegreeList() throws ServiceException;

  /**
   * 职称
   * 
   * @return
   * @throws ServiceException
   */
  List<Map<String, Object>> getTitleList() throws ServiceException;

  /**
   * 获取基金关键词.
   * 
   * @param startWith
   * @param size
   * @return
   * @throws ServiceException
   */
  String getAcFundKeywords(String startWith, int size) throws ServiceException;

  /**
   * 比较两个agency对象
   * 
   * @param agency
   * @param parentAgency
   * @return
   * @throws ServiceException
   */
  String[] compareConstFundAgency(ConstFundAgency agency, ConstFundAgency parentAgency) throws ServiceException;

  /**
   * 比较两个category对象
   * 
   * @param caetgory
   * @param parentCategory
   * @return
   * @throws ServiceException
   */
  String[] compareConstFundCategory(ConstFundCategory caetgory, ConstFundCategory parentCategory)
      throws ServiceException;

  List<ConstFundCategory> getConstFundCategoryAll(Long id);

  List<ConstFundAgency> getMatchAgencyByKey(String key);

  /**
   * 根据名称不区分中英文 查找资助机构
   * 
   * @param key
   * @return
   */
  ConstFundAgency getFundAgencyByName(String key);
}
