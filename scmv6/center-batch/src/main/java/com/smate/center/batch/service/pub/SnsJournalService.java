package com.smate.center.batch.service.pub;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 期刊查找.
 * 
 * @author cwli
 */
public interface SnsJournalService {

  // ===============期刊推荐算法改造2014/03/05==================
  /**
   * 获取人员关键词
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<Long> getPnsIdKeywordHashList(Long psnId) throws ServiceException;

}
