package com.smate.web.psn.dao.cooperation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.model.cooperation.PsnCopartner;

@Repository
public class PsnCopartnerDao extends SnsHibernateDao<PsnCopartner, Long> {
  /**
   * 查询合作者
   *
   * @param page
   * @param cptType
   * @param psnId
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Long> getCopartner(Page page, Integer cptType, Long psnId, String type) {
    String countSql =
        "select count(distinct(t.co_Psn_Id)) from psn_copartner t where t.psn_Id=:psnId and not exists (select 1 from PSN_PRIVATE pp where pp.psn_Id=t.co_Psn_Id)";
    String hql = "";
    String cptHql = "";
    if (1 == cptType) {
      cptHql = " and t.coType =1 ";
    } else if (2 == cptType) {
      cptHql = "and t.coType =2 ";
    }
    page.setTotalCount(
        this.getSession().createSQLQuery(countSql).setParameter("psnId", psnId).uniqueResult() == null ? 0L
            : ((BigDecimal) this.getSession().createSQLQuery(countSql).setParameter("psnId", psnId).uniqueResult())
                .longValue());
    String hql1 =
        "select t.coPsnId,count(t.coPsnId) from PsnCopartner t where t.psnId=:psnId and not exists (select 1 from PsnPrivate pp where pp.psnId=t.coPsnId)";
    String orderHql = "Group By t.coPsnId order by count(t.coPsnId) Desc ";
    String strangerHql = " and not exists(select 1 from Friend f where f.psnId=:psnId and f.friendPsnId=t.coPsnId)";
    Query query = null;

    if ("1".equals(type)) {// 不是好友且没发送过好友请求的
      String notRequest =
          " and not exists(select a.receivePsnId from  FriendReqRecord a where a.sendPsnId = :psnId and a.receivePsnId = t.coPsnId and a.status != 1)";
      query = super.createQuery(hql1 + hql + cptHql + strangerHql + notRequest + orderHql);
    }
    if ("2".equals(type)) {// 不是好友，但发送过好友请求的
      query = super.createQuery(hql1 + hql + cptHql + strangerHql + orderHql);
    }
    if ("3".equals(type)) {// 是好友的
      String friendHql = " and exists(select 1 from Friend f where f.psnId=:psnId and f.friendPsnId=t.coPsnId)";
      query = super.createQuery(hql1 + hql + cptHql + friendHql + orderHql);
    }
    List<Object[]> list = query.setParameter("psnId", psnId).list();
    List<Long> coPsnIds = new ArrayList<Long>();
    for (Object[] psnObject : list) {
      Long coPsnId = (Long) psnObject[0];
      coPsnIds.add(coPsnId);
    }
    return coPsnIds;
  }

}
