package com.smate.center.batch.chain.pubassign.task;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignCniprScore;
import com.smate.center.batch.model.rol.pub.PubAssignCnkiPatScore;
import com.smate.center.batch.model.rol.pub.PubAssignCnkiScore;
import com.smate.center.batch.model.rol.pub.PubAssignEiScore;
import com.smate.center.batch.model.rol.pub.PubAssignPubMedScore;
import com.smate.center.batch.model.rol.pub.PubAssignScore;
import com.smate.center.batch.model.rol.pub.PubAssignScoreDetail;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.model.rol.pub.PubAssignSpsScore;
import com.smate.center.batch.service.rol.pub.KpiRefreshPubService;
import com.smate.center.batch.service.rol.pub.PubAssignCniprMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignCnkiMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignCnkiPatMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignEiMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignPubMedMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignSpsMatchService;

/**
 * 成果匹配-成果匹配任务结束，保存匹配结果.
 * 
 * @author liqinghua
 * 
 */
public class PubAssignMatchScoreStoreTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = 5180556344550932301L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private final String name = "PubAssignMatchScoreStoreTask";
  @Autowired
  private PubAssignMatchService pubAssignMatchService;
  @Autowired
  private PubAssignCnkiMatchService pubAssignCnkiMatchService;
  @Autowired
  private KpiRefreshPubService kpiRefreshPubService;
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
    Map<Integer, PubAssignScoreMap> pubAssignScoreMap = context.getPubAssignScoreMap();

    // 计算总分
    PubAssignScoreMapUtils.calculateDetailTotalScore(pubAssignScoreMap, context);

    // 过滤未达到阀值的人员
    PubAssignScoreMapUtils.removeUnBoundScore(pubAssignScoreMap, context);

    if (!context.hasMatchedPsn()) {
      return false;
    }
    // 一个人可能匹配上多个作者，需要将同一个人的最高分保留，其他删除
    PubAssignScoreMapUtils.removeDupPsnScore(pubAssignScoreMap, context);
    // 过滤差距过大的匹配结果
    PubAssignScoreMapUtils.removeLowerPsnScore(pubAssignScoreMap, context);
    // 获取匹配上成果的人员列表
    List<PubAssignScoreDetail> detailList = PubAssignScoreMapUtils.getMatchedPsnDetail(pubAssignScoreMap, context);
    // 保存结果
    this.saveIsiPubAssignScore(detailList, context);
    try {
      // 更新统计冗余
      kpiRefreshPubService.addPubRefresh(context.getPubId(), false);
    } catch (Exception e) {
      logger.warn("更新统计冗余", e);
    }
    return false;
  }

  private void saveIsiPubAssignScore(Collection<PubAssignScoreDetail> detailList, PubAssginMatchContext context)
      throws ServiceException {

    if (CollectionUtils.isEmpty(detailList)) {
      return;
    }

    for (PubAssignScoreDetail detail : detailList) {
      // 保存结果
      if (context.isCnkiImport()) {// cnki
        pubAssignCnkiMatchService.saveAssignScore(new PubAssignCnkiScore(detail));
      } else if (context.isIsiImport()) {// isi
        pubAssignMatchService.saveAssignScore(new PubAssignScore(detail));
      } else if (context.isScopusImport()) {// scopus
        pubAssignSpsMatchService.saveAssignScore(new PubAssignSpsScore(detail));
      } else if (context.isCniprImport()) {// cnipr
        pubAssignCniprMatchService.saveAssignScore(new PubAssignCniprScore(detail));
      } else if (context.isCnkiPatImport()) {// cnkipat
        pubAssignCnkiPatMatchService.saveAssignScore(new PubAssignCnkiPatScore(detail));
      } else if (context.isPubMedImport()) {// pubMed
        pubAssignPubMedMatchService.saveAssignScore(new PubAssignPubMedScore(detail));
      } else if (context.isEiImport()) {// ei
        pubAssignEiMatchService.saveAssignScore(new PubAssignEiScore(detail));
      }
    }

  }
}
