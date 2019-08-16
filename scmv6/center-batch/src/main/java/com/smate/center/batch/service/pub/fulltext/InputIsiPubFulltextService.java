package com.smate.center.batch.service.pub.fulltext;

import java.io.File;

import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * isi全文导入任务服务类
 * 
 * @author tsz
 *
 */
public interface InputIsiPubFulltextService {

  /**
   * 任务调用 读取文件路径方法
   */
  public void readFile() throws BatchTaskException;


  /**
   * 任务调用 文件处理方法
   * 
   * @param filePath
   */
  public void dealFile(String filePath) throws BatchTaskException;


  public String getMarkByStrategy(String inputFullTextQuartz);


  public String getSourceFileDir();


  public void changeErrorName(File file, String fileName) throws BatchTaskException;


}
