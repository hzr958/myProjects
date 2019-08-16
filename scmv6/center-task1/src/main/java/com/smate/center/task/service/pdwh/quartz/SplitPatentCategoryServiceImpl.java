package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.base.TaskJobTypeConstants;
import com.smate.center.task.dao.pdwh.quartz.PdwhPatentCategoryDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlDao;
import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.center.task.model.pdwh.pub.PdwhPatentCategory;
import com.smate.center.task.model.pdwh.quartz.PdwhPubXml;
import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;
import com.smate.center.task.single.oldXml.pub.ImportPubXmlDocument;

@Service("splitPatentCategoryService")
@Transactional(rollbackFor = Exception.class)
public class SplitPatentCategoryServiceImpl implements SplitPatentCategoryService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
  @Autowired
  private PdwhPatentCategoryDao pdwhPatentCategoryDao;
  private static Integer jobType = TaskJobTypeConstants.SplitPatentCategoryTask;

  @Override
  public List<TmpTaskInfoRecord> batchGetJobList(Integer size) throws Exception {
    return tmpTaskInfoRecordDao.getTaskInfoRecordList(size, jobType);
  }

  @Override
  public void startSplitInfo(TmpTaskInfoRecord job) throws Exception {
    Long pubId = job.getHandleId();
    Long jobId = job.getJobId();
    PdwhPubXml pdwhPubXml = (PdwhPubXml) this.pdwhPubXmlDao.get(pubId);
    if (pdwhPubXml == null) {
      tmpTaskInfoRecordDao.updateTaskStatusById(jobId, 2, "数据不存在！");
      return;
    }
    String XmlString = pdwhPubXml.getXml();

    if (StringUtils.isEmpty(XmlString)) {
      tmpTaskInfoRecordDao.updateTaskStatusById(jobId, 2, "获取到的xml为空！");
      return;
    }

    ImportPubXmlDocument document = new ImportPubXmlDocument(XmlString);

    Integer patentCategory = document.getPatentCategory();
    String patentCategoryNo = document.getPatentCategoryNo();
    String patentMainCategoryNo = document.getPatentMainCategoryNo();
    if (patentCategory == null && StringUtils.isBlank(patentCategoryNo) && StringUtils.isBlank(patentMainCategoryNo)) {
      tmpTaskInfoRecordDao.updateTaskStatusById(jobId, 2, "专利号都为空！");
      return;
    }

    if (pdwhPatentCategoryDao.getDupPubId(pubId) > 0) {
      tmpTaskInfoRecordDao.updateTaskStatusById(jobId, 2, "已存在相同记录！");
      return;
    } else {
      pdwhPatentCategoryDao.save(new PdwhPatentCategory(pubId, patentCategory, patentMainCategoryNo, patentCategoryNo));
    }
    this.updateTaskStatus(jobId, 1, "");

  }

  @Override
  public void updateTaskStatus(Long jobId, int status, String err) {
    try {
      tmpTaskInfoRecordDao.updateTaskStatusById(jobId, status, err);
    } catch (Exception e) {
      logger.error("更新任务状态记录出错！jobId " + jobId, e);
    }
  }

}
