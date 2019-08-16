package com.smate.web.management.service.institution;

import javax.annotation.Resource;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.management.dao.institution.bpo.InsRegDao;
import com.smate.web.management.model.institution.bpo.FileUploadSimple;
import com.smate.web.management.model.institution.bpo.InsReg;

@Service("approveService")
@Transactional(rollbackFor = Exception.class)
public class ApproveServiceImpl implements ApproveService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "archiveFileService")
  private ArchiveFilesService archiveFilesService;
  @Autowired
  private InsRegDao insRegDao;
  @Resource(name = "rolArchiveFilesService")
  private ArchiveFilesService rolArchiveFilesService;

  @Override
  public FileUploadSimple uploadAndSaveFaxAttachment(FileUploadSimple fileUploadSimple) throws Exception {
    try {
      fileUploadSimple = archiveFilesService.uploadInsFax(fileUploadSimple);
      InsReg insReg = insRegDao.get(NumberUtils.toLong(fileUploadSimple.getDes3Id()));
      if (insReg != null) {
        insReg.setFaxAttachmentPath(fileUploadSimple.getArchiveFile().getFilePath());
        this.insRegDao.save(insReg);
      }
    } catch (Exception e) {
      logger.error("上传和保存传真附件失败: ", e);
    }
    return fileUploadSimple;
  }

  @Override
  public FileUploadSimple uploadAndSaveInsLog(FileUploadSimple fileUploadSimple) throws Exception {
    try {
      fileUploadSimple = rolArchiveFilesService.uploadInsLogo(fileUploadSimple);
      InsReg insReg = this.insRegDao.get(Long.valueOf(fileUploadSimple.getDes3Id()));
      if (insReg != null) {
        insReg.setInsLogoPath(fileUploadSimple.getArchiveFile().getFilePath());
        this.insRegDao.save(insReg);
      }
    } catch (Exception e) {
      logger.error("上传和保存单位Logo失败: ", e);
    }
    return fileUploadSimple;
  }

}
