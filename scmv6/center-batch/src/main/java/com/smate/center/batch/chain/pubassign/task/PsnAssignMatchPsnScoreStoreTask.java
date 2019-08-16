package com.smate.center.batch.chain.pubassign.task;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.dao.rol.pub.PubAssignCniprScoreDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiPatScoreDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiScoreDao;
import com.smate.center.batch.dao.rol.pub.PubAssignPubMedScoreDao;
import com.smate.center.batch.dao.rol.pub.PubAssignScoreDao;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsScoreDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignCniprScore;
import com.smate.center.batch.model.rol.pub.PubAssignCnkiPatScore;
import com.smate.center.batch.model.rol.pub.PubAssignCnkiScore;
import com.smate.center.batch.model.rol.pub.PubAssignPubMedScore;
import com.smate.center.batch.model.rol.pub.PubAssignScore;
import com.smate.center.batch.model.rol.pub.PubAssignScoreDetail;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.model.rol.pub.PubAssignSpsScore;
import com.smate.center.batch.service.rol.pub.KpiRefreshPubService;
import com.smate.center.batch.service.rol.pub.PubAssignCniprMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignCnkiMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignCnkiPatMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignPubMedMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignSpsMatchService;

/**
 * ISI成果匹配-成果匹配任务结束，保存匹配结果.
 * 
 * @author liqinghua
 * 
 */
public class PsnAssignMatchPsnScoreStoreTask implements IPubAssignMatchTask {

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
  private PubAssignSpsMatchService pubAssignSpsMatchService;
  @Autowired
  private PubAssignCnkiPatMatchService pubAssignCnkiPatMatchService;
  @Autowired
  private PubAssignCniprMatchService pubAssignCniprMatchService;
  @Autowired
  private KpiRefreshPubService kpiRefreshPubService;
  @Autowired
  private PubAssignPubMedMatchService pubAssignPubMedMatchService;
  @Autowired
  private PubAssignCnkiScoreDao pubAssignCnkiScoreDao;
  @Autowired
  private PubAssignCnkiPatScoreDao pubAssignCnkiPatScoreDao;
  @Autowired
  private PubAssignCniprScoreDao pubAssignCniprScoreDao;
  @Autowired
  private PubAssignScoreDao pubAssignScoreDao;
  @Autowired
  private PubAssignSpsScoreDao pubAssignSpsScoreDao;
  @Autowired
  private PubAssignPubMedScoreDao pubAssignPubMedScoreDao;

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
    // 获取匹配上成果的人员列表
    List<PubAssignScoreDetail> detailList = PubAssignScoreMapUtils.getMatchedPsnDetail(pubAssignScoreMap, context);
    if (CollectionUtils.isEmpty(detailList)) {
      return false;
    }
    // 此时只剩下一个人
    PubAssignScoreDetail scoreDetail = detailList.get(0);
    // 过滤差距过大的匹配结果 TODO 验证差值，先注释掉
    /*
     * if (!this.isDisparityScore(context, scoreDetail)) { return false; }
     */
    // 保存结果
    saveIsiPubAssignScore(scoreDetail, context);
    // 更新统计冗余
    try {
      kpiRefreshPubService.addPubRefresh(context.getPubId(), false);
    } catch (Exception e) {
      logger.warn("更新统计冗余", e);
    }
    return false;
  }

  /**
   * 过滤分数差距大于等于50分的数据.
   * 
   * @param context
   * @param scoreDetail
   * @return
   * @throws Exception
   */
  private boolean isDisparityScore(PubAssginMatchContext context, PubAssignScoreDetail scoreDetail) throws Exception {

    // 获取数据库中的最大值，比较
    Float maxScore = this.getMaxScorePubPsnRol(context.getPubId(), scoreDetail.getSeqNo(), context);
    if (maxScore != null && maxScore - scoreDetail.getTotal() > 50) {
      return false;
    }
    return true;
  }

  /**
   * 获取最大分数.
   * 
   * @param pubId
   * @param seqNo
   * @param context
   * @return
   * @throws Exception
   */
  private Float getMaxScorePubPsnRol(Long pubId, Integer seqNo, PubAssginMatchContext context) throws Exception {

    if (context.isCnkiImport()) {// cnki
      return this.pubAssignCnkiScoreDao.getMaxScorePubPsnRol(pubId, seqNo);
    } else if (context.isIsiImport()) {// isi
      return this.pubAssignScoreDao.getMaxScorePubPsnRol(pubId, seqNo);
    } else if (context.isScopusImport()) {// scopus
      return this.pubAssignSpsScoreDao.getMaxScorePubPsnRol(pubId, seqNo);
    } else if (context.isCnkiPatImport()) {// cnkipat
      return this.pubAssignCnkiPatScoreDao.getMaxScorePubPsnRol(pubId, seqNo);
    } else if (context.isCniprImport()) {// cnipr
      return this.pubAssignCniprScoreDao.getMaxScorePubPsnRol(pubId, seqNo);
    } else if (context.isPubMedImport()) {// pubMed
      return this.pubAssignPubMedScoreDao.getMaxScorePubPsnRol(pubId, seqNo);
    }
    return null;
  }

  private void saveIsiPubAssignScore(PubAssignScoreDetail score, PubAssginMatchContext context)
      throws ServiceException {

    // 保存结果
    if (context.isCnkiImport()) {// cnki
      pubAssignCnkiMatchService.saveAssignScore(new PubAssignCnkiScore(score));
    } else if (context.isIsiImport()) {// isi
      pubAssignMatchService.saveAssignScore(new PubAssignScore(score));
    } else if (context.isScopusImport()) {// scopus
      pubAssignSpsMatchService.saveAssignScore(new PubAssignSpsScore(score));
    } else if (context.isCnkiPatImport()) {// cnkipat
      pubAssignCnkiPatMatchService.saveAssignScore(new PubAssignCnkiPatScore(score));
    } else if (context.isCniprImport()) {// cnipr
      pubAssignCniprMatchService.saveAssignScore(new PubAssignCniprScore(score));
    } else if (context.isPubMedImport()) {// pubMed
      pubAssignPubMedMatchService.saveAssignScore(new PubAssignPubMedScore(score));
    }

  }
}
