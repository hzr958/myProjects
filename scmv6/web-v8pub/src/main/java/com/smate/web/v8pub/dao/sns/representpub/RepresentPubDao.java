package com.smate.web.v8pub.dao.sns.representpub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.representpub.RepresentPub;
import com.smate.web.v8pub.po.representpub.RepresentPubPk;

/**
 * 人员代表性成果DAO
 *
 * @author wsn
 * @createTime 2017年3月14日 下午8:25:55
 *
 */
@Repository
public class RepresentPubDao extends SnsHibernateDao<RepresentPub, RepresentPubPk> {


  /**
   * 获取人员代表性成果ID
   * 
   * @param psnId
   * @param status
   * @return
   */
  public List<Long> findPsnRepresentPubIds(Long psnId, Integer status, Long cnfId, Integer anyUser) {
    // 增加逻辑，过滤个人已被删除的成果
    String hql =
        "select t.representPubPk.pubId from RepresentPub t where t.representPubPk.psnId = :psnId and t.status = :status "
            + "and (exists (select 1 from PsnConfigPub pcp "
            + "where pcp.id.cnfId = :cnfId and pcp.anyUser > :anyUser and pcp.id.pubId = t.representPubPk.pubId)"
            + "or not exists(select 1 from PsnConfigPub p where p.id.cnfId = :cnfId and p.id.pubId = t.representPubPk.pubId)) "
            + "and exists(select 1 from PsnPubPO pp where pp.pubId = t.representPubPk.pubId and pp.ownerPsnId = :psnId and pp.status = 0) "
            + "order by t.seqNo asc";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("status", status)
        .setParameter("cnfId", cnfId).setParameter("anyUser", anyUser).list();
  }

  /**
   * 更新代表性成果状态
   * 
   * @param psnId
   * @param status
   */
  public void updatePsnRepresentPubStatus(Long psnId, Integer status) {
    String hql = "update RepresentPub t set t.status = :status where t.representPubPk.psnId = :psnId";
    super.createQuery(hql).setParameter("status", status).setParameter("psnId", psnId).executeUpdate();
  }

  /**
   * 查找人员代表性成果
   * 
   * @param psnId
   * @param pubId
   * @return
   */
  public RepresentPub findPsnRepresentPub(Long psnId, Long pubId) {
    String hql = " from RepresentPub t where t.representPubPk.psnId = :psnId and t.representPubPk.pubId = :pubId";
    return (RepresentPub) super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubId", pubId)
        .uniqueResult();
  }
}
