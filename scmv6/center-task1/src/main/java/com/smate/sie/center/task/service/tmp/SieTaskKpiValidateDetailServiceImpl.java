package com.smate.sie.center.task.service.tmp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.sie.core.base.utils.consts.validate.SieKpiVdConstant;
import com.smate.sie.core.base.utils.dao.validate.tmp.SieTaskKpiValidateDetailDao;
import com.smate.sie.core.base.utils.dao.validate.tmp.SieTaskKpiVdDetailSplitOrgDao;
import com.smate.sie.core.base.utils.dao.validate.tmp.SieTaskKpiVdDetailSplitPsnDao;
import com.smate.sie.core.base.utils.dao.validate.tmp.SieTaskKpiVdDetailSplitPubDao;
import com.smate.sie.core.base.utils.model.validate.tmp.SieTaskKpiValidateDetail;
import com.smate.sie.core.base.utils.model.validate.tmp.SieTaskKpiVdDetailSplitOrg;
import com.smate.sie.core.base.utils.model.validate.tmp.SieTaskKpiVdDetailSplitPsn;
import com.smate.sie.core.base.utils.model.validate.tmp.SieTaskKpiVdDetailSplitPub;

/**
 * TASK_KPI_VALIDATE_DETAIL 表处理
 * 
 * @author ztg
 *
 */
@Service("sieTaskKpiValidateDetailService")
@Transactional(rollbackOn = Exception.class)
public class SieTaskKpiValidateDetailServiceImpl implements SieTaskKpiValidateDetailService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SieTaskKpiValidateDetailDao sieTaskKpiValidateDetailDao;
  @Autowired
  private SieTaskKpiVdDetailSplitOrgDao sieTaskKpiVdDetailSplitOrgDao;
  @Autowired
  private SieTaskKpiVdDetailSplitPsnDao sieTaskKpiVdDetailSplitPsnDao;
  @Autowired
  private SieTaskKpiVdDetailSplitPubDao sieTaskKpiVdDetailSplitPubDao;



  @Override
  public Long getAllCountByUUID(String uuId) {
    try {
      return sieTaskKpiValidateDetailDao.getAllCountByUUID(uuId);
    } catch (Exception e) {
      logger.error("根据uuid:{}读取TASK_KPI_VALIDATE_DETAIL表中对应的记录总数出错 ！", uuId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取 TASK_KPI_VALIDATE_DETAIL 中待处理记录
   */
  @Override
  public List<SieTaskKpiValidateDetail> getByUUID(String uuId) {
    return sieTaskKpiValidateDetailDao.getByUUID(uuId);
  }

  /**
   * 拆分数据 到 三个拆分表
   */
  @Override
  public void doSplit(SieTaskKpiValidateDetail kpiVdDetail) throws Exception {
    // 根据TASK_KPI_VALIDATE_DETAIL.TYPE 值 分情况 拆分: 人员 ，单位， 成果
    try {
      switch (kpiVdDetail.getType().intValue()) {
        case SieKpiVdConstant.VERIFY_TYPE_PSN:
          this.splitPsnDetail(kpiVdDetail);
          break;
        case SieKpiVdConstant.VERIFY_TYPE_ORG:
          this.splitOrgDetail(kpiVdDetail);
          break;
        case SieKpiVdConstant.VERIFY_TYPE_PUB:
          this.splitPubDetail(kpiVdDetail);
          break;
      }
    } catch (Exception e) {
      // 拆分detail表中数据有异常， 存状态：9
      kpiVdDetail.setSplitStatus(9);
      sieTaskKpiValidateDetailDao.save(kpiVdDetail);
      logger.error("拆分TASK_KPI_VALIDATE_DETAIL表数据出错失败,数据主键id:{},uuid:{}",
          new Object[] {kpiVdDetail.getId(), kpiVdDetail.getUuId(), e});
    }

  }



  /**
   * 拆分到TMP_KPI_VDDETAIL_SPLIT_PSN
   * 
   * @param kpiVdDetail
   */
  @SuppressWarnings("unchecked")
  private void splitPsnDetail(SieTaskKpiValidateDetail kpiVdDetail) throws Exception {
    SieTaskKpiVdDetailSplitPsn splitPsn = new SieTaskKpiVdDetailSplitPsn();
    try {
      splitPsn.setUuId(kpiVdDetail.getUuId());
      splitPsn.setDetailId(kpiVdDetail.getId());
      splitPsn.setStatus(kpiVdDetail.getStatus());
      splitPsn.setInterfaceResult(kpiVdDetail.getInterfaceResult());

      Map<String, Object> paramsinMap = JacksonUtils.json2HashMap(kpiVdDetail.getParamsIn());
      List<Map<String, Object>> dataList = (List<Map<String, Object>>) paramsinMap.get(SieKpiVdConstant.MAP_DATA);
      Map<String, Object> psnMap = dataList.get(0);

      /**
       * {"data":[{"certNo":"360102197108015813","keyCode":"65831216700286193","certType":"身份证",
       * "insName":"南昌大学第三附属医院（南昌市第一医院）","name":"夏剑","phoneNo":"13006219526","email":"xiajian81@163.com","url":"","wechatNo":""}],
       * "openid":"88888888","token":"11111111verfypsn"}
       */
      // 从params_in 请求参数中解析字段
      splitPsn.setCertNo((String) psnMap.get("certNo"));
      splitPsn.setCertType((String) psnMap.get("certType"));
      splitPsn.setInsName((String) psnMap.get("insName"));
      splitPsn.setName((String) psnMap.get("name"));
      splitPsn.setPhone((String) psnMap.get("phoneNo"));
      splitPsn.setEmail((String) psnMap.get("email"));

      // 从params_out 中解析字段
      /**
       * {"correlation_email":["许珠"],"validate_mobile":"3","error_msg_cert":"",
       * "validate_email":"2","correlation_url":[],"psn_name":"任泰昌","error_msg_email":"",
       * "validate_url":"","error_msg_mobile":"","error_msg_url":"","org_name":"江西新余国泰特种化工有限责任公司",
       * "correlation_cert":[],"correlation_mobile":[],"psn_pubs":"","validate_cert":""}
       */
      if (kpiVdDetail.getStatus() != null && kpiVdDetail.getParamsOut() != null) {
        Map<String, Object> paramsOutMap = JacksonUtils.json2HashMap(kpiVdDetail.getParamsOut());
        String vPhone = (String) paramsOutMap.get("validate_mobile");
        String vEmail = (String) paramsOutMap.get("validate_email");
        splitPsn.setvPhone(vPhone == "" ? null : Integer.valueOf(vPhone));
        splitPsn.setvEmail(vEmail == "" ? null : Integer.valueOf(vEmail));

        List<String> cMails = (List<String>) paramsOutMap.get("correlation_email");
        if (cMails != null && !cMails.isEmpty()) {
          if (cMails.size() > 1) {
            splitPsn.setcEmail1(cMails.get(0));
            splitPsn.setcEmail2(cMails.get(1));
          } else {
            splitPsn.setcEmail1(cMails.get(0));
          }
        }

        List<String> cMoblies = (List<String>) paramsOutMap.get("correlation_mobile");
        if (cMoblies != null && !cMoblies.isEmpty()) {
          if (cMoblies.size() > 1) {
            splitPsn.setcPhone1(cMoblies.get(0));
            splitPsn.setcPhone2(cMoblies.get(1));
          } else {
            splitPsn.setcPhone1(cMoblies.get(0));
          }
        }
      }
      sieTaskKpiVdDetailSplitPsnDao.saveOrUpdate(splitPsn);
      kpiVdDetail.setSplitStatus(1);
      sieTaskKpiValidateDetailDao.save(kpiVdDetail);
    } catch (Exception e) {
      kpiVdDetail.setSplitStatus(9);
      sieTaskKpiValidateDetailDao.save(kpiVdDetail);
      logger.error(
          "TASK_KPI_VALIDATE_DETAIL数据拆分到TASK_KPI_VDDETAIL_SPLIT_PSN时失败， TASK_KPI_VALIDATE_DETAIL数据主键id:{},uuid:{}",
          new Object[] {kpiVdDetail.getId(), kpiVdDetail.getUuId(), e});
    }
  }

  /**
   * 拆分到TMP_KPI_VDDETAIL_SPLIT_ORG
   * 
   * @param kpiVdDetail
   */
  @SuppressWarnings("unchecked")
  private void splitOrgDetail(SieTaskKpiValidateDetail kpiVdDetail) {
    SieTaskKpiVdDetailSplitOrg splitOrg = new SieTaskKpiVdDetailSplitOrg();
    try {
      splitOrg.setUuId(kpiVdDetail.getUuId());
      splitOrg.setDetailId(kpiVdDetail.getId());
      splitOrg.setStatus(kpiVdDetail.getStatus());
      splitOrg.setInterfaceResult(kpiVdDetail.getInterfaceResult());


      Map<String, Object> paramsinMap = JacksonUtils.json2HashMap(kpiVdDetail.getParamsIn());
      List<Map<String, Object>> dataList = (List<Map<String, Object>>) paramsinMap.get(SieKpiVdConstant.MAP_DATA);
      Map<String, Object> orgMap = dataList.get(0);

      /**
       * {"data":[{"keyCode":"65753216491605675",
       * "orgNo":"91360111MA35JFGC18","name":"国网江西省电力有限公司电力科学研究院","url":"330096"}],"openid":"88888888","token":"11111111verfyorg"}
       */
      // 从params_in 请求参数中解析字段
      splitOrg.setOrgNo((String) orgMap.get("orgNo"));
      splitOrg.setName((String) orgMap.get("name"));

      // 从params_out 中解析字段
      /**
       * {"correlation_uniform":[{"name":"江西省生态气象中心","orgNo":"12360000MB0554943U","type":"2"}],
       * "error_msg_uniform":"","org_name":"江西省生态气象中心（江西省气候变化监测评估中心）","validate_uniform":"2"}
       */
      if (kpiVdDetail.getStatus() != null && kpiVdDetail.getParamsOut() != null) {
        Map<String, Object> paramsOutMap = JacksonUtils.json2HashMap(kpiVdDetail.getParamsOut());
        List<Map<String, Object>> cUniforms = (List<Map<String, Object>>) paramsOutMap.get("correlation_uniform");
        if (cUniforms != null && !cUniforms.isEmpty()) {
          if (cUniforms.size() > 1) {
            Map<String, Object> map1 = cUniforms.get(0);
            Map<String, Object> map2 = cUniforms.get(1);
            splitOrg.setcName1((String) map1.get("name"));
            splitOrg.setcUniform1((String) map1.get("orgNo"));
            splitOrg.setcName2((String) map2.get("name"));
            splitOrg.setcUniform2((String) map2.get("orgNo"));
          } else {
            Map<String, Object> map1 = cUniforms.get(0);
            splitOrg.setcName1((String) map1.get("name"));
            splitOrg.setcUniform1((String) map1.get("orgNo"));
          }
        }
        String vUniform = (String) paramsOutMap.get("validate_uniform");
        splitOrg.setvUniform(vUniform == "" ? null : Integer.valueOf(vUniform));
      }
      sieTaskKpiVdDetailSplitOrgDao.saveOrUpdate(splitOrg);
      kpiVdDetail.setSplitStatus(1);
      sieTaskKpiValidateDetailDao.save(kpiVdDetail);
    } catch (Exception e) {
      kpiVdDetail.setSplitStatus(9);
      sieTaskKpiValidateDetailDao.save(kpiVdDetail);
      logger.error(
          "TASK_KPI_VALIDATE_DETAIL数据拆分到TASK_KPI_VDDETAIL_SPLIT_ORG时失败， TASK_KPI_VALIDATE_DETAIL表数据主键id:{},uuid:{}",
          new Object[] {kpiVdDetail.getId(), kpiVdDetail.getUuId(), e});
    }
  }


  /**
   * 拆分到TMP_KPI_VDDETAIL_SPLIT_PUB
   * 
   * @param kpiVdDetail
   */
  @SuppressWarnings("unchecked")
  private void splitPubDetail(SieTaskKpiValidateDetail kpiVdDetail) {
    SieTaskKpiVdDetailSplitPub splitPub = new SieTaskKpiVdDetailSplitPub();
    try {
      splitPub.setUuId(kpiVdDetail.getUuId());
      splitPub.setDetailId(kpiVdDetail.getId());
      splitPub.setStatus(kpiVdDetail.getStatus());
      splitPub.setInterfaceResult(kpiVdDetail.getInterfaceResult());

      // 根据 params_in 拆分
      /**
       * {"data":"{\"participantNames\":\"王磊;杨词慧;温靖;应进;高文;刘志高\",
       * \"pubInfoList\":[{\"keyCode\":\"65753626648537499\",
       * \"publishYear\":\"2017\",\"journalName\":\"Materials Letters\", \"authorNames\":\"Wang; Lei;Pan;
       * Jing;Wen; Jing\", \"title\":\"The exploration of role of tip-sample contact on scanning probe
       * phase-change memory\",
       * \"doi\":\"10.1016/j.matlet.2017.06.130\"}]}","openid":99999999,"token":"11111111lxj3219s"}
       */
      Map<String, Object> paramsinMap = JacksonUtils.json2HashMap(kpiVdDetail.getParamsIn());
      Map<String, Object> dataMap = JacksonUtils.json2HashMap((String) paramsinMap.get(SieKpiVdConstant.MAP_DATA));
      splitPub.setpNames((String) dataMap.get("participantNames"));
      List<Map<String, Object>> pubInfoList = (List<Map<String, Object>>) dataMap.get("pubInfoList");
      if (pubInfoList != null && !pubInfoList.isEmpty()) {
        Map<String, Object> pubInfo = pubInfoList.get(0);
        splitPub.setYear(
            (String) pubInfo.get("publishYear") == "" ? null : Integer.valueOf((String) pubInfo.get("publishYear")));
        splitPub.setjName((String) pubInfo.get("journalName"));
        splitPub.setAuthor((String) pubInfo.get("authorNames"));
        splitPub.setTitle((String) pubInfo.get("title"));
        splitPub.setDoi((String) pubInfo.get("doi"));
        splitPub.setFundInfo((String) (pubInfo.get("fundingInfo") == null ? "" : pubInfo.get("fundingInfo")));
      }


      // 从 params_out 中拆分数据
      /**
       * {"zh_title":"The exploration of role of tip-sample contact on scanning probe phase-change
       * memory", "validate_doi":"1","validate_title":"1","error_msg_author":"","error_msg_title":"",
       * "error_msg_doi":"","validate_fundinfo":"","validate_jname":"1","error_msg_fundinfo":"",
       * "error_msg_pubyear":"","correlation_pub":"{\"publishYear\":2017,\"journalName\":\"Materials
       * Letters\", \"fundingInfo\":\"Natural Science Foundation of Jiangxi Science and Technology
       * Department [20151BAB217003]; Foundation of Jiangxi Education Department [GJJ50719]\",
       * \"authorNames\":\"Wang, Lei; Pan, Jing; Wen, Jing\",\"title\":\"The exploration of role of
       * tip-sample contact on scanning probe phase-change memory\",
       * \"doi\":\"10.1016/j.matlet.2017.06.130\"}",
       * "validate_author":"3","error_msg_jname":"","validate_pubyear":"1"}
       */

      if (kpiVdDetail.getStatus() != null && kpiVdDetail.getParamsOut() != null) {
        Map<String, Object> paramsOutMap = JacksonUtils.json2HashMap(kpiVdDetail.getParamsOut());
        String vAuthor = (String) paramsOutMap.get("validate_author");
        String vJname = (String) paramsOutMap.get("validate_jname");
        String vDoi = (String) paramsOutMap.get("validate_doi");
        String vYear = (String) paramsOutMap.get("validate_pubyear");
        String vTitle = (String) paramsOutMap.get("validate_title");
        String vFundingInfo = (String) paramsOutMap.get("validate_fundinfo");



        splitPub.setvAuthor(vAuthor == "" ? null : Integer.valueOf(vAuthor));
        splitPub.setvJname(vJname == "" ? null : Integer.valueOf(vJname));
        splitPub.setvDoi(vDoi == "" ? null : Integer.valueOf(vDoi));
        splitPub.setvYear(vYear == "" ? null : Integer.valueOf(vYear));
        splitPub.setvTitle(vTitle == "" ? null : Integer.valueOf(vTitle));
        splitPub.setvFundingInfo(vFundingInfo == "" ? null : Integer.valueOf(vFundingInfo));
        Map<String, Object> correlationPub = new HashMap<String, Object>();
        try {
          correlationPub = (Map<String, Object>) paramsOutMap.get("correlation_pub");// 待修改
        } catch (Exception e) {
          correlationPub = (String) paramsOutMap.get("correlation_pub") == "" ? null
              : JacksonUtils.json2HashMap((String) paramsOutMap.get("correlation_pub"));// 待修改
        }
        if (correlationPub != null) {
          splitPub.setcAuthor((String) correlationPub.get("authorNames"));
          splitPub.setcDoi((String) correlationPub.get("doi"));
          splitPub.setcJname((String) correlationPub.get("journalName"));
          splitPub.setcTitle((String) correlationPub.get("title"));
          // String cYear = (String) correlationPub.get("publishYear");
          try {
            splitPub.setcYear((Integer) correlationPub.get("publishYear"));
          } catch (ClassCastException e) {
            String cYear = (String) correlationPub.get("publishYear");
            splitPub.setcYear(cYear == "" ? null : Integer.valueOf(cYear));
          }
          splitPub.setcFundingInfo((String) correlationPub.get("fundingInfo"));
        }
      }

      // 从interface_result中的itemMsg 节点拆出 authorNames内容
      /**
       * {"msg":"scm-000 数据处理成功","result":[ {"correlationData": {"publishYear":2017,"journalName":"Journal
       * of Hydroecology","fundingInfo":"", "authorNames":"王生; 方春林; 周辉明; 贺刚; 张燕萍; 傅培峰; 吴斌;
       * 王庆萍","title":"鄱阳湖刀鲚的渔汛特征及渔获物分析","doi":"10.15928/j.1674-3075.2017.06.011"},
       * "keyCode":"7jchplg1547467825265","itemStatus":2,
       * "type":{"keyCode":"","title":"1","doi":"3","journalName":"1","authorNames":"2","publishYear":"1","fundingInfo":"","otherErrorMsg":""},
       * "itemMsg":{"keyCode":"","title":"","doi":"scm-2059 doi为空","journalName":"",
       * "authorNames":"scm-2065
       * 作者名少了","publishYear":"","fundingInfo":"","otherErrorMsg":""}}],"openid":99999999,"time":28,"status":"success"}
       */
      if (kpiVdDetail.getInterfaceResult() != null) {
        Map<String, Object> interfaceResultMap = JacksonUtils.json2HashMap(kpiVdDetail.getInterfaceResult());
        List<Object> resultList = (List<Object>) interfaceResultMap.get("result");
        if (resultList != null && !resultList.isEmpty()) {
          Map<String, Object> result0 = (Map<String, Object>) resultList.get(0);
          Map<String, Object> itemMsg = (Map<String, Object>) result0.get("itemMsg");
          if (itemMsg != null && !itemMsg.isEmpty()) {
            String vAuthorCode = (String) itemMsg.get("authorNames");
            splitPub.setvAuthorCode(vAuthorCode == "" ? null : vAuthorCode);
          }
        }
      }

      sieTaskKpiVdDetailSplitPubDao.saveOrUpdate(splitPub);
      kpiVdDetail.setSplitStatus(1);
      sieTaskKpiValidateDetailDao.save(kpiVdDetail);
    } catch (Exception e) {
      kpiVdDetail.setSplitStatus(9);
      sieTaskKpiValidateDetailDao.save(kpiVdDetail);
      logger.error(
          "TASK_KPI_VALIDATE_DETAIL数据拆分到TASK_KPI_VDDETAIL_SPLIT_PUB时失败， TASK_KPI_VALIDATE_DETAIL表数据主键id:{},uuid:{}",
          new Object[] {kpiVdDetail.getId(), kpiVdDetail.getUuId(), e});
    }
  }

  @Override
  public Long getSuccessCountByUUID(String uuId) {
    try {
      return sieTaskKpiValidateDetailDao.getSuccessCountByUUID(uuId);
    } catch (Exception e) {
      logger.error("根据uuid:{}读取TASK_KPI_VALIDATE_DETAIL表中需要处理的总数出错 ！", uuId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getErrorCountByUUID(String uuId) {
    try {
      return sieTaskKpiValidateDetailDao.getErrorCountByUUID(uuId);
    } catch (Exception e) {
      logger.error("根据uuid:{}读取TASK_KPI_VALIDATE_DETAIL表中需要处理的总数出错 ！", uuId, e);
      throw new ServiceException(e);
    }
  }

  // 给service层调用，保存错误状态
  @Override
  public void saveErrorForDetail(SieTaskKpiValidateDetail kpiVdDetail) {
    kpiVdDetail.setSplitStatus(9);
    sieTaskKpiValidateDetailDao.save(kpiVdDetail);
  }
}
