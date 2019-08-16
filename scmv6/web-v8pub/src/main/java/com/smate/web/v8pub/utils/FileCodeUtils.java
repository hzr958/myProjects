package com.smate.web.v8pub.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.any23.encoding.TikaEncodingDetector;

/**
 * 文件编码格式工具类
 * 
 * @author YJ
 *
 *         2018年11月21日
 */
public class FileCodeUtils {

  /**
   * 获取文件流内的文件编码格式 此方法不准确，废用
   * 
   * @param inputStream
   * @return
   * @throws Exception
   */
  public static String getFileCharset(InputStream inputStream) throws Exception {
    BufferedInputStream bin = new BufferedInputStream(inputStream);
    int p = (bin.read() << 8) + bin.read();
    bin.close();
    String code = null;
    switch (p) {
      case 0xefbb:
        code = "UTF-8";
        break;
      case 0xfffe:
        code = "Unicode";
        break;
      case 0xfeff:
        code = "UTF-16BE";
        break;
      default:
        code = "UTF-8";
    }
    return code;
  }

  public static Charset guessCharset(InputStream is) throws IOException {
    return Charset.forName(new TikaEncodingDetector().guessEncoding(is));
  }

}
