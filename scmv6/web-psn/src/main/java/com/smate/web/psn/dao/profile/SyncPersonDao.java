package com.smate.web.psn.dao.profile;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.psninfo.SyncPerson;


/**
 * 人员信息冗余Dao
 * 
 * @author zk
 *
 */
@Repository
public class SyncPersonDao extends SnsHibernateDao<SyncPerson, Long> {

}
