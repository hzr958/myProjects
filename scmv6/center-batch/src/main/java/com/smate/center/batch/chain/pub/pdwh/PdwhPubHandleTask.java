package com.smate.center.batch.chain.pub.pdwh;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubImportContext;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;

/**
 * @author hzr 基准库XML过程任务单元
 */
public interface PdwhPubHandleTask {
  /**
   * 任务单元名称.
   * 
   * @return Sting
   * 
   */
  String getName();

  /**
   * 是否可以运行本任务单元.
   * 
   * @param PdwhPublication pdwhPub
   * @param PdwhPubImportContext context
   * @return boolean
   */
  boolean can(PdwhPublication pdwhPub, PdwhPubImportContext context);

  /**
   * 执行本任务单元.
   * 
   * @param PdwhPublication pdwhPub
   * @param PdwhPubImportContext context
   * @return boolean
   * @throws Exception Exception
   */
  boolean run(PdwhPublication pdwhPub, PdwhPubImportContext context) throws Exception;
}
