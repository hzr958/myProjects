package com.smate.center.job.framework.dao;

import com.smate.center.job.common.po.OfflineJobPO;
import com.smate.center.job.framework.dto.OfflineJobDTO;
import com.smate.core.base.exception.DAOException;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.string.StringUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * 简单任务业务信息统计dao
 *
 * @author houchuanjie
 * @date 2017年12月27日 下午3:20:38
 */
@Repository
public class DynamicDataSourceDAO {

  private static final String COUNT_SQL =
      "SELECT count(1) FROM :tableName WHERE :uniqueKey >= :begin AND " + ":uniqueKey < :end";
  private static final String SELECT_BEGIN_END_SQL = "SELECT uk FROM (SELECT ROWNUM AS seq, "
      + ":uniqueKey as uk FROM :tableName WHERE :uniqueKey >= :begin AND :uniqueKey < :end "
      + ":filter ORDER BY :uniqueKey) where seq in (:seqList)";
  private static final String HAS_UNIQUE_KEY_SQL =
      "SELECT 1 FROM :tableName GROUP BY :uniqueKey " + "HAVING count(1) > 1";
  private static final String HAS_TABLE_SQL =
      "select count(1) from user_tables where table_name " + "= :tableName";
  private static final String SELECT_DATA_OBJECT_SQL = "SELECT * FROM :tableName WHERE :uniqueKey"
      + " >= :begin AND :uniqueKey <= :end :filter ORDER BY :uniqueKey";
  protected Logger logger = LoggerFactory.getLogger(getClass());
  protected SessionFactory sessionFactory;
  @Resource(name = "sessionFactoryMap")
  private Map<String, SessionFactory> sessionFactoryMap; // 装载数据源

  /**
   * 根据任务信息，获取任务需要处理的总记录数
   *
   * @author houchuanjie
   * @date 2018年1月4日 下午10:59:07
   */
  public long getRecordCount(OfflineJobDTO jobInfo) throws DAOException {
    try {
      Session session = getSession(jobInfo.getDbSessionEnum());
      String sql = prepareCountSQL(jobInfo.getTableName(), jobInfo.getUniqueKey(),
          jobInfo.getFilter());
      SQLQuery sqlQuery = session.createSQLQuery(sql);
      sqlQuery.setParameter("begin", jobInfo.getBegin()).setParameter("end", jobInfo.getEnd());
      // 总记录数
      BigDecimal count = (BigDecimal) sqlQuery.uniqueResult();
      if (count != null && count.longValue() > 0) {
        return count.longValue();
      }
      return 0;
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 根据任务信息，从相关业务表，对指定主键进行排序，查出rownum，筛选出rownum在seqList中的记录的主键。
   *
   * @param jobInfo 任务信息
   * @param seqList 要查询记录的rownum的集合
   * @return 指定rownum的业务表记录的主键集合
   * @author houchuanjie
   * @date 2018年1月4日 下午11:07:19
   */
  @SuppressWarnings("unchecked")
  public List<Long> getUniqueKeyList(OfflineJobDTO jobInfo, List<Long> seqList) {
    Session session = getSession(jobInfo.getDbSessionEnum());
    String sql = prepareSelectSQL(SELECT_BEGIN_END_SQL, jobInfo.getTableName(),
        jobInfo.getUniqueKey(), jobInfo.getFilter());
    Query sqlQuery = session.createSQLQuery(sql).addScalar("uk", StandardBasicTypes.LONG);
    sqlQuery.setParameter("begin", jobInfo.getBegin()).setParameter("end", jobInfo.getEnd())
        .setParameterList("seqList", seqList);
    // 查询每个分区开始和结束的唯一键值（id）
    List<Long> uniqueKeyList = (List<Long>) sqlQuery.list();
    return uniqueKeyList;
  }

  /**
   * 检查给定数据库的表的唯一键是否唯一
   *
   * @param dbSessionEnum 数据源枚举
   * @param tableName 表名
   * @param uniqueKey 唯一键列名
   * @author houchuanjie
   * @date 2017年12月28日 下午2:15:22
   */
  public boolean isUniqueKey(DBSessionEnum dbSessionEnum, String tableName, String uniqueKey) {
    Session session = getSession(dbSessionEnum);
    SQLQuery sqlQuery = session
        .createSQLQuery(prepareSQL(HAS_UNIQUE_KEY_SQL, tableName, uniqueKey));
    List list = sqlQuery.list();
    if (CollectionUtils.isNotEmpty(list)) {
      return false;
    }
    return true;
  }

  /**
   * 判断给定数据源是否有指定的表
   */
  public boolean hasTable(DBSessionEnum dbSessionEnum, String tableName) {
    Session session = getSession(dbSessionEnum);
    List list = session.createSQLQuery(HAS_TABLE_SQL).setParameter("tableName", tableName).list();
    if (CollectionUtils.isEmpty(list)) {
      return false;
    }
    return true;
  }

  private String prepareSelectSQL(String sql, String tableName, String uniqueKey, String filter) {
    sql = prepareSQL(sql, tableName, uniqueKey);
    if (StringUtils.isNotBlank(filter)) {
      sql = sql.replace(":filter", " AND (" + filter + ")");
    } else {
      sql = sql.replace(":filter", "");
    }
    return sql;
  }

  private String prepareCountSQL(String tableName, String uniqueKey, String filter) {
    String sql = prepareSQL(COUNT_SQL, tableName, uniqueKey);
    if (StringUtils.isNotBlank(filter)) {
      sql += " AND (" + filter + ")";
    }
    return sql;
  }

  private String prepareSQL(String sql, String tableName, String uniqueKey) {
    return sql.replaceAll(":tableName", tableName).replaceAll(":uniqueKey", uniqueKey);
  }

  protected Session getSession(DBSessionEnum dbSessionEnum) {
    this.sessionFactory = sessionFactoryMap.get(dbSessionEnum.toString());
    return sessionFactory.getCurrentSession();
  }

  public List<?> getJobData(long begin, long end, int batchSize, Class<?> persistentClass,
      OfflineJobPO offlineJobPO) throws DAOException {
    try {
      Session session = getSession(offlineJobPO.getDbSessionEnum());
      String sql = prepareSelectSQL(SELECT_DATA_OBJECT_SQL, offlineJobPO.getTableName(),
          offlineJobPO.getUniqueKey(), offlineJobPO.getFilter());
      Query query = session.createSQLQuery(sql).addEntity(persistentClass)
          .setParameter("begin", begin).setParameter("end", end).setMaxResults(batchSize);
      return query.list();
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 判断是否有配置对应数据源
   * @param dbSessionEnum
   * @return
   */
  public boolean hasDbSessionEnum(DBSessionEnum dbSessionEnum) {
    return Objects.nonNull(sessionFactoryMap.get(dbSessionEnum.toString()));
  }
}
