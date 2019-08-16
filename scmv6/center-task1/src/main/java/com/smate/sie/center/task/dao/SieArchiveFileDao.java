package com.smate.sie.center.task.dao;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SieArchiveFile;

/**
 * 文件dao
 * 
 * @author sjzhou
 *
 */
@Repository
public class SieArchiveFileDao extends SieHibernateDao<SieArchiveFile, Long> {

  /**
   * 查询附件.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public SieArchiveFile findArchiveFilesById(Long id) {

    return super.findUniqueBy("fileId", id);
  }

  /**
   * 查询附件.
   * 
   * @param id
   * @return
   * @throws Exception
   */
  public SieArchiveFile findArchiveFileById(Long id) throws Exception {
    return super.findUniqueBy("fileId", id);
  }

  public void deleteArchiveFileByFileId(Long id) throws Exception {
    if (id == null) {
      return;
    }
    String hql = "delete from SieArchiveFile where fileId = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  public SieArchiveFile findArchiveFileByFileId(Long id) throws Exception {
    if (id == null) {
      return null;
    }
    String hql = "from SieArchiveFile where fileId=?";
    return (SieArchiveFile) super.createQuery(hql, id).uniqueResult();
  }

  /**
   * 查询附件的所有人.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public Long queryArchiveFileOwner(Long id) {
    return (Long) super.createQuery("select t.createPsnId from SieArchiveFile t where t.fileId=?", id).uniqueResult();
  }
}
