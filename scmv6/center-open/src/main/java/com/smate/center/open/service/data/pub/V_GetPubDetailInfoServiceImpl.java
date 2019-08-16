package com.smate.center.open.service.data.pub;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.dao.grp.GrpPubRcmdDao;
import com.smate.center.open.dao.interconnection.log.InterconnectionImportPubLogDao;
import com.smate.center.open.model.grp.GrpPubRcmd;
import com.smate.center.open.model.interconnection.log.InterconnectionImportPubLog;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.data.ThirdDataTypeService;
import com.smate.center.open.service.interconnection.pub.InterconnectionIsisPubService;
import com.smate.center.open.service.login.ThirdLoginService;
import com.smate.center.open.service.util.PubDetailVoUtil;
import com.smate.core.base.email.service.EmailCommonService;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.url.HttpRequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;

/**
 * 获取成果详细信息接口 (可取多个);分sns库和sie库成
 * 
 * @author tsz
 */
@Transactional(rollbackFor = Exception.class)
public class V_GetPubDetailInfoServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private InterconnectionImportPubLogDao interconnectionImportPubLogDao;
  @Resource(name = "isisPubService")
  private InterconnectionIsisPubService interconnectionIsisPubService;
  @Resource(name = "pdwhPubService")
  private InterconnectionIsisPubService pdwhPubService;

  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private GrpPubRcmdDao grpPubRcmdDao;
  @Resource(name = "oauthLoginSecurityIDImpl")
  private ThirdDataTypeService thirdDataTypeService;
  @Autowired
  private ThirdLoginService thirdLoginService;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    // 没有 参数校验 直接返回成功
    Map<String, Object> temp = new HashMap<String, Object>();

    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data != null && data.toString().length() > 0) {
      Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
      if (dataMap != null) {
        paramet.putAll(dataMap);
      }
    }
    // 2018-10-31--新扩展的类型不要检查，查询各种类型的成果，这里不需要解析
    Object pubSource = paramet.get("pubSource");
    if (pubSource != null && "allType".equals(pubSource.toString())) {
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      return temp;
    }
    // end
    List<Long> pubIdList = handleParmaToPubIds(paramet);
    if (pubIdList == null || pubIdList.size() == 0) {
      logger.error("服务参数  pubIdList列表不能为空，或pubIdList列表不符合要求");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_268, paramet, "服务参数  pubIdList列表不能为空，或pubIdList列表不符合要求");
      return temp;
    }
    paramet.put(PubDetailConstant.PUB_ID_LIST, pubIdList);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    // 要查询的成果详情列表
    Object pubSource = paramet.get("pubSource");
    if (pubSource != null && "pdwh".equals(pubSource.toString())) {
      // 查找的基准库成果
      List<Long> pubIdList = (List<Long>) paramet.get(PubDetailConstant.PUB_ID_LIST);
      GetPdwhPubDetail(paramet, dataList, pubIdList);
    } else if (pubSource != null && "allType".equals(pubSource.toString())) {
      // 处理所有类型的成果，demo使用
      dealAllTypePub(paramet, dataList);
    } else {
      // 个人成果和未认领的成果
      List<Long> pubIdList = (List<Long>) paramet.get(PubDetailConstant.PUB_ID_LIST);
      GetPsnPubDetail(paramet, dataList, pubIdList);
    }
    return successMap(OpenMsgCodeConsts.SCM_000, PubDetailVoUtil.unescapeList(dataList));
  }

  private void dealAllTypePub(Map<String, Object> paramet, List<Map<String, Object>> dataList) {
    // 个人成果， 个人认领成果， 群组推荐成果，群组认领成果
    List<Long> psnPubIds = new ArrayList<>();
    List<Long> rcmdGrpPubIds = new ArrayList<>();
    Object pubIds = paramet.get(PubDetailConstant.PUB_ID_LIST);
    if (pubIds != null && StringUtils.isNotBlank(pubIds.toString())) {
      String[] pubIdArr = pubIds.toString().split(",");
      if (pubIdArr != null && pubIdArr.length > 0) {
        for (int i = 0; i < pubIdArr.length; i++) {
          Long pubId = NumberUtils.toLong(pubIdArr[i].split("\\|")[0]);
          // 成果类型：1=个人成果；2=推荐成果；4=群组成果 ;5=群组推荐成果
          if(pubIdArr[i].split("\\|") .length == 1){
            psnPubIds.add(pubId);
            continue;
          }
          int type = NumberUtils.toInt(pubIdArr[i].split("\\|")[1]);
          switch (type) {
            case 5:
              rcmdGrpPubIds.add(pubId);
              break;
            default:
              psnPubIds.add(pubId);
          }
        }
        if (psnPubIds.size() > 0) {
          GetPsnPubDetail(paramet, dataList, psnPubIds);
        }
        if (rcmdGrpPubIds.size() > 0) {
          List<GrpPubRcmd> grpPubRcmdList = grpPubRcmdDao.findPubIdsByIds(rcmdGrpPubIds);
          List<Long> pdwhPubIds = new ArrayList<>();
          Long grpId = 0L;
          Long psnId = (Long) paramet.get("psnId");
          List<Long>  delPubIds = new ArrayList<>();
          if (grpPubRcmdList != null && grpPubRcmdList.size() > 0) {
            for (int i = 0; i < grpPubRcmdList.size(); i++) {
              pdwhPubIds.add(grpPubRcmdList.get(i).getPubId());
              grpId = grpPubRcmdList.get(i).getGrpId();
              // 认领群组的推荐成果
              if (psnId != null && psnId != 0L && grpPubRcmdList.get(i).getStatus() == 0) {
                comfirmGrpRcmdPub(pdwhPubIds.get(i), psnId, grpId);
              }
              if( grpPubRcmdList.get(i).getStatus() == 8){
                delPubIds.add( grpPubRcmdList.get(i).getPubId());
              }
            }
            paramet.put("delPubIds",delPubIds);
            GetPdwhPubDetail(paramet, dataList, pdwhPubIds);
          }

        }
      }
    }
    // 认领群组推荐成果
  }

  /**
   * 获取个人成果详情
   * 
   * @param paramet
   * @param dataList
   * @param pubIdList
   */
  private void GetPsnPubDetail(Map<String, Object> paramet, List<Map<String, Object>> dataList, List<Long> pubIdList) {
    if (pubIdList != null && pubIdList.size() > 0) {
      // 发邮件信息
      Map<Long, List<Long>> receiveMailPersonMap = new HashMap<>();
      // 先从个人库查找成果，没有在去基准库查找成果
      for (Long pubId : pubIdList) {
        try {
          String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
          Map<String, Object> paramMap = new HashMap<>();
          paramMap.put(V8pubQueryPubConst.DES3_PUB_ID, Des3Utils.encodeToDes3(pubId.toString()));
          paramMap.put("serviceType", V8pubQueryPubConst.QUERY_OPEN_SNS_PUB);
          Map<String, Object> pubInfoMap = (Map<String, Object>) getRemotePubInfo(paramMap, SERVER_URL);
          PubDetailVO pubDetailVO = null;
          if (pubInfoMap != null  && pubInfoMap.get("pubId") != null) {
            pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubInfoMap));
          }
          if (pubDetailVO != null) {
            dataList.add(interconnectionIsisPubService.parseXmlToMap1(pubDetailVO));
          } else {// 以下是去基准库成果查找的

            paramMap.put("serviceType", V8pubQueryPubConst.PDWH_PUBBY_PUB_CONFIRM_ID);
            pubInfoMap = (Map<String, Object>) getRemotePubInfo(paramMap, SERVER_URL);
            if (pubInfoMap != null) {
              pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubInfoMap));
            }
            if (pubDetailVO != null) {
              // 基准库成果不是自己的，所以传0L , 下面会判断是否自动确认该成果
              Map<String, Object> map = pdwhPubService.parsePdwhXmlXmlToMap1(pubDetailVO, 0L);
              // pub_id 保存基准库的成果id
              map.put("pub_id", pubDetailVO.getPubId().toString());
              dataList.add(map);
              paramet.put("pdwhPubId", pubDetailVO.getPubId().toString());
              if(map.get("status") != "" &&map.get("status").toString().equals("1")){
                continue;
              }
              paramet.put("rolPubOwnerPsnId", pubInfoMap.get("pubOwnerPsnId"));// 待认领成果的psnId
              getRolPubInfoNextStep(paramet, receiveMailPersonMap);
            } else {
              logger.error("获取不到对应的成果ID详情：pubId=" + pubId);
              continue;
            }

          }
          InterconnectionImportPubLog log = new InterconnectionImportPubLog();
          log.setOpenId(Long.valueOf(paramet.get(OpenConsts.MAP_OPENID).toString()));
          log.setToken(paramet.get(OpenConsts.MAP_TOKEN).toString());
          log.setPubId(pubId);
          log.setImportDate(new Date());
          log.setDescribe("获取成果详情：psnPub");
          interconnectionImportPubLogDao.save(log);
        } catch (Exception e) {
          logger.error("获取成果详情异常：pubId=" + pubId, e);
        }
      }
      String senderPsnName = paramet.get("psnName") != null ? paramet.get("psnName").toString() : "";
      sendEmail((Long) paramet.get("psnId"), senderPsnName, receiveMailPersonMap);
    }
  }

  /**
   * 获取基准库成果详情
   * 
   * @param paramet
   * @param dataList
   * @param pubIdList
   */
  private void GetPdwhPubDetail(Map<String, Object> paramet, List<Map<String, Object>> dataList, List<Long> pubIdList) {
    Long psnId = (Long) paramet.get("psnId");
    psnId = psnId == null ? 0L : psnId;
    if (pubIdList != null && pubIdList.size() > 0) {
      for (Long pubId : pubIdList) {
        try {
          String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
          Map<String, Object> paramMap = new HashMap<>();
          paramMap.put(V8pubQueryPubConst.DES3_PUB_ID, Des3Utils.encodeToDes3(pubId.toString()));
          paramMap.put("serviceType", V8pubQueryPubConst.OPEN_PDWH_PUB);
          Map<String, Object> pubInfoMap = (Map<String, Object>) getRemotePubInfo(paramMap, SERVER_URL);
          PubDetailVO pubDetailVO = null;
          if (pubInfoMap != null) {
            pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubInfoMap));
          }
          if (pubDetailVO != null) {
            if(paramet.get("delPubIds") != null){
              //删除的成果id
              List  delPubIds = (List<Long>)paramet.get("delPubIds");
              if( delPubIds.contains(pubDetailVO.getPubId())){
                pubDetailVO.setStatus(1);
              }
            }
            Map<String, Object> map = pdwhPubService.parsePdwhXmlXmlToMap1(pubDetailVO, psnId);
            map.put("pub_id", pubId.toString());
            dataList.add(map);
          } else {
            logger.error("获取不到对应的成果ID详情：pubId=" + pubId);
            continue;
          }
          InterconnectionImportPubLog log = new InterconnectionImportPubLog();
          log.setOpenId(Long.valueOf(paramet.get(OpenConsts.MAP_OPENID).toString()));
          log.setToken(paramet.get(OpenConsts.MAP_TOKEN).toString());
          log.setPubId(pubId);
          log.setImportDate(new Date());
          log.setDescribe("获取成果详情：psnPub");
          interconnectionImportPubLogDao.save(log);
        } catch (Exception e) {
          logger.error("获取成果详情异常：pubId=" + pubId, e.toString());
        }
      }
    }
  }

  /**
   * 解析参数中的成果id
   * 
   * @return
   */
  public List<Long> handleParmaToPubIds(Map<String, Object> serviceData) {

    List<Long> pubIdList = new ArrayList<Long>();
    if (serviceData != null && serviceData.get("pubIdList") != null) {
      String pubIds = serviceData.get("pubIdList").toString();
      if (StringUtils.isNotBlank(pubIds)) {
        String[] pubIdArr = pubIds.split(",");
        for (int i = 0; i < pubIdArr.length; i++) {
          String pubId = StringUtils.trim(pubIdArr[i]);
          if (StringUtils.isNotBlank(pubId) && NumberUtils.isNumber(pubId)) {
            pubIdList.add(NumberUtils.toLong(pubId));
          }
        }
      }
    }
    return pubIdList;
  }

  /** 获取确认成果后，接下来要做的事情，自己的成果==自动确认，别人的成果=发邮件给成果拥有者 */
  private void getRolPubInfoNextStep(Map<String, Object> paramMap, Map<Long, List<Long>> receiveMailPersonMap) {
    boolean owenerRolPub = false;
    Long rolPubOwnerPsnId = NumberUtils.toLong(paramMap.get("rolPubOwnerPsnId").toString());
    // Long pcId = (Long) paramMap.get("pcId");
    Long pdwhPubId = Long.parseLong(paramMap.get("pdwhPubId").toString());
    if (rolPubOwnerPsnId == null) {
      logger.error("推荐库成果，推荐的成员psnId为空");
      return;
    }
    Long psnId = (Long) paramMap.get("psnId");
    if (!paramMap.get(OpenConsts.MAP_OPENID).toString().equals(OpenConsts.SYSTEM_OPENID.toString())
        && psnId.longValue() == rolPubOwnerPsnId.longValue()) {
      owenerRolPub = true;
    }
    if (owenerRolPub) {
      logger.info("自动确认！！！");
      comfirmRolPub(pdwhPubId, psnId);
    } else {
      List<Long> pdwhPubIdList = receiveMailPersonMap.get(rolPubOwnerPsnId);
      if (pdwhPubIdList == null) {
        pdwhPubIdList = new ArrayList<>();
        pdwhPubIdList.add(pdwhPubId);
        receiveMailPersonMap.put(rolPubOwnerPsnId, pdwhPubIdList);
      } else {
        pdwhPubIdList.add(pdwhPubId);
      }

    }

  }

  /** 发送邮件 */
  public void sendEmail(Long psnId, String senderPsnName, Map<Long, List<Long>> receiveMailPersonMap) {

    if (receiveMailPersonMap == null || receiveMailPersonMap.size() < 1) {
      return;
    }
    logger.info("发送" + receiveMailPersonMap.size() + "封邮件");
    // 如果发送者是 99999999
    Person sender = null;
    if (psnId == null || psnId == 0L || psnId.longValue() == OpenConsts.SYSTEM_OPENID.longValue()) {
      sender = new Person();
      sender.setName(senderPsnName);
      sender.setPersonId(OpenConsts.SYSTEM_OPENID);
    } else {
      sender = personDao.get(psnId);
      if (sender == null) {
        sender = new Person();
        sender.setName(senderPsnName);
        sender.setPersonId(OpenConsts.SYSTEM_OPENID);
      }
    }
    Set<Entry<Long, List<Long>>> entrySet = receiveMailPersonMap.entrySet();
    for (Entry<Long, List<Long>> entry : entrySet) {
      Long rolPubOwnerPsnId = entry.getKey();
      Person receiver = personDao.get(rolPubOwnerPsnId);
      try {
        if (isSend(sender, receiver)) {
          restSendEmail(entry.getValue(), sender, receiver);
        }
      } catch (Exception e) {
        logger.error("获取让人成果详情时，发送邮件异常！！+ psnId=" + psnId, e);
      }
    }

  }

  /**
   * 如果名字相同，就不要发送邮件 同一个人也不发送邮件
   * 
   * @param send
   * @param receive
   * @return
   */
  public boolean isSend(Person send, Person receive) {
    if (send == null || StringUtils.isBlank(send.getName())) {
      return false;
    }
    if (receive == null) {
      return false;
    }
    // 存在就不发送
    if (existName(send.getName(), receive)) {
      return false;
    }
    if (existName(send.getEnName(), receive)) {
      return false;
    }
    if (StringUtils.isNotBlank(send.getFirstName()) && StringUtils.isNotBlank(send.getLastName())
        && existName(send.getFirstName() + " " + send.getLastName(), receive)) {
      return false;
    }
    return true;
  }

  public boolean existName(String name, Person receive) {
    if (StringUtils.isBlank(name)) {
      return false;
    }
    if (name.equalsIgnoreCase(receive.getName()) || name.equalsIgnoreCase(receive.getEname())
        || name.equalsIgnoreCase(receive.getFirstName() + " " + receive.getLastName())) {
      return true;
    }
    return false;
  }

  /**
   * 调用接口发送项目成员推荐成果邮件
   * 
   * @param pubIdList
   * @param sender
   * @param receiver
   * @throws Exception
   */
  private void restSendEmail(List<Long> pubIdList, Person sender, Person receiver) throws Exception {
    if (receiver == null || pubIdList == null || pubIdList.isEmpty()) {
      throw new Exception("构建成果推荐确认回复，邮件对象为空" + this.getClass().getName());
    }
    String language = "";
    language = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }

    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, Object> mailData = new HashMap<String, Object>();

    // 1跟踪链接 根据key放置到模板中 所有的链接不再需要放到mailData中
    // 发送者的psnUrl
    String psnUrl = "";
    if (sender.getPersonId().longValue() != OpenConsts.SYSTEM_OPENID.longValue()) {
      // https://uat.scholarmate.com/P/n2Qf6z
      PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(sender.getPersonId());
      psnUrl = this.domainscm + "/P/" + psnProfileUrl.getPsnIndexUrl();
    }
    String AID = "";
    AID = getAID(receiver);
    // key3 , pubDetail.pubUrl pubDetail.zhTitle pubDetail.authorNames
    // pubDetail.briefDesc key15 proLogin1101
    String key3 = EmailCommonService.PC_OR_MB_TOKEN + "pc=" + domainscm
        + "/psnweb/homepage/show?module=pub&jumpto=puball&menuId=1200&" + "AID=" + AID + "&resetpwd=true" + "&&mobile="
        + domainMobile + "/psnweb/mobile/msgbox?model=centerMsg&whoFirst=pubRcmd";
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("psnUrl");
    l2.setUrl(psnUrl);
    l2.setUrlDesc("发件人主页链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    MailLinkInfo l3 = new MailLinkInfo();
    l3.setKey("cfmUrl");
    l3.setUrl(key3);
    l3.setUrlDesc("成果链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));

    // 2构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    Integer tempcode = 10070;
    info.setSenderPsnId(sender.getPersonId());
    info.setReceiverPsnId(receiver.getPersonId());
    info.setReceiver(receiver.getEmail());
    info.setMsg("项目成员推荐成果邮件");
    info.setMailTemplateCode(tempcode);
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));

    // 3模版参数集
    // 接收者 name
    String RcmPsnName = getPersonName(receiver, language);
    String psnName = getPersonName(sender, language);
    mailData.put("RcmPsnName", RcmPsnName);
    mailData.put("psnName", psnName);
    List<PubDetail> pubDetails = new ArrayList<PubDetail>();
    int total = 0;
    for (Long pubId : pubIdList) {
      String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
      PubQueryDTO pubQueryDTO = new PubQueryDTO();
      pubQueryDTO.setSearchPubId(pubId);
      pubQueryDTO.setServiceType(V8pubQueryPubConst.QUERY_PDWH_PUB_BY_PUB_ID_SERVICE);
      Object object = getRemotePubInfo(pubQueryDTO, SERVER_URL);
      Map<String, Object> result = (Map<String, Object>) getRemotePubInfo(pubQueryDTO, SERVER_URL);
      Map<String, Object> pubInfo = null;
      // https://test.scholarmate.com/pubweb/details/showpdwh?des3Id=8L0iq0ZD7sM%3D&language=ZH_CN&dbid=4
      if (result.get("status").equals("success")) {
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("resultList");
        pubInfo = resultList.get(0);
      }
      if (pubInfo != null) {
        total++;// 显示前三条
        if (total > 3) {
          break;
        }
        PubDetail pubDetail = new PubDetail();
        // //https://uat.scholarmate.com/pubweb/outside/pdwhdetails?des3Id=I8ohCZzdaFI%3D
        String pubUrl = pubInfo.get("pubIndexUrl").toString();
        if (pubInfo.get("pubType") != null) {
          int pubType = Integer.parseInt(pubInfo.get("pubType").toString());
          switch (pubType) {
            case 3:
            case 4:
            case 8:
              pubDetail.setPubTypeName("论文");
              break;
            case 5:
              pubDetail.setPubTypeName("专利");
              break;
            case 2:
              pubDetail.setPubTypeName("书/著作");
              break;
            case 7:
              pubDetail.setPubTypeName("其他");
              break;
            case 10:
              pubDetail.setPubTypeName("书籍章节");
              break;
            default:
              break;
          }
        }
        pubDetail.setPubUrl(pubUrl);
        pubDetail.setZhTitle(pubInfo.get("title").toString());
        pubDetail.setAuthorNames(pubInfo.get("authorNames").toString());
        pubDetail.setBriefDesc(pubInfo.get("briefDesc").toString());
        pubDetails.add(pubDetail);
      }
    }
    mailData.put("pubDetails", pubDetails);
    mailData.put("pubSum", pubIdList.size());

    // 4主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    subjectParamLinkList.add(RcmPsnName);
    subjectParamLinkList.add(psnName);
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
  }



  /**
   * @param receiver
   */
  private String getAID(Person receiver) {
    // {openid=83351686, token=12345678, data={"AutoLoginType":"ResetPWD"},
    // type=ime82dt2, psnId=1000000733630}
    Long openId = thirdLoginService.getOpenId("00000000", receiver.getPersonId(), 2);
    Map<String, Object> paramMap = new HashMap<String, Object>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    paramMap.put("openid", openId);
    paramMap.put("token", "00000000");
    paramMap.put("psnId", receiver.getPersonId());
    paramMap.put("type", "ime82dt2");
    dataMap.put("AutoLoginType", "ResetPWD");
    paramMap.put("data", JacksonUtils.mapToJsonStr(dataMap));
    Map<String, Object> resultMap = thirdDataTypeService.handleOpenDataForType(paramMap);
    List<Map<String, Object>> list = (List<Map<String, Object>>) resultMap.get("data");
    if (list != null && list.size() > 0) {
      Object obj = list.get(0).get("AID");
      return obj == null ? "" : obj.toString();
    }
    return "";
  }

  /**
   * @param person
   * @param language zh=中文
   * @return
   */
  String getPersonName(Person person, String language) {
    if ("zh".equalsIgnoreCase(language) || "zh_CN".equalsIgnoreCase(language)) {
      if (StringUtils.isNotBlank(person.getName())) {
        return person.getName();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getEname();
      }
    } else {
      if (StringUtils.isNotBlank(person.getEname())) {
        return person.getEname();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getName();
      }
    }
  }

  /**
   * @param zhContent
   * @param enContent
   * @param language
   * @return
   */
  String getContentByLanguage(String zhContent, String enContent, String language) {
    if ("zh".equalsIgnoreCase(language) || "zh_CN".equalsIgnoreCase(language)) {
      if (StringUtils.isNotBlank(zhContent)) {
        return zhContent;
      } else {
        return enContent;
      }
    } else {
      if (StringUtils.isNotBlank(enContent)) {
        return enContent;
      } else {
        return zhContent;
      }
    }
  }

  /**
   * 
   * 
   * @param pdwhPubId 基准库的成果id
   * 
   * @param psnId
   */
  public void comfirmRolPub(Long pdwhPubId, Long psnId) {
    // 然后，把基准库的成果导入到科研之友库
    // des3PsnId
    String SERVER_URL = domainscm + V8pubQueryPubConst.PUB_CONFIRM;
    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("des3PubId", Des3Utils.encodeToDes3(pdwhPubId.toString()));
    paramMap.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    Map<String, Object> result = (Map<String, Object>) getRemotePubInfo(paramMap, SERVER_URL);
    logger.info(result.toString());
  }

  public void comfirmGrpRcmdPub(Long pdwhPubId, Long psnId, Long grpId) {
    // 然后，把基准库的成果导入到科研之友库
    // des3PsnId
    String SERVER_URL = domainscm + V8pubQueryPubConst.GRP_PUB_CONFIRM;
    String des3PsnId = Des3Utils.encodeToDes3(psnId.toString());
    String outputStr = "grpId=" + grpId + "&pubId=" + pdwhPubId + "&des3PsnId=" + des3PsnId;
    String result = HttpRequestUtils.sendPost(SERVER_URL, outputStr);
    Map<Object, Object> resultMap = JacksonUtils.jsonToMap(result);
    if (resultMap.get("result").equals("success")) {
    }
    logger.info(result.toString());
  }

  public static void main(String[] args) {
    String url = "https://dev.scholarmate.com/groupweb/grppub/ajaxacceptpubrcmd";
    String des3PsnId = Des3Utils.encodeToDes3("1000000733255");
    des3PsnId = "gdC9pv0cs%2Bs8yWde%2FPxjKw%3D%3D";
    String outputStr = "grpId=110000000000751&pubId=7652&des3PsnId=" + des3PsnId;
    System.out.println("kk");
    String result = HttpRequestUtils.sendPost(url, outputStr);
    Map<Object, Object> resultMap = JacksonUtils.jsonToMap(result);
    if (resultMap.get("result").equals("success")) {
      System.out.println("success");
    }
    System.out.println(result);
  }
}
