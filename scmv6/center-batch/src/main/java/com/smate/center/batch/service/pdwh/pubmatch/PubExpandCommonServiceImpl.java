package com.smate.center.batch.service.pdwh.pubmatch;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.cnki.CnkiPubExpandLogDao;
import com.smate.center.batch.dao.pdwh.pub.isi.IsiPubExpandLogDao;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubExpandLog;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubExpandLog;

/**
 * 成果拆分的公共业务逻辑实现类.
 * 
 * @author mjg
 * 
 */
@Service("pubExpandCommonService")
@Transactional(rollbackFor = Exception.class)
public class PubExpandCommonServiceImpl implements PubExpandCommonService {

  @Autowired
  private IsiPubExpandLogDao isiPubExpandLogDao;
  @Autowired
  private CnkiPubExpandLogDao cnkiPubExpandLogDao;

  /**
   * 获取需要拆分的ISI成果ID.
   * 
   * @return
   */
  @Override
  public List<Long> loadIsiNeedExpandPubId(Integer number) {
    return isiPubExpandLogDao.loadIsiNeedExpandPubId(number);
  }

  /**
   * 获取需要拆分的CNKI成果ID.
   * 
   * @return
   */
  @Override
  public List<Long> loadCnkiNeedExpandPubId(Integer number) {
    return cnkiPubExpandLogDao.loadCnkiNeedExpandPubId(number);
  }

  /**
   * 保存CNKI成果拆分日志.
   * 
   * @param pubLog
   */
  public void saveCnkiNeedExpandLog(CnkiPubExpandLog pubLog) {
    cnkiPubExpandLogDao.saveCnkiPubExpandLog(pubLog);
  }

  /**
   * 保存ISI成果拆分日志.
   * 
   * @param pubLog
   */
  public void saveIsiPubExpandLog(IsiPubExpandLog pubLog) {
    isiPubExpandLogDao.saveIsiPubExpandLog(pubLog);
  }

  /**
   * 更新拆分成果的状态.
   * 
   * @param pubId
   * @param status
   */
  @Override
  public void updateCnkiSplitDataStatus(Long pubId, Integer status) {
    cnkiPubExpandLogDao.updateCnkiSplitDataStatus(pubId, status);
  }

  /**
   * 更新拆分成果的状态.
   * 
   * @param pubId
   * @param status
   */
  @Override
  public void updateIsiSplitDataStatus(Long pubId, Integer status) {
    isiPubExpandLogDao.updateIsiSplitDataStatus(pubId, status);
  }

  /**
   * 记录CNKI成果拆分日志记录.
   * 
   * @param pubId
   * @param splitResultFailed
   * @param errorMsg
   */
  @Override
  public void writeCnkiSplitLog(Long pubId, Integer splitResultFailed, String errorMsg) {
    cnkiPubExpandLogDao.writeIsiSplitLog(pubId, splitResultFailed, errorMsg);
  }

  /**
   * 记录ISI成果拆分日志记录.
   * 
   * @param pubId
   * @param splitResultFailed
   * @param errorMsg
   */
  @Override
  public void writeIsiSplitLog(Long pubId, Integer splitResultFailed, String errorMsg) {
    isiPubExpandLogDao.writeIsiSplitLog(pubId, splitResultFailed, errorMsg);
  }
}
