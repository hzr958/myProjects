package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.grp.GrpPubs;
import com.smate.center.task.model.sns.quartz.GrpFile;
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

  /**
   * 查找今天上传文件 ， 课件的群组id
   * 
   * @return
   */
  public List<Long> findTodayUploadFileGrpId() {
    String hql =
        "select     distinct  t.grpId  from GrpFile t where t.uploadDate>trunc(sysdate) and t.uploadDate<trunc(sysdate+1)  "
            + "  and t.fileStatus = 0 and (t.fileModuleType =0 or t.fileModuleType =2 )  "
            + "  and   not exists (select   e.grpId from  EmailGrpFilePsn  e where e.createDate >  trunc(sysdate) and e.createDate<trunc(sysdate+1)  and t.grpId = e.grpId   ) ";
    return super.createQuery(hql).list();
  }


  /**
   * 获取该群组，当天上传的第一个文件，或者课件 成果
   * 
   * @param grpId
   * @return
   */
  public GrpFile findTodayUplaodGrpPubsByGrpId(Long grpId) {
    String hql =
        " from GrpFile t where    t.grpId =:grpId and  t.uploadDate>trunc(sysdate) and t.uploadDate<trunc(sysdate+1)   "
            + "  and t.fileStatus = 0 and (t.fileModuleType =0 or t.fileModuleType =2 )   order by  t.uploadDate asc ";
    List list = this.createQuery(hql).setParameter("grpId", grpId).list();
    if (list != null && list.size() > 0) {
      return (GrpFile) list.get(0);
    }
    return null;
  }
}
