package com.smate.web.v8pub.dao.sns.psn;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.psn.RecommendDisciplineKey;

/**
 * 
 * @author liqinghua
 * 
 */
@Repository
public class RecommendDisciplineKeyDao extends SnsHibernateDao<RecommendDisciplineKey, Long> {

  /**
   * 查找人员关键词List
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<RecommendDisciplineKey> findAllDisciplineKeyByPsnId(Long psnId) {
    String hql =
        "select new RecommendDisciplineKey(id, keyWords, psnId) from RecommendDisciplineKey t where t.psnId=:psnId order by updateDate asc";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  @SuppressWarnings("unchecked")
  public Integer saveKeys(String keyWords, Long psnId) {
    keyWords = keyWords.replaceAll("&quot;", "\"");
    String hql = "from RecommendDisciplineKey t where t.psnId = :psnId and lower(t.keyWords) = :keyWords";
    List<RecommendDisciplineKey> pdkList = super.createQuery(hql).setParameter("psnId", psnId)
        .setParameter("keyWords", StringUtils.lowerCase(keyWords)).list();
    if (CollectionUtils.isEmpty(pdkList)) {
      RecommendDisciplineKey pdk = new RecommendDisciplineKey(psnId, keyWords, new Date());
      this.save(pdk);
      return 1;
    }
    return 0;
  }

  /**
   * 查找人员关键词List
   * 
   * @param psnId
   * @return
   */
  public List<RecommendDisciplineKey> findRecommendDisciplineKeyByPsnId(Long psnId) {
    String hql = "from RecommendDisciplineKey t where t.psnId=:psnId order by id desc";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 统计人员有效关键词数量
   * 
   * @param psnId
   * @return
   */
  public Long countRecommendDisciplineKey(Long psnId) {
    String hql = "select count(1) from RecommendDisciplineKey t where t.psnId=:psnId";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 删除记录
   * 
   * @param keyWords
   * @param psnId
   * @return
   */
  public void deleteKeys(String keyWords, Long psnId) {
    String hql = "delete RecommendDisciplineKey t where t.psnId = :psnId and lower(t.keyWords) = :keyWords";
    super.createQuery(hql).setParameter("psnId", psnId).setParameter("keyWords", StringUtils.lowerCase(keyWords))
        .executeUpdate();
  }

  /**
   * 删除个人所有的关键词
   * 
   * @param psnId
   */
  public void deletePsnAllKey(Long psnId) {
    String hql = "delete  RecommendDisciplineKey t where t.psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

}
