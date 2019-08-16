package com.smate.center.merge.dao.account;

import com.smate.center.merge.model.cas.account.Account;
import com.smate.core.base.utils.data.CasHibernateDao;
import org.springframework.stereotype.Repository;

/**
 * 帐号dao .
 * 
 * @author tsz
 *
 * @date 2018年9月11日
 */
@Repository
public class AccountDao extends CasHibernateDao<Account, Long> {
}
