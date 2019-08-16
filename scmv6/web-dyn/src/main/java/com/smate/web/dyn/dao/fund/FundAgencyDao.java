package com.smate.web.dyn.dao.fund;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.dyn.model.fund.ConstFundAgency;

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

  // 查询资助机构
  @SuppressWarnings("unchecked")
  public List<ConstFundAgency> findFundAgencyList(List<Long> regionId, String searchKey, Page page) {
    String hql = "";
    String totalCount = "";
    if (CollectionUtils.isNotEmpty(regionId) && !regionId.contains(156L)) {
      hql = "from ConstFundAgency c where c.regionId in(:regionId) and c.insId=0 and c.status =0 "
          + "and (instr(c.nameZh,:searchKeyZh)>0 or instr(c.nameEn,:searchKeyEn)>0)";
      totalCount =
          "select count(*) from ConstFundAgency c where c.regionId in(:regionId) and c.insId=0 and c.status =0 "
              + "and (instr(c.nameZh,:searchKeyZh)>0 or instr(c.nameEn,:searchKeyEn)>0)";
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
      hql =
          "from ConstFundAgency c where c.insId=0 and c.status =0 and (instr(c.nameZh,:searchKeyZh)>0 or instr(c.nameEn,:searchKeyEn)>0)";
      totalCount =
          "select count(*) from  ConstFundAgency c where c.insId=0 and c.status =0 and (instr(c.nameZh,:searchKeyZh)>0 or instr(c.nameEn,:searchKeyEn)>0)";
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
      hql = "from ConstFundAgency where regionId in(:regionId) and insId=0 and status =0";
      return super.createQuery(hql).setParameterList("regionId", regionId).list();
    } else {
      hql = "from ConstFundAgency where insId=0 and status =0";
      return super.createQuery(hql).list();
    }
  }

  // 查询国家级资助机构
  @SuppressWarnings("unchecked")
  public List<ConstFundAgency> getCountryFundAgencyList() {
    String hql = "from ConstFundAgency where type=120 and status=0";
    return super.createQuery(hql).list();
  }

  // 查询其他资助机构
  @SuppressWarnings("unchecked")
  public List<ConstFundAgency> getOtherFundAgencyList() {
    String hql = "from ConstFundAgency where type!=120 and regionId is null and status=0";
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
        "select count(*) as count from ConstFundAgency t ,ConstFundCategory c where t.id=:agencyId and t.id=c.agencyId";
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

}
