package com.smate.center.open.dao.publication;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.publication.PubAssignLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubAssignLogDao extends SnsHibernateDao<PubAssignLog, Long> {

  public PubAssignLog getAssignLogById(Long psnId, Long pdwhPubId) {
    String hql = "from PubAssignLog t where t.pdwhPubId = :pdwhPubId and t.psnId= :psnId  and t.status=0";
    return (PubAssignLog) super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("psnId", psnId)
        .uniqueResult();
  }

  public PubAssignLog getAssignLogByPubConfirmId(Long pubConfirmId) {
    String hql = "from PubAssignLog t where  t.id=:pubConfirmId  and t.status=0";
    List list = super.createQuery(hql).setParameter("pubConfirmId", pubConfirmId).list();
    if (list != null && list.size() > 0) {
      return (PubAssignLog) list.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<PubAssignLog> queryPubConfirmIdList(Long psnId) {
    String hql =
        "from PubAssignLog t  where t.confirmResult = 0 and t.psnId = :psnId  and t.status=0 and nvl(t.score,0)>0 order by nvl(t.score,0) desc,t.pdwhPubId desc";
    Query query = super.createQuery(hql).setParameter("psnId", psnId);
    return query.list();
  }

  @SuppressWarnings("unchecked")
  public List<PubAssignLog> queryAllPubAssignLog(List<Long> psnIdList, Date lastGetPubDate, Integer pageNo,
      Integer pageSize) {
    String hql = "from PubAssignLog t  where  t.psnId in( :psnIdList )  and t.status=0 and nvl(t.score,0) > 0";
    if (lastGetPubDate != null) {
      hql += " and t.createDate > :lastGetPubDate ";
    }
    hql += "order by nvl(t.score,0) desc,t.pdwhPubId desc";
    Query query = super.createQuery(hql).setParameterList("psnIdList", psnIdList);
    if (lastGetPubDate != null) {
      query.setParameter("lastGetPubDate", lastGetPubDate);
    }
    return query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
  }

  @SuppressWarnings("unchecked")
  public Integer queryAllCount(List<Long> psnIdList, Date lastGetPubDate) {
    String hql =
        "select count(1) from PubAssignLog t  where  t.psnId in( :psnIdList )  and t.status=0 and nvl(t.score,0) > 0";
    if (lastGetPubDate != null) {
      hql += " and t.createDate > :lastGetPubDate ";
    }
    hql += "order by nvl(t.score,0) desc,t.pdwhPubId desc";
    Query query = super.createQuery(hql).setParameterList("psnIdList", psnIdList);
    if (lastGetPubDate != null) {
      query.setParameter("lastGetPubDate", lastGetPubDate);
    }
    Object result = query.uniqueResult();
    if (result != null) {
      return Integer.parseInt(result.toString());
    }
    return 0;
  }

  public void updateConfirmResult(Long pdwhPubId, Long psnId, int result) {
    String hql =
        "update  PubAssignLog t set  t.confirmResult = :result ,t.confirmDate= :confirmDate where t.pdwhPubId= :pdwhPubId and t.psnId=:psnId  and t.status=0";
    super.createQuery(hql).setParameter("result", result).setParameter("confirmDate", new Date())
        .setParameter("pdwhPubId", pdwhPubId).setParameter("psnId", psnId).executeUpdate();
  }

}
