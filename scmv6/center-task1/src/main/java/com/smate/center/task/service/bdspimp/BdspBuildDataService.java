package com.smate.center.task.service.bdspimp;

import java.util.List;

import com.smate.center.task.model.bdsp.BdspPatentBase;
import com.smate.center.task.model.pdwh.quartz.PdwhPublication;
import com.smate.center.task.model.snsbak.bdsp.BdspProject;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

public interface BdspBuildDataService {
  /**
   * 更新任务状态
   * 
   * @param status
   * @param msg
   * @param dataId
   * @param typeId
   */
  void updateLogRecord(Integer status, String msg, Long dataId, int typeId);

  List<Person> psnList(Integer size);

  void handlePsnInfo(Person one) throws Exception;

  List<BdspProject> prjList(Integer size);

  void handlePrjInfo(BdspProject one) throws Exception;

  List<PubPdwhDetailDOM> paperList(Integer size);

  void handlePaperInfo(PubPdwhDetailDOM one) throws Exception;

  List<PubPdwhDetailDOM> patentList(Integer size);

  void handlePatentInfo(PubPdwhDetailDOM one) throws Exception;

  List<BdspPatentBase> UpdatePatentList(Integer size);

  void UpdatePatentType(BdspPatentBase one) throws Exception;

}
