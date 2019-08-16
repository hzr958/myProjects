package com.smate.web.management.dao.institution.rol;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.web.management.model.institution.rol.ConstCnCity;

/**
 * 中国地区.
 * 
 * @author zjh
 * 
 */
@Repository
public class ConstCnCityDao extends RolHibernateDao<ConstCnCity, Serializable> {

  public List<ConstCnCity> getAllCit(Long provinceId) {
    String hql = "from ConstCnCity as c where c.prvId = ? order by c.zhSeq";
    return super.createQuery(hql, provinceId).list();
  }

}
