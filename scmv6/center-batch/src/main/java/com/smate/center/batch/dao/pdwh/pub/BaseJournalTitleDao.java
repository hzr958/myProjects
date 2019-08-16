package com.smate.center.batch.dao.pdwh.pub;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.pdwh.pub.BaseJournalTitleTo;
import com.smate.center.batch.model.pdwh.pub.Journal;
import com.smate.core.base.utils.string.JnlFormateUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
@Repository
public class BaseJournalTitleDao extends HibernateTemplateDao<BaseJournalTitleTo, Long> {

  public List<BaseJournalTitleTo> findBaseJournalTitle(BaseJournalTitleTo journalTitle) throws DaoException {
    String titleXxAlias = StringUtils.trimToEmpty(journalTitle.getTitleXxAlias());
    String titleEnAlias = StringUtils.trimToEmpty(journalTitle.getTitleEnAlias());
    DetachedCriteria criteria = DetachedCriteria.forClass(journalTitle.getClass());
    if (StringUtils.isNotBlank(journalTitle.getPissn()) && StringUtils.isNotBlank(journalTitle.getTitleXx())
        && StringUtils.isBlank(journalTitle.getTitleEn())) {
      criteria.add(Restrictions.or(Restrictions.eq("pissn", journalTitle.getPissn().trim()),
          Restrictions.eq("titleXxAlias", titleXxAlias)));
    } else if (StringUtils.isNotBlank(journalTitle.getPissn()) && StringUtils.isNotBlank(journalTitle.getTitleEn())
        && StringUtils.isBlank(journalTitle.getTitleXx())) {
      criteria.add(Restrictions.or(Restrictions.eq("pissn", journalTitle.getPissn().trim()),
          Restrictions.eq("titleEnAlias", titleEnAlias)));
    } else if (StringUtils.isNotBlank(journalTitle.getPissn()) && StringUtils.isNotBlank(journalTitle.getTitleEn())
        && StringUtils.isNotBlank(journalTitle.getTitleXx())) {
      criteria.add(Restrictions.or(Restrictions.eq("pissn", journalTitle.getPissn().trim()), Restrictions
          .or(Restrictions.eq("titleXxAlias", titleXxAlias), Restrictions.eq("titleEnAlias", titleEnAlias))));
    }
    if (StringUtils.isBlank(journalTitle.getPissn())) {
      if (StringUtils.isNotBlank(titleXxAlias) && StringUtils.isNotBlank(titleEnAlias)) {
        criteria.add(Restrictions.or(Restrictions.eq("titleXxAlias", titleXxAlias),
            Restrictions.eq("titleEnAlias", titleEnAlias)));
      } else if (StringUtils.isNotBlank(titleXxAlias) && StringUtils.isBlank(titleEnAlias)) {
        criteria.add(Restrictions.eq("titleXxAlias", titleXxAlias));
      } else if (StringUtils.isBlank(titleXxAlias) && StringUtils.isNotBlank(titleEnAlias)) {
        criteria.add(Restrictions.eq("titleEnAlias", titleEnAlias));
      }
    }
    criteria.addOrder(Order.asc("jnlId"));
    return super.find(criteria);
  }

  public List<BaseJournalTitleTo> findBaseJnlTitle(String titleXxAlias, String titleEnAlias, String pissn,
      String eissn) {
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

  /**
   * 根据pissn，titleEn，dbId得到journalId.
   * 
   * @param pissn
   * @param titleEn
   * @param dbId
   * @return
   */
  @SuppressWarnings({"rawtypes"})
  public List findBaseJournalTitleToByJnlId(Long jnlId, String pissn, String titleEn, String titleXx, Long dbId) {
    DetachedCriteria criteria = DetachedCriteria.forClass(BaseJournalTitleTo.class);
    if (StringUtils.isNotBlank(pissn)) {
      criteria.add(Restrictions.eq("pissn", pissn));
    }
    String titleEnAlias = JnlFormateUtils.getStrAlias(titleEn);
    String titleXxAlias = JnlFormateUtils.getStrAlias(titleXx);
    if (StringUtils.isNotBlank(titleEnAlias)) {
      criteria.add(Restrictions.eq("titleEnAlias", titleEnAlias));
    }
    if (StringUtils.isNotBlank(titleXxAlias)) {
      criteria.add(Restrictions.eq("titleXxAlias", titleXxAlias));
    }
    criteria.add(Restrictions.eq("jnlId", jnlId));
    if (dbId == 2) {
      criteria.add(Restrictions.in("dbId", new Object[] {2L, 15L, 16L, 17L, 18L, 19L}));
    } else {
      criteria.add(Restrictions.eq("dbId", dbId));
    }
    return this.hibernateTemplate.findByCriteria(criteria);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public List getJID(String pissn, String titleEn, String titleXx) {
    String hql = "select distinct jnlId  from BaseJournalTitleTo where pissn=? ";
    List params = new ArrayList();
    params.add(pissn);
    if (StringUtils.isNotBlank(titleEn)) {
      String titleEnAlias = JnlFormateUtils.getStrAlias(titleEn);
      hql += " and titleEnAlias=?";
      params.add(titleEnAlias);
    }
    if (StringUtils.isNotBlank(titleXx)) {
      String titleXxAlias = JnlFormateUtils.getStrAlias(titleXx);
      hql += " and titleXxAlias=?";
      params.add(titleXxAlias);
    }
    return super.createQuery(hql, params.toArray()).list();
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public List<Long> findBjid(String pissn, String titleXx) {
    String hql = "select distinct jnlId  from BaseJournalTitleTo where ";
    List params = new ArrayList();
    if (StringUtils.isNotBlank(pissn)) {
      hql += " pissn=? ";
      params.add(pissn);
    }
    if (StringUtils.isNotBlank(titleXx)) {
      String titleXxAlias = JnlFormateUtils.getStrAlias(titleXx);
      if (hql.indexOf("=?") > 0)
        hql += " and ";
      hql += " titleEnAlias=? or titleXxAlias=?";
      params.add(titleXxAlias);
      params.add(titleXxAlias);
    }
    return super.createQuery(hql, params.toArray()).list();
  }

  public void updateTitleStatus(Long titleId, Integer status) {
    super.createQuery("update BaseJournalTitleTo set titleStatus=? where jouTitleId=?", status, titleId)
        .executeUpdate();
  }

  public List<BaseJournalTitleTo> findRepeatJournalTitle(BaseJournalTitleTo journalTitle) throws DaoException {
    DetachedCriteria criteria = DetachedCriteria.forClass(BaseJournalTitleTo.class);
    if (journalTitle.getDbId() != null) {
      if (journalTitle.getDbId().intValue() == 2) {
        criteria.add(Restrictions.in("dbId", new Object[] {2L, 15L, 16L, 17L, 18L, 19L}));
      } else {
        criteria.add(Restrictions.eq("dbId", journalTitle.getDbId()));
      }
    }
    if (StringUtils.isNotBlank(journalTitle.getPissn())) {
      criteria.add(Restrictions.eq("pissn", journalTitle.getPissn()));
    }
    if (StringUtils.isNotBlank(journalTitle.getTitleEnAlias())) {
      criteria.add(Restrictions.eq("titleEnAlias", journalTitle.getTitleEnAlias()));
    }
    if (StringUtils.isNotBlank(journalTitle.getTitleXxAlias())) {
      criteria.add(Restrictions.eq("titleXxAlias", journalTitle.getTitleXxAlias()));
    }
    if (journalTitle.getJnlId() != null) {
      criteria.add(Restrictions.eq("jnlId", journalTitle.getJnlId()));
    }
    return super.find(criteria);
  }

  @Override
  public void save(BaseJournalTitleTo entity) {
    try {
      if (CollectionUtils.isEmpty(this.findRepeatJournalTitle(entity)))
        super.save(entity);
      else
        super.getSession().update(entity);
    } catch (DaoException e) {
      logger.error("========保存出错", entity.toString());
    }
  }

  // 导入录入新增期刊时匹配 根据fzq要求，sns期刊与基础期刊匹配时，修改为标题or，加issn号 2013/03/01
  public List<BaseJournalTitleTo> snsJnlMatchBaseJnlId(String jname, String issn) {
    String nameAlias = JnlFormateUtils.getStrAlias(jname);
    String hql = "from BaseJournalTitleTo where (titleXxAlias=? or titleEnAlias=? or titleAbbrAlias=?) and pissn=?";
    return find(hql, nameAlias, nameAlias, nameAlias, issn);
  }

  @SuppressWarnings("rawtypes")
  public Long PdwhPubAllJournalMatchBaseJnlIdByNameOrIssn(String jname, String issn) {
    String nameAlias = JnlFormateUtils.getStrAlias(jname);
    String hql =
        "select jnlId from BaseJournalTitleTo where (titleXxAlias=? or titleEnAlias=? or titleAbbrAlias=?) or pissn=?";
    List list = super.createQuery(hql, nameAlias, nameAlias, nameAlias, issn).setMaxResults(1).list();
    return CollectionUtils.isEmpty(list) ? null : (Long) list.get(0);
  }

  @SuppressWarnings("rawtypes")
  public Long PdwhPubAllJournalMatchBaseJnlId(String jname, String issn) {
    String nameAlias = JnlFormateUtils.getStrAlias(jname);
    String hql =
        "select jnlId from BaseJournalTitleTo where (titleXxAlias=? or titleEnAlias=? or titleAbbrAlias=?) and pissn=?";
    List list = super.createQuery(hql, nameAlias, nameAlias, nameAlias, issn).setMaxResults(1).list();
    return CollectionUtils.isEmpty(list) ? null : (Long) list.get(0);
  }

  // 任务匹配
  @SuppressWarnings("unchecked")
  public List<BaseJournalTitleTo> snsJnlMatchBaseJnlId(Journal jnl) {
    DetachedCriteria criteria = DetachedCriteria.forClass(BaseJournalTitleTo.class);
    String titleZhAlias = StringUtils.trimToEmpty(jnl.getZhNameAlias());
    String titleEnAlias = StringUtils.trimToEmpty(jnl.getEnameAlias());
    criteria.add(
        Restrictions.or(Restrictions.eq("titleXxAlias", titleZhAlias), Restrictions.eq("titleEnAlias", titleEnAlias)));
    if (StringUtils.isNotBlank(jnl.getIssn())) {
      criteria.add(Restrictions.eq("pissn", jnl.getIssn()));
    }
    return (List<BaseJournalTitleTo>) this.hibernateTemplate.findByCriteria(criteria);
  }

  public List<BaseJournalTitleTo> jnlMatchBaseJnlTitile(Journal jnl) {
    String hql =
        "from BaseJournalTitleTo where pissn=? or titleXxAlias like ? or titleEnAlias like ? or titleXxAlias like ? or titleEnAlias like ? or titleAbbrAlias like ? ";
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

  public List<BaseJournalTitleTo> jnlMatchBaseJnlTitile(String zhTitleAlias, String enTitleAlias) {
    String hql = "from BaseJournalTitleTo where titleXxAlias=? or titleEnAlias=?";
    zhTitleAlias = StringUtils.isBlank(zhTitleAlias) ? "" : zhTitleAlias;
    enTitleAlias = StringUtils.isBlank(enTitleAlias) ? "" : enTitleAlias;
    return super.find(hql, zhTitleAlias, enTitleAlias);
  }

}
