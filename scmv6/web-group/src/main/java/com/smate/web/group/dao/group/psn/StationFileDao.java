package com.smate.web.group.dao.group.psn;

import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.StationFile;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * 我的文件库Dao
 * 
 * @author ajb
 *
 */
@Repository
public class StationFileDao extends SnsHibernateDao<StationFile, Long> {

  /**
   * 检查我的文件是否存在
   * 
   * @param psnId
   * @param archiveFileId
   * @return
   */
  public Boolean checkMyFileExit(Long psnId, Long archiveFileId) {
    String hql =
        "select  sf.fileId   from StationFile where sf.psnId =:psnId and  sf.archiveFileId =:archiveFileId and sf.fileStatus = 0  ";
    Object obj =
        this.createQuery(hql).setParameter("psnId", psnId).setParameter("archiveFileId", archiveFileId).uniqueResult();
    if (obj != null) {
      return true;
    }
    return false;
  }

  /**
   * 查找我的文件
   * 
   * @param psnId
   * @param archiveFileId
   * @return
   */
  public StationFile findMyFile(Long psnId, Long archiveFileId) {
    String hql = " from StationFile  sf where sf.psnId =:psnId and  sf.archiveFileId =:archiveFileId    ";
    Object obj =
        this.createQuery(hql).setParameter("psnId", psnId).setParameter("archiveFileId", archiveFileId).uniqueResult();
    if (obj != null) {
      return (StationFile) obj;
    }
    return null;
  }
}
