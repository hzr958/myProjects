package com.smate.center.batch.dao.pdwh.pub.cnki;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubAssign;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubExtend;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubAssign;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * cnki成果地址匹配单位结果.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class CnkiPubAssignDao extends PdwhHibernateDao<CnkiPubAssign, Long> {

  /**
   * 获取CnkiPubAssign.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public CnkiPubAssign getCnkiPubAssign(Long pubId, Long insId) {

    String hql = "from CnkiPubAssign t where t.pubId = ? and t.insId = ? ";
    return super.findUnique(hql, pubId, insId);
  }

  /**
   * 查找单位的所有已匹配上机构的成果.
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CnkiPubAssign> getCnkiPubAssign(Long insId) {
    String hql = "from CnkiPubAssign t where t.result=1 and t.insId = ? ";
    return super.createQuery(hql, insId).list();
  }

  /**
   * 查找单位的所有已匹配上机构的成果ID.
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getCnkiPubAssignId(Long insId) {
    String hql = "select pubId from CnkiPubAssign t where t.result=1 and t.insId = ? ";
    return super.createQuery(hql, insId).list();
  }

  /**
   * 获取需要重新匹配的数据列表.
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CnkiPubAssign> getRematchMatchPub(Long startId) {

    String hql = "from CnkiPubAssign t where t.status = 0 and t.assignId > ?  order by assignId asc ";
    return super.createQuery(hql, startId).setMaxResults(100).list();
  }

  /**
   * 获取需要发送到机构的指派信息.
   * 
   * @param startId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CnkiPubAssign> getNeedSendPub(Long startId, int size) {

    String hql =
        "from CnkiPubAssign t where t.assignId > ? and ((t.result = 1 and t.isSend = 0) or t.isSend = 9)  order by assignId asc ";
    return super.createQuery(hql, startId).setMaxResults(size).list();
  }

  /**
   * 获取特定数量的成果数据.
   * 
   * @param insId
   * @param size 未用到.
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getCnkiPubAssList(Long insId, List<Long> pubIdList, int size) {
    List<Long> resultList = new ArrayList<Long>();
    String hql = "select pubId from CnkiPubAssign t where t.result=1 and t.insId = :insId and t.pubId in (:pubId) ";
    Collection<Collection<Long>> container = ServiceUtil.splitList(pubIdList, 500);
    for (Collection<Long> item : container) {
      // 如果size为0 ，则不限制查询结果记录数.
      if (size == 0) {
        resultList.addAll(super.createQuery(hql).setParameter("insId", insId).setParameterList("pubId", item).list());
      } else {
        if (resultList.size() > size) {
          break;
        }
        resultList.addAll(super.createQuery(hql).setParameter("insId", insId).setParameterList("pubId", item)
            .setMaxResults(size).list());
      }
    }
    return resultList;
  }

  /**
   * 获取需要发送到机构的指派信息.
   * 
   * @param assignId
   * @return
   */
  public CnkiPubAssign getPubAssignByAssignId(Long assignId) {

    String hql = "from CnkiPubAssign t where t.assignId = ? and ((t.result = 1 and t.isSend = 0) or t.isSend = 9) ";
    return super.findUnique(hql, assignId);
  }

  /**
   * 获取需要发送到机构的成果的xml.
   * 
   * @param pubId
   * @return
   */
  public CnkiPubExtend getCnkiPubExtend(Long pubId) {

    String hql = "from CnkiPubExtend t where t.pubId = ?";
    return super.findUnique(hql, pubId);
  }

  /**
   * 获取需要发送到某机构的成果Id.
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getCnkiPubId(Long insId) {

    String hql = "select t.pubId from CnkiPubAssign t where t.insId = ? and t.status = 3";
    return (List<Long>) super.createQuery(hql, insId).list();

  }
}
