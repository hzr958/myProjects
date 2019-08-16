package com.smate.sie.center.task.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.ArchiveFileDao;
import com.smate.center.task.model.sns.quartz.ArchiveFile;
import com.smate.center.task.v8pub.dao.sns.PubFullTextDAO;
import com.smate.center.task.v8pub.sns.po.PubFullTextPO;
import com.smate.sie.center.task.dao.SieArchiveFileDao;
import com.smate.sie.center.task.dao.SieDynMsgDao;
import com.smate.sie.center.task.dao.SiePubSyncFulltextRefreshDao;
import com.smate.sie.center.task.dao.SieSnsPubFulltextDao;
import com.smate.sie.center.task.model.SieArchiveFile;
import com.smate.sie.center.task.model.SiePubSyncFulltextRefresh;
import com.smate.sie.center.task.model.SieSnsPubFulltext;

/**
 * 
 * @author yxy
 *
 */
@Service("sieSynSnsPubFulltextService")
@Transactional(rollbackFor = Exception.class)
public class SieSynSnsPubFulltextServiceImpl implements SieSynSnsPubFulltextService {
  @Autowired
  private SieDynMsgDao sieDynMsgDao;
  @Autowired
  private SiePubSyncFulltextRefreshDao siePubSyncFulltextRefreshDao;
  @Autowired
  private PubFullTextDAO snsPubFullTextDAO;
  @Autowired
  private SieSnsPubFulltextDao siePubFulltextDao;
  @Autowired
  private SieArchiveFileDao sieArchiveFileDao;
  @Autowired
  private ArchiveFileDao snsArchiveFileDao;

  public int getMaxDynPubNum() {
    return sieDynMsgDao.getPubSize().intValue();
  }

  /**
   * 根据刷新表同步全文即相关信息
   */
  @Override
  public void synNewFulltext(SiePubSyncFulltextRefresh refresh) {
    Long snsPubId = refresh.getSnsPubId();
    Long sieFulltextId = refresh.getFulltextId();
    PubFullTextPO snsPubFullTextPO = snsPubFullTextDAO.getPubFullTextByPubId(snsPubId);
    // 如果sns全文不存在,则把刷新表的状态设置为0
    if (snsPubFullTextPO == null) {
      refresh.setFulltextId(0L);
      refresh.setStatus(1);
      refresh.setUpdateTime(new Date());
      siePubSyncFulltextRefreshDao.save(refresh);
      return;
    }
    SieSnsPubFulltext siePubFulltext = siePubFulltextDao.get(sieFulltextId);
    Long snsFileId = snsPubFullTextPO.getFileId();
    // 如果sie全文是空的，则插入数据
    if (siePubFulltext == null) {
      // 同步文件数据
      syncSnsArchiveFile(snsFileId);
      // 同步全文相关信息
      siePubFulltext = new SieSnsPubFulltext();
      siePubFulltext.setPubId(snsPubId);
      siePubFulltext.setFulltextImagePath(snsPubFullTextPO.getThumbnailPath());
      siePubFulltext.setFulltextFileId(snsFileId);
      siePubFulltextDao.save(siePubFulltext);
      // 更新刷新表的全文
      refresh.setFulltextId(snsPubId);
      refresh.setStatus(1);
      refresh.setUpdateTime(new Date());
      siePubSyncFulltextRefreshDao.save(refresh);
    } else {
      Long sieFulltextFileId = siePubFulltext.getFulltextFileId();
      // 如果全文不是空的,则比较全文对应的文件id是否发生了变化
      if (!snsFileId.equals(sieFulltextFileId)) {
        sieArchiveFileDao.delete(sieFulltextFileId);
        syncSnsArchiveFile(snsFileId);
        siePubFulltext.setFulltextFileId(snsFileId);
        siePubFulltextDao.save(siePubFulltext);
      }
      // 判断缩略图的变化,如果缩略图发生了变化，则取最新的缩略图
      String snsImgPath = snsPubFullTextPO.getThumbnailPath();
      snsImgPath = snsImgPath == null ? "" : snsImgPath.trim();
      String sieImgPath = siePubFulltext.getFulltextImagePath();
      sieImgPath = sieImgPath == null ? "" : sieImgPath.trim();
      if (!sieImgPath.equals(snsImgPath)) {
        siePubFulltext.setFulltextImagePath(snsImgPath);
        siePubFulltextDao.save(siePubFulltext);
      }
    }
  }


  @Override
  public void syncSnsPubToSie(long batchSize) {
    // 动态中的成果
    List<Long> dynPublist = sieDynMsgDao.getPubList();
    for (int i = 0; i < batchSize && i < dynPublist.size(); i++) {
      Long snsPubId = dynPublist.get(i);
      syncDataToSiePubSyncFulltextRefresh(snsPubId);
    }
  }

  /**
   * 根据snsPubId同步数据到刷新表中
   */
  private void syncDataToSiePubSyncFulltextRefresh(Long snsPubId) {
    // 处理未同步的数据
    SiePubSyncFulltextRefresh siePubSyncFulltextRefresh = siePubSyncFulltextRefreshDao.getBySnsPubId(snsPubId);
    if (siePubSyncFulltextRefresh == null) {
      siePubSyncFulltextRefresh = new SiePubSyncFulltextRefresh();
      siePubSyncFulltextRefresh.setSnsPubId(snsPubId);
      siePubSyncFulltextRefresh.setStatus(0);
      siePubSyncFulltextRefresh.setUpdateTime(new Date());
      siePubSyncFulltextRefreshDao.save(siePubSyncFulltextRefresh);
    }
  }

  /**
   * 同步SNS全文文件数据到SIE文件表中
   */
  private void syncSnsArchiveFile(Long snsFileId) {
    ArchiveFile snsArchiveFile = snsArchiveFileDao.get(snsFileId);
    // 获取SIE中相关文件信息，用于比较全文更新时间
    SieArchiveFile sieArchiveFile = sieArchiveFileDao.get(snsFileId);
    if (sieArchiveFile == null) {
      sieArchiveFile = new SieArchiveFile();
      sieArchiveFile.setFileId(snsFileId);
      sieArchiveFile.setCreatePsnId(snsArchiveFile.getCreatePsnId());
      sieArchiveFile.setCreateTime(snsArchiveFile.getCreateTime());
      sieArchiveFile.setFileName(snsArchiveFile.getFileName());
      sieArchiveFile.setFilePath(snsArchiveFile.getFilePath());
      sieArchiveFile.setInsId(snsArchiveFile.getInsId());
      sieArchiveFile.setNodeId(snsArchiveFile.getNodeId());
      sieArchiveFile.setFileUUID(snsArchiveFile.getFileUUID());
      sieArchiveFile.setFileSize(snsArchiveFile.getFileSize());
      sieArchiveFile.setStatus(snsArchiveFile.getStatus());
      sieArchiveFileDao.save(sieArchiveFile);
    }
  }

  @Override
  public List<SiePubSyncFulltextRefresh> syncSnsPubOnSie(int firstPage, long batchSize) {
    return siePubSyncFulltextRefreshDao.getByTypeOnSize(firstPage, Long.valueOf(batchSize).intValue());
  }

}
