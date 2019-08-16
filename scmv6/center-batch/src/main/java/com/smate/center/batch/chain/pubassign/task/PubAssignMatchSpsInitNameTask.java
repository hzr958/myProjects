package com.smate.center.batch.chain.pubassign.task;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreDetail;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.service.rol.pub.PubAssignSpsMatchService;


/**
 * scopus成果匹配-成果指派匹配用户名称简写匹配任务，序号为1.
 * 
 * @author liqinghua
 * 
 */
public class PubAssignMatchSpsInitNameTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = 1915778042404773646L;
  private final String name = "PubAssignMatchSpsInitNameTask";
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

  /**
   * <pre>
   * 此匹配分两步：
   * 匹配用户简写比如li qh,li q h,li q-h.
   * </pre>
   */
  @Override
  public boolean run(PubAssginMatchContext context) throws Exception {
    // 获取匹配上isi成果作者名称简写的单位人员
    List<Object[]> matchedPsns = pubAssignSpsMatchService.getSpsInitNameMatchPubAuthor(context.getPubId(),
        context.getInsId(), context.getMatchedPsnIds());
    // 如果未匹配到一个用户，直接返回
    if (CollectionUtils.isEmpty(matchedPsns)) {
      return true;
    }
    // 存储匹配结果
    Map<Integer, PubAssignScoreMap> pubAssignScoreMap = context.getPubAssignScoreMap();
    for (Object[] matchedPsn : matchedPsns) {
      Integer seqNo = (Integer) matchedPsn[0];
      Long psnId = (Long) matchedPsn[1];
      PubAssignScoreMap scoreMap = pubAssignScoreMap.get(seqNo);
      if (scoreMap == null) {
        continue;
      }
      PubAssignScoreDetail scoreDetail = scoreMap.getDetailMap().get(psnId);
      if (scoreDetail == null) {
        continue;
      }
      // 简称匹配上
      scoreDetail.setInitName(context.getSettingPubAssignMatchScore().getInitName());

      // seq_no为空的情况，如果对上了同一个人，则将email分数加过去同一个人
      PubAssignScoreDetail nullDetail = context.getNullSeqScoreMap().get(psnId);
      if (nullDetail != null) {
        scoreDetail.setEmail(nullDetail.getEmail());
      }
    }

    return true;
  }

}
