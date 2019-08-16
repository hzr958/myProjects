package com.smate.center.task.service.pdwh.quartz;

import com.smate.center.task.exception.InputFullTextTaskException;

import java.io.File;

public interface InputPubFulltextService {
  /**
   * 读取文件路径方法
   */
  public File readFile() throws InputFullTextTaskException;

  /**
   * 文件错误上传
   * 
   * @param file
   * @param fileName
   * @throws InputFullTextTaskException
   */
  public void changeErrorName(File file, String fileName) throws Exception;

  public void dealEiFile(String path) throws Exception;

  public void dealIsiFile(String path) throws Exception;

  public void dealRainPatFile(String path) throws Exception;

  public void dealCnkiFile(String path) throws Exception;

  public void dealDoiNameFile(String path) throws Exception;

  public void dealOalibNameFile(String path) throws Exception;

}
