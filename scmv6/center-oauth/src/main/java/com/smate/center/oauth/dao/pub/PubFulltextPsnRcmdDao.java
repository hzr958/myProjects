package com.smate.center.oauth.dao.pub;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.oauth.model.pub.PubFulltextPsnRcmd;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果全文人员指派记录Dao.
 * 
 * @author wsn
 * 
 */
@Repository
public class PubFulltextPsnRcmdDao extends SnsHibernateDao<PubFulltextPsnRcmd, Long> {

  /**
   * 获取成果全文推荐总数（有可能会不准确，因为一条成果可能会多篇全文推荐记录，但是页面只显示一条）.
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  public Long queryRcmdFulltextCount(Long psnId) throws Exception {

    String hql =
        "select t2.id from (select t1.id, ROW_NUMBER() OVER(PARTITION BY t1.pub_id ORDER BY t1.match_type asc, t1.db_id desc) as nm from PUB_FULLTEXT_PSN_RCMD t1, PUBLICATION t2 where t2.status = 0 and t1.pub_id = t2.pub_id and t1.psn_id = :psnId and t1.status = 0) t2 where t2.nm = 1";

    Object count = super.getSession().createSQLQuery("select count(t3.id) from (" + hql.toString() + ") t3")
        .setParameter("psnId", psnId).uniqueResult();

    return NumberUtils.toLong(ObjectUtils.toString(count));
  }

}
