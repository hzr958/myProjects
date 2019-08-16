package com.smate.core.base.file.dao;

import org.springframework.stereotype.Repository;

import com.smate.core.base.file.model.Sie6ArchiveFile;
import com.smate.core.base.utils.data.SieHibernateDao;

/**
 * 文件dao
 * 
 * @author sjzhou
 *
 */
@Repository
public class Sie6ArchiveFileDao extends SieHibernateDao<Sie6ArchiveFile, Long> {

  /**
   * 查询附件.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public Sie6ArchiveFile findArchiveFilesById(Long id) {

    return super.findUniqueBy("fileId", id);
  }

  /**
   * 查询附件.
   * 
   * @param id
   * @return
   * @throws Exception
   */
  public Sie6ArchiveFile findArchiveFileById(Long id) throws Exception {
    return super.findUniqueBy("fileId", id);
  }

  public void deleteArchiveFileByFileId(Long id) throws Exception {
    if (id == null) {
      return;
    }
    String hql = "delete from Sie6ArchiveFile where fileId = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  public Sie6ArchiveFile findArchiveFileByFileId(Long id) throws Exception {
    if (id == null) {
      return null;
    }
    String hql = "from SieArchiveFile where fileId=?";
    return (Sie6ArchiveFile) super.createQuery(hql, id).uniqueResult();
  }

  /**
   * 查询附件的所有人.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public Long queryArchiveFileOwner(Long id) {
    return (Long) super.createQuery("select t.createPsnId from Sie6ArchiveFile t where t.fileId=?", id).uniqueResult();
  }
}
