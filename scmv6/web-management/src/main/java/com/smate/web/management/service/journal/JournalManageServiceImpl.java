package com.smate.web.management.service.journal;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.management.dao.journal.ConstRefDbDao;
import com.smate.web.management.model.journal.BaseJournal;
import com.smate.web.management.model.journal.BaseJournalLog;
import com.smate.web.management.model.journal.BaseJournalTempBatch;
import com.smate.web.management.model.journal.BaseJournalTempCheck;
import com.smate.web.management.model.journal.BaseJournalTempIsiIf;
import com.smate.web.management.model.journal.BaseJournalTitle;
import com.smate.web.management.model.journal.JournalForm;

/**
 * bpo期刊管理service
 * 
 * @author hht
 * @Time 2019年4月19日 下午4:32:49
 */
@Service
public class JournalManageServiceImpl implements JournalManageService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UploadExcelFileService uploadExcelFileService;
  @Autowired
  private BaseJournalService baseJournalService;
  @Autowired
  private ConstRefDbDao constRefDbDao;
  @Autowired
  private BaseJournalTitleService baseJournalTitleService;


  /**
   * excel文件导入
   */
  @SuppressWarnings("unchecked")
  @Override
  public void importExcel(JournalForm journalForm) {
    String temp = "";
    try {
      // 1:读取Excel并转换成list对象
      List<BaseJournalTempBatch> importBatchList = new ArrayList<BaseJournalTempBatch>();
      importBatchList = BaseJournalUtils.excelToEntity(JournalManageService.BASE_JOURNAL_TYPE, journalForm.getXlsFile(),
          importBatchList, logger);
      // 2 将excel文件上传
      uploadExcelFileService.saveExcelFile(journalForm.getXlsFile(), journalForm.getFileName());
      logger.error("读取Excel完成，转换entityList的大小为：{}", importBatchList.size());
      // 3 开始导入
      temp = importJournal(importBatchList);
    } catch (Exception e) {
      temp = "期刊导入时系统出现错误";
      logger.error("导入基础期刊Excel数据出错", e);
    }
    journalForm.setTemp(temp);
  }

  /**
   * 导入期刊逻辑
   * 
   * @param importBatchList
   */
  private String importJournal(List<BaseJournalTempBatch> importBatchList) {
    String msg = "";
    if (CollectionUtils.isNotEmpty(importBatchList)) {
      BaseJournalLog baseJournalLog = new BaseJournalLog();
      int totalCount = 0;
      // 转手工处理的条数！
      int manualCount = 0;
      // 成功导入的条数！
      int batchCount = 0;
      // 导入更新的条数！
      int updateCount = 0;
      // 没有dbId的条数
      int noDbIdCount = 0;
      // 异常条数
      int errorCount = 0;
      for (BaseJournalTempBatch baseJournalTempBatch : importBatchList) {
        if (baseJournalTempBatch != null) {
          JournalImportStatusEnum statusEnum = JournalImportStatusEnum.OTHER;
          try {
            statusEnum = baseJournalService.importJournal(baseJournalTempBatch);
          } catch (Exception e) {
            errorCount++;
            logger.error("导入期刊出现异常", e);
          }
          switch (statusEnum) {
            case ADD:
              batchCount++;
              break;
            case MANUAL:
              manualCount++;
              break;
            case UPDATE:
              updateCount++;
              break;
            case ERROR:
              errorCount++;
              break;
            case NODBID:
              noDbIdCount++;
              break;
            default:
              break;
          }
          if (JournalImportStatusEnum.NOTIMPORT != statusEnum)// 排除那些不进行导入操作的数据
            totalCount++;
        }
      }
      baseJournalLog.setImpCount(totalCount);
      baseJournalLog.setBatchCount(batchCount);
      baseJournalLog.setManualCount(manualCount);
      baseJournalLog.setUpdateCount(updateCount);
      baseJournalLog.setNoDbIdCount(noDbIdCount);
      baseJournalLog.setErrorCount(errorCount);
      msg = "基础期刊此次导入共" + totalCount + "条: 无dbId" + noDbIdCount + "条，导入异常" + errorCount + "条，新增" + batchCount + "条，更新"
          + updateCount + "条" + "，转手工处理" + manualCount + "条";
      baseJournalService.saveJournalImportLog(baseJournalLog);
    } else {
      msg = "导入使用的excel文件中不存在期刊记录";
    }
    return msg;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void importIsIExcel(JournalForm journalForm) {
    BaseJournalLog baseJournalLog = new BaseJournalLog();
    BaseJournalLog baseJournalLogTem;
    String msg = "";
    String temp = "";
    try {
      // 1:读取Excel并转换成list
      List<BaseJournalTempIsiIf> isiIfList = new ArrayList<BaseJournalTempIsiIf>();
      File xlsFileIf = journalForm.getXlsFileIf();
      String fileName = journalForm.getFileName();
      isiIfList = BaseJournalUtils.excelToEntity(JournalManageService.ISIIF_TYPE, xlsFileIf, isiIfList, logger);
      // 上传文件
      uploadExcelFileService.saveExcelFile(xlsFileIf, fileName);
      // 1.1 过滤TitleXx为空、titleEn的数据
      for (int i = isiIfList.size() - 1; i >= 0; i--) {
        BaseJournalTempIsiIf ifTemp = isiIfList.get(i);
        if (StringUtils.isBlank(ifTemp.getTitleXx()) && StringUtils.isBlank(ifTemp.getTitleEn()))
          isiIfList.remove(i);
      }
      logger.error("读取Excel完成，转换entityList的大小为：{}", isiIfList.size());
      // 2:开始导入
      baseJournalLogTem = baseJournalService.impoutJournalIf(isiIfList);
      if (null != baseJournalLogTem) {
        baseJournalLog
            .setImpCount(BaseJournalUtils.addInteger(baseJournalLog.getImpCount(), baseJournalLogTem.getImpCount()));
        baseJournalLog.setBatchCount(
            BaseJournalUtils.addInteger(baseJournalLog.getBatchCount(), baseJournalLogTem.getBatchCount()));
        baseJournalLog.setManualCount(
            BaseJournalUtils.addInteger(baseJournalLog.getManualCount(), baseJournalLogTem.getManualCount()));
        baseJournalLog.setUpdateCount(
            BaseJournalUtils.addInteger(baseJournalLog.getUpdateCount(), baseJournalLogTem.getUpdateCount()));
        msg = "影响因子此次导入共" + baseJournalLog.getImpCount() + "条，新增" + baseJournalLog.getBatchCount() + "条，更新"
            + baseJournalLog.getUpdateCount() + "条" + "，转手工处理" + baseJournalLogTem.getManualCount() + "条";
        baseJournalLog.setMessage(msg);
      }
    } catch (Exception e) {
      temp = "导入过程发生异常，部分导入信息：/r/n";
      logger.error("导入期刊影响因子Excel数据出错", e);
    } finally {
      // 插入日志信息！
      if ("".equals(msg)) {
        msg = "此次导入共0条，新增0条，更新0条!";
      }
      temp = "isi" + temp + msg;
      journalForm.setTemp(temp);
      baseJournalLog.setMessage(temp);
      baseJournalService.saveJournalImportLog(baseJournalLog);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void getAuditData(JournalForm journalForm) {
    journalForm.setIsBatchOrManual(0);
    try {
      // 移除session
      Struts2Utils.getSession().removeAttribute("page");
      Struts2Utils.getSession().removeAttribute("check");
      // 期刊审核查询
      baseJournalService.getTempCheck(journalForm, "batch");
      List<BaseJournalTempCheck> checkList = journalForm.getPage().getResult();
      // 根据id批量获取baseJournalTitle基础期刊title表. baseJournalTempBatch期刊批量导入临时表.
      List<Long> titleIdList = new ArrayList<Long>();
      List<Long> batchIdList = new ArrayList<Long>();
      checkList.forEach(TempCheck -> {
        Long tuttiJouId = TempCheck.getTuttiJouId();
        Long tempBatchId = TempCheck.getTempBatchId();
        if (tuttiJouId != null)
          titleIdList.add(tuttiJouId);
        if (tempBatchId != null)
          batchIdList.add(tempBatchId);
      });
      if (titleIdList.size() > 0)
        journalForm.setBaseJournalTitleList(baseJournalService.getTempTitleByTempCheck(titleIdList));
      if (batchIdList.size() > 0)
        journalForm.setBaseJournalTempBatchList(baseJournalService.getBatchTitleByTempCheck(batchIdList));
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("获取批量导入审核数据出错", e);
    }

  }

  @SuppressWarnings("unchecked")
  @Override
  public void getBaseJournal(JournalForm journalForm) {
    BaseJournal journal = journalForm.getJournal();
    if (journal == null)
      journal = new BaseJournal();
    Page<BaseJournal> page = journalForm.getPage();
    try {
      page = baseJournalService.getBaseJournal(page, journal);
      List<BaseJournal> jnlList = page.getResult();
      if (CollectionUtils.isEmpty(jnlList) && journal != null) {
        List<BaseJournal> newbjList = baseJournalService.findNewBaseJournalList(journal);
        page.setResult(newbjList);
      }
      baseJournalService.updateBaseJournal(jnlList);
      journalForm.setPage(page);
    } catch (Exception e) {
      logger.error("查询基础期刊所有数据出错", e);
    }
  }


  /**
   * 期刊审核通过
   */
  @Override
  public String auditPassed(JournalForm journalForm) {
    // 待通过的期刊ids
    String preparePassIds = journalForm.getJnlIds();
    // 区别是isi还是批量导入
    String temps = journalForm.getTemp();
    int result = 0;
    if (StringUtils.isNotBlank(preparePassIds)) {
      String[] jnlIds = preparePassIds.split(",");
      for (String jnlId : jnlIds) {
        if (!StringUtils.isBlank(temps) && !StringUtils.isBlank(jnlId)) {
          result = baseJournalService.passTempCheck(Long.valueOf(jnlId), temps);
        }
      }
    }
    if (result == 1)
      return "true";
    return "false";
  }


  /**
   * 期刊审核拒绝
   */
  @Override
  public String auditRefuse(JournalForm journalForm) {
    // 待拒绝的期刊ids
    String tempIds = journalForm.getJnlIds();
    // 区别是isi还是批量导入
    String temps = journalForm.getTemp();
    int result = 0;
    if (StringUtils.isNotBlank(tempIds)) {
      String[] jinIds = tempIds.split(",");
      for (String tempId : jinIds) {
        if (!StringUtils.isBlank(temps) && !StringUtils.isBlank(tempId)) {
          result = baseJournalService.refuseTempCheck(Long.valueOf(tempId), temps);
        }
      }
    }
    if (result == 1)
      return "true";
    return "false";
  }


  /**
   * 获取批量导入之后等待手工处理数据
   */
  @SuppressWarnings("unchecked")
  @Override
  public void getBatchImportCheckData(JournalForm journalForm) {
    try {
      BaseJournalTempBatch baseJournalTempBatch = journalForm.getBatch();
      Page<BaseJournalTempBatch> page = journalForm.getPage();
      if (baseJournalTempBatch == null) {
        baseJournalTempBatch = new BaseJournalTempBatch();
      }
      Page<BaseJournalTempBatch> pageBatch = baseJournalService.getAllBatchData(page, baseJournalTempBatch);
      journalForm.setPage(pageBatch);
      journalForm.setIsBatchOrManual(0);
    } catch (Exception e) {
      logger.error("获取批量导入之后等待手工处理数据出错", e);
    }
  }


  @Override
  public void toProccess(JournalForm journalForm) {
    String temp = journalForm.getTemp();
    String jnlId = journalForm.getJnlId();
    List<BaseJournalTitle> baseJournalTitleList = null;
    BaseJournalTempIsiIf isiIf = null;
    if ("batch".equals(temp)) {
      journalForm.setIsBatchOrManual(0);
      BaseJournalTempBatch batch = baseJournalService.getTempBatchById(Long.valueOf(jnlId));
      if (batch != null) {
        baseJournalTitleList =
            baseJournalService.findBaseJournalTitle(batch.getPissn(), batch.getTitleXx(), batch.getTitleEn());
        // check = this.getBaseJournalService().findCheck(batch.getTempBatchId(), temp);
      }
      journalForm.setBatch(batch);
    }
    if ("isiIf".equals(temp)) {
      isiIf = baseJournalService.getTempIsiIfById(Long.valueOf(jnlId));
      if (isiIf != null) {
        baseJournalTitleList =
            baseJournalService.findBaseJournalTitle(isiIf.getPissn(), isiIf.getTitleXx(), isiIf.getTitleEn());
        // check = this.getBaseJournalService().findCheck(isiIf.getTempIsiIfId(), "isiIf");
      }
      journalForm.setIsiIf(isiIf);
    }
    journalForm.setBaseJournalTitleList(baseJournalTitleList);
  }

  @Override
  public String check(JournalForm journalForm) {
    String result = "false";
    String temp = journalForm.getTemp();
    if ("batch".equalsIgnoreCase(temp)) {
      baseJournalService.updateTempBatch(journalForm.getBatch(), null, 3);
      result = "true";
    }
    if ("isIif".equalsIgnoreCase(temp)) {
      result = baseJournalService.updateTempIsIif(journalForm.getIsiIf(), null, 3L);
    }
    return result;
  }


  @Override
  public String checkAddJournal(JournalForm journalForm) {
    String result = "false";
    String temp = journalForm.getTemp();
    BaseJournalTempBatch batch = journalForm.getBatch();
    BaseJournalTempIsiIf isiIf = journalForm.getIsiIf();
    try {
      if ("batch".equals(temp)) {
        baseJournalService.updateTempBatch(batch, null, 2);
        result = "true";
      }
      if ("isIif".equals(temp)) {
        result = baseJournalService.updateTempIsIif(isiIf, null, 2L);
      }
    } catch (Exception e) {
      logger.error("bpo处理临时数据，新增期刊出错", e);
    }
    return result;
  }


  @Override
  public String toCheckUpdatejournal(JournalForm journalForm) {
    Map<String, Object> resultMap = new HashMap<>();
    String temp = journalForm.getTemp();
    String jnlId = journalForm.getJnlId();
    BaseJournalTempBatch batch = journalForm.getBatch();
    BaseJournalTempIsiIf isiIf = journalForm.getIsiIf();
    try {
      if ("batch".equalsIgnoreCase(temp)) {
        Long jId = StringUtils.isBlank(jnlId) ? null : Long.valueOf(jnlId);
        resultMap = baseJournalService.updateTempBatch(batch, jId, 1);
      }
      if ("isIif".equalsIgnoreCase(temp)) {
        Long jId = StringUtils.isBlank(jnlId) ? null : Long.valueOf(jnlId);
        baseJournalService.updateTempIsIif(isiIf, jId, 1L);
      }
    } catch (Exception e) {
      logger.error("bpo处理临时数据，更新至选中期刊出错", e);
      resultMap.put("result", "error");
      resultMap.put("mesg", "执行操作时出现系统异常");
    }
    return JacksonUtils.jsonObjectSerializer(resultMap);
  }



  @SuppressWarnings("unchecked")
  @Override
  public void getIsIBatchImportCheckData(JournalForm journalForm) {
    try {
      BaseJournalTempIsiIf isiIf = journalForm.getIsiIf();
      Page<BaseJournalTempIsiIf> page = journalForm.getPage();
      if (isiIf == null) {
        isiIf = new BaseJournalTempIsiIf();
      }
      // jnlbpo账号只查询转手工处理的数据
      isiIf.setThrowsCause(1L);
      Page<BaseJournalTempIsiIf> pageIsIif = baseJournalService.getAllIsIData(page, isiIf);
      List<BaseJournalTempIsiIf> isiIfList = pageIsIif.getResult();
      for (BaseJournalTempIsiIf baseJournalTempIsiIf : isiIfList) {
        if (baseJournalTempIsiIf.getDbId() != null)
          baseJournalTempIsiIf.setDbCode(constRefDbDao.getCodeByDbId(baseJournalTempIsiIf.getDbId()));
      }
      journalForm.setPage(pageIsIif);
    } catch (ServiceException e) {
      logger.error("获取IsI期刊影响因子临时表数据出错", e);
    }
  }

  @Override
  public int mergeJournal(JournalForm journalForm) {
    // 合并的期刊id
    String tuJnlId = journalForm.getTuId();
    // 源期刊id
    String jnlId = journalForm.getJnlId();
    int result = 0;
    try {
      if (StringUtils.isNotBlank(jnlId) && StringUtils.isNotBlank(tuJnlId)) {
        result = baseJournalService.mergeJournal(Long.valueOf(jnlId), Long.valueOf(tuJnlId));
      }
    } catch (Exception e) {
      logger.error("期刊合并出错", e);
      return 0;
    }
    return result;
  }

  /**
   * 根据期刊ID查询基础期刊详细信息
   */
  @Override
  public void getBaseJournalByTitle(JournalForm journalForm, Long jouId) throws ServiceException {
    try {
      journalForm.setTemp("");
      Long jnlId = jouId != null ? jouId : Long.valueOf(journalForm.getJnlId());
      BaseJournal journal = baseJournalService.findBaseJournal(jnlId);
      journalForm.setJournal(journal);
      List<BaseJournalTitle> titleList = baseJournalTitleService.getBaseJournalTitleByJnlId(jnlId);
      journalForm.setBaseJournalTitleList(titleList);
    } catch (ServiceException e) {
      logger.error("根据期刊ID获取期刊数据及相关表数据出错", e);
    }
  }

  @Override
  public void mergeUpdate(JournalForm journalForm) {
    int result = 0;
    try {
      result = baseJournalService.updateJnlAndJnlTitle(journalForm.getJournal());
    } catch (Exception e) {
      logger.error("根据期刊ID查询基础期刊详细信息出错", e);
    }
    this.getBaseJournalByTitle(journalForm, journalForm.getJournal().getJnlId());
    journalForm.setUpdateResult(result);
  }

  @Override
  public void delJournalTitle(JournalForm journalForm) {
    String jnlId = journalForm.getJnlId();
    baseJournalService.deletBaseJournal(Long.valueOf(jnlId));
  }

  @Override
  public void confirmDelJournalTitle(JournalForm journalForm) {
    try {
      String idstr = journalForm.getId();
      if (StringUtils.isNotBlank(idstr)) {
        Long bjtId = NumberUtils.toLong(idstr);
        baseJournalService.deleteBaseJournalTitleById(bjtId);
      }
      /*
       * List<BaseJournalTitle> jnlTitleList = new ArrayList<BaseJournalTitle>(); String[] titleIds =
       * journalForm.getTitleId(); String[] pissns = journalForm.getTitlePssn(); String[] titleEns =
       * journalForm.getTitleEn(); String[] titleXxs = journalForm.getTitleXx(); String[] titleStatuss =
       * journalForm.getTitleStatus(); if (titleIds != null && titleXxs != null && titleEns != null &&
       * pissns != null) { for (int i = 0; i < titleIds.length; i++) { BaseJournalTitle jnlTitle = new
       * BaseJournalTitle(); jnlTitle.setJouTitleId(Long.valueOf(titleIds[i]));
       * jnlTitle.setPissn(pissns[i]); jnlTitle.setTitleXx(titleXxs[i]); jnlTitle.setTitleEn(titleEns[i]);
       * jnlTitle.setTitleStatus(Integer.valueOf(titleStatuss[i])); jnlTitleList.add(jnlTitle); } }
       */
      this.getBaseJournalByTitle(journalForm, journalForm.getJournal().getJnlId());
      journalForm.setUpdateResult(1);
    } catch (Exception e) {
      logger.error("根据基础期刊title表Id删除相应数据出错", e);
    }
  }

  @Override
  public void deletBaseJournal(JournalForm journalForm) {
    if (StringUtils.isNotBlank(journalForm.getJnlId())) {
      baseJournalService.deletBaseJournal(Long.valueOf(journalForm.getJnlId()));
    }
  }
}
