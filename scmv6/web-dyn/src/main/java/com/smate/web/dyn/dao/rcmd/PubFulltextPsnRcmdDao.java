package com.smate.web.dyn.dao.rcmd;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.pub.PubFulltextPsnRcmd;

/**
 * 成果全文人员指派记录Dao.
 * 
 * @author pwl
 * 
 */
@Repository
public class PubFulltextPsnRcmdDao extends SnsHibernateDao<PubFulltextPsnRcmd, Long> {

  /*
   * 获取成果全文推荐总数（有可能会不准确，因为一条成果可能会多篇全文推荐记录，但是页面只显示一条）.
   * 
   * @param psnId
   * 
   * @return @
   */
  public Long queryRcmdFulltextCount(Long psnId) {
    String hql =
        "select t1.* from (select t.*, ROW_NUMBER() OVER(PARTITION BY t.pub_id ORDER BY t.match_type asc, t.db_id desc, t.rcmd_date desc) as nm from PUB_FULLTEXT_PSN_RCMD t where t.psn_id = :psnId and t.status = 0) t1,v_psn_pub t2 where t1.nm=1 and t1.pub_id = t2.pub_id and t2.status = 0";
    Object count = super.getSession().createSQLQuery("select count(t3.id) from (" + hql.toString() + ") t3")
        .setParameter("psnId", psnId).uniqueResult();

    return NumberUtils.toLong(ObjectUtils.toString(count));
  }

}
