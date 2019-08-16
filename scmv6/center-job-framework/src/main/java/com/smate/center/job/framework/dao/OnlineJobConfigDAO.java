package com.smate.center.job.framework.dao;

import com.smate.center.job.common.po.OnlineJobConfigPO;
import com.smate.center.job.web.support.hibernate.CriteriaQueryBuilder;
import com.smate.core.base.exception.DAOException;
import com.smate.core.base.utils.data.SnsHibernateDao;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * 在线任务配置表DAO
 *
 * @author houchuanjie
 * @date 2018年4月24日 下午5:52:48
 */
@Repository
public class OnlineJobConfigDAO extends SnsHibernateDao<OnlineJobConfigPO, String> {

  /**
   * 批量保存或更新
   *
   * @param onlineJobConfigPOS
   */
  public void batchUpdate(List<OnlineJobConfigPO> onlineJobConfigPOS) {
    Assert.isTrue(!CollectionUtils.isEmpty(onlineJobConfigPOS), "批量更新的list不能为空！");
    Session session = getSession();
    onlineJobConfigPOS.forEach(o -> {
      o.setGmtModified(new Date());
      session.update(o);
    });
  }

  /**
   * 分页查询
   *
   * @param rows 要获取的条数
   * @param pageNo 要获取的页码
   * @param order 排序规则
   * @param conditions 过滤条件
   * @return
   * @throws DAOException
   */
  public List<OnlineJobConfigPO> search(Integer rows, Integer pageNo, Order order,
      Criterion... conditions)
      throws DAOException {
    try {
      return new CriteriaQueryBuilder<OnlineJobConfigPO>(getSession(), OnlineJobConfigPO.class)
          .addCriterions(conditions).addOrder(order).setPageQuery(pageNo, rows).buildQueryList();
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 获取表总记录数
   *
   * @return
   */
  public Long getTotalCount(Criterion... conditions) throws DAOException {
    try {
      return new CriteriaQueryBuilder(getSession(), OnlineJobConfigPO.class)
          .addCriterions(conditions).buildQueryCount();
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  @Override
  public void save(OnlineJobConfigPO onlineJobConfigPO) throws DAOException {
    try {
      Date now = new Date();
      onlineJobConfigPO.setGmtCreate(now);
      onlineJobConfigPO.setGmtModified(now);
      getSession().save(onlineJobConfigPO);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 批量删除
   *
   * @param idList
   */
  public void batchDelete(List<String> idList) throws DAOException {
    String hql = "delete OnlineJobConfigPO where id in (:ids)";
    try {
      getSession().createQuery(hql).setParameterList("ids", idList).executeUpdate();
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 更新对象
   *
   * @param persistent
   * @throws DAOException
   */
  public void update(OnlineJobConfigPO persistent) throws DAOException {
    try {
      Assert.notNull(persistent, "更新的对象不能为空！");
      persistent.setGmtModified(new Date());
      getSession().update(persistent);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }
}
