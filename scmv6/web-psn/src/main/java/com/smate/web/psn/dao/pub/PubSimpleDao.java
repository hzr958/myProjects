package com.smate.web.psn.dao.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.pub.PubSimple;

/**
 * 简化成果表DAO
 * 
 * @author wsn
 *
 */
@Repository
public class PubSimpleDao extends SnsHibernateDao<PubSimple, Long> {

  /**
   * 获取pubsimple的list
   * 
   * @param pubIds
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PubSimple> getPubSimpleListByPubIds(List<Long> pubIds) throws DaoException {
    String hql = " from PubSimple t where t.pubId in (:pubIds) and t.status=0";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).list();
  }

  /**
   * 查找待查找成果是否全部属于对应的人员
   * 
   * @param pubIds
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Long getPsnOwnerPubCount(List<Long> pubIds, Long psnId) throws DaoException {
    String hql = "select count(1) from PubSimple t where t.pubId in (:pubIds) and t.status=0 and t.ownerPsnId = :psnId";
    return (Long) super.createQuery(hql).setParameterList("pubIds", pubIds).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 是否有成果配置信息丢失-------- 由于初始化成果配置的地方的逻辑是从publication表找的 所以为了统一，就不要从v_pub_simple找了
   * 
   * @param psnId
   * @param cnfId
   * @return
   */
  public boolean hasPsnConfigPubLost(Long psnId, Long cnfId) {
    String hql =
        "select count(1) from PubSimple t where t.articleType = 1 and t.status=0 and t.ownerPsnId = :psnId and not exists(select 1 from PsnConfigPub p where p.id.cnfId = :cnfId and p.id.pubId = t.pubId)";
    Long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("cnfId", cnfId).uniqueResult();
    if (count != null && count > 0) {
      return true;
    }
    return false;
  }
}
