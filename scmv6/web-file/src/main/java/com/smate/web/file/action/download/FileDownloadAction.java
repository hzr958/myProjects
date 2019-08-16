package com.smate.web.file.action.download;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.file.FileUtils.FileNameExtension;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.file.exception.FileDownloadNoPermissionException;
import com.smate.web.file.exception.FileDownloadTimeOutException;
import com.smate.web.file.exception.FileNotExistException;
import com.smate.web.file.form.FileDownloadForm;
import com.smate.web.file.service.download.FileDownloadService;
import com.smate.web.file.service.record.FileDownloadRecordService;
import com.smate.web.file.utils.ZipUtil;

/**
 * 文件上传 支持多个文件上传
 *
 * @author tsz
 *
 */
@Results({@Result(name = "fileNotExit", location = "/WEB-INF/jsp/fileNotExit.jsp"),
    @Result(name = "fileWithTimeOut", location = "/WEB-INF/jsp/fileWithTimeOut.jsp"),
    @Result(name = "fileWithNoPermission", location = "/WEB-INF/jsp/fileWithNoPermission.jsp")})
public class FileDownloadAction extends ActionSupport implements Preparable, ModelDriven<FileDownloadForm> {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private static final long serialVersionUID = -384378994398035107L;

  private FileDownloadForm form;

  /**
   * 文件根路径
   */
  @Value("${file.root}")
  private String rootPath;

  @Value("${domainscm}")
  private String domainscm;

  @Resource(name = "fileDownloadService")
  private FileDownloadService fileDownloadService;

  @Resource(name = "batchFileDownloadService")
  private FileDownloadService batchFileDownloadService;

  @Autowired
  private FileDownloadRecordService fileDownloadRecordService;

  private String realUrlParamet;

  /**
   * 文件下载
   *
   * @return
   * @throws Exception
   */
  @Action("/fileweb/filedownload")
  public String fileDownload() throws Exception {
    String value = Struts2Utils.getParameter(FileDownloadUrlService.DOWNLOAD_KEY);
    if (StringUtils.isBlank(value)) {
      return "fileNotExit";
    }
    form.setKey(value);// 设置缓存KEY
    try {
      fileDownloadService.buildDownloadRes(form);
      /*
       * if (form.getFormSie() == true) { Struts2Utils.getResponse().sendRedirect(form.getResPath());
       * return null; }
       */
    } catch (FileNotExistException e) {
      // 文件不存在
      logger.error("该文件在数据库表记录中不存在或已删除! 请求表单参数：{}", form.toString(), e);
      return "fileNotExit";
    } catch (FileDownloadTimeOutException e) {
      // 缓存key超时
      logger.error("缓存key超时! 请求表单参数：{}", form.toString(), e);
      return "fileWithTimeOut";
    } catch (FileDownloadNoPermissionException e) {
      // 没有下载该文件的权限
      logger.error("用户没有下载权限! 请求表单参数：{}", form.toString());
      if (NumberUtils.isNullOrZero(SecurityUtils.getCurrentUserId())) {
        // 获取当前访问成果的url,返回点击下载的页面，而不是返回到下载的链接，不然一登录就会开始下载
        String curUrl = Struts2Utils.getHttpReferer();
        // 跳转登录界面进行登录
        Struts2Utils.redirect(domainscm + "/oauth/index?sys=SNS" + "&service=" + Des3Utils.encodeToDes3(curUrl));
        return null;
      }
      return "fileWithNoPermission";
    } catch (NumberFormatException | NullPointerException e) {
      logger.error("文件类型不正确，非数字或为Null！请求表单参数：{}", form.toString());
      return "fileNotExit";
    } catch (IllegalArgumentException e) {
      logger.error("文件类型不正确！{}， 请求表单参数：{}", e.getMessage(), form.toString());
      return "fileNotExit";
    }

    try {
      download();
      // 保存文件下载记录
      fileDownloadRecordService.saveRecord(form.getFileType(), form.getArchiveFile().getFileId(), form.getOwnerPsnId(),
          SecurityUtils.getCurrentUserId());
      fileDownloadService.downloadAfterExtend(form);// 执行下载后的扩展操作
    } catch (FileNotFoundException e) {
      // 文件不存在
      logger.warn("文件下载出错：文件不存在! 请求表单参数：{}", form.toString());
      logger.warn("文件路径：{}", e.getMessage());
      return "fileNotExit";
    }
    return null;
  }

  private void download() throws Exception {
    ArchiveFile af = form.getArchiveFile();
    HttpServletResponse response = Struts2Utils.getResponse();
    HttpServletRequest request = Struts2Utils.getRequest();

    /**
     * 下载链接改用https， .replace("https://", "http://")
     */
    URL fileUrl = new URL(domainscm + form.getResPath());
    HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
    // 设置 HttpURLConnection的字符编码
    connection.setRequestProperty("Accept-Charset", "UTF-8");
    /*
     * hcj SCM-14409 改造全文下载时发现的问题： 这里一定要放在设置response的一系列信息之前。 因为如果文件不存在，下面这行代码会报FileNotFoundException，
     * 如果放在response设置之后，会导致客户端仍然打开输出流进行下载， 下载的文件也是一个伪文件，没有任何内容的假文件。
     */
    DataInputStream in = new DataInputStream(connection.getInputStream());

    response.reset();
    // response.setCharacterEncoding("gbk");
    // 文件下载内容编码应该设置成UTF-8
    response.setCharacterEncoding("UTF-8");
    String agent = (String) request.getHeader("USER-AGENT");
    /*
     * if (agent.contains("MSIE 8.0")) { response.setContentType("application/octet-stream'"); } else {
     * response.setContentType(connection.getContentType()); }
     */
    // WSN-2017-10-26 移动端下载没反应
    if (agent.contains("MSIE 8.0")) {
      response.setContentType("application/octet-stream'");
    } else {
      if (SmateMobileUtils.isMobileBrowser(agent)) {
        if (SmateMobileUtils.isIphone(agent)) {
          response.setContentType(connection.getContentType());
        } else {
          response.setContentType("application/octet-stream");
        }
      } else {
        response.setContentType(connection.getContentType());
      }
    }
    // 文件名先转义，SCM-20717
    af.setFileName(StringEscapeUtils.unescapeHtml(af.getFileName()));
    if (agent != null && agent.indexOf("MSIE") == -1) {
      if (SmateMobileUtils.isMobileBrowser(agent)) {
        // SCM-23917 安卓手机微信扫描首页下载APP的二维码，自动跳转浏览器下载
        if (SmateMobileUtils.isWeChatBrowser(agent) && af.getFileName().contains(".apk")) {
          response.setHeader("Content-type", "text/plain;charset=UTF-8");
          response.setHeader("Accept-Ranges", "bytes 0-1/1");
          response.setHeader("Content-Disposition", "attachment;filename=Scholarmate.apk");
          response.setHeader("status", "206");
        } else {
          response.setHeader("Content-Disposition", "attachment;filename=" + StringUtils
              .replace(java.net.URLEncoder.encode(af.getFileName().replaceAll("%", ""), "UTF-8"), "+", "%20"));
        }
      } else {
        if (agent.indexOf("Windows NT") == -1 || agent.indexOf("Firefox") != -1) { // 解决IE11中文名乱码问题
          String enableFileName =
              "=?UTF-8?B?" + (new String(Base64.encodeBase64(af.getFileName().getBytes("UTF-8")))) + "?=";
          response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);
        } else {
          response.setHeader("Content-Disposition", "attachment;filename="
              + StringUtils.replace(java.net.URLEncoder.encode(af.getFileName(), "UTF-8"), "+", "%20"));
        }
      }

    } else {
      response.setHeader("Content-Disposition", "attachment;filename="
          + StringUtils.replace(java.net.URLEncoder.encode(af.getFileName(), "UTF-8"), "+", "%20"));
    }

    response.setContentLength(connection.getContentLength());
    OutputStream out = response.getOutputStream();
    BufferedInputStream buf = new BufferedInputStream(in);
    int len = 0;
    byte[] buffer = new byte[in.available()];
    while ((len = buf.read(buffer)) > 0) {
      out.write(buffer, 0, len);
    }
    out.flush();
    out.close();
    buf.close();

  }

  /**
   * 文件下载 (短地址用)
   * 
   * @return
   * @throws Exception
   */
  @Action("/fileweb/filedownload1")
  public String fileDownload1() throws Exception {
    // 短地址跳转
    if (StringUtils.isNotBlank(this.getRealUrlParamet())) {
      Map<String, String> map = JacksonUtils.jsonToMap(this.getRealUrlParamet());
      if (map != null) {
        form.setFileId(Long.parseLong(Des3Utils.decodeFromDes3(map.get("des3FileId"))));
        if (StringUtils.isNoneBlank(map.get("pubId"))) {
          form.setPubId(Long.parseLong(map.get("pubId")));
        }
        form.setFileType(map.get("fileType"));
        // 是站外短地址
        form.setShortUrl(true);
      }
    }

    if (form.getFileId() == null) {
      return "fileNotExit";
    }
    try {
      fileDownloadService.buildDownloadRes(form);
    } catch (FileDownloadTimeOutException e) {
      // 缓存key超时
      logger.warn("缓存key超时! 请求表单参数：{}", form.toString());
      return "fileWithTimeOut";
    } catch (FileNotExistException e) {
      // 文件不存在
      logger.error("文件不存在" + form.toString(), e);
      return "fileNotExit";
    } catch (FileDownloadNoPermissionException e) {
      // 没有下载该文件的权限
      logger.error("没有下载权限" + form.toString());
      return "fileWithNoPermission";
    }
    try {
      download();
      // 保存文件下载记录
      fileDownloadRecordService.saveRecord(form.getFileType(), form.getArchiveFile().getFileId(), form.getOwnerPsnId(),
          SecurityUtils.getCurrentUserId());
      fileDownloadService.downloadAfterExtend(form);// 执行下载后的扩展操作
    } catch (FileNotFoundException e) {
      // 文件不存在
      logger.warn("文件下载出错：文件不存在! 请求表单参数：{}", form.toString(), e);
      return "fileNotExit";
    }
    return null;

  }

  /**
   * 批量文件打包下载
   *
   * @author houchuanjie
   * @date 2018年3月6日 上午11:35:31
   * @return
   * @throws Exception
   */
  @Action("/fileweb/batchDownload")
  public String batchFileDownloadZip() throws Exception {
    if (StringUtils.isBlank(form.getDes3fids())) {
      return "fileNotExit";
    }
    try {
      batchFileDownloadService.buildDownloadRes(form);
      /*
       * if (form.getFormSie() == true) { Struts2Utils.getResponse().sendRedirect(form.getResPath());
       * return null; }
       */
    } catch (FileNotExistException e) {
      // 文件不存在
      logger.warn("该文件在数据库表记录中不存在或已删除! 请求表单参数：{}", form.toString());
      return "fileNotExit";
    } catch (FileDownloadTimeOutException e) {
      // 缓存key超时
      logger.warn("缓存key超时! 请求表单参数：{}", form.toString());
      return "fileWithTimeOut";
    } catch (FileDownloadNoPermissionException e) {
      // 没有下载该文件的权限
      logger.warn("用户没有下载权限! 请求表单参数：{}", form.toString());
      return "fileWithNoPermission";
    } catch (NumberFormatException | NullPointerException e) {
      logger.warn("文件类型不正确，非数字或为Null！请求表单参数：{}", form.toString());
      return "fileNotExit";
    } catch (IllegalArgumentException e) {
      logger.warn("文件类型不正确！{}， 请求表单参数：{}", e.getMessage(), form.toString());
      return "fileNotExit";
    } catch (Exception e) {
      logger.error("文件下载发生异常！请求表单参数={}", form.toString(), e);
      return "fileNotExit";
    }
    try {
      batchDownload();
      // 保存文件下载记录
      fileDownloadRecordService.saveRecords(form.getFileType(), form.getDownloadFileResMap(),
          SecurityUtils.getCurrentUserId());
    } catch (FileNotFoundException e) {
      // 文件不存在
      logger.warn("文件下载出错：文件不存在! 请求表单参数：{}", form.toString(), e);
      return "fileNotExit";
    }
    return null;
  }

  /**
   * 批量打包下载
   * 
   * @author houchuanjie
   * @date 2018年3月6日 下午5:35:15
   */
  private void batchDownload() throws Exception {
    // TODO 文件打包下载
    HttpServletResponse response = Struts2Utils.getResponse();
    HttpServletRequest request = Struts2Utils.getRequest();
    // 将资源文件打包，得到zip压缩包的路径地址
    String zipFilePath = ZipUtil.zipFile(form.getDownloadFileResMap(), rootPath);
    File file = new File(zipFilePath);
    response.reset();
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/zip");
    /*
     * response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
     */
    String fileName = getText("fileDownload.text.batchFileName") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
        + FileUtils.SYMBOL_DOT_CHAR + FileNameExtension.ZIP.toString();
    // 解决中文名在各浏览器下乱码等问题
    String agent = (String) request.getHeader("USER-AGENT");
    // 火狐中文乱码
    if (StringUtils.containsIgnoreCase(agent, "firefox")) {
      fileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
    } else { // 其他
      fileName = URLEncoder.encode(fileName, "UTF-8");
    }
    response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
    response.setContentLength(new Long(file.length()).intValue());

    BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
    OutputStream out = response.getOutputStream();
    int len = 0;
    byte[] buffer = new byte[in.available()];
    while ((len = in.read(buffer)) > 0) {
      out.write(buffer, 0, len);
    }
    out.flush();
    out.close();
    in.close();

    // 删除临时压缩文件
    file.delete();
  }

  @Override
  public FileDownloadForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FileDownloadForm();
    }

  }

  public FileDownloadForm getForm() {
    return form;
  }

  public void setForm(FileDownloadForm form) {
    this.form = form;
  }

  public String getRootPath() {
    return rootPath;
  }

  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  public String getRealUrlParamet() {
    return realUrlParamet;
  }

  public void setRealUrlParamet(String realUrlParamet) {
    this.realUrlParamet = realUrlParamet;
  }

}
