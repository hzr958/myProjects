package com.smate.center.open.dao.inspg;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.inspg.InspgAdmin;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 机构主页管理员实体Dao
 * 
 * 
 * @author ajb
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Repository
public class InspgAdminDao extends SnsHibernateDao<InspgAdmin, Long> {


  /**
   * 通过psnId获取
   * 
   * @param inspgId，psnId
   * 
   */
  @SuppressWarnings("unchecked")
  public List<Long> getInspgByPsnId(Long psnId) {
    List<Long> list = super.createQuery(" select t.inspgId from InspgAdmin t where  t.psnId=?", psnId).list();
    return list;
  }


}
