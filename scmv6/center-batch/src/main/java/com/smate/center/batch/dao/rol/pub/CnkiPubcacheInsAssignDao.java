package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.CnkiPubcacheInsAssign;
import com.smate.core.base.utils.data.RolHibernateDao;


/**
 * 单位端CNKI成果匹配到单位记录.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class CnkiPubcacheInsAssignDao extends RolHibernateDao<CnkiPubcacheInsAssign, Long> {

  /**
   * 保存记录.
   * 
   * @param xmlId
   * @param pubId
   * @param insId
   * @param imported
   * @return
   */
  public CnkiPubcacheInsAssign saveCnkiPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported) {

    String hql = "from CnkiPubcacheInsAssign where pubId = ? and xmlId = ? ";
    List<CnkiPubcacheInsAssign> list = super.find(hql, pubId, xmlId);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    CnkiPubcacheInsAssign obj = new CnkiPubcacheInsAssign(xmlId, pubId, insId, imported);
    this.save(obj);
    return obj;
  }
}
