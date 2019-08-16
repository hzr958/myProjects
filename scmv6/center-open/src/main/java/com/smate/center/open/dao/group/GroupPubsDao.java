package com.smate.center.open.dao.group;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.group.pub.GroupPubs;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组 成果关系 冗余 dao
 * 
 * @author tsz
 *
 */

@Repository
public class GroupPubsDao extends SnsHibernateDao<GroupPubs, Long> {


  public GroupPubs findGroupPub(Long groupId, Long pubId) {
    String hql = "from  GroupPubs t where t.groupId=:groupId  and t.pubId=:pubId";
    Object obj = this.createQuery(hql).setParameter("groupId", groupId).setParameter("pubId", pubId).uniqueResult();
    if (obj != null) {
      return (GroupPubs) obj;
    }
    return null;
  }

  /**
   * 查询成群组果列表
   */
  public List<Long> findGroupPubIdList(Long groupId, Integer pageNo, Integer pageSize) {
    String hql = "select t.pubId  from  GroupPubs t where t.groupId=:groupId  order by t.pubId desc";
    return this.createQuery(hql).setParameter("groupId", groupId).setFirstResult((pageNo - 1) * pageSize)
        .setMaxResults(pageSize).list();
  }

  /**
   * 查询成群组果列表
   */
  public List<GroupPubs> findGroupPubInfoList(Long groupId, Integer pageNo, Integer pageSize) {
    String hql =
        "select  new GroupPubs(t.pubId , t.labeled , t.relevance) from  GroupPubs t where t.groupId=:groupId  order by t.pubId desc";
    return this.createQuery(hql).setParameter("groupId", groupId).setFirstResult((pageNo - 1) * pageSize)
        .setMaxResults(pageSize).list();
  }

  /**
   * 查询成群组果列表
   */
  public List<GroupPubs> findGroupPubInfoList(Long groupId, Integer pageNo, Integer pageSize, Date getPubDate) {
    String hql = "select  new GroupPubs(t.pubId , t.labeled , t.relevance) from  GroupPubs t where ";

    if (getPubDate != null) {
      hql += " t.updateDate >= :getPubDate  and  ";
    }
    hql += " t.groupId=:groupId  order by t.pubId desc";
    Query query = this.createQuery(hql).setParameter("groupId", groupId).setFirstResult((pageNo - 1) * pageSize)
        .setMaxResults(pageSize);
    if (getPubDate != null) {
      query.setParameter("getPubDate", getPubDate);
    }
    return query.list();
  }

  /**
   * 查询成群组果列表
   */
  public List<Long> findGroupPubIdList(Long groupId, Integer pageNo, Integer pageSize, Date getPubDate) {
    String hql = "select t.pubId from  GroupPubs t where ";

    if (getPubDate != null) {
      hql += " t.updateDate >= :getPubDate  and  ";
    }
    hql += " t.groupId=:groupId  order by t.pubId desc";
    Query query = this.createQuery(hql).setParameter("groupId", groupId).setFirstResult((pageNo - 1) * pageSize)
        .setMaxResults(pageSize);
    if (getPubDate != null) {
      query.setParameter("getPubDate", getPubDate);
    }
    return query.list();
  }



  /**
   * 查找总数
   * 
   * @param psnId
   * @return
   */
  public Long findCount(Long groupId) {
    String hql = "select  count(t)  from  GroupPubs t   where   t.groupId =:groupId ";
    Object object = this.createQuery(hql).setParameter("groupId", groupId).uniqueResult();
    if (object != null) {
      return NumberUtils.toLong(object.toString());
    }
    return null;
  }

  /**
   * 查找总数
   * 
   * @param psnId
   * @param getPubDate
   * @return
   */
  public Long findCount(Long groupId, Date getPubDate) {
    String hql = "select  count(t)  from  GroupPubs t   where   t.groupId =:groupId ";
    if (getPubDate != null) {
      hql += " and t.updateDate >= :getPubDate ";
    }
    Query query = this.createQuery(hql).setParameter("groupId", groupId);
    if (getPubDate != null) {
      query.setParameter("getPubDate", getPubDate);
    }

    Object object = query.uniqueResult();
    if (object != null) {
      return NumberUtils.toLong(object.toString());
    }
    return null;
  }


  public List<GroupPubs> findGroupListBy(Integer pageNo, Integer pageSize) {
    String hql =
        "select g.ownerPsnId as psnId, g.groupId, g.pubId from GroupPubs g where g.createDate>sysdate order by g.groupPubsId";
    return this.createQuery(hql).setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
  }

  /**
   * 判断成果id是否属于 该群组
   * 
   * @param pubId
   * @param groupId
   * @return
   */
  public Boolean isBelongGroupPub(Long pubId, Long groupId) {
    boolean flag = false;
    String hql = "select g.pubId  from GroupPubs  g where g.pubId=:pubId  and g.groupId=:groupId ";
    List<Object> list = this.createQuery(hql).setParameter("pubId", pubId).setParameter("groupId", groupId).list();
    if (list != null && list.size() > 0) {
      flag = true;
    }
    return flag;
  }

  /**
   * 查找成果更新的时间
   * 
   * @param pubId
   * @return
   */
  public Date findGroupPubUpdateTime(Long groupId, Long pubId) {
    Query query =
        super.createQuery(" select t.updateDate  from  GroupPubs t where   t.pubId=:pubId   and t.groupId=:groupId  ")
            .setParameter("pubId", pubId).setParameter("groupId", groupId);
    Object obj = query.uniqueResult();
    if (obj != null) {
      return (Date) obj;
    }
    return null;
  }
}
