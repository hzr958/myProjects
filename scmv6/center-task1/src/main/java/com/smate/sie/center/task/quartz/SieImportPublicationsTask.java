package com.smate.sie.center.task.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.model.ImportPdwhPub;
import com.smate.sie.center.task.model.ImportPdwhPubError;
import com.smate.sie.center.task.pdwh.service.ImportPdwhPubService;
import com.smate.sie.center.task.service.ImportPdwhPubErrorService;
import com.smate.sie.center.task.service.ImportPublicationsService;

/**
 * 将基准库成果导入至SIE新库业务表
 * 
 * @author ztt,zsj,yxy
 */
public class SieImportPublicationsTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 300; // 每次处理的个数

  @Autowired
  private ImportPublicationsService importPublicationsService;
  @Autowired
  private ImportPdwhPubErrorService importPdwhPubErrorService;
  @Autowired
  private ImportPdwhPubService importPdwhPubService;

  public SieImportPublicationsTask() {
    super();
  }

  public SieImportPublicationsTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException, SysServiceException {
    if (!super.isAllowExecution()) {
      return;
    }
    // 判断是否还存在带指派成果的单位，否则状态全更新为1。
    Long count = importPdwhPubService.getInsCountByStatus();
    if (count.intValue() == 0) {
      importPdwhPubService.updateAllImportPdwhPub();
    }
    while (true) {
      List<ImportPdwhPub> insIdList = importPdwhPubService.getImportPdwhPubList(SIZE);
      if (CollectionUtils.isEmpty(insIdList)) {
        break;
      }
      for (ImportPdwhPub importPdwhPub : insIdList) {
        Long insId = importPdwhPub.getInsId();
        List<Map<String, Object>> resultList = new ArrayList<>();
        while (true) {
          // 从基准库获取该单位中的记录
          List<PubPdwhPO> pdwhPubList = importPublicationsService.getPwdhPubList(SIZE, insId);
          // 如果记录为空，则结束处理该单位信息，并记录更新时间是最后一条的记录
          if (CollectionUtils.isEmpty(pdwhPubList)) {
            importPublicationsService.saveImportPdwhPub(pdwhPubList, insId, 0);
            break;
          }
          // 处理业务
          for (PubPdwhPO pdwhPublication : pdwhPubList) {
            try {
              Map<String, Object> resultMap = importPublicationsService.importSiePublication(pdwhPublication, insId);
              resultList.add(resultMap);
            } catch (Exception e) {
              ImportPdwhPubError error = new ImportPdwhPubError();
              error.setPdwhPubId(pdwhPublication.getPubId());
              error.setErrDate(new Date());
              error.setErrMsg("基准库成果ID：" + pdwhPublication.getPubId() + "   错误日志： " + e.toString()); // +e.getMessage()+"---"
              importPdwhPubErrorService.saveObject(error);
              logger.error("将基准库成果导入至SIE新库业务表线程出错", e);
            }
          }
          if (pdwhPubList.size() < SIZE) {
            importPublicationsService.saveImportPdwhPub(pdwhPubList, insId, 0);
            break;
          }
          // 保存最新更新时间。
          importPublicationsService.saveImportPdwhPub(pdwhPubList, insId, null);
        }
        // 统计单位数据
        importPublicationsService.stPdwhData(resultList, insId);
      }
    }
  }

}
