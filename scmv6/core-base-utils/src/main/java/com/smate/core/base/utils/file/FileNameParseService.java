package com.smate.core.base.utils.file;

import java.io.Serializable;

/**
 * 文件名称、路径生成策略接口.
 * 
 * 现有两个实现:
 * 
 * 按文件名的hash值生成文件路径：{@link:com.iris.scm.system.file.HashFileNameParseService},该实现可以根据原文件名还原路径.
 * 
 * 按当前的时间戳生成文件路径：{@link:com.iris.scm.system.file.DateFileNameParseService}，该实现可以根据新文件名还原路径.
 * 
 * @author liqinghua
 * 
 */
public interface FileNameParseService extends Serializable {

  /**
   * 指定文件名，index0路径，index1文件名.
   * 
   * @param fileName
   * @return
   */
  public String[] generalDirWithFileName(String fileName);

  /**
   * 指定文件后缀(不包括.号)，生成唯一的文件名，index0路径，index1文件名.
   * 
   * @param fileName
   * @return
   */
  public String[] generalUniqueDirWithFileName(String fileExt);

  /**
   * 传入文件名，解析还原的文件路径(带文件名).
   * 
   * @param fileName
   * @return
   */
  public String parseFileNameDir(String fileName);
}
