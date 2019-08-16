package com.smate.web.v8pub.dao.sns;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.PrjPubAssignLog;
import com.smate.web.v8pub.service.query.PubQueryDTO;

/**
 * 项目成果指派记录表dao
 * 
 * @author yhx
 * @date 2019年8月9日
 *
 */
@Repository
public class PrjPubAssignLogDao extends SnsHibernateDao<PrjPubAssignLog, Serializable> {

  @SuppressWarnings("unchecked")
  public void queryPrjPubConfirmIdList(PubQueryDTO pubQueryDTO) {
    String hql =
        "select t.pubId from PrjPubAssignLog t  where t.confirmResult = 0 and t.prjId = :prjId order by t.pubId desc";
    Query query = super.createQuery(hql).setParameter("prjId", pubQueryDTO.getSearchPrjId());
    String countHql = "select count( t.pubId) from PrjPubAssignLog t where t.confirmResult=0 and t.prjId = :prjId";
    Long totalCount =
        (Long) super.createQuery(countHql).setParameter("prjId", pubQueryDTO.getSearchPrjId()).uniqueResult();
    pubQueryDTO.setTotalCount(totalCount);
    if (pubQueryDTO.getIsAll() == 1) {// 不分页查询 一次全部查出所有
      List list = query.setMaxResults(400).list();
      pubQueryDTO.setPubIds(list);
    } else {
      List list = query.setFirstResult(pubQueryDTO.getFirst() - pubQueryDTO.getConfirmCount())
          .setMaxResults(pubQueryDTO.getPageSize()).list();
      pubQueryDTO.setPubIds(list);
    }
  }
}
