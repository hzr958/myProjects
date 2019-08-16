package com.smate.web.v8pub.dao.pdwh;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.pdwh.PdwhPubFullTextPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;

/**
 * 基准库成果全文Dao
 * 
 * @author YJ
 *
 *         2018年5月31日
 */
@Repository
public class PdwhPubFullTextDAO extends PdwhHibernateDao<PdwhPubFullTextPO, Long> {

  @SuppressWarnings("unchecked")
  public List<PdwhPubFullTextPO> findByIds(PubQueryDTO pubQueryDTO) {
    List<PdwhPubFullTextPO> list = new ArrayList<PdwhPubFullTextPO>();
    String hql = "from PdwhPubFullTextPO t where t.pdwhPubId "
        + " in (SELECT p.pubId FROM PubPdwhPO p where p.status = 0 and p.pubId in(:ids) ) "
        + " order by t.pdwhPubId,t.fileId desc";
    if (pubQueryDTO.getPubIds().size() > 1000) {
      for (int i = 0; i < pubQueryDTO.getPubIds().size() / 1000; i++) {
        list.addAll(this.createQuery(hql)
            .setParameterList("ids", pubQueryDTO.getPubIds().subList(i * 1000, 1000 * (i + 1))).list());
      }
    } else {
      list = this.createQuery(hql).setParameterList("ids", pubQueryDTO.getPubIds()).list();
    }
    return list;
  }

  public PdwhPubFullTextPO getByPubId(Long pubId) {
    String hql = "from PdwhPubFullTextPO t where t.pdwhPubId =:pubId order by t.fileId desc";
    List list = this.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return (PdwhPubFullTextPO) list.get(0);
    }
    return null;
  }

  public PdwhPubFullTextPO getByPubId(Long pubId, Long fileId) {
    String hql = "from PdwhPubFullTextPO t where t.pdwhPubId =:pubId and t.fileId =:fileId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId) ";
    List list = this.createQuery(hql).setParameter("pubId", pubId).setParameter("fileId", fileId).list();
    if (list != null && list.size() > 0) {
      return (PdwhPubFullTextPO) list.get(0);
    }
    return null;
  }

  /**
   * 获取基准库成果全文的数量
   * 
   * @param pdwhPubId
   * @return
   */
  public Long getCountByPdwhPubId(Long pdwhPubId) {
    String hql = "select count(1) from PdwhPubFullTextPO t where t.pdwhPubId =:pdwhPubId";
    return (Long) super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();
  }

}
