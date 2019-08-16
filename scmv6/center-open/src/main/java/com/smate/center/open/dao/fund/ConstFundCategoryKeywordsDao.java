package com.smate.center.open.dao.fund;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.fund.ConstFundCategoryKeywords;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * @author cwli
 * 
 */
@Repository
public class ConstFundCategoryKeywordsDao extends RcmdHibernateDao<ConstFundCategoryKeywords, Long> {

  @SuppressWarnings("unchecked")
  public List<ConstFundCategoryKeywords> getAcFundKeywords(String startWith, int size) {
    startWith = StringUtils.trim(startWith).toLowerCase();
    String hql =
        "from ConstFundCategoryKeywords where instr(lower(keyword),:keyword1)>0 order by instr(lower(keyword),:keyword2) asc,id asc";
    return super.createQuery(hql).setParameter("keyword1", startWith).setParameter("keyword2", startWith)
        .setMaxResults(size).list();
  }

  public void deleteFundKeywordByCategoryId(Long categoryId) {
    String hql = "delete from ConstFundCategoryKeywords t where t.categoryId=?";
    super.createQuery(hql, categoryId).executeUpdate();
  }

  public List<ConstFundCategoryKeywords> findFundKeywordByCategoryId(Long categoryId) {
    String hql = "from ConstFundCategoryKeywords t where t.categoryId=?";
    return super.find(hql, categoryId);
  }

  public List<Long> findFundKeywordHashByCategoryId(Long categoryId) {
    String hql = "select keywordHash from ConstFundCategoryKeywords t where t.categoryId=?";
    return super.find(hql, categoryId);
  }
}
