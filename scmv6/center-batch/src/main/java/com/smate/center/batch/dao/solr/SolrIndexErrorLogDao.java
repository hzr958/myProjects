package com.smate.center.batch.dao.solr;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.solr.SolrIndexErrorLog;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class SolrIndexErrorLogDao extends PdwhHibernateDao<SolrIndexErrorLog, Long> {

}
