package com.smate.web.file.service.download;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.utils.common.MD5Util;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.file.cache.FileCacheService;
import com.smate.web.file.exception.FileDownloadTimeOutException;
import com.smate.web.file.exception.FileNotExistException;
import com.smate.web.file.form.FileDownloadForm;

/**
 * 批量文件下载资源构建服务实现类
 * 
 * @author houchuanjie
 * @date 2018年3月6日 下午2:51:37
 */
public class BatchFileDownloadServiceImpl implements FileDownloadService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private Map<String, BuildResService> serviceMap;

  @Autowired
  private FileCacheService fileCacheService;

  /**
   * 构建批量文件的下载资源
   */
  @Override
  public void buildDownloadRes(FileDownloadForm form) throws Exception {
    // TODO 构造文件下载资源
    // 分割多个
    List<Long> fileIdList =
        StringUtils.split2List(form.getDes3fids(), ",", s -> NumberUtils.parseLong(Des3Utils.decodeFromDes3(s)));
    if (CollectionUtils.isEmpty(fileIdList))
      throw new FileNotExistException();
    // 判断该现在地址是否 有效 (取缓存)
    Object obj = fileCacheService.get(FileDownloadUrlService.DOWNLOAD_CACHE_MARK,
        MD5Util.string2MD5(form.getDes3fids() + form.getKey()));
    if (Objects.isNull(obj)) {
      throw new FileDownloadTimeOutException();
    }
    form.setFileIdList(fileIdList);
    // 判断下载权限 （根据下载类型 区分下载权限）
    BuildResService buildResService = serviceMap.get(form.getFileType().getValue());
    if (Objects.isNull(buildResService)) {
      throw new FileNotExistException();
    }
    buildResService.build(form);
  }

  public Map<String, BuildResService> getServiceMap() {
    return serviceMap;
  }

  public void setServiceMap(Map<String, BuildResService> serviceMap) {
    this.serviceMap = serviceMap;
  }

  @Override
  public void downloadAfterExtend(FileDownloadForm form) throws Exception {
    // 判断下载权限 （根据下载类型 区分下载权限）
    BuildResService buildResService = serviceMap.get(form.getFileType().getValue());
    if (Objects.isNull(buildResService)) {
      throw new FileNotExistException();
    }
    buildResService.extend(form);
  }
}
