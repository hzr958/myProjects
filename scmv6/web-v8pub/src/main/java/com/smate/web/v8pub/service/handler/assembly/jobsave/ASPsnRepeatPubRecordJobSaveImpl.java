package com.smate.web.v8pub.service.handler.assembly.jobsave;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.job.TmpTaskInfoRecord;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.job.TmpTaskInfoRecordService;

/**
 * 个人重复成果分组任务
 * 
 * @author YJ
 *
 *         2018年9月10日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPsnRepeatPubRecordJobSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private TmpTaskInfoRecordService tmpTaskInfoRecordService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      if (pub.pubGenre != PubGenreConstants.PSN_PUB) {
        return null;
      }
      // 个人重复成果分组任务
      TmpTaskInfoRecord psnRepeatRecord = tmpTaskInfoRecordService.getPsnRepeatPubRecord(pub.psnId);
      if (psnRepeatRecord != null) {
        psnRepeatRecord.setStatus(0);
        psnRepeatRecord.setHandletime(null);
        psnRepeatRecord.setErrMsg(null);
      } else {
        psnRepeatRecord = new TmpTaskInfoRecord();
        psnRepeatRecord.setHandleId(pub.psnId);
        psnRepeatRecord.setJobType(11);
        psnRepeatRecord.setStatus(0);
      }
      tmpTaskInfoRecordService.saveOrUpdate(psnRepeatRecord);
    } catch (Exception e) {
      logger.error("个人成果触发重复成果分组任务添加记录错误！pubId:" + pub.pubId + ",psnId:" + pub.psnId, e);
      // 不往外抛出异常，不影响业务逻辑
    }
    return null;
  }

}
