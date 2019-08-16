package com.smate.core.base.utils.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.CharacterType;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.InterruptibleBatchPreparedStatementSetter;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SqlProvider;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.util.Assert;

import com.smate.core.base.utils.common.ReflectionUtils;
import com.smate.core.base.utils.exception.DAOException;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.PropertyFilter;
import com.smate.core.base.utils.model.PropertyFilter.MatchType;

/**
 * 封装带分页功能的Hibernat泛型基类. 扩展功能包括分页查询,按属性过滤条件列表查询. 可在Service层直接使用,也可以扩展泛型DAO子类使用,见两个构造函数的注释.
 * 
 * @param <T> DAO操作的对象类型
 * @param <PK> 主键类型
 */
public abstract class HibernateDao<T, PK extends Serializable> extends SimpleHibernateDao<T, PK> {
  /**
   * 用于Dao层子类使用的构造函数. 通过子类的泛型定义取得对象类型Class. eg. public class UserDao extends HibernateDao<User, Long>{
   * }
   */
  public HibernateDao() {
    super();
  }

  /**
   * 用于省略Dao层, Service层直接使用通用HibernateDao的构造函数. 在构造函数中定义对象类型Class. eg. HibernateDao<User, Long>
   * userDao = new HibernateDao<User, Long>(sessionFactory, User.class);
   */
  public HibernateDao(final SessionFactory sessionFactory, final Class<T> entityClass) {
    super(sessionFactory, entityClass);
  }

  // 分页查询函数 //

  /**
   * 分页获取全部对象.
   */
  public Page<T> getAll(final Page<T> page) throws DAOException {
    return findPage(page);
  }

  /**
   * 按HQL分页查询.
   * 
   * @param page 分页参数.不支持其中的orderBy参数.
   * @param hql hql语句.
   * @param values 数量可变的查询参数,按顺序绑定.
   * @return 分页查询结果, 附带结果列表及所有查询时的参数.
   */
  @SuppressWarnings("unchecked")
  public Page<T> findPage(final Page<T> page, final String hql, final Object... values) throws DAOException {
    Assert.notNull(page, "page不能为空");

    try {
      Query q = createQuery(hql, values);

      if (page.isAutoCount()) {
        long totalCount = countHqlResult(hql, values);
        page.setTotalCount(totalCount);
      }

      setPageParameter(q, page);
      List result = q.list();
      page.setResult(result);
      return page;
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 按HQL分页查询.
   * 
   * @param page 分页参数.
   * @param hql hql语句.
   * @param values 命名参数,按名称绑定.
   * @return 分页查询结果, 附带结果列表及所有查询时的参数.
   */
  @SuppressWarnings("unchecked")
  public Page<T> findPage(final Page<T> page, final String hql, final Map<String, Object> values) throws DAOException {
    Assert.notNull(page, "page不能为空");

    try {
      Query q = createQuery(hql, values);

      if (page.isAutoCount()) {
        long totalCount = countHqlResult(hql, values);
        page.setTotalCount(totalCount);
      }

      setPageParameter(q, page);
      List result = q.list();
      page.setResult(result);
      return page;
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 按Criteria分页查询.
   * 
   * @param page 分页参数.
   * @param criterions 数量可变的Criterion.
   * @return 分页查询结果.附带结果列表及所有查询时的参数.
   */
  @SuppressWarnings("unchecked")
  public Page<T> findPage(final Page<T> page, final Criterion... criterions) throws DAOException {
    Assert.notNull(page, "page不能为空");

    try {
      Criteria c = createCriteria(criterions);

      if (page.isAutoCount()) {
        int totalCount = countCriteriaResult(c);
        page.setTotalCount(totalCount);
      }

      setPageParameter(c, page);
      List result = c.list();
      page.setResult(result);
      return page;
    } catch (HibernateException e) {
      throw new DAOException(e);
    }
  }

  /**
   * 设置分页参数到Query对象,辅助函数.
   */
  protected Query setPageParameter(final Query q, final Page<T> page) {
    // hibernate的firstResult的序号从0开始
    q.setFirstResult(page.getFirst() - 1);
    q.setMaxResults(page.getPageSize());
    return q;
  }

  /**
   * 设置分页参数到Criteria对象,辅助函数.
   */
  protected Criteria setPageParameter(final Criteria c, final Page<T> page) {
    // hibernate的firstResult的序号从0开始
    c.setFirstResult(page.getFirst() - 1);
    c.setMaxResults(page.getPageSize());

    if (page.isOrderBySetted()) {
      String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
      String[] orderArray = StringUtils.split(page.getOrder(), ',');

      Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");

      for (int i = 0; i < orderByArray.length; i++) {
        if (Page.ASC.equals(orderArray[i])) {
          c.addOrder(Order.asc(orderByArray[i]));
        } else {
          c.addOrder(Order.desc(orderByArray[i]));
        }
      }
    }
    return c;
  }

  /**
   * 执行count查询获得本次Hql查询所能获得的对象总数. 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
   */
  protected long countHqlResult(final String hql, final Object... values) {
    Long count = 0L;
    String fromHql = hql;
    // select子句与order by子句会影响count查询,进行简单的排除.
    fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
    fromHql = StringUtils.substringBefore(fromHql, "order by");

    String countHql = "select count(*) " + fromHql;

    try {
      count = findUnique(countHql, values);
    } catch (Exception e) {
      throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
    }
    return count;
  }

  /**
   * 执行count查询获得本次Hql查询所能获得的对象总数. 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
   */
  protected long countHqlResult(final String hql, final Map<String, Object> values) {
    Long count = 0L;
    String fromHql = hql;
    // select子句与order by子句会影响count查询,进行简单的排除.
    fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
    fromHql = StringUtils.substringBefore(fromHql, "order by");

    String countHql = "select count(*) " + fromHql;

    try {
      count = findUnique(countHql, values);
    } catch (Exception e) {
      throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
    }

    return count;
  }

  /**
   * 执行count查询获得本次Criteria查询所能获得的对象总数.
   */
  @SuppressWarnings("unchecked")
  protected int countCriteriaResult(final Criteria c) {
    CriteriaImpl impl = (CriteriaImpl) c;

    // 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
    Projection projection = impl.getProjection();
    ResultTransformer transformer = impl.getResultTransformer();

    List<CriteriaImpl.OrderEntry> orderEntries = null;
    try {
      orderEntries = (List) ReflectionUtils.getFieldValue(impl, "orderEntries");
      ReflectionUtils.setFieldValue(impl, "orderEntries", new ArrayList());
    } catch (Exception e) {
      logger.error("不可能抛出的异常:{}", e.getMessage());
    }

    // 执行Count查询
    Object obj = c.setProjection(Projections.rowCount()).uniqueResult();
    int totalCount = Integer.valueOf(String.valueOf(obj));

    // 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
    c.setProjection(projection);

    if (projection == null) {
      c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
    }
    if (transformer != null) {
      c.setResultTransformer(transformer);
    }
    try {
      ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
    } catch (Exception e) {
      logger.error("不可能抛出的异常:{}", e.getMessage());
    }

    return totalCount;
  }

  // -- 属性过滤条件(PropertyFilter)查询函数 --//

  /**
   * 按属性查找对象列表,支持多种匹配方式.
   * 
   * @param matchType 匹配方式,目前支持的取值见PropertyFilter的MatcheType enum.
   */
  public List<T> findBy(final String propertyName, final Object value, final MatchType matchType) throws DAOException {
    try {
      Criterion criterion = buildPropertyFilterCriterion(propertyName, value, matchType);
      return find(criterion);
    } catch (DAOException e) {
      throw new DAOException(e);
    }
  }

  /**
   * 按属性过滤条件列表查找对象列表.
   */
  public List<T> find(List<PropertyFilter> filters) throws DAOException {
    try {
      Criterion[] criterions = buildPropertyFilterCriterions(filters);
      return find(criterions);
    } catch (DAOException e) {
      throw new DAOException(e);
    }
  }

  /**
   * 按属性过滤条件列表分页查找对象.
   */
  public Page<T> findPage(final Page<T> page, final List<PropertyFilter> filters) throws DAOException {
    try {
      Criterion[] criterions = buildPropertyFilterCriterions(filters);
      return findPage(page, criterions);
    } catch (DAOException e) {
      throw new DAOException(e);
    }
  }

  /**
   * 按属性条件列表创建Criterion数组,辅助函数.
   */
  protected Criterion[] buildPropertyFilterCriterions(final List<PropertyFilter> filters) {
    List<Criterion> criterionList = new ArrayList<Criterion>();
    for (PropertyFilter filter : filters) {
      if (!filter.isMultiProperty()) { // 只有一个属性需要比较的情况.
        Criterion criterion =
            buildPropertyFilterCriterion(filter.getPropertyName(), filter.getPropertyValue(), filter.getMatchType());
        criterionList.add(criterion);
      } else {// 包含多个属性需要比较的情况,进行or处理.
        Disjunction disjunction = Restrictions.disjunction();
        for (String param : filter.getPropertyNames()) {
          Criterion criterion = buildPropertyFilterCriterion(param, filter.getPropertyValue(), filter.getMatchType());
          disjunction.add(criterion);
        }
        criterionList.add(disjunction);
      }
    }
    return criterionList.toArray(new Criterion[criterionList.size()]);
  }

  /**
   * 按属性条件参数创建Criterion,辅助函数.
   */
  protected Criterion buildPropertyFilterCriterion(final String propertyName, final Object propertyValue,
      final MatchType matchType) {
    Assert.hasText(propertyName, "propertyName不能为空");
    Criterion criterion = null;
    try {

      // 根据MatchType构造criterion
      if (MatchType.EQ.equals(matchType)) {
        criterion = Restrictions.eq(propertyName, propertyValue);
      } else if (MatchType.LIKE.equals(matchType)) {
        criterion = Restrictions.like(propertyName, (String) propertyValue, MatchMode.ANYWHERE);
      } else if (MatchType.LE.equals(matchType)) {
        criterion = Restrictions.le(propertyName, propertyValue);
      } else if (MatchType.LT.equals(matchType)) {
        criterion = Restrictions.lt(propertyName, propertyValue);
      } else if (MatchType.GE.equals(matchType)) {
        criterion = Restrictions.ge(propertyName, propertyValue);
      } else if (MatchType.GT.equals(matchType)) {
        criterion = Restrictions.gt(propertyName, propertyValue);
      }
    } catch (Exception e) {
      throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
    }
    return criterion;
  }

  /**
   * 判断对象的属性值在数据库内是否唯一. 在修改对象的情景下,如果属性新修改的值(value)等于属性原来的值(orgValue)则不作比较.
   */
  public boolean isPropertyUnique(final String propertyName, final Object newValue, final Object oldValue) {
    if (newValue == null || newValue.equals(oldValue)) {
      return true;
    }
    Object object = findUniqueBy(propertyName, newValue);
    return (object == null);
  }

  /**
   * 仿spring JdbcTemplate 的 queryForList 方法.
   * 
   * @param sql
   * @param objects
   * @return
   */
  @SuppressWarnings("unchecked")
  public List queryForList(String sql, Object[] objects) throws DAOException {
    try {
      Session session = this.getSession();

      SQLQuery sqlQuery = (SQLQuery) session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

      if (objects != null) {
        sqlQuery.setParameters(objects, this.findTypes(objects));
      }

      return sqlQuery.list();
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  public List queryForList(String sql, Object[] objects, int maxSize, int firstResult) throws DAOException {
    try {
      Session session = this.getSession();

      SQLQuery sqlQuery = (SQLQuery) session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

      if (objects != null) {
        sqlQuery.setParameters(objects, this.findTypes(objects));
      }

      sqlQuery.setMaxResults(maxSize);
      sqlQuery.setFirstResult(firstResult);

      return sqlQuery.list();
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 仿spring JdbcTemplate 的 queryForList 方法.
   * 
   * @param sql
   * @return
   */
  @SuppressWarnings("unchecked")
  public List queryForList(String sql) throws DAOException {
    return this.queryForList(sql, null);
  }

  /**
   * 仿spring JdbcTemplate 的 queryForInt 方法.
   * 
   * @param sql
   * @param objects
   * @return
   */
  public int queryForInt(String sql, Object[] objects) throws DAOException {
    try {
      Session session = this.getSession();

      SQLQuery sqlQuery = (SQLQuery) session.createSQLQuery(sql);

      if (objects != null) {
        sqlQuery.setParameters(objects, this.findTypes(objects));
      }
      Object obj = sqlQuery.uniqueResult();
      String rsValue = "";
      if (obj instanceof Map) {
        Map result = (Map) obj;
        for (Iterator iterator = result.keySet().iterator(); iterator.hasNext();) {
          Object key = (Object) iterator.next();
          rsValue = ObjectUtils.toString(result.get(key));
        }
      } else {
        rsValue = ObjectUtils.toString(obj);
      }
      return NumberUtils.toInt(rsValue);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 仿spring JdbcTemplate 的 queryForInt 方法.
   * 
   * @param sql
   * @return
   */
  public int queryForInt(String sql) throws DAOException {

    return this.queryForInt(sql, null);
  }

  /**
   * 仿spring JdbcTemplate 的 queryForLong 方法.
   * 
   * @param sql
   * @param objects
   * @return
   */
  public long queryForLong(String sql, Object[] objects) throws DAOException {
    try {
      Session session = this.getSession();

      SQLQuery sqlQuery = (SQLQuery) session.createSQLQuery(sql);

      if (objects != null) {
        sqlQuery.setParameters(objects, this.findTypes(objects));
      }
      Object obj = sqlQuery.uniqueResult();
      String rsValue = "";
      if (obj instanceof Map) {
        Map result = (Map) obj;
        for (Iterator iterator = result.keySet().iterator(); iterator.hasNext();) {
          Object key = (Object) iterator.next();
          rsValue = ObjectUtils.toString(result.get(key));
        }
      } else {
        rsValue = ObjectUtils.toString(obj);
      }

      return NumberUtils.toLong(rsValue);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 仿spring JdbcTemplate 的 queryForLong 方法.
   * 
   * @param sql
   * @return
   */
  public long queryForLong(String sql) throws DAOException {

    return this.queryForLong(sql, null);
  }

  /**
   * 仿spring JdbcTemplate 的 update 方法.
   * 
   * @param sql
   * @param objects
   * @return
   */
  public int update(String sql, Object[] objects) throws DAOException {
    try {
      Session session = this.getSession();

      SQLQuery sqlQuery = (SQLQuery) session.createSQLQuery(sql);
      if (objects != null) {
        sqlQuery.setParameters(objects, this.findTypes(objects));
      }

      int i = sqlQuery.executeUpdate();

      return i;
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 仿spring JdbcTemplate 的 update 方法.
   * 
   * @param sql
   * @return
   */
  public int update(String sql) throws DAOException {

    return this.update(sql, null);
  }

  /**
   * 仿spring JdbcTemplate 的 batchUpdate 方法.
   * 
   * @param sql
   * @param pss
   * @return
   * @throws SQLException
   * @throws DataAccessException
   */
  @SuppressWarnings("rawtypes")
  public int[] batchUpdate(String sql, final BatchPreparedStatementSetter pss)
      throws DataAccessException, SQLException, DAOException {
    if (logger.isDebugEnabled()) {
      logger.debug("Executing SQL batch update [" + sql + "]");
    }

    try {
      return (int[]) execute(sql, new PreparedStatementCallback() {
        @Override
        public Object doInPreparedStatement(PreparedStatement ps) throws SQLException {
          try {
            int batchSize = pss.getBatchSize();
            InterruptibleBatchPreparedStatementSetter ipss = (pss instanceof InterruptibleBatchPreparedStatementSetter
                ? (InterruptibleBatchPreparedStatementSetter) pss
                : null);
            if (JdbcUtils.supportsBatchUpdates(ps.getConnection())) {
              for (int i = 0; i < batchSize; i++) {
                pss.setValues(ps, i);
                if (ipss != null && ipss.isBatchExhausted(i)) {
                  break;
                }
                ps.addBatch();
              }
              return ps.executeBatch();
            } else {
              List rowsAffected = new ArrayList();
              for (int i = 0; i < batchSize; i++) {
                pss.setValues(ps, i);
                if (ipss != null && ipss.isBatchExhausted(i)) {
                  break;
                }
                rowsAffected.add(new Integer(ps.executeUpdate()));
              }
              int[] rowsAffectedArray = new int[rowsAffected.size()];
              for (int i = 0; i < rowsAffectedArray.length; i++) {
                rowsAffectedArray[i] = ((Integer) rowsAffected.get(i)).intValue();
              }
              return rowsAffectedArray;
            }
          } finally {
            if (pss instanceof ParameterDisposer) {
              ((ParameterDisposer) pss).cleanupParameters();
            }
          }
        }
      });
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  private Object execute(String sql, PreparedStatementCallback action) throws DAOException {
    try {
      return execute(new SimplePreparedStatementCreator(sql), action);
    } catch (SQLException e) {
      throw new DAOException(e);
    }
  }

  /**
   * Simple adapter for PreparedStatementCreator, allowing to use a plain SQL statement.
   */
  private static class SimplePreparedStatementCreator implements PreparedStatementCreator, SqlProvider {

    private final String sql;

    public SimplePreparedStatementCreator(String sql) {
      Assert.notNull(sql, "SQL must not be null");
      this.sql = sql;
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
      return con.prepareStatement(this.sql);
    }

    @Override
    public String getSql() {
      return this.sql;
    }
  }

  // -------------------------------------------------------------------------
  // Methods dealing with prepared statements
  // -------------------------------------------------------------------------

  private Object execute(PreparedStatementCreator psc, PreparedStatementCallback action)
      throws DataAccessException, SQLException {

    Assert.notNull(psc, "PreparedStatementCreator must not be null");
    Assert.notNull(action, "Callback object must not be null");
    Session session = super.getSession();
    Connection con = SessionFactoryUtils.getDataSource(getSessionFactory()).getConnection();
    PreparedStatement ps = null;
    try {

      Object result = action.doInPreparedStatement(ps);

      return result;
    } catch (SQLException ex) {
      if (psc instanceof ParameterDisposer) {
        ((ParameterDisposer) psc).cleanupParameters();
      }
      psc = null;
      ps.close();
      ps = null;
      throw new SQLException(ex.getMessage() + "批处理失败");
    } finally {
      if (psc instanceof ParameterDisposer) {
        ((ParameterDisposer) psc).cleanupParameters();
      }
      if (ps != null) {
        ps.close();
      }

    }
  }

  /**
   * 仿spring JdbcTemplate 的 batchUpdate 方法.
   * 
   * @param sql
   * @return
   * @throws SQLException
   */
  public int[] batchUpdate(final String[] sql) throws SQLException {

    Assert.notEmpty(sql, "SQL array must not be empty");
    if (logger.isDebugEnabled()) {
      logger.debug("Executing SQL batch update of " + sql.length + " statements");
    }
    class BatchUpdateStatementCallback implements StatementCallback, SqlProvider {
      private String currSql;

      @Override
      public Object doInStatement(Statement stmt) throws SQLException, DataAccessException {
        int[] rowsAffected = new int[sql.length];
        if (JdbcUtils.supportsBatchUpdates(stmt.getConnection())) {
          for (int i = 0; i < sql.length; i++) {
            this.currSql = sql[i];
            stmt.addBatch(sql[i]);
          }
          rowsAffected = stmt.executeBatch();
        } else {
          for (int i = 0; i < sql.length; i++) {
            this.currSql = sql[i];
            if (!stmt.execute(sql[i])) {
              rowsAffected[i] = stmt.getUpdateCount();
            } else {
              throw new InvalidDataAccessApiUsageException("Invalid batch SQL statement: " + sql[i]);
            }
          }
        }
        return rowsAffected;
      }

      @Override
      public String getSql() {
        return currSql;
      }
    }
    try {
      return (int[]) execute(new BatchUpdateStatementCallback());
    } catch (DataAccessException e) {
      throw new SQLException(e.getMessage() + "批处理SQL失败");
    } catch (SQLException e) {
      throw new SQLException(e.getMessage() + "批处理SQL失败");
    }
  }

  // -------------------------------------------------------------------------
  // Methods dealing with a plain java.sql.Connection
  // -------------------------------------------------------------------------

  @SuppressWarnings("rawtypes")
  private Object execute(StatementCallback action) throws DataAccessException, SQLException {
    Assert.notNull(action, "Callback object must not be null");
    Session session = super.getSession();
    Connection con = SessionFactoryUtils.getDataSource(getSessionFactory()).getConnection();
    Statement stmt = null;
    try {
      stmt = con.createStatement();
      // applyStatementSettings(stmt);
      Statement stmtToUse = stmt;
      // if (this.nativeJdbcExtractor != null) {
      // stmtToUse = this.nativeJdbcExtractor.getNativeStatement(stmt);
      // }
      Object result = action.doInStatement(stmt);
      // handleWarnings(stmt.getWarnings());
      return result;
    } catch (SQLException ex) {
      // Release Connection early, to avoid potential connection pool
      // deadlock
      // in the case when the exception translator hasn't been initialized
      // yet.
      stmt.close();
      stmt = null;
      con.close();
      con = null;
      throw new SQLException(ex.getMessage() + "批处理SQL失败");
    } finally {
      if (stmt != null) {
        stmt.close();
      }
      if (con != null) {
        con.close();
      }

    }

  }

  /**
   * 构造数据集检索排序部分.
   * 
   * @param sortFields
   * @return
   */
  public String getSortSqlPart(Map sortFields, String tableAlias) {
    Set allKeys = sortFields.keySet();
    Iterator itor = allKeys.iterator();
    String temp = "";
    while (itor.hasNext()) {
      String key = String.valueOf(itor.next());
      String value = String.valueOf(sortFields.get(key));
      if ("default".equalsIgnoreCase(value))// extremeCompontent
      {
        value = "asc";
      }
      temp += "," + tableAlias + "." + key + " " + value + "  nulls last ";
    }
    if (!"".equals(temp)) {
      temp = temp.substring(1) + ",";
    }
    return temp;
  }

  /**
   * Example: List sqhlist=[aa,bb,cc,dd,ee,ff,gg] ; Test.getSqlStrByList(sqhList,3,"SHENQINGH")= "AND
   * SHENQING IN ('aa','bb','cc') OR SHENQINGH IN ('dd','ee','ff') OR SHENQINGH IN ('gg')" .
   * 把超过1000的申请号集合拆分成数量splitNum的多组sql的in 集合。
   * 
   * @param sqhList 申请号的List
   * @param splitNum 拆分的间隔数目,例如： 1000
   * @param columnName SQL中引用的字段名例如： Z.SHENQINGH
   * @return
   */
  @SuppressWarnings("rawtypes")
  public String getSqlStrByList(List sqhList, int splitNum, String columnName) {
    if (splitNum > 1000) // 因为数据库的列表sql限制，不能超过1000.
    {
      return null;
    }
    StringBuffer sql = new StringBuffer("");
    if (sqhList != null) {
      sql.append(" AND ").append(columnName).append(" IN ( ");
      for (int i = 0; i < sqhList.size(); i++) {
        sql.append("'").append(sqhList.get(i) + "',");
        if ((i + 1) % splitNum == 0 && (i + 1) < sqhList.size()) {
          sql.deleteCharAt(sql.length() - 1);
          sql.append(" ) OR ").append(columnName).append(" IN (");
        }
      }
      sql.deleteCharAt(sql.length() - 1);
      sql.append(" )");
    }
    return sql.toString();
  }

  /**
   * 把超过1000的申请号数组拆分成数量splitNum的多组sql的in 集合.
   * 
   * @param sqhArrays 申请号的数组
   * @param splitNum 拆分的间隔数目,例如： 1000
   * @param columnName SQL中引用的字段名例如： Z.SHENQINGH
   * @return
   */
  public String getSqlStrByArrays(String[] sqhArrays, int splitNum, String columnName) {
    return getSqlStrByList(Arrays.asList(sqhArrays), splitNum, columnName);
  }

  /**
   * 使用SQL的方式查询，使用HQL的请不要使用该方法.
   * 
   * @param sql
   * @param objects
   * @param page
   * @return
   */
  @SuppressWarnings("unchecked")
  public Page QueryTable(String sql, Object[] objects, Page page) {

    Session session = this.getSession();

    SQLQuery sqlQuery = (SQLQuery) session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

    if (objects != null) {
      sqlQuery.setParameters(objects, this.findTypes(objects));
    }
    Long totalCount = this.queryForLong("select count(1) c from (" + sql + ")a", objects);
    page.setTotalCount(totalCount);

    // 查询数据实体
    sqlQuery.setFirstResult(page.getFirst() - 1);
    sqlQuery.setMaxResults(page.getPageSize());
    page.setResult(sqlQuery.list());

    // 记录数
    return page;

  }

  /**
   * 使用SQL的方式查询，使用HQL的请不要使用该方法.
   * 
   * @param sql
   * @param page
   * @return
   */
  public Page QueryTable(String sql, Page page) {

    return this.QueryTable(sql, null, page);

  }

  // 拼类型，所有类型都当字符串处理
  protected Type[] findTypes(Object[] objects) {
    List<Type> list = new ArrayList<Type>();
    for (Object object : objects) {
      if (object instanceof Integer) {
        list.add(new IntegerType());
      } else if (object instanceof Long) {
        list.add(new LongType());
      } else if (object instanceof BigDecimal) {
        list.add(new BigDecimalType());
      } else if (object instanceof Character) {
        list.add(new CharacterType());
      } else if (object instanceof Double) {
        list.add(new DoubleType());
      } else if (object instanceof Date) {
        list.add(new DateType());
      } else {
        list.add(new StringType());
      }
    }
    return list.toArray(new Type[] {});
  }
}
