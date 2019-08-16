package com.smate.web.file.dao;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.model.FilePubFulltextPsnRcmd;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.file.form.FileDownloadForm;

/**
 * 成果全文人员指派记录Dao.
 * 
 * @author pwl
 * 
 */
@Repository
public class FilePubFulltextPsnRcmdDao extends SnsHibernateDao<FilePubFulltextPsnRcmd, Long> {

  /**
   * 根据fileId查询是否有推荐成果全文
   * 
   * @param form
   * @return
   */
  public Long queryRcmdPubFulltext(FileDownloadForm form) {
    String hql = "select count(1) from FilePubFulltextPsnRcmd t where t.fulltextFileId = :fileId";
    Object count = super.createQuery(hql).setParameter("fileId", form.getFileId()).uniqueResult();
    return NumberUtils.toLong(ObjectUtils.toString(count));
  }
}
