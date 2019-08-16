package com.smate.center.batch.dao.pdwh.pub.ei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.ei.EiPubAssign;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.string.ServiceUtil;


/**
 * isi成果地址匹配单位结果.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class EiPubAssignDao extends PdwhHibernateDao<EiPubAssign, Long> {

  /**
   * 获取EiPubAssign.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public EiPubAssign getEiPubAssign(Long pubId, Long insId) {

    String hql = "from EiPubAssign t where t.pubId = ? and t.insId = ? ";
    return super.findUnique(hql, pubId, insId);
  }

  /**
   * 查找单位所有已匹配上的成果.
   * 
   * @param insId
   * @return
   */
  public List<EiPubAssign> getEiPubAssignList(Long insId) {
    String hql = "from EiPubAssign t where t.result=1 and t.insId = ? ";
    return super.find(hql, insId);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public void pubStatistics(List<Long> insIds, Page<Object> page) {

    // 查询HQL
    StringBuilder countHql = new StringBuilder("select count(distinct insId) from EiPubAssign t  where 1 = 1 ");
    StringBuilder queryHql = new StringBuilder("select t.insId,count(t.assignId) from EiPubAssign t where 1 = 1 ");
    if (insIds != null) {
      countHql.append(" and insId in (:insIds)");
      queryHql.append(" and insId in (:insIds)");
    }
    queryHql.append(" group by insId order by count(t.assignId) desc ");

    // 统计
    Long count = 0L;
    if (insIds != null) {
      count = (Long) super.createQuery(countHql.toString()).setParameterList("insIds", insIds).uniqueResult();
    } else {
      count = (Long) super.createQuery(countHql.toString()).uniqueResult();
    }
    page.setTotalCount(count);
    if (count == 0) {
      return;
    }
    // 获取实际数据
    Query queryResult = null;
    if (insIds != null) {
      queryResult = super.createQuery(queryHql.toString()).setParameterList("insIds", insIds);
    } else {
      queryResult = super.createQuery(queryHql.toString());
    }
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    List<Object[]> dataList = queryResult.list();
    // 转换成MAP
    List<Object> resultList = new ArrayList<Object>();
    // 机构ID与统计信息映射
    Map<Long, Map> insMap = new HashMap<Long, Map>();
    for (Object[] objs : dataList) {
      Long insId = (Long) objs[0];
      Long ct = (Long) objs[1];
      Map map = new HashMap();
      map.put("insId", insId);
      map.put("count", ct);
      map.put("acount", 0);
      map.put("bcount", 0);
      map.put("ccount", 0);
      map.put("dcount", 0);
      insMap.put(insId, map);
      resultList.add(map);
    }
    // 1:匹配上机构，2部分匹配上机构，3不确认是否是机构成果，4不是机构成果
    this.pubStatistics(insMap, 1, "acount");
    this.pubStatistics(insMap, 2, "bcount");
    this.pubStatistics(insMap, 3, "ccount");
    this.pubStatistics(insMap, 4, "dcount");
    page.setResult(resultList);
  }

  /**
   * 传入各种状态统计匹配结果.
   * 
   * @param insMap
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private void pubStatistics(Map<Long, Map> insMap, final int result, final String key) {
    Set<Long> insIds = insMap.keySet();
    String hql =
        "select t.insId,count(t.insId) from EiPubAssign t where  t.result = :result and t.insId in(:insIds) group by t.insId";
    Query queryResult = super.createQuery(hql).setParameter("result", result).setParameterList("insIds", insIds);
    List<Object[]> dataList = queryResult.list();
    for (Object[] objs : dataList) {
      Long insId = (Long) objs[0];
      Long ct = (Long) objs[1];
      Map map = insMap.get(insId);
      map.put(key, ct);
    }
  }

  /**
   * 查找单位所有已匹配上的成果.
   * 
   * @param insId
   * @return
   */
  public List<Long> getEiPubAssignIdList(Long insId) {
    String hql = "select pubId from EiPubAssign t where t.result=1 and t.insId = ? ";
    return super.find(hql, insId);
  }

  /**
   * 加载匹配成果结果XML_ID列表.
   * 
   * @param matched
   * @param insId
   * @param page
   */
  @SuppressWarnings("unchecked")
  public void loadInsPubAssign(Integer result, Long insId, Page<Object> page) {

    String countHql = "select count(t.insId) from EiPubAssign t where t.result = :result and t.insId = :insId";
    Long count =
        (Long) super.createQuery(countHql).setParameter("result", result).setParameter("insId", insId).uniqueResult();
    page.setTotalCount(count);
    if (count == 0) {
      return;
    }
    String hql = "select pubId from EiPubAssign t where t.result in(:result) and t.insId = :insId order by assignId ";
    // 获取实际数据
    Query queryResult = super.createQuery(hql).setParameter("result", result).setParameter("insId", insId);
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
  }

  /**
   * 获取需要重新匹配的数据列表(2部分匹配上机构，3不确认是否是机构成果).
   * 
   * @param insId
   * @return
   */
  public List<EiPubAssign> getNeedMatchPub(Long insId) {

    String hql = "from EiPubAssign t where t.insId = ? and t.result in(2,3)";
    return super.find(hql, insId);
  }

  /**
   * 获取需要重新匹配的数据列表.
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<EiPubAssign> getRematchMatchPub(Long startId) {

    String hql = "from EiPubAssign t where t.status = 0 and t.assignId > ?  order by assignId asc ";
    return super.createQuery(hql, startId).setMaxResults(100).list();
  }

  /**
   * 获取需要发送到机构的指派信息.
   * 
   * @param startId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<EiPubAssign> getNeedSendPub(Long startId, int size) {

    String hql =
        "from EiPubAssign t where t.assignId > ? and ((t.result = 1 and t.isSend = 0) or t.isSend = 9)  order by assignId asc ";
    return super.createQuery(hql, startId).setMaxResults(size).list();
  }

  /**
   * 获取特定数量的成果数据.
   * 
   * @param insId
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getIsiPubAssList(Long insId, List<Long> pubIdList, int size) {
    List<Long> resultList = new ArrayList<Long>();
    String hql = "select pubId from EiPubAssign t where t.result=1 and t.insId = :insId and t.pubId in (:pubId) ";
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
   * @param startId
   * @return
   */
  public EiPubAssign getPubAssignByAssignId(Long assignId) {

    String hql = "from EiPubAssign t where t.assignId = ? and ((t.result = 1 and t.isSend = 0) or t.isSend = 9) ";
    return super.findUnique(hql, assignId);
  }
}
