package com.smate.core.base.pub.po.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.po.PubPdwhSnsRelationPO;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * sns个人库成果ID与基准库成果ID关系Dao
 * 
 * @author YJ
 *
 *         2018年5月31日
 */
@Repository
public class PubPdwhSnsRelationDAO extends SnsHibernateDao<PubPdwhSnsRelationPO, Long> {

  public PubPdwhSnsRelationPO getByPubIdAndPdwhId(Long pubId, Long pdwhId) {
    String hql = "from PubPdwhSnsRelationPO p where p.snsPubId =:pubId and p.pdwhPubId =:pdwhId ";
    List list = this.createQuery(hql).setParameter("pubId", pubId).setParameter("pdwhId", pdwhId).list();
    if (!list.isEmpty()) {
      return (PubPdwhSnsRelationPO) list.get(0);
    }
    return null;
  }

  /**
   * 根据基准库成果ID获取关联的个人库成果ID List
   * 
   * @param pdwhPubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getSnsPubIdListByPdwhId(Long pdwhPubId) {
    String hql = "select t.snsPubId from PubPdwhSnsRelationPO t where t.pdwhPubId=:pdwhPubId ";
    return super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

  /**
   * 根据基准库成果id获取个人库成果ids
   * 
   * @param pdwhPubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getSnsPubIds(Long pdwhPubId, Long pubId) {
    String hql = "select t.snsPubId from PubPdwhSnsRelationPO t where t.pdwhPubId=:pdwhPubId and t.snsPubId !=:pubId "
        + " and exists(select 1 from PubSnsPO s where t.snsPubId=s.pubId and s.status=0)";
    return super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("pubId", pubId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getSnsPubIdsByPdwhId(Long pdwhPubId, Long snsPsnId) {
    String hql = "select t.snsPubId from PubPdwhSnsRelationPO t where "
        + "t.pdwhPubId=:pdwhPubId and t.snsPubId not in (:snsPsnId) ";
    return super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("snsPsnId", snsPsnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> findPdwhPubIdByPsnId(Long ownerPsnId) {
    String hql = "select distinct t.pdwhPubId from PubPdwhSnsRelationPO t, PubSnsPublicPO p "
        + "where t.snsPubId = p.pubId and p.createPsnId=:psnId and p.status=0 and t.pdwhPubId is not null ";
    return super.createQuery(hql).setParameter("psnId", ownerPsnId).setMaxResults(1000).list();
  }

  @SuppressWarnings("unchecked")
  public Long getPdwhPubIdBySnsPubId(Long snsPubId) {
    String hql = "select t.pdwhPubId from PubPdwhSnsRelationPO t where t.snsPubId=:snsPubId ";
    List<Long> snsPubIds = super.createQuery(hql).setParameter("snsPubId", snsPubId).list();
    if (snsPubIds != null && snsPubIds.size() > 0) {
      return snsPubIds.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public PubPdwhSnsRelationPO getPdwhPubBySnsPubId(Long snsPubId) {
    String hql = "from PubPdwhSnsRelationPO t where t.snsPubId=:snsPubId ";
    List<PubPdwhSnsRelationPO> snsPubs = super.createQuery(hql).setParameter("snsPubId", snsPubId).list();
    if (snsPubs != null && snsPubs.size() > 0) {
      return snsPubs.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPdwhPubIds(Long lastPubId, Integer size) {
    String hql = "select t.pdwhPubId from PubPdwhSnsRelationPO t where t.pdwhPubId > :lastPubId order by t.pdwhPubId";
    return super.createQuery(hql).setParameter("lastPubId", lastPubId).setMaxResults(size).list();
  }

  public void deleteRelation(Long pdwhPubId, Long snsPubId) {
    String hql = "delete from PubPdwhSnsRelationPO t where t.pdwhPubId = :pdwhPubId and t. snsPubId = :snsPubId ";
    super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("snsPubId", snsPubId).executeUpdate();
  }

  /**
   * 根据基准库成果id获取个人库公开成果ids
   * 
   * @param pdwhPubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getSnsOpenPubIds(Long pdwhPubId, Long pubId) {
    String hql = "select t.snsPubId from PubPdwhSnsRelationPO t where t.pdwhPubId=:pdwhPubId and t.snsPubId !=:pubId "
        + " and exists(select 1 from PubSnsPO s where t.snsPubId=s.pubId and s.status=0)"
        + " and not exists(select 1 from PsnConfigPub pc where pc.id.pubId=t.snsPubId and pc.anyUser <=6)";
    return super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("pubId", pubId).list();
  }

  public void delPubPdwhSnsRelation(Long scmPubId) {
    String hql = "delete from PubPdwhSnsRelationPO t where t. snsPubId = :snsPubId ";
    super.createQuery(hql).setParameter("snsPubId", scmPubId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPdwhPubIdsBySnsPubId(List<Long> psnPubIdList) {
    if (psnPubIdList != null && psnPubIdList.size() > 0) {
      if (psnPubIdList.size() <= 1000) {
        String hql = "select distinct t.pdwhPubId from PubPdwhSnsRelationPO t where t.snsPubId in ( :psnPubIdList) ";
        return super.createQuery(hql).setParameterList("psnPubIdList", psnPubIdList).list();
      } else {
        Integer counts = psnPubIdList.size() / 1000;
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        String hql = "select distinct t.pdwhPubId from PubPdwhSnsRelationPO t where t.snsPubId in ( :psnPubIdList)";
        List<Long> psnPubIds = new ArrayList<Long>(psnPubIdList.subList(0, 1000));
        parameterMap.put("psnPubIdList", psnPubIds);
        for (int i = 1; i <= counts; i++) {
          hql = hql + " or t.snsPubId in (:psnPubIdList" + i + ")";
          if (i < counts) {
            List<Long> valueList = new ArrayList<Long>(psnPubIdList.subList(i * 1000, i * 1000 + 1000));
            parameterMap.put("psnPubIdList" + i, valueList);
          } else if (i == counts) {
            List<Long> valueList = new ArrayList<Long>(psnPubIdList.subList(i * 1000, psnPubIdList.size()));
            parameterMap.put("psnPubIdList" + i, valueList);
          }
        }
        return super.createQuery(hql).setProperties(parameterMap).list();
      }
    } else {
      return null;
    }

  }

  @SuppressWarnings("unchecked")
  public List<Long> getSnsPubIdListByPdwhId(List<Long> pdwhPubIdList) {
    if (pdwhPubIdList != null && pdwhPubIdList.size() > 0) {
      if (pdwhPubIdList.size() <= 1000) {
        String hql = "select distinct t.snsPubId from PubPdwhSnsRelationPO t where t.pdwhPubId in ( :pdwhPubIdList) ";
        return super.createQuery(hql).setParameterList("pdwhPubIdList", pdwhPubIdList).list();
      } else {
        Integer counts = pdwhPubIdList.size() / 1000;
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        String hql = "select distinct t.snsPubId from PubPdwhSnsRelationPO t where t.pdwhPubId in ( :pdwhPubIdList)";
        List<Long> pdwhPubIds = new ArrayList<Long>(pdwhPubIdList.subList(0, 1000));
        parameterMap.put("pdwhPubIdList", pdwhPubIds);
        for (int i = 1; i <= counts; i++) {
          hql = hql + " or t.pdwhPubId in (:pdwhPubIdList" + i + ")";
          if (i < counts) {
            List<Long> valueList = new ArrayList<Long>(pdwhPubIdList.subList(i * 1000, i * 1000 + 1000));
            parameterMap.put("pdwhPubIdList" + i, valueList);
          } else if (i == counts) {
            List<Long> valueList = new ArrayList<Long>(pdwhPubIdList.subList(i * 1000, pdwhPubIdList.size()));
            parameterMap.put("pdwhPubIdList" + i, valueList);
          }
        }
        return super.createQuery(hql).setProperties(parameterMap).list();
      }
    } else {
      return null;
    }
  }

  public void delPubPdwhSnsRelationByPdwhPubId(Long pdwhPubId) {
    String hql = "delete from PubPdwhSnsRelationPO t where t. pdwhPubId = :pdwhPubId ";
    super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).executeUpdate();
  }

  public List<Long> findSnsPsnIds(Long pdwhPubId) {
    String hql = " select distinct p.createPsnId from PubSnsPO p where p.status=0 and "
        + " exists (select 1 from PubPdwhSnsRelationPO t where t.pdwhPubId =:pdwhPubId and t.snsPubId=p.pubId)";
    List list = this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
    return list;
  }


}
