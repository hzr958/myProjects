package com.smate.center.batch.dao.pdwh.pubimport;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pubimport.PdwhInsName;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库机构名dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhInsNameDao extends PdwhHibernateDao<PdwhInsName, Long> {


  @SuppressWarnings("unchecked")
  public List<PdwhInsName> getPdwhInsName(Long insId) {
    String hql =
        "select new PdwhInsName(id, insId, insName, nameLength) from PdwhInsName where insId =:insId order by nameLength desc";
    return super.createQuery(hql).setParameter("insId", insId).list();
  }


}
