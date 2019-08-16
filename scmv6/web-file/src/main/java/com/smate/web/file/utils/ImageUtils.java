package com.smate.web.file.utils;

import java.io.FileOutputStream;
import java.io.OutputStream;

import sun.misc.BASE64Decoder;

/**
 * 图片处理工具类
 * 
 * @author tsz
 *
 */
public class ImageUtils {

  /**
   * 通过base64编码转图片
   * 
   * @param imgStr
   * @return
   */
  public static boolean GenerateImage(String imgStr, String path) { // 对字节数组字符串进行Base64解码并生成图片
    if (imgStr == null) // 图像数据为空
      return false;

    BASE64Decoder decoder = new BASE64Decoder();
    try {
      // Base64解码
      byte[] b = decoder.decodeBuffer(imgStr);
      for (int i = 0; i < b.length; ++i) {
        if (b[i] < 0) {// 调整异常数据
          b[i] += 256;
        }
      }
      // 生成jpeg图片
      String imgFilePath = path;// 新生成的图片
      OutputStream out = new FileOutputStream(imgFilePath);
      out.write(b);
      out.flush();
      out.close();
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
