package com.smate.web.psn.dao.autocomplete;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.autocomplete.AcInsUnit;

/**
 * 单位院系自动提示DAO.
 * 
 * @author lhd
 * 
 */
@Repository
public class AcInsUnitDao extends SnsHibernateDao<AcInsUnit, Long> {

  /**
   * 获取自动填充的部门
   * 
   * @param seachKey
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<AcInsUnit> getAcInsUnit(String searchKey, String insName, Integer size) {
    if (StringUtils.isBlank(insName)) {
      return null;
    }
    if (size == null || size == 0) {
      size = 5;
    }
    String hql = "select new AcInsUnit(t.collegeName, t.department, t.unitIds)" + " from AcInsUnit t where "
        + " t.insName=:insName and (instr(upper(t.department),:searchKey) > 0 or instr(upper(t.collegeName),:searchKey) > 0)"
        + " order by (case when instr(upper(t.department),:searchKey) > 0 then instr(upper(t.department),:searchKey)"
        + " else (power(instr(upper(t.collegeName),:searchKey), 3) + 0.5) end), t.seqNo";
    return this.createQuery(hql).setParameter("insName", insName)
        .setParameter("searchKey", searchKey == null ? "" : searchKey.replaceAll("\'", "&#39;").toUpperCase().trim())
        .setMaxResults(size).list();
  }


}
