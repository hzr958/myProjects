package com.smate.center.task.single.service.pub;

import java.util.List;

/**
 * 成果XML解析的业务逻辑接口.
 * 
 * @author mjg
 * 
 */
public interface LibraryPubService {

  // 设定基准库名.
  final static String PUB_LIBRARY_ISI = "1";
  final static String PUB_LIBRARY_CNKI = "2";

  /**
   * 拆分基准库中的成果XML.
   */
  List<Long> getPubTaskData(Long pubId, Integer maxSize);
}
