package com.smate.web.management.dao.institution.rol;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.web.management.model.institution.rol.ConstCnProvince;

@Repository
public class ConstCnProvinceDao extends RolHibernateDao<ConstCnProvince, Serializable> {

  /**
   * 获取所有的省份.
   * 
   * @param locale
   * @return
   */
  public List<ConstCnProvince> getAllProvince(Locale locale) {
    String hql = "from ConstCnProvince order by  " + locale.getLanguage() + "Seq";
    Query query = createQuery(hql);
    query.setCacheable(true);
    List<ConstCnProvince> list = query.list();
    return list;
  }

}
