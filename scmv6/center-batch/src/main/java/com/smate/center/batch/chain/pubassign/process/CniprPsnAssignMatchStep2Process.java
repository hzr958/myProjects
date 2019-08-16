package com.smate.center.batch.chain.pubassign.process;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.smate.center.batch.chain.pubassign.task.IPubAssignMatchTask;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.exception.pub.XmlProcessStopExecuteException;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.service.rol.pub.PubAssignCniprMatchService;
import com.smate.center.batch.service.rol.pub.PublicationRolService;

/**
 * 单位人员匹配单位所有Cnipr成果任务第二步.
 * 
 * @author liqinghua
 * 
 */
public class CniprPsnAssignMatchStep2Process implements IPubAssignMatchProcess {

  /**
   * 
   */
  private static final long serialVersionUID = -8537847913414540438L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  private List<IPubAssignMatchTask> tasks = null;
  @Autowired
  private PubAssignCniprMatchService pubAssignCniprMatchService;
  @Autowired
  private PublicationRolService publicationRolService;

  @Override
  public void setTasks(List<IPubAssignMatchTask> tasks) {
    Assert.notNull(tasks);
    Assert.notEmpty(tasks);
    this.tasks = tasks;
  }

  @Override
  public void start(PubAssginMatchContext context) throws Exception {

    // 必须有匹配上的成果
    if (MapUtils.isEmpty(context.getPsnAssignScoreMap())) {
      return;
    }
    Map<Long, Map<Integer, PubAssignScoreMap>> map = context.getPsnAssignScoreMap();
    Iterator<Long> pubIter = map.keySet().iterator();
    while (pubIter.hasNext()) {
      Long pubId = pubIter.next();
      // 重新构造context
      PubAssginMatchContext pubContext = this.buildPubAssginMatchContext(pubId, map.get(pubId));
      for (int index = 0; index < this.tasks.size(); index++) {
        IPubAssignMatchTask task = this.tasks.get(index);
        boolean flag = task.can(pubContext);
        if (flag) {
          try {
            boolean result = task.run(pubContext);
            if (!result) {
              break;
            }
          } catch (XmlProcessStopExecuteException e) {
            logger.error("CniprPsnAssignMatchStep2Process成果匹配任务: task=" + task.getName(), e);
            throw e;
          } catch (Exception e) {
            logger.error("CniprPsnAssignMatchStep2Process成果匹配任务: task=" + task.getName(), e);
            throw e;
          }
        }
      }
    }

    if (this.getNextProcess() != null) {
      this.getNextProcess().start(context);
    }
  }

  /**
   * 构造具体成果匹配人员PubAssginMatchContext.
   * 
   * @param pubId
   * @param score
   * @param context
   * @throws ServiceException
   */
  private PubAssginMatchContext buildPubAssginMatchContext(Long pubId, Map<Integer, PubAssignScoreMap> scoreMap)
      throws ServiceException {

    PubAssginMatchContext pubContext = new PubAssginMatchContext();
    PublicationRol pub = publicationRolService.getPublicationById(pubId);
    pubContext.setDbId(Long.valueOf(pub.getSourceDbId()));
    pubContext.setPub(pub);
    pubContext.setPubId(pub.getId());
    pubContext.setInsId(pub.getInsId());
    pubContext.setSettingPubAssignMatchScore(
        pubAssignCniprMatchService.getSettingPubAssignScore(pub.getSourceDbId().longValue()));
    pubContext.setPubAssignScoreMap(scoreMap);
    return pubContext;

  }

  @Override
  public IPubAssignMatchProcess getNextProcess() {
    return null;
  }

  @Override
  public void setNextProcess(IPubAssignMatchProcess process) {}
}
