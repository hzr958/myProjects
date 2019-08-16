package com.smate.center.task.single.service.person;

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

import com.smate.center.task.dao.group.GroupInvitePsnDao;
import com.smate.center.task.dao.sns.quartz.AwardStatisticsDao;
import com.smate.center.task.dao.sns.quartz.FriendDao;
import com.smate.center.task.dao.sns.quartz.PublicationQueryDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.center.task.service.sns.psn.SendDealMessageNoticeMailService;
import com.smate.center.task.single.constants.MessageConstant;
import com.smate.center.task.single.util.pub.ConstPublicationType;
import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.cache.CacheService;

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
  private PublicationQueryDao publicationQueryDao;
  @Autowired
  private ProjectDao projectDao;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private GroupInvitePsnDao groupInvitePsnDao;
  @Autowired
  private AwardStatisticsDao awardStatisticsDao;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private SendDealMessageNoticeMailService sendDealMessageNoticeMailService;

  /**
   * 初始化人员统计信息.
   * 
   * @param psnId
   * @return @
   */
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
      psnStatistics.setPrjSum(projectDao.getSumProject(psnId));
      psnStatistics
          .setPatentSum(publicationQueryDao.countPubByPsnId(psnId, ConstPublicationType.PUB_PATENT_TYPE).intValue());
      // 增加设置成果全文推荐数_MJG_SCM-5991.
      Long pubFTextReSum = 0L;
      try {
        pubFTextReSum = sendDealMessageNoticeMailService.getFulltextCount(psnId);
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
        buildZero(psnStatistics);
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

  @Override
  public void setPsnPendingConfirmPubNum(Long psnId, Integer pubNum) {
    try {
      PsnStatistics ps = this.initPsnStatics(psnId);
      ps.setPcfPubSum(pubNum);
      // 属性为null的保存为0
      buildZero(ps);
      psnStatisticsDao.save(ps);
      cacheService.remove(MessageConstant.MSG_TIP_CACHE_NAME, MessageConstant.MSG_TIP_CACHE_KEY + psnId);
    } catch (Exception e) {
      logger.error("设置用户待认领成果数据psnId=" + psnId, e);
      throw new ServiceException("设置用户待认领成果数据psnId=" + psnId, e);
    }

  }

  private void buildZero(PsnStatistics p) {
    // 1.成果总数
    if (p.getPubSum() == null) {
      p.setPubSum(0);
    }
    // 2.成果引用次数总数
    if (p.getCitedSum() == null) {
      p.setCitedSum(0);
    }
    // 3.hindex指数
    if (p.getHindex() == null) {
      p.setHindex(0);
    }
    // 4.中文成果数
    if (p.getZhSum() == null) {
      p.setZhSum(0);
    }
    // 5.英文成果数
    if (p.getEnSum() == null) {
      p.setEnSum(0);
    }
    // 6.项目总数
    if (p.getPrjSum() == null) {
      p.setPrjSum(0);
    }
    // 7.好友总数
    if (p.getFrdSum() == null) {
      p.setFrdSum(0);
    }
    // 8.群组总数
    if (p.getGroupSum() == null) {
      p.setGroupSum(0);
    }
    // 9.成果被赞的总数
    if (p.getPubAwardSum() == null) {
      p.setPubAwardSum(0);
    }
    // 10.专利数
    if (p.getPatentSum() == null) {
      p.setPatentSum(0);
    }
    // 11.待认领成果数
    if (p.getPcfPubSum() == null) {
      p.setPcfPubSum(0);
    }
    // 12.成果全文推荐数
    if (p.getPubFullTextSum() == null) {
      p.setPubFullTextSum(0);
    }
    // 13.公开成果总数
    if (p.getOpenPubSum() == null) {
      p.setOpenPubSum(0);
    }
    // 14.公开项目总数
    if (p.getOpenPrjSum() == null) {
      p.setOpenPrjSum(0);
    }
    // 15.访问总数
    if (p.getVisitSum() == null) {
      p.setVisitSum(0);
    }
  }

}
