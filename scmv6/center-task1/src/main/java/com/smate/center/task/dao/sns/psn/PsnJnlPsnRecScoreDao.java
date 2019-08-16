package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.psn.PsnJnlPsnRecScore;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 期刊推荐，给人员推荐期刊
 * 
 * @author lichangwen
 * 
 */
@Repository
public class PsnJnlPsnRecScoreDao extends SnsHibernateDao<PsnJnlPsnRecScore, Long> {

  /**
   * 质量：高于或等于用户发表档次+1，用户在期刊上发表过再+1<br/>
   * 相关度： ∑（个人特征关键词匹配个数+用户发表期刊次数+用户收藏期刊） 准确度取决于期刊研究领域［关键词］的信息收集 <br/>
   * 合作度：好友在期刊上发表过=1 <br/>
   * 推荐度 (1+相关度 +质量)＊Ln(2.72+合作度) [新算法］
   * 
   * @param psnId
   */
  public void updateScore(Long psnId) {
    String hql =
        "update PsnJnlPsnRecScore set score=((1+gradeInner+gradeMost+gradeHt+kwTf+htTf)*ln(2.72+frdTf)) where psnId=?";
    super.createQuery(hql, psnId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PsnJnlPsnRecScore> getPsnJnlRecommend(Long psnId) {
    String hql = "select new PsnJnlPsnRecScore(jnlId,score) from PsnJnlPsnRecScore where psnId=? order by score desc";
    return super.createQuery(hql, psnId).setMaxResults(100).list();
  }

  public void truncateJnlRefRecScore() {
    String sql = "truncate table PSN_JNL_PSNREC_SCORE";
    super.update(sql);
  }

  public List<String> getPsnKeyWordsByPsnId(Long psnId) {
    String hql = "select keywordTxt from PsnKwRmc where psnId=?";
    return super.find(hql, psnId);
  }

  public Double getPsnJnlScore(Long psnId, Long jnlId) {
    String hql = "select score from PsnJnlPsnRecScore where psnId=? and jnlId=?";
    return super.findUnique(hql, psnId, jnlId);
  }

  public PsnJnlPsnRecScore getPsnJnlPsnRecScore(Long psnId, Long jnlId) {
    String hql = "from PsnJnlPsnRecScore where psnId=? and jnlId=?";
    return super.findUnique(hql, psnId, jnlId);
  }

}
