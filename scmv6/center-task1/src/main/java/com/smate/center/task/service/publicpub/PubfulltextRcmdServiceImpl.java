package com.smate.center.task.service.publicpub;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gdata.util.ServiceException;
import com.smate.center.task.dao.publicpub.PubFulltextUploadLogDao;
import com.smate.center.task.dao.sns.quartz.ArchiveFileDao;
import com.smate.center.task.dao.sns.quartz.PubFulltextPsnRcmdDao;
import com.smate.center.task.model.publicpub.PubFulltextUploadLog;
import com.smate.center.task.model.sns.quartz.ArchiveFile;
import com.smate.center.task.model.sns.quartz.PubFulltextPsnRcmd;
import com.smate.center.task.v8pub.dao.sns.PubFullTextDAO;
import com.smate.center.task.v8pub.dao.sns.PubSnsDAO;
import com.smate.center.task.v8pub.sns.po.PubFullTextPO;
import com.smate.center.task.v8pub.sns.po.PubSnsPO;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;

@Service("pubfulltextRcmdService")
@Transactional(rollbackFor = Exception.class)
public class PubfulltextRcmdServiceImpl implements PubfulltextRcmdService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubFulltextUploadLogDao pubFulltextUploadLogDao;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubFulltextPsnRcmdDao pubFulltextPsnRcmdDao;
  @Autowired
  private PubFullTextDAO pubFullTextDAO;
  @Autowired
  private ArchiveFileDao archiveFileDao;

  @Override
  public List<PubFulltextUploadLog> getNeedRcmdData() {
    return pubFulltextUploadLogDao.getNeedRcmdData();
  }

  @Override
  public Long getPdwhPubId(Long snsPubId) {
    return pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(snsPubId);
  }

  @Override
  public List<Long> getSnsPubId(Long pdwhPubId, Long snsPsnId) {
    return pubPdwhSnsRelationDAO.getSnsPubIdsByPdwhId(pdwhPubId, snsPsnId);
  }

  @Override
  public void savePubFulltextPsnRcmd(List<Long> snsPubIds, Long srcPubId, int dbid, PubFulltextUploadLog uploadLog)
      throws ServiceException {
    List<PubSnsPO> pubList = pubSnsDAO.getSnsPubList(snsPubIds);// 排除群组成果
    try {
      for (PubSnsPO pubSnsPO : pubList) {
        // 保存推荐记录前需要判断有没有全文。有全文就不推荐全文
        PubFullTextPO fulltext = pubFullTextDAO.getPubFullTextByPubId(pubSnsPO.getPubId());
        if (fulltext != null) {
          pubFulltextPsnRcmdDao.deletePubFulltextPsnRcmd(pubSnsPO.getPubId());
        } else {
          PubFulltextPsnRcmd psnRcmd = pubFulltextPsnRcmdDao.getPsnRcmd(pubSnsPO.getPubId(), dbid, srcPubId);
          // 获取文件大小
          ArchiveFile archiveFile = archiveFileDao.get(uploadLog.getFulltextFileId());
          if (archiveFile == null) {
            continue;
          }
          Long fileSize = archiveFile.getFileSize();
          // 过滤掉相同文件 (同一个文件Id或者相同文件大小)
          List<PubFulltextPsnRcmd> repeatPsnRcmds = pubFulltextPsnRcmdDao.getRepeatPsnRcmd(pubSnsPO.getPubId(),
              pubSnsPO.getCreatePsnId(), uploadLog.getFulltextFileId(), fileSize);
          // 有重复记录的删除 另起一个事务删除
          if (CollectionUtils.isNotEmpty(repeatPsnRcmds)) {
            if (dbid == 1) {// 删除所有
              repeatPsnRcmds.forEach(repeatPsnRcmd -> {
                pubFulltextPsnRcmdDao.deleteWithNewTs(repeatPsnRcmd.getId());
              });
            }
            if (dbid == 0) {
              repeatPsnRcmds.forEach(repeatPsnRcmd -> {
                // 基准库的不删除
                if (repeatPsnRcmd.getDbId() == 0)
                  pubFulltextPsnRcmdDao.deleteWithNewTs(repeatPsnRcmd.getId());
              });
            }
          }
          if (psnRcmd != null) {
            // 已经拒绝了的不推荐
            if (psnRcmd.getStatus() != 2) {
              psnRcmd.setFulltextFileId(uploadLog.getFulltextFileId());
              psnRcmd.setStatus(0);
              psnRcmd.setFileSize(fileSize);
            }
          } else {
            psnRcmd = new PubFulltextPsnRcmd(pubSnsPO.getPubId(), pubSnsPO.getCreatePsnId(),
                uploadLog.getFulltextFileId(), dbid, 3, 0, new Date(), srcPubId, uploadLog.getUploadPsnId(), fileSize);
          }
          pubFulltextPsnRcmdDao.saveOrUpdateWithNewTs(psnRcmd);
        }
      }
    } catch (Exception e) {
      logger.error("全文推荐数据保存出错---", e);
    }

  }

  @Override
  public void updateLogStatus(Long id, int status) {
    pubFulltextUploadLogDao.updateStatus(id, status);
  }

  @Override
  public void deleteFulltextPsnRcmd(Long srcPubId) {
    pubFulltextPsnRcmdDao.deleteFulltextPsnRcmd(srcPubId);
  }

  @Override
  public PubFulltextPsnRcmd getFulltextRcmd(Long snsPubId) {

    return pubFulltextPsnRcmdDao.getFulltextRcmd(snsPubId);
  }

  @Override
  public void deleteFulltextRcmd(Long snsPubId) throws Exception {
    pubFulltextPsnRcmdDao.deletePubFulltextPsnRcmd(snsPubId);

  }

  @Override
  public PubSnsPO getSnsPubById(Long snsPubId) {
    return pubSnsDAO.getSnsPubById(snsPubId);
  }

}
