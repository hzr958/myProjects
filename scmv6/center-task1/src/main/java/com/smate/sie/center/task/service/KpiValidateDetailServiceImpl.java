package com.smate.sie.center.task.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.irissz.codec.utils.encrypt.AESUtil;
import com.irissz.codec.utils.encrypt.SHA256Util;
import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.sie.center.task.service.codec.TaskCodecService;
import com.smate.sie.core.base.utils.consts.validate.SieKpiVdConstant;
import com.smate.sie.core.base.utils.dao.validate.KpiValidateDetailDao;
import com.smate.sie.core.base.utils.dao.validate.KpiValidateLogDao;
import com.smate.sie.core.base.utils.model.validate.KpiValidateDetail;
import com.smate.sie.core.base.utils.model.validate.KpiValidateLog;

/**
 * 
 * @author ztg
 *
 */
@Service("kpiValidateDetailService")
@Transactional(rollbackOn = Exception.class)
public class KpiValidateDetailServiceImpl implements KpiValidateDetailService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private TaskCodecService taskCodecServiceImpl;
  @Autowired
  private KpiValidateDetailDao kpiValidateDetailDao;
  @Autowired
  private KpiValidateLogDao kpiValidateLogDao;

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  @Override
  public List<KpiValidateDetail> getByUUID(String uuId) {
    return kpiValidateDetailDao.getByUUID(uuId);
  }

  /**
   * 遍历，根据验证类型条用接口进行验证
   */
  @Override
  public void doValidate(KpiValidateDetail kpiVdDetail) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    @SuppressWarnings("unused")
    Integer type = kpiVdDetail.getType();
    try {
      this.checkInterfaceParamsIn(kpiVdDetail);
      int i = 0;
      while (i < 10) {// 10次失败后取消校验
        resultMap = this.doPostForObject(kpiVdDetail);
        if ("success".equals(resultMap.get("status"))) {
          break;
        } else {
          i = i + 1;
        }
      }
      try {
        String resultMapStr = JacksonUtils.mapToJsonStr(resultMap);// 原始接口返回数据直接转成String存log表

        this.recombineResult(kpiVdDetail, resultMap);
        kpiValidateDetailDao.saveOrUpdate(kpiVdDetail);

        // 保存接口调用日志
        KpiValidateLog kpiVdLog =
            new KpiValidateLog(kpiVdDetail.getId(), new Date(), resultMap.get("msg").toString(), resultMapStr);
        kpiValidateLogDao.save(kpiVdLog);
      } catch (Exception e) {
        kpiVdDetail.setInterfaceStatus(SieKpiVdConstant.REQ_ERROR);// 调用异常或者失败
        kpiValidateDetailDao.saveOrUpdate(kpiVdDetail);
        logger.error("处理调用{}验证接口返回数据出错, KPI_VALIDATE_DETAIL.ID = {} ",
            SieKpiVdConstant.INTERFACE_TYPE_MAP.get(kpiVdDetail.getType()), kpiVdDetail.getId(), e);
        // 保存接口调用日志
        String msg = "";
        String resultMapStr = "";
        if (resultMap != null && !resultMap.isEmpty()) {
          msg = resultMap.get("msg") == null ? "" : resultMap.get("msg").toString();
          resultMapStr = JacksonUtils.mapToJsonStr(resultMap);
        }
        KpiValidateLog kpiVdLog = new KpiValidateLog(kpiVdDetail.getId(), new Date(), msg, resultMapStr);
        kpiValidateLogDao.save(kpiVdLog);
      }
    } catch (Exception e) {
      kpiVdDetail.setInterfaceStatus(SieKpiVdConstant.REQ_ERROR);// 调用异常或者失败
      kpiValidateDetailDao.saveOrUpdate(kpiVdDetail);
      // 保存接口调用日志
      String detailLog = e.getMessage();
      KpiValidateLog kpiVdLog = new KpiValidateLog(kpiVdDetail.getId(), new Date(), "", "", detailLog);
      kpiValidateLogDao.save(kpiVdLog);
      logger.error("调用{}验证接口出错 , KPI_VALIDATE_DETAIL.ID = {} ",
          SieKpiVdConstant.INTERFACE_TYPE_MAP.get(kpiVdDetail.getType()), kpiVdDetail.getId(), e);
      // throw new ServiceException(e);
    }
  }

  /**
   * 校验paramsIn 值 是不是json格式串， 同时校验"data", "openId", "token" 字段值是否正常
   * 
   * @param kpiVdDetail
   */
  public void checkInterfaceParamsIn(KpiValidateDetail kpiVdDetail) {
    String expStr1 = "SieKpiValidateTask任务调用" + SieKpiVdConstant.INTERFACE_TYPE_MAP.get(kpiVdDetail.getType()) + " : ";
    String expStr2 = "";
    if (!JacksonUtils.isJsonString(kpiVdDetail.getParamsIn())) {
      expStr2 = "请求参数字符串不是json格式的字符串, KPI_VALIDATE_DETAIL.ID = " + kpiVdDetail.getId();
      throw new ServiceException(expStr1 + expStr2);
    }
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      map = JacksonUtils.jsonToMap2(kpiVdDetail.getParamsIn());
    } catch (Exception e) {
      expStr2 = "请求参数字符串转成Map类型出错, KPI_VALIDATE_DETAIL.ID = " + kpiVdDetail.getId();
      throw new ServiceException(expStr1 + expStr2, e);
    }
    if (!map.containsKey(SieKpiVdConstant.MAP_DATA)) {
      expStr2 = "请求参数字符串中data为 空, KPI_VALIDATE_DETAIL.ID = " + kpiVdDetail.getId();
      throw new ServiceException(expStr1 + expStr2);
    }
  }



  /**
   * 已经调用接口, 处理接口返回数据 同时保存每条数据的验证状况
   * 
   * @param kpiVdDetail
   * @param resultMap
   */
  @SuppressWarnings("unused")
  private void recombineResult(KpiValidateDetail kpiVdDetail, Map<String, Object> resultMap) {
    switch (kpiVdDetail.getType().intValue()) {
      case SieKpiVdConstant.VERIFY_TYPE_PSN:
        this.recombinePsnValidaResult(kpiVdDetail, resultMap);
        break;
      case SieKpiVdConstant.VERIFY_TYPE_ORG:
        this.recombineOrgValidaResult(kpiVdDetail, resultMap);
        break;
      case SieKpiVdConstant.VERIFY_TYPE_PUB:
        this.recombinePubValidaResult(kpiVdDetail, resultMap);
        break;
      case SieKpiVdConstant.VERIFY_TYPE_PSNPUB:
        this.recombinePsnPubValidaResult(kpiVdDetail, resultMap);
        break;
    }
  }


  /**
   * 处理人员验证结果 保存status， paramsOut两个字段值
   * 
   * @param kpiVdDetail
   * @param resultMap
   */
  @SuppressWarnings("unchecked")
  private void recombinePsnValidaResult(KpiValidateDetail kpiVdDetail, Map<String, Object> resultMap) {
    try {

      String status = (String) resultMap.get(SieKpiVdConstant.RESULT_STATUS);
      if (SieKpiVdConstant.RESULT_STATUS＿SUCCESS.equals(status)) {
        kpiVdDetail.setInterfaceStatus(SieKpiVdConstant.REQ_SUCCESS);

        Map<String, Object> resultMap2 = new HashMap<String, Object>();
        resultMap2 = JacksonUtils.jsonToMap(JacksonUtils.mapToJsonStr(resultMap));// resultMap2 存储替换响应码后的 返回结果

        List<Object> resultList = (List<Object>) resultMap2.get(SieKpiVdConstant.RESULT_RESULT);
        if (resultList == null || resultList.isEmpty()) {
          return;
        }
        List<Map<String, Object>> result = (List<Map<String, Object>>) resultList.get(0);// 本任务中每次请求接口只是验证一个人， 所以取一个数据
        if (result == null || result.isEmpty()) {
          return;
        }

        Map<String, Object> paramsinMap = JacksonUtils.json2HashMap(kpiVdDetail.getParamsIn());
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) paramsinMap.get(SieKpiVdConstant.MAP_DATA);
        Map<String, Object> psnMap = dataList.get(0);

        Map<String, Object> paramsoutMap = new HashMap<String, Object>();// 对应kpi_validate_detail.params_out 字段
        paramsoutMap.put("psn_name", psnMap.get("name"));
        paramsoutMap.put("org_name", psnMap.get("insName"));
        paramsoutMap.put("w_email", psnMap.get("email"));
        paramsoutMap.put("w_mobile", psnMap.get("phoneNo"));


        String nameEmailStatus = ""; // validate_email
        String nameMobileStatus = "";// validate_mobile
        String nameCertStatus = "";// validate_cert
        String nameUrlStatus = "";// validate_url
        String psnPubs = "";// psn_pubs
        Integer passCount = 0;
        Integer uncertainCount = 0;
        Integer doubtCount = 0;
        Integer detailStatus = 0;

        String emailErrorMsg = "";// error_msg_email
        String mobileErrorMsg = "";// error_msg_mobile
        String certErrorMsg = "";// error_msg_cert
        String urlErrorMsg = "";// error_msg_url

        List<String> correlationEmail = new ArrayList<String>(); // correlation_email
        List<String> correlationMobile = new ArrayList<String>(); // correlation_mobile
        List<String> correlationCert = new ArrayList<String>(); // correlation_cert
        List<String> correlationUrl = new ArrayList<String>(); // correlation_url
        for (Map<String, Object> map : result) {
          Integer itemStatus = (Integer) map.get(SieKpiVdConstant.RESULT_ITEMSTATUS);// 验证结果
          String resultType = (String) map.get(SieKpiVdConstant.RESULT_TYPE);

          if (resultType == null) {// 4. 合法性校验不通过
            if (itemStatus == SieKpiVdConstant.REUSLT_VALIDATE_UNCERTAIN) {// itemStatus : 3 不确定
              String itemMsg = (String) map.get(SieKpiVdConstant.RESULT_ITEMMSG);
              Map<String, String> codeAndMsgMap = this.replaceRespCode(itemMsg);
              if (codeAndMsgMap != null && !codeAndMsgMap.isEmpty()) {
                String errorMsg = codeAndMsgMap.get("errorMsg") == null ? "" : codeAndMsgMap.get("errorMsg");
                switch (codeAndMsgMap.get("code")) {
                  case "irisv-101":
                    mobileErrorMsg = errorMsg;
                    nameMobileStatus = "4";
                    nameEmailStatus = "4";
                    break;

                  case "irisv-102":
                    nameMobileStatus = "4";
                    nameEmailStatus = "4";
                    break;
                  case "irisv-103":
                    emailErrorMsg = errorMsg;
                    nameMobileStatus = "4";
                    nameEmailStatus = "4";
                    break;
                  default:
                    break;
                }
                map.put(SieKpiVdConstant.RESULT_ITEMMSG, errorMsg);
              }
              detailStatus = SieKpiVdConstant.REQ_PARAM_ILLEGAL;
            }
          } else if (resultType != null) { // itemStatus: 1.通过; 2.存疑 ; 3. 不确定
            Integer type = Integer.valueOf(resultType);
            String paramStatus = itemStatus.toString();


            if (itemStatus == SieKpiVdConstant.RESULT_VALIDATE_DOUBT) {// 存疑的时候，取各种correlationData
              List<String> crlDataList = (List<String>) map.get(SieKpiVdConstant.RESULT_CORRELATION_DATA);

              if (crlDataList != null && !crlDataList.isEmpty()) { // 有值才处理
                if (type.intValue() == SieKpiVdConstant.NAME_MOBILE_TYPE) {
                  correlationMobile = crlDataList;
                }
                if (type.intValue() == SieKpiVdConstant.NAME_EMAIL_TYPE) {
                  correlationEmail = crlDataList;
                }
                if (type.intValue() == SieKpiVdConstant.NAME_CERT_TYPE) {
                  correlationCert = crlDataList;
                }
              }
            }

            if (type.intValue() == SieKpiVdConstant.NAME_MOBILE_TYPE) {
              nameMobileStatus = paramStatus;
            }
            if (type.intValue() == SieKpiVdConstant.NAME_EMAIL_TYPE) {
              nameEmailStatus = paramStatus;
            }
            if (type.intValue() == SieKpiVdConstant.NAME_CERT_TYPE) {
              nameCertStatus = paramStatus;
            }
            switch (itemStatus.intValue()) {
              case SieKpiVdConstant.RESULT_VALIDATE_PASS:
                ++passCount;
                break;
              case SieKpiVdConstant.RESULT_VALIDATE_DOUBT:
                ++doubtCount;
                break;
              case SieKpiVdConstant.REUSLT_VALIDATE_UNCERTAIN:
                ++uncertainCount;
                break;
              default:
                break;
            }
          }
        }

        kpiVdDetail.setInterfaceResult(JacksonUtils.mapToJsonStr(resultMap2));

        paramsoutMap.put("validate_email", nameEmailStatus);
        paramsoutMap.put("validate_mobile", nameMobileStatus);
        paramsoutMap.put("validate_cert", nameCertStatus);
        paramsoutMap.put("validate_url", nameUrlStatus);
        paramsoutMap.put("error_msg_email", emailErrorMsg);
        paramsoutMap.put("error_msg_mobile", mobileErrorMsg);
        paramsoutMap.put("error_msg_cert", certErrorMsg);
        paramsoutMap.put("error_msg_url", urlErrorMsg);

        paramsoutMap.put("correlation_email", correlationEmail);
        paramsoutMap.put("correlation_mobile", correlationMobile);
        paramsoutMap.put("correlation_cert", correlationCert);
        paramsoutMap.put("correlation_url", correlationUrl);

        /* paramsoutMap.put("psn_name_error_msg", psnNameErrorMsg); */
        paramsoutMap.put("psn_pubs", psnPubs);
        kpiVdDetail.setParamsOut(JacksonUtils.mapToJsonStr(paramsoutMap));
        // 接口验证情况
        if (detailStatus.intValue() == 0) {
          if (uncertainCount > 0) {// 有一个不通过
            kpiVdDetail.setStatus(SieKpiVdConstant.ALL_VALIDATE_EXIT_UNCERTAIN);
          } else if (uncertainCount == 0 && doubtCount > 0) {// 没有不通过，但是有一个存疑
            kpiVdDetail.setStatus(SieKpiVdConstant.ALL_VALIDATE_EXIT_DOUBT);
          } else if (uncertainCount == 0 && doubtCount == 0 && passCount > 0) {// 既没有不通过，也没有存疑
            kpiVdDetail.setStatus(SieKpiVdConstant.ALL_VALIDATE_PASS);
          }
        } else {
          kpiVdDetail.setStatus(detailStatus); // 请求参数合法性校验不通过
        }
      } else if (SieKpiVdConstant.RESULT_STATUS＿ERROR.equals(status)) {
        kpiVdDetail.setInterfaceStatus(SieKpiVdConstant.REQ_ERROR);// 调用失败
        kpiVdDetail.setInterfaceResult(JacksonUtils.mapToJsonStr(resultMap));// 存储返回结果
      }
    } catch (Exception e) {
      logger.error("调用人员验证接口，处理接口返回数据， 存在异常");
      throw new ServiceException("调用人员验证接口，处理接口返回数据， 存在异常", e);
    }
  }

  /**
   * 替换个人接口，单位接口返回数据result 的itemMsg 的 响应码
   * 
   * @param itemMsg
   * @return
   */
  private Map<String, String> replaceRespCode(String itemMsg) {
    Map<String, String> codeMap = SieKpiVdConstant.getResponseCodeMap();
    Map<String, String> codeAndMsg = new HashMap<String, String>();
    for (Entry<String, String> entry : codeMap.entrySet()) {
      if (itemMsg != null) {
        if (itemMsg.indexOf(entry.getKey()) != -1) {
          itemMsg = itemMsg.replace(entry.getKey(), entry.getValue());
          codeAndMsg.put("code", entry.getKey());// 保存接口返回数据的响应码
          codeAndMsg.put("errorMsg", itemMsg);// 保存接口返回数据中itemMsg 替换响应码后的新串
        }
      }
    }
    return codeAndMsg;
  }

  /**
   * 单位验证 保存status， paramsOut两个字段值
   * 
   * @param kpiVdDetail
   * @param resultMap
   */
  @SuppressWarnings("unchecked")
  private void recombineOrgValidaResult(KpiValidateDetail kpiVdDetail, Map<String, Object> resultMap) {
    try {

      String status = (String) resultMap.get(SieKpiVdConstant.RESULT_STATUS);
      if (SieKpiVdConstant.RESULT_STATUS＿SUCCESS.equals(status)) {
        kpiVdDetail.setInterfaceStatus(SieKpiVdConstant.REQ_SUCCESS);// 调用成功
        Map<String, Object> resultMap2 = new HashMap<String, Object>();
        resultMap2 = JacksonUtils.jsonToMap(JacksonUtils.mapToJsonStr(resultMap));// 存储替换响应码后的结果数据
        Map<String, Object> paramsoutMap = new HashMap<String, Object>();// paramOut字段值

        Map<String, Object> paramsinMap = JacksonUtils.json2HashMap(kpiVdDetail.getParamsIn());
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) paramsinMap.get(SieKpiVdConstant.MAP_DATA);
        Map<String, Object> orgMap = dataList.get(0);
        paramsoutMap.put("org_name", orgMap.get("name"));
        paramsoutMap.put("w_uniform", orgMap.get("orgNo"));


        String uniformMsg = ""; // error_msg_uniform

        String itemStatusStr = "";
        Integer paramStatus = 0;
        List<Object> correlationUniform = new ArrayList<Object>(); // correlation_uniform
        List<Map<String, Object>> resultList =
            (List<Map<String, Object>>) resultMap2.get(SieKpiVdConstant.RESULT_RESULT);
        if (resultList == null || resultList.isEmpty()) {
          return;
        }
        Map<String, Object> result = resultList.get(0);
        Integer itemStatus = (Integer) result.get(SieKpiVdConstant.RESULT_ITEMSTATUS);
        itemStatusStr = itemStatus.toString();

        if (itemStatus == SieKpiVdConstant.REUSLT_VALIDATE_UNCERTAIN) {
          String itemMsg = (String) result.get(SieKpiVdConstant.RESULT_ITEMMSG);
          if (itemMsg != null && itemMsg.equals("不确定")) {// 相当于3. 不确定
            paramStatus = SieKpiVdConstant.ALL_VALIDATE_EXIT_UNCERTAIN; // TODO
            itemStatusStr = "3";
          } else {
            Map<String, String> codeAndMsgMap = this.replaceRespCode(itemMsg);// 认证结果不通过信息
            if (codeAndMsgMap != null && !codeAndMsgMap.isEmpty()) {
              String errorMsg = codeAndMsgMap.get("errorMsg") == null ? "" : codeAndMsgMap.get("errorMsg");
              switch (codeAndMsgMap.get("code")) {
                /*
                 * case "irisv-201" : nameMsg = errorMsg; break;
                 */
                case "irisv-202":
                  uniformMsg = errorMsg;
                  break;
                default:
                  break;
              }
              itemStatusStr = "4";
              result.put(SieKpiVdConstant.RESULT_ITEMMSG, errorMsg);
            }
            paramStatus = SieKpiVdConstant.REQ_PARAM_ILLEGAL;
          }
        } else {
          if (itemStatus == SieKpiVdConstant.RESULT_VALIDATE_PASS) {
            paramStatus = SieKpiVdConstant.ALL_VALIDATE_PASS;
          } else if (itemStatus == SieKpiVdConstant.RESULT_VALIDATE_DOUBT) {
            paramStatus = SieKpiVdConstant.ALL_VALIDATE_EXIT_DOUBT;
            // 存疑情况下，取correlationData
            List<Object> crlDataList = (List<Object>) result.get(SieKpiVdConstant.RESULT_CORRELATION_DATA);
            if (crlDataList != null && !crlDataList.isEmpty()) {
              correlationUniform = crlDataList; //
            }
          }
        }
        kpiVdDetail.setInterfaceResult(JacksonUtils.mapToJsonStr(resultMap2));
        paramsoutMap.put("validate_uniform", itemStatusStr);// 1通过 ; 2存疑， 3 不确定， 4 合法性校验不通过
        paramsoutMap.put("error_msg_uniform", uniformMsg);

        paramsoutMap.put("correlation_uniform", correlationUniform);
        /* paramsoutMap.put("org_name_msg", nameMsg); */
        kpiVdDetail.setParamsOut(JacksonUtils.mapToJsonStr(paramsoutMap));
        kpiVdDetail.setStatus(paramStatus);
      } else if (SieKpiVdConstant.RESULT_STATUS＿ERROR.equals(status)) {
        kpiVdDetail.setInterfaceStatus(SieKpiVdConstant.REQ_ERROR);// 调用失败
        kpiVdDetail.setInterfaceResult(JacksonUtils.mapToJsonStr(resultMap));// 存储返回结果
      }
    } catch (Exception e) {
      logger.error("调用单位验证接口，处理接口返回数据， 存在异常");
      throw new ServiceException("调用单位验证接口，处理接口返回数据， 存在异常", e);
    }
  }

  /**
   * 项目成果验证
   * 
   * @param kpiVdDetail
   * @param resultMap
   */
  @SuppressWarnings("unchecked")
  private void recombinePubValidaResult(KpiValidateDetail kpiVdDetail, Map<String, Object> resultMap) {
    try {

      String status = (String) resultMap.get(SieKpiVdConstant.RESULT_STATUS);
      if (SieKpiVdConstant.RESULT_STATUS＿SUCCESS.equals(status)) {
        kpiVdDetail.setInterfaceStatus(SieKpiVdConstant.REQ_SUCCESS);// 调用成功
        Map<String, Object> paramsoutMap = new HashMap<String, Object>();

        Map<String, Object> paramsinMap = JacksonUtils.json2HashMap(kpiVdDetail.getParamsIn());
        Map<String, Object> dataMap = JacksonUtils.jsonToMap(paramsinMap.get(SieKpiVdConstant.MAP_DATA).toString());
        List<Map<String, Object>> pubInfoList =
            (List<Map<String, Object>>) dataMap.get(SieKpiVdConstant.MAP_DATA_PUBINFOLIST);
        Map<String, Object> prjPubMap = pubInfoList.get(0);
        paramsoutMap.put("zh_title", prjPubMap.get("title"));
        paramsoutMap.put("w_doi", prjPubMap.get("doi"));
        paramsoutMap.put("w_jname", prjPubMap.get("journalName"));
        paramsoutMap.put("w_author", prjPubMap.get("authorNames"));
        paramsoutMap.put("w_pubyear", prjPubMap.get("publishYear"));
        paramsoutMap.put("w_fundinfo", prjPubMap.get("fundingInfo") == null ? "" : prjPubMap.get("fundingInfo"));

        String vdTitle = "";// validate_title
        String vdDoi = "";// validate_doi
        String vdJName = "";// validate_jname
        String vdAtuhor = "";// validate_author
        String vdPubyear = "";// validate_pubyear
        String vdFuninfo = "";// validate_fundinfo
        String titleErrorMsg = "";// error_msg_title
        String doiErrorMsg = "";// error_msg_doi
        String jnameErrorMsg = "";// error_msg_jname
        String authorErrorMsg = "";// error_msg_author
        String pubyearErrorMsg = "";// error_msg_pubyear
        String fundinfoErrorMsg = "";// error_msg_fundinfo


        Map<String, Object> relatePub = new HashMap<String, Object>();// relate_pub


        paramsoutMap.put("error_msg_title", titleErrorMsg);
        paramsoutMap.put("error_msg_doi", doiErrorMsg);
        paramsoutMap.put("error_msg_jname", jnameErrorMsg);
        paramsoutMap.put("error_msg_author", authorErrorMsg);
        paramsoutMap.put("error_msg_pubyear", pubyearErrorMsg);
        paramsoutMap.put("error_msg_fundinfo", fundinfoErrorMsg);


        List<Map<String, Object>> resultList =
            (List<Map<String, Object>>) resultMap.get(SieKpiVdConstant.RESULT_RESULT);
        if (resultList == null || resultList.isEmpty()) {
          return;
        }
        Map<String, Object> result = resultList.get(0);
        Integer itemStatus = (Integer) result.get(SieKpiVdConstant.RESULT_ITEMSTATUS);// 项目成果返回结果result中的itemStatus

        // 项目成果验证接口: 目前返回结果中的itemStatus 有三种值， 1， 2， 3， 分别对应这 kpi_validate_detail.status 的 1， 2， 3
        Map<String, Object> itemMsgMap = (Map<String, Object>) result.get(SieKpiVdConstant.RESULT_ITEMMSG);
        if (itemMsgMap != null && !itemMsgMap.isEmpty()) {
          titleErrorMsg = itemMsgMap.get("title") == null ? "" : (String) itemMsgMap.get("title");
          doiErrorMsg = itemMsgMap.get("doi") == null ? "" : (String) itemMsgMap.get("doi");
          jnameErrorMsg = itemMsgMap.get("journalName") == null ? "" : (String) itemMsgMap.get("journalName");
          authorErrorMsg = itemMsgMap.get("authorNames") == null ? "" : (String) itemMsgMap.get("authorNames");
          pubyearErrorMsg = itemMsgMap.get("publishYear") == null ? "" : (String) itemMsgMap.get("publishYear");
          fundinfoErrorMsg = itemMsgMap.get("fundingInfo") == null ? "" : (String) itemMsgMap.get("fundingInfo");
        }

        Map<String, Object> typeMap = (Map<String, Object>) result.get("type");
        if (typeMap != null && !typeMap.isEmpty()) {
          vdTitle = typeMap.get("title") == null ? "" : (String) typeMap.get("title");
          if (!vdTitle.equals("") && checkExistForPubVd(titleErrorMsg, "title")) {
            vdTitle = Integer.toString(SieKpiVdConstant.REQ_PARAM_ILLEGAL);
            paramsoutMap.put("error_msg_title", titleErrorMsg);
          }


          vdDoi = typeMap.get("doi") == null ? "" : (String) typeMap.get("doi");
          if (!vdDoi.equals("") && checkExistForPubVd(doiErrorMsg, "doi")) {
            vdDoi = Integer.toString(SieKpiVdConstant.REQ_PARAM_ILLEGAL);
            paramsoutMap.put("error_msg_doi", doiErrorMsg);
          }


          vdJName = typeMap.get("journalName") == null ? "" : (String) typeMap.get("journalName");
          if (!vdJName.equals("") && checkExistForPubVd(jnameErrorMsg, "journalName")) {
            vdJName = Integer.toString(SieKpiVdConstant.REQ_PARAM_ILLEGAL);
            paramsoutMap.put("error_msg_jname", jnameErrorMsg);
          }


          vdAtuhor = typeMap.get("authorNames") == null ? "" : (String) typeMap.get("authorNames");
          if (!vdAtuhor.equals("") && checkExistForPubVd(authorErrorMsg, "authorNames")) {
            vdAtuhor = Integer.toString(SieKpiVdConstant.REQ_PARAM_ILLEGAL);
            paramsoutMap.put("error_msg_author", authorErrorMsg);
          }


          vdPubyear = typeMap.get("publishYear") == null ? "" : (String) typeMap.get("publishYear");
          if (!vdPubyear.equals("") && checkExistForPubVd(pubyearErrorMsg, "publishYear")) {
            vdPubyear = Integer.toString(SieKpiVdConstant.REQ_PARAM_ILLEGAL);
            paramsoutMap.put("error_msg_pubyear", pubyearErrorMsg);
          }


          vdFuninfo = typeMap.get("fundingInfo") == null ? "" : (String) typeMap.get("fundingInfo");
          if (!vdFuninfo.equals("") && checkExistForPubVd(fundinfoErrorMsg, "fundingInfo")) {
            vdFuninfo = Integer.toString(SieKpiVdConstant.REQ_PARAM_ILLEGAL);
            paramsoutMap.put("error_msg_fundinfo", fundinfoErrorMsg);
          }
        }
        Map<String, Object> correlationData = (Map<String, Object>) result.get("correlationData");
        if (correlationData != null && !correlationData.isEmpty()) {
          relatePub = correlationData;
        } else {
          // 保持""
        }

        paramsoutMap.put("validate_title", vdTitle);
        paramsoutMap.put("validate_doi", vdDoi);
        paramsoutMap.put("validate_jname", vdJName);
        paramsoutMap.put("validate_author", vdAtuhor);
        paramsoutMap.put("validate_pubyear", vdPubyear);
        paramsoutMap.put("validate_fundinfo", vdFuninfo);
        paramsoutMap.put("correlation_pub", relatePub);
        kpiVdDetail.setInterfaceResult(JacksonUtils.mapToJsonStr(resultMap));
        kpiVdDetail.setParamsOut(JacksonUtils.mapToJsonStr(paramsoutMap));
        kpiVdDetail.setStatus(itemStatus);
      } else if (SieKpiVdConstant.RESULT_STATUS＿ERROR.equals(status)) {
        kpiVdDetail.setInterfaceStatus(SieKpiVdConstant.REQ_ERROR);// 调用失败
        kpiVdDetail.setInterfaceResult(JacksonUtils.mapToJsonStr(resultMap));// 存储返回结果
      }
    } catch (Exception e) {
      logger.error("调用项目成果验证接口，处理接口返回数据， 存在异常");
      throw new ServiceException("调用项目成果验证接口，处理接口返回数据， 存在异常", e);
    }
  }

  /**
   * 查看项目成果验证结果中否有相应字段的合法性校验信息
   * 
   * @param errorMsg
   * @param reqParam
   * @return
   */
  private boolean checkExistForPubVd(String errorMsg, String reqParam) {
    Map<String, String> map = SieKpiVdConstant.getPubResponseCodeMap();
    boolean exist = false;
    for (Entry<String, String> entry : map.entrySet()) {
      if (errorMsg.indexOf(entry.getKey()) != -1 && entry.getValue().equals(reqParam)) {
        exist = true;
        break;
      }
    }
    return exist;
  }

  /**
   * 查看人员成果验证结果中否有相应字段的合法性校验信息
   * 
   * @param errorMsg
   * @param reqParam
   * @return
   */
  private boolean checkExistForPsnPubVd(String errorMsg, String reqParam) {
    Map<String, String> map = SieKpiVdConstant.getPsnPubResponseCodeMap();
    boolean exist = false;
    for (Entry<String, String> entry : map.entrySet()) {
      if (errorMsg.indexOf(entry.getKey()) != -1 && entry.getValue().equals(reqParam)) {
        exist = true;
        break;
      }
    }
    return exist;
  }

  /**
   * 个人成果验证
   * 
   * @param kpiVdDetail
   * @param resultMap
   */
  @SuppressWarnings("unchecked")
  private void recombinePsnPubValidaResult(KpiValidateDetail kpiVdDetail, Map<String, Object> resultMap) {
    try {

      String status = (String) resultMap.get(SieKpiVdConstant.RESULT_STATUS);
      if (SieKpiVdConstant.RESULT_STATUS＿SUCCESS.equals(status)) {
        kpiVdDetail.setInterfaceStatus(SieKpiVdConstant.REQ_SUCCESS);
        Map<String, Object> paramsoutMap = new HashMap<String, Object>();

        Map<String, Object> paramsinMap = JacksonUtils.json2HashMap(kpiVdDetail.getParamsIn());
        Map<String, Object> dataMap = JacksonUtils.jsonToMap(paramsinMap.get(SieKpiVdConstant.MAP_DATA).toString());
        List<Map<String, Object>> pubInfoList =
            (List<Map<String, Object>>) dataMap.get(SieKpiVdConstant.MAP_DATA_PUBINFOLIST);
        Map<String, Object> psnMap = pubInfoList.get(0);
        paramsoutMap.put("zh_title", psnMap.get("title") == null ? "" : psnMap.get("title"));
        paramsoutMap.put("authors", psnMap.get("authorNames") == null ? "" : psnMap.get("authorNames"));

        String vdTitle = ""; // validate_title
        String vdAuthor = ""; // validate_author
        String titleErrorMsg = ""; // error_msg_title;
        String authorErrorMsg = ""; // error_msg_author;
        paramsoutMap.put("error_msg_title", titleErrorMsg);
        paramsoutMap.put("error_msg_author", authorErrorMsg);
        Map<String, Object> relatePub = new HashMap<String, Object>();// relate_pub

        List<Map<String, Object>> resultList =
            (List<Map<String, Object>>) resultMap.get(SieKpiVdConstant.RESULT_RESULT);
        if (resultList == null || resultList.isEmpty()) {
          return;
        }
        Map<String, Object> result = resultList.get(0);
        Integer itemStatus = (Integer) result.get("itemStatus");


        Map<String, Object> itemMsgMap = (Map<String, Object>) result.get(SieKpiVdConstant.RESULT_ITEMMSG);
        if (itemMsgMap != null && !itemMsgMap.isEmpty()) {
          titleErrorMsg = itemMsgMap.get("title") == null ? "" : (String) itemMsgMap.get("title");
          authorErrorMsg = itemMsgMap.get("authorNames") == null ? "" : (String) itemMsgMap.get("authorNames");
        }


        Map<String, Object> typeMap = (Map<String, Object>) result.get("type");
        if (typeMap != null && !typeMap.isEmpty()) {
          vdTitle = typeMap.get("title") == null ? "" : (String) typeMap.get("title");
          if (!vdTitle.equals("") && checkExistForPsnPubVd(titleErrorMsg, "title")) {
            vdTitle = Integer.toString(SieKpiVdConstant.REQ_PARAM_ILLEGAL);
            paramsoutMap.put("error_msg_title", titleErrorMsg);
          }

          vdAuthor = typeMap.get("authorNames") == null ? "" : (String) typeMap.get("authorNames");
          if (!vdAuthor.equals("") && checkExistForPsnPubVd(authorErrorMsg, "authorNames")) {
            vdAuthor = Integer.toString(SieKpiVdConstant.REQ_PARAM_ILLEGAL);
            paramsoutMap.put("error_msg_author", authorErrorMsg);
          }
        }
        Map<String, Object> correlationData = (Map<String, Object>) result.get("correlationData");
        if (correlationData != null && !correlationData.isEmpty()) {
          relatePub = correlationData;
        } else {
          // 保持{}
        }
        paramsoutMap.put("validate_title", vdTitle);
        paramsoutMap.put("validate_author", vdAuthor);
        paramsoutMap.put("correlation_pub", relatePub);
        kpiVdDetail.setInterfaceResult(JacksonUtils.mapToJsonStr(resultMap));
        kpiVdDetail.setParamsOut(JacksonUtils.mapToJsonStr(paramsoutMap));
        kpiVdDetail.setStatus(itemStatus);
      } else if (SieKpiVdConstant.RESULT_STATUS＿ERROR.equals(status)) {
        kpiVdDetail.setInterfaceStatus(SieKpiVdConstant.REQ_ERROR);// 调用失败
        kpiVdDetail.setInterfaceResult(JacksonUtils.mapToJsonStr(resultMap));// 存储返回结果
      }
    } catch (Exception e) {
      logger.error("调用个人成果验证接口，处理接口返回数据， 存在异常");
      throw new ServiceException("调用个人成果验证接口，处理接口返回数据， 存在异常", e);
    }
  }

  /**
   * 进行接口调用， 返回处理结果， Map类型
   * 
   * @param kpiVdDetail
   * @return
   */
  private Map<String, Object> doPostForObject(KpiValidateDetail kpiVdDetail) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    switch (kpiVdDetail.getType().intValue()) {
      case SieKpiVdConstant.VERIFY_TYPE_PSN:
        resultMap = doPostForObject1(kpiVdDetail);
        break;
      case SieKpiVdConstant.VERIFY_TYPE_ORG:
        resultMap = doPostForObject1(kpiVdDetail);
        break;
      case SieKpiVdConstant.VERIFY_TYPE_PUB:
        resultMap = doPostForObject2(kpiVdDetail);
        break;
      case SieKpiVdConstant.VERIFY_TYPE_PSNPUB:
        resultMap = doPostForObject2(kpiVdDetail);
        break;
    }

    return resultMap;
  }

  /**
   * 调用云计算接口 调用人员验证， 单位验证接口（3楼）
   * 
   * @param kpiVdDetail
   * @return
   */
  @SuppressWarnings("unchecked")
  private Map<String, Object> doPostForObject1(KpiValidateDetail kpiVdDetail) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    Map<String, Object> map = JacksonUtils.jsonToMap(kpiVdDetail.getParamsIn());
    this.dataEncrypt1(map, SieKpiVdConstant.COLUD_TOKEN);// 用云计算的公钥加密
    // 解决调用云计算接口，415 错误
    HttpHeaders headers = new HttpHeaders();
    MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
    headers.setContentType(type);
    headers.add("Accept", MediaType.APPLICATION_JSON.toString());
    HttpEntity<String> formEntity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(map), headers);// 调用云计算接口时， 415错误

    resultMap =
        (Map<String, Object>) restTemplate.postForObject(kpiVdDetail.getInterfaceUrl(), formEntity, Object.class);
    this.dataDecrypt1(resultMap, SieKpiVdConstant.SIE_TOKEN);// 用SIE的私钥解密
    return resultMap;
  }

  /**
   * 调用云计算接口 对请求参数中data数据进行加密
   * 
   * @param map
   * @param token
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private void dataEncrypt1(Map<String, Object> map, String token) {
    Object dataO = map.get(SieKpiVdConstant.MAP_DATA);
    try {
      List<Object> dataList = (List<Object>) dataO;
      String aeskey = AESUtil.genKeyAES();// 生成aeskey密钥
      String content = AESUtil.encrypt(JacksonUtils.listToJsonStr(dataList), AESUtil.loadKeyAES(aeskey));// 使用aes密钥加密personList数据的json格式串
      String aeskeyEncrypt = taskCodecServiceImpl.encode(token, aeskey);// 使用ECC加密aeskey
      String shakey = SHA256Util.encryptSHA256(JacksonUtils.listToJsonStr(dataList));// 使用sha256 加密personList 的json格式串

      // 填充三个值content， aeskey， shakey;
      Map<String, Object> dataMap = new HashMap<String, Object>();
      dataMap.put("content", content);
      dataMap.put("aeskey", aeskeyEncrypt);
      dataMap.put("shakey", shakey);
      map.put(SieKpiVdConstant.MAP_DATA, dataMap);
    } catch (Exception e) {
      logger.error("对请求数据加密失败");
      throw new ServiceException("对请求数据加密失败", e);
    }
  }



  /**
   * 调用云计算接口 对请求参数中data数据进行解密
   * 
   * @param resultMap
   * @param token
   */
  @SuppressWarnings("unchecked")
  private void dataDecrypt1(Map<String, Object> resultMap, String token) {
    Object dataO = resultMap.get(SieKpiVdConstant.RESULT_RESULT);
    try {
      if (dataO != null) {
        String decodeContent = "";
        Map<String, String> tempMap = (Map<String, String>) dataO;
        String content = tempMap.get("content");
        String aeskeyEncrypt = tempMap.get("aeskey");
        String shakey = tempMap.get("shakey");
        String aeskey = taskCodecServiceImpl.decode(token, aeskeyEncrypt);
        decodeContent = AESUtil.decrypt(content, AESUtil.loadKeyAES(aeskey));
        resultMap.put(SieKpiVdConstant.RESULT_RESULT, JacksonUtils.jsonToList(decodeContent));
      }
    } catch (Exception e) {
      logger.error("对请求结果解密失败");
      throw new ServiceException("对请求结果解密失败", e);
    }
  }

  /**
   * 调用个人版 成果（项目成果， 个人成果）验证接口
   * 
   * @param kpiVdDetail
   * @return
   */
  private Map<String, Object> doPostForObject2(KpiValidateDetail kpiVdDetail) {
    Map<String, Object> map = JacksonUtils.jsonToMap(kpiVdDetail.getParamsIn().toString());
    this.dataEncrypt2(map, SieKpiVdConstant.SNS_TOKEN);
    Map<String, Object> resultMap =
        JacksonUtils.jsonToMap(restTemplate.postForObject(kpiVdDetail.getInterfaceUrl(), map, Object.class).toString());// JacksonUtils.mapToJsonStr(map)
    this.dataDecrypt2(resultMap, SieKpiVdConstant.SIE_TOKEN);
    return resultMap;
  }

  /**
   * 调用个人版接口 请求前， 对请求参数中的data字段进行加密
   * 
   * @param map
   * @param token
   */
  @SuppressWarnings("unused")
  private void dataEncrypt2(Map<String, Object> map, String token) {
    Object dataO = map.get(SieKpiVdConstant.MAP_DATA);
    if (dataO == null) {
      return;
    }
    String content = taskCodecServiceImpl.encode(token, dataO.toString());
    Map<String, Object> encodeData = new HashMap<String, Object>();
    encodeData.put(SieKpiVdConstant.ENCODE_CONTENT, content);
    map.put(SieKpiVdConstant.MAP_DATA, JacksonUtils.mapToJsonStr(encodeData));
  }



  /**
   * 调用个人版接口 获取返回结果后，对结果中的result字段进行解密
   * 
   * @param resultMap
   * @param token
   * @throws IOException
   */
  @SuppressWarnings("unused")
  private void dataDecrypt2(Map<String, Object> resultMap, String token) {
    Object dataO = resultMap.get(SieKpiVdConstant.RESULT_RESULT);
    String content = "";
    if (dataO != null && !dataO.toString().equals("")) {
      String data = dataO.toString();
      Map<String, String> tempMap = new HashMap<String, String>();
      try {
        tempMap = JacksonUtils.json2Map(data);
      } catch (IOException e) {
        logger.error("参数获取失败", e);
      }
      content = tempMap.get("content");
    }
    String dataStr = "";
    if (StringUtils.isNotBlank(content)) {
      dataStr = taskCodecServiceImpl.decode(token, content);
    }
    if (StringUtils.isNotBlank(dataStr)) {
      resultMap.put(SieKpiVdConstant.RESULT_RESULT, JacksonUtils.jsonToList(dataStr));
    } else {
      resultMap.put(SieKpiVdConstant.RESULT_RESULT, new ArrayList<>());
    }
  }

  @Override
  public Long countNeedHandleKeyId(String uuId) {
    try {
      return kpiValidateDetailDao.countNeedHandleKeyId(uuId);
    } catch (Exception e) {
      logger.error("根据uuid:{}读取KPI_VALIDATE_MAIN表中需要处理的总数出错 ！", uuId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long countStatusIsNull(String uuId) {
    try {
      return kpiValidateDetailDao.countStatusIsNull(uuId);
    } catch (Exception e) {
      logger.error("根据uuid:{}读取KPI_VALIDATE_MAIN表中status为空的个数出错 ！", uuId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveKpiValidateDetail(KpiValidateDetail kpiVdDetail) {
    kpiValidateDetailDao.saveOrUpdate(kpiVdDetail);
  }

  @Override
  public void saveKpiValidateLog(KpiValidateLog kpiVdLog) {
    kpiValidateLogDao.saveOrUpdate(kpiVdLog);
  }
}


