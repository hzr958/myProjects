package com.smate.sie.web.application.dao.analysis;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.web.application.model.analysis.KpiKeywordsTfCotfNKw;
import com.smate.sie.web.application.model.analysis.KpiKeywordsTfCotfNKwPk;

@Repository
public class KpiKeywordsTfCotfNKwDao extends SieHibernateDao<KpiKeywordsTfCotfNKw, KpiKeywordsTfCotfNKwPk> {

  public List<KpiKeywordsTfCotfNKw> findNsfcKeywordsListBykw(String sub3DisCode) {
    String hql = "from KpiKeywordsTfCotfNKw t where t.kwPk.sub3DisCode = ? order by t.counts desc";
    Query query = super.createQuery(hql, new Object[] {sub3DisCode});
    List<KpiKeywordsTfCotfNKw> list = query.list();
    return list;
  }

}
