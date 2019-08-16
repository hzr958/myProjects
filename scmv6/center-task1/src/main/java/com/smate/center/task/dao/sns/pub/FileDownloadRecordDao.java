package com.smate.center.task.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.FileDownloadRecord;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 文件下载记录dao
 * 
 * @author ChuanjieHou
 * @date 2017年9月14日
 */
@Repository
public class FileDownloadRecordDao extends SnsHibernateDao<FileDownloadRecord, Long> {

  public Integer findCount(Long fileId) {
    String hql = "select count(1) from FileDownloadRecord t where t.fileType = 3 and t.fileId=:fileId";
    Long count = (Long) this.createQuery(hql).setParameter("fileId", fileId).uniqueResult();
    if (count != null) {
      return count.intValue();
    }
    return 0;
  }

  public Long getFileDonwloadSum(Long fileId) {
    String hql = "select count(*) from FileDownloadRecord p where  p.fileId=:fileId";
    return (Long) super.createQuery(hql).setParameter("fileId", fileId).uniqueResult();
  }
}
