package com.smate.center.open.dao.grp;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.grp.GrpPubs;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组成果dao
 * 
 * @author tsz
 *
 */
@Repository
public class GrpPubsDao extends SnsHibernateDao<GrpPubs, Long> {

  /**
   * 查找总数
   * 
   * @param psnId
   * @param getPubDate
   * @return
   */
  public Long findCount(Long grpId, Date getPubDate) {
    String hql = "select  count(t)  from  GrpPubs t   where   t.grpId =:grpId   ";
    if (getPubDate != null) {
      hql += " and t.updateDate >= :getPubDate ";
    }
    Query query = this.createQuery(hql).setParameter("grpId", grpId);
    if (getPubDate != null) {
      query.setParameter("getPubDate", getPubDate);
    }

    Object object = query.uniqueResult();
    if (object != null) {
      return NumberUtils.toLong(object.toString());
    }
    return null;
  }

  /**
   * 查询成群组果列表
   */
  public List<GrpPubs> findGrpPubList(Long grpId, Integer pageNo, Integer pageSize, Date getPubDate) {
    String hql =
        "select     new GrpPubs( t.pubId  , t.labeled  , t.relevance  , t.updateDate  ) from  GrpPubs t where ";

    if (getPubDate != null) {
      hql += " t.updateDate >= :getPubDate  and  ";
    }
    hql += " t.grpId=:grpId      order by t.pubId desc";
    Query query = this.createQuery(hql).setParameter("grpId", grpId).setFirstResult((pageNo - 1) * pageSize)
        .setMaxResults(pageSize);
    if (getPubDate != null) {
      query.setParameter("getPubDate", getPubDate);
    }
    return query.list();
  }

  /**
   * 判断成果id是否属于 该群组
   * 
   * @param pubId
   * @param groupId
   * @return
   */
  public Boolean isBelongGrpPub(Long pubId, Long grpId) {
    boolean flag = false;
    String hql = "select g.pubId  from GrpPubs  g where g.pubId=:pubId  and g.grpId=:grpId and g.status  in ( 0 , 1) ";
    List<Object> list = this.createQuery(hql).setParameter("pubId", pubId).setParameter("grpId", grpId).list();
    if (list != null && list.size() > 0) {
      flag = true;
    }
    return flag;
  }

  /**
   * 群组成果的删除状态
   * 
   * @param pubId
   * @param grpId
   * @return
   */
  public Integer findGrpPubsStatus(Long pubId, Long grpId) {
    String hql = "select g.status  from GrpPubs  g where g.pubId=:pubId  and g.grpId=:grpId ";
    Object obj = this.createQuery(hql).setParameter("pubId", pubId).setParameter("grpId", grpId).uniqueResult();
    if (obj != null) {
      return Integer.parseInt(obj.toString());
    }
    return 1;
  }

}
