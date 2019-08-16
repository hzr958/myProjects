package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.EiPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.PdwhPubcacheInsAssign;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 单位端成果匹配到单位记录.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PdwhPubcacheInsAssignDao extends RolHibernateDao<PdwhPubcacheInsAssign, Long> {

  public PdwhPubcacheInsAssign savaPdwhPubcacheInsAssign(Long xmlId, Long pubId, Long insId, int imported) {
    String hql = "from PdwhPubcacheInsAssign where pubId = ? and xmlId = ? ";
    List<PdwhPubcacheInsAssign> list = super.find(hql, pubId, xmlId);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    PdwhPubcacheInsAssign obj = new PdwhPubcacheInsAssign(xmlId, pubId, insId, imported);
    this.save(obj);
    return obj;
  }

  public List<PdwhPubcacheInsAssign> getPdwhPubcacheInsAssign(Long xmlId, Long insId) {
    String hql = "from PdwhPubcacheInsAssign where xmlId = ? and insId = ? ";
    return super.find(hql, xmlId, insId);
  }

}
