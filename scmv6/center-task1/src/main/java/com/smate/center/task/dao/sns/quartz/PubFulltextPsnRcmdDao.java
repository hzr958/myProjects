package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.model.sns.quartz.PubFulltextPsnRcmd;
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
  @SuppressWarnings("unchecked")
  public List<PubFulltextPsnRcmd> queryRcmdFulltextCount(Long psnId) throws Exception {
    String sql =
        "select t1.* from (select t.*, ROW_NUMBER() OVER(PARTITION BY t.pub_id ORDER BY t.match_type asc, t.db_id desc, t.rcmd_date desc) as nm from PUB_FULLTEXT_PSN_RCMD t where t.psn_id = :psnId and t.status = 0) t1 where t1.nm=1 "
            + "and exists (select 1 from V_PSN_PUB pp where pp.pub_id = t1.pub_id and  pp.status = 0 ) order by t1.rcmd_date desc , t1.id desc ";
    SQLQuery query = super.getSession().createSQLQuery(sql).addEntity(PubFulltextPsnRcmd.class);
    return query.setParameter("psnId", psnId).list();
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

  @SuppressWarnings("unchecked")
  public PubFulltextPsnRcmd getPsnRcmd(Long pubId, int dbId, Long srcPubId) {
    String hql = "from PubFulltextPsnRcmd t where t.pubId = :pubId and t.srcPubId = :srcPubId and t.dbId = :dbId";
    return (PubFulltextPsnRcmd) super.createQuery(hql).setParameter("pubId", pubId).setParameter("srcPubId", srcPubId)
        .setParameter("dbId", dbId).uniqueResult();
  }

  public void deleteFulltextPsnRcmd(Long srcPubId) {
    String hql = "delete from PubFulltextPsnRcmd t where t.srcPubId = :srcPubId";
    super.createQuery(hql).setParameter("srcPubId", srcPubId).executeUpdate();

  }

  public PubFulltextPsnRcmd getFulltextRcmd(Long snsPubId) {
    List<PubFulltextPsnRcmd> rcmdList =
        super.createQuery("from PubFulltextPsnRcmd t where t.pubId = ?", snsPubId).list();
    if (rcmdList != null && rcmdList.size() > 0) {
      return rcmdList.get(0);
    } else {
      return null;
    }

  }

  public List<PubFulltextPsnRcmd> getRepeatPsnRcmd(Long pubId, Long psnId, Long fulltextFileId, Long fileSize) {
    String hql =
        "from PubFulltextPsnRcmd t where t.pubId = :pubId and t.psnId = :psnId and (t.fulltextFileId=:fulltextFileId or t.fileSize=:fileSize)";
    return super.createQuery(hql).setParameter("pubId", pubId).setParameter("psnId", psnId)
        .setParameter("fulltextFileId", fulltextFileId).setParameter("fileSize", fileSize).list();

  }

  /**
   * 删除所有重复记录
   * 
   * @param pubId
   * @param createPsnId
   * @param fulltextFileId
   * @param fileSize
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteAllRepeatPsnRcmd(Long pubId, Long psnId, Long fulltextFileId, Long fileSize) {
    String hql =
        "delete from  PubFulltextPsnRcmd t where t.pubId = :pubId and t.psnId = :psnId and (t.fulltextFileId=:fulltextFileId or t.fileSize=:fileSize)";
    super.createQuery(hql).setParameter("pubId", pubId).setParameter("psnId", psnId)
        .setParameter("fulltextFileId", fulltextFileId).setParameter("fileSize", fileSize).executeUpdate();
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveOrUpdateWithNewTs(PubFulltextPsnRcmd psnRcmd) {
    super.saveOrUpdate(psnRcmd);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteWithNewTs(Long id) {
    super.delete(id);
  }
}
