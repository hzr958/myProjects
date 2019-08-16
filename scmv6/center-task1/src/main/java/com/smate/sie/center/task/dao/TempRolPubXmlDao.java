package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.RolPubXml;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 成果XML.
 * 
 * @author jszhou
 *
 */
@Repository
public class TempRolPubXmlDao extends RolHibernateDao<RolPubXml, Long> {
  @SuppressWarnings("unchecked")
  public List<RolPubXml> getRolPubXmlListPub(int size) {
    String hql = "from RolPubXml";
    return super.createQuery(hql).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<RolPubXml> getRolPubXmlListPat(int size) {
    String hql = "from RolPubXml where pubId in (select id from PublicationRol where pubType = 5)";
    return super.createQuery(hql).setMaxResults(size).list();
  }

}
