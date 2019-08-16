package com.smate.center.batch.service.projectmerge;

import java.io.Serializable;
import java.util.Map;

import com.smate.center.batch.oldXml.prj.ProjectOperationEnum;

/**
 * 项目日志.
 * 
 * @author liqinghua
 * 
 */
public interface ProjectLogService extends Serializable {

  /**
   * @param prjId 项目ID
   * @param opPsnId 操作人
   * @param op 操作枚举
   * @param opDetail 操作详细
   * @throws DaoException
   */
  public void logOp(long prjId, long opPsnId, Long insId, ProjectOperationEnum op, Map<String, String> opDetail)
      throws Exception;

  /**
   * 
   * @param prjId
   * @param opPsnId
   * @param insId
   * @param op
   * @param opDetail
   * @throws Exception
   */
  public void logOpDetail(long prjId, long opPsnId, Long insId, ProjectOperationEnum op, String opDetail)
      throws Exception;

}
