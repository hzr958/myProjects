package com.smate.center.task.dao.sns.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.quartz.SnsArchiveFiles;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 文件dao
 * 
 * @author tsz
 *
 */
@Repository
public class SnsArchiveFilesDao extends SnsHibernateDao<SnsArchiveFiles, Long> {

  /**
   * 查询附件.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public SnsArchiveFiles findArchiveFileById(Long id) {
    return super.findUniqueBy("fileId", id);
  }

  /**
   * 查询附件的所有人.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public Long queryArchiveFileOwner(Long id) {
    return (Long) super.createQuery("select t.createPsnId from SnsArchiveFiles t where t.fileId=?", id).uniqueResult();
  }
}
