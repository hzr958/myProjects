package com.smate.web.v8pub.dao.sns;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.enums.PubFullTextReqStatusEnum;
import com.smate.core.base.pub.model.fulltext.req.PubFullTextReqBase;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 全文请求记录DAO
 * 
 * @author houchuanjie
 *
 */
@Repository
public class PubFullTextReqBaseDao extends SnsHibernateDao<PubFullTextReqBase, Serializable> {

  /**
   * 查询请求人请求的成果是否被同意
   *
   * @author houchuanjie
   * @date 2017年12月12日 上午10:51
   * @param pubId 请求的成果id
   * @param reqPsnId 请求人
   * @param pubDb TODO
   * @return 请求人请求的成果是否被同意返回true，否则返回false
   */
  public boolean isFullTextReqAgree(Long pubId, Long reqPsnId, PubDbEnum pubDb) {
    String hql =
        "select count(1) from PubFullTextReqBase t where t.status in(:status) and t.pubId = :pubId and t.pubDb = :pubDb and t.reqPsnId = :reqPsnId";
    // 同意上传全文和直接点击上传全文都会认为是已经同意
    Long count = (Long) super.createQuery(hql)
        .setParameterList("status", new Object[] {PubFullTextReqStatusEnum.AGREE, PubFullTextReqStatusEnum.UPLOAD})
        .setParameter("pubDb", pubDb).setParameter("pubId", pubId).setParameter("reqPsnId", reqPsnId).uniqueResult();
    return count > 0 ? true : false;
  }

  /**
   * 根据reqId获取未被处理的请求记录
   *
   * @author houchuanjie
   * @date 2017年12月14日 上午10:59:47
   * @param reqId
   * @return
   */
  public PubFullTextReqBase getUnprocessed(Long reqId) {
    String hql = "select r from PubFullTextReqBase r where r.reqId = ? and r.status = ?";
    PubFullTextReqBase lastReqBase = (PubFullTextReqBase) findUnique(hql, reqId, PubFullTextReqStatusEnum.UNPROCESSED);
    return lastReqBase;
  }

}
