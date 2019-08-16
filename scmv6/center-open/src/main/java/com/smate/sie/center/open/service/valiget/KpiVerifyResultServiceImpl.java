package com.smate.sie.center.open.service.valiget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.sie.center.open.service.valisent.SciResearchVerfyConstant;
import com.smate.sie.core.base.utils.dao.validate.KpiValidateDetailDao;
import com.smate.sie.core.base.utils.dao.validate.KpiValidateMainDao;
import com.smate.sie.core.base.utils.dao.validate.KpiValidateMainDataDao;
import com.smate.sie.core.base.utils.model.validate.KpiValidateDetail;
import com.smate.sie.core.base.utils.model.validate.KpiValidateMain;
import com.smate.sie.core.base.utils.model.validate.KpiValidateMainData;

/**
 * 科研认证结果加工
 * 
 * @author ztg
 *
 */
@Service("kpiVerifyResultService")
@Transactional(rollbackFor = Exception.class)
public class KpiVerifyResultServiceImpl implements KpiVerifyResultService {

  @Autowired
  private KpiValidateMainDao kpiValidateMaindao;

  @Autowired
  private KpiValidateDetailDao kpiValidateDetailDao;
  @Autowired
  private KpiValidateMainDataDao kpiValidateMainDataDao;


  /**
   * 根据请求参数uuid 判断主表，从detail表中获取数据拼接json串
   */
  @SuppressWarnings("rawtypes")
  @Override
  public Map constituteContent(Map<String, Object> paramet) {
    Map resultMap = null;
    Map<String, Object> content = new HashMap<String, Object>();
    String uuid = (String) paramet.get("uuid");
    KpiValidateMain kpiVdMain = kpiValidateMaindao.get(uuid);
    KpiValidateMainData kpiVdMaindata = kpiValidateMainDataDao.get(uuid);
    if (kpiVdMain.getStatus() == SciVerificationGainConstant.MAIN_STATUS_COMPLETE) {
      if (kpiVdMain.getReceiptTime() == null || kpiVdMain.getReceiptCount() == null
          || kpiVdMain.getReceiptCount() < 5) {
        content = this.buildContent(kpiVdMaindata == null ? null : kpiVdMaindata.getData(), kpiVdMain);;
        resultMap = KpiVerifyResultServiceImpl.buildResultMap(uuid, content, SciVerificationGainConstant.GAIN_SUCCESS,
            SciVerificationGainConstant.UUID_DATA_VERIFY_SUCCESS);
        kpiVdMain.setReceiptTime(new Date());
        kpiVdMain.setReceiptCount((kpiVdMain.getReceiptCount() == null ? 0 : kpiVdMain.getReceiptCount()) + 1);
        kpiValidateMaindao.saveOrUpdate(kpiVdMain);
      } else {// 已经返回验证成功结果
        resultMap = KpiVerifyResultServiceImpl.buildResultMap(uuid, content,
            SciVerificationGainConstant.GAIN_HAS_RETURN, SciVerificationGainConstant.UUID_DATA_VERIFY_HASRETURN);
      }
    } else if (kpiVdMain.getStatus() == SciVerificationGainConstant.MAIN_STATUS_UNCOMPLETE) {// 所有验证还没完成
      Long errorCount = kpiValidateDetailDao.getErrorCount(kpiVdMain.getUuId());// 验证失败条数

      if (errorCount == 0) {// KPI_VALIDATE_DETAIL表中uuid对应数据确实处于待验证中
        resultMap = KpiVerifyResultServiceImpl.buildResultMap(uuid, content, SciVerificationGainConstant.GAIN_ERROR,
            SciVerificationGainConstant.UUID_DATA_VERIFY_UNCOMPLETE);
      } else if (errorCount > 0) {// KPI_VALIDATE_DETAIL表中uuid对应数据 ， interface_status == 2;
        resultMap = KpiVerifyResultServiceImpl.buildResultMap(uuid, content, SciVerificationGainConstant.GAIN_HAS_ERROR,
            SciVerificationGainConstant.UUID_DATA_VERFIY_ERROR);
      }
    }
    return resultMap;
  }

  /**
   * 构建验证结果数据 content
   * 
   * @param data
   * @param uuid
   * @return
   */
  @SuppressWarnings({"unchecked", "unlikely-arg-type"})
  private Map<String, Object> buildContent(String data, KpiValidateMain kpiVdMain) {
    String uuid = kpiVdMain.getUuId();
    Map<String, Object> content = new HashMap<>();
    Map<String, Object> dataMap = new HashMap<>();
    Map<String, Object> basicInfo = new HashMap<>();
    if (data != null) {
      try {
        dataMap = JacksonUtils.jsonToMap(data);
        basicInfo = (Map<String, Object>) dataMap.get("basic_info");
      } catch (Exception e) {
        basicInfo = new HashMap<>();;
      }
    }
    content.put("basic_info", basicInfo);
    String keyType = kpiVdMain.getKeyType();
    if (StringUtils.isNotBlank(keyType) && (keyType.equals(SciVerificationGainConstant.VD_PROGRESS_REPORT)
        || keyType.equals(SciVerificationGainConstant.VD_CONCLUDING_REPORT))) {
      // 如果key_type == 2 或者 3 ， 只返回项目成果节点

    } else {
      this.addPersons(content, uuid);
      this.addOrgnazations(content, uuid);
    }
    this.addPublications(content, uuid);
    return content;
  }

  /**
   * 组装: 人员验证结果
   * 
   * @param content
   * @param uuid
   */
  private void addPersons(Map<String, Object> content, String uuid) {
    Map<String, Object> persons = new HashMap<String, Object>();
    Long psnVdCount = kpiValidateDetailDao.getVdDetailCount(uuid, SciResearchVerfyConstant.VERIFY_TYPE_PSN);// 总验证数
    List<KpiValidateDetail> kpiVdDetails =
        kpiValidateDetailDao.getParamsOuts(uuid, SciResearchVerfyConstant.VERIFY_TYPE_PSN);// 人员验证结果

    List<Object> dataList = new ArrayList<Object>();
    Integer passCount = 0;// 科研验证通过个数
    Integer errorCount = 0;// 合法性验证不通过个数
    if (kpiVdDetails != null && kpiVdDetails.size() != 0) {
      for (KpiValidateDetail kpiVdDetail : kpiVdDetails) {
        Integer status = kpiVdDetail.getStatus();
        if (status.intValue() == 1) {
          ++passCount;
        }
        if (status.intValue() == 4) {
          ++errorCount;
        }
        String paramsOut = kpiVdDetail.getParamsOut();
        Map<String, Object> map = new HashMap<String, Object>();
        if (paramsOut != null) {
          map = JacksonUtils.jsonToMap(paramsOut);
        }
        this.addPsnPubs(map, kpiVdDetail.getId(), uuid);
        dataList.add(map);
      }
    }
    persons.put("psn_count", psnVdCount.toString());
    persons.put("pass_count", passCount.toString());
    persons.put("error_count", errorCount.toString());
    persons.put("data", dataList);
    content.put("persons", persons);
  }

  /**
   * 组装： 人员成果验证结果
   * 
   * @param persons
   * @param parentId
   * @param uuid
   */
  private void addPsnPubs(Map<String, Object> personsData, Long parentId, String uuid) {
    Map<String, Object> psnPubs = new HashMap<String, Object>();
    Long psnPubVdCount =
        kpiValidateDetailDao.getVdDetailCountWithParentId(uuid, parentId, SciResearchVerfyConstant.VERIFY_TYPE_PSNPUB);// 人员成果总验证数
    List<KpiValidateDetail> kpiVdDetails =
        kpiValidateDetailDao.getParamsOutsWithParentId(uuid, parentId, SciResearchVerfyConstant.VERIFY_TYPE_PSNPUB);// 人员成果验证结果

    List<Object> dataList = new ArrayList<Object>();
    Integer passCount = 0;
    Integer errorCount = 0;
    if (kpiVdDetails != null && kpiVdDetails.size() != 0) {
      for (KpiValidateDetail kpiVdDetail : kpiVdDetails) {
        Integer status = kpiVdDetail.getStatus();
        if (status.intValue() == 1) {
          ++passCount;
        }
        if (status.intValue() == 4) {
          ++errorCount;
        }
        String paramsOut = kpiVdDetail.getParamsOut();
        Map<String, Object> map = new HashMap<String, Object>();
        if (paramsOut != null) {
          map = JacksonUtils.jsonToMap(paramsOut);
        }
        dataList.add(map);
      }
    }
    if (dataList.isEmpty()) {
      psnPubs.put("data", "");
    } else {
      psnPubs.put("pub_count", psnPubVdCount.toString());
      psnPubs.put("pass_count", passCount.toString());
      psnPubs.put("error_count", errorCount.toString());
      psnPubs.put("data", dataList);
    }

    personsData.put("psn_pubs", psnPubs);
  }

  /**
   * 组装: 单位验证结果
   * 
   * @param content
   * @param uuid
   */
  private void addOrgnazations(Map<String, Object> content, String uuid) {
    Map<String, Object> orgnazations = new HashMap<String, Object>();
    Long insVdCount = kpiValidateDetailDao.getVdDetailCount(uuid, SciResearchVerfyConstant.VERIFY_TYPE_ORG);// 总验证数
    List<KpiValidateDetail> kpiVdDetails =
        kpiValidateDetailDao.getParamsOuts(uuid, SciResearchVerfyConstant.VERIFY_TYPE_ORG);// 单位验证结果

    List<Object> dataList = new ArrayList<Object>();
    Integer passCount = 0;
    Integer errorCount = 0;
    if (kpiVdDetails != null && kpiVdDetails.size() != 0) {
      for (KpiValidateDetail kpiVdDetail : kpiVdDetails) {
        Integer status = kpiVdDetail.getStatus();
        if (status.intValue() == 1) {
          ++passCount;
        }
        if (status.intValue() == 4) {
          ++errorCount;
        }
        String paramsOut = kpiVdDetail.getParamsOut();
        Map<String, Object> map = new HashMap<String, Object>();
        if (paramsOut != null) {
          map = JacksonUtils.jsonToMap(paramsOut);
        }
        dataList.add(map);
      }
    }
    orgnazations.put("org_count", insVdCount.toString());
    orgnazations.put("pass_count", passCount.toString());
    orgnazations.put("error_count", errorCount.toString());
    orgnazations.put("data", dataList);
    content.put("orgnazations", orgnazations);
  }


  /**
   * 组装： 项目成果验证结果
   * 
   * @param content
   * @param uuid
   */
  private void addPublications(Map<String, Object> content, String uuid) {
    Map<String, Object> publications = new HashMap<String, Object>();
    Long pubVdCount = kpiValidateDetailDao.getVdDetailCount(uuid, SciResearchVerfyConstant.VERIFY_TYPE_PUB);
    List<KpiValidateDetail> kpiVdDetails =
        kpiValidateDetailDao.getParamsOuts(uuid, SciResearchVerfyConstant.VERIFY_TYPE_PUB);// 项目成果验证结果

    List<Object> dataList = new ArrayList<Object>();
    Integer passCount = 0;
    Integer errorCount = 0;
    if (kpiVdDetails != null && kpiVdDetails.size() != 0) {
      for (KpiValidateDetail kpiVdDetail : kpiVdDetails) {
        Integer status = kpiVdDetail.getStatus();
        if (status.intValue() == 1) {
          ++passCount;
        }
        if (status.intValue() == 4) {
          ++errorCount;
        }
        String paramsOut = kpiVdDetail.getParamsOut();
        Map<String, Object> map = JacksonUtils.jsonToMap(paramsOut);
        dataList.add(map);
      }
    }
    publications.put("pub_count", pubVdCount.toString());
    publications.put("pass_count", passCount.toString());
    publications.put("error_count", errorCount.toString());
    publications.put("data", dataList);
    content.put("publications", publications);
  }



  /**
   * 请求参数校验
   */
  @SuppressWarnings("rawtypes")
  @Override
  public Map doDataValidate(Map<String, Object> paramet) {
    Map resultMap = null;
    Map<String, Object> content = new HashMap<String, Object>();
    if (paramet.get("uuid") == null) {
      resultMap = KpiVerifyResultServiceImpl.buildResultMap("", content, SciVerificationGainConstant.PARAME_ERROR,
          SciVerificationGainConstant.UUID_EMPTY);
      return resultMap;
    }
    KpiValidateMain kpiVdMain = kpiValidateMaindao.get(paramet.get("uuid").toString());
    if (kpiVdMain == null) {
      resultMap = KpiVerifyResultServiceImpl.buildResultMap(paramet.get("uuid").toString(), content,
          SciVerificationGainConstant.PARAME_ERROR, SciVerificationGainConstant.UUID_EORROR);
      return resultMap;
    }
    return resultMap;
  }


  /**
   * 封装result 字段数据
   * 
   * @param uuid
   * @param content
   * @param itemStatus
   * @param itemMsg
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private static Map buildResultMap(String uuid, Object content, Integer itemStatus, String itemMsg) {
    Map resultMap = new HashMap();

    resultMap.put("uuid", uuid);
    resultMap.put("content", content);
    Long endTime = new Date().getTime();
    resultMap.put("endTime", endTime.toString());
    resultMap.put("itemStatus", itemStatus);
    resultMap.put("itemMsg", itemMsg);

    return resultMap;
  }
}
