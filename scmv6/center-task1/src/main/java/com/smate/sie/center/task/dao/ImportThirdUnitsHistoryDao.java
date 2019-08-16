package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.ImportThirdUnitsHistory;

/**
 * 第三方部门信息处理历史记录DAO.
 * 
 * @author xys
 *
 */
@Repository
public class ImportThirdUnitsHistoryDao extends SieHibernateDao<ImportThirdUnitsHistory, Long> {

  /**
   * 获取SIE部门ID.
   * 
   * @param insId
   * @param unitId
   * @return
   */
  @SuppressWarnings("unchecked")
  public Long getSieUnitId(Long insId, String unitId) {
    String hql =
        "select t.sieUnitId from ImportThirdUnitsHistory t where t.insId = ? and t.unitId = ? and t.status=1 order by t.importDate desc";
    List<Long> list = super.createQuery(hql, insId, unitId).setMaxResults(1).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 获取单位最新第三方部门信息处理历史记录.
   * 
   * @param insId
   * @param unitId
   * @return
   */
  @SuppressWarnings("unchecked")
  public ImportThirdUnitsHistory getImportThirdUnitsHistory(Long insId, String unitId) {
    String hql =
        "from ImportThirdUnitsHistory t where t.insId = ? and t.unitId = ? and t.status=1 order by t.importDate desc";
    List<ImportThirdUnitsHistory> list = super.createQuery(hql, insId, unitId).setMaxResults(1).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }
}
