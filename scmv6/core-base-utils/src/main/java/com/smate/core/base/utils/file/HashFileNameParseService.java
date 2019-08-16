package com.smate.core.base.utils.file;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

/**
 * 根据文件名称hash值生成路径.
 * 
 * @author liqinghua
 * 
 */
@Service("fileNameParseService")
public class HashFileNameParseService implements FileNameParseService {

  /**
   * 
   */
  private static final long serialVersionUID = -7233891235259097353L;

  /**
   * 指定文件名，index0路径，index1文件名.
   * 
   * 该生成方式根据文件名称的hash值生成路径，不会改变文件名的值(index1==fileName)， 根据原文件名调用parseFileNameDir可以还原路径.
   * 
   * @param fileName
   * @return
   */
  public String[] generalDirWithFileName(String fileName) {

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
  public String[] generalUniqueDirWithFileName(String fileExt) {

    String newFileName = UUID.randomUUID().toString().replace("-", "");
    if (fileExt != null) {
      newFileName += "." + fileExt;
    }
    String dir = getFileDir(newFileName);
    String[] dirName = new String[] {dir, newFileName};
    return dirName;
  }

  @Override
  public String parseFileNameDir(String fileName) {

    return this.getFileDir(fileName) + fileName;
  }

  private String getFileDir(String fileName) {

    String secr = DigestUtils.md5Hex(fileName);
    StringBuilder sb = new StringBuilder("/");
    sb.append(secr.substring(1, 2)).append(secr.substring(3, 4)).append("/");
    sb.append(secr.substring(5, 6)).append(secr.substring(7, 8)).append("/");
    sb.append(secr.substring(9, 10)).append(secr.substring(11, 12)).append("/");
    String dir = sb.toString();
    return dir;
  }

  public static void main(String[] args) {

    HashFileNameParseService service = new HashFileNameParseService();

    System.out.println(service.getFileDir("6e107d70574346faba54b9dd41be6276.ppt"));
  }
}
