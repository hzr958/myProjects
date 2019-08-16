package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.CnkiPatPubcacheInsAssign;
import com.smate.core.base.utils.data.RolHibernateDao;


/**
 * 单位端cnkipat成果匹配到单位记录.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class CnkiPatPubcacheInsAssignDao extends RolHibernateDao<CnkiPatPubcacheInsAssign, Long> {

  /**
   * 保存记录.
   * 
   * @param xmlId
   * @param pubId
   * @param insId
   * @param imported
   * @return
   */
  public CnkiPatPubcacheInsAssign saveCnkiPatPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported) {

    String hql = "from CnkiPatPubcacheInsAssign where pubId = ? and xmlId = ? ";
    List<CnkiPatPubcacheInsAssign> list = super.find(hql, pubId, xmlId);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    CnkiPatPubcacheInsAssign obj = new CnkiPatPubcacheInsAssign(xmlId, pubId, insId, imported);
    this.save(obj);
    return obj;
  }
}
