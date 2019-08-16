package com.smate.web.file.service.upload;

import java.util.Locale;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.web.file.model.FileInfo;

/**
 * 文件上传 服务类
 * 
 * @author tsz
 *
 */
@Transactional(rollbackOn = Exception.class)
public class FileUploadServiceImpl implements FileUploadService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private Map<String, BuildFileService> serviceMap;
  private Map<Integer, BuildFileService> buildFileServiceMap;

  @Override
  public Boolean saveFile(FileInfo fileInfo) throws Exception {
    Locale locale = LocaleContextHolder.getLocale();
    if (StringUtils.isBlank(fileInfo.getFileDealType())) {
      if (locale.equals(Locale.US)) {
        fileInfo.setResultMsg("The deal type is blank!");
      } else {
        fileInfo.setResultMsg("文件处理类型不能为空");
      }
      return false;
    }
    BuildFileService buildFileService = serviceMap.get(fileInfo.getFileDealType());
    if (buildFileService == null) {
      if (locale.equals(Locale.US)) {
        fileInfo.setResultMsg("The deal type is wrong!");
      } else {
        fileInfo.setResultMsg("文件处理类型不正确");
      }
      return false;
    }

    try {
      return buildFileService.buildFile(fileInfo);
    } catch (Exception e) {
      logger.error("构建并上传文件出错!" + fileInfo.toString(), e);
      return false;
    }
  }

  public Map<String, BuildFileService> getServiceMap() {
    return serviceMap;
  }

  public void setServiceMap(Map<String, BuildFileService> serviceMap) {
    this.serviceMap = serviceMap;
  }

  /*
   * @Override public void buildUploadRes(FileUploadForm form) throws Exception { BuildFileService
   * buildFileService = buildFileServiceMap.get(form.getUploadFileType().getValue()); if
   * (Objects.isNull(buildFileService)) { throw new IllegalArgumentException("参数错误！文件上传类型错误！"); }
   * buildFileService.build(form); }
   */

}
