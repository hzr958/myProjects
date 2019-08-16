package com.smate.center.batch.chain.pubassign.task;

import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.psn.PsnPmJournalRol;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.service.rol.pub.PubAssignCnkiMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignEiMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignPubMedMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignSpsMatchService;
import com.smate.center.batch.util.pub.ConstPublicationType;

/**
 * ISI成果匹配-匹配成果期刊.
 * 
 * @author liqinghua
 * 
 */
public class PubAssignMatchJournalTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = -2543025311934784536L;
  private final String name = "PubAssignMatchJournalTask";
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
    return context.hasMatchedPsn() && ConstPublicationType.PUB_JOURNAL_TYPE.equals(context.getPub().getTypeId());
  }

  @Override
  public boolean run(PubAssginMatchContext context) throws Exception {

    /**
     * 匹配上的期刊.
     */
    Map<Long, PsnPmJournalRol> psnPjList = getPJMatchPubPJ(context);
    if (CollectionUtils.isEmpty(psnPjList)) {
      return true;
    }
    Map<Integer, PubAssignScoreMap> pubAssignScoreMap = context.getPubAssignScoreMap();
    // 设置用户分数
    Iterator<Long> psnIds = psnPjList.keySet().iterator();
    while (psnIds.hasNext()) {
      Long psnId = psnIds.next();
      // 计算分数
      PsnPmJournalRol pj = psnPjList.get(psnId);
      Float score = calculateJournalScore(context, pj);
      // 设置值
      PubAssignScoreMapUtils.setPsnJournalScore(pubAssignScoreMap, psnId, score,
          context.getSettingPubAssignMatchScore().getJournal());
    }
    return true;
  }

  private Map<Long, PsnPmJournalRol> getPJMatchPubPJ(PubAssginMatchContext context) throws ServiceException {

    Map<Long, PsnPmJournalRol> psnPjList = null;
    // cnki库
    if (context.isCnkiImport()) {
      psnPjList = pubAssignCnkiMatchService.getPJMatchPubPJ(context.getPubId(), context.getMatchedPsnIds());
      // isi库
    } else if (context.isIsiImport()) {
      psnPjList = pubAssignMatchService.getPJMatchPubPJ(context.getPubId(), context.getMatchedPsnIds());
      // scopus库
    } else if (context.isScopusImport()) {
      psnPjList = pubAssignSpsMatchService.getPJMatchPubPJ(context.getPubId(), context.getMatchedPsnIds());
      // pubMed库
    } else if (context.isPubMedImport()) {
      psnPjList = pubAssignPubMedMatchService.getPJMatchPubPJ(context.getPubId(), context.getMatchedPsnIds());
    } else if (context.isEiImport()) {
      psnPjList = pubAssignEiMatchService.getPJMatchPubPJ(context.getPubId(), context.getMatchedPsnIds());
    }

    return psnPjList;
  }

  /**
   * 计算分数 matchScore = min{20, jcount * 5}.
   * 
   * @param context
   * @param pj
   * @return
   */
  private Float calculateJournalScore(PubAssginMatchContext context, PsnPmJournalRol pj) {
    return Math.min(context.getSettingPubAssignMatchScore().getJournal(), pj.getJcount() * 5.0f);
  }

}
