package com.smate.web.file.dao.record;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.file.model.record.FileDownloadRecord;

/**
 * 文件下载记录dao
 * 
 * @author ChuanjieHou
 * @date 2017年9月14日
 */
@Repository
public class FileDownloadRecordDao extends SnsHibernateDao<FileDownloadRecord, Long> {
  private Logger logger = LoggerFactory.getLogger(getClass());

}
