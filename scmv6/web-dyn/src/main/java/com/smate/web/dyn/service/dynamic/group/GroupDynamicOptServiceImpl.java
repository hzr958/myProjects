package com.smate.web.dyn.service.dynamic.group;

import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.project.model.ProjectStatistics;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.string.IrisStringUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.dao.dynamic.group.GroupDynamicAwardsDao;
import com.smate.web.dyn.dao.dynamic.group.GroupDynamicCommentsDao;
import com.smate.web.dyn.dao.dynamic.group.GroupDynamicShareDao;
import com.smate.web.dyn.dao.dynamic.group.GroupDynamicStatisticDao;
import com.smate.web.dyn.dao.fund.FundStatisticsDao;
import com.smate.web.dyn.dao.news.NewsShareDao;
import com.smate.web.dyn.dao.news.NewsStatisticsDao;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubShareDAO;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubStatisticsDAO;
import com.smate.web.dyn.dao.pub.ProjectStatisticsDao;
import com.smate.web.dyn.dao.pub.PubShareDAO;
import com.smate.web.dyn.dao.pub.PubStatisticsDAO;
import com.smate.web.dyn.dao.share.ShareStatisticsDao;
import com.smate.web.dyn.exception.DynGroupException;
import com.smate.web.dyn.form.dynamic.group.GroupDynOptForm;
import com.smate.web.dyn.form.dynamic.group.GroupDynProduceForm;
import com.smate.web.dyn.model.dynamic.group.GroupDynamicAwards;
import com.smate.web.dyn.model.dynamic.group.GroupDynamicComments;
import com.smate.web.dyn.model.dynamic.group.GroupDynamicShare;
import com.smate.web.dyn.model.dynamic.group.GroupDynamicStatistic;
import com.smate.web.dyn.model.fund.FundStatistics;
import com.smate.web.dyn.model.news.NewsShare;
import com.smate.web.dyn.model.news.NewsStatistics;
import com.smate.web.dyn.model.pdwhpub.PdwhPubSharePO;
import com.smate.web.dyn.model.pdwhpub.PdwhPubStatisticsPO;
import com.smate.web.dyn.model.pub.PubSharePO;
import com.smate.web.dyn.model.pub.PubStatisticsPO;
import com.smate.web.dyn.model.share.ShareStatistics;
import com.smate.web.dyn.service.dynamic.restful.group.GroupDynamicProduceController;
import com.smate.web.dyn.service.share.ShareStatisticsService;
import com.smate.web.dyn.service.statistic.StatisticsService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("groupDynamicOptService")
@Transactional(rollbackFor = Exception.class)
public class GroupDynamicOptServiceImpl implements GroupDynamicOptService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Resource
  private GroupDynamicProduceController groupDynamicProduceController;

  @Resource
  private GroupDynamicStatisticDao groupDynamicStatisticDao;
  @Resource
  private GroupDynamicAwardsDao groupDynamicAwardsDao;
  @Resource
  private GroupDynamicCommentsDao groupDynamicCommentsDao;
  @Resource
  private GroupDynamicShareDao groupDynamicShareDao;

  @Autowired
  private ShareStatisticsService shareStatisticsService;

  @Autowired
  private StatisticsService statisticsService;

  @Autowired
  private ShareStatisticsDao shareStatisticsDao;

  @Autowired
  private ProjectStatisticsDao projectStatisticsDao;

  @Autowired
  private FundStatisticsDao fundStatisticsDao;

  @Autowired
  private PubStatisticsDAO pubStatisticsDAO;
  @Autowired
  private PubShareDAO pubShareDAO;
  @Autowired
  private PdwhPubStatisticsDAO pdwhStatisticsDAO;
  @Autowired
  private PdwhPubShareDAO pdwhPubShareDAO;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private NewsShareDao newsShareDao;
  @Autowired
  private NewsStatisticsDao newsStatisticsDao;

  @Override
  public String produceGroupDyn(GroupDynProduceForm form) throws DynGroupException {
    Map<String, Object> serviceMap = new HashMap<String, Object>();
    serviceMap.put("psnId", form.getPsnId());
    serviceMap.put("groupId", form.getGroupId());
    serviceMap.put("resType", form.getResType());
    serviceMap.put("resId", form.getResId());
    serviceMap.put("dynContent", HtmlUtils.htmlUnescape(form.getDynContent()));
    serviceMap.put("tempType", form.getTempType());
    serviceMap.put("receiverGrpId", form.getReceiverGrpId());
    serviceMap.put("receiverGrpId", form.getReceiverGrpId());
    serviceMap.put("dbId", form.getDbId());
    serviceMap.put("resInfoJson", form.getResInfoJson());
    Object obj = groupDynamicProduceController.getScmOpenData(serviceMap);
    try {
      Integer resType = null;
      Long resOwnerID = null;
      switch (form.getResType()) {
        case "pub":
        case "fulltext":
          resType = 1;
          // SCM-23563
          addPubShareStatistics(form, form.getResId());
          sysSnsPdwhShareStatistics(form);
          break;
        case "pdwhpub":
          resType = 22;
          addPdwhPubShareStatistics(form.getResId(), form);
          Long pdwhPubId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(form.getResId());
          if (pdwhPubId != null && pdwhPubId > 0) {
            List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsOpenPubIds(pdwhPubId, 0L);
            if (CollectionUtils.isNotEmpty(snsPubIds)) {
              for (Long snsPubId : snsPubIds) {
                addPubShareStatistics(form, snsPubId);// 个人库数据同步
              }
            }
          }
          break;
        case "fund":
          resType = 11;
          addFundShareStatistics(form);
          break;
        case "prj":
          resType = 4;
          addPrjShareStatistics(form);
          break;
        case "agency":
          resType = 25;
          break;
        case "news":
          resType = 26;
          addNewsShareStatistics(form);
          break;
        case "grpfile":
          resType = DynamicConstants.RES_TYPE_GROUP_FILE;

          break;
        case "psnfile":
          resType = DynamicConstants.RES_TYPE_FILE;

          break;
        default:
          break;
      }
      // 获取资源拥有者的id
      resOwnerID = statisticsService.findPsnId(form.getResId(), 1, resType);
      // 将记录保存到分享记录表中
      if (resOwnerID != null) {
        shareStatisticsService.addShareRecord(form.getPsnId(), resOwnerID, form.getResId(), resType);// ResPsnId
      }
    } catch (DynException e) {
      logger.error("生成群组动态出错", e);
    }

    if (obj != null) {
      return obj.toString();
    }
    return "error";
  }

  /**
   * 动态赞
   */
  @Override
  public Map<String, Object> groupDynAward(GroupDynOptForm form) throws DynGroupException {
    HashMap<String, Object> map = new HashMap<>();
    // 赞统计
    GroupDynamicStatistic groupDynamicStatistic = groupDynamicStatisticDao.get(form.getDynId());
    if (groupDynamicStatistic == null) {
      groupDynamicStatistic = new GroupDynamicStatistic();
      groupDynamicStatistic.setDynId(form.getDynId());
    }
    // 赞记录
    GroupDynamicAwards groupDynamicAwards =
        groupDynamicAwardsDao.getGroupDynamicAwardsBydynIdAndPsnId(form.getDynId(), form.getPsnId());
    if (groupDynamicAwards == null) {
      groupDynamicAwards = new GroupDynamicAwards();
      groupDynamicAwards.setDynId(form.getDynId());
      groupDynamicAwards.setAwardPsnId(form.getPsnId());
    }
    groupDynamicAwards.setAwardDate(new Date());
    // 反转点赞状态
    LikeStatusEnum action =
        groupDynamicAwards.getStatus() == LikeStatusEnum.LIKE ? LikeStatusEnum.UNLIKE : LikeStatusEnum.LIKE;
    Integer awardCount = groupDynamicStatistic.getAwardCount();
    if (action == LikeStatusEnum.LIKE) { // 点赞
      awardCount += 1;
    } else if (action == LikeStatusEnum.UNLIKE) { // 取消赞
      awardCount = awardCount.intValue() > 0 ? awardCount.intValue() - 1 : 0;
    }
    groupDynamicStatistic.setAwardCount(awardCount);
    groupDynamicAwards.setStatus(action);
    map.put("hasAward", action == LikeStatusEnum.LIKE ? true : false);

    groupDynamicStatisticDao.save(groupDynamicStatistic);
    groupDynamicAwardsDao.save(groupDynamicAwards);
    map.put("awardCount", groupDynamicStatistic.getAwardCount());
    return map;
  }

  public void sysSnsPdwhShareStatistics(GroupDynProduceForm form) {
    Long pdwhPubId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(form.getResId());
    if (pdwhPubId != null && pdwhPubId > 0) {
      addPdwhPubShareStatistics(pdwhPubId, form);// 基准库数据同步
      List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsOpenPubIds(pdwhPubId, 0L);
      if (CollectionUtils.isNotEmpty(snsPubIds)) {
        for (Long snsPubId : snsPubIds) {
          if (!snsPubId.equals(form.getResId())) {
            addPubShareStatistics(form, snsPubId);// 个人库数据同步
          }
        }
      }
    }
  }

  /**
   * 动态评论
   */
  @Override
  public void groupDynComment(GroupDynOptForm form) throws DynGroupException {
    // 评论统计
    GroupDynamicStatistic groupDynamicStatistic = groupDynamicStatisticDao.get(form.getDynId());
    if (groupDynamicStatistic == null) {
      groupDynamicStatistic = new GroupDynamicStatistic();
      groupDynamicStatistic.setDynId(form.getDynId());
    }
    Integer commentCount = groupDynamicStatistic.getCommentCount() + 1;
    form.setCommentCount(commentCount);
    groupDynamicStatistic.setCommentCount(commentCount);
    GroupDynamicComments groupDynamicComments;
    groupDynamicComments = new GroupDynamicComments();
    groupDynamicComments.setDynId(form.getDynId());
    groupDynamicComments.setCommentPsnId(form.getPsnId());
    groupDynamicComments.setCommentContent(IrisStringUtils.subMaxLengthString(form.getCommentContent(), 500));
    groupDynamicComments.setCommentDate(new Date());
    groupDynamicComments.setStatus(0);
    groupDynamicComments.setCommentResId(form.getCommentResId());
    groupDynamicCommentsDao.save(groupDynamicComments);
    groupDynamicStatisticDao.save(groupDynamicStatistic);

  }

  @Override
  public void groupDynShare(GroupDynOptForm form) throws DynGroupException {
    // 分享统计
    GroupDynamicStatistic groupDynamicStatistic = groupDynamicStatisticDao.get(form.getDynId());
    if (groupDynamicStatistic == null) {
      groupDynamicStatistic = new GroupDynamicStatistic();
      groupDynamicStatistic.setDynId(form.getDynId());
    }
    groupDynamicStatistic.setShareCount(groupDynamicStatistic.getShareCount() + 1);
    GroupDynamicShare groupDynamicShare;
    groupDynamicShare = new GroupDynamicShare();
    groupDynamicShare.setDynId(form.getDynId());
    groupDynamicShare.setSharePsnId(form.getPsnId());
    groupDynamicShare.setShareContent(form.getShareContent());
    groupDynamicShare.setShareDate(new Date());
    groupDynamicShare.setStatus(0);
    groupDynamicShareDao.save(groupDynamicShare);
    groupDynamicStatisticDao.save(groupDynamicStatistic);
  }

  // ---------------------------------------------

  private void addPrjShareStatistics(GroupDynProduceForm form) {
    ProjectStatistics ps = projectStatisticsDao.get(form.getResId());
    if (ps == null) {
      ps = new ProjectStatistics();
      ps.setProjectId(form.getResId());
      ps.setShareCount(0);
    }
    if (ps.getShareCount() == null) {
      ps.setShareCount(0);
    }
    ps.setShareCount(ps.getShareCount() + 1);
    projectStatisticsDao.save(ps);

  }

  private void addFundShareStatistics(GroupDynProduceForm form) {
    FundStatistics fs = fundStatisticsDao.get(form.getResId());
    if (fs == null) {
      fs = new FundStatistics();
      fs.setFundId(form.getResId());
      fs.setShareCount(0);
    }
    if (fs.getShareCount() == null) {
      fs.setShareCount(0);
    }
    /* fs.setShareCount(fs.getShareCount() + 1); */
    fundStatisticsDao.save(fs);
  }

  private void addPdwhPubShareStatistics(Long resId, GroupDynProduceForm form) {
    PdwhPubSharePO pdwhPubShare = new PdwhPubSharePO();
    pdwhPubShare.setPdwhPubId(resId);
    pdwhPubShare.setPsnId(form.getPsnId());
    pdwhPubShare.setComment(form.getDynContent());
    pdwhPubShare.setPlatform(3);
    pdwhPubShare.setStatus(0);// 状态 0=正常 ; 9=删除
    pdwhPubShare.setGmtCreate(new Date());
    pdwhPubShare.setGmtModified(new Date());
    pdwhPubShare.setSharePsnGroupId(form.getReceiverGrpId());
    pdwhPubShareDAO.save(pdwhPubShare);

    PdwhPubStatisticsPO pdwhPubStatisticsPO = pdwhStatisticsDAO.get(resId);
    if (pdwhPubStatisticsPO == null) {
      pdwhPubStatisticsPO = new PdwhPubStatisticsPO();
      pdwhPubStatisticsPO.setPdwhPubId(resId);
      pdwhPubStatisticsPO.setShareCount(1);
    } else {
      int shareCount = pdwhPubStatisticsPO.getShareCount() == null ? 0 : pdwhPubStatisticsPO.getShareCount();
      pdwhPubStatisticsPO.setShareCount(shareCount + 1);
    }
    pdwhStatisticsDAO.save(pdwhPubStatisticsPO);

  }

  private void addPubShareStatistics(GroupDynProduceForm form, Long snsPubId) {
    PubSharePO pubShare = new PubSharePO();
    pubShare.setPubId(snsPubId);
    pubShare.setPsnId(form.getPsnId());
    pubShare.setComment(form.getDynContent());
    pubShare.setPlatform(3);// 成果分享平台 SharePlatformEnum
    pubShare.setStatus(0);
    pubShare.setGmtCreate(new Date());
    pubShare.setGmtModified(new Date());
    pubShare.setSharePsnGroupId(form.getReceiverGrpId());
    pubShareDAO.save(pubShare);

    PubStatisticsPO s = pubStatisticsDAO.get(snsPubId);
    if (s == null) {
      s = new PubStatisticsPO();
      s.setPubId(snsPubId);
      s.setShareCount(0);
      s.setAwardCount(0);
      s.setCommentCount(0);
      s.setReadCount(1);
      s.setRefCount(0);
    }
    if (s.getShareCount() == null) {
      s.setShareCount(0);
    }
    s.setShareCount(s.getShareCount() + 1);
    pubStatisticsDAO.save(s);

  }

  public void addNewsShareStatistics(GroupDynProduceForm form) {
    NewsShare newsShare = new NewsShare();
    newsShare.setNewsId(form.getResId());
    newsShare.setSharePsnId(form.getPsnId());
    newsShare.setBeSharedId(form.getGroupId());
    newsShare.setContent(form.getDynContent());
    newsShare.setPlatform(3);
    newsShare.setStatus(0);// 状态 0=正常 ; 9=删除
    newsShare.setGmtCreate(new Date());
    newsShare.setGmtUpdate(new Date());
    newsShareDao.save(newsShare);

    NewsStatistics newsStatistics = newsStatisticsDao.get(form.getResId());
    if (newsStatistics == null) {
      newsStatistics = new NewsStatistics();
      newsStatistics.setNewsId(form.getResId());
      newsStatistics.setShareCount(1);
    } else {
      int shareCount = newsStatistics.getShareCount() == null ? 0 : newsStatistics.getShareCount();
      newsStatistics.setShareCount(shareCount + 1);
    }
    newsStatisticsDao.save(newsStatistics);
  }


  // ------------------------------------------------

  // ------------------------------------------------

  public void addShareStatisticsRecord(Long psnId, Long SharePsnId, Long actionKey, Integer actionType)
      throws DynException {

    try {
      if (!DynamicConstants.SHARE_TYPE_MAP.containsKey(actionType)) {
        logger.warn("分享统计，分享类型actionType=" + actionType + "的记录，不需要保存");
        return;
      }
      ShareStatistics shareStatistics = new ShareStatistics();
      shareStatistics.setPsnId(psnId);
      shareStatistics.setSharePsnId(SharePsnId);
      shareStatistics.setActionKey(actionKey);
      shareStatistics.setActionType(actionType);
      Date nowDate = new Date();
      shareStatistics.setCreateDate(nowDate);
      shareStatistics.setFormateDate(DateUtils.getDateTime(nowDate));
      shareStatistics.setIp(Struts2Utils.getRemoteAddr());
      shareStatisticsDao.save(shareStatistics);

    } catch (Exception e) {
      logger.error("保存分享记录出错！PsnId=" + psnId + " sharePsnId=" + SharePsnId + " actionKey=" + actionKey + " actionType"
          + actionType, e);
      throw new DynException(e);
    }
  }

  @Override
  public String produceGroupDynWithoutUpdateStatistics(GroupDynProduceForm form) throws DynGroupException {
    Map<String, Object> serviceMap = new HashMap<String, Object>();
    serviceMap.put("psnId", form.getPsnId());
    serviceMap.put("groupId", form.getGroupId());
    serviceMap.put("resType", form.getResType());
    serviceMap.put("resId", form.getResId());
    serviceMap.put("dynContent", HtmlUtils.htmlUnescape(form.getDynContent()));
    serviceMap.put("tempType", form.getTempType());
    serviceMap.put("receiverGrpId", form.getReceiverGrpId());
    serviceMap.put("receiverGrpId", form.getReceiverGrpId());
    serviceMap.put("dbId", form.getDbId());
    serviceMap.put("resInfoJson", form.getResInfoJson());
    Object result = groupDynamicProduceController.getScmOpenData(serviceMap);
    if (result != null) {
      return result.toString();
    }
    return "error";
  }

  // -----------------------------------------------

}
