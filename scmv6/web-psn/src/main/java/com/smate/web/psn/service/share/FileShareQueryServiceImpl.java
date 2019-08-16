package com.smate.web.psn.service.share;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.file.dao.ArchiveFileDao;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.psn.dao.PsnFileDao;
import com.smate.core.base.psn.model.PsnFile;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.dao.file.PsnFileShareBaseDao;
import com.smate.web.psn.dao.file.PsnFileShareRecordDao;
import com.smate.web.psn.dao.group.GrpFileShareBaseDao;
import com.smate.web.psn.dao.group.GrpFileShareRecordDao;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.file.PsnFileShareBase;
import com.smate.web.psn.model.file.PsnFileShareRecord;
import com.smate.web.psn.model.grp.GrpFile;
import com.smate.web.psn.model.grp.GrpFileShareBase;
import com.smate.web.psn.model.grp.GrpFileShareRecord;
import com.smate.web.psn.model.share.FileShareForm;

/**
 * 查询分享文件服务实现.
 * 
 * @author pwl
 * 
 */
@Service("fileShareQueryService")
@Transactional(rollbackFor = Exception.class)
public class FileShareQueryServiceImpl implements FileShareQueryService {

  /**
   * 
   */
  private static final long serialVersionUID = 4924223486545405005L;

  private static Logger logger = LoggerFactory.getLogger(FileShareQueryServiceImpl.class);

  @Autowired
  private PsnFileShareBaseDao psnFileShareBaseDao;
  @Autowired
  private PsnFileShareRecordDao psnFileShareRecordDao;
  @Autowired
  private PsnFileDao psnFileDao;
  @Autowired
  private ArchiveFileDao archiveFileDao;

  @Autowired
  private ArchiveFileService archiveFileService;

  @Autowired
  private GrpFileShareBaseDao grpFileShareBaseDao;
  @Autowired
  private GrpFileShareRecordDao grpFileShareRecordDao;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;

  @Override
  public Page<FileShareForm> getFileShareDataInSendSide1(Long resSendId, Long resReveiverId, Long baseId,
      Page<FileShareForm> page) throws ServiceException {
    try {
      Page<PsnFileShareRecord> resSendPage = this.psnFileShareRecordDao.queryPsnResSendResByPage(resSendId,
          resReveiverId, page.getParamPageNo(), page.getPageSize(), baseId);
      page.setTotalCount(resSendPage.getTotalCount());
      List<PsnFileShareRecord> resSendResList = resSendPage.getResult();
      if (CollectionUtils.isNotEmpty(resSendResList)) {
        List<FileShareForm> fileQueryFormList = new ArrayList<FileShareForm>();
        for (PsnFileShareRecord resSendRes : resSendResList) {
          PsnFile psnFile = this.psnFileDao.get(resSendRes.getFileId());
          if (psnFile != null) {
            FileShareForm form = this.buildeFileShareQueryForm1(psnFile,
                resSendRes == null ? new Date() : resSendRes.getCreateDate(), 0);
            fileQueryFormList.add(form);
          }
        }
        page.setResult(fileQueryFormList);
      }

      return page;
    } catch (Exception e) {
      logger.error("发送端查询文件分享列表记录出现异常resSendId=" + resSendId, e);
      throw new ServiceException(e);
    }
  }

  public FileShareForm buildeFileShareQueryForm1(PsnFile stationFile, Date shareDate, int status)
      throws ServiceException {
    FileShareForm form = new FileShareForm();
    form.setResId(stationFile.getId());
    form.setDes3ResId(ServiceUtil.encodeToDes3(stationFile.getId().toString()));

    form.setResNodeId(1);
    form.setFileName(stationFile.getFileName());
    form.setFileType(stationFile.getFileType());
    form.setFileViewType(
        stationFile.getFileName().substring(stationFile.getFileName().lastIndexOf(".") + 1).toLowerCase());
    form.setShareDate(shareDate);

    form.setIsImported(status);
    form.setDes3ArchId(ServiceUtil.encodeToDes3(stationFile.getArchiveFileId().toString()));
    try {
      ArchiveFile archiveFile = archiveFileDao.findArchiveFileById(stationFile.getArchiveFileId());
      form.setFileStatus(stationFile.getStatus() == 0 && archiveFile.getStatus() == 0 ? 0 : 1);
      form.setFileSize(archiveFile.getFileSize());
    } catch (Exception e) {

    }
    String downUrl = fileDownUrlService.getDownloadUrl(FileTypeEnum.PSN, stationFile.getId(), 0L);
    form.setDownloadUrl(downUrl);
    form.setImgThumbUrl(archiveFileService.getImgFileThumbUrl(stationFile.getArchiveFileId()));
    return form;
  }

  @Override
  public int checkNewShareStatus(Long baseId) {
    PsnFileShareBase psnFileShareBase = psnFileShareBaseDao.get(baseId);
    if (psnFileShareBase == null || psnFileShareBase.getStatus() != 0) {
      return -1;
    }
    return 0;
  }

  @Override
  public int checkGrpFileShareStatus(Long baseId) {
    GrpFileShareBase grpFileShareBase = grpFileShareBaseDao.get(baseId);
    if (grpFileShareBase == null || grpFileShareBase.getStatus() != 0) {
      return -1;
    }
    return 0;
  }

  @Override
  public Page<FileShareForm> getGrpFileShareDataInSendSide(Long resSendId, Long resReveiverId, Long baseId,
      Page<FileShareForm> page) throws ServiceException {
    try {
      Page<GrpFileShareRecord> resSendPage =
          this.grpFileShareRecordDao.queryGrpFileShareByPage(resSendId, resReveiverId, baseId);
      // page.setTotalCount(resSendPage.getTotalCount());
      List<GrpFileShareRecord> resSendResList = resSendPage.getResult();
      if (CollectionUtils.isNotEmpty(resSendResList)) {
        List<FileShareForm> fileQueryFormList = new ArrayList<FileShareForm>();
        for (GrpFileShareRecord resSendRes : resSendResList) {
          GrpFile grpFile = grpFileShareRecordDao.findGrpFileById(resSendRes.getGrpFileId());
          if (grpFile != null) {
            FileShareForm form = this.buildGrpFileShareQueryForm(grpFile,
                resSendRes == null ? new Date() : resSendRes.getCreateDate(), 0);
            fileQueryFormList.add(form);
          }
        }
        page.setResult(fileQueryFormList);
      }

      return page;
    } catch (Exception e) {
      logger.error("发送端查询文件分享列表记录出现异常resSendId=" + resSendId, e);
      throw new ServiceException(e);
    }
  }

  public FileShareForm buildGrpFileShareQueryForm(GrpFile grpFile, Date shareDate, int status) throws ServiceException {
    FileShareForm form = new FileShareForm();
    // form.setResId(stationFile.getId());
    // form.setDes3ResId(ServiceUtil.encodeToDes3(stationFile.getId().toString()));

    form.setResNodeId(1);
    form.setFileName(grpFile.getFileName());
    form.setFileType(grpFile.getFileType());
    form.setFileViewType(grpFile.getFileName().substring(grpFile.getFileName().lastIndexOf(".") + 1).toLowerCase());
    form.setShareDate(shareDate);

    form.setIsImported(status);
    form.setDes3ArchId(ServiceUtil.encodeToDes3(grpFile.getArchiveFileId().toString()));
    try {
      ArchiveFile archiveFile = archiveFileDao.findArchiveFileById(grpFile.getArchiveFileId());
      form.setFileStatus(grpFile.getFileStatus() == 0 && archiveFile.getStatus() == 0 ? 0 : 1);
      form.setFileSize(archiveFile.getFileSize());
    } catch (Exception e) {

    }
    String downUrl = fileDownUrlService.getDownloadUrl(FileTypeEnum.GROUP, grpFile.getGrpFileId(), 0L);
    form.setDownloadUrl(downUrl);
    form.setImgThumbUrl(archiveFileService.getImgFileThumbUrl(grpFile.getArchiveFileId()));
    return form;
  }

}
