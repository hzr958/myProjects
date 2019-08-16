package com.smate.center.batch.dao.sns.prj;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.prj.AwardStatistics;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 赞操作统计模块
 * 
 * @author Scy
 * 
 */
@Repository(value = "awardStatisticsDao")
public class AwardStatisticsDao extends SnsHibernateDao<AwardStatistics, Long> {

  /**
   * 获取赞数据 zk add.
   */
  public List findAwardPsn(Integer size, Integer type) throws Exception {
    // String hql =
    // "select count(p.id) as count,p.awardPsnId as psnId from AwardStatistics p where
    // p.createDate>=trunc(last_day(add_months(sysdate,-2))+1)and
    // p.createDate<trunc(last_day(add_months(sysdate,-1))+1) and action=1 group by p.awardPsnId having
    // count(p.awardPsnId)>=1";
    String hql =
        "select  count(p.id) as count,p.awardPsnId  as psnId from AwardStatistics p where trunc(p.createDate)>=trunc(sysdate-49)  and action=1 and p.actionType = ? group by p.awardPsnId having count(p.awardPsnId)>=1";
    return super.createQuery(hql, type).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
        .setMaxResults(100).setFirstResult(size * 100).list();
  }

  /**
   * 分页查询赞人员
   * 
   * @param page
   * @param awardPsnId
   * @return
   * @throws Exception
   */
  public Page findAwardPersonPage(Page page, Long awardPsnId) throws Exception {
    Long count = 0l;
    String countSql =
        "select count(1) from (select distinct t.psn_id,t.formate_date from AWARD_STATISTICS t where t.AWARD_PSN_ID = ? and t.action = 1)";
    count = super.queryForLong(countSql, new Object[] {awardPsnId});
    page.setTotalCount(count);

    String hql =
        "select new AwardStatistics(t.psnId,t.formateDate) from AwardStatistics t where t.awardPsnId = ? and t.action = 1 group by t.psnId,t.formateDate order by max(t.createDate) desc";
    List result =
        super.createQuery(hql, awardPsnId).setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    page.setResult(result);
    return page;
  }

  public Page findAwardPersonPage(Page page, Long awardPsnId, Integer actionType) throws Exception {
    Long count = 0l;
    String countSql =
        "select count(1) from (select distinct t.psn_id,t.formate_date from AWARD_STATISTICS t where t.AWARD_PSN_ID = ? and t.action = 1 and t.ACTION_TYPE=?)";
    count = super.queryForLong(countSql, new Object[] {awardPsnId, actionType});
    page.setTotalCount(count);

    String hql =
        "select new AwardStatistics(t.psnId,t.formateDate) from AwardStatistics t where t.awardPsnId = ? and t.action = 1 and t.actionType=? group by t.psnId,t.formateDate order by max(t.createDate) desc";
    List result = super.createQuery(hql, new Object[] {awardPsnId, actionType}).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
    page.setResult(result);
    return page;
  }

  /**
   * 删除赞记录
   * 
   * @param psnId
   * @param awardPsnId
   * @param formateDate
   * @throws Exception
   */
  public void delAwardRecord(Long psnId, Long awardPsnId, Long formateDate) throws Exception {
    String hql = "delete from AwardStatistics t where t.psnId = ? and t.awardPsnId = ? and t.formateDate = ?";
    super.createQuery(hql, psnId, awardPsnId, formateDate).executeUpdate();
  }

  public List<AwardStatistics> findAwardRecord(Long psnId, Long awardPsnId, Long formateDate) throws Exception {
    String hql =
        "from AwardStatistics t where t.psnId = ? and t.awardPsnId = ? and t.formateDate = ? and t.action = 1 and t.actionType = 1";
    return super.createQuery(hql, psnId, awardPsnId, formateDate).list();
  }

  /**
   * 计算一段时间内的赞人数
   * 
   * @param awardPsnId
   * @param formateDate
   * @return
   * @throws Exception
   */
  public Integer countInDate(Long awardPsnId, Long startDate, Long endDate) throws Exception {
    String countSql =
        "select count(*) from award_statistics t where t.award_psn_id = ? and t.formate_date >= ? and t.formate_date < ? and t.action = 1";
    Long count = super.queryForLong(countSql, new Object[] {awardPsnId, startDate, endDate});
    return count.intValue();
  }

  /**
   * 查找某个日期前的赞某个人的psnId
   * 
   * @param awardPsnId
   * @param formateDate
   * @return
   * @throws Exception
   */
  public List<String> findIpsRecordBefDay(Long awardPsnId, Long formateDate) throws Exception {
    String hql = "select t.ip from AwardStatistics t where t.awardPsnId = ? and t.formateDate >= ? and t.action = 1";
    return super.createQuery(hql, awardPsnId, formateDate).list();
  }

  /**
   * 获取赞统计记录列表.
   * 
   * @param psnId
   * @return
   */
  public List<AwardStatistics> getAwardStaticsList(Long psnId) {
    String ql = "from AwardStatistics where psnId = ? ";
    List<AwardStatistics> resultList = super.createQuery(ql, psnId).list();
    ql = "from AwardStatistics where awardPsnId=? ";
    List<AwardStatistics> resultList2 = super.createQuery(ql, psnId).list();
    resultList.addAll(resultList2);
    return resultList;
  }

  /**
   * 获取某人成果被赞的总数
   */
  public Long getAwardtotalPsn(Long psnId, Integer type) {
    return super.queryForLong(
        "select count(id) from award_statistics t where t.award_psn_id = ? and t.action = 1 and ACTION_TYPE = ?",
        new Object[] {psnId, type});
  }

  public Long countAward(Long psnId, Integer actionType) {
    return super.findUnique(
        "select count(t.id) from AwardStatistics t where t.awardPsnId = ? and t.action = 1 and t.actionType=?", psnId,
        actionType);
  }

  /**
   * 查询某个东西被赞的次数
   * 
   * @param actionKey
   * @param actionType
   * @return
   */
  public Long countAwardByKey(Long actionKey, Integer actionType) {
    Long awardCount = super.findUnique(
        "select count(t.id) from AwardStatistics t where t.actionKey = ? and t.action = ? and t.actionType = ?",
        actionKey, 1, actionType);
    Long cancelAwardCount = super.findUnique(
        "select count(t.id) from AwardStatistics t where t.actionKey = ? and t.action = ? and t.actionType = ?",
        actionKey, 0, actionType);
    awardCount = awardCount == null ? 0l : awardCount;
    cancelAwardCount = cancelAwardCount == null ? 0l : cancelAwardCount;
    Long count = awardCount - cancelAwardCount;
    return count < 0 ? 0l : count;
  }

  public AwardStatistics findAwardStatistics(Long psnId, Long awardPsnId, Long actionKey, Integer actionType,
      Integer tempAction) throws Exception {
    String hql =
        "select t from AwardStatistics t where t.psnId=? and t.awardPsnId=? and t.actionKey=? and t.actionType=? and t.action=?";
    List<AwardStatistics> list = super.createQuery(hql, psnId, awardPsnId, actionKey, actionType, tempAction).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    } else {
      return null;
    }
  }
}
