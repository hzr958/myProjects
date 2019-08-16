package com.smate.web.management.dao.institution.bpo;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.BpoHibernateDao;
import com.smate.web.management.model.institution.bpo.InstitutionAttachment;

/**
 * 持久化单位附件Dao.
 * 
 * @author zjh
 *
 */
@Repository
public class InstitutionAttachmentDao extends BpoHibernateDao<InstitutionAttachment, Long> {

}
