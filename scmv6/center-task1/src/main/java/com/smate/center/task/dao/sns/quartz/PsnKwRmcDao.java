package com.smate.center.task.dao.sns.quartz;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PsnKwRmc;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PsnKwRmcDao extends SnsHibernateDao<PsnKwRmc, Long> {
  public void deleteByPsnId(Long psnId) {
    String sql = "delete from PSN_KW_RMC t where t.PSN_ID =?";
    super.update(sql, new Object[] {psnId});
  }

  public int getCount(Long psnId) {// rcmd库中的psn_kw_scmzt表数据是从sns中PSN_DISCIPLINE_KEY表同步过去的。
    StringBuffer str = new StringBuffer();
    str.append("select count(1) from psn_kw_rmc h where h.psn_id=?");
    str.append(" and exists (select 1 from DISC_KEYWORD_HOT dh where dh.kw_txt=h.keyword_txt )");
    str.append(
        "and not exists (SELECT 1 FROM RM_KW_DROP_HISTORY th WHERE th.psn_Id =h.psn_id and h.keyword_txt=th.kw_txt)");
    str.append(
        " and not exists (select 1 from PSN_DISCIPLINE_KEY tk where h.keyword_txt = lower(tk.key_words) and tk.psn_id =h.psn_id)");
    Long count = super.queryForLong(str.toString(), new Object[] {psnId});
    return Integer.parseInt(count.toString());
  }

  public int getZtCount(Long psnId) {
    String sql = "select count(1) from PSN_DISCIPLINE_KEY t where t.psn_Id=?";
    Long count = super.queryForLong(sql.toString(), new Object[] {psnId});
    return Integer.parseInt(count.toString());
  }

  public Long getCountByPsnIdKW(Long psnId, String keywordTxt) {
    String sql = "select count(1) from PsnKwRmc t where t.psnId=:psnId and t.keywordTxt=:keywordTxt";
    return (Long) super.createQuery(sql).setParameter("psnId", psnId).setParameter("keywordTxt", keywordTxt)
        .uniqueResult();

  }

  public List<BigDecimal> getHotKid(Long psnId, int maxSize) {
    StringBuffer sb = new StringBuffer();
    sb.append("select hot_kid from Disc_Keyword_Hot_Related t where exists(");
    sb.append(
        "select 1 from psn_kw_rmc_gid t1,psn_kw_rmc_group t2 where t1.kw_gid = t2.gid and t.kw_txt = t2.kw_txt and t1.psn_id=:psnId)");
    sb.append(
        " and not exists (select 1 from  DISC_KEYWORD_HOT dh, psn_kw_rmc kr where kr.psn_id=:psnId and dh.kw_txt=kr.keyword_txt and dh.id=hot_kid )");
    sb.append(" group by t.hot_kid  order by count(t.kw_txt) desc");

    return super.getSession().createSQLQuery(sb.toString()).setParameter("psnId", psnId).setParameter("psnId", psnId)
        .setMaxResults(maxSize).list();

  }

  /**
   * 根据人员ID获取个人关键词.
   * 
   * @param psnId
   * @return
   */
  public List<String> getKwByPsn(Long psnId) {
    String hql = "select keyword from PsnKwRmc t where t.psnId=? ";
    return super.find(hql, psnId);
  }

  /**
   * 获取推荐研究领域的人员任务列表.
   * 
   * @param startPsnId
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnIdList(Long startPsnId, Integer maxSize) {
    String hql = "select distinct t.psnId from PsnKwRmc t where t.psnId>? order by t.psnId ";
    return super.createQuery(hql, startPsnId).setMaxResults(maxSize).list();
  }

  /**
   * 研究领域推荐关键词数据
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnKwRmc> findRecommendKw(Long psnId) {
    String hql = "select new PsnKwRmc(t.id,t.psnId,t.keyword,t.keywordTxt,t.type) from PsnKwRmc t where t.psnId=:psnId"
        + " and exists (select 1 from DiscKeywordHot t1 where t.keywordTxt=t1.kwTxt)"
        + " and not exists(select 1 from PsnDisciplineKey t2 where t2.psnId=t.psnId and t2.status=1)"
        + " and not exists(select 1 from PsnInfoFillDiscRecmd t3 where t3.psnId=t.psnId and t3.status=2)"
        + " and not exists(select 1 from RecommandKwDropHistory t4 where t4.psnId=t.psnId)"
        + " order by t.score desc,t.id desc";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }
}
