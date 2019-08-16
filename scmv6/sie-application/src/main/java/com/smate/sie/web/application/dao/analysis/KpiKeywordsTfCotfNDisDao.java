package com.smate.sie.web.application.dao.analysis;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.web.application.model.analysis.KpiKeywordsTfCotfNDis;
import com.smate.sie.web.application.model.analysis.KpiKeywordsTfCotfNDisPk;

@Repository
public class KpiKeywordsTfCotfNDisDao extends SieHibernateDao<KpiKeywordsTfCotfNDis, KpiKeywordsTfCotfNDisPk> {

  public List<KpiKeywordsTfCotfNDis> findNsfcKeywordsListBykw(String selectKw) {
    String hql = "from KpiKeywordsTfCotfNDis t where t.disPk.kwFirst = ? order by t.counts desc";
    Query query = super.createQuery(hql, new Object[] {selectKw});
    List<KpiKeywordsTfCotfNDis> list = query.list();
    return list.size() > 5 ? list.subList(0, 5) : list;
  }

}
