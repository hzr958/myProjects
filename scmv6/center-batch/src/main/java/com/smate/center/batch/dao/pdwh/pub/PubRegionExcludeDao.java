package com.smate.center.batch.dao.pdwh.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PubRegionExclude;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * ISI匹配排除国外、或地区名称.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubRegionExcludeDao extends PdwhHibernateDao<PubRegionExclude, Long> {

}
