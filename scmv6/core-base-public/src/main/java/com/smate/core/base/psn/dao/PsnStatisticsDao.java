package com.smate.core.base.psn.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员信息统计Dao.
 *
 * @author zk
 *
 */
@Repository
public class PsnStatisticsDao extends SnsHibernateDao<PsnStatistics, Long> {
  /**
   * 根据psnId获取项目数和成果数
   *
   * @param psnId
   * @return
   */
  public PsnStatistics getPubAndPrjNum(Long psnId) {
    String hql =
        "select new PsnStatistics(t.pubSum,t.prjSum,t.openPubSum,t.openPrjSum) from PsnStatistics t where t.psnId=:psnId";
    return (PsnStatistics) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 根据psnId获取项目数
   *
   * @param psnIdList
   * @return
   */
  public Long getPrjNum(Long psnId) {
    String hql = "select t.prjSumfrom PsnStatistics t where t.psnId=:psnId";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PsnStatistics> getHidexByPsnIds(List<Long> psnIdList) {
    String hql = "select new PsnStatistics(p.psnId,p.hindex) from PsnStatistics p where p.psnId in (:psnIdList)";
    return super.createQuery(hql).setParameterList("psnIdList", psnIdList).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> findUnStaticsPsnID() {
    String hql =
        "select t.personId from Person t where not exists(select 1 from PsnStatistics t2 where t2.psnId=t.personId) order by t.personId";
    return super.createQuery(hql).setMaxResults(10).list();
  }

  public Long countUnStaticsPsnID() {
    String hql =
        "select count(t.personId) from Person t where not exists(select 1 from PsnStatistics t2 where t2.psnId=t.personId)";
    return (Long) findUnique(hql);
  }

  /**
   * 将查询的列表封装成Map<便于用psnId查询>
   *
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<Long, PsnStatistics> getPsnStatisticsListMap(List<Long> psnIds) {
    Map<Long, PsnStatistics> psMap = new HashMap<Long, PsnStatistics>();
    String hql = "from PsnStatistics p where p.psnId in (:psnIds)";
    List<PsnStatistics> psList = super.createQuery(hql).setParameterList("psnIds", psnIds).list();
    if (CollectionUtils.isNotEmpty(psList)) {
      for (PsnStatistics ps : psList) {
        psMap.put(ps.getPsnId(), ps);
      }
    }
    return psMap;
  }

  /**
   * 设置人员待认领成果数.
   *
   * @param psnId
   * @param pubNum
   */
  public void setPsnPendingConfirmPubNum(Long psnId, Integer pubNum) {}

  public PsnStatistics getPsnStatistics(Long psnId) {
    String hql =
        "select new PsnStatistics(t.pubSum, t.prjSum,t.patentSum) from PsnStatistics t  where t.psnId = :psnId ";
    return (PsnStatistics) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 更新项目数量
   */
  public void updatePrjNum(Integer prjNum, Long psnId) {
    String hql = "update PsnStatistics t set t.prjSum =:prjNum where t.psnId =:psnId";
    super.createQuery(hql).setParameter("prjNum", prjNum).setParameter("psnId", psnId).executeUpdate();
  }

  public PsnStatistics getPubAndPantentSumById(Long psnId) {
    String hql = "select new PsnStatistics(t.patentSum, t.pubSum) from PsnStatistics t where t.psnId = :psnId";
    return (PsnStatistics) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  public PsnStatistics getPubAndPrjSumById(Long psnId) {
    String hql =
        "select new PsnStatistics(t.pubSum,t.prjSum, t.openPubSum) from PsnStatistics t where t.psnId = :psnId";
    return (PsnStatistics) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  public int getPatentNumByPsnId(Long personId) {
    String hql = "select t.patentSum from PsnStatistics t where t.psnId=:psnId";
    return (int) super.createQuery(hql).setParameter("psnId", personId).uniqueResult();
  }

  /**
   * 获取统计信息在影响力页面显示
   * 
   * @param psnId
   * @return PsnStatistics对象，含hindex、citedSum
   */
  public PsnStatistics getPsnStatisticsForInfluence(Long psnId) {
    String hql =
        "select new PsnStatistics(t.psnId, t.citedSum, t.hindex, t.visitSum, t.frdSum,t.pubAwardSum) from PsnStatistics t where t.psnId = :psnId";
    return (PsnStatistics) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 查找人员hindex在好友中的排名
   * 
   * @param psnId
   * @param hindex
   * @return
   */
  public Long findPsnHindexRanking(Long psnId, Integer hindex) {
    String hql =
        "select count(1) from PsnStatistics pst, Friend pf where pst.psnId = pf.friendPsnId and pst.hindex > :hindex and pf.psnId = :psnId";
    return (Long) super.createQuery(hql).setParameter("hindex", hindex).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 查找人员访问记录数在好友中排名
   * 
   * @param psnId
   * @param visitSum
   * @return
   */
  public Long findPsnVisitSumRanking(Long psnId, Integer visitSum) {
    String hql =
        "select count(1) from PsnStatistics pst, Friend pf where pst.psnId = pf.friendPsnId and pst.visitSum > :visitSum and pf.psnId = :psnId";
    return (Long) super.createQuery(hql).setParameter("visitSum", visitSum).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 查找引用数在好友中排名
   * 
   * @param psnId
   * @param citeSum
   * @return
   */
  public Long findCiteSumRank(Long psnId, Integer citeSum) {
    String hql =
        "select count(1) from PsnStatistics pst, Friend pf where pst.psnId = pf.friendPsnId and pst.citedSum > :citeSum and pf.psnId = :psnId";
    return (Long) super.createQuery(hql).setParameter("citeSum", citeSum).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 获取成果总数.
   * 
   * @param ownerPsnId not null
   * @return
   */
  public int getPubSum(Long ownerPsnId) {
    String sql = "select count(t.pub_id) from v_psn_pub t where t.status=0 and t.owner_psn_id=" + ownerPsnId;
    int count = super.queryForInt(sql);
    return count;
  }

  /**
   * 获取待认领成果总数.
   * 
   * @param ownerPsnId not null
   * @return
   */
  public int getPubAssignSum(Long ownerPsnId) {
    String sql =
        "select count(1) from pub_assign_log t where t.psn_id=" + ownerPsnId + " and t.confirm_result=0 and t.status=0";
    int count = super.queryForInt(sql);
    return count;
  }

  /**
   * 获取专利总数.
   * 
   * @param ownerPsnId not null
   * @return
   */
  public int getPatentSum(Long ownerPsnId) {
    String sql = "select count(t.pub_id) from v_pub_sns t where   t.pub_type=5 "
        + "and  exists(select 1 from v_psn_pub t1 where t1.pub_id=t.pub_id " + "and t1.owner_psn_id=" + ownerPsnId
        + " and t1.status=0)";
    int count = super.queryForInt(sql);
    return count;
  }

  /**
   * 获取全文总数.
   * 
   * @param ownerPsnId not null
   * @return
   */
  public int getPubFulltextSum(Long ownerPsnId) {
    String sql = "select count(1) from v_pub_fulltext t where exists"
        + "(select 1 from v_psn_pub t1 where t1.pub_id=t.pub_id and t1.owner_psn_id=" + ownerPsnId
        + " and t1.status=0 )";
    int count = super.queryForInt(sql);
    return count;
  }

  /**
   * 获取公开成果总数.
   * 
   * @param ownerPsnId not null
   * @return
   */
  public int getOpenPubSum(Long ownerPsnId) {
    String sql = "select count(t.pub_id) from v_pub_sns t where  "
        + "exists(select 1 from v_psn_pub t1 where t1.pub_id=t.pub_id and t1.owner_psn_id=" + ownerPsnId
        + " and t1.status=0) "
        + "and exists(select 1 from psn_config_pub t2 where t2.pub_id=t.pub_id and t2.any_user=7)";
    int count = super.queryForInt(sql);
    return count;
  }

  /**
   * 获取成果被赞总数.
   * 
   * @param ownerPsnId not null
   * @return
   */
  public int getPubLikeSum(Long ownerPsnId) {
    String sql = "select count(t.pub_id) from v_pub_like t where t.status=1 and exists"
        + "(select 1 from v_psn_pub t1 where t1.pub_id=t.pub_id and t1.owner_psn_id=" + ownerPsnId
        + " and t1.status=0 )";
    int count = super.queryForInt(sql);
    return count;
  }

  /**
   * 获取成果引用总数.
   * 
   * @param ownerPsnId not null
   * @return
   */
  public int getPubCitedSum(Long ownerPsnId) {
    String sql = "select sum(t.citations) from v_pub_sns t where exists"
        + "(select t1.pub_id from v_psn_pub t1 where t1.pub_id=t.pub_id and t1.owner_psn_id=" + ownerPsnId
        + " and t1.status=0 )";
    int count = super.queryForInt(sql);
    return count;
  }

  /**
   * 获取项目总数.
   * 
   * @param ownerPsnId not null
   * @return
   */
  public int getPrjSum(Long ownerPsnId) {// status为5的现在不用了
    String sql = "select count(t.project_id) from project t where t.status=0 and t.owner_psn_id=" + ownerPsnId;
    int count = super.queryForInt(sql);
    return count;
  }

  /**
   * 获取公开项目总数.
   * 
   * @param ownerPsnId not null
   * @return
   */
  public int getOpenPrjSum(Long ownerPsnId) {
    String sql = "select count(t.project_id) from project t where t.status=0 and t.owner_psn_id= " + ownerPsnId
        + " and exists(select 1 from psn_config_prj t2 where t2.prj_id=t.project_id and t2.any_user=7)";
    int count = super.queryForInt(sql);
    return count;
  }

  /**
   * 获取好友总数.
   * 
   * @param psnId not null
   * @return
   */
  public int getFriendSum(Long psnId) {
    String sql = "select count(t.id) from psn_friend t where t.psn_id=" + psnId;
    int count = super.queryForInt(sql);
    return count;
  }

  /**
   * 获取群组总数.
   * 
   * @param ownerPsnId not null
   * @return
   */
  public int getGrpSum(Long ownerPsnId) {
    String sql = "select count(1) from v_grp_baseinfo t where t.owner_psn_id= " + ownerPsnId + " and t.status='01'";
    int count = super.queryForInt(sql);
    return count;
  }

  /**
   * 获取访问次数总数.
   * 
   * @param ownerPsnId not null
   * @return
   */
  public int getPsnVistSum(Long ownerPsnId) {
    String sql = "select  sum(t.count) from VIST_STATISTICS t where t.vist_psn_id=" + ownerPsnId;
    int count = super.queryForInt(sql);
    return count;
  }
}
