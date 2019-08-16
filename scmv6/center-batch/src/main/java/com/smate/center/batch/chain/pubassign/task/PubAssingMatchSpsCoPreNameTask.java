package com.smate.center.batch.chain.pubassign.task;

import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.service.rol.pub.PubAssignSpsMatchService;

/**
 * scopus成果匹配-成果合作者前缀匹配任务.
 * 
 * @author liqinghua
 * 
 */
public class PubAssingMatchSpsCoPreNameTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = 6840172591546704868L;
  private final String name = "PubAssingMatchSpsCoPreNameTask";
  @Autowired
  private PubAssignSpsMatchService pubAssignSpsMatchService;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubAssginMatchContext context) {
    return context.hasMatchedPsn();
  }

  @Override
  public boolean run(PubAssginMatchContext context) throws Exception {
    Map<Long, Long> psnCo = getConMatchPubAuth(context);
    if (psnCo == null || psnCo.size() == 0) {
      return true;
    }
    Map<Integer, PubAssignScoreMap> pubAssignScoreMap = context.getPubAssignScoreMap();
    // 设置用户分数
    Iterator<Long> psnIds = psnCo.keySet().iterator();
    while (psnIds.hasNext()) {
      Long psnId = psnIds.next();
      // 计算分数
      Float score = this.calculateConameScore(psnCo.get(psnId), context);
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
  private Float calculateConameScore(Long count, PubAssginMatchContext context) {

    return Math.min(context.getSettingPubAssignMatchScore().getConame(), count * 20.0f);
  }

  /**
   * 获取匹配上成果作者的指定用户的合作者个数.
   * 
   * @param context
   * @return
   * @throws ServiceException
   */
  private Map<Long, Long> getConMatchPubAuth(PubAssginMatchContext context) throws ServiceException {

    Map<Long, Long> psnCo = null;
    if (context.isScopusImport()) {// scopus
      psnCo = pubAssignSpsMatchService.getSpsConMatchPubAuthPreName(context.getPubId(), context.getMatchedPsnIds());
    }
    return psnCo;
  }

}
