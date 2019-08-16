package com.smate.center.task.dao.sns.msg;

import org.springframework.stereotype.Repository;
import com.smate.core.base.pub.model.fulltext.req.PubFullTextReqRecv;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 收到的全文请求记录Dao
 * 
 * @author zzx
 *
 */

@Repository
public class PubFulltextReqRecvDao extends SnsHibernateDao<PubFullTextReqRecv, Long> {

}
