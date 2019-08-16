package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.Dynamic;
import com.smate.center.batch.util.pub.DynamicConstant;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 动态消息Dao.
 * 
 * @author chenxiangrong
 * 
 */
@Repository
public class DynamicDao extends SnsHibernateDao<Dynamic, Long> {
  /**
   * 更新动态信息状态.
   * 
   * @param status
   */
  public void updateDynamicStatus(Long dynId, int status) throws DaoException {
    String hql = "update Dynamic t set t.status=? where t.dynId=?";
    super.createQuery(hql, new Object[] {status, dynId}).executeUpdate();
  }

  public void updateDynamicStatus(Long dynId, int status, int syncFlag) throws DaoException {
    String hql = "update Dynamic t set t.status=?,t.syncFlag=? where t.dynId=?";
    super.createQuery(hql, new Object[] {status, syncFlag, dynId}).executeUpdate();
  }

  public void updateAllDynamicStatus(Long dynId, int status, int syncFlag) throws DaoException {
    String hql = "update Dynamic t set t.status=?,t.syncFlag=? where t.dynId=? or t.dynParentId=?";
    super.createQuery(hql, new Object[] {status, syncFlag, dynId, dynId}).executeUpdate();
  }

  /**
   * 查询个人中心所有动态. --------------保留 其他地方调用
   * 
   * @param psnId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  @Deprecated
  public List<Dynamic> loadPsnAllDynamic(Long psnId, int firstResult, int maxSize) throws DaoException {
    String hql =
        "from Dynamic t where t.receiver=? and t.visible=? and t.groupId=? and t.status=? order by t.updateDate desc";

    return super.createQuery(hql, new Object[] {psnId, DynamicConstant.DYN_VISIBLE_TRUE, 0l, 0})
        .setFirstResult(firstResult).setMaxResults(maxSize + 1).list();// 多查询一条
  }

  /**
   * 查询个人中心所有动态.----------修复新增 删除动态,点击更多出现结果不正确 以dynid排序 产生新的问题: MQ多个处理
   * updatedate与dynid顺序可能不一致,几率比较小,暂时处理
   * 
   * @param psnId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Dynamic> loadPsnAllDynamic(Long psnId, int maxSize, Long lastDynId) throws DaoException {
    String hql = "from Dynamic t where t.receiver=? and t.visible=? and t.groupId=? and t.status=? ";
    String hql2 = " and t.dynId<" + lastDynId + " ";
    String hql3 = " order by t.dynId desc ";
    if (lastDynId != -1) {
      hql = hql + hql2 + hql3;
    } else {
      hql = hql + hql3;
    }
    return super.createQuery(hql, new Object[] {psnId, DynamicConstant.DYN_VISIBLE_TRUE, 0l, 0}).setFirstResult(0)
        .setMaxResults(maxSize + 1).list();// 多查询一条
  }

  /**
   * 查询个人中心与我相关的动态. --保留
   * 
   * @param psnId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  @Deprecated
  public List<Dynamic> loadPsnMeDynamic(Long psnId, int firstResult, int maxSize) throws DaoException {
    String hql =
        "from Dynamic t where (t.producer=? or objectOwner=?) and t.receiver=? and t.visible=? and t.groupId=? and t.status=? order by t.updateDate desc";
    return super.createQuery(hql, new Object[] {psnId, psnId, psnId, DynamicConstant.DYN_VISIBLE_TRUE, 0l, 0})
        .setFirstResult(firstResult).setMaxResults(maxSize + 1).list();// 多查询一条
  }

  /**
   * 查询个人中心与我相关的动态. --new
   * 
   * @param psnId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Dynamic> loadPsnMeDynamic(Long psnId, int maxSize, Long lastDynId) throws DaoException {
    String hql =
        "from Dynamic t where (t.producer=? or objectOwner=?) and t.receiver=? and t.visible=? and t.groupId=? and t.status=? ";
    String hql2 = " and t.dynId<" + lastDynId + " ";
    String hql3 = " order by t.dynId desc ";
    if (lastDynId != -1) {
      hql = hql + hql2 + hql3;
    } else {
      hql = hql + hql3;
    }
    return super.createQuery(hql, new Object[] {psnId, psnId, psnId, DynamicConstant.DYN_VISIBLE_TRUE, 0l, 0})
        .setFirstResult(0).setMaxResults(maxSize + 1).list();// 多查询一条
  }

  /**
   * 查询个人中心新鲜事动态. --保留
   * 
   * @param psnId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  @Deprecated
  public List<Dynamic> loadPsnNewDynamic(Long psnId, int firstResult, int maxSize) throws DaoException {
    String hql =
        "from Dynamic t where t.receiver=? and t.visible=? and t.groupId=? and t.tmpId in(?,?) and t.status=? order by t.updateDate desc";
    return super.createQuery(hql,
        new Object[] {psnId, DynamicConstant.DYN_VISIBLE_TRUE, 0l, DynamicConstant.DYNMSG_TEMPLATE_NORMALNEW,
            DynamicConstant.DYNMSG_TEMPLATE_EXTENDNEW, 0}).setFirstResult(firstResult).setMaxResults(maxSize + 1)
                .list();// 多查询一条
  }

  /**
   * 查询个人中心新鲜事动态. --new
   * 
   * @param psnId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Dynamic> loadPsnNewDynamic(Long psnId, int maxSize, Long lastDynId) throws DaoException {
    String hql =
        "from Dynamic t where t.receiver=? and t.visible=? and t.groupId=? and t.tmpId in(?,?) and t.status=? ";
    String hql2 = " and t.dynId<" + lastDynId + " ";
    String hql3 = " order by t.dynId desc ";
    if (lastDynId != -1) {
      hql = hql + hql2 + hql3;
    } else {
      hql = hql + hql3;
    }
    return super.createQuery(hql, new Object[] {psnId, DynamicConstant.DYN_VISIBLE_TRUE, 0l,
        DynamicConstant.DYNMSG_TEMPLATE_NORMALNEW, DynamicConstant.DYNMSG_TEMPLATE_EXTENDNEW, 0}).setFirstResult(0)
            .setMaxResults(maxSize + 1).list();// 多查询一条
  }

  /**
   * 查询个人中心好友动态. --保留
   * 
   * @param psnId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @Deprecated
  @SuppressWarnings("unchecked")
  public List<Dynamic> loadPsnFriendDynamic(Long psnId, int firstResult, int maxSize) throws DaoException {
    String hql =
        "from Dynamic t where t.receiver=? and t.producer!=? and t.visible=? and t.groupId=? and t.relation in(?,?) and t.status=? order by t.updateDate desc";
    return super.createQuery(hql, new Object[] {psnId, psnId, DynamicConstant.DYN_VISIBLE_TRUE, 0l,
        DynamicConstant.DYN_RELATION_FRIEND, DynamicConstant.DYN_RELATION_FANDA, 0}).setFirstResult(firstResult)
            .setMaxResults(maxSize + 1).list();
  }

  /**
   * 查询个人中心好友动态.--new
   * 
   * @param psnId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Dynamic> loadPsnFriendDynamic(Long psnId, int maxSize, Long lastDynId) throws DaoException {
    String hql =
        "from Dynamic t where t.receiver=? and t.producer!=? and t.visible=? and t.groupId=? and t.relation in(?,?) and t.status=? ";
    String hql2 = " and t.dynId<" + lastDynId + " ";
    String hql3 = " order by t.dynId desc ";
    if (lastDynId != -1) {
      hql = hql + hql2 + hql3;
    } else {
      hql = hql + hql3;
    }
    return super.createQuery(hql, new Object[] {psnId, psnId, DynamicConstant.DYN_VISIBLE_TRUE, 0l,
        DynamicConstant.DYN_RELATION_FRIEND, DynamicConstant.DYN_RELATION_FANDA, 0}).setFirstResult(0)
            .setMaxResults(maxSize + 1).list();
  }

  /**
   * 查询群组所有动态.
   * 
   * @param groupId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Dynamic> loadGroupAllDynamic(Long groupId, int firstResult, int maxSize) throws DaoException {
    String hql = "from Dynamic t where t.groupId=? and t.status=? order by t.updateDate desc";
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
  public List<Dynamic> loadGroupNewDynamic(Long groupId, int firstResult, int maxSize) throws DaoException {
    String hql = "from Dynamic t where t.groupId=? and t.status=? and t.tmpId in(?,?) order by t.updateDate desc";
    return super.createQuery(hql,
        new Object[] {groupId, 0, DynamicConstant.DYNMSG_TEMPLATE_NORMALNEW, DynamicConstant.DYNMSG_TEMPLATE_EXTENDNEW})
            .setFirstResult(firstResult).setMaxResults(maxSize + 1).list();
  }

  /**
   * 查询某人动态.--保留
   * 
   * @param psnId
   * @param isFriend
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Deprecated
  public List<Dynamic> loadUserDynamic(Long psnId, boolean isFriend, int firstResult, int maxSize) throws DaoException {
    StringBuffer hql = new StringBuffer();
    List params = new ArrayList();
    hql.append("from Dynamic t where t.groupId=? and t.status=? and t.producer=? and t.receiver=?");
    params.add(0l);
    params.add(0);
    params.add(psnId);
    params.add(psnId);
    if (isFriend) {
      hql.append(" and t.permission in(?,?)");
      params.add(DynamicConstant.DYN_PERMISSION_FRIEND);
      params.add(DynamicConstant.DYN_PERMISSION_ALL);
    } else {
      hql.append(" and t.permission=?");
      params.add(DynamicConstant.DYN_PERMISSION_ALL);
    }
    hql.append(" order by t.updateDate desc");

    return super.createQuery(hql.toString(), params.toArray()).setFirstResult(firstResult).setMaxResults(maxSize + 1)
        .list();
  }

  /**
   * 查询某人动态.--new
   * 
   * @param psnId
   * @param isFriend
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Dynamic> loadUserDynamic(Long psnId, boolean isFriend, int maxSize, Long lastDynId) throws DaoException {
    StringBuffer hql = new StringBuffer();
    List params = new ArrayList();
    hql.append("from Dynamic t where t.groupId=? and t.status=? and t.producer=? and t.receiver=?");
    params.add(0l);
    params.add(0);
    params.add(psnId);
    params.add(psnId);
    if (isFriend) {
      hql.append(" and t.permission in(?,?)");
      params.add(DynamicConstant.DYN_PERMISSION_FRIEND);
      params.add(DynamicConstant.DYN_PERMISSION_ALL);
    } else {
      hql.append(" and t.permission=?");
      params.add(DynamicConstant.DYN_PERMISSION_ALL);
    }

    if (lastDynId != -1) {
      hql.append(" and t.dynId<" + lastDynId + " ");
    }
    hql.append(" order by t.dynId desc");

    return super.createQuery(hql.toString(), params.toArray()).setFirstResult(0).setMaxResults(maxSize + 1).list();
  }

  /**
   * 取消动态的可见性.
   * 
   * @param producer
   * @param receiver
   * @param dynTypeList
   * @throws DaoException
   */
  public void cancelDynamicVisible(Long producer, Long receiver, List<Integer> dynTypeList) throws DaoException {
    StringBuffer hql = new StringBuffer();
    hql.append("update Dynamic t set t.visible=:visible where t.producer=:producer and t.receiver=:receiver");
    Query query = null;
    if (dynTypeList != null && dynTypeList.size() != 0) {
      hql.append(" and dynType in(:dynTypes)");
      query = super.createQuery(hql.toString());
      query.setParameter("visible", 1);
      query.setParameter("producer", producer);
      query.setParameter("receiver", receiver);
      query.setParameterList("dynTypes", dynTypeList);
    } else {
      query = super.createQuery(hql.toString());
      query.setParameter("visible", 1);
      query.setParameter("producer", producer);
      query.setParameter("receiver", receiver);
    }
    query.executeUpdate();
  }

  /**
   * 更新同步标识.
   * 
   * @param objectId
   * @param dynType
   * @throws DaoException
   */
  public void updateDynamicSync(Long objectId, int dynType) throws DaoException {
    String hql = "update Dynamic t set t.syncFlag=? where t.objectId=? and t.dynType=?";
    super.createQuery(hql, new Object[] {1, objectId, dynType}).executeUpdate();
  }

  public void updateDynamicSyncStatus(Long objectId, int dynType) throws DaoException {
    String hql = "update Dynamic t set t.syncFlag=?,t.status=? where t.objectId=? and t.dynType=?";
    super.createQuery(hql, new Object[] {1, 1, objectId, dynType}).executeUpdate();
  }

  /**
   * 获取需要综合的动态列表.
   * 
   * @param objectId
   * @param dynType
   * @param marginTime
   * @return
   * @throws DaoException
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Map> getComplexDynamicList(int dynType, Date upDate) throws DaoException {
    String hql =
        "select new map(t.sameFlag as sameFlag,t.dynType as dynType,t.permission as permission,t.groupId as groupId,count(t.sameFlag) as sameTotal) from Dynamic t where t.dynType=?  and t.syncFlag=? and t.updateDate>=? and t.status=? group by t.sameFlag,t.dynType,t.permission,t.groupId";
    return super.createQuery(hql, new Object[] {dynType, 0, upDate, 0}).list();
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
        "select new map(t.sameFlag as sameFlag,t.dynType as dynType,t.groupId as groupId,count(t.sameFlag) as sameTotal) from Dynamic t where t.dynType=?  and t.syncFlag=? and t.updateDate>=? and t.status=? group by t.sameFlag,t.dynType,t.groupId";
    return super.createQuery(hql, new Object[] {dynType, 0, upDate, 0}).list();
  }

  /**
   * 获取需要综合的动态列表.
   * 
   * @param objectId
   * @param dynType
   * @param marginTime
   * @return
   * @throws DaoException
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Map> getComplexDynamicList3(List<Integer> dynTypeList, Date upDate) throws DaoException {
    String hql =
        "select new map(t.sameFlag as sameFlag,t.dynType as dynType,t.permission as permission,t.groupId as groupId,count(t.sameFlag) as sameTotal) from Dynamic t where t.dynType in(:dynTypes) and t.syncFlag=:syncFlag and t.updateDate>=:updateDate and t.status=:status group by t.sameFlag,t.dynType,t.permission,t.groupId";
    Query query = super.createQuery(hql);
    query.setParameterList("dynTypes", dynTypeList);
    query.setParameter("syncFlag", 0);
    query.setParameter("updateDate", upDate);
    query.setParameter("status", 0);
    return query.list();
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
  public List<Map> getComplexDynamicList4(List<Integer> dynTypeList, int marginTime) throws DaoException {
    String hql =
        "select new map(t.sameFlag as sameFlag,t.dynType as dynType,t.permission as permission,t.groupId as groupId,count(t.sameFlag) as sameTotal) from Dynamic t where t.dynType in(:dynTypes) and t.syncFlag=:syncFlag and t.updateDate>=:updateDate and t.status=:status group by t.sameFlag,t.dynType,t.permission,t.groupId";
    Date upDate = DateUtils.addHours(new Date(), marginTime);
    Query query = super.createQuery(hql);
    query.setParameterList("dynTypes", dynTypeList);
    query.setParameter("syncFlag", 0);
    query.setParameter("updateDate", upDate);
    query.setParameter("status", 0);
    return query.list();
  }

  /**
   * 获取不需要的综合同步列表.
   * 
   * @param marginTime
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Dynamic> getNoComplexDynamicList(int marginTime) throws DaoException {
    String hql = "from Dynamic t where t.syncFlag=:syncFlag and t.status=:status and t.updateDate>=:updateDate";
    Date upDate = DateUtils.addHours(new Date(), marginTime);
    Query query = super.createQuery(hql);
    query.setParameter("syncFlag", 2);
    query.setParameter("status", 0);
    query.setParameter("updateDate", upDate);
    return query.list();
  }

  /**
   * 获取相同的动态.
   * 
   * @param sameFlag
   * @param dynType
   * @param permission
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Dynamic> getComplexDynamicsBySame(String sameFlag, int dynType, int permission, Long groupId, Date upDate)
      throws DaoException {
    String hql =
        "from Dynamic t where t.sameFlag=? and t.dynType=? and t.permission=? and t.groupId=? and t.updateDate>=?";
    return super.createQuery(hql, new Object[] {sameFlag, dynType, permission, groupId, upDate}).list();
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
  public List<Dynamic> getComplexDynamicsBySame(String sameFlag, int dynType, Long groupId, Date upDate)
      throws DaoException {
    String hql = "from Dynamic t where t.sameFlag=? and t.dynType=? and t.groupId=?  and t.updateDate>=?";
    return super.createQuery(hql, new Object[] {sameFlag, dynType, groupId, upDate}).list();
  }

  /**
   * 删除综合动态（权限）.
   * 
   * @param sameFlag
   * @param dynType
   * @param permission
   * @throws DaoException
   */
  public void deleteComplexDynamicsBySame(String sameFlag, int dynType, int permission, Long groupId)
      throws DaoException {
    String hql =
        "update Dynamic t set t.status=? where t.sameFlag=? and t.dynType=? and t.permission=? and t.groupId=? and t.syncFlag=?";
    super.createQuery(hql, new Object[] {1, sameFlag, dynType, permission, groupId, 1}).executeUpdate();
  }

  /**
   * 删除综合动态.
   * 
   * @param sameFlag
   * @param dynType
   * @throws DaoException
   */
  public void deleteComplexDynamicsBySame(String sameFlag, int dynType, Long groupId) throws DaoException {
    String hql = "update Dynamic t set t.status=? where t.sameFlag=? and t.dynType=? and t.groupId=?";
    super.createQuery(hql, new Object[] {1, sameFlag, dynType, groupId}).executeUpdate();
  }

  /**
   * 屏蔽此类动态.
   * 
   * @param psnId
   * @param dynTypeList
   * @throws DaoException
   */
  public void updateDynamicByDynType(Long receiver, List<Integer> dynTypeList, int visible) throws DaoException {
    StringBuffer hql = new StringBuffer();
    hql.append("update Dynamic t set t.visible=:visible");
    hql.append(" where t.dynType in(:dynTypes) and t.receiver=:receiver and t.status=:status");
    Query query = super.createQuery(hql.toString());
    query.setParameter("visible", visible);
    query.setParameterList("dynTypes", dynTypeList);
    query.setParameter("receiver", receiver);
    query.setParameter("status", 0);
    query.executeUpdate();
  }

  public void updateDynamicByAttId(Long receiver, List<Integer> tmpList, List<Integer> dynTypeList, int visible)
      throws DaoException {
    if (tmpList.size() != 0 || dynTypeList.size() != 0) {
      StringBuffer hql = new StringBuffer();
      hql.append("update Dynamic t set t.visible=:visible where");
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
   * 获取需要清理的动态.
   * 
   * @param marginTime
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Dynamic> getCleanDynamicList(int marginTime, int maxSize) throws DaoException {
    String hql = "from Dynamic t where t.updateDate<=?";
    return super.createQuery(hql, DateUtils.addDays(new Date(), marginTime)).setMaxResults(maxSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Dynamic> getCleanDynamicList2(int marginTime, int maxSize) throws DaoException {
    String hql = "from Dynamic t where t.updateDate<=? and t.status=?";
    return super.createQuery(hql, new Object[] {DateUtils.addDays(new Date(), marginTime), 1}).setMaxResults(maxSize)
        .list();
  }

  /**
   * 修改动态的可见性.
   * 
   * @param oldRelation
   * @param newRelation
   * @param permission
   * @throws DaoException
   */
  public void updateDynamicVisible(int oldRelation, int newRelation, Long producer, Long receiver, int visible)
      throws DaoException {
    StringBuffer hql = new StringBuffer();
    hql.append("update Dynamic t set t.visible=?,t.relation=?");
    hql.append(" where t.producer=? and t.receiver=? and t.receiver!=producer and t.relation=? and t.status=?");
    super.createQuery(hql.toString(), new Object[] {visible, newRelation, producer, receiver, oldRelation, 0})
        .executeUpdate();
  }

  public void updateDynamicVisible(Long producer, Long receiver, int visible, List<Integer> tmpList,
      List<Integer> dynTypeList) throws DaoException {
    StringBuffer hql = new StringBuffer();
    hql.append("update Dynamic t set t.visible=:visible");
    hql.append(" where t.producer=:producer and t.receiver=:receiver and t.status=:status");

    if (tmpList.size() > 0) {
      hql.append(" and t.tmpId not in(:tmpIds)");
    }
    if (dynTypeList.size() > 0) {
      hql.append(" and t.dynType not in(:dynTypes)");
    }

    Query query = super.createQuery(hql.toString());
    query.setParameter("visible", visible);
    query.setParameter("producer", producer);
    query.setParameter("receiver", receiver);
    query.setParameter("status", 0);

    if (tmpList.size() > 0) {
      query.setParameterList("tmpIds", tmpList);
    }
    if (dynTypeList.size() > 0) {
      query.setParameterList("dynTypes", dynTypeList);
    }

    query.executeUpdate();
  }

  /**
   * 更新关系.
   * 
   * @param oldRelation
   * @param newRelation
   * @param producer
   * @param receiver
   * @throws DaoException
   */
  public void updateDynamicRelation(int oldRelation, int newRelation, Long producer, Long receiver)
      throws DaoException {
    String hql =
        "update Dynamic t set t.relation=? where  t.producer=? and t.receiver=? and t.relation=? and t.status=?";
    super.createQuery(hql, new Object[] {newRelation, producer, receiver, oldRelation, 0,}).executeUpdate();
  }

  /**
   * 按照动态时间获取列表.
   * 
   * @param dynDate
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Dynamic> getDynamicListByDate(Long receiver, Date dynDate) throws DaoException {
    String hql = "from Dynamic t where t.receiver=? and t.syncFlag=? and t.dynDate<? and t.groupId=?";
    return super.createQuery(hql, new Object[] {receiver, 1, dynDate, 0l}).list();
  }

  @SuppressWarnings("unchecked")
  public List<Dynamic> getDynamicListOrder(Long receiver, int maxSize) throws DaoException {
    String hql = "from Dynamic t where t.receiver=? and t.syncFlag=? and t.groupId=? order by dynDate desc";
    return super.createQuery(hql, new Object[] {receiver, 1, 0l}).setMaxResults(maxSize).list();
  }

  public boolean isParentById(Long dynId, Date dynDate) throws DaoException {
    String hql = "select count(t.dynId) from Dynamic t where t.dynParentId=? and t.dynDate>=?";
    Long count = super.findUnique(hql, new Object[] {dynId, dynDate});
    if (count != null && count.longValue() > 0l)
      return true;
    else
      return false;
  }

  public int isExistSyncDynamic(Long dynParentId, Long receiverId) {
    String hql = "select count(t.dynId) from Dynamic t where t.dynParentId=? and t.receiver=?";
    Long count = (Long) super.createQuery(hql, new Object[] {dynParentId, receiverId}).uniqueResult();
    return count.intValue();
  }

  /**
   * 查询接收人和产生人的所有动态.
   * 
   * @param receiver
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Dynamic> queryDynamicByReveiver(Long producer, Long receiver) throws DaoException {
    return super.createQuery("from Dynamic t where t.producer=? or t.receiver=?", new Object[] {producer, receiver})
        .list();
  }

  /**
   * 分页查询某种类型的动态.
   * 
   * @param startId
   * @param batchSize
   * @param tmpId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Dynamic> queryDynamicList(Long startId, int batchSize, int tmpId) throws DaoException {
    return super.createQuery(
        "select new Dynamic(t1.dynId, t2.dynJson) from Dynamic t1, DynMsgContent t2 where t1.tmpId=? and t1.dynId>? and t1.dcId = t2.dcId order by t1.dynId asc",
        new Object[] {tmpId, startId}).setMaxResults(batchSize).list();
  }

  /**
   * 批量查询动态信息.
   * 
   * @param dynIdList
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Dynamic> queryDynamic(List<Long> dynIdList, Long psnId) throws DaoException {

    List<Dynamic> list =
        super.createQuery("from Dynamic t where t.dynId in(:dynIdList) and t.status = 0 and t.receiver = :psnId")
            .setParameterList("dynIdList", dynIdList).setParameter("psnId", psnId).list();
    return list;
  }

  /**
   * 获取动态产生者.
   * 
   * @param id
   * @return
   */
  public Long getDynamicProducer(Long id) {

    return super.findUnique("select producer from Dynamic t where dynId = ? ", id);
  }

  /**
   * 获取人员指定数量的动态.
   * 
   * @param psnId
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Dynamic> getDynamicListWithPsnId(Long psnId, int maxSize) {
    String hql =
        "from Dynamic t where t.receiver=? and t.visible=? and t.groupId=? and t.status=? order by t.updateDate desc";

    return super.createQuery(hql, new Object[] {psnId, DynamicConstant.DYN_VISIBLE_TRUE, 0l, 0})
        .setMaxResults(maxSize + 1).list();// 多查询一条
  }

  /**
   * 删除群组动态记录_MJG_SCM-5912
   * 
   * @param groupDynId
   */
  public void delGroupDynById(Long groupDynId) {
    String hql = "delete from Dynamic t where t.dynId=? and t.groupId>0 ";
    super.createQuery(hql, groupDynId).executeUpdate();
  }

  /**
   * 判断动态是否存在于个人动态表.
   * 
   * @param dynId
   * @return true-存在；false-不存在.
   */
  public boolean isDynamicExists(Long dynId) {
    String hql = "select count(t.dynId) from Dynamic t where t.dynId=? ";
    Long count = super.findUnique(hql, new Object[] {dynId});
    if (count != null && count.longValue() > 0l)
      return true;
    else
      return false;
  }

  /**
   * 是否可以删除动态内容.
   * 
   * @param dcId
   * @param psnId
   * @return
   */
  public boolean isAbleToDelDynContent(Long dcId, Long psnId) {
    String hql = "select count(t.dynId) from Dynamic t where t.dcId=? and t.receiver<>?";
    Long count = super.findUnique(hql, dcId, psnId);
    return count != null && count.longValue() > 0 ? false : true;
  }
}
