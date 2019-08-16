package com.smate.center.batch.dao.pdwh.pub.sps;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.sps.SpsCtyNameExclude;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * SCOPUS匹配排除国外名称.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class SpsCtyNameExcludeDao extends PdwhHibernateDao<SpsCtyNameExclude, Long> {

}
