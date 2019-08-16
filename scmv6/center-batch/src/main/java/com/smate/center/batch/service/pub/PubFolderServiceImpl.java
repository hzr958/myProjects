package com.smate.center.batch.service.pub;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.PubFolderDao;
import com.smate.center.batch.dao.sns.pub.PubFolderItemsDao;
import com.smate.center.batch.dao.sns.pub.PublicationDao;
import com.smate.center.batch.form.pub.PubFolderForm;
import com.smate.center.batch.model.sns.pub.PubFolder;
import com.smate.center.batch.model.sns.pub.PubFolderItems;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 成果/文献文件夹管理.
 * 
 * @author lqh
 * 
 */
@Service("pubFolderService")
@Transactional(rollbackFor = Exception.class)
public class PubFolderServiceImpl implements PubFolderService {

  /**
   * 
   */

  private static final long serialVersionUID = -6688221892018827018L;
  private static final Logger LOGGER = LoggerFactory.getLogger(PubFolderServiceImpl.class);
  @Autowired
  private PubFolderDao pubFolderDao;
  @Autowired
  private PubFolderItemsDao pubFolderItemsDao;
  @Autowired
  private PublicationStatisticsService publicationStatisticsService;
  @Autowired
  private PublicationDao publicationDao;

  /**
   * 获取单个文件夹.
   * 
   * @see com.iris.scm.scmweb.service.forder.FolderManagerImpl#getFolder()
   */
  public PubFolder getPubFolder(Long folderId) {
    try {
      return this.pubFolderDao.getPubFolderById(folderId);
    } catch (Exception e) {
      LOGGER.error("保存成果文件夹失败");
    }
    return null;
  }

  /**
   * 保存新创建的文件夹.
   * 
   * @see com.iris.scm.scmweb.service.forder.FolderManagerImpl#saveFolder(com.iris.scm.scmweb.model.folder.Folder)
   */
  public PubFolderForm savePubFolder(PubFolderForm pubFolderForm) {

    try {
      PubFolder pubFolder = this.pubFolderDao.getFolderByName(pubFolderForm.getFolderName(),
          SecurityUtils.getCurrentUserId(), pubFolderForm.getArticleType());
      // 该文件夹名已经存在
      if (pubFolder != null) {
        pubFolderForm.setResult("exist");
        return pubFolderForm;
      }
      pubFolder = new PubFolder();
      pubFolder.setArticleType(pubFolderForm.getArticleType());
      pubFolder.setCreateDate(new Date());
      pubFolder.setEnabled(1);
      pubFolder.setName(pubFolderForm.getFolderName());
      pubFolder.setPsnId(SecurityUtils.getCurrentUserId());
      Long folderId = this.pubFolderDao.addPubFolder(pubFolder);
      pubFolderForm.setId(folderId);
    } catch (Exception e) {
      LOGGER.error("创建文件夹失败");
    }
    return pubFolderForm;
  }

  /**
   * 删除文件夹.
   * 
   * @param PubFolderId
   * @ @see
   *   com.iris.scm.scmweb.service.PubFolderServiceImpl.PubFolderManagerImpl#deletePubFolderById(Long
   *   PubFolderId)
   */
  public void deletePubFolderById(Long folderId) {
    try {
      PubFolder folder = this.pubFolderDao.getPubFolderById(folderId);
      if (folder != null) {
        this.pubFolderItemsDao.removePublicationByFolderId(folderId);
        this.pubFolderDao.removePubFolder(folder);
        // 清除统计缓存
        publicationStatisticsService.clearPubFolderStatistic(SecurityUtils.getCurrentUserId(), folder.getArticleType());
      }
    } catch (Exception e) {
      LOGGER.error("删除成果文件夹失败");
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.service.folder.PubFolderService#findPubFolderByPsnIds
   * (java.lang.Integer)
   */
  @Override
  public List<PubFolder> findPubFolderByPsnIds(Integer articleType) {
    try {
      return this.pubFolderDao.getPubFolderByPsnId(SecurityUtils.getCurrentUserId(), articleType);
    } catch (Exception e) {
      LOGGER.error("查询文件列表失败");
    }
    return null;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Map findCurrPsnPubFolders(Integer articleType) {
    try {
      Map map = new HashMap();
      List<PubFolder> list = this.pubFolderDao.getPubFolderByPsnId(SecurityUtils.getCurrentUserId(), articleType);
      for (PubFolder pubFolder : list) {
        pubFolder.setCount(this.publicationDao.getFolderPubNum(pubFolder.getId()));
      }
      map.put("list", list);
      map.put("notClassifiedCount",
          this.publicationDao.getNoFolderPubNum(SecurityUtils.getCurrentUserId(), articleType));
      return map;
    } catch (Exception e) {
      LOGGER.error("查询文件列表失败");
      throw e;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.service.folder.PubFolderService#saveEditPubFolder
   * (com.iris.scm.scmweb.model.folder.PubFolder)
   */
  @Override
  public PubFolder saveEditPubFolder(PubFolderForm pubFolderForm) {
    try {
      PubFolder pubFolder = this.pubFolderDao.getFolderByName(pubFolderForm.getFolderName(),
          SecurityUtils.getCurrentUserId(), pubFolderForm.getArticleType());
      // 该文件夹名已经存在
      if (pubFolder != null) {
        return null;
      } else {
        pubFolder = this.pubFolderDao.getPubFolderById(Long.valueOf(pubFolderForm.getFolderId()));
        pubFolder.setName(pubFolderForm.getFolderName());
        this.pubFolderDao.updatePubFolder(pubFolder);
        return pubFolder;
      }

    } catch (Exception e) {
      LOGGER.error("更新文件夹信息失败");
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.iris.scm.scmweb.service.folder.PubFolderService#
   * getPubFolderItemsByFolderId(java.lang.Long)
   */
  @Override
  public List<PubFolderItems> getPubFolderItemsByFolderId(Long folderId) {
    try {
      return this.pubFolderItemsDao.getPubFolderItemsById(folderId);
    } catch (Exception e) {
      LOGGER.error("查询文件夹的成果失败");
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.service.folder.PubFolderService#isPublicationInPubFolder
   * (java.lang.Long, java.lang.Long)
   */
  @Override
  public Boolean isPublicationInPubFolder(Long folderId, Long pubId) {
    long count = 0;
    try {
      count = this.pubFolderItemsDao.getStatByPubId(folderId, pubId);
      return count > 0 ? true : false;
    } catch (Exception e) {
      LOGGER.error("查询成果是否在文件夹中失败==isPublicationInPubFolder");
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.iris.scm.scmweb.service.folder.PubFolderService#
   * removePublicationFromPubFolder(java.lang.Long, java.lang.Long)
   */
  @Override
  public void removePublicationFromPubFolder(Long folderId, Long pubId) {
    try {
      PubFolderItems pubFolderItems = this.pubFolderItemsDao.getPubFolderItems(folderId, pubId);
      this.pubFolderItemsDao.removePublicationFromPubFolder(pubFolderItems);
      if (pubFolderItems.getPubFolder() != null) {
        // 清除统计缓存
        publicationStatisticsService.clearPubFolderStatistic(SecurityUtils.getCurrentUserId(),
            pubFolderItems.getPubFolder().getArticleType());
      }
    } catch (Exception e) {
      LOGGER.error("删除文件夹中的成果失败");
    }
  }

  @Override
  public void removePublicationFromPubFolder(String pubIds, Integer articleType) {
    this.pubFolderItemsDao.removePublicationFromPubFolder(pubIds);
    // 清除统计缓存 2015-11-11 不执行缓存
    // publicationStatisticsService.clearPubFolderStatistic(SecurityUtils.getCurrentUserId(),
    // articleType);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public List<Map<String, Object>> changePubFolders(Object[] pubIds, int articleType) {
    try {
      List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
      List objArr = pubFolderItemsDao.changePubFolders(SecurityUtils.getCurrentUserId(), pubIds, articleType);
      for (Object objects : objArr) {
        Map objMap = (Map) objects;
        Map map = new HashMap();
        map.put("id", objMap.get("FOLDER_ID"));
        int count = NumberUtils.toInt(ObjectUtils.toString(objMap.get("COUNT(1)")));
        map.put("flag", count == pubIds.length);
        mapList.add(map);
      }
      return mapList;
    } catch (Exception e) {
      LOGGER.error("编辑标签检查出错", e);
    }
    return null;
  }

}
