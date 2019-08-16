package com.smate.web.fund.agency.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.fund.model.common.ConstFundAgency;

/*
 * @author zx 基金constFundCategoryDao
 */
@Repository
public class FundAgencyDao extends SnsHibernateDao<ConstFundAgency, Long> {

  // 查询资助机构
  @SuppressWarnings("unchecked")
  public List<ConstFundAgency> getFundAgencyList(List<Long> regionId, Page page) {

    String hql = "";
    String totalCount = "";
    if (CollectionUtils.isNotEmpty(regionId) && !regionId.contains(156L)) {
      hql = "from ConstFundAgency where regionId in(:regionId) and insId=0 and status =0";
      totalCount = "select count(*) from ConstFundAgency where regionId in(:regionId) and insId=0 and status =0";
      Long count = (Long) super.createQuery(totalCount).setParameterList("regionId", regionId).uniqueResult();
      page.setTotalCount(count);
      if (count == 0) {
        return null;
      } else {
        return super.createQuery(hql).setParameterList("regionId", regionId).setFirstResult(page.getFirst() - 1)
            .setMaxResults(page.getPageSize()).list();
      }
    } else {
      hql = "from ConstFundAgency where insId=0 and status =0";
      totalCount = "select count(*) from  ConstFundAgency where insId=0 and status =0";
      Long count = (Long) super.createQuery(totalCount).uniqueResult();
      page.setTotalCount(count);
      if (count == 0) {
        return null;
      } else {
        return super.createQuery(hql).setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
      }
    }
  }

  // 查询所选地区下的所有资助机构的id
  @SuppressWarnings("unchecked")
  public List<Long> getFundAgencyIdList(List<Long> regionId) {
    String hql = "select t.id from ConstFundAgency t where t.regionId in(:regionId) and t.insId=0 and t.status =0";
    return super.createQuery(hql).setParameterList("regionId", regionId).list();
  }

  // 查询资助机构
  @SuppressWarnings("unchecked")
  public List<ConstFundAgency> findFundAgencyList(List<Long> regionId, String searchKey, Page page) {
    String hql = "";
    String totalCount = "";
    if (CollectionUtils.isNotEmpty(regionId) && !regionId.contains(156L)) {
      hql = "from ConstFundAgency c where c.regionId in(:regionId) and c.insId=0 and c.status =0 "
          + "and (instr(c.nameZh,lower(:searchKeyZh))>0 or instr(c.nameEn,lower(:searchKeyEn))>0"
          + " or instr(c.nameZh,upper(:searchKeyZh))>0 or instr(c.nameEn,upper(:searchKeyEn))>0)";
      totalCount =
          "select count(*) from ConstFundAgency c where c.regionId in(:regionId) and c.insId=0 and c.status =0 "
              + "and (instr(c.nameZh,lower(:searchKeyZh))>0 or instr(c.nameEn,lower(:searchKeyEn))>0"
              + " or instr(c.nameZh,upper(:searchKeyZh))>0 or instr(c.nameEn,upper(:searchKeyEn))>0)";
      Long count = (Long) super.createQuery(totalCount).setParameterList("regionId", regionId)
          .setParameter("searchKeyZh", searchKey).setParameter("searchKeyEn", searchKey).uniqueResult();
      page.setTotalCount(count);
      if (count == 0) {
        return null;
      } else {
        return super.createQuery(hql).setParameterList("regionId", regionId).setParameter("searchKeyZh", searchKey)
            .setParameter("searchKeyEn", searchKey).setFirstResult(page.getFirst() - 1)
            .setMaxResults(page.getPageSize()).list();
      }
    } else {
      hql = "from ConstFundAgency c where c.insId=0 and c.status =0 and"
          + " (instr(c.nameZh,lower(:searchKeyZh))>0 or instr(c.nameEn,lower(:searchKeyEn))>0"
          + " or instr(c.nameZh,upper(:searchKeyZh))>0 or instr(c.nameEn,upper(:searchKeyEn))>0)";
      totalCount = "select count(*) from  ConstFundAgency c where c.insId=0 and c.status =0 and"
          + " (instr(c.nameZh,lower(:searchKeyZh))>0 or instr(c.nameEn,lower(:searchKeyEn))>0"
          + " or instr(c.nameZh,upper(:searchKeyZh))>0 or instr(c.nameEn,upper(:searchKeyEn))>0)";
      Long count = (Long) super.createQuery(totalCount).setParameter("searchKeyZh", searchKey)
          .setParameter("searchKeyEn", searchKey).uniqueResult();
      page.setTotalCount(count);
      if (count == 0) {
        return null;
      } else {
        return super.createQuery(hql).setParameter("searchKeyZh", searchKey).setParameter("searchKeyEn", searchKey)
            .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
      }

    }
  }

  // 查询资助机构
  @SuppressWarnings("unchecked")
  public List<ConstFundAgency> getFundAgencyListByRegionIds(List<Long> regionId) {
    String hql = "";
    if (CollectionUtils.isNotEmpty(regionId) && !regionId.contains(156L)) {
      hql = "from ConstFundAgency where regionId in(:regionId) and insId=0 and status =0 and type!=120";
      return super.createQuery(hql).setParameterList("regionId", regionId).list();
    } else {
      hql = "from ConstFundAgency where insId=0 and status =0";
      return super.createQuery(hql).list();
    }
  }

  // 查询资助机构
  @SuppressWarnings("unchecked")
  public List<ConstFundAgency> getFundAgencyListByRegionId(Long regionId) {
    String hql = "";
    if (regionId != null && regionId.longValue() != 156L) {
      hql = "from ConstFundAgency where regionId=:regionId and insId=0 and status =0";
      return super.createQuery(hql).setParameter("regionId", regionId).list();
    } else {
      hql = "from ConstFundAgency where insId=0 and status =0";
      return super.createQuery(hql).list();
    }
  }

  @SuppressWarnings("unchecked")
  public List<ConstFundAgency> getFundAgencyListByName(String name, List<Long> regionIds) {
    String sql = "";
    if (StringUtils.isNoneBlank(name) && CollectionUtils.isNotEmpty(regionIds)) {
      sql =
          "select * from CONST_FUND_AGENCY where regexp_like(name_zh,:name,'i') and region_id in (:regionIds) and ins_id=0 and status=0 order by region_id asc,id asc";
      return super.getSession().createSQLQuery(sql).addEntity(ConstFundAgency.class).setParameter("name", name)
          .setParameterList("regionIds", regionIds).list();
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public ConstFundAgency getOneFundAgencyByName(String name) {
    String hql = "";
    if (StringUtils.isNoneBlank(name)) {
      hql =
          "from ConstFundAgency where nameZh = :name or nameEn = :name and insId=0 and status =0 and regionId is not null order by id asc";
      List<ConstFundAgency> agencyList = super.createQuery(hql).setParameter("name", name).list();
      if (CollectionUtils.isNotEmpty(agencyList)) {
        return agencyList.get(0);
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<ConstFundAgency> getFundAgencyListByNameaAndRegionId(String name, Long regionId) {
    String hql = "";
    if (StringUtils.isNoneBlank(name) && regionId != null) {
      hql =
          "from ConstFundAgency where nameZh like :name or nameEn like :name and regionId=:regionId and insId=0 and status =0";
      return super.createQuery(hql).setParameter("name", "%" + name + "%").setParameter("regionId", regionId).list();
    }
    return null;
  }

  // 根据资助机构id查询资助机构的名称
  @SuppressWarnings("unchecked")
  public ConstFundAgency getFundAgencyNameByAgencyId(Long agencyId) {
    String hql = "select new ConstFundAgency(nameZh,nameEn) from ConstFundAgency t where t.id=:agencyId";
    return (ConstFundAgency) super.createQuery(hql).setParameter("agencyId", agencyId).uniqueResult();
  }

  // 查询国家级资助机构
  @SuppressWarnings("unchecked")
  public List<ConstFundAgency> getCountryFundAgencyList() {
    String hql = "from ConstFundAgency where insId=0 and type=120 and status=0";
    return super.createQuery(hql).list();
  }

  // 查询国家级资助机构id
  @SuppressWarnings("unchecked")
  public List<Long> getCountryFundAgencyIdList() {
    String hql = "select t.id from ConstFundAgency t where t.insId=0 and t.type=120 and t.status=0";
    return super.createQuery(hql).list();
  }

  // 查询其他资助机构
  @SuppressWarnings("unchecked")
  public List<ConstFundAgency> getOtherFundAgencyList() {
    String hql = "from ConstFundAgency where insId=0 and type!=120 and regionId is null and status=0";
    return super.createQuery(hql).list();
  }

  // 查询机构下符合基金列表的基金数
  @SuppressWarnings({"unchecked"})
  public List<Map<String, Long>> getFundAgencyCount(List<Long> regionId, List<Long> ids) {
    String hql = "";
    if (CollectionUtils.isNotEmpty(regionId)) {
      hql =
          "select t.id as id,count(*) as count from ConstFundAgency t ,ConstFundCategory c where t.regionId in(:regionId) and t.id in(:ids) and t.id=c.agencyId and c.insId=0 and c.status =0 and t.insId=0 and t.status =0 and trunc(c.endDate)>=trunc(sysdate) group by t.id";
      return super.createQuery(hql).setParameterList("regionId", regionId).setParameterList("ids", ids)
          .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    } else {
      hql =
          "select t.id as id,count(*) as count from ConstFundAgency t ,ConstFundCategory c where t.id in(:ids) and c.insId=0 and c.status =0 and t.insId=0 and t.status =0 and t.id=c.agencyId and trunc(c.endDate)>=trunc(sysdate) group by t.id";
      return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("ids", ids)
          .list();
    }

  }

  // 查询一个机构下符合基金列表的基金数
  @SuppressWarnings({"unchecked"})
  public Long getOneFundAgencyCount(Long agencyId) {
    String hql = "";
    hql =
        "select count(*) as count from ConstFundAgency t ,ConstFundCategory c where t.id=:agencyId and t.id=c.agencyId and c.endDate>sysdate group by t.id";
    return (Long) super.createQuery(hql).setParameter("agencyId", agencyId).uniqueResult();

  }

  // 查询一个机构下基金数
  @SuppressWarnings({"unchecked"})
  public Long getOneFundAllAgencyCount(Long agencyId) {
    String hql = "";
    hql =
        "select count(*) as count from ConstFundAgency t ,ConstFundCategory c where t.id=:agencyId and t.id=c.agencyId and c.insId=0";
    return (Long) super.createQuery(hql).setParameter("agencyId", agencyId).uniqueResult();

  }

  // 获取全国机构统计数
  public Long getAllAgencyCount(Long regionId) {
    String hql = null;
    if (regionId != 156L) {
      hql =
          "select count(*) from ConstFundAgency t where  exists (select p.id from ConstRegion p where (p.id =:regionId or p.superRegionId=:regionId) and t.regionId=:regionId) and t.insId=0 and t.status =0";
      return (Long) super.createQuery(hql).setParameter("regionId", regionId).uniqueResult();
    } else {
      hql = "select count(*) from ConstFundAgency t where t.insId=0 and t.status =0";
      return (Long) super.createQuery(hql).uniqueResult();
    }

  }

  @SuppressWarnings("unchecked")
  public ConstFundAgency getFundAgencyByName(String name) {
    String hql = "";
    if (StringUtils.isNoneBlank(name)) {
      hql = "from ConstFundAgency where nameZh = :name or nameEn = :name and insId=0 and status =0 order by id asc";
      List<ConstFundAgency> agencyList = super.createQuery(hql).setParameter("name", name).list();
      if (CollectionUtils.isNotEmpty(agencyList)) {
        return agencyList.get(0);
      }
    }
    return null;
  }

}
