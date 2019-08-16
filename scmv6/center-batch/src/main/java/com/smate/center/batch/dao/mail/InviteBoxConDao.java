package com.smate.center.batch.dao.mail;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.mail.InviteBoxCon;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 站内邀请收件箱关联邮件内容表.
 * 
 * @author maojianguo
 * 
 */
@Repository
public class InviteBoxConDao extends SnsHibernateDao<InviteBoxCon, Long> {

}
