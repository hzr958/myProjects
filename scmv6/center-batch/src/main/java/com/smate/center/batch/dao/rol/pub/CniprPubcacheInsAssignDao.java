package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnipr.CniprPubcacheInsAssign;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 单位端CNKIPR成果匹配到单位记录.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class CniprPubcacheInsAssignDao extends RolHibernateDao<CniprPubcacheInsAssign, Long> {

  /**
   * 保存记录.
   * 
   * @param xmlId
   * @param pubId
   * @param insId
   * @param imported
   * @return
   */
  public CniprPubcacheInsAssign saveCniprPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported) {

    String hql = "from CniprPubcacheInsAssign where pubId = ? and xmlId = ? ";
    List<CniprPubcacheInsAssign> list = super.find(hql, pubId, xmlId);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    CniprPubcacheInsAssign obj = new CniprPubcacheInsAssign(xmlId, pubId, insId, imported);
    this.save(obj);
    return obj;
  }
}
