package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.ArchiveFile;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 附件Dao
 * 
 * @author zk
 * @since 6.0.1
 */
@Repository
public class ArchiveFileDao extends SnsHibernateDao<ArchiveFile, Long> {
  public ArchiveFile findArchiveFileById(Long id) throws Exception {
    return super.findUniqueBy("fileId", id);
  }

  /**
   * 根据文件Id更新文件名
   * 
   * @param fileId
   * @param fileName
   */
  public void updateFileNameById(Long fileId, String fileName) {
    String hql = "update ArchiveFile set fileName=:fileName where fileId=:fileId";
    super.createQuery(hql).setParameter("fileName", fileName).setParameter("fileId", fileId).executeUpdate();
  }

  public List<ArchiveFile> batchGetEmptyFile(Integer size) {
    String hql = "from ArchiveFiles where fileSize is null or fileSize = 0";
    return super.createQuery(hql).setMaxResults(size).list();

  }

}
