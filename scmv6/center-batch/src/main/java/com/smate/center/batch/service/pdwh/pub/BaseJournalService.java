package com.smate.center.batch.service.pdwh.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.BaseJournal;


/**
 * 期刊查找.
 * 
 * @author cwli
 */
public interface BaseJournalService extends Serializable {

  /**
   * SIE 成果发布，根据jid获取OA Type(Open Access Type:开放存储类型).
   * 
   * @param jid
   * @return
   * @throws ServiceException
   */
  String getRomeoColourByJid(Long jid) throws ServiceException;

  List<BaseJournal> findBaseJournalsByIssns(List<String> issns) throws ServiceException;

}
