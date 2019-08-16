package com.smate.center.open.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.pdwh.pub.PdwhFullTextFile;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库全文文件dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhFullTextFileDao extends PdwhHibernateDao<PdwhFullTextFile, Long> {

  public PdwhFullTextFile findNewestPdwhFullTextFileByPubId(Long pubId) {
    String hql = " from PdwhFullTextFile p where  p.pubId =:pubId  order by p.fileId desc ";
    List<PdwhFullTextFile> list = this.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

}
