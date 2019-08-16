package com.smate.web.prj.dao.project;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.prj.model.common.PrjGroupRelation;

/**
 * 项目-群组关系dao.
 * 
 * @author zx
 * 
 */
@Repository
public class PrjGroupRelationDao extends SnsHibernateDao<PrjGroupRelation, Long> {
  /**
   * 查找项目群组关系的群组Id
   * 
   * @param prjId
   * @return
   * @throws Exception
   */
  public Long findGroupIdByPrjId(Long prjId) throws Exception {
    String hql = "select t.groupId from PrjGroupRelation t where t.prjId=:prjId";
    return (Long) super.createQuery(hql).setParameter("prjId", prjId).uniqueResult();
  }

  /**
   * 查找群组是不是隐私
   */
  public String findGroupPrivacy(Long grpId) throws Exception {
    String hql = "select t.openType from GrpBaseinfo t where t.grpId=:grpId";
    return (String) super.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
  }

  /**
   * 查找这个人是不是这个群组的成员
   */
  public Long findGroupMember(Long psnId, Long grpId) throws Exception {
    String hql = "select t.id from GrpMember t where t.psnId=:psnId and t.grpId=:grpId";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("grpId", grpId).uniqueResult();
  }

  /**
   * 通过群组Id找到对应的项目ID
   * 
   * @param grpId
   * @return
   * @throws Exception
   */
  public Long findPrjIdByGroupId(Long grpId) throws Exception {
    String hql = "select t.prjId from PrjGroupRelation t where t.groupId=:grpId";
    return (Long) super.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
  }
}
