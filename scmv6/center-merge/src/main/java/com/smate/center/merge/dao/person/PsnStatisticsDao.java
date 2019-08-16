package com.smate.center.merge.dao.person;

import org.springframework.stereotype.Repository;

import com.smate.center.merge.model.sns.person.PsnStatistics;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * PsnStatisticsDao.
 * 
 * @author tsz
 *
 * @date 2018年9月11日
 */
@Repository
public class PsnStatisticsDao extends SnsHibernateDao<PsnStatistics, Long> {
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
  public int getPrjSum(Long ownerPsnId) {
    String sql = "select count(t.project_id) from project t where t.status!=1 and t.owner_psn_id=" + ownerPsnId;
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
    String sql = "select count(t.project_id) from project t where t.status!=1 and t.owner_psn_id= " + ownerPsnId
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
    String sql =
        "select  sum(t.count) from VIST_STATISTICS t where t.vist_psn_id=" + ownerPsnId + " and t.action_type=6";
    int count = super.queryForInt(sql);
    return count;
  }
}
