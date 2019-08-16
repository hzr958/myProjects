package com.smate.center.open.dao.rcmd.pub;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.rcmd.pub.PublicationConfirm;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * 成果提交持久层.
 * 
 * @author LY
 * 
 */
@Repository
public class PublicationConfirmDao extends RcmdHibernateDao<PublicationConfirm, Long> {

  private List list;

  /**
   * 获取成果确认的rolPubId , 和已经认领的成果
   * 
   * @param psnId
   * @param pageNo
   * @param pageSize
   * @param getPubDate
   * @return
   */
  public List<Object[]> queryPubConfirmListByPsnIdList(List<Long> psnIdList, int pageNo, int pageSize,
      Date getPubDate) {
    String hql = " select     t.rolPubId   ,  p.psnId  , p.id   " + " from PubConfirmRolPub t ,PublicationConfirm p "
        + " where t.rolPubId = p.rolPubId " + "	and p.confirmResult  = 0 " + " and p.psnId  in  (  :psnIdList ) ";
    if (getPubDate != null) {
      hql += "  and   t.updateDate >=:getPubDate     ";
    }
    hql += "order by t.rolPubId  asc ";
    Query query = super.createQuery(hql).setParameterList("psnIdList", psnIdList);
    if (getPubDate != null) {
      query.setParameter("getPubDate", getPubDate);
    }
    List<Object[]> pubConfirmList = null;
    pubConfirmList = query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
    return pubConfirmList;
  }

  /**
   * 已经认领的成果的总数
   * 
   * @param psnId
   * @param pageNo
   * @param pageSize
   * @param getPubDate
   * @return
   */
  public List<Object[]> queryPubConfirmHiListByPsnIdList(List<Long> psnIdList, Date getPubDate) {
    String hql = " select     t.rolPubId   ,  p.psnId  , p.id   " + " from PubConfirmRolPub t ,PublicationConfirmHi p "
        + " where t.rolPubId = p.rolPubId " + "	and p.confirmResult  = 1 " + " and p.psnId  in  (  :psnIdList ) ";
    if (getPubDate != null) {
      hql += "  and    p.confirmDate >=:getPubDate   ) ";
    }
    hql += "order by t.rolPubId  asc ";
    Query query = super.createQuery(hql).setParameterList("psnIdList", psnIdList);
    if (getPubDate != null) {
      query.setParameter("getPubDate", getPubDate);
    }
    List<Object[]> pubConfirmList = null;
    pubConfirmList = query.list();
    return pubConfirmList;
  }

  /**
   * 成果认领的总数和历史认领总数
   * 
   * @param psnId
   */
  public Long queryPubConfirmCountByPsnIdList(List<Long> psnIdList, Date getPubDate) {
    String countHql = " select count(1) " + " from PubConfirmRolPub t ,PublicationConfirm p "
        + " where     t.rolPubId = p.rolPubId " + " and p.confirmResult = 0  " + " and p.psnId in (:psnIdList ) ";

    if (getPubDate != null) {
      countHql += "  and   t.updateDate >=:getPubDate   ) ";
    }
    Query query = super.createQuery(countHql).setParameterList("psnIdList", psnIdList);
    if (getPubDate != null) {
      query.setParameter("getPubDate", getPubDate);
    }
    return (Long) query.uniqueResult();
  }

  /**
   * 成果认领的总数和历史认领总数
   * 
   * @param psnId
   */
  public Long queryPubConfirmHiCountByPsnIdList(List<Long> psnIdList, Date getPubDate) {
    String countHql = " select count(1) " + " from PublicationConfirmHi p " + " where    p.confirmResult = 1  "
        + " and p.psnId in (:psnIdList ) ";
    if (getPubDate != null) {
      countHql += "  and   p.confirmDate >=:getPubDate   ) ";
    }
    Query query = super.createQuery(countHql).setParameterList("psnIdList", psnIdList);
    if (getPubDate != null) {
      query.setParameter("getPubDate", getPubDate);
    }
    return (Long) query.uniqueResult();
  }


  public Long getInsId(Long psnId, Long rolPubId) {
    String hql = " select   p.insId  from  PublicationConfirm p where  p.psnId=:psnId and p.rolPubId=:rolPubId   ";
    Object obj = this.createQuery(hql).setParameter("psnId", psnId).setParameter("rolPubId", rolPubId).uniqueResult();
    if (obj != null) {
      return (Long) obj;
    }
    return null;
  }

  public Long getPsnIdByPubId(Long rolPubId) {
    String hql = " select   p.psnId  from  PublicationConfirm p where  p.rolPubId=:rolPubId   ";
    Object obj = this.createQuery(hql).setParameter("rolPubId", rolPubId).uniqueResult();
    if (obj != null) {
      return (Long) obj;
    }
    return null;
  }

  public Long getPsnIdById(Long id) {
    String hql = " select   p.psnId  from  PublicationConfirm p where  p.id=:id   ";
    Object obj = this.createQuery(hql).setParameter("id", id).uniqueResult();
    if (obj != null) {
      return (Long) obj;
    }
    return null;
  }

  /**
   * 通过主键，查询历史认领成果表确认状态
   * 
   * @param id
   * @return
   */
  public Integer getConfirmResultByPId(Long id) {
    String hql = " select   p.confirmResult  from  PublicationConfirmHi p where  p.id=:id   ";
    Object obj = this.createQuery(hql).setParameter("id", id).uniqueResult();
    if (obj != null) {
      return (Integer) obj;
    }
    return null;
  }

  /**
   * 查询需要确认的成果总数.
   * 
   * @param psnId
   * @return
   */
  public Long queryPubConfirmCount(Long psnId) {

    String hql =
        "select count(p.id) from PubConfirmRolPub t,PublicationConfirm p where t.rolPubId=p.rolPubId and p.confirmResult=0 and p.psnId=?";

    return super.findUnique(hql, psnId);

  }

  /**
   * 查询所有的为认领的成果
   * 
   * @param psnId
   */
  public List<Object[]> queryAllPubConfirm(Long psnId) {
    String countHql =
        "  select     t.rolPubId   ,  p.psnId  , p.id   " + " from PubConfirmRolPub t ,PublicationConfirm p "
            + " where     t.rolPubId = p.rolPubId " + " and p.confirmResult = 0  " + " and p.psnId =:psnId ";
    Query query = super.createQuery(countHql).setParameter("psnId", psnId);
    return (List<Object[]>) query.list();
  }

}
