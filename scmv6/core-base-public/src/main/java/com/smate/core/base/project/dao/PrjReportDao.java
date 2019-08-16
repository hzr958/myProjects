package com.smate.core.base.project.dao;

import com.smate.core.base.project.model.PrjReport;
import com.smate.core.base.utils.data.SnsHibernateDao;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目报告Dao
 * 
 * @author yhx
 * @date 2019年8月5日
 *
 */
@Repository
public class PrjReportDao extends SnsHibernateDao<PrjReport, Serializable> {

  @SuppressWarnings("unchecked")
  public List<PrjReport> getReportList(Long prjId, List<Integer> allreportType) {
    StringBuffer hql = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    hql.append("from PrjReport where prjId=?");
    params.add(prjId);
    if (CollectionUtils.isNotEmpty(allreportType)) {
      hql.append(" and reportType in(:reportType) order by gmtCreate desc");
      return super.createQuery(hql.toString(), params.toArray()).setParameterList("reportType", allreportType).list();
    } else {
      hql.append(" order by gmtCreate desc");
      return super.createQuery(hql.toString(), params.toArray()).list();
    }

  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> queryReportCountList(Long prjId) {
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    String includeType = " select new Map( t.reportType as reportType  ,count(t.id) as count) ";
    StringBuilder hql = new StringBuilder();
    hql.append(" from PrjReport t where t.prjId = ?");
    params.add(prjId);
    hql.append("  group by  t.reportType  ");
    return this.createQuery(includeType + hql.toString(), params.toArray()).list();
  }

  public Long getReportSum(Long prjId) {
    String hql = "select count(id) from PrjReport where prjId=:prjId";
    return (Long) super.createQuery(hql).setParameter("prjId", prjId).uniqueResult();
  }
  public void delete(Long prjId){
    String hql = "delete from  PrjReport t where t.prjId=:prjId" ;
    super.createQuery(hql).setParameter("prjId", prjId).executeUpdate();
  }

  public PrjReport getPrjReport(Long prjId) {
    String hql = "from PrjReport p where p.prjId=:prjId and p.warnDate>=trunc(sysdate) order by p.warnDate desc";
    List list = this.createQuery(hql).setParameter("prjId", prjId).list();
    if (list != null && list.size() > 0) {
      return (PrjReport) list.get(0);
    }
    return null;
  }
}
