package com.smate.web.group.dao.grp.label;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.grp.label.GrpFileLabel;


/**
 * 群组文件标签dao
 * 
 * @author aijiangbin
 *
 */
@Repository
public class GrpFileLabelDao extends SnsHibernateDao<GrpFileLabel, Long> {

  /**
   * 删除群组文件的标签 , 通过标签id删除
   * 
   * @param currentPsnId
   * @param labelId
   * @return
   */
  public Integer deleteGrpFileLabel(Long currentPsnId, Long grpLabelId) {
    String hql = "update GrpFileLabel t set t.status=1 , t.updatePsnId =:updatePsnId , t.updateDate =:updateDate"
        + " where t.grpLabelId =:grpLabelId  and t.status = 0 ";
    int result = this.createQuery(hql).setParameter("updatePsnId", currentPsnId).setParameter("updateDate", new Date())
        .setParameter("grpLabelId", grpLabelId).executeUpdate();

    return result;
  }

  /**
   * 删除群组文件的标签 , 通过 文件标签id主键删除
   * 
   * @param currentPsnId
   * @param labelId
   * @param grpFileId
   * @return
   */
  public Integer deleteGrpFileLabelById(Long currentPsnId, Long filelabelId) {
    String hql = "update GrpFileLabel t set t.status=1 , t.updatePsnId =:updatePsnId , t.updateDate =:updateDate"
        + " where t.id =:filelabelId  and t.status = 0 ";
    int result = this.createQuery(hql).setParameter("updatePsnId", currentPsnId).setParameter("updateDate", new Date())
        .setParameter("filelabelId", filelabelId).executeUpdate();

    return result;
  }

  /**
   * 查找群组文件的标签集合
   * 
   * @param grpFileId
   * @return
   */
  public List<GrpFileLabel> findLabelByFileId(Long grpFileId) {
    String hql = " from GrpFileLabel t where t.status = 0 and t.grpFileId =:grpFileId   order by t.id asc";
    List list = this.createQuery(hql).setParameter("grpFileId", grpFileId).list();
    return list;
  }

  /**
   * 统计文件标签的信息
   * 
   * @param grpFileId
   * @param fileModuleType
   * @return
   */
  public Integer statisticalFileLabelCount(Long grpLabelId, Long grpId, Integer fileModuleType) {
    String hql = "select count(1) from  GrpFileLabel t   where t.status = 0  and t.grpLabelId=:grpLabelId"
        + "  and  exists  (select gf.grpFileId  from  GrpFile gf where gf.grpFileId = t.grpFileId and gf.fileStatus=0"
        + "       and gf.fileModuleType=:fileModuleType         and gf.grpId =:grpId ) ";
    Object count = this.createQuery(hql).setParameter("grpLabelId", grpLabelId).setParameter("grpId", grpId)
        .setParameter("fileModuleType", fileModuleType).uniqueResult();
    if (count != null) {
      return Integer.parseInt(count.toString());
    }
    return 0;
  }

  public GrpFileLabel findObjByFileIdAndLabelId(Long grpFileId, Long grpLabelId) {
    String hql = "from  GrpFileLabel t where  t.status=0 and  t.grpFileId=:grpFileId and t.grpLabelId=:grpLabelId  ";
    List list =
        this.createQuery(hql).setParameter("grpFileId", grpFileId).setParameter("grpLabelId", grpLabelId).list();
    if (list != null && list.size() > 0) {
      return (GrpFileLabel) list.get(0);
    }
    return null;
  }

}
