package com.smate.center.open.service.data.pub.verify;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.pubinfo.PubInfoVerifyConstant;
import com.smate.center.open.service.pubinfo.PubInfoVerifyService;
import com.smate.center.open.service.pubinfo.PubInfoVerifyServiceImpl;
import com.smate.center.open.service.pubinfo.PubInfoVerifyUtil;
import com.smate.center.open.service.util.PubDetailVoUtil;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.string.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 个人主页成果验证 SCM-21178
 * 
 * @author ajb
 * @date 2018-11-13
 */
@Transactional(rollbackFor = Exception.class)
public class PsnPubVerifyServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Resource
  private PubInfoVerifyService pubInfoVerifyService;


  @Value("${domainscm}")
  public String domainscm;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    Object psnInfo = serviceData.get("psnInfo");
    Object pubInfoList = serviceData.get("pubInfoList");
    if (psnInfo == null) {
      logger.error("scm-2033 具体服务类型参数psnInfo不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2033, paramet, "scm-2033 具体服务类型参数psnInfo不能为空");
      return temp;
    }
    VerifyPsnInfo verifyPsnInfo = new VerifyPsnInfo();
    Map<String, Object> psnInfoObj = (Map) psnInfo;
    try {
      BeanUtils.populate(verifyPsnInfo, psnInfoObj);
    } catch (Exception e) {
      logger.error("解析psnInfo 信息异常，psnInfo=" + psnInfo, e);
    }
    if (StringUtils.isBlank(verifyPsnInfo.getName()) || StringUtils.isBlank(verifyPsnInfo.getEmail())) {
      logger.error("SCM_2034 具体服务类型参数psnInfo对象的name和email不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2034, paramet, "具体服务类型参数psnInfo对象的name和email不能为空");
      return temp;
    }
    if (verifyPsnInfo.getName().length() > PubInfoVerifyUtil.name_max_len) {
      logger.error("SCM_2037 具体服务类型参数psnInfo对象的name长度不能超过" + PubInfoVerifyUtil.name_max_len);
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2037, paramet,
          "具体服务类型参数psnInfo对象的name长度不能超过" + PubInfoVerifyUtil.name_max_len);
      return temp;
    }
    if (verifyPsnInfo.getEmail().length() > PubInfoVerifyUtil.email_max_len) {
      logger.error("SCM_2038 具体服务类型参数psnInfo对象的email长度不能超过" + PubInfoVerifyUtil.email_max_len);
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2038, paramet,
          "具体服务类型参数psnInfo对象的email长度不能超过" + PubInfoVerifyUtil.email_max_len);
      return temp;
    }
    if (verifyPsnInfo.getPhone().length() > PubInfoVerifyUtil.tel_max_len) {
      logger.error("SCM_2037 具体服务类型参数psnInfo对象的phone最大长度必须小于等于" + PubInfoVerifyUtil.tel_max_len);
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2039, paramet,
          "具体服务类型参数psnInfo对象的phone长度必须小于等于" + PubInfoVerifyUtil.tel_max_len);
      return temp;
    }
    paramet.put("verifyPsnInfo", verifyPsnInfo);
    if (pubInfoList == null) {
      logger.error("具体服务类型参数pubInfoList不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2032, paramet, "具体服务类型参数pubInfoList不能为空");
      return temp;
    }
    dealPubInfoList(paramet, (List) pubInfoList);
    paramet.putAll(serviceData);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  private void dealPubInfoList(Map<String, Object> paramet, List pubInfoList) {
    List<PaperInfo> paperlist = new ArrayList<>();
    List<Map<String, Object>> list = pubInfoList;
    if (list != null && list.size() > 0) {
      for (int i = 0; i < list.size(); i++) {
        PaperInfo paperInfo = new PaperInfo();
        try {
          BeanUtils.populate(paperInfo, list.get(i));
          paperlist.add(paperInfo);
        } catch (Exception e) {
          logger.error("验证论文的信息，解析异常 pubInfo=" + list.get(i), e);
        }
      }
    }
    paramet.put("paperlist", paperlist);
  }


  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    // 具体业务
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    List<PaperInfo> paperlist = (List<PaperInfo>) paramet.get("paperlist");
    VerifyPsnInfo verifyPsnInfo = (VerifyPsnInfo) paramet.get("verifyPsnInfo");
    // 匹配科研之友人员信息，获取姓名 在组合 start
    Set<String> psnNameListTemp = new HashSet<>();
    Set<String> psnNameList = new HashSet<>();
    buildPsnName(verifyPsnInfo, psnNameListTemp);
    String psnNameStr = "";
    for(String name : psnNameListTemp){
      name = PubDetailVoUtil.dealAuthorNames(name);
      psnNameList.add(name);
      psnNameStr += name;
    }
    // 组合姓名结束 end
    if (paperlist != null && paperlist.size() > 0) {
      if (paperlist.size() > 50) {
        paperlist = paperlist.subList(0, 50);
      }
      for (PaperInfo paperInfo : paperlist) {
        // 验证成果信息是否符合规范
        PaperItemMsg itemMsg = new PaperItemMsg();
        paperInfo.setParticipantNames(psnNameStr);
        boolean flag = doVerifyPaperInfo(paperInfo, itemMsg);
        Map<String, Object> resultMap = null ;
        if (!flag) {
          resultMap = PubInfoVerifyServiceImpl.buildResultMap(PubInfoVerifyConstant.NOT_SURE,
              paperInfo.getKeyCode());
          dataList.add(resultMap);
          resultMap.put(PubInfoVerifyConstant.ITEM_MSG, itemMsg);
          pubInfoVerifyService.savePubVerifyLog(resultMap,paperInfo,PubInfoVerifyUtil.home_pub_type);
          continue;
        }
        // 如果用户不存在于 成果直接返回不匹配
        int psnPubPosIdx = pubInfoVerifyService.findPubAuthorPosition(paperInfo.getAuthorNames(), psnNameList);
        if (psnPubPosIdx >= 0) {
          resultMap = pubInfoVerifyService.doMatchPdwhPubAuthor(paperInfo, psnPubPosIdx, psnNameList);
          dataList.add(resultMap);
        } else {
          // {"itemStatus":1,"keyCode":"1234","itemMsg":"xxx"}
         resultMap = PubInfoVerifyServiceImpl.buildResultMap(PubInfoVerifyConstant.UNMATCH,
              paperInfo.getKeyCode());
          dataList.add(resultMap);
          itemMsg.setAuthorNames(OpenMsgCodeConsts.SCM_2045);
          resultMap.put(PubInfoVerifyConstant.ITEM_MSG, itemMsg);
        }
        // 判断作者名长度是否大于5
        PaperItemMsg itemMsg2 =  (PaperItemMsg)resultMap.get(PubInfoVerifyConstant.ITEM_MSG);
        if(resultMap.get("itemStatus").equals(PubInfoVerifyConstant.UNMATCH) && !itemMsg2.getAuthorNames().equals(OpenMsgCodeConsts.SCM_2067)){
          Object correlationData = resultMap.get("correlationData");
          PsnPubInfo psnPubInfo = null ;
          if(correlationData !=null){
            psnPubInfo = (PsnPubInfo) correlationData;
          }
          if(checkNameGtlen5(paperInfo,psnPubInfo , psnNameList)){
            resultMap.put("itemStatus",PubInfoVerifyConstant.NOT_SURE);
            itemMsg2.setAuthorNames(OpenMsgCodeConsts.SCM_2072);
          }
        }
        //最后记录日志
        pubInfoVerifyService.savePubVerifyLog(resultMap,paperInfo,PubInfoVerifyUtil.home_pub_type);
      }
    }

    return successMap(OpenMsgCodeConsts.SCM_000, dataList);


  }

  private  boolean checkNameGtlen5(PaperInfo paperInfo , PsnPubInfo psnPubInfo , Set<String> psnNameList){
    String authorNames = paperInfo.getAuthorNames();
    String[] split =  null ;
    split = authorNames.split(PubInfoVerifyServiceImpl.sem_split);
    split = PubInfoVerifyUtil.dealBlankAuthorsNames(split);
    for(String s : split){
      if(s.length() > 5 && ServiceUtil.isChineseStr(s)){
           return true ;
      }
    }
    //基准库成果
    if( psnPubInfo != null){
      split = psnPubInfo.getAuthorNames().split(PubInfoVerifyServiceImpl.sem_split);
      split = PubInfoVerifyUtil.dealBlankAuthorsNames(split);
      for(String s : split){
        if(s.length() > 5  && ServiceUtil.isChineseStr(s)){
          return true ;
        }
      }
    }

    //参与人
    for(String s : psnNameList){
      if(s.length() > 5  && ServiceUtil.isChineseStr(s)){
        return true ;
      }
    }
    return false ;
  }

  private boolean doVerifyPaperInfo(PaperInfo paperInfo, PaperItemMsg itemMsg) {
    if (StringUtils.isBlank(paperInfo.getKeyCode())) {
      itemMsg.setKeyCode(OpenMsgCodeConsts.SCM_2055);
      return false;
    }
    if (paperInfo.getKeyCode().length() > PubInfoVerifyUtil.keycode_max_len) {
      itemMsg.setKeyCode(OpenMsgCodeConsts.SCM_2056);
      return false;
    }
    if (StringUtils.isBlank(paperInfo.getTitle())) {
      itemMsg.setTitle(OpenMsgCodeConsts.SCM_2052);
      return false;
    }
    if (paperInfo.getTitle().length() > PubInfoVerifyUtil.title_max_len) {
      itemMsg.setTitle(OpenMsgCodeConsts.SCM_2053);
      return false;
    }

    if (StringUtils.isBlank(paperInfo.getAuthorNames())) {
      itemMsg.setAuthorNames(OpenMsgCodeConsts.SCM_2057);
      return false;
    }
    if (paperInfo.getAuthorNames().length() > PubInfoVerifyUtil.authors_max_len) {
      itemMsg.setAuthorNames(OpenMsgCodeConsts.SCM_2054);
      return false;
    }
    return true;
  }

  private void buildPsnName(VerifyPsnInfo verifyPsnInfo, Set<String> nameList) {
    nameList.add(verifyPsnInfo.getName());
    // 如果匹配上 科研之友 把名字组合加入匹配组合中tsz
    Set<String> personNames = pubInfoVerifyService.findPersonByVerifyPsnInfo(verifyPsnInfo);
    if (CollectionUtils.isNotEmpty(personNames)) {
      nameList.addAll(personNames);
    }
  }



}
