package com.smate.web.management.service.journal;

import com.smate.core.base.exception.ServiceException;
import com.smate.web.management.model.journal.JournalForm;

/**
 * 期刊
 * 
 * @author hht
 * @Time 2019年4月19日 下午4:31:30
 */
public interface JournalManageService {

  public static final String BASE_JOURNAL_TYPE = "baseJournal";
  public static final String ISIIF_TYPE = "isiIf";

  /**
   * excel文件导入service
   * 
   * @param journalForm
   */
  void importExcel(JournalForm journalForm) throws ServiceException;

  /**
   * isi基础期刊影响因子导入
   * 
   * @param journalForm
   */
  void importIsIExcel(JournalForm journalForm);

  /**
   * 获取批量导入审核数据
   * 
   * @param journalForm
   */
  void getAuditData(JournalForm journalForm);

  void getBaseJournal(JournalForm journalForm);

  /**
   * 期刊审核通过
   * 
   * @param journalForm
   * @return
   */
  String auditPassed(JournalForm journalForm);

  /**
   * 
   * 期刊审核拒绝
   * 
   * @param journalForm
   * @return
   */
  String auditRefuse(JournalForm journalForm);

  /**
   * 获取批量导入转手工处理数据
   * 
   * @param journalForm
   */
  void getBatchImportCheckData(JournalForm journalForm);

  /**
   * 跳转到处理页面数据来源
   * 
   * @param journalForm
   */
  void toProccess(JournalForm journalForm);

  /**
   * 审核-保留原样
   * 
   * @param journalForm
   * @return
   */
  String check(JournalForm journalForm);

  /**
   * 期刊审核-- 新增期刊
   * 
   * @param journalForm
   * @return
   */
  String checkAddJournal(JournalForm journalForm);

  /**
   * 期刊审核--更新至选中期刊
   * 
   * @param journalForm
   * @return
   */
  String toCheckUpdatejournal(JournalForm journalForm);

  /**
   * 获取批量导入isi之后 需要手工处理数据
   * 
   * @param journalForm
   */
  void getIsIBatchImportCheckData(JournalForm journalForm);

  /**
   * 合并期刊
   * 
   * @param journalForm
   * @return
   */
  int mergeJournal(JournalForm journalForm);

  void getBaseJournalByTitle(JournalForm journalForm, Long jouId) throws ServiceException;

  /**
   * 修改合并-更新
   * 
   * @param journalForm
   */
  void mergeUpdate(JournalForm journalForm);

  /**
   * 修改合并-删除
   * 
   * @param journalForm
   */
  void delJournalTitle(JournalForm journalForm);

  /**
   * 根据基础期刊title表Id删除相应数据
   * 
   * @param journalForm
   */
  void confirmDelJournalTitle(JournalForm journalForm);

  /**
   * 删除期刊，同时也会删除一些相关表的数据
   * 
   * @param journalForm
   */
  void deletBaseJournal(JournalForm journalForm);
}
