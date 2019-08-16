package com.smate.center.batch.service.pub;

import com.smate.center.batch.model.pdwh.pub.BaseJournal;
import com.smate.center.batch.model.sns.pub.BaseJournalSns;
import com.smate.center.batch.service.pdwh.pub.BaseJournalToSnsRefreshService;
import com.smate.center.batch.service.pdwh.pub.JournalService;
import com.smate.core.base.utils.exception.BatchTaskException;


/**
 * sns 冗余基础期刊Service
 * 
 * @author tsz
 * 
 */
public interface BaseJournalSnsService {

  /**
   * 根据id获取sns基础期刊
   * 
   * @param jouId
   * @return
   * @throws BatchTaskException
   */
  BaseJournalSns getById(Long jouId) throws BatchTaskException;

  /**
   * 新加冗余基础期刊 主要是供pdwh哪边用
   */
  void addBaseJournalSns(BaseJournal bj, JournalService journalService,
      BaseJournalToSnsRefreshService baseJournalToSnsRefreshService) throws Exception;
}
