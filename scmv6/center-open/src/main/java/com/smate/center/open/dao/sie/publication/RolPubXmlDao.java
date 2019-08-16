package com.smate.center.open.dao.sie.publication;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.sie.publication.RolPubXml;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 单位成果xmlDAO.
 * 
 * @author xys
 * 
 */
@Repository
public class RolPubXmlDao extends RolHibernateDao<RolPubXml, Long> {

  public List<RolPubXml> getPubXmlListByIds(List<Long> rolPubIdList) {
    if (rolPubIdList == null || rolPubIdList.size() == 0) {
      return null;
    }
    String hql = "from RolPubXml p where p.pubId in ( :rolPubIdList ) ";
    return this.createQuery(hql).setParameterList("rolPubIdList", rolPubIdList).list();
  }
}
