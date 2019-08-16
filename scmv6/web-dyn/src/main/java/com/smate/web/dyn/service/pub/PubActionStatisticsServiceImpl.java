package com.smate.web.dyn.service.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 成果操作统计信息服务
 * 
 * @author Scy
 * 
 */
@Deprecated
@Service("pubActionStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class PubActionStatisticsServiceImpl implements PubActionStatisticsService {

  private static final long serialVersionUID = -54659931785917646L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /*
   * @Autowired private PublicationStatisticsDao publicationStatisticsDao;
   * 
   * @Override public void updateData(Long pubId, Integer type, Boolean isCancelAward) throws
   * DynException { PublicationStatistics pubStatistics = this.publicationStatisticsDao.get(pubId); if
   * (pubStatistics == null) { pubStatistics = new PublicationStatistics(pubId, 0, 0, 0, 0, 0); } if
   * (PublicationStatistics.AWARD_TYPE.equals(type)) { Integer awardCount =
   * pubStatistics.getAwardCount(); if (awardCount == null) awardCount = 0; if (isCancelAward) {
   * awardCount = awardCount - 1; awardCount = awardCount < 0 ? 0 : awardCount; } else { awardCount =
   * awardCount + 1; } pubStatistics.setAwardCount(awardCount); } if
   * (PublicationStatistics.COMMENT_TYPE.equals(type)) { if (pubStatistics.getCommentCount() == null)
   * { pubStatistics.setCommentCount(1); } else {
   * pubStatistics.setCommentCount(pubStatistics.getCommentCount() + 1); } } if
   * (PublicationStatistics.READ_TYPE.equals(type)) { if (pubStatistics.getReadCount() == null) {
   * pubStatistics.setReadCount(1); } else { pubStatistics.setReadCount(pubStatistics.getReadCount() +
   * 1); } } if (PublicationStatistics.SHARE_TYPE.equals(type)) { if (pubStatistics.getShareCount() ==
   * null) { pubStatistics.setShareCount(1); } else {
   * pubStatistics.setShareCount(pubStatistics.getShareCount() + 1); } } if
   * (PublicationStatistics.REF_TYPE.equals(type)) { if (pubStatistics.getRefCount() == null) {
   * pubStatistics.setRefCount(1); } else { pubStatistics.setRefCount(pubStatistics.getRefCount() +
   * 1); } } this.publicationStatisticsDao.save(pubStatistics); }
   */

}
