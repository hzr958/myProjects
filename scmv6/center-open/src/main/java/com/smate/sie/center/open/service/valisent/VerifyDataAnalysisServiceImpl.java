package com.smate.sie.center.open.service.valisent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.sie.core.base.utils.dao.validate.KpiValidateDetailDao;
import com.smate.sie.core.base.utils.dao.validate.KpiValidateLogDao;
import com.smate.sie.core.base.utils.dao.validate.KpiValidateMainDao;
import com.smate.sie.core.base.utils.dao.validate.KpiValidateMainDataDao;
import com.smate.sie.core.base.utils.model.validate.KpiValidateDetail;
import com.smate.sie.core.base.utils.model.validate.KpiValidateLog;
import com.smate.sie.core.base.utils.model.validate.KpiValidateMain;
import com.smate.sie.core.base.utils.model.validate.KpiValidateMainData;
import com.smate.sie.core.base.utils.random.RandomStrUtil;

/**
 * 科研验证数据加工服务实现
 * 
 * @author hd
 *
 */
@Service("verifyDataAnalysisService")
@Transactional(rollbackFor = Exception.class)
public class VerifyDataAnalysisServiceImpl implements VerifyDataAnalysisService {
  @Resource
  private KpiValidateDetailDao kpiValidateDetailDao;
  @Resource
  private KpiValidateMainDao kpiValidateMainDao;
  @Resource
  private KpiValidateMainDataDao kpiValidateMainDataDao;
  @Resource
  private KpiValidateLogDao kpiValidateLogDao;
  @Value("${verifyPsnOrg.restful.url}")
  private String psnOrOrgUrl;
  @Value("${initOpen.restful.url}")
  private String paperUrl;

  @SuppressWarnings("unchecked")
  @Override
  public String saveMain(Map<String, Object> paramet) {
    Map<String, Object> basicInfo = (Map<String, Object>) paramet.get("basic_info");
    String uuid = null;
    if (basicInfo != null) {
      String title = getTitle(basicInfo.get("zh_title"), basicInfo.get("en_title"));
      String grandOrg = basicInfo.get("grant_org") == null ? null : basicInfo.get("grant_org").toString().trim();
      // 申请年份支持两种格式：yyyy-MM-dd，yyyy。且年份超过4位数，视为无效年份，置空
      String prpYear = dateToYear(basicInfo.get("prp_date"));
      if (StringUtils.isBlank(prpYear)) {
        prpYear = convertYear(basicInfo.get("prp_date"));
      }
      if (prpYear.length() > 4) {
        prpYear = "";
      }
      String orgName = basicInfo.get("org_name") == null ? "" : basicInfo.get("org_name").toString().trim();
      String para = String.valueOf(paramet.get("data"));
      String verNo = basicInfo.get("version_no") == null ? null : basicInfo.get("version_no").toString().trim();
      String keyCode = basicInfo.get("key_code") == null ? null : basicInfo.get("key_code").toString().trim();
      String keyType = basicInfo.get("key_type") == null ? null : basicInfo.get("key_type").toString().trim();
      KpiValidateMain valiMain = doCheckDup(keyCode, verNo, keyType, grandOrg);
      if (valiMain == null) {// 新增
        valiMain = new KpiValidateMain(StringUtils.substring(title, 0, 200), new Date(),
            StringUtils.substring(grandOrg, 0, 20), 1, 0, prpYear, orgName, keyCode, keyType, verNo);
      } else {// 更新
        doUpdate(valiMain, StringUtils.substring(title, 0, 200), StringUtils.substring(grandOrg, 0, 20), 1, 0, prpYear,
            orgName, keyCode, keyType, verNo);
      }
      kpiValidateMainDao.save(valiMain);
      uuid = valiMain.getUuId();
      // 保存data
      KpiValidateMainData mainData = kpiValidateMainDataDao.get(uuid);
      if (mainData == null) {
        mainData = new KpiValidateMainData(uuid);
      }
      mainData.setData(para);
      kpiValidateMainDataDao.save(mainData);
    }
    return uuid;
  }

  /**
   * 查重
   * 
   * @param keyCode
   * @param verNo
   * @param keyType
   * @return
   */
  private KpiValidateMain doCheckDup(String keyCode, String verNo, String keyType, String dataFrom) {
    if (keyCode == null || keyType == null || dataFrom == null) {
      return null;
    }
    List<KpiValidateMain> list = kpiValidateMainDao.getByKeyCodeAndType(keyCode, verNo, keyType, dataFrom);
    if (list == null || list.size() == 0) {
      return null;
    } else {
      return list.get(0);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Map<String, Object> doDataAnalysis(Map<String, Object> content, String uuid, String fromKeyCode) {
    Map resultMap = null;
    String participantNames = "";
    Map<String, Object> basicinfo = (Map<String, Object>) content.get("basic_info");
    Boolean isPRP = true;
    Object okeyType = basicinfo.get("key_type");
    if (okeyType != null
        && (StringUtils.equalsIgnoreCase(okeyType.toString().trim(), SciResearchVerfyConstant.KEY_TYPE_PROGRESS)
            || StringUtils.equalsIgnoreCase(okeyType.toString().trim(), SciResearchVerfyConstant.KEY_TYPE_CONCLUDE))) {
      isPRP = false;
    }
    /**
     * 根据key_type ，如果是2或3，只拆出项目成果等待验证，其他数据不拆到detail表
     */
    // 拆分人员信息
    List<Map<String, Object>> psns = (List) content.get("persons");
    if (isPRP) {
      participantNames = this.spiltPsnIntoDetail(psns, uuid);
    } else {
      participantNames = this.spiltPsnIntoDetailPRJ(psns, uuid);
    }
    // 拆分项目成果信息
    List<Map<String, Object>> pubs = (List) content.get("publications");
    this.spiltPubIntoDetail(pubs, uuid, participantNames, isPRP);
    if (isPRP) {
      // 拆分单位信息
      List<Map<String, Object>> org = (List) content.get("organizations");
      this.spiltOrgIntoDetail(org, uuid);
    }

    KpiValidateMain valiMain = kpiValidateMainDao.get(uuid);
    resultMap = VerifyDataAnalysisServiceImpl.buildResultMap(SciResearchVerfyConstant.ALREADY_ACCEPT, uuid,
        SciResearchVerfyConstant.SUCESS, fromKeyCode, valiMain.getSmDate());

    return resultMap;

  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public String spiltPsnIntoDetail(List<Map<String, Object>> list, String uuid) {
    StringBuffer participantNames = new StringBuffer();
    // 拆分人员
    if (list != null && list.size() > 0) {
      Map<String, Object> mapPsn = new HashMap<String, Object>();
      mapPsn.put("openid", "88888888");// 系统默认openId
      mapPsn.put("token", "11111111verfypsn");
      for (int i = 0; i < list.size(); i++) {
        String keyCode = getKeyCode();
        Map<String, Object> data = new HashMap<String, Object>();
        Object oName = list.get(i).get("psn_name");
        data.put("phoneNo", list.get(i).get("mobile"));
        data.put("name", list.get(i).get("psn_name"));
        data.put("certNo", list.get(i).get("cert_no"));
        data.put("certType", list.get(i).get("cert_type"));
        data.put("email", list.get(i).get("email"));
        data.put("wechatNo", list.get(i).get("wechart_no"));
        data.put("insName", list.get(i).get("org_name"));
        data.put("url", list.get(i).get("psn_url"));
        data.put("keyCode", keyCode);
        List mapInfoList = new ArrayList();
        mapInfoList.add(data);
        mapPsn.put("data", mapInfoList);
        String paramsIn = JacksonUtils.mapToJsonStr(mapPsn);
        KpiValidateDetail detail =
            new KpiValidateDetail(uuid, keyCode, SciResearchVerfyConstant.VERIFY_TYPE_PSN, paramsIn, psnOrOrgUrl, 0);
        kpiValidateDetailDao.save(detail);
        if (oName != null && StringUtils.isNotBlank(oName.toString())) {
          if (i != 0) {
            participantNames.append(";");
          }
          participantNames.append(oName.toString());
        }

        Object psnM = list.get(i).get("psn_pubs");
        if (psnM != null) {
          List<Map<String, Object>> psnPubs = (List) psnM;
          Map<String, Object> mapPsnPubOpen = new HashMap<String, Object>();
          mapPsnPubOpen.put("openid", 99999999L);// 系统默认openId
          mapPsnPubOpen.put("token", "11111111jyh99kls");
          for (int j = 0; j < psnPubs.size(); j++) {
            keyCode = getKeyCode();
            Map<String, Object> mapPsnPub = new HashMap<String, Object>();
            Map<String, Object> datapub = new HashMap<String, Object>();
            Map<String, Object> datapsn = new HashMap<String, Object>();
            List mapPubInfoList = new ArrayList();
            datapsn.put("name", list.get(i).get("psn_name"));
            datapsn.put("email", list.get(i).get("email"));
            datapsn.put("phone", list.get(i).get("mobile"));
            datapub.put("keyCode", keyCode);
            datapub.put("title", getTitle(psnPubs.get(j).get("zh_title"), psnPubs.get(j).get("en_title")));
            datapub.put("authorNames", psnPubs.get(j).get("authors"));
            mapPubInfoList.add(datapub);
            mapPsnPub.put("psnInfo", datapsn);
            mapPsnPub.put("pubInfoList", mapPubInfoList);
            mapPsnPubOpen.put("data", JacksonUtils.mapToJsonStr(mapPsnPub));
            String paramsInPub = JacksonUtils.mapToJsonStr(mapPsnPubOpen);
            KpiValidateDetail psnPubdetail =
                new KpiValidateDetail(uuid, keyCode, SciResearchVerfyConstant.VERIFY_TYPE_PSNPUB, paramsInPub, paperUrl,
                    0, detail == null ? null : detail.getId());
            kpiValidateDetailDao.save(psnPubdetail);
          }
        }
      }
    }
    return participantNames.toString();

  }

  private String spiltPsnIntoDetailPRJ(List<Map<String, Object>> list, String uuid) {
    StringBuffer participantNames = new StringBuffer();
    // 拆分人员
    if (list != null && list.size() > 0) {
      for (int i = 0; i < list.size(); i++) {
        Object oName = list.get(i).get("psn_name");
        if (oName != null && StringUtils.isNotBlank(oName.toString())) {
          if (i != 0) {
            participantNames.append(";");
          }
          participantNames.append(oName.toString());
        }
      }
    }
    return participantNames.toString();

  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void spiltPubIntoDetail(List<Map<String, Object>> pubs, String uuid, String participantNames,
      Boolean emptyPrjCode) {
    Map<String, Object> mapOpen = new HashMap<String, Object>();
    mapOpen.put("openid", 99999999L);// 系统默认openId
    mapOpen.put("token", "11111111lxj3219s");
    if (pubs != null && pubs.size() > 0) {
      for (int i = 0; i < pubs.size(); i++) {
        /* ROL-6579 publication大节点下，pub_type 为期刊论文 或 会议论文 才生产kpi_validate_detail记录，其他类型不验证 */
        Object pubType = pubs.get(i).get("pub_type");
        if (pubType == null || (!StringUtils.equalsIgnoreCase(pubType.toString().trim(),
            SciResearchVerfyConstant.PUB_TYPE_VALUE1)
            && !StringUtils.equalsIgnoreCase(pubType.toString().trim(), SciResearchVerfyConstant.PUB_TYPE_VALUE2))) {
          continue;
        }
        String keyCode = getKeyCode();
        List mapInfoList = new ArrayList();
        Map<String, Object> mapData = new HashMap<String, Object>();
        Map<String, Object> mapInfo = new HashMap<String, Object>();
        mapInfo.put("keyCode", keyCode);
        mapInfo.put("title", getTitle(pubs.get(i).get("zh_title"), pubs.get(i).get("en_title")));
        mapInfo.put("doi", pubs.get(i).get("doi"));
        String publishYear = dateToYear(pubs.get(i).get("publish_date"));
        if (StringUtils.isBlank(publishYear)) {
          publishYear = convertYear(pubs.get(i).get("publish_date"));
        }
        if (publishYear.length() > 4) {
          publishYear = "";
        }
        mapInfo.put("publishYear", publishYear);
        mapInfo.put("journalName", pubs.get(i).get("jname"));
        if (!emptyPrjCode) {
          mapInfo.put("fundingInfo", pubs.get(i).get("fundinfo"));
        }
        mapInfo.put("authorNames", pubs.get(i).get("authors"));
        mapInfoList.add(mapInfo);
        mapData.put("participantNames", participantNames);
        mapData.put("pubInfoList", mapInfoList);
        mapOpen.put("data", JacksonUtils.mapToJsonStr(mapData));
        String paramsIn = JacksonUtils.mapToJsonStr(mapOpen);
        KpiValidateDetail detail =
            new KpiValidateDetail(uuid, keyCode, SciResearchVerfyConstant.VERIFY_TYPE_PUB, paramsIn, paperUrl, 0);
        kpiValidateDetailDao.save(detail);
      }
    }

  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void spiltOrgIntoDetail(List<Map<String, Object>> orgs, String uuid) {
    Map<String, Object> mapOpen = new HashMap<String, Object>();
    mapOpen.put("openid", "88888888");// 系统默认openId
    mapOpen.put("token", "11111111verfyorg");
    if (orgs != null && orgs.size() > 0) {
      for (int i = 0; i < orgs.size(); i++) {
        Object orgNameObj = orgs.get(i).get("org_name");
        Object orgNoObj = orgs.get(i).get("uniform_id");
        String orgName = orgNameObj == null ? null : orgNameObj.toString().trim();
        if (StringUtils.isBlank(orgName)) {
          /* ROL-6579 orgnazation大节点下，org_name为空或unifrom_id为空的不产生kpi_validate_detail记录。 */
          continue;
        }
        String keyCode = getKeyCode();
        Map<String, Object> mapData = new HashMap<String, Object>();
        List mapInfoList = new ArrayList();
        mapData.put("keyCode", keyCode);
        mapData.put("name", orgNameObj);
        mapData.put("orgNo", orgNoObj);
        mapData.put("url", orgs.get(i).get("ins_url"));
        mapInfoList.add(mapData);
        mapOpen.put("data", mapInfoList);
        String paramsIn = JacksonUtils.mapToJsonStr(mapOpen);
        KpiValidateDetail detail =
            new KpiValidateDetail(uuid, keyCode, SciResearchVerfyConstant.VERIFY_TYPE_ORG, paramsIn, psnOrOrgUrl, 0);
        kpiValidateDetailDao.save(detail);
      }
    }

  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static Map buildResultMap(Integer itemStatus, String uuid, String itemMsg, String keyCode, Date smTime) {
    Map resultMap = new HashMap();
    String smTimes = "";
    if (smTime != null) {
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      try {
        smTimes = format.format(smTime);
      } catch (Exception e) {
        smTimes = "";
      }

    }
    resultMap.put("submitTime", smTimes);
    resultMap.put("itemStatus", itemStatus);
    resultMap.put("uuid", uuid);
    if (StringUtils.isNotBlank(keyCode)) {
      resultMap.put("keyCode", keyCode);
    }
    resultMap.put("itemMsg", itemMsg);
    return resultMap;
  }

  // 需要测试事务
  private String dateToYear(Object publishdate) {
    String year = "";
    if (publishdate != null) {
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      try {
        Date publishDate = format.parse(publishdate.toString());
        Calendar c = Calendar.getInstance();
        c.setTime(publishDate);
        Integer lyear = c.get(Calendar.YEAR);
        if (lyear != null) {
          year = lyear.toString();
        }
      } catch (Exception e) {
        return year;
      }

    }
    return year;

  }

  private String convertYear(Object prpYear) {
    String year = "";
    if (prpYear != null) {
      DateFormat format = new SimpleDateFormat("yyyy");
      try {
        Date publishDate = format.parse(prpYear.toString());
        Calendar c = Calendar.getInstance();
        c.setTime(publishDate);
        Integer lyear = c.get(Calendar.YEAR);
        if (lyear != null) {
          year = lyear.toString();
        }
      } catch (Exception e) {
        return year;
      }

    }
    return year;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Map<String, Object> doDataValidate(Map<String, Object> content, String fromKeyCode) {
    Map resultMap = null;
    if (content.get("basic_info") == null) {
      resultMap = VerifyDataAnalysisServiceImpl.buildResultMap(SciResearchVerfyConstant.PARAME_ERROR, "",
          SciResearchVerfyConstant.BASIC_EMPTY, fromKeyCode, null);
      return resultMap;
    }
    Map<String, Object> basicInfo = (Map<String, Object>) content.get("basic_info");
    Object keyCode = basicInfo.get("key_code");
    if (keyCode == null || StringUtils.isBlank(keyCode.toString())) {
      resultMap = VerifyDataAnalysisServiceImpl.buildResultMap(SciResearchVerfyConstant.PARAME_ERROR, "",
          SciResearchVerfyConstant.KEY_CODE_EMPTY, fromKeyCode, null);
      return resultMap;
    }
    Object keyType = basicInfo.get("key_type");
    if (keyType == null || StringUtils.isBlank(keyType.toString())) {
      resultMap = VerifyDataAnalysisServiceImpl.buildResultMap(SciResearchVerfyConstant.PARAME_ERROR, "",
          SciResearchVerfyConstant.KEY_TYPE_EMPTY, fromKeyCode, null);
      return resultMap;
    }
    Object grandOrg = basicInfo.get("grant_org");
    if (grandOrg == null || StringUtils.isBlank(grandOrg.toString())) {
      resultMap = VerifyDataAnalysisServiceImpl.buildResultMap(SciResearchVerfyConstant.PARAME_ERROR, "",
          SciResearchVerfyConstant.GRANT_ORG_EMPTY, fromKeyCode, null);
      return resultMap;
    }
    /* 查重记录多条，数据异常 */
    String verNo = basicInfo.get("version_no") == null ? null : basicInfo.get("version_no").toString().trim();
    Integer cnt =
        kpiValidateMainDao.getContKeyCodeAndType(keyCode.toString(), verNo, keyType.toString(), grandOrg.toString());
    if (cnt > 1) {
      resultMap = VerifyDataAnalysisServiceImpl.buildResultMap(SciResearchVerfyConstant.PARAME_ERROR, "",
          SciResearchVerfyConstant.DUP_INCORECT, fromKeyCode, null);
      return resultMap;
    }

    if (content.get("persons") == null) {
      resultMap = VerifyDataAnalysisServiceImpl.buildResultMap(SciResearchVerfyConstant.PARAME_ERROR, "",
          SciResearchVerfyConstant.PSN_EMPTY, fromKeyCode, null);
      return resultMap;
    }
    if (content.get("publications") == null) {
      resultMap = VerifyDataAnalysisServiceImpl.buildResultMap(SciResearchVerfyConstant.PARAME_ERROR, "",
          SciResearchVerfyConstant.PUB_EMPTY, fromKeyCode, null);
      return resultMap;
    }
    if (content.get("organizations") == null) {
      resultMap = VerifyDataAnalysisServiceImpl.buildResultMap(SciResearchVerfyConstant.PARAME_ERROR, "",
          SciResearchVerfyConstant.ORG_EMPTY, fromKeyCode, null);
      return resultMap;
    }
    // 项目成员不能为空
    List<Map<String, Object>> psns = (List) content.get("persons");
    if (psns == null || psns.size() <= 0) {
      resultMap = VerifyDataAnalysisServiceImpl.buildResultMap(SciResearchVerfyConstant.PARAME_ERROR, "",
          SciResearchVerfyConstant.PSN_EMPTY, fromKeyCode, null);
      return resultMap;
    }
    return resultMap;
  }

  private String getTitle(Object zhO, Object enO) {
    String title = zhO == null ? "" : zhO.toString(), enTitle = enO == null ? "" : enO.toString();
    if (StringUtils.isBlank(title)) {
      title = enTitle;
    }
    return title;

  }

  /**
   * 科研验证，内部唯一标识keyCode
   * 
   * @return
   * @throws Exception
   */
  private String getKeyCode() {
    // String uuid = UUID.randomUUID().toString().replaceAll("-", "");
    String uuid = RandomStrUtil.gainRandomStr();
    while (StringUtils.isBlank(uuid)) {
      uuid = RandomStrUtil.gainRandomStr();
    }
    return uuid;
  }

  @Override
  public void doUpdate(KpiValidateMain valiMain, String title, String dataFrom, Integer priority, Integer status,
      String prpYear, String orgName, String keyCode, String keyType, String versionNo) {
    List<KpiValidateDetail> details = kpiValidateDetailDao.getByUUID2(valiMain.getUuId());
    if (details != null) {
      for (KpiValidateDetail detail : details) {
        List<KpiValidateLog> logs = kpiValidateLogDao.getByDetail(detail.getId());
        if (logs != null) {
          for (KpiValidateLog log : logs) {
            kpiValidateLogDao.delete(log.getId());
          }
        }
        kpiValidateDetailDao.delete(detail.getId());
      }
    }
    valiMain.setTitle(title);
    valiMain.setSmDate(new Date());
    valiMain.setDataFrom(dataFrom);
    valiMain.setPriority(1);
    valiMain.setStatus(0);
    valiMain.setPrpYear(prpYear);
    valiMain.setOrgName(orgName);
    valiMain.setEndDate(null);
    valiMain.setReceiptTime(null);
    valiMain.setReceiptCount(0);
    valiMain.setKeyCode(keyCode);
    valiMain.setKeyType(keyType);
    valiMain.setVersionNo(versionNo);
  }


}
