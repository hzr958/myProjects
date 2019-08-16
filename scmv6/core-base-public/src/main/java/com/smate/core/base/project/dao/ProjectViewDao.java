package com.smate.core.base.project.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.project.model.ProjectView;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 项目阅读记录Dao
 * 
 * @author YJ
 *
 *         2019年8月7日
 */
@Repository
public class ProjectViewDao extends SnsHibernateDao<ProjectView, Long> {

  public ProjectView findPrjView(Long psnId, Long prjId, long formateDate, String ip) {
    String hql = "from ProjectView t where t.viewPsnId = ? and t.prjId = ? "
        + " and exists (SELECT 1 FROM Project pp where pp.status = 0 and pp.id = t.prjId) "
        + " and t.formateDate = ? and ";
    List<ProjectView> list = null;

    if (StringUtils.isNotBlank(ip)) {
      hql += " t.ip = ? ";
      list = super.createQuery(hql, psnId, prjId, formateDate, ip).list();
    } else {
      hql += " t.ip is null ";
      list = super.createQuery(hql, psnId, prjId, formateDate).list();
    }

    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

}
