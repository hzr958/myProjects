package com.smate.center.batch.service.pdwh.pub;

import java.util.List;

import com.smate.center.batch.model.pdwh.pub.Journal;
import com.smate.center.batch.model.pdwh.pub.JournalToSnsRefresh;
import com.smate.core.base.utils.exception.BatchTaskException;


/**
 * 同步期刊到sns 服务类
 * 
 * @author tsz
 * 
 */
public interface JournalToSnsRefreshService {

  /**
   * 根据id得到期刊
   * 
   * @param jid
   * @return
   */
  Journal findJournalById(Long jid) throws BatchTaskException;

  /**
   * 得到需要同步的数据
   * 
   * @param max
   * @return
   */
  List<Journal> getNeedRefresh(Integer maxsize) throws BatchTaskException;

  /**
   * 得到需要同步的数据
   * 
   * @param max
   * @return
   */
  List<JournalToSnsRefresh> findNeedRefresh(Integer maxsize) throws BatchTaskException;

  /**
   * 更新更新表记录
   * 
   * @param jouId
   */
  void updateRefresh(Long jId) throws BatchTaskException;
}
