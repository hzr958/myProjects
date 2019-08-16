package com.smate.web.group.dao.group;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.OpenGroupUnion;

/**
 * 互联互通 关联群组
 * 
 * @author AiJiangBin
 *
 */
@Repository
public class OpenGroupUnionDao extends SnsHibernateDao<OpenGroupUnion, Long> {
  /**
   * 是否是关联群组
   * 
   * @return 0是 1 否
   */
  public Integer IsExist(Long grpId) {
    String hql = "select t.id  from OpenGroupUnion t where t.groupId=:grpId ";
    List list = this.createQuery(hql).setParameter("grpId", grpId).list();
    if (list != null && list.size() > 0) {
      return 0;
    }
    return 1;
  }

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
  public Long findGroupIdByGroupCode(String groupCode) {
    String hql = "select t.groupId  from OpenGroupUnion t where t.groupCode=:groupCode";
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
  public Boolean groupHasUnionByGroupId(Long groupId) {
    String hql = "select t.groupId  from OpenGroupUnion t where t.groupId=:groupId";
    Object object = this.createQuery(hql).setParameter("groupId", groupId).uniqueResult();
    if (object != null) {
      return true;
    }
    return false;

  }


}
