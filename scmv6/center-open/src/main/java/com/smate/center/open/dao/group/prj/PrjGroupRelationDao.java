package com.smate.center.open.dao.group.prj;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.group.prj.PrjGroupRelation;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 项目-群组关系dao.
 * 
 * @author xieyushou
 * 
 */
@Repository
public class PrjGroupRelationDao extends SnsHibernateDao<PrjGroupRelation, Long> {

  public Long findGroupIdByPrjId(Long prjId) throws Exception {
    String hql = "select t.groupId from PrjGroupRelation t where t.prjId=?";
    return super.findUnique(hql, prjId);
  }

  public PrjGroupRelation findPrjGroupRelation(Long groupId) throws Exception {
    String hql = "from PrjGroupRelation t where t.groupId=?";
    return super.findUnique(hql, groupId);
  }

  public void delPrjGroupRelation(Long groupId) {
    String hql = "delete from PrjGroupRelation t where t.groupId=?";
    super.createQuery(hql, groupId).executeUpdate();
  }
}
