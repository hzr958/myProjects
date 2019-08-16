package com.smate.web.file.dao.rol;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.web.file.model.rol.RolArchiveFile;

/**
 * ROL文件
 * 
 * @author zx
 *
 */
@Repository
public class RolArchiveFileDao extends RolHibernateDao<RolArchiveFile, Long> {

  public RolArchiveFile findArchiveFilesById(Long id) throws Exception {
    return super.findUniqueBy("fileId", id);
  }

}
