package com.smate.center.batch.dao.sns.pub;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.DynamicGroup;
import com.smate.center.batch.util.pub.DynamicConstant;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组动态DAO类_SCM-5912.
 * 
 * @author mjg
 * 
 */
@Repository
public class DynamicGroupDao extends SnsHibernateDao<DynamicGroup, Long> {

  /**
   * 查询群组所有动态.
   * 
   * @param groupId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<DynamicGroup> loadGroupAllDynamic(Long groupId, int firstResult, int maxSize) throws DaoException {
    String hql = "from DynamicGroup t where t.groupId=? and t.status=? order by t.updateDate desc";
    return super.createQuery(hql, new Object[] {groupId, 0}).setFirstResult(firstResult).setMaxResults(maxSize + 1)
        .list();
  }

  /**
   * 查询群组新鲜事动态.
   * 
   * @param groupId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<DynamicGroup> loadGroupNewDynamic(Long groupId, int firstResult, int maxSize) throws DaoException {
    String hql = "from DynamicGroup t where t.groupId=? and t.status=? and t.tmpId in(?,?) order by t.updateDate desc";
    return super.createQuery(hql,
        new Object[] {groupId, 0, DynamicConstant.DYNMSG_TEMPLATE_NORMALNEW, DynamicConstant.DYNMSG_TEMPLATE_EXTENDNEW})
            .setFirstResult(firstResult).setMaxResults(maxSize + 1).list();
  }

  /**
   * 获取群组动态记录.
   * 
   * @param groupDynId
   * @return
   */
  public DynamicGroup getDynamicGroup(Long groupDynId) {
    String hql = "from DynamicGroup t where t.groupDynId=? and rownum=1 ";
    Object obj = super.createQuery(hql, groupDynId).uniqueResult();
    if (obj != null) {
      return (DynamicGroup) obj;
    }
    return null;
  }

  /**
   * 保存群组动态记录.
   * 
   * @param dynamicGroup
   */
  public void saveDynamicGroup(DynamicGroup dynamicGroup) {
    if (dynamicGroup != null) {
      if (dynamicGroup.getGroupDynId() != null) {
        super.getSession().update(dynamicGroup);
      } else {
        super.save(dynamicGroup);
      }
    }
  }

  /**
   * 获取动态产生者.
   * 
   * @param id
   * @return
   */
  public Long getDynamicProducer(Long id) {

    return super.findUnique("select producer from DynamicGroup t where groupDynId = ? ", id);
  }

  /**
   * 更新动态信息状态.
   * 
   * @param status
   */
  public void updateDynamicStatus(Long dynId, int status) throws DaoException {
    String hql = "update DynamicGroup t set t.status=? where t.groupDynId=?";
    super.createQuery(hql, new Object[] {status, dynId}).executeUpdate();
  }

  public void updateDynamicStatus(Long dynId, int status, int syncFlag) throws DaoException {
    String hql = "update DynamicGroup t set t.status=?,t.syncFlag=? where t.groupDynId=?";
    super.createQuery(hql, new Object[] {status, syncFlag, dynId}).executeUpdate();
  }

  public void updateAllDynamicStatus(Long dynId, int status, int syncFlag) throws DaoException {
    String hql = "update DynamicGroup t set t.status=?,t.syncFlag=? where t.groupDynId=? or t.dynParentId=?";
    super.createQuery(hql, new Object[] {status, syncFlag, dynId, dynId}).executeUpdate();
  }

  /**
   * 更新动态关注人.
   * 
   * @param receiver
   * @param tmpList
   * @param dynTypeList
   * @param visible
   * @throws DaoException
   */
  public void updateDynamicByAttId(Long receiver, List<Integer> tmpList, List<Integer> dynTypeList, int visible)
      throws DaoException {
    if (tmpList.size() != 0 || dynTypeList.size() != 0) {
      StringBuffer hql = new StringBuffer();
      hql.append("update DynamicGroup t set t.visible=:visible where");
      int andFlag = 0;
      if (dynTypeList.size() != 0) {
        hql.append(" t.dynType in(:dynTypes)");
        andFlag = 1;
      }
      if (tmpList.size() != 0) {
        if (andFlag == 1) {
          hql.append(" and t.tmpId in(:tmpIds)");
        } else {
          hql.append(" t.tmpId in(:tmpIds)");
        }
      }
      hql.append(" and t.receiver=:receiver and t.receiver!=producer and t.status=:status");
      Query query = super.createQuery(hql.toString());
      query.setParameter("visible", visible);
      if (dynTypeList.size() != 0) {
        query.setParameterList("dynTypes", dynTypeList);
      }
      if (tmpList.size() != 0) {
        query.setParameterList("tmpIds", tmpList);
      }
      query.setParameter("receiver", receiver);
      query.setParameter("status", 0);
      query.executeUpdate();
    }
  }

  /**
   * 获取需要综合的动态列表(无权限).
   * 
   * @param dynType
   * @param marginTime
   * @return
   * @throws DaoException
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Map> getComplexDynamicList2(int dynType, Date upDate) throws DaoException {
    String hql =
        "select new map(t.sameFlag as sameFlag,t.dynType as dynType,t.groupId as groupId,count(t.sameFlag) as sameTotal) from DynamicGroup t where t.dynType=?  and t.syncFlag=? and t.updateDate>=? and t.status=? group by t.sameFlag,t.dynType,t.groupId";
    return super.createQuery(hql, new Object[] {dynType, 0, upDate, 0}).list();
  }

  /**
   * 获取相同的动态.
   * 
   * @param sameFlag
   * @param dynType
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<DynamicGroup> getComplexDynamicsBySame(String sameFlag, int dynType, Long groupId, Date upDate)
      throws DaoException {
    String hql = "from DynamicGroup t where t.sameFlag=? and t.dynType=? and t.groupId=?  and t.updateDate>=?";
    return super.createQuery(hql, new Object[] {sameFlag, dynType, groupId, upDate}).list();
  }
}
