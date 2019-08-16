package com.smate.center.task.service.pdwh.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.base.TaskJobTypeConstants;
import com.smate.center.task.dao.pdwh.quartz.PdwhFullTextFileDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubFulltextImageRefreshDao;
import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.center.task.model.sns.quartz.ArchiveFile;
import com.smate.center.task.service.sns.quartz.ArchiveFilesService;
import com.smate.core.base.pub.dao.pdwh.PdwhFullTextImageDao;
import com.smate.core.base.pub.model.pdwh.PdwhFullTextImage;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.image.im4java.gm.FilePathBean;
import com.smate.core.base.utils.image.im4java.gm.GMImageUtil;

@Service("batchFulltextPdfToImageService")
@Transactional(rollbackFor = Exception.class)
@Deprecated
public class BatchFulltextPdfToImageServiceImpl implements BatchFulltextPdfToImageService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  private static Integer jobType = TaskJobTypeConstants.BatchFulltextPdfToImageTask;
  /*
   * @Autowired private BatchJobsDao batchJobsDao;
   */
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;
  @Autowired
  private PdwhPubFulltextImageRefreshDao pdwhPubFulltextImageRefreshDao;
  @Autowired
  private PdwhFullTextFileDao pdwhFullTextFileDao;
  @Autowired
  private PdwhFullTextImageDao pdwhFullTextImagedao;
  @Value("${file.root}")
  private String fileroot;
  @Autowired
  private ArchiveFilesService archiveFilesService;

  @Autowired
  private FileService fileService;

  @Override
  public List<Long> batchGetPdwhPubIds(Integer batchSize) throws Exception {
    return tmpTaskInfoRecordDao.getbatchhandleIdList(batchSize, jobType);
  }

  @Override
  public void updateTaskStatus(Long pubId, int status, String err) {
    try {
      tmpTaskInfoRecordDao.updateTaskStatus(pubId, status, err, jobType);
    } catch (Exception e) {
      logger.error("更新任务状态记录出错！handleId,status,jobtype=" + pubId + ",status" + ",jobType", e);
    }
  }

  @Override
  public Long getBatchOnProcessingCount() {
    /* return batchJobsDao.getBatchOnProcessingCount(); */
    return null;

  }

  @Override
  public boolean deleteOldImage(Long pdwhPubId) {
    try {
      // 首先删除成果全文图片，以免造成垃圾的附件.
      this.delPdwhPubFulltextByPubId(pdwhPubId);
    } catch (Exception e) {
      logger.error("pdwh全文图片生成任务删除旧成果全文图片出错", e);
      updateTaskStatus(pdwhPubId, 2, "pdwh全文图片生成任务删除旧成果全文图片出错");
      return false;
    }
    return true;
  }

  /**
   * 删除pdwh fulltext image
   * 
   * @param pubId
   * @throws BatchTaskException
   */
  public void delPdwhPubFulltextByPubId(Long pubId) throws BatchTaskException {
    try {
      PdwhFullTextImage pubFulltext = this.pdwhFullTextImagedao.get(pubId);
      if (pubFulltext != null) {
        String fulltextImagePath = pubFulltext.getImagePath();
        this.pdwhFullTextImagedao.delete(pubId);
        if (StringUtils.isNotBlank(pubFulltext.getImagePath())) {
          this.archiveFilesService.deleteFileByPath(fulltextImagePath);
        }
      }
    } catch (Exception e) {
      logger.error("删除pdwh成果pubId={}的全文转换后的图片出现异常：{}", pubId, e);
      throw new BatchTaskException(e);
    }

  }

  @Override
  public void batchConvertPubFulltextPdfToimage(List<Long> pdwhPubIds) throws Exception {
    // 获取文件id
    List<FilePathBean> filePathBeans = new ArrayList<>();
    for (Long pdwhPubId : pdwhPubIds) {
      Long fileId = pdwhFullTextFileDao.getFileIdByPubId(pdwhPubId);
      ArchiveFile archiveFile = archiveFilesService.getArchiveFiles(fileId);
      if (archiveFile == null) {
        this.updateTaskStatus(pdwhPubId, 2, "文件不存在");
        continue;
      }
      String fileName = archiveFile.getFileName();
      String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
      if (!"pdf".equalsIgnoreCase(fileType)) {
        logger.error("转换pdwh成果的全文包含非pdf文件，不处理!pdwhPubId" + pdwhPubId);
        continue;
      }
      String filePath = fileService.getFilePath(archiveFile.getFilePath());
      String srcFilePath = fileroot + "/" + ServiceConstants.DIR_UPFILE + filePath;
      String imageExt = "jpeg";
      String desPath = fileService.getFilePath(fileId + "_img_" + 1 + "." + imageExt);
      String desImagePath = fileroot + "/" + ServiceConstants.DIR_PUB_FULLTEXT_IMAGE + desPath;
      filePathBeans.add(new FilePathBean(pdwhPubId, fileId, fileName, srcFilePath, desImagePath, "239", "178", "20"));
    }
    if (CollectionUtils.isEmpty(filePathBeans)) {
      return;
    }
    Map<String, List<FilePathBean>> result = GMImageUtil.processThumbnailImage(filePathBeans);
    // 保存转换后的图片信息.
    for (FilePathBean file : result.get("success")) {
      this.savePdwhPubFulltextImage(file.getId(), 1, file.getFileFullToPath().replace(fileroot, ""), file.getFileId(),
          ".pdf");
      this.updateTaskStatus(file.getId(), 1, "pdf全文转换图片成功");
    }
    for (FilePathBean file : result.get("error")) {
      this.updateTaskStatus(file.getId(), 2, "pdf全文转换图片出错");
    }

  }

  @Override
  public void batchConvertPubFulltextPdfToimage(Long pdwhPubId) throws Exception {
    // 获取文件id
    Long fileId = pdwhFullTextFileDao.getFileIdByPubId(pdwhPubId);
    ArchiveFile archiveFile = archiveFilesService.getArchiveFiles(fileId);
    if (archiveFile == null) {
      this.updateTaskStatus(pdwhPubId, 2, "文件不存在");
      return;
    }
    String fileName = archiveFile.getFileName();
    String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    if (!"pdf".equalsIgnoreCase(fileType)) {
      logger.error("转换pdwh成果的全文包含非pdf文件，不处理!pdwhPubId" + pdwhPubId);
      this.updateTaskStatus(pdwhPubId, 2, "转换pdwh成果的全文包含非pdf文件");
      return;
    }
    String filePath = fileService.getFilePath(archiveFile.getFilePath());
    String srcFilePath = fileroot + "/" + ServiceConstants.DIR_UPFILE + filePath;
    String imageExt = "jpeg";
    String desPath = fileService.getFilePath(fileId + "_img_" + 1 + "." + imageExt);
    String desImagePath = fileroot + "/" + ServiceConstants.DIR_PUB_FULLTEXT_IMAGE + desPath;
    try {
      GMImageUtil.thumbnail(178, 239, true, srcFilePath, desImagePath);
      this.savePdwhPubFulltextImage(pdwhPubId, 1, "/" + ServiceConstants.DIR_PUB_FULLTEXT_IMAGE + desPath, fileId,
          ".pdf");
      this.updateTaskStatus(pdwhPubId, 1, "pdf全文转换图片成功");
    } catch (Exception e) {
      logger.error("pdf全文转换图片失败!pdwhPubId" + pdwhPubId);
      this.updateTaskStatus(pdwhPubId, 2, "pdf全文转换图片失败");
    }

  }

  /*
   * public static void main(String[] args) { File file = new File("D:\\scm_pdf\\new"); String[] list
   * = file.list(); List<FilePathBean> filePathBeans = new ArrayList<>(); for (String string : list) {
   * filePathBeans.add(new FilePathBean(NumberUtils.toLong(string.replace(".pdf", "")),
   * NumberUtils.toLong(string.replace(".pdf", "")), string, "D:\\scm_pdf\\new\\" + string,
   * "D:\\scm_pdf\\pdftojpg\\" + string.replace(".pdf", ".jpg"), "239", "178", "20")); }
   * 
   * long start = System.currentTimeMillis(); System.out.println("pdf to jpg 开始处理，总文件数：" +
   * list.length);
   * 
   * Map<String, List<FilePathBean>> result = GMImageUtil.processThumbnailImage(filePathBeans); //
   * 保存转换后的图片信息. for (FilePathBean sfile : result.get("success")) {
   * System.out.println(sfile.getFileName()); } for (FilePathBean efile : result.get("error")) {
   * System.err.println(efile.getFileName()); }
   * 
   * long end = System.currentTimeMillis(); System.out.println("处理完毕,耗时(s):" + (end - start) / 1000);
   * 
   * }
   * 
   * /** 保存pdwh成果全文图片信息.
   * 
   * @throws BatchTaskException
   */
  private void savePdwhPubFulltextImage(Long pubId, Integer fulltextImagePageIndex, String fulltextImagePath,
      Long fileId, String fileExtend) throws BatchTaskException {
    PdwhFullTextImage pubFulltext;
    try {
      pubFulltext = pdwhFullTextImagedao.get(pubId);
      if (pubFulltext == null) {
        pubFulltext = new PdwhFullTextImage();
        pubFulltext.setPubId(pubId);
        pubFulltext.setFileId(fileId);
        pubFulltext.setImagePageIndex(fulltextImagePageIndex);
        pubFulltext.setImagePath(fulltextImagePath);
        pubFulltext.setFileExtend(fileExtend);
        pdwhFullTextImagedao.save(pubFulltext);
      } else {
        pubFulltext.setImagePageIndex(fulltextImagePageIndex);
        pubFulltext.setImagePath(fulltextImagePath);
        pubFulltext.setFileExtend(fileExtend);
        pdwhFullTextImagedao.save(pubFulltext);
      }
    } catch (Exception e) {
      logger.error("保存pdwh成果pubId={}全文图片信息出现异常：{}", pubId, e);
      throw new BatchTaskException(e);
    }
  }

}
