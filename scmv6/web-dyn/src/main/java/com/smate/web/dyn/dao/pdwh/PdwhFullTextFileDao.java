package com.smate.web.dyn.dao.pdwh;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.dyn.model.pdwhpub.PdwhFullTextFile;

/**
 * 基准库全文文件dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhFullTextFileDao extends PdwhHibernateDao<PdwhFullTextFile, Long> {

  public Long getCountByPubAllId(Long pubAllId) {
    String hql = "select count(1) from PdwhFullTextFile t where t.pubId =:pubAllId";
    return (Long) super.createQuery(hql).setParameter("pubAllId", pubAllId).uniqueResult();
  }

  public PdwhFullTextFile getPubByPubId(Long pubId) {
    String hql =
        "from PdwhFullTextFile t where t.pubId =:pubId and t.fileId =(select max(fileId) from PdwhFullTextFile where pubId =:pubId)";
    return (PdwhFullTextFile) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  /**
   * 通过成果id获取成果全文文件
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public PdwhFullTextFile getByPubId(Long pubId) {
    String hqlMax = "select max(createDate) from PdwhFullTextFile where pubId =:pubId";
    Object maxDate = createQuery(hqlMax).setParameter("pubId", pubId).uniqueResult();

    String hql = "from PdwhFullTextFile t where t.pubId =:pubId";
    PdwhFullTextFile pftf = null;
    if (Objects.nonNull(maxDate)) {
      hql += " and t.createDate = :maxDate";
      pftf = (PdwhFullTextFile) createQuery(hql).setParameter("pubId", pubId).setParameter("maxDate", maxDate)
          .uniqueResult();
    } else {
      List<PdwhFullTextFile> list = createQuery(hql).setParameter("pubId", pubId).list();
      if (CollectionUtils.isNotEmpty(list)) {
        pftf = list.get(0);
      }
    }
    return pftf;
  }

}
