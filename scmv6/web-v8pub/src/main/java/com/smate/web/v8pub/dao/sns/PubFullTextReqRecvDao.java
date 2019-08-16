package com.smate.web.v8pub.dao.sns;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.enums.PubFullTextReqStatusEnum;
import com.smate.core.base.pub.model.fulltext.req.PubFullTextReqRecv;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 全文请求接收DAO
 * 
 * @author houchuanjie
 *
 */
@Repository
public class PubFullTextReqRecvDao extends SnsHibernateDao<PubFullTextReqRecv, Serializable> {
  /**
   * 根据msgId和pubId查找全文请求未处理的记录
   *
   * @author houchuanjie
   * @date 2017年12月14日 上午10:53:26
   * @param msgId
   * @param pubId
   * @return
   */
  public PubFullTextReqRecv getUnprocessed(Long msgId, Long pubId) {
    String hql = "select r from PubFullTextReqRecv r where r.pubId = ? and r.msgId = ? and r.status = ?";
    PubFullTextReqRecv lastReqRecv =
        (PubFullTextReqRecv) findUnique(hql, pubId, msgId, PubFullTextReqStatusEnum.UNPROCESSED);
    return lastReqRecv;
  }

}
