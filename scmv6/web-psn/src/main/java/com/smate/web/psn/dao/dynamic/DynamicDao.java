package com.smate.web.psn.dao.dynamic;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.dynamic.Dynamic;

/**
 * 动态DAO
 *
 * @author wsn
 * @createTime 2017年6月21日 下午12:03:52
 *
 */
@Repository
public class DynamicDao extends SnsHibernateDao<Dynamic, Long> {

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
        "update Dynamic t set t.relation=:newRelation where  t.producer=:producer and t.receiver=:receiver and t.relation=:oldRelation and t.status=:status";
    super.createQuery(hql).setParameter("newRelation", newRelation).setParameter("producer", producer)
        .setParameter("receiver", receiver).setParameter("oldRelation", oldRelation).setParameter("status", 0)
        .executeUpdate();
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
}
