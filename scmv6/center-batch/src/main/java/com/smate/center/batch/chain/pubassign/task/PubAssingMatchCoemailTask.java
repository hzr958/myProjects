package com.smate.center.batch.chain.pubassign.task;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.service.rol.pub.PubAssignEiMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignPubMedMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignSpsMatchService;

/**
 * ISI成果匹配-成果合作者email匹配任务.
 * 
 * @author liqinghua
 * 
 */
public class PubAssingMatchCoemailTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = -3778206286014919995L;
  private final String name = "PubAssingMatchCoemailTask";
  @Autowired
  private PubAssignMatchService pubAssignMatchService;
  @Autowired
  private PubAssignSpsMatchService pubAssignSpsMatchService;
  @Autowired
  private PubAssignPubMedMatchService pubAssignPubMedMatchService;
  @Autowired
  private PubAssignEiMatchService pubAssignEiMatchService;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubAssginMatchContext context) {
    return (context.isIsiImport() || context.isScopusImport() || context.isPubMedImport() || context.isEiImport())
        && context.hasMatchedPsn();
  }

  @Override
  public boolean run(PubAssginMatchContext context) throws Exception {

    Map<Long, Long> psnCo = getConMatchPubAuth(context, context.getPubId(), context.getMatchedPsnIds());
    if (MapUtils.isEmpty(psnCo)) {
      return true;
    }
    Map<Integer, PubAssignScoreMap> pubAssignScoreMap = context.getPubAssignScoreMap();
    // 设置用户分数
    Iterator<Long> psnIds = psnCo.keySet().iterator();
    while (psnIds.hasNext()) {
      Long psnId = psnIds.next();
      // 计算分数
      Float score = calculateCoemailScore(psnCo.get(psnId), context);
      // 设置值
      PubAssignScoreMapUtils.setPsnCoauthorScore(pubAssignScoreMap, psnId, score,
          context.getSettingPubAssignMatchScore().getConame());
    }
    return true;
  }

  /**
   * 计算分数 matchScore =min{20, cnt * 5}.
   * 
   * @param count
   * @param context
   * @return
   */
  private Float calculateCoemailScore(Long count, PubAssginMatchContext context) {

    return Math.min(context.getSettingPubAssignMatchScore().getConame(), count * 20.0f);
  }

  /**
   * 获取匹配上成果作者的指定用户的合作者个数.
   * 
   * @param context
   * @return
   * @throws ServiceException
   */
  private Map<Long, Long> getConMatchPubAuth(PubAssginMatchContext context, Long pubId, Set<Long> needPsnIds)
      throws ServiceException {

    if (context.isIsiImport()) {
      return pubAssignMatchService.getIsiCoeMatchPubAuth(pubId, needPsnIds);
    } else if (context.isScopusImport()) {
      return pubAssignSpsMatchService.getSpsCoeMatchPubAuth(pubId, needPsnIds);
    } else if (context.isPubMedImport()) {
      return pubAssignPubMedMatchService.getPubMedCoeMatchPubAuth(pubId, needPsnIds);
    } else if (context.isEiImport()) {
      return pubAssignEiMatchService.getEiCoeMatchPubAuth(pubId, needPsnIds);
    }
    return null;
  }

}
