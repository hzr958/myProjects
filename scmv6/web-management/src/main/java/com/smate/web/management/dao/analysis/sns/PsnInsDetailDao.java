package com.smate.web.management.dao.analysis.sns;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.analysis.sns.PsnInsDetail;

/**
 * 人员首要单位信息(如果科研之友没有，则使用科研在线信息补充，取等级最高的一个).
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnInsDetailDao extends SnsHibernateDao<PsnInsDetail, Long> {

}
