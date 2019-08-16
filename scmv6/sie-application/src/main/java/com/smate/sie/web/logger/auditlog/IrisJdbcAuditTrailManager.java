/**
 * Licensed to IRIS-System co. Copyright (C) 2014 IRIS, The IRIS Systems (Shenzhen) Limited.
 */
package com.smate.sie.web.logger.auditlog;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.github.inspektr.audit.AuditActionContext;
import com.github.inspektr.audit.AuditTrailManager;
import com.github.inspektr.audit.support.NoMatchWhereClauseMatchCriteria;
import com.github.inspektr.audit.support.WhereClauseMatchCriteria;
import com.github.inspektr.common.Cleanable;

/**
 * <p>
 * Implementation of {@link com.github.inspektr.audit.AuditTrailManager} to persist the audit trail
 * to the AUDIT_TRAIL table in the Oracle data base.
 * </p>
 * 
 * <pre>
 * CREATE TABLE COM_AUDIT_TRAIL
 * (
 *  AUD_USER      VARCHAR2(100) NOT NULL,
 *  AUD_CLIENT_IP VARCHAR(15)   NOT NULL,
 *  AUD_SERVER_IP VARCHAR(15)   NOT NULL,
 *  AUD_RESOURCE  VARCHAR2(100) NOT NULL,
 *  AUD_ACTION    VARCHAR2(100) NOT NULL,
 *  APPLIC_CD     VARCHAR2(5)   NOT NULL,
 *  AUD_DATE      TIMESTAMP     NOT NULL
 * )
 * </pre>
 * 
 * @author Scott Battaglia
 * @author Marvin S. Addison
 * @version $Revision: 1.7 $ $Date: 2007/12/03 22:02:41 $
 * @since 1.0
 */
public final class IrisJdbcAuditTrailManager extends JdbcDaoSupport
    implements AuditTrailManager, Cleanable, DisposableBean {

  private static final String INSERT_SQL_TEMPLATE = "INSERT INTO %s "
      + "(AUD_ID,AUD_USER, AUD_CLIENT_IP, AUD_SERVER_IP, AUD_RESOURCE, AUD_ACTION, APPLIC_CD, AUD_DATE, AUD_CONTENT,AUD_RESULT) "
      + "VALUES (SEQ_COM_AUDIT_TRAIL.NEXTVAL,?, ?, ?, ?, ?, ?, ?, ?,?)";

  private static final String DELETE_SQL_TEMPLATE = "DELETE FROM %s %s";

  private static final int DEFAULT_COLUMN_LENGTH = 100;

  /**
   * Logger instance
   */
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * Instance of TransactionTemplate to manually execute a transaction since threads are not in the
   * same transaction.
   */
  @NotNull
  private final TransactionTemplate transactionTemplate;

  @NotNull
  @Size(min = 1)
  private String tableName = "COM_AUDIT_TRAIL";

  @Min(50)
  private int columnLength = DEFAULT_COLUMN_LENGTH;

  /**
   * ExecutorService that has one thread to asynchronously save requests.
   * 
   * You can configure one with an
   * {@link org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean}.
   */
  @NotNull
  private ExecutorService executorService = Executors.newSingleThreadExecutor();

  private boolean defaultExecutorService = true;

  /**
   * Criteria used to determine records that should be deleted on cleanup
   */
  private WhereClauseMatchCriteria cleanupCriteria = new NoMatchWhereClauseMatchCriteria();

  public IrisJdbcAuditTrailManager(final TransactionTemplate transactionTemplate) {
    this.transactionTemplate = transactionTemplate;
  }

  public void record(final AuditActionContext auditActionContext) {
    this.executorService.execute(new LoggingTask(auditActionContext, this.transactionTemplate, this.columnLength));
  }

  protected class LoggingTask implements Runnable {

    private final AuditActionContext auditActionContext;

    private final TransactionTemplate transactionTemplate;

    private final int columnLength;

    public LoggingTask(final AuditActionContext auditActionContext, final TransactionTemplate transactionTemplate,
        final int columnLength) {
      this.auditActionContext = auditActionContext;
      this.transactionTemplate = transactionTemplate;
      this.columnLength = columnLength;
    }

    public void run() {
      this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {
        protected void doInTransactionWithoutResult(final TransactionStatus transactionStatus) {
          final String userId =
              auditActionContext.getPrincipal().length() <= columnLength ? auditActionContext.getPrincipal()
                  : auditActionContext.getPrincipal().substring(0, columnLength);
          final String resource = auditActionContext.getResourceOperatedUpon().length() <= columnLength
              ? auditActionContext.getResourceOperatedUpon()
              : auditActionContext.getResourceOperatedUpon().substring(0, columnLength);
          final String content = auditActionContext.getResourceOperatedUpon();
          String[] actionAry = auditActionContext.getActionPerformed().split("\\|\\|");
          getJdbcTemplate().update(String.format(INSERT_SQL_TEMPLATE, tableName), userId,
              auditActionContext.getClientIpAddress(), auditActionContext.getServerIpAddress(), resource, actionAry[0],
              auditActionContext.getApplicationCode(), auditActionContext.getWhenActionWasPerformed(), content,
              actionAry[1]);
        }
      });
    }
  }

  public void setTableName(final String tableName) {
    this.tableName = tableName;
  }

  public void setCleanupCriteria(final WhereClauseMatchCriteria criteria) {
    this.cleanupCriteria = criteria;
  }

  public void setExecutorService(final ExecutorService executorService) {
    this.executorService = executorService;
    this.defaultExecutorService = false;
  }

  public void setColumnLength(final int columnLength) {
    this.columnLength = columnLength;
  }

  /**
   * We only shut down the default executor service. We assume, that if you've injected one, its being
   * managed elsewhere.
   */
  public void destroy() throws Exception {
    if (this.defaultExecutorService) {
      this.executorService.shutdown();
    }
  }

  public void clean() {
    this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      protected void doInTransactionWithoutResult(final TransactionStatus transactionStatus) {
        final String sql = String.format(DELETE_SQL_TEMPLATE, tableName, cleanupCriteria);
        final List<?> params = cleanupCriteria.getParameterValues();
        IrisJdbcAuditTrailManager.this.logger.info("Cleaning audit records with query " + sql);
        IrisJdbcAuditTrailManager.this.logger.debug("Query parameters: " + params);
        final int count = getJdbcTemplate().update(sql, params.toArray());
        IrisJdbcAuditTrailManager.this.logger.info(count + " records deleted.");
      }
    });
  }
}
