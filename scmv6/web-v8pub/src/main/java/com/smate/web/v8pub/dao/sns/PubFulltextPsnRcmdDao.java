package com.smate.web.v8pub.dao.sns;

import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.v8pub.po.sns.PubFulltextPsnRcmd;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

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
   * @return @
   */
  public PubFulltextPsnRcmd queryPubFulltextPsnRcmd(Long pubId, Long fulltextFileId) {

    String hql = "from PubFulltextPsnRcmd t where t.pubId = ? and t.fulltextFileId = ?";
    PubFulltextPsnRcmd fulltextPsnCmd =
        (PubFulltextPsnRcmd) super.createQuery(hql, new Object[] {pubId, fulltextFileId}).uniqueResult();
    return fulltextPsnCmd;
  }

  @SuppressWarnings("unchecked")
  public List<PubFulltextPsnRcmd> queryPubRcmdPubFulltext(Long psnId, Long pubId) {
    String sql =
        "select t1.* from (select t.*, ROW_NUMBER() OVER(PARTITION BY t.pub_id ORDER BY t.match_type asc, t.db_id desc, t.rcmd_date desc) as nm from PUB_FULLTEXT_PSN_RCMD t where t.psn_id = :psnId and pub_id = :pubId and t.status = 0) t1 where t1.nm=1";
    SQLQuery query = super.getSession().createSQLQuery(sql).addEntity(PubFulltextPsnRcmd.class);
    return query.setParameter("psnId", psnId).setParameter("pubId", pubId).list();
  }

  @SuppressWarnings("unchecked")
  public List<PubFulltextPsnRcmd> queryRcmdPubFulltext(PubOperateVO pubOperateVO, Page page) {
    String sql =
        "select t1.* from (select t.*, ROW_NUMBER() OVER(PARTITION BY t.pub_id ORDER BY t.match_type asc, t.db_id desc, t.rcmd_date desc) as nm from PUB_FULLTEXT_PSN_RCMD t where t.psn_id = :psnId and t.status = 0) t1 where t1.nm=1 "
            + "and exists (select 1 from V_PSN_PUB pp where pp.pub_id = t1.pub_id and  pp.status = 0 ) "
            + " order by t1.rcmd_date desc , t1.id desc ";
    Object count = super.getSession().createSQLQuery("select count(t4.id) from (" + sql + ") t4")
        .setParameter("psnId", pubOperateVO.getPsnId()).uniqueResult();
    page.setTotalCount(NumberUtils.toLong(ObjectUtils.toString(count)));
    List<PubFulltextPsnRcmd> list = null;
    SQLQuery query = super.getSession().createSQLQuery(sql).addEntity(PubFulltextPsnRcmd.class);
    query.setParameter("psnId", pubOperateVO.getPsnId());
    if (pubOperateVO.getIsAll() == 1) {
      // 不需要设置最大条数
      list = query.setFirstResult(page.getFirst() - 1 - pubOperateVO.getFulltextCount()).list();
    } else {
      list = query.setFirstResult(page.getFirst() - 1).setMaxResults(1).list();
    }
    page.setResult(list);
    return list;
  }

  @SuppressWarnings("unchecked")
  public List<PubFulltextPsnRcmd> queryRcmdPubFulltextCount(Long psnId) {
    String sql =
        "select t1.* from (select t.*, ROW_NUMBER() OVER(PARTITION BY t.pub_id ORDER BY t.match_type asc, t.db_id desc, t.rcmd_date desc) as nm from PUB_FULLTEXT_PSN_RCMD t where t.psn_id = :psnId and t.status = 0) t1 where t1.nm=1 "
            + "and exists (select 1 from V_PSN_PUB pp where pp.pub_id = t1.pub_id and  pp.status = 0 ) order by t1.rcmd_date desc , t1.id desc ";
    List<PubFulltextPsnRcmd> list = null;
    SQLQuery query = super.getSession().createSQLQuery(sql).addEntity(PubFulltextPsnRcmd.class);
    return query.setParameter("psnId", psnId).list();
  }



  /**
   * 根据成果id更新状态.
   * 
   * @param pubId @
   */
  public void updateStatusByPubId(Long pubId, int status) {

    super.createQuery("update PubFulltextPsnRcmd t set t.status = ? where t.pubId = ?", new Object[] {status, pubId})
        .executeUpdate();
  }

  /**
   * 获取成果全文推荐总数（有可能会不准确，因为一条成果可能会多篇全文推荐记录，但是页面只显示一条）.
   * 
   * @param psnId
   * @return @
   */
  public Long queryRcmdFulltextCount(Long psnId) {

    String hql =
        "select t1.* from (select t.*, ROW_NUMBER() OVER(PARTITION BY t.pub_id ORDER BY t.match_type asc, t.db_id desc, t.rcmd_date desc) as nm from PUB_FULLTEXT_PSN_RCMD t where t.psn_id = :psnId and t.status = 0) t1,v_psn_pub t2 where t1.nm=1 and t1.pub_id = t2.pub_id and t2.status = 0";

    Object count = super.getSession().createSQLQuery("select count(t3.id) from (" + hql.toString() + ") t3")
        .setParameter("psnId", psnId).uniqueResult();

    return NumberUtils.toLong(ObjectUtils.toString(count));
  }

  /**
   * 删除成果的全文推荐记录.
   * 
   * @param pubId @
   */
  public void deletePubFulltextPsnRcmd(Long pubId) {

    super.createQuery("delete from PubFulltextPsnRcmd t where t.pubId = ?", pubId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PubFulltextPsnRcmd> queryFulltextRcmdByPubId(Long pubId) {

    return super.createQuery("from PubFulltextPsnRcmd t where t.pubId = ?", pubId).list();
  }

  @SuppressWarnings("unchecked")
  public List<PubFulltextPsnRcmd> queryFulltextRcmdBySrcPubId(Long srcPubId) {

    return super.createQuery("from PubFulltextPsnRcmd t where t.srcPubId = ?", srcPubId).list();
  }

  /**
   * 移动端-全文认领列表,查询所有的全文
   * 
   * @param form
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void queryFulltextList(PubOperateVO pubOperateVO, Page page) {
    String sql =
        "select t1.* from (select t.*, ROW_NUMBER() OVER(PARTITION BY t.pub_id ORDER BY t.match_type asc, t.db_id desc, t.rcmd_date desc) as nm from PUB_FULLTEXT_PSN_RCMD t where t.psn_id = :psnId and t.status = 0) t1,v_pub_sns t2 where t1.nm=1 and t1.pub_id = t2.pub_id and t2.status = 0";
    Object count = super.getSession().createSQLQuery("select count(t4.id) from (" + sql + ") t4")
        .setParameter("psnId", pubOperateVO.getPsnId()).uniqueResult();
    page.setTotalCount(NumberUtils.toLong(ObjectUtils.toString(count)));
    if (page.getTotalCount().longValue() > 0) {
      List<PubFulltextPsnRcmd> list = super.getSession().createSQLQuery(sql).addEntity(PubFulltextPsnRcmd.class)
          .setParameter("psnId", pubOperateVO.getPsnId()).list();
      page.setResult(list);
    }

  }

  public PubFulltextPsnRcmd getPubFulltext(Long psnId, Long pubId) {
    String sql = "from PubFulltextPsnRcmd t where t.pubId= :pubId and t.psnId = :psnId and t.status=0";
    return (PubFulltextPsnRcmd) super.createQuery(sql).setParameter("psnId", psnId).setParameter("pubId", pubId)
        .setMaxResults(1).uniqueResult();
  }

  public void delePubFulltext(Long psnId, Long pubId) {
    String sql = "delete from PubFulltextPsnRcmd t where t.pubId= :pubId and t.psnId = :psnId and t.status=0";
    super.createQuery(sql).setParameter("psnId", psnId).setParameter("pubId", pubId).executeUpdate();
  }

  public List<PubFulltextPsnRcmd> queryRcmdPubFulltext(Long psnId) {
    String sql =
        "select t1.* from (select t.*, ROW_NUMBER() OVER(PARTITION BY t.pub_id ORDER BY t.match_type asc, t.db_id desc, t.rcmd_date desc) as nm from PUB_FULLTEXT_PSN_RCMD t where t.psn_id = :psnId and t.status = 0) t1 where t1.nm=1 "
            + "and exists (select 1 from V_PSN_PUB pp where pp.pub_id = t1.pub_id and  pp.status = 0 ) ";
    SQLQuery query = super.getSession().createSQLQuery(sql).addEntity(PubFulltextPsnRcmd.class);
    List<PubFulltextPsnRcmd> list = query.setParameter("psnId", psnId).list();
    return list;
  }

  public void deleteBySnsPubId(Long srcPubId) {
    String hql = "delete from PubFulltextPsnRcmd t where t.status=0 and t.srcPubId=:srcPubId";
    super.createQuery(hql).setParameter("srcPubId", srcPubId).executeUpdate();
  }

  public Long getPubRcmdPubFulltextCount(Long psnId, Long pubId) {
    String hql = "SELECT count(*) FROM PubFulltextPsnRcmd t where  t.pubId= :pubId and t.psnId = :psnId and t.status=0";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).setParameter("psnId", psnId).uniqueResult();
  }
}
