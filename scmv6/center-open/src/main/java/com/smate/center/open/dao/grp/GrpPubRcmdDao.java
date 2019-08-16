package com.smate.center.open.dao.grp;

import com.smate.center.open.model.grp.GrpPubRcmd;
import com.smate.core.base.utils.data.SnsHibernateDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 群组成果推荐实体
 * 
 * @author tsz
 *
 */
@Repository
public class GrpPubRcmdDao extends SnsHibernateDao<GrpPubRcmd, Long> {


  public List<GrpPubRcmd> getAllGrpPubRcmd(Long grpId, int pageNo, int pageSize, Date lastGetPubDate) {
    String hql = "from GrpPubRcmd t where t.grpId=:grpId and t.status!=8 ";
    if (lastGetPubDate != null) {
      hql += " and t.createDate >=:lastGetPubDate";
    }
    String order = " order by t.createDate asc";
    Query query = super.createQuery(hql + order).setMaxResults(1).setParameter("grpId", grpId)
        .setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);;
    if (lastGetPubDate != null) {
      query.setParameter("lastGetPubDate", lastGetPubDate);
    }
    Object obj = query.list();
    if (obj != null) {
      return (List<GrpPubRcmd>) obj;
    }
    return null;
  }

  public Long getAllGrpPubRcmdCount(Long grpId, Date lastGetPubDate) {
    String hql = "select count(1) from GrpPubRcmd t where t.grpId=:grpId and t.status!=8 ";
    if (lastGetPubDate != null) {
      hql += " and t.createDate >=:lastGetPubDate";
    }
    Query query = super.createQuery(hql).setParameter("grpId", grpId);

    if (lastGetPubDate != null) {
      query.setParameter("lastGetPubDate", lastGetPubDate);
    }
    Object obj = query.uniqueResult();
    if (obj != null) {
      return (Long) obj;
    }
    return null;
  }

  public List<GrpPubRcmd> findPubIdsByIds(List<Long> ids) {
    String hql = "from GrpPubRcmd t where t.id in (:ids)";
    List result = this.createQuery(hql).setParameterList("ids", ids).list();
    return result;
  }

}
