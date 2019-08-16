package com.smate.center.batch.chain.pubassign.task;

import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PsnPmConference;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.service.rol.pub.PubAssignCnkiMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignEiMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignPubMedMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignSpsMatchService;
import com.smate.center.batch.util.pub.ConstPublicationType;

/**
 * ISI成果匹配-成果指派会议名称匹配任务.
 * 
 * @author liqinghua
 * 
 */
public class PubAssingMatchConfereceTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = 7431018531796074289L;
  private final String name = "PubAssingMatchConfereceTask";
  @Autowired
  private PubAssignMatchService pubAssignMatchService;
  @Autowired
  private PubAssignCnkiMatchService pubAssignCnkiMatchService;
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
    return context.hasMatchedPsn() && ConstPublicationType.PUB_CONFERECE_TYPE.equals(context.getPub().getTypeId());
  }

  @Override
  public boolean run(PubAssginMatchContext context) throws Exception {

    Map<Long, PsnPmConference> psnPjList = getPCMatchPubPc(context);
    if (psnPjList == null || psnPjList.size() == 0) {
      return true;
    }
    Map<Integer, PubAssignScoreMap> pubAssignScoreMap = context.getPubAssignScoreMap();
    // 设置用户分数
    Iterator<Long> psnIds = psnPjList.keySet().iterator();
    while (psnIds.hasNext()) {
      Long psnId = psnIds.next();
      // 计算分数
      PsnPmConference pc = psnPjList.get(psnId);
      Float score = calculatePcScore(context, pc);
      // 设置值
      PubAssignScoreMapUtils.setPsnConferenceScore(pubAssignScoreMap, psnId, score,
          context.getSettingPubAssignMatchScore().getConference());
    }
    return true;
  }

  /**
   * 获取匹配上成果会议论文列表.
   * 
   * @param context
   * @return
   * @throws ServiceException
   */
  private Map<Long, PsnPmConference> getPCMatchPubPc(PubAssginMatchContext context) throws ServiceException {
    Map<Long, PsnPmConference> psnPjList = null;
    if (context.isCnkiImport()) {// cnki
      psnPjList = pubAssignCnkiMatchService.getPCMatchPubPc(context.getPubId(), context.getMatchedPsnIds());
    } else if (context.isIsiImport()) {// isi
      psnPjList = pubAssignMatchService.getPCMatchPubPc(context.getPubId(), context.getMatchedPsnIds());
    } else if (context.isScopusImport()) {// scopus
      psnPjList = pubAssignSpsMatchService.getPCMatchPubPc(context.getPubId(), context.getMatchedPsnIds());
    } else if (context.isPubMedImport()) {// pubMed
      psnPjList = pubAssignPubMedMatchService.getPCMatchPubPc(context.getPubId(), context.getMatchedPsnIds());
    } else if (context.isEiImport()) {// Ei
      psnPjList = pubAssignEiMatchService.getPCMatchPubPc(context.getPubId(), context.getMatchedPsnIds());
    }

    return psnPjList;
  }

  /**
   * 计算分数 matchScore = min{20, ccount * 5}.
   * 
   * @param context
   * @param pc
   * @return
   */
  private Float calculatePcScore(PubAssginMatchContext context, PsnPmConference pc) {

    return Math.min(context.getSettingPubAssignMatchScore().getConference(), pc.getCcount() * 5.0f);
  }

}
