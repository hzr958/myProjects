package com.smate.center.open.service.data.isis;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.data.isis.NsfcPrjReportDao;
import com.smate.center.open.dao.data.isis.NsfcPrjRptPubDao;
import com.smate.center.open.dao.data.isis.NsfcProjectDao;
import com.smate.center.open.isis.model.data.isis.NsfcPrjReport;
import com.smate.center.open.isis.model.data.isis.NsfcPrjRptPub;
import com.smate.center.open.isis.model.data.isis.NsfcProject;
// import com.smate.center.open.model.nsfc.project.NsfcPrjRptPub;



/**
 * save项目结题数据
 * 
 * @author hp
 * @date 2015-10-23
 */
@Service("prjFinalReport2Service")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public class PrjFinalReport2ServiceImpl implements PrjFinalReport2Service {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private NsfcPrjReportDao nsfcPrjReportDao;
  @Autowired
  private NsfcProjectDao nsfcProjectDao;
  @Autowired
  private NsfcPrjRptPubDao nsfcPrjRptPubDao;

  @Override
  public void saveNsfcPrjReport(List<NsfcPrjReport> nsfcPrjReportList) {
    for (NsfcPrjReport nsfcPrjReport : nsfcPrjReportList) {
      nsfcPrjReportDao.save(nsfcPrjReport);
    }
  }

  @Override
  public void saveNsfcProject(List<NsfcProject> nsfcProjectList) {
    for (NsfcProject nsfcProject : nsfcProjectList) {
      nsfcProjectDao.save(nsfcProject);
    }
  }

  @Override
  public void saveNsfcPrjRptPub(List<NsfcPrjRptPub> nsfcPrjRptPubList) {
    for (NsfcPrjRptPub nsfcPrjRptPub : nsfcPrjRptPubList) {
      nsfcPrjRptPubDao.save(nsfcPrjRptPub);
    }
  }

  /** update sie nsfc_project email and pi_psn_name **/
  @SuppressWarnings("deprecation")
  @Override
  public void updateNsfcProject(List<Map<String, Object>> snsPrjMapList) {
    for (Map<String, Object> map : snsPrjMapList) {
      nsfcProjectDao.update("update nsfc_project np set np.email=? , np.pi_psn_name=? where np.prj_id=?",
          new Object[] {ObjectUtils.toString(map.get("EMAIL")), ObjectUtils.toString(map.get("NAME")),
              ((BigDecimal) map.get("PRJ_ID")).longValue()});
    }
  }
}
