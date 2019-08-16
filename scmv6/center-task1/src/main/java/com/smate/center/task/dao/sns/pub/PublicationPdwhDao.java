package com.smate.center.task.dao.sns.pub;


import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.PublicationPdwh;
import com.smate.center.task.single.service.pub.ConstPdwhPubRefDb;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果基准库id.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PublicationPdwhDao extends SnsHibernateDao<PublicationPdwh, Long> {

  @SuppressWarnings("rawtypes")
  public int getPubPdwhIdByPsnFriend(Long psnId, Long pdwhPubId, int dbid) {
    String hql =
        "select count(t.pubId) from PublicationPdwh t,PubKnow t2,Friend t3 where t.pubId=t2.id and t2.psnId=t3.friendPsnId and t3.psnId=:psnId and ";
    return makeFindPwdhPub(psnId, pdwhPubId, dbid, hql);
  }

  @SuppressWarnings("rawtypes")
  private int makeFindPwdhPub(Long psnId, Long pdwhPubId, int dbid, String hql) {
    if (dbid == ConstPdwhPubRefDb.ISI) {
      hql += " t.isiId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.EI) {
      hql += " t.eiId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.SCOPUS) {
      hql += " t.spsId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.CNKI) {
      hql += " t.cnkiId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.WanFang) {
      hql += " t.wfId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.CNIPR) {
      hql += " t.cniprId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.PubMed) {
      hql += " t.pubmedId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.ScienceDirect) {
      hql += " t.scdId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.IEEE) {
      hql += " t.ieeeXpId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.Baidu) {
      hql += " t.baiduId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.Cnkipat) {
      hql += " t.cnkiPatId in(:pdwhIds) ";
    } else {
      return 0;
    }
    List list = super.createQuery(hql).setParameter("psnId", psnId).setParameter("pdwhIds", pdwhPubId).list();
    return CollectionUtils.isEmpty(list) ? 0 : NumberUtils.toInt(ObjectUtils.toString(list.get(0)));
  }
}
