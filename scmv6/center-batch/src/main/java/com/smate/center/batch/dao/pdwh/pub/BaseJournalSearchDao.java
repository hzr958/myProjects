package com.smate.center.batch.dao.pdwh.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.BaseJournalSearch;
import com.smate.center.batch.model.pdwh.pub.JnlRecLeftForm;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 
 * @author WeiLong Peng
 * 
 */
@Repository
public class BaseJournalSearchDao extends PdwhHibernateDao<BaseJournalSearch, Long> {

  /**
   * 分页查询
   * 
   * @param page
   * @param jnlIdList
   * @param hashList
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Page<BaseJournalSearch> findJournalByPage(Page<BaseJournalSearch> page, JnlRecLeftForm form) {
    String hql = "";
    List<Object> params = new ArrayList<Object>();
    params.add(form.getPsnId());
    hql =
        "select new BaseJournalSearch(t1.jnlId,t1.titleXx,t1.titleEn,t1.dbCodes,t1.impactFactors,t1.ifYear)  from BaseJournalSearch t1,PsnJnlRefRecommend t2 where t1.jnlId=t2.jnlId and t2.psnId=? order by t2.score desc,t2.id";
    Query q = createQuery(hql, params.toArray());
    if (page.isAutoCount()) {
      Long count = 0L;
      String fromHql = hql;
      fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
      fromHql = StringUtils.substringBefore(fromHql, "order by");
      String countHql = "select count(t1.id) " + fromHql;
      try {
        count = findUnique(countHql, params.toArray());
      } catch (Exception e) {
        throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
      }
      page.setTotalCount(count);
    }
    setPageParameter(q, page);
    List result = q.list();
    page.setResult(result);
    return findPage(page, hql, params.toArray());
  }
}
