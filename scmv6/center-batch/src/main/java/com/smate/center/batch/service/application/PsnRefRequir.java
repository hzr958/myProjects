package com.smate.center.batch.service.application;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.RefKwForm;
import com.smate.center.batch.service.pdwh.pub.JournalAreaClassifyService;
import com.smate.center.batch.service.pdwh.pub.JournalHqService;
import com.smate.center.batch.service.pdwh.pub.PublicationAllService;
import com.smate.center.batch.service.psn.PsnAreaClassifyService;
import com.smate.center.batch.service.pub.SnsJournalService;

/**
 * 个人文献推荐必要条件
 * 
 * @author lichangwen
 * 
 */
@Service("psnRefRequir")
@Transactional(rollbackFor = Exception.class)
public class PsnRefRequir implements RequirService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SnsJournalService snsJournalService;
  @Autowired
  private PsnAreaClassifyService psnAreaClassifyService;
  @Autowired
  private JournalAreaClassifyService journalAreaClassifyService;
  @Autowired
  private PublicationAllService publicationAllService;
  @Autowired
  private JournalHqService journalHqService;

  @Override
  public List<?> matching(Long psnId, List<?> kwList) throws ServiceException {
    List<RefKwForm> refKwFormList = null;
    try {
      refKwFormList = matchKwResult(psnId);
      refKwFormList = this.filterJnlHQ(refKwFormList);
      refKwFormList = this.filterClassify(psnId, refKwFormList);
    } catch (Exception e) {
      logger.info("个人文献推荐必要条件,psnId:{}", psnId, e);
    }
    return refKwFormList;
  }

  /**
   * 只推CSSCI核心期刊跟ISI的期刊
   * 
   * @throws ServiceException
   */
  public List<RefKwForm> filterJnlHQ(List<RefKwForm> refKwFormList) throws ServiceException {
    if (CollectionUtils.isEmpty(refKwFormList))
      return null;
    try {
      return journalHqService.filterJournalHq(refKwFormList);
    } catch (Exception e) {
      logger.info("只推CSSCI核心期刊跟ISI的期刊", e);
      throw new ServiceException("只推CSSCI核心期刊跟ISI的期刊", e);
    }
  }

  /**
   * 个人大类要相同
   */
  public List<RefKwForm> filterClassify(Long psnId, List<RefKwForm> refKwFormList) throws ServiceException {
    if (CollectionUtils.isEmpty(refKwFormList))
      return null;
    List<String> psnClassifyList = psnAreaClassifyService.getPsnClassify(psnId);
    if (CollectionUtils.isEmpty(psnClassifyList))
      return null;
    for (int i = refKwFormList.size() - 1; i >= 0; i--) {
      String issn = refKwFormList.get(i).getIssn();
      boolean flag = false;
      List<String> jnlareaList = journalAreaClassifyService.getJournalAreaClassify(issn);
      if (CollectionUtils.isNotEmpty(jnlareaList)) {
        for (int j = 0; j < psnClassifyList.size(); j++) {
          boolean ag = false;
          for (int k = 0; k < jnlareaList.size(); k++) {
            if (psnClassifyList.get(j).equalsIgnoreCase(jnlareaList.get(k))) {
              ag = true;
              break;
            }
          }
          if (ag) {
            flag = true;
            break;
          }
        }
      }
      if (!flag) {
        refKwFormList.remove(i);
      }
    }
    return refKwFormList;
  }

  /**
   * 个人特征关键词与参考文献关键词相同，且是近3年内的文章
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public List<RefKwForm> matchKwResult(Long psnId) throws ServiceException {
    try {
      List<Long> kwHashList = getPsnKwHashList(psnId);

      if (CollectionUtils.isEmpty(kwHashList))
        return null;

      return publicationAllService.findPubAllByKwHashByRecommend(kwHashList);
    } catch (Exception e) {
      logger.info("个人特征关键词与参考文献关键词相同，且是近3年内的文章,psnId:{}", psnId, e);
      throw new ServiceException("个人特征关键词与参考文献关键词相同，且是近3年内的文章,psnId=" + psnId, e);
    }
  }

  public List<Long> getPsnKwHashList(Long psnId) throws ServiceException {
    return snsJournalService.getPnsIdKeywordHashList(psnId);
  }

}
