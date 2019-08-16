package com.smate.core.base.file.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 文件dao
 * 
 * @author tsz
 *
 */
@Repository
public class ArchiveFileDao extends SnsHibernateDao<ArchiveFile, Long> {
  /**
   * 查询附件.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public ArchiveFile findArchiveFileById(Long id) {
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
    return (Long) super.createQuery("select t.createPsnId from ArchiveFile t where t.fileId=?", id).uniqueResult();
  }

  /**
   * 获取文件名
   * 
   * @param fileId
   * @return
   */
  public String getArchiveFileName(Long fileId) {
    String sql = "select t.FILE_NAME from ARCHIVE_FILES t where t.FILE_ID=:fileId";
    return (String) super.getSession().createSQLQuery(sql).setParameter("fileId", fileId).uniqueResult();
  }

  /**
   * 当前个人库已经存在在群组中的id
   */
  public List<Long> getGrpPsnFileIdList(Long grpId) {
    String HQL = "select g.archiveFileId from GrpFile g where g.grpId=:grpId and g.fileStatus = 0";
    return getSession().createQuery(HQL).setParameter("grpId", grpId).list();
  }

  /**
   * 根据ID列表值查找附件列表.
   * 
   * @param ids
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ArchiveFile> getArchiveFileByIds(List<Long> ids) {

    if (CollectionUtils.isEmpty(ids)) {
      return null;
    }
    return super.createQuery("from ArchiveFiles a where a.fileId in (:ids) order by id asc")
        .setParameterList("ids", ids).list();

  }
}
