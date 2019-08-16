package com.smate.web.pub.service.email;

import java.util.Map;

/**
 * 下载全文邮件服务
 * 
 * @author lhd
 *
 */
public interface DownloadYourPubEmailService {
  // 没全文的默认图片
  public final static String DEFAULT_PUBFULLTEXT_IMAGE = "/resscmwebsns/images_v5/images2016/file_img.jpg";
  // 有全文的默认图片
  public final static String DEFAULT_PUBFULLTEXT_IMAGE1 = "/resscmwebsns/images_v5/images2016/file_img1.jpg";

  /**
   * 发送下载全文邮件
   * 
   * @param map
   * @throws Exception
   */
  public void sendDownloadFulltextMail(Map<String, Object> map) throws Exception;

}
