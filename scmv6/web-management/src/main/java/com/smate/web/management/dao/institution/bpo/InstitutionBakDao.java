package com.smate.web.management.dao.institution.bpo;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.BpoHibernateDao;
import com.smate.web.management.model.institution.bpo.InstitutionBak;

/**
 * 单位信息修改备份DAO
 * 
 * @author zjh
 *
 */
@Repository
public class InstitutionBakDao extends BpoHibernateDao<InstitutionBak, Serializable> {

}
