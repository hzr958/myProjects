package com.smate.center.batch.chain.pubassign.task;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PsnPmKeyWordRol;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.model.rol.pub.SettingPubAssignKwWt;
import com.smate.center.batch.service.rol.pub.PubAssignCniprMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignCnkiMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignCnkiPatMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignEiMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignPubMedMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignSpsMatchService;
import com.smate.center.batch.service.rol.pubassign.PsnPmService;

/**
 * ISI成果匹配-成果指派，关键词匹配.
 * 
 * @author liqinghua
 * 
 */
public class PubAssignMatchKeyWordTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = -4166853371614295943L;
  private final String name = "PubAssignMatchKeyWordTask";
  @Autowired
  private PubAssignMatchService pubAssignMatchService;
  @Autowired
  private PubAssignCnkiMatchService pubAssignCnkiMatchService;
  @Autowired
  private PubAssignSpsMatchService pubAssignSpsMatchService;
  @Autowired
  private PubAssignCniprMatchService pubAssignCniprMatchService;
  @Autowired
  private PubAssignCnkiPatMatchService pubAssignCnkiPatMatchService;
  @Autowired
  private PubAssignPubMedMatchService pubAssignPubMedMatchService;
  @Autowired
  private PubAssignEiMatchService pubAssignEiMatchService;
  @Autowired
  private PsnPmService psnPmService;

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

    Map<Long, Long> psnKwMap = getKwMatchPubKw(context);
    if (MapUtils.isEmpty(psnKwMap)) {
      return true;
    }
    Map<Integer, PubAssignScoreMap> pubAssignScoreMap = context.getPubAssignScoreMap();
    // 设置用户分数
    Iterator<Long> psnIds = psnKwMap.keySet().iterator();
    while (psnIds.hasNext()) {
      Long psnId = psnIds.next();
      // 计算分数
      Float score = Math.min(psnKwMap.get(psnId) * 20f, context.getSettingPubAssignMatchScore().getKewords());
      // 设置值
      PubAssignScoreMapUtils.setPsnKeyWordScore(pubAssignScoreMap, psnId, score,
          context.getSettingPubAssignMatchScore().getKewords());
    }
    return true;
  }

  /**
   * 获取作者确认过的成果关键词匹配上了成果关键词.
   * 
   * @param context
   * @return
   * @throws ServiceException
   */
  private Map<Long, Long> getKwMatchPubKw(PubAssginMatchContext context) throws ServiceException {

    if (context.isCnkiImport()) {// cnki
      return pubAssignCnkiMatchService.getKwMatchPubKw(context.getPubId(), context.getMatchedPsnIds());
    } else if (context.isIsiImport()) {// isi
      return pubAssignMatchService.getKwMatchPubKw(context.getPubId(), context.getMatchedPsnIds());
    } else if (context.isScopusImport()) {// scopus
      return pubAssignSpsMatchService.getKwMatchPubKw(context.getPubId(), context.getMatchedPsnIds());
    } else if (context.isCniprImport()) {// cnipr
      return pubAssignCniprMatchService.getKwMatchPubKw(context.getPubId(), context.getMatchedPsnIds());
    } else if (context.isCnkiPatImport()) {// cnkipat
      return pubAssignCnkiPatMatchService.getKwMatchPubKw(context.getPubId(), context.getMatchedPsnIds());
    } else if (context.isPubMedImport()) {// pubMed
      return pubAssignPubMedMatchService.getKwMatchPubKw(context.getPubId(), context.getMatchedPsnIds());
    } else if (context.isEiImport()) {// ei
      return pubAssignEiMatchService.getKwMatchPubKw(context.getPubId(), context.getMatchedPsnIds());
    }

    return null;
  }

  /**
   * 计算分数// 取消权重matchScore = min{20,(count + weight)*5}.
   * 
   * matchScore = min{20,count*5}.
   * 
   * @param context
   * @param pmkwList
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("unused")
  @Deprecated
  private Float calculateKWScore(PubAssginMatchContext context, List<PsnPmKeyWordRol> pmkwList)
      throws ServiceException {
    // Float matchScore = (pmkwList.size() + getKeyWeight(pmkwList)) * 5;
    Float matchScore = pmkwList.size() * 5f;
    return Math.min(context.getSettingPubAssignMatchScore().getKewords(), matchScore);
  }

  /**
   * 获取权重值.
   * 
   * @param pmkwList
   * @return
   */
  @SuppressWarnings("unused")
  @Deprecated
  private Float getKeyWeight(List<PsnPmKeyWordRol> pmkwList) throws ServiceException {
    List<SettingPubAssignKwWt> kwWtList = psnPmService.getSettingPubAssignKwWt();
    Float keyWordWeight = 0F;
    if (kwWtList == null) {
      return keyWordWeight;
    }
    // 获取匹配上的权重合集
    for (PsnPmKeyWordRol kw : pmkwList) {
      int count = kw.getKcount();
      for (SettingPubAssignKwWt kwWt : kwWtList) {
        // 使用次数大于边界值
        if (count >= kwWt.getBound()) {
          keyWordWeight += kwWt.getWeight();
          break;
        }
      }
    }
    return keyWordWeight;
  }

}
