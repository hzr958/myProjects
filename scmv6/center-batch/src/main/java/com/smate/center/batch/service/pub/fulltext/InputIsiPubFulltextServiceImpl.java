package com.smate.center.batch.service.pub.fulltext;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.batch.connector.dao.job.BatchQuartzDao;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.constant.BatchJobDetailConstant;
import com.smate.center.batch.dao.pdwh.pub.PdwhPubDuplicateDAO;
import com.smate.center.batch.dao.pdwh.pub.PdwhPubSituationDAO;
import com.smate.center.batch.dao.pdwh.pub.PubPdwhDetailDAO;
import com.smate.center.batch.model.pdwh.pub.PdwhPubDuplicatePO;
import com.smate.center.batch.service.pub.archiveFiles.ArchiveFilesService;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.pub.consts.RestTemplateUtils;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dto.PubFulltextDTO;

/**
 * isi全文导入任务服务类
 * 
 * @author tsz
 *
 */
@Service("inputIsiPubFulltextService")
@Transactional(rollbackFor = Exception.class)
public class InputIsiPubFulltextServiceImpl implements InputIsiPubFulltextService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${fulltext.input_targetfileDir}")
  private String targetDir; // 目标文件存放路径
  @Value("${domainscm}")
  private String domainscm;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private ArchiveFilesService archiveFilesService;
  @Autowired
  private BatchJobsService batchJobsService;
  @Autowired
  private BatchQuartzDao batchQuartzDao;

  private String finishPrefix = "finish";

  private String errorPrefix = "error";
  @Autowired
  private PdwhPubSituationDAO pdwhPubSituationDAO;
  @Autowired
  private PdwhPubDuplicateDAO pdwhPubDuplicateDAO;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;



  /**
   * 获取 源文件 文件名 命名方式
   * 
   * @return
   */
  private String getFileNameForm() {
    String remark = batchQuartzDao.getMarkByStrategy(BatchJobDetailConstant.INPUT_FULL_TEXT_QUARTZ);
    String fileNameForm = null;
    if (StringUtils.isNoneBlank(remark)) {
      fileNameForm = remark.split(",")[1].split("=")[1];
    }
    return fileNameForm;

  }

  /**
   * 获取 全文源文件路径
   * 
   * @return
   */
  @Override
  public String getSourceFileDir() {
    String remark = batchQuartzDao.getMarkByStrategy(BatchJobDetailConstant.INPUT_FULL_TEXT_QUARTZ);
    String sourceFileDir = null;
    if (StringUtils.isNoneBlank(remark)) {
      sourceFileDir = remark.split(",")[0].split("=")[1];
    }
    return sourceFileDir;

  }

  /**
   * 文件错误上传
   * 
   * @param file
   * @param fileName
   * @throws BatchTaskException
   */
  @Override
  public void changeErrorName(File file, String fileName) throws BatchTaskException {
    File newFile = new File(file.getPath().replace(fileName, "") + errorPrefix + "_" + fileName);
    try {
      FileUtils.moveFile(file, newFile);
    } catch (Exception e1) {
      logger.error("文件重命名失败：" + fileName);
      throw new BatchTaskException("文件重命名失败：" + fileName);
    }
  }

  /**
   * 文件处理方法
   * 
   * @throws BatchTaskException
   */
  @Override
  public void dealFile(String filePath) throws BatchTaskException {
    File file = new File(filePath);
    String fileName = file.getName();
    try {
      // 根据文件名称获取pdwhPubId
      Long pdwhPubId = getPubIdByFileSourceId(fileName);
      if (pdwhPubId == null) {
        changeErrorName(file, fileName);
        logger.error("在pdwhpub中没有找到对应的 pub_id,文件名：" + fileName);
        return;
      }
      PubFulltextDTO pubfulltext = uploadFile(file, pdwhPubId);
      changeFinishName(file, fileName);
      // SCM-25412
      savaPubFulltext(pdwhPubId, pubfulltext);
    } catch (Exception e) {
      changeErrorName(file, fileName);
      throw new BatchTaskException(e);
    }
  }



  /**
   * 文件正确上传
   *
   * @param file
   * @param fileName
   * @throws Exception
   */
  private void changeFinishName(File file, String fileName) throws Exception {
    File newFile = new File(file.getPath().replace(fileName, "") + finishPrefix + "_" + fileName);
    try {
      FileUtils.moveFile(file, newFile);
    } catch (IOException e) {
      logger.error("文件命名finish_失败" + fileName + "path:" + file.getPath(), e);
      throw new Exception("文件命名finish_失败" + fileName + "path:" + file.getPath(), e);
    }

  }

  /**
   * 标记为处理失败
   *
   * @param file
   * @param fileName
   * @throws Exception
   */
  public void changeFailed(File file, String fileName) {
    File newFile = new File(file.getPath().replace(fileName, "") + "failed" + "_" + fileName);
    try {
      FileUtils.moveFile(file, newFile);
    } catch (Exception e) {
      logger.error("文件标记处理失败(存在failed_开头的重名文件，无法重命名，将尝试命名为failed_x2_开头)：" + "path:" + file.getPath(), e);
      retryChangeFailed(file, fileName);
    }
  }

  public void retryChangeFailed(File file, String fileName) {
    File newFile = new File(file.getPath().replace(fileName, "") + "failed_x2" + "_" + fileName);
    try {
      FileUtils.moveFile(file, newFile);
    } catch (Exception e) {
      logger.error("文件命名为failed_x2_开头处理失败,请查找是否存在重复文件：" + "path:" + file.getPath(), e);
    }
  }

  /**
   * EISourceId匹配 获取 文件 全文的 基准库 pudid 基准库改造后从新表取PUB数据
   *
   * @param fileName
   * @return
   */
  public Long getPubIdByFileSourceId(String fileName) {
    String str = fileName.substring(0, fileName.lastIndexOf("."));
    Long pdwhPudId = null;
    String fileNameForm = this.getFileNameForm();
    // 文件名格式1:sourceid命名;
    if ("1".equals(fileNameForm)) {
      pdwhPudId = pdwhPubSituationDAO.getPubIdBySrcId(str);

    } else {
      // 文件名格式2：pdwhPudid命名
      PdwhPubDuplicatePO pub = pdwhPubDuplicateDAO.get(Long.parseLong(str));
      if (pub != null) {
        pdwhPudId = pub.getPdwhPubId();
      }
    }
    return pdwhPudId;
  }

  /**
   * 上传文件到制定目录
   * <p>
   * 保存文件对象
   * 
   * @throws IOException
   */
  private PubFulltextDTO uploadFile(File file, Long pdwhPubId) throws Exception {
    String fileName = file.getName();
    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
    String hashFileName = ArchiveFileUtil.buildUniqueFileName(fileExt);
    String fileDir = ArchiveFileUtil.getFilePath(hashFileName);
    String ext = FileUtils.getFileNameExtensionStr(fileName);
    // 上传文件到制定目录
    FileUtils.copyFile(file, new File(targetDir + fileDir));
    // 保存文件对象
    ArchiveFile archiveFile = new ArchiveFile();
    archiveFile.setCreatePsnId(9999999999999L);// 后台任务默认PSDID
    archiveFile.setCreateTime(new Date());
    PubPdwhDetailDOM detail = pubPdwhDetailDAO.findById(pdwhPubId);
    // SCM-15806 修改基准库全文pdf下载 改下载的文件名为成果的标题名
    if (StringUtils.isNotEmpty(detail.getTitle())) {
      archiveFile.setFileName(FileUtils.cleanArcFileName(detail.getTitle()) + "." + ext);
    }
    archiveFile.setFilePath(hashFileName);
    // SCM-15193
    if (file.exists() && file.isFile()) {
      archiveFile.setFileSize(file.length());
    } else {
      archiveFile.setFileSize(-1L);
    }
    archiveFile.setFileFrom("pdwh");
    archiveFile.setFileType(ArchiveFileUtil.getFileType(archiveFile.getFileName()));
    // 默认为sns节点,如果没有会导致老系统报空指针错误
    archiveFile.setNodeId(ServiceConstants.SCHOLAR_NODE_ID_1);
    archiveFile.setFileUrl("0");
    archiveFilesService.saveArchiveFile(archiveFile);

    PubFulltextDTO fulltext = new PubFulltextDTO();
    fulltext.setDes3fileId(Des3Utils.encodeToDes3(String.valueOf(archiveFile.getFileId())));
    fulltext.setFileName(archiveFile.getFileName());
    fulltext.setPermission(0);
    fulltext.setSrcDbId(detail.getSrcDbId());
    return fulltext;
  }

  private void savaPubFulltext(Long pdwhPubId, PubFulltextDTO fulltext) {
    try {
      Map<String, Object> result = new HashMap<>();
      result.put("pubId", String.valueOf(pdwhPubId));
      result.put("des3PubId", Des3Utils.encodeToDes3(String.valueOf(pdwhPubId)));
      result.put("pubHandlerName", "updatePdwhFullTextHandler");
      result.put("fullText", fulltext);
      result.put("srcDbId", fulltext.getSrcDbId());
      RestTemplateUtils.post(restTemplate, domainscm + V8pubQueryPubConst.PUBHANDLER_URL,
          JacksonUtils.mapToJsonStr(result));
      // 上传了全文要更新solr信息
      this.updatePubInfoInSolr(pdwhPubId);
    } catch (Exception e) {
      logger.error("更新全文出错,pubId=" + pdwhPubId, e);
    }
  }

  private void updatePubInfoInSolr(Long pdwhPubId) {
    // TODO Auto-generated method stub

  }

  public void setTargetDir(String targetDir) {
    this.targetDir = targetDir;
  }

  public static void main(String[] args) {
    System.out.println("A1976CM95900010.pdf".matches("^[0-9A-Z]+\\.pdf$"));
  }

  @Override
  public void readFile() throws BatchTaskException {
    // TODO Auto-generated method stub

  }

  @Override
  public String getMarkByStrategy(String inputFullTextQuartz) {
    // TODO Auto-generated method stub
    return null;
  }

}
