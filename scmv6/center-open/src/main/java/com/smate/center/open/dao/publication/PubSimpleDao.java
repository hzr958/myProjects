package com.smate.center.open.dao.publication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.publication.PubSimple;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * V_PUB_SIMPLE表实体Dao
 * 
 * @author hzr
 * 
 */
@Repository
public class PubSimpleDao extends SnsHibernateDao<PubSimple, Long> {

  /**
   * 传入时间检查成果
   * 
   * @param pubId
   * @param importDate
   * @return
   */
  public PubSimple checkPubNewest(Long pubId, Date importDate) {
    StringBuilder hql = new StringBuilder(
        "select  new PubSimple(t.pubId,t.updateDate)  from PubSimple t where   t.pubId=:pubId and t.status!=1 ");

    if (importDate != null) {
      hql.append("  and  t.updateDate >= :importDate ");
    }
    Query query = super.createQuery(hql.toString()).setParameter("pubId", pubId).setParameter("importDate", importDate);
    Object obj = query.uniqueResult();
    if (obj != null) {
      return (PubSimple) obj;
    }
    return null;

  }

  /**
   * 通过psnId 查找 pubIds
   * 
   * @author ajb
   */
  @SuppressWarnings("unchecked")
  public List<Long> findPubIdListByPsnId(Long psnId, Integer pageNo, Integer pageSize) {
    String hql = "select   t.id  from PubSimple t where t.status=0 and t.articleType=1 and t.ownerPsnId=:psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).setFirstResult((pageNo - 1) * pageSize)
        .setMaxResults(pageSize).list();
  }

  /**
   * 查找总数
   * 
   * @param psnId
   * @return
   */
  public Long findCount(Long psnId) {
    Query query = super.createQuery(
        "select count(t.pubId)  from PubSimple t where t.status=:status and t.articleType=:articleType and  t.ownerPsnId=:psnId")
            .setParameter("status", 0).setParameter("articleType", 1).setParameter("psnId", psnId);
    Object obj = query.uniqueResult();
    if (obj != null) {
      return NumberUtils.toLong(obj.toString());
    }
    return 0L;
  }

  /**
   * 查找总数
   * 
   * @param psnId
   * @return
   */
  public List<PubSimple> findAllPubSimple(Long psnId) {
    Query query = super.createQuery(
        "from PubSimple t where t.status=:status and t.articleType=:articleType and  t.ownerPsnId=:psnId")
            .setParameter("status", 0).setParameter("articleType", 1).setParameter("psnId", psnId);
    Object obj = query.list();
    if (obj != null) {
      return (List<PubSimple>) obj;
    }
    return null;
  }

  /**
   * 通过成果类型，查找总数
   * 
   * @param psnId
   * @return
   */
  public Long findCountWithPubType(Long psnId, Integer pubType) {
    Query query = super.createQuery(
        "select count(t.pubId)  from PubSimple t where t.status=:status and t.articleType=:articleType and t.pubType=:pubType  and  t.ownerPsnId=:psnId")
            .setParameter("status", 0).setParameter("articleType", 1).setParameter("psnId", psnId)
            .setParameter("pubType", pubType);
    Object obj = query.uniqueResult();
    if (obj != null) {
      return NumberUtils.toLong(obj.toString());
    }
    return 0L;
  }

  /**
   * 通过psnId 查找 pubIds
   * 
   * @param pubTypeList 兼容成果类型
   * @author ajb
   */
  @SuppressWarnings("unchecked")
  public List<PubSimple> findPubListByPsnId(Long psnId, Integer pageNo, Integer pageSize,
      ArrayList<Integer> pubTypeList, Date getPubDate) {

    String hql =
        "select    new PubSimple(t.pubId , t.status)  from PubSimple t where  t.articleType=1 and t.ownerPsnId=:psnId";
    if (pubTypeList != null && pubTypeList.size() > 0) {
      hql += "  and  t.pubType in (:pubTypeList)";
    }
    if (getPubDate != null) {
      hql += "  and  t.updateDate >= :getPubDate ";
      // 第N次获取成果是，获取正常加改动的
      hql += " and 	t.status in ( 0 ,1,5 )  ";
    } else {
      // 第一次获取成果是，获取正常的
      hql += "  and 	t.status in ( 0  )  ";
    }
    Query query = super.createQuery(hql).setParameter("psnId", psnId).setFirstResult((pageNo - 1) * pageSize)
        .setMaxResults(pageSize);

    if (getPubDate != null) {
      query.setParameter("getPubDate", getPubDate);
    }
    if (pubTypeList != null && pubTypeList.size() > 0) {
      query.setParameterList("pubTypeList", pubTypeList);
    }
    return query.list();
  }

  /**
   * 通过psnId 查找 pubIds
   * 
   * @param pubTypeList 兼容成果类型
   * @author tsz
   */
  @SuppressWarnings("unchecked")
  public List<Long> findPubIdListByPsnId(Long psnId, Integer pageNo, Integer pageSize, ArrayList<Integer> pubTypeList,
      Date getPubDate) {

    String hql = "select    t.pubId   from PubSimple t where   t.articleType=1 and t.ownerPsnId=:psnId ";
    if (pubTypeList != null && pubTypeList.size() > 0) {
      hql += "  and  t.pubType in (:pubTypeList)";
    }
    if (getPubDate != null) {
      hql += "  and  t.updateDate >= :getPubDate ";
      // 第N次获取成果是，获取正常加改动的
      hql += " and 	t.status in ( 0 ,1,5 )  ";
    } else {
      // 第一次获取成果是，获取正常的
      hql += "  and 	t.status in ( 0  )  ";
    }
    Query query = super.createQuery(hql).setParameter("psnId", psnId).setFirstResult((pageNo - 1) * pageSize)
        .setMaxResults(pageSize);

    if (getPubDate != null) {
      query.setParameter("getPubDate", getPubDate);
    }
    if (pubTypeList != null && pubTypeList.size() > 0) {
      query.setParameterList("pubTypeList", pubTypeList);
    }
    return query.list();
  }

  /**
   * 通过psnId 查找 pubIds
   * 
   * @param pubTypeList 兼容成果类型
   * @author tsz
   */
  @SuppressWarnings("unchecked")
  public List<Long> findPubIdListByPsnIdList(List<Long> psnIdList, Integer pageNo, Integer pageSize,
      ArrayList<Integer> pubTypeList, Date getPubDate) {

    String hql = "select    t.pubId   from PubSimple t where   t.articleType=1 and t.ownerPsnId  in ( :psnIdList) ";
    if (pubTypeList != null && pubTypeList.size() > 0) {
      hql += "  and  t.pubType in (:pubTypeList)";
    }
    if (getPubDate != null) {
      hql += "  and  t.updateDate >= :getPubDate ";
      // 第N次获取成果是，获取正常加改动的
      hql += " and 	t.status in ( 0 ,1,5 )  ";
    } else {
      // 第一次获取成果是，获取正常的
      hql += "  and 	t.status in ( 0  )  ";
    }
    Query query = super.createQuery(hql).setParameterList("psnIdList", psnIdList)
        .setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);

    if (getPubDate != null) {
      query.setParameter("getPubDate", getPubDate);
    }
    if (pubTypeList != null && pubTypeList.size() > 0) {
      query.setParameterList("pubTypeList", pubTypeList);
    }
    return query.list();
  }

  /**
   * 通过psnId 查找 pubIds
   * 
   * @param pubTypeList 兼容成果类型
   * 
   * @param excludePubIds排除的成果id
   * @author tsz
   */
  @SuppressWarnings("unchecked")
  public List<Long> findPubIdListByPsnIdList(List<Long> psnIdList, Integer pageNo, Integer pageSize,
      ArrayList<Integer> pubTypeList, Date getPubDate, Long ownPsnId, List<Long> excludePubIds) {

    String hql = "select    t.pubId   from PubSimple t where   t.articleType=1 and t.ownerPsnId  in ( :psnIdList) ";
    if (pubTypeList != null && pubTypeList.size() > 0) {
      hql += "  and  t.pubType in (:pubTypeList)";
    }
    // ---------权限---start
    if (ownPsnId == null || ownPsnId == 0L) {
      // 没传真实的openId，查询公开的成果
      hql +=
          " and  exists  ( select   pb.id.pubId  from PsnConfig pc , PsnConfigPub pb where  pc.psnId in(  :psnIdList ) and  pb.id.cnfId =pc.cnfId and pb.id.pubId = t.pubId and pb.anyUser = 7   ) ";
    } else {
      // 传了真实的openId获取成果。
    }
    if (getPubDate != null) {
      hql += "  and  t.updateDate >= :getPubDate ";
      // 第N次获取成果是，获取正常加改动的
      hql += " and 	t.status in ( 0 ,1,5 )  ";
    } else {
      // 第一次获取成果是，获取正常的
      hql += "  and 	t.status in ( 0  )  ";
    }
    // 排除的成果id
    if (excludePubIds != null && excludePubIds.size() > 0) {
      hql += " and  t.pubId not in (:excludePubIds )  ";
    }
    Query query = super.createQuery(hql).setParameterList("psnIdList", psnIdList)
        .setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);

    if (getPubDate != null) {
      query.setParameter("getPubDate", getPubDate);
    }
    if (pubTypeList != null && pubTypeList.size() > 0) {
      query.setParameterList("pubTypeList", pubTypeList);
    }
    if (excludePubIds != null && excludePubIds.size() > 0) {
      query.setParameterList("excludePubIds", excludePubIds);
    }
    return query.list();
  }

  /**
   * 查找总数
   * 
   * @param psnId
   * @param pubTypeList 兼容成果类型
   * @return
   */
  public Long findCount(Long psnId, ArrayList<Integer> pubTypeList, Date getPubDate) {
    String hql = "select count(t.pubId)  from PubSimple t where    t.articleType=:articleType and  t.ownerPsnId=:psnId";
    if (pubTypeList != null && pubTypeList.size() > 0) {
      hql += "  and  t.pubType in (:pubTypeList)";
    }

    if (getPubDate != null) {
      hql += "  and  t.updateDate >= :getPubDate ";
      // 第N次获取成果是，获取正常加改动的
      hql += " and 	t.status in (0 , 1 , 5)  ";
    } else {
      // 第一次获取成果是，获取正常的
      hql += " and  t.status in (0)  ";
    }
    Query query = super.createQuery(hql).setParameter("articleType", 1).setParameter("psnId", psnId);
    if (getPubDate != null) {
      query.setParameter("getPubDate", getPubDate);
    }

    if (pubTypeList != null && pubTypeList.size() > 0) {
      query.setParameterList("pubTypeList", pubTypeList);

    }

    Object obj = query.uniqueResult();
    if (obj != null) {
      return NumberUtils.toLong(obj.toString());
    }
    return 0L;
  }

  /**
   * 查找总数
   * 
   * @param psnId
   * @param pubTypeList 兼容成果类型
   * @return
   */
  public Long findCountByPsnIdList(List<Long> psnIdList, ArrayList<Integer> pubTypeList, Date getPubDate) {
    String hql = "select count(t.pubId)  from PubSimple t "
        + "where    t.articleType=:articleType and  t.ownerPsnId  in ( :psnIdList )"
        + "exists  ( select    from PsnConfig pc , PsnConfigPub pb where  pc.psnId in(  :psnIdList ) and  pb.id.cnfId =pc.cnfId and pb.id.pubId = t.pubId and pb.   )";
    if (pubTypeList != null && pubTypeList.size() > 0) {
      hql += "  and  t.pubType in (:pubTypeList)";
    }

    if (getPubDate != null) {
      hql += "  and  t.updateDate >= :getPubDate ";
      // 第N次获取成果是，获取正常加改动的
      hql += " and 	t.status in (0 , 1 , 5)  ";
    } else {
      // 第一次获取成果是，获取正常的
      hql += " and  t.status in (0)  ";
    }
    Query query = super.createQuery(hql).setParameter("articleType", 1).setParameterList("psnIdList", psnIdList);
    if (getPubDate != null) {
      query.setParameter("getPubDate", getPubDate);
    }

    if (pubTypeList != null && pubTypeList.size() > 0) {
      query.setParameterList("pubTypeList", pubTypeList);

    }

    Object obj = query.uniqueResult();
    if (obj != null) {
      return NumberUtils.toLong(obj.toString());
    }
    return 0L;
  }

  /**
   * 查找总数 2017-09-08
   * 
   * @param psnId
   * @param pubTypeList 兼容成果类型
   * @return
   */
  public Long findCountByPsnIdList(List<Long> psnIdList, ArrayList<Integer> pubTypeList, Date getPubDate, Long ownPsnId,
      List<Long> excludePubIds) {
    String hql = "select count(t.pubId)  from PubSimple t "
        + "where    t.articleType=:articleType and  t.ownerPsnId  in ( :psnIdList )";
    // ---------权限---start
    if (ownPsnId == null || ownPsnId == 0L) {
      // 没传真实的openId，查询公开的成果
      hql +=
          " and  exists  ( select   pb.id.pubId  from PsnConfig pc , PsnConfigPub pb where  pc.psnId in(  :psnIdList ) and  pb.id.cnfId =pc.cnfId and pb.id.pubId = t.pubId and pb.anyUser = 7   ) ";
    } else {
      // 传了真实的openId获取成果。
    }
    // ---------权限---end
    if (pubTypeList != null && pubTypeList.size() > 0) {
      hql += "  and  t.pubType in (:pubTypeList)";
    }

    if (getPubDate != null) {
      hql += "  and  t.updateDate >= :getPubDate ";
      // 第N次获取成果是，获取正常加改动的
      hql += " and 	t.status in (0 , 1 , 5)  ";
    } else {
      // 第一次获取成果是，获取正常的
      hql += " and  t.status in (0)  ";
    }
    if (excludePubIds != null && excludePubIds.size() > 0) {
      hql += " and  t.pubId not in (:excludePubIds )  ";
    }
    Query query = super.createQuery(hql).setParameter("articleType", 1).setParameterList("psnIdList", psnIdList);
    if (getPubDate != null) {
      query.setParameter("getPubDate", getPubDate);
    }

    if (pubTypeList != null && pubTypeList.size() > 0) {
      query.setParameterList("pubTypeList", pubTypeList);

    }
    if (excludePubIds != null && excludePubIds.size() > 0) {
      query.setParameterList("excludePubIds", excludePubIds);
    }

    Object obj = query.uniqueResult();
    if (obj != null) {
      return NumberUtils.toLong(obj.toString());
    }
    return 0L;
  }

  /**
   * 查找总数
   * 
   * @param psnId
   * @return
   */
  public List<Long> handlePubIdList(List<Long> pubIdList) {

    List<Long> pubIdListNew = new ArrayList<Long>();
    if (pubIdList == null || pubIdList.size() == 0)
      return pubIdList;

    for (int i = 0; i < pubIdList.size(); i++) {
      Long pubId = pubIdList.get(i);
      Query query = super.createQuery("select t.pubId from PubSimple t where  t.pubId=:pubId  and  t.status!=:status ")
          .setParameter("status", 1).setParameter("pubId", pubId);
      Object obj = query.uniqueResult();
      if (obj != null) {
        pubIdListNew.add(pubId);
      }
    }

    return pubIdListNew;
  }

  /**
   * 判断成果id是否属于自己
   * 
   * @param pubId
   * @return
   */
  public PubSimple findPubById(Long pubId) {
    Query query = super.createQuery(
        " select new PubSimple(t.pubId ,t.ownerPsnId , t.status , t.updateDate)  from  PubSimple t where   t.pubId=:pubId   ")
            .setParameter("pubId", pubId);
    Object obj = query.uniqueResult();
    if (obj != null) {
      return (PubSimple) obj;
    }
    return null;
  }

  /**
   * 查找成果更新的时间
   * 
   * @param pubId
   * @return
   */
  public Date findPubUpdateTime(Long pubId) {
    Query query = super.createQuery(" select t.updateDate  from  PubSimple t where   t.pubId=:pubId   ")
        .setParameter("pubId", pubId);
    Object obj = query.uniqueResult();
    if (obj != null) {
      return (Date) obj;
    }
    return null;
  }
}
