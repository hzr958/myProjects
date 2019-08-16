package com.smate.sie.center.task.service;

import java.io.File;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.sie.center.task.dao.SieArchiveFileDao;
import com.smate.sie.center.task.model.SieArchiveFile;

/**
 * SIE文件接口
 * 
 * @author yxy
 */
@Service("sieArchiveFilesService")
@Transactional(rollbackFor = Exception.class)
public class SieArchiveFilesServiceImpl implements SieArchiveFilesService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${sie.file.root}")
  private String sieFileRoot;
  @Autowired
  private SieArchiveFileDao archiveFilesDao;

  @Override
  public void deleteFileByPath(String filePath) throws ServiceException {
    try {
      File file = new File(sieFileRoot + filePath);
      if (file.exists()) {
        file.delete();
      }
    } catch (Exception e) {
      logger.error("删除文件{}出现异常：{}", filePath + e);
      throw new ServiceException("删除文件{" + filePath + "}出错", e);
    }
  }

  @Override
  public void saveArchiveFile(SieArchiveFile a) {
    archiveFilesDao.save(a);
  }

}
