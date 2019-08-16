package com.smate.center.open.service.data.pub.verify;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.pubinfo.PubInfoVerifyConstant;
import com.smate.center.open.service.pubinfo.PubInfoVerifyService;
import com.smate.center.open.service.pubinfo.PubInfoVerifyServiceImpl;
import com.smate.center.open.service.pubinfo.PubInfoVerifyUtil;
import com.smate.core.base.utils.string.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 论文验证SCM-21177
 * 
 * @author ajb
 * @date 2018-11-12
 */
@Transactional(rollbackFor = Exception.class)
public class PaperVerifyServiceImpl extends ThirdDataTypeBase {

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
    Object participantNames = serviceData.get("participantNames");
    Object pubInfoList = serviceData.get("pubInfoList");
    if (participantNames == null || StringUtils.isBlank(participantNames.toString())
        || participantNames.toString().length() > 500) {
      logger.error("具体服务类型参数participantNames不能为空，且长度不能超过500");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2031, paramet, "具体服务类型参数participantNames不能为空，且长度不能超过500");
      return temp;
    }
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
          Map<String, Object> map = list.get(i);
          paperInfo.setDoiCompare(map.get("doi") != null ? true : false );
          paperInfo.setJournalNameCompare(map.get("journalName") != null ? true : false );
          paperInfo.setAuthorNamesCompare(map.get("authorNames") != null ? true : false );
          paperInfo.setPublishYearCompare(map.get("publishYear") != null ? true : false );
          paperInfo.setFundingInfoCompare(map.get("fundingInfo") != null ? true : false );
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
    String participantNames = paramet.get("participantNames").toString();
    if (paperlist != null && paperlist.size() > 0) {
      if (paperlist.size() > 50) {
        paperlist = paperlist.subList(0, 50);
      }
      for (PaperInfo paperInfo : paperlist) {
        PaperItemMsg itemMsg = new PaperItemMsg();
        paperInfo.setParticipantNames(participantNames);
        if (!checkPaper(paperInfo, itemMsg)) {
          Map resultMap = PubInfoVerifyServiceImpl.buildResultMap(PubInfoVerifyConstant.NOT_SURE,
              paperInfo.getKeyCode());
          dataList.add(resultMap);
          resultMap.put(PubInfoVerifyConstant.ITEM_MSG, itemMsg);
          pubInfoVerifyService.savePubVerifyLog(resultMap,paperInfo,PubInfoVerifyUtil.paper_type);
          continue;
        }
        if (true) {
          // 包含 开始验证成果
          Map map = pubInfoVerifyService.doVerfiyPaper(paperInfo,participantNames);
          pubInfoVerifyService.savePubVerifyLog(map,paperInfo,PubInfoVerifyUtil.paper_type);
          dataList.add(map);
        }
      }
    }
    return successMap(OpenMsgCodeConsts.SCM_000, dataList);
  }



  public boolean checkPaper(PaperInfo paperInfo, PaperItemMsg itemMsg) {

    if (StringUtils.isBlank(paperInfo.getTitle())) {
      itemMsg.setTitle(OpenMsgCodeConsts.SCM_2063);
      return false;
    }
    if (paperInfo.getTitle().length() > PubInfoVerifyUtil.title_max_len) {
      itemMsg.setTitle(OpenMsgCodeConsts.SCM_2053);
      return false;
    }
    if (StringUtils.isBlank(paperInfo.getKeyCode())) {
      itemMsg.setKeyCode(OpenMsgCodeConsts.SCM_2055);
      return false;
    }
    if (paperInfo.getKeyCode().length() > PubInfoVerifyUtil.keycode_max_len) {
      itemMsg.setKeyCode(OpenMsgCodeConsts.SCM_2056);
      return false;
    }
    if (paperInfo.getAuthorNames().length() > PubInfoVerifyUtil.authors_max_len) {
      itemMsg.setAuthorNames(OpenMsgCodeConsts.SCM_2054);
      return false;
    }
    return true;
  }

}
