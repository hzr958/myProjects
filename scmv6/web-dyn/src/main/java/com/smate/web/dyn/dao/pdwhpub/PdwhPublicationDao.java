package com.smate.web.dyn.dao.pdwhpub;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.dyn.model.pdwhpub.PdwhPublication;

/**
 * 基准库成果dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhPublicationDao extends PdwhHibernateDao<PdwhPublication, Long> {
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Map getBriefDesc(Long pubId) {
    String hql =
        "select new Map(t.pubId as pubId, t.dbId as dbid, t.zhBriefDesc as briefDescZh, t.enBriefDesc as briefDescEn) from PdwhPublication t where t.pubId=? ";
    return (Map<String, Object>) findUnique(hql, pubId);
  }

  /**
   * 通过ID获取成果psnID.
   * 
   * @param pubId
   * @return
   * @throws DaoException
   */
  public PdwhPublication getPdwhPubPsnIdOrStatus(Long pubId) {
    String hql = " from PdwhPublication t where t.pubId=? ";
    return this.findUnique(hql, pubId);

  }

  public PdwhPublication getPdwhPubShare(Long pubId) {
    String hql =
        " select new PdwhPublication(t.pubId,t.zhTitle,t.enTitle,t.authorName,t.zhBriefDesc,t.enBriefDesc,t.pubYear) from  PdwhPublication t where t.pubId=? ";
    return this.findUnique(hql, pubId);
  }

}
