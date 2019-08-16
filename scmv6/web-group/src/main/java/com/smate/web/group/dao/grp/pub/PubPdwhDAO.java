package com.smate.web.group.dao.grp.pub;

import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.group.model.group.pub.pdwh.PubPdwhPO;

/**
 * @description 基准库成果查询是否删除
 * @author xiexing
 * @date 2019年3月12日
 */
@Repository
public class PubPdwhDAO extends PdwhHibernateDao<PubPdwhPO, Long> {
  /**
   * 校验基准库成果是否删除
   * 
   * @param pubId
   * @return
   */
  public boolean checkPdwhIsDel(Long pubId, PubPdwhStatusEnum status) {
    String HQL = "select count(1) from PubPdwhPO t where t.pubId = :pubId and t.status = :status";
    Long count =
        (Long) getSession().createQuery(HQL).setParameter("pubId", pubId).setParameter("status", status).uniqueResult();
    return count != null && count > 0;
  }
}
