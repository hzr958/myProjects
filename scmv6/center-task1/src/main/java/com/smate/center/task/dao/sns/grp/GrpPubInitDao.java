package com.smate.center.task.dao.sns.grp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.grp.GrpPubInit;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class GrpPubInitDao extends SnsHibernateDao<GrpPubInit, Long> {

  public GrpPubInit getGrpPub(Long grpId, Long pdwhPubId) {
    String hql = " from GrpPubInit t where t.grpId = :grpId and t.pubId = :pdwhPubId";
    return (GrpPubInit) super.createQuery(hql).setParameter("grpId", grpId).setParameter("pdwhPubId", pdwhPubId)
        .uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<GrpPubInit> getGrpPubInit(Long grpId, int year, int number) {
    List<GrpPubInit> grpPubInitList = new ArrayList<GrpPubInit>();
    // 1.有全文高引用的成果
    String hql =
        "from GrpPubInit t where t.grpId = :grpId and t.pubYear = :year and t.hasFulltext =1 and t.status=0  order by t.citations desc";
    // 1.无全文高引用的成果
    String hql2 =
        " from GrpPubInit t where t.grpId = :grpId and t.pubYear = :year and t.hasFulltext =0   and t.status=0  order by t.citations desc";
    List<GrpPubInit> grpPubInitList1 =
        super.createQuery(hql).setParameter("grpId", grpId).setParameter("year", year).setMaxResults(number).list();
    if (grpPubInitList1 == null) {
      List<GrpPubInit> GrpPubInitList2 =
          super.createQuery(hql2).setParameter("grpId", grpId).setParameter("year", year).setMaxResults(number).list();
      grpPubInitList.addAll(GrpPubInitList2);
    } else if (grpPubInitList1 != null && grpPubInitList1.size() < number) {// 2.如果有全文的成果小于number，取无全文高引用的成果
      grpPubInitList.addAll(grpPubInitList1);
      List<GrpPubInit> GrpPubInitList2 = super.createQuery(hql2).setParameter("grpId", grpId).setParameter("year", year)
          .setMaxResults(number - grpPubInitList1.size()).list();
      grpPubInitList.addAll(GrpPubInitList2);
    } else {
      grpPubInitList.addAll(grpPubInitList1);
    }
    return grpPubInitList;
  }

  @SuppressWarnings("unchecked")
  public List<GrpPubInit> getGrpPubInit(Long grpId) {
    String hql = "from GrpPubInit t where t.grpId = :grpId";
    return super.createQuery(hql).setParameter("grpId", grpId).list();

  }

}
