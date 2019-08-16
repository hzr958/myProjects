package com.smate.center.batch.chain.pubassign.task;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreDetail;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.service.rol.pub.PubAssignEiMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignPubMedMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignSpsMatchService;

/**
 * 成果匹配-匹配成果人员全称.
 * 
 * @author liqinghua
 * 
 */
public class PubAssignMatchFullNameTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = -5241341019488434751L;
  private final String name = "PubAssignMatchFullNameTask";
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
    return context.hasMatchedPsn();
  }

  @Override
  public boolean run(PubAssginMatchContext context) throws Exception {

    List<Object[]> matchedPsns = getMathedPsns(context);

    if (matchedPsns == null || matchedPsns.size() == 0) {
      return true;
    }
    // 存储匹配结果
    Map<Integer, PubAssignScoreMap> pubAssignScoreMap = context.getPubAssignScoreMap();
    for (Object[] matchedPsn : matchedPsns) {
      Long psnId = (Long) matchedPsn[0];
      Integer seqNo = (Integer) matchedPsn[1];
      PubAssignScoreMap scoreMap = pubAssignScoreMap.get(seqNo);
      if (scoreMap == null) {
        continue;
      }
      PubAssignScoreDetail scoreDetail = scoreMap.getDetailMap().get(psnId);
      if (scoreDetail == null) {
        continue;
      }
      scoreDetail.setFullName(context.getSettingPubAssignMatchScore().getFullName());
      // seq_no为空的情况，如果对上了同一个人，则将email分数加过去同一个人
      PubAssignScoreDetail nullDetail = context.getNullSeqScoreMap().get(psnId);
      if (nullDetail != null) {
        scoreDetail.setEmail(nullDetail.getEmail());
      }
    }
    return true;
  }

  private List<Object[]> getMathedPsns(PubAssginMatchContext context) throws ServiceException {
    List<Object[]> matchedPsns = null;
    if (context.isIsiImport()) {
      matchedPsns = pubAssignMatchService.getIsiFullNameMatchPubAuthor(context.getPubId(), context.getMatchedPsnIds(),
          context.getInsId());
    } else if (context.isScopusImport()) {
      matchedPsns = pubAssignSpsMatchService.getSpsFullNameMatchPubAuthor(context.getPubId(),
          context.getMatchedPsnIds(), context.getInsId());
    } else if (context.isPubMedImport()) {
      matchedPsns = pubAssignPubMedMatchService.getPubMedFullNameMatchPubAuthor(context.getPubId(),
          context.getMatchedPsnIds(), context.getInsId());
    } else if (context.isEiImport()) {
      matchedPsns = pubAssignEiMatchService.getEiFullNameMatchPubAuthor(context.getPubId(), context.getMatchedPsnIds(),
          context.getInsId());
    }

    return matchedPsns;
  }

}
