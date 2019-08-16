package com.smate.web.file.service.download;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.psn.dao.PsnFileDao;
import com.smate.core.base.psn.model.PsnFile;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.file.exception.FileDownloadNoPermissionException;
import com.smate.web.file.exception.FileNotExistException;
import com.smate.web.file.form.DownloadFileRes;
import com.smate.web.file.form.FileDownloadForm;

/**
 * 个人文件批量下载构建资源服务类
 * 
 * @author houchuanjie
 * @date 2018年3月6日 下午3:24:53
 */
public class BuildBatchPsnFileResServiceImpl extends BuildResServiceBase implements InitializingBean {
  @Autowired
  private PsnFileDao psnFileDao;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public void check(FileDownloadForm form) throws Exception {
    // TODO 检查个人文件id是否存在，权限判断
    List<PsnFile> existPsnFileList = checkExistPsnFile(form.getFileIdList());
    if (CollectionUtils.isEmpty(existPsnFileList)) {
      form.setResult(false);
      form.setResultMsg("文件不存在！");
      throw new FileNotExistException();
    }

    List<PsnFile> availablePsnFileList = checkAvailablePsnFile(existPsnFileList);
    if (CollectionUtils.isEmpty(availablePsnFileList)) {
      form.setResult(false);
      form.setResultMsg("没有下载文件的权限！");
      throw new FileDownloadNoPermissionException();
    }

    Map<Long, DownloadFileRes> downloadFileResMap = checkAvailableArchiveFile(availablePsnFileList);
    if (MapUtils.isEmpty(downloadFileResMap)) {
      form.setResult(false);
      form.setResultMsg("文件不存在！");
      throw new FileNotExistException();
    }
    form.setDownloadFileResMap(downloadFileResMap);
  }

  @Override
  public void buildResUrl(FileDownloadForm form) throws Exception {
    Map<Long, DownloadFileRes> downloadFileResMap = form.getDownloadFileResMap();
    // 中间变量，用于文件名去重
    HashMap<String, Object> fileNameMap = new HashMap<>();
    downloadFileResMap.values().forEach(res -> {
      ArchiveFile af = res.getArchiveFile();
      String filePath = FileUtils.SYMBOL_VIRGULE + basicPath + ArchiveFileUtil.getFilePath(af.getFilePath());
      // 记录文件下载url
      res.setUrl(domainscm + filePath);
      // 文件名重名处理
      String fileName = af.getFileName();
      int i = 0;
      while (fileNameMap.containsKey(fileName)) {
        ++i;
        fileName += "_" + i;
      }
      fileNameMap.put(fileName, null);
      // 记录文件名称
      res.setName(fileName);
    });
    form.setDownloadFileResMap(downloadFileResMap);
  }

  @Override
  public void extend(FileDownloadForm form) throws Exception {
    // TODO 自动生成的方法存根

  }

  /**
   * 检查所有文件id是否存在或者是否被删除，返回存在且未被删除的文件列表
   *
   * @author houchuanjie
   * @date 2018年3月6日 下午4:02:41
   * @param form
   * @return
   */
  private List<PsnFile> checkExistPsnFile(List<Long> idList) {
    List<PsnFile> existPsnFileList = new ArrayList<>();
    idList.forEach(id -> {
      PsnFile psnFile = psnFileDao.get(id);
      // 文件存在且未删除
      if (Objects.nonNull(psnFile) && (Objects.isNull(psnFile.getStatus()) || psnFile.getStatus() == 0)) {
        existPsnFileList.add(psnFile);
      }
    });

    return existPsnFileList;
  }

  /**
   * 检查存在的文件列表的每个文件的权限，返回可下载的文件列表
   *
   * @author houchuanjie
   * @date 2018年3月6日 下午4:12:09
   * @param existPsnFileList
   * @return
   */
  private List<PsnFile> checkAvailablePsnFile(List<PsnFile> existPsnFileList) {
    List<PsnFile> availablePsnFileList = new ArrayList<>();
    existPsnFileList.forEach(psnFile -> {
      // 权限判断
      boolean flag = false;
      Integer permission = psnFile.getPermission();
      switch (permission) {
        case 0: // 所有人下载
          flag = true;
          break;
        default: { // 隐私
          // 判断是不是自己的文件
          Long ownerPsnId = psnFile.getOwnerPsnId();
          Long currentUserId = SecurityUtils.getCurrentUserId();
          flag = currentUserId != 0 && ownerPsnId.equals(currentUserId);
        }
      }
      // 权限检查通过，将该文件放入可下载文件列表
      if (flag) {
        availablePsnFileList.add(psnFile);
      }
    });
    return availablePsnFileList;
  }

  /**
   * 检查附件文件是否存在，并返回下载资源集合
   * 
   * @author houchuanjie
   * @date 2018年3月6日 下午4:22:14
   * @param availablePsnFileList
   * @return
   */
  private Map<Long, DownloadFileRes> checkAvailableArchiveFile(List<PsnFile> availablePsnFileList) {
    HashMap<Long, DownloadFileRes> resMap = new HashMap<>();
    availablePsnFileList.forEach(psnFile -> {
      ArchiveFile af = archiveFileDao.get(psnFile.getArchiveFileId());
      if (Objects.nonNull(af) && (Objects.isNull(af.getStatus()) || af.getStatus() == 0)) {
        DownloadFileRes downloadFileRes = new DownloadFileRes(psnFile.getId(), psnFile, af);
        downloadFileRes.setOwnerPsnId(psnFile.getOwnerPsnId());
        resMap.put(downloadFileRes.getId(), downloadFileRes);
      }
    });
    return resMap;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    // 用于构造下载链接的domain不能使用https协议，要替换为http协议
    this.domainscm = this.domainscm.replace("https://", "http://");
  }
}
