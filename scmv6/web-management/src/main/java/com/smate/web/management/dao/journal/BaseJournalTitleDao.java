package com.smate.web.management.dao.journal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.string.JnlFormateUtils;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.journal.BaseJournalTitle;
import com.smate.web.management.model.journal.Journal;

/**
 * 
 */
@Repository
public class BaseJournalTitleDao extends PdwhHibernateDao<BaseJournalTitle, Long> {
  public List<BaseJournalTitle> findBaseJnlTitle(String titleXxAlias, String titleEnAlias, String pissn, String eissn) {
    List<Criterion> criterionList = new ArrayList<Criterion>();
    if (StringUtils.isNotBlank(titleXxAlias))
      criterionList.add(Restrictions.eq("titleXxAlias", titleXxAlias));
    if (StringUtils.isNotBlank(titleEnAlias))
      criterionList.add(Restrictions.eq("titleEnAlias", titleEnAlias));
    if (StringUtils.isNotBlank(pissn))
      criterionList.add(Restrictions.eq("pissn", pissn.trim()));
    if (StringUtils.isNotBlank(eissn))
      criterionList.add(Restrictions.eq("eissn", eissn.trim()));
    return find(criterionList.toArray(new Criterion[criterionList.size()]));
  }

  public List<BaseJournalTitle> jnlMatchBaseJnlTitile(Journal jnl) {
    String hql =
        "from BaseJournalTitle where pissn=? or titleXxAlias like ? or titleEnAlias like ? or titleXxAlias like ? or titleEnAlias like ? or titleAbbrAlias like ? ";
    String issn = StringUtils.isBlank(jnl.getIssn()) ? "" : jnl.getIssn();
    issn = issn.length() > 9 ? issn.substring(0, 9) : issn;
    String titleZhAlias = StringUtils.trimToEmpty(jnl.getZhNameAlias());
    String zhTitleAlias = StringUtils.isBlank(titleZhAlias) ? JnlFormateUtils.getLocalStr(jnl.getEnameAlias(), true)
        : JnlFormateUtils.getLocalStr(titleZhAlias, true);
    String enTitleAlias = StringUtils.isBlank(jnl.getEnameAlias()) ? JnlFormateUtils.getLocalStr(titleZhAlias, false)
        : JnlFormateUtils.getLocalStr(jnl.getEnameAlias(), false);
    zhTitleAlias = StringUtils.isNotBlank(zhTitleAlias) ? zhTitleAlias + "%" : zhTitleAlias;
    enTitleAlias = StringUtils.isNotBlank(enTitleAlias) ? enTitleAlias + "%" : enTitleAlias;
    String titleAbbrAlias = StringUtils.isNotBlank(enTitleAlias) ? enTitleAlias : zhTitleAlias;
    return super.find(hql, issn, zhTitleAlias, enTitleAlias, JnlFormateUtils.getSubStr(jnl.getZhName()),
        JnlFormateUtils.getSubStr(jnl.getEnName()), titleAbbrAlias);
  }

  /**
   * 任意title去BASE_JOURNAL_TITLE查重
   * 
   * @param zhTitleAlias
   * @param enTitleAlias
   * @return
   */
  public List<BaseJournalTitle> jnlMatchBaseJnlTitile(String zhTitleAlias, String enTitleAlias) {
    String hql = "from BaseJournalTitle where titleXxAlias=? or titleEnAlias=?";
    zhTitleAlias = StringUtils.isBlank(zhTitleAlias) ? "" : zhTitleAlias;
    enTitleAlias = StringUtils.isBlank(enTitleAlias) ? "" : enTitleAlias;
    return super.find(hql, zhTitleAlias, enTitleAlias);
  }

  /**
   * 使用处理好的中英文标题和pissn去查询那些符合任意条件的期刊title
   * 
   * @param titleXxAlias
   * @param titleEnAlias
   * @param pissn
   * @return
   * @throws DaoException
   */
  public List<BaseJournalTitle> findBaseJournalTitle(String titleXxAlias, String titleEnAlias, String pissn) {
    StringBuilder hql = new StringBuilder("from BaseJournalTitle where 1=1 ");
    List<Object> params = new ArrayList<>();
    if (StringUtils.isNotBlank(pissn) && StringUtils.isNotBlank(titleXxAlias) && StringUtils.isBlank(titleEnAlias)) {
      hql.append(" and pissn=? or titleXxAlias=?");
      params.add(pissn);
      params.add(titleXxAlias);
    } else if (StringUtils.isNotBlank(pissn) && StringUtils.isNotBlank(titleEnAlias)
        && StringUtils.isBlank(titleXxAlias)) {
      hql.append(" and pissn=? or titleEnAlias=?");
      params.add(pissn);
      params.add(titleEnAlias);
    } else if (StringUtils.isNotBlank(pissn) && StringUtils.isNotBlank(titleEnAlias)
        && StringUtils.isNotBlank(titleXxAlias)) {
      hql.append(" and pissn=? or (titleXxAlias=? or titleEnAlias=?)");
      params.add(pissn);
      params.add(titleXxAlias);
      params.add(titleEnAlias);
    }
    if (StringUtils.isBlank(pissn)) {
      if (StringUtils.isNotBlank(titleXxAlias) && StringUtils.isNotBlank(titleEnAlias)) {
        hql.append(" and titleXxAlias=? or titleEnAlias =?");
        params.add(titleXxAlias);
        params.add(titleEnAlias);
      } else if (StringUtils.isNotBlank(titleXxAlias) && StringUtils.isBlank(titleEnAlias)) {
        hql.append(" and titleXxAlias=?");
        params.add(titleXxAlias);
      } else if (StringUtils.isBlank(titleXxAlias) && StringUtils.isNotBlank(titleEnAlias)) {
        hql.append(" and titleEnAlias=?");
        params.add(titleEnAlias);
      }
    }
    hql.append(" order by jnlId");

    return super.find(hql.toString(), params.toArray());
  }

  /**
   * 根据jnl_id查询
   * 
   * @param jouId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<BaseJournalTitle> findByJnlId(Long jnlId) {
    String hql = "from BaseJournalTitle where jnlId = :jnlId";
    return super.createQuery(hql).setParameter("jnlId", jnlId).list();
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public List<Long> getJnlID(String pissn, String titleEn, String titleXx) {
    StringBuilder hql = new StringBuilder("select distinct jnlId  from BaseJournalTitle where pissn=?");
    List params = new ArrayList();
    params.add(pissn);
    if (StringUtils.isNotBlank(titleEn)) {
      String titleEnAlias = JnlFormateUtils.getStrAlias(titleEn);
      hql.append(" and titleEnAlias=?");
      params.add(titleEnAlias);
    }
    if (StringUtils.isNotBlank(titleXx)) {
      String titleXxAlias = JnlFormateUtils.getStrAlias(titleXx);
      hql.append(" and titleXxAlias=?");
      params.add(titleXxAlias);
    }
    return super.createQuery(hql.toString(), params.toArray()).list();
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public List<Long> findBaseJournalIds(String pissn, String titleXx) {
    String hql = "select distinct jnlId  from BaseJournalTitle where ";
    List params = new ArrayList();
    if (StringUtils.isNotBlank(pissn)) {
      hql += " pissn=? ";
      params.add(pissn);
    }
    if (StringUtils.isNotBlank(titleXx)) {
      String titleXxAlias = JnlFormateUtils.getStrAlias(titleXx);
      if (hql.indexOf("=?") > 0)
        hql += " and ";
      hql += " upper(titleEnAlias)=? or upper(titleXxAlias)=?";
      params.add(titleXxAlias.toUpperCase());
      params.add(titleXxAlias.toUpperCase());
    }
    return super.createQuery(hql, params.toArray()).list();
  }

  public void updateTitleStatus(Long titleId, Integer status) {
    super.createQuery("update BaseJournalTitle set titleStatus=? where jouTitleId=?", status, titleId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<BaseJournalTitle> findBaseJournalTitleToByJnlId(Long jnlId, String pissn, String titleEn, String titleXx,
      Long dbId) {
    StringBuilder hql = new StringBuilder("from BaseJournalTitle where 1=1 ");
    List<Object> params = new ArrayList<>();
    if (StringUtils.isNotBlank(pissn)) {
      hql.append(" and pissn =?");
      params.add(pissn);
    }
    String titleEnAlias = JnlFormateUtils.getStrAlias(titleEn);
    String titleXxAlias = JnlFormateUtils.getStrAlias(titleXx);
    if (StringUtils.isNotBlank(titleEnAlias)) {
      hql.append(" and titleEnAlias = ?");
      params.add(titleEnAlias);
    }
    if (StringUtils.isNotBlank(titleXxAlias)) {
      hql.append(" and titleXxAlias =?");
      params.add(titleXxAlias);
    }
    hql.append(" and jnlId=?");
    params.add(jnlId);
    if (dbId != null && dbId == 2) {
      hql.append(" and dbId in(2,15,16,17,18,19)");
    } else {
      hql.append(" and dbId=?");
      params.add(dbId);
    }
    return super.createQuery(hql.toString(), params.toArray()).list();
  }

  /**
   * 根据jnlId删除
   * 
   * @param jnlId
   */
  public void deleteByJnlId(Long jnlId) {
    String hql = "delete from BaseJournalTitle t where t.jnlId=?";
    super.createQuery(hql, jnlId).executeUpdate();
  }

}
