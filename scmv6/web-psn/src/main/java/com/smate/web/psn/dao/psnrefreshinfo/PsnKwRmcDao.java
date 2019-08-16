package com.smate.web.psn.dao.psnrefreshinfo;



import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.psnrefreshinfo.PsnKwRmc;



/**
 * @author zk
 */
@Repository
public class PsnKwRmcDao extends SnsHibernateDao<PsnKwRmc, Long> implements Serializable {


  public boolean isExists(Long psnId, String keywordTxt) {
    StringBuilder hql = new StringBuilder("select count(t.id) FROM PsnKwRmc t WHERE t.psnId = ? and t.keywordTxt=?");
    Long count = super.findUnique(hql.toString(), psnId, keywordTxt);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 查找人员推荐关键词
   * 
   * @param psnId
   * @param size
   * @return
   */
  public List<PsnKwRmc> findPsnRecommendKeywords(Long psnId) {
    StringBuilder hql =
        new StringBuilder("SELECT new PsnKwRmc(id,psnId,keyword,keywordTxt,type) FROM PsnKwRmc t WHERE t.psnId=:psnId");
    hql.append(" AND t.keywordTxt in (SELECT t1.kwTxt FROM DiscKeywordHot t1 where t.keywordTxt=t1.kwTxt) ");
    hql.append(
        " AND t.keywordTxt not in(SELECT lower(t_.keyWords) FROM PsnDisciplineKey t_ WHERE t_.psnId = :psnId and t_.status=1)");
    hql.append(
        " AND t.keywordTxt not in(SELECT lower(t_1.discName) FROM PsnInfoFillDiscRecmd t_1 WHERE t_1.psnId = :psnId and t_1.status=2)");
    hql.append(" AND t.keywordTxt not in(SELECT t_.kwTxt FROM RecommandKwDropHistory t_ WHERE t_.psnId = :psnId)");
    hql.append("ORDER BY t.score desc,t.id desc ");
    return super.createQuery(hql.toString()).setParameter("psnId", psnId).setParameter("psnId", psnId)
        .setParameter("psnId", psnId).setParameter("psnId", psnId).list();
  }


}
