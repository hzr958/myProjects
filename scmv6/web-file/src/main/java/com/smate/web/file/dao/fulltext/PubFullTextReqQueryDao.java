package com.smate.web.file.dao.fulltext;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.enums.PubFullTextReqStatusEnum;
import com.smate.core.base.pub.model.fulltext.req.PubFullTextReqBase;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 全文请求记录查询DAO
 * 
 * @author houchuanjie
 *
 */
@Repository
public class PubFullTextReqQueryDao extends SnsHibernateDao<PubFullTextReqBase, Serializable> {
  /**
   * 查询请求人请求的成果是否被同意
   *
   * @author houchuanjie
   * @date 2017年11月29日 下午6:37:06
   * @param pubId 请求的成果id
   * @param reqPsnId 请求人
   * @param dbEnum 成果所属
   * @return 请求人请求的成果是否被同意返回true，否则返回false
   */
  public boolean isFullTextReqAgree(Long pubId, Long reqPsnId, PubDbEnum dbEnum) {
    String hql =
        "select count(1) from PubFullTextReqBase t where t.status in (:status) and t.pubId = :pubId and t.pubDb = :pubDb and t.reqPsnId = :reqPsnId";
    // 同意上传全文和直接点击上传全文都会认为是已经同意
    Long count = (Long) super.createQuery(hql)
        .setParameterList("status", new Object[] {PubFullTextReqStatusEnum.AGREE, PubFullTextReqStatusEnum.UPLOAD})
        .setParameter("pubId", pubId).setParameter("pubDb", dbEnum).setParameter("reqPsnId", reqPsnId).uniqueResult();
    return count > 0 ? true : false;
  }
}
