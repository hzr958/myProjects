package com.smate.center.merge.dao.grp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.merge.model.sns.grp.GrpFile;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组文件dao
 * 
 * @author AiJiangBin
 */

@Repository
public class GrpFileDao extends SnsHibernateDao<GrpFile, Long> {


  /**
   * 获取我的所有群组的文件
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GrpFile> getGrpFileByPsnId(Long psnId) throws Exception {
    String hql =
        " from GrpFile t where  t.uploadPsnId=:psnId and t.fileStatus=0 and  exists(select t1.grpId from GrpBaseinfo t1 where t1.grpId=t.grpId and t1.status='01' )";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

}
