package com.smate.core.base.utils.file;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 根据时间的文件名称、路径生成策略.
 * 
 * @author liqinghua
 * 
 */
public class DateFileNameParseService implements FileNameParseService {

  /**
   * 
   */
  private static final long serialVersionUID = -7233891235259097353L;

  /**
   * 指定文件名，index0路径，index1文件名.
   * 
   * index1 != fileName,index1包含文件目录信息，调用parseFileNameDir可解析还原文件路径.
   * 
   * @param fileName
   * @return
   */
  public String[] generalDirWithFileName(String fileName) {

    String[] dir = this.generalDirFileName(fileName);
    return dir;
  }

  /**
   * 指定文件后缀(不包括.号)，生成唯一的文件名，index0路径，index1文件名.
   * 
   * index1包含文件目录信息，调用parseFileNameDir可解析还原文件路径.
   * 
   * @param fileName
   * @return
   */
  public String[] generalUniqueDirWithFileName(String fileExt) {

    String newFileName = UUID.randomUUID().toString().replace("-", "");
    if (fileExt != null) {
      newFileName += "." + fileExt;
    }
    String[] dir = this.generalDirFileName(newFileName);
    return dir;
  }

  @Override
  public String parseFileNameDir(String fileName) {

    if (fileName.matches("^[\\d]{12}.*$")) {
      StringBuilder sb = new StringBuilder();
      sb.append(fileName.substring(0, 4)).append("/");
      sb.append(fileName.substring(4, 6)).append("/");
      sb.append(fileName.substring(6, 8)).append("/");
      sb.append(fileName.substring(8, 10)).append("/");
      sb.append(fileName.substring(10, 12)).append("/");
      sb.append(fileName);
      return sb.toString();
    }
    return fileName;
  }

  private String[] generalDirFileName(String fileName) {
    Date now = new Date();
    SimpleDateFormat dirFt = new SimpleDateFormat("yyyy/MM/dd/HH/mm/");
    SimpleDateFormat nameFt = new SimpleDateFormat("yyyyMMddHHmm");
    String[] obj = new String[] {dirFt.format(now), nameFt.format(now) + fileName};
    return obj;
  }

  public static void main(String[] args) {

    DateFileNameParseService service = new DateFileNameParseService();

    service.parseFileNameDir("200901020101aaa.txt");
  }
}
