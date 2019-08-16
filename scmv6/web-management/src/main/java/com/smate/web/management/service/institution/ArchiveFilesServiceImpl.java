package com.smate.web.management.service.institution;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.file.dao.ArchiveFileDao;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.common.WebObjectUtil;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.MapBuilder;
import com.smate.web.management.model.institution.bpo.FileUploadSimple;

@Service("archiveFileService")
@Transactional(rollbackFor = Exception.class)
public class ArchiveFilesServiceImpl implements ArchiveFilesService {

  Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "fileService")
  private FileService fileService;
  @Value("${bpofile.root}")
  private String fileRoot;
  @Autowired
  private ArchiveFileDao archiveFileDao;

  @Override
  public FileUploadSimple uploadInsFax(FileUploadSimple fileUploadSimple) throws Exception {
    try {
      fileUploadSimple = this.saveArchiveFiles(fileUploadSimple, ServiceConstants.DIR_INS_FAX);
      return fileUploadSimple;
    } catch (Exception e) {
      logger.error("saveArchiveFiles文件上传，保存附件失败: ", e);
      fileUploadSimple
          .setSaveResult(MapBuilder.getInstance().put("result", "error").put("msg", "upload error!").getJson());
      return fileUploadSimple;
    }
  }

  /**
   * 文件上传，保存附件.
   * 
   * @param fileUploadSimple
   * @return
   * @throws ServiceException
   */
  public FileUploadSimple saveArchiveFiles(FileUploadSimple fileUploadSimple, String baseDir) throws Exception {
    try {
      Map<String, String> filterResult = this.filterType(fileUploadSimple.getFiledataFileName(),
          fileUploadSimple.getAllowType(), fileUploadSimple.getDenyTypes());
      if (filterResult != null) {
        fileUploadSimple.setResult(false);
        fileUploadSimple.setSaveResult(JacksonUtils.mapToJsonStr(filterResult));
        return fileUploadSimple;
      }

      Long fileSize = ArchiveFileUtil.getFileSize(fileUploadSimple.getFiledata());
      // 过滤文件大小
      filterResult = this.filterMBSize(fileSize, fileUploadSimple.getLimitSize());
      if (filterResult != null) {

        fileUploadSimple.setResult(false);
        fileUploadSimple.setSaveResult(JacksonUtils.mapToJsonStr(filterResult));
        return fileUploadSimple;
      }
      fileUploadSimple.setFileSize(fileSize);// 计算文件大小
      String filePath = writeFile(fileRoot, fileUploadSimple.getFiledataFileName(), fileUploadSimple.getFiledata());
      ArchiveFile af = new ArchiveFile();
      af.setFilePath(filePath);
      af.setCreatePsnId(SecurityUtils.getCurrentUserId());
      af.setCreateTime(new Date());
      af.setFileDesc(fileUploadSimple.getFileDesc());
      af.setFileName(fileUploadSimple.getFiledataFileName());
      af.setFileType(fileUploadSimple.getFileType());
      af.setNodeId(SecurityUtils.getCurrentUserNodeId());
      fileUploadSimple.setArchiveFile(af);
      fileUploadSimple.setResult(true);
      return fileUploadSimple;
    } catch (Exception e) {
      logger.error("保存文件出错", e);
    }
    return fileUploadSimple;
  }

  /**
   * 写文件,返回新文件名.
   * 
   * @return
   * @throws Exception
   */
  public String writeFile(String baseDir, String fileName, File fileData) throws IOException {
    String ext = WebObjectUtil.getFileNameExt(fileName);
    String filePatch = fileService.writeUniqueFile(fileData, baseDir, ext);
    return filePatch;
  }

  /**
   * 大小过滤.
   * 
   * @param fileSize
   * @param limitSize
   * @return
   */
  public Map<String, String> filterMBSize(Long fileSize, Long limitSize) {

    if (limitSize == null) {
      limitSize = 30L;
    }
    return filterSize(fileSize, limitSize + "MB");
  }

  /**
   * 大小过滤.
   * 
   * @param fileSize
   * @param limitSize
   * @return
   */
  public Map<String, String> filterSize(Long fileSize, String limitLength) {
    Map<String, String> map = new HashMap<String, String>();
    if (fileSize == 0) {
      map.put("result", "error");
      Locale locale = LocaleContextHolder.getLocale();
      if (locale.equals(Locale.US)) {
        map.put("msg", "Don't upload blank file!");
      } else {
        map.put("msg", "不能上传空文件！");
      }
      return map;
    }
    // 默认30M
    if (StringUtils.isBlank(limitLength)) {
      limitLength = "30MB";
    }
    long limitSize = 0;
    if (StringUtils.endsWithIgnoreCase(limitLength, "MB")) {
      limitSize = ArchiveFileUtil.getMBByte(NumberUtils.createLong(limitLength.toUpperCase().replace("MB", "")));
    } else if (StringUtils.endsWithIgnoreCase(limitLength, "KB")) {
      limitSize = ArchiveFileUtil.getKBByte(NumberUtils.createLong(limitLength.toUpperCase().replace("KB", "")));
    } else {
      // 默认30M
      limitSize = ArchiveFileUtil.getMBByte(30);
    }
    // 默认最大30M;
    if (limitSize > ArchiveFileUtil.getMBByte(30)) {
      limitLength = "30MB";
      limitSize = ArchiveFileUtil.getMBByte(30);
    }
    if (fileSize > limitSize) {
      map.put("result", "error");
      Locale locale = LocaleContextHolder.getLocale();
      if (locale.equals(Locale.US)) {
        map.put("msg", "Please upload a file within " + limitLength + ".");
      } else {
        map.put("msg", "请选择" + limitLength + "范围内的文件。");
      }
      return map;
    }

    return null;
  }

  /**
   * 过滤文件类型.
   * 
   * @return
   */
  public Map<String, String> filterType(String fileName, String allowType, String denyType) {

    if (allowType != null)
      allowType = allowType.toLowerCase();
    if (denyType != null)
      denyType = denyType.toLowerCase();

    String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    /**
     * 判断是否允许上传该类型
     */
    if (!StringUtils.isBlank(allowType) && allowType.indexOf(fileType) < 0) {
      Map<String, String> map = new HashMap<String, String>();
      map.put("result", "error");
      Locale locale = LocaleContextHolder.getLocale();
      if (locale.equals(Locale.US)) {
        map.put("msg", "Deny this file type for upload!");
      } else {
        map.put("msg", "该文件类型不允许上传");
      }
      return map;
    }

    if (!WebObjectUtil.isStrNvl(denyType) && allowType.indexOf(denyType) > -1) {
      Map<String, String> map = new HashMap<String, String>();
      map.put("result", "error");
      Locale locale = LocaleContextHolder.getLocale();
      if (locale.equals(Locale.US)) {
        map.put("msg", "Deny this file type for upload!");
      } else {
        map.put("msg", "该文件类型不允许上传");
      }
      return map;
    }

    return null;

  }

  @Override
  public FileUploadSimple uploadInsLogo(FileUploadSimple fileUploadSimple) throws Exception {
    try {
      fileUploadSimple = writeImageById(fileUploadSimple, ServiceConstants.DIR_LOGO_UPFILE,
          Long.valueOf(fileUploadSimple.getDes3Id()));
      return fileUploadSimple;
    } catch (Exception e) {
      logger.error("saveArchiveFiles单位Logo上传，保存附件失败: ", e);

      fileUploadSimple
          .setSaveResult(MapBuilder.getInstance().put("result", "error").put("msg", "upload error!").getJson());
      return fileUploadSimple;
    }
  }

  public FileUploadSimple writeImageById(FileUploadSimple fileUploadSimple, String baseDir, Long id) throws Exception {
    try {
      // 过滤文件类型
      Map<String, String> filterResult = this.filterType(fileUploadSimple.getFiledataFileName(),
          fileUploadSimple.getAllowType(), fileUploadSimple.getDenyTypes());
      if (filterResult != null) {
        fileUploadSimple.setResult(false);
        fileUploadSimple.setSaveResult(JacksonUtils.mapToJsonStr(filterResult));
        return fileUploadSimple;
      }
      Long fileSize = ArchiveFileUtil.getFileSize(fileUploadSimple.getFiledata());
      // 过滤文件大小
      filterResult = this.filterSize(fileSize, fileUploadSimple.getLimitLength());
      if (filterResult != null) {
        fileUploadSimple.setResult(false);
        fileUploadSimple.setSaveResult(JacksonUtils.mapToJsonStr(filterResult));
        return fileUploadSimple;
      }
      String fileName = fileUploadSimple.getFiledataFileName();
      fileName = id + fileName.substring(fileName.lastIndexOf("."));
      // 上传图片
      String realPath = getFileRoot();
      String dir = realPath + "/" + baseDir;
      String name = writeFile(dir, fileName, fileUploadSimple.getFiledata());
      String path = fileService.getFilePath(name);
      // 外面要的图片路径
      String filePath = "/" + baseDir + path;
      ArchiveFile af = new ArchiveFile();
      af.setFilePath(filePath);
      fileUploadSimple.setArchiveFile(af);
      fileUploadSimple.setResult(true);
    } catch (Exception e) {
      logger.error("整理图片是出错", e);
      throw new Exception();
    }
    return fileUploadSimple;

  }

  public String parseFileNameDir(String fileName) {

    if (fileName.matches("^[\\d]{12}.*$")) {
      StringBuilder sb = new StringBuilder();
      sb.append(fileName.substring(0, 4)).append("/");
      sb.append(fileName.substring(4, 6)).append("/");
      sb.append(fileName.substring(6, 8)).append("/");
      sb.append(fileName.substring(8, 10)).append("/");
      sb.append(fileName.substring(10, 12)).append("/");
      sb.append(fileName);
      return sb.toString();
    }
    return fileName;
  }



  public FileUploadSimple saveArchiveFundFiles(FileUploadSimple fileUploadSimple) throws Exception {
    try {
      fileUploadSimple = this.saveArchiveFiles(fileUploadSimple, ServiceConstants.DIR_UPFILE);
      if (fileUploadSimple.getResult()) {
        ArchiveFile archiveFile = this.saveArchiveFiles(fileUploadSimple.getArchiveFile());
        fileUploadSimple.setResult(true);

        fileUploadSimple.setSaveResult(covertArchiveFilesJson(archiveFile));
      }
      return fileUploadSimple;
    } catch (Exception e) {
      logger.error("saveArchiveFiles文件上传，保存附件失败: ", e);
      fileUploadSimple
          .setSaveResult(MapBuilder.getInstance().put("result", "error").put("msg", "upload error!").getJson());
      return fileUploadSimple;
    }
  }

  /**
   * 保存附件.
   */
  public ArchiveFile saveArchiveFiles(ArchiveFile archiveFile) throws ServiceException {

    try {
      archiveFile.setNodeId(SecurityUtils.getCurrentAllNodeId().get(0));
      if (archiveFile.getInsId() == null)
        archiveFile.setInsId(SecurityUtils.getCurrentInsId());

      archiveFileDao.save(archiveFile);
      return archiveFile;
    } catch (Exception e) {
      logger.error("saveArchiveFiles保存附件失败: ", e);
      throw new ServiceException(e);
    }

  }

  /**
   * 将ArchiveFiles转换为JSON数据.
   * 
   * @param archiveFiles
   * @return
   */
  @SuppressWarnings("unchecked")
  public String covertArchiveFilesJson(ArchiveFile archiveFile) {

    Map map = new HashMap();
    map.put("result", "success");
    map.put("des3FileId", Des3Utils.encodeToDes3(archiveFile.getFileId().toString()));
    map.put("createPsnId", archiveFile.getCreatePsnId());
    if (archiveFile.getCreateTime() != null)
      map.put("createTime", WebObjectUtil.covertDateToYMD(archiveFile.getCreateTime()));
    if (archiveFile.getFileDesc() != null)
      map.put("fileDesc", WebObjectUtil.replaceJsonStr(archiveFile.getFileDesc()));
    map.put("fileId", archiveFile.getFileId().toString());
    if (archiveFile.getFileName() != null)
      map.put("fileName", WebObjectUtil.replaceJsonStr(archiveFile.getFileName()));
    map.put("fileType", archiveFile.getFileType());
    map.put("path", this.fileService.getFilePath(archiveFile.getFilePath()));
    map.put("nodeId", archiveFile.getNodeId());
    // 生成附件下载链接
    map.put("fileLink", ArchiveFileUtil.DOWNLOAD_ACTION_URL
        + ArchiveFileUtil.getFDesId(archiveFile.getFileId(), archiveFile.getNodeId(), null));
    return JacksonUtils.mapToJsonStr(map);
  }

  /**
   * 获取附件.
   */
  @Override
  public ArchiveFile getArchiveFiles(Long id) throws ServiceException {

    return archiveFileDao.findArchiveFileById(id);
  }

  public String getFileRoot() {
    return fileRoot;
  }

  public void setFileRoot(String fileRoot) {
    this.fileRoot = fileRoot;
  }
}
