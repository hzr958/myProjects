package com.smate.center.batch.service.application;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.RefKwForm;
import com.smate.center.batch.model.rcmd.journal.RcmdPsnJournal;
import com.smate.center.batch.model.sns.psn.PsnRefPsnRecScore;
import com.smate.center.batch.service.pdwh.pub.PublicationPdwhService;
import com.smate.center.batch.service.psn.PsnJournalService;
import com.smate.center.batch.service.psn.PsnKwRmcGroupService;
import com.smate.center.batch.service.pub.ConstPdwhPubRefDb;
import com.smate.center.batch.service.pub.PublicationRefcJournalService;
import com.smate.center.batch.service.rcmd.journal.JournalGradeService;

/**
 * 个人文献推荐-加分条件
 * 
 * @author lichangwen
 * 
 */
@Service("psnRefPlus")
@Transactional(rollbackFor = Exception.class)
public class PsnRefPlus implements PlusService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnJournalService psnJournalService;
  @Autowired
  private JournalGradeService journalGradeService;
  @Autowired
  private PublicationPdwhService publicationPdwhService;
  @Autowired
  private PsnKwRmcGroupService psnKwRmcGroupService;
  @Autowired
  private PublicationRefcJournalService publicationRefcJournalService;

  @SuppressWarnings("unchecked")
  @Override
  public List<?> complex(Long psnId, List<?> list, List<?> kwList) throws ServiceException {
    if (CollectionUtils.isEmpty(list))
      return null;
    List<PsnRefPsnRecScore> scoreList = new ArrayList<PsnRefPsnRecScore>();
    List<RefKwForm> refKwList = (List<RefKwForm>) list;
    for (RefKwForm refKwForm : refKwList) {
      PsnRefPsnRecScore entity = new PsnRefPsnRecScore();
      entity.setPuballId(refKwForm.getPuballId());
      entity.setJnlId(refKwForm.getJnlId());
      entity.setIssn(refKwForm.getIssn());
      entity.setPsnId(psnId);
      if (ArrayUtils.contains(ConstPdwhPubRefDb.EN_LIST, refKwForm.getDbid())) {
        entity.setLanguage(2);
      } else {
        entity.setLanguage(1);
      }
      this.quality(psnId, refKwForm, entity);
      this.relevance(psnId, refKwForm, entity);
      this.cooperation(psnId, refKwForm, entity);
      scoreList.add(entity);
    }
    return scoreList;
  }

  /**
   * 质量：高于或等于用户发表档次+1，用户在期刊上发表过再+1
   * 
   * 
   * @param psnId
   * @throws ServiceException
   */
  public void quality(Long psnId, RefKwForm refKwForm, PsnRefPsnRecScore entity) throws ServiceException {
    try {
      // 获取人员最多发表等级
      int psnJnlMostGrade = psnJournalService.getPsnJnlMaxGrade(psnId);
      // 重构期刊等级
      entity.setGrade(journalGradeService.getJnlGrade(refKwForm.getIssn()));
      // 质量 高于等于用户发表论文最多档内+1
      if (entity.getGrade().intValue() <= psnJnlMostGrade) {
        entity.setGradeMost(1);
      }
      // 获取人员已发表期刊
      List<RcmdPsnJournal> pnsJnlList = psnJournalService.getPsnJournal(psnId);
      if (CollectionUtils.isEmpty(pnsJnlList))
        return;
      boolean isissn = false;
      for (RcmdPsnJournal psnJournal : pnsJnlList) {
        if (StringUtils.equalsIgnoreCase(refKwForm.getIssn(), psnJournal.getIssn())) {
          isissn = true;
          break;
        }
      }
      if (isissn) {
        entity.setGradeHt(1);// 质量:用户在期刊上发表过+1
      }
    } catch (Exception e) {
      logger.error("个人文献推荐-质量加分条件出错,psnId:{}", psnId, e);
    }
  }

  /**
   * 相关度： ∑（关键词/同义词/翻译词+用户发表期刊次数+用户收藏期刊）
   * 
   * 
   * @param psnId
   * @throws ServiceException
   */
  public void relevance(Long psnId, RefKwForm refKwForm, PsnRefPsnRecScore entity) throws ServiceException {
    try {
      // 关键词/同义词/翻译词
      int kwRmcCount = psnKwRmcGroupService.getMatchPsnKwRmcGids(psnId, refKwForm.getRefKws());
      entity.setKwTf(kwRmcCount);
      // 用户发表期刊次数
      int psnJnlCount = psnJournalService.getPsnJnlCountByIssn(psnId, refKwForm.getIssn());
      entity.setHtTf(psnJnlCount);
      // 用户收藏期刊
      int refJnlCount = publicationRefcJournalService.getPsnJnlByRefc(psnId, refKwForm.getIssn());
      entity.setGradeInner(refJnlCount);
    } catch (Exception e) {
      logger.error("个人文献推荐-相关度加分条件出错,psnId:{}", psnId, e);
    }
  }

  /**
   * 合作度：好友的论文=1
   * 
   * @param psnId
   * @throws ServiceException
   */
  public void cooperation(Long psnId, RefKwForm refKwForm, PsnRefPsnRecScore entity) throws ServiceException {
    try {
      // 好友导入过的基准文献
      int frdCount = publicationPdwhService.getPubPdwhIdByPsnFriend(psnId, refKwForm.getPubId(), refKwForm.getDbid());
      entity.setFrdTf(frdCount > 0 ? 1 : 0);
    } catch (Exception e) {
      logger.error("个人文献推荐-合作度加分条件出错,psnId:{}", psnId, e);
    }
  }

}
