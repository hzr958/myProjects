package com.smate.center.job.framework.dao;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.enums.JobWeightEnum;
import com.smate.center.job.common.po.OnlineJobPO;
import com.smate.center.job.web.support.hibernate.CriteriaQueryBuilder;
import com.smate.core.base.exception.DAOException;
import com.smate.core.base.utils.collections.ListUtils;
import com.smate.core.base.utils.data.SnsHibernateDao;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

/**
 * 在线任务DAO
 *
 * @author houchuanjie
 * @date 2018年4月24日 下午5:51:32
 */
@Repository
public class OnlineJobDAO extends SnsHibernateDao<OnlineJobPO, String> {

  private static final String HQL_QUERY_ENABLE_LIST = "select o from OnlineJobPO o where exists"
      + "(select 1 from OnlineJobConfigPO c where c.weight = :weight  and c.enable = true "
      + "and c.jobName = o.jobName) and o.status in(:statusList) order by o.priority, o.gmtModified";
  private static final String COUNT_BY_JOB_NAME_AND_STATUS =
      "select count(1) from OnlineJobPO where jobName = :jobName and status in (:statusList)";

  /**
   * 获取指定权重指定数目的、开启的、未执行的在线任务列表，按优先级，修改时间升序排序
   *
   * @param weight 权重
   * @param size 要获取的任务数
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<OnlineJobPO> getEnableList(JobWeightEnum weight, int size) {
    try {
      List<OnlineJobPO> list = getSession().createQuery(HQL_QUERY_ENABLE_LIST)
          .setParameter("weight", weight)
          .setParameterList("statusList", Arrays.asList(JobStatusEnum.UNPROCESS))
          .setMaxResults(size)
          .list();
      return list;
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 批量保存或更新
   *
   * @param onlineJobs
   */
  public void batchUpdate(List<OnlineJobPO> onlineJobs) {
    Session session = getSession();
    onlineJobs.forEach(o -> {
      o.setGmtModified(new Date());
      session.update(o);
    });
  }

  /**
   * @param idList
   */
  public void batchDelete(List<String> idList) {
    try {
      String hql = "delete from OnlineJobPO t where t.id in(:idList)";
      List<String>[] idLists = ListUtils.split(idList, 1000);
      Arrays.stream(idLists).forEach(
          list -> getSession().createQuery(hql).setParameterList("idList", list).executeUpdate());
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 根据任务名称统计出错的任务数
   *
   * @param jobName
   * @return
   */
  public Long countFailingByJobName(String jobName) throws DAOException {
    try {
      Long count = (Long) getSession().createQuery(COUNT_BY_JOB_NAME_AND_STATUS)
          .setParameter("jobName", jobName)
          .setParameterList("statusList", Arrays.asList(JobStatusEnum.FAILED)).uniqueResult();
      return count;
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 根据任务名称统计正常的任务数
   *
   * @param jobName
   * @return
   */
  public Long countNormalByJobName(String jobName) throws DAOException {
    try {
      EnumSet<JobStatusEnum> statusEnums = EnumSet.allOf(JobStatusEnum.class);
      statusEnums.remove(JobStatusEnum.FAILED);
      Long count = (Long) getSession().createQuery(COUNT_BY_JOB_NAME_AND_STATUS)
          .setParameter("jobName", jobName)
          .setParameterList("statusList", statusEnums).uniqueResult();
      return count;
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  public void batchSave(List<OnlineJobPO> list) {
    Session session = getSession();
    list.forEach(o -> {
      Date now = new Date();
      o.setGmtCreate(now);
      o.setGmtModified(now);
      session.save(o);
    });
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
  public List<OnlineJobPO> search(Integer rows, Integer pageNo, Order order,
      Criterion... conditions) throws DAOException {
    try {
      return new CriteriaQueryBuilder<OnlineJobPO>(getSession(), OnlineJobPO.class)
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
      return new CriteriaQueryBuilder(getSession(), OnlineJobPO.class)
          .addCriterions(conditions).buildQueryCount();
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  public void save(OnlineJobPO onlineJobPO) throws DAOException {
    try {
      Date now = new Date();
      onlineJobPO.setGmtModified(now);
      onlineJobPO.setGmtCreate(now);
      getSession().save(onlineJobPO);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  public void update(OnlineJobPO onlineJobPO) throws DAOException {
    try {
      onlineJobPO.setGmtModified(new Date());
      getSession().update(onlineJobPO);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }
}
