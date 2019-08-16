package com.smate.sie.center.task.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.dao.security.role.Sie6InsPortalDao;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.sie.center.task.dao.SieDataSrvPatTmpDao;
import com.smate.sie.center.task.dao.SieDataSrvPatTmpRefreshDao;
import com.smate.sie.center.task.model.SieDataSrvPatTmp;
import com.smate.sie.center.task.model.SieDataSrvPatTmpRefresh;
import com.smate.sie.core.base.utils.dao.pub.SiePatentDao;
import com.smate.sie.core.base.utils.date.DateUtils;
import com.smate.sie.core.base.utils.model.pub.SiePatent;
import com.smate.sie.core.base.utils.pub.dom.PatAppliersBean;
import com.smate.sie.core.base.utils.pub.dom.PatentInfoBean;
import com.smate.sie.core.base.utils.pub.dom.PubDetailDOM;
import com.smate.sie.core.base.utils.pub.service.PatJsonPOService;

/***
 * 生成单位成果数据
 * 
 * @author 叶星源
 * @Date 20180911
 */
@SuppressWarnings("deprecation")
@Service("sieDataSrvPatTmpService")
@Transactional(rollbackOn = Exception.class)
public class SieDataSrvPatTmpServiceImpl implements SieDataSrvPatTmpService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SieDataSrvPatTmpRefreshDao sieDataSrvPatTmpRefreshDao;
  @Autowired
  private SieDataSrvPatTmpDao sieDataSrvPatTmpDao;
  @Autowired
  private Sie6InsPortalDao sieInsPortalDao;
  @Autowired
  private SiePatentDao siePatentDao;
  @Autowired
  private PatJsonPOService patJsonPOService;

  @Override
  public List<SieDataSrvPatTmpRefresh> getNeedRefreshData(int maxSize) {
    try {
      return this.sieDataSrvPatTmpRefreshDao.queryNeedRefresh(maxSize);
    } catch (Exception e) {
      logger.error("获取需要生成的单位成果数据失败", e);
      throw new ServiceException("", e);
    }
  }

  @Override
  public void saveSieDataSrvPatTmpRefresh(SieDataSrvPatTmpRefresh sieDataSrvPatTmpRefresh) {
    sieDataSrvPatTmpRefresh.setStatus(9);
    sieDataSrvPatTmpRefreshDao.save(sieDataSrvPatTmpRefresh);
  }

  @Override
  public void refreshData(SieDataSrvPatTmpRefresh sieDataSrvPatTmpRefresh) {
    SiePatent pat = siePatentDao.get(sieDataSrvPatTmpRefresh.getPatId());
    // 获取json字符串，并且构造成PubDetailDOM对象
    PubDetailDOM detail = patJsonPOService.getDOMByIdAndType(pat.getPatId());
    // 数据处理
    String patentType = "";
    String legalStatus = "";
    // 数据处理填充
    SieDataSrvPatTmp sieDataSrvPatTmp = new SieDataSrvPatTmp();
    sieDataSrvPatTmp.setPatId(sieDataSrvPatTmpRefresh.getPatId());
    sieDataSrvPatTmp.setZhTitle(ObjectUtils.toString(null != pat.getZhTitle() ? pat.getZhTitle() : pat.getEnTitle()));
    sieDataSrvPatTmp.setApplyYear(pat.getApplyYear());
    sieDataSrvPatTmp.setApplyMonth(pat.getApplyMonth());
    sieDataSrvPatTmp.setApplyDay(pat.getApplyDay());
    sieDataSrvPatTmp.setHttp(getPatAddress(pat.getPatId(), pat.getInsId()));
    if (null != detail) {
      PatentInfoBean bean = (PatentInfoBean) detail.getTypeInfo();
      if (bean != null) {
        // *专利类别：发明专利51 实用新型 52 外观专利53 国际专利54
        String patType = bean.getTypeCode();
        if ("51".equals(patType)) {
          patentType = "发明专利";
        } else if ("52".equals(patType)) {
          patentType = "实用新型";
        } else if ("53".equals(patType)) {
          patentType = "外观专利";
        } else if ("54".equals(patType)) {
          patentType = "国际专利";
        } else {
          patentType = "";
        }
        // *专利状态：申请0 授权1
        String legalStatusStr = bean.getPatentStatusCode();
        if ("1".equals(legalStatusStr)) {
          legalStatus = "授权";
        } else if ("0".equals(legalStatusStr)) {
          legalStatus = "申请";
        } else if ("2".equals(legalStatusStr)) {
          legalStatus = "视撤";
        } else if ("3".equals(legalStatusStr)) {
          legalStatus = "有效";
        } else if ("4".equals(legalStatusStr)) {
          legalStatus = "失效";
        } else {
          legalStatus = "";
        }
      }
      String patentNo = null != bean ? bean.getApplicationNo() : "";
      String patentOpenNo = null != bean ? bean.getPublicationOpenNo() : "";
      String remark = detail.getSummary();
      String inventor = detail.getAuthorNames();
      sieDataSrvPatTmp.setRemark(StringEscapeUtils.escapeHtml4(remark));
      sieDataSrvPatTmp.setInventor(inventor);
      sieDataSrvPatTmp.setPatentno(patentNo);// 申请号
      sieDataSrvPatTmp.setPatentOpenNo(patentOpenNo);// 公开号
      sieDataSrvPatTmp.setPatentType(patentType);
      sieDataSrvPatTmp.setLegalStatus(legalStatus);
      sieDataSrvPatTmp.setIpcNo(null != bean ? bean.getIPC() : "");// IPC
      sieDataSrvPatTmp.setPatentee(getPatentee(bean));// 专利权人
      // 授权时间
      /* if ("1".equals(legalStatusStr)) { */
      String[] authDates = DateUtils.splitToYearMothDayByStr(bean.getAuthDate());
      sieDataSrvPatTmp.setStartYear(authDates[0]);
      sieDataSrvPatTmp.setStartMonth(authDates[1]);
      sieDataSrvPatTmp.setStartDay(authDates[2]);
      /* } */
    }
    sieDataSrvPatTmpDao.save(sieDataSrvPatTmp);
    sieDataSrvPatTmpRefresh.setStatus(1);
    sieDataSrvPatTmpRefreshDao.save(sieDataSrvPatTmpRefresh);
  }

  private String getPatentee(PatentInfoBean bean) {
    // 获取专利权人的节点
    List<PatAppliersBean> appliers = bean.getAppliers();
    if (CollectionUtils.isEmpty(appliers) && appliers.size() == 0) {
      return "";
    }
    StringBuilder applierNames = new StringBuilder();
    for (int i = 0; i < appliers.size(); i++) {
      PatAppliersBean app = appliers.get(i);
      String applierName = StringUtils.trimToEmpty(app.getApplierName());
      if (i == 0) {
        applierNames.append(applierName);
      } else {
        applierNames.append(";");
        applierNames.append(applierName);
      }
    }
    return applierNames.toString();
  }


  private String getPatAddress(Long patId, Long insId) {
    StringBuilder sbl = new StringBuilder();
    sbl.append("https://");
    sbl.append(sieInsPortalDao.get(insId).getDomain());
    sbl.append("/pubweb/patent/view?des3Id=");
    sbl.append(ServiceUtil.encodeToDes3(patId + ""));
    return sbl.toString();
  }

}
