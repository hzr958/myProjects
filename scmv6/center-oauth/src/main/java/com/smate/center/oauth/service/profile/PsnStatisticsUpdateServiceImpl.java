package com.smate.center.oauth.service.profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.oauth.consts.ConstPublicationType;
import com.smate.center.oauth.dao.profile.AwardStatisticsDao;
import com.smate.center.oauth.dao.profile.FriendDao;
import com.smate.center.oauth.dao.profile.GroupInvitePsnDao;
import com.smate.center.oauth.dao.profile.SnsProjectDao;
import com.smate.center.oauth.dao.pub.PubFulltextPsnRcmdDao;
import com.smate.center.oauth.dao.pub.PublicationQueryDao;
import com.smate.center.oauth.model.pub.Publication;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.statistics.PsnStatisticsUtils;

/**
 * 人员统计信息更新服务.
 * 
 */
@Service("psnStatisticsUpdateService")
@Transactional(rollbackFor = Exception.class)
public class PsnStatisticsUpdateServiceImpl implements PsnStatisticsUpdateService {

  /**
   * 
   */
  private static final long serialVersionUID = -4587637543307693877L;

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private PubFulltextPsnRcmdDao pubFulltextPsnRcmdDao;
  @Autowired
  private PublicationQueryDao publicationQueryDao;
  @Autowired
  private SnsProjectDao snsProjectDao;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private GroupInvitePsnDao groupInvitePsnDao;
  @Autowired
  private AwardStatisticsDao awardStatisticsDao;

  /**
   * 初始化人员统计信息.
   * 
   * @param psnId
   * @return @
   */
  @Override
  public PsnStatistics initPsnStatics(Long psnId) {

    try {
      PsnStatistics psnStatistics = psnStatisticsDao.get(psnId);
      if (psnStatistics != null) {
        return psnStatistics;
      }
      psnStatistics = new PsnStatistics();
      psnStatistics.setPsnId(psnId);
      psnStatistics.setPubAwardSum(awardStatisticsDao.countAward(psnId, 1).intValue());
      psnStatistics.setGroupSum(groupInvitePsnDao.getGroupCountByPsnId(psnId));
      psnStatistics.setFrdSum(friendDao.getFriendCount(psnId).intValue());
      psnStatistics.setPrjSum(snsProjectDao.getSumProject(psnId));
      psnStatistics
          .setPatentSum(publicationQueryDao.countPubByPsnId(psnId, ConstPublicationType.PUB_PATENT_TYPE).intValue());
      // 增加设置成果全文推荐数_MJG_SCM-5991.
      Long pubFTextReSum = 0L;
      try {
        pubFTextReSum = pubFulltextPsnRcmdDao.queryRcmdFulltextCount(psnId);
        if (pubFTextReSum.longValue() > 0) {
          psnStatistics.setPubFullTextSum(NumberUtils.toInt(ObjectUtils.toString(pubFTextReSum)));
        }
      } catch (Exception e) {
        logger.error("获取个人统计信息时初始化获取成果全文推荐数出错，psnId=" + psnId + ":", e);
      }

      List<Publication> pubs = publicationQueryDao.queryPubsByPsnId(psnId);
      if (CollectionUtils.isNotEmpty(pubs)) {
        Map<String, Integer> langMap = getLangauge(pubs);
        int hidex = getHindex(pubs);
        psnStatistics.setPubSum(pubs.size());
        psnStatistics.setHindex(hidex);
        psnStatistics.setZhSum(NumberUtils.toInt(ObjectUtils.toString(langMap.get("zhcount"))));
        psnStatistics.setEnSum(NumberUtils.toInt(ObjectUtils.toString(langMap.get("enCount"))));
        psnStatistics.setCitedSum(publicationQueryDao.queryPubsCiteTimesByPsnId(psnId));
        // 属性为null的保存为0
        PsnStatisticsUtils.buildZero(psnStatistics);
        psnStatisticsDao.save(psnStatistics);
      }
      return psnStatistics;
    } catch (Exception e) {
      logger.error("初始化人员统计信息psnId=" + psnId, e);
      throw e;
    }
  }

  public Map<String, Integer> getLangauge(List<Publication> pubs) {
    Map<String, Integer> langMap = new HashMap<String, Integer>();
    int zhcount = 0;
    int enCount = 0;
    if (CollectionUtils.isNotEmpty(pubs)) {
      for (Publication pub : pubs) {
        if (pub.getZhTitleHash() == null) {
          enCount++;
        } else {
          zhcount++;
        }
      }
    }
    langMap.put("zhcount", zhcount);
    langMap.put("enCount", enCount);
    return langMap;
  }

  private int getHindex(List<Publication> pubs) {
    int hidex = 0;
    if (CollectionUtils.isNotEmpty(pubs)) {
      for (int i = 1; i <= pubs.size(); i++) {
        Publication pub = pubs.get(i - 1);
        Integer citeTimes = (int) (pub.getCitedTimes() == null ? 0 : pub.getCitedTimes());
        if (citeTimes.intValue() >= i) {
          hidex += 1;
        }
      }
    }
    return hidex;
  }

}
