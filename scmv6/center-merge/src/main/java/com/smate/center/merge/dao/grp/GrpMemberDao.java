package com.smate.center.merge.dao.grp;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.merge.model.sns.grp.GrpMember;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组成员dao
 * 
 * @author tsz
 *
 */

@Repository
public class GrpMemberDao extends SnsHibernateDao<GrpMember, Long> {

  /**
   * 获取我的所有群组
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GrpMember> getGrpByPsnId(Long psnId) throws Exception {
    String hql =
        " from GrpMember t where  t.psnId=:psnId and t.status='01' and  exists(select t1.grpId from GrpBaseinfo t1 where t1.grpId=t.grpId and t1.status='01' )";

    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 通过群组id跟psnid判断是否属于该群组成员
   * 
   * @param psnId
   * @param grpId
   * @return
   */
  public GrpMember getGrpMemberByGrpIdAndPsnId(Long psnId, Long grpId) {
    String hql = " from GrpMember t where  t.psnId=:psnId and t.grpId=:grpId and t.status='01' ";
    Object obj = super.createQuery(hql).setParameter("psnId", psnId).setParameter("grpId", grpId).uniqueResult();
    if (obj == null) {
      return null;
    }
    return (GrpMember) obj;
  }

  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
  public void deleteGrpMember(GrpMember grpMember) {

    this.delete(grpMember);
  }

  /**
   * 统计群组成员的数量
   * 
   * @param grpId
   * @return
   */
  public Integer countGrpMenberNum(Long grpId) {
    String hql = "select count(1)  from   GrpMember t where t.grpId =:grpId and  t.status = '01'  ";
    Object obj = this.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
    if (obj != null) {
      return Integer.parseInt(obj.toString());
    }
    return 0;
  }

}
