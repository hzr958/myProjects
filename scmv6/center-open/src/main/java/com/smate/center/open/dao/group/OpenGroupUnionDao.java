package com.smate.center.open.dao.group;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.group.association.OpenGroupUnion;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 互联互通 关联群组
 * 
 * @author AiJiangBin
 *
 */
@Repository
public class OpenGroupUnionDao extends SnsHibernateDao<OpenGroupUnion, Long> {

  /**
   * 通过ownPsnId查找 id
   * 
   * @param psnId
   * @return
   */
  public Long findIdByOwnPsnIdAndGroupCode(Long ownerPsnId, String groupCode) {
    String hql = "select t.id  from OpenGroupUnion t where t.ownerPsnId=:ownerPsnId and t.groupCode=:groupCode";
    Object object = this.createQuery(hql).setParameter("ownerPsnId", ownerPsnId).setParameter("groupCode", groupCode)
        .uniqueResult();
    if (object != null) {
      return NumberUtils.toLong(object.toString());
    }
    return null;

  }

  /**
   * 通过groupCode查找 id
   * 
   * @param psnId
   * @return
   */
  public Long findIdByOwnPsnIdAndGroupCode(String groupCode) {
    String hql = "select t.id  from OpenGroupUnion t where t.groupCode=:groupCode ";
    Object object = this.createQuery(hql).setParameter("groupCode", groupCode).uniqueResult();
    if (object != null) {
      return NumberUtils.toLong(object.toString());
    }
    return null;

  }

  /**
   * 通过groupCode查找 id
   * 
   * @param psnId
   * @return
   */
  public Long findGroupIdByGroupCode(String groupCode, Long ownerPsnId) {
    String hql = "select t.groupId  from OpenGroupUnion t where t.groupCode=:groupCode and t.ownerPsnId=:ownerPsnId ";
    Object object = this.createQuery(hql).setParameter("groupCode", groupCode).setParameter("ownerPsnId", ownerPsnId)
        .uniqueResult();
    if (object != null) {
      return NumberUtils.toLong(object.toString());
    }
    return null;

  }


}
