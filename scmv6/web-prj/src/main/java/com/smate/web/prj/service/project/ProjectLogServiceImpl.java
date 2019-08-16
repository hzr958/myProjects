package com.smate.web.prj.service.project;

import java.util.Map;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.prj.dao.project.ProjectLogDao;
import com.smate.web.prj.enums.ProjectOperationEnum;

/**
 * 项目日志.
 * 
 * @author liqinghua
 * 
 */
@Service("projectLogService")
@Transactional(rollbackFor = Exception.class)
public class ProjectLogServiceImpl implements ProjectLogService {

  /**
   * 
   */
  private static final long serialVersionUID = 5838039016082113490L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ProjectLogDao projectLogDao;

  @Override
  public void logOp(long prjId, long opPsnId, Long insId, ProjectOperationEnum op, Map<String, String> opDetail)
      throws ServiceException {
    try {
      String detail = "";
      if (opDetail != null && opDetail.size() > 0) {
        detail = JacksonUtils.jsonMapSerializer(opDetail).toString();
      }
      projectLogDao.logOp(prjId, opPsnId, insId, op, detail);
    } catch (Exception e) {
      logger.error("保存项目日志错误", e);
    }
  }

  @Override
  public void logOpDetail(long prjId, long opPsnId, Long insId, ProjectOperationEnum op, String opDetail)
      throws ServiceException {
    try {
      projectLogDao.logOp(prjId, opPsnId, insId, op, opDetail);
    } catch (Exception e) {
      logger.error("保存项目日志错误", e);
    }
  }

}
