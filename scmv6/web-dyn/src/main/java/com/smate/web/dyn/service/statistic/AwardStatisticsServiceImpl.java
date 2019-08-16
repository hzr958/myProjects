package com.smate.web.dyn.service.statistic;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.pub.consts.PubStatisticsConstant;
import com.smate.core.base.statistics.PsnStatisticsUtils;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.dao.fund.FundAwardDao;
import com.smate.web.dyn.dao.fund.FundStatisticsDao;
import com.smate.web.dyn.dao.psn.AwardStatisticsDao;
import com.smate.web.dyn.dao.psn.rol.PsnStatisticsRolDao;
import com.smate.web.dyn.dao.pub.rol.SnsSyncPubStatisticsDao;
import com.smate.web.dyn.dao.rcmd.PsnStatisticsRcmdDao;
import com.smate.web.dyn.model.fund.FundAward;
import com.smate.web.dyn.model.fund.FundStatistics;
import com.smate.web.dyn.model.psn.AwardStatistics;
import com.smate.web.dyn.model.psn.rcmd.PsnStatisticsRcmd;
import com.smate.web.dyn.model.psn.rol.PsnStatisticsRol;
import com.smate.web.dyn.model.pub.PubConfirmRecord;
import com.smate.web.dyn.model.pub.rol.SnsSyncPubStatistics;
import com.smate.web.dyn.service.pub.PubActionStatisticsService;
import com.smate.web.dyn.service.pub.PubConfirmRecordService;
import com.smate.web.dyn.service.pub.rol.PubStatisticsService;

/**
 * 赞统计
 * 
 * @author Administrator
 * 
 */

@Service("awardStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class AwardStatisticsServiceImpl implements AwardStatisticsService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private StatisticsService statisticsService;
  @Autowired
  private PubActionStatisticsService pubActionStatisticsService;
  @Autowired
  private PubStatisticsService pubStatisticsService;
  @Autowired
  private PubConfirmRecordService pubConfirmRecordService;
  @Autowired
  private SnsSyncPubStatisticsDao snsSyncPubStatisticsDao;
  @Autowired
  private AwardStatisticsDao awardStatisticsDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private PsnStatisticsRcmdDao psnStatisticsRcmdDao;
  @Autowired
  private PsnStatisticsRolDao psnStatisticsRolDao;
  @Autowired
  private FundAwardDao fundAwardDao;
  @Autowired
  private FundStatisticsDao fundStatisticsDao;

  /**
   * 
   * actionKey： 资源Id 成果ID actionType： resType action: 1 ,点赞
   */
  @Deprecated
  @Override
  public void addAwardRecord(Long psnId, Integer resNodeId, Long actionKey, Integer actionType, LikeStatusEnum action,
      Long awardPsnId) throws DynException {
    // 添加时否点赞
    updateAwardStatistics(psnId, actionKey, actionType, action, awardPsnId);
    boolean isCancelAward = action == LikeStatusEnum.LIKE ? false : true; // 不会取消赞
    // 更新成果被赞总次数
    if (DynamicConstants.RES_TYPE_PUB == actionType) {
      // 同步统计到， 简化 SNS_SYNC_PUB_STATISTICS rol中 TODO actionKey == snsPubid
      PubStatisticsSyncToRol(actionKey, action);
      // 更新个人成果被赞统计 , 同步三个数据库。
      psnStatisticSyncToSns(awardPsnId, action);
      // this.pubActionStatisticsService.updateData(actionKey,
      // DynTemplateConstant.AWARD_TYPE, isCancelAward);
    } else if (DynamicConstants.RES_TYPE_FUND == actionType) {
      this.updateFundAward(action, actionKey);
    }

  }

  /*
   * 更新 赞操作统计
   */

  // String ip = Struts2Utils.getRemoteAddr();

  // AwardStatistics awardStatistics =
  // awardStatisticsDao.findAwardStatistics(psnId, awardPsnId, actionKey,
  // actionType);

  private void updateAwardStatistics(Long psnId, Long actionKey, Integer actionType, LikeStatusEnum action,
      Long awardPsnId) {
    String ip = Struts2Utils.getRemoteAddr();
    AwardStatistics awardStatistics = awardStatisticsDao.findAwardStatistics(psnId, awardPsnId, actionKey, actionType);
    Date nowDate = new Date();
    if (awardStatistics == null) {
      awardStatistics = new AwardStatistics();
      awardStatistics.setPsnId(psnId);
      awardStatistics.setAwardPsnId(awardPsnId);
      awardStatistics.setActionKey(actionKey);
      awardStatistics.setActionType(actionType);
    }
    awardStatistics.setAction(action);
    awardStatistics.setCreateDate(nowDate);
    long formateDate = nowDate.getTime();
    awardStatistics.setFormateDate(formateDate);
    awardStatistics.setIp(ip);
    awardStatisticsDao.save(awardStatistics);
  }

  // 同步统计 SNS_SYNC_PUB_STATISTICS 至rol
  @Deprecated // PUB_CONFIRM_RECORD表已弃用
  private void PubStatisticsSyncToRol(Long snsPubId, LikeStatusEnum likeStatusEnum) throws DynException {
    PubConfirmRecord pubConfirmRecord = pubConfirmRecordService.getRecordBySnsPubId(snsPubId);
    if (pubConfirmRecord != null) {
      SnsSyncPubStatistics pubStis =
          this.snsSyncPubStatisticsDao.findPubStatistics(pubConfirmRecord.getRolPubId(), snsPubId);
      if (pubStis == null) {
        pubStis = new SnsSyncPubStatistics(pubConfirmRecord.getRolPubId(), snsPubId);
        pubStis.setAwardNum(0L);
      }
      int statisticType = PubStatisticsConstant.STATISTIC_TYPE_AWARD;
      if (likeStatusEnum == LikeStatusEnum.LIKE) {
        pubStis.setAwardNum(pubStis.getAwardNum() == null ? 1 : (pubStis.getAwardNum() + 1));
      } else {
        pubStis.setAwardNum(
            (pubStis.getAwardNum() == null || pubStis.getAwardNum() <= 1) ? 0 : (pubStis.getAwardNum() - 1));
      }
      this.snsSyncPubStatisticsDao.save(pubStis);
      // 保存或更新成果相关统计
      this.pubStatisticsService.saveOrUpdatePubStatisticNew(pubStis.getSiePubId(), statisticType,
          pubStis.getAwardNum());
    }

  }

  // PSN_STATISTICS 同步到 sns
  private void psnStatisticSyncToSns(Long psnId, LikeStatusEnum likeStatusEnum) {
    PsnStatistics psnStatistics = psnStatisticsDao.get(psnId);
    if (psnStatistics == null) {
      psnStatistics = new PsnStatistics();
      psnStatistics.setPsnId(psnId);
      psnStatistics.setPubAwardSum(0);
    }
    if (likeStatusEnum == LikeStatusEnum.LIKE) {
      psnStatistics.setPubAwardSum(psnStatistics.getPubAwardSum() == null ? 1 : (psnStatistics.getPubAwardSum() + 1));
    } else {
      psnStatistics.setPubAwardSum((psnStatistics.getPubAwardSum() == null || psnStatistics.getPubAwardSum() <= 1) ? 0
          : (psnStatistics.getPubAwardSum() - 1));
    }
    // 属性为null的保存为0
    PsnStatisticsUtils.buildZero(psnStatistics);
    psnStatisticsDao.save(psnStatistics);
    // PSN_STATISTICS 同步到 rol
    psnStatisticSyncToRol(psnId);
    // PSN_STATISTICS 同步到 rcmd
    psnStatisticSyncToRcmd(psnId);
  }

  // PSN_STATISTICS 同步到 rol
  private void psnStatisticSyncToRol(Long psnId) {
    PsnStatisticsRol psnStatisticsRol = psnStatisticsRolDao.get(psnId);
    if (psnStatisticsRol == null) {
      psnStatisticsRol = new PsnStatisticsRol();
      psnStatisticsRol.setPsnId(psnId);
    }
    psnStatisticsRol.setPubAwardSum(psnStatisticsRol.getPubAwardSum() + 1);
    psnStatisticsRolDao.save(psnStatisticsRol);
  }

  // PSN_STATISTICS 同步到 rcmd
  private void psnStatisticSyncToRcmd(Long psnId) {
    PsnStatisticsRcmd psnStatisticsRcmd = psnStatisticsRcmdDao.get(psnId);
    if (psnStatisticsRcmd == null) {
      psnStatisticsRcmd = new PsnStatisticsRcmd();
      psnStatisticsRcmd.setPsnId(psnId);
    }
    psnStatisticsRcmd.setPubAwardSum(psnStatisticsRcmd.getPubAwardSum() + 1);
    psnStatisticsRcmdDao.save(psnStatisticsRcmd);

  }

  /**
   * 更新基金赞
   * 
   * @param action
   * @param fundId
   */
  private void updateFundAward(LikeStatusEnum action, Long fundId) {
    Long psnId = SecurityUtils.getCurrentUserId();
    // 查询赞操作记录
    FundAward award = fundAwardDao.findFundAwardRecord(psnId, fundId);
    FundStatistics sta = fundStatisticsDao.findFundStatisticsById(fundId);
    boolean sameOperate = false;
    if (action == LikeStatusEnum.UNLIKE) {
      // 取消赞操作
      if (award == null) {
        award = new FundAward();
        award.setFundId(fundId);
        award.setAwardPsnId(psnId);
      } else if (award.getStatus() == LikeStatusEnum.UNLIKE) {
        // 是否已取消赞了
        sameOperate = true;
      }
    } else {
      // 赞操作
      if (award == null) {
        award = new FundAward();
        award.setFundId(fundId);
        award.setAwardPsnId(psnId);
      } else if (award.getStatus() == LikeStatusEnum.LIKE) {
        // 是否已经赞过该基金
        sameOperate = true;
      }
    }
    // 更新赞操作信息
    award.setAwardDate(new Date());
    award.setStatus(action);
    fundAwardDao.save(award);
    if (sta == null) {
      sta = new FundStatistics();
      sta.setFundId(fundId);
      sta.setAwardCount(0);
      sta.setShareCount(0);
    }
    // 非重复操作时更新基金赞统计数
    if (!sameOperate) {
      if (action == LikeStatusEnum.UNLIKE) {
        sta.setAwardCount(sta.getAwardCount() - 1 >= 0 ? sta.getAwardCount() - 1 : 0);
      } else {
        sta.setAwardCount(sta.getAwardCount() + 1);
      }
      fundStatisticsDao.save(sta);
    }
  }

  @Override
  public void AwardPubSync(Long actionKey, Long awardPsnId, LikeStatusEnum likeStatusEnum) throws DynException {
    // 同步统计到rol PUB_CONFIRM_RECORD表已弃用
    // PubStatisticsSyncToRol(actionKey, likeStatusEnum);
    // 更新个人成果被赞统计 , 同步三个数据库。
    psnStatisticSyncToSns(awardPsnId, likeStatusEnum);
  }

}
