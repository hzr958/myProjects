package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.InsRegion;
import com.smate.center.batch.model.rol.pub.InstitutionRol;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 单位所在省市.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class InsRegionDao extends RolHibernateDao<InsRegion, Long> {

  /**
   * 获取机构所在省市.
   * 
   * @param insIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InsRegion> getInsRegion(List<Long> insIds) {

    String hql = "from InsRegion where insId in (:insIds) ";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }

  @SuppressWarnings("unchecked")
  public List<InstitutionRol> getInstitution(String selectedCyIds, String selectedPrvIds) {
    List<Object> params = new ArrayList<Object>();
    StringBuffer hql = new StringBuffer();
    hql.append("select t from InsRegion ins,InstitutionRol t where t.insId=ins.insId ");
    if (StringUtils.isNotBlank(selectedPrvIds) && selectedPrvIds.indexOf(",") > 0) {
      hql.append(" and t.ins.prvId in(" + selectedPrvIds + ")");
    } else if (StringUtils.isNotBlank(selectedPrvIds)) {
      hql.append(" and t.ins.prvId in(" + selectedPrvIds + ")");
      params.add(selectedPrvIds);
    }
    if (StringUtils.isNotBlank(selectedCyIds) && selectedCyIds.indexOf(",") > 0) {
      hql.append(" and t.ins.cyId in(" + selectedCyIds + ")");
    } else if (StringUtils.isNotBlank(selectedPrvIds)) {
      hql.append(" and t.ins.cyId in(" + selectedCyIds + ")");
      params.add(selectedPrvIds);
    }
    return super.createQuery(hql.toString(), params.toArray()).list();
  }

}
