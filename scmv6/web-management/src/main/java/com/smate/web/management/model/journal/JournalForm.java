package com.smate.web.management.model.journal;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.smate.core.base.utils.model.Page;

/**
 * 导入期刊接受表单参数类
 * 
 * @author hht
 * @Time 2019年4月19日 下午4:35:47
 */
public class JournalForm {


  /**
   * excel文件
   */
  private File xlsFile;
  // isi影响因子文件
  private File xlsFileIf;
  /**
   * 文件名
   */
  private String fileName;
  private String temp = "";
  private Date startTime;
  private Date endTime;
  private String resultMsg;
  private Boolean haveDB;
  private String jnlId;
  private int isBatchOrManual;
  private List<BaseJournalTitle> baseJournalTitleList;
  private List<BaseJournalTempBatch> baseJournalTempBatchList;
  @SuppressWarnings("rawtypes")
  private Page page = new Page(10);
  private BaseJournalTempCheck check;
  private BaseJournal journal;
  private String jnlIds;
  private BaseJournalTempBatch batch;
  private Journal handJournal;
  private BaseJournalTempIsiIf isiIf;
  private Journal jnl;
  private String tuId;
  private BaseJournalByTitle jnlByTitle;
  private String[] titleId;
  private String[] titlePssn;

  private String[] titleEn;

  private String[] titleXx;
  private String[] titleStatus;
  private int updateResult;
  private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getUpdateResult() {
    return updateResult;
  }

  public void setUpdateResult(int updateResult) {
    this.updateResult = updateResult;
  }

  public String[] getTitleId() {
    return titleId;
  }

  public void setTitleId(String[] titleId) {
    this.titleId = titleId;
  }

  public String[] getTitlePssn() {
    return titlePssn;
  }

  public void setTitlePssn(String[] titlePssn) {
    this.titlePssn = titlePssn;
  }

  public String[] getTitleEn() {
    return titleEn;
  }

  public void setTitleEn(String[] titleEn) {
    this.titleEn = titleEn;
  }

  public String[] getTitleXx() {
    return titleXx;
  }

  public void setTitleXx(String[] titleXx) {
    this.titleXx = titleXx;
  }

  public String[] getTitleStatus() {
    return titleStatus;
  }

  public void setTitleStatus(String[] titleStatus) {
    this.titleStatus = titleStatus;
  }

  public BaseJournalByTitle getJnlByTitle() {
    return jnlByTitle;
  }

  public void setJnlByTitle(BaseJournalByTitle jnlByTitle) {
    this.jnlByTitle = jnlByTitle;
  }

  public String getTuId() {
    return tuId;
  }

  public void setTuId(String tuId) {
    this.tuId = tuId;
  }

  public File getXlsFileIf() {
    return xlsFileIf;
  }

  public void setXlsFileIf(File xlsFileIf) {
    this.xlsFileIf = xlsFileIf;
  }

  public Journal getJnl() {
    return jnl;
  }

  public void setJnl(Journal jnl) {
    this.jnl = jnl;
  }

  public BaseJournalTempIsiIf getIsiIf() {
    return isiIf;
  }

  public void setIsiIf(BaseJournalTempIsiIf isiIf) {
    this.isiIf = isiIf;
  }

  public Journal getHandJournal() {
    return handJournal;
  }

  public void setHandJournal(Journal handJournal) {
    this.handJournal = handJournal;
  }

  public BaseJournalTempBatch getBatch() {
    return batch;
  }

  public void setBatch(BaseJournalTempBatch batch) {
    this.batch = batch;
  }

  public String getJnlIds() {
    return jnlIds;
  }

  public void setJnlIds(String jnlIds) {
    this.jnlIds = jnlIds;
  }

  public BaseJournal getJournal() {
    return journal;
  }

  public void setJournal(BaseJournal journal) {
    this.journal = journal;
  }

  public BaseJournalTempCheck getCheck() {
    return check;
  }

  public void setCheck(BaseJournalTempCheck check) {
    this.check = check;
  }

  public int getIsBatchOrManual() {
    return isBatchOrManual;
  }

  public List<BaseJournalTitle> getBaseJournalTitleList() {
    return baseJournalTitleList;
  }

  public void setBaseJournalTitleList(List<BaseJournalTitle> baseJournalTitleList) {
    this.baseJournalTitleList = baseJournalTitleList;
  }

  public List<BaseJournalTempBatch> getBaseJournalTempBatchList() {
    return baseJournalTempBatchList;
  }

  public void setBaseJournalTempBatchList(List<BaseJournalTempBatch> baseJournalTempBatchList) {
    this.baseJournalTempBatchList = baseJournalTempBatchList;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public void setIsBatchOrManual(int isBatchOrManual) {
    this.isBatchOrManual = isBatchOrManual;
  }

  public String getTemp() {
    return temp;
  }

  public void setTemp(String temp) {
    this.temp = temp;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public String getResultMsg() {
    return resultMsg;
  }

  public void setResultMsg(String resultMsg) {
    this.resultMsg = resultMsg;
  }

  public Boolean getHaveDB() {
    return haveDB;
  }

  public void setHaveDB(Boolean haveDB) {
    this.haveDB = haveDB;
  }

  public String getJnlId() {
    return jnlId;
  }

  public void setJnlId(String jnlId) {
    this.jnlId = jnlId;
  }

  public File getXlsFile() {
    return xlsFile;
  }

  public void setXlsFile(File xlsFile) {
    this.xlsFile = xlsFile;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
}
