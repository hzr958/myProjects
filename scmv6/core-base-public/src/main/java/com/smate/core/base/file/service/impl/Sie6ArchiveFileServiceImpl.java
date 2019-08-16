package com.smate.core.base.file.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.file.dao.Sie6ArchiveFileDao;
import com.smate.core.base.file.model.Sie6ArchiveFile;
import com.smate.core.base.file.service.Sie6ArchiveFileService;
import com.smate.core.base.utils.common.WebObjectUtil;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.model.impfilelog.ImportFileLog;

@Service("sie6ArchiveFileService")
@Transactional(rollbackFor = Exception.class)
public class Sie6ArchiveFileServiceImpl implements Sie6ArchiveFileService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private Sie6ArchiveFileDao sie6ArchiveFileDao;
  @Autowired
  private FileService fileService;

  @Override
  public Sie6ArchiveFile saveSie6ArchiveFile(ImportFileLog importFileLogs, File fileSource, String uuid,
      String filedataFileName, String savePathRoot) throws SysServiceException {
    try {
      Long fileSize = ArchiveFileUtil.getFileSize(fileSource);
      Sie6ArchiveFile sieFile = new Sie6ArchiveFile();
      String filePatch = writeFile(ServiceConstants.DIR_UPFILE, filedataFileName, fileSource, savePathRoot);
      String file_irl = "/" + ServiceConstants.DIR_UPFILE + fileService.getFilePath(filePatch);
      sieFile.setFileType("pdf");
      sieFile.setCreatePsnId(importFileLogs.getPsnId());
      sieFile.setCreateTime(new Date());
      sieFile.setFileDesc("科研验证文档");
      sieFile.setFileName(filedataFileName);
      sieFile.setFilePath(filePatch);
      sieFile.setNodeId(0);
      sieFile.setInsId(importFileLogs.getInsId());
      sieFile.setFileUUID(uuid);
      sieFile.setFileUrl(file_irl);
      sieFile.setFileSize(fileSize);
      sieFile.setStatus(0);
      sie6ArchiveFileDao.save(sieFile);
      sie6ArchiveFileDao.getSession().flush();
      return sieFile;
    } catch (Exception e) {
      logger.error("保存科研验证文件报错", e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public Sie6ArchiveFile getArchiveFile(Long id) throws SysServiceException {
    try {
      return sie6ArchiveFileDao.findArchiveFilesById(id);
    } catch (Exception e) {
      logger.error("getArchiveFile获取附件失败 : ", e);
      throw new SysServiceException();
    }
  }

  /**
   * 写文件,返回新文件名.
   * 
   * @return
   * @throws Exception
   */
  public String writeFile(String baseDir, String fileName, File fileData, String fileRoot) throws IOException {
    String currentPatch = fileRoot + "/" + baseDir;
    String ext = WebObjectUtil.getFileNameExt(fileName);
    String filePatch = fileService.writeUniqueFile(fileData, currentPatch, ext);
    // String filePatch = fileService.writeFile(fileData, baseDir,
    // fileName);
    return filePatch;
  }

}
