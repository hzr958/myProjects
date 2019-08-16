package com.smate.web.file.service.download;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.file.dao.ArchiveFileDao;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.file.dao.rol.RolArchiveFileDao;
import com.smate.web.file.exception.FileNotExistException;
import com.smate.web.file.form.FileDownloadForm;

/**
 * 权限检查基础类
 * 
 * @author tsz
 *
 */
@Transactional(rollbackOn = Exception.class)
public abstract class BuildResServiceBase implements BuildResService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 文件上下文路径 区分文件要放到哪个文件夹上
   */
  protected String basicPath = "upfile";

  @Autowired
  protected ArchiveFileDao archiveFileDao;
  @Autowired
  protected RolArchiveFileDao rolArchiveFileDao;
  @Resource(name = "restTemplate")
  protected RestTemplate restTemplate;
  @Value("${domainscm}")
  protected String domainscm;

  /**
   * 检验
   * 
   * @param form
   * @return
   */
  public abstract void check(FileDownloadForm form) throws Exception;

  /**
   * 构建文件真实连接
   * 
   * @param form
   * @return
   */
  public abstract void buildResUrl(FileDownloadForm form) throws Exception;

  /**
   * 检查权限
   * 
   * @param form
   * @return
   * @throws Exception
   */
  @Override
  public void build(FileDownloadForm form) throws Exception {

    // 检查
    check(form);
    // 构建地址
    buildResUrl(form);
    // 扩展方法可以空实现(应该放在下载完成后执行)
    // extend(form);
  }

  public void checkArchiveFile(Long archiveFileId, FileDownloadForm form) throws Exception {
    ArchiveFile file = archiveFileDao.get(archiveFileId);
    if (file == null || (file.getStatus() != null && file.getStatus() == 1)) { // 需要加状态位
      form.setResult(false);
      form.setResultMsg("文件不存在！");
      throw new FileNotExistException();
    }
    form.setArchiveFile(file);
  }

  /* *//**
        * 查看附件下载
        * 
        * @return
        *//*
           * public void checkAttachmentArchiveFile(Long archiveFileId, FileDownloadForm form) throws
           * Exception { ArchiveFile file = archiveFileDao.get(archiveFileId); if (file == null ||
           * (file.getStatus() != null && file.getStatus() == 1)) { // 需要加状态位 form.setResult(false);
           * form.setResultMsg("文件不存在！"); throw new FileNotExistException(); RolArchiveFile rolfile =
           * rolArchiveFileDao.get(archiveFileId); if (rolfile == null) { form.setResult(false);
           * form.setResultMsg("文件不存在！"); throw new FileNotExistException(); } else {
           * form.setRolArchiveFile(rolfile); } } else { form.setArchiveFile(file); } }
           */

  public String getBasicPath() {
    return basicPath;
  }

  public void setBasicPath(String basicPath) {
    this.basicPath = basicPath;
  }

  /**
   * 查询远程成果的信息
   * 
   * @param pubQueryDTO
   * @param SERVER_URL
   * @return
   */
  public Object getRemotePubInfo(Object obj, String SERVER_URL) {
    // 设置请求头部
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> requestEntity = new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(obj), requestHeaders);
    Object object = restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    return object;
  }

}
