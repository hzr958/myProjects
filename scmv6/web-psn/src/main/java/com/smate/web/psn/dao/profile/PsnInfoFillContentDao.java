package com.smate.web.psn.dao.profile;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.profile.PsnInfoFillContent;

@Repository
public class PsnInfoFillContentDao extends SnsHibernateDao<PsnInfoFillContent, Long> {

  // 删除相关数据
  @Deprecated
  public void deleteByPsnIdSaveId(Long psnId, Long saveId) {
    String hql = "delete from PsnInfoFillContent where psnId=:psnId and saveId=:saveId";
    super.createQuery(hql).setParameter("psnId", psnId).setParameter("saveId", saveId).executeUpdate();
  }
}
