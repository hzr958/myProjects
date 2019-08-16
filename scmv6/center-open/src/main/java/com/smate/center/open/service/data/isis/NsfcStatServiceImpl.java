package com.smate.center.open.service.data.isis;


import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.open.dao.data.isissns.SnsNsfcPrjReportDao;
import com.smate.center.open.dao.data.isissns.SnsNsfcPrjRptPubDao;
import com.smate.center.open.isis.model.data.isis.NsfcPrjPubReport;
import com.smate.center.open.isis.model.data.isissns.SnsNsfcPrjReport;
import com.smate.center.open.isis.model.data.isissns.SnsNsfcPrjRptPub;

/**
 * 向基金委提供项目结题数据-接口
 * 
 * @author hp
 * @date 2015-10-21
 */
@Service("nsfcStatService")
public class NsfcStatServiceImpl implements NsfcStatService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private NsfcPrjFinalStatService nsfcPrjFinalStatService;

  @Autowired
  private NsfcPubStatService nsfcPubStatService;
  @Autowired
  private SnsNsfcPrjReportDao snsNsfcPrjReportDao;
  @Autowired
  private SnsNsfcPrjRptPubDao snsNsfcPrjRptPubDao;

  @Override
  public List<NsfcPrjPubReport> getPrjPubReportList(String insName, Long rptYear) {
    return nsfcPrjFinalStatService.getPrjPubReportList(insName, rptYear);
  }

  @Override
  public List<Map<String, Object>> getStatData2(Map<String, Object> param) {
    // TODO Auto-generated method stub
    return nsfcPrjFinalStatService.getStatData2(param);
  }

  @Override
  public List<Map<String, Object>> getStatData3(Map<String, Object> param) {
    // TODO Auto-generated method stub
    return nsfcPrjFinalStatService.getStatData3(param);
  }

  @Override
  public List<Map<String, Object>> getStatData4(Map<String, Object> param) {
    // TODO Auto-generated method stub
    return nsfcPrjFinalStatService.getStatData4(param);
  }

  @Override
  public List<Map<String, Object>> getStatData5(Map<String, Object> param) {
    // TODO Auto-generated method stub
    return nsfcPrjFinalStatService.getStatData5(param);
  }


  /** 从SNS取数据 */
  @Override
  public List<SnsNsfcPrjReport> getNsfcPrjReportList(Map<String, Object> dataMap) {
    return snsNsfcPrjReportDao.getNsfcPrjReportList(dataMap);
  }

  /** 从SNS取数据 */
  @Override
  public List<SnsNsfcPrjRptPub> getPubsByRptIds(Set<Long> rptIdSet) {
    // TODO Auto-generated method stub
    return snsNsfcPrjRptPubDao.getPubsByRptIds(rptIdSet);
  }

  /**
   * 从SNS取数据
   * 
   * @throws Exception
   */
  @Override
  public Map<String, Object> getPubStatByIns(Map<String, Object> dataMap) throws Exception {
    return nsfcPubStatService.getPubStatByIns(dataMap);
  }

}
