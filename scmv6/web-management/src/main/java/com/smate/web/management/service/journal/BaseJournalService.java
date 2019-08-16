package com.smate.web.management.service.journal;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.journal.BaseJournal;
import com.smate.web.management.model.journal.BaseJournalLog;
import com.smate.web.management.model.journal.BaseJournalTempBatch;
import com.smate.web.management.model.journal.BaseJournalTempIsiIf;
import com.smate.web.management.model.journal.BaseJournalTitle;
import com.smate.web.management.model.journal.Journal;
import com.smate.web.management.model.journal.JournalForm;

/**
 * 期刊查找.
 * 
 * @author cwli
 */
public interface BaseJournalService extends Serializable {

  /**
   * 导入期刊
   * 
   * @param baseJournalTempBatchList
   * @return
   */
  JournalImportStatusEnum importJournal(BaseJournalTempBatch baseJournalTempBatch) throws ServiceException;

  /**
   * 导入isiif
   * 
   * @param isiIfList
   * @return
   */
  BaseJournalLog impoutJournalIf(List<BaseJournalTempIsiIf> isiIfList);

  /**
   * 查重。是否存在该期刊 .
   * 
   * @param batch
   * @return
   */
  int isExistJournal(String pissn, String eissn, String title_en, String title_xx, Long db_id, String cn);

  /**
   * 使用当前导入的临时表对象进行查重
   * 
   * @param baseJournalTempBatch
   * @return
   */
  int isExistJournal(BaseJournalTempBatch baseJournalTempBatch);

  /**
   * 保存期刊title.
   * 
   * @param batch
   * @throws ServiceException
   */
  void saveJournalTitle(BaseJournalTempBatch batch) throws ServiceException;

  /**
   * 保存导入的日志
   * 
   * @param baseJournalLog
   */
  void saveJournalImportLog(BaseJournalLog baseJournalLog);

  /**
   * 获取审核表所有数据
   * 
   * @param journalForm
   * @param string
   */
  void getTempCheck(JournalForm journalForm, String string);

  /**
   * 批量查询title表实体.
   * 
   * @param titleIdList
   * @return
   * @throws ServiceException
   */
  List<BaseJournalTitle> getTempTitleByTempCheck(List<Long> titleIdList) throws ServiceException;

  /**
   * 批量查询临时导入表实体
   * 
   * @param batchIdList
   * @return
   * @throws ServiceException
   */
  List<BaseJournalTempBatch> getBatchTitleByTempCheck(List<Long> batchIdList) throws ServiceException;


  /**
   * 期刊审核通过
   * 
   * @param tempId
   * @param temps
   * @return
   */
  int passTempCheck(Long tempId, String temps);

  /**
   * 
   * @param baseJournalTempBatch
   * @return
   */
  BaseJournalLog importJournalByPass(BaseJournalTempBatch baseJournalTempBatch);

  /**
   * 期刊审核拒绝
   * 
   * @param tempId
   * @param temps
   * @return
   */
  int refuseTempCheck(Long tempId, String temps);

  /**
   * 获取所有期刊导入之后转手工处理数据
   * 
   * @param page
   * 
   * @param baseJournalTempBatch
   * @return
   */
  Page<BaseJournalTempBatch> getAllBatchData(Page<BaseJournalTempBatch> page,
      BaseJournalTempBatch baseJournalTempBatch);

  /**
   * 根据主键id
   * 
   * @param jnlId
   * @return
   */
  BaseJournalTempBatch getTempBatchById(Long jnlId);


  /**
   * 使用格式化好的数据去title中进行查重
   * 
   * @param pissn
   * @param titleXx
   * @param titleEn
   * @return
   */
  List<BaseJournalTitle> findBaseJournalTitle(String pissn, String titleXx, String titleEn);


  /**
   * 获取主键获取期刊实体
   * 
   * @param jnlId
   * @return
   */
  Journal findJournal(Long jnlId);

  /**
   * 通过jnlId查找baseJournal记录
   */
  BaseJournal findBaseJournal(Long jnlId);

  /**
   * 根据dbcode模糊查询dbId
   * 
   * @param dbCode
   * @return
   */
  Long findDBIdLikeDbCode(String dbCode);


  /**
   * 更新
   * 
   * @param batch
   * @param jnlIds
   * @param choice
   * @return
   */
  Map<String, Object> updateTempBatch(BaseJournalTempBatch batch, Long jnlIds, int choice);


  /**
   * 获取批量导入isi所有待审核数据
   * 
   * @param page
   * @param isiIf
   * @return
   */
  Page<BaseJournalTempIsiIf> getAllIsIData(Page<BaseJournalTempIsiIf> page, BaseJournalTempIsiIf isiIf);

  /**
   * 跟新isi
   * 
   * @param isiIf
   * @param jnlIds
   * @param handleMethod
   * @return
   */
  String updateTempIsIif(BaseJournalTempIsiIf isiIf, Long jnlIds, Long handleMethod);

  /**
   * 根据Id获取isiif实体
   * 
   * @param valueOf
   * @return
   */
  BaseJournalTempIsiIf getTempIsiIfById(Long valueOf);

  /**
   * 获取基础期刊主表数据
   * 
   * @param page
   * @param journal
   * @return
   */
  Page<BaseJournal> getBaseJournal(Page<BaseJournal> page, BaseJournal journal);

  List<BaseJournal> findNewBaseJournalList(BaseJournal journal);

  /**
   * 更新基础期刊列表
   * 
   * @param jnlList
   */
  void updateBaseJournal(List<BaseJournal> jnlList);

  /**
   * 期刊合并
   * 
   * @param jouId 源期刊id
   * @param toJouId 合并到的期刊id
   * @return
   */
  int mergeJournal(Long jouId, Long toJouId);

  /**
   * 更新指定jnlID对应的journal表和jnlTiltle表中的数据
   * 
   * @param journal
   * @return
   */
  int updateJnlAndJnlTitle(BaseJournal journal);

  /**
   * 删除基础期刊数据，同时更新一些表，也会删除一些关联的字表
   * 
   * @param jouId
   * @throws ServiceException
   */
  void deletBaseJournal(Long jnlId) throws ServiceException;

  /**
   * 通过id删除basejournal
   * 
   * @param bjtId
   */
  void deleteBaseJournalTitleById(Long bjtId);


}
