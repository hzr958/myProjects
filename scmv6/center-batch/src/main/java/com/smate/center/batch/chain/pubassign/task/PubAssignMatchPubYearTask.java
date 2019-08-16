package com.smate.center.batch.chain.pubassign.task;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.service.rol.pub.PubAssignMatchService;

/**
 * 成果年份匹配任务.
 * 
 * @author liqinghua
 * 
 */
public class PubAssignMatchPubYearTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = -5999984609178126788L;
  private final String name = "PubAssignMatchPubYearTask";
  @Autowired
  private PubAssignMatchService pubAssignMatchService;

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

    List<Long> psnIds = pubAssignMatchService.getWkhMatchPubYear(context.getInsId(), context.getPub().getPublishYear(),
        context.getMatchedPsnIds());
    if (CollectionUtils.isEmpty(psnIds)) {
      return true;
    }
    Map<Integer, PubAssignScoreMap> pubAssignScoreMap = context.getPubAssignScoreMap();
    // 设置分数
    for (Long psnId : psnIds) {
      Float score = context.getSettingPubAssignMatchScore().getPubyear();
      // 设置值
      PubAssignScoreMapUtils.setPsnPubyearScore(pubAssignScoreMap, psnId, score, score);
    }
    return true;
  }
}
