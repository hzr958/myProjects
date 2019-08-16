package com.smate.web.dyn.service.dynamic;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.dyn.dao.DynamicMsgDao;
import com.smate.core.base.dyn.model.DynamicMsg;
import com.smate.core.base.email.service.EmailSendService;
import com.smate.core.base.email.service.MailInitDataService;
import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.project.model.ProjectStatistics;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.MapBuilder;
import com.smate.web.dyn.dao.dynamic.DynStatisticsDao;
import com.smate.web.dyn.dao.dynamic.DynamicAwardPsnDao;
import com.smate.web.dyn.dao.dynamic.DynamicAwardResDao;
import com.smate.web.dyn.dao.fund.AgencyStatisticsDao;
import com.smate.web.dyn.dao.fund.FundAgencyAwardDao;
import com.smate.web.dyn.dao.fund.FundAwardDao;
import com.smate.web.dyn.dao.fund.FundStatisticsDao;
import com.smate.web.dyn.dao.news.NewsLikeDao;
import com.smate.web.dyn.dao.news.NewsStatisticsDao;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubLikeDAO;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubStatisticsDAO;
import com.smate.web.dyn.dao.pdwhpub.PubPdwhDAO;
import com.smate.web.dyn.dao.psn.AwardStatisticsDao;
import com.smate.web.dyn.dao.pub.ProjectStatisticsDao;
import com.smate.web.dyn.dao.pub.PubLikeDAO;
import com.smate.web.dyn.dao.pub.PubSimpleDao;
import com.smate.web.dyn.dao.pub.PubSnsDAO;
import com.smate.web.dyn.dao.pub.PubStatisticsDAO;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.dynamic.DynStatistics;
import com.smate.web.dyn.model.dynamic.DynamicAwardPsn;
import com.smate.web.dyn.model.dynamic.DynamicAwardRes;
import com.smate.web.dyn.model.fund.AgencyStatistics;
import com.smate.web.dyn.model.fund.FundAgencyAward;
import com.smate.web.dyn.model.fund.FundAward;
import com.smate.web.dyn.model.fund.FundStatistics;
import com.smate.web.dyn.model.news.NewsLike;
import com.smate.web.dyn.model.news.NewsStatistics;
import com.smate.web.dyn.model.pdwhpub.PdwhPubLikePO;
import com.smate.web.dyn.model.pdwhpub.PdwhPubStatisticsPO;
import com.smate.web.dyn.model.psn.AwardStatistics;
import com.smate.web.dyn.model.pub.PubLikePO;
import com.smate.web.dyn.model.pub.PubPdwhPO;
import com.smate.web.dyn.model.pub.PubSimple;
import com.smate.web.dyn.model.pub.PubStatisticsPO;
import com.smate.web.dyn.service.psn.PersonQueryservice;
import com.smate.web.dyn.service.statistic.AwardStatisticsService;
import com.smate.web.dyn.service.statistic.StatisticsService;

/**
 * 动态赞服务
 * 
 * @author zk
 *
 */
@Service("dynamicAwardService")
@Transactional(rollbackFor = Exception.class)
public class DynamicAwardServiceImpl implements DynamicAwardService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private AwardStatisticsDao awardStatisticsDao;
  @Autowired
  private PersonQueryservice personQueryservice;
  @Autowired
  private DynamicAwardResDao dynamicAwardResDao;
  @Autowired
  private DynamicAwardPsnDao dynamicAwardPsnDao;
  @Autowired
  private DynStatisticsDao dynStatisticsDao;
  @Autowired
  private AwardStatisticsService awardStatisticsService;
  @Autowired
  private StatisticsService statisticsService;
  @Autowired
  private EmailSendService likedYourPubEmailService;
  @Autowired
  private DynamicRealtimeService dynamicRealtimeService;
  @Autowired
  private FundAwardDao fundAwardDao;
  @Autowired
  private MailInitDataService mailInitDataService;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PubSimpleDao pubSimpleDao;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private FundStatisticsDao fundStatisticsDao;
  @Autowired
  private PubStatisticsDAO pubStatisticsDAO;
  @Autowired
  private DynamicMsgDao dynamicMsgDao;
  @Autowired
  private ProjectStatisticsDao projectStatisticsDao;
  @Autowired
  private ProjectDao projectDao;
  @Autowired
  private PubLikeDAO pubLikeDAO;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PdwhPubLikeDAO pdwhPubLikeDAO;
  @Autowired
  private PdwhPubStatisticsDAO pdwhPubStatisticsDAO;
  @Autowired
  private FundAgencyAwardDao fundAgencyAwardDao;
  @Autowired
  private AgencyStatisticsDao agencyStatisticsDao;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private NewsLikeDao newsLikeDao;
  @Autowired
  private NewsStatisticsDao newsStatisticsDao;

  /**
   * 点击赞
   */
  @Override
  public String addAward(DynamicForm form) throws DynException {
    MapBuilder mb = MapBuilder.getInstance();
    // 当前人员信息
    Long userId = SecurityUtils.getCurrentUserId();
    Person person = personQueryservice.findPersonBase(userId);
    // 查找拥有者
    Long ownerPsnId = null;
    DynamicAwardRes dynamicAwardRes;
    // 初始化当前行为-(赞/取消赞)-赞要发动态和邮件
    form.setHasAward(LikeStatusEnum.LIKE);
    // 初始化赞次数
    form.setAwardTimes(0L);
    form.setResNode(1);
    // SCM-23420 数据来源调整 跟基准库关联的成果，就共用基准库的数据；不是关联成果 就独立一份自己的数据
    Long pdwhPubId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(form.getResId());
    // 根据类型处理
    if ("B2TEMP".equals(form.getDynType()) || "B3TEMP".equals(form.getDynType())) {
      switch (form.getResType()) {
        case DynamicConstants.RES_TYPE_PUB:// 成果
        case DynamicConstants.RES_TYPE_REF:// 文献
          ownerPsnId = statisticsService.findPsnId(form.getResId(), form.getResNode(), form.getResType());
          if (ownerPsnId == null || ownerPsnId == 0L) {
            // 找不到资源拥有者
            mb.put("result", "resNotExist");
            return mb.getJson();
          }
          buildSnsAward(form, userId, ownerPsnId);
          if (pdwhPubId != null && pdwhPubId > 0) {// 关联成果进行数据同步
            ownerPsnId = statisticsService.findPsnId(form.getResId(), form.getResNode(), form.getResType());
            buildPdwhStatistics(pdwhPubId, form, userId);// 基准库数据同步
            List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsOpenPubIds(pdwhPubId, 0L);
            if (CollectionUtils.isNotEmpty(snsPubIds)) {
              for (Long snsPubId : snsPubIds) {
                if (!snsPubId.equals(form.getResId())) {
                  buildSnsStatistics(form, userId, ownerPsnId, snsPubId);// 个人库数据同步
                }
              }
            }
          }
          break;
        case DynamicConstants.RES_TYPE_FUND:// 基金推荐
          ownerPsnId = statisticsService.findPsnId(form.getResId(), form.getResNode(), form.getResType());
          // 记录基金赞
          fundAward(form, userId);
          // 基金统计数更新
          updateFundStatistics(form);
          break;
        case DynamicConstants.RES_TYPE_JOURNALAWARD:// 期刊
        case DynamicConstants.RES_TYPE_PUB_PDWH:// 基准库成果
          ownerPsnId = statisticsService.findPsnId(form.getResId(), form.getResNode(), form.getResType());
          buildPdwhAward(form.getResId(), form, userId);
          List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsOpenPubIds(pdwhPubId, 0L);
          if (CollectionUtils.isNotEmpty(snsPubIds)) {
            for (Long snsPubId : snsPubIds) {
              buildSnsStatistics(form, userId, ownerPsnId, snsPubId);// 个人库数据同步
            }
          }
          break;
        case DynamicConstants.RES_TYPE_PRJ:// 项目
          ownerPsnId = projectDao.findPsnId(form.getResId());
          // 记录项目赞
          prjAward(form, userId, ownerPsnId);
          // 记录项目统计数
          updatePrjStatistics(form);
          break;
        case DynamicConstants.RES_TYPE_AGENCY:// 资助机构
          ownerPsnId = statisticsService.findPsnId(form.getResId(), form.getResNode(), form.getResType());
          // 记录基金赞
          agencyAward(form, userId);
          // 资助机构统计数更新
          updateAgencyStatistics(form);
          break;
        case DynamicConstants.RES_TYPE_NEWS:// 新闻推荐
          // 记录新闻赞
          newsAward(form, userId);
          // 新闻统计数更新
          updateNewsStatistics(form);
          break;
        // TODO 以后有其他资源类型往这里加

        default:// 其他resType默认为同动态处理
          // TODO
          break;
      }
      dynamicAwardRes =
          insertAwardRecord(person, form.getResType(), form.getResNode(), form.getResId(), form.getHasAward());
    } else {// 其他dynType默认为同动态处理
      if (form.getParentDynId() == null) {
        form.setParentDynId(dynamicMsgDao.getParentDynId(form.getDynId()));
      }
      ownerPsnId =
          statisticsService.findPsnId(form.getParentDynId(), form.getResNode(), DynamicConstants.RES_TYPE_NORMAL);
      // 记录动态赞
      dynAward(form, ownerPsnId, userId);
      // 更新动态赞统计数
      dynStatisticsSyncToSns(form, form.getHasAward());
      dynamicAwardRes = insertAwardRecord(person, DynamicConstants.RES_TYPE_NORMAL, form.getResNode(),
          form.getParentDynId(), form.getHasAward());
    }
    // 如果是赞-产生动态-发邮件
    if (form.getHasAward() == LikeStatusEnum.LIKE) {
      // 修改要发的动态的模版类型
      form.setDynType(form.getNextDynType());
      dynamicRealtimeService.dynamicRealtime(form);
      // 纯成果动态要发邮件-目前只发了resType=1
      if (ownerPsnId != null && !ownerPsnId.equals(userId) && form.getResType() == DynamicConstants.RES_TYPE_PUB
          && ("B2TEMP".equals(form.getDynType()) || "B3TEMP".equals(form.getDynType()))) {
        if (pdwhPubId != null && pdwhPubId > 0) {
          sendEmail(form, userId, pdwhPubId);// 通知所有关联成果的所属用户
        } else {
          // 每人对同一个资源只能发一次邮件
          int awardCount = dynamicAwardPsnDao.getAwardCount(form.getResId(), userId, form.getResType());
          if (awardCount <= 1) {
            sendAwardEmail(userId, ownerPsnId, form.getResId());
          }
        }
      }
      if (dynamicAwardRes != null) {
        mb.put("awardId", dynamicAwardRes.getAwardId());
      }
    }
    mb.put("action", form.getHasAward().getValue());
    mb.put("awardTimes", form.getAwardTimes());
    mb.put("result", "success");
    return mb.getJson();
  }

  public void sendEmail(DynamicForm form, Long userId, Long pdwhPubId) {
    List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsOpenPubIds(pdwhPubId, 0L);
    if (CollectionUtils.isNotEmpty(snsPubIds)) {
      int count = dynamicAwardPsnDao.getAwardCount(form.getResId(), userId, form.getResType());
      if (count < 1) {
        List<Long> pubOwnerList = pubSnsDAO.getSnsPubList(snsPubIds);
        List<Long> newPubOwnerList = pubOwnerList.stream().distinct().collect(Collectors.toList());
        List<Map<Long, Long>> pubPsbList = pubSnsDAO.getSnsPub(snsPubIds);
        if (CollectionUtils.isNotEmpty(newPubOwnerList)) {
          for (Long ownerId : newPubOwnerList) {// 操作要通知所有关联成果的所属用户
            if (pubPsbList != null && pubPsbList.size() > 0) {
              Long newPubId = null;
              for (Map<Long, Long> map : pubPsbList) {
                if (ownerId.equals(map.get("createPsnId"))) {
                  newPubId = map.get("pubId");
                }
              }
              sendAwardEmail(userId, ownerId, newPubId);// 发送邮件通知用户
            }
          }
        }
      }
    }
  }

  public void buildSnsAward(DynamicForm form, Long userId, Long ownerPsnId) throws DynException {
    // 记录成果赞
    pubAward(form, userId, ownerPsnId);
    // 记录成果赞统计数
    updatePubStatistics(form, userId);
    // 对成果的操作需要更新到其他库
    // 同步统计 SNS_SYNC_PUB_STATISTICS 至rol
    // 更新个人成果被赞统计 , 同步三个数据库。
    awardStatisticsService.AwardPubSync(form.getResId(), ownerPsnId, form.getHasAward());
  }

  public void buildSnsStatistics(DynamicForm form, Long userId, Long ownerPsnId, Long pubId) throws DynException {
    // 记录成果赞
    pubSnsAward(form, userId, ownerPsnId, pubId);
    // 记录成果赞统计数
    updateSnsPub(form, userId, pubId);
    // 对成果的操作需要更新到其他库
    // 同步统计 SNS_SYNC_PUB_STATISTICS 至rol
    // 更新个人成果被赞统计 , 同步三个数据库。
    awardStatisticsService.AwardPubSync(pubId, ownerPsnId, form.getHasAward());
  }

  public void buildPdwhAward(Long resId, DynamicForm form, Long userId) {
    PubPdwhPO pdwhPub = pubPdwhDAO.get(resId);
    // 记录基准库成果赞
    pubPdwhAward(resId, form, userId, pdwhPub);
    // 更新基准库统计数
    updatePdwhPubStatistics(resId, form);
  }

  public void buildPdwhStatistics(Long resId, DynamicForm form, Long userId) {
    PubPdwhPO pdwhPub = pubPdwhDAO.get(resId);
    // 记录基准库成果赞
    pubPdwhLike(resId, form, userId, pdwhPub);
    // 更新基准库统计数
    pdwhPubStatistics(resId, form);
  }

  private void updatePrjStatistics(DynamicForm form) {
    ProjectStatistics statistics = projectStatisticsDao.get(form.getResId());
    if (statistics == null) {
      statistics = new ProjectStatistics();
      statistics.setProjectId(form.getResId());
      statistics.setAwardCount(0);
      statistics.setCommentCount(0);
      statistics.setShareCount(0);
      statistics.setReadCount(0);
      statistics.setRelatedPubCount(0);
      statistics.setRefCount(0);
    }
    if (form.getHasAward() == LikeStatusEnum.LIKE) {
      statistics.setAwardCount(statistics.getAwardCount() == null ? 1 : (statistics.getAwardCount() + 1));
    } else {
      statistics.setAwardCount((statistics.getAwardCount() == null || statistics.getAwardCount() <= 1) ? 0
          : (statistics.getAwardCount() - 1));
    }
    form.setAwardTimes(statistics.getAwardCount().longValue());
    projectStatisticsDao.save(statistics);

  }

  private void prjAward(DynamicForm form, Long userId, Long ownerPsnId) {
    Date date = new Date();
    AwardStatistics as =
        awardStatisticsDao.findAwardStatistics(userId, ownerPsnId, form.getResId(), DynamicConstants.RES_TYPE_PRJ);
    if (as != null) {
      as.setAction(setLikeStatus(as.getAction()));
    } else {
      as = new AwardStatistics(null, userId, ownerPsnId, form.getResId(), DynamicConstants.RES_TYPE_PRJ,
          LikeStatusEnum.LIKE, date, date.getTime(), null);
    }
    form.setHasAward(as.getAction());
    awardStatisticsDao.save(as);

  }

  /**
   * 动态赞记录
   * 
   * @param form
   * @param ownerPsnId
   * @param userId
   */
  private void dynAward(DynamicForm form, Long ownerPsnId, Long userId) {
    Date date = new Date();
    AwardStatistics as2 = awardStatisticsDao.findAwardStatistics(userId, ownerPsnId, form.getParentDynId(),
        DynamicConstants.RES_TYPE_NORMAL);
    if (as2 != null) {
      as2.setAction(setLikeStatus(as2.getAction()));
    } else {
      as2 = new AwardStatistics();
      as2.setCreateDate(date);
      as2.setPsnId(userId);
      as2.setAwardPsnId(ownerPsnId);
      as2.setActionType(DynamicConstants.RES_TYPE_NORMAL);
      as2.setActionKey(form.getParentDynId());
      as2.setAction(LikeStatusEnum.LIKE);
      as2.setFormateDate(date.getTime());
    }
    form.setHasAward(as2.getAction());
    awardStatisticsDao.save(as2);

  }

  /**
   * 更新基准库统计数
   * 
   * @param form
   * @param pdwhPub
   */
  private void updatePdwhPubStatistics(Long resId, DynamicForm form) {
    PdwhPubStatisticsPO pdwhPubStatistics = pdwhPubStatistics(resId, form);
    form.setAwardTimes(pdwhPubStatistics.getAwardCount().longValue());
  }

  public PdwhPubStatisticsPO pdwhPubStatistics(Long resId, DynamicForm form) {
    PdwhPubStatisticsPO pdwhPubStatistics = pdwhPubStatisticsDAO.get(resId);
    if (pdwhPubStatistics == null) {
      pdwhPubStatistics = new PdwhPubStatisticsPO();
    }
    if (form.getHasAward() == LikeStatusEnum.LIKE) {
      pdwhPubStatistics
          .setAwardCount(pdwhPubStatistics.getAwardCount() == null ? 1 : (pdwhPubStatistics.getAwardCount() + 1));
    } else {
      pdwhPubStatistics
          .setAwardCount((pdwhPubStatistics.getAwardCount() == null || pdwhPubStatistics.getAwardCount() <= 1) ? 0
              : (pdwhPubStatistics.getAwardCount() - 1));
    }
    pdwhPubStatisticsDAO.save(pdwhPubStatistics);
    return pdwhPubStatistics;
  }

  /**
   * 记录基准库成果赞
   * 
   * @param form
   * @param userId
   */
  private void pubPdwhAward(Long resId, DynamicForm form, Long userId, PubPdwhPO pdwhPub) {
    PdwhPubLikePO p = pdwhPubLikeDAO.findByPubIdAndPsnId(resId, userId);
    if (p != null) {
      if (p.getStatus() == null || p.getStatus() == 0) {
        p.setStatus(1);
      } else {
        p.setStatus(0);
        form.setHasAward(LikeStatusEnum.UNLIKE);
      }
    } else {
      p = new PdwhPubLikePO();
      p.setPdwhPubId(resId);
      p.setPsnId(userId);
      p.setGmtCreate(new Date());
      p.setStatus(1);
    }
    p.setGmtModified(new Date());
    pdwhPubLikeDAO.save(p);
  }

  private void pubPdwhLike(Long resId, DynamicForm form, Long userId, PubPdwhPO pdwhPub) {
    PdwhPubLikePO p = pdwhPubLikeDAO.findByPubIdAndPsnId(resId, userId);
    if (p != null) {
      if (p.getStatus() == null || p.getStatus() == 0) {
        p.setStatus(1);
      } else {
        p.setStatus(0);
      }
    } else {
      p = new PdwhPubLikePO();
      p.setPdwhPubId(resId);
      p.setPsnId(userId);
      p.setGmtCreate(new Date());
      p.setStatus(1);
    }
    p.setGmtModified(new Date());
    pdwhPubLikeDAO.save(p);
  }

  /**
   * 更新基金统计数
   * 
   * @param form
   */
  private void updateNewsStatistics(DynamicForm form) {
    NewsStatistics sta = newsStatisticsDao.get(form.getResId());
    if (sta == null) {
      sta = new NewsStatistics(form.getResId(), 0, 0, 0);
    }
    if (form.getHasAward() == LikeStatusEnum.LIKE) {
      sta.setAwardCount(sta.getAwardCount() == null ? 1 : (sta.getAwardCount() + 1));
    } else {
      sta.setAwardCount((sta.getAwardCount() == null || sta.getAwardCount() <= 1) ? 0 : (sta.getAwardCount() - 1));
    }
    form.setAwardTimes(sta.getAwardCount().longValue());
    newsStatisticsDao.save(sta);
  }

  /**
   * 记录基金赞
   * 
   * @param form
   * @param userId
   */
  private void newsAward(DynamicForm form, Long userId) {
    NewsLike nl = newsLikeDao.findByNewsIdAndPsnId(form.getResId(), userId);
    if (nl != null) {
      // 赞-》取消赞，取消赞-》赞
      nl.setStatus(CommonUtils.compareIntegerValue(nl.getStatus(), 1) ? 0 : 1);
    } else {
      nl = new NewsLike(form.getResId(), userId, 1, new Date(), new Date());
    }
    form.setHasAward(nl.getStatus() == 1 ? LikeStatusEnum.LIKE : LikeStatusEnum.UNLIKE);
    newsLikeDao.save(nl);

  }

  /**
   * 更新基金统计数
   * 
   * @param form
   */
  private void updateFundStatistics(DynamicForm form) {
    FundStatistics sta = fundStatisticsDao.findFundStatisticsById(form.getResId());
    if (sta == null) {
      sta = new FundStatistics(form.getResId(), 0, 0);
    }
    if (form.getHasAward() == LikeStatusEnum.LIKE) {
      sta.setAwardCount(sta.getAwardCount() == null ? 1 : (sta.getAwardCount() + 1));
    } else {
      sta.setAwardCount((sta.getAwardCount() == null || sta.getAwardCount() <= 1) ? 0 : (sta.getAwardCount() - 1));
    }
    form.setAwardTimes(sta.getAwardCount().longValue());
    fundStatisticsDao.save(sta);
  }

  /**
   * 记录基金赞
   * 
   * @param form
   * @param userId
   */
  private void fundAward(DynamicForm form, Long userId) {
    FundAward a = fundAwardDao.findFundAwardRecord(userId, form.getResId());
    if (a != null) {
      a.setStatus(setLikeStatus(a.getStatus()));
    } else {
      a = new FundAward(null, form.getResId(), userId, new Date(), LikeStatusEnum.LIKE);
    }
    form.setHasAward(a.getStatus());
    fundAwardDao.save(a);

  }

  /**
   * 记录赞资助机构
   * 
   * @param form
   * @param userId
   */
  private void agencyAward(DynamicForm form, Long userId) {
    FundAgencyAward award = fundAgencyAwardDao.findAgencyAwardByPsnIdAndAgencyId(userId, form.getResId());
    if (award != null) {
      // 赞-》取消赞，取消赞-》赞
      award.setStatus(CommonUtils.compareIntegerValue(award.getStatus(), 1) ? 0 : 1);
    } else {
      award = new FundAgencyAward(userId, form.getResId(), new Date(), new Date(), 1);
    }
    form.setHasAward(award.getStatus() == 1 ? LikeStatusEnum.LIKE : LikeStatusEnum.UNLIKE);
    fundAgencyAwardDao.save(award);
  }

  /**
   * 更新基金统计数
   * 
   * @param form
   */
  private void updateAgencyStatistics(DynamicForm form) {
    AgencyStatistics sta = agencyStatisticsDao.findAgencyStatistics(form.getResId());
    if (sta == null) {
      sta = new AgencyStatistics(form.getResId(), 0L, 0L, 0L);
    }
    if (form.getHasAward() == LikeStatusEnum.LIKE) {
      sta.setAwardSum(sta.getAwardSum() == null ? 1 : (sta.getAwardSum() + 1));
    } else {
      sta.setAwardSum((sta.getAwardSum() == null || sta.getAwardSum() <= 1) ? 0 : (sta.getAwardSum() - 1));
    }
    form.setAwardTimes(sta.getAwardSum().longValue());
    agencyStatisticsDao.save(sta);
  }

  /**
   * 记录成果赞统计数
   * 
   * @param form
   */
  private void updatePubStatistics(DynamicForm form, Long userId) {
    PubLikePO pubLike = pubLikeDAO.findByPubIdAndPsnId(form.getResId(), userId);
    if (pubLike != null) {
      if (pubLike.getStatus() == null || pubLike.getStatus() == 0) {
        pubLike.setStatus(1);
        form.setHasAward(LikeStatusEnum.LIKE);
      } else {
        pubLike.setStatus(0);
        form.setHasAward(LikeStatusEnum.UNLIKE);
      }
    } else {
      pubLike = new PubLikePO();
      pubLike.setPsnId(userId);
      pubLike.setPubId(form.getResId());
      pubLike.setGmtCreate(new Date());
      pubLike.setStatus(1);
    }
    pubLike.setGmtModified(new Date());
    pubLikeDAO.save(pubLike);

    PubStatisticsPO statistics = pubStatisticsDAO.get(form.getResId());
    if (statistics == null) {
      statistics = new PubStatisticsPO(form.getResId());
    }
    if (form.getHasAward() == LikeStatusEnum.LIKE) {
      statistics.setAwardCount(statistics.getAwardCount() == null ? 1 : (statistics.getAwardCount() + 1));
    } else {
      statistics.setAwardCount((statistics.getAwardCount() == null || statistics.getAwardCount() <= 1) ? 0
          : (statistics.getAwardCount() - 1));
    }
    form.setAwardTimes(statistics.getAwardCount().longValue());
    pubStatisticsDAO.save(statistics);
  }

  /**
   * 处理成果赞
   */
  private void pubAward(DynamicForm form, Long userId, Long ownerPsnId) {
    AwardStatistics as = pubSnsAward(form, userId, ownerPsnId, form.getResId());
    form.setHasAward(as.getAction());

  }

  public PubStatisticsPO updateSnsPub(DynamicForm form, Long userId, Long pubId) {
    PubLikePO pubLike = pubLikeDAO.findByPubIdAndPsnId(pubId, userId);
    if (pubLike != null) {
      if (pubLike.getStatus() == null || pubLike.getStatus() == 0) {
        pubLike.setStatus(1);
      } else {
        pubLike.setStatus(0);
      }
    } else {
      pubLike = new PubLikePO();
      pubLike.setPsnId(userId);
      pubLike.setPubId(pubId);
      pubLike.setGmtCreate(new Date());
      pubLike.setStatus(1);
    }
    pubLike.setGmtModified(new Date());
    pubLikeDAO.save(pubLike);

    PubStatisticsPO statistics = pubStatisticsDAO.get(pubId);
    if (statistics == null) {
      statistics = new PubStatisticsPO(form.getResId());
    }
    if (form.getHasAward() == LikeStatusEnum.LIKE) {
      statistics.setAwardCount(statistics.getAwardCount() == null ? 1 : (statistics.getAwardCount() + 1));
    } else {
      statistics.setAwardCount((statistics.getAwardCount() == null || statistics.getAwardCount() <= 1) ? 0
          : (statistics.getAwardCount() - 1));
    }
    pubStatisticsDAO.save(statistics);
    return statistics;
  }

  public AwardStatistics pubSnsAward(DynamicForm form, Long userId, Long ownerPsnId, Long pubId) {
    Date date = new Date();
    AwardStatistics as =
        awardStatisticsDao.findAwardStatistics(userId, ownerPsnId, pubId, DynamicConstants.RES_TYPE_PUB);
    if (as != null) {
      as.setAction(setLikeStatus(as.getAction()));
    } else {
      as = new AwardStatistics(null, userId, ownerPsnId, pubId, DynamicConstants.RES_TYPE_PUB, LikeStatusEnum.LIKE,
          date, date.getTime(), null);
    }
    awardStatisticsDao.save(as);
    return as;
  }

  /**
   * 根据赞记录，反转赞状态： 已赞 ==> 取消赞， 未赞 ==> 赞
   * 
   * @param action
   * @return
   */
  private LikeStatusEnum setLikeStatus(LikeStatusEnum action) {
    if (action == null || action == LikeStatusEnum.UNLIKE) {
      return LikeStatusEnum.LIKE;
    } else {
      return LikeStatusEnum.UNLIKE;
    }
  }

  private void newSendAwardEmail(Long awarderPsnId, Long ownerPsnId, DynamicForm form) {
    HashMap<String, Object> emailMap = new HashMap<String, Object>();
    PubSimple pub = pubSimpleDao.get(form.getResId());
    if (pub != null) {
      try {
        Person awarderPsn = personDao.get(awarderPsnId);
        Person ownerPsn = personDao.get(ownerPsnId);
        String languageVersion = ownerPsn.getEmailLanguageVersion();
        if (StringUtils.isBlank(languageVersion)) {
          languageVersion = LocaleContextHolder.getLocale().toString();
        }
        String mailTemplate = "";
        String pubTitle = "";
        if ("en_US".equals(languageVersion)) {
          mailTemplate = "liked_your_pub_en_US.ftl";
          pubTitle = StringUtils.isBlank(pub.getEnTitle()) ? pub.getZhTitle() : pub.getEnTitle();
          emailMap.put(EmailConstants.EMAIL_SUBJECT_KEY,
              awarderPsn.getPsnName() + "liked your publications : " + pubTitle);// 主题
        } else {
          mailTemplate = "liked_your_pub_zh_CN.ftl";
          pubTitle = StringUtils.isBlank(pub.getZhTitle()) ? pub.getEnTitle() : pub.getZhTitle();
          emailMap.put(EmailConstants.EMAIL_SUBJECT_KEY, awarderPsn.getPsnName() + "赞了你的论文：" + pubTitle);// 主题
        }
        String psnShortUrl = "";
        String pubShortUrl = "";
        PsnProfileUrl profileUrl = psnProfileUrlDao.get(SecurityUtils.getCurrentUserId());
        if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
          psnShortUrl = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + profileUrl.getPsnIndexUrl();
        }
        PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(form.getResId());
        if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
          pubShortUrl = domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
        }
        emailMap.put("domainUrl", domainscm);
        emailMap.put("likedPsnUrl", psnShortUrl);
        emailMap.put("pubDetailUrl", pubShortUrl);
        emailMap.put("fullTextUrl", null);
        emailMap.put("impactsUrl", domainscm + "/scmwebsns/spread/pubConsequence");
        emailMap.put("pubTitle", pubTitle);
        emailMap.put("pubAuthors", pub.getAuthorNames());
        emailMap.put("pubBrief", pub.getBriefDesc());
        emailMap.put("likedPsnName", awarderPsn.getPsnName());
        emailMap.put("psnName", ownerPsn.getPsnName());

        emailMap.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, ownerPsn.getEmail());// 收件人邮箱
        emailMap.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, ownerPsn.getPersonId());// 收件人邮箱
        emailMap.put(EmailConstants.EMAIL_SENDER_PSNID_KEY, awarderPsnId);// 发件人id
        emailMap.put(EmailConstants.EMAIL_TEMPLATE_KEY, mailTemplate);// 邮件模板名称
        mailInitDataService.saveMailInitData(emailMap);
      } catch (Exception e) {
        logger.error("发送成果动态赞邮件出错", e);
      }
    }
  }

  private void sendAwardEmail(Long awarderPsnId, Long ownerPsnId, Long resId) {
    try {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("psnId", ownerPsnId);
      map.put("likedPsnId", awarderPsnId);
      map.put("pubId", resId);
      likedYourPubEmailService.syncEmailInfo(map);
    } catch (Exception e) {
      logger.error("发送赞邮件错误：resId:" + resId + ",awarderPsnId:" + awarderPsnId, e);
    }
  }

  // 同步到 DYN_STATISTICS ，新增的
  private void dynStatisticsSyncToSns(DynamicForm form, LikeStatusEnum action) throws DynException {
    DynStatistics dynStatistics = dynStatisticsDao.get(form.getParentDynId());
    if (dynStatistics == null) {
      dynStatistics = new DynStatistics();
      dynStatistics.setAwardCount(0);
      dynStatistics.setCommentCount(0);
      dynStatistics.setShareCount(0);
    }
    Integer awardCount = dynStatistics.getAwardCount();
    if (action == LikeStatusEnum.LIKE) {
      awardCount = awardCount.intValue() + 1;
    } else if (action == LikeStatusEnum.UNLIKE) {
      awardCount = awardCount.intValue() > 0 ? awardCount.intValue() - 1 : 0;
    }
    dynStatistics.setAwardCount(awardCount);
    form.setAwardTimes(awardCount.longValue());
    dynStatisticsDao.save(dynStatistics);
  }

  /**
   * 添加赞记录.
   * 
   * @param resType
   * @param resNode
   * @param resId
   * @throws DynException
   */
  private DynamicAwardRes insertAwardRecord(Person person, int resType, int resNode, Long resId, LikeStatusEnum action)
      throws DynException {

    // 更新资源赞次数.
    DynamicAwardRes dynamicAwardRes = this.dynamicAwardResDao.getDynamicAwardRes(resId, resType, resNode);
    if (dynamicAwardRes == null) {
      dynamicAwardRes = new DynamicAwardRes();
      dynamicAwardRes.setResId(resId);
      dynamicAwardRes.setResType(resType);
      dynamicAwardRes.setResNode(resNode);
      dynamicAwardRes.setAwardTimes(1l);
    } else {
      Long awardTimes = dynamicAwardRes.getAwardTimes();
      if (action == LikeStatusEnum.LIKE) {
        awardTimes += 1;
      } else if (action == LikeStatusEnum.UNLIKE) {
        awardTimes = awardTimes > 0 ? awardTimes - 1 : 0;
      }
      dynamicAwardRes.setAwardTimes(awardTimes);
    }
    dynamicAwardRes.setUpdateDate(new Date());// 无论是创建一个记录和修改记录，都要将操作记录修改为最新的时间
    this.dynamicAwardResDao.save(dynamicAwardRes);
    // 添加赞记录.
    DynamicAwardPsn dynamicAwardPsn =
        dynamicAwardPsnDao.getDynamicAwardPsn(person.getPersonId(), dynamicAwardRes.getAwardId());
    if (dynamicAwardPsn == null) {
      dynamicAwardPsn = new DynamicAwardPsn();
    }
    dynamicAwardPsn.setAwardId(dynamicAwardRes.getAwardId());
    dynamicAwardPsn.setAwarderPsnId(person.getPersonId());
    dynamicAwardPsn.setAwarderName(personQueryservice.getPsnName(person, "zh_CN"));
    dynamicAwardPsn.setAwarderEnName(personQueryservice.getPsnName(person, "en_US"));
    dynamicAwardPsn.setAwarderAvatar(person.getAvatars());
    dynamicAwardPsn.setAwardDate(new Date());
    dynamicAwardPsn.setStatus(action);
    this.dynamicAwardPsnDao.save(dynamicAwardPsn);
    return dynamicAwardRes;
  }

  @Override
  public List<DynamicAwardPsn> getAwardPsnList(Long dynId, Integer maxSize) throws DynException {
    return dynamicAwardPsnDao.getDynamicAwardPsns(dynId, maxSize);
  }

  @Override
  public Map<String, Boolean> getAwardPsnHasAward(DynamicForm form) throws DynException {
    Map<String, Boolean> hasAwardMap = new HashMap<String, Boolean>();
    String resIdResTypes = form.getDynResIdResTypes();
    if (StringUtils.isNotBlank(resIdResTypes)) {
      String[] resIdResTypeList = resIdResTypes.split(",");
      if (resIdResTypeList != null && resIdResTypeList.length > 0) {
        // 定义必要参数
        Long dynId;
        Long psnId = SecurityUtils.getCurrentUserId();
        for (int i = 0; i < resIdResTypeList.length; i++) {
          // 获取必要参数
          dynId = Long.parseLong(Des3Utils.decodeFromDes3(resIdResTypeList[i]));
          DynamicMsg d = dynamicMsgDao.get(dynId);
          if (dynId == null || d == null || d.getDelstatus() == 99) {
            continue;
          }
          boolean hasAward =
              this.resHasAwrd(psnId, dynId, d.getDynType(), d.getSameFlag(), d.getResId(), d.getResType());
          hasAwardMap.put(dynId.toString(), hasAward);
        }
      }
    }
    return hasAwardMap;
  }

  /**
   * 根据动态Id获取该动态/资源是否被赞
   * 
   * @param psnId
   * @param dynId
   * @return
   */
  @Override
  public boolean getPsnHasAward(Long psnId, Long dynId) throws DynException {
    DynamicMsg d = dynamicMsgDao.get(dynId);
    if (dynId == null || d == null || d.getDelstatus() == 99) {
      return false;
    }
    boolean hasAward = this.resHasAwrd(psnId, dynId, d.getDynType(), d.getSameFlag(), d.getResId(), d.getResType());

    return hasAward;

  }

  /**
   * 根据类型判断资源是否被赞
   * 
   * @param psnId
   * @param dynId
   * @param dynType
   * @param sameFlag
   * @param resId
   * @param resType
   * @return
   */

  public boolean resHasAwrd(Long psnId, Long dynId, String dynType, Long sameFlag, Long resId, Integer resType) {

    boolean hasAward = false;
    if ("B2TEMP".equals(dynType) || "B3TEMP".equals(dynType)) {// 只有纯成果动态才读取资源的赞信息
      switch (resType) {
        case DynamicConstants.RES_TYPE_JOURNALAWARD:// 期刊
        case DynamicConstants.RES_TYPE_PUB_PDWH:// 基准库成果
          hasAward = buildIsHasAward(psnId, resId, hasAward);
          break;
        case DynamicConstants.RES_TYPE_FUND:// 基金
          // 是否赞过
          FundAward awardRecord = fundAwardDao.findFundAwardRecord(SecurityUtils.getCurrentUserId(), resId);
          if (awardRecord != null && awardRecord.getStatus() == LikeStatusEnum.LIKE) {
            hasAward = true;
          }
          break;
        case DynamicConstants.RES_TYPE_AGENCY:// 资助机构
          // 是否赞过
          FundAgencyAward awardAgency = fundAgencyAwardDao.findAgencyAwardByPsnIdAndAgencyId(psnId, resId);
          if (awardAgency != null
              && CommonUtils.compareIntegerValue(awardAgency.getStatus(), LikeStatusEnum.LIKE.getValue())) {
            hasAward = true;
          }
          break;
        case DynamicConstants.RES_TYPE_PUB:// 成果
        case DynamicConstants.RES_TYPE_REF:// 文献
          PubLikePO pubLike = pubLikeDAO.findByPubIdAndPsnId(resId, psnId);
          if (pubLike != null && pubLike.getStatus() == 1) {
            hasAward = true;
          }
          break;
        case DynamicConstants.RES_TYPE_PRJ:// 项目
          hasAward = awardStatisticsDao.hasAward(psnId, resId, resType);
          break;
        case DynamicConstants.RES_TYPE_NEWS:// 新闻
          NewsLike newsLike = newsLikeDao.findByNewsIdAndPsnId(resId, psnId);
          if (newsLike != null && newsLike.getStatus() == 1) {
            hasAward = true;
          }
          break;
        default:
          hasAward = awardStatisticsDao.hasAward(psnId, sameFlag, DynamicConstants.RES_TYPE_NORMAL);
          break;
      }
    } else {// 其他动态读取动态的赞信息
      hasAward = awardStatisticsDao.hasAward(psnId, sameFlag, DynamicConstants.RES_TYPE_NORMAL);
    }

    return hasAward;

  }

  public boolean buildIsHasAward(Long psnId, Long resId, boolean hasAward) {
    PdwhPubLikePO pdwhPubAward = pdwhPubLikeDAO.findByPubIdAndPsnId(resId, psnId);
    if (pdwhPubAward != null && pdwhPubAward.getStatus() == 1) {
      hasAward = true;
    }
    return hasAward;
  }

}
