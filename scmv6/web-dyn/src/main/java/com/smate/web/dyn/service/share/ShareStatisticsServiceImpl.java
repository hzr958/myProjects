package com.smate.web.dyn.service.share;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.email.service.EmailSendService;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.project.model.ProjectStatistics;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.dao.dynamic.DynStatisticsDao;
import com.smate.web.dyn.dao.fund.FundStatisticsDao;
import com.smate.web.dyn.dao.news.NewsShareDao;
import com.smate.web.dyn.dao.news.NewsStatisticsDao;
import com.smate.web.dyn.dao.pdwh.PdwhPubShareDao;
import com.smate.web.dyn.dao.pdwh.PdwhPubStatisticsDao;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubShareDAO;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubStatisticsDAO;
import com.smate.web.dyn.dao.pub.ProjectStatisticsDao;
import com.smate.web.dyn.dao.pub.PubShareDAO;
import com.smate.web.dyn.dao.pub.PubStatisticsDAO;
import com.smate.web.dyn.dao.share.ShareStatisticsDao;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.dynamic.DynStatistics;
import com.smate.web.dyn.model.fund.FundStatistics;
import com.smate.web.dyn.model.news.NewsShare;
import com.smate.web.dyn.model.news.NewsStatistics;
import com.smate.web.dyn.model.pdwhpub.PdwhPubShare;
import com.smate.web.dyn.model.pdwhpub.PdwhPubSharePO;
import com.smate.web.dyn.model.pdwhpub.PdwhPubStatistics;
import com.smate.web.dyn.model.pdwhpub.PdwhPubStatisticsPO;
import com.smate.web.dyn.model.pub.PubSharePO;
import com.smate.web.dyn.model.pub.PubStatisticsPO;
import com.smate.web.dyn.model.share.ShareStatistics;
import com.smate.web.dyn.service.dynamic.DynamicShareService;
import com.smate.web.dyn.service.statistic.StatisticsService;

/**
 * 分享统计
 * 
 * @author zk
 * 
 */
@Service("shareStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class ShareStatisticsServiceImpl implements ShareStatisticsService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ShareStatisticsDao shareStatisticsDao;
  @Autowired
  private StatisticsService statisticsService;
  @Autowired
  private EmailSendService sharedYourPubEmailService;
  @Autowired
  private PdwhPubShareDao pdwhPubShareDao;
  @Autowired
  private PdwhPubStatisticsDao pdwhPubStatisticsDao;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PubStatisticsDAO pubStatisticsDAO;
  @Autowired
  private PubShareDAO pubShareDAO;
  @Autowired
  private PdwhPubStatisticsDAO pdwhStatisticsDAO;
  @Autowired
  private PdwhPubShareDAO pdwhPubShareDAO;
  @Autowired
  private DynStatisticsDao dynStatisticsDao;
  @Autowired
  private ProjectStatisticsDao projectStatisticsDao;
  @Autowired
  private DynamicShareService dynamicShareService;
  @Autowired
  private FundStatisticsDao fundStatisticsDao;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private NewsShareDao newsShareDao;
  @Autowired
  private NewsStatisticsDao newsStatisticsDao;

  @Override
  public void addBatchShareRecord(Long psnId, Integer resType, Long resId) throws DynException {
    try {
      if (psnId != null && resType != null && resId != null) {
        Long resOwner = this.statisticsService.findPsnId(resId, 1, resType);
        if (resOwner != null) {
          this.addShareRecord(psnId, resOwner, resId, resType);
        }
      }
    } catch (Exception e) {
      logger.error("批量保存分享记录出错！PsnId=" + psnId + " resType=" + resType + " resId" + resId, e);
      throw new DynException(e);
    }
  }

  private void sendShareEmail(Long sharerPsnId, Long resOwnerPsnId, int resType, Long resId) {
    if (resType == 1 || resType == 2) {
      try {
        if (!sharerPsnId.equals(resOwnerPsnId)) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("sharedPsnId", sharerPsnId);
          map.put("psnId", resOwnerPsnId);
          map.put("pubId", resId);
          // /////////////////SCM-14335////////////////////////
          PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(resId);
          String url = "";
          if (pubIndexUrl != null) {
            url = domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
          } else {
            url = domainscm + "/pub/outside/details?des3PubId=" + Des3Utils.encodeToDes3(resId + "");
          }
          map.put("pubDetailUrl", url);
          // //////////////////SCM-14335///////////////////////
          // sharedYourPubEmailService.syncEmailInfo(map);
        }
      } catch (Exception e) {
        logger.error("发送分享邮件错误：resType:{},resId:{},sharerPsnId:{}", resType, resId, sharerPsnId, e);
      }
    }
  }

  @Override
  public void addShareRecord(Long psnId, Long resOwner, Long resId, Integer resType) throws DynException {

    try {
      if (!DynamicConstants.SHARE_TYPE_MAP.containsKey(resType)) {
        logger.warn("分享统计，分享类型actionType=" + resType + "的记录，不需要保存");
        return;
      }
      if (DynamicConstants.RES_TYPE_PUB == resType || DynamicConstants.RES_TYPE_REF == resType) {
        this.sendShareEmail(psnId, resOwner, resType, resId);
        return;// 分享的资源为成果时不需要将记录插入到SHARE_STATISTICS表中而是V_PUB_SHARE表中
      }
      ShareStatistics shareStatistics = new ShareStatistics();
      shareStatistics.setPsnId(psnId);
      shareStatistics.setSharePsnId(resOwner);
      shareStatistics.setActionKey(resId);
      shareStatistics.setActionType(resType);
      Date nowDate = new Date();
      shareStatistics.setCreateDate(nowDate);
      shareStatistics.setFormateDate(DateUtils.getDateTime(nowDate));
      shareStatistics.setIp(Struts2Utils.getRemoteAddr());
      shareStatisticsDao.save(shareStatistics);

      // 更新项目被分享总次数
      // if (DynamicConstants.RES_TYPE_PRJ == actionType) {
      // this.prjActionStatisticsService.updateData(actionKey,
      // ProjectStatistics.SHARE_TYPE, null);
      // }
    } catch (Exception e) {
      logger.error(
          "保存分享记录出错！PsnId=" + psnId + " sharePsnId=" + resOwner + " actionKey=" + resId + " actionType" + resType, e);
      throw new DynException(e);
    }
  }

  /**
   * 基准库批量添加分享记录
   */
  @Override
  public void addPdwhBatchShareRecord(Long psnId, Integer resType, Long resId) throws DynException {
    PdwhPubShare pdwhPubShare = new PdwhPubShare();
    pdwhPubShare.setSharePsnId(psnId);
    Date date = new Date();
    pdwhPubShare.setShareDate(date);
    pdwhPubShare.setResId(resId);
    pdwhPubShare.setResType(resType);
    pdwhPubShareDao.save(pdwhPubShare);
    addPdwhShareRecord(psnId, resId, resType);

  }

  /**
   * 添加分享记录
   */
  @Override
  public void addPdwhShareRecord(Long psnId, Long resId, Integer resType) throws DynException {
    PdwhPubStatistics pubStatistics = pdwhPubStatisticsDao.get(resId);
    if (pubStatistics == null) {
      pubStatistics = new PdwhPubStatistics(resId, 0, 0, 0, 0, 0, 0);
    }
    if (pubStatistics.getShareCount() == null) {
      pubStatistics.setShareCount(1);
    } else {
      pubStatistics.setShareCount(pubStatistics.getShareCount() + 1);
    }
    pdwhPubStatisticsDao.save(pubStatistics);
  }

  @Override
  public void addNewShareRecord(DynamicForm form) throws DynException {
    if (form.getResId() != null && form.getResType() != null) {
      if (form.getResType() == 1) {
        dynamicShareService.addPubShareStatistics(form, form.getResId());
        dynamicShareService.sysSnsPdwhShareStatistics(form);// 数据同步 SCM-23563
      } else if (form.getResType() == 22 || form.getResType() == 24) {
        dynamicShareService.addPdwhPubShareStatistics(form.getResId(), form);
        dynamicShareService.sysPdwhSnsShare(form);// 个人库关联成果数据同步
      }

      // 增加这个判断用于修复在分享项目至动态时分享数量不变的问题，在这里将数据插入到对应的表中
      else if (form.getResType() == 4) {
        addPrjShareStatistics(form);
        Long resOwnerID = statisticsService.findPsnId(form.getResId(), 1, form.getResType());
        if (resOwnerID != null) {
          addShareRecord(form.getPsnId(), resOwnerID, form.getResId(), form.getResType());// ResPsnId
        }
      } else if (form.getResType() == 26) {
        form.setPlatform("1");
        addNewsShareStatistics(form);
      }
    }
  }

  public void addNewsShareStatistics(DynamicForm form) {
    NewsShare newsShare = new NewsShare();
    newsShare.setNewsId(form.getResId());
    newsShare.setSharePsnId(form.getPsnId());
    newsShare.setContent(form.getDynText());
    newsShare.setPlatform(Integer.parseInt(form.getPlatform()));
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

  /**
   * syl测试添加（完成项目分享至动态后更新分享数量）
   * 
   * @param form
   */
  private void addPrjShareStatistics(DynamicForm form) {
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

  @Override
  public void updateDynStatistics(Long dynId) {
    if (dynId != null) {
      DynStatistics dynStatistics = dynStatisticsDao.getDynamicStatistics(dynId);
      if (dynStatistics != null) {
        Integer count = dynStatistics.getShareCount() == null ? 0 : dynStatistics.getShareCount();
        dynStatisticsDao.updateDynamicShareStatistics(dynId, count + 1);
      }
    }
  }

  @Override
  public void updateFundShareNum(Long fundId) throws ServiceException {
    FundStatistics fs = fundStatisticsDao.get(fundId);
    if (fs == null) {
      fs = new FundStatistics();
      fs.setFundId(fundId);
      fs.setShareCount(0);
    }
    if (fs.getShareCount() == null) {
      fs.setShareCount(0);
    }
    fs.setShareCount(fs.getShareCount() + 1);
    fundStatisticsDao.save(fs);

  }

  @Override
  public void updatePrjShareNum(Long prjId) throws ServiceException {
    ProjectStatistics ps = projectStatisticsDao.get(prjId);
    if (ps == null) {
      ps = new ProjectStatistics();
      ps.setProjectId(prjId);
      ps.setShareCount(0);
    }
    if (ps.getShareCount() == null) {
      ps.setShareCount(0);
    }
    ps.setShareCount(ps.getShareCount() + 1);
    projectStatisticsDao.save(ps);
  }

  @Override
  public void updateSnsShareNum(DynamicForm form, Long snsId) throws ServiceException {
    PubSharePO pubShare = new PubSharePO();
    pubShare.setPubId(snsId);
    pubShare.setPsnId(form.getPsnId());
    pubShare.setComment(form.getDynText());
    pubShare.setPlatform(Integer.parseInt(form.getPlatform()));// 成果分享平台
    pubShare.setStatus(0);
    pubShare.setGmtCreate(new Date());
    pubShare.setGmtModified(new Date());
    pubShareDAO.save(pubShare);
    PubStatisticsPO ps = pubStatisticsDAO.get(snsId);
    if (ps == null) {
      ps = new PubStatisticsPO(snsId);
    }
    ps.setShareCount(ps.getShareCount() + 1);
    pubStatisticsDAO.save(ps);
    // 个人库数据与基准库同步
    synchronizeSnsToPdwh(form);
  }

  /**
   * 个人库数据与基准库同步
   * 
   * @param form
   */
  public void synchronizeSnsToPdwh(DynamicForm form) {
    Long pdwhPubId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(form.getResId());
    if (pdwhPubId != null && pdwhPubId > 0) {
      updatePdwhShareNum(form, pdwhPubId);// 基准库数据同步
      List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsOpenPubIds(pdwhPubId, 0L);
      if (CollectionUtils.isNotEmpty(snsPubIds)) {
        for (Long snsPubId : snsPubIds) {
          if (!snsPubId.equals(form.getResId())) {
            updateSnsShareNum(form, snsPubId);// 个人库数据同步
          }
        }
      }
    }
  }

  @Override
  public void updatePdwhShareNum(DynamicForm form, Long pdwhId) throws ServiceException {
    PdwhPubSharePO pdwhPubShare = new PdwhPubSharePO();
    pdwhPubShare.setPdwhPubId(pdwhId);
    pdwhPubShare.setPsnId(pdwhId);
    pdwhPubShare.setComment(form.getDynText());
    pdwhPubShare.setPlatform(Integer.parseInt(form.getPlatform()));
    pdwhPubShare.setStatus(0);// 状态 0=正常 ; 9=删除
    pdwhPubShare.setGmtCreate(new Date());
    pdwhPubShare.setGmtModified(new Date());
    pdwhPubShareDAO.save(pdwhPubShare);

    PdwhPubStatisticsPO pdwhPubStatisticsPO = pdwhStatisticsDAO.get(pdwhId);
    if (pdwhPubStatisticsPO == null) {
      pdwhPubStatisticsPO = new PdwhPubStatisticsPO();
      pdwhPubStatisticsPO.setPdwhPubId(pdwhId);
      pdwhPubStatisticsPO.setShareCount(1);
    } else {
      int shareCount = pdwhPubStatisticsPO.getShareCount() == null ? 0 : pdwhPubStatisticsPO.getShareCount();
      pdwhPubStatisticsPO.setShareCount(shareCount + 1);
    }
    pdwhStatisticsDAO.save(pdwhPubStatisticsPO);
    // 基准库数据与个人库同步
    synchronizePdwhToSns(form);
  }

  /**
   * 基准库数据与个人库同步
   * 
   * @param form
   */
  public void synchronizePdwhToSns(DynamicForm form) {
    List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsOpenPubIds(form.getResId(), 0L);
    if (CollectionUtils.isNotEmpty(snsPubIds)) {
      for (Long snsPubId : snsPubIds) {
        updateSnsShareNum(form, snsPubId);// 个人库数据同步
      }
    }
  }

}
