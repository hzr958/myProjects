package com.smate.center.batch.service.psn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.constant.MessageConstant;
import com.smate.center.batch.dao.rcmd.pub.RcmdPsnStatisticsDao;
import com.smate.center.batch.dao.rol.pub.RolPsnStatisticsDao;
import com.smate.center.batch.dao.sns.prj.AwardStatisticsDao;
import com.smate.center.batch.dao.sns.prj.FriendDao;
import com.smate.center.batch.dao.sns.prj.GroupInvitePsnDao;
import com.smate.center.batch.dao.sns.prj.PubFulltextPsnRcmdDao;
import com.smate.center.batch.dao.sns.prj.PublicationQueryDao;
import com.smate.center.batch.dao.sns.pub.PublicationDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rcmd.pub.RcmdPsnStatistics;
import com.smate.center.batch.model.rol.prj.RolPsnStatistics;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.service.pub.mq.PsnStatisticsSyncProducer;
import com.smate.center.batch.util.pub.ConstPublicationType;
import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.psn.consts.PsnCnfConst;
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
  private PsnHtmlService psnHtmlService;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private PubFulltextPsnRcmdDao pubFulltextPsnRcmdDao;
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
  private PsnStatisticsSyncProducer psnStatisticsSyncProducer;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private RolPsnStatisticsDao rolPsnStatisticsDao;
  @Autowired
  private RcmdPsnStatisticsDao rcmdPsnStatisticsDao;
  @Autowired
  private PublicationDao publicationDao;

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

  /**
   * 更新保存成果全文推荐总数_MJG_SCM-5991.
   * 
   * @param psnId
   * @param pubFullTextReSum
   * @throws ServiceException
   */
  public void updatePsnStatisByPubFull(Long psnId, Integer pubFullTextReSum) {
    try {
      PsnStatistics ps = this.initPsnStatics(psnId);
      ps.setPubFullTextSum(pubFullTextReSum);
      // 属性为null的保存为0
      buildZero(ps);
      psnStatisticsDao.save(ps);
      this.psnStatisticsSync(ps);
    } catch (Exception e) {
      logger.error("设置用户待认领成果数据", e);
      throw new RuntimeException("设置用户待认领成果数据", e);
    }
  }

  /**
   * 同步人员统计信息
   * 
   * @param psnStatistics
   * @throws ServiceException
   */
  private void psnStatisticsSync(PsnStatistics psnStatistics) {
    // TODO 2015-10-13 取消MQ -done
    psnStatisticsSyncProducer.syncMessage(psnStatistics);
  }

  @Override
  public void updatePsnStatisticsByPrj(Long psnId) throws Exception {
    PsnStatistics psnStatistics = initPsnStatics(psnId);
    if (psnStatistics != null) {
      psnStatistics.setPrjSum(projectDao.getSumProject(psnId));
      // 属性为null的保存为0
      buildZero(psnStatistics);
      psnStatisticsDao.save(psnStatistics);
      this.psnStatisticsSync(psnStatistics);
    }
  }

  @Override
  public void updatePsnStatisticsByPub(Long psnId) throws ServiceException {
    PsnStatistics psnStatistics = initPsnStatics(psnId);
    if (psnStatistics != null) {
      psnStatistics
          .setPatentSum(publicationQueryDao.countPubByPsnId(psnId, ConstPublicationType.PUB_PATENT_TYPE).intValue());
      List<Publication> pubs = publicationQueryDao.queryPubsByPsnId(psnId);
      int hidex = getHindex(pubs);
      psnStatistics.setPubSum(pubs.size());
      Long openPubSum = getOpenPubSum(psnId);
      psnStatistics.setOpenPubSum(openPubSum.intValue());
      psnStatistics.setHindex(hidex);
      psnStatistics.setCitedSum(publicationQueryDao.queryPubsCiteTimesByPsnId(psnId));
      Map<String, Integer> langMap = getLangauge(pubs);
      psnStatistics.setZhSum(NumberUtils.toInt(ObjectUtils.toString(langMap.get("zhcount"))));
      psnStatistics.setEnSum(NumberUtils.toInt(ObjectUtils.toString(langMap.get("enCount"))));
      // 属性为null的保存为0
      buildZero(psnStatistics);
      psnStatisticsDao.save(psnStatistics);
      this.psnStatisticsSync(psnStatistics);
    }
  }

  private long getOpenPubSum(Long psnId) {
    List<Integer> permissions = new ArrayList<Integer>();
    permissions.add(PsnCnfConst.ALLOWS);// 默认公开
    try {
      // 查询关联权限表的公开成果数
      Long pubCount1 = this.publicationDao.queryPsnPublicPubCount(psnId, null, null, permissions, null);
      // 获取因其它导入方式在权限表没有记录的成果数，这些成果默认为公开
      Long pubCount2 = this.publicationDao.getPsnNotExistsResumePubCount(psnId);

      return (pubCount1 + pubCount2);
    } catch (Exception e) {
      logger.error(String.format("查询用户psnId=${1}的公开成果数出现异常：", psnId), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void updatePsnStatisticsByGroup(Long psnId) throws ServiceException {
    PsnStatistics psnStatistics = initPsnStatics(psnId);
    if (psnStatistics != null) {
      psnStatistics.setGroupSum(groupInvitePsnDao.getGroupCountByPsnId(psnId));
      // 属性为null的保存为0
      buildZero(psnStatistics);
      psnStatisticsDao.save(psnStatistics);
      this.psnStatisticsSync(psnStatistics);
    }
  }

  @Override
  public void updatePsnStatisticsByFrd(Long psnId) throws ServiceException {
    PsnStatistics psnStatistics = initPsnStatics(psnId);
    if (psnStatistics != null) {
      Long psnFrdSum = friendDao.getFriendCount(psnId);
      psnStatistics.setFrdSum(psnFrdSum.intValue());
      // 属性为null的保存为0
      buildZero(psnStatistics);
      psnStatisticsDao.save(psnStatistics);
      this.psnStatisticsSync(psnStatistics);
    }
  }

  @Override
  public void updatePsnStatisticsByPubAward(Long psnId) throws ServiceException {
    PsnStatistics psnStatistics = initPsnStatics(psnId);
    if (psnStatistics != null) {
      Long count = awardStatisticsDao.countAward(psnId, 1);
      psnStatistics.setPubAwardSum(count.intValue());
      // 属性为null的保存为0
      buildZero(psnStatistics);
      psnStatisticsDao.save(psnStatistics);
      this.psnStatisticsSync(psnStatistics);
    }
  }

  @Override
  public void setPsnPendingConfirmPubNum(Long psnId, Integer pubNum) throws ServiceException {
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

  // 更新人员统计表群组数
  @Override
  public void refreshPsnGroupStatistics(Long psnId) throws ServiceException {
    PsnStatistics psnStatistics = this.initPsnStatics(psnId);
    if (psnStatistics != null) {
      Integer groupCount = groupInvitePsnDao.getGroupCountByPsnId(psnId);
      psnStatistics.setGroupSum(groupCount);
      // 属性为null的保存为0
      buildZero(psnStatistics);
      psnStatisticsDao.save(psnStatistics);
      // 更新rol库人员统计表群组数
      RolPsnStatistics rolPsnStatistics = rolPsnStatisticsDao.findByPsnId(psnId);
      if (rolPsnStatistics != null) {
        try {
          psnStatistics.setPsnId(rolPsnStatistics.getPsnId());
          BeanUtils.copyProperties(rolPsnStatistics, psnStatistics);
        } catch (Exception e) {
          logger.error("更新rol库人员统计表群组数出错psnId=" + psnId, e);
          throw new ServiceException("更新rol库人员统计表群组数出错psnId=" + psnId, e);
        }
        // 属性为null的保存为0
        buildZeroByRol(rolPsnStatistics);
        rolPsnStatisticsDao.save(rolPsnStatistics);
      }
      // 更新人员html信息
      psnHtmlService.saveToRefreshTask(psnId);
      // 更新rcmd库人员统计表群组数
      RcmdPsnStatistics rcmdPsnStatistics = rcmdPsnStatisticsDao.findByPsnId(psnId);
      if (rcmdPsnStatistics != null) {
        try {
          psnStatistics.setPsnId(rcmdPsnStatistics.getPsnId());
          BeanUtils.copyProperties(rcmdPsnStatistics, psnStatistics);
        } catch (Exception e) {
          logger.error("更新rcmd库人员统计表群组数出错psnId=" + psnId, e);
          throw new ServiceException("更新rcmd库人员统计表群组数出错psnId=" + psnId, e);
        }
        // 属性为null的保存为0
        buildZeroByRcmd(rcmdPsnStatistics);
        rcmdPsnStatisticsDao.save(rcmdPsnStatistics);
      }
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

  }

  private void buildZeroByRol(RolPsnStatistics rps) {
    // 1.成果总数
    if (rps.getPubSum() == null) {
      rps.setPubSum(0);
    }
    // 2.成果引用次数总数
    if (rps.getCitedSum() == null) {
      rps.setCitedSum(0);
    }
    // 3.hindex指数
    if (rps.getHindex() == null) {
      rps.setHindex(0);
    }
    // 4.中文成果数
    if (rps.getZhSum() == null) {
      rps.setZhSum(0);
    }
    // 5.英文成果数
    if (rps.getEnSum() == null) {
      rps.setEnSum(0);
    }
    // 6.项目总数
    if (rps.getPrjSum() == null) {
      rps.setPrjSum(0);
    }
    // 7.好友总数
    if (rps.getFrdSum() == null) {
      rps.setFrdSum(0);
    }
    // 8.群组总数
    if (rps.getGroupSum() == null) {
      rps.setGroupSum(0);
    }
    // 9.成果被赞的总数
    if (rps.getPubAwardSum() == null) {
      rps.setPubAwardSum(0);
    }
    // 10.专利数
    if (rps.getPatentSum() == null) {
      rps.setPatentSum(0);
    }
  }

  private void buildZeroByRcmd(RcmdPsnStatistics rps) {
    // 1.成果总数
    if (rps.getPubSum() == null) {
      rps.setPubSum(0);
    }
    // 2.成果引用次数总数
    if (rps.getCitedSum() == null) {
      rps.setCitedSum(0);
    }
    // 3.hindex指数
    if (rps.getHindex() == null) {
      rps.setHindex(0);
    }
    // 4.中文成果数
    if (rps.getZhSum() == null) {
      rps.setZhSum(0);
    }
    // 5.英文成果数
    if (rps.getEnSum() == null) {
      rps.setEnSum(0);
    }
    // 6.项目总数
    if (rps.getPrjSum() == null) {
      rps.setPrjSum(0);
    }
    // 7.好友总数
    if (rps.getFrdSum() == null) {
      rps.setFrdSum(0);
    }
    // 8.群组总数
    if (rps.getGroupSum() == null) {
      rps.setGroupSum(0);
    }
    // 9.成果被赞的总数
    if (rps.getPubAwardSum() == null) {
      rps.setPubAwardSum(0);
    }
    // 10.专利数
    if (rps.getPatentSum() == null) {
      rps.setPatentSum(0);
    }
    // 11.待认领成果数
    if (rps.getPcfPubSum() == null) {
      rps.setPcfPubSum(0);
    }
    // 12.成果全文推荐数
    if (rps.getPubFullTextSum() == null) {
      rps.setPubFullTextSum(0);
    }
  }

}
