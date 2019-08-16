package com.smate.web.management.dao.other.fund;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.web.management.model.other.fund.ConstFundCategorykeywords;

/**
 * @author cwli
 * 
 */
@Repository
public class ConstFundCategoryKeywordsDao extends HibernateDao<ConstFundCategorykeywords, Long> {

  @SuppressWarnings("unchecked")
  public List<ConstFundCategorykeywords> getAcFundKeywords(String startWith, int size) {
    startWith = StringUtils.trim(startWith).toLowerCase();
    String hql =
        "from ConstFundCategorykeywords where instr(lower(keyword),:keyword1)>0 order by instr(lower(keyword),:keyword2) asc,id asc";
    return super.createQuery(hql).setParameter("keyword1", startWith).setParameter("keyword2", startWith)
        .setMaxResults(size).list();
  }

  public void deleteFundKeywordByCategoryId(Long categoryId) {
    String hql = "delete from ConstFundCategorykeywords t where t.categoryId=?";
    super.createQuery(hql, categoryId).executeUpdate();
  }

  public List<ConstFundCategorykeywords> findFundKeywordByCategoryId(Long categoryId) {
    String hql = "from ConstFundCategorykeywords t where t.categoryId=?";
    return super.find(hql, categoryId);
  }

  public List<Long> findFundKeywordHashByCategoryId(Long categoryId) {
    String hql = "select keywordHash from ConstFundCategorykeywords t where t.categoryId=?";
    return super.find(hql, categoryId);
  }

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.RCMD;
  }
}
