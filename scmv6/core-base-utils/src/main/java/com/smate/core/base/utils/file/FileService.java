package com.smate.core.base.utils.file;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * 文件操作服务接口.
 * 
 * @author liqinghua
 * 
 */
public interface FileService extends Serializable {

  /**
   * 获取文件路径.
   *
   * @param fileName
   * @return
   */
  String getFilePath(String fileName);

  /**
   * 读取指定文件.
   * 
   * @param baseDir
   * @param fileName
   * @return
   * @throws IOException
   */
  File readFile(String fileName, String baseDir) throws IOException;

  /**
   * 读取指定文件名的字符串内容，可指定字符串编码.
   * 
   * @param fileName
   * @param baseDir
   * @param encoding
   * @return
   * @throws IOException
   */
  String readText(String fileName, String baseDir, String encoding) throws IOException;

  /**
   * 读取指定文件名的字符串内容，如果文件不存在，则返回'',可指定字符串编码.
   * 
   * @param fileName
   * @param baseDir
   * @param encoding
   * @return
   * @throws IOException
   */
  String readTextTrimEmpty(String fileName, String baseDir, String encoding) throws IOException;

  /**
   * 保存字符串到文件，可指定字符串编码(也可为空),返回保存后的文件名.
   * 
   * @param text
   * @param fileName
   * @param baseDir
   * @param encoding
   * @return
   * @throws IOException
   */
  String writeText(String text, String baseDir, String fileName, String encoding) throws IOException;

  /**
   * 保存字符串到唯一文件名，须指定文件后缀（不包含.号）,返回保存后的文件名.
   * 
   * @param text
   * @param baseDir
   * @param fileExt
   * @param encoding
   * @return
   * @throws IOException
   */
  String writeUniqueText(String text, String baseDir, String fileExt, String encoding) throws IOException;

  /**
   * 保存文件，指定文件名，返回保存后的文件名.
   * 
   * @param fileData
   * @param baseDir
   * @param fileName
   * @return
   * @throws IOException
   */
  String writeFile(File fileData, String baseDir, String fileName) throws IOException;

  /**
   * 保存文件为唯一文件名，须指定文件后缀（不包含.号），返回保存后的文件名.
   * 
   * @param fileData
   * @param baseDir
   * @param fileExt
   * @return
   * @throws IOException
   */
  String writeUniqueFile(File fileData, String baseDir, String fileExt) throws IOException;

  String copyUniqueFile(String url, String baseDir, String fileExt) throws IOException;

}
