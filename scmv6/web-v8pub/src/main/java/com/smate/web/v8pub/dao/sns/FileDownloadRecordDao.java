package com.smate.web.v8pub.dao.sns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.FileDownloadRecord;

/**
 * 文件下载记录dao
 * 
 * @author ChuanjieHou
 * @date 2017年9月14日
 */
@Repository
public class FileDownloadRecordDao extends SnsHibernateDao<FileDownloadRecord, Long> {
  private Logger logger = LoggerFactory.getLogger(getClass());

  public Long getFileDonwloadSum(Long fileId) {
    String hql = "select count(*) from FileDownloadRecord p where  p.fileId=:fileId";
    return (Long) super.createQuery(hql).setParameter("fileId", fileId).uniqueResult();
  }

  public Long findPdwhDownloadCount(Long fileId) {
    String hql = "select count(*) from FileDownloadRecord p where  p.fileType=3 and p.fileId=:fileId";
    Object result = super.createQuery(hql).setParameter("fileId", fileId).uniqueResult();
    if (result == null) {
      return 0L;
    }
    return (Long) result;
  }
}
