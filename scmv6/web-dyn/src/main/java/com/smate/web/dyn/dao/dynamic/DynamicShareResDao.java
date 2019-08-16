package com.smate.web.dyn.dao.dynamic;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.dynamic.DynamicShareRes;

/**
 * 资源分享Dao.
 * 
 * @author zk
 * 
 */
@Repository
public class DynamicShareResDao extends SnsHibernateDao<DynamicShareRes, Long> {

  /**
   * 查询动态分享资源 @param resId @param resType @param resNode @return @throws
   */
  @SuppressWarnings("unchecked")
  public DynamicShareRes getDynamicShareRes(Long resId, int resType, int resNode) {
    String hql = "from DynamicShareRes t where t.resId=:resId and t.resType=:resType and t.resNode=:resNode";
    List<DynamicShareRes> list = super.createQuery(hql).setParameter("resId", resId).setParameter("resType", resType)
        .setParameter("resNode", resNode).setMaxResults(1).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 检查psnId在当天是否对同一条资源进行过分享
   * 
   * @param resId
   * @param resType
   * @param psnId
   * @return
   */
  @SuppressWarnings("rawtypes")
  public boolean checkShareSameDyn(Long resId, int resType, Long psnId) {
    String hql = "select sr.shareId from DynamicShareRes sr ,DynamicSharePsn sp where "
        + " sr.shareId=sp.shareId and sr.resType=:resType and sr.resId=:resId and sp.sharerPsnId=:psnId"
        + " and to_char(sp.shareDate,'yyyy-mm-dd') = to_char(sysdate,'yyyy-mm-dd')";
    List objList = super.createQuery(hql).setParameter("resType", resType).setParameter("resId", resId)
        .setParameter("psnId", psnId).list();
    if (CollectionUtils.isEmpty(objList)) {
      return false;
    } else {
      return true;
    }
  }
}
