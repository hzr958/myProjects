package com.smate.center.open.service.data.isis;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.data.isis.NsfcPrjPubReportDao;
import com.smate.center.open.isis.model.data.isis.NsfcPrjPubReport;
import com.smate.center.open.isis.model.data.isis.NsfcPrjPubReportPK;

/**
 * 向基金委提供项目结题数据-接口
 * 
 * @author hp
 * @date 2015-10-21
 */
@Service("nsfcPrjFinalStatService")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public class NsfcPrjFinalStatServiceImpl implements NsfcPrjFinalStatService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private NsfcPrjPubReportDao nsfcPrjPubReportDao;

  @Override

  public List<NsfcPrjPubReport> getPrjPubReportList(String insName, Long rptYear) {
    return nsfcPrjPubReportDao.getNsfcPrjPubReportList(insName, rptYear);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void saveNsfcPrjPubReport(List<Map<String, Object>> reportList, Map<Long, String> jidColorMap,
      Map<Long, Long> hxjMap, List<Long> inCategoryJidList) {


    nsfcPrjPubReportDao.deleteByDeliverDate((Date) reportList.get(0).get("DELIVER_DATE"));
    for (Map<String, Object> reportMap : reportList) {
      NsfcPrjPubReport n = new NsfcPrjPubReport();
      n.setCreateDate(new Date());
      n.setEi(Long.valueOf(ObjectUtils.toString(reportMap.get("EI"))));
      n.setGrade(Long.valueOf(ObjectUtils.toString(reportMap.get("GRADE"))));
      n.setInsId(Long.valueOf(ObjectUtils.toString(reportMap.get("INS_ID"))));
      n.setInsName(ObjectUtils.toString(reportMap.get("INS_NAME")));
      n.setIsTag(Long.valueOf(ObjectUtils.toString(reportMap.get("IS_TAG"))));
      n.setIstp(Long.valueOf(ObjectUtils.toString(reportMap.get("ISTP"))));
      if (reportMap.get("JID") != null) {
        n.setJid(Long.valueOf(ObjectUtils.toString(reportMap.get("JID"))));
        n.setRomeoColour(jidColorMap.get(n.getJid()));
        n.setHxj(hxjMap.get(n.getJid()));
      }
      if (n.getHxj() == null) {
        n.setHxj(0L);
      }
      n.setNsfcPrjPubReportPK(new NsfcPrjPubReportPK(Long.valueOf(ObjectUtils.toString(reportMap.get("RPT_ID"))),
          Long.valueOf(ObjectUtils.toString(reportMap.get("PRJ_ID"))),
          Long.valueOf(ObjectUtils.toString(reportMap.get("PUB_ID")))));
      n.setPubType(Long.valueOf(ObjectUtils.toString(reportMap.get("PUB_TYPE"))));
      n.setRptType(Long.valueOf(ObjectUtils.toString(reportMap.get("RPT_TYPE"))));
      n.setRptYear(Long.valueOf(ObjectUtils.toString(reportMap.get("RPT_YEAR"))));
      n.setSci(Long.valueOf(ObjectUtils.toString(reportMap.get("SCI"))));
      n.setSsci(Long.valueOf(ObjectUtils.toString(reportMap.get("SSCI"))));
      n.setStatus(Long.valueOf(ObjectUtils.toString(reportMap.get("STATUS"))));
      n.setUpdateDate(new Date());
      n.setDeliverDate((Date) reportMap.get("DELIVER_DATE"));
      n.setNsfcPrjId(Long.valueOf(ObjectUtils.toString(reportMap.get("NSFC_PRJ_ID"))));
      n.setNsfcRptId(Long.valueOf(ObjectUtils.toString(reportMap.get("NSFC_RPT_ID"))));
      if (NumberUtils.isDigits(ObjectUtils.toString(reportMap.get("SOURCE_DB_ID")))) {
        n.setSourceDbId(((BigDecimal) reportMap.get("SOURCE_DB_ID")).intValue());
      }
      n.setFundinfo(ObjectUtils.toString(reportMap.get("FUNDINFO")));
      n.setPrjExternalNo(ObjectUtils.toString(reportMap.get("PRJ_EXTERNAL_NO")));
      if (n.getIsTag() == 1L) {// 标注了，但是未找到标注信息
        if (StringUtils.indexOf(n.getFundinfo(), n.getPrjExternalNo()) < 1) {
          n.setTagError(1);
          n.setIsError(1);
        }
      }
      if (n.getSci() == 1L || n.getSsci() == 1L) {// 收录了
        if (n.getSourceDbId() == null
            || (n.getSourceDbId() != 16 && n.getSourceDbId() != 17 && !inCategoryJidList.contains(n.getJid()))) {// ，但是未检查到收录信息
          n.setSciSsciError(1);
          n.setIsError(1);
        }
      } else {// 未收录
        if (n.getSourceDbId() != null
            && (n.getSourceDbId() == 16 || n.getSourceDbId() == 17 || inCategoryJidList.contains(n.getJid()))) {// ，但是未检查到收录信息
          n.setMaySciSsci(1);
          n.setIsError(1);
        }
      }
      n.setPubTitle(ObjectUtils.toString(reportMap.get("PUB_TITLE")));
      nsfcPrjPubReportDao.save(n);
    }

  }

  @Override
  public List<Map<String, Object>> getStatData2(Map<String, Object> params) {
    return nsfcPrjPubReportDao.getStatData2(params);
  }

  @Override
  public List<Map<String, Object>> getStatData3(Map<String, Object> params) {
    return nsfcPrjPubReportDao.getStatData3(params);
  }

  @Override
  public List<Map<String, Object>> getStatData4(Map<String, Object> params) {
    return nsfcPrjPubReportDao.getStatData4(params);
  }

  @Override
  public List<Map<String, Object>> getStatData5(Map<String, Object> params) {
    return nsfcPrjPubReportDao.getStatData5(params);
  }
}
