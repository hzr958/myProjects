package com.smate.web.v8pub.dao.fulltext;

import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.model.fulltext.req.PubFullTextReqBase;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * @description 校验成果全文请求是否有过同意记录
 * @author xiexing
 * @date 2019年2月25日
 */
@Repository
public class CheckHasAgreeRecordDao extends SnsHibernateDao<PubFullTextReqBase, Long> {
  public boolean checkHasAgreeRecord(Long reqPsnId, Long pubId, PubDbEnum pubDb) {
    String HQl = "select count(1) from PubFullTextReqBase t where "
        + "t.reqPsnId = :reqPsnId and t.pubId = :pubId and t.pubDb = :pubDb and t.status in (1,3)";
    Long count = (Long) getSession().createQuery(HQl).setParameter("reqPsnId", reqPsnId).setParameter("pubId", pubId)
        .setParameter("pubDb", pubDb).uniqueResult();
    return count != null && count > 0;
  }
}
