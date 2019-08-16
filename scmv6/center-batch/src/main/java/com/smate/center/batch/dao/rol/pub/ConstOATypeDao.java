package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.ConstOAType;
import com.smate.core.base.utils.data.RolHibernateDao;


/**
 * 开放存储类型DAO.
 * 
 * @author xys
 * 
 */
@Repository
public class ConstOATypeDao extends RolHibernateDao<ConstOAType, Long> {

  @SuppressWarnings("unchecked")
  public List<ConstOAType> getAllOATypes() {
    return super.createQuery("from ConstOAType").setCacheable(true).list();
  }

  @SuppressWarnings("unchecked")
  public ConstOAType getConstOATypeByRomeoColour(String romeoColour) {
    String hql = "from ConstOAType t where lower(t.romeoColourZh)=? or lower(t.romeoColourEn)=?";
    romeoColour = romeoColour.trim().toLowerCase();
    List<ConstOAType> list = super.createQuery(hql, romeoColour, romeoColour).list();
    if (list == null || list.size() == 0) {
      return null;
    }
    return list.get(0);
  }
}
