package com.smate.center.batch.dao.mail;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.mail.MsgBoxTemplate;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 动态模板Dao.
 * 
 * @author pwl
 * 
 */
@Repository
public class MsgBoxTemplateDao extends SnsHibernateDao<MsgBoxTemplate, Integer> {

}
