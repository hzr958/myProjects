package com.smate.sie.core.base.utils.dao.validate;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.validate.KpiPayValidate;

@Repository
public class KpiPayValidateDao extends SieHibernateDao<KpiPayValidate, Long> {


  public List<KpiPayValidate> judgementPaymentByInsIds(List<Long> insIds) {
    String hql =
        "from KpiPayValidate t where t.insId in(:insId) and t.status = 1 and t.endDate >= :endDate order by t.endDate desc";
    List<KpiPayValidate> list =
        super.createQuery(hql).setParameterList("insId", insIds).setParameter("endDate", new Date()).list();
    if (CollectionUtils.isEmpty(list)) {
      return null;
    } else {
      return list;
    }
  }

  public List<KpiPayValidate> judgementPaymentListByIP() {
    String hql = "from KpiPayValidate t where t.status = 1 and t.endDate >= :endDate order by t.endDate desc";
    List<KpiPayValidate> list = super.createQuery(hql).setParameter("endDate", new Date()).list();
    if (CollectionUtils.isEmpty(list)) {
      return null;
    } else {
      return list;
    }
  }


  @SuppressWarnings("unchecked")
  public boolean judgementPayment(List<Long> insIds) {
    if (CollectionUtils.isEmpty(insIds)) {
      return false;
    }
    String hql = "from KpiPayValidate t where t.insId in(:insId) and t.status = 1 and t.endDate >= :endDate";
    List<KpiPayValidate> list =
        super.createQuery(hql).setParameterList("insId", insIds).setParameter("endDate", new Date()).list();
    if (CollectionUtils.isEmpty(list)) {
      return false;
    } else {
      return true;
    }
  }

  @SuppressWarnings("unchecked")
  public List<KpiPayValidate> getLimitCount(List<Long> insIds) {
    if (CollectionUtils.isEmpty(insIds)) {
      return null;
    }
    String hql = "from KpiPayValidate t where t.insId in(:insId) and t.status = 1 and t.endDate >= :endDate";
    List<KpiPayValidate> list =
        super.createQuery(hql).setParameterList("insId", insIds).setParameter("endDate", new Date()).list();
    if (CollectionUtils.isEmpty(list)) {
      return null;
    } else {
      return list;
    }
  }

  @SuppressWarnings("unchecked")
  public boolean judgementPaymentByEndTime(List<Long> insIds) {
    if (CollectionUtils.isEmpty(insIds)) {
      return false;
    }
    String hql = "from KpiPayValidate t where t.insId in(:insId) and t.status = 1 and t.endDate < :endDate";
    List<KpiPayValidate> list =
        super.createQuery(hql).setParameterList("insId", insIds).setParameter("endDate", new Date()).list();
    if (CollectionUtils.isEmpty(list)) {
      return false;
    } else {
      return true;
    }
  }

  @SuppressWarnings("unchecked")
  public List<KpiPayValidate> judgementPaymentList() {
    String hql =
        "from KpiPayValidate t where t.status = 1 and t.endDate >= :endDate and t.permitIP is not null order by t.endDate desc";
    List<KpiPayValidate> list = super.createQuery(hql).setParameter("endDate", new Date()).list();
    if (CollectionUtils.isEmpty(list)) {
      return null;
    } else {
      return list;
    }
  }

  @SuppressWarnings("unchecked")
  public List<KpiPayValidate> judgementPaymentListByEndTime() {
    String hql = "from KpiPayValidate t where t.status = 1 and t.endDate < :endDate and t.permitIP is not null ";
    List<KpiPayValidate> list = super.createQuery(hql).setParameter("endDate", new Date()).list();
    if (CollectionUtils.isEmpty(list)) {
      return null;
    } else {
      return list;
    }
  }
}
