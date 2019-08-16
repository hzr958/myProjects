package com.smate.center.batch.service.pdwh.pubmatch;

import java.util.List;

import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubExpandLog;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubExpandLog;

/**
 * 成果拆分的公共业务逻辑接口.
 * 
 * @author mjg
 * 
 */
public interface PubExpandCommonService {

  /**
   * 获取ISI拆分的成果ID任务.
   * 
   * @return
   */
  List<Long> loadIsiNeedExpandPubId(Integer number);

  /**
   * 获取CNKI拆分的成果ID任务.
   * 
   * @return
   */
  List<Long> loadCnkiNeedExpandPubId(Integer number);

  /**
   * 保存CNKI成果拆分日志.
   * 
   * @param pubLog
   */
  void saveCnkiNeedExpandLog(CnkiPubExpandLog pubLog);

  /**
   * 保存ISI成果拆分日志.
   * 
   * @param pubLog
   */
  void saveIsiPubExpandLog(IsiPubExpandLog pubLog);

  /**
   * 更新CNKI拆分成果的状态.
   * 
   * @param pubId
   * @param status
   */
  void updateCnkiSplitDataStatus(Long pubId, Integer status);

  /**
   * 更新ISI拆分成果的状态.
   * 
   * @param pubId
   * @param status
   */
  void updateIsiSplitDataStatus(Long pubId, Integer status);

  /**
   * 记录CNKI成果拆分日志记录.
   * 
   * @param pubId
   * @param splitResultFailed
   * @param errorMsg
   */
  void writeCnkiSplitLog(Long pubId, Integer splitResultFailed, String errorMsg);

  /**
   * 记录ISI成果拆分日志记录.
   * 
   * @param pubId
   * @param splitResultFailed
   * @param errorMsg
   */
  void writeIsiSplitLog(Long pubId, Integer splitResultFailed, String errorMsg);
}
