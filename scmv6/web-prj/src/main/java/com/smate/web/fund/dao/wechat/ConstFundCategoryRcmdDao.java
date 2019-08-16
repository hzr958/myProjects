package com.smate.web.fund.dao.wechat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RcmdHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.fund.model.common.ConstFundCategory;

/*
 * @author zjh 基金constFundCategoryDao
 */
@Repository
public class ConstFundCategoryRcmdDao extends RcmdHibernateDao<ConstFundCategory, Long> {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 根据地区，领域查出该机构下的基金
   * 
   * @param agencyId
   * @param disId
   * @param insType
   * @param page
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstFundCategory> findConstFundCategory(Long agencyId, List<Long> disIdList, String insType, Page page) {
    Map<String, Object> param = new HashMap<String, Object>();
    String listHql = "select new ConstFundCategory(t.id,t.agencyId,t.nameZh,t.nameEn,t.startDate,t.endDate) ";
    String countHql = "select count(t.id) ";
    StringBuilder hql = new StringBuilder();
    hql.append(" from ConstFundCategory t ");
    hql.append("where ");
    hql.append("t.agencyId=:agencyId ");
    param.put("agencyId", agencyId);
    if (CollectionUtils.isNotEmpty(disIdList)) {
      if (disIdList.get(0) != 0) {
        hql.append(
            "and exists( select 1 from ConstFundCategoryDis a where t.id=a.categoryId and a.disId in(:disIdList)) ");
        param.put("disIdList", disIdList);
      } else {
        hql.append("and not exists( select 1 from ConstFundCategoryDis a where t.id=a.categoryId) ");
      }

    }
    if (StringUtils.isNotBlank(insType)) {
      if (insType.equals("0")) {
        hql.append("and t.insType is null ");
      } else if (insType.equals("1.2")) {
        hql.append("and (instr(t.insType,1)>0 or instr(t.insType,2)>0) ");
        param.put("insType", insType);
      } else {
        hql.append("and instr(t.insType,:insType)>0 ");
        param.put("insType", insType);
      }
    }
    String orderHql = " order by t.endDate desc nulls last,t.startDate desc nulls last ";
    Long totalCount = super.findUnique(countHql + hql, param);
    page.setTotalCount(totalCount);// 查询总数

    Query querResult = super.createQuery(listHql + hql + orderHql, param);
    querResult.setFirstResult(page.getFirst() - 1);
    querResult.setMaxResults(page.getPageSize());
    List<ConstFundCategory> fundCategoryList = querResult.list();
    return fundCategoryList;
  }

  /**
   * 根据地区，领域，关键词查出该机构下的基金
   * 
   * @param agencyId
   * @param disIdList
   * @param insType
   * @param page
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstFundCategory> findConstFundCategory(Long agencyId, List<Long> disIdList, String searchKey,
      String insType, Page page) {
    Map<String, Object> param = new HashMap<String, Object>();
    String listHql = "select new ConstFundCategory(t.id,t.agencyId,t.nameZh,t.nameEn,t.startDate,t.endDate) ";
    String countHql = "select count(t.id) ";
    StringBuilder hql = new StringBuilder();
    hql.append(" from ConstFundCategory t ");
    hql.append("where ");
    hql.append("t.agencyId=:agencyId and t.insId=0 ");
    param.put("agencyId", agencyId);
    hql.append("and (instr(t.nameZh,upper(:searchKeyZh))>0 ");
    hql.append("or instr(t.nameZh,lower(:searchKeyZh))>0 ");
    param.put("searchKeyZh", searchKey);
    hql.append("or instr(t.nameEn,upper(:searchKeyEn))>0 ");
    hql.append("or instr(t.nameEn,lower(:searchKeyEn))>0)");
    param.put("searchKeyEn", searchKey);

    if (CollectionUtils.isNotEmpty(disIdList)) {
      if (disIdList.get(0) != 0) {
        hql.append(
            "and exists( select 1 from ConstFundCategoryDis a where t.id=a.categoryId and a.disId in(:disIdList)) ");
        param.put("disIdList", disIdList);
      } else {
        hql.append("and not exists( select 1 from ConstFundCategoryDis a where t.id=a.categoryId) ");
      }

    }
    if (StringUtils.isNotBlank(insType)) {
      if (insType.equals("0")) {
        hql.append("and t.insType is null ");
      } else if (insType.equals("1.2")) {
        hql.append("and (instr(t.insType,1)>0 or instr(t.insType,2)>0) ");
        param.put("insType", insType);
      } else {
        hql.append("and instr(t.insType,:insType)>0 ");
        param.put("insType", insType);
      }
    }
    String orderHql = "order by t.endDate desc nulls last,t.startDate desc nulls last ";
    Long totalCount = super.findUnique(countHql + hql, param);
    page.setTotalCount(totalCount);// 查询总数

    Query querResult = super.createQuery(listHql + hql + orderHql, param);
    querResult.setFirstResult(page.getFirst() - 1);
    querResult.setMaxResults(page.getPageSize());
    List<ConstFundCategory> fundCategoryList = querResult.list();
    return fundCategoryList;
  }

  /**
   * 根据地区，领域，关键词，资助机构获取基金列表
   * 
   * @param fundAgencyIdList, ifFindCountry, searchKey, fundIdBySearchKey, insType, disIdList, page
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstFundCategory> findConstFundCategory(String regionCodesSelect, List<Long> categoryfundIdList,
      boolean ifFindCountry, String searchKey, List<Long> fundIdBySearchKey, String insType, List<Long> disIdList,
      Page page) {
    Map<String, Object> param = new HashMap<String, Object>();
    String listHql = "select new ConstFundCategory(t.id,t.agencyId,t.nameZh,t.nameEn,t.startDate,t.endDate) ";
    String countHql = "select count(t.id) ";
    StringBuilder hql = new StringBuilder();
    hql.append(" from ConstFundCategory t ");
    hql.append("where ");
    if (CollectionUtils.isNotEmpty(categoryfundIdList)) {
      hql.append("t.id in(:fundId) and ");
      param.put("fundId", categoryfundIdList);
    } else if (!ifFindCountry && StringUtils.isNotBlank(regionCodesSelect)) {// 已检索地区但没有适合该地区的基金，直接返回空
      return null;
    }
    hql.append("t.insId=0 and t.status=0 ");
    if (StringUtils.isNotBlank(searchKey)) {
      // 关键词匹配基金标题
      hql.append("and (instr(lower(t.nameZh),lower(:searchKeyNameZh))>0 ");
      param.put("searchKeyNameZh", searchKey);
      hql.append("or instr(lower(t.nameEn),lower(:searchKeyNameZh))>0 ");
      param.put("searchKeyNameEn", searchKey);
      // 关键词匹配基金描述
      hql.append("or instr(lower(t.description),lower(:searchKeyDescription))>0 ");
      param.put("searchKeyDescription", searchKey);
      // 关键词匹配基金关键词
      if (CollectionUtils.isNotEmpty(fundIdBySearchKey)) {
        hql.append("or t.id in(:fundIdBySearchKey) ");
        param.put("fundIdBySearchKey", fundIdBySearchKey);
      }
      hql.append(")");
    }
    // 匹配单位类型
    if (StringUtils.isNotBlank(insType)) {
      if (insType.equals("0")) {
        // 不选默认获取所有单位类型的基金
      } else {
        hql.append("and (t.insType = :insType or t.insType = '1,2') ");
        param.put("insType", insType);
      }
    }
    // 匹配科技领域
    if (CollectionUtils.isNotEmpty(disIdList)) {
      if (disIdList.get(0) != 0) {
        hql.append(
            "and exists( select 1 from ConstFundCategoryDis a where t.id=a.categoryId and a.disId in(:disIdList)) ");
        param.put("disIdList", disIdList);
      } else {
        hql.append("and not exists( select 1 from ConstFundCategoryDis a where t.id=a.categoryId) ");
      }
    }

    if (ifFindCountry) {
      if (CollectionUtils.isNotEmpty(categoryfundIdList)) {
        hql.append("or ");
      } else {
        hql.append("and ");
      }
      hql.append("(not exists( select 1 from ConstFundCategoryRegion c where t.id=c.fundCategoryId )");
      if (StringUtils.isNotBlank(searchKey)) {
        // 关键词匹配基金标题
        hql.append("and (instr(lower(t.nameZh),lower(:searchKeyNameZh))>0 ");
        param.put("searchKeyNameZh", searchKey);
        hql.append("or instr(lower(t.nameEn),lower(:searchKeyNameZh))>0 ");
        param.put("searchKeyNameEn", searchKey);
        // 关键词匹配基金描述
        hql.append("or instr(lower(t.description),lower(:searchKeyDescription))>0 ");
        param.put("searchKeyDescription", searchKey);
        // 关键词匹配基金关键词
        if (CollectionUtils.isNotEmpty(fundIdBySearchKey)) {
          hql.append("or t.id in(:fundIdBySearchKey) ");
          param.put("fundIdBySearchKey", fundIdBySearchKey);
        }
        hql.append(") ");
      }
      // 匹配单位类型
      if (StringUtils.isNotBlank(insType)) {
        if (insType.equals("0")) {
          // 不选默认获取所有单位类型的基金
        } else {
          hql.append("and (t.insType = :insType or t.insType = '1,2') ");
          param.put("insType", insType);
        }
      }
      // 匹配科技领域
      if (CollectionUtils.isNotEmpty(disIdList)) {
        if (disIdList.get(0) != 0) {
          hql.append(
              "and exists( select 1 from ConstFundCategoryDis a where t.id=a.categoryId and a.disId in(:disIdList)) ");
          param.put("disIdList", disIdList);
        } else {
          hql.append("and not exists( select 1 from ConstFundCategoryDis a where t.id=a.categoryId) ");
        }
      }
      hql.append(") ");
    }
    String orderHql = "order by t.endDate desc nulls last,t.startDate desc nulls last,t.id desc";
    Long totalCount = super.findUnique(countHql + hql, param);
    page.setTotalCount(totalCount);// 查询总数

    Query querResult = super.createQuery(listHql + hql + orderHql, param);
    querResult.setFirstResult(page.getFirst() - 1);
    querResult.setMaxResults(page.getPageSize());
    List<ConstFundCategory> fundCategoryList = querResult.list();
    return fundCategoryList;
  }

  /**
   * 查找这个包含这个一级的科技领域和他的子领域下的基金的数量
   * 
   * @param disId
   * @return
   */
  public Long getfundCountByDisId(List<Long> disIdList, Long agencyId) {
    String hql =
        "select count(t.id) from  ConstFundCategory t where exists( select 1 from ConstFundCategoryDis a where t.id=a.categoryId and a.disId in(:disIdList)) and agencyId=:agencyId";
    return (Long) super.createQuery(hql).setParameterList("disIdList", disIdList).setParameter("agencyId", agencyId)
        .uniqueResult();

  }

  /**
   * 没有科技领域的基金数量
   * 
   * @param agencyId
   * @return
   */
  public Long getNoDisFundCount(Long agencyId) {
    String hql =
        "select count(t.id) from  ConstFundCategory t where not exists( select 1 from ConstFundCategoryDis a where t.id=a.categoryId) and agencyId=:agencyId";
    return (Long) super.createQuery(hql).setParameter("agencyId", agencyId).uniqueResult();
  }

  /**
   * 根据单位要求查找基金数量
   * 
   * @param insType
   * @return
   */
  public Long getfundCountByInsType(String insType, Long agencyId) {
    String hql = "select count(t.id) from ConstFundCategory t where instr(t.insType,:insType)>0 and agencyId=:agencyId";
    return (Long) super.createQuery(hql).setParameter("insType", insType).setParameter("agencyId", agencyId)
        .uniqueResult();
  }

  /**
   * 没有单位要求基金数量
   * 
   * @param insType
   * @return
   */
  public Long getfundCountNotInsType(Long agencyId) {
    String hql = "select count(t.id) from ConstFundCategory t where agencyId=:agencyId and t.insType is null";
    return (Long) super.createQuery(hql).setParameter("agencyId", agencyId).uniqueResult();
  }

  /**
   * 根据IDs获取基金信息
   * 
   * @param fundIds
   * @return
   */
  public List<ConstFundCategory> findConstFundCategoryByIds(List<Long> fundIds) {
    String hql = " from ConstFundCategory t where t.id in (:fundIds)";
    return super.createQuery(hql).setParameterList("fundIds", fundIds).list();
  }

  /**
   * 获取基金名称
   * 
   * @param fundId
   * @return
   */
  public ConstFundCategory findFundName(Long fundId) {
    String hql = "select new ConstFundCategory(nameZh,nameEn) from ConstFundCategory t where t.id=:fundId";
    return (ConstFundCategory) super.createQuery(hql).setParameter("fundId", fundId).uniqueResult();
  }

  /**
   * 获取所有基金，根据截至时间排序
   * 
   * @param fundIds
   * @return
   */
  public List<ConstFundCategory> findAllConstFundCategory(Page page) {
    String countHql = "select count(t.id) ";
    String hql = "from ConstFundCategory t where t.status = 0 and t.insId = 0";
    String orderHql = "order by t.endDate desc nulls last,t.startDate desc nulls last,t.id desc";
    Long totalCount = super.findUnique(countHql + hql);
    page.setTotalCount(totalCount);// 查询总数
    Query querResult = super.createQuery(hql + orderHql);
    querResult.setFirstResult(page.getFirst() - 1);
    querResult.setMaxResults(page.getPageSize());
    return querResult.list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getFundIdList(Long fundId) {
    String hql = "select t.id from ConstFundCategory t where t.status = 0 and t.insId = 0 and t.id <> :fundId";
    String orderHql = " order by t.endDate desc nulls last,t.startDate desc nulls last,t.updateDate desc nulls last";
    return super.createQuery(hql + orderHql).setParameter("fundId", fundId).setMaxResults(5).list();
  }
}
