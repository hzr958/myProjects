package com.smate.web.file.dao;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.file.model.PsnFileShareRecordQuery;

/**
 * 个人文件分享记录查询DAO
 *
 * @author houchuanjie
 * @date 2017年11月28日 下午4:47:51
 */
@Repository
public class PsnFileShareRecordQueryDao extends SnsHibernateDao<PsnFileShareRecordQuery, Long> {
  public boolean isExistIn(Long receiverId, Long psnFileId) {
    String hql =
        "select count(1) from PsnFileShareRecordQuery r WHERE r.status=0 and r.fileId=:psnFileId and r.reveiverId=:receiverId";
    Long count = (Long) super.createQuery(hql).setParameter("psnFileId", psnFileId)
        .setParameter("receiverId", receiverId).uniqueResult();
    return count > 0 ? true : false;
  }
}
