package com.smate.core.base.project.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.project.model.PrjReportAccessory;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 项目报告附件dao
 * 
 * @author yhx
 * @date 2019年8月6日
 *
 */
@Repository
public class PrjReportAccessoryDao extends SnsHibernateDao<PrjReportAccessory, Serializable> {

  public void deleteByReportId(Long reportId) {
    String hql = "delete from PrjReportAccessory t where t.reportId =:reportId";
    this.createQuery(hql).setParameter("reportId", reportId).executeUpdate();
  }

  public List<PrjReportAccessory> getByReportIds(List<Long> reportIds) {
    String hql = "from PrjReportAccessory t where t.reportId in (:reportIds)";
    List list = this.createQuery(hql).setParameterList("reportIds", reportIds).list();
    return list;
  }
}
