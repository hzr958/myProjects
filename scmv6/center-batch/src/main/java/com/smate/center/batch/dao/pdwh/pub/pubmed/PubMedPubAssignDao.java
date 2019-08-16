package com.smate.center.batch.dao.pdwh.pub.pubmed;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.pubmed.PubMedPubAssign;
import com.smate.center.batch.model.pdwh.pub.pubmed.PubMedPubExtend;
import com.smate.center.batch.model.pdwh.pub.sps.SpsPubAssign;
import com.smate.center.batch.model.pdwh.pub.sps.SpsPubExtend;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * PUBMED成果地址匹配表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubMedPubAssignDao extends PdwhHibernateDao<PubMedPubAssign, Long> {

  /**
   * 获取PubMedPubAssign.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public PubMedPubAssign getPubMedPubAssign(Long pubId, Long insId) {

    String hql = "from PubMedPubAssign t where t.pubId = ? and t.insId = ? ";
    return super.findUnique(hql, pubId, insId);
  }

  /**
   * 获取需要重新匹配的数据列表.
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubMedPubAssign> getRematchMatchPub(Long startId) {

    String hql = "from PubMedPubAssign t where t.status = 0 and t.assignId > ?  order by assignId asc ";
    return super.createQuery(hql, startId).setMaxResults(100).list();
  }

  /**
   * 获取需要发送到机构的指派信息.
   * 
   * @param startId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubMedPubAssign> getNeedSendPub(Long startId, int size) {

    String hql =
        "from PubMedPubAssign t where t.assignId > ? and ((t.result = 1 and t.isSend = 0) or t.isSend = 9) order by assignId asc ";
    return super.createQuery(hql, startId).setMaxResults(size).list();
  }

  /**
   * 获取需要发送到机构的指派信息.
   * 
   * @param assignId
   * @return
   */
  public PubMedPubAssign getPubMedPubAssignByAssignId(Long assignId) {

    String hql = "from PubMedPubAssign t where t.assignId = ? and ((t.result = 1 and t.isSend = 0) or t.isSend = 9) ";
    return super.findUnique(hql, assignId);
  }

  /**
   * 获取需要发送到机构的成果的xml.
   * 
   * @param pubId
   * @return
   */
  public PubMedPubExtend getPubMedPubExtend(Long pubId) {

    String hql = "from PubMedPubExtend t where t.pubId = ?";
    return super.findUnique(hql, pubId);
  }
}
