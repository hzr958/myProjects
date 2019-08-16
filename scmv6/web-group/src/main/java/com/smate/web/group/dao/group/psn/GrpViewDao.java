package com.smate.web.group.dao.group.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.psn.GrpView;

/**
 * 群组访问记录dao
 * 
 * @author yhx
 * @date 2019年7月23日
 */
@Repository
public class GrpViewDao extends SnsHibernateDao<GrpView, Long> {

  public GrpView findGrpView(Long psnId, Long grpId, Long formateDate, String ip) {
    String hql = "from GrpView t where t.viewPsnId = ? and t.grpId = ? and t.formateDate = ? and ";
    List<GrpView> list = null;
    if (ip != null) {
      hql += " t.ip = ? ";
      list = super.createQuery(hql, psnId, grpId, formateDate, ip).list();
    } else {
      hql += " t.ip is null ";
      list = super.createQuery(hql, psnId, grpId, formateDate).list();
    }

    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }
}
