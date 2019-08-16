package com.smate.center.task.service.tmp;

import java.util.List;

import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;

/**
 * 基准库成果临时任务处理业务接口(不再使用的方法可以删除)
 * 
 * @author LIJUN
 * @date 2018年5月11日
 */
public interface TmpPdwhPubTaskService {

  List<TmpTaskInfoRecord> batchGetJobList(Integer size) throws Exception;

  void startUpdateKeywords(TmpTaskInfoRecord job);

  void updateTaskStatus(Long jobId, int i, String string);

}
