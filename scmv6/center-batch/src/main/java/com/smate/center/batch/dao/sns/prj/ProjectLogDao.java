package com.smate.center.batch.dao.sns.prj;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.prj.ProjectLog;
import com.smate.center.batch.oldXml.prj.ProjectOperationEnum;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 项目日志Dao.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class ProjectLogDao extends SnsHibernateDao<ProjectLog, Long> {

  /**
   * @param prjId 项目ID
   * @param opPsnId 操作人
   * @param insId 单位ID
   * @param op 操作枚举
   * @param opDetail 操作详细
   * @param versionNo 项目版本号
   * @throws Exception
   */
  public void logOp(long prjId, long opPsnId, Long insId, ProjectOperationEnum op, String opDetail) throws Exception {
    ProjectLog prjlog = new ProjectLog();
    prjlog.setOpPsnId(opPsnId);
    prjlog.setOpAction(op.ordinal());
    prjlog.setOpDate(new Date());
    prjlog.setOpDetail(opDetail);
    prjlog.setPrjId(prjId);
    prjlog.setInsId(insId);
    super.save(prjlog);
  }

  /**
   * 查询指定项目的导入日志.
   * 
   * @param prjId
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<ProjectLog> queryPrjLogByPrjId(Long prjId) throws Exception {
    return super.createQuery("from ProjectLog t where t.prjId=?", new Object[] {prjId}).list();
  }

  @SuppressWarnings("unchecked")
  public List<ProjectLog> queryPrjLogByPsnId(Long psnId) throws Exception {
    return super.createQuery("from ProjectLog t where t.opPsnId=?", new Object[] {psnId}).list();
  }
}
