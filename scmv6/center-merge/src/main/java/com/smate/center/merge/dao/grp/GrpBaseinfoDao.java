package com.smate.center.merge.dao.grp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.merge.model.sns.grp.GrpBaseinfo;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组dao
 * 
 * @author tsz
 *
 */

@Repository
public class GrpBaseinfoDao extends SnsHibernateDao<GrpBaseinfo, Long> {

  /**
   * 获取群组
   * 
   * @param ownerPsnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GrpBaseinfo> getGrpByOwnerPsnId(Long ownerPsnId) throws Exception {
    String hql = " from GrpBaseinfo t where t.ownerPsnId=:ownerPsnId and t.status='01' ";

    return super.createQuery(hql).setParameter("ownerPsnId", ownerPsnId).list();
  }

  /**
   * 获取群组
   * 
   * @param grpid
   * @return
   */
  public GrpBaseinfo getGrpByGrpId(Long grpId) throws Exception {
    String hql = " from GrpBaseinfo t where t.grpId=:grpId and t.status='01' ";
    Object obj = super.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
    if (obj == null) {
      return null;
    }
    return (GrpBaseinfo) obj;
  }

  /**
   * 获取群组ids
   * 
   * @param ownerPsnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getGrpIdsByOwnerPsnId(Long ownerPsnId) throws Exception {
    String sql = "select   from  V_GRP_BASEINFO  g where g.OWNER_PSN_ID = ? and g.STATUS = '01' ";
    List<Object> list = new ArrayList<>();
    list.add(ownerPsnId);
    return super.queryForList(sql, list.toArray());
  }

}
