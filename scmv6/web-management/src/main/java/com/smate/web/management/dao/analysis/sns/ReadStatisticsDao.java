package com.smate.web.management.dao.analysis.sns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.analysis.sns.ReadStatistics;

/**
 * 阅读统计
 * 
 * @author Scy
 * 
 */
@Repository(value = "readStatisticsDao")
public class ReadStatisticsDao extends SnsHibernateDao<ReadStatistics, Long> {

  /**
   * 遍历查询上月有阅读数据的人员 zk add.
   */
  @SuppressWarnings("rawtypes")
  public List findReadPsn(Integer size, Integer type) {
    // String hql =
    // "select count(r.psnId) as count,r.readPsnId as psnId from ReadStatistics r where
    // r.createDate>=trunc(last_day(add_months(sysdate,-2))+1)and
    // r.createDate<trunc(last_day(add_months(sysdate,-1))+1) group by r.readPsnId having
    // count(r.psnId)>=1";
    String hql =
        "select  sum(totalCount) as count,r.readPsnId as psnId from ReadStatistics r where trunc(r.createDate)>=trunc(sysdate-7)  and r.actionType=?  group by r.readPsnId having count(r.psnId)>=1";
    return super.createQuery(hql, type).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
        .setMaxResults(500).setFirstResult(500 * size).list();
  }

  /**
   * 获取被阅读人信息
   */
  @SuppressWarnings("unchecked")
  public List<ReadStatistics> findReadStatistics(Long psnId) {
    return super.createQuery("from ReadStatistics r where r.readPsnId=?", psnId).list();
  }

  /**
   * 分页查询访问人员
   * 
   * @param page
   * @param readPsnId
   * @return @
   */
  public Page findReadPersonPage(Page page, Long readPsnId) {
    Long count = 0l;
    String countSql =
        "select count(1) from (select distinct t.psn_id,t.formate_date from READ_STATISTICS t where t.READ_PSN_ID = ?)";
    count = super.queryForLong(countSql, new Object[] {readPsnId});
    page.setTotalCount(count);

    String hql =
        "select new ReadStatistics(t.psnId,t.formateDate) from ReadStatistics t where t.readPsnId = ? group by t.psnId,t.formateDate order by max(t.createDate) desc";
    List result =
        super.createQuery(hql, readPsnId).setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    page.setResult(result);
    return page;
  }

  /**
   * 分页查询访问人员
   * 
   * @param page
   * @param readPsnId
   * @return @
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Page findReadPersonPage(Page page, Long readPsnId, Integer actionType) {
    Long count = 0l;
    String countSql =
        "select count(1) from (select distinct t.psn_id,t.formate_date from READ_STATISTICS t where t.READ_PSN_ID = ? and t.ACTION_TYPE=?)";
    count = super.queryForLong(countSql, new Object[] {readPsnId, actionType});
    page.setTotalCount(count);

    String hql =
        "select new ReadStatistics(t.psnId,t.formateDate) from ReadStatistics t where t.readPsnId = ? and t.actionType=? group by t.psnId,t.formateDate order by max(t.createDate) desc";
    List result = super.createQuery(hql, new Object[] {readPsnId, actionType}).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
    page.setResult(result);
    return page;
  }

  /**
   * 删除访问记录
   * 
   * @param psnId
   * @param readPsnId
   * @param formateDate @
   */
  public void delReadRecord(Long psnId, Long readPsnId, Long formateDate) {
    String hql = "delete from ReadStatistics t where t.psnId = ? and t.readPsnId = ? and t.formateDate = ?";
    super.createQuery(hql, psnId, readPsnId, formateDate).executeUpdate();
  }

  /**
   * 计算某种类型东西一段时间内的访问人数
   * 
   * @param readPsnId
   * @param actionType 为空时，表示查询全部
   * @param formateDate
   * @return @
   */
  public Integer countInDate(Long readPsnId, Integer actionType, Long startDate, Long endDate) {
    String hql =
        "select sum(totalCount) from ReadStatistics t where t.readPsnId = :readPsnId and t.formateDate >= :startDate and t.formateDate < :endDate";
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("readPsnId", readPsnId);
    params.put("startDate", startDate);
    params.put("endDate", endDate);
    if (actionType != null) {
      hql += " and t.actionType = :actionType";
      params.put("actionType", actionType);
    }

    Long count = (Long) super.createQuery(hql, params).uniqueResult();
    return count == null ? 0 : count.intValue();
  }

  /**
   * 查找某个日期前的访问过某个人某种类型东西的psnId
   * 
   * @param readPsnId * @param actionType 为空时，表示查询全部
   * @param formateDate
   * @return @
   */
  public List<ReadStatistics> findIpsRecordBefDay(Long readPsnId, Integer actionType, Long formateDate) {
    String hql = "from ReadStatistics t where t.readPsnId = :readPsnId and t.formateDate >= :formateDate";
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("readPsnId", readPsnId);
    params.put("formateDate", formateDate);
    if (actionType != null) {
      hql += " and actionType = :actionType";
      params.put("actionType", actionType);
    }
    return super.createQuery(hql, params).list();
  }

  /**
   * 查找访问记录
   * 
   * @param psnId
   * @param readPsnId
   * @param actionKey
   * @param actionType
   * @param formateDate
   * @return @
   */
  public ReadStatistics findReadRecord(Long psnId, Long readPsnId, Long actionKey, Integer actionType, Long formateDate,
      String ip) {
    String hql =
        "from ReadStatistics t where t.psnId = ? and t.readPsnId = ? and t.actionKey = ? and t.actionType = ? and t.formateDate = ? and ";
    List<ReadStatistics> list = null;
    if (ip != null) {
      hql += " t.ip = ? ";
      list = super.createQuery(hql, psnId, readPsnId, actionKey, actionType, formateDate, ip).list();
    } else {
      hql += " t.ip is null ";
      list = super.createQuery(hql, psnId, readPsnId, actionKey, actionType, formateDate).list();
    }

    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  /**
   * 查看某个人某种类型的东西的被阅读的次数
   * 
   * @param psnId
   * @param actionType 详情请看DynamicConstant.java
   * @return @
   */
  public Long countReadByPsnId(Long psnId, Integer actionType) {
    String sql = "select sum(total_count) from read_statistics t where t.read_psn_id = ? and t.action_type = ?";
    Long count = super.queryForLong(sql, new Object[] {psnId, actionType});
    return count;
  }

  /**
   * 查看某个人被阅读的次数
   */

  public Long countReadByPsnId(Long psnId) {
    String sql = "select count(total_count) from read_statistics t where t.read_psn_id = ? ";
    Long count = super.queryForLong(sql, new Object[] {psnId});
    return count;
  }

  /**
   * 查看某个东西被阅读了多少次
   * 
   * @param actionKey
   * @param actionType
   * @return @
   */
  public Long countReadByKey(Long actionKey, Integer actionType) {
    String hql = "select sum(totalCount) from ReadStatistics t where t.actionKey = ? and t.actionType = ?";
    Long count = (Long) super.createQuery(hql, actionKey, actionType).uniqueResult();
    if (count == null) {
      return 0L;
    }
    return count;
  }

  /**
   * 基准库文献阅读次数
   * 
   * @param actionKeyList
   * @param actionTypeList
   * @return @
   */
  public Long readPdwhCountKey(List<Long> actionKeyList, List<Integer> actionTypeList) {
    String hql =
        "select count(*) from ReadStatistics t where t.actionKey in (:actionKeyList) and t.actionType in (:actionTypeList)";
    Long count = (Long) super.createQuery(hql).setParameterList("actionKeyList", actionKeyList)
        .setParameterList("actionTypeList", actionTypeList).uniqueResult();
    return count;
  }

  /**
   * 删除记录
   * 
   * @param actionKey
   * @param actionType @
   */
  public void delReadRecord(Long actionKey, Integer actionType) {
    String hql = "delete from ReadStatistics t where t.actionKey = ? and t.actionType = ?";
    super.createQuery(hql, actionKey, actionType).executeUpdate();
  }

  /**
   * 查找最近访问的用户ID
   * 
   * @param psnId
   * @param actionType
   * @param maxSize
   * @return @
   */
  public List<ReadStatistics> findRecVist(Long psnId, Integer actionType, int maxSize) {
    String hql =
        "select new ReadStatistics(t.psnId,max(t.createDate)) from ReadStatistics t where t.readPsnId = :readPsnId and t.psnId <> 0";
    Map<String, Object> paraMap = new HashMap<String, Object>();
    paraMap.put("readPsnId", psnId);
    if (actionType != null) {
      hql += " and t.actionType = :actionType";
      paraMap.put("actionType", actionType);
    }
    hql += " group by t.psnId order by max(t.createDate) desc";
    return super.createQuery(hql, paraMap).setMaxResults(maxSize).list();
  }

  /**
   * 获取阅读统计记录列表.
   * 
   * @param psnId
   * @return
   */
  public List<ReadStatistics> getReadStaticList(Long psnId) {
    String ql = "from ReadStatistics where psnId = ? ";
    List<ReadStatistics> resultList = super.createQuery(ql, psnId).list();
    ql = "from ReadStatistics where readPsnId=? ";
    List<ReadStatistics> resultList2 = super.createQuery(ql, psnId).list();
    resultList.addAll(resultList2);
    return resultList;
  }
}
