package com.smate.sie.center.task.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.dao.consts.Sie6InstitutionDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.consts.Sie6Institution;
import com.smate.core.base.utils.model.security.Person;
import com.smate.sie.center.task.dao.SnsPsnScienceAreaDao;
import com.smate.sie.core.base.utils.dao.prj.SieProjectDao;
import com.smate.sie.core.base.utils.dao.pub.SiePatentDao;
import com.smate.sie.core.base.utils.dao.pub.SiePublicationDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactBaseAwardDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactBaseDownloadDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactBaseIndexDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactBaseShareDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactBaseViewDao;
import com.smate.sie.core.base.utils.dao.statistics.SieBhAwardDao;
import com.smate.sie.core.base.utils.dao.statistics.SieBhDownloadDao;
import com.smate.sie.core.base.utils.dao.statistics.SieBhIndexReadDao;
import com.smate.sie.core.base.utils.dao.statistics.SieBhReadDao;
import com.smate.sie.core.base.utils.dao.statistics.SieBhShareDao;
import com.smate.sie.core.base.utils.model.prj.SieProject;
import com.smate.sie.core.base.utils.model.pub.SiePatent;
import com.smate.sie.core.base.utils.model.pub.SiePublication;
import com.smate.sie.core.base.utils.model.statistics.ImpactConsts;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactBaseAward;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactBaseDownload;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactBaseIndex;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactBaseShare;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactBaseView;
import com.smate.sie.core.base.utils.model.statistics.SieBhAward;
import com.smate.sie.core.base.utils.model.statistics.SieBhDownload;
import com.smate.sie.core.base.utils.model.statistics.SieBhIndexRead;
import com.smate.sie.core.base.utils.model.statistics.SieBhRead;
import com.smate.sie.core.base.utils.model.statistics.SieBhShare;

/**
 * 社交化数据基表服务
 * 
 * @author hd
 *
 */
@Service("sieImpactBaseService")
@Transactional(rollbackFor = Exception.class)
public class SieImpactBaseServiceImpl implements SieImpactBaseService {
  Logger logger = LoggerFactory.getLogger(getClass());

  private static final int BATCH_SIZE = 1000;// 一次最多处理数量
  @Autowired
  private SiePublicationDao siePublicationDao;
  @Autowired
  private SieProjectDao sieProjectDao;
  @Autowired
  private SiePatentDao siePatentDao;
  @Autowired
  private SieBhShareDao sieBhShareDao;
  @Autowired
  private SieBhAwardDao sieBhAwardDao;
  @Autowired
  private SieBhReadDao sieBhReadDao;
  @Autowired
  private SieBhDownloadDao sieBhDownloadDao;
  @Autowired
  private SieBhIndexReadDao sieBhIndexReadDao;
  @Autowired
  private KpiImpactBaseIndexDao kpiImpactBaseIndexDao;
  @Autowired
  private KpiImpactBaseViewDao kpiImpactBaseViewDao;
  @Autowired
  private KpiImpactBaseShareDao kpiImpactBaseShareDao;
  @Autowired
  private KpiImpactBaseDownloadDao kpiImpactBaseDownloadDao;
  @Autowired
  private KpiImpactBaseAwardDao kpiImpactBaseAwardDao;
  @Autowired
  private Sie6InstitutionDao sie6InstitutionDao;
  @Autowired
  private PersonDao snsPersonDao;
  @Autowired
  private SnsPsnScienceAreaDao snsPsnScienceAreaDao;

  @Override
  public void doDeal(Long insId) throws ServiceException {
    try {
      /* 成果 */
      doDealPUB(insId);
      /* 专利 */
      doDealPAT(insId);
      /* 项目 */
      doDealPRJ(insId);
      /**/
      doDealIndex(insId);

    } catch (Exception e) {
      logger.error("SieImpactBaseTask任务，社交化行为处理异常！insId : {}", new Object[] {insId, e});
      throw new ServiceException("SieImpactBaseTask任务，社交化行为处理异常！insId : " + insId, e);
    }

  }


  @Override
  public void doDealIndex(Long insId) throws ServiceException {
    try {
      Page<SieBhIndexRead> tempPageRead = new Page<SieBhIndexRead>(BATCH_SIZE);
      tempPageRead = sieBhIndexReadDao.findByDate(insId, getFirstTimeOfMonth(-1), getFirstTimeOfMonth(0), tempPageRead);
      if (tempPageRead.getTotalCount() > 0) {
        Long tempTotalPageRead = tempPageRead.getTotalPages();
        for (int r = 1; r <= tempTotalPageRead; r++) {
          if (r > 1) {
            tempPageRead.setPageNo(r);
            tempPageRead =
                sieBhIndexReadDao.findByDate(insId, getFirstTimeOfMonth(-1), getFirstTimeOfMonth(0), tempPageRead);
          }
          for (SieBhIndexRead read : tempPageRead.getResult()) {
            String ip = read.getIp();
            /* 192.168.xx 127.0.xx不保存 */
            if (ip != null) {
              if (ip.startsWith("192.168.") || ip.startsWith("127.0.")) {
                continue;
              }
            }
            /* ip解析出的地址为空，不放入基表 */
            if (StringUtils.isBlank(read.getIpCountry()) && StringUtils.isBlank(read.getIpProv())
                && StringUtils.isBlank(read.getIpCity())) {
              continue;
            }
            KpiImpactBaseIndex view = new KpiImpactBaseIndex(insId, read.getCreDate(), read.getIp(),
                read.getIpCountry(), read.getIpProv(), read.getIpCity(), read.getPsnId());
            // 数据所属单位名
            view.setInsName(this.getInsName(insId));
            // 拆分记录时间
            Map<String, Integer> timemap = analyTime(view.getTimeRecords());
            if (timemap != null) {
              view.setTiemYear(timemap.get(ImpactConsts.FIELD_YEAR));
              view.setTimeMon(timemap.get(ImpactConsts.FIELD_MONTH));
              view.setTimeDay(timemap.get(ImpactConsts.FIELD_DAY));
            }
            // 填充操作人相关信息
            analyPersonIndexView(view);
            kpiImpactBaseIndexDao.save(view);
          }

        }
      }

    } catch (Exception e) {
      logger.error("SieImpactBaseTask任务，主页社交化行为处理异常！insId : {}", new Object[] {insId, e});
      throw new ServiceException("SieImpactBaseTask任务，主页社交化行为处理异常！insId : " + insId, e);
    }

  }

  /* 统计成果 */
  @Override
  public void doDealPUB(Long insId) throws ServiceException {
    try {
      String types = getTypes(ImpactConsts.SIE_PUB);
      if (StringUtils.isBlank(types)) {
        throw new ServiceException("SieImpactBaseTask任务，bh_read,成果社交化行为处理异常,insId : " + insId);
      }
      Date beginTime = getFirstTimeOfMonth(-1);
      Date endTime = getFirstTimeOfMonth(0);


      doDealViewPUB(insId, types, beginTime, endTime);
      doDealSharePUB(insId, types, beginTime, endTime);
      doDealAwardPUB(insId, types, beginTime, endTime);
      doDealDownloadPUB(insId, types, beginTime, endTime);

    } catch (Exception e) {
      logger.error("SieImpactBaseTask任务，成果社交化行为处理异常！insId : {}", new Object[] {insId, e});
      throw new ServiceException("SieImpactBaseTask任务，成果社交化行为处理异常！insId : " + insId, e);
    }

  }

  /**
   * 统计成果阅读记录
   * 
   * @param insId
   * @param types
   * @param beginTime
   * @param endTime
   */
  private void doDealViewPUB(Long insId, String types, Date beginTime, Date endTime) {
    // 获得简单类名，用于sql语句
    String classSimpleName = SieBhRead.class.getSimpleName();

    Page<SiePublication> tempPage = new Page<SiePublication>(BATCH_SIZE);
    tempPage =
        siePublicationDao.findPublicationByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
    if (tempPage.getTotalCount() > 0) {
      Long tempTotalPage = tempPage.getTotalPages();
      for (int j = 1; j <= tempTotalPage; j++) {
        if (j > 1) {
          tempPage.setPageNo(j);
          tempPage = siePublicationDao.findPublicationByInsIdWithBhX(insId, tempPage, types, beginTime, endTime,
              classSimpleName);
        }
        for (SiePublication pub : tempPage.getResult()) {
          /* 阅读数 */
          doDealView(insId, pub.getTitle(), pub.getPubId(), ImpactConsts.SIE_PUB);
        }
      }
    }
  }

  /**
   * 统计成果分享记录
   * 
   * @param insId
   * @param types
   * @param beginTime
   * @param endTime
   */
  private void doDealSharePUB(Long insId, String types, Date beginTime, Date endTime) {
    // 获得简单类名，用于sql语句
    String classSimpleName = SieBhShare.class.getSimpleName();

    Page<SiePublication> tempPage = new Page<SiePublication>(BATCH_SIZE);
    tempPage =
        siePublicationDao.findPublicationByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
    if (tempPage.getTotalCount() > 0) {
      Long tempTotalPage = tempPage.getTotalPages();
      for (int j = 1; j <= tempTotalPage; j++) {
        if (j > 1) {
          tempPage.setPageNo(j);
          tempPage = siePublicationDao.findPublicationByInsIdWithBhX(insId, tempPage, types, beginTime, endTime,
              classSimpleName);
        }
        for (SiePublication pub : tempPage.getResult()) {
          /* 分享数 */
          doDealShare(insId, pub.getTitle(), pub.getPubId(), ImpactConsts.SIE_PUB);
        }
      }
    }
  }

  /**
   * 统计成果点赞记录
   * 
   * @param insId
   * @param types
   * @param beginTime
   * @param endTime
   */
  private void doDealAwardPUB(Long insId, String types, Date beginTime, Date endTime) {
    // 获得简单类名，用于sql语句
    String classSimpleName = SieBhAward.class.getSimpleName();

    Page<SiePublication> tempPage = new Page<SiePublication>(BATCH_SIZE);
    tempPage =
        siePublicationDao.findPublicationByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
    if (tempPage.getTotalCount() > 0) {
      Long tempTotalPage = tempPage.getTotalPages();
      for (int j = 1; j <= tempTotalPage; j++) {
        if (j > 1) {
          tempPage.setPageNo(j);
          tempPage = siePublicationDao.findPublicationByInsIdWithBhX(insId, tempPage, types, beginTime, endTime,
              classSimpleName);
        }
        for (SiePublication pub : tempPage.getResult()) {
          /* 点赞数 */
          doDealAward(insId, pub.getTitle(), pub.getPubId(), ImpactConsts.SIE_PUB);
        }
      }
    }
  }

  /**
   * 统计成果下载记录
   * 
   * @param insId
   * @param types
   * @param beginTime
   * @param endTime
   */
  private void doDealDownloadPUB(Long insId, String types, Date beginTime, Date endTime) {
    // 获得简单类名，用于sql语句
    String classSimpleName = SieBhDownload.class.getSimpleName();

    Page<SiePublication> tempPage = new Page<SiePublication>(BATCH_SIZE);
    tempPage =
        siePublicationDao.findPublicationByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
    if (tempPage.getTotalCount() > 0) {
      Long tempTotalPage = tempPage.getTotalPages();
      for (int j = 1; j <= tempTotalPage; j++) {
        if (j > 1) {
          tempPage.setPageNo(j);
          tempPage = siePublicationDao.findPublicationByInsIdWithBhX(insId, tempPage, types, beginTime, endTime,
              classSimpleName);
        }
        for (SiePublication pub : tempPage.getResult()) {
          /* 下载数 */
          doDealDownload(insId, pub.getTitle(), pub.getPubId(), ImpactConsts.SIE_PUB);
        }
      }
    }
  }

  /**
   * 统计专利
   */
  @Override
  public void doDealPAT(Long insId) throws ServiceException {
    try {
      String types = getTypes(ImpactConsts.SIE_PATENT);
      if (StringUtils.isBlank(types)) {
        throw new ServiceException("SieImpactBaseTask任务，bh_read,专利社交化行为处理异常,insId : " + insId);
      }
      Date beginTime = getFirstTimeOfMonth(-1);
      Date endTime = getFirstTimeOfMonth(0);

      doDealViewPAT(insId, types, beginTime, endTime);
      doDealSharePAT(insId, types, beginTime, endTime);
      doDealAwardPAT(insId, types, beginTime, endTime);
      doDealDownloadPAT(insId, types, beginTime, endTime);
    } catch (Exception e) {
      logger.error("SieImpactBaseTask任务，专利社交化行为处理异常！insId : {}", new Object[] {insId, e});
      throw new ServiceException("SieImpactBaseTask任务，专利社交化行为处理异常！insId : " + insId, e);
    }

  }

  /**
   * 统计专利阅读记录
   * 
   * @param insId
   * @param types
   * @param beginTime
   * @param endTime
   */
  private void doDealViewPAT(Long insId, String types, Date beginTime, Date endTime) {
    // 获得简单类名，用于sql语句
    String classSimpleName = SieBhRead.class.getSimpleName();

    Page<SiePatent> tempPage = new Page<SiePatent>(BATCH_SIZE);
    tempPage = siePatentDao.findPatentByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
    if (tempPage.getTotalCount() > 0) {
      Long tempTotalPage = tempPage.getTotalPages();
      for (int j = 1; j <= tempTotalPage; j++) {
        if (j > 1) {
          tempPage.setPageNo(j);
          tempPage = siePatentDao.findPatentByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
        }
        for (SiePatent pub : tempPage.getResult()) {
          /* 阅读数 */
          doDealView(insId, pub.getTitle(), pub.getPatId(), ImpactConsts.SIE_PATENT);
        }
      }
    }
  }

  /**
   * 统计专利分享记录
   * 
   * @param insId
   * @param types
   * @param beginTime
   * @param endTime
   */
  private void doDealSharePAT(Long insId, String types, Date beginTime, Date endTime) {
    // 获得简单类名，用于sql语句
    String classSimpleName = SieBhShare.class.getSimpleName();

    Page<SiePatent> tempPage = new Page<SiePatent>(BATCH_SIZE);
    tempPage = siePatentDao.findPatentByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
    if (tempPage.getTotalCount() > 0) {
      Long tempTotalPage = tempPage.getTotalPages();
      for (int j = 1; j <= tempTotalPage; j++) {
        if (j > 1) {
          tempPage.setPageNo(j);
          tempPage = siePatentDao.findPatentByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
        }
        for (SiePatent pub : tempPage.getResult()) {
          /* 分享数 */
          doDealShare(insId, pub.getTitle(), pub.getPatId(), ImpactConsts.SIE_PATENT);
        }
      }
    }
  }

  /**
   * 统计专利点赞记录
   * 
   * @param insId
   * @param types
   * @param beginTime
   * @param endTime
   */
  private void doDealAwardPAT(Long insId, String types, Date beginTime, Date endTime) {
    // 获得简单类名，用于sql语句
    String classSimpleName = SieBhAward.class.getSimpleName();

    Page<SiePatent> tempPage = new Page<SiePatent>(BATCH_SIZE);
    tempPage = siePatentDao.findPatentByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
    if (tempPage.getTotalCount() > 0) {
      Long tempTotalPage = tempPage.getTotalPages();
      for (int j = 1; j <= tempTotalPage; j++) {
        if (j > 1) {
          tempPage.setPageNo(j);
          tempPage = siePatentDao.findPatentByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
        }
        for (SiePatent pat : tempPage.getResult()) {
          /* 点赞数 */
          doDealAward(insId, pat.getTitle(), pat.getPatId(), ImpactConsts.SIE_PATENT);
        }
      }
    }
  }

  /**
   * 统计专利下载记录
   * 
   * @param insId
   * @param types
   * @param beginTime
   * @param endTime
   */
  private void doDealDownloadPAT(Long insId, String types, Date beginTime, Date endTime) {
    // 获得简单类名，用于sql语句
    String classSimpleName = SieBhDownload.class.getSimpleName();

    Page<SiePatent> tempPage = new Page<SiePatent>(BATCH_SIZE);
    tempPage = siePatentDao.findPatentByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
    if (tempPage.getTotalCount() > 0) {
      Long tempTotalPage = tempPage.getTotalPages();
      for (int j = 1; j <= tempTotalPage; j++) {
        if (j > 1) {
          tempPage.setPageNo(j);
          tempPage = siePatentDao.findPatentByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
        }
        for (SiePatent pat : tempPage.getResult()) {
          /* 下载数 */
          doDealDownload(insId, pat.getTitle(), pat.getPatId(), ImpactConsts.SIE_PATENT);
        }
      }
    }
  }

  /**
   * 统计项目
   */
  @Override
  public void doDealPRJ(Long insId) throws ServiceException {
    try {
      String types = getTypes(ImpactConsts.SIE_PRJ);
      if (StringUtils.isBlank(types)) {
        throw new ServiceException("SieImpactBaseTask任务，bh_read,项目社交化行为处理异常,insId : " + insId);
      }
      Date beginTime = getFirstTimeOfMonth(-1);
      Date endTime = getFirstTimeOfMonth(0);

      doDealViewPRJ(insId, types, beginTime, endTime);
      doDealSharePRJ(insId, types, beginTime, endTime);
      doDealAwardPRJ(insId, types, beginTime, endTime);
      doDealDownloadPRJ(insId, types, beginTime, endTime);
    } catch (Exception e) {
      logger.error("SieImpactBaseTask任务，项目社交化行为处理异常！insId : {}", new Object[] {insId, e});
      throw new ServiceException("SieImpactBaseTask任务，项目社交化行为处理异常！insId : " + insId, e);
    }

  }

  /**
   * 统计项目阅读记录
   * 
   * @param insId
   * @param types
   * @param beginTime
   * @param endTime
   */
  private void doDealViewPRJ(Long insId, String types, Date beginTime, Date endTime) {
    // 获得简单类名，用于sql语句
    String classSimpleName = SieBhRead.class.getSimpleName();

    Page<SieProject> tempPage = new Page<SieProject>(BATCH_SIZE);
    tempPage = sieProjectDao.findProjectByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
    if (tempPage.getTotalCount() > 0) {
      Long tempTotalPage = tempPage.getTotalPages();
      for (int j = 1; j <= tempTotalPage; j++) {
        if (j > 1) {
          tempPage.setPageNo(j);
          tempPage =
              sieProjectDao.findProjectByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
        }
        for (SieProject prj : tempPage.getResult()) {
          /* 阅读数 */
          doDealView(insId, prj.getTitle(), prj.getId(), ImpactConsts.SIE_PRJ);
        }
      }
    }
  }

  /**
   * 统计项目分享记录
   * 
   * @param insId
   * @param types
   * @param beginTime
   * @param endTime
   */
  private void doDealSharePRJ(Long insId, String types, Date beginTime, Date endTime) {
    // 获得简单类名，用于sql语句
    String classSimpleName = SieBhShare.class.getSimpleName();

    Page<SieProject> tempPage = new Page<SieProject>(BATCH_SIZE);
    tempPage = sieProjectDao.findProjectByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
    if (tempPage.getTotalCount() > 0) {
      Long tempTotalPage = tempPage.getTotalPages();
      for (int j = 1; j <= tempTotalPage; j++) {
        if (j > 1) {
          tempPage.setPageNo(j);
          tempPage =
              sieProjectDao.findProjectByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
        }
        for (SieProject prj : tempPage.getResult()) {
          /* 分享数 */
          doDealShare(insId, prj.getTitle(), prj.getId(), ImpactConsts.SIE_PRJ);
        }
      }
    }
  }


  /**
   * 统计项目点赞记录
   * 
   * @param insId
   * @param types
   * @param beginTime
   * @param endTime
   */
  private void doDealAwardPRJ(Long insId, String types, Date beginTime, Date endTime) {
    String classSimpleName = SieBhAward.class.getSimpleName();

    Page<SieProject> tempPage = new Page<SieProject>(BATCH_SIZE);
    tempPage = sieProjectDao.findProjectByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
    if (tempPage.getTotalCount() > 0) {
      Long tempTotalPage = tempPage.getTotalPages();
      for (int j = 1; j <= tempTotalPage; j++) {
        if (j > 1) {
          tempPage.setPageNo(j);
          tempPage =
              sieProjectDao.findProjectByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
        }
        for (SieProject prj : tempPage.getResult()) {
          /* 点赞数 */
          doDealAward(insId, prj.getTitle(), prj.getId(), ImpactConsts.SIE_PRJ);
        }
      }
    }
  }


  /**
   * 统计项目下载记录
   * 
   * @param insId
   * @param types
   * @param beginTime
   * @param endTime
   */
  private void doDealDownloadPRJ(Long insId, String types, Date beginTime, Date endTime) {
    String classSimpleName = SieBhDownload.class.getSimpleName();

    Page<SieProject> tempPage = new Page<SieProject>(BATCH_SIZE);
    tempPage = sieProjectDao.findProjectByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
    if (tempPage.getTotalCount() > 0) {
      Long tempTotalPage = tempPage.getTotalPages();
      for (int j = 1; j <= tempTotalPage; j++) {
        if (j > 1) {
          tempPage.setPageNo(j);
          tempPage =
              sieProjectDao.findProjectByInsIdWithBhX(insId, tempPage, types, beginTime, endTime, classSimpleName);
        }
        for (SieProject prj : tempPage.getResult()) {
          /* 下载数 */
          doDealDownload(insId, prj.getTitle(), prj.getId(), ImpactConsts.SIE_PRJ);
        }
      }
    }
  }


  @Override
  public void doDealView(Long insId, String title, Long keyId, Integer keyType) throws ServiceException {
    try {
      Page<SieBhRead> tempPageRead = new Page<SieBhRead>(BATCH_SIZE);
      String types = getTypes(keyType);
      if (StringUtils.isBlank(types)) {
        throw new ServiceException(
            "SieImpactBaseTask任务，bh_read,阅读行为处理异常,keyType不能为空！insId : " + insId + ";keyId : " + keyId);
      }
      tempPageRead =
          sieBhReadDao.findByTypeAndKeyIds(keyId, types, getFirstTimeOfMonth(-1), getFirstTimeOfMonth(0), tempPageRead);
      if (tempPageRead.getTotalCount() > 0) {
        Long tempTotalPageRead = tempPageRead.getTotalPages();
        for (int r = 1; r <= tempTotalPageRead; r++) {
          if (r > 1) {
            tempPageRead.setPageNo(r);
            tempPageRead = sieBhReadDao.findByTypeAndKeyIds(keyId, types, getFirstTimeOfMonth(-1),
                getFirstTimeOfMonth(0), tempPageRead);
          }
          for (SieBhRead read : tempPageRead.getResult()) {
            String ip = read.getIp();
            /* 192.168.xx 127.0.xx不保存 */
            if (ip != null) {
              if (ip.startsWith("192.168.") || ip.startsWith("127.0.")) {
                continue;
              }
            }
            /* ip解析出的地址为空，不放入基表 */
            if (StringUtils.isBlank(read.getIpCountry()) && StringUtils.isBlank(read.getIpProv())
                && StringUtils.isBlank(read.getIpCity())) {
              continue;
            }
            KpiImpactBaseView view = new KpiImpactBaseView(insId, read.getCreDate(), keyType, read.getKeyId(), title,
                read.getIp(), read.getIpCountry(), read.getIpProv(), read.getIpCity(), read.getReadPsnId());
            // 数据所属单位名
            view.setInsName(this.getInsName(insId));
            // 拆分记录时间
            Map<String, Integer> timemap = analyTime(view.getTimeRecords());
            if (timemap != null) {
              view.setTiemYear(timemap.get(ImpactConsts.FIELD_YEAR));
              view.setTimeMon(timemap.get(ImpactConsts.FIELD_MONTH));
              view.setTimeDay(timemap.get(ImpactConsts.FIELD_DAY));
            }
            // 填充操作人相关信息
            analyPersonView(view);
            kpiImpactBaseViewDao.save(view);
          }

        }
      }

    } catch (Exception e) {
      logger.error("SieImpactBaseTask任务，bh_read,阅读行为处理异常！insId : {}; keyId :{}; keyType:{}",
          new Object[] {insId, keyId, keyType, e});
      throw new ServiceException(
          "SieImpactBaseTask任务，bh_read,阅读行为处理异常！insId : " + insId + ";keyId : " + keyId + " ;keyType" + keyType, e);
    }

  }



  @Override
  public void doDealAward(Long insId, String title, Long keyId, Integer keyType) throws ServiceException {
    try {
      Page<SieBhAward> tempPageAwrd = new Page<SieBhAward>(BATCH_SIZE);
      String types = getTypes(keyType);
      if (StringUtils.isBlank(types)) {
        throw new ServiceException(
            "SieImpactBaseTask任务，bh_award,赞行为处理异常,keyType不能为空！insId : " + insId + ";keyId : " + keyId);
      }
      tempPageAwrd = sieBhAwardDao.findByTypeAndKeyIds(keyId, types, getFirstTimeOfMonth(-1), getFirstTimeOfMonth(0),
          tempPageAwrd);
      if (tempPageAwrd.getTotalCount() > 0) {
        Long tempTotalPageRead = tempPageAwrd.getTotalPages();
        for (int r = 1; r <= tempTotalPageRead; r++) {
          if (r > 1) {
            tempPageAwrd.setPageNo(r);
            tempPageAwrd = sieBhAwardDao.findByTypeAndKeyIds(keyId, types, getFirstTimeOfMonth(-1),
                getFirstTimeOfMonth(0), tempPageAwrd);
          }
          for (SieBhAward award : tempPageAwrd.getResult()) {
            String ip = award.getIp();
            /* 192.168.xx 127.0.xx不保存 */
            if (ip != null) {
              if (ip.startsWith("192.168.") || ip.startsWith("127.0.")) {
                continue;
              }
            }
            /* ip解析出的地址为空，不放入基表 */
            if (StringUtils.isBlank(award.getIpCountry()) && StringUtils.isBlank(award.getIpProv())
                && StringUtils.isBlank(award.getIpCity())) {
              continue;
            }
            KpiImpactBaseAward kpiaward =
                new KpiImpactBaseAward(insId, award.getCreDate(), keyType, award.getKeyId(), title, award.getIp(),
                    award.getIpCountry(), award.getIpProv(), award.getIpCity(), award.getAwardPsnId());
            // 数据所属单位名
            kpiaward.setInsName(this.getInsName(insId));
            // 拆分记录时间
            // 拆分记录时间
            Map<String, Integer> timemap = analyTime(kpiaward.getTimeRecords());
            if (timemap != null) {
              kpiaward.setTiemYear(timemap.get(ImpactConsts.FIELD_YEAR));
              kpiaward.setTimeMon(timemap.get(ImpactConsts.FIELD_MONTH));
              kpiaward.setTimeDay(timemap.get(ImpactConsts.FIELD_DAY));
            }
            // 填充操作人相关信息
            analyPersonAward(kpiaward);
            kpiImpactBaseAwardDao.save(kpiaward);
          }

        }
      }


    } catch (Exception e) {
      logger.error("SieImpactBaseTask任务，bh_award,赞行为处理异常！insId : {}; keyId :{}; keyType:{}",
          new Object[] {insId, keyId, keyType, e});
      throw new ServiceException(
          "SieImpactBaseTask任务，bh_award,赞行为处理异常！insId : " + insId + ";keyId : " + keyId + ";keyType : " + keyType, e);
    }

  }

  @Override
  public void doDealDownload(Long insId, String title, Long keyId, Integer keyType) throws ServiceException {
    try {
      Page<SieBhDownload> tempPageRead = new Page<SieBhDownload>(BATCH_SIZE);
      String types = getTypes(keyType);
      if (StringUtils.isBlank(types)) {
        throw new ServiceException(
            "SieImpactBaseTask任务，bh_download,下载行为处理异常,keyType不能为空！insId : " + insId + ";keyId : " + keyId);
      }
      tempPageRead = sieBhDownloadDao.findByTypeAndKeyIds(keyId, types, getFirstTimeOfMonth(-1), getFirstTimeOfMonth(0),
          tempPageRead);
      if (tempPageRead.getTotalCount() > 0) {
        Long tempTotalPageRead = tempPageRead.getTotalPages();
        for (int r = 1; r <= tempTotalPageRead; r++) {
          if (r > 1) {
            tempPageRead.setPageNo(r);
            tempPageRead = sieBhDownloadDao.findByTypeAndKeyIds(keyId, types, getFirstTimeOfMonth(-1),
                getFirstTimeOfMonth(0), tempPageRead);
          }
          for (SieBhDownload download : tempPageRead.getResult()) {
            String ip = download.getIp();
            /* 192.168.xx 127.0.xx不保存 */
            if (ip != null) {
              if (ip.startsWith("192.168.") || ip.startsWith("127.0.")) {
                continue;
              }
            }
            /* ip解析出的地址为空，不放入基表 */
            if (StringUtils.isBlank(download.getIpCountry()) && StringUtils.isBlank(download.getIpProv())
                && StringUtils.isBlank(download.getIpCity())) {
              continue;
            }
            KpiImpactBaseDownload kpidownload = new KpiImpactBaseDownload(insId, download.getCreDate(), keyType,
                download.getKeyId(), title, download.getIp(), download.getIpCountry(), download.getIpProv(),
                download.getIpCity(), download.getDownloadPsnId());
            // 数据所属单位名
            kpidownload.setInsName(this.getInsName(insId));
            // 拆分记录时间
            Map<String, Integer> timemap = analyTime(kpidownload.getTimeRecords());
            if (timemap != null) {
              kpidownload.setTiemYear(timemap.get(ImpactConsts.FIELD_YEAR));
              kpidownload.setTimeMon(timemap.get(ImpactConsts.FIELD_MONTH));
              kpidownload.setTimeDay(timemap.get(ImpactConsts.FIELD_DAY));
            }
            // 填充操作人相关信息
            analyPersonDownload(kpidownload);
            kpiImpactBaseDownloadDao.save(kpidownload);
          }

        }
      }

    } catch (Exception e) {
      logger.error("SieImpactBaseTask任务，bh_download,下载行为处理异常！insId : {}; keyId :{}; keyType:{}",
          new Object[] {insId, keyId, keyType, e});
      throw new ServiceException(
          "SieImpactBaseTask任务，bh_download,下载行为处理异常！insId : " + insId + ";keyId : " + keyId + " ;keyType" + keyType, e);
    }

  }


  @Override
  public void doDealShare(Long insId, String title, Long keyId, Integer keyType) throws ServiceException {
    try {
      Page<SieBhShare> tempPageRead = new Page<SieBhShare>(BATCH_SIZE);
      String types = getTypes(keyType);
      if (StringUtils.isBlank(types)) {
        throw new ServiceException(
            "SieImpactBaseTask任务，bh_share,分享行为处理异常,keyType不能为空！insId : " + insId + ";keyId : " + keyId);
      }
      tempPageRead = sieBhShareDao.findByTypeAndKeyIds(keyId, types, getFirstTimeOfMonth(-1), getFirstTimeOfMonth(0),
          tempPageRead);
      if (tempPageRead.getTotalCount() > 0) {
        Long tempTotalPageRead = tempPageRead.getTotalPages();
        for (int r = 1; r <= tempTotalPageRead; r++) {
          if (r > 1) {
            tempPageRead.setPageNo(r);
            tempPageRead = sieBhShareDao.findByTypeAndKeyIds(keyId, types, getFirstTimeOfMonth(-1),
                getFirstTimeOfMonth(0), tempPageRead);
          }
          for (SieBhShare share : tempPageRead.getResult()) {
            String ip = share.getIp();
            /* 192.168.xx 127.0.xx不保存 */
            if (ip != null) {
              if (ip.startsWith("192.168.") || ip.startsWith("127.0.")) {
                continue;
              }
            }
            /* ip解析出的地址为空，不放入基表 */
            if (StringUtils.isBlank(share.getIpCountry()) && StringUtils.isBlank(share.getIpProv())
                && StringUtils.isBlank(share.getIpCity())) {
              continue;
            }
            KpiImpactBaseShare kpishare =
                new KpiImpactBaseShare(insId, share.getCreDate(), keyType, share.getKeyId(), title, share.getIp(),
                    share.getIpCountry(), share.getIpProv(), share.getIpCity(), share.getSharePsnId());
            // 数据所属单位名
            kpishare.setInsName(this.getInsName(insId));
            // 拆分记录时间
            Map<String, Integer> timemap = analyTime(kpishare.getTimeRecords());
            if (timemap != null) {
              kpishare.setTiemYear(timemap.get(ImpactConsts.FIELD_YEAR));
              kpishare.setTimeMon(timemap.get(ImpactConsts.FIELD_MONTH));
              kpishare.setTimeDay(timemap.get(ImpactConsts.FIELD_DAY));
            }
            // 填充操作人相关信息
            analyPersonShare(kpishare);
            kpiImpactBaseShareDao.save(kpishare);
          }

        }
      }

    } catch (Exception e) {
      logger.error("SieImpactBaseTask任务，bh_share,分享行为处理异常！insId : {}; keyId :{}; keyType:{}",
          new Object[] {insId, keyId, keyType, e});
      throw new ServiceException(
          "SieImpactBaseTask任务，bh_share,分享行为处理异常！insId : " + insId + ";keyId : " + keyId + " ;keyType" + keyType, e);
    }

  }



  /**
   * 获取insName
   * 
   * @param insId
   * @return
   */
  private String getInsName(Long insId) {
    if (insId != null) {
      Sie6Institution ins = sie6InstitutionDao.get(insId);
      if (ins != null) {
        return ins.getInsName();
      }
    }
    return null;
  }

  private String getTypes(Integer type) {
    switch (type) {
      case 1:
        return ImpactConsts.PRJ;
      case 2:
        return ImpactConsts.PUB;
      case 3:
        return ImpactConsts.PAT;
      default:
        return null;
    }
  }

  @SuppressWarnings("unused")
  private void analyTime(KpiImpactBaseView view) throws ServiceException {
    try {
      if (view == null || view.getTimeRecords() == null) {
        return;
      }
      Calendar c = Calendar.getInstance();
      c.setTime(view.getTimeRecords());
      view.setTiemYear(c.get(Calendar.YEAR));
      view.setTimeMon(c.get(Calendar.MONTH));
      view.setTimeDay(c.get(Calendar.DAY_OF_MONTH));
    } catch (Exception e) {
      logger.error("SieImpactBaseTask任务拆分时间异常！time : {}; keyType :{}; keyCode:{}",
          new Object[] {view.getTimeRecords(), view.getKeyType(), view.getKeyCode(), e});
      throw new ServiceException("SieImpactBaseTask任务拆分时间异常！time : " + view.getTimeRecords() + ";keyType : "
          + view.getKeyType() + ";keyCode:" + view.getKeyCode(), e);
    }
  }

  /**
   * 拆分出日期的年、月、日
   * 
   * @param dateRecords
   * @return
   * @throws ServiceException
   */
  private Map<String, Integer> analyTime(Date dateRecords) throws ServiceException {
    Map<String, Integer> m = null;
    try {
      if (dateRecords == null) {
        return null;
      }
      m = new HashMap<String, Integer>();
      Calendar c = Calendar.getInstance();
      c.setTime(dateRecords);
      m.put(ImpactConsts.FIELD_YEAR, c.get(Calendar.YEAR));
      m.put(ImpactConsts.FIELD_MONTH, c.get(Calendar.MONTH) + 1);
      m.put(ImpactConsts.FIELD_DAY, c.get(Calendar.DAY_OF_MONTH));
      return m;
    } catch (Exception e) {
      logger.error("SieImpactBaseTask任务拆分时间异常！time : {}", new Object[] {dateRecords, e});
      throw new ServiceException("SieImpactBaseTask任务拆分时间异常！time : " + dateRecords, e);
    }
  }

  /**
   * 填充操作人相关信息
   * 
   * @param view
   * @throws ServiceException
   */
  private void analyPersonView(KpiImpactBaseView view) throws ServiceException {
    try {

      if (view == null || view.getPsnId() == null || view.getPsnId() == 0) {
        return;
      }
      Person person = snsPersonDao.getPersonName(view.getPsnId());
      if (person != null) {
        view.setPsnName(person.getZhName());
        view.setPsnRegId(person.getRegionId());
        view.setPsnPos(person.getPosition());
        view.setPsnInsName(getInsName(person.getInsId()));
        if (StringUtils.isBlank(view.getPsnInsName())) {
          view.setPsnInsName(person.getInsName());
        }
      }
      List<String> areas = snsPsnScienceAreaDao.findPsnScienceAreas(view.getPsnId());
      if (areas != null && areas.size() > 0) {
        view.setPsnSub(areas.get(0));
      }

    } catch (Exception e) {
      logger.error("SieImpactBaseTask任务获取人员信息异常！psnId : {};  keyType :{}; keyCode:{}",
          new Object[] {view.getPsnId(), view.getKeyType(), view.getKeyCode(), e});
      throw new ServiceException("SieImpactBaseTask任务获取人员信息异常！psnId : " + view.getPsnId() + ";keyType : "
          + view.getKeyType() + ";keyCode:" + view.getKeyCode(), e);
    }
  }

  /**
   * 填充操作人相关信息
   * 
   * @param view
   * @throws ServiceException
   */
  private void analyPersonIndexView(KpiImpactBaseIndex view) throws ServiceException {
    try {

      if (view == null || view.getPsnId() == null || view.getPsnId() == 0) {
        return;
      }
      Person person = snsPersonDao.getPersonName(view.getPsnId());
      if (person != null) {
        view.setPsnName(person.getZhName());
        view.setPsnRegId(person.getRegionId());
        view.setPsnPos(person.getPosition());
        view.setPsnInsName(getInsName(person.getInsId()));
        if (StringUtils.isBlank(view.getPsnInsName())) {
          view.setPsnInsName(person.getInsName());
        }
      }
      List<String> areas = snsPsnScienceAreaDao.findPsnScienceAreas(view.getPsnId());
      if (areas != null && areas.size() > 0) {
        view.setPsnSub(areas.get(0));
      }

    } catch (Exception e) {
      logger.error("SieImpactBaseTask任务获取人员信息异常！psnId : {};  insId :{}",
          new Object[] {view.getPsnId(), view.getInsId(), e});
      throw new ServiceException(
          "SieImpactBaseTask任务获取人员信息异常！psnId : " + view.getPsnId() + " ;insId:" + view.getInsId(), e);
    }
  }

  /**
   * 填充操作人相关信息
   * 
   * @param award
   * @throws ServiceException
   */
  private void analyPersonAward(KpiImpactBaseAward award) throws ServiceException {
    try {

      if (award == null || award.getPsnId() == null || award.getPsnId() == 0) {
        return;
      }
      Person person = snsPersonDao.getPersonName(award.getPsnId());
      if (person != null) {
        award.setPsnName(person.getZhName());
        award.setPsnRegId(person.getRegionId());
        award.setPsnPos(person.getPosition());
        award.setPsnInsName(getInsName(person.getInsId()));
        if (StringUtils.isBlank(award.getPsnInsName())) {
          award.setPsnInsName(person.getInsName());
        }
      }
      List<String> areas = snsPsnScienceAreaDao.findPsnScienceAreas(award.getPsnId());
      if (areas != null && areas.size() > 0) {
        award.setPsnSub(areas.get(0));
      }

    } catch (Exception e) {
      logger.error("SieImpactBaseTask任务获取人员信息异常！psnId : {};  keyType :{}; keyCode:{}",
          new Object[] {award.getPsnId(), award.getKeyType(), award.getKeyCode(), e});
      throw new ServiceException("SieImpactBaseTask任务获取人员信息异常！psnId : " + award.getPsnId() + ";keyType : "
          + award.getKeyType() + ";keyCode:" + award.getKeyCode(), e);
    }
  }

  /**
   * 填充操作人相关信息
   * 
   * @param load
   * @throws ServiceException
   */
  private void analyPersonDownload(KpiImpactBaseDownload load) throws ServiceException {
    try {

      if (load == null || load.getPsnId() == null || load.getPsnId() == 0) {
        return;
      }
      Person person = snsPersonDao.getPersonName(load.getPsnId());
      if (person != null) {
        load.setPsnName(person.getZhName());
        load.setPsnRegId(person.getRegionId());
        load.setPsnPos(person.getPosition());
        load.setPsnInsName(getInsName(person.getInsId()));
        if (StringUtils.isBlank(load.getPsnInsName())) {
          load.setPsnInsName(person.getInsName());
        }
      }
      List<String> areas = snsPsnScienceAreaDao.findPsnScienceAreas(load.getPsnId());
      if (areas != null && areas.size() > 0) {
        load.setPsnSub(areas.get(0));
      }

    } catch (Exception e) {
      logger.error("SieImpactBaseTask任务获取人员信息异常！psnId : {};  keyType :{}; keyCode:{}",
          new Object[] {load.getPsnId(), load.getKeyType(), load.getKeyCode(), e});
      throw new ServiceException("SieImpactBaseTask任务获取人员信息异常！psnId : " + load.getPsnId() + ";keyType : "
          + load.getKeyType() + ";keyCode:" + load.getKeyCode(), e);
    }
  }

  /**
   * 填充操作人相关信息
   * 
   * @param share
   * @throws ServiceException
   */
  private void analyPersonShare(KpiImpactBaseShare share) throws ServiceException {
    try {

      if (share == null || share.getPsnId() == null || share.getPsnId() == 0) {
        return;
      }
      Person person = snsPersonDao.getPersonName(share.getPsnId());
      if (person != null) {
        share.setPsnName(person.getZhName());
        share.setPsnRegId(person.getRegionId());
        share.setPsnPos(person.getPosition());
        share.setPsnInsName(getInsName(person.getInsId()));
        if (StringUtils.isBlank(share.getPsnInsName())) {
          share.setPsnInsName(person.getInsName());
        }
      }
      List<String> areas = snsPsnScienceAreaDao.findPsnScienceAreas(share.getPsnId());
      if (areas != null && areas.size() > 0) {
        share.setPsnSub(areas.get(0));
      }

    } catch (Exception e) {
      logger.error("SieImpactBaseTask任务获取人员信息异常！psnId : {};  keyType :{}; keyCode:{}",
          new Object[] {share.getPsnId(), share.getKeyType(), share.getKeyCode(), e});
      throw new ServiceException("SieImpactBaseTask任务获取人员信息异常！psnId : " + share.getPsnId() + ";keyType : "
          + share.getKeyType() + ";keyCode:" + share.getKeyCode(), e);
    }
  }

  /**
   * 获取每个月的第一天
   * 
   * @param increment
   * @return
   */
  private Date getFirstTimeOfMonth(Integer increment) {
    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.add(Calendar.MONTH, increment);
    c.set(Calendar.DAY_OF_MONTH, 1);
    // 将小时至0
    c.set(Calendar.HOUR_OF_DAY, 0);
    // 将分钟至0
    c.set(Calendar.MINUTE, 0);
    // 将秒至0
    c.set(Calendar.SECOND, 0);
    // 将毫秒至0
    c.set(Calendar.MILLISECOND, 0);
    Date m = c.getTime();
    return m;
  }

  public static void main(String[] args) {
    // 2018-12-06 15:45:59.0
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.add(Calendar.MONTH, 0);
    c.set(Calendar.DAY_OF_MONTH, 1);
    // 将小时至0
    c.set(Calendar.HOUR_OF_DAY, 0);
    // 将分钟至0
    c.set(Calendar.MINUTE, 0);
    // 将秒至0
    c.set(Calendar.SECOND, 0);
    // 将毫秒至0
    c.set(Calendar.MILLISECOND, 0);
    System.out.println(c.getTime());
    System.out.println(c.get(Calendar.MONTH));
    System.out.println(c.get(Calendar.YEAR));

    System.out.println(new SieImpactBaseServiceImpl().getFirstTimeOfMonth(-1));
  }


  @Override
  public boolean checkRepeat(Long insId) throws ServiceException {
    boolean flag = false; // 默认没有重复数据
    Date beginTime = getFirstTimeOfMonth(-1);
    Date endTime = getFirstTimeOfMonth(0);
    // 查询阅读明细基础表在时间段内的数量
    Long baseViewCount = kpiImpactBaseViewDao.getCountByDateTime(insId, beginTime, endTime);
    // 查询分享明细基础表在时间段内的数量
    Long baseShareCount = kpiImpactBaseShareDao.getCountByDateTime(insId, beginTime, endTime);
    // 查询点赞明细基础表在时间段内的数量
    Long baseAwardCount = kpiImpactBaseAwardDao.getCountByDateTime(insId, beginTime, endTime);
    // 查询下载明细基础表在时间段内的数量
    Long baseDownloadCount = kpiImpactBaseDownloadDao.getCountByDateTime(insId, beginTime, endTime);
    // 查询主页访问基础表在时间段内的数量
    Long baseIndexCount = kpiImpactBaseIndexDao.getCountByDateTime(insId, beginTime, endTime);
    if (baseViewCount > 0 || baseShareCount > 0 || baseAwardCount > 0 || baseDownloadCount > 0 || baseIndexCount > 0) {
      flag = true; // 上月份单位的相关数据已存在基表中
    }
    return flag;
  }

}
