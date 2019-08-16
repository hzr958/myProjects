package com.smate.web.group.dao.grp.pub;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.GrpPubs;
import com.smate.web.group.model.grp.pub.PubSimple;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 群组成果dao
 * 
 * @author tsz
 */
@Repository
public class GrpPubsDao extends SnsHibernateDao<GrpPubs, Long> {
  public List<Object[]> findImpPubMember(Long grpId, Integer isProjectPub) {
    // 上传数 修改为 属于各自的成果数
    // 属于各自的成果数
    String sql = " select t.ownerPsnId,count(1)   from GrpPubs  t where t.grpId =:grpId and t.status = 0";
    if (isProjectPub != null) {
      sql += " and  t.isProjectPub=:isProjectPub ";
    }
    sql += " group by t.ownerPsnId order by count(1) desc";
    Query query = this.createQuery(sql).setParameter("grpId", grpId);
    if (isProjectPub != null) {
      query.setParameter("isProjectPub", isProjectPub);
    }
    return query.list();
  }

  /**
   * 获取我的群组成果-复制群组用
   * 
   * @param grpId
   * @param psnId
   * @return
   */
  public List<GrpPubs> getGrpPubsList(Long grpId, Long psnId) {
    String hql =
        "select new GrpPubs(t.pubId, t.labeled, t.relevance, t.createPsnId, t.createDate, t.updateDate,t.updatePsnId, t.isProjectPub, t.status,t.ownerPsnId) from GrpPubs t where t.status=0 and t.ownerPsnId=:psnId and t.grpId=:grpId";
    return this.createQuery(hql).setParameter("psnId", psnId).setParameter("grpId", grpId).list();
  }

  /**
   * 获取我的项目群组群组成果-复制群组用
   * 
   * @param grpId
   * @param psnId
   * @param isProjectPub 是否项目成果 是否项目成果 (0否)（1是）
   * @return
   */
  public List<GrpPubs> getProjectGrpPubsList(Long grpId, Long psnId, Integer isProjectPub) {
    String hql =
        "select new GrpPubs(t.pubId, t.labeled, t.relevance, t.createPsnId, t.createDate, t.updateDate,t.updatePsnId, t.isProjectPub, t.status,t.ownerPsnId) from GrpPubs t where t.status=0 and t.ownerPsnId =:psnId   and t.isProjectPub=:isProjectPub and t.grpId=:grpId";
    return this.createQuery(hql).setParameter("isProjectPub", isProjectPub).setParameter("psnId", psnId)
        .setParameter("grpId", grpId).list();
  }

  /** （退出群组或删除群组）删除该人拥有的成果 */
  public Integer delGrpPubsForexitGrp(Long psnId, Long grpId) {
    String hql =
        "update GrpPubs t set t.status=1 where status=0 and t.grpId=:grpId and t.pubId in(select t1.pubId from PsnPubPO t1 where t1.ownerPsnId=:psnId ) ";
    return this.createQuery(hql).setParameter("psnId", psnId).setParameter("grpId", grpId).executeUpdate();
  }

  public List<PubSimple> getFiveGrpPubsForDiscuss(Long grpId, boolean isProjectGrp) {
    String hql =
        " select new PubSimple( t.pubId , t.citedTimes  , t.zhTitle  , t.enTitle  , t.authorNames ,t.briefDesc,t.briefDescEn) "
            + " from  PubSimple t " + " where   t.pubId in";
    String middleHql = "";
    if (isProjectGrp) {
      middleHql = " ( select  gb.pubId  " + "   from  GrpPubs gb "
          + "  where gb.grpId =:grpId  and gb.status =  0  and gb.isProjectPub = 1  ) ";
    } else {
      middleHql = " ( select  gb.pubId  " + "   from  GrpPubs gb " + "  where gb.grpId =:grpId  and gb.status = 0 ) ";
    }
    hql += middleHql;
    hql += " and t.status  !=1 order by t.citedTimes  desc  nulls last   ";
    Object obj = this.createQuery(hql).setParameter("grpId", grpId).setMaxResults(5).list();
    if (obj != null) {
      return (List<PubSimple>) obj;
    }
    return null;
  }

  /**
   * 根据成果id跟群组id 查询群组成果对象
   * 
   * @param pubId
   * @param grpId
   * @return
   * @throws Exception
   */
  public GrpPubs getGrpPubs(Long pubId, Long grpId) throws Exception {
    String hql = "from GrpPubs where pubId=:pubId and grpId=:grpId and status=0";
    Object obj = super.createQuery(hql).setParameter("pubId", pubId).setParameter("grpId", grpId).uniqueResult();
    if (obj != null) {
      return (GrpPubs) obj;
    }
    return null;
  }

  /**
   * 根据成果id跟群组id 查询群组成果对象
   * 
   * @param pubId
   * @param grpId
   * @return
   * @throws Exception
   */
  public GrpPubs getGrpPubsByPubIdAndGrpId(Long pubId, Long grpId) throws Exception {
    String hql = "from GrpPubs where pubId=:pubId and grpId=:grpId";
    Object obj = super.createQuery(hql).setParameter("pubId", pubId).setParameter("grpId", grpId).uniqueResult();
    if (obj != null) {
      return (GrpPubs) obj;
    }
    return null;
  }

  /**
   * 获取群组成果数量
   * 
   * @param grpId
   * @return
   * @throws Exception
   */
  public Long getGrpPubsSum(Long grpId) throws Exception {
    String hql =
        "select count(t.grpId) from GrpPubs t where exists (select 1 from PubSnsPO t1 where t1.status <> 1 and t.pubId = t1.pubId) and t.status=0 and t.grpId=:grpId";
    return (Long) super.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
  }

  /**
   * @param grpId
   * @param isProjectPub
   * @return
   * @throws Exception
   */
  public Long countGrpPubsSum(Long grpId, Integer isProjectPub) {
    String hql =
        "select count(1) from GrpPubs t where exists (select 1 from PubSnsPO t1 where t1.status <> 1 and t.pubId = t1.pubId) and t.status=0 and  t.isProjectPub=:isProjectPub  and  t.grpId=:grpId";
    return (Long) super.createQuery(hql).setParameter("isProjectPub", isProjectPub).setParameter("grpId", grpId)
        .uniqueResult();
  }
}
