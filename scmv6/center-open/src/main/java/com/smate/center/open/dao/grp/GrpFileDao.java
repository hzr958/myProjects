package com.smate.center.open.dao.grp;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.grp.GrpFile;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组文件dao
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class GrpFileDao extends SnsHibernateDao<GrpFile, Long> {

  /**
   * 判断是不是我的文件
   * 
   * @param grpId
   * @param psnId
   * @param grpFileId
   * @return
   */
  public boolean checkIsMyGrpFile(Long grpId, Long psnId, Long grpFileId) {
    String hql =
        " select gf.grpFileId from  GrpFile gf where gf.grpId =:grpId  and  gf.uploadPsnId=:psnId and gf.grpFileId=:grpFileId  and gf.fileStatus=0";
    Object ojb = this.createQuery(hql).setParameter("grpId", grpId).setParameter("psnId", psnId)
        .setParameter("grpFileId", grpFileId).uniqueResult();
    if (ojb != null) {
      return true;
    }
    return false;
  }

}
