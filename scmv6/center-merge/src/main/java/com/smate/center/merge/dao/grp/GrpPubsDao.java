package com.smate.center.merge.dao.grp;

import com.smate.center.merge.model.sns.grp.GrpPubs;
import com.smate.core.base.utils.data.SnsHibernateDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 群组成果dao.
 * 
 * @author tsz
 */
@Repository
public class GrpPubsDao extends SnsHibernateDao<GrpPubs, Long> {
  /**
   * 获取我的所有群组成果.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GrpPubs> getGrpPubsByPsnId(Long createPsnId) throws Exception {
    String hql = " from GrpPubs t where  t.createPsnId=:createPsnId and t.status=0  and  "
        + "exists(select t1.grpId from GrpBaseinfo t1 where t1.grpId=t.grpId and t1.status='01' )";
    return super.createQuery(hql).setParameter("createPsnId", createPsnId).list();
  }

  /**
   * 获取群组成果 通过拥有者ID.
   * 
   * @param ownerPsnId not null
   * @return list
   */
  @SuppressWarnings("unchecked")
  public List<GrpPubs> getGrpPubsByOwnerPsnId(Long ownerPsnId) throws Exception {
    String hql = " from GrpPubs t where  t.ownerPsnId=:ownerPsnId and t.status=0  and "
        + " exists(select t1.grpId from GrpBaseinfo t1 where t1.grpId=t.grpId and t1.status='01' )";
    return super.createQuery(hql).setParameter("ownerPsnId", ownerPsnId).list();
  }
}
