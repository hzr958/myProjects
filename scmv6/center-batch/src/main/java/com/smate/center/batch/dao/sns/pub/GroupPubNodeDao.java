package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.GroupPubNode;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 当前人成果群组关系Dao.
 * 
 * yanmingzhuang
 */
@Repository
public class GroupPubNodeDao extends SnsHibernateDao<GroupPubNode, Long> {

  public Long getCountGroupPubNode(Long pubId, Long groupId, Integer articleType) throws DaoException {
    String hql = "select count(*) from GroupPubNode t where t.pubId=? and t.groupId=? and t.articleType=?";
    return super.findUnique(hql, new Object[] {pubId, groupId, articleType});
  }

  public GroupPubNode getGroupPubNode(Long pubId, Long groupId, Integer articleType) throws DaoException {
    String hql = "select t from GroupPubNode t where t.pubId=? and t.groupId=? and t.articleType=?";
    return super.findUnique(hql, new Object[] {pubId, groupId, articleType});
  }

  public void deleteGroupPubNode(Long groupId, Long psnId, Long pubId, Integer articleType) throws DaoException {
    String hql = "delete from GroupPubNode t where t.pubId=? and t.groupId=? and t.psnId=? and t.articleType=?";
    this.createQuery(hql, new Object[] {pubId, groupId, psnId, articleType}).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<GroupPubNode> getGroupPubsByPubId(Long pubId, Long ownerPsnId) throws DaoException {
    String hql = "from GroupPubNode t where t.pubId=? and t.psnId=?";
    return super.createQuery(hql, new Object[] {pubId, ownerPsnId}).list();

  }

  public void deleteGroupPubNode(Long ownerPsnId, Long pubId) throws DaoException {
    String hql = "delete from GroupPubNode t where t.psnId=? and t.pubId=?";
    super.createQuery(hql, new Object[] {ownerPsnId, pubId}).executeUpdate();
  }

  /**
   * 根据资料类型删除群组成果关系数据_MJG_SCM-3543.
   * 
   * @param ownerPsnId
   * @param pubId
   * @param articleType
   * @throws DaoException
   */
  public void deleteGroupPubNode(Long ownerPsnId, Long pubId, Integer articleType) throws DaoException {
    String hql = "delete from GroupPubNode t where t.psnId=? and t.pubId=? and t.articleType=?";
    super.createQuery(hql, new Object[] {ownerPsnId, pubId, articleType}).executeUpdate();
  }

  /**
   * 查找成果在我加入的群组中的数量.
   * 
   * @param pubId
   * @param psnId
   * @return
   * @throws DaoException
   */
  public Integer findPubCountInMyGroup(Long pubId, Long psnId) throws DaoException {
    StringBuffer hql = new StringBuffer();
    hql.append("select count(t.groupId) from GroupPubNode t");
    hql.append(" where t.pubId=?");
    hql.append(" and exists(select 1 from GroupInvitePsnNode where id.groupId=t.groupId and id.psnId=?)");

    return NumberUtils.toInt(this.findUnique(hql.toString(), new Object[] {pubId, psnId}) + "");
  }

  // ==============人员合并 start============
  @SuppressWarnings("unchecked")
  public List<GroupPubNode> getListByPsnId(Long delPsnId, Long groupId) throws DaoException {
    String hql = "from GroupPubNode where psnId=? and groupId=?";
    return super.createQuery(hql, delPsnId, groupId).list();
  }

  // ==============人员合并 end============

  @SuppressWarnings("unchecked")
  public List<GroupPubNode> queryGroupPubNodeByPubId(Long pubId) throws DaoException {
    String hql = "from GroupPubNode t where t.pubId=?";
    return super.createQuery(hql, pubId).list();
  }
}
