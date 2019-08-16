package com.smate.center.batch.dao.pdwh.pub.cnkipat;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubAssign;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubExtend;
import com.smate.center.batch.model.pdwh.pub.cnkipat.CnkiPatPubAssign;
import com.smate.center.batch.model.pdwh.pub.cnkipat.CnkiPatPubExtend;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * cnkipat成果地址匹配单位结果.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class CnkiPatPubAssignDao extends PdwhHibernateDao<CnkiPatPubAssign, Long> {

  /**
   * 获取CnkiPatPubAssign.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public CnkiPatPubAssign getCnkiPatPubAssign(Long pubId, Long insId) {

    String hql = "from CnkiPatPubAssign t where t.pubId = ? and t.insId = ? ";
    return super.findUnique(hql, pubId, insId);
  }

  /**
   * 获取需要重新匹配的数据列表.
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CnkiPatPubAssign> getRematchMatchPub(Long startId) {

    String hql = "from CnkiPatPubAssign t where t.status = 0 and t.assignId > ?  order by assignId asc ";
    return super.createQuery(hql, startId).setMaxResults(100).list();
  }

  /**
   * 获取需要发送到机构的指派信息.
   * 
   * @param startId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CnkiPatPubAssign> getNeedSendPub(Long startId, int size) {

    String hql =
        "from CnkiPatPubAssign t where t.assignId > ? and ((t.result = 1 and t.isSend = 0) or t.isSend = 9)  order by assignId asc ";
    return super.createQuery(hql, startId).setMaxResults(size).list();
  }

  /**
   * 获取需要发送到机构的指派信息.
   * 
   * @param assignId
   * @return
   */
  public CnkiPatPubAssign getPubPatAssignByAssignId(Long assignId) {

    String hql = "from CnkiPatPubAssign t where t.assignId = ? and ((t.result = 1 and t.isSend = 0) or t.isSend = 9) ";
    return super.findUnique(hql, assignId);
  }

  /**
   * 获取需要发送到机构的成果的xml.
   * 
   * @param pubId
   * @return
   */
  public CnkiPatPubExtend getCnkiPatPubExtend(Long pubId) {

    String hql = "from CnkiPatPubExtend t where t.pubId = ?";
    return super.findUnique(hql, pubId);
  }
}
