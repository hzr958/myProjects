package com.smate.web.file.action;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.web.annotation.RequestMethod;
import com.smate.web.file.form.FileDownloadUrlForm;

/**
 * 获取文件下载地址的 action
 * 
 * @author houchuanjie
 * @date 2017年11月30日 下午5:40:24
 */
public class FileDownloadUrlAction extends ActionSupport implements ModelDriven<FileDownloadUrlForm>, Preparable {
  private static final long serialVersionUID = 1832736796288665642L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  private FileDownloadUrlForm form;

  @Autowired
  private FileDownloadUrlService fileDownUrlService;

  /**
   * 获取文件下载地址<br>
   * 各文件类型不同，需要传的id也不同，对应如下：<br>
   * 
   * <pre>
   * type     枚举类型                    id
   *  0   FileTypeEnum.PSN            v_psn_file.id
   *  1   FileTypeEnum.GROUP          v_grp_file.grp_file_id
   *  2   FileTypeEnum.SNS_FULLTEXT   pub_fulltext.pub_id
   *  3   FileTypeEnum.PDWH_FULLTEXT  pdwh_fulltext_file.pub_id
   * </pre>
   * 
   * @param type 文件类型，必填
   * @param id 文件id，非必填，但必须des3Id和id二选一
   * @param des3Id 加密的文件id，非必填，但必须des3Id和id二选一
   * @param shortUrl true 是否获取短地址
   * @return json格式字符串.<br>
   *         <br>
   *         获取成功样例1：<br>
   *         {status="success", msg="获取文件下载地址成功！", data={type="0",
   *         url="http://dev.scholarmate.com/F/5c95c6b2ecd78b4831360b60db6a7a2b"}}<br>
   *         <br>
   *         获取成功样例2：<br>
   *         {status="success", msg="获取文件下载地址成功！", data={type="1",
   *         url="http://dev.scholarmate.com/fileweb/filedownload?des3FileId=JvUzHyT7%2BGJQ%2BQdL%2BxTHRw%3D%3D&fileType=1&key=TV89"}}<br>
   *         <br>
   *         失败样例：<br>
   *         {status="error", msg="请求参数错误！"}
   * 
   * @author houchuanjie
   * @date 2017年12月4日 上午11:14:43
   */
  @RequestMethod(RequestMethod.POST)
  @Action("/fileweb/download/ajaxgeturl")
  public void getDownloadUrl() {
    try {
      String str = Struts2Utils.getHttpReferer();
      if (StringUtils.isNotBlank(str)) {
        URL url = new URL(str);
        String domain = url.getProtocol() + "://" + url.getHost();
        Struts2Utils.getResponse().addHeader("Access-Control-Allow-Origin", domain);
        Struts2Utils.getResponse().addHeader("Access-Control-Allow-Credentials", "true");
      }
    } catch (MalformedURLException e) {
    }
    if (form.validate()) {
      HashMap<String, String> dataMap = new HashMap<String, String>();
      dataMap.put("type", form.getType().toString());
      if (form.isShortUrl()) {
        String shortDownloadUrl = fileDownUrlService.getShortDownloadUrl(form.getType(), form.getId(), form.getId());
        dataMap.put("url", shortDownloadUrl);
      } else {
        String downloadUrl = "";
        if (FileTypeEnum.GROUP == form.getType()) {
          // 群组文件下载
          downloadUrl = fileDownUrlService.getDownloadUrl(form.getType(), form.getId(), 0L);
        } else {
          // 默认下载
          downloadUrl = fileDownUrlService.getDownloadUrl(form.getType(), form.getId());
        }
        dataMap.put("url", downloadUrl);
      }
      Struts2Utils.renderSuccessResult("获取文件下载地址成功！", dataMap);
    } else {
      Struts2Utils.renderErrorResult("请求参数错误！");
    }
  }

  /**
   * 获取文件下载地址<br>
   * 各文件类型不同，需要传的id也不同，对应如下：<br>
   * 
   * <pre>
   * type     枚举类型                    id
   *  0   FileTypeEnum.PSN            v_psn_file.id
   *  1   FileTypeEnum.GROUP          v_grp_file.grp_file_id
   *  2   FileTypeEnum.SNS_FULLTEXT   pub_fulltext.pub_id
   *  3   FileTypeEnum.PDWH_FULLTEXT  pdwh_fulltext_file.pub_id
   * </pre>
   * 
   * @param type 文件类型，必填
   * @param des3Ids 加密的文件id列表，必填，“,”分隔的字符串
   * @return json格式字符串.<br>
   *         <br>
   *         获取成功样例1：<br>
   *         {status="success", msg="获取文件批量下载地址成功！", data={type="0",
   *         url="http://dev.scholarmate.com/F/5c95c6b2ecd78b4831360b60db6a7a2b"}}<br>
   *         <br>
   *         获取成功样例2：<br>
   *         {status="success", msg="获取文件批量下载地址成功！", data={type="1",
   *         url="http://dev.scholarmate.com/fileweb/filedownload?des3FileId=JvUzHyT7%2BGJQ%2BQdL%2BxTHRw%3D%3D&fileType=1&key=TV89"}}<br>
   *         <br>
   *         失败样例：<br>
   *         {status="error", msg="请求参数错误！"}
   * 
   * @author houchuanjie
   * @date 2017年12月4日 上午11:14:43
   */
  @RequestMethod(RequestMethod.POST)
  @Action("/fileweb/batchdownload/ajaxgeturl")
  public void getBatchDownloadUrl() {
    if (form.validate()) {
      HashMap<String, String> dataMap = new HashMap<String, String>();
      dataMap.put("type", form.getType().toString());
      if (form.isShortUrl()) {
        /*
         * String shortDownloadUrl = fileDownUrlService.getShortDownloadUrl(form.getType(), form.getId());
         * dataMap.put("url", shortDownloadUrl);
         */
      } else {
        String downloadUrl = fileDownUrlService.getZipDownloadUrl(form.getType(), form.getDes3Ids());
        dataMap.put("url", downloadUrl);
      }
      Struts2Utils.renderSuccessResult("获取文件批量下载地址成功", dataMap);
    } else {
      Struts2Utils.renderErrorResult("请求参数错误");
    }
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FileDownloadUrlForm();
    }
  }

  @Override
  public FileDownloadUrlForm getModel() {
    return form;
  }

}
