package com.smate.center.open.dao.publication;

import org.springframework.stereotype.Repository;
import com.smate.center.open.model.publication.PubDataStore;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubDataStoreDao extends SnsHibernateDao<PubDataStore, Long> {

  public void specialSave(PubDataStore data) {

    getSession().saveOrUpdate(data);
    getSession().evict(data);

  }

}
