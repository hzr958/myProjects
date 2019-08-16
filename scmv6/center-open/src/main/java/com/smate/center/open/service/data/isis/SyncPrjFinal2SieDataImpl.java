package com.smate.center.open.service.data.isis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.exception.OpenIsisStatDataException;
import com.smate.center.open.isis.model.data.isis.NsfcPrjReport;
import com.smate.center.open.isis.model.data.isis.NsfcPrjRptPub;
import com.smate.center.open.isis.model.data.isis.NsfcProject;
// import com.smate.center.open.model.nsfc.project.NsfcPrjRptPub;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * SNS中的结题报告数据同步到SIE
 * 
 * @author hp
 * @date 2015-10-23
 */

@Transactional(rollbackFor = Exception.class)
public class SyncPrjFinal2SieDataImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PrjFinalReportService prjFinalReportService;
  @Autowired
  private PdwhJournalService pdwhJournalService;
  @Autowired
  private NsfcPrjFinalStatService nsfcPrjFinalStatService;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  /**
   * 
   * @param deliver_date 报告提交日期
   * @throws OpenException
   */
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {

      Object data = paramet.get(OpenConsts.MAP_DATA);
      @SuppressWarnings("unchecked")
      Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
      Object o = dataMap.get("deliver_start_date");
      Object o2 = dataMap.get("deliver_end_date");
      Date deliverStartDate = null, deliverEndDate = null;
      if (o == null) {
        deliverStartDate = DateUtils.truncate(DateUtils.addDays(new Date(), -1), Calendar.DATE);
      } else {
        deliverStartDate = DateUtils.parseDate(o.toString(), "yyyy-MM-dd");
      }
      if (o2 == null) {
        deliverEndDate = DateUtils.truncate(DateUtils.addDays(new Date(), -1), Calendar.DATE);
      } else {
        deliverEndDate = DateUtils.parseDate(o2.toString(), "yyyy-MM-dd");
      }
      for (Date deliverDate = deliverStartDate; deliverDate.compareTo(deliverEndDate) <= 0; deliverDate =
          DateUtils.addDays(deliverDate, 1)) {
        // 获取结题报告列表
        List<Map<String, Object>> reportList = prjFinalReportService.getPrjFinalReportList(deliverDate);
        if (reportList.size() > 0) {
          // 获取是否开放存储
          Set<Long> jidSet = new HashSet<Long>();
          for (Map<String, Object> map : reportList) {
            if (map.get("JID") != null) {
              jidSet.add(((BigDecimal) map.get("JID")).longValue());
            }
          }
          Map<Long, String> jidColorMap = pdwhJournalService.getJournalRomeoColour(jidSet);
          Map<Long, Long> hxjMap = pdwhJournalService.getHxj(jidSet);
          List<Long> inJournalCategoryjidList = pdwhJournalService.getIsInJournalCategory(jidSet);
          // 数据同步到SIE
          prjFinalReportService.saveNsfcPrjPubReport(reportList, jidColorMap, hxjMap, inJournalCategoryjidList);
        }

        // 同步NSFC_PRJ_REPORT NSFC_PROJECT NSFC_PRJ_RPT_PUB 数据
        List<NsfcPrjReport> nsfcPrjReportList = prjFinalReportService.getNsfcPrjReportListByDeliverDate(deliverDate);
        Set<Long> rptIdSet = new HashSet<Long>();
        Set<Long> prjIdSet = new HashSet<Long>();
        for (NsfcPrjReport n : nsfcPrjReportList) {
          rptIdSet.add(n.getRptId());
          prjIdSet.add(n.getPrjId());
        }
        if (rptIdSet.size() > 0) {
          List<NsfcPrjRptPub> nsfcPrjRptPubList = prjFinalReportService.getNsfcPrjRptPubListByRptId(rptIdSet);
          prjFinalReportService.saveNsfcPrjReport(nsfcPrjReportList);
          prjFinalReportService.saveNsfcPrjRptPub(nsfcPrjRptPubList);
        }
        if (prjIdSet.size() > 0) {
          List<NsfcProject> nsfcProjectList = prjFinalReportService.getNsfcProjectListByPrjId(prjIdSet);
          prjFinalReportService.saveNsfcProject(nsfcProjectList);

          // update rol.nsfc_project.email rol.nsfc_project.pi_psn_name
          List<Map<String, Object>> snsPrjMapList = prjFinalReportService.getPrjInfoMap(prjIdSet);
          prjFinalReportService.updateNsfcProject(snsPrjMapList);
        }

      }
      Map<String, Object> temp = new HashMap<String, Object>();
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, "scm-0000");// 响应成功
      List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
      temp.put(OpenConsts.RESULT_DATA, dataList);
      return temp;
    } catch (Exception e) {
      logger.error("获取项目结题数据服务异常 map=" + paramet.toString(), e);
      throw new OpenIsisStatDataException(e);
    }
  }

}
