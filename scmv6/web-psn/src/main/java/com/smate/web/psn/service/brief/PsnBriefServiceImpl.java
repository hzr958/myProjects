package com.smate.web.psn.service.brief;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.exception.PsnException;

/**
 * 个人简介 服务实现
 * 
 * @author zzx
 *
 */
@Service("psnBriefService")
public class PsnBriefServiceImpl implements PsnBriefService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private FileService fileService;
  @Value("${file.root}")
  private String fileroot;

  @Override
  public String getPsnBrief(Long psnId) throws PsnException {
    Assert.notNull(psnId);
    try {
      return this.fileService.readTextTrimEmpty(getBriefFileName(psnId), this.getRootFolder() + "/brief", "utf-8");
    } catch (IOException e) {
      logger.error("获取人员简介信息出错", e);
      throw new PsnException("获取人员简介信息出错", e);
    }
  }

  /**
   * 根据psnId获取简介文件名
   * 
   * @param psnId
   * @return
   * @throws PsnException
   */
  private String getBriefFileName(Long psnId) throws PsnException {
    @SuppressWarnings("deprecation")
    Integer nodeId = SecurityUtils.getCurrentAllNodeId().get(0);
    String fileName = String.format("brief-%s-%s.txt", nodeId, psnId);
    return fileName;
  }

  public void setRootFolder(String fileroot) {
    Assert.notNull(fileroot);
    fileroot = fileroot.replace("\\", "/");
    if (fileroot.endsWith("/")) {
      fileroot = fileroot.substring(0, fileroot.length() - 1);
    }
    this.fileroot = fileroot;
  }

  public String getRootFolder() {
    return fileroot;
  }
}
