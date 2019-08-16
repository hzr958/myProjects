package com.smate.center.task.dao.snsbak;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.PubPdwhAddrInfoStandard;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class PubPdwhAddrInfoStandardDao extends SnsbakHibernateDao<Long, PubPdwhAddrInfoStandard> {

  @SuppressWarnings("unchecked")
  public List<Integer> getProvinceIdsByPubId(Long pubId) {

    String hql =
        "select distinct(t.provinceId)  from PubPdwhAddrInfoStandard t where t.pdwhPubId =:pubId and t.provinceId !=0";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Integer> getProvinceIdsByPubIdExclude(Long pubId, Integer exludedProvinceId) {

    String hql =
        "select distinct(t.provinceId)  from PubPdwhAddrInfoStandard t where t.pdwhPubId =:pubId and t.provinceId !=0 and t.provinceId !=:exludedProvinceId";
    return super.createQuery(hql).setParameter("pubId", pubId).setParameter("exludedProvinceId", exludedProvinceId)
        .list();
  }

  // 排除中国id
  public List<Integer> getCountryIdsByPubId(Long pubId) {

    String hql =
        "select distinct(t.countryId)  from PubPdwhAddrInfoStandard t where t.pdwhPubId =:pubId and t.countryId != 0 and t.countryId != 156";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Integer> getCityIdsByPubId(Long pubId) {

    String hql = "select distinct(t.cityId)  from PubPdwhAddrInfoStandard t where t.pdwhPubId =:pubId and t.cityId !=0";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }
}
