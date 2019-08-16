package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.EiPubcacheInsAssign;
import com.smate.core.base.utils.data.RolHibernateDao;


/**
 * 单位端成果匹配到单位记录.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class EiPubcacheInsAssignDao extends RolHibernateDao<EiPubcacheInsAssign, Long> {

  /**
   * 保存记录.
   * 
   * @param xmlId
   * @param pubId
   * @param insId
   * @param imported
   * @return
   */
  public EiPubcacheInsAssign saveEiPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported) {

    String hql = "from EiPubcacheInsAssign where pubId = ? and xmlId = ? ";
    List<EiPubcacheInsAssign> list = super.find(hql, pubId, xmlId);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    EiPubcacheInsAssign obj = new EiPubcacheInsAssign(xmlId, pubId, insId, imported);
    this.save(obj);
    return obj;
  }

  /**
   * 获取导入机构的成果关系列表.
   * 
   * @param xmlId
   * @param insId
   * @return
   */
  public List<EiPubcacheInsAssign> getEiPubcacheInsAssign(Long xmlId, Long insId) {

    String hql = "from EiPubcacheInsAssign where xmlId = ? and insId = ? ";
    return super.find(hql, xmlId, insId);
  }
}
