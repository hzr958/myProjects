package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.AcFundCategory;

/**
 * 基金申请计划类别.
 * 
 * @author chenxiangrong
 * 
 */
@Repository
public class AcFundCategoryDao extends AutoCompleteDao<AcFundCategory> {
  @SuppressWarnings("unchecked")
  public List<AcFundCategory> getAcFundCategory(String startStr, int agencyFlag, int size) throws DaoException {
    boolean isChinese = !StringUtils.isAsciiPrintable(startStr);
    String hql = "";
    if (isChinese) {
      hql = "select new AcFundCategory(t.id,t.fullCategory) from AcFundCategory t where lower(t.fullCategory) like ?";
    } else {
      hql =
          "select new AcFundCategory(t.id,t.enFullCategory) from AcFundCategory t where lower(t.enFullCategory) like ?";
    }
    if (agencyFlag != 0 && agencyFlag != -1) {
      hql = hql + " and t.agencyFlag=" + agencyFlag;
    }
    Query query = super.createQuery(hql, new Object[] {"%" + startStr.toLowerCase() + "%"});
    query.setMaxResults(size);
    return query.list();
  }
}
