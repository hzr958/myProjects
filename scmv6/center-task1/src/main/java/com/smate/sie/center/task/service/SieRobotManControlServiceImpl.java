package com.smate.sie.center.task.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.sie.center.task.dao.SiePatStatDao;
import com.smate.sie.center.task.dao.SiePrjStatDao;
import com.smate.sie.center.task.dao.SiePubStatDao;
import com.smate.sie.center.task.dao.SieRobotManConfigDao;
import com.smate.sie.center.task.dao.SieRobotManReflushLogDao;
import com.smate.sie.center.task.model.PatStat;
import com.smate.sie.center.task.model.PubStat;
import com.smate.sie.center.task.model.SiePrjStat;
import com.smate.sie.center.task.model.SieRobotMan;
import com.smate.sie.center.task.model.SieRobotManConfig;
import com.smate.sie.center.task.model.SieRobotManReflushLog;
import com.smate.sie.core.base.utils.dao.prj.SieProjectDao;
import com.smate.sie.core.base.utils.dao.pub.SiePatentDao;
import com.smate.sie.core.base.utils.dao.pub.SiePublicationDao;
import com.smate.sie.core.base.utils.dao.statistics.SieBhCitationDao;
import com.smate.sie.core.base.utils.dao.statistics.SieBhDownloadDao;
import com.smate.sie.core.base.utils.dao.statistics.SieBhIndexReadDao;
import com.smate.sie.core.base.utils.dao.statistics.SieBhIndexShareDao;
import com.smate.sie.core.base.utils.dao.statistics.SieBhReadDao;
import com.smate.sie.core.base.utils.dao.statistics.SieBhShareDao;
import com.smate.sie.core.base.utils.model.prj.SieProject;
import com.smate.sie.core.base.utils.model.pub.SiePatent;
import com.smate.sie.core.base.utils.model.pub.SiePublication;
import com.smate.sie.core.base.utils.model.statistics.SieBhCitation;
import com.smate.sie.core.base.utils.model.statistics.SieBhDownload;
import com.smate.sie.core.base.utils.model.statistics.SieBhIndexRead;
import com.smate.sie.core.base.utils.model.statistics.SieBhIndexShare;
import com.smate.sie.core.base.utils.model.statistics.SieBhRead;
import com.smate.sie.core.base.utils.model.statistics.SieBhShare;

/**
 * 机器人操作service
 * 
 * @author 叶星源
 */
@Service("sieRobotManControlService")
@Transactional(rollbackOn = Exception.class)
public class SieRobotManControlServiceImpl implements SieRobotManControlService {

  @Autowired
  private SiePatStatDao siePatStatDao;
  @Autowired
  private SiePubStatDao siePubStatDao;
  @Autowired
  private SiePrjStatDao siePrjStatDao;
  @Autowired
  private SieBhReadDao sieBhReadDao;
  @Autowired
  private SieBhShareDao sieBhShareDao;
  @Autowired
  private SieBhDownloadDao sieBhDownloadDao;
  @Autowired
  private SieProjectDao sieProjectDao;
  @Autowired
  private SiePatentDao siePatentDao;
  @Autowired
  private SiePublicationDao siePublicationDao;
  @Autowired
  private SieBhCitationDao sieBhCitationDao;
  @Autowired
  private SieRobotManReflushLogDao sieRobotManReflushLogDao;
  @Autowired
  private SieBhIndexShareDao sieBhIndexShareDao;
  @Autowired
  private SieBhIndexReadDao sieBhIndexReadDao;
  @Autowired
  private SieRobotManConfigDao sieRobotManConfigDao;

  @Override
  public void interviewIns(Integer configId, Long insId, SieRobotMan man) {
    if (robotIsAllowExecution(configId)) {
      // 首页分享概率
      if (getSuccessByProbability(man.getInsShareProb())) {
        Date tempTimeOne = man.getControlTime();
        SieBhIndexShare sieBhIndexShare = new SieBhIndexShare();
        sieBhIndexShare.setIp(man.getIp());
        sieBhIndexShare.setCreDate(tempTimeOne);
        sieBhIndexShare.setFmtDate(tempTimeOne.getTime());
        sieBhIndexShare.setInsId(insId);
        sieBhIndexShare.setIpCountry(man.getIpCountry());
        sieBhIndexShare.setIpProv(man.getIpProv());
        sieBhIndexShare.setIpCity(man.getIpCity());
        sieBhIndexShare.setPlatForm(man.getsieBhIndexSharePlatForm());
        sieBhIndexShareDao.save(sieBhIndexShare);
        recoredRobotLog(man.getIp(), sieBhIndexShare.getId(), 9L);
      }
      // 首页阅读
      SieBhIndexRead sieBhIndexRead = new SieBhIndexRead();
      Date thisTime = man.getControlTime();
      sieBhIndexRead.setInsId(insId);
      sieBhIndexRead.setIp(man.getIp());
      sieBhIndexRead.setCreDate(thisTime);
      sieBhIndexRead.setFmtDate(thisTime.getTime());
      sieBhIndexRead.setIpCountry(man.getIpCountry());
      sieBhIndexRead.setIpProv(man.getIpProv());
      sieBhIndexRead.setIpCity(man.getIpCity());
      sieBhIndexReadDao.save(sieBhIndexRead);
      recoredRobotLog(man.getIp(), sieBhIndexRead.getId(), 8L);
    }
  }

  @Override
  public List<Map<String, Object>> interviewProject(Integer configId, Long ins, SieRobotMan man) {
    if (robotIsAllowExecution(configId)) {
      int prjNum = man.getInterviewPrj();
      List<SieProject> projectList = sieProjectDao.getListByInsIdOrderRandom(ins, prjNum);
      if (projectList.size() == 0) {
        return null;
      }
      // 最大访问数
      for (SieProject prj : projectList) {
        Long prjId = prj.getId();
        Long insId = prj.getInsId();
        // 阅读
        if (getSuccessByProbability(man.getReadProb())) {
          readProject(prjId, man, insId);
          // 分享
          if (getSuccessByProbability(man.getShareProb())) {
            shareProject(prjId, man, insId);
          }
          // 下载
          if (prj.getFulltextFileid() != null && getSuccessByProbability(man.getDownloadProb())) {
            downloadProject(prjId, man, insId);
          }
          // 引用
          /*
           * if (getSuccessByProbability(man.getQuoteProb())) { quoteProject(prjId, man, insId); }
           */
        }
      }
    }
    return null;
  }

  @Override
  public List<Map<String, Object>> interviewPatent(Integer configId, Long ins, SieRobotMan man) {
    if (robotIsAllowExecution(configId)) {
      // 获取要访问的专利数
      int patNum = man.getInterviewPat();
      List<SiePatent> patList = siePatentDao.getListByInsIdOrderRandom(ins, patNum);
      if (patList.size() == 0) {
        return null;
      }
      for (SiePatent pat : patList) {
        Long insId = pat.getInsId();
        Long patId = pat.getPatId();
        // 阅读
        if (getSuccessByProbability(man.getReadProb())) {
          readPatent(patId, man, insId);
          // 分享
          if (getSuccessByProbability(man.getShareProb())) {
            sharePatent(patId, man, insId);
          }
          // 下载
          if (pat.getFulltextFileid() != null && getSuccessByProbability(man.getDownloadProb())) {
            downloadPatent(patId, man, insId);
          }
          // 引用
          if (getSuccessByProbability(man.getQuoteProb())) {
            quotePatent(patId, man, insId);
          }
        }
      }
    }
    return null;
  }

  @Override
  public List<Map<String, Object>> interviewPublication(Integer configId, Long ins, SieRobotMan man) {
    if (robotIsAllowExecution(configId)) {
      int pubNum = man.getInterviewPub();
      List<SiePublication> pubList = siePublicationDao.getListByInsIdOrderRandom(ins, pubNum);
      if (pubList.size() == 0) {
        return null;
      }
      for (SiePublication pub : pubList) {
        Long insId = pub.getInsId();
        Long pubId = pub.getPubId();
        // 阅读
        if (getSuccessByProbability(man.getReadProb())) {
          readPublication(pubId, man, insId);
          // 分享
          if (getSuccessByProbability(man.getShareProb())) {
            sharePublication(pubId, man, insId);
          }
          // 下载
          if (pub.getFulltextFileid() != null && getSuccessByProbability(man.getDownloadProb())) {
            downloadPublication(pubId, man, insId);
          }
          // 引用
          if (getSuccessByProbability(man.getQuoteProb())) {
            quotePublication(pubId, man, insId);
          }
        }
      }
    }
    return null;
  }

  /**
   * 阅读项目
   * 
   * @param insId
   */
  public boolean readProject(Long prjId, SieRobotMan man, Long insId) {
    setBhRead(man, prjId, 1, insId);
    // 更新project的阅读数
    SiePrjStat siePrjStat = siePrjStatDao.get(prjId);
    if (siePrjStat != null) {
      Long readNum = siePrjStat.getReadNum();
      if (readNum != null) {
        readNum++;
      } else {
        readNum = 1L;
      }
      if (readNum <= 99999) {
        siePrjStat.setReadNum(readNum);
      }
    } else {
      siePrjStat = new SiePrjStat();
      siePrjStat.setPrjId(prjId);
      siePrjStat.setAwardNum(0L);
      siePrjStat.setCiteNum(0L);
      siePrjStat.setDownloadNum(0L);
      siePrjStat.setReadNum(1L);
      siePrjStat.setShareNum(0L);
    }
    siePrjStatDao.save(siePrjStat);
    return true;
  }

  /**
   * 阅读专利
   * 
   * @param long1
   */
  public boolean readPatent(Long patId, SieRobotMan man, Long insId) {
    setBhRead(man, patId, 3, insId);
    // 更新Patent的阅读数
    PatStat patStat = siePatStatDao.get(patId);
    if (patStat != null) {
      Long readNum = patStat.getReadNum();
      if (readNum != null) {
        readNum++;
      } else {
        readNum = 1L;
      }
      if (readNum <= 99999) {
        patStat.setReadNum(readNum);
      }
    } else {
      patStat = new PatStat();
      patStat.setPatId(patId);
      patStat.setAwardNum(0L);
      patStat.setCiteNum(0L);
      patStat.setDownloadNum(0L);
      patStat.setReadNum(1L);
      patStat.setShareNum(0L);
    }
    siePatStatDao.save(patStat);
    return true;
  }

  /**
   * 阅读成果
   * 
   * @param insId
   */
  public boolean readPublication(Long pubId, SieRobotMan man, Long insId) {
    setBhRead(man, pubId, 2, insId);
    // 更新Publication的阅读数
    PubStat pubStat = siePubStatDao.get(pubId);
    if (pubStat != null) {
      Long readNum = pubStat.getReadNum();
      if (readNum != null) {
        readNum++;
      } else {
        readNum = 1L;
      }
      if (readNum <= 99999) {
        pubStat.setReadNum(readNum);
      }
    } else {
      pubStat = new PubStat();
      pubStat.setPubId(pubId);
      pubStat.setAwardNum(0L);
      pubStat.setCiteNum(0L);
      pubStat.setDownloadNum(0L);
      pubStat.setReadNum(1L);
      pubStat.setShareNum(0L);
    }
    siePubStatDao.save(pubStat);
    return true;
  }

  /**
   * 分享项目
   * 
   * @param insId
   */
  public boolean shareProject(Long prjId, SieRobotMan man, Long insId) {
    setBhShare(man, prjId, 1, insId);
    // 更新project的分享数
    SiePrjStat siePrjStat = siePrjStatDao.get(prjId);
    if (siePrjStat != null) {
      Long shareNum = siePrjStat.getShareNum();
      if (shareNum != null) {
        shareNum++;
      } else {
        shareNum = 1L;
      }
      if (shareNum <= 99999) {
        siePrjStat.setShareNum(shareNum);
      }
    } else {
      siePrjStat = new SiePrjStat();
      siePrjStat.setPrjId(prjId);
      siePrjStat.setAwardNum(0L);
      siePrjStat.setCiteNum(0L);
      siePrjStat.setDownloadNum(0L);
      siePrjStat.setReadNum(0L);
      siePrjStat.setShareNum(1L);
    }
    siePrjStatDao.save(siePrjStat);
    return true;
  }

  /**
   * 分享专利
   * 
   * @param insId
   */
  public boolean sharePatent(Long patId, SieRobotMan man, Long insId) {
    setBhShare(man, patId, 3, insId);
    // 更新Patent的分享数
    PatStat patStat = siePatStatDao.get(patId);
    if (patStat != null) {
      Long shareNum = patStat.getShareNum();
      if (shareNum != null) {
        shareNum++;
      } else {
        shareNum = 1L;
      }
      if (shareNum <= 99999) {
        patStat.setShareNum(shareNum);
      }
    } else {
      patStat = new PatStat();
      patStat.setPatId(patId);
      patStat.setAwardNum(0L);
      patStat.setCiteNum(0L);
      patStat.setDownloadNum(0L);
      patStat.setReadNum(0L);
      patStat.setShareNum(1L);
    }
    siePatStatDao.save(patStat);
    return true;
  }

  /**
   * 分享成果
   * 
   * @param insId
   */
  public boolean sharePublication(Long pubId, SieRobotMan man, Long insId) {
    setBhShare(man, pubId, 2, insId);
    // 更新Publication的分享数
    PubStat pubStat = siePubStatDao.get(pubId);
    if (pubStat != null) {
      Long shareNum = pubStat.getShareNum();
      if (shareNum != null) {
        shareNum++;
      } else {
        shareNum = 1L;
      }
      if (shareNum <= 99999) {
        pubStat.setShareNum(shareNum);
      }
    } else {
      pubStat = new PubStat();
      pubStat.setPubId(pubId);
      pubStat.setAwardNum(0L);
      pubStat.setCiteNum(0L);
      pubStat.setDownloadNum(0L);
      pubStat.setReadNum(0L);
      pubStat.setShareNum(1L);
    }
    siePubStatDao.save(pubStat);
    return true;
  }

  /**
   * 下载项目全文
   * 
   * @param insId
   */
  public boolean downloadProject(Long prjId, SieRobotMan man, Long insId) {
    setBhDownload(man, prjId, 1, insId);
    // 更新project的下载数
    SiePrjStat siePrjStat = siePrjStatDao.get(prjId);
    if (siePrjStat != null) {
      Long downloadNum = siePrjStat.getDownloadNum();
      if (downloadNum != null) {
        downloadNum++;
      } else {
        downloadNum = 1L;
      }
      if (downloadNum <= 99999) {
        siePrjStat.setDownloadNum(downloadNum);
      }
    } else {
      siePrjStat = new SiePrjStat();
      siePrjStat.setPrjId(prjId);
      siePrjStat.setAwardNum(0L);
      siePrjStat.setCiteNum(0L);
      siePrjStat.setDownloadNum(1L);
      siePrjStat.setReadNum(0L);
      siePrjStat.setShareNum(0L);
    }
    siePrjStatDao.save(siePrjStat);
    return true;
  }

  /**
   * 下载专利全文
   * 
   * @param insId
   */
  public boolean downloadPatent(Long patId, SieRobotMan man, Long insId) {
    setBhDownload(man, patId, 3, insId);
    // 更新Patent的下载数
    PatStat patStat = siePatStatDao.get(patId);
    if (patStat != null) {
      Long downloadNum = patStat.getDownloadNum();
      if (downloadNum != null) {
        downloadNum++;
      } else {
        downloadNum = 1L;
      }
      if (downloadNum <= 99999) {
        patStat.setDownloadNum(downloadNum);
      }
    } else {
      patStat = new PatStat();
      patStat.setPatId(patId);
      patStat.setAwardNum(0L);
      patStat.setCiteNum(0L);
      patStat.setDownloadNum(1L);
      patStat.setReadNum(0L);
      patStat.setShareNum(0L);
    }
    siePatStatDao.save(patStat);
    return true;
  }

  /**
   * 下载成果全文
   * 
   * @param insId
   */
  public boolean downloadPublication(Long pubId, SieRobotMan man, Long insId) {
    // 判断该成果是否有全文
    setBhDownload(man, pubId, 2, insId);
    // 更新Publication的下载数
    PubStat pubStat = siePubStatDao.get(pubId);
    if (pubStat != null) {
      Long downloadNum = pubStat.getDownloadNum();
      if (downloadNum != null) {
        downloadNum++;
      } else {
        downloadNum = 1L;
      }
      if (downloadNum <= 99999) {
        pubStat.setDownloadNum(downloadNum);
      }
    } else {
      pubStat = new PubStat();
      pubStat.setPubId(pubId);
      pubStat.setAwardNum(0L);
      pubStat.setCiteNum(0L);
      pubStat.setDownloadNum(1L);
      pubStat.setReadNum(0L);
      pubStat.setShareNum(0L);
    }
    siePubStatDao.save(pubStat);
    return true;
  }

  /**
   * 引用项目
   * 
   * @param insId
   */
  public boolean quoteProject(Long prjId, SieRobotMan man, Long insId) {
    setBHCitation(man, prjId, 1, insId);
    // 更新project的引用数
    SiePrjStat siePrjStat = siePrjStatDao.get(prjId);
    if (siePrjStat != null) {
      Long citeNum = siePrjStat.getCiteNum();
      if (citeNum != null) {
        citeNum++;
      } else {
        citeNum = 1L;
      }
      if (citeNum <= 99999) {
        siePrjStat.setCiteNum(citeNum);
      }
    } else {
      siePrjStat = new SiePrjStat();
      siePrjStat.setPrjId(prjId);
      siePrjStat.setAwardNum(0L);
      siePrjStat.setCiteNum(1L);
      siePrjStat.setDownloadNum(0L);
      siePrjStat.setReadNum(0L);
      siePrjStat.setShareNum(0L);
    }
    siePrjStatDao.save(siePrjStat);
    return true;
  }



  /**
   * 引用专利
   * 
   * @param insId
   */
  public boolean quotePatent(Long patId, SieRobotMan man, Long insId) {
    setBHCitation(man, patId, 3, insId);
    // 更新Patent的引用数
    PatStat patStat = siePatStatDao.get(patId);
    if (patStat != null) {
      Long citeNum = patStat.getCiteNum();
      if (citeNum != null) {
        citeNum++;
      } else {
        citeNum = 1L;
      }
      if (citeNum <= 99999) {
        patStat.setCiteNum(citeNum);
      }
    } else {
      patStat = new PatStat();
      patStat.setPatId(patId);
      patStat.setAwardNum(0L);
      patStat.setCiteNum(1L);
      patStat.setDownloadNum(0L);
      patStat.setReadNum(0L);
      patStat.setShareNum(0L);
    }
    siePatStatDao.save(patStat);
    return true;
  }

  /**
   * 引用成果
   * 
   * @param insId
   */
  public boolean quotePublication(Long pubId, SieRobotMan man, Long insId) {
    setBHCitation(man, pubId, 2, insId);
    // 更新Publication的引用数
    PubStat pubStat = siePubStatDao.get(pubId);
    if (pubStat != null) {
      Long citeNum = pubStat.getCiteNum();
      if (citeNum != null) {
        citeNum++;
      } else {
        citeNum = 1L;
      }
      if (citeNum <= 99999) {
        pubStat.setCiteNum(citeNum);
      }
    } else {
      pubStat = new PubStat();
      pubStat.setPubId(pubId);
      pubStat.setAwardNum(0L);
      pubStat.setCiteNum(1L);
      pubStat.setDownloadNum(0L);
      pubStat.setReadNum(0L);
      pubStat.setShareNum(0L);
    }
    siePubStatDao.save(pubStat);
    return true;
  }

  /**
   * 机器人阅读记录
   * 
   * @param man
   * @param typeId
   * @param type 1项目,2论文,3专利
   * @param insId
   */
  private void setBhRead(SieRobotMan man, Long typeId, Integer type, Long insId) {
    String ip = man.getIp();
    Long psnId = man.getPsnId();
    Date tempDate = man.getControlTime();
    SieBhRead sieBhRead = new SieBhRead();
    sieBhRead.setIp(ip);
    sieBhRead.setType(type);
    sieBhRead.setCreDate(tempDate); // 创建时间
    sieBhRead.setFmtDate(tempDate.getTime()); // 格式化创建时间
    sieBhRead.setKeyId(typeId);
    sieBhRead.setReadPsnId(psnId);
    sieBhRead.setIpCountry(man.getIpCountry());
    sieBhRead.setIpProv(man.getIpProv());
    sieBhRead.setIpCity(man.getIpCity());
    sieBhReadDao.save(sieBhRead);
    // 记录机器人的操作至日志表中
    recoredRobotLog(man.getIp(), sieBhRead.getId(), 1L);
  }

  /**
   * 机器人分享记录
   * 
   * @param man
   * @param typeId
   * @param type 1项目,2论文,3专利
   * @param insId
   */
  private void setBhShare(SieRobotMan man, Long typeId, Integer type, Long insId) {
    String ip = man.getIp();
    Long psnId = man.getPsnId();
    Date tempDate = man.getControlTime();
    SieBhShare sieBhShare = new SieBhShare();
    sieBhShare.setIp(ip);
    sieBhShare.setType(type);
    sieBhShare.setCreDate(tempDate);// 创建时间
    sieBhShare.setFmtDate(tempDate.getTime());// 格式化创建时间
    sieBhShare.setKeyId(typeId);
    sieBhShare.setSharePsnId(psnId);
    sieBhShare.setIpCountry(man.getIpCountry());
    sieBhShare.setIpProv(man.getIpProv());
    sieBhShare.setIpCity(man.getIpCity());
    // 随机生成分享平台
    sieBhShare.setPlatForm(man.getsieBhSharePlatForm());
    sieBhShareDao.save(sieBhShare);
    // 记录机器人的操作至日志表中
    recoredRobotLog(man.getIp(), sieBhShare.getId(), 2L);
  }

  /**
   * 机器人下载记录
   * 
   * @param man
   * @param typeId
   * @param type 1项目,2论文,3专利
   * @param insId
   */
  private void setBhDownload(SieRobotMan man, Long typeId, Integer type, Long insId) {
    String ip = man.getIp();
    Long psnId = man.getPsnId();
    Date tempDate = man.getControlTime();
    SieBhDownload sieBhDownload = new SieBhDownload();
    sieBhDownload.setIp(ip);
    sieBhDownload.setType(type);
    sieBhDownload.setCreDate(tempDate);// 创建时间
    sieBhDownload.setFmtDate(tempDate.getTime());// 格式化创建时间
    sieBhDownload.setKeyId(typeId);
    sieBhDownload.setDownloadPsnId(psnId);
    sieBhDownload.setIpCountry(man.getIpCountry());
    sieBhDownload.setIpProv(man.getIpProv());
    sieBhDownload.setIpCity(man.getIpCity());
    sieBhDownloadDao.save(sieBhDownload);
    // 记录机器人的操作至日志表中
    recoredRobotLog(man.getIp(), sieBhDownload.getId(), 3L);
  }

  /**
   * 机器人引用记录
   * 
   * @param type 1项目,2论文,3专利
   * @param insId
   */
  private void setBHCitation(SieRobotMan man, Long typeId, int type, Long insId) {
    String ip = man.getIp();
    Long psnId = man.getPsnId();
    Date tempDate = man.getControlTime();
    SieBhCitation sieBhCitation = new SieBhCitation();
    sieBhCitation.setIp(ip);
    sieBhCitation.setType(type);
    sieBhCitation.setCreDate(tempDate);// 创建时间
    sieBhCitation.setFmtDate(tempDate.getTime());// 格式化创建时间
    sieBhCitation.setKeyId(typeId);
    sieBhCitation.setCitePsnId(psnId);
    sieBhCitation.setIpCountry(man.getIpCountry());
    sieBhCitation.setIpProv(man.getIpProv());
    sieBhCitation.setIpCity(man.getIpCity());
    sieBhCitationDao.save(sieBhCitation);
    // 记录机器人的操作至日志表中
    recoredRobotLog(man.getIp(), sieBhCitation.getId(), 4L);
  }

  /**
   * 记录操作日志
   * 
   * @param ip 机器人的IP地址
   * @param keyCode bh_表主键
   * @param keyType
   *        业务类型：1阅读，对应bh_read表，2分享，对应bh_share表，3下载，对应bh_download表，4引用，对应bh_citation表，8主页查看，9主页分享对应bh_index_share表
   */
  private void recoredRobotLog(String ip, Long keyCode, Long keyType) {
    SieRobotManReflushLog sieRobotManReflushLog = new SieRobotManReflushLog();
    sieRobotManReflushLog.setIp(ip);
    sieRobotManReflushLog.setKeyCode(keyCode);
    sieRobotManReflushLog.setKeyType(keyType);
    sieRobotManReflushLog.setCreateTime(new Date());
    sieRobotManReflushLogDao.save(sieRobotManReflushLog);
  }

  /**
   * 获取一定概率是否成功
   * 
   * @param floadPercent 百分比的概率数,值为0~100
   */
  private boolean getSuccessByProbability(int tempPercent) {
    int percent = tempPercent > 100 ? 100 : tempPercent < 0 ? 0 : tempPercent;
    boolean flag;
    int randomInt = RandomUtils.nextInt(0, 100);
    // 如果结果大于这个数，则失败
    if (randomInt > percent) {
      flag = false;
    } else {
      flag = true;
    }
    return flag;
  }

  /**
   * 是否需要继续运行
   */
  private boolean robotIsAllowExecution(Integer configId) {
    boolean result = false;
    SieRobotManConfig sieRobotManConfig = sieRobotManConfigDao.getNormalModel(configId);
    if (sieRobotManConfig != null) {
      result = true;
    }
    return result;
  }
}
