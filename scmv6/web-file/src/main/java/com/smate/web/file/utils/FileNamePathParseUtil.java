package com.smate.web.file.utils;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * hash 模式 文件名字路径解析器
 * 
 * 生成随机的名字
 * 
 * @author tsz
 *
 */
public class FileNamePathParseUtil {

  /**
   * 指定文件名，index0路径，index1文件名.
   * 
   * 该生成方式根据文件名称的hash值生成路径，不会改变文件名的值(index1==fileName)，(也可以制定文件名字) 根据原文件名调用parseFileNameDir可以还原路径.
   * 
   * @param fileName
   * @return
   */
  public static String[] generalDirWithFileName(String fileName) {

    String dir = getFileDir(fileName);
    String[] dirName = new String[] {dir, fileName};
    return dirName;
  }

  /**
   * 指定文件后缀(不包括.号)，生成唯一的文件名，index0路径，index1文件名.
   * 
   * 该生成方式根据文件名称的hash值生成路径，根据文件名调用parseFileNameDir可以还原路径.
   * 
   * @param fileName
   * @return
   */
  public static String[] generalUniqueDirWithFileName(String fileExt) {

    String newFileName = UUID.randomUUID().toString().replace("-", "");
    if (fileExt != null) {
      newFileName += "." + fileExt;
    }
    String dir = getFileDir(newFileName);
    String[] dirName = new String[] {dir, newFileName};
    return dirName;
  }

  /**
   * 解析文件 路径 根据名字 解析文件路径
   * 
   * @param fileName
   * @return
   */
  public static String parseFileNameDir(String fileName) {

    return getFileDir(fileName) + fileName;
  }

  public static String getFileDir(String fileName) {

    String secr = DigestUtils.md5Hex(fileName);
    StringBuilder sb = new StringBuilder("/");
    sb.append(secr.substring(1, 2)).append(secr.substring(3, 4)).append("/");
    sb.append(secr.substring(5, 6)).append(secr.substring(7, 8)).append("/");
    sb.append(secr.substring(9, 10)).append(secr.substring(11, 12)).append("/");
    String dir = sb.toString();
    return dir;
  }

  public static void main(String[] args) {

    System.out.println(generalDirWithFileName("tanshaozhi")[0].toString());
    System.out.println(generalDirWithFileName("tanshaozhi")[1].toString());

    System.out.println(getFileDir("6e107d70574346faba54b9dd41be6276.ppt"));
  }

}
