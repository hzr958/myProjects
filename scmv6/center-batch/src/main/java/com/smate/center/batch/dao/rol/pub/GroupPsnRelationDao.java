package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.GroupPsnRelation;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 合作分析群组成员关系.
 * 
 * @author zhuangyanming
 * 
 */
@Repository
public class GroupPsnRelationDao extends RolHibernateDao<GroupPsnRelation, Long> {

  /**
   * 合作分析群组成员关系记录（群组一般不会有太多成员）.
   * 
   * @param groupId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<GroupPsnRelation> findGroupPsnRelationList(Long groupId) throws DaoException {
    String hql = "from GroupPsnRelation t where t.groupId = ?";
    return super.createQuery(hql, new Object[] {groupId}).list();
  }

  /**
   * 删除合作分析群组成员关系记录.
   * 
   * @param groupId
   * @throws DaoException
   */
  public void delAllRelationByGroupId(Long groupId) throws DaoException {

    String hql = "delete GroupPsnRelation where groupId = ? ";
    super.batchExecute(hql, new Object[] {groupId});
  }

  /**
   * 按人员查找：合作分析群组成员关系记录.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<GroupPsnRelation> findGroupPsnRelationListByPsnId(Long psnId) throws DaoException {
    String hql = "from GroupPsnRelation t where t.psnId = ?";
    return super.createQuery(hql, new Object[] {psnId}).list();
  }

  /**
   * 删除合作分析群组成员关系记录.
   * 
   * @param groupId
   * @throws DaoException
   */
  public void delAllRelationByPsnId(Long psnId) throws DaoException {

    String hql = "delete from GroupPsnRelation where psnId = ? ";
    super.batchExecute(hql, new Object[] {psnId});
  }

  /**
   * 合作分析单条群组成员关系记录.
   * 
   * @param groupId
   * @param psnId
   * @param insId
   * @return
   * @throws DaoException
   */
  public GroupPsnRelation findGroupPsnRelation(Long groupId, Long psnId, Long insId) throws DaoException {
    String hql = "from GroupPsnRelation t where t.groupId = ? and t.psnId=? and t.insId=?";
    return super.findUnique(hql, new Object[] {groupId, psnId, insId});
  }
}
