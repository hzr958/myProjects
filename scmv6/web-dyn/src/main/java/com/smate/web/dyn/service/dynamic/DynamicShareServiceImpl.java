package com.smate.web.dyn.service.dynamic;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.project.model.ProjectStatistics;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.dyn.dao.dynamic.DynamicSharePsnDao;
import com.smate.web.dyn.dao.dynamic.DynamicShareResDao;
import com.smate.web.dyn.dao.fund.AgencyStatisticsDao;
import com.smate.web.dyn.dao.fund.FundStatisticsDao;
import com.smate.web.dyn.dao.news.NewsShareDao;
import com.smate.web.dyn.dao.news.NewsStatisticsDao;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubShareDAO;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubStatisticsDAO;
import com.smate.web.dyn.dao.pub.ProjectStatisticsDao;
import com.smate.web.dyn.dao.pub.PubShareDAO;
import com.smate.web.dyn.dao.pub.PubStatisticsDAO;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.dynamic.DynamicSharePsn;
import com.smate.web.dyn.model.dynamic.DynamicShareRes;
import com.smate.web.dyn.model.fund.FundStatistics;
import com.smate.web.dyn.model.news.NewsShare;
import com.smate.web.dyn.model.news.NewsStatistics;
import com.smate.web.dyn.model.pdwhpub.PdwhPubSharePO;
import com.smate.web.dyn.model.pdwhpub.PdwhPubStatisticsPO;
import com.smate.web.dyn.model.pub.PubSharePO;
import com.smate.web.dyn.model.pub.PubStatisticsPO;
import com.smate.web.dyn.service.psn.PersonQueryservice;
import com.smate.web.dyn.service.pub.PubConfirmRecordService;
import com.smate.web.dyn.service.share.ShareStatisticsService;

/**
 * 动态分享服务类
 * 
 * @author zk
 *
 */
@Service("dynamicShareService")
@Transactional(rollbackOn = Exception.class)
public class DynamicShareServiceImpl implements DynamicShareService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DynamicShareResDao dynamicShareResDao;
  @Autowired
  private DynamicSharePsnDao dynamicSharePsnDao;
  @Autowired
  private PersonQueryservice personQueryservic;
  @Autowired
  private PubConfirmRecordService pubConfirmRecordService;
  @Autowired
  private ShareStatisticsService shareStatisticsService;
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
  private AgencyStatisticsDao agencyStatisticsDao;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private NewsShareDao newsShareDao;
  @Autowired
  private NewsStatisticsDao newsStatisticsDao;

  @Override
  public void shareDynamic(DynamicForm form) throws DynException {
    addResShare(form);// 更新统计数
    shareStatisticsService.addBatchShareRecord(form.getPsnId(), form.getResType(), form.getResId());
  }

  /**
   * 保存分享信息
   * 
   * @param form
   */
  private void addResShare(DynamicForm form) throws DynException {

    Long resId = form.getResId();
    Integer resType = form.getResType();
    if (!"B2TEMP".equals(form.getDynType()) && !"B3TEMP".equals(form.getDynType())) {// 不是纯资源动态，就查动态的
      resId = form.getDynId();
      resType = 0;// 0表示动态不是纯资源
    }
    Person person = personQueryservic.findPerson(form.getPsnId());
    if (person == null) {
      throw new DynException("分享动态时，不能获取到分享人员的person对象");
    }
    String shareTitle = "分享到他（她）的动态。";
    String shareEnTitle = "share to him (her) dynamic.";
    // 添加分享记录
    saveDynamicShareRecord(resId, resType, person, shareTitle, shareEnTitle);
    if (form.getResId() != null && form.getResType() != null && "B2TEMP".equals(form.getDynType())) {
      switch (form.getResType()) {
        case 4:
          addPrjShareStatistics(form);
          break;
        case 22:
        case 24:
          form.setPlatform("1");
          addPdwhPubShareStatistics(form.getResId(), form);
          sysPdwhSnsShare(form);// 个人库关联成果数据同步
          break;
        case 1:
        case 2:
          form.setPlatform("1");
          addPubShareStatistics(form, form.getResId());
          sysSnsPdwhShareStatistics(form);
          break;
        case 11:
          addFundShareStatistics(form);
          break;
        case 26:
          form.setPlatform("1");
          addNewsShareStatistics(form);
        default:
          break;
      }
    }

  }

  private void saveDynamicShareRecord(Long resId, Integer resType, Person person, String shareTitle,
      String shareEnTitle) {
    Date now = new Date();
    DynamicShareRes dynamicShareRes = this.dynamicShareResDao.getDynamicShareRes(resId, resType, 1);
    // 更新资源的分享次数
    if (dynamicShareRes == null) {
      dynamicShareRes = new DynamicShareRes();
      dynamicShareRes.setResId(resId);
      dynamicShareRes.setResType(resType);
      dynamicShareRes.setResNode(1);
      dynamicShareRes.setShareTimes(1l);
      dynamicShareRes.setUpdateDate(now);
    } else {
      dynamicShareRes.setShareTimes(dynamicShareRes.getShareTimes() + 1);
      dynamicShareRes.setUpdateDate(now);
    }
    dynamicShareResDao.save(dynamicShareRes);
    // 添加人员分享记录
    DynamicSharePsn dynamicSharePsn = new DynamicSharePsn();
    dynamicSharePsn.setShareId(dynamicShareRes.getShareId());
    dynamicSharePsn.setShareTitle(shareTitle);
    dynamicSharePsn.setShareEnTitle(shareEnTitle);
    dynamicSharePsn.setSharerPsnId(person.getPersonId());
    dynamicSharePsn.setSharerName(personQueryservic.getPsnName(person, "zh_CN"));
    dynamicSharePsn.setSharerEnName(personQueryservic.getPsnName(person, "en_US"));
    dynamicSharePsn.setSharerAvatar(person.getAvatars());
    dynamicSharePsn.setShareDate(now);
    dynamicSharePsnDao.save(dynamicSharePsn);
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

  private void addFundShareStatistics(DynamicForm form) {
    FundStatistics fs = fundStatisticsDao.get(form.getResId());
    if (fs == null) {
      fs = new FundStatistics();
      fs.setFundId(form.getResId());
      fs.setShareCount(0);
    }
    if (fs.getShareCount() == null) {
      fs.setShareCount(0);
    }
    fs.setShareCount(fs.getShareCount() + 1);
    fundStatisticsDao.save(fs);

  }

  public void addPdwhPubShareStatistics(Long resId, DynamicForm form) {
    PdwhPubSharePO pdwhPubShare = new PdwhPubSharePO();
    pdwhPubShare.setPdwhPubId(resId);
    pdwhPubShare.setPsnId(form.getPsnId());
    pdwhPubShare.setComment(form.getDynText());
    pdwhPubShare.setPlatform(Integer.parseInt(form.getPlatform()));
    pdwhPubShare.setStatus(0);// 状态 0=正常 ; 9=删除
    pdwhPubShare.setGmtCreate(new Date());
    pdwhPubShare.setGmtModified(new Date());
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

  public void addPubShareStatistics(DynamicForm form, Long snsPubId) {
    PubSharePO pubShare = new PubSharePO();
    pubShare.setPubId(snsPubId);
    pubShare.setPsnId(form.getPsnId());
    pubShare.setComment(form.getDynText());
    pubShare.setPlatform(Integer.parseInt(form.getPlatform()));// 成果分享平台
                                                               // SharePlatformEnum
    pubShare.setStatus(0);
    pubShare.setGmtCreate(new Date());
    pubShare.setGmtModified(new Date());
    pubShareDAO.save(pubShare);

    PubStatisticsPO ps = pubStatisticsDAO.get(snsPubId);
    if (ps == null) {
      ps = new PubStatisticsPO(snsPubId);
    }
    ps.setShareCount(ps.getShareCount() + 1);
    pubStatisticsDAO.save(ps);
  }

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
  public void ajaxAddResShareCounts(DynamicForm form) throws DynException {
    Long resId = form.getResId();
    Integer resType = form.getResType();
    Person person = personQueryservic.findPerson(form.getPsnId());
    if (person == null) {
      throw new DynException("分享动态时，不能获取到分享人员的person对象");
    }
    String shareTitle = "分享到站外";
    String shareEnTitle = "Share to outside";
    // 添加分享记录
    saveDynamicShareRecord(resId, resType, person, shareTitle, shareEnTitle);
    if (form.getResId() != null && form.getResType() != null) {
      switch (form.getResType()) {
        case 22:
        case 24:
          form.setPlatform("4");
          addPdwhPubShareStatistics(form.getResId(), form);
          sysPdwhSnsShare(form);// 个人库关联成果数据同步
          break;
        case 1:
        case 2:
          // SCM-23563
          form.setPlatform("4");
          addPubShareStatistics(form, form.getResId());
          sysSnsPdwhShareStatistics(form);
          break;
        default:
          break;
      }
    }
  }

  @Override
  public void sysSnsPdwhShareStatistics(DynamicForm form) throws DynException {
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

  @Override
  public void sysPdwhSnsShare(DynamicForm form) throws DynException {
    List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsOpenPubIds(form.getResId(), 0L);
    if (CollectionUtils.isNotEmpty(snsPubIds)) {
      for (Long snsPubId : snsPubIds) {
        addPubShareStatistics(form, snsPubId);// 个人库数据同步
      }
    }
  }
}
