package com.smate.web.management.dao.analysis.sns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.analysis.sns.CooperatorMayRecommend;
import com.smate.web.management.model.analysis.sns.RecommendScore;


/**
 * 基金、论文推荐合作者：可能合作者Dao.
 * 
 * @author zhuangyanming
 * 
 */

@Repository
public class CooperatorMayRecommendDao extends SnsHibernateDao<CooperatorMayRecommend, Long> {
  // 最大查询数量
  private final Integer MAX_RESULT = 30;

  /**
   * 查找可能合作者.
   * 
   * @param psnId
   * @param coPsnId
   * @return
   * @throws DaoException
   */
  public CooperatorMayRecommend findRecommend(Long psnId, Long coPsnId) throws DaoException {
    String hql = "from CooperatorMayRecommend t where t.psnId=? and t.coPsnId=?";
    return super.findUnique(hql, psnId, coPsnId);
  }

  /**
   * 当前合作者最大版本号
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public Long findMaxVersion(Long psnId) throws DaoException {
    String hql = "select max(t.coVersion) from CooperatorMayRecommend t where t.psnId=?";
    return super.findUnique(hql, psnId);
  }

  // 删除过时的合作者版本
  public void delPassCoVersion(Long psnId) throws DaoException {

    String hql = "select max(t.coVersion) from CooperatorMayRecommend t where t.psnId=?";
    Long max = super.findUnique(hql, psnId);
    if (max != null) {
      max = Math.max(0, max);
      // 更新新增数据版本
      super.batchExecute("update CooperatorMayRecommend t set t.coVersion=? where t.psnId=? and t.coVersion=-1", max,
          psnId);
      // 删除旧版本
      super.batchExecute("delete CooperatorMayRecommend t where t.psnId=? and t.coVersion<>?", psnId, max);
    }

  }

  // 删除关键词临时记录
  public void delTmpCoKwRecord(Long psnId) throws DaoException {

    String hql = "delete from CooperatorMayRecommend t where t.psnId=? and t.tmpCoKw>0";
    super.batchExecute(hql, psnId);

  }

  // 删除合作者数据
  public void delCoRecord(Long psnId) throws DaoException {
    String hql = "delete from CooperatorMayRecommend t where t.psnId=?";
    super.batchExecute(hql, psnId);
  }

  // 删除分数值低的数据
  @SuppressWarnings("unchecked")
  public void delLowScoreRecord(Long psnId) throws DaoException {
    // 可能合作者(不包括关键词分数)
    String hql =
        "select t.coPsnId from CooperatorMayRecommend t where t.psnId=? and t.tmpCoKw=0 order by t.coTotal desc";
    List<Long> list = super.createQuery(hql, psnId).setMaxResults(MAX_RESULT).list();

    if (list != null && list.size() > 0) {
      String delHql = "delete from CooperatorMayRecommend t where t.psnId=? and t.coPsnId not in(:coPsnIds)";

      super.createQuery(delHql, psnId).setParameterList("coPsnIds", list).executeUpdate();
    }
  }

  /**
   * 可能合作者列表.
   * 
   * @param psnId
   * @param coPsnIds
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<CooperatorMayRecommend> findRecommendList(Long psnId, Set<Long> coPsnIds) throws DaoException {
    if (psnId == null || coPsnIds == null || coPsnIds.size() == 0) {
      return null;
    }
    String hql = "from CooperatorMayRecommend t where t.psnId=? and t.coPsnId in(:coPsnIds)";
    return super.createQuery(hql, psnId).setParameterList("coPsnIds", coPsnIds).list();
  }

  /**
   * 可能合作者合并查询(关键词专用).
   * 
   * @param psnId
   * @param gids
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Map<Long, RecommendScore> findKwRecommendScoreList(Long psnId, List<Long> gids, Set<Long> coPsnIds,
      Integer minKw, Integer firstResult, Integer maxResult) throws DaoException {

    List<RecommendScore> list = null;
    if (gids != null && gids.size() > 0) {
      if (coPsnIds == null || coPsnIds.size() == 0) {
        String hql =
            "select new com.smate.web.management.model.analysis.sns.RecommendScore(t.psnId,count(t.psnId)) from PsnKwRmcGid t where t.psnId<>?"
                + " and t.gid in(:gids) and exists (select 1 from PsnAreaClassify t2 where t2.psnId=t.psnId)"
                + " and exists(select 1 from Person t3 where t3.personId=t.psnId) and not exists(select 1 from PsnPrivate pp where pp.psnId=t.psnId)"
                + " group by t.psnId having count(t.psnId)>" + minKw + " order by count(t.psnId) desc";

        list = super.createQuery(hql, psnId).setParameterList("gids", gids).setFirstResult(firstResult)
            .setMaxResults(maxResult).list();
      } else {
        // 可能合作者(关键词分数)
        String hql =
            "select new com.smate.web.management.model.analysis.sns.RecommendScore(t.psnId,count(t.psnId)) from PsnKwRmcGid t where t.psnId<>?"
                + " and t.gid in(:gids) and t.psnId not in(:coPsnIds) and exists (select 1 from PsnAreaClassify t2 where t2.psnId=t.psnId)"
                + " and exists(select 1 from Person t3 where t3.personId=t.psnId) and not exists(select 1 from PsnPrivate pp where pp.psnId=t.psnId)"
                + " group by t.psnId having count(t.psnId)>" + minKw + " order by count(t.psnId) desc";

        list = super.createQuery(hql, psnId).setParameterList("gids", gids).setParameterList("coPsnIds", coPsnIds)
            .setFirstResult(firstResult).setMaxResults(maxResult).list();
      }
    }
    // 转为Map结构，方便其它内容查找合并
    Map<Long, RecommendScore> rsMap = new HashMap<Long, RecommendScore>();
    if (list != null) {
      for (RecommendScore rs : list) {
        rsMap.put(rs.getCoPsnId(), rs);
      }
    }
    return rsMap;
  }

  /**
   * 可能合作者合并查询(预先计算的人员).
   * 
   * @param psnId
   * @param gids
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Map<Long, RecommendScore> findRecommendScoreList(Long psnId, List<Long> gids) throws DaoException {
    // 可能合作者(不包括关键词分数)
    String hqlA =
        "select new com.smate.web.management.model.analysis.sns.RecommendScore(t.coPsnId,0L) from CooperatorMayRecommend t where t.psnId=? and t.tmpCoKw=0 order by t.coTotal desc";
    List<RecommendScore> partAList = super.createQuery(hqlA, psnId).setMaxResults(MAX_RESULT).list();
    if (partAList == null) {// 必要条件不成立，直接返回null
      return null;
    }

    List<RecommendScore> partBList = null;
    if (gids != null && gids.size() > 0) {
      // 可能合作者(关键词分数)
      String hqlB =
          "select new com.smate.web.management.model.analysis.sns.RecommendScore(t.psnId,count(t.psnId)) from PsnKwRmcGid t where t.psnId<>?"
              + " and exists(select 1 from CooperatorMayRecommend t2 where t2.psnId=? and t2.coPsnId=t.psnId and t2.tmpCoKw=0) and t.gid in(:gids)"
              + " group by t.psnId having count(t.psnId)>0 order by count(t.psnId) desc";

      partBList = super.createQuery(hqlB, psnId, psnId).setParameterList("gids", gids).setMaxResults(MAX_RESULT).list();

    }

    List<RecommendScore> rsList = null;
    if (partBList != null) {// List A和List B两列表合并，List
      // B(关键词分数)列表拥有更高优先级，有关键词计分
      for (RecommendScore rs : partAList) {
        if (partBList.contains(rs)) {
          continue;
        }
        partBList.add(rs);// List A加到List B
      }
      rsList = partBList;
    } else {
      rsList = partAList;
    }

    // 转为Map结构，方便其它内容查找合并
    Map<Long, RecommendScore> rsMap = new HashMap<Long, RecommendScore>();
    if (rsList != null) {
      for (RecommendScore rs : rsList) {
        rsMap.put(rs.getCoPsnId(), rs);
      }
    }

    return rsMap;
  }

  /**
   * 可能合作者合并查询(实时计算的人员).
   * 
   * @param psnId
   * @param gids
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Map<Long, RecommendScore> findKwRecommendScoreList(Long psnId) throws DaoException {
    // 可能合作者(不包括关键词分数)
    String hqlA =
        "select new com.smate.web.management.model.analysis.sns.RecommendScore(t.coPsnId,0L) from CooperatorMayRecommend t where t.psnId=? and t.tmpCoKw>0 order by t.coTotal desc";
    List<RecommendScore> partAList = super.createQuery(hqlA, psnId).setMaxResults(MAX_RESULT).list();
    if (partAList == null) {// 必要条件不成立，直接返回null
      return null;
    }

    // 转为Map结构，方便其它内容查找合并
    Map<Long, RecommendScore> rsMap = new HashMap<Long, RecommendScore>();
    if (partAList != null) {
      for (RecommendScore rs : partAList) {
        rsMap.put(rs.getCoPsnId(), rs);
      }
    }

    return rsMap;
  }

}
