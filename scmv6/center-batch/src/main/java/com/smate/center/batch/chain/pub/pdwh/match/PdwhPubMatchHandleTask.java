package com.smate.center.batch.chain.pub.pdwh.match;

import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

/**
 * 
 * @author LIJUN
 * @date 2018年3月19日
 */
public interface PdwhPubMatchHandleTask {
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
   * @param String context
   * @return boolean
   */
  boolean can(PubPdwhDetailDOM pdwhPub, String context);

  /**
   * 执行本任务单元.
   * 
   * @param PdwhPublication pdwhPub
   * @param String context
   * @return boolean
   * @throws Exception Exception
   */
  boolean run(PubPdwhDetailDOM pdwhPub, String context) throws Exception;
}
