package com.smate.core.base.utils.constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import sun.misc.BASE64Encoder;

/**
 * 图片base64编码
 * 
 * @author lhd
 *
 */
public class ImageBase64Encode {

  public static String getBase64Encode(String filePath) throws IOException {
    String base64Str = "";
    FileInputStream is = new FileInputStream(new File(filePath));
    byte[] buffer = new byte[is.available()];
    is.read(buffer);
    base64Str = new BASE64Encoder().encode(buffer);
    is.close();
    return base64Str;
  }
}
