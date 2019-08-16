package com.smate.center.task.service.pdwh.quartz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.base.TaskJobTypeConstants;
import com.smate.center.task.dao.pdwh.quartz.PdwhFullTextFileDao;
import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.center.task.dao.sns.quartz.ArchiveFileDao;
import com.smate.center.task.dao.sns.quartz.PubFulltextDao;
import com.smate.center.task.model.sns.quartz.ArchiveFile;
import com.smate.center.task.model.sns.quartz.PubFulltext;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileUtils;

@Service("nsfcFullTextMatchService")
@Transactional(rollbackFor = Exception.class)
public class NsfcFullTextMatchServiceImpl implements NsfcFullTextMatchService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  private static Integer jobType = TaskJobTypeConstants.NsfcFullTextMatchTask;
  @Autowired
  ArchiveFileDao archiveFileDao;
  @Autowired
  PdwhFullTextFileDao pdwhFullTextFileDao;
  @Autowired
  PubFulltextDao pubFulltextDao;
  @Autowired
  TmpTaskInfoRecordDao tmpTaskInfoRecordDao;
  @Value("${file.root}")
  private String rootPath;
  private static String desNsfcPath = "/home/smate/files/nsfc_pubs_fulltext/";

  @Override
  public List<Long> batchGetPdwhPubIds(Integer size) throws Exception {
    return tmpTaskInfoRecordDao.getbatchhandleIdList(size, jobType);
  }

  @Override
  public boolean matchSnsPubFulltext(Long pubId) throws Exception {

    PubFulltext pubFulltext = pubFulltextDao.get(pubId);
    if (pubFulltext == null) {
      // sns对应全文为空
      return false;
    }
    Long fulltextFileId = pubFulltext.getFulltextFileId();

    if (fulltextFileId == 0L || fulltextFileId == null) {
      return false;
    }

    ArchiveFile file = archiveFileDao.findArchiveFileById(fulltextFileId);
    if (file == null) {
      return false;
    } else {
      String desfilePath = file.getFilePath();
      try {
        this.copyFulltextFile(desfilePath, pubId, "sns");
        return true;
      } catch (Exception e) {
        logger.error("复制sns全文文件到nsfc目录出错！", e);
        this.updateTaskStatus(pubId, 2, "复制sns全文文件到nsfc目录出错！");
      }
    }

    return false;
  }

  @Override
  public void matchPdwhPubFulltext(Long pubId) throws Exception {
    Long fileIdByPubId = pdwhFullTextFileDao.getFileIdByPubId(pubId);
    if (fileIdByPubId == 0L || fileIdByPubId == null) {
      this.updateTaskStatus(pubId, 2, "sns匹配不到，pdwh此成果无全文");
      return;
    } else {
      ArchiveFile file = archiveFileDao.findArchiveFileById(fileIdByPubId);
      if (file == null) {
        this.updateTaskStatus(pubId, 2, "sns匹配不到，ArchiveFiles表此fileId对应文件信息为空！");
        return;
      } else {
        String desfilePath = file.getFilePath();
        try {
          this.copyFulltextFile(desfilePath, pubId, "pdwh");
        } catch (Exception e) {
          logger.error("复制pdwh全文文件到nsfc目录出错！", e);
          this.updateTaskStatus(pubId, 2, "复制pdwh全文文件到nsfc目录出错！" + getErrorMsg(e));
        }
      }

    }

  }

  @Override
  public void updateTaskStatus(Long psnId, int status, String err) {
    try {
      tmpTaskInfoRecordDao.updateTaskStatus(psnId, status, err, jobType);
    } catch (Exception e) {
      logger.error("更新任务状态记录出错！handleId,status,jobtype=" + psnId + ",status" + ",jobType", e);
    }
  }

  /**
   * 
   * @param srcPath
   * @param pubId
   * @throws Exception
   */
  public void copyFulltextFile(String srcPath, Long pubId, String dbflag) throws Exception {
    // 解析真实地址
    String realpath =
        FileUtils.SYMBOL_VIRGULE_CHAR + ServiceConstants.DIR_UPFILE + ArchiveFileUtil.getFilePath(srcPath);
    File file = new File(rootPath + realpath);
    String fileName = file.getName();
    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
    String tarFileName = pubId + "." + fileExt;
    // 复制文件到制定目录
    try {
      this.writeFile(file, desNsfcPath + tarFileName);
    } catch (FileNotFoundException e) {
      logger.error("复制全文文件到nsfc目录出错,全文文件找不到！pubId：" + pubId);
      this.updateTaskStatus(pubId, 2, "复制全文文件到nsfc目录出错,请确认文件地址文件是否存在：" + realpath);
      return;
    }
    // 处理成功更新状态
    this.updateTaskStatus(pubId, 1, dbflag);

  }

  /**
   * 写入文件
   * 
   * @param fileData
   * @param filePath
   * @throws Exception
   */
  public void writeFile(File fileData, String filePath) throws Exception {

    try {
      File file = new File(filePath);
      // 目录是否存在
      File parentFile = file.getParentFile();
      if (!parentFile.exists()) {
        boolean b = false;
        try {
          b = parentFile.mkdirs();
        } catch (Throwable e) {
          e.printStackTrace();
        }
        if (!b) {
          throw new IOException("文件夹" + parentFile.getPath() + "创建失败");
        }
        if (!parentFile.exists()) {
          throw new IOException("文件夹" + parentFile.getPath() + "创建后目录不存在");
        }
      }
      FileOutputStream fos = new FileOutputStream(filePath);
      FileInputStream fis = new FileInputStream(fileData);
      byte[] buffer = new byte[1024];
      int len = 0;
      while ((len = fis.read(buffer)) > 0) {
        fos.write(buffer, 0, len);
      }
      fos.close();
      fis.close();
    } catch (FileNotFoundException e) {
      throw new FileNotFoundException();
    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /**
   * 获取异常信息
   * 
   * @param e
   * @return
   */

  private String getErrorMsg(Exception e) {
    Writer result = new StringWriter();
    PrintWriter printWriter = new PrintWriter(result);
    e.printStackTrace(printWriter);
    String ErrorMsg = result.toString().length() > 450 ? result.toString().substring(450) : result.toString();
    return ErrorMsg;
  }
}
