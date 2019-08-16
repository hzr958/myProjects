package com.smate.core.base.utils.dao.shorturl;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.shorturl.OpenShortUrlUseLog;

@Repository
public class OpenShortUrlUseLogDao extends SnsHibernateDao<OpenShortUrlUseLog, Long> {

}
