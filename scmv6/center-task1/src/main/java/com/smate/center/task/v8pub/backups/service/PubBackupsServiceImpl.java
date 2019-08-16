package com.smate.center.task.v8pub.backups.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.v8pub.backups.dao.PubDataBackupsDao;
import com.smate.center.task.v8pub.backups.model.PubDataBackups;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDetailDAO;
import com.smate.center.task.v8pub.dao.sns.PubSnsDetailDAO;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;

/**
 * 成果备份处理的实现
 * 
 * @author YJ
 *
 *         2018年11月15日
 */
@Service(value = "pubBackupsService")
@Transactional(rollbackFor = Exception.class)
public class PubBackupsServiceImpl implements PubBackupsService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubDataBackupsDao pubDataBackupsDao;
  @Autowired
  private PubSnsDetailDAO pubSnsDetailDAO;
  @Autowired
  private PubSplitService pubSplitService;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Autowired
  private PdwhSplitService pdwhSplitService;


  @Override
  public List<PubDataBackups> findPubNeedDeal(Integer SIZE, Integer dataType) throws ServiceException {
    return pubDataBackupsDao.findPubBySize(SIZE, dataType);
  }

  @Override
  public void save(PubDataBackups pubData) throws ServiceException {
    try {
      pubData.setGmtModified(new Date());
      pubDataBackupsDao.saveOrUpdate(pubData);
    } catch (Exception e) {
      logger.error("保存备份记录表出错！", pubData);
      throw new ServiceException(e);
    }
  }

  @Override
  public void backupSnsData(PubDataBackups pubData) throws ServiceException {
    try {
      if (pubData != null && !NumberUtils.isNullOrZero(pubData.getPubId())) {
        // 先根据pubId获取到mongodb中的数据
        PubSnsDetailDOM pubSnsDetailDOM = pubSnsDetailDAO.findByPubId(pubData.getPubId());
        pubSplitService.backUpSnsPubJson(pubSnsDetailDOM);
        pubSplitService.backUpSnsPubDoi(pubSnsDetailDOM);
        pubSplitService.backUpSnsPubJournal(pubSnsDetailDOM);
        pubSplitService.backUpSnsPubPatent(pubSnsDetailDOM);
        pubData.setStatus(1);
      } else {
        pubData.setStatus(99);
        pubData.setErrorMsg("V_PUB_SNS_DETAIL(monogdb)中没有数据");
      }
      // 保存记录表数据
      save(pubData);
    } catch (Exception e) {
      logger.error("备份Sns库数据出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void backupPdwhData(PubDataBackups pubData) throws ServiceException {
    try {
      if (pubData != null && !NumberUtils.isNullOrZero(pubData.getPubId())) {
        Long pdwhPubId = pubData.getPubId();
        // 先根据pubId获取到mongodb中的数据
        PubPdwhDetailDOM pubPdwhDetailDOM = pubPdwhDetailDAO.findByPubId(pdwhPubId);
        pdwhSplitService.backUpPdwhPubJson(pubPdwhDetailDOM);
        pdwhSplitService.backUpPdwhPubDoi(pubPdwhDetailDOM);
        pdwhSplitService.backUpPdwhPubIns(pubPdwhDetailDOM);
        pdwhSplitService.backUpPdwhPubJournal(pubPdwhDetailDOM);
        pdwhSplitService.backUpPdwhPubPatent(pubPdwhDetailDOM);
        pubData.setStatus(1);
      } else {
        pubData.setStatus(99);
        pubData.setErrorMsg("V_PUB_PDWH_DETAIL(monogdb)中没有数据");
      }
      // 保存记录表数据
      save(pubData);
    } catch (Exception e) {
      logger.error("备份Pdwh库数据出错！", e);
      throw new ServiceException(e);
    }
  }

}
