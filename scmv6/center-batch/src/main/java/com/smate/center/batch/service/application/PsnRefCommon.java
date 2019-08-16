package com.smate.center.batch.service.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.PsnPubAllRecommend;
import com.smate.center.batch.model.pdwh.pub.RefKwForm;
import com.smate.center.batch.model.sns.psn.PsnRefPsnRecScore;
import com.smate.center.batch.service.pdwh.pub.PublicationAllService;
import com.smate.center.batch.service.psn.inforefresh.PsnRefPsnRecScoreService;
import com.smate.core.base.utils.common.BeanUtils;


/**
 * 个人文献推荐公用
 * 
 * @author lichangwen
 * 
 */
@Service("psnRefCommon")
@Transactional(rollbackFor = Exception.class)
public class PsnRefCommon implements CommonService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnRefPsnRecScoreService psnRefPsnRecScoreService;
  @Autowired
  private PublicationAllService publicationAllService;

  @Override
  public void clear(Long psnId, List<?> list) {
    try {
      // 如果必要条件结果list为空，则删除原推荐数据
      if (CollectionUtils.isEmpty(list)) {
        this.publicationAllService.deletePsnPubAllRecommend(psnId);
      } else {
        psnRefPsnRecScoreService.truncatePsnRefRecommendScore();
      }
    } catch (Exception e) {
      logger.error("个人文献推荐-删除推荐得分表数据出错,psnId:{}", psnId, e);
    }

  }

  @SuppressWarnings("unchecked")
  @Override
  public void save(Long psnId, List<?> list) {
    try {
      List<PsnRefPsnRecScore> refList = (List<PsnRefPsnRecScore>) list;
      if (CollectionUtils.isEmpty(refList)) {
        return;
      }
      for (PsnRefPsnRecScore psnRefRecScore : refList) {
        psnRefPsnRecScoreService.save(psnRefRecScore);
      }
    } catch (Exception e) {
      logger.error("个人文献推荐-保存推荐数据到得分表出错,psnId:{}", psnId, e);
    }
  }

  /*
   * (相关度 +质量)* Ln(2.72+合作度)
   */
  @Override
  public void degrees(Long psnId) {
    try {
      psnRefPsnRecScoreService.psnRefDegrees(psnId);
    } catch (ServiceException e) {
      logger.error("个人文献推荐-推荐度算法出错,psnId:{}", psnId, e);
    }
  }

  /*
   * 根据新推荐的数据来更新原推荐的最终数据
   */
  @SuppressWarnings({"unchecked"})
  @Override
  public void product(Long psnId, List<?> list) {
    try {
      List<RefKwForm> refKwList = (List<RefKwForm>) list;
      if (CollectionUtils.isEmpty(refKwList)) {
        return;
      }
      // 找60篇出来(中文30，英文30)
      int size = 30;
      // 中文推荐
      List<PsnRefPsnRecScore> zhList = psnRefPsnRecScoreService.getDescRefList(psnId, 1, size);
      if (CollectionUtils.isNotEmpty(zhList)) {
        List<PsnPubAllRecommend> oldZhList = publicationAllService.getPsnPuballOldList(psnId, 1);
        List<PsnPubAllRecommend> newZhRecList = this.assemblyPsnRef(psnId, refKwList, zhList);
        this.refreshPsnRefRecommend(psnId, oldZhList, newZhRecList);
      }
      // 英文推荐
      List<PsnRefPsnRecScore> enList = psnRefPsnRecScoreService.getDescRefList(psnId, 2, size);
      if (CollectionUtils.isNotEmpty(enList)) {
        List<PsnPubAllRecommend> oldEnList = publicationAllService.getPsnPuballOldList(psnId, 2);
        List<PsnPubAllRecommend> newEnRecList = this.assemblyPsnRef(psnId, refKwList, enList);
        this.refreshPsnRefRecommend(psnId, oldEnList, newEnRecList);
      }
    } catch (Exception e) {
      logger.error("个人文献推荐-整理推荐最终数据出错,psnId:{}", psnId, e);
    }
  }

  @SuppressWarnings("unchecked")
  private void refreshPsnRefRecommend(Long psnId, List<PsnPubAllRecommend> oldResList,
      List<PsnPubAllRecommend> newRecList) throws ServiceException {
    try {
      Map<String, List<? extends Object>> recMap = BeanUtils.compareListObject(newRecList, oldResList);
      List<PsnPubAllRecommend> delList = ((List<PsnPubAllRecommend>) recMap.get(BeanUtils.DEL));
      publicationAllService.deletePsnPubAllRecommend(psnId, delList);
      List<PsnPubAllRecommend> addList = (List<PsnPubAllRecommend>) recMap.get(BeanUtils.ADD);
      publicationAllService.savePsnPubAllRecommend(addList);
      List<PsnPubAllRecommend> updateList = (List<PsnPubAllRecommend>) recMap.get(BeanUtils.UPDATE);
      publicationAllService.savePsnPubAllRecommend(updateList);
    } catch (Exception e) {
      logger.error("refreshPsnRefRecommend出错,psnId:{}", psnId, e);
    }
  }

  private List<PsnPubAllRecommend> assemblyPsnRef(Long psnId, List<RefKwForm> refKwList,
      List<PsnRefPsnRecScore> zhList) {
    List<PsnPubAllRecommend> newList = new ArrayList<PsnPubAllRecommend>();
    if (CollectionUtils.isNotEmpty(zhList)) {
      for (PsnRefPsnRecScore score : zhList) {
        PsnPubAllRecommend entity = new PsnPubAllRecommend();
        entity.setPsnId(psnId);
        entity.setPubAllId(score.getPuballId());
        entity.setScore(score.getScore());
        String keywords = "";
        for (RefKwForm form : refKwList) {
          if (form.getPuballId().equals(score.getPuballId())) {
            for (String kw : form.getMatchKws()) {
              keywords += ", " + kw;
            }
            break;
          }
        }
        keywords = StringUtils.isBlank(keywords) ? "" : keywords.substring(2);
        entity.setKeywords(StringUtils.trimToEmpty(keywords));
        entity.setRecDate(new Date());
        entity.setLanguage(score.getLanguage());
        newList.add(entity);
      }
    }
    return newList;
  }

}
