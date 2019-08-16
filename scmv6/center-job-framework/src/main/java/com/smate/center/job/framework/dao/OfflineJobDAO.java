package com.smate.center.job.framework.dao;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.enums.JobWeightEnum;
import com.smate.center.job.common.po.OfflineJobPO;
import com.smate.center.job.web.support.hibernate.CriteriaQueryBuilder;
import com.smate.core.base.exception.DAOException;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.sun.istack.internal.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * TaskInfo表Dao
 *
 * @author houchuanjie
 * @date 2017年12月28日 下午5:07:16
 */
@Repository
public class OfflineJobDAO extends SnsHibernateDao<OfflineJobPO, String> {

  private static final String HQL_QUERY_ENABLE_LIST = "from OfflineJobPO where enable = true and weight = :weight and status in(:statusList) order by priority, gmtModified";
  private static final String HQL_SELECT_BY_ID_LIST = "from OfflineJobPO where id in (:idList)";

  /**
   * 获取指定权重指定数量的、开启的、未执行或执行完毕的任务列表，按优先级，修改时间升序排序
   *
   * @return
   * @author houchuanjie
   * @date 2018年1月6日 下午2:34:47
   */
  @SuppressWarnings("unchecked")
  public List<OfflineJobPO> getEnableList(JobWeightEnum weight, int size) {
    List<OfflineJobPO> list = (List<OfflineJobPO>) getSession().createQuery(HQL_QUERY_ENABLE_LIST)
        .setParameter("weight", weight)
        .setParameterList("statusList", Arrays.asList(JobStatusEnum.UNPROCESS))
        .setMaxResults(size).list();
    return list;

  }

  /**
   * 批量更新任务信息
   *
   * @param list
   */
  public void batchUpdate(List<OfflineJobPO> list) {
    Session session = getSession();
    list.forEach(o -> {
      o.setGmtModified(new Date());
      session.update(o);
    });
  }

  /**
   * 分页查询，带检索条件
   *
   * @param rows 获取的一页记录行数
   * @param pageNo 要获取的页码
   * @param order 排序规则
   * @param conditions 过滤条件
   * @return
   */
  public List<OfflineJobPO> search(@NotNull Integer rows, @NotNull Integer pageNo,
      Order order, Criterion... conditions) throws DAOException {
    try {
      return new CriteriaQueryBuilder<OfflineJobPO>(getSession(), OfflineJobPO.class)
          .addCriterions(conditions).addOrder(order).setPageQuery((pageNo - 1) * rows, rows)
          .buildQueryList();
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 获取表总记录数
   *
   * @param conditions
   * @return
   */
  public Long getTotalCount(Criterion... conditions) {
    try {
      return new CriteriaQueryBuilder<OfflineJobPO>(getSession(), OfflineJobPO.class)
          .addCriterions(conditions).buildQueryCount();
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 批量删除TaskInfo
   *
   * @param idList 要删除的id列表
   * @author houchuanjie
   * @date 2018年3月1日 下午2:23:46
   */
  public void batchDelete(List<String> idList) throws DAOException {
    String hql = "delete OfflineJobPO where id in (:ids)";
    try {
      getSession().createQuery(hql).setParameterList("ids", idList).executeUpdate();
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 更新
   *
   * @param offlineJobPO
   * @author houchuanjie
   * @date 2018年3月1日 下午4:32:29
   */
  public void update(OfflineJobPO offlineJobPO) throws DAOException {
    try {
      offlineJobPO.setGmtModified(new Date());
      getSession().merge(offlineJobPO);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 根据id获取TaskInfo，瞬时态的，不受Hibernate Session生命周期管理
   *
   * @param id
   * @return
   * @author houchuanjie
   * @date 2018年3月2日 下午3:16:46
   */
  public OfflineJobPO getStateless(String id) {
    StatelessSession statelessSession = getSessionFactory().openStatelessSession();
    OfflineJobPO offlineJobPO = (OfflineJobPO) statelessSession.get(OfflineJobPO.class, id);
    statelessSession.close();
    return offlineJobPO;
  }

  /**
   * 根据id列表获取任务信息
   *
   * @param idList
   * @return
   * @author houchuanjie
   * @date 2018年3月2日 下午7:09:49
   */
  @SuppressWarnings("unchecked")
  public List<OfflineJobPO> getByIds(List<String> idList) {
    List<OfflineJobPO> list = getStatelessSession().createQuery(HQL_SELECT_BY_ID_LIST)
        .setParameterList("idList", idList)
        .list();
    return list;
  }

  /**
   * 新增保存
   *
   * @param offlineJobPO
   * @throws DAOException
   */
  public void save(@NotNull OfflineJobPO offlineJobPO) throws DAOException {
    try {
      Assert.notNull(offlineJobPO, "新增或修改的OfflineJobPO不能为null！");
      Date now = new Date();
      offlineJobPO.setGmtCreate(now);
      offlineJobPO.setGmtModified(now);
      getSession().save(offlineJobPO);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  private StatelessSession getStatelessSession() {
    return getSessionFactory().openStatelessSession();
  }
}
