package com.smate.web.file.service.download;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smate.core.base.file.dao.ArchiveFileDao;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.web.file.dao.GrpFileDao;
import com.smate.web.file.exception.FileNotExistException;
import com.smate.web.file.form.DownloadFileRes;
import com.smate.web.file.form.FileDownloadForm;
import com.smate.web.file.model.V_GrpFile;

/**
 * 群组文件批量下载构建资源服务类
 * 
 * @author xiexing
 * @date 2018年11月20日
 */
public class BuildBatchGrpFileResServiceImpl extends BuildResServiceBase implements InitializingBean {
  @Autowired
  private GrpFileDao grpFileDao;
  @Autowired
  private ArchiveFileDao archiveFileDao;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public void check(FileDownloadForm form) throws Exception {
    // TODO 群组文件是否存在
    List<V_GrpFile> availableGrpFileList = checkExistGrpFile(form.getFileIdList());
    if (CollectionUtils.isEmpty(availableGrpFileList)) {
      form.setResult(false);
      form.setResultMsg("文件不存在！");
      throw new FileNotExistException();
    }
    // 附件文件是否存在
    Map<Long, DownloadFileRes> availableArchiveFileMap = checkAvailableArchiveFile(availableGrpFileList);
    if (MapUtils.isEmpty(availableArchiveFileMap)) {
      form.setResult(false);
      form.setResultMsg("文件不存在！");
      throw new FileNotExistException();
    }
    form.setDownloadFileResMap(availableArchiveFileMap);
  }

  public List<V_GrpFile> checkExistGrpFile(List<Long> grpFileIds) {
    List<V_GrpFile> vGrpFileList = new ArrayList<V_GrpFile>();
    grpFileIds.forEach(id -> {
      V_GrpFile grpFile = grpFileDao.get(id);
      // 文件存在在状态正常
      if (Objects.nonNull(grpFile) && (Objects.isNull(grpFile.getFileStatus()) || grpFile.getFileStatus() == 0)) {
        vGrpFileList.add(grpFile);
      }
    });
    return vGrpFileList;
  }

  public Map<Long, DownloadFileRes> checkAvailableArchiveFile(List<V_GrpFile> availableGrpFileList) {
    Map<Long, DownloadFileRes> downloadFileResMap = new HashMap<Long, DownloadFileRes>();
    availableGrpFileList.forEach(grpFile -> {
      ArchiveFile af = archiveFileDao.get(grpFile.getArchiveFileId());
      if (Objects.nonNull(af) && (Objects.isNull(af.getStatus()) || af.getStatus() == 0)) {
        DownloadFileRes downloadFileRes = new DownloadFileRes(grpFile.getGrpFileId(), grpFile, af);
        downloadFileRes.setOwnerPsnId(grpFile.getUploadPsnId());
        downloadFileResMap.put(downloadFileRes.getId(), downloadFileRes);
      }
    });
    return downloadFileResMap;
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
    // TODO Auto-generated method stub
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    // 用于构造下载链接的domain不能使用https协议，要替换为http协议
    this.domainscm = this.domainscm.replace("https://", "http://");
  }
}
