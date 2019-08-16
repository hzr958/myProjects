package com.smate.web.v8pub.controller;

import com.smate.core.base.utils.common.WebObjectUtil;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.spring.SpringUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class FileOpenController {
  @Value("${domainscm}")
  private String domainscm;

  @RequestMapping(value = "/pub/one/openfile")
  @ResponseBody
  public void openfile() throws Exception {
    HttpServletRequest request = SpringUtils.getRequest();
    HttpServletResponse response = SpringUtils.getResponse();
    response.setCharacterEncoding("gbk");
    String fileRoot = domainscm + "/";
    fileRoot = fileRoot.substring(0, fileRoot.lastIndexOf('/'));
    int flag =
        Integer.parseInt(StringUtils.isNotBlank(request.getParameter("flag")) ? request.getParameter("flag") : "0");
    int type =
        Integer.parseInt(StringUtils.isNotBlank(request.getParameter("flag")) ? request.getParameter("type") : "0");

    String[] parameters = this.getDowLoadParameters(fileRoot, flag, type);
    /**
     * 20190529 所有链接强制为https，不需要再转为http进行文件下载
     */
    // if (StringUtils.isNotBlank(parameters[0])) { // https 协议不能下载文件
    // parameters[0] = parameters[0].replace("https://", "http://");
    // }
    try {
      URL fileUrl = new URL(parameters[0]);
      HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();

      response.reset();
      String agent = request.getHeader("USER-AGENT");
      if (agent != null
          && (agent.indexOf("MSIE") != -1 || agent.indexOf("rv:11.0") != -1 || agent.indexOf("Edge") != -1)) {
        response.setHeader("Content-Disposition",
            "attachment;filename=" + new String(parameters[1].getBytes("gbk"), "ISO-8859-1"));
      } else if (agent != null && agent.indexOf("Chrome") == -1 && agent.indexOf("Safari") != -1) {
        response.sendRedirect(parameters[0]);
        return;
      } else {
        // =_UTF-8_B_U01hdGVfT3V0cHV0X3poLnhscw==_=
        String fileName = parameters[1].substring(parameters[1].lastIndexOf("/") + 1, parameters[1].length());
        String enableFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
        response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);
      }
      if (agent != null && agent.contains("MSIE 8.0")) {
        response.setContentType("application/octet-stream'");
      } else {
        response.setContentType(connection.getContentType());
      }
      response.setContentLength(connection.getContentLength());

      DataInputStream in = new DataInputStream(connection.getInputStream());
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
    } catch (Exception ex) {
      String url = domainscm;
      url = url + "/pub/folder/openfilefail";
      response.sendRedirect(url);
    }
  }

  @RequestMapping(value = "/pub/folder/openfilefail")
  public ModelAndView enterFailPage() {
    ModelAndView view = new ModelAndView();
    view.setViewName("pub/folder/openfile-fail");
    return view;
  }

  /**
   * 获取下载参数.
   * 
   * @param fileRoot
   * @return
   * @throws Exception
   */
  private String[] getDowLoadParameters(String fileRoot, int flag, int type) throws Exception {
    String[] parameters = new String[3];

    try {
      if (flag == 9) { // 成果文件导入，excel下载
        String res = "ressns/";
        if (type == 1) {
          String language = LocaleContextHolder.getLocale().getLanguage();
          String filePath = ServiceConstants.PUBLCATION_IMPORT_FILE_PATH + "SMate_Output_" + language + ".xls";
          parameters[0] = fileRoot + "/" + res + filePath;
          parameters[1] = filePath.substring(filePath.indexOf("/") + 1, filePath.length());
        }
      }else if (flag == 10) { //  项目文件导入，excel下载
        String res = "ressns/";
        if (type == 1) {
          String language = LocaleContextHolder.getLocale().getLanguage();
          String filePath = ServiceConstants.PUBLCATION_IMPORT_FILE_PATH + "SMate_project_" + language + ".xls";
          parameters[0] = fileRoot + "/" + res + filePath;
          parameters[1] = filePath.substring(filePath.indexOf("/") + 1, filePath.length());
        }
      }else if (flag == 11) { //   群组文件，excel下载
        String res = "ressns/";
        if (type == 1) {
          String language = LocaleContextHolder.getLocale().getLanguage();
          String filePath = ServiceConstants.PUBLCATION_IMPORT_FILE_PATH + "smate_group" + ".xls";
          parameters[0] = fileRoot + "/" + res + filePath;
          parameters[1] = filePath.substring(filePath.indexOf("/") + 1, filePath.length());
        }
      }
      parameters[2] = WebObjectUtil.getFileNameExt(parameters[1]);
      return parameters;
    } catch (Exception e) {
      throw new Exception(e);
    }
  }
}
