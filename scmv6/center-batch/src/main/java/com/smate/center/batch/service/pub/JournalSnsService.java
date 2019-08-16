package com.smate.center.batch.service.pub;

import java.util.List;

import com.smate.center.batch.model.pdwh.pub.AcJournal;
import com.smate.center.batch.model.pdwh.pub.Journal;
import com.smate.center.batch.model.sns.pub.JournalSns;
import com.smate.center.batch.service.pdwh.pub.JournalToSnsRefreshService;
import com.smate.core.base.utils.exception.BatchTaskException;


/**
 * sns 冗余期刊Service
 * 
 * @author tsz
 * 
 */
public interface JournalSnsService {

  /**
   * 根据id得到期刊
   * 
   * @param jid
   * @return
   * @throws BatchTaskException
   */
  JournalSns getById(long jid) throws BatchTaskException;

  /**
   * 根据名字 查找单个期刊
   * 
   * @param jname
   * @param issn
   * @param psnId
   * @return
   * @throws BatchTaskException
   */
  JournalSns findJournalByNameIssn(String jname, String issn, Long psnId) throws BatchTaskException;

  /**
   * 新增期刊 需要发送消息 去pdwh同步期刊数据
   * 
   * @param jname
   * @param jissn
   * @param currentUserId
   * @param from
   * @return
   * @throws BatchTaskException
   */
  JournalSns addJournal(String jname, String jissn, long currentUserId, String from) throws BatchTaskException;

  /**
   * 新加sns冗余期刊数据 主要是供 pdwh 推送匹配上期刊数据用
   * 
   * @param journal
   * @throws ServiceException
   */
  void addJournalSnsFromPdwh(Journal j, JournalToSnsRefreshService journalToSnsRefreshService) throws Exception;

  /**
   * 期刊自动提示 数据获取
   * 
   * @param startWith
   * @param size
   * @param uid
   * @return
   * @throws ServiceException
   */
  List<AcJournal> getAcJournalList(String startWith, int size, Long uid) throws BatchTaskException;

  /**
   * 在成果录入过程中通过ajax添加期刊.
   * 
   * @param name
   * @param issn
   * @return
   * @throws ServiceException
   */
  String ajaxAddJournalByPubEnter(String name, String issn) throws BatchTaskException;

  /**
   * 继续添加
   * 
   * @param name
   * @return
   * @throws ServiceException
   */
  String ajaxAddJnlContinue(String name) throws BatchTaskException;

}
