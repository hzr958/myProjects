package com.smate.center.merge.dao.task;

import org.springframework.stereotype.Repository;

import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 帐号合并备份记录dao
 * 
 * @author tsz
 *
 */
@Repository
public class AccountsMergeDataDao extends SnsHibernateDao<AccountsMergeData, Long> {

}
