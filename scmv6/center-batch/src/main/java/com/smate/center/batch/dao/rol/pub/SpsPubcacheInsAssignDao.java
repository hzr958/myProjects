package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.SpsPubcacheInsAssign;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 单位端SCOPUS成果匹配到单位记录.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class SpsPubcacheInsAssignDao extends RolHibernateDao<SpsPubcacheInsAssign, Long> {

  /**
   * 保存记录.
   * 
   * @param xmlId
   * @param pubId
   * @param insId
   * @param imported
   * @return
   */
  public SpsPubcacheInsAssign saveSpsPubcacheInsAssign(Long xmlId, Long pubId, Long insId, Integer imported) {

    String hql = "from SpsPubcacheInsAssign where pubId = ? and xmlId = ? ";
    List<SpsPubcacheInsAssign> list = super.find(hql, pubId, xmlId);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    SpsPubcacheInsAssign obj = new SpsPubcacheInsAssign(xmlId, pubId, insId, imported);
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
  public List<SpsPubcacheInsAssign> getSpsPubcacheInsAssign(Long xmlId, Long insId) {

    String hql = "from SpsPubcacheInsAssign where xmlId = ? and insId = ? ";
    return super.find(hql, xmlId, insId);
  }
}
