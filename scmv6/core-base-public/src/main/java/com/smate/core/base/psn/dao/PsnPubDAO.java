package com.smate.core.base.psn.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.PsnPubPO;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.number.NumberUtils;

/**
 * 个人成果关系dao
 * 
 * @author yhx
 * @date 2018年5月31日
 */

@Repository
public class PsnPubDAO extends SnsHibernateDao<PsnPubPO, Long> {

  public PsnPubPO getPsnPub(Long pubId, Long ownerPsnId) {
    String hql = "from PsnPubPO where pubId=:pubId and ownerPsnId=:ownerPsnId";
    return (PsnPubPO) super.createQuery(hql).setParameter("pubId", pubId).setParameter("ownerPsnId", ownerPsnId)
        .uniqueResult();
  }

  /**
   * 根据psnId获取用户拥有的所有成果关系对象
   * 
   * @param psnId
   */
  @SuppressWarnings("unchecked")
  public List<PsnPubPO> getPubsByPsnId(Long psnId) {
    String hql = "from PsnPubPO where ownerPsnId=:psnId and status = 0";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 获取成果拥有者
   * 
   * @param pubId
   * @return
   */
  public Long getPsnOwner(Long pubId) {
    String hql = "select ownerPsnId from PsnPubPO where pubId=:pubId";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  /**
   * 获取成果拥有者
   * 
   * @param pubId
   * @return
   */
  public Long getPubOwner(Long pubId) {
    String hql = "select ownerPsnId from PsnPubPO where pubId=:pubId";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  /**
   * 更新个人与成果关系表状态为删除在状态
   * 
   * @param psnId
   * @param pubId
   */
  public void updateStatus(Long psnId, Long pubId) {
    String hql =
        "update PsnPubPO t set t.status = 1,t.gmtModified=:gmtModified where t.ownerPsnId=:psnId and t.pubId= :pubId";
    super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubId", pubId)
        .setParameter("gmtModified", new Date()).executeUpdate();

  }

  /**
   * 获取成果拥有者
   * 
   * @param pubId
   * @return
   */
  public int findPsnPubStatus(Long pubId) {
    String hql = "select status from PsnPubPO where pubId=:pubId";
    return (Integer) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPubIdsByPsnId(Long psnId) {
    String hql = "select pubId from PsnPubPO where ownerPsnId=:psnId and status = 0 order by pubId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Integer> getPubStatus(Long pubId) {
    String hql = "select t.status from PsnPubPO t where t.pubId =:pubId and t.status = 0";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }

  /**
   * 更新成果的更新时间
   * 
   * @param pubId
   * @param gmtModified
   * @return
   */
  public Integer updatePubDate(Long pubId, Date gmtModified) {
    String hql = "update PsnPubPO p set p.gmtModified =:gmtModified  where p.pubId=:pubId ";
    String hql2 = "update PubSnsPO p set p.gmtModified =:gmtModified  where p.pubId=:pubId ";
    this.createQuery(hql).setParameter("gmtModified", gmtModified).setParameter("pubId", pubId).executeUpdate();
    return this.createQuery(hql2).setParameter("gmtModified", gmtModified).setParameter("pubId", pubId).executeUpdate();
  }

  public void deleteAll(Long pubId) {
    String hql = "delete from PsnPubPO t where t.pubId=:pubId";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public Integer getStatus(Long pubId) {
    String hql = "select t.status from PsnPubPO t where t.pubId =:pubId and t.status = 0";
    List<Integer> staList = super.createQuery(hql).setParameter("pubId", pubId).list();
    if (staList != null && staList.size() > 0) {
      return staList.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPubOwnerPsnIds(List<Long> snsPubIdList, Long psnId) {
    String hql =
        "select distinct(ownerPsnId) from PsnPubPO where pubId in (:pubIds)  and ownerPsnId != :psnId and  status = 0";
    return super.createQuery(hql).setParameterList("pubIds", snsPubIdList).setParameter("psnId", psnId).list();
  }

  /**
   * 通过成果id来获取对应的所属者id
   * 
   * @param pubId
   * @return
   */
  @Transactional
  public Long getOwnerPsnId(Long pubId) {
    if (NumberUtils.isNullOrZero(pubId)) {
      return 0L;
    }
    String HQL = "select ownerPsnId from PsnPubPO t where t.pubId = :pubId";
    return (Long) getSession().createQuery(HQL).setParameter("pubId", pubId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Long> gethandlePsnId(Long lastPsnId, int batchSize) {
    String hql =
        "select distinct(ownerPsnId) from PsnPubPO where status = 0 and ownerPsnId > :lastPsnId order by ownerPsnId ";
    return super.createQuery(hql).setParameter("lastPsnId", lastPsnId).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPubsOwner(List<Long> snsPubIdList) {
    if (snsPubIdList != null && snsPubIdList.size() > 0) {
      if (snsPubIdList.size() <= 1000) {
        String hql = "select distinct(ownerPsnId) from PsnPubPO where status = 0 and pubId in (:snsPubIdList)";
        return super.createQuery(hql).setParameterList("snsPubIdList", snsPubIdList).list();
      } else {
        Integer counts = snsPubIdList.size() / 1000;
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        String hql = "select distinct(ownerPsnId) from PsnPubPO where status = 0 and pubId in (:snsPubIdList)";
        List<Long> snsPubIds = new ArrayList<Long>(snsPubIdList.subList(0, 1000));
        parameterMap.put("snsPubIdList", snsPubIds);
        for (int i = 1; i <= counts; i++) {
          hql = hql + " or t.pubId in (:snsPubIdList" + i + ")";
          if (i < counts) {
            List<Long> valueList = new ArrayList<Long>(snsPubIdList.subList(i * 1000, i * 1000 + 1000));
            parameterMap.put("snsPubIdList" + i, valueList);
          } else if (i == counts) {
            List<Long> valueList = new ArrayList<Long>(snsPubIdList.subList(i * 1000, snsPubIdList.size()));
            parameterMap.put("snsPubIdList" + i, valueList);
          }
        }
        return super.createQuery(hql).setProperties(parameterMap).list();
      }
    } else {
      return null;
    }



  }
}
