package com.smate.center.batch.dao.sns.prj;

import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.prj.PubFulltextPsnRcmd;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果全文人员指派记录Dao.
 * 
 * @author pwl
 * 
 */
@Repository
public class PubFulltextPsnRcmdDao extends SnsHibernateDao<PubFulltextPsnRcmd, Long> {

  /**
   * 查询成果全文人员推荐记录.
   * 
   * @param pubId
   * @param fulltextFileId
   * @return
   * @throws Exception
   */
  public PubFulltextPsnRcmd queryPubFulltextPsnRcmd(Long pubId, Long fulltextFileId) throws Exception {

    String hql = "from PubFulltextPsnRcmd t where t.pubId = ? and t.fulltextFileId = ?";
    PubFulltextPsnRcmd fulltextPsnCmd =
        (PubFulltextPsnRcmd) super.createQuery(hql, new Object[] {pubId, fulltextFileId}).uniqueResult();
    return fulltextPsnCmd;
  }


  /**
   * 根据成果id更新状态.
   * 
   * @param pubId
   * @throws Exception
   */
  public void updateStatusByPubId(Long pubId, int status) throws Exception {

    super.createQuery("update PubFulltextPsnRcmd t set t.status = ? where t.pubId = ?", new Object[] {status, pubId})
        .executeUpdate();
  }

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

  /**
   * 删除成果的全文推荐记录.
   * 
   * @param pubId
   * @throws Exception
   */
  public void deletePubFulltextPsnRcmd(Long pubId) throws Exception {

    super.createQuery("delete from PubFulltextPsnRcmd t where t.pubId = ?", pubId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PubFulltextPsnRcmd> queryFulltextRcmdByPubId(Long pubId) throws Exception {

    return super.createQuery("from PubFulltextPsnRcmd t where t.pubId = ?", pubId).list();
  }

  @SuppressWarnings("unchecked")
  public List<PubFulltextPsnRcmd> queryFulltextRcmdBySrcPubId(Long srcPubId) throws Exception {

    return super.createQuery("from PubFulltextPsnRcmd t where t.srcPubId = ?", srcPubId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> queryOwnerPsnIdByPubId(Long pubId) throws Exception {

    String hql = "select distinct(t.psnId) from PubFulltextPsnRcmd t where t.pubId = ?";
    List<Long> ownerPsnIds = super.createQuery(hql, pubId).list();
    return ownerPsnIds;
  }
}
