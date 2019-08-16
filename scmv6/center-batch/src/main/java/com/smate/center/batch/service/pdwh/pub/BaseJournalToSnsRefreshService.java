package com.smate.center.batch.service.pdwh.pub;

import java.util.List;

import com.smate.center.batch.model.pdwh.pub.BaseJournal;
import com.smate.center.batch.model.pdwh.pub.BaseJournalToSnsRefresh;
import com.smate.core.base.utils.exception.BatchTaskException;


/**
 * 同步基准期刊到sns 服务类
 * 
 * @author tsz
 * 
 */
public interface BaseJournalToSnsRefreshService {

  /**
   * 根据id得到基础期刊
   * 
   * @param jouId
   * @return
   */
  BaseJournal findBaseJournalById(Long jouId) throws BatchTaskException;

  /**
   * 得到需要同步的数据
   * 
   * @param max
   * @return
   */
  List<BaseJournal> getNeedRefresh(Integer maxsize) throws BatchTaskException;

  /**
   * 得到需要同步的数据
   * 
   * @param max
   * @return
   */
  List<BaseJournalToSnsRefresh> findNeedRefresh(Integer maxsize) throws BatchTaskException;

  /**
   * 更新更新表记录
   * 
   * @param jouId
   */
  void updateRefresh(Long jouId) throws BatchTaskException;

}
