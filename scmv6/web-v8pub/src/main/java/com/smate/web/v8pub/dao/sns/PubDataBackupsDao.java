package com.smate.web.v8pub.dao.sns;

import org.springframework.stereotype.Repository;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.backups.PubDataBackups;

@Repository
public class PubDataBackupsDao extends SnsHibernateDao<PubDataBackups, Long> {

}
