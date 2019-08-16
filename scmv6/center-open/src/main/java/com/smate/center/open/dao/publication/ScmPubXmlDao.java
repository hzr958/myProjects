package com.smate.center.open.dao.publication;


import org.springframework.stereotype.Repository;

import com.smate.center.open.model.publication.ScmPubXml;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;



/**
 * 成果XML.
 * 
 * @author ajb
 * 
 */
@Repository
public class ScmPubXmlDao extends HibernateDao<ScmPubXml, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }
}
