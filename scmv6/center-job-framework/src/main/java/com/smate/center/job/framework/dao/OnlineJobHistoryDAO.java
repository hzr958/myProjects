package com.smate.center.job.framework.dao;

import com.smate.center.job.common.po.OnlineJobHistoryPO;
import com.smate.center.job.web.support.hibernate.CriteriaQueryBuilder;
import com.smate.core.base.exception.DAOException;
import com.smate.core.base.utils.data.SnsHibernateDao;
import java.util.Date;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

/**
 * 在线任务执行记录表Dao
 *
 * @author houchuanjie
 * @date 2018年4月24日 下午5:54:17
 */
@Repository
public class OnlineJobHistoryDAO extends SnsHibernateDao<OnlineJobHistoryPO, String> {

  private static final String COUNT_BY_JOB_NAME = "select count(1) from OnlineJobHistoryPO "
      + "where jobName = :jobName";

  /**
   * 批量插入
   *
   * @param historyList
   */
  public void batchSave(List<OnlineJobHistoryPO> historyList) {
    try {
      historyList.forEach(o -> {
        Date now = new Date();
        o.setGmtCreate(now);
        o.setGmtModified(now);
        save(o);
      });
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 根据任务名称统计
   *
   * @param jobName
   * @return
   */
  public Long countByJobName(String jobName) throws DAOException {
    try {
      Long count = (Long) getSession().createQuery(COUNT_BY_JOB_NAME)
          .setParameter("jobName", jobName).uniqueResult();
      return count;
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 根据查询条件获取结果集
   *
   * @param rows 查询的记录数
   * @param pageNo 查询的页数
   * @param order 排序规则
   * @param conditions 查询条件列表
   * @return
   * @throws DAOException
   */
  public List<OnlineJobHistoryPO> search(Integer rows, Integer pageNo, Order order,
      Criterion... conditions) throws DAOException {
    try {
      return new CriteriaQueryBuilder<OnlineJobHistoryPO>(getSession(), OnlineJobHistoryPO.class)
          .addCriterions(conditions)
          .addOrder(order).setPageQuery(pageNo, rows).buildQueryList();
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 根据查询条件获取数量
   *
   * @param conditions 查询条件列表
   * @return
   */
  public Long getTotalCount(Criterion... conditions) {
    try {
      return new CriteriaQueryBuilder(getSession(), OnlineJobHistoryPO.class)
          .addCriterions(conditions).buildQueryCount();
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  public void batchDelete(List<String> idList) {
    try {
      String hql = "delete from OnlineJobHistoryPO t where t.id in(:idList)";
      getSession().createQuery(hql).setParameterList("idList", idList).executeUpdate();
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }
}
