package com.smate.web.group.dao.grp.label;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.grp.label.GrpLabel;


/**
 * 群组标签dao
 * 
 * @author aijiangbin
 *
 */
@Repository
public class GrpLabelDao extends SnsHibernateDao<GrpLabel, Long> {
  /**
   * 获取群组所有的标签
   * 
   * @param grpId
   * @return
   */
  public List<GrpLabel> getAllGrpLabel(Long grpId) {
    String hql = "from  GrpLabel t where  t.status=0  and  t.grpId=:grpId order by t.labelId asc";
    List list = this.createQuery(hql).setParameter("grpId", grpId).list();
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  /**
   * 通过群组ID和标签名，来获取群组标签
   * 
   * @param grpId
   * @param labelName
   * @return
   */
  public GrpLabel getLabelByGrpIdAndLabelName(Long grpId, String labelName) {
    String hql = "from  GrpLabel t where  t.labelName=:labelName  and t.status=0  and   t.grpId=:grpId ";
    Object object =
        this.createQuery(hql).setParameter("grpId", grpId).setParameter("labelName", labelName).uniqueResult();
    if (object != null) {
      return (GrpLabel) object;
    }
    return null;
  }

  /**
   * 通过群组ID和标签id，来获取群组标签
   * 
   * @param grpId
   * @param labelName
   * @return
   */
  public GrpLabel getLabelByGrpIdAndLabelId(Long grpId, Long labelId) {
    String hql = "from  GrpLabel t where  t.labelId=:labelId  and t.status=0  and   t.grpId=:grpId ";
    Object object = this.createQuery(hql).setParameter("grpId", grpId).setParameter("labelId", labelId).uniqueResult();
    if (object != null) {
      return (GrpLabel) object;
    }
    return null;
  }

  /**
   * 删除标签
   * 
   * @param currentPsnId
   * @param labelId
   * @return
   */
  public Integer deleteGrpLabel(Long currentPsnId, Long labelId) {
    String hql = "update GrpLabel t set t.status=1 , t.updatePsnId =:updatePsnId , t.updateDate =:updateDate"
        + " where t.labelId =:labelId ";
    int result = this.createQuery(hql).setParameter("updatePsnId", currentPsnId).setParameter("updateDate", new Date())
        .setParameter("labelId", labelId).executeUpdate();

    return result;
  }



  /**
   * 获取群组所有的标签,排除文件自己的标签
   * 
   * @param grpId
   * @return
   */
  public List<GrpLabel> getAllGrpLabelExcludeOwner(Long grpId, Long grpFileId) {
    String hql = "from  GrpLabel t where  t.status=0  and  t.grpId=:grpId "
        + " and  t.labelId not  in  ( select  distinct gf.grpLabelId   from   GrpFileLabel gf where  gf.status = 0 and  gf.grpFileId =:grpFileId  ) "
        + "order by t.labelId asc";
    List list = this.createQuery(hql).setParameter("grpId", grpId).setParameter("grpFileId", grpFileId).list();
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }



}
