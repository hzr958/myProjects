package com.smate.center.batch.dao.sns.pub;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.GrpPubs;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * @author zjh 群组成果
 *
 */
@Repository
public class GrpPubsDao extends SnsHibernateDao<GrpPubs, Long> {
  public GrpPubs getGrpPubs(Long grpId, Long pubId) {
    String hql = "from GrpPubs where grpId=:grpId and pubId=:pubId";
    return (GrpPubs) super.createQuery(hql).setParameter("grpId", grpId).setParameter("pubId", pubId).uniqueResult();
  }

  public Long getId() {
    String sql = "select SEQ_V_GRP_PUBS.nextval from dual";
    BigDecimal id = (BigDecimal) super.getSession().createSQLQuery(sql).uniqueResult();
    return id.longValue();

  }

  /**
   * 根据群组ID获取群组成果详细信息.
   * 
   * @param groupId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GrpPubs> getGroupPubList(Long grpId) {
    String hql = "from GrpPubs t where t.grpId =:grpId and t.status=0";
    return super.createQuery(hql).setParameter("grpId", grpId).list();
  }

  public Long getCountGroupPub(Long pubId, Long groupId) {
    String hql = "select count(*) from GrpPubs t where t.pubId=? and t.grpId=?";
    return super.findUnique(hql, new Object[] {pubId, groupId});

  }

  /**
   * 根据pubId获取GrpPubs
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GrpPubs> findGrpPubs(Long pubId) {
    String sql = "from GrpPubs t where t.pubId=:pubId and t.status=0";
    return super.createQuery(sql).setParameter("pubId", pubId).list();
  }

}
