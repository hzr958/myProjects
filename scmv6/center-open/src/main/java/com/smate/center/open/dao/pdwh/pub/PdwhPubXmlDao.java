package com.smate.center.open.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.pdwh.pub.PdwhPubXml;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * @author zjh
 *
 */
@Repository
public class PdwhPubXmlDao extends PdwhHibernateDao<PdwhPubXml, Long> {

  public List<PdwhPubXml> getPubXmlListByIds(List<Long> pubIdList) {
    String hql = " from  PdwhPubXml  pd where pd.pubId   in  (:pubIdList ) ";
    return this.createQuery(hql).setParameterList("pubIdList", pubIdList).list();
  }

}
