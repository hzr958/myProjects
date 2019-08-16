package com.smate.center.oauth.dao.profile;

import org.springframework.stereotype.Repository;

import com.smate.center.oauth.model.profile.SyncPerson;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员信息冗余Dao
 * 
 * @author zk
 *
 */
@Repository
public class SyncPersonDao extends SnsHibernateDao<SyncPerson, Long> {

}
