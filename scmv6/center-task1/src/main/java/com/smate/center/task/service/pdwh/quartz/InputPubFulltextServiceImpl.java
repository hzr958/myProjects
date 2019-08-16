package com.smate.center.task.service.pdwh.quartz;

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

import com.smate.center.task.dao.pdwh.quartz.OalibPubDupDao;
import com.smate.center.task.exception.InputFullTextTaskException;
import com.smate.center.task.model.sns.quartz.ArchiveFile;
import com.smate.center.task.service.sns.quartz.ArchiveFilesService;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubDuplicateDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubFullTextDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubSituationDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDetailDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubDuplicatePO;
import com.smate.core.base.pub.consts.RestTemplateUtils;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dto.PubFulltextDTO;

/**
 * 全文匹配上传任务业务实现
 *
 * @author LJ
 */
@Service("inputIsiPubFulltextService")
@Transactional(rollbackFor = Exception.class)
public class InputPubFulltextServiceImpl implements InputPubFulltextService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${fulltext.input_targetfileDir}")
  private String targetDir; // 匹配文件存放路径
  private String finishPrefix = "finish";
  private String errorPrefix = "error";
  @Value("${source.file.dir}")
  private String remark;
  @Value("${domainscm}")
  private String domainscm;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private ArchiveFilesService archiveFilesService;
  @Autowired
  private OalibPubDupDao oalibPubDupDao;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Autowired
  private PdwhPubFullTextDAO pdwhPubFullTextDAO;
  @Autowired
  private PdwhPubSituationDAO pdwhPubSituationDAO;
  @Autowired
  private PdwhPubDuplicateDAO pdwhPubDuplicateDAO;

  /**
   * ISI成果全文处理方法
   *
   * @throws Exception
   */
  @Override
  public void dealIsiFile(String filePath) throws Exception {
    File file = new File(filePath);
    String fileName = file.getName();
    // 根据文件名称获取pdwhPubId
    Long pdwhPubId = getPubIdByFileSourceId(fileName);
    if (pdwhPubId == null) {
      changeErrorName(file, fileName);
      logger.info("在pdwhpub中没有找到对应的 pub_id,文件名：" + fileName);
      return;
    }
    PubFulltextDTO pubfulltext = uploadFile(file, pdwhPubId);
    changeFinishName(file, fileName);
    // SCM-25412
    savaPubFulltext(pdwhPubId, pubfulltext);
  }

  /**
   * 处理CNKI全文 * CNKI全文文件名lzh抓取特殊字符的替换规则
   * <p>
   * \,@_@
   * <p>
   * /,#_#
   * <p>
   * :,&_&
   * <p>
   * *,@_# \
   * <p>
   * ?,@_&
   * <p>
   * <,#_@
   * <p>
   * >,#_&
   * <p>
   * |,&_@
   *
   * @param path
   * @throws Exception
   */
  @Override
  public void dealCnkiFile(String path) throws Exception {
    File file = new File(path);
    String fileName = file.getName();

    Long pdwhPubId = getPubPdwhIdByTitleHash(fileName);// 还没处理完，后续补全
    if (pdwhPubId == null) {
      changeErrorName(file, fileName);
      logger.info("在pdwhpub中没有找到对应的 pub_id,文件名：" + fileName);
      return;
    }
    PubFulltextDTO pubfulltext = uploadFile(file, pdwhPubId);
    changeFinishName(file, fileName);
    // SCM-25412
    savaPubFulltext(pdwhPubId, pubfulltext);

  }

  /**
   * 处理ei成果全文文件
   *
   * @param filePath
   * @throws Exception
   */
  @Override
  public void dealEiFile(String filePath) throws Exception {
    File file = new File(filePath);
    String fileName = file.getName();
    // 根据文件名称获取pdwhPubId
    Long pdwhPubId = getPubIdByFileSourceId(fileName);
    if (pdwhPubId == null) {
      changeErrorName(file, fileName);
      logger.info("在pdwhpub中没有找到对应的 pub_id,文件名：" + fileName);
      return;
    }
    PubFulltextDTO pubfulltext = uploadFile(file, pdwhPubId);
    changeFinishName(file, fileName);
    // SCM-25412
    savaPubFulltext(pdwhPubId, pubfulltext);

  }

  /**
   * RainPat全文处理方法
   *
   * @throws Exception
   */
  @Override
  public void dealRainPatFile(String filePath) throws Exception {

    File file = new File(filePath);
    String fileName = file.getName();
    // 根据文件名称获取pdwhPubId
    Long pdwhPubId = getPubIdByFile(fileName);
    if (pdwhPubId == null) {
      changeErrorName(file, fileName);
      logger.info("在pdwhpub中没有找到对应的 pub_id,文件名：" + fileName);
      return;
    }
    PubFulltextDTO pubfulltext = uploadFile(file, pdwhPubId);
    changeFinishName(file, fileName);
    // SCM-25412
    savaPubFulltext(pdwhPubId, pubfulltext);

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
    // 访问V8pub系统接口更新sorl索引
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("des3PubId", Des3Utils.encodeToDes3(pdwhPubId.toString()));
    String SERVER_URL = domainscm + V8pubQueryPubConst.PDWHUPDATESORL_URL;
    restTemplate.postForObject(SERVER_URL, params, String.class);
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

  public void setTargetDir(String targetDir) {
    this.targetDir = targetDir;
  }

  @Override
  public void dealDoiNameFile(String path) throws Exception {
    File file = new File(path);
    String fileName = file.getName();

    Long pdwhPubId = getPubIdByCleanDoiHash(fileName);
    if (pdwhPubId == null || pdwhPubId == 0L) {
      changeErrorName(file, fileName);
      logger.info("在pdwhpub中没有找到对应的 pub_id,文件名：" + fileName);
      return;
    }
    PubFulltextDTO pubfulltext = uploadFile(file, pdwhPubId);
    changeFinishName(file, fileName);
    // SCM-25412
    savaPubFulltext(pdwhPubId, pubfulltext);
  }

  @Override
  public void dealOalibNameFile(String path) throws Exception {
    File file = new File(path);
    String fileName = file.getName();
    String sourceId = fileName.substring(0, fileName.lastIndexOf("."));
    Long sourceIdHash = HashUtils.getStrHashCode(XmlUtil.getTrimBlankLower(sourceId));
    Long pdwhPubId = oalibPubDupDao.getPubBySourceIdHash(sourceIdHash);
    if (pdwhPubId == null || pdwhPubId == 0L) {
      changeErrorName(file, fileName);
      logger.info("在pdwhpub中没有找到对应的 pub_id,文件名：" + fileName);
      return;
    }
    PubFulltextDTO pubfulltext = uploadFile(file, pdwhPubId);
    changeFinishName(file, fileName);
    // SCM-25412
    savaPubFulltext(pdwhPubId, pubfulltext);
  }

  /**
   * 任务调用 文件处理方法
   *
   * @throws InputFullTextTaskException
   */
  @Override
  public File readFile() throws InputFullTextTaskException {
    String sourceFileDir = this.getSourceFileDir();
    File file = new File(sourceFileDir);

    if (!file.exists()) {
      logger.error("目录:" + sourceFileDir + " 不存在,将自动创建");
    }

    // 目录不存在则创建
    if (!file.getParentFile().exists()) {
      try {
        file.mkdirs();
      } catch (Exception e) {
        throw new InputFullTextTaskException("目录:" + sourceFileDir + " 创建失败");
      }
    }
    return file;

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

  public Long getPubIdByFile(String fileName) {
    String name = fileName.substring(0, fileName.lastIndexOf("."));
    String[] str = name.split("-");
    String patentNo = str[0];// 申请号
    String patentOpenNo = str[1];// 公开号
    Long patentNoHash = PubHashUtils.cleanPatentNoHash(patentNo);
    Long patentOpenNoHash = PubHashUtils.cleanPatentNoHash(patentOpenNo);
    Long pdwhPudId = pdwhPubDuplicateDAO.getPubIdByPatentNo(patentNoHash, patentOpenNoHash);
    return pdwhPudId;
  }

  public Long getPubPdwhIdByTitleHash(String fileName) {
    String title = fileName.substring(0, fileName.lastIndexOf("."));
    // 还原文件名特殊字符
    try {
      title = title.replaceAll("@_@", "\\\\").replaceAll("#_#", "/").replaceAll("&_&", ":").replaceAll("@_#", "*")
          .replaceAll("@_&", "?").replaceAll("#_@", "<").replaceAll("#_&", ">").replaceAll("&_@", "|");
    } catch (Exception e) {
      logger.error("文件名特殊字符处理出错！", e);
      return null;
    }
    String cleanTitle = PubHashUtils.cleanTitle(title);
    Long strHashCode = HashUtils.getStrHashCode(cleanTitle);
    Long pdwhPubId = pdwhPubDuplicateDAO.getPubPdwhIdByTitleHash(strHashCode);
    return pdwhPubId;
  }

  public Long getPubIdByCleanDoiHash(String fileName) {
    String orgdoi = fileName.substring(0, fileName.lastIndexOf("."));
    Long doiHash = PubHashUtils.cleanDoiHash(orgdoi);
    Long doiCleanHash = PubHashUtils.getDoiHashRemotePun(orgdoi);
    Long pdwhPudId = pdwhPubDuplicateDAO.getPubIdByCleanDoiHash(doiHash, doiCleanHash);
    return pdwhPudId;
  }

  /**
   * 文件错误上传
   *
   * @param file
   * @param fileName
   * @throws InputFullTextTaskException
   */
  @Override
  public void changeErrorName(File file, String fileName) throws Exception {
    File newFile = new File(file.getPath().replace(fileName, "") + errorPrefix + "_" + fileName);
    try {
      FileUtils.moveFile(file, newFile);
    } catch (Exception e) {
      logger.error("文件命名error_失败" + fileName + "path:" + file.getPath(), e);
      throw new Exception("文件命名error_失败：" + "path:" + file.getPath(), e);
    }
  }

  /**
   * 获取 全文源文件路径
   *
   * @return
   */
  private String getSourceFileDir() {
    String sourceFileDir = null;
    if (StringUtils.isNoneBlank(remark)) {
      sourceFileDir = remark.split(",")[0].split("=")[1];
    }
    return sourceFileDir;

  }

  /**
   * 获取 源文件 文件名 命名方式
   *
   * @return
   */
  private String getFileNameForm() {
    String fileNameForm = null;
    if (StringUtils.isNoneBlank(remark)) {
      fileNameForm = remark.split(",")[1].split("=")[1];
    }
    return fileNameForm;

  }
}
