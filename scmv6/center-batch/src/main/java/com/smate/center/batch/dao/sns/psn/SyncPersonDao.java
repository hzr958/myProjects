package com.smate.center.batch.dao.sns.psn;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.SyncPerson;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class SyncPersonDao extends SnsHibernateDao<SyncPerson, Long> {

}
