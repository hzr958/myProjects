package com.smate.web.v8pub.dao.sns;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.v8pub.po.sns.AwardStatistics;

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
  public List findAwardPsn(Integer size, Integer type) {
    String hql =
        "select  count(p.id) as count,p.awardPsnId  as psnId from AwardStatistics p where trunc(p.createDate)>=trunc(sysdate-7)  and action=? and p.actionType = ? group by p.awardPsnId having count(p.awardPsnId)>=1";
    return super.createQuery(hql, LikeStatusEnum.LIKE, type)
        .setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).setMaxResults(100).setFirstResult(size * 100)
        .list();
  }

  /**
   * 分页查询赞人员
   * 
   * @param page
   * @param awardPsnId
   * @return @
   */
  public Page findAwardPersonPage(Page page, Long awardPsnId) {
    Long count = 0l;
    String countSql =
        "select count(1) from (select distinct t.psn_id,t.formate_date from AWARD_STATISTICS t where t.AWARD_PSN_ID = ? and t.action = 1)";
    count = super.queryForLong(countSql, new Object[] {awardPsnId});
    page.setTotalCount(count);

    String hql =
        "select new AwardStatistics(t.psnId,t.formateDate) from AwardStatistics t where t.awardPsnId = ? and t.action = ? group by t.psnId,t.formateDate order by max(t.createDate) desc";
    List result = super.createQuery(hql, awardPsnId, LikeStatusEnum.LIKE).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
    page.setResult(result);
    return page;
  }

  public Page findAwardPersonPage(Page page, Long awardPsnId, Integer actionType) {
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
   * @param formateDate @
   */
  public void delAwardRecord(Long psnId, Long awardPsnId, Long formateDate) {
    String hql = "delete from AwardStatistics t where t.psnId = ? and t.awardPsnId = ? and t.formateDate = ?";
    super.createQuery(hql, psnId, awardPsnId, formateDate).executeUpdate();
  }

  public List<AwardStatistics> findAwardRecord(Long psnId, Long awardPsnId, Long formateDate) {
    String hql =
        "from AwardStatistics t where t.psnId = ? and t.awardPsnId = ? and t.formateDate = ? and t.action = ? and t.actionType = 1";
    return super.createQuery(hql, psnId, awardPsnId, formateDate, LikeStatusEnum.LIKE).list();
  }

  /**
   * 计算一段时间内的赞人数
   * 
   * @param awardPsnId
   * @param formateDate
   * @return @
   */
  public Integer countInDate(Long awardPsnId, Long startDate, Long endDate) {
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
   * @return @
   */
  public List<String> findIpsRecordBefDay(Long awardPsnId, Long formateDate) {
    String hql = "select t.ip from AwardStatistics t where t.awardPsnId = ? and t.formateDate >= ? and t.action = ?";
    return super.createQuery(hql, awardPsnId, formateDate, LikeStatusEnum.LIKE).list();
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
        "select count(t.id) from AwardStatistics t where t.awardPsnId = ? and t.action = ? and t.actionType=?", psnId,
        LikeStatusEnum.LIKE, actionType);
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
        actionKey, LikeStatusEnum.LIKE, actionType);
    Long cancelAwardCount = super.findUnique(
        "select count(t.id) from AwardStatistics t where t.actionKey = ? and t.action = ? and t.actionType = ?",
        actionKey, LikeStatusEnum.UNLIKE, actionType);
    awardCount = awardCount == null ? 0l : awardCount;
    cancelAwardCount = cancelAwardCount == null ? 0l : cancelAwardCount;
    Long count = awardCount - cancelAwardCount;
    return count < 0 ? 0l : count;
  }

  @SuppressWarnings("unchecked")
  public AwardStatistics findAwardStatistics(Long psnId, Long awardPsnId, Long actionKey, Integer actionType,
      LikeStatusEnum tempAction) {
    String hql = "select t from AwardStatistics t " + "where t.psnId=:psnId " + "and t.awardPsnId=:awardPsnId "
        + "and t.actionKey=:actionKey " + "and t.actionType=:actionType " + "and t.action=:action";
    List<AwardStatistics> list = super.createQuery(hql).setParameter("psnId", psnId)
        .setParameter("awardPsnId", awardPsnId).setParameter("actionKey", actionKey)
        .setParameter("actionType", actionType).setParameter("action", tempAction).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    } else {
      return null;
    }
  }
}
