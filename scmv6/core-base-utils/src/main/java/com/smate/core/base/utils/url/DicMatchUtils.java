package com.smate.core.base.utils.url;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 字典匹配字符串
 * 
 * @author zzx
 *
 */
public class DicMatchUtils {

  /**
   * 搜索匹配内容
   * 
   * @param list
   * @param str
   * @return
   */
  public static List<String> matchList(List<String> list, String str) {
    if (list == null || list.size() == 0 || StringUtils.isBlank(str)) {
      return null;
    }
    List<String> result = new ArrayList<String>();
    Iterator<String> it = list.iterator();
    String one = null;
    while (it.hasNext()) {
      one = it.next();
      if (str.length() >= one.length() && str.indexOf(one) != -1 && !result.contains(one)) {
        result.add(one);
      }
    }
    return result;
  }

  /**
   * 文件转list-建议放内存
   * 
   * @param filePath
   * @param encoding
   * @return
   * @throws Exception
   */
  public static List<String> fileToList(String filePath, String encoding) throws Exception {
    List<String> result = null;
    BufferedReader bf = null;
    if (StringUtils.isBlank(filePath) || StringUtils.isBlank(encoding)) {
      throw new Exception("文件路径或编码为空");
    }
    try {
      File file = new File(filePath);
      if (file != null && file.isFile() && file.exists()) {
        InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
        bf = new BufferedReader(read);
        if (bf != null) {
          String str = null;
          result = new ArrayList<String>();
          while (true) {
            if ((str = bf.readLine()) != null) {
              if (StringUtils.isNotBlank(str)) {
                result.add(str.trim());
              }
            } else {
              break;
            }
          }
          bf.close();
        }
      } else {
        throw new Exception("文件不存在");
      }
    } catch (Exception e) {
      throw new Exception("文件解析异常", e);
    } finally {
      if (bf != null) {
        bf.close();
      }
    }
    return result;
  }
}
