package com.smate.center.batch.service.utils;

import java.io.File;
import java.io.IOException;
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

  public static void main(String[] args) throws IOException {

    // System.out.println(generalDirWithFileName("tanshaozhi")[0].toString());
    // System.out.println(generalDirWithFileName("tanshaozhi")[1].toString());

    System.out.println(parseFileNameDir("e7d12b7fcb924b8ab2d515aba75d7c51.pdf"));
    System.out.println(parseFileNameDir("9b774e714fa74862b40f290b3cda5d4e.pdf"));
    System.out.println(parseFileNameDir("47e9a9e4a3a94d01a3e3fe6122eb761c.pdf"));
    System.out.println(parseFileNameDir("3d87e49c8fa145c5bc57963f57b9e21f.pdf"));
    System.out.println(parseFileNameDir("ffa3dbc0b8c84f43a4a6ce0a5fc11ff8.pdf"));
    File f = new File("E:/upfile/" + parseFileNameDir("e7d12b7fcb924b8ab2d515aba75d7c51.pdf"));
    f.mkdirs();
    File f1 = new File("E:/upfile/" + parseFileNameDir("9b774e714fa74862b40f290b3cda5d4e.pdf"));
    f1.mkdirs();
    File f2 = new File("E:/upfile/" + parseFileNameDir("47e9a9e4a3a94d01a3e3fe6122eb761c.pdf"));
    f2.mkdirs();
    File f3 = new File("E:/upfile/" + parseFileNameDir("3d87e49c8fa145c5bc57963f57b9e21f.pdf"));
    f3.mkdirs();
    File f4 = new File("E:/upfile/" + parseFileNameDir("ffa3dbc0b8c84f43a4a6ce0a5fc11ff8.pdf"));
    f4.mkdirs();

    // System.out.println(getFileDir("11b27436ee354bdf8f4a8eb57a74a618.apk"));
  }

}
