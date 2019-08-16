package com.smate.center.batch.dao.sns.psn.inforefresh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.psn.PsnRefPsnRecScore;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 给人员推荐基准文献
 * 
 * @author lichangwen
 * 
 */
@Repository
public class PsnRefPsnRecScoreDao extends SnsHibernateDao<PsnRefPsnRecScore, Long> {
  /**
   * 质量：在用户发表档内+1，高于等于用户发表论文最多档内+1，用户在期刊上发表过再+1 <br/>
   * 相关度： ∑（个人特征关键词个数+用户发表期刊次数） <br/>
   * 合作度：好友的论文=1 <br/>
   * 推荐度 (相关度 +质量)×Ln(2.72+合作度)[新算法］
   * 
   * @param psnId
   */
  public void updateScore(Long psnId) {
    String hql =
        "update PsnRefPsnRecScore set score=(((kwTf+htTf+gradeInner)+(gradeMost+gradeHt)) * ln(2.72+frdTf)) where psnId=?";
    super.createQuery(hql, psnId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PsnRefPsnRecScore> getDescRefList(Long psnId, int language, int size) {
    String hql = "from PsnRefPsnRecScore where psnId=? and language=? order by score desc";
    return super.createQuery(hql, psnId, language).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPsnRefRecommendByPuballId(Long psnId) {
    String hql = "select puballId from PsnRefPsnRecScore where psnId=? order by score desc";
    return super.createQuery(hql, psnId).setMaxResults(100).list();
  }

  public void truncatePsnRefPsnRecScore() {
    String sql = "truncate table PSN_REF_PSNREC_SCORE";
    super.update(sql);
  }

  public List<String> getPsnKeyWordsByPsnId(Long psnId) {
    String hql = "select keywordTxt from PsnKwRmc where psnId=?";
    return super.find(hql, psnId);
  }

  public Double getPsnRefPsnRecScore(Long psnId, Long puballId) {
    String hql = "select score from PsnRefPsnRecScore where psnId=? and puballId=?";
    return super.findUnique(hql, psnId, puballId);
  }

}
