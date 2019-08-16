package com.smate.core.base.project.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.project.model.ProjectExpenRecord;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 项目经费支出记录表
 * 
 * @author YJ
 *
 *         2019年8月6日
 */
@Repository
public class ProjectExpenRecordDao extends SnsHibernateDao<ProjectExpenRecord, Long> {

  @SuppressWarnings("unchecked")
  public List<ProjectExpenRecord> findByExpenId(Long expenId) {
    String hql = "from ProjectExpenRecord t where t.expenId=:expenId and t.status = 0 "
        + "and exists(select 1 from ProjectExpenditure pp where pp.id = t.expenId and pp.status = 0) "
        + "order by t.gmtExpen asc";
    return this.createQuery(hql).setParameter("expenId", expenId).list();
  }

  /**
   * 统计已用金额
   * 
   * @param expenId
   * @return
   */
  public Float countUsedAmount(Long expenId) {
    String hql =
        "SELECT nvl(sum(t.expenAmount),0) FROM ProjectExpenRecord t where t.status = 0 and to_char(t.gmtExpen,'yyyyMMdd') <= to_char(t.gmtModified,'yyyyMMdd') and t.expenId =?";
    Double amount = super.findUnique(hql, new Object[] {expenId});
    return Float.parseFloat(String.valueOf(amount));
  }

  /**
   * 统计预支金额
   * 
   * @param expenId
   * @return
   */
  public Float countAdvanceAmount(Long expenId) {
    String hql =
        "SELECT nvl(sum(t.expenAmount),0) FROM ProjectExpenRecord t where t.status = 0 and to_char(t.gmtExpen,'yyyyMMdd') > to_char(t.gmtModified,'yyyyMMdd') and t.expenId =?";
    Double amount = super.findUnique(hql, new Object[] {expenId});
    return Float.parseFloat(String.valueOf(amount));
  }

}
