package com.smate.center.batch.dao.sns.pub;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import java.sql.Clob;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.smate.center.batch.model.sns.pub.HtmlExtend;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * html推广
 * 
 * @author zk
 * 
 */
@Repository
public class HtmlExtendDao extends SnsHibernateDao<HtmlExtend, Long> {

  public Long getHtmlExtendId() {
    String sql = "select seq_html_extend.nextval from dual";
    BigDecimal id = (BigDecimal) super.getSession().createSQLQuery(sql).uniqueResult();
    return id.longValue();
  }

  /**
   * 保存HtmlExtend Rol-2759
   */
  public void saveHtmlExtendHandler(HtmlExtend htmlExtend, Long extendId) {
    String sql = "insert into html_extend values(?,?,?)";
    super.update(sql, new Object[] {extendId, htmlExtend.getHtmlZh(), htmlExtend.getHtmlEn()});
  }

  /**
   * 根据extendId查找HtmlExtend Rol-2759
   */
  public HtmlExtend findHtmlExtendById(Long extendId) {
    String hql = "from HtmlExtend t where t.id = ? ";
    return (HtmlExtend) super.createQuery(hql, extendId).uniqueResult();
  }

  /**
   * 根据extendId删除HtmlExtend Rol-2759
   */
  public int deleteHtmlExtendById(Long extendId) {
    String sql = "delete from html_extend t where t.id =? ";
    int count = super.getSession().createSQLQuery(sql).setParameter(0, extendId).executeUpdate();
    return count;
  }

}
