package com.smate.center.task.service.sns.quartz;

import java.util.List;

import com.smate.center.task.model.pdwh.pub.JxkjtPrpInfo;
import com.smate.center.task.model.pdwh.pub.JxkjtPrpInfoTemp;
import com.smate.center.task.model.pdwh.pub.JxstcPrpInfo;
import com.smate.center.task.model.pdwh.pub.JxstcPrpInfoTemp;

/**
 * 
 * @author zzx
 *
 */
public interface JxstcPrpInfoIndexService {

  List<JxstcPrpInfo> findList(int batchSize) throws Exception;

  JxstcPrpInfoTemp dohandle(JxstcPrpInfo one) throws Exception;

  void saveJxstcPrpInfoTemp(JxstcPrpInfoTemp jxstcPrpInfoTemp) throws Exception;


  List<JxkjtPrpInfo> findList2(int batchSize) throws Exception;

  JxkjtPrpInfoTemp dohandle2(JxkjtPrpInfo one) throws Exception;


  void saveJxkjtPrpInfoTemp(JxkjtPrpInfoTemp jxkjtPrpInfoTemp) throws Exception;

}
