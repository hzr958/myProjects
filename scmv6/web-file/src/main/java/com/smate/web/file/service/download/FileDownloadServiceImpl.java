package com.smate.web.file.service.download;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.web.file.cache.FileCacheService;
import com.smate.web.file.exception.FileDownloadTimeOutException;
import com.smate.web.file.exception.FileNotExistException;
import com.smate.web.file.form.FileDownloadForm;

/**
 * 文件下载服务实现
 * 
 * @author tsz
 *
 */
public class FileDownloadServiceImpl implements FileDownloadService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private Map<String, BuildResService> serviceMap;

  @Autowired
  private FileCacheService fileCacheService;

  /**
   * 构建下载资源
   */
  @Override
  public void buildDownloadRes(FileDownloadForm form) throws Exception {
    if (!form.isShortUrl()) { // 不是短地址
      // 判断该现在地址是否 有效 (取缓存)
      Object obj = fileCacheService.get(FileDownloadUrlService.DOWNLOAD_CACHE_MARK, form.getKey());
      if (obj == null) {
        throw new FileDownloadTimeOutException();
      }
      String[] value = obj.toString().split(";");
      if (value.length != 3) {
        throw new FileDownloadTimeOutException();
      }
      if (StringUtils.isNotBlank(value[0])) {
        form.setFileId(Long.valueOf(value[0]));
      }
      if (StringUtils.isNotBlank(value[1])) {
        form.setPubId(Long.valueOf(value[1]));
      }
      if (StringUtils.isNotBlank(value[2])) {
        form.setFileType(value[2]);
      }
    }
    // 判断下载权限 （根据下载类型 区分下载权限）
    BuildResService checkResOauthService = serviceMap.get(form.getFileType().getValue());
    if (checkResOauthService == null) {
      throw new FileNotExistException();
    }
    checkResOauthService.build(form);

  }

  public Map<String, BuildResService> getServiceMap() {
    return serviceMap;
  }

  public void setServiceMap(Map<String, BuildResService> serviceMap) {
    this.serviceMap = serviceMap;
  }

  public static void main(String[] args) {
    String str = "321321w|ewrwe";
    String[] a = str.split("w");

    System.out.println(a.length);
  }

  @Override
  public void downloadAfterExtend(FileDownloadForm form) throws Exception {
    // 判断下载权限 （根据下载类型 区分下载权限）
    BuildResService checkResOauthService = serviceMap.get(form.getFileType().getValue());
    if (checkResOauthService == null) {
      throw new FileNotExistException();
    }
    checkResOauthService.extend(form);
  }

}
